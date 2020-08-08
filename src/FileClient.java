
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

public class FileClient extends JFrame {

    private static Socket sock;
    private static String fileName;
    private static BufferedReader stdin;

    public static ObjectInputStream objectInputStream;
    public static ObjectOutputStream objectOutputStream;

    private static PrintStream os;

    private JScrollPane jsp;
    private JTextField userText;
    private JTextArea chatWindow;
    private String message = "";

    
    public ArrayList<String> fileNames;
    public File[] listOfFiles;
    
    static FileClient fc;

    public void init() {
        setLayout(null);
        fileNames = null;
        userText = new JTextField();
        userText.setBounds(0, 0, 120, 30);
        userText.setEditable(true);
        userText.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent event) {

                        message = (String) event.getActionCommand();
                        sendMessage(message);
                        userText.setText("");
                    }
                }
        );
        add(userText);

        chatWindow = new JTextArea();

        jsp = new JScrollPane(chatWindow);
        jsp.setBounds(200, 200, 200, 100);

        add(jsp);

        setSize(800, 600);
        setVisible(true);
    }

    public void sendMessage(String message) {
        try {
            objectOutputStream.writeObject(message);
            objectOutputStream.flush();

        } catch (Exception e) {

        }
    }

    public static void main(String[] args) throws IOException {
        try {
            sock = new Socket("localhost", 4444);
            fc = new FileClient();
            fc.init();
            fc.setUpStreams();
            fc.whileRunning();

        } catch (Exception e) {
            System.err.println("Cannot connect to the server, try again later.");
            System.exit(1);
        }

        sock.close();
    }

    public void setUpStreams() {
        try {
            objectOutputStream = new ObjectOutputStream(sock.getOutputStream());
            objectOutputStream.flush();
            objectInputStream = new ObjectInputStream(sock.getInputStream());

        } catch (Exception e) {
            System.out.println("Inside setUpStreams Exception");
        }

    }

    public void whileRunning() {
        do {
            try {
                if (message.equals("3")) {
                    //fileNames = (ArrayList<String>) objectInputStream.readObject();
                    listOfFiles = (File[]) objectInputStream.readObject();
//                    for (String str : fileNames) {
//                        System.out.println(str);
//                    }
                    for (int i = 0; i < listOfFiles.length; i++) {
                        System.out.println(listOfFiles[i].getName());
                    }
                    message = "";
                }
                else if(message.equals("2")){
                    receiveFile();
                    message = "";
                }
                else if(message.equals("1")){
                    sendFile();
                    message = "";
                }

            } catch (Exception e) {
                System.out.println("INSIDE EXCEPTION");
            }
        } while (!message.equals("SERVER - END"));
    }

    public static String selectAction() throws IOException {
        System.out.println("1. Send file.");
        System.out.println("2. Recieve file.");
        System.out.println("3. Listing files in directory");
        System.out.print("\nMake selection: ");

        return stdin.readLine();
    }

    public static void sendFile() {
        try {
            //System.err.print("Enter file name: ");
            fileName = "F:\\Networking_Project\\File_Sharing_System-master\\FileSharingSystem3_2\\Server\\b.txt";

            File myFile = new File(fileName);
            byte[] mybytearray = new byte[(int) myFile.length()];

            FileInputStream fis = new FileInputStream(myFile);
            BufferedInputStream bis = new BufferedInputStream(fis);
            //bis.read(mybytearray, 0, mybytearray.length);

            DataInputStream dis = new DataInputStream(bis);
            dis.readFully(mybytearray, 0, mybytearray.length);

            OutputStream os = sock.getOutputStream();

            //Sending file name and file size to the server
            DataOutputStream dos = new DataOutputStream(os);
            dos.writeUTF(myFile.getName());
            dos.writeLong(mybytearray.length);
            dos.write(mybytearray, 0, mybytearray.length);
            dos.flush();
            //newly added
            os.flush();
            System.out.println("File " + fileName + " sent to Server.");
        } catch (Exception e) {
            System.err.println("File does not exist!");
        }
    }

    public static void receiveFile() {
        try {
            int bytesRead;
            InputStream in = sock.getInputStream();

            DataInputStream clientData = new DataInputStream(in);

            fileName = clientData.readUTF();
            OutputStream output = new FileOutputStream(("received_from_server_" + fileName));
            long size = clientData.readLong();
            byte[] buffer = new byte[1024];
            while (size > 0 && (bytesRead = clientData.read(buffer, 0, (int) Math.min(buffer.length, size))) != -1) {
                output.write(buffer, 0, bytesRead);
                size -= bytesRead;
            }
            //this was ehere
            //output.close();
            //in.close();
            //ends here
            output.flush();
            
            
            //sock = new Socket("localhost", 4444);
            //fc.setUpStreams();
            System.out.println("File " + fileName + " received from Server.");
        } catch (Exception ex) {
            
            Logger.getLogger(CLIENTConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
