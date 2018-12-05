package lsm.util;

import java.util.List;

/**
 * @author lishenming
 * @date 2018-11-22 15:21
 **/
public class GsonTest {

    /**
     * rescode : 0
     * resmsg : OK
     * resdata : {"orderNo":"41220821634441216","ticketCount":1,"driverPhone":"400-874-5418","driverName":"(客服电话)","busLicenseNum":"湘NG5850","ticketInfoList":[{"qrcode":"EFGEFGDDDEDEEGMCGFB","ticketPassword":"074930","passengerName":"李测试","passengerIdCode":"350582198706290571","seatNum":"1"}]}
     */

    private String rescode;
    private String resmsg;
    private ResdataBean resdata;

    public String getRescode() {
        return rescode;
    }

    public void setRescode(String rescode) {
        this.rescode = rescode;
    }

    public String getResmsg() {
        return resmsg;
    }

    public void setResmsg(String resmsg) {
        this.resmsg = resmsg;
    }

    public ResdataBean getResdata() {
        return resdata;
    }

    public void setResdata(ResdataBean resdata) {
        this.resdata = resdata;
    }

    public static class ResdataBean {
        /**
         * orderNo : 41220821634441216
         * ticketCount : 1
         * driverPhone : 400-874-5418
         * driverName : (客服电话)
         * busLicenseNum : 湘NG5850
         * ticketInfoList : [{"qrcode":"EFGEFGDDDEDEEGMCGFB","ticketPassword":"074930","passengerName":"李测试","passengerIdCode":"350582198706290571","seatNum":"1"}]
         */

        private String orderNo;
        private int ticketCount;
        private String driverPhone;
        private String driverName;
        private String busLicenseNum;
        private List<TicketInfoListBean> ticketInfoList;

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        public int getTicketCount() {
            return ticketCount;
        }

        public void setTicketCount(int ticketCount) {
            this.ticketCount = ticketCount;
        }

        public String getDriverPhone() {
            return driverPhone;
        }

        public void setDriverPhone(String driverPhone) {
            this.driverPhone = driverPhone;
        }

        public String getDriverName() {
            return driverName;
        }

        public void setDriverName(String driverName) {
            this.driverName = driverName;
        }

        public String getBusLicenseNum() {
            return busLicenseNum;
        }

        public void setBusLicenseNum(String busLicenseNum) {
            this.busLicenseNum = busLicenseNum;
        }

        public List<TicketInfoListBean> getTicketInfoList() {
            return ticketInfoList;
        }

        public void setTicketInfoList(List<TicketInfoListBean> ticketInfoList) {
            this.ticketInfoList = ticketInfoList;
        }

        public static class TicketInfoListBean {
            /**
             * qrcode : EFGEFGDDDEDEEGMCGFB
             * ticketPassword : 074930
             * passengerName : 李测试
             * passengerIdCode : 350582198706290571
             * seatNum : 1
             */

            private String qrcode;
            private String ticketPassword;
            private String passengerName;
            private String passengerIdCode;
            private String seatNum;

            public String getQrcode() {
                return qrcode;
            }

            public void setQrcode(String qrcode) {
                this.qrcode = qrcode;
            }

            public String getTicketPassword() {
                return ticketPassword;
            }

            public void setTicketPassword(String ticketPassword) {
                this.ticketPassword = ticketPassword;
            }

            public String getPassengerName() {
                return passengerName;
            }

            public void setPassengerName(String passengerName) {
                this.passengerName = passengerName;
            }

            public String getPassengerIdCode() {
                return passengerIdCode;
            }

            public void setPassengerIdCode(String passengerIdCode) {
                this.passengerIdCode = passengerIdCode;
            }

            public String getSeatNum() {
                return seatNum;
            }

            public void setSeatNum(String seatNum) {
                this.seatNum = seatNum;
            }
        }
    }
}
