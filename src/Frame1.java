/**
 * Author : QuaziRafi
 */
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableModel;

public class Frame1 {

    JButton connectButton, openButton, deleteButton, downloadButton, sendButton, newFolderButton, goToHomeButton;
    JFrame f;
    JPanel p, p1, p2, p3;
    JList jl1;
    JLabel showUserPath;
    JTextField searchText;
    DefaultTableModel dtm;
    JFileChooser fileChooser;
    static ObjectInputStream objectInputStream;
    static ObjectOutputStream objectOutputStream;
    private static Socket sock;
    private static String message;
    public ArrayList<File> fileNames;
    public File file[], tempFile,fileToBeSent;
    public String newFolderName,searchString="";

    public void init() {

        f = new JFrame("File Sharing System");
        f.setVisible(true);
        f.setSize(400, 450);
        f.setResizable(false);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLayout(null);
    }

    public void setUpInitialScreen() {

        p = new JPanel();
        p.setLayout(null);
        p.setBounds(0, 0, 400, 450);
        JButton connectButton = new JButton("Connect");
        connectButton.setBounds(140, 220, 120, 40);
        connectButton.setFocusable(false);
        p.add(connectButton);
        Event e = new Event();
        connectButton.addActionListener(e);
        f.getContentPane().add(p);

    }

    public void setUpInitialScreen2() {

        //this one for upper panel
        p1 = new JPanel();
        p1.setLayout(null);
        p1.setBounds(0, 0, 400, 450);
        //p1.setBackground(Color.red);
        
        showUserPath = new JLabel("Current Directory :  .\\"+"Server"+"\\");
        showUserPath.setBounds(0, 385, 280, 30);
        p1.add(showUserPath);
        
        searchText = new JTextField("");
        searchText.setBounds(5,10,120,30);
        p1.add(searchText);
        
        searchText.addKeyListener(new KeyAdapter() {
        public void keyReleased(KeyEvent e7) {
            JTextField textField = (JTextField) e7.getSource();
            searchString = textField.getText();
            jl1.setCellRenderer(new FileRenderer());
            System.out.println(searchString);
        }

        public void keyTyped(KeyEvent e) {
        }

        public void keyPressed(KeyEvent e) {
        }
        });
  

        openButton = new JButton("Open");
        openButton.setBounds(290, 50, 100, 30);
        openButton.setFocusable(false);
        p1.add(openButton);
        Event2 e2 = new Event2();
        openButton.addActionListener(e2);

        goToHomeButton = new JButton("GoHome");
        goToHomeButton.setBounds(290, 80, 100, 30);
        goToHomeButton.setFocusable(false);
        p1.add(goToHomeButton);
        Event3 e3 = new Event3();
        goToHomeButton.addActionListener(e3);

        newFolderButton = new JButton("New Folder");
        newFolderButton.setBounds(290, 110, 100, 30);
        newFolderButton.setFocusable(false);
        p1.add(newFolderButton);
        Event4 e4 = new Event4();
        newFolderButton.addActionListener(e4);
        //

        sendButton = new JButton("send");
        sendButton.setBounds(290, 140, 100, 30);
        sendButton.setFocusable(false);
        p1.add(sendButton);
        Event5 e5 = new Event5();
        sendButton.addActionListener(e5);
        
        
        downloadButton = new JButton("Download");
        downloadButton.setBounds(290, 170, 100, 30);
        downloadButton.setFocusable(false);
        p1.add(downloadButton);
        Event6 e6 = new Event6();
        downloadButton.addActionListener(e6);

//        p2 = new JPanel();
//        p2.setLayout(null);
//        p2.setBounds(0, 50, 400, 450);
        jl1 = new JList(file);
        jl1.setCellRenderer(new FileRenderer());

        //this is for selecting the files from the JList
        jl1.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent arg0) {
                if (!arg0.getValueIsAdjusting()) {
                    //System.out.println(jl1.getSelectedValue());
                    tempFile = (File) jl1.getSelectedValue();

                }

            }
        });
        //

        JScrollPane jsp = new JScrollPane(jl1);
        jsp.setBounds(0, 50, 280, 335);

        p1.add(jsp);
        f.getContentPane().remove(p);
        f.getContentPane().add(p1);
        //f.getContentPane().add(p2);

        p1.repaint();
        p1.setVisible(false);
        p1.setVisible(true);

//        p2.repaint();
//        p2.setVisible(false);
//        p2.setVisible(true);
//        p2.revalidate();

        f.setVisible(false);
        f.setVisible(true);
    }

    public class Event implements ActionListener {

        public void actionPerformed(ActionEvent e) {

            sendMessage("3");
            message = "3";

        }
    }

    public class Event2 implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            System.out.println("NISDIE "+tempFile);
            try {
                //if (tempFile.getName().contains(".")) {
                    System.out.println("NISDIE "+tempFile);
                    sendMessage("opendirectory" + tempFile.getPath());//13
                    message = "opendirectory";
                    showUserPath.setText("Current Directory :  "+tempFile.getPath()+"\\");
                //}
                tempFile = null;
                jl1.clearSelection();
            } catch (Exception e2) {
                e2.printStackTrace();
            };

        }
    }

    public class Event3 implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            try {
                jl1.clearSelection();
                sendMessage("home");
                try {
                    Thread.sleep(200);
                } catch (Exception e2) {
                };
                jl1.clearSelection();
                jl1.removeAll();
                message = "home";
                showUserPath.setText("Current Directory :  .\\"+"Server"+"\\");

            } catch (Exception e2) {
            };
            tempFile = null;
        }
    }

    public class Event4 implements ActionListener {

        public void actionPerformed(ActionEvent e) {

            jl1.clearSelection();
            jl1.removeAll();
            newFolderName = (JOptionPane.showInputDialog("Folder name", ""));
            System.out.println("INSIDE EVENT 4 " + newFolderName + " " + file[0].getParent());
            if (newFolderName.equals("")) {
                newFolderName = "New Folder";
            }
            sendMessage("newfolder" + file[0].getParent() + "\\" + newFolderName);
            jl1.clearSelection();
            jl1.removeAll();
            message = "newfolder";
            tempFile = null;
        }
    }

    public class Event5 implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            
            fileChooser = new JFileChooser();
            System.out.println("INSIDE EVENT 5");
            int returnVal = fileChooser.showOpenDialog((Component) e.getSource());
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                fileToBeSent = fileChooser.getSelectedFile();
                System.out.println(fileToBeSent);
                System.out.println("filesendingfromclient "+file[0].getParent());
                sendMessage("filesendingfromclient "+file[0].getParent());
                sendFile();
                message="filesending";
                fileToBeSent = null;
            } else {
                System.out.println("File access cancelled by user.");
            }
            tempFile = null;
        }
    }
    
      public class Event6 implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            
            if (tempFile != null && tempFile.getName().contains(".")) {
                System.out.println("INSIDE EVENT 6 "+tempFile);
                sendMessage("download"+tempFile.getPath());    
                receiveFile();
                message="download";
                tempFile = null;
            }
             else if (tempFile != null) {
                System.out.println("INSIDE EVENT 6 " + tempFile);
                sendMessage("ddownload" + tempFile.getPath());
                receiveFile();
                message = "download";
                JOptionPane.showMessageDialog(f, "Download Complete");
                tempFile = null;
            }
        }
    }

    public void sendMessage(String message) {
        try {
            objectOutputStream.writeObject(message);
            objectOutputStream.flush();

        } catch (Exception e) {

        }
    }

    public static void setUpStreams() {
        try {
            objectOutputStream = new ObjectOutputStream(sock.getOutputStream());
            objectOutputStream.flush();
            objectInputStream = new ObjectInputStream(sock.getInputStream());

        } catch (Exception e) {
            System.out.println("Inside setUpStreams Exception");
        }
        return;
    }

    public void whileRunning() {
        do {
            try {
                if (message.equals("3")) {

                    fileNames = (ArrayList<File>) objectInputStream.readObject();
                    for (File str : fileNames) {
                        System.out.println(str.getName());

                    }
                    file = new File[fileNames.size()];
                    for (int i = 0; i < file.length; ++i) {
                        file[i] = fileNames.get(i);
                        System.out.println("File or Directory -- > " + file[i]);
                    }

                    setUpInitialScreen2();
                    message = "";

                } else if (message.equals("opendirectory")) {

                    fileNames = (ArrayList<File>) objectInputStream.readObject();

                    file = new File[fileNames.size()];
                    for (int i = 0; i < file.length; ++i) {
                        file[i] = fileNames.get(i);

                    }
                    jl1.removeAll();
                    jl1.setListData(file);
                    //setUpInitialScreen3();

                    message = "";
                } else if (message.equals("home")) {
                    fileNames = (ArrayList<File>) objectInputStream.readObject();
                    file = new File[fileNames.size()];
                    for (int i = 0; i < file.length; ++i) {
                        file[i] = fileNames.get(i);
                    }
                    jl1.removeAll();
                    jl1.setListData(file);
                    message = "";
                } else if (message.equals("newfolder")) {
                    fileNames = (ArrayList<File>) objectInputStream.readObject();
                    file = new File[fileNames.size()];
                    for (int i = 0; i < file.length; ++i) {
                        file[i] = fileNames.get(i);
                    }
                    jl1.removeAll();
                    jl1.setListData(file);
                    message = "";
                }
                else if (message.equals("filesending")) {
                    System.out.println("INSIDE FILE SENDING");
//                    try{
//                        Thread.sleep(200);
//                    }
//                    catch(Exception e){}
                    fileNames = (ArrayList<File>) objectInputStream.readObject();
                    file = new File[fileNames.size()];
                    for (int i = 0; i < file.length; ++i) {
                        file[i] = fileNames.get(i);
                    }
                    jl1.removeAll();
                    jl1.setListData(file);
                    message = "";
                }
                else if (message.equals("download")) {
                    fileNames = (ArrayList<File>) objectInputStream.readObject();
                    file = new File[fileNames.size()];
                    for (int i = 0; i < file.length; ++i) {
                        file[i] = fileNames.get(i);
                    }
                    jl1.removeAll();
                    jl1.setListData(file);
                    message = "";
                }
            } catch (Exception e) {
            };
        } while (true);
    }
    
    public void sendFile() {
        try {
            //System.err.print("Enter file name: ");
//            fileName = "F:\\Networking_Project\\File_Sharing_System-master\\FileSharingSystem3_2\\Server\\b.txt";
//
//            File myFile = new File(fileName);
            byte[] mybytearray = new byte[(int) fileToBeSent.length()];

            FileInputStream fis = new FileInputStream(fileToBeSent);
            BufferedInputStream bis = new BufferedInputStream(fis);
            //bis.read(mybytearray, 0, mybytearray.length);

            DataInputStream dis = new DataInputStream(bis);
            dis.readFully(mybytearray, 0, mybytearray.length);

            OutputStream os = sock.getOutputStream();

            //Sending file name and file size to the server
            DataOutputStream dos = new DataOutputStream(os);
            dos.writeUTF(fileToBeSent.getName());
            dos.writeLong(mybytearray.length);
            dos.write(mybytearray, 0, mybytearray.length);
            dos.flush();
            //newly added
            os.flush();
            System.out.println("File " + fileToBeSent + " sent to Server.");
        } catch (Exception e) {
            System.err.println("File does not exist!");
        }
    }
    
    public void receiveFile() {
        try {
            int bytesRead;
            InputStream in = sock.getInputStream();

            DataInputStream clientData = new DataInputStream(in);

            String fileName = clientData.readUTF();
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

    public static void main(String args[]) {

        try {

            Frame1 fr1 = new Frame1();
            fr1.init();
            fr1.setUpInitialScreen();

            sock = new Socket("localhost", 4444);
            fr1.setUpStreams();
            fr1.whileRunning();

            sock.close();
            objectInputStream.close();
            objectOutputStream.close();

        } catch (Exception e) {
        };

    }

    class FileRenderer extends DefaultListCellRenderer {

        @Override
        public Component getListCellRendererComponent(
                JList list,
                Object value,
                int index,
                boolean isSelected,
                boolean cellHasFocus) {

            JLabel l = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            ImageIcon img1 = new ImageIcon("File.png");
            ImageIcon img2 = new ImageIcon("Folder.png");
            File f = (File) value;
            l.setText(f.getName());
            //l.setIcon(FileSystemView.getFileSystemView().getSystemIcon(f));
            if (f.getName().contains(searchString) && searchString.length()>0)
                l.setBackground(Color.cyan);
            if (f.getName().contains("."))
                l.setIcon(img1);
            else 
                l.setIcon(img2);

            return l;
        }
    }

}
