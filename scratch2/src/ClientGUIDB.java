import javax.smartcardio.Card;
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

            setLayout(new GridLayout(5,1, 10, 10)); //KEVIN
            JPanel topPanel = new JPanel();

            //Top panel look
            topPanel.setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();

            //Bot panel look
            JPanel botPanel = new JPanel();
            botPanel.setLayout(new BoxLayout(botPanel,BoxLayout.LINE_AXIS));
            botPanel.setBorder(BorderFactory.createEmptyBorder(10,45,10,45));

            JLabel userNameLabel = new JLabel("Enter user name: ");
            userNameLabel.setLabelFor(userName);
            JLabel passwordLabel = new JLabel("Enter password here: ");
            passwordLabel.setLabelFor(password);


            //Edit buttons
            ButtonHandler buttonHandler = new ButtonHandler();
            Dimension buttonSize = new Dimension(150,75);
            seller.addActionListener(buttonHandler);
            seller.setMaximumSize(buttonSize);
            seller.setSize(buttonSize);
            buyer.addActionListener(buttonHandler);
            buyer.setMaximumSize(buttonSize);
            buyer.setSize(buttonSize);
            both.addActionListener(buttonHandler);
            both.setMaximumSize(buttonSize);
            both.setSize(buttonSize);

            //Add panels
            add(topPanel);
            add(botPanel);

            //Add topPanel features
            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridx = 0; c.gridy = 0; c.insets = new Insets(10, 30, 10, 30); c.weightx = .4;
            topPanel.add(userNameLabel, c);
            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridx = 1; c.gridy = 0;
            topPanel.add(userName, c);
            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridx = 0; c.gridy = 1;
            topPanel.add(passwordLabel, c);
            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridx = 1; c.gridy = 1;
            topPanel.add(password, c);

            //Add botPanel features
            botPanel.add(seller);
            botPanel.add(Box.createRigidArea(new Dimension(15, 0)));
            botPanel.add(buyer);
            botPanel.add(Box.createRigidArea(new Dimension(15, 0)));
            botPanel.add(both);
            topPanel.setVisible(true);
            botPanel.setVisible(true);
            setLocationRelativeTo(null);
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

            setLayout(new BorderLayout(30, 30));
            setBorder(BorderFactory.createEmptyBorder(30,30,30,30));

            //Frame
            JPanel centerPanel = new JPanel(new GridLayout(2,1));
            centerPanel.setBorder(BorderFactory.createEmptyBorder(30,30,30,30));
            JPanel purchasePanel = new JPanel(new GridLayout(5,1));
            JPanel sellPanel = new JPanel(new GridLayout(5,1));

            //Purchase List
            textArea = new JTextArea();
            textArea.setEditable(false);
            textArea.setPreferredSize(new Dimension(300, 400));

            //Transaction Panel
            JPanel itemBoughtPanel = new JPanel(new BorderLayout(0, 5));
            JLabel itemBoughtLabel = new JLabel("Transaction Information");
            itemBoughtArea = new JTextArea();
            itemBoughtArea.setEditable(false);
            itemBoughtArea.setPreferredSize(new Dimension(300, 100));

            //Buy are
            itemToBuy = new JTextField(20);
            JLabel buyLabel = new JLabel("Enter product to buy");
            buyLabel.setLabelFor(itemToBuy);
            itemToBuy.setPreferredSize(new Dimension(50, 20));

            //Sell area
            itemToSell = new JTextField(20);
            JLabel sellLabel = new JLabel("Enter product to sell");
            sellLabel.setLabelFor(itemToSell);
            itemToSell.setPreferredSize(new Dimension(50, 20));

            //Button Handling
            ButtonHandler buttonHandler = new ButtonHandler();
            buyButton = new JButton("Purchase");
            buyButton.addActionListener(buttonHandler);
            sellButton = new JButton("Sell");
            sellButton.addActionListener(buttonHandler);


            //Adding items together
            add(new JScrollPane(textArea), BorderLayout.WEST);
            add(itemBoughtPanel, BorderLayout.NORTH);
            itemBoughtPanel.add(itemBoughtLabel, BorderLayout.NORTH);
            itemBoughtPanel.add(new JScrollPane(itemBoughtArea), BorderLayout.CENTER);
            add(centerPanel, BorderLayout.CENTER);
            centerPanel.add(purchasePanel);
            centerPanel.add(sellPanel);
            purchasePanel.add(buyLabel);
            purchasePanel.add(itemToBuy);
            purchasePanel.add(buyButton);
            sellPanel.add(sellLabel);
            sellPanel.add(itemToSell);
            sellPanel.add(sellButton);
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

                else if (e.getSource() == transactionPanel.buyButton){

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
                        //System.out.println(itemName);
                    }

                    // item in the table
                    if (itemInStock) {
                        transactionPanel.itemBoughtArea.setText("");
                        transactionPanel.itemBoughtArea.append(itemName + "\n" + description + "\n" +
                                "in stock: " + numItems + "\nprice: " + String.valueOf(price));
                        itemInStock = false;
                        buyItem("siqu", itemName, 1);
                    }
                    else{
                        JOptionPane.showMessageDialog(ClientGUIDB.this,
                                "Item not found.");
                    }
                }

                if (e.getSource() == transactionPanel.sellButton){

                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                mySQL.closeConn();
            }
        }

    }

    public void buyItem(String user, String itemName, int numItems){

        System.out.println(itemName);
        String sql = "SELECT * FROM engr_class019.item where (itemname = '"+itemName+"')";
        mySQL.connectToDataBase(sql);
        String description = "";
        double price = 0;
        int itemLeft = 0;
        boolean found = false;

        try {
            while (resultSet.next()){
                if (itemName.toLowerCase().equals(resultSet.getString("itemname").toLowerCase())){
                    found = true;
                    itemLeft = resultSet.getInt("numitems");
                    description = resultSet.getString("description");
                    price = resultSet.getDouble("price");
                }

            }

            if (found) {
                found = false;
                itemLeft -= numItems;
                if (itemLeft < 0){
                    JOptionPane.showMessageDialog(ClientGUIDB.this.transactionPanel,
                            "Not enough items in stock.");
                }
                else{
                    transactionPanel.itemBoughtArea.setText("");
                    transactionPanel.itemBoughtArea.append(itemName + "\n" + description + "\n" +
                            "in stock: " + itemLeft + "\nprice: " + String.valueOf(price));
                    sql = "update item set numitems="+itemLeft+" where (itemname='"+itemName+"')";
                    statement.executeUpdate(sql);
                    displayItems(transactionPanel.itemBoughtArea);
                }
            }
            else {
                System.out.println("Not found.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void sellItem(String itemName, int numItems){

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
        clientGUIDB.setSize(800, 700);
        clientGUIDB.pack();
        clientGUIDB.setLocationRelativeTo(null);
        clientGUIDB.setVisible(true);
    }
}
