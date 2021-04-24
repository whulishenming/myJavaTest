package lsm.util;

import org.apache.commons.lang.StringUtils;

/**
 * @author lishenming
 * @version 1.0
 * @date 2019/9/20 10:39
 **/

public enum ErrorCode {
    /**
     *
     */
    SUCCESS(1, "成功"),

    FAIL(0, "服务器错鸟,请联系管理员"),

    NO_RESULT(2, "结果为空"),

    PARAM_ERROR(3, "参数错误"),

    LOGIN_VALID(4, "登录失效,请重新登录"),

    RIDE_NOT_EXIST(5, "行程信息不存在,请重新发起行程"),

    RIDE_STATUS_ERROR(6, "行程状态已变化,请刷新行程"),

    ORDER_STATUS_ERROR(7, "订单状态已经改变,请刷新行程"),

    THANKS_FEE_ERROR(8, "已经添加同金额感谢费,请勿重复添加"),

    PERMISSION_DENIED(9, "权限不足"),

    CANCEL_ORDER_FULL(10, "今日取消订单超过限制"),

    CITY_NOT_EXIST(11, "今日取消订单超过限制"),

    ILLEGALITY_USER(12, "该用户不存在"),

    ORDER_NOT_EXIST(13,"订单信息不存在"),

    VEHICLE_NOT_EXIST(14,"车辆信息不存在"),

    RISK_CONTROL(15,"触发风险控制"),

    // 支付回调相关 code必须>100
    WXPAY_PRE_FAIL(108, "预支付异常"),

    WXPAY_QUERY_TOTAL_FEE_ERROR(109, "支付金额异常"),

    WXPAY_QUERY_FAIL(110, "查询支付状态异常"),

    WXPAY_BACK_ORDER_PROCESS_FAIL(111, "支付回调异常"),

    PAY_CALL_BACK_PROCESS_FAIL(112, "支付回调处理失败"),

    PAY_CALL_BACK_PARAM_ERROR(113, "支付回调参数异常"),

    PAY_CALL_BACK_STATUS_ERROR(114, "支付回调状态异常"),

    PAY_CALL_BACK_ORDER_PROCESS_ERROR(115, "支付回调处理订单异常"),

    LANDING_BILL_APPLY_ERROR(116, "支付前校验失败"),

    PAYMENT_CALLBACK_SIGN_ERROR(117, "签名不正确"),

    REFUND_NOT_EXIST(118, "退款信息不存在"),

    REFUND_FEE_NOT_ENOUGH(119, "可退金额不足"),

    PAYMENT_NOT_EXIST(120, "支付信息无效"),

    PAYMENT_STATUS_ERROR(121, "支付状态异常"),

    USER_NOT_EXIST(122, "用户信息不存在,请重新登录"),

    RIDE_JOIN_STATUS_ERROR(123, "未找到拼友信息"),

    COUPON_RISK_LEVEL(124, "优惠券使用次数太多,请取消重试"),

    COUPON_USE_ERROR(125, "优惠券使用失败，请返回到订单填写页面修改订单信息"),

    COUPON_OVER_LIMIT(126, "优惠券不支持订单全额抵扣,请返回到订单填写页面修改订单信息"),

    REPEAT_SUCCESS(200, "已经是成功状态"),

    // 接口错误码
    INTERFACE_DIDA_ERROR(701, "diDa接口错误"),

    // 获取携程MapGateway失败
    INTERFACE_MAP_GATEWAY(702, "获取携程MapGateway失败"),

    DRIVER_NOT_APPROVE(10001, "司机未认证通过"),

    WX_ACCREDIT_NOT_BINDING(10002, "微信账号未绑定"),

    ;

    public static ErrorCode ofDesc(String desc) {
        if (StringUtils.isNotBlank(desc)) {
            for (ErrorCode errorCode : values()) {
                if (StringUtils.equals(desc, errorCode.getMessage())) {
                    return errorCode;
                }
            }
        }
        return FAIL;
    }

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private int code;
    private String message;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
