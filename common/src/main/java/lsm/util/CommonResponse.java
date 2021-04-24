package lsm.util;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.alibaba.fastjson.JSONObject;

import lombok.Data;

/**
 * @author lishenming
 * @version 1.0
 * @date 2019/9/20 10:39
 **/

@Data
public class CommonResponse<T> implements Serializable {

    private static final long serialVersionUID = -2627993886108918418L;

    private T data;

    private Integer code = 1;

    private String message = "成功";

    private String timeStamp = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS").format(LocalDateTime.now());

    private long processTime = 0L;

    public void setErrorCode(ErrorCode errorCode) {
        setCode(errorCode.getCode());
        setMessage(errorCode.getMessage());
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

    public static <T> CommonResponse ok(T data) {
        CommonResponse<T> result = new CommonResponse<>();

        result.setCode(1);
        result.setData(data);
        result.setMessage("成功");

        return result;
    }
}
