#!/bin/bash

# Define image and container names
IMAGE_NAME="catscan-api"
CONTAINER_NAME="$IMAGE_NAME-dev"
LOG_GROUP_NAME="$IMAGE_NAME"
LOG_STREAM_NAME="$CONTAINER_NAME"
HEALTHCHECK_URL="http://localhost:8080/management/health" # Update with the correct port and endpoint

# Stop and remove the existing container if it exists
docker stop $CONTAINER_NAME || true
docker rm $CONTAINER_NAME || true

# Build the Docker image
echo "Building Docker image..."
DOCKER_TAG=$(git rev-parse --short HEAD)
docker build -t $IMAGE_NAME .

# Check if the image build was successful
if [ $? -eq 0 ]; then
    echo "Image built successfully."
else
    echo "Error in building image."
    exit 1
fi

# Run the Docker container
echo "Running Docker container..."
docker run -d -p 8080:8080 --name $CONTAINER_NAME --log-driver=awslogs --log-opt awslogs-region=us-east-2 --log-opt awslogs-create-group=true --log-opt awslogs-group=$CONTAINER_NAME $IMAGE_NAME

# Check if the container started successfully
if [ $? -eq 0 ]; then
    echo "Container started successfully."
else
    echo "Error in starting container."
    exit 1
fi

# Health check loop
echo "Performing health check..."
MAX_ATTEMPTS=3
SLEEP_SECONDS=5
for ((i=1;i<=MAX_ATTEMPTS;i++)); do
    # Using curl to perform the health check
    HTTP_RESPONSE=$(curl --silent --write-out "HTTPSTATUS:%{http_code}" -X GET $HEALTHCHECK_URL)
    # Extract the status code
    HTTP_STATUS=$(echo $HTTP_RESPONSE | tr -d '\n' | sed -e 's/.*HTTPSTATUS://')

    if [ "$HTTP_STATUS" -eq 200 ]; then
        echo "API health check passed."
        break
    else
        echo "API health check failed, attempt $i of $MAX_ATTEMPTS..."
        if [ $i -eq $MAX_ATTEMPTS ]; then
            echo "Max health check attempts reached. Stopping and removing container."
            docker stop $CONTAINER_NAME
            docker rm $CONTAINER_NAME
            # Clear dangling images
            docker image prune -f
            exit 1
        fi
    fi
    sleep $SLEEP_SECONDS
done
