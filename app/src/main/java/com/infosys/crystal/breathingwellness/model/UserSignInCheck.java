package com.infosys.crystal.breathingwellness.model;

public class UserSignInCheck {
    public String user_email;
    public String signInStatus;


    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getSignInStatus() {
        return signInStatus;
    }

    public void setSignInStatus(String signInStatus) {
        this.signInStatus = signInStatus;
    }

    public UserSignInCheck(String user_email, String signInStatus) {
        this.user_email = user_email;
        this.signInStatus = signInStatus;
    }
}
