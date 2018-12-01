import java.util.HashMap;

enum UserType {
    BUYER, SELLER, BOTH;
}

public class User {

    private final UserType modifier;

    private final String name;

    private final String password;

    private HashMap<String, Integer> activeItems;

    /**
     * Constructor to make a User
     * @param type type of User, buyer, seller, or both
     * @param username name
     * @param pass password
     */
    User(int type, String username, String pass){
        switch (type){
            case 1: modifier = UserType.SELLER; break;
            case 2: modifier = UserType.BUYER; break;
            case 3: modifier = UserType.BOTH; break;
            default: modifier = UserType.SELLER; break;
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

    public UserType getModifier() {
        return modifier;
    }

    //Check if the user can login to their account
    public static boolean login(String user, String pass, String correctUser, String correctPass){
        return (user.compareTo(correctUser) == 0 && pass.compareTo(correctPass) == 0);
    }

    //TODO: Create functionality to save client object in database.
}
