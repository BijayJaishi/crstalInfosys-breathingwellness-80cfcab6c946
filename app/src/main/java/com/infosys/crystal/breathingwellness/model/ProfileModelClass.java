package com.infosys.crystal.breathingwellness.model;

public class ProfileModelClass {
    public String image;
    public String user_name;
    public String about_us;
    public String phone;
    public String id;

    public ProfileModelClass(){
        /// empty constructor
    }

    public ProfileModelClass(String image, String user_name, String about_us, String phone) {
        this.image = image;
        this.user_name = user_name;
        this.about_us = about_us;
        this.phone = phone;

    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getAbout_us() {
        return about_us;
    }

    public void setAbout_us(String about_us) {
        this.about_us = about_us;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
