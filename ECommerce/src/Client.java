import javax.swing.*;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class Client {
    private ObjectOutputStream output; // output stream to server
    private ObjectInputStream input; // input stream from server
    private String message = ""; // message from server
    private Permission permissionType = new Permission(UserType.NONE); // Permission level for this particular client
    private String chatServer; // host server for this application
    private Socket client; // socket to communicate with server


    // connect to server and process messages from server
    public void runClient() {
        try // connect to server, get streams, process connection
        {
            connectToServer(); // create a Socket to make connection
            getStreams(); // get the input and output streams
            processConnection(); // process connection
        } // end try
        catch (EOFException eofException) {
            displayMessage("\nClient terminated connection");
        } // end catch
        catch (IOException ioException) {
            ioException.printStackTrace();
        } // end catch
        finally {
            closeConnection(); // close connection
        } // end finally
    } // end method runClient

    // connect to server
    private void connectToServer() throws IOException {
        displayMessage("Attempting connection\n");

        // create Socket to make connection to server
        client = new Socket(InetAddress.getByName(chatServer), 12345);

        // display connection information
        displayMessage("Connected to: " +
                client.getInetAddress().getHostName());
    } // end method connectToServer

    // get streams to send and receive data
    private void getStreams() throws IOException {
        // set up output stream for objects
        output = new ObjectOutputStream(client.getOutputStream());
        output.flush(); // flush output buffer to send header information

        // set up input stream for objects
        input = new ObjectInputStream(client.getInputStream());

        displayMessage("\nGot I/O streams\n");
    } // end method getStreams

    // process connection with server
    private void processConnection() throws IOException {

        do // process messages sent from server
        {
            try // read message and display it
            {
                //Server needs to send 2 messages: 1 for how to handle the second message
                String type = (String) input.readObject();
                if(type.equals("PERMISSION")){
                    permissionType = (Permission) input.readObject();
                }
                else if(type.equals("ACCOUNT")){
                    permissionType = (Permission) input.readObject();
                }
                else {
                    message = (String) input.readObject(); // read new message
                    displayMessage("\n" + message); // display message
                }
            } // end try
            catch (ClassNotFoundException classNotFoundException) {
                displayMessage("\nUnknown object type received");
            } // end catch

        } while (!message.equals("SERVER>>> TERMINATE"));
    } // end method processConnection

    // close streams and socket
    private void closeConnection() {
        displayMessage("\nClosing connection");

        try {
            output.close(); // close output stream
            input.close(); // close input stream
            client.close(); // close socket
        } // end try
        catch (IOException ioException) {
            ioException.printStackTrace();
        } // end catch
    } // end method closeConnection

    // send message to server
    private void sendData(String message) {
        try // send object to server
        {
            output.writeObject("CLIENT>>> " + message);
            output.flush(); // flush data to output
            displayMessage("\nCLIENT>>> " + message);
        } // end try
        catch (IOException ioException) {
            //TODO: Display error to user
        } // end catch
    } // end method sendData


    //TODO: Wait for Simeng to Implement GUI?
    // manipulates displayArea in the event-dispatch thread
    private void displayMessage(final String messageToDisplay) {
        SwingUtilities.invokeLater(
                new Runnable() {
                    public void run() // updates displayArea
                    {
                    } // end method run
                }  // end anonymous inner class
        ); // end call to SwingUtilities.invokeLater
    } // end method displayMessage


}
