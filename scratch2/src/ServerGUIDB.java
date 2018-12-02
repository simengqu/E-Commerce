import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class ServerGUIDB extends JFrame {
    private MySQL mySQL;
    private Connection connection = null;
    private Statement statement = null;
    private String sql;
    private ResultSet resultSet;

    private JPanel mainPanel;
    private JPanel buttonPanel;
    private JPanel historyPanel;
    private JPanel clientPanel;

    private JButton showHistory;
//    private JButton buyHistory;
//    private JButton sellHistory;
    private JButton searchUser;
    private JButton inventory;

    private JTextArea historyArea;
    private JTextArea buyHistoryArea;
    private JTextArea sellHistoryArea;
    private JTextArea clientsArea;
    private JTextArea userHistory;
    private JTextArea inventoryArea;
    //private JTextArea sellers;

    private JTextField userField;

    public ServerGUIDB(){

        super("Server");
        // mySQL
        mySQL = new MySQL();

        // panels
        mainPanel = new JPanel();
        buttonPanel = new JPanel();
        historyPanel = new JPanel();
        clientPanel = new JPanel();

        // buttons
        ButtonHandler buttonHandler = new ButtonHandler();
        showHistory = new JButton("show transaction history");
        showHistory.addActionListener(buttonHandler);
//        buyHistory = new JButton("buy history");
//        buyHistory.addActionListener(buttonHandler);
//        sellHistory = new JButton("sell history");
//        sellHistory.addActionListener(buttonHandler);
        searchUser = new JButton("search user");
        searchUser.addActionListener(buttonHandler);
        inventory = new JButton("Show inventory");
        inventory.addActionListener(buttonHandler);

        // text areas
        historyArea = new JTextArea();
        historyArea.setPreferredSize(new Dimension(150,300));
        historyArea.setEditable(false);
        buyHistoryArea = new JTextArea();
        buyHistoryArea.setPreferredSize(new Dimension(150,300));
        buyHistoryArea.setEditable(false);
        sellHistoryArea = new JTextArea();
        sellHistoryArea.setPreferredSize(new Dimension(150,300));
        sellHistoryArea.setEditable(false);
        clientsArea = new JTextArea();
        clientsArea.setPreferredSize(new Dimension(150,300));
        clientsArea.setEditable(false);
        userHistory = new JTextArea();
        userHistory.setPreferredSize(new Dimension(150,300));
        userHistory.setEditable(false);
        inventoryArea = new JTextArea();
        inventoryArea.setPreferredSize(new Dimension(150,300));
        inventoryArea.setEditable(false);
//        sellers = new JTextArea();
//        sellers.setPreferredSize(new Dimension(150,300));
//        sellers.setEditable(false);

        // text field
        userField = new JTextField(15);
        JLabel userTransactionHistory = new JLabel("search user's transaction history");
        userTransactionHistory.setLabelFor(userField);

        // text area panel
        historyPanel.setLayout(new GridLayout(2,3));
        historyPanel.add(new JScrollPane(historyArea));
        historyPanel.add(new JScrollPane(buyHistoryArea));
        historyPanel.add(new JScrollPane(sellHistoryArea));
        historyPanel.add(new JScrollPane(clientsArea));
        historyPanel.add(new JScrollPane(userHistory));
//        historyPanel.add(new JScrollPane(sellers));

        // button panel
        buttonPanel.setPreferredSize(new Dimension(600, 100));
        buttonPanel.setLayout(new GridLayout(2,4));
        buttonPanel.add(showHistory);
//        buttonPanel.add(buyHistory);
//        buttonPanel.add(sellHistory);
        buttonPanel.add(inventory);
        buttonPanel.add(userTransactionHistory);
        buttonPanel.add(userField);
        buttonPanel.add(searchUser);

        // client panel
//        clientPanel.add(new JScrollPane(buyers), BorderLayout.CENTER);
//        clientPanel.add(new JScrollPane(sellers), BorderLayout.CENTER);

        //mainPanel.add(buttonPanel);
        mainPanel.add(historyPanel, BorderLayout.WEST);
        mainPanel.add(buttonPanel, BorderLayout.EAST);
        //mainPanel.add(clientPanel, BorderLayout.WEST);
        add(mainPanel, BorderLayout.CENTER);
    }

    private class ButtonHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e){

            try {

                if (e.getSource() == showHistory){

                    historyArea.setText("");
                    buyHistoryArea.setText("");
                    sellHistoryArea.setText("");
                    String sql = "SELECT * FROM engr_class019.transactionHistory";
                    mySQL.connectToDataBase(sql);

                    String user = "";
                    String item = "";
                    int numItems = 0;
                    double price = 0;
                    String type = "";

                    while (resultSet.next()){

                        user = resultSet.getString("username");
                        item = resultSet.getString("item");
                        numItems = resultSet.getInt("numitems");
                        price = resultSet.getDouble("price");
                        type = resultSet.getString("type");

                        historyArea.append("user " + user + " " + type + " " + numItems + " " + item + " at a price of " + price);

                        if (type.equals("buy")) {
                            buyHistoryArea.append("user " + user + " " + type + " " + numItems + " " + item + " at a price of " + price);
                        }
                        else if (type.equals("sell")) {
                            sellHistoryArea.append("user " + user + " " + type + " " + numItems + " " + item + " at a price of " + price);
                        }
                    }
                    //resultSet.close();

                    System.out.println("show history...");

                }
                else if (e.getSource() == searchUser){

                    String userInput = userField.getText();
                    userHistory.setText("");
                    String sql = "SELECT * FROM engr_class019.transactionHistory";
                    mySQL.connectToDataBase(sql);

                    String user = "";
                    String item = "";
                    int numItems = 0;
                    double price = 0;
                    String type = "";
                    boolean found = false;

                    while (resultSet.next()){

                        user = resultSet.getString("username");
                        if (userInput.equals(user)){

                            found = true;
                            item = resultSet.getString("item");
                            numItems = resultSet.getInt("numitems");
                            price = resultSet.getDouble("price");
                            type = resultSet.getString("type");

                            userHistory.append("user " + user + " " + type + " " + numItems + " " + item + " at a price of " + price);
                        }
                    }

                    if (!found) {
                        JOptionPane.showMessageDialog(ServerGUIDB.this,
                                "User is not registered.");
                    }

                    System.out.println("show user history...");

                }
                else if (e.getSource() == inventory){

                    inventoryArea.setText("");
                    String sql = "SELECT * FROM engr_class019.item";
                    mySQL.connectToDataBase(sql);

                    String item = "";
                    String description = "";
                    int numItems = 0;
                    double price = 0;

                    while (resultSet.next()){

                        item = resultSet.getString("itemname");
                        description = resultSet.getString("description");
                        numItems = resultSet.getInt("numitems");
                        price = resultSet.getDouble("price");

                        userHistory.append(item + "\n" + description + "\n" + "in stock: " + numItems + "\nprice: " + price + "\n");

                    }

                    System.out.println("show user history...");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                mySQL.closeConn();
            }

        }
    }

    private class MySQL {

        public void connectToDataBase(String sqlStr) {
            try {
                // This will load the MySQL driver, each DB has its own driver
                Class.forName("com.mysql.cj.jdbc.Driver");
                System.out.println("open connection...");
                // Setup the connection with the DB
                connection = DriverManager.getConnection(
                        "jdbc:mysql://s-l112.engr.uiowa.edu:3306/engr_class019",
                        "engr_class019",
                        "team19");

                // Statements allow to issue SQL queries to the database
                statement = connection.createStatement();
                // Result set get the result of the SQL query
                sql = sqlStr;
                resultSet = statement.executeQuery(sql);

                System.out.println(sql);
                System.out.println(resultSet.toString());
                System.out.println("done connection...");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void closeConn()
        {
            try {
                if(connection!=null)
                {
                    connection.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public static void main(String args[]){
        ServerGUIDB serverGUIDB = new ServerGUIDB();
        serverGUIDB.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        serverGUIDB.setSize(1200, 700);
        serverGUIDB.setLocationRelativeTo(null);
        serverGUIDB.setVisible(true);
    }
}
