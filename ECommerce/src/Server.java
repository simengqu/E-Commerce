import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Server extends JFrame {
    //JUST FOR RECORD KEEPING, REMOVE LATER
    private static final String messageTypes[] = {"MESSAGE","ERROR", "PERMISSION","ACCOUNT",
                                                  "BUY", "SELL", "MAP"};


    private JTextArea displayArea; // display information to user

    /**
     * Place to store Items. First is item Name
     *
     */
    HashMap<String, Item> itemList = new HashMap<>();

    /**
     * Maintains every user account. First is Username
     */
    HashMap<String, User> userAccounts = new HashMap<>();

    // Transactions list
    HashMap<Integer, String> transactionsList = new HashMap<>();

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

    /**
     * Total Number of connections in 1 session
     */
    private int counter = 1;

    /**
     * Number of Active clients
     */
    private int nClientsActive = 0;

    //TMake Server to display information, not do much else
    Server(){
        super("Server");

        connection = new SockServer[10]; // allocate array for up to 100 server threads
        executor = Executors.newFixedThreadPool(10); // create thread pool
        displayArea = new JTextArea(); // create displayArea
        add(new JScrollPane(displayArea), BorderLayout.CENTER);

        setSize(300, 150); // set size of window
        setVisible(true); // show window
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
                        displayArea.append(messageToDisplay);
                    } // end method run
                } // end anonymous inner class
        ); // end call to SwingUtilities.invokeLater
    } // end method displayMessage

    //TODO: Read in file of User Information/Item Information/Transaction History or Maintain Database?



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
                    displayMessage("\nServer " + myConID + " terminated connection");
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
            //Send all information in current list to client to start item window
            String message = "Connection " + myConID + " successful";
            sendList("MAP"); // send first list of information

            do // process messages sent from client
            {
                try // read message and display it
                {
                    String type = (String) input.readObject();
                    //Chooses which type of processes to run and messages to send to client
                    if(type.equals("PERMISSION")){ // Requires 3 inputs
                        sendPermission((String) input.readObject(), (String) input.readObject());
                    }
                    else if(type.equals("ACCOUNT")){ // Requires 4 inputs (name, pass, type of user)
                        sendAccount((String) input.readObject(), (String) input.readObject(), (String) input.readObject());
                    }
                    else if(type.equals("BUY")){ //Requires 3 inputs (name of Item, string of stored info)
                        type = updateList((String) input.readObject(), (String) input.readObject(), type);
                        sendList(type);
                    }
                    else if(type.equals("SELL")){ //Requires 2 inputs, inputs new item into listings
                        Item x = (Item) input.readObject();
                        updateList(x, x.getItemName(), type);
                        sendList("TRANSACTION");
                    }
                    else if(type.equals("TERMINATE")) message = "TERMINATE"; //Requires 1 input
                    else {message = (String) input.readObject(); // read new message (2 inputs)

                    displayMessage("\n" + myConID + message); // display message
                    }
                } // end try
                catch (ClassNotFoundException classNotFoundException) {
                    displayMessage("\nUnknown object type received");
                } // end catch

            } while (!message.equals("TERMINATE"));
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

        //Function to send a message
        private void sendData(String message) {
            try // send object to client
            {
                //TWO INPUTS AND OUTPUTS
                output.writeObject("MESSAGE");
                output.writeObject("SERVER" + myConID + ">>> " + message);
                output.flush(); // flush output to client
                displayMessage("\nSERVER" + myConID + ">>> " + message);
            } // end try
            catch (IOException ioException) {
                displayArea.append("\nError writing object");
            } // end catch
        } // end method sendData

        //Function to send the client a level of permission
        private void sendPermission(String login, String pass){
            boolean check = checkLoginInfo(login, pass);
            try{
                if(check) {
                    output.writeObject("PERMISSION");
                    output.writeObject(userAccounts.get(login).getModifier());

                }
                else {
                    output.writeObject("ERROR");
                    output.writeObject("Incorrect Username/Password");
                }
                output.flush();


            }//end try
            catch(IOException ioException){
                displayArea.append("\nError writing object");
            }//end Catch
        }// end method permission

        private void sendAccount(String login, String pass, String permission){
            try{
                if(!checkUsername(login)){
                    userAccounts.put(login, new User(permission, login, pass, userAccounts.size()+1));
                    output.writeObject("ACCOUNT");
                    output.writeObject(new Permission(permission));
                } else {
                    output.writeObject("MESSAGE");
                    output.writeObject("Incorrect Username/Password");
                }
                output.flush();

            }//end try
            catch(IOException ioException){
                displayArea.append("\nError writing object");
            }//end Catch
        }//End sendAccount

        //Send a whole list to the client to be read
        private void sendList(String type){
            try{
                output.writeObject(type);
                //Ensure there is no Error in the HashMaps
                if(type.equals("ERROR")){

                    output.writeObject("That item doesn't exist!");
                }
                else {
                    output.writeInt(itemList.size());
                    for (String i : itemList.keySet()) {
                        output.writeObject(i);
                    }
                }
                output.flush();
            }
            catch(IOException ioException){
                displayArea.append("\nError writing object");
            }//end Catch
        }

        //Removing/Decrementing Item
        private String updateList(String nameOfItem, String formattedInformation, String type){
            if(itemList.containsKey(nameOfItem)){
                if(itemList.get(nameOfItem).getInventory() > 0){
                    addTransaction(formattedInformation);
                    itemList.get(nameOfItem).decrementInventory();

                    //Remove Item if Inventory gone
                    if(itemList.get(nameOfItem).getInventory() == 0){
                        itemList.remove(nameOfItem);
                    }
                }
                //Error that shouldn't happen
                else {
                    itemList.remove(nameOfItem);
                    return "ERROR";
                }
            }
            else {
                return "ERROR";
            }



            return "TRANSACTION";
        }

        //Adding new item
        private void updateList(Item item, String nameOfItem, String type){
            if(itemList.containsKey(nameOfItem)){
                itemList.get(nameOfItem).incrementInventory();
            }
            else itemList.put(nameOfItem, item);
        }

        //Adds to transaction list. Should not be destroyed
        private void addTransaction(String formatted){
            transactionsList.put(transactionsList.size(), formatted);
        }
        private boolean checkLoginInfo(String login, String pass){
            if(checkUsername(login)){
                if(userAccounts.get(login).getPassword() == pass){
                    return true;
                }
            }
            return false;
        }
        private boolean checkUsername(String login){
            return(userAccounts.containsKey(login));
        }

    } // end class SockServer
}
