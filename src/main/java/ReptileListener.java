package main.java;

import org.eclipse.paho.client.mqttv3.MqttException;

// Class Overview:
// This class implements Runnable in order to allow it to be used for multithreading. It creates the 2 MQTTListeners,
// reptile1 & reptile2, that listen over for topics 'lizard' and 'snake'. This keeps the listeners referring to reptiles
// on a thread by itself.
public class ReptileListener implements Runnable
{
    @Override
    public void run() {
        System.out.println("\n*******|A new thread has begun: " + Thread.currentThread().getName() + "|*******");
        MqttListener reptile1 = new MqttListener("[Lizard]", "lizard");
        MqttListener reptile2 = new MqttListener("[Snake]", "snake");

        try {
            reptile1.connect();
            //Thread.sleep(5000);
            reptile2.connect();
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

            if (reptile1.isConnected()) {

                try {
                    reptile1.client.disconnect();
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }

            if (reptile2.isConnected()) {

                try {
                    reptile2.client.disconnect();
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}