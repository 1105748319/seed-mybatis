package com.czy.seed.mybatis.base.param;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by 003914[panlc] on 2017-06-09.
 */
public class Param implements Serializable {

    private static final long serialVersionUID = 1853557890332197293L;

    private Map<String, Object> like;
    private Map<String, Between> between;
    private Map<String, Object> in;
    private Map<String, Object> notIn;
    private Map<String, Object> equals;
    private Map<String, Object> lg;
    private Map<String, Object> le;
    private Map<String, Object> ge;
    private Map<String, Object> gg;

    static class Between {
        private String bengin;
        private String end;

        public String getBengin() {
            return bengin;
        }

        public void setBengin(String bengin) {
            this.bengin = bengin;
        }

        public String getEnd() {
            return end;
        }

        public void setEnd(String end) {
            this.end = end;
        }
    }
}
