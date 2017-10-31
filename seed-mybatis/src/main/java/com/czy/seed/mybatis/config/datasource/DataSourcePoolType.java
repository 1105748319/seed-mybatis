package com.czy.seed.mybatis.config.datasource;

/**
 * Created by panlc on 2017-03-14.
 */
public enum DataSourcePoolType {

    druid {
        public String getPoolClass() {
            return "com.alibaba.druid.pool.DruidDataSource";
        }

        public String getInitMethod() {
            return null;
        }

        public String getDestroyMethod() {
            return null;
        }
    },

    dbcp {
        public String getPoolClass() {
            return null;
        }

        public String getInitMethod() {
            return null;
        }

        public String getDestroyMethod() {
            return null;
        }
    },

    atomikos_noxa {
        public String getPoolClass() {
            return "com.atomikos.jdbc.nonxa.AtomikosNonXADataSourceBean";
        }

        public String getInitMethod() {
            return "init";
        }

        public String getDestroyMethod() {
            return "close";
        }
    },

    atomikos_xa {
        public String getPoolClass() {
            return "com.atomikos.jdbc.AtomikosDataSourceBean";
        }

        public String getInitMethod() {
            return "init";
        }

        public String getDestroyMethod() {
            return "close";
        }
    };

    public abstract String getPoolClass();

    public abstract String getInitMethod();

    public abstract String getDestroyMethod();
}
