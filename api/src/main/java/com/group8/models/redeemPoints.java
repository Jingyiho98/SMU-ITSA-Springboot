package com.group8.models;

import java.io.Serializable;

public class redeemPoints implements Serializable {
    private String profileToken;
    private Integer amount;
    private String description;
    private String memberNo;

    public redeemPoints(){

    }

    public redeemPoints(String profileToken, Integer amount, String description, String memberNo) {
        this.profileToken = profileToken;
        this.amount = amount;
        this.description = description;
        this.memberNo = memberNo;
    }

    public String getProfileToken() {
        return profileToken;
    }

    public void setProfileToken(String profileToken) {
        this.profileToken = profileToken;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMemberNo() {
        return memberNo;
    }

    public void setMemberNo(String memberNo) {
        this.memberNo = memberNo;
    }
}
