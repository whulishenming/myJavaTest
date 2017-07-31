package lsm.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * Created by lishenming on 2017/3/26.
 * 先启动 C:\apache\apache-activemq-5.14.4\bin\win64 activemq.bat
 */
public class Sender {

    public static void main(String[] args) throws JMSException, InterruptedException {

        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory();

        Connection connection = connectionFactory.createConnection();

        Session session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);

        Destination destination = session.createQueue("my-queue");

        MessageProducer producer = session.createProducer(destination);
        for (int i = 0; i < 3; i++) {
            TextMessage message = session.createTextMessage("this is a test message, i = " + i);

            Thread.sleep(2000);

            producer.send(message);
        }

        session.commit();
        session.close();
        connection.close();
    }
}
