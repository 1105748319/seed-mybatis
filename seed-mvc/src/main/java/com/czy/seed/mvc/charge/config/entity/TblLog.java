package com.czy.seed.mvc.charge.config.entity;

import com.czy.seed.mvc.base.entity.BaseEntity;

import java.util.Date;

public class TblLog extends BaseEntity {

    private String name;

    private String fs;

    private String fi;

    private String fid;

    private String ip;

    private Date date;

    private String passenger;

    private String message;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getFs() {
        return fs;
    }

    public void setFs(String fs) {
        this.fs = fs == null ? null : fs.trim();
    }

    public String getFi() {
        return fi;
    }

    public void setFi(String fi) {
        this.fi = fi == null ? null : fi.trim();
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid == null ? null : fid.trim();
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip == null ? null : ip.trim();
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getPassenger() {
        return passenger;
    }

    public void setPassenger(String passenger) {
        this.passenger = passenger == null ? null : passenger.trim();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message == null ? null : message.trim();
    }
}