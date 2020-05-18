package main.java;

// Class Overview:
// This is the only class with a main method and it manages the MqttController.
// Creates a MqttController instance, with default settings. This is meant to allow for multiple devices, running their
// topics, locations, and owner.


public class MqttController
{

    private static int controllerID;
    private static int locationID;
    private static String owner;


    private MqttController(int controllerID, int locationID, String owner)
    {
        this.controllerID = controllerID;
        this.locationID = locationID;
        this.owner = owner;
    }

    // The gateway controller operates using 2 Threads to fulfil its functions.
    // 1. MammalListener Thread
    //    This thread manages the MammalListener data. Data is received, then posted via HTTPS to a test address.
    // 2. ReptileListener Thread
    //    This thread manages the ReptileListener data. Data is received, then posted via HTTPS to a test address.



    public static void main (String [] args)
    {
        System.out.println("\n**************|Mqtt Controller Setup Begun|************************");
        MqttController controller = new MqttController(1, 101, "Adam the Animal Lover");

        System.out.println("Your MqttController has been initialized.");
        System.out.println("MqttController: " + controller.controllerID + "\nLocation: " + controller.locationID +
                "\nOwner: " + controller.owner);

        MammalListener mammalListenerThread = new MammalListener();
        Thread mammalThread = new Thread(mammalListenerThread);
        mammalThread.setName("Mammal Thread");

        ReptileListener reptileListenerThread = new ReptileListener();
        Thread reptileThread = new Thread(reptileListenerThread);
        reptileThread.setName("Reptile Thread");

        // Start all threads
        mammalThread.start();
        reptileThread.start();

    }

}