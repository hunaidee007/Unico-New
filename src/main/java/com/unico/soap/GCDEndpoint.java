package com.unico.soap;

import java.util.List;

import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.util.JAXBSource;
import javax.xml.transform.Source;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.unico.model.Push;
import com.unico.schemas.GCDListRequest;
import com.unico.schemas.GCDListResponse;
import com.unico.schemas.GCDRequest;
import com.unico.schemas.GCDResponse;
import com.unico.schemas.GCDSumRequest;
import com.unico.schemas.GCDSumResponse;
import com.unico.service.GCDService;
import com.unico.service.MessageQeueService;

@Endpoint
public class GCDEndpoint {

	@Autowired
	GCDService gcdService;

	@Autowired
	MessageQeueService messageQeueService;

	@Resource(mappedName = "java:jboss/exported/jms/queue/test")
	private Queue myQueue;
	
	@Resource(mappedName = "java:jboss/exported/jms/queue/gcd")
	private Queue gcdQueue;

	@Resource(mappedName = "java:/ConnectionFactory")
	private ConnectionFactory myQueueFactory;
	private static final String NAMESPACE_URI = "http://mycompany.com/login/schemas";

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "GCDRequest")
	public @ResponsePayload Source handleHolidayRequest(
			@RequestPayload Source source) throws Exception {

		System.out.println("in here called");
		GCDRequest gcdRequest = (GCDRequest) unmarshal(source, GCDRequest.class);
		GCDResponse gcdResponse = new GCDResponse();
		Message message = messageQeueService.readMessage(myQueue,
				myQueueFactory);
		Push push = (Push) ((ObjectMessage) message).getObject();
		Integer gcd = gcdService.calculateGCD(push.getI1(), push.getI2());
		
		messageQeueService.sendMessage(gcd.toString(), gcdQueue, myQueueFactory);

		gcdResponse.setCalculatedCGD(gcd.toString());

		return marshal(gcdResponse);

	}
	
	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "GCDListRequest")
	public @ResponsePayload Source gcdList(
			@RequestPayload Source source) throws Exception {

		System.out.println("in here called");
		GCDListRequest  gcdListRequest = (GCDListRequest) unmarshal(source, GCDListRequest.class);
		GCDListResponse gcdListResponse = new GCDListResponse();
		
		List<Message> messages=messageQeueService.readAllMessages(gcdQueue, myQueueFactory);
		
		for(Message message : messages){
			String gcd=(String)((ObjectMessage) message).getObject();
			gcdListResponse.getGcd().add(gcd);
		}
		return marshal(gcdListResponse);

	}
	
	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "GCDSumRequest")
	public @ResponsePayload Source gcdSum(
			@RequestPayload Source source) throws Exception {

		System.out.println("in here called");
		GCDSumRequest  gcdSumRequest = (GCDSumRequest) unmarshal(source, GCDSumRequest.class);
		GCDSumResponse gcdSumResponse = new GCDSumResponse();
		
		List<Message> messages=messageQeueService.readAllMessages(gcdQueue, myQueueFactory);
		Integer gcdSum=0;
		for(Message message : messages){
			String gcd=(String)((ObjectMessage) message).getObject();
			int gcdint=Integer.parseInt(gcd);
			gcdSum+=gcdint;
		}
		gcdSumResponse.setGcdSum(gcdSum.toString());
		return marshal(gcdSumResponse);

	}

	private Source marshal(Object obj) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(obj.getClass());
		return new JAXBSource(context, obj);
	}

	private Object unmarshal(Source source, Class clazz) throws JAXBException {
		JAXBContext context;
		try {
			context = JAXBContext.newInstance(clazz);
			Unmarshaller um = context.createUnmarshaller();
			return um.unmarshal(source);
		} catch (JAXBException e) {
			e.printStackTrace();
			throw e;
		}
	}
}