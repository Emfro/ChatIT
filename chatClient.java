
package chat;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;



public class chatClient extends Thread{
    
        private ObjectInputStream input;
        private ObjectOutputStream output;
        private Socket connection;
        private List<chatClient> chattLista = Collections.synchronizedList(new ArrayList<chatClient>());
        boolean chatting = true;          
        private String text;
        
        public chatClient(Socket sock) throws IOException {
            text = "";
            this.connection = sock;
            
        }
        
        public void run(){
            try {
                setStreams();
            } catch (IOException ex) {
                System.out.println("IO: setStreams: " + ex);
            }
                   
            while(chatting) {      
                try {
                    text = (String) input.readObject();
                    System.out.println(text);
                } catch (IOException ex) {
                    System.out.println("IO: " + ex);
                } catch (ClassNotFoundException ex) {
                    System.out.println("händer int");
                }
                if(text.contains("HowMany?")) {NumberOfParticipants();}
                chatting = !text.equals("AXEL: done");
                broadcast(text);
            }
        
        System.out.println("CLient " + connection.getInetAddress() +  " has disconnected!");
        
            try {
                closeConnection();
            } catch (IOException ex) {
                System.out.println("IO closeConnections: " + ex);
            }
            
        }
        
        public String getText(){
            return this.text;
        }
        
        private void setStreams() throws IOException {
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
           System.out.println("Server streams setup!");
        }
        
        public void setChatlist(List<chatClient> al) {
            this.chattLista = al;
        }
        
        private void closeConnection() throws IOException {
            if(input != null) input.close();
            if(output != null)output.close();
            if(connection != null) connection.close();
        }
        
        private void NumberOfParticipants(){
            try {          
                output.writeObject(Integer.toString(chattLista.size()) + " clients are currently connected!");
            } catch (IOException ex) {
                System.out.println("IO NumberOfParticipants: " + ex);
            }
        }
        
        public void receive(String text) {
            try {
                output.writeObject(text);
                output.flush();
            } catch (IOException ex) {
                System.out.println("IO broadcast: "+ex);
            }
            
            
        }
        
        private void broadcast(String text) {
            for (int i = 0; i < chattLista.size(); i++) {
                chattLista.get(i).receive(text);
            }
    }
}
