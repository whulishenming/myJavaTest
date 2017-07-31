package lsm.cxf;

import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;

/**
 * Created by lishenming on 2017/4/21.
 */
public class Client {

    public static void main(String args[]) throws Exception{

        JaxWsDynamicClientFactory dcf =JaxWsDynamicClientFactory.newInstance();
        org.apache.cxf.endpoint.Client client =dcf.createClient("http://localhost:8081/springboot/soap/login?wsdl");
        //getUser 为接口中定义的方法名称  张三为传递的参数   返回一个Object数组
        Object[] objects=client.invoke("getUser",1);
        System.out.println("11111------"+objects[0]);

      /*  LoginDto loginDto = new LoginDto();
        loginDto.setPhoneNum(18521461595L);
        loginDto.setUsername("testtest");
        Object[] objects2=client.invoke("insertUser",loginDto);
        User user2 = (User) objects2[0];
        System.out.println("11111------"+user2.getId());*/
    }

}
