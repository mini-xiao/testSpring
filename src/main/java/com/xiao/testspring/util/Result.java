package com.xiao.testspring.util;

import java.io.Serializable;

/**
 * 功能描述:
 *
 * @Params:
 * @Author: LiuMZ
 * @Date: 2020/10/19 11:22
 */
public class Result<T> implements Serializable {

    private final static String SUCCESS="success";
    private final static String ERROR_MSG="fail";

    /**
     * 状态码
     */
    private int code;

    /**
     *说明信息
     */
    private String message;

    /**
     * 返回结果 快速判断 success fail
     */
    private String result;

    /**
     * 返回数据
     */
    private T data;

    public Result() {
        this(200,"成功",SUCCESS,null);
    }

    public Result(int code, String message) {
        this(code,message,ERROR_MSG,null);
    }

    public Result(int code, String message, T data) {
        this(code,message,ERROR_MSG,data);
    }

    public Result(T data) {
        this(200,"成功",SUCCESS,data);
    }

    public Result(int code, String message, String result, T data) {
        this.code = code;
        this.message = message;
        this.result = result;
        this.data = data;
    }

    public final static Result success(){
        return new Result();
    }

    public final static <T> Result<T> success(T data){
        return new Result(data);
    }

    public final static <T> Result<T> success(int code,T data,String message){
        return new Result(code,message,data);
    }

    public final static Result error(String message){
        return new Result(400,message);
    }

    public final static Result error(int code,String message){
        return new Result(code,message);
    }
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
