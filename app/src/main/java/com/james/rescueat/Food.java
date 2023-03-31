package com.james.rescueat;

public class Food {

    String imageName, imageURL, imageCaption;

    public Food() {}

    public Food(String imageName,String imageCaption, String imageURL) {
        this.imageName = imageName;
        this.imageURL = imageURL;
        this.imageCaption = imageCaption;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageCaption() {
        return imageCaption;
    }

    public void setImageCaption(String imageCaption) {
        this.imageCaption = imageCaption;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
