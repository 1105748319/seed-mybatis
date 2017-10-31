package com.czy.seed.mvc.charge.config.entity;

import javax.persistence.Id;
import java.util.Date;
import java.math.BigDecimal;

public class TblProductRules {


    private String rulesName;

    private BigDecimal rulesVal;

    private String fs;

    private Integer productId;

    private String productNum;

    private Integer status;
    @Id
    private Long id;
    private Date createdDt;
    private String createdBy;
    private Date updatedDt;
    private String updatedBy;


    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
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



    public boolean autoConfig() {
        return true;
    }

    public String getRulesName() {
        return rulesName;
    }

    public void setRulesName(String rulesName) {
        this.rulesName = rulesName == null ? null : rulesName.trim();
    }

    public BigDecimal getRulesVal() {
        return rulesVal;
    }

    public void setRulesVal(BigDecimal rulesVal) {
        this.rulesVal = rulesVal ;
    }

    public String getFs() {
        return fs;
    }

    public void setFs(String fs) {
        this.fs = fs == null ? null : fs.trim();
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductNum() {
        return productNum;
    }

    public void setProductNum(String productNum) {
        this.productNum = productNum;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}