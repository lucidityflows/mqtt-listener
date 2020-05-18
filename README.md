# mqtt-listener

*Application Overview*

The mqtt-listener is Maven application meant to demo how mqtt topics can be subcribed and published onto using the 
Mosquitto Test Broker.

There are two threads setup by the main method in MqttController.java. The threads are set to monitor topics
relating to Mammals and Reptiles. These are stand in topics that could be altered to whatever is needed.

After receiving an on topic message, the message communicates via HTTPS to a test API endpoint associated with 
httpbin.org. 

This demo is meant to show that remote IoT devices could report into topics to a cloud server, from their operating
system.
