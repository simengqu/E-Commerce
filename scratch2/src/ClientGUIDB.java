import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class ClientGUIDB extends JFrame {

    private MainPanel mainPanel;
    private RegisterPanel registerPanel;
    private LogInPanel logInPanel;
    private TransactionPanel transactionPanel;
    private MySQL mySQL;
    private Connection connection = null;
    private Statement statement = null;
    private String sql;
    private ResultSet resultSet;

    public ClientGUIDB(){

        // database
        mySQL = new MySQL();

        // main GUI, only display once at the beginning, contains list of items
        mainPanel = new MainPanel();

        // registration, has username and password fields, will check if username is valid
        registerPanel = new RegisterPanel();

        // login, has username and password, will check if valid
        logInPanel = new LogInPanel();

        // buy/sell
        transactionPanel = new TransactionPanel();
        mainPanel.setSize(800, 600);
        add(mainPanel);

    }

    public class MainPanel extends JPanel {

        private JTextArea displayArea;
        private JButton register = new JButton("Register");
        private JButton logIn = new JButton("Log in");

        private MainPanel(){

            displayArea = new JTextArea();
            displayArea.setEditable(false);
            displayArea.setPreferredSize(new Dimension(400, 600));

            ButtonHandler buttonHandler = new ButtonHandler();
            register.addActionListener(buttonHandler);
            logIn.addActionListener(buttonHandler);

            add(new JScrollPane(displayArea), BorderLayout.CENTER);
            add(register);
            add(logIn);
            setVisible(true);
        }

        public JButton getRegister() {
            return register;
        }

        public JButton getLogIn() {
            return logIn;
        }

    }

    public class RegisterPanel extends JPanel {

        JTextField userName = new JTextField(10);
        JPasswordField password = new JPasswordField(10);
        JButton seller = new JButton("Register as seller");
        JButton buyer = new JButton("Register as buyer");
        JButton both = new JButton("Register as both");

        private RegisterPanel(){

            JLabel userNameLabel = new JLabel("Enter user name: ");
            userNameLabel.setLabelFor(userName);
            JLabel passwordLabel = new JLabel("Enter password here: ");
            passwordLabel.setLabelFor(password);

            ButtonHandler buttonHandler = new ButtonHandler();
            seller.addActionListener(buttonHandler);
            buyer.addActionListener(buttonHandler);
            both.addActionListener(buttonHandler);

            add(userNameLabel);
            add(userName);
            add(passwordLabel);
            add(password);
            add(seller);
            add(buyer);
            add(both);
            setVisible(false);
        }

        public JButton getSeller() {
            return seller;
        }

        public JButton getBuyer() {
            return buyer;
        }

        public JButton getBoth() {
            return both;
        }
    }

    public class LogInPanel extends JPanel {
        JTextField userName = new JTextField(10);
        JPasswordField password = new JPasswordField(10);
        JButton logInButton = new JButton("Log in");

        private LogInPanel(){

            JLabel userNameLabel = new JLabel("Enter user name: ");
            userNameLabel.setLabelFor(userName);
            JLabel passwordLabel = new JLabel("Enter password here: ");
            passwordLabel.setLabelFor(password);

            ButtonHandler buttonHandler = new ButtonHandler();
            logInButton.addActionListener(buttonHandler);

            add(userNameLabel);
            add(userName);
            add(passwordLabel);
            add(password);
            add(logInButton);
            setVisible(false);
        }

        public JButton getLogInButton() {
            return logInButton;
        }

    }

    public class TransactionPanel extends JPanel {

        JTextField itemToBuy;
        JTextField itemToSell;
        JTextArea textArea;
        JTextArea itemBoughtArea;
        JButton buyButton;
        JButton sellButton;

        public TransactionPanel() {

            textArea = new JTextArea();
            textArea.setEditable(false);
            textArea.setPreferredSize(new Dimension(400, 600));

            itemBoughtArea = new JTextArea();
            itemBoughtArea.setEditable(false);
            itemBoughtArea.setPreferredSize(new Dimension(400, 200));

            itemToBuy = new JTextField(20);
            JLabel buyLabel = new JLabel("Enter product to buy");
            buyLabel.setLabelFor(itemToBuy);

            itemToSell = new JTextField(20);
            JLabel sellLabel = new JLabel("Enter product to sell");
            sellLabel.setLabelFor(itemToSell);

            ButtonHandler buttonHandler = new ButtonHandler();
            buyButton = new JButton("Search");
            buyButton.addActionListener(buttonHandler);
            sellButton = new JButton("Sell");
            sellButton.addActionListener(buttonHandler);

            //setLayout(new FlowLayout());

            add(new JScrollPane(textArea), BorderLayout.WEST);
            add(new JScrollPane(itemBoughtArea), BorderLayout.NORTH);
            add(buyLabel);
            add(itemToBuy);
            add(buyButton);
            add(sellLabel);
            add(itemToSell);
            add(sellButton);
            setVisible(false);
        }

    }

    /**
     * display items in database in the JTextArea
     * @param textArea  the text area to append the items
     */
    public void displayItems(JTextArea textArea){

        // inquiry item table
        String sql = "SELECT * FROM engr_class019.item";
        // get result set
        mySQL.connectToDataBase(sql);
        // read table
        try{
            while (resultSet.next()){

                textArea.append(resultSet.getString("itemname") + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class ButtonHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e){

            try {
                // go to register panel
                if (e.getSource() == mainPanel.getRegister()){

                    // remove main panel, set it invisible, add register panel
                    mainPanel.setVisible(false);
                    remove(mainPanel);
                    add(registerPanel);
                    registerPanel.setVisible(true);
                }
                else if (e.getSource() == mainPanel.getLogIn()){

                    // remove main panel, set it invisible, add log in panel
                    mainPanel.setVisible(false);
                    remove(mainPanel);
                    add(logInPanel);
                    logInPanel.setVisible(true);
                }
                else if ( e.getSource() == registerPanel.getSeller() ||
                        e.getSource() == registerPanel.getBuyer() ||
                        e.getSource() == registerPanel.getBoth()){

                    // register new user
                    boolean registered = false;

                    // get input user name
                    String username = registerPanel.userName.getText();

                    // inquiry database
                    String sql = "SELECT * FROM engr_class019.registration where (username = '"+username+"')";
                    mySQL.connectToDataBase(sql);
                    String user;
                    String password = String.valueOf(registerPanel.password.getPassword());
                    String type = "";
                    if (e.getSource() == registerPanel.getSeller()){
                        type = "seller";
                    }
                    else if (e.getSource() == registerPanel.getBuyer()){
                        type = "buyer";
                    }
                    else if (e.getSource() == registerPanel.getBoth()){
                        type = "both";
                    }

                    while (resultSet.next()){
                        user = resultSet.getString("username");
                        // user exists
                        if (user.equals(username)){
                            registered = true;
                            break;
                        }
                    }

                    // username valid
                    if (!registered){

                        sql = "insert into registration" +
                                "(username, password, type) values('"+username+"', '"+password+"', '"+type+"')";
                        statement.executeUpdate(sql);
                        System.out.println("username: " + username + " password: " + password);
                    }
                    else {
                        JOptionPane.showMessageDialog(ClientGUIDB.this,
                                "Username has already been registered, try another one.");
                    }

                    // remove registerPanel, set it invisible, list items in text area, add transaction panel
                    if (!registered){

                        remove(registerPanel);
                        registerPanel.setVisible(false);
                        displayItems(transactionPanel.textArea);
                        add(transactionPanel);
                        transactionPanel.setVisible(true);
                    }

                }
                else if (e.getSource() == logInPanel.getLogInButton()) {

                    boolean logIn = false;

                    String username = logInPanel.userName.getText();
                    String password = String.valueOf(logInPanel.password.getPassword());
                    String sql = "SELECT * FROM engr_class019.registration where (username = '"+username+"')";
                    mySQL.connectToDataBase(sql);
                    String user;
                    String pwd;

                    while (resultSet.next()){
                        user = resultSet.getString("username");
                        pwd = resultSet.getString("password");
                        // log in success
                        if (user.equals(username) && password.equals(pwd)){
                            logIn = true;
                            break;
                        }
                    }

                    if (logIn){
                        remove(logInPanel);
                        logInPanel.setVisible(false);
                        displayItems(transactionPanel.textArea);
                        add(transactionPanel);
                        transactionPanel.setVisible(true);
                        System.out.println("username: " + username + " password: " + password);
                    }
                    else {
                        JOptionPane.showMessageDialog(ClientGUIDB.this,
                                "Username not exist/wrong password.");
                    }

                }

                if (e.getSource() == transactionPanel.buyButton){

                    boolean itemInStock = false;

                    // in item table, itemname is unique
                    String itemName = transactionPanel.itemToBuy.getText();
                    String sql = "SELECT * FROM engr_class019.item where (itemname = '"+itemName+"')";
                    mySQL.connectToDataBase(sql);
                    String description = "";
                    int numItems = 0;
                    double price = 0;

                    while (resultSet.next()){
                        itemInStock = true;
                        itemName = resultSet.getString("itemname");
                        description = resultSet.getString("description");
                        price = resultSet.getDouble("price");
                        numItems = resultSet.getInt("numitems");
                        System.out.println(itemName);
                    }

                    // item in the table
                    if (itemInStock) {
                        transactionPanel.itemBoughtArea.setText("");
                        transactionPanel.itemBoughtArea.append(itemName + "\n" + description + "\n" +
                                "in stock: " + numItems + "\nprice: " + String.valueOf(price));
                        itemInStock = false;
                    }
                    else{
                        JOptionPane.showMessageDialog(ClientGUIDB.this,
                                "Item not found.");
                    }
                }

                if (e.getSource() == transactionPanel.sellButton){

                }
            }

            catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                mySQL.closeConn();
            }
        }

    }

    public class MySQL {


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

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        // not in user for now
        public void writeToDataBase(String table) {

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
                sql = "SELECT * FROM engr_class019." + table;
                resultSet = statement.executeQuery(sql);


                // write to table
                if (table.equals("registration")) {
                    String username = registerPanel.userName.getText();
                    String password = String.valueOf(registerPanel.password.getPassword());

                    sql = "insert into " + table + "(username, password) values('"+username+"', '"+password+"')";
                    statement.executeUpdate(sql);
                } else if (table.equals("transactionHistory")) {

                } else if (table.equals("item")) {

                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                closeConn();
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
        ClientGUIDB clientGUIDB = new ClientGUIDB();
        clientGUIDB.displayItems(clientGUIDB.mainPanel.displayArea);
        clientGUIDB.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        clientGUIDB.setSize(1000, 800);
        clientGUIDB.setVisible(true);
    }
}
