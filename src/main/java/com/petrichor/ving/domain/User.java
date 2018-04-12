package com.petrichor.ving.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class User {
    @Id
    @Column(name = "UID")
    private String uId;
    @Column(name = "UPassword")
    private String uPassword;
    @Column(name = "UName")
    private String uName;
    @Column(name = "UGender")
    private String uGender;
    @Column(name = "UAge")
    private String uAge;
    @Column(name = "UPortrait")
    private String uPortrait;
    @Column(name = "UDescription")
    private String uDescription;

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getuPassword() {
        return uPassword;
    }

    public void setuPassword(String uPassword) {
        this.uPassword = uPassword;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public String getuGender() {
        return uGender;
    }

    public void setuGender(String uGender) {
        this.uGender = uGender;
    }

    public String getuAge() {
        return uAge;
    }

    public void setuAge(String uAge) {
        this.uAge = uAge;
    }

    public String getuPortrait() {
        return uPortrait;
    }

    public void setuPortrait(String uPortrait) {
        this.uPortrait = uPortrait;
    }

    public String getuDescription() {
        return uDescription;
    }

    public void setuDescription(String uDescription) {
        this.uDescription = uDescription;
    }
}
