
import java.util.ArrayList;
import javax.swing.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author DANA
 */
public class ServerGui {
    
    private JFrame f;
    private JButton connectButton ;
    private JButton freeSpaceButton;
    private JList jl1;
    private static ArrayList<String> conns;
    private static String names[]; 
    
    public void init(){
         f = new JFrame("File Sharing System Server");
        f.setVisible(true);
        f.setSize(400, 500);
        f.setResizable(false);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLayout(null);
        JLabel lab = new JLabel("Status : Running (Share your files on the Server folder)");
          lab.setBounds(40,15,340,40);
          f.add(lab);
          jl1 = new JList(names);
          JScrollPane jsp = new JScrollPane(jl1);
        jsp.setBounds(0, 50, 280, 335);
        f.add(jsp);
        f.repaint();
    }
    
    public void sendName(String name){
        conns.add(name);
        names = new String[conns.size()];
        for (int i=0;i<names.length;++i){
            names[i] = conns.get(i);
        }
        jl1.setListData(names);
    }
}
