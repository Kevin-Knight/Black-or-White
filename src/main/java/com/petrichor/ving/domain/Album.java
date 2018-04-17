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
    private String aVisibility;
    @Column(name = "ATag")
    private String aTag;
    @Column(name = "ACover")
    private String aCover;
    @Column(name = "ADescription")
    private String aDescription;

    public Album(){

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Album)) return false;

        Album album = (Album) o;

        if (!aId.equals(album.aId)) return false;
        if (!uId.equals(album.uId)) return false;
        if (aName != null ? !aName.equals(album.aName) : album.aName != null) return false;
        if (aVisibility != null ? !aVisibility.equals(album.aVisibility) : album.aVisibility != null) return false;
        if (aTag != null ? !aTag.equals(album.aTag) : album.aTag != null) return false;
        if (aCover != null ? !aCover.equals(album.aCover) : album.aCover != null) return false;
        return aDescription != null ? aDescription.equals(album.aDescription) : album.aDescription == null;
    }

    @Override
    public int hashCode() {
        int result = aId.hashCode();
        result = 31 * result + uId.hashCode();
        result = 31 * result + (aName != null ? aName.hashCode() : 0);
        result = 31 * result + (aVisibility != null ? aVisibility.hashCode() : 0);
        result = 31 * result + (aTag != null ? aTag.hashCode() : 0);
        result = 31 * result + (aCover != null ? aCover.hashCode() : 0);
        result = 31 * result + (aDescription != null ? aDescription.hashCode() : 0);
        return result;
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

    public String getaVisibility() {
        return aVisibility;
    }

    public void setaVisibility(String aVisibility) {
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
