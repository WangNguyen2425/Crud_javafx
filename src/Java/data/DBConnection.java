package Java.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static Connection connection;
    public void getDBConnection()  {
        synchronized (""){
            try {
                if(getConnection() == null || getConnection().isClosed()){
                    try {
                        // 1 load the JDBC driver
                        Class.forName("com.mysql.cj.jdbc.Driver");
                        System.out.println("Driver load");
                        // 2 connect database
                        String url = "jdbc:mysql://localhost:3306/Crud_test";
                        String username = "root";
                        String password = "@HoangHy01";
                        //3 set connection
                        setConnection(DriverManager.getConnection(url,username,password));
                        System.out.println("ok");
                    }
                    catch (SQLException e){
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }
    }

    public  Connection getConnection() {
        return connection;
    }

    public static void setConnection(Connection con){
        connection = con;
    }
    public  void closeConnection(){
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
