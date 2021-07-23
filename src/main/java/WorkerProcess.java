import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;
import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;

public class WorkerProcess {
    
    public static void main(String[] args) throws Exception {
        
    /*String url = "jdbc:postgresql://localhost/localdb?user=postgres&password=qwerty911";
    Connection connection = DriverManager.getConnection(url);
    System.out.println("Connected to the PostgreSQL server successfully.");*/
    
    /*Connection connection = getConnection();
    System.out.println("Connected to the PostgreSQL server successfully.");*/
    
    //copyToMainSchema(connection);
    
    backupToAmazonS3();
    
    }
    
    private static Connection getConnection() throws URISyntaxException, SQLException {
        
    
        
        URI dbUri = new URI(System.getenv("DATABASE_URL"));

        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + dbUri.getPath();
        
        return DriverManager.getConnection(dbUrl, username, password);
    }
    
    private static void copyToMainSchema (Connection con) throws SQLException, IOException {
         
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
           
           /*StringWriter sw = new StringWriter();
           PrintWriter pw = new PrintWriter(sw);
           err.printStackTrace(pw);
           String sStackTrace = sw.toString(); // stack trace as a string
           System.out.println(sStackTrace);
           
           sendNotificationEmail(sStackTrace);
           //err.printStackTrace();*/
        }
    }
    
    private static void sendNotificationEmail (String stackTrace) throws IOException{
        Email from = new Email("vasco.pedro@cgi.com");
        Email to = new Email("vasco.pedro@cgi.com");
        String subject = "Sending with Twilio SendGrid";
        Content content = new Content("text/plain", "It is easy");
        Mail mail = new Mail(from, subject, to, content);
        
        SendGrid sg = new SendGrid(System.getenv("SENDGRID_API_KEY"));
        Request request = new Request();
        
        try{
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
        } catch(IOException ex){
            throw ex;
        }
}
     private static void backupToAmazonS3() throws Exception {
                try {
                        String target = new String("src/main/java/pg_backup_to_s3.sh");
// String target = new String("mkdir stackOver");
                        Runtime rt = Runtime.getRuntime();
                        Process proc = rt.exec(target);
                        proc.waitFor();
                        StringBuffer output = new StringBuffer();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
                        String line = "";                       
                        while ((line = reader.readLine())!= null) {
                                output.append(line + "\n");
                        }
                        System.out.println("### " + output);
                } catch (Throwable t) {
                        t.printStackTrace();
                }
        }
}
   

  
