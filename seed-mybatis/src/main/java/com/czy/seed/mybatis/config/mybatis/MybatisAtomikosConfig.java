package com.czy.seed.mybatis.config.mybatis;

import com.czy.seed.mybatis.config.datasource.DataSourceBuilder;
import com.czy.seed.mybatis.tool.SpringContextHelper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by PLC on 2017/5/1.
 */
public class MybatisAtomikosConfig extends MybatisConfig {

    @Autowired
    public MybatisAtomikosConfig(SpringContextHelper springContextHelper, DataSourceBuilder dataSourceBuilder) {
        super(springContextHelper, dataSourceBuilder);
    }

    @Override
    public void registerDynamicTransactionManager() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("transactionTimeout", 300);
        try {
            Class atomikosUserTransactionImp = Class.forName("com.atomikos.icatch.jta.UserTransactionImp");
            springContextHelper.addBean(atomikosUserTransactionImp, "atomikosUserTransaction", map, null, null);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("e");
        }
        map.clear();
        map.put("userTransaction", SpringContextHelper.getBeanById("atomikosUserTransaction"));
        springContextHelper.addBean(org.springframework.transaction.jta.JtaTransactionManager.class, "transactionManager", map, null, null);
    }
}
