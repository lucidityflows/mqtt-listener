package main.java;

import org.eclipse.paho.client.mqttv3.MqttException;

// Class Overview:
// This class implements Runnable in order to allow it to be used for multithreading. It creates the 2 MQTTListeners,
// mammal1 & mammal2, that listen over for topics 'cat' and 'dog'. This keeps the listeners referring to mammals on a
// thread by itself.
public class MammalListener implements Runnable
{
    @Override
    public void run() {
        System.out.println("\n*******|A new thread has begun: " + Thread.currentThread().getName() + "|*******");
        MqttListener mammal1 = new MqttListener("[Cat]", "cat");
        MqttListener mammal2 = new MqttListener("[Dog]", "dog");

        try {
            mammal1.connect();
            //Thread.sleep(5000);
            mammal2.connect();
            //Thread.sleep(5000);

            while (true) {

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        // Disconnection for all drones
        finally {

            if (mammal1.isConnected()) {

                try {
                    mammal1.client.disconnect();
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }

            if (mammal2.isConnected()) {

                try {
                    mammal2.client.disconnect();
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}