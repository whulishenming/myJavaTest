package lsm.spring.src;

import org.springframework.context.annotation.Scope;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by lishenming on 2017/3/25.
 *
 */

public class ResourceLoader {
    public static void main(String[] args) throws IOException
    {
        ResourceLoader resourceLoader = new ResourceLoader();
        resourceLoader.loadProperties1();
        resourceLoader.loadProperties2();
        resourceLoader.loadProperties3();
        resourceLoader.loadProperties4();
        resourceLoader.loadProperties5();

    }

    /**
     * 通过调用Class类的getResourceAsStream方法来加载资源文件
     * 第一种方式为绝对定位方式:以"/"开头
     * @throws IOException
     */
    public void loadProperties1() throws IOException
    {

        InputStream input = this.getClass().getResourceAsStream("/com/lsm/spring/resource/config.properties");
        printProperties(input);
    }

    /**
     * 通过调用Class类的getResourceAsStream方法来加载资源文件
     * 第二种方式为相对定位方式:不以"/"开头
     * @throws IOException
     */
    public void loadProperties2() throws IOException
    {
        InputStream input = this.getClass().getResourceAsStream("../resource/config.properties");
        printProperties(input);
    }
    public void loadProperties3() throws IOException
    {
        InputStream input = this.getClass().getClassLoader().getResourceAsStream("com/lsm/spring/resource/config.properties");
        printProperties(input);
    }
    public void loadProperties4() throws IOException
    {
        InputStream input = ClassLoader.getSystemResourceAsStream("com/lsm/spring/resource/config.properties");
        printProperties(input);
    }
    public void loadProperties5() throws IOException
    {
        InputStream input = ClassLoader.getSystemClassLoader().getResourceAsStream("com/lsm/spring/resource/config.properties");

        printProperties(input);
    }

    private void printProperties(InputStream input) throws IOException
    {
        Properties properties = new Properties();
        properties.load(input);
        System.out.println(properties.getProperty("test2"));
    }
}
