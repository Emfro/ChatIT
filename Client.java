
package ChatIT;

import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

public class Client extends JFrame{
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private InetAddress serverIP;
    
    Scanner sc = new Scanner(System.in);
    
    public Client(InetAddress host)  {
        serverIP = host;
        try {
            socket = new Socket(host, 6700);
            setStreams();
        } catch (IOException ex) {
          
        }
        
        String alias = "";
        String textLine = "";
        String message = "";
        System.out.println("Choose an alias: ");
        alias = sc.nextLine();
        
        messageHandler handler = new messageHandler(in);
        handler.start();
        
        while(!textLine.equals(".done")) {
        
            textLine = sc.nextLine();
                
            try {
                 
                 out.writeObject(alias + "> " + textLine);
                 out.flush();
            } catch (IOException ex) {
                System.out.println("IO: " + ex);   
            }
            
            
        }
        handler.stop();            
        try {
            closeConnection();
        } catch (IOException ex) {
            System.out.println("IO closeConnection: " + ex);
        }
        
    }


private void setStreams() throws IOException {
      out = new ObjectOutputStream(socket.getOutputStream());
      out.flush();
      in = new ObjectInputStream(socket.getInputStream());
      System.out.println("Client streams setup!");
}
private void closeConnection() throws IOException {
        if(in != null) in.close();
        if(out != null)out.close();
        if(socket != null) socket.close();
}

    public static void main(String[] args) throws IOException {
        Client client = new Client(InetAddress.getLocalHost());
    }
}

