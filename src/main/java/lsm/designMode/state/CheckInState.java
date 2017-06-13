package lsm.designMode.state;

/**
 * Created by lishenming on 2017/3/1.
 */
public class CheckInState implements State {
    Room hotelManagement;
    public CheckInState(Room hotelManagement) {
        this.hotelManagement = hotelManagement;
    }

    public void bookRoom() {
        System.out.println("该房间已经入住了...");
    }

    public void checkInRoom() {
        System.out.println("该房间已经入住了...");
    }

    public void checkOutRoom() {
        System.out.println("退房成功....");
        hotelManagement.setState(hotelManagement.getFreeTimeState());     //状态变成空闲
    }

    public void unsubscribeRoom() {
        //不需要做操作
    }

}
