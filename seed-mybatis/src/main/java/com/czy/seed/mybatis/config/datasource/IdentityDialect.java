/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2016 abel533@gmail.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.czy.seed.mybatis.config.datasource;

import com.czy.seed.mybatis.sql.template.*;

/**
 * Created by panlc on 2017-03-14.
 */
public enum IdentityDialect {
    DB2("VALUES IDENTITY_VAL_LOCAL()") {
        public AbstractSqlTemplate getSqlTemplate() {
            return null;
        }
    },
    MYSQL("SELECT LAST_INSERT_ID()") {
        public AbstractSqlTemplate getSqlTemplate() {
            return new MySqlTemplate();
        }
    },
    SQLSERVER("SELECT SCOPE_IDENTITY()") {
        public AbstractSqlTemplate getSqlTemplate() {
            return new SqlServerTemplate();
        }
    },
    ORACLE("xxx") {
        public AbstractSqlTemplate getSqlTemplate() {
            return new OracleTemplate();
        }
    },
    CLOUDSCAPE("VALUES IDENTITY_VAL_LOCAL()") {
        public AbstractSqlTemplate getSqlTemplate() {
            return null;
        }
    },
    DERBY("VALUES IDENTITY_VAL_LOCAL()") {
        public AbstractSqlTemplate getSqlTemplate() {
            return null;
        }
    },
    HSQLDB("CALL IDENTITY()") {
        public AbstractSqlTemplate getSqlTemplate() {
            return null;
        }
    },
    SYBASE("SELECT @@IDENTITY") {
        public AbstractSqlTemplate getSqlTemplate() {
            return null;
        }
    },
    DB2_MF("SELECT IDENTITY_VAL_LOCAL() FROM SYSIBM.SYSDUMMY1") {
        public AbstractSqlTemplate getSqlTemplate() {
            return null;
        }
    },
    INFORMIX("select dbinfo('sqlca.sqlerrd1') from systables where tabid=1") {
        public AbstractSqlTemplate getSqlTemplate() {
            return null;
        }
    },
    SQLITE("SELECT LAST_INSERT_ROWID()") {
        public AbstractSqlTemplate getSqlTemplate() {
            return new SqliteTemplate();
        }
    };
    private String identityRetrievalStatement;

    private IdentityDialect(String identityRetrievalStatement) {
        this.identityRetrievalStatement = identityRetrievalStatement;
    }

    public static IdentityDialect getDatabaseDialect(String database) {
        IdentityDialect returnValue = null;
        if ("DB2".equalsIgnoreCase(database)) {
            returnValue = DB2;
        } else if ("MySQL".equalsIgnoreCase(database)) {
            returnValue = MYSQL;
        } else if ("SqlServer".equalsIgnoreCase(database)) {
            returnValue = SQLSERVER;
        } else if ("Cloudscape".equalsIgnoreCase(database)) {
            returnValue = CLOUDSCAPE;
        } else if ("Derby".equalsIgnoreCase(database)) {
            returnValue = DERBY;
        } else if ("HSQLDB".equalsIgnoreCase(database)) {
            returnValue = HSQLDB;
        } else if ("SYBASE".equalsIgnoreCase(database)) {
            returnValue = SYBASE;
        } else if ("DB2_MF".equalsIgnoreCase(database)) {
            returnValue = DB2_MF;
        } else if ("Informix".equalsIgnoreCase(database)) {
            returnValue = INFORMIX;
        }
        return returnValue;
    }

    public String getIdentityRetrievalStatement() {
        return identityRetrievalStatement;
    }

    public abstract AbstractSqlTemplate getSqlTemplate();
}
