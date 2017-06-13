package lsm.designMode.observer;

/**
 * Created by lishenming on 2017/3/11.
 * 观察者模式
 */
public class Client {
    public static void main(String[] args) {
        // 建立WeatherData对象
        WeatherData weatherData = new WeatherData();

        // 建立目前状况布告板
        CurrentConditionsDisplay currentDisplay = new CurrentConditionsDisplay(weatherData);

        // 模拟新的气象测量
        weatherData.setMeasurements(80, 65, 30.4f);
        weatherData.setMeasurements(82, 70, 29.2f);
        weatherData.setMeasurements(78, 90, 29.2f);
    }
}
