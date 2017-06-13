package lsm.designMode.state;

/**
 * Created by lishenming on 2017/3/1.
 */
public class BookedState implements State {
    Room hotelManagement;

    public BookedState(Room hotelManagement) {
        this.hotelManagement = hotelManagement;
    }

    public void bookRoom() {
        System.out.println("该房间已近给预定了...");
    }

    public void checkInRoom() {
        System.out.println("入住成功...");
        hotelManagement.setState(hotelManagement.getCheckInState());         //状态变成入住
    }

    public void checkOutRoom() {
        //不需要做操作
    }

    public void unsubscribeRoom() {
        System.out.println("退订成功,欢迎下次光临...");
        hotelManagement.setState(hotelManagement.getFreeTimeState());   //变成空闲状态
    }

}
