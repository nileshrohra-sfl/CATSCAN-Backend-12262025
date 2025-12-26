# sflcore

## This project includes the following things:

1. Spring Security Configured.
2. MySQL Configured.
3. Liquibase Configured.
4. Auditing Configured.
5. HazelCast Local and Embedded Distributed Cache Configured.
6. Two different env Configured (Dev and Prod).
7. Spring actuator Configured.
8. Logging Configured (Through AOP).
9. Pre-made User and Authority Class with CRUD Rest end points ready.
10. Swagger Configured.
11. Sonar SunflowerLab Profile Passed.
12.Entity Auditing.
13. SSo Login with platforms like apple, google and facebook.
14. Default OTP feature. Instead of twilio OTP you can set and use default OTP by adding key "defaultOtp" in twilio configuration inside (twilio.util.features)

## Architecture followed:

    In this base project we have used service layer architecture
    with MVC architecture.

    A service layer architecture includes a service layer in the
    application where we need to write business logic and acts as
    a bridge between higer level layer (controller) and lower level
    layer (Repository).

## Project Related Terminologies:

    Controller: It is same as a Controller concept in the MVC application
                in which controller is a front face of the applicaiton
                that is where we expose our application endpoints and
                which is a place that handles the request and the response for the
                applicaiton.

    DTO: It represents the request or response objects that is basically exposed to the
         Http Client. It is simple POJO class which is used in the controller, the service
         and is not used in the Repository. It is used so that we can only expose those fields
         from the domain class which are actually required by the Http Client specific to
         applicaiton requirement.

    Domain: It is a simple POJO class that is used by the Repository and acts as an Entity (Model in MVC)
            in this applicaiton. It is used in Service, Repository and is not exposed to the Controller.
            It can also be used to represent an table in ORM like Hibernate.

    Mapper: It is used to convert DTO to Domain/Entity and Domain/Entity to DTO. This is generally used in
            Service where we convert DTO to Domain and Domain DTO while interacting with Controller and Respository.

    Repository: It is a interface that is used to deal with our data source of the application and acts
                as a bridge between our application and data source where we store applicaiton data.
                It provides methods for creation and manuplication of the data stored in data source. It is
                basically a data access layer for our application.

    Service: The layer of the applicaiton where we write our business logic and which
             acts as a bridge between the contoller and repository. Here basically we perform following
             actions and other actions can also be performed specific to application requirements:
                1. Conversion of DTO to Domain and Domain to DTO.
                2. Business logic of the applicaiton api.
                3. Acts as a bridge between Controller and Repository.
                4. Accepting the Request from the Controller.
                5. Return Response to the Controller.

              In services we use concept of interface and implementation class for each service to
              provide loose coupling in the applicaiton.

## Project Details

### Swagger URL:

[http://ec2-18-223-171-132.us-east-2.compute.amazonaws.com:9009/swagger-ui.html](http://ec2-18-223-171-132.us-east-2.compute.amazonaws.com:9009/swagger-ui.html)

To run on the local follow the given steps:

    1. git clone repo_link.
    2. Import as gradle project in Intellji.
    3. Copy application-dev.xml to application-local.xml
    3. Create a schema in db server with the name mentioned in
       the application-local.yml file in the datasource url.
    4. if linux user run ./gradlew clean and then ./gradlew -Plocal.
    5. if windows user run gradlew clean and then gradlew -Plocal.

Base project have two env one is dev for development purpose and production for production server.

To start clean your application simply run:

    ./gradlew clean --> Linux/Mac Users

      gradlew clean --> Windows Users

To start your application in dev env simply run:

    ./gradlew --> Linux/Mac Users

      gradlew --> Windows Users

To check common configurations check **application.yml**.
To check db and other configurations for dev profile check **application-dev.yml**.
To check db and other configurations for prod profile check **application-prod.yml**.

For db just create schema in your db server and run the application tables would be create
and loaded with dummy data.

## Building for production

### Packaging as jar

To build the final jar and optimize the sflcore application for production, run:

```


./gradlew -Pprod clean bootJar

```

To ensure everything worked, run:

```


java -jar build/libs/*.jar

```

Refer to [Using JHipster in production][] for more details.

### Packaging as war

To package your application as a war in order to deploy it to an application server, run:

```


./gradlew -Pprod -Pwar clean bootWar

```

## Notification Configuration
```
By default Notification will be enable in the base Project
```

### Support notification for multidevice
````
1. Set multipleDevice: true (in application.yml), support for multidevice notification
2. Set multipleDevice: false (in application.yml), support for single-device notification
````

### How to disable Notification in Project
```
1. Set notification=false in gradle.properties file
2. Remove Notification Utility specific properties from the
   application property files
3. Delete private key [json file] 
   Path : resource/firebase/baseproject-8dc44-firebase-adminsdk-gdcnn-ee1711d78e.json
4. Removed the Notification related changesets file
    -- 202007151130_added_notification_entity.xml
    -- 202007271230_added_recipient_token_entity.xml
    -- 202008051637_added_recipient_topic_entity.xml
```

### How to enable Notification in Project
```
1. Set notification=true in gradle.properties file
2. Generate the private key [json file] for your project and replace
   it with existing private key [json file] added in base project.
   Path : resource/firebase/baseproject-8dc44-firebase-adminsdk-gdcnn-ee1711d78e.json
3. In your application property file replace the firebase-configuration-file
   name with your private key [json file].
4. Add the Notification related changesets file
    -- 202007151130_added_notification_entity.xml
    -- 202007271230_added_recipient_token_entity.xml
    -- 202008051637_added_recipient_topic_entity.xml
```


## Testing

To launch your application's tests, run:

```
./gradlew test integrationTest jacocoTestReport
```

For more information, refer to the [Running tests page][].

### Code quality

Sonar is used to analyse code quality. You can start a local Sonar server (accessible on http://localhost:9001) with:

```
docker-compose -f src/main/docker/sonar.yml up -d
```

You can run a Sonar analysis with using the [sonar-scanner](https://docs.sonarqube.org/display/SCAN/Analyzing+with+SonarQube+Scanner) or by using the gradle plugin.

Then, run a Sonar analysis:

```
./gradlew -Pprod clean check jacocoTestReport sonarqube
```

## Using Docker to simplify development (optional)

You can also fully dockerize your application and all the services that it depends on.

To achieve this follow the below guidelines:

### Packaging as War and Building Docker Image

Build a docker image of your app by running :

    ./gradlew clean
    ./gradlew  bootWar -Pprod  jibDockerBuild

### Starting the container

To start your application simply run:

    docker-compose -f src/main/docker/app.yml up -d

You can test the application by running it on https://localhost:<API_PORT> wherein the API_PORT is mentioned in the .env file in your project root. Your application would be up and running.

### Stop and Remove the container

After you have finished testing, you can stop containers as well as removes them by running the following command:

    docker-compose -f src/main/docker/app.yml down

### List Running and Stop Container and Container Status :

If you want to list out the all running containers, simply run :

    docker-compose -f src/main/docker/app.yml ps

If you want to list out all containers(running as well as stopped), you can simply run:

    docker-compose -f src/main/docker/app.yml ps -a


### For Environment Variable

If you want to add or remove or edit any environment variable, you can do that in .env file in the project root.
For Example: If you want to change environment variable like API_PORT, DB_PORT, DB_USER, DB_NAME etc just go to the .env file and change that.

When you only make changes in environment variables related to **mysql** then use this command:

    docker-compose -f src/main/docker/app.yml up -d

When you only make changes in environment variables related to **application** then use this command:

    docker-compose -f src/main/docker/app.yml restart sflcore-app

### Changes in Code :

Whenever you make any changes in code run this:

    docker-compose -f src/main/docker/app.yml rm -s -f sflcore-app

After that run this command :

    docker-compose -f src/main/docker/app.yml up -d  sflcore-app

### Stop and Start container

If you want to stop both the containers but do not want to remove them, then use following command:

    docker-compose -f src/main/docker/app.yml stop

If you want to start both the containers:

    docker-compose -f src/main/docker/app.yml start

If you want to stop the **mysql** container(will stop the application container also) but do not want to remove it, then use this command:

    docker-compose -f src/main/docker/app.yml stop sflcore-mysql

If you want to start the **mysql** container, then use this :

```
docker-compose -f src/main/docker/app.yml up -d
```

If you want to stop the **application** container but do not want to remove it, then use this:

    docker-compose -f src/main/docker/app.yml stop sflcore-app

If you want to start the **application** container, then use this:

    docker-compose -f src/main/docker/app.yml start sflcore-app


### Container Logs:

If you want to see both container logs, you can see by using this command:

    docker-compose -f src/main/docker/app.yml logs -f

If you want to see specific **application** container logs, then run this command:

    docker-compose -f src/main/docker/app.yml logs -f sflcore-app

If you want to see specific **mysql** container logs, then run this command:

    docker-compose -f src/main/docker/app.yml logs -f sflcore-mysql

# Creating new project from base project HOW-TO link:

https://thesunflowerlab.sharepoint.com/:w:/s/TeamSFL/ERCEIf972DJNst5onMphk2gBP0k6j4J7oGyQTw5h_dt3gQ?e=dus0vV
