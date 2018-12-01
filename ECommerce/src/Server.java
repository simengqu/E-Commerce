import javax.swing.*;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;


public class Server {
    /**
     * Place to store Items
     */
    HashMap<String, Integer> itemList = new HashMap<>();

    /**
     * Maintains number of active users at any given time
     */
    HashMap<UserType, Integer> activeUsers = new HashMap<>();

    /**
     * Runs the client
     */
    private ExecutorService executor;
    /**
     * Server Socket to connect to connection
     */
    private ServerSocket server;
    /**
     * Socket to take client request
     */
    private SockServer connection[];

    private int counter = 1;

    private int nClientsActive = 0;


    Server(){

    }

    /**
     * Create server socket and begin connections
     */
    public void startHost(){
        try
        {
            server = new ServerSocket(12345, 100);

            while (true) {
                try {
                    //create a new runnable object to serve the next client to call in
                    connection[counter] = new SockServer(counter);
                    // make that new object wait for a connection on that new server object
                    connection[counter].waitForConnection();
                    nClientsActive++;
                    // launch that server object into its own new thread
                    executor.execute(connection[counter]);
                    // then, continue to create another object and wait (loop)

                } // end try
                catch (EOFException eofException) {
                    displayMessage("\nServer terminated connection");
                } // end catch
                finally {
                    ++counter;
                } // end finally
            } // end while

        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    // manipulates displayArea in the event-dispatch thread
    private void displayMessage(final String messageToDisplay) {
        SwingUtilities.invokeLater(
                new Runnable() {
                    public void run() // updates displayArea
                    {
                    } // end method run
                } // end anonymous inner class
        ); // end call to SwingUtilities.invokeLater
    } // end method displayMessage


    //TODO: Edit number of active users
    private void newClient(Socket client){
    }


    /* This new Inner Class implements Runnable and objects instantiated from this
     * class will become server threads each serving a different client
     */
    private class SockServer implements Runnable {
        private ObjectOutputStream output; // output stream to client
        private ObjectInputStream input; // input stream from client
        private Socket connection; // connection to client
        private int myConID;
        private boolean alive = false;

        public SockServer(int counterIn) {
            myConID = counterIn;
        }

        public void run() {
            try {
                alive = true;
                try {
                    getStreams(); // get input & output streams
                    processConnection(); // process connection
                    nClientsActive--;

                } // end try
                catch (EOFException eofException) {
                    displayMessage("\nServer" + myConID + " terminated connection");
                } finally {
                    closeConnection(); //  close connection
                }// end catch
            } // end try
            catch (IOException ioException) {
                ioException.printStackTrace();
            } // end catch
        } // end try

        // wait for connection to arrive, then display connection info
        private void waitForConnection() throws IOException {

            displayMessage("Waiting for connection" + myConID + "\n");
            connection = server.accept(); // allow server to accept connection
            displayMessage("Connection " + myConID + " received from: " +
                    connection.getInetAddress().getHostName());
        } // end method waitForConnection

        private void getStreams() throws IOException {
            // set up output stream for objects
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush(); // flush output buffer to send header information

            // set up input stream for objects
            input = new ObjectInputStream(connection.getInputStream());

            displayMessage("\nGot I/O streams\n");
        } // end method getStreams

        // process connection with client
        private void processConnection() throws IOException {
            String message = "Connection " + myConID + " successful";
            sendData(message); // send connection successful message

            do // process messages sent from client
            {
                try // read message and display it
                {
                    message = (String) input.readObject(); // read new message
                    displayMessage("\n" + myConID + message); // display message
                } // end try
                catch (ClassNotFoundException classNotFoundException) {
                    displayMessage("\nUnknown object type received");
                } // end catch

            } while (!message.equals("CLIENT>>> TERMINATE"));
        } // end method processConnection

        // close streams and socket
        private void closeConnection() {
            displayMessage("\nTerminating connection " + myConID + "\n");
            displayMessage("\nNumber of connections = " + nClientsActive + "\n");
            alive = false;

            try {
                output.close(); // close output stream
                input.close(); // close input stream
                connection.close(); // close socket
            } // end try
            catch (IOException ioException) {
                ioException.printStackTrace();
            } // end catch
        } // end method closeConnection

        private void sendData(String message) {
            try // send object to client
            {
                output.writeObject("SERVER" + myConID + ">>> " + message);
                output.flush(); // flush output to client
                displayMessage("\nSERVER" + myConID + ">>> " + message);
            } // end try
            catch (IOException ioException) {
                //TODO: Show error in display box
            } // end catch
        } // end method sendData


    } // end class SockServer
}
