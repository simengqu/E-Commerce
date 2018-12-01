import java.util.HashMap;



public class User {

    private final Permission modifier;

    private final String name;

    private final String password;

    private int userID;
    private HashMap<String, Integer> itemsToSell;
    private HashMap<String, Integer> itemsToBuy;
    /**
     * Constructor to make a User
     * @param type type of User, buyer, seller, or both
     * @param username name
     * @param pass password
     */
    User(String type, String username, String pass, int userID){
        modifier = new Permission(type);
        name = username;
        password = pass;
        this.userID = userID;
        itemsToSell = new HashMap<>();
        itemsToBuy = new HashMap<>();
    }


    public String getName() { return name; }

    public String getPassword() { return password; }

    public Permission getModifier() { return modifier; }

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

    //TODO: Create functionality to save client object in database or file.
}
