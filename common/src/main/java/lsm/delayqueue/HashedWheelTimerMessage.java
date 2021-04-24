package lsm.delayqueue;

import com.alibaba.fastjson.JSONObject;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lishenming
 * @version 1.0
 * @date 2020/6/30 18:21
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HashedWheelTimerMessage implements TimerTask {
    private Integer id;

    private String content;

    @Override
    public void run(Timeout timeout) {
        System.out.println(String.format("currentTimeMillis=%s, message=%s", System.currentTimeMillis(),
            String.format("{id=%s, content=%s}", id, content)));
    }
}
