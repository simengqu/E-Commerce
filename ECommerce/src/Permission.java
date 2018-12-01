/**
 * Types of permission levels
 */
enum UserType{BUYER, SELLER, BOTH}

/**
 * Class containing permission level for clients
 */
public class Permission {
    /**
     * User's assigned permission level
     */
   private UserType permissionLevel;

    /**
     * Constructor for UserType
     * @param type permission level
     */
   Permission(UserType type){
       permissionLevel = type;
   }

    /**
     * Constrcutor using Strings
     * @param type permission level in string format
     */
   Permission(String type){
       switch(type){
           case "BUYER": permissionLevel = UserType.BUYER; break;
           case "SELLER": permissionLevel = UserType.SELLER; break;
           case "BOTH": permissionLevel = UserType.BOTH; break;
           default: permissionLevel = UserType.BUYER; break;
       }
   }


   public UserType getPermissionLevel() {
       return permissionLevel;
   }

    /**
     * Returns a string format of the permission level
     * @return permission level as string
     */
   public String getPemissionString(){
       switch (permissionLevel){
           case BUYER: return "BUYER";
           case SELLER: return "SELLER";
           case BOTH: return "BOTH";
           default: return "Error";
       }
   }
}
