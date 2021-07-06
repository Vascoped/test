import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;

public class WorkerProcess {
    
    public static void main(String[] args) throws Exception {
        
        Connection connection = getConnection();
        
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
        String dbUrl = "jdbc:postgres://djyfywgcyljccc:1aefd385de78d48a6962515308d9f7b3320a9e996e54082dc32510e141d59ca9@ec2-54-217-195-234.eu-west-1.compute.amazonaws.com:5432/ddc19nf5cgufb";

        return DriverManager.getConnection(dbUrl, username, password);
    }

}
