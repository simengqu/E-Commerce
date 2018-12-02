import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.sql.*;

public class ClientGUI extends JFrame {

    private HashMap<String, User> users;
    private HashMap<String, Item> items;
    private MainPanel mainPanel;
    private RegisterPanel registerPanel;
    private LogInPanel logInPanel;
    private TransactionPanel transactionPanel;
    private boolean inTransactionPanel = false;

    public ClientGUI(){

        users = new HashMap<>();
        items = new HashMap<>();
        items.put("MacBook", new Item("MacBook", "MacBook Pro", 1, 1000));
        items.put("iPad", new Item("iPad", "iPad 12.9", 2, 800));
        items.put("iPhone", new Item("iPhone", "iPhone XS", 3, 500));
        items.get("MacBook").addSeller("Apple", 5);
        items.get("MacBook").addSeller("Amazon", 50);
        items.get("iPad").addSeller("Apple", 5);
        items.get("iPad").addSeller("Amazon", 50);
        items.get("iPhone").addSeller("Apple", 5);
        items.get("iPhone").addSeller("Amazon", 50);

        User user1 = new User("siqu", "12345", users.size());
        users.put(user1.getUserName(), user1);
        User user2 = new User("kevins", "54321", users.size());
        users.put(user2.getUserName(), user2);

        mainPanel = new MainPanel();
        registerPanel = new RegisterPanel();
        logInPanel = new LogInPanel();
        transactionPanel = new TransactionPanel();
        add(mainPanel);

    }

    public HashMap<String, User> getUsers() {
        return users;
    }

    public HashMap<String, Item> getItems() {
        return items;
    }

    public class MainPanel extends JPanel {

        private JTextArea displayArea;
        /*private JButton registerAsSeller;
        private JButton registerAsBuyer;
        private JButton registerAsBoth;*/
        private JButton register = new JButton("Register");
        private JButton logIn = new JButton("Log in");

        private MainPanel(){

            displayArea = new JTextArea();
            displayArea.setEditable(false);
            ButtonHandler buttonHandler = new ButtonHandler();
            register.addActionListener(buttonHandler);
            logIn.addActionListener(buttonHandler);
            /*registerAsSeller = new JButton("Register as a seller");
            registerAsSeller.addActionListener(buttonHandler);
            registerAsBuyer = new JButton("Register as a buyer");
            registerAsBuyer.addActionListener(buttonHandler);
            registerAsBoth = new JButton("Register as both");
            registerAsBoth.addActionListener(buttonHandler);*/

            for (Map.Entry<String, Item> entry : items.entrySet()){
                //displayArea.append(entry.getValue().toString() + "\n");
                displayArea.append("Product: " + entry.getValue().getItemName() + "\n");
                displayArea.append("Price: " + entry.getValue().getPrice() + "\n\n");
            }

            /*JScrollPane jScrollPane = new JScrollPane(displayArea);
            jScrollPane.setSize(400,500);
            //jScrollPane.add(displayArea);
            add(jScrollPane, BorderLayout.CENTER);*/
            add(new JScrollPane(displayArea), BorderLayout.CENTER);
            add(register);
            add(logIn);
            setVisible(true);
            /*add(registerAsSeller);
            add(registerAsBuyer);
            add(registerAsBoth);*/
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

        public void register(String userName, String password, int userID){

            if (!users.containsKey(userName)){
                User newUser = new User(userName, password, userID);
                users.put(userName, newUser);
                System.out.println("Registered new user " + newUser + " with password " + password);
            }
            else {
                JOptionPane.showMessageDialog(ClientGUI.this,
                        "Username has already exist, try another one.");
            }
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

        public void logIn(String userName, String password){

            if (!users.containsKey(userName)){
                JOptionPane.showMessageDialog(ClientGUI.this,
                        "Username has already exist, try another one.");
            }
            else {
                System.out.println("Log in success");
            }
        }
    }

    public class TransactionPanel extends JPanel {

        ArrayList<JButton> jButtons = new ArrayList<>();

        public TransactionPanel() {

            inTransactionPanel = true;
            setLayout(new FlowLayout());
            for (Map.Entry<String, Item> entry : items.entrySet()){

                ButtonHandler buttonHandler = new ButtonHandler();
                JButton temp = new JButton(entry.getValue().getItemName());
                temp.addActionListener(buttonHandler);
                jButtons.add(temp);
                add(temp);
            }

            setVisible(false);
        }


    }

    public class ButtonHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e){

            if (e.getSource() == mainPanel.getRegister()){

                mainPanel.setVisible(false);
                remove(mainPanel);
                add(registerPanel);
                registerPanel.setVisible(true);
            }
            else if (e.getSource() == mainPanel.getLogIn()){

                mainPanel.setVisible(false);
                remove(mainPanel);
                add(logInPanel);
                logInPanel.setVisible(true);
            }
            else if ( e.getSource() == registerPanel.getSeller() ||
                    e.getSource() == registerPanel.getBuyer() ||
                    e.getSource() == registerPanel.getBoth()){

                boolean registered = false;

                //TODO: Reimplement to access server information (sendData) --KEVIN
                //Add SwingUtilities.invokeLater(new Runnable) to make program wait for answer from connection?


                /*registerPanel.register(registerPanel.userName.getText(),
                        registerPanel.password.getPassword().toString(),
                        users.size());*/
                if (!users.containsKey(registerPanel.userName.getText())){
                    User newUser = new User(
                            registerPanel.userName.getText(),
                            String.valueOf(registerPanel.password.getPassword()),
                            users.size()
                    );
                    users.put(logInPanel.userName.getText(), newUser);
                    System.out.println("Registered new user " + logInPanel.userName.getText() + " with password " +
                            String.valueOf(registerPanel.password.getPassword()));
                    registered = true;
                }
                else {
                    JOptionPane.showMessageDialog(ClientGUI.this,
                            "Username has already been registered, try another one.");
                }

                /*System.out.println("Username: " + registerPanel.userName.getText() +
                        "\nPassword entered:" + String.valueOf(registerPanel.password.getPassword()) +
                        "\nCorrect password: " + users.get(registerPanel.userName.getText()).getUserPassword());*/

                if (registered){

                    remove(registerPanel);
                    registerPanel.setVisible(false);
                    add(transactionPanel);
                    transactionPanel.setVisible(true);
                }

            }
            else if (e.getSource() == logInPanel.getLogInButton()) {

                boolean logInSuccess = false;

                //TODO: Reimplement to access server information  (sendData) --KEVIN
                //Add SwingUtilities.invokeLater(new Runnable) to make program wait for answer from connection?

                boolean exist = users.containsKey(logInPanel.userName.getText());
                if (!exist){
                    JOptionPane.showMessageDialog(ClientGUI.this,
                            "Username doesn't exist.");
                }
                else if (!users.get(logInPanel.userName.getText()).getUserPassword().equals(String.valueOf(logInPanel.password.getPassword()))){
                    JOptionPane.showMessageDialog(ClientGUI.this,
                            "Wrong password, try again.");
                }
                else {
                    logInSuccess = true;
                    System.out.println("Log in success");
                }

                System.out.println("Username: " + logInPanel.userName.getText() +
                        "\nPassword entered:" + String.valueOf(logInPanel.password.getPassword()) +
                        "\nCorrect password: " + users.get(logInPanel.userName.getText()).getUserPassword());

                if (logInSuccess){
                    logInPanel.setVisible(false);
                    remove(logInPanel);
                    add(transactionPanel);
                    transactionPanel.setVisible(true);
                }

            }

            if (inTransactionPanel){

            }
        }

    }

    public static void main(String args[]){
        ClientGUI clientGUI = new ClientGUI();
        clientGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        clientGUI.setSize(800, 600);
        clientGUI.setVisible(true);
    }
}
