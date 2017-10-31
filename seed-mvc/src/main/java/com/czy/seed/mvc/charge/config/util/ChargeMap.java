package com.czy.seed.mvc.charge.config.util;

import org.apache.commons.collections.map.HashedMap;

import java.util.Map;

/**
 * Created by 004410 on 2017/7/5.
 * 收银项目集合
 */
public class ChargeMap {

    private static String baggage = PropertiesUtil.getStringProperty("baggage");//行李收费

    private static String seat = PropertiesUtil.getStringProperty("seat");//行李收费

    private static String baggage_others = PropertiesUtil.getStringProperty("baggage_others");//特殊行李

    private static String other = PropertiesUtil.getStringProperty("other");//其他收费

    public static Map<String, Object> getCharge(){
        Map<String, Object> map = new HashedMap();
        map.put("baggage",baggage);
        map.put("seat",seat);
        map.put("baggage_others",baggage_others);
        map.put("other",other);

        return map;
    }
}
