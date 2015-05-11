package com.unico.service;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.Session;

import org.springframework.stereotype.Service;
/**
 * 
 * @author hunaid.husain
 *
 */
@Service
public class MessageQueueServiceImpl implements MessageQeueService{
	  public <E extends Serializable> void sendMessage(E message, Queue myQueue, ConnectionFactory myQueueFactory) throws Exception {
	        Connection connection = null;
	        Session session = null;
	        try {
	            connection = myQueueFactory.createConnection();
	            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
	            MessageProducer publisher = session.createProducer((Destination) myQueue);
	            connection.start();
	            ObjectMessage objectMessage = session.createObjectMessage(message);
	            publisher.send(objectMessage);
	        } finally {
	        	 if (session != null) {
	 	            session.close();
	 	        }
	 	        if (connection != null) {
	 	            connection.close();
	 	        }
	        }
	    }

	    /**
	     *
	     * @param session_acknowledge
	     */
	    public List<Message> readAllMessages(Queue myQueue, ConnectionFactory myQueueFactory) throws Exception {
	        Connection connection = null;
	        Session session = null;
	        try {
	            connection = myQueueFactory.createConnection();
	            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
	            QueueBrowser browser = session.createBrowser(myQueue);
	            return Collections.list(browser.getEnumeration());

	        } finally {
	        	 if (session != null) {
	 	            session.close();
	 	        }
	 	        if (connection != null) {
	 	            connection.close();
	 	        }
	        }

	    }

	    /**
	     *
	     * @param session_acknowledge
	     */
	    public Message readMessage(Queue myQueue, ConnectionFactory myQueueFactory) throws Exception {
	        Connection connection = null;
	        Session session = null;
	        try {
	            connection = myQueueFactory.createConnection();
	            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
	            Destination dest = (Destination) myQueue;
	            MessageConsumer consumer = session.createConsumer(dest);
	            connection.start();
	            return consumer.receive(1);
	        } finally {
	        	 if (session != null) {
	 	            session.close();
	 	        }
	 	        if (connection != null) {
	 	            connection.close();
	 	        }
	        }

	    }

	   
	   
}
