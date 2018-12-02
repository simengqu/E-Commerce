import java.util.HashMap;

public class User {

    private String userName;
    private String userPassword;
    private int userID;
    private HashMap<String, Integer> itemsToSell;
    private HashMap<String, Integer> itemsToBuy;

    public User(String userName, String userPassword, int userID){
        this.userName = userName;
        this.userPassword = userPassword;
        this.userID = userID;
        itemsToSell = new HashMap<>();
        itemsToBuy = new HashMap<>();
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

    public int getUserID(){
        return userID;
    }

    public void setUserID(int userID){
        this.userID = userID;
    }

    public HashMap<String, Integer> getItemsToSell(){
        return itemsToSell;
    }

    public void setItemToSell(String itemName, Integer numItems){

        if (!itemsToSell.containsKey(itemName)){
            itemsToSell.put(itemName, numItems);
        }
        else {
            itemsToSell.replace(itemName, itemsToSell.get(itemName) + numItems);
        }
    }

    public HashMap<String, Integer> getItemsToBuy(){
        return itemsToBuy;
    }

    public void setItemsToBuy(String itemName, Integer numItems){

        if (!itemsToBuy.containsKey(itemName)){
            itemsToBuy.put(itemName, numItems);
        }
        else {
            itemsToBuy.replace(itemName, itemsToBuy.get(itemName) + numItems);
        }
    }

}
