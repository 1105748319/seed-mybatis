package com.czy.seed.mvc.charge.config.entity;


import com.czy.seed.mvc.base.entity.BaseEntity;
import com.czy.seed.mybatis.config.mybatis.annotations.One2Many;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class TblOrder extends BaseEntity {


    private String name;

    private BigDecimal overweightLuggage;

    private BigDecimal seat;

    private BigDecimal specialLuggage;

    private BigDecimal other;

    private BigDecimal receivableAmount;

    private String type;

    private String fi;

    private String licenseNo;

    private BigDecimal actualAmount;

    private String paymentType;

    private String orderNum;

    private String amountType;

    private String ip;

    private String postback;

    private Date fdy;

    private String airport;

    private String da;

    private String fid;

    private String fs;

    private String rl;

    private String sn;

    private String netBaggage;

    private Integer freeLuggageNum;

    private Date createdDt;

    private String createdBy;

    private Date updatedDt;

    private String updatedBy;




    @One2Many(columns = "id=orderId")
    private List<TblOrderDetail> tblOrderDetailList;

    public List<TblOrderDetail> getTblOrderDetailList() {
        return tblOrderDetailList;
    }

    public void setTblOrderDetailList(List<TblOrderDetail> tblOrderDetailList) {
        this.tblOrderDetailList = tblOrderDetailList;
    }

    public String getName() {
        return name;
    }

    public String getAirport() {
        return airport;
    }

    public void setAirport(String airport) {
        this.airport = airport;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public BigDecimal getOverweightLuggage() {
        return overweightLuggage;
    }

    public void setOverweightLuggage(BigDecimal overweightLuggage) {
        this.overweightLuggage = overweightLuggage;
    }

    public BigDecimal getSeat() {
        return seat;
    }

    public void setSeat(BigDecimal seat) {
        this.seat = seat;
    }

    public BigDecimal getSpecialLuggage() {
        return specialLuggage;
    }

    public void setSpecialLuggage(BigDecimal specialLuggage) {
        this.specialLuggage = specialLuggage;
    }

    public BigDecimal getOther() {
        return other;
    }

    public void setOther(BigDecimal other) {
        this.other = other;
    }

    public BigDecimal getReceivableAmount() {
        return receivableAmount;
    }

    public void setReceivableAmount(BigDecimal receivableAmount) {
        this.receivableAmount = receivableAmount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getFi() {
        return fi;
    }

    public void setFi(String fi) {
        this.fi = fi == null ? null : fi.trim();
    }

    public String getLicenseNo() {
        return licenseNo;
    }

    public void setLicenseNo(String licenseNo) {
        this.licenseNo = licenseNo == null ? null : licenseNo.trim();
    }

    public BigDecimal getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(BigDecimal actualAmount) {
        this.actualAmount = actualAmount;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType == null ? null : paymentType.trim();
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum == null ? null : orderNum.trim();
    }

    public String getAmountType() {
        return amountType;
    }

    public void setAmountType(String amountType) {
        this.amountType = amountType == null ? null : amountType.trim();
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip == null ? null : ip.trim();
    }

    public String getPostback() {
        return postback;
    }

    public void setPostback(String postback) {
        this.postback = postback == null ? null : postback.trim();
    }

    public Date getFdy() {
        return fdy;
    }

    public void setFdy(Date fdy) {
        this.fdy = fdy;
    }

    public String getDa() {
        return da;
    }

    public void setDa(String da) {
        this.da = da;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getFs() {
        return fs;
    }

    public void setFs(String fs) {
        this.fs = fs;
    }

    public String getNetBaggage() {
        return netBaggage;
    }

    public void setNetBaggage(String netBaggage) {
        this.netBaggage = netBaggage;
    }

    public String getRl() {
        return rl;
    }

    public void setRl(String rl) {
        this.rl = rl;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public Integer getFreeLuggageNum() {
        return freeLuggageNum;
    }

    public void setFreeLuggageNum(Integer freeLuggageNum) {
        this.freeLuggageNum = freeLuggageNum;
    }

    public Date getCreatedDt() {
        return this.createdDt;
    }

    public void setCreatedDt(Date createdDt) {
        this.createdDt = createdDt;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getUpdatedDt() {
        return this.updatedDt;
    }

    public void setUpdatedDt(Date updatedDt) {
        this.updatedDt = updatedDt;
    }

    public String getUpdatedBy() {
        return this.updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }
}