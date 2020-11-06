/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package securenotes;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author mahaveersingh
 */
public class Password {
    
     // Configuration values
  public static final String KEY_FACTORY_TYPE = "PBKDF2WithHmacSHA512";
  public static final int    ITERATIONS       = 65536;
  public static final int    KEY_SIZE         = 256;
  public static final int    SALT_BYTES       = 32;

  // Exception messages
  public static final String ERROR_BAD_ALGORITHM  = "Hash configuration error: bad algorithm";
  public static final String ERROR_BAD_SPEC       = "Hash configuration error: bad key specification";
    
  private String password;
  private String salt;
  
   public static String generateRandomSalt() {
       SecureRandom random = new SecureRandom();
       byte[] saltBytes = new byte[SALT_BYTES];
       random.nextBytes(saltBytes);
       return Base64.getEncoder().encodeToString(saltBytes);
   }
   
    //Constructor
    public Password(String password, String salt) {
    this.setPassword(password);
    this.setSalt(salt);
  }
    
    
    //Copy Constructor
    public Password(Password other) {
    this.setPassword(other.getPassword());
    this.setSalt(other.getSalt());
  }
    
    
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        
        //this.password = password;
      
         Pattern pattern = Pattern.compile("(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}");
         Matcher matcher = pattern.matcher(password);
         
          // Check the pattern matches
         boolean matches = matcher.matches();
         
         if(matches){
             this.password = password;
         } else {
             System.out.println("Enter a valid password!");
         }
         
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
    
    public String generateHash() throws PasswordException{
         try {

      char[] passwordChars     = this.getPassword().toCharArray();
      //byte[] saltBytes         = Base64.getDecoder().decode(this.getSalt());
      byte[] saltBytes         = this.getSalt().getBytes();
      // PBKDF2 Generator
      SecretKeyFactory factory = SecretKeyFactory.getInstance(KEY_FACTORY_TYPE);
      
      // Wrapper object for all configuration data
      PBEKeySpec spec    = new PBEKeySpec(passwordChars, saltBytes, ITERATIONS, KEY_SIZE);
      
      // Generating Secret key from factory and spec
      SecretKey key     = factory.generateSecret(spec);

      // Encoding the hash bytes as a Base64 string
      return Base64.getEncoder().encodeToString(key.getEncoded());
    }
    catch(NoSuchAlgorithmException e) {
        throw new PasswordException(ERROR_BAD_ALGORITHM);
    }
    catch(InvalidKeySpecException e) {
      throw new PasswordException(ERROR_BAD_SPEC);
    }
  }
    
    public boolean matchesHash(String hash) throws PasswordException {
    return this.generateHash().equals(hash);
  }
    }

