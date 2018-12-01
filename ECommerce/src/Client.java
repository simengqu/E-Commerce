import java.util.HashMap;

enum Users{
    BUYER, SELLER, BOTH;
}

public class Client {

    private final Users modifier;

    private final String name;

    private final String password;

    private HashMap<String, Integer> activeItems;

    Client(int type, String username, String pass){
        switch (type){
            case 1: modifier = Users.SELLER; break;
            case 2: modifier = Users.BUYER; break;
            case 3: modifier = Users.BOTH; break;
            default: modifier = Users.SELLER; break;
        }
        name = username;
        password = pass;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public Users getModifier() {
        return modifier;
    }
}
