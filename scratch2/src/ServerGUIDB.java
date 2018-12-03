import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class ServerGUIDB extends JFrame {

    // database
    private MySQL mySQL;
    private Connection connection = null;
    private Statement statement = null;
    private String sql;
    private ResultSet resultSet;

    // panels
    private JPanel mainPanel;
    private JPanel buttonPanel;
    private JPanel displayPanel;
    private JPanel clientPanel;

    // buttons
    private JButton showHistory;
    private JButton buyHistory;
    private JButton sellHistory;
    private JButton searchUser;
    private JButton inventory;
    private JButton showClients;

    // text area/field
    private JTextArea displayArea;
    private JTextField userField;
    private JTextArea sellerArea;
    private JTextArea buyerArea;

    public ServerGUIDB(){

        super("Server");

        // mySQL
        mySQL = new MySQL();

        // panels
        mainPanel = new JPanel();
        buttonPanel = new JPanel();
        displayPanel = new JPanel();
        clientPanel = new JPanel();

        // buttons
        ButtonHandler buttonHandler = new ButtonHandler();
        showHistory = new JButton("show transaction history");
        showHistory.addActionListener(buttonHandler);
        buyHistory = new JButton("buy history");
        buyHistory.addActionListener(buttonHandler);
        sellHistory = new JButton("sell history");
        sellHistory.addActionListener(buttonHandler);
        searchUser = new JButton("search user");
        searchUser.addActionListener(buttonHandler);
        inventory = new JButton("Show inventory");
        inventory.addActionListener(buttonHandler);
        showClients = new JButton("Show clients");
        showClients.addActionListener(buttonHandler);

        // text areas
        displayArea = new JTextArea();
        displayArea.setPreferredSize(new Dimension(400,600));
        displayArea.setEditable(false);
        sellerArea = new JTextArea();
        sellerArea.setPreferredSize(new Dimension(300,600));
        sellerArea.setEditable(false);
        buyerArea = new JTextArea();
        buyerArea.setPreferredSize(new Dimension(300,600));
        buyerArea.setEditable(false);

        // text field
        userField = new JTextField(15);
        JLabel userTransactionHistory = new JLabel("username");
        userTransactionHistory.setLabelFor(userField);

        // text area panel
        displayPanel.setLayout(new BoxLayout(displayPanel, BoxLayout.X_AXIS));
        displayPanel.add(new JScrollPane(displayArea), BorderLayout.CENTER);

        // button panel
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.add(Box.createRigidArea(new Dimension(15, 100)));
        buttonPanel.add(showHistory);
        buttonPanel.add(buyHistory);
        buttonPanel.add(sellHistory);
        buttonPanel.add(inventory);
        buttonPanel.add(showClients);
        buttonPanel.add(Box.createRigidArea(new Dimension(15, 100)));
        buttonPanel.add(userTransactionHistory);
        buttonPanel.add(userField);
        buttonPanel.add(searchUser);
        buttonPanel.add(Box.createRigidArea(new Dimension(15, 200)));

        // client panel
        clientPanel.setPreferredSize(new Dimension(400, 400));
        clientPanel.setLayout(new BoxLayout(clientPanel, BoxLayout.X_AXIS));
        clientPanel.add(new JScrollPane(buyerArea));
        clientPanel.add(new JScrollPane(sellerArea));

        //mainPanel.add(buttonPanel);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
        mainPanel.add(Box.createRigidArea(new Dimension(50, 200)));
        mainPanel.add(displayPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(100, 200)));
        mainPanel.add(buttonPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(100, 200)));
        mainPanel.add(clientPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(50, 200)));
        JPanel temp1 = new JPanel();
        temp1.setPreferredSize(new Dimension(100, 100));
        JPanel temp2 = new JPanel();
        temp2.setPreferredSize(new Dimension(100, 100));
        add(temp1, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        add(temp2, BorderLayout.SOUTH);
    }

    private class ButtonHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e){

            try {

                if (e.getSource() == showHistory){

                    displayArea.setText("");
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

                        displayArea.append(user + " " + type + " " + numItems + " " + item + " at a price of " + price + "\n");

                    }

                    System.out.println("show history...");

                }
                else if (e.getSource() == buyHistory || e.getSource() == sellHistory){

                    displayArea.setText("");
                    String sql = "SELECT * FROM engr_class019.transactionHistory";
                    mySQL.connectToDataBase(sql);

                    String user = "";
                    String item = "";
                    int numItems = 0;
                    double price = 0;
                    String type = "";

                    if (e.getSource() == buyHistory) {

                        while (resultSet.next()){

                            user = resultSet.getString("username");
                            item = resultSet.getString("item");
                            numItems = resultSet.getInt("numitems");
                            price = resultSet.getDouble("price");
                            type = resultSet.getString("type");

                            if (type.equals("buy")) {
                                displayArea.append(user + " buy " + numItems + " " + item + " at a price of " + price + "\n");
                            }
                        }
                    }
                    else if (e.getSource() == sellHistory) {

                        while (resultSet.next()){

                            user = resultSet.getString("username");
                            item = resultSet.getString("item");
                            numItems = resultSet.getInt("numitems");
                            price = resultSet.getDouble("price");
                            type = resultSet.getString("type");

                            if (type.equals("sell")) {
                                displayArea.append(user + " sell " + numItems + " " + item + " at a price of " + price + "\n");
                            }
                        }
                    }


                    System.out.println("show history...");

                }
                else if (e.getSource() == searchUser){

                    String userInput = userField.getText();
                    displayArea.setText("");
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

                            displayArea.append(user + " " + type + " " + numItems + " " + item + " at a price of " + price + "\n");
                        }
                    }

                    if (!found) {
                        JOptionPane.showMessageDialog(ServerGUIDB.this,
                                "No transaction history.");
                    }

                    System.out.println("show user history...");

                }
                else if (e.getSource() == inventory){

                    displayArea.setText("");
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

                        displayArea.append(item + "\n" + description + "\n" + "in stock: " + numItems + "\nprice: " + price + "\n\n");

                    }

                }
                else if (e.getSource() == showClients){

                    sellerArea.setText("");
                    buyerArea.setText("");
                    String sql = "SELECT * FROM engr_class019.registration";
                    mySQL.connectToDataBase(sql);

                    String username = "";
                    String type = "";
                    String connection = "";

                    while (resultSet.next()){

                        username = resultSet.getString("username");
                        type = resultSet.getString("type");
                        connection = resultSet.getString("connection");

                        if (connection.equals("yes")){

                            if (type.equals("seller") || type.equals("both")) {
                                sellerArea.append(username + " connects.\n");
                            }
                            else if (type.equals("buyer") || type.equals("both")) {
                                buyerArea.append(username + " connects.\n");
                            }
                        }
                    }
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
