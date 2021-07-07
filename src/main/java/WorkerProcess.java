import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;

public class WorkerProcess {
    
    public static void main(String[] args) throws Exception {
        
        Connection connection = getConnection();
        ResultSet rs = setupTable(connection);
        
         while (rs.next()) {
            
                System.out.print(rs.getInt(1));
                System.out.print(": ");
                System.out.println(rs.getString(2));
            }
        
        /*Statement stmt = connection.createStatement();
        stmt.executeUpdate("DROP TABLE IF EXISTS ticks");
        stmt.executeUpdate("CREATE TABLE ticks (tick timestamp)");
        stmt.executeUpdate("INSERT INTO ticks VALUES (now())");
        ResultSet rs = stmt.executeQuery("SELECT tick FROM ticks");
        while (rs.next()) {
            System.out.println("Read from DB: " + rs.getTimestamp("tick"));
        }*/
    }
    
    private static Connection getConnection() throws URISyntaxException, SQLException {
        URI dbUri = new URI(System.getenv("DATABASE_URL"));

        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + dbUri.getPath();

        return DriverManager.getConnection(dbUrl, username, password);
    }
    
    private static ResultSet setupTable (Connection con) throws SQLException {
         
        PreparedStatement pst = con.prepareStatement("SELECT * FROM ticks");
        ResultSet rs = pst.executeQuery();
        return rs;
    }
}
  
