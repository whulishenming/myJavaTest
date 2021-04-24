package lsm.aviator;

import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorDouble;
import com.googlecode.aviator.runtime.type.AviatorObject;

import java.util.Map;

/**
 * @author lishenming
 * @version 1.0
 * @date 2019/10/10 15:08
 *
 **/

public class AddFunction extends AbstractFunction {

    @Override
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2) {
        Number left = FunctionUtils.getNumberValue(arg1, env);
        Number right = FunctionUtils.getNumberValue(arg2, env);
        return new AviatorDouble(left.doubleValue() + right.doubleValue());
    }

    @Override
    public String getName() {
        return "add";
    }
}
