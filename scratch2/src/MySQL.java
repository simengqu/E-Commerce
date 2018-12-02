import java.sql.*;

public class MySQL {

    private Connection connect = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;

    private String sql;




    private void close() {
        try {
            if (resultSet != null) {
                resultSet.close();
            }

            if (statement != null) {
                statement.close();
            }

            if (connect != null) {
                connect.close();
            }
        } catch (Exception e) {

        }
    }

//    public static void main(String[] args) throws Exception {
//        MySQL dao = new MySQL();
//        dao.readDataBase();
//    }
}
