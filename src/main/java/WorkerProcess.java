import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WorkerProcess {
    
    public static void main(String[] args) throws Exception {
        
        Connection connection = getConnection();
        ResultSet setupTable = ReadSetupTable(connection);
        
        while (setupTable.next()) {
            
                System.out.print(setupTable.getInt(1));
                System.out.print(": ");
                System.out.println(setupTable.getString(2));
            }
        
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("DROP TABLE IF EXISTS ticks");
        stmt.executeUpdate("CREATE TABLE ticks (tick timestamp)");
        stmt.executeUpdate("INSERT INTO ticks VALUES (now())");
        ResultSet rs = stmt.executeQuery("SELECT tick FROM ticks");
        while (rs.next()) {
            System.out.println("Read from DB: " + rs.getTimestamp("tick"));
        }
    }
    
    private static Connection getConnection() throws URISyntaxException, SQLException {
        URI dbUri = new URI(System.getenv("DATABASE_URL"));

        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + dbUri.getPath();

        return DriverManager.getConnection(dbUrl, username, password);
    }
    
    private static ResultSet ReadSetupTable (Connection connection) throws SQLException{
        
        PreparedStatement pst = connection.prepareStatement("SELECT * FROM ticks");
        ResultSet rs = pst.executeQuery();
            
       return rs;    
    }

}
