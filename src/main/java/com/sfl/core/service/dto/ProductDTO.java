package com.sfl.core.service.dto;

/**
 * A ProductDTO
 */
public class ProductDTO extends KeywordDTO {

    private String name;

    private String description;

    private String image;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "ProductDTO{" +
            "name='" + name + '\'' +
            ", description='" + description + '\'' +
            ", image='" + image + '\'' +
            '}';
    }
}
