package com.czy.seed.mybatis.base;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Mybatis调用存储过程
 * Created by panlc on 2017-04-13.
 */
public class Procedure {

    public Procedure(Class<?> baseMapper) {
        this.baseMapper = baseMapper;
    }

    private Class<?> baseMapper;

    private String procedureName;           //存储过程名称

    private Map<String, Object> inParams = new HashMap<String, Object>();   //存储过程输入参数

    private List<String> outParams;         //存储过程输出参数

    private Class resultType;               //存储过程调用返回类型

    private Object result;                  //存储过程调用返回结果

    public Class<?> getBaseMapper() {
        return baseMapper;
    }

    public String getProcedureName() {
        return procedureName;
    }

    public void setProcedureName(String procedureName) {
        this.procedureName = procedureName;
    }

    public Map<String, Object> getInParams() {
        return inParams;
    }

    public void addInParams(String key, Object value) {
        inParams.put(key, value);
    }

    public Class getResultType() {
        return resultType;
    }

    public void setResultType(Class resultType) {
        this.resultType = resultType;
    }

    public List<String> getOutParams() {
        return outParams;
    }

    public void setOutParams(List<String> outParams) {
        this.outParams = outParams;
    }

    public String getMapperName() {
        return getBaseMapper().getName();
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
