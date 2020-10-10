package com.tiankong44.util;

/**
 * 描述：controller下主动抛出的异常描述
 * Created on 2017/8/24.
 * <p>Title:</p>
 * <p>Copyright:Copyright (c) 2017</p>
 * <p>Company:北京思特奇信息技术股份有限公司</p>
 * <p>Department:NBC</p>
 */
public class RequestException extends Exception
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int code;//出异常时的代码
    @SuppressWarnings("unused")
	private RequestException(){}

    /**
     *
     * @param code
     * @param msg 出异常时的描述信息
     */
    public RequestException(int code,String msg)
    {
        super(msg);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
