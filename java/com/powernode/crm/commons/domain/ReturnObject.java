package com.powernode.crm.commons.domain;

/**
 * 专门用来返回json字符串
 */
public class ReturnObject {
    /**
     * 处理成功或者失败的标记，1为成功，0为失败
     */
    private String code;
    /**
     * 提示为什么失败的信息
     */

    private String message;
    /**
     * 返回的其他数据
     */
    private Object retDate;


    public ReturnObject() {
    }

    public ReturnObject(String code, String message, Object retDate) {
        this.code = code;
        this.message = message;
        this.retDate = retDate;
    }
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getRetDate() {
        return retDate;
    }

    public void setRetDate(Object retDate) {
        this.retDate = retDate;
    }



    @Override
    public String toString() {
        return "ReturnObject{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", retDate=" + retDate +
                '}';
    }
}
