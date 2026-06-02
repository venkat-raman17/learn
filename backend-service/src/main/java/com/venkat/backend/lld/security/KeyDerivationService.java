package com.venkat.backend.lld.security;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;

/**
 * LLD Kata: PBKDF2 Key Derivation Service
 *
 * PBKDF2 (Password-Based Key Derivation Function 2) turns a low-entropy
 * password into a high-entropy cryptographic key. The "work factor" (iterations)
 * makes brute-force attacks computationally expensive.
 *
 * OWASP 2023 recommendation:
 *   - Algorithm:  PBKDF2WithHmacSHA256
 *   - Iterations: 600,000 (increased from the older 310,000 recommendation)
 *   - Key length: 256 bits
 *   - Salt:       16 bytes random (unique per credential)
 *
 * Salt prevents rainbow table attacks — even if two users have the same
 * password, their derived keys will differ.
 *
 * Never store the plaintext password. Store (salt, derivedKey) — or use
 * bcrypt/argon2 for password hashing (better suited than PBKDF2 for auth).
 * PBKDF2 is best suited for deriving encryption keys from passphrases.
 */
public class KeyDerivationService {

    private static final String   ALGORITHM  = "PBKDF2WithHmacSHA256";
    private static final int      ITERATIONS = 600_000;
    private static final int      KEY_LEN    = 256; // bits
    private static final int      SALT_LEN   = 16;  // bytes
    private static final SecureRandom RANDOM = new SecureRandom();

    /** Generate a cryptographically secure random salt. Store this alongside the key. */
    public byte[] generateSalt() {
        byte[] salt = new byte[SALT_LEN];
        RANDOM.nextBytes(salt);
        return salt;
    }

    /**
     * Derive a 256-bit AES key from a password and salt.
     *
     * @param password the user-supplied passphrase (cleared from memory after use)
     * @param salt     a unique random salt (16+ bytes); must be stored to reproduce the key
     * @return a SecretKey suitable for AES-GCM encryption
     */
    public SecretKey deriveKey(char[] password, byte[] salt) {
        try {
            PBEKeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, KEY_LEN);
            SecretKeyFactory factory = SecretKeyFactory.getInstance(ALGORITHM);
            byte[] keyBytes = factory.generateSecret(spec).getEncoded();
            spec.clearPassword(); // wipe sensitive data
            return new SecretKeySpec(keyBytes, "AES");
        } catch (Exception e) {
            throw new RuntimeException("Key derivation failed", e);
        }
    }

    /** Convenience overload for String passwords (less secure — prefer char[] to avoid GC leaks). */
    public SecretKey deriveKey(String password, byte[] salt) {
        return deriveKey(password.toCharArray(), salt);
    }
}
