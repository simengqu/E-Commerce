package ClientTypes;

public class Client {

    private Users modifier;

    Client(int type){
        switch (type){
            case 1: modifier = Users.SELLER; break;
            case 2: modifier = Users.BUYER; break;
            case 3: modifier = Users.BOTH; break;
        }
    }

}
