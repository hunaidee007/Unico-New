package com.unico.service;

import java.io.Serializable;
import java.util.List;

import javax.jms.ConnectionFactory;
import javax.jms.Message;
import javax.jms.Queue;

/**
 * 
 * @author hunaid.husain
 *
 */
public interface MessageQeueService {

	 public Message readMessage(Queue myQueue, ConnectionFactory myQueueFactory) throws Exception;
	 public List<Message> readAllMessages(Queue myQueue, ConnectionFactory myQueueFactory) throws Exception;
	 public <E extends Serializable> void sendMessage(E message, Queue myQueue, ConnectionFactory myQueueFactory) throws Exception;
}
