package main.java;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

// Class Overview:
// - This class was designed by Oracle to facilitate MQTT protocols. It has been modified significantly to react
//	 operate on Mosquitto Test Broker, and to react specifically to MQTT topics referencing: 'dog', 'cat', 'lizard',
//   'reptile'
// - This class allows the instantiation of 'MqttListener' objects that subscribe/publish to our preset MQTT Topics.

public class MqttListener implements MqttCallback, IMqttActionListener
{

    private String topic;

    public static final String ENCODING = "UTF-8";

    // Quality of Service = Exactly once
    // I want to receive all messages exactly once
    public static final int QUALITY_OF_SERVICE = 2;
    protected String name;
    protected String clientId;
    public String diagnosticTest;
    protected MqttAsyncClient client;
    protected MemoryPersistence memoryPersistence;
    protected IMqttToken connectToken;
    protected IMqttToken subscribeToken;
    public String successCode = "200";
    public String errorCode = "404";

    public MqttListener(String name, String topic)
    {
        this.name = name;
        this.topic = topic;
    }

    public String getName()
    {
        return name;
    }

    public void connect()
    {

        try {
            MqttConnectOptions options = new MqttConnectOptions();

            memoryPersistence = new MemoryPersistence();
            String serverURI = "tcp://test.mosquitto.org:1883";


            clientId = MqttAsyncClient.generateClientId();
            client = new MqttAsyncClient(serverURI, clientId, memoryPersistence);
            client.setCallback(this);
            connectToken = client.connect(options, null, this);
        }

        catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public boolean isConnected()
    {
        return (client != null) && (client.isConnected());
    }

    @Override
    public void connectionLost(Throwable cause)
    {
        // The MQTT client lost the connection
        cause.printStackTrace();
    }

    @Override
    public void onSuccess(IMqttToken asyncActionToken)
    {
        if (asyncActionToken.equals(connectToken))
        {
            System.out.println(String.format("%s successfully connected", name));

            try {
                subscribeToken = client.subscribe(topic, QUALITY_OF_SERVICE, null, this);
            }

            catch (MqttException e) {
                e.printStackTrace();
            }
        }

        else if (asyncActionToken.equals(subscribeToken)) {
            System.out.println(String.format("%s subscribed to the %s topic", name, topic));
            publishTextMessage(String.format("%s is listening.", name));
        }
    }

    @Override
    public void onFailure(IMqttToken asyncActionToken, Throwable exception)
    {
        // The method will run if an operation failed
        exception.printStackTrace();
    }

    public MessageActionListener publishTextMessage(String messageText)
    {
        byte[] bytesMessage;

        try {
            bytesMessage = messageText.getBytes(ENCODING);
            MqttMessage message;
            message = new MqttMessage(bytesMessage);
            String userContext = "ListeningMessage";
            MessageActionListener actionListener = new MessageActionListener(topic, messageText, userContext);
            client.publish(topic, message, userContext, actionListener);
            return actionListener;
        }

        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }

        catch (MqttException e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception
    {
        try {
            URL mammalURL = new URL("https://httpbin.org/post");
            URL reptileURL = new URL("https://httpbin.org/post");

            // A message has arrived from the MQTT broker
            // The MQTT broker doesn't send back
            // an acknowledgment to the server until
            // this method returns cleanly
            if (!topic.equals(topic))
            {
                this.publishTextMessage(errorCode);
                return;
            }

            String messageText = new String(message.getPayload(), ENCODING);
            if (messageText.equals(successCode) || (messageText.equals(errorCode)))
            {
                return;
            }

            System.out.println(String.format("%s received %s: %s", name, topic, messageText));
            //String[] keyValue = messageText.split(COMMAND_SEPARATOR);


            if (messageText.equals("[Cat] is listening.") || (messageText.equals("[Dog] is listening.")) ||
                    (messageText.equals("[Reptile] is listening.")) || (messageText.equals("[Snake] is listening.")))
            {
                System.out.println("\n***********|New Listener for topic: " + topic + "|************************");
            }

            else if (topic.equals("cat"))
            {
                System.out.println("\n***************|Cat Message Start|*********************************");
                HTTPSHandler.sendPOST(messageText, "cat_topic", mammalURL);
                System.out.println("Cat Message POST is complete!");
                System.out.println("***************|Cat Message End|***********************************");
            }

            else if (topic.equals("dog"))
            {
                System.out.println("\n***************|Dog Message Start|*******************************");
                HTTPSHandler.sendPOST(messageText, "dog_topic", mammalURL);
                System.out.println("Dog Message POST is complete!");
                System.out.println("***************|Dog Message End|*********************************");
            }

            else if (topic.equals("lizard"))
            {
                System.out.println("\n***************|Lizard Message Start|**********************************");
                HTTPSHandler.sendPOST(messageText, "lizard_topic", reptileURL);
                System.out.println("Lizard Message POST is complete!");
                System.out.println("***************|Lizard Message End|*************************************");
            }

            else if (topic.equals("snake"))
            {
                System.out.println("\n***************|Snake Message Start|**********************************");
                HTTPSHandler.sendPOST(messageText, "snake_topic", reptileURL);
                System.out.println("Snake Message POST is complete!");
                System.out.println("***************|Snake Message End|*************************************");
            }

        }

        catch (MalformedURLException e) {
            e.printStackTrace();
            System.out.println("A MalformedURL exception has occurred! Check the way you set up your HTTPS request.");
        }

    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        // Delivery for a message has been completed
        // and all acknowledgments have been received

    }

    public void disconnectDrone()
    {
        if (this.isConnected())
        {
            try {
                this.client.disconnect();
            }

            catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }
}