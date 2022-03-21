package com.endless.video.core.bean;

/**
 * @ClassName ApiResponse
 * @Description TODO
 * @Author haosiyuan
 * @Date 2022/3/19 22:56
 * @Version 1.0
 */
public class ApiResponse<T> {

    private int status;
    private String message;
    private T data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
