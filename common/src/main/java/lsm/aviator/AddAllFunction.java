package lsm.aviator;

import com.googlecode.aviator.runtime.function.AbstractVariadicFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorDouble;
import com.googlecode.aviator.runtime.type.AviatorObject;

import java.util.Map;

/**
 * @author lishenming
 * @version 1.0
 * @date 2019/10/10 15:11
 * 多个参数
 **/

public class AddAllFunction extends AbstractVariadicFunction {
    @Override
    public AviatorObject variadicCall(Map<String, Object> map, AviatorObject... aviatorObjects) {
        double sum = 0;
        if (aviatorObjects != null) {
            for (AviatorObject aviatorObject : aviatorObjects) {
                sum = sum + FunctionUtils.getNumberValue(aviatorObject, map).doubleValue();
            }
        }
        return new AviatorDouble(sum);
    }

    @Override
    public String getName() {
        return "addAll";
    }
}
