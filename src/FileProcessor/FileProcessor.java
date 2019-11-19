// Author: Hayden Migliore
// Date: September 20, 2019
// Class: CMSC 412 6380

package FileProcessor;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.io.*;
import java.awt.event.*;
import java.nio.file.*;
import java.io.FileInputStream;

public class FileProcessor extends JFrame {
    
    JTextArea jta2;
    JTextField jtf;
    String directoryName;
    File[] directory;
    
    public FileProcessor(){
        JFrame frame = new JFrame("File/Directory Processing");
        frame.setSize(400, 600);
        JPanel panel = new JPanel(); 
        JPanel panel2 = new JPanel();
        JLabel so = new JLabel("Select option:");
        String options = "0 - Exit\n"
                + "1 - Select directory: Enter path in text box.\n"
                + "2 - List directory content (frist level)\n"
                + "3 - List directory content (all levels)\n"
                + "4 - Delete file: Enter file name in text box.\n"
                + "5 - Display file (hexadecimal view): Enter file name in text box.\n"
                + "6 - Encrypt file (XOR with password)\n"
                + "7 - Decrypt file (XOR with password)\n";
        JTextArea jta = new JTextArea(options);
        jta2 = new JTextArea("Results: \n");
        JComboBox <String> jcb = new JComboBox <String> ();
        jcb.addItem("0");
        jcb.addItem("1");
        jcb.addItem("2");
        jcb.addItem("3");
        jcb.addItem("4");
        jcb.addItem("5");
        jcb.addItem("6");
        jcb.addItem("7");
        jtf = new JTextField(30);
        JScrollPane jsp = new JScrollPane(jta2);
        jsp.setPreferredSize(new Dimension(300,300));

        panel.add(jta);
        panel.add(so);
        panel.add(jcb);
        panel.add(jtf);
        panel.add(jsp);
        frame.add(panel);
        frame.show();
        
        jcb.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                Options((String)(jcb.getSelectedItem()),jtf.getText());
            }//end required method   
        }//end local definition of inner class
        );//the anonymous inner class
    } 
    
    public void Options(String choice, String data){
        switch(choice){
            case "0":
                Exit();
                break;
            case "1":
                jta2.append("Selecting " + data + ".\n"); 
                SD(data);
                break;
            case "2":
                jta2.append("Finding all files and subdirectories in first level.\n");
                LDC1(directory);
                break;
            case "3":
                jta2.append("Finding all files and subdirectories.\n");
                LDCA(directory);
                break;
            case "4":
                jta2.append("Deleting file listed in text box.\n");
                DF(directoryName);
                break;
            case "5":
                jta2.append("Displaying file listed in text box in hexadecimal view.\n");
                DFHD(directoryName);
                break;
            case "6": 
                jta2.append("Please enter a file name followed by a password.\n");
                jta2.append("Seperate the file name and password with a space.\n");
                jta2.append("Encrypting file listed in text box.\n");
                EncryptDecrypt(directoryName);
                return;
            case "7":
                jta2.append("Please enter a file name followed by a password.\n");
                jta2.append("Seperate the file name and password with a space.\n");
                jta2.append("Decrypting file listed in text box.\n");
                EncryptDecrypt(directoryName);
                return;
        }         
    }
    
    //Exit the application
    public void Exit(){
        System.exit(0);
    }
    
    //Select the directory
    public void SD(String data){
        try{
            directory = new File(data).listFiles();
            if (directory != null){
                directoryName = data;
                jta2.append(directoryName + " selected.\n");
            }    
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    
    //Display files and directories in selected directory (1 level)
    public void LDC1(File[] files){
        try{
            //Error if no directory is selected
            if(directoryName == null){
                jta2.append("No directory selected.\n");
                return;
            }            
            for (File file : files) {
                if (file.isDirectory()) {
                    jta2.append("Directory: " + file.getName() + "\n");
                }
                else {
                    jta2.append("File: " + file.getName() + "\n");
                }
            }
        }
        catch(Exception e){
            jta2.append(e.getMessage());
        }
    }
    
    //Display files and directories in selected directory (all levels)
    public void LDCA(File[] files){
        try{
            //Error if no directory is selected
            if(directoryName == null){
                jta2.append("No directory selected.\n");
                return;
            }
            
            for (File file : files) {
                if (file.isDirectory()) {
                    jta2.append("Directory: " + file.getName() + "\n");
                    LDCA(file.listFiles());
                }
                else {
                    jta2.append("File: " + file.getName() + "\n");
                }
            }
        }
        catch(Exception e){
            jta2.append(e.getMessage());
        }
    }
    
    //Delete file
    public void DF(String directory){
        try{
            //Error if no directory is selected
            if(directoryName == null){
                jta2.append("No directory selected.\n");
                return;
            }
            String fileName = jtf.getText();
            File file = new File(directory + "\\" + fileName);
            jta2.append(file.getName() + " found.\n");
            if(file.delete()){
                jta2.append(file.getName() + " deleted.\n");
            }
            else{
                jta2.append("Unable to delete file.\n");
            }   
        }
        catch(Exception e){
            jta2.append(e.getMessage());
        }   
    }
    
    //Display file in hexadecimal view
    public void DFHD(String directory){
        //Error if no directory is selected
        if(directoryName == null){
            jta2.append("No directory selected.\n");
            return;
        }
        
        String fileName = jtf.getText();
        try(FileInputStream fis = new FileInputStream(directory + "\\" + fileName)){
            int i = 0;
            int count = 0;
            while ((i = fis.read()) != -1){
                StringBuilder s = new StringBuilder();
                s.append(String.format("%02X ", i));
                jta2.append(s.toString());
                count++;
                
                if (count == 16){
                    jta2.append("\n");
                    count = 0;
                }
            }
              
        }
        catch(Exception e){
            jta2.append(e.getMessage());
        }   
    }
    
    //Encrypt file (XOR with password)
    public void EncryptDecrypt(String directory){
        
        //Error if no directory is selected
        if(directoryName == null){
            jta2.append("No directory selected.\n");
            return;
        }
            
        String data = jtf.getText();
        String[] data2 = data.split(" ");
        String fileName = data2[0];
        String password = data2[1];
        String output = "";
        
         
        try{
            Path path = Paths.get(directory + "\\" + fileName);
            byte[] input = Files.readAllBytes(path);
            
            int len = input.length;
            
            for (int i = 0; i < len; i++){
                int c = i % password.length();
                output = output + Character.toString((char) (input[i] ^ password.charAt(c))); 
            } 
            
            try (FileWriter fw = new FileWriter(directory + "\\" + fileName)) {
                fw.write(output);
            }
        }
        catch(Exception e){
            jta2.append(e.getMessage());
        }   
    }
    
    public static void main(String[] args) {
        FileProcessor C = new FileProcessor();
    }
}
