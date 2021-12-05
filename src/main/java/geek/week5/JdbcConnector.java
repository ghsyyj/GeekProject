package geek.week5;

import java.sql.*;

public class JdbcConnector {

    private final static String URL = "jdbc:mysql://localhost:3306/qianqiu";
    private final static String USER = "root";
    private final static String PASS_WORD = "root";
    private final static String DRIVER_CLASS = "com.mysql.jdbc.Driver";

    public static void main(String[] args) {
        addUser();
        selectUser();

    }

    private static  void addUser(){
        Connection connection = getConnection();
        PreparedStatement ps = null;
        try {
            connection.setAutoCommit(Boolean.FALSE);
            ps = connection.prepareStatement("insert into user values(?, ?)");
            for(int i =0 ; i<10 ; i++){
                ps.setString(1, "tom" + i);
                ps.setString(2, "12" + i);
                ps.addBatch();
            }
            ps.executeBatch();
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException sqle) {
                sqle.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                if(ps != null){
                    ps.close();
                }
                if(connection != null){
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static void selectUser(){
        Connection connection = getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement("select * from user");
            rs = ps.executeQuery();
            while(rs.next()){
                //do something
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                if(rs != null){
                    rs.close();
                }
                if(ps != null){
                    ps.close();
                }
                if(connection != null){
                    connection.close();
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    private static Connection getConnection(){

        Connection connection = null;
        try {
            //加载驱动
            Class.forName(DRIVER_CLASS);
            //创建连接
            connection = DriverManager.getConnection(URL,USER,PASS_WORD);
        } catch (Exception e){
            e.printStackTrace();
        }
        return connection;
    }

}
