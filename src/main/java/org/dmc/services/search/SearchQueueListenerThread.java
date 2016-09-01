package org.dmc.services.search;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.dmc.services.ServiceLogger;
import org.dmc.solr.SolrUtils;

import javax.jms.*;

import java.io.IOException;

import static java.lang.Thread.sleep;

/**
 * Created by 200005921 on 9/1/2016.
 */
public class SearchQueueListenerThread implements Runnable {

    private static final String LOGTAG = SearchQueueListenerThread.class.getName();

    private String url;
    private Connection connection = null;
    private Session session = null;
    private Destination queue = null;

    volatile boolean running = false;

    private int DELAY_SECONDS = 30;

    public SearchQueueListenerThread (String url) {
        this.url = url;
    }

    public void  init () throws SearchException {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);

        MessageConsumer consumer = null;

        try {
            connection = connectionFactory.createConnection();
            connection.setExceptionListener(new ExceptionListener() {
                @Override
                public void onException(JMSException e) {
                    ServiceLogger.log(LOGTAG, "Exception: " + e.toString());
                }
            });

            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            queue = session.createQueue(SearchQueueImpl.SEARCH_QUEUE_NAME);
            consumer = session.createConsumer(queue);
            MessageListener listener = getListener();
            consumer.setMessageListener(listener);
            connection.start();

        } catch (JMSException e) {
            e.printStackTrace();
            throw new SearchException(e);
        }
    }

    public void close ()  throws SearchException{
        if (connection != null) {
            try {
                connection.stop();
                connection.close();
            } catch (JMSException e) {
                e.printStackTrace();
                throw new SearchException(e);
            }
        }
    }

    public MessageListener getListener () {
        MessageListener listener = new MessageListener() {
            public void onMessage(Message message) {
                ServiceLogger.log(LOGTAG, "onMessage: " + message);

                try {
                    if (message instanceof TextMessage) {
                        TextMessage textMessage = (TextMessage) message;
                        ServiceLogger.log(LOGTAG,"Received message" + textMessage.getText() + "'");

                        String msg = textMessage.getText();
                        if (msg != null) {

                            if (msg.indexOf(SearchQueueImpl.FULL_INDEXING) >= 0) {
                                int off = msg.indexOf(":");
                                if (off >= 0 && (off+1 < msg.length())) {
                                    String collectionName = msg.substring(off+1);

                                    // Delay before triggering the index to give time for the database to commit
                                    try {
                                        Thread.sleep (DELAY_SECONDS * 1000);
                                    } catch (InterruptedException e) {
                                        //e.printStackTrace();
                                    }

                                    try {
                                        SolrUtils.invokeFullIndexing(SolrUtils.getBaseUrl(), collectionName);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                        ServiceLogger.log(LOGTAG, "Exception: " + e.toString());
                                    }
                                }
                            }
                        }
                    }
                    else if (message instanceof ObjectMessage) {
                        ObjectMessage objMessage = (ObjectMessage) message;
                        ServiceLogger.log(LOGTAG,"Received message" + objMessage.getObject() + "'");
                    }
                } catch (JMSException e) {
                    ServiceLogger.log(LOGTAG, "Exception: " + e.toString());
                    e.printStackTrace();
                }
            }
        };
        return listener;
    }

    static public void sendTextMessage (String url, String topic, String message)  throws SearchException {
        MessageProducer messageProducer = null;
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);

        Connection connection = null;
        Session session = null;

        try {
            connection = connectionFactory.createConnection();
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createQueue(topic);
            messageProducer = session.createProducer(destination);
            messageProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            TextMessage textMessage = session.createTextMessage(message);
            messageProducer.send(textMessage);

        } catch (JMSException e) {
            e.printStackTrace();
            throw new SearchException(e);
        } finally {
            try {
                if (session != null) session.close();
                if (connection != null) connection.close();
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopThread () {
        running = false;
    }

    @Override
    public void run() {

        try {
            init();
        } catch (SearchException e) {
            e.printStackTrace();
        }

        running = true;
        ServiceLogger.log(LOGTAG, "Starting thread...");
        int count = 0;
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

        ServiceLogger.log(LOGTAG, "Thread stopped ... cleaning up ...");

        try {
            close();
        } catch (SearchException e) {
            e.printStackTrace();
        }
    }
}
