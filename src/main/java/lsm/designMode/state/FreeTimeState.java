package lsm.designMode.state;

/**
 * Created by lishenming on 2017/3/1.
 */
public class FreeTimeState implements State {

    Room hotelManagement;

    public FreeTimeState(Room hotelManagement){
        this.hotelManagement = hotelManagement;
    }


    public void bookRoom() {
        System.out.println("您已经成功预订了...");
        hotelManagement.setState(hotelManagement.getBookedState());   //状态变成已经预订
    }

    public void checkInRoom() {
        System.out.println("您已经成功入住了...");
        hotelManagement.setState(hotelManagement.getCheckInState());   //状态变成已经入住
    }

    public void checkOutRoom() {
        //不需要做操作
    }

    public void unsubscribeRoom() {
        //不需要做操作
    }

}
