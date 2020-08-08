
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

public class CLIENTConnection extends JFrame implements Runnable {

    private Socket clientSocket;
    private BufferedReader in = null;
    public ObjectInputStream objectInputStream;
    public ObjectOutputStream objectOutputStream;
   private String message,fileSavePath;
   public static int downloadFileCounter=0;

    public CLIENTConnection(Socket client) {

        this.clientSocket = client;

        // super("Client it is");
    }

    @Override
    public void run() {
        setUpStreams();
        whileRunning();
    }

    public void setUpStreams() {
        try {
            objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            objectOutputStream.flush();
            objectInputStream = new ObjectInputStream(clientSocket.getInputStream());

        } catch (Exception e) {
            System.out.println("Inside setUpStreams Exception");
        }
    }

    public void whileRunning() {
        
        do {
            try {
                message = (String) objectInputStream.readObject();
                if (message.equals("3")) {
                    //File folder = new File("F:\\Networking_Project\\File_Sharing_System-master\\FileSharingSystem3_4\\Server\\");
                    File folder = new File(".\\Server\\");
                    File[] listOfFiles = folder.listFiles();
                    ArrayList<File> fileNames = new ArrayList<File>();
                    for (int i = 0; i < listOfFiles.length; i++) {
                        if (listOfFiles[i].isFile()) {
                            fileNames.add(listOfFiles[i]);
                        } else if (listOfFiles[i].isDirectory()) {
                            fileNames.add(listOfFiles[i]);
                        }
                    }
                    sendMessage(fileNames);

                    message = "";
                } else if (message.equals("2")) {
                    sendFile("F:\\Networking_Project\\File_Sharing_System-master\\FileSharingSystem3_4\\Server\\a.txt");
                    message = "";
                } else if (message.equals("1")) {
                    receiveFile();
                    message = "";
                } else if (message.startsWith("opendirectory")) {
                    String path = message.substring(13);
                    System.out.println("THIs is path :" + path);
                    File folder = new File(path + "\\");
                    File[] listOfFiles = folder.listFiles();
                    ArrayList<File> fileNames = new ArrayList<File>();
                    for (int i = 0; i < listOfFiles.length; i++) {
                        if (listOfFiles[i].isFile()) {
                            fileNames.add(listOfFiles[i]);
                        } else if (listOfFiles[i].isDirectory()) {
                            fileNames.add(listOfFiles[i]);
                        }
                    }
                    sendMessage(fileNames);

                    message = "";
                } else if (message.equals("home")) {
                    //File folder = new File("F:\\Networking_Project\\File_Sharing_System-master\\FileSharingSystem3_4\\Server\\");
                    File folder = new File(".\\Server\\");
                    File[] listOfFiles = folder.listFiles();
                    ArrayList<File> fileNames = new ArrayList<File>();
                    for (int i = 0; i < listOfFiles.length; i++) {
                        if (listOfFiles[i].isFile()) {
                            fileNames.add(listOfFiles[i]);
                        } else if (listOfFiles[i].isDirectory()) {
                            fileNames.add(listOfFiles[i]);
                        }
                    }
                    sendMessage(fileNames);

                    message = "";
                } else if (message.startsWith("newfolder")) {
                    
                    String path = message.substring(9);
                    System.out.println("THIs is path :" + path);
                    File file = new File(path);
                    if (!file.exists()) {
                        if (file.mkdir()) {
                            System.out.println("Directory is created!");
                        } else {
                            System.out.println("Failed to create directory!");
                        }
                    }
                    
                    try {
                        File defaultFile = new File(path + "\\${default-file-FSS}.txt");
                        if (defaultFile.createNewFile()) {
                            System.out.println("File is created!");
                        } else {
                            System.out.println("File already exists.");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    String parentPath = path.substring(0, path.lastIndexOf('\\'));
                    System.out.println("This is paarent path "+parentPath);
                    File folder = new File(parentPath);
                    File[] listOfFiles = folder.listFiles();
                    ArrayList<File> fileNames = new ArrayList<File>();
                    for (int i = 0; i < listOfFiles.length; i++) {
                        if (listOfFiles[i].isFile()) {
                            fileNames.add(listOfFiles[i]);
                        } else if (listOfFiles[i].isDirectory()) {
                            fileNames.add(listOfFiles[i]);
                        }
                        System.out.println(listOfFiles[i]);
                    }
                    sendMessage(fileNames);
                    message = "";
                }
                else if (message.startsWith("filesendingfromclient")){
                    //fileSavePath = message.substring(21);
                    fileSavePath = message.substring(22);
                    System.out.println("file Sent "+fileSavePath);
                    String fileSavePath2 = fileSavePath;
                    receiveFile();
                    //File folder = new File("F:\\Networking_Project\\File_Sharing_System-master\\FileSharingSystem3_4\\"+fileSavePath2+"\\");
                    //File folder = new File(".\\"+fileSavePath2+"\\");
                    File folder = new File(fileSavePath2+"\\");
                    File[] listOfFiles = folder.listFiles();
                    ArrayList<File> fileNames = new ArrayList<File>();
                    for (int i = 0; i < listOfFiles.length; i++) {
                        if (listOfFiles[i].isFile()) {
                            fileNames.add(listOfFiles[i]);
                        } else if (listOfFiles[i].isDirectory()) {
                            fileNames.add(listOfFiles[i]);
                        }
                        System.out.println(listOfFiles[i]);
                    }
                    sendMessage(fileNames);
                    message = "";
                    fileSavePath2 = "";
                }
                else if (message.startsWith("download")){
                    sendFile(message.substring(8));
                    //File folder = new File(message.substring(0, message.lastIndexOf('\\')));
                    File folder = new File(".\\Server\\");
                    File[] listOfFiles = folder.listFiles();
                    ArrayList<File> fileNames = new ArrayList<File>();
                    for (int i = 0; i < listOfFiles.length; i++) {
                        if (listOfFiles[i].isFile()) {
                            fileNames.add(listOfFiles[i]);
                        } else if (listOfFiles[i].isDirectory()) {
                            fileNames.add(listOfFiles[i]);
                        }
                        System.out.println(listOfFiles[i]);
                    }
                    sendMessage(fileNames);
                    message = "";
                }
                 else if (message.startsWith("ddownload")){
                    ++downloadFileCounter;
                    ZipperClass2.goZip(message.substring(9),downloadFileCounter);
                    sendFile(".\\download"+downloadFileCounter+".zip");
                    System.out.println("INSDE DDWONLAD");
                    //File folder = new File(message.substring(0, message.lastIndexOf('\\')));
                    File folder = new File(".\\Server\\");
                    File[] listOfFiles = folder.listFiles();
                    ArrayList<File> fileNames = new ArrayList<File>();
                    for (int i = 0; i < listOfFiles.length; i++) {
                        System.out.println(listOfFiles[i].getName());
                        if (listOfFiles[i].isFile()) {
                            fileNames.add(listOfFiles[i]);
                        } else if (listOfFiles[i].isDirectory()) {
                            fileNames.add(listOfFiles[i]);
                        }
                            System.out.println("DDOWNLOAD : "+listOfFiles[i]);
                    }
                    
                    sendMessage(fileNames);
                   
                    message = "";
                }

            } catch (Exception e) {
                //showMessage("\n dont know this object");
            }
        } while (!message.equals("SERVER - END"));
    }

    //public void sendMessage(ArrayList<String> fileNames) {
    public void sendMessage(ArrayList<File> fileNames) {
        try {
            objectOutputStream.writeObject(fileNames);
            objectOutputStream.flush();

        } catch (Exception e) {

        }
    }

    public void receiveFile() {
        try {

            System.out.println("Inside Receive Client");
            int bytesRead;

            DataInputStream clientData = new DataInputStream(clientSocket.getInputStream());

            String fileName = clientData.readUTF();
            System.out.println("INSIDE RECEIVE FILE SERVER "+fileName);
            
            //OutputStream output = new FileOutputStream((".\\"+fileSavePath+"\\" + fileName));
            OutputStream output = new FileOutputStream((fileSavePath+"\\" + fileName));
            long size = clientData.readLong();
            byte[] buffer = new byte[1024];
            while (size > 0 && (bytesRead = clientData.read(buffer, 0, (int) Math.min(buffer.length, size))) != -1) {
                output.write(buffer, 0, bytesRead);
                size -= bytesRead;
            }
            //this was here
            //output.close();
            //clientData.close();
            //ends here

            output.flush();
            fileSavePath = "";

            System.out.println("File " + fileName + " received from client.");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void sendFile(String fileName) {
        try {
            //handle file read
            File myFile = new File(fileName);
            byte[] mybytearray = new byte[(int) myFile.length()];

            FileInputStream fis = new FileInputStream(myFile);
            BufferedInputStream bis = new BufferedInputStream(fis);
            //bis.read(mybytearray, 0, mybytearray.length);

            DataInputStream dis = new DataInputStream(bis);
            dis.readFully(mybytearray, 0, mybytearray.length);

            //handle file send over socket
            OutputStream os = clientSocket.getOutputStream();

            //Sending file name and file size to the server
            DataOutputStream dos = new DataOutputStream(os);
            dos.writeUTF(myFile.getName());
            dos.writeLong(mybytearray.length);
            dos.write(mybytearray, 0, mybytearray.length);
            dos.flush();

            //newly added
            os.flush();
            System.out.println("File " + fileName + " sent to client.");
        } catch (Exception e) {
            System.err.println("File does not exist!");
        }
    }
}
