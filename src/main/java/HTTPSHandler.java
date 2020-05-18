package main.java;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

// Class Overview:
// This class is used to create an HTTPS connection when necessary to POST data from an MqttListener object.
// It is put into a separate class for clarity of code.

public class HTTPSHandler
{
    private static HttpURLConnection connection;

    public static void sendPOST(String data, String headerName, URL destination)
    {
        try
        {

            connection = (HttpURLConnection) destination.openConnection();

            // Request Setup
            connection.setRequestMethod("POST");

            // Instantiate a requestData object to store our data
            StringBuilder postData = new StringBuilder();
            postData.append(data);

            byte[] postDataBytes = postData.toString().getBytes("UTF-8");

            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Content-Length", String.valueOf(postDataBytes));
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("header1", headerName);
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);
            connection.setDoOutput(true);

            try (DataOutputStream writer = new DataOutputStream(connection.getOutputStream()))
            {
                writer.write(postDataBytes);
                writer.flush();
                writer.close();
            }

            StringBuilder content;

            try (BufferedReader in = new BufferedReader(new InputStreamReader((connection.getInputStream()))))
            {
                String line;
                content = new StringBuilder();

                while ((line = in.readLine()) != null)
                {
                    content.append(line);
                    content.append(System.lineSeparator());
                }
            }

            System.out.println(content.toString());
        }

        catch (MalformedURLException e)
        {
            e.printStackTrace();
            System.out.println("A MalformedURL exception has occurred! Check the way you set up your HTTPS request.");
        }

        catch (IOException e)
        {
            e.printStackTrace();
            System.out.println("An IO exception was generated! Look at how you are transmitting your data.");
        }

        finally
        {
            connection.disconnect();
        }
    }


}