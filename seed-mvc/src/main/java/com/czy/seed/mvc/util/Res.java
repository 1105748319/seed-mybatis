package com.czy.seed.mvc.util;

import java.util.HashMap;

/**
 * Controller 层操作结果封装对象
 * Created by PLC on 2017/5/31
 */
public class Res extends HashMap<String, Object> {

    private static final long serialVersionUID = 915079455546890857L;

	private static final String CODE = "code";      //操作结果编码
	private static final String MSG = "msg";        //操作结果描述
	private static final String DATA = "data";      //返回数据

    /**
     * 操作成功默认编码
     */
	public static final int CODE_OK = 200;
    /**
     * 操作失败默认编码
     */
	public static final int CODE_ERROR = 500;
    /**
     * 操作成功默认描述
     */
	public static final String MSG_OK = "操作成功";
    /**
     * 操作失败默认描述
     */
	public static final String MSG_ERROR = "未知异常，请联系管理员";

    /**
     * 默认操作结果数据
     */
	public static final String DATA_DEFAULT = "";

	/**
	 * 操作错误信息
	 * @return 返回status=500,操作信息="未知异常，请联系管理员"，返回数据=""的结果
	 */
	public static Res error() {
		return custom(CODE_ERROR, MSG_ERROR, DATA_DEFAULT);
	}

    /**
     * 操作错误信息
     * @return 返回status=500,操作信息=msg，返回数据=""的结果
     */
    public static Res error(String msg) {
        return custom(CODE_ERROR, msg, DATA_DEFAULT);
    }

    /**
     * 操作成功信息
     * @return 返回status=200,操作信息="操作成功"，返回数据=""的结果
     */
    public static Res ok() {
        return custom(CODE_OK, MSG_OK, DATA_DEFAULT);
    }

	/**
	 * 操作成功信息
	 * @return 返回status=200,操作信息="操作成功"，返回数据=data的结果
	 */
	public static Res ok(Object data) {
		return custom(CODE_OK, MSG_OK, data);
	}

	/**
	 * 自定义操作信息
	 * @param status 操作结果编码
	 * @param msg 操作结果描述
	 * @param data 操作结果数据
	 * @return Res
	 */
	public static Res custom (int status, String msg, Object data) {
		Res res = new Res();
		res.put(CODE, status).put(MSG, msg).put(DATA, data);
		return res;
	}

    /**
     * 向操作结果设置额外属性
     * @param key
     * @param value
     * @return Res
     */
	public Res put(String key, Object value) {
		super.put(key, value);
		return this;
	}

}
