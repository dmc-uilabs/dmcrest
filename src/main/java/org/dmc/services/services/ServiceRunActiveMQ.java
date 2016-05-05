package org.dmc.services.services;

import java.util.ArrayList;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.dmc.services.DMCError;
import org.dmc.services.DMCServiceException;

public class ServiceRunActiveMQ {
	
    private String activeMQServer = "52.36.23.38";
    private String activeMQServerPort = "61616";
    private String activeMQUser = "admin";
    private String activeMQUserPass = "asdfgqwer";
    private int TIMEOUT = 3000;
    private Boolean NON_TRANSACTED = false;
    private String activeMQServerURL;
	
	public ServiceRunActiveMQ()
	{
		// If these variables are set, use the ones, otherwise use default
		String server = System.getenv("ActiveMQ_URL");
		if (server != null)  activeMQServer = server;
		String port = System.getenv("ActiveMQ_Port");
		if (port != null)  activeMQServerPort = port;
		String user = System.getenv("ActiveMQ_User");
		if (user != null)  activeMQUser = user;
		String pass = System.getenv("ActiveMQ_Password");
		if (pass != null)  activeMQUserPass = pass;
		activeMQServerURL = "tcp://"+activeMQServer+":"+activeMQServerPort;
	}
		
    public ArrayList<String> readMessageFromMQ(String queueName) throws DMCServiceException
    {  	
    	ArrayList<String> result = new ArrayList<String>();
//        System.out.println("\nWaiting to receive messages... will timeout after " + TIMEOUT / 1000 +"s");
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(activeMQUser, activeMQUserPass, activeMQServerURL);
        Connection connection = null;
        try {
            connection = connectionFactory.createConnection();
            connection.start();
            Session session = connection.createSession(NON_TRANSACTED, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createQueue(queueName);
            MessageConsumer consumer = session.createConsumer(destination);
            while (true) {
                Message message = consumer.receive(TIMEOUT);

                if (message != null) {
                    if (message instanceof TextMessage) {
                        String text = ((TextMessage) message).getText();
                        result.add(text);
                    }
                } else {
                    break;
                }
            }

            consumer.close();
            session.close();

        } catch (Exception e) {
        	throw (new DMCServiceException(DMCError.CanNotReadMessage, "Can not read from ActiveMQ queue: " + e.toString()));
        }
        finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (JMSException e) {
                	throw (new DMCServiceException(DMCError.CanNotCloseActiveMQConnection, "Can close ActiveMQ connection: " + e.toString()));
                }
            }
        }
        return result;
    }

	
    public void createQueue(String queueName) throws DMCServiceException
    {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(activeMQUser, activeMQUserPass, activeMQServerURL);
        Connection connection = null;
        try {

            connection = connectionFactory.createConnection();
            connection.start();

            Session session = connection.createSession(NON_TRANSACTED, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createQueue(queueName);
            MessageProducer producer = session.createProducer(destination);
            producer.close();
            session.close();

        } catch (Exception e) {
        	throw (new DMCServiceException(DMCError.CanNotCreateQueue, "Can not create ActiveMQ queue: " + e.toString()));
        }
        finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (JMSException e) {
                	throw (new DMCServiceException(DMCError.CanNotCloseActiveMQConnection, "Can close ActiveMQ connection: " + e.toString()));
                }
            }
        }
    }
    
    public void deleteQueue(String queueID) throws DMCServiceException
    {   
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(activeMQUser, activeMQUser, activeMQServerURL);
        ActiveMQConnection connection = null;
        try {

            connection = (ActiveMQConnection) connectionFactory.createConnection();
            connection.destroyDestination(new ActiveMQQueue(queueID));
        } catch (Exception e) {
        	throw (new DMCServiceException(DMCError.CanNotDeleteQueue, "Can not delete ActiveMQ queue: " + e.toString()));
        }
        finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (JMSException e) {
                	throw (new DMCServiceException(DMCError.CanNotCloseActiveMQConnection, "Can close ActiveMQ connection: " + e.toString()));
                }
            }
        }
    }
    
/*    public static void main(String[] args)
    {
    	
    	try {
    		ServiceRunActiveMQ activeMQServer = new ServiceRunActiveMQ();
    		// Test 1:
    		System.out.println("Now try to create a queue, with name: DOME_Model_Run_TestQueue");
    		activeMQServer.createQueue("DOME_Model_Run_TestQueue");
    		// Test 2: 
    		// System.out.println("Now try to read from a queue, with name: DOME_Model_Run_TestQueue");
    		//ArrayList<String> messages = activeMQServer.readMessageFromMQ("DOME_Model_Run_TestQueue");
    		//for (int i=0;i<messages.size();i++)
    		//{
    		//	System.out.println("Message " + i + " is: " + messages.get(i));
    		//}
    		// Test 3:
    		// System.out.println("Now try to delete a queue, with name: DOME_Model_Run_TestQueue");
    		// activeMQServer.deleteQueue("DOME_Model_Run_TestQueue");
    	}
    	catch (DMCServiceException e)
    	{
    		e.printStackTrace();
    	}   	
    } */
}
