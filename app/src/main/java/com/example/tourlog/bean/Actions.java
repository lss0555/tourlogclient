package com.example.tourlog.bean;

import java.io.Serializable;

public class Actions implements Serializable {
    private int id;
    private int userid;
    private int xc_id;
    private int type;//1图片 2vedio
    private String vediopath;
    private String address;
    private String content;
    private String creatTime;
    private String imglist;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getVediopath() {
        return vediopath;
    }

    public void setVediopath(String vediopath) {
        this.vediopath = vediopath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(String creatTime) {
        this.creatTime = creatTime;
    }

    public int getXc_id() {
        return xc_id;
    }

    public void setXc_id(int xc_id) {
        this.xc_id = xc_id;
    }

    public String getImglist() {
        return imglist;
    }

    public void setImglist(String imglist) {
        this.imglist = imglist;
    }
}
