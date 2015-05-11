package com.unico.rest;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Queue;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.unico.model.Push;
import com.unico.model.QueueResponse;
import com.unico.model.Response;
import com.unico.service.MessageQeueService;

@Controller
public class RestController {

	Logger logger = Logger.getLogger(RestController.class);

	@Autowired
	MessageQeueService messageQeueService;

	@Resource(mappedName = "java:jboss/exported/jms/queue/test")
	private Queue myQueue;

	@Resource(mappedName = "java:/ConnectionFactory")
	private ConnectionFactory myQueueFactory;

	@RequestMapping(value = "/pushRequest", method = RequestMethod.POST, headers = "Accept=application/xml")
	public @ResponseBody Response pushRequest(@RequestBody Push push)
			throws Exception {

		logger.info("Inside Method Push");
		
		Response response = new Response();
		System.out.println(push.getI1());

		messageQeueService.sendMessage(push, myQueue, myQueueFactory);

		response.setMessage("Numbers added to Message Queue");
		return response;
	}

	@RequestMapping(value = "/list", method = RequestMethod.POST, headers = "Accept=application/xml")
	public @ResponseBody QueueResponse list(@RequestBody Push push11)
			throws Exception {
		/*
		 * Message message = MessageQeueService.readMessage(myQueue,
		 * myQueueFactory); Push gcd = (Push) ((ObjectMessage)
		 * message).getObject(); return gcd;
		 */
		List<Message> messages = messageQeueService.readAllMessages(myQueue,
				myQueueFactory);
		List<Push> pushs = new ArrayList<Push>();
		QueueResponse queueResponse = new QueueResponse();
		for (Message message : messages) {
			Push push = (Push) ((ObjectMessage) message).getObject();
			queueResponse.getPush().add(push);
		}
		
		
		return queueResponse;
	}

}
