package com.czy.seed.mvc.charge.config.entity;



import com.czy.seed.mybatis.config.mybatis.annotations.One2Many;
import java.math.BigDecimal;

import javax.persistence.Id;
import java.util.Date;
import java.util.List;

public class TblProduct {


    private String productNum;

    private String productName;

    private String productType;

    private BigDecimal productUnitPrice;

    private String unitType;

    private Integer status;
    @Id
    private Long id;
    private Date createdDt;
    private String createdBy;
    private Date updatedDt;
    private String updatedBy;

    @One2Many(columns = "id=productId")
    private List<TblProductRules> tblProductRulesList;

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


    public String getProductNum() {
        return productNum;
    }

    public void setProductNum(String productNum) {
        this.productNum = productNum == null ? null : productNum.trim();
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName == null ? null : productName.trim();
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType == null ? null : productType.trim();
    }

    public BigDecimal getProductUnitPrice() {
        return productUnitPrice;
    }

    public void setProductUnitPrice(BigDecimal productUnitPrice) {
        this.productUnitPrice = productUnitPrice;
    }

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType == null ? null : unitType.trim();
    }

    public List<TblProductRules> getTblProductRulesList() {
        return tblProductRulesList;
    }

    public void setTblProductRulesList(List<TblProductRules> tblProductRulesList) {
        this.tblProductRulesList = tblProductRulesList;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}