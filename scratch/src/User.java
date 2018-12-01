import java.util.HashMap;

public class User {

    private String userName;
    private String userPassword;
    private HashMap<String, Integer> items;

    public User(String userName, String userPassword){
        this.userName = userName;
        this.userPassword = userPassword;
        items = new HashMap<>();
    }

    public void addItem(String itemName, Integer numItems){

        if (!items.containsKey(itemName)){
            items.put(itemName, numItems);
        }
        else {
            items.replace(itemName, items.get(itemName) + numItems);
        }
    }

    public String getUserName(){
        return userName;
    }

    public String getUserPassword(){
        return userPassword;
    }

    public void setUserPassword(String newPassword){
        this.userPassword = newPassword;
    }

    public HashMap<String, Integer> getItems(){
        return items;
    }

}
