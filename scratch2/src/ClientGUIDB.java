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
    private String type = "";

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
        mainPanel.setPreferredSize(new Dimension(800, 650));
        add(mainPanel);

    }

    public class MainPanel extends JPanel {

        private JTextArea displayArea;
        private JButton register = new JButton("Register");
        private JButton logIn = new JButton("Log in");

        private MainPanel(){

            setLayout(new BorderLayout());
            setBorder(BorderFactory.createEmptyBorder(10,45,10,45));

            displayArea = new JTextArea();
            displayArea.setEditable(false);
            displayArea.setPreferredSize(new Dimension(400, 600));

            JPanel buttonArea = new JPanel(new BorderLayout());
            JPanel buttonUpperArea = new JPanel(new GridLayout(2,1));
            JPanel emptyArea = new JPanel();
            emptyArea.setPreferredSize(new Dimension(200,50));

            ButtonHandler buttonHandler = new ButtonHandler();
            register.addActionListener(buttonHandler);
            logIn.addActionListener(buttonHandler);


            add(new JLabel("Welcome to The AmazonZ v0.1"), BorderLayout.NORTH);
            add(new JScrollPane(displayArea), BorderLayout.CENTER);
            add(buttonArea, BorderLayout.LINE_END);


            buttonArea.add(emptyArea, BorderLayout.CENTER);
            buttonArea.add(buttonUpperArea, BorderLayout.NORTH);
            buttonUpperArea.add(register);
            buttonUpperArea.add(logIn);
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
            //Top panel look
            JPanel topPanel = new JPanel();
            JPanel topBox = new JPanel();
            topBox.setLayout(new BoxLayout(topBox, BoxLayout.LINE_AXIS));
            topPanel.setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();

            //Bot panel look
            JPanel botPanel = new JPanel();
            botPanel.setLayout(new BoxLayout(botPanel,BoxLayout.LINE_AXIS));
            botPanel.setBorder(BorderFactory.createEmptyBorder(10,45,10,45));
            botPanel.setMaximumSize(new Dimension(300, 50));

            JLabel userNameLabel = new JLabel("Enter user name: ");
            userNameLabel.setLabelFor(userName);
            JLabel passwordLabel = new JLabel("Enter password here: ");
            passwordLabel.setLabelFor(password);


            //Edit buttons
            ButtonHandler buttonHandler = new ButtonHandler();
            Dimension buttonSize = new Dimension(150,50);
            seller.addActionListener(buttonHandler);
            seller.setMaximumSize(buttonSize);
            seller.setPreferredSize(buttonSize);
            buyer.addActionListener(buttonHandler);
            buyer.setMaximumSize(buttonSize);
            buyer.setPreferredSize(buttonSize);
            both.addActionListener(buttonHandler);
            both.setMaximumSize(buttonSize);
            both.setPreferredSize(buttonSize);

            //Add panels
            add(topBox);
            add(botPanel);

            //Add topBox features
            topBox.add(Box.createHorizontalGlue());
            topBox.add(topPanel);
            topBox.add(Box.createHorizontalGlue());

            //Add topPanel features
            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridx = 0; c.gridy = 0; c.insets = new Insets(30, 80, 10, 0); c.weightx = .3;
            topPanel.add(userNameLabel, c);

            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridx = 1; c.gridy = 0; c.insets = new Insets(30, 0, 10, 80); c.weightx = 0.7;
            c.ipady = 15;
            topPanel.add(userName, c);

            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridx = 0; c.gridy = 1; c.insets = new Insets(10, 80, 10, 0); c.weightx = .3;
            c.ipady = 0;
            topPanel.add(passwordLabel, c);

            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridx = 1; c.gridy = 1; c.insets = new Insets(10, 0, 10, 80); c.weightx = 0.7;
            c.ipady = 15;
            topPanel.add(password, c);

            //Add botPanel features
            botPanel.add(Box.createHorizontalGlue());
            botPanel.add(seller);
            botPanel.add(Box.createRigidArea(new Dimension(15, 30)));
            botPanel.add(buyer);
            botPanel.add(Box.createRigidArea(new Dimension(15, 30)));
            botPanel.add(both);
            botPanel.add(Box.createHorizontalGlue());

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
        JButton logInButton = new JButton("        LOG IN         ");

        private LogInPanel(){

            setLayout(new BorderLayout(20, 10));

            JPanel topBox = new JPanel();
            topBox.setLayout(new BoxLayout(topBox, BoxLayout.LINE_AXIS));
            topBox.setBorder(BorderFactory.createEmptyBorder(40,10,15,10));

            JLabel userNameLabel = new JLabel("Enter user name: ");
            userNameLabel.setLabelFor(userName);
            JLabel passwordLabel = new JLabel("Enter password here: ");
            passwordLabel.setLabelFor(password);

            JPanel centerBox = new JPanel(new FlowLayout());
            ButtonHandler buttonHandler = new ButtonHandler();
            logInButton.addActionListener(buttonHandler);
            logInButton.setSize(200,50);

            add(topBox, BorderLayout.NORTH);
            topBox.add(userNameLabel);
            topBox.add(userName);
            topBox.add(Box.createRigidArea(new Dimension(15, 30)));
            topBox.add(passwordLabel);
            topBox.add(password);

            add(centerBox, BorderLayout.CENTER);
            centerBox.add(logInButton);

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
                // go to register panel from Main
                if (e.getSource() == mainPanel.getRegister()){
                    // remove main panel, set it invisible, add register panel
                    swapPanel(registerPanel, mainPanel);

                }
                // go to login from Main
                else if (e.getSource() == mainPanel.getLogIn()){

                    // remove main panel, set it invisible, add log in panel
                    swapPanel(logInPanel, mainPanel);

                }
                // Logic of Register
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
                    type = "";
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

                        swapPanel(transactionPanel, registerPanel);
                        displayItems(transactionPanel.textArea);
                    }

                }
                //Logic of login
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
                            type = resultSet.getString("type");
                            logIn = true;
                            break;
                        }
                    }

                    if (logIn){
                        swapPanel(transactionPanel, logInPanel);
                        displayItems(transactionPanel.textArea);
                        System.out.println("username: " + username + " password: " + password);
                    }
                    else {
                        JOptionPane.showMessageDialog(ClientGUIDB.this,
                                "Username does not exist/wrong password.");
                    }

                }
                //Logic of transaction panel
                else if (e.getSource() == transactionPanel.buyButton || e.getSource() == transactionPanel.sellButton){

                    boolean itemInStock = false;
                    String itemName;

                    // in item table, itemname is unique
                    if(e.getSource() == transactionPanel.buyButton) {
                        itemName = transactionPanel.itemToBuy.getText();
                    }
                    else itemName = transactionPanel.itemToSell.getText();
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
                        if (e.getSource() == transactionPanel.buyButton) {
                            buyItem(itemName, 1);
                        }
                        else if(e.getSource() == transactionPanel.sellButton) sellItem(itemName, 1);
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

        //Function to swap active panels to change pages
        private void swapPanel(JPanel addPanel, JPanel removePanel){

            // remove main panel, set it invisible, add register panel
            removePanel.setVisible(false);
            remove(removePanel);
            add(addPanel);
            addPanel.setVisible(true);
        }

    }

    public void buyItem(String itemName, int numItems){

        System.out.println(itemName);
        String sql = "SELECT * FROM engr_class019.item where (itemname = '"+itemName+"')";
        mySQL.connectToDataBase(sql);
        String description = "";
        double price = 0;
        int itemLeft = 0;
        boolean found = false;

        //Check if user is allow to purchase this item
        if(type.equals("buyer") || type.equals("both")) {
            try {
                while (resultSet.next()) {
                    if (itemName.toLowerCase().equals(resultSet.getString("itemname").toLowerCase())) {
                        found = true;
                        itemLeft = resultSet.getInt("numitems");
                        description = resultSet.getString("description");
                        price = resultSet.getDouble("price");
                    }

                }

                if (found) {
                    itemLeft -= numItems;
                    if (itemLeft < 0) {
                        JOptionPane.showMessageDialog(ClientGUIDB.this.transactionPanel,
                                "Not enough items in stock.");
                    } else {
                        transactionPanel.itemBoughtArea.setText("");
                        transactionPanel.itemBoughtArea.append(itemName + "\n" + description + "\n" +
                                "in stock: " + itemLeft + "\nprice: " + String.valueOf(price));
                        sql = "update item set numitems=" + itemLeft + " where (itemname='" + itemName + "')";
                        statement.executeUpdate(sql);
                        displayItems(transactionPanel.itemBoughtArea);
                    }
                } else {
                    System.out.println("Not found.");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        else {
            JOptionPane.showMessageDialog(ClientGUIDB.this,
                    "You are not allowed to purchase.");
        }
    }

    public void sellItem(String itemName, int numItems){
        System.out.println(itemName);
        String sql = "SELECT * FROM engr_class019.item where (itemname = '"+itemName+"')";
        mySQL.connectToDataBase(sql);
        String description = "";
        double price = 0;
        int itemLeft = 0;
        boolean found = false;

        //Check if user is allow to purchase this item
        if(type.equals("seller") || type.equals("both")) {
            try {
                while (resultSet.next()) {
                    if (itemName.toLowerCase().equals(resultSet.getString("itemname").toLowerCase())) {
                        found = true;
                        itemLeft = resultSet.getInt("numitems");
                        description = resultSet.getString("description");
                        price = resultSet.getDouble("price");
                    }

                }

                if (found) {
                    itemLeft += numItems;
                    transactionPanel.itemBoughtArea.setText("");
                    transactionPanel.itemBoughtArea.append(itemName + "\n" + description + "\n" +
                            "in stock: " + itemLeft + "\nprice: " + String.valueOf(price));
                    sql = "update item set numitems=" + itemLeft + " where (itemname='" + itemName + "')";
                    statement.executeUpdate(sql);
                    displayItems(transactionPanel.itemBoughtArea);
                } else {
                    System.out.println("Not found.");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        else {
            JOptionPane.showMessageDialog(ClientGUIDB.this,
                    "You are not allowed to sell items.");
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
        clientGUIDB.setSize(1200, 700);
        clientGUIDB.pack();
        clientGUIDB.setLocationRelativeTo(null);
        clientGUIDB.setVisible(true);
    }
}
