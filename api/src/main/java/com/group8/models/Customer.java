package com.group8.models;

import java.io.Serializable;

public class Customer implements Serializable {
    private String customerNumber;
    private double pointsAvailable;
    private String profileToken;

    public Customer() {
    }

    public Customer(String customerNumber,double pointsAvailable, String profileToken) {
        this.customerNumber = customerNumber;
        this.pointsAvailable = pointsAvailable;
        this.profileToken = profileToken;
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    public double getPointsAvailable() {
        return pointsAvailable;
    }

    public void setPointsAvailable(double pointsAvailable) {
        this.pointsAvailable = pointsAvailable;
    }

    public String getProfileToken() {
        return profileToken;
    }

    public void setProfileToken(String profileToken) {
        this.profileToken = profileToken;
    }

}
