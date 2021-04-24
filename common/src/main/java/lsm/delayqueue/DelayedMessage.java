package lsm.delayqueue;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * @author lishenming
 * @version 1.0
 * @date 2020/6/30 16:51
 **/

@Data
@NoArgsConstructor
public class DelayedMessage implements Delayed {
    private Integer id;

    private String content;
    /**
     * 延迟时间
     */
    private long delay;
    /**
     * 执行时间
     */
    private long executeTime;

    public DelayedMessage(Integer id, String content, long delay) {
        this.id = id;
        this.content = content;
        this.delay = delay;
        this.executeTime = System.currentTimeMillis() + delay;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(this.executeTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed delayed) {
        return (int) (this.getDelay(TimeUnit.MILLISECONDS) -delayed.getDelay(TimeUnit.MILLISECONDS));

    }
}
