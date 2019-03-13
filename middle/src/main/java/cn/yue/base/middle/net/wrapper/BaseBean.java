package cn.yue.base.middle.net.wrapper;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;


public class BaseBean<T> {

    protected String msg;
    protected String message;
    @SerializedName("flag")
    protected String code;//错误码
    protected T data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRealMessage(){
        if(!TextUtils.isEmpty(msg)){
            return msg;
        }else if(!TextUtils.isEmpty(message)){
            return message;
        }
        return "";
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "BaseBean{" +
                "message='" + message + '\'' +
                ", code='" + code + '\'' +
                ", data=" + data +
                '}';
    }
}
