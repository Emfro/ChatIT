
package ChatIT;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.*;




public class Server {
    private static ServerSocket server;
    private static Socket connection;
    private static Socket newClient;
    //private static ArrayList<chatClient> al;
     
    
    public Server() throws IOException, ClassNotFoundException {
        System.out.println("Setting up server on: " + InetAddress.getLocalHost());
        
        server = new ServerSocket(6700, 100, InetAddress.getLocalHost());
        
        
    }  
    private static Socket waitForConnection() throws IOException{
        connection = server.accept();
        System.out.println("Connected to " + connection.getInetAddress());
        return connection;
    }
    
    
   /* 
    private static void broadcast(List<chatClient> al){
        String text = null;
        
        for (int j = 0; j < al.size(); j++) {
            
            
            if(al.get(j).getText() != null && al.get(j).getText().equals("\n"))
            {
            
            text = al.get(j).getText();
             System.out.println(text);
            for (int i = 0; i < al.size(); i++) {
                al.get(i).receive(text);
            }
            
            }         
        }
    }
    */
    
    private static void setLists(List<chatClient> al) {
        for (int i = 0; i < al.size(); i++) {
            if(!al.get(i).isAlive()) al.remove(i);
            
            al.get(i).setChatlist(al);
        }
    }
            
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        
        Server server = new Server();
        List<chatClient> chattLista = Collections.synchronizedList(new ArrayList<chatClient>());
        
        
       
         while(true) {    
             newClient = waitForConnection();
             chatClient c = new chatClient(newClient);    
             c.start();
             chattLista.add(c);
             setLists(chattLista);
             
        }
        
    }
    
}
