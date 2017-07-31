package lsm.designMode.observer;

/**
 * Created by lishenming on 2017/3/11.
 * 此布告板实现了Observer接口，所以可以从WeatherData对象中获得改变
 * 它也实现了DisplayElement接口，因为我们的API规定所有的布告板都必须实现此接口
 */
public class CurrentConditionsDisplay implements Observer, DisplayElement {
    private float temperature;
    private float humidity;
    private Subject weatherData;

    // 构造器需要WeatherData对象（也就是主题）作为注册之用
    public CurrentConditionsDisplay(Subject weatherData){
        this.weatherData = weatherData;
        weatherData.registerObserver(this);
    }

    // display()方法就是把最近的温度和湿度显示出来
    @Override
    public void display() {
        System.out.println("Cuttent conditions:" + temperature + "F degrees and " + humidity + "% humidity");
    }

    // 当update()被调用时，我们把温度和湿度保存起来，然后调用display()
    @Override
    public void update(float temp, float humidity, float pressure) {
        this.temperature = temp;
        this.humidity = humidity;
        display();
    }
}
