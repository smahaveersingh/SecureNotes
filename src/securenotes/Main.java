/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package securenotes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author mahaveersingh
 */
public class Main {
    private ArrayList<Note> notes = new ArrayList<>();

    public static void main(String[] args) throws IOException, PasswordException {
        
          
          SaveUserPassword();
          VerifySavedPassword();
          
          Scanner in = new Scanner(System.in);
          
          System.out.println("\n -----MENU-----");
          System.out.println("\n 1.Save Note");
          System.out.println("\n 2.Reset Password");
          System.out.println("\n ----0.Quit----");
            
        boolean quit = false;
        int menuItem;
        do {
            System.out.print("Choose menu item: ");

            menuItem = in.nextInt();

            switch (menuItem) {
                case 1:
            
                    SaveNote();
            
                    break;
                case 2:
                    ResetPassword();
                    break;
                
                case 0:
                    quit = true;
                    break;
                default:

                    System.out.println("Invalid choice.");
                  }
            } while (!quit);

            System.out.println("Bye-bye!");       
 }

    private static void SaveUserPassword() throws IOException, PasswordException {

        System.out.println("Enter Password: ");

        Scanner keyboard = new Scanner(System.in);
        String plaintextPassword = keyboard.nextLine();
        
        String passwordSalt = Password.generateRandomSalt();

        //Read the stored password hash and config data
            Scanner inFile = new Scanner(new FileReader("./password.txt"));

            String salt = inFile.nextLine();
            String hash = inFile.nextLine();
           
            //close the file reader
            inFile.close();
            
            // Create a password object from the file contents to check if password already exists!
            Password password = new Password(plaintextPassword, salt);

            if (password.matchesHash(hash)) {
                //System.out.println("Password already exists! ");   
                throw new RuntimeException("Password already exists! ");
            } else {
                
                // Create a password object from the file contents
                Password password1 = new Password(plaintextPassword, passwordSalt);
                
                hash = password1.generateHash();
                
                FileWriter outFile = new FileWriter("./password.txt");
                
                outFile.write(password1.getSalt() + "\n");
                outFile.write(hash);
                
                outFile.close();
                System.out.println("Password hash saved to file"); 
            }
        
    }

    private static String VerifySavedPassword() throws IOException, PasswordException {

        int count = 1;

        for (int i = 0; i < count; count++) {
            // Ask user to input a password
            System.out.println("Please enter your password (for verification):");

            Scanner keyboard = new Scanner(System.in);
            String plaintextPassword = keyboard.nextLine();

            // Read the stored password hash and config data
            Scanner inFile = new Scanner(new FileReader("./password.txt"));

            String salt = inFile.nextLine();
            String hash = inFile.nextLine();
           
            //close the file reader
            inFile.close();
            
            // Create a password object from the file contents
            Password password = new Password(plaintextPassword, salt);
            
            
            
            if (password.matchesHash(hash)) {
                System.out.println("Correct password entered");
                    return null;
            } else {
                System.out.println("Bad password entered");
                if (count >= 3) {
                    System.out.println("You have Exhausted your login tries! Wait for 10 seconds and try again later!");
                    
                    try {
                        TimeUnit.SECONDS.sleep(10);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }

                }
            }

        }
        return null;
            
    }

    private static String SaveNote() throws IOException, PasswordException {
        
           
           ArrayList<Note> notelist = new ArrayList<>();
           
           int id;
           String title;
           String text;
           String tags;
           String line;
           Timestamp createdAt = new Timestamp(System.currentTimeMillis());
           
           System.out.println("Enter how many notes to save: ");
           Scanner keyboard = new Scanner(System.in);
           int noOfnotes = keyboard.nextInt();
           
           
           
           for(int i=1;i<=noOfnotes;i++) {
           
           BufferedReader Br =new BufferedReader(new InputStreamReader(System.in));
           line = Br.readLine();
           id = Integer.parseInt(line);
           System.out.println("Please enter your Title: ");
           title = Br.readLine();
           
           System.out.println("Please enter your text: ");
           text = Br.readLine();
           
           System.out.println("Please enter your tags: ");
           tags = Br.readLine();
           
           
           notelist.add(new Note(id,title,text,tags,createdAt));
           }
           String allnotes = noteListToString(notelist);
           
           //Note n1 = new Note(id,title,text,tags,createdAt);
            //String origanalplaintext = n1.toString();

            String password = "P@ssword124";
            String salt = Password.generateRandomSalt();
            String masterEncryptionKey = new Password(password, salt).generateHash();
//            
            // Encrypt and print ciphertext
            String ciphertext = Cipher.encryptString(allnotes, masterEncryptionKey);
            System.out.println("\nCIPHERTEXT:");
            System.out.println(ciphertext);
            
            FileWriter outFile = new FileWriter("./notes.txt");
            
            outFile.write(ciphertext + "\n");
            
            outFile.close();
           
            // Decrypt and print plaintext
            String decryptedPlaintext = Cipher.decryptString(ciphertext, masterEncryptionKey);
            System.out.println("\nPLAINTEXT:");
            System.out.println(decryptedPlaintext);

            System.out.println("Note saved to file");
           
            return masterEncryptionKey;
           
    

    }

    private static String noteListToString(ArrayList<Note> notes) {
        String line = "";
        
        for(Note note: notes) {
            line += note.toline() + "\n";
        }
        
        return line;
    }

    private static void ReadNote(String masterEncryptionKey) throws FileNotFoundException {
        
        
        
        Scanner inFile = new Scanner(new FileReader("./notes.txt"));

            String ciphertext = inFile.nextLine();

//            // Decrypt and print plaintext
            String decryptedPlaintext = Cipher.decryptString(ciphertext, masterEncryptionKey);
//            System.out.println("\nPLAINTEXT:");
//            System.out.println(decryptedPlaintext);
         
         ArrayList<Note> notelist = new ArrayList<>();
         
         String allnotes = noteListToString(notelist);
         
         ArrayList<Note> parsedNoteList = noteListFromString(allnotes);
         
         for(Note each: parsedNoteList) {
             System.out.println("ID: " + each.getId());
             System.out.println("Title: " + each.getTitle());
             System.out.println("Text: " + each.getText());
             System.out.println("Time: " + each.getCreatedAt());
             System.out.println("");
             
         }
         //close the file reader
         inFile.close();
         
    }

    private static ArrayList<Note> noteListFromString(String noteString) {
        
        ArrayList<Note> notes = new ArrayList<>();
        
        // A scanner object to parse through the line of text
        Scanner parser = new Scanner(noteString);
        parser.useDelimiter("[,\n]");
            
        while(parser.hasNextLine() && parser.hasNext()) {
        
            int id              = parser.nextInt();
            String Title        = parser.next();
            String Text         = parser.next();
            //Timestamp createdAt = parser.hasNextDouble();
        }
        return notes;
    }
    
    private static void ResetPassword() throws IOException, PasswordException {
        
         System.out.println("Please enter your old password :");
         
            Scanner keyboard = new Scanner(System.in);
            String plaintextPassword = keyboard.nextLine();
            
            //Read the stored password hash and config data
            Scanner inFile = new Scanner(new FileReader("./password.txt"));

            String salt = inFile.nextLine();
            String hash = inFile.nextLine();
           
            //close the file reader
            inFile.close();
            
            Password password = new Password(plaintextPassword, salt);

            if (password.matchesHash(hash)) {
                System.out.println("Please enter your new password: ");
                
                String newplaintextPassword = keyboard.nextLine();
                String passwordSalt = Password.generateRandomSalt();
                
                Password password1 = new Password(newplaintextPassword, passwordSalt);
                
                hash = password1.generateHash();
                
                FileWriter outFile = new FileWriter("./password.txt");
                
                outFile.write(password1.getSalt() + "\n");
                outFile.write(hash);
                
                outFile.close();
                System.out.println("Updated Password hash saved to file");
            }
        

}
       
}

