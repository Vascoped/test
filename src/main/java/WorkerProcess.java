import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;

public class WorkerProcess {
    
    public static void main(String[] args) throws Exception {
        
    String url = "jdbc:postgresql://localhost/localdb?user=postgres&password=qwerty911";
    Connection connection = DriverManager.getConnection(url);
    System.out.println("Connected to the PostgreSQL server successfully.");
    
    /*Connection connection = getConnection();
    System.out.println("Connected to the PostgreSQL server successfully.");*/
    
    //copyToMainSchema(connection);
    
    }
    
    private static Connection getConnection() throws URISyntaxException, SQLException {
        
    
        
        URI dbUri = new URI(System.getenv("DATABASE_URL"));

        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + dbUri.getPath();
        
        return DriverManager.getConnection(dbUrl, username, password);
    }
    
    private static void copyToMainSchema (Connection con) throws SQLException {
         
        try
        {
           PreparedStatement stmt = con.prepareStatement("call public.readsetupandcopy_proc()");
           stmt.execute();
           System.out.println("Stored Procedure executed successfully");
        }
        catch(Exception err)
        {
           System.out.println("An error has occurred.");
           System.out.println("See full details below.");
           err.printStackTrace();
        }
    }
   
}
  
