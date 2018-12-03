import javax.swing.*;

public class ServerDriver {
    public static void main(String args[]){
        ServerGUIDB serverGUIDB = new ServerGUIDB();
        serverGUIDB.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        serverGUIDB.setSize(1200, 700);
        serverGUIDB.setLocationRelativeTo(null);
        serverGUIDB.setVisible(true);
    }
}
