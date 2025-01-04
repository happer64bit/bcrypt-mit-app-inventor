package bcrypt;

import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.runtime.AndroidNonvisibleComponent;
import com.google.appinventor.components.runtime.ComponentContainer;
import com.google.appinventor.components.runtime.errors.YailRuntimeError;
import com.google.appinventor.components.runtime.util.YailList;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

public class Bcrypt extends AndroidNonvisibleComponent {

  public Bcrypt(ComponentContainer container) {
    super(container.$form());
  }

  private static final int SALT_LENGTH = 16;
  private static final int HASH_ITERATIONS = 1000;

  @SimpleFunction(description = "Hashes a password using a custom algorithm.")
  public String HashPassword(String password) {
    try {
      // Generate a random salt
      byte[] salt = generateSalt();
      // Compute the hash
      byte[] hash = hashWithSalt(password, salt, HASH_ITERATIONS);
      // Combine salt and hash
      String saltBase64 = Base64.getEncoder().encodeToString(salt);
      String hashBase64 = Base64.getEncoder().encodeToString(hash);
      return saltBase64 + ":" + hashBase64;
    } catch (Exception e) {
      throw new YailRuntimeError("Error hashing password: " + e.getMessage(), "CustomHashingError");
    }
  }

  @SimpleFunction(description = "Verifies a password against a stored hash.")
  public boolean VerifyPassword(String password, String storedHash) {
    try {
      String[] parts = storedHash.split(":");
      if (parts.length != 2) {
        throw new IllegalArgumentException("Invalid hash format");
      }
      byte[] salt = Base64.getDecoder().decode(parts[0]);
      byte[] expectedHash = Base64.getDecoder().decode(parts[1]);
      byte[] actualHash = hashWithSalt(password, salt, HASH_ITERATIONS);
      return MessageDigest.isEqual(expectedHash, actualHash);
    } catch (Exception e) {
      throw new YailRuntimeError("Error verifying password: " + e.getMessage(), "CustomHashingError");
    }
  }

  private byte[] generateSalt() {
    SecureRandom random = new SecureRandom();
    byte[] salt = new byte[SALT_LENGTH];
    random.nextBytes(salt);
    return salt;
  }

  private byte[] hashWithSalt(String password, byte[] salt, int iterations) throws Exception {
    MessageDigest digest = MessageDigest.getInstance("SHA-256");
    digest.update(salt);
    byte[] hash = digest.digest(password.getBytes("UTF-8"));
    for (int i = 1; i < iterations; i++) {
      digest.reset();
      hash = digest.digest(hash);
    }
    return hash;
  }
}
