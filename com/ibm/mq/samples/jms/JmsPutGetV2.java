package com.ibm.mq.samples.jms;

import java.io.IOException;
import java.util.Scanner;

import javax.jms.Destination;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.TextMessage;

import com.ibm.msg.client.jms.JmsConnectionFactory;
import com.ibm.msg.client.jms.JmsFactoryFactory;
import com.ibm.msg.client.wmq.WMQConstants;

public class JmsPutGetV2 {

	// System exit status value (assume unset value to be 1)
	private static int status = 1;

	// Create variables for the connection to MQ
	private static final String HOST = "127.0.0.1"; // Host name or IP address
	private static final int PORT = 1414; // Listener port for your queue manager
	private static final String CHANNEL = "DEV.APP.SVRCONN"; // Channel name
	private static final String QMGR = "QM1"; // Queue manager name
	private static final String APP_USER = "app"; // User name that application uses to connect to MQ
	private static final String APP_PASSWORD = "passw0rd"; // Password that the application uses to connect to MQ
	private static final String QUEUE_NAME = "DEV.QUEUE.1"; // Queue that the application uses to put and get messages to and from
    
    // Create a connection factory variables
    private static JmsFactoryFactory ff = null;
	private static JmsConnectionFactory cf = null;

    // Variables
    private static JMSContext context = null;
    private static Destination destination = null;
    private static JMSProducer producer = null;
    private static JMSConsumer consumer = null;

	public static void main(String[] args) throws IOException{
		Scanner myInput = new Scanner(System.in);
		int usrChoice = 1;
        int usrMenuChoice = 0;
		String msg = "Unknown";

        mqConnection();
        while (usrChoice == 1) {
            menuDisplay();
            usrMenuChoice = myInput.nextInt();
            //System.out.println(usrMenuChoice);
            if (usrMenuChoice == 1) {
                System.out.print("Enter the Message : ");
                msg = myInput.next();
                mqPut(msg);
            } else {
                //System.out.println("In Else 1");
                if (usrMenuChoice == 2) {
                    //System.out.println("In 2nd If");
                    mqGet();
                } else {
                    System.out.println("Wrong Menu Choice");
                }
            }
            System.out.print ("Do you want to continue [1-Yes/2-No] : ");
            usrChoice = myInput.nextInt();
            System.out.println(usrChoice);
        }
	} // end main()

	private static void mqConnection() {
		try {
            ff = JmsFactoryFactory.getInstance(WMQConstants.WMQ_PROVIDER);
	        cf = ff.createConnectionFactory();
			// Set the properties
			cf.setStringProperty(WMQConstants.WMQ_HOST_NAME, HOST);
			cf.setIntProperty(WMQConstants.WMQ_PORT, PORT);
			cf.setStringProperty(WMQConstants.WMQ_CHANNEL, CHANNEL);
			cf.setIntProperty(WMQConstants.WMQ_CONNECTION_MODE, WMQConstants.WMQ_CM_CLIENT);
			cf.setStringProperty(WMQConstants.WMQ_QUEUE_MANAGER, QMGR);
			cf.setStringProperty(WMQConstants.WMQ_APPLICATIONNAME, "JmsPutGet (JMS)");
			cf.setBooleanProperty(WMQConstants.USER_AUTHENTICATION_MQCSP, true);
			cf.setStringProperty(WMQConstants.USERID, APP_USER);
			cf.setStringProperty(WMQConstants.PASSWORD, APP_PASSWORD);

            // Create JMS objects
			context = cf.createContext();
			destination = context.createQueue("queue:///" + QUEUE_NAME);

            // Create Producer
            producer = context.createProducer();

            // Create Consumer
            consumer = context.createConsumer(destination); // autoclosable
			recordSuccess();
		} 
		catch (JMSException jmsex) {
			recordFailure(jmsex);
		}
	}

	private static void mqPut(String msg) {
		TextMessage message = context.createTextMessage(msg);
        producer.send(destination, message);
        recordSuccess();
	}

	private static void mqGet(){
        String receivedMessage = consumer.receiveBody(String.class, 15000); // in ms or 15 seconds
        System.out.println("\nReceived message:\n" + receivedMessage);
        recordSuccess();
	}
	/**
	 * Record this run as successful.
	 */
	private static void recordSuccess() {
		System.out.println("SUCCESS");
		status = 0;
		return;
	}

	/**
	 * Menu 
	 */
	private static void menuDisplay() {
		System.out.println("MQ PUT/GET Demo");
		System.out.println("~~~~~~~~~~~~~~~");
		System.out.println("");
		System.out.println("1. MQ PUT");
		System.out.println("2. MQ GET");
		System.out.print("Enter your choice [1/2] : ");
	}

	/**
	 * Record this run as failure.
	 *
	 * @param ex
	 */
	private static void recordFailure(Exception ex) {
		if (ex != null) {
			if (ex instanceof JMSException) {
				processJMSException((JMSException) ex);
			} else {
				System.out.println(ex);
			}
		}
		System.out.println("FAILURE");
		status = -1;
		return;
	}

	/**
	 * Process a JMSException and any associated inner exceptions.
	 *
	 * @param jmsex
	 */
	private static void processJMSException(JMSException jmsex) {
		System.out.println(jmsex);
		Throwable innerException = jmsex.getLinkedException();
		if (innerException != null) {
			System.out.println("Inner exception(s):");
		}
		while (innerException != null) {
			System.out.println(innerException);
			innerException = innerException.getCause();
		}
		return;
	}

}
