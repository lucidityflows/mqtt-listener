package main.java;

// Class Overview:
// - This is the only class with a main method, and is what needs to be run to turn on the Gateway Controller.
// - Creates a GWController instance, that connects to the Gateway Diagnostic component.
// - For this class to operate correctly, the Python program (*gateway_diagnostics.py*) that complements it must be
//   started first on the same local machine, to create a socket that it can use to fulfill heartbeat requests and on
//   demand test requests.
// - All helper methods are used to order tests from the Gateway Diagnostics component.


public class MqttController
{

    private static int controllerID;
    private static int locationID;
    private static String owner;


    public MqttController(int controllerID, int locationID, String owner)
    {
        try {

            this.controllerID = controllerID;
            this.locationID = locationID;
            this.owner = owner;

        }
        catch (Exception e)
        {
            System.out.println("Your Gateway Controller did not get instantiated!!!!");
            e.printStackTrace();
        }
    }

    // The gateway controller operates using 2 Threads to fulfil its functions.
    // 1. MammalListener Thread
    //    - This thread manages the MammalListener data. Data is received, then posted via HTTPS to a test address.
    // 2. ReptileListener Thread
    //    - This thread manages the ReptileListener data. Data is received, then posted via HTTPS to a test address.



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