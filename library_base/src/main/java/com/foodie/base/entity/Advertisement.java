package com.foodie.base.entity;

public class Advertisement {

    /**
     * id : 1
     * city : 长春市
     * photoUrl : https://lanke-foodie.oss-cn-beijing.aliyuncs.com/advertisement/15631113647581845
     * typeId : 1
     * redirectId : 3
     * createTime : 2019-07-07 15:00:26.0
     */

    private int id;
    private String city;
    private String photoUrl;
    private int typeId;
    private int redirectId;
    private String createTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public int getRedirectId() {
        return redirectId;
    }

    public void setRedirectId(int redirectId) {
        this.redirectId = redirectId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
