package com.petrichor.ving.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Album {
    @Id
    @Column(name="AID")
    private String aId;
    @Column(name = "UID")
    private String uId;
    @Column(name = "AName")
    private String aName;
    @Column(name = "AVisibility")
    private int aVisibility;
    @Column(name = "ATag")
    private String aTag;
    @Column(name = "ACover")
    private String aCover;
    @Column(name = "ADescription")
    private String aDescription;

    public Album(){

    }

    public String getaId() {
        return aId;
    }

    public void setaId(String aId) {
        this.aId = aId;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getaName() {
        return aName;
    }

    public void setaName(String aName) {
        this.aName = aName;
    }

    public int getaVisibility() {
        return aVisibility;
    }

    public void setaVisibility(int aVisibility) {
        this.aVisibility = aVisibility;
    }

    public String getaTag() {
        return aTag;
    }

    public void setaTag(String aTag) {
        this.aTag = aTag;
    }

    public String getaCover() {
        return aCover;
    }

    public void setaCover(String aCover) {
        this.aCover = aCover;
    }

    public String getaDescription() {
        return aDescription;
    }

    public void setaDescription(String aDescription) {
        this.aDescription = aDescription;
    }

}
