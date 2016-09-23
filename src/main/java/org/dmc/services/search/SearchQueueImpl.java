package org.dmc.services.search;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.dmc.services.DMCError;
import org.dmc.services.DMCServiceException;
import org.dmc.services.ServiceLogger;
import org.dmc.services.services.ServiceRunActiveMQ;

import javax.jms.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.lang.Thread.sleep;

/**
 * Created by 200005921 on 9/1/2016.
 */
public class SearchQueueImpl {

    private static final String LOGTAG = SearchQueueImpl.class.getName();

    private String activeMQServer; // = "52.36.23.38";
    private String activeMQServerPort = "61616";
    private String activeMQUser = "admin";
    private String activeMQUserPass = "asdfgqwer";
    private int TIMEOUT = 3000;
    private Boolean NON_TRANSACTED = false;
    static private String activeMQServerURL;

    private int poolSize = 5;
    private ExecutorService executorService;
    private SearchQueueListenerThread searchQueueListenerThread;

    public static final String SEARCH_QUEUE_NAME = "SEARCH_QUEUE";

    public SearchQueueImpl () {
        // If these variables are set, use the ones, otherwise use default
        String server = System.getenv("ActiveMQ_URL");
        if (server != null)  activeMQServer = server;
        else throw (new DMCServiceException(DMCError.ActiveMQServerURLNotSet, "The URL of ActiveMQ server is not set in the system environment."));
        String port = System.getenv("ActiveMQ_Port");
        if (port != null)  activeMQServerPort = port;
        String user = System.getenv("ActiveMQ_User");
        if (user != null)  activeMQUser = user;
        String pass = System.getenv("ActiveMQ_Password");
        if (pass != null)  activeMQUserPass = pass;
        activeMQServerURL = "tcp://"+activeMQServer+":"+activeMQServerPort;
        ServiceLogger.log(LOGTAG, "Successfully initialized necessary variables for activeMQ...");
    }

    public void createQueue (String queueName) throws DMCServiceException {
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
            ServiceLogger.log(LOGTAG, "Successfully created queue: " + queueName);

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

    public static final String FULL_INDEXING = "FULL_INDEXING";
    public static void sendFullIndexingMessage (String collectionName) throws SearchException {

        String msg = FULL_INDEXING + ":" + collectionName;
        ServiceLogger.log(LOGTAG, "sendFullIndexingMessage: " + msg);
        SearchQueueListenerThread.sendTextMessage(SearchQueueImpl.activeMQServerURL, SearchQueueImpl.SEARCH_QUEUE_NAME, msg);

    }

    public void startListenerThread () {
        if (executorService == null) {
            executorService = Executors.newFixedThreadPool(poolSize);
        }

        searchQueueListenerThread = new SearchQueueListenerThread(activeMQServerURL);
        executorService.execute(searchQueueListenerThread);
    }

    public void stopListenerThread () {
        if (searchQueueListenerThread != null) {
            searchQueueListenerThread.stopThread();
        }
    }

    public void init () {
        createQueue(SEARCH_QUEUE_NAME);
        startListenerThread();
    }

    public static void main (String[] args) {

        SearchQueueImpl impl = new SearchQueueImpl();
        impl.init();

        boolean running = true;
        while (running) {
            try {
                sleep (5000);
                if (Thread.interrupted()) {
                    running = false;
                }
            } catch (InterruptedException e) {
                //e.printStackTrace();
            }
        }
    }
}
