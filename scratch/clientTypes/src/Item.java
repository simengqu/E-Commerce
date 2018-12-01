import java.util.HashMap;
import java.util.Map;

public class Item {

    private String itemName;
    private String description;
    private HashMap<String, Integer> sellers;
    private HashMap<String, Integer> buyers;


    public Item(String itemName, String description) {
        this.itemName = itemName;
        this.description = description;
        sellers = new HashMap<>();
        buyers = new HashMap<>();

    }

    /**
     * add new seller of the item
     * @param seller    seller id
     * @param numItems  number of items to sell
     */
    public void addSeller(String seller, Integer numItems){

        if (sellers.containsKey(seller)){
            sellers.replace(seller, sellers.get(seller) + numItems);
            System.out.println("adding seller " + seller + " to item " + itemName + " with " + numItems + " items more");
        }
        else {
            sellers.put(seller, numItems);
            System.out.println("adding seller " + seller + " to item " + itemName + " with " + numItems + "items");
        }
    }

    /**
     * add new buyer of the item
     * @param buyer     buyer id
     * @param numItems  number of items to buy
     */
    public void addBuyer(String buyer, Integer numItems){

        if (buyers.containsKey(buyer)){
            buyers.replace(buyer, buyers.get(buyer) + numItems);
            System.out.println("buyer " + buyer + " want to buy item " + numItems + " more " + itemName);
        }
        else {
            buyers.put(buyer, numItems);
            System.out.println("buyer " + buyer + " want to buy item " + numItems + " " + itemName);
        }

    }

    /**
     * clear buyer's cart
     * @param buyer buyer id
     */
    public void clearCart(String buyer){

        if (buyers.containsKey(buyer)) {
            buyers.remove(buyer);
        }
        else {
            System.out.println("no items");
        }
    }

    /**
     * change seller's inventory
     * @param seller    seller id
     * @param numItems  number of items added/sold
     */
    public void changeInventory(String seller, Integer numItems){

        if (sellers.containsKey(seller)){
            sellers.replace(seller, sellers.get(seller) + numItems);
        }
    }

    /**
     * get current inventory of the item
     * @param seller    seller id
     * @return          number of items left
     */
    public Integer getCurrentInventory(String seller){
        if (sellers.containsKey(seller)){
            return sellers.get(seller);
        }
        else return 0;
    }

    public String getDescription(){
        return description;
    }

    public String getItemName(){
        return itemName;
    }

    public String getSellers(){

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Available Sellers: \n");
        for (Map.Entry<String, Integer> entry : sellers.entrySet()){
            stringBuilder.append(entry.getKey() + ": " + entry.getValue() + "\n");
        }

        return stringBuilder.toString();
    }

    public String getBuyers(){

        StringBuilder stringBuilder = new StringBuilder();

        for (Map.Entry<String, Integer> entry : buyers.entrySet()){
            stringBuilder.append(entry.getKey() + ": " + entry.getValue() + "\n");
        }

        return stringBuilder.toString();
    }
}
