package com.petrichor.ving.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
public class Production {
    @Id
    @Column(name = "PID")
    private String pId;
    @Column(name = "UID")
    private String uId;
    @Column(name = "PName")
    private String pName;
    @Column(name = "PVisibility")
    private String pVisibility;
    @Column(name = "PTag")
    private String pTag;
    @Column(name = "PCover")
    private String pCover;
    @Column(name = "PDescription")
    private String pDescription;

    public Production(){
        this.pId="P-"+ UUID.randomUUID().toString().replace("-", "").substring(0, 10);;
        uId="1";
        pName="a";
        pVisibility="0.5";
        pTag="测试";
        pCover="q://";
        pDescription="测试";
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public String getpVisibility() {
        return pVisibility;
    }

    public void setpVisibility(String pVisibility) {
        this.pVisibility = pVisibility;
    }

    public String getpTag() {
        return pTag;
    }

    public void setpTag(String pTag) {
        this.pTag = pTag;
    }

    public String getpCover() {
        return pCover;
    }

    public void setpCover(String pCover) {
        this.pCover = pCover;
    }

    public String getpDescription() {
        return pDescription;
    }

    public void setpDescription(String pDescription) {
        this.pDescription = pDescription;
    }
}
