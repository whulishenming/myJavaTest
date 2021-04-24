package lsm.util;

import java.time.Clock;
import java.util.function.Consumer;

import com.alibaba.fastjson.JSONObject;

import lombok.extern.slf4j.Slf4j;

/**
 * @author lishenming
 * @version 1.0
 * @date 2019/9/20 10:39
 **/

@Slf4j
public class ControllerHelper {

    public static <K> CommonResponse<K> call(Consumer<CommonResponse<K>> callback) {
        return process(callback, new CommonResponse<>());
    }

    public static <T extends CommonResponse> T call(Consumer<T> callback, Class<T> clazz) {
        T response;
        try {
            response = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return process(callback, response);
    }

    private static <T extends CommonResponse> T process(Consumer<T> callback, T response) {
        Clock clock = Clock.systemUTC();
        long start = clock.millis();
        if (callback != null) {

            try {
                callback.andThen(res -> response.setErrorCode(ErrorCode.SUCCESS)).accept(response);

            } catch (RuntimeException re) {
                convertError(response, ErrorCode.FAIL, re);
            }
        }
        response.setProcessTime(clock.millis() - start);
        log.info("[[title={}]] callback={} response={}  ", "", JSONObject.toJSONString(callback), response);
        return response;
    }

    private static void convertError(CommonResponse response, ErrorCode errorCode, Exception thr) {
        response.setCode(errorCode.getCode());
        response.setMessage(errorCode.getMessage());
        if (thr instanceof BusinessException) {
            response.setMessage(thr.getMessage());
            response.setCode(ErrorCode.ofDesc(thr.getMessage()).getCode());
        }
        log.error("[[title={}]] convertError err response={}  ", "", response, thr);
    }

}
