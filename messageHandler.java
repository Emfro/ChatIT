

package chat;

import java.io.IOException;
import java.io.ObjectInputStream;

public class messageHandler extends Thread{
    
    private ObjectInputStream in;
    
    public messageHandler(ObjectInputStream in) {
        this.in = in;
    }
    
    
    public void run() {
        while(true) {
            printMessages();
        }
    }
    
    private void printMessages(){
     String message = "";
        try {
             message = (String) in.readObject();
            } catch (IOException ex) {
                System.out.println("IO: " + ex);
            } catch (ClassNotFoundException ex) {
                System.out.println("Wont happen for Strings...");
            }
            System.out.println(message);
    }
}