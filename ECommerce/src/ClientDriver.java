import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ClientDriver {
    public static void main(String args[]){

        ClientGUIDB clientGUIDB = new ClientGUIDB();

        clientGUIDB.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        clientGUIDB.setSize(1200, 700);
        clientGUIDB.pack();
        clientGUIDB.setLocationRelativeTo(null);
        clientGUIDB.setVisible(true);
        //Disconnects the user if they close window
        clientGUIDB.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                if(!clientGUIDB.getUsername().equals("~~~")) {
                    clientGUIDB.logoutUser();
                    clientGUIDB.getMySQL().closeConn();
                }
            }
        });
    }
}
