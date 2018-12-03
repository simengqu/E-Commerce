import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;
import java.util.HashMap;

public class ClientGUIDB extends JFrame {

    private MainPanel mainPanel; //Main Viewer Panel
    private RegisterPanel registerPanel; //User Registration Panel
    private LogInPanel logInPanel;//Login Panel
    private TransactionPanel transactionPanel;//Main Transaction Panel
    private SellPanel sellPanel;//Adding to inventory panel
    private MySQL mySQL;//Access to mySQL database
    private Connection connection = null;//Database connection
    private Statement statement = null;//Database statement
    private String sql;//sql command string
    private ResultSet resultSet;//current table line
    private String type = ""; //Type of user permission
    private String username = "~~~"; //User
    private HashMap<String, Integer> cartList = new HashMap<>(); //List of items in the cart
    private StringBuilder stringBuilder;
    private boolean purchased = false;

    public ClientGUIDB(){

        // database
        mySQL = new MySQL();
        stringBuilder  = new StringBuilder();

        // main GUI, only display once at the beginning, contains list of items
        mainPanel = new MainPanel();

        // registration, has username and password fields, will check if username is valid
        registerPanel = new RegisterPanel();

        // login, has username and password, will check if valid
        logInPanel = new LogInPanel();

        // sell, will only appear if there is a new item to be added
        sellPanel = new SellPanel();

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
        JButton back = new JButton("Back");

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
            back.addActionListener(buttonHandler);
            back.setMaximumSize(buttonSize);
            back.setPreferredSize(buttonSize);

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
            botPanel.add(Box.createRigidArea(new Dimension(15, 30)));
            botPanel.add(back);
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
        JButton back = new JButton("Back");

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
            back.addActionListener(buttonHandler);
            back.setSize(200,50);

            add(topBox, BorderLayout.NORTH);
            topBox.add(userNameLabel);
            topBox.add(userName);
            topBox.add(Box.createRigidArea(new Dimension(15, 30)));
            topBox.add(passwordLabel);
            topBox.add(password);

            add(centerBox, BorderLayout.CENTER);
            centerBox.add(logInButton);
            centerBox.add(back);

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
        JButton clearCart;

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
            //textArea.setPreferredSize(new Dimension(300, 400));

            //Items Bought Panel
            JPanel itemBoughtPanel = new JPanel(new BorderLayout(0, 5));
            JPanel textBoxPanel = new JPanel(new GridLayout(1,2));
            JPanel labelPanel = new JPanel(new GridLayout(1,2));

            //Items Bought Area
            JLabel itemBoughtLabel = new JLabel("Transaction Information");
            itemBoughtArea = new JTextArea();
            itemBoughtArea.setEditable(false);

            //Cart Area
            JLabel cartLabel = new JLabel("Current Cart");
            cartArea = new JTextArea();
            cartArea.setEditable(false);


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
            clearCart = new JButton("Clear Cart");
            clearCart.addActionListener(buttonHandler);
            logoutButton = new JButton("LOGOUT");
            logoutButton.addActionListener(buttonHandler);

            //Adding west panel
            JPanel westPanel = new JPanel(new BorderLayout());
            westPanel.setPreferredSize(new Dimension(300, 400));
            add(westPanel, BorderLayout.WEST);
            westPanel.add(new JScrollPane(textArea), BorderLayout.CENTER);
            westPanel.add(new JLabel("Current Item Inventory"), BorderLayout.NORTH);

            //Adding items bought together
            add(itemBoughtPanel, BorderLayout.NORTH);
            itemBoughtPanel.add(labelPanel, BorderLayout.NORTH);
            itemBoughtPanel.add(textBoxPanel, BorderLayout.CENTER);

            //Label Panel and Text box Area
            labelPanel.add(itemBoughtLabel); labelPanel.add(cartLabel);
            textBoxPanel.setPreferredSize(new Dimension(300, 100));
            textBoxPanel.add(new JScrollPane(itemBoughtArea));
            textBoxPanel.add(new JScrollPane(cartArea));


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
            buyButtonPanel.add(clearCart);

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

    //Panel to facilitate
    private class SellPanel extends JPanel{
        JTextArea nameArea;
        JTextField descriptionField, priceField, unitField;
        JButton addItem;

        SellPanel(){
            //Create all Panels and setup layout
            setLayout(new BorderLayout(20,20));
            JPanel centerPanel = new JPanel(new GridLayout(4,2));
            centerPanel.setPreferredSize(new Dimension(600,400));
            JPanel southPanel = new JPanel();
            southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.LINE_AXIS));
            add(centerPanel, BorderLayout.CENTER);
            add(southPanel, BorderLayout.SOUTH);


            //Create text area
            nameArea = new JTextArea();
            nameArea.setEditable(false);
            descriptionField = new JTextField();
            priceField = new JTextField();
            unitField = new JTextField();
            centerPanel.add(new JLabel("Name: ")); centerPanel.add(nameArea);
            centerPanel.add(new JLabel("Description: ")); centerPanel.add(descriptionField);
            centerPanel.add(new JLabel("Price: ")); centerPanel.add(priceField);
            centerPanel.add(new JLabel("Unit Count: ")); centerPanel.add(unitField);

            //Create button place
            addItem = new JButton("Add Item");
            addItem.addActionListener(new ButtonHandler());
            southPanel.add(Box.createHorizontalGlue());
            southPanel.add(addItem);
            southPanel.add(Box.createHorizontalGlue());

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
            resultSet = statement.executeQuery(sql);
            textArea.setText("");
            while (resultSet.next()){

                textArea.append(resultSet.getString("itemname") + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mySQL.closeConn();
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

                    logInPanel.password.setText("");
                    logInPanel.userName.setText("");
                    // remove main panel, set it invisible, add log in panel
                    swapPanel(logInPanel, mainPanel);

                }
                else if (e.getSource() == logInPanel.back){
                    logInPanel.password.setText("");
                    logInPanel.userName.setText("");
                    swapPanel(mainPanel, logInPanel);
                }
                else if (e.getSource() == registerPanel.back) {
                    registerPanel.password.setText("");
                    registerPanel.userName.setText("");
                    swapPanel(mainPanel, registerPanel);
                }
                // Logic of Register
                else if ( e.getSource() == registerPanel.getSeller() ||
                        e.getSource() == registerPanel.getBuyer() ||
                        e.getSource() == registerPanel.getBoth()){

                    // register new user
                    boolean registered = false;

                    // get input user name
                    username = registerPanel.userName.getText();

                    // inquiry database
                    String sql = "SELECT * FROM engr_class019.registration where (username = '"+username+"')";
                    mySQL.connectToDataBase(sql);
                    resultSet = statement.executeQuery(sql);
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

                        //TODO: TEST
                        try {
                            sql = "insert into registration" +
                                    "(username, password, type, connection) " +
                                    "values('" + username + "', '" + password + "', '" + type + "', 'yes')";
                            statement.executeUpdate(sql);
                        }
                        catch (Exception ex){ex.printStackTrace();}

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

                    registerPanel.password.setText("");
                    registerPanel.userName.setText("");

                }
                //Logic of login
                else if (e.getSource() == logInPanel.getLogInButton()) {

                    boolean logIn = false;

                    username = logInPanel.userName.getText();
                    String password = String.valueOf(logInPanel.password.getPassword());
                    String sql = "SELECT * FROM engr_class019.registration where (username = '"+username+"')";
                    mySQL.connectToDataBase(sql);
                    resultSet = statement.executeQuery(sql);
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
                        try {
                            sql = "update registration set connection='yes' where (username='" + username + "')";
                            mySQL.connectToDataBase(sql);
                            statement.executeUpdate(sql);
                        }
                        catch (Exception ex){ex.printStackTrace();}

                    }
                    else {
                        JOptionPane.showMessageDialog(ClientGUIDB.this,
                                "Username does not exist/wrong password.");
                    }

                }
                else if (e.getSource() == transactionPanel.clearCart) {
                    transactionPanel.cartArea.setText("");
                    cartList.clear();
                }
                //Logic of transaction panel
                else if (e.getSource() == transactionPanel.searchButton || e.getSource() == transactionPanel.sellButton
                        || e.getSource() == transactionPanel.addToCart){

                    boolean itemInStock = false;
                    String itemName;

                    // in item table, itemname is unique
                    if(e.getSource() == transactionPanel.sellButton) {
                        itemName = transactionPanel.itemToSell.getText();
                    }
                    else  itemName = transactionPanel.itemToBuy.getText();

                    String sql = "SELECT * FROM engr_class019.item where (itemname = '"+itemName+"')";
                    mySQL.connectToDataBase(sql);
                    resultSet = statement.executeQuery(sql);
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

                        if(e.getSource() == transactionPanel.addToCart) updateCart(itemName, price);
                    }
                    else{
                        if (e.getSource() != transactionPanel.sellButton){
                            JOptionPane.showMessageDialog(ClientGUIDB.this,
                                    "Item not found.");
                        }
                    }

                    if(e.getSource() == transactionPanel.sellButton) {
                        sellItem(itemName, 1);
                    }

                }
                else if(e.getSource() == transactionPanel.buyButton){

                    stringBuilder.append("You bought:\n");
                    buyItemsInCart();
                    if (purchased){
                        JOptionPane.showMessageDialog(ClientGUIDB.this.transactionPanel,
                                stringBuilder.toString());

                        transactionPanel.itemBoughtArea.setText("");
                        String sql = "SELECT * FROM engr_class019.transactionHistory where (username = '"+username+"')";
                        mySQL.connectToDataBase(sql);
                        resultSet = statement.executeQuery(sql);

                        String user = "";
                        String item = "";
                        int numItems = 0;
                        double price = 0;

                        while (resultSet.next()){

                            user = resultSet.getString("username");
                            item = resultSet.getString("item");
                            numItems = resultSet.getInt("numitems");
                            price = resultSet.getDouble("price");
                            String type = resultSet.getString("type");

                            if (type.equals("buy")) {
                                transactionPanel.itemBoughtArea.append(user + " bought " + numItems + " " + item + " at a price of " + price + "\n");
                            }
                        }

                    }

                    purchased = false;
                    stringBuilder.setLength(0);

                }
                else if(e.getSource() == transactionPanel.logoutButton) logoutUser();
                else if (e.getSource() == sellPanel.addItem){
                    addItemToSell();
                    swapPanel(transactionPanel, sellPanel);
                    displayItems(transactionPanel.textArea);
                    sellPanel.priceField.setText("");
                    sellPanel.unitField.setText("");
                    sellPanel.descriptionField.setText("");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                mySQL.closeConn();
            }
        }

        //Checks that the fields don't have words
        private boolean checkFieldsSell(){
            return !(sellPanel.unitField.getText().matches("\\w+")
                    || sellPanel.priceField.getText().matches("\\w+"));
        }



    }

    //Swaps client back to main panel, disconnects user, resets their permission, and resets cart
    public void logoutUser(){
        try {
            String sql = "update registration set connection='no' where (username='" + username + "')";
            mySQL.connectToDataBase(sql);
            statement.executeUpdate(sql);
        }
        catch (Exception ex){ex.printStackTrace();}
        finally {
            mySQL.closeConn();
        }
        type = "";
        username = "";
        cartList.clear();
        transactionPanel.cartArea.setText("");
        swapPanel(mainPanel, transactionPanel);
    }

    //Function to swap active panels to change pages
    private void swapPanel(JPanel addPanel, JPanel removePanel){
        // remove main panel, set it invisible, add register panel
        removePanel.setVisible(false);
        remove(removePanel);
        add(addPanel);
        addPanel.setVisible(true);
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
            } finally {
                mySQL.closeConn();
            }
        }
        else {
            JOptionPane.showMessageDialog(ClientGUIDB.this,
                    "You are not allowed to purchase.");
        }
    }

    //Purchases all items in the cart
    public void buyItemsInCart(){

        int itemLeft = 0;
        boolean found = false;
        double price = 0.0;

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

                    double totalPrice = 0;
                    for (String currentItem : cartList.keySet()) {
                        String sql = "SELECT * FROM engr_class019.item where (itemname = '" + currentItem + "')";
                        mySQL.connectToDataBase(sql);
                        resultSet = statement.executeQuery(sql);

                        //Loops through the list of items to display the item information
                        while (resultSet.next()) {
                            if (currentItem.toLowerCase().equals(resultSet.getString("itemname").toLowerCase())) {
                                found = true;
                                itemLeft = resultSet.getInt("numitems");
                                price = resultSet.getDouble("price");

                            }

                        }

                        if (found) {
                            //Update the database with the number of items purchased
                            Integer transactionItem = cartList.get(currentItem);
                            itemLeft -= transactionItem;

                            //Yes, this has the potential to pull up multiple error messages
                            if (itemLeft < 0) {
                                JOptionPane.showMessageDialog(ClientGUIDB.this.transactionPanel,
                                        "Not enough " + currentItem + " in stock.");
                            } else {
                                sql = "update item set numitems=" + itemLeft + " where (itemname='" + currentItem + "')";
                                statement.executeUpdate(sql);
                                sql = "INSERT INTO transactionHistory VALUES ('"+username+"', '"+currentItem+"'," +
                                        " '"+transactionItem+"', '"+price*transactionItem+"', '"+"buy"+"')";
                                statement.executeUpdate(sql);

                                purchased = true;
                                stringBuilder.append(transactionItem + " " + currentItem + ": $" + price +
                                        "; total price: " + price*transactionItem +"\n");
                                totalPrice += price*transactionItem;
                            }

                        }
                        found = false;

                    }

                    stringBuilder.append("Total Price: " + totalPrice);
                    //Clear cart after purchasing
                    cartList.clear();
                    transactionPanel.cartArea.setText("");

                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    mySQL.closeConn();
                }
            }
        }
        else {
            JOptionPane.showMessageDialog(ClientGUIDB.this,
                    "You are not allowed to purchase.");
        }

        System.out.println(stringBuilder.toString());

    }

    //Increments or adds item to database
    public void sellItem(String itemName, int numItems){

        String sql = "SELECT * FROM engr_class019.item where (itemname = '"+itemName+"')";
        mySQL.connectToDataBase(sql);
        String description = "";
        double price = 0;
        int itemLeft = 0;
        boolean found = false;

        //Check if user is allow to purchase this item
        if(type.equals("seller") || type.equals("both")) {
            try {
                resultSet = statement.executeQuery(sql);
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

                    // transaction
                    sql = "INSERT INTO transactionHistory VALUES ('"+username+"', '"+itemName+"'," +
                            " '"+"1"+"', '"+price+"', '"+"sell"+"')";
                    statement.executeUpdate(sql);

                    transactionPanel.itemBoughtArea.setText("");
                    sql = "SELECT * FROM engr_class019.transactionHistory where (username = '"+username+"')";
                    mySQL.connectToDataBase(sql);
                    resultSet = statement.executeQuery(sql);

                    String user = "";
                    String item = "";

                    while (resultSet.next()){

                        String type = resultSet.getString("type");
                        user = resultSet.getString("username");
                        item = resultSet.getString("item");
                        numItems = resultSet.getInt("numitems");
                        price = resultSet.getDouble("price");

                        if (type.equals("sell")) {
                            transactionPanel.itemBoughtArea.append(user + " sold " + numItems + " " + item + " at a price of " + price + "\n");
                        }
                    }

                } else {
                    swapPanel(sellPanel, transactionPanel);
                    sellPanel.nameArea.setText(itemName);
                }

                transactionPanel.itemBoughtArea.setText("");
                sql = "SELECT * FROM engr_class019.transactionHistory where (username = '"+username+"')";
                mySQL.connectToDataBase(sql);
                resultSet = statement.executeQuery(sql);

                String user = "";
                String item = "";

                while (resultSet.next()){

                    user = resultSet.getString("username");
                    item = resultSet.getString("item");
                    numItems = resultSet.getInt("numitems");
                    price = resultSet.getDouble("price");
                    String type = resultSet.getString("type");

                    if (type.equals("sell")) {
                        transactionPanel.itemBoughtArea.append(user + " sold " + numItems + " " + item + " at a price of " + price + "\n");
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                mySQL.closeConn();
            }
        }
        else {
            JOptionPane.showMessageDialog(ClientGUIDB.this,
                    "You are not allowed to sell items.");
        }
    }

    //Adds an item to the database
    private void addItemToSell(){
        String itemName = sellPanel.nameArea.getText();
        String description = sellPanel.descriptionField.getText();
        Double price = Double.parseDouble(sellPanel.priceField.getText());
        Integer units = Integer.parseInt(sellPanel.unitField.getText());
        String sql;

        try {
            sql = "INSERT INTO transactionHistory VALUES ('" + username + "', '" + itemName + "'," +
                    " '" + units + "', '" + price + "', '" + "sell" + "')";

            mySQL.connectToDataBase(sql);
            statement.executeUpdate(sql);

            sql = "INSERT INTO item VALUES ('" + itemName + "', " +
                    "'" + description + "'," +
                    "'" + units + "', " +
                    "'" + price + "')";
            mySQL.connectToDataBase(sql);
            statement.executeUpdate(sql);

            // transaction
            transactionPanel.itemBoughtArea.setText("");
            sql = "SELECT * FROM engr_class019.transactionHistory where (username = '"+username+"')";
            mySQL.connectToDataBase(sql);
            resultSet = statement.executeQuery(sql);

            String user = "";
            String item = "";

            while (resultSet.next()){

                String type = resultSet.getString("type");
                user = resultSet.getString("username");
                item = resultSet.getString("item");
                price = resultSet.getDouble("price");

                if (type.equals("sell")) {
                    transactionPanel.itemBoughtArea.append(user + " sold " + units + " " + item + " at a price of " + price + "\n");
                }
            }
        }
        catch (SQLException e){e.printStackTrace();}
        finally {
            mySQL.closeConn();
        }

    }

    public class MySQL {

        public void connectToDataBase(String sqlStr) {
            try {
                // This will load the MySQL driver, each DB has its own driver
                Class.forName("com.mysql.cj.jdbc.Driver");
                // Setup the connection with the DB
                connection = DriverManager.getConnection(
                        "jdbc:mysql://s-l112.engr.uiowa.edu:3306/engr_class019",
                        "engr_class019",
                        "team19");

                // Statements allow to issue SQL queries to the database
                statement = connection.createStatement();
                // Result set get the result of the SQL query
                sql = sqlStr;


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


    public String getUsername() {
        return username;
    }

    public MySQL getMySQL() {
        return mySQL;
    }
}
