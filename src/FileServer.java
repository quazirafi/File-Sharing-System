
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class FileServer {

    private static ServerSocket serverSocket;
    private static Socket clientSocket = null;
    private static boolean flag=false;
    
    public static void main(String[] args) throws IOException {
        
        File f = new File(".\\");
          File[] ff = f.listFiles();
          for (int i=0;i<ff.length;++i){
              if (ff[i].getName().startsWith("download") && ff[i].getName().endsWith(".zip"))
                  ff[i].delete();
          }
          
          try {
            serverSocket = new ServerSocket(4444);
            System.out.println("Server started.");
        } catch (Exception eee) {
            System.err.println("Port already in use.");
            System.exit(1);
        }
          
          ServerGui fs = new ServerGui();
         fs.init();
          
          
          while (true) {
            try {
                clientSocket = serverSocket.accept();
                System.out.println("Accepted connection : " + clientSocket);
                
                Thread t = new Thread(new CLIENTConnection(clientSocket));

                t.start();
               

            } catch (Exception ee) {
                System.err.println("Error in connection attempt.");
            }
        }
        
    }
}