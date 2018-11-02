package lsm.util;

import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

/**
 * @author lishenming
 * @create 2017-12-14
 **/

@Slf4j
public class LogUtils {

    private static ThreadLocal<String> logId = new ThreadLocal<>();
    
    public static void info(String format, Object... arguments) {
        if (StringUtils.isEmpty(logId.get())) {
            logId.set(UUID.randomUUID().toString());
        }

        format = format + " with UUID:{}";

        if (arguments == null) {
            log.info(format, logId.get());
        }else{
            int length = arguments.length;

            Object[] newArguments = new Object[length + 1];

            System.arraycopy(arguments, 0, newArguments, 0, length);

            newArguments[length] = logId.get();

            log.info(format, newArguments);
        }

    }

}
