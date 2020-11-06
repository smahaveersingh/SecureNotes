/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package securenotes;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author mahaveersingh
 */
public class Note implements Serializable {
    
    private static final AtomicInteger count = new AtomicInteger(0); 
    private int id;
    private String title;
    private String text;
    private String tags;
    private Timestamp createdAt;
    
    public Note() {
        id = 1;
        title = "";
        text = "";
        tags = "";
        
    }
    
    //Constructor
    public Note(int id, String title, String text, String tags, Timestamp createdAt) {
    
        this.setId(id);
        this.setTitle(title);
        this.setText(text);
        this.setTags(tags);
        this.setCreatedAt(createdAt);
    
  }
        //Copy Constructor
    public Note(Note other) {
    this.setId(other.getId());
        this.setTitle(other.getTitle());
        this.setText(other.getText());
        this.setTags(other.getTags());
        this.setCreatedAt(other.getCreatedAt());
  }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = count.incrementAndGet();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        //this.title = title;
        if(title.length()<=55) {
            this.title = title;
        } else {
            System.out.println("Title can be only 55 characters long! ");
        }
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        //this.text = text;
        if(text.length()<=255) {
            this.text = text;
        } else {
            System.out.println("Text Limit is 255 charecters! ");
        }
        
        if (text.isEmpty()) {
      throw new RuntimeException("text cannot be empty");
    }
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
//        Pattern pattern = Pattern.compile("[a-z0-9_]{,16}");
//         Matcher matcher = pattern.matcher((CharSequence) tags);
//         
//          // Check the pattern matches
//         boolean matches = matcher.matches();
//         
//         if(matches){
//             this.tags = tags;
//         } else {
//             System.out.println("Enter a valid Tag!(should only contain lowercase letters, numbers, and underscores and should be no more than 16 characters)");
//         }
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    public String toline() {
        return this.getId() + "," + this.getTitle() + "," + this.getText() + 
                ","  + this.getTags();
    }

    @Override
    public String toString() {
        return "Note{" + "id=" + id + ", title=" + title + ", text=" + text + ", tags=" + tags + ", createdAt=" + createdAt + '}';
    }
    
    
    
    
    
}
