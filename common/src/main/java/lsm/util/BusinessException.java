package lsm.util;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @author lishenming
 * @create 2018-08-21
 **/

@Data
@Builder
@AllArgsConstructor
public class BusinessException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = -1753375989738809558L;

    private int code;

    private String description;

    private Exception e;

    public BusinessException(ErrorCode resultCodeEnum, Exception e) {
        this.code = resultCodeEnum.getCode();
        this.description = resultCodeEnum.getMessage();
        this.e = e;
    }

    public BusinessException(ErrorCode resultCodeEnum) {
        this.code = resultCodeEnum.getCode();
        this.description = resultCodeEnum.getMessage();
    }

    public synchronized static BusinessException fail(String msg){
        return BusinessException.builder()
                .code(ErrorCode.FAIL.getCode())
                .description(msg)
                .build();
    }

    public synchronized static BusinessException fail(String msg, Exception e){
        return BusinessException.builder()
                .code(ErrorCode.FAIL.getCode())
                .description(msg)
                .e(e)
                .build();
    }

}
