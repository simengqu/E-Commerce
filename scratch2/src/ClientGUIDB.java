import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

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
    private HashMap<String, Integer> cartList = new HashMap<>();

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

        //Sets up the display window
        displayItems(mainPanel.displayArea);

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
        JTextArea cartArea;
        JButton searchButton;
        JButton buyButton;
        JButton sellButton;
        JButton addToCart;
        JButton logoutButton;

        public TransactionPanel() {

            setLayout(new BorderLayout(30, 30));
            setBorder(BorderFactory.createEmptyBorder(30,30,30,30));

            //Framing
            JPanel centerPanel = new JPanel(new GridLayout(2,1));
            centerPanel.setBorder(BorderFactory.createEmptyBorder(30,30,30,30));

            //Button Panels
            JPanel purchasePanel = new JPanel(new GridLayout(5,1));
            JPanel sellPanel = new JPanel(new GridLayout(5,1));

            //Purchase List
            textArea = new JTextArea();
            textArea.setEditable(false);
            textArea.setPreferredSize(new Dimension(300, 400));

            //Items Bought Panel
            JPanel itemBoughtPanel = new JPanel(new BorderLayout(0, 5));
            JPanel textBoxPanel = new JPanel(new GridLayout(1,2));
            JPanel labelPanel = new JPanel(new GridLayout(1,2));

            //Items Bought Area
            JLabel itemBoughtLabel = new JLabel("Transaction Information");
            itemBoughtArea = new JTextArea();
            itemBoughtArea.setEditable(false);
            itemBoughtArea.setPreferredSize(new Dimension(300, 100));

            //Cart Area
            JLabel cartLabel = new JLabel("Current Cart");
            cartArea = new JTextArea();
            cartArea.setEditable(false);
            cartArea.setPreferredSize(new Dimension(300, 100));


            //Buy area
            JPanel buyButtonPanel = new JPanel(new GridLayout(1,2));
            itemToBuy = new JTextField(20);
            JLabel buyLabel = new JLabel("Enter product to buy");
            buyLabel.setLabelFor(itemToBuy);
            itemToBuy.setPreferredSize(new Dimension(50, 20));

            //Sell area
            itemToSell = new JTextField(20);
            JLabel sellLabel = new JLabel("Enter product to sell");
            sellLabel.setLabelFor(itemToSell);
            itemToSell.setPreferredSize(new Dimension(50, 20));
            JPanel logoutPanel = new JPanel(new BorderLayout());

            //Button Handling
            ButtonHandler buttonHandler = new ButtonHandler();
            buyButton = new JButton("Purchase Items in Cart");
            buyButton.addActionListener(buttonHandler);
            searchButton = new JButton("Search");
            searchButton.addActionListener(buttonHandler);
            sellButton = new JButton("Sell");
            sellButton.addActionListener(buttonHandler);
            addToCart = new JButton("Add To Cart");
            addToCart.addActionListener(buttonHandler);
            logoutButton = new JButton("LOGOUT");
            logoutButton.addActionListener(buttonHandler);

            //Adding west panel
            JPanel westPanel = new JPanel(new BorderLayout());
            add(westPanel, BorderLayout.WEST);
            westPanel.add(new JScrollPane(textArea), BorderLayout.CENTER);
            westPanel.add(new JLabel("Current Item Inventory"), BorderLayout.NORTH);

            //Adding items bought together
            add(itemBoughtPanel, BorderLayout.NORTH);
            itemBoughtPanel.add(labelPanel, BorderLayout.NORTH);
            itemBoughtPanel.add(textBoxPanel, BorderLayout.CENTER);

            //Label Panel and Text box Area
            labelPanel.add(itemBoughtLabel); labelPanel.add(cartLabel);
            textBoxPanel.add(new JScrollPane(itemBoughtArea)); textBoxPanel.add(new JScrollPane(cartArea));


            //Central Panel look
            add(centerPanel, BorderLayout.CENTER);
            centerPanel.add(purchasePanel);
            centerPanel.add(sellPanel);

            //Purchase Panel
            purchasePanel.add(buyLabel);
            purchasePanel.add(itemToBuy);
            purchasePanel.add(buyButtonPanel);
            purchasePanel.add(buyButton);
            buyButtonPanel.add(searchButton);
            buyButtonPanel.add(addToCart);

            //Sell Panel
            sellPanel.add(sellLabel);
            sellPanel.add(itemToSell);
            sellPanel.add(sellButton);
            sellPanel.add(new JPanel());
            sellPanel.add(logoutPanel);
            logoutPanel.add(logoutButton, BorderLayout.EAST);

            setVisible(false);
        }

    }

    /*
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
            textArea.setText("");
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
                else if (e.getSource() == transactionPanel.searchButton || e.getSource() == transactionPanel.sellButton
                        || e.getSource() == transactionPanel.buyButton || e.getSource() == transactionPanel.addToCart){

                    boolean itemInStock = false;
                    String itemName;

                    // in item table, itemname is unique
                    if(e.getSource() == transactionPanel.sellButton) {
                        itemName = transactionPanel.itemToSell.getText();
                    }
                    else  itemName = transactionPanel.itemToBuy.getText();

                    String sql = "SELECT * FROM engr_class019.item where (itemname = '"+itemName+"')";
                    mySQL.connectToDataBase(sql);
                    String description = "";
                    int numItems = 0;
                    double price = 0;

                    //Find the requested item
                    while (resultSet.next()){
                        if (itemName.toLowerCase().equals(resultSet.getString("itemname").toLowerCase())) {
                            itemInStock = true;
                            itemName = resultSet.getString("itemname");
                            description = resultSet.getString("description");
                            price = resultSet.getDouble("price");
                            numItems = resultSet.getInt("numitems");
                        }

                    }

                    // item in the table
                    if (itemInStock) {
                        transactionPanel.itemBoughtArea.setText("");
                        transactionPanel.itemBoughtArea.append(itemName + "\n" + description + "\n" +
                                "in stock: " + numItems + "\nprice: " + String.valueOf(price));

                        if(e.getSource() == transactionPanel.sellButton) sellItem(itemName, 1);
                        else if(e.getSource() == transactionPanel.addToCart) updateCart(itemName, price);
                        else if(e.getSource() == transactionPanel.buyButton){ buyItems(); }
                    }
                    else{
                        JOptionPane.showMessageDialog(ClientGUIDB.this,
                                "Item not found.");
                    }
                }
                else if(e.getSource() == transactionPanel.logoutButton) logoutUser();

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

        //Swaps client back to main panel and resets their permission
        private void logoutUser(){
            type = "";
            cartList.clear();
            swapPanel(mainPanel, transactionPanel);
        }

    }


    //Update the cartList and the displayed cart
    public void updateCart(String itemName, double price){


        //Check if user is allow to purchase this item
        if(type.equals("buyer") || type.equals("both")) {
            try {
                transactionPanel.cartArea.append(itemName + "\tprice: " + String.valueOf(price) +"\n");
                if(cartList.containsKey(itemName)){
                    Integer i = cartList.get(itemName);
                    cartList.replace(itemName, i+1);
                }
                else cartList.put(itemName, 1);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        else {
            JOptionPane.showMessageDialog(ClientGUIDB.this,
                    "You are not allowed to purchase.");
        }
    }


    public void buyItems(){

        String description = "";
        double price = 0;
        int itemLeft = 0;
        boolean found = false;

        //Checks if this client can purchase items
        if(type.equals("buyer") || type.equals("both")) {
            //Checks if there are items in cart
            if(transactionPanel.cartArea.getText().equals("")){
                JOptionPane.showMessageDialog(ClientGUIDB.this,
                        "Your cart is empty.");
            }
            else{
                //Handles exception caused by connectToDataBase()
                try {
                    for (String currentItem : cartList.keySet()) {
                        String sql = "SELECT * FROM engr_class019.item where (itemname = '" + currentItem + "')";
                        mySQL.connectToDataBase(sql);

                        //Loops through the list of items to display the item information
                        while (resultSet.next()) {
                            if (currentItem.toLowerCase().equals(resultSet.getString("itemname").toLowerCase())) {
                                found = true;
                                itemLeft = resultSet.getInt("numitems");
                            }

                        }

                        if (found) {
                            //Update the database with the number of items purchased
                            itemLeft -= cartList.get(currentItem);

                            //Yes, this has the potential to pull up multiple error messages
                            if (itemLeft < 0) {
                                JOptionPane.showMessageDialog(ClientGUIDB.this.transactionPanel,
                                        "Not enough " + currentItem + " in stock.");
                            } else {
                                sql = "update item set numitems=" + itemLeft + " where (itemname='" + currentItem + "')";
                                statement.executeUpdate(sql);
                            }

                        } else {
                            System.out.println("Not found: " + currentItem);
                        }
                        found = false;

                    }

                    //Clear cart after purchasing
                    cartList.clear();
                    transactionPanel.cartArea.setText("");

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
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
//REMOVE        clientGUIDB.displayItems(clientGUIDB.mainPanel.displayArea);
        clientGUIDB.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        clientGUIDB.setSize(1200, 700);
        clientGUIDB.pack();
        clientGUIDB.setLocationRelativeTo(null);
        clientGUIDB.setVisible(true);
    }
}
