package lsm.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * Created by lishenming on 2017/3/26.
 */
public class Receiver {
    public static void main(String[] args) throws JMSException {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory();

        Connection connection = connectionFactory.createConnection();

        connection.start();

        Session session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);

        Destination destination = session.createQueue("my-queue");

        MessageConsumer consumer = session.createConsumer(destination);

        for (int i = 0; i < 3; i++) {
            TextMessage message = (TextMessage) consumer.receive();
            session.commit();
            System.out.println("receive message : " + message.getText());
        }

        session.close();
        connection.close();
    }
}
