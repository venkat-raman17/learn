package com.venkat.backend.lld.security;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * LLD Kata: AES-GCM Encryption Service
 *
 * AES-GCM (Galois/Counter Mode) is the recommended authenticated encryption mode:
 *   - Confidentiality: plaintext is encrypted (AES CTR mode)
 *   - Integrity + Authentication: GCM appends a 128-bit authentication tag
 *     → tampering with the ciphertext is detected during decryption
 *
 * Wire format: base64(iv[12] || ciphertext+tag)
 *   - IV (Initialization Vector): 12 bytes, random per encryption
 *   - Never reuse (key, IV) pairs — a collision breaks GCM security
 *
 * Key requirements:
 *   - 256-bit (32-byte) AES key
 *   - Derived from a password using PBKDF2 (see KeyDerivationService)
 *   - Stored in a secrets manager (AWS Secrets Manager, HashiCorp Vault)
 *
 * GCM tag length: 128 bits (maximum, recommended)
 * GCM IV length:  96 bits (12 bytes) — optimal for GCM; longer IVs reduce performance
 */
public class AesEncryptionService {

    private static final String ALGORITHM = "AES/GCM/NoPadding";
    private static final int    IV_LEN    = 12;  // bytes (96 bits)
    private static final int    TAG_LEN   = 128; // bits (16 bytes)

    private static final SecureRandom RANDOM = new SecureRandom();

    /**
     * Encrypt plaintext with the given AES key.
     *
     * @return Base64-encoded ciphertext in the format: iv[12] + ciphertext+tag
     */
    public String encrypt(String plaintext, SecretKey key) {
        try {
            byte[] iv = new byte[IV_LEN];
            RANDOM.nextBytes(iv); // fresh random IV every time

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key, new GCMParameterSpec(TAG_LEN, iv));

            byte[] ciphertext = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));

            // Prepend IV so decrypt knows which IV was used
            ByteBuffer buf = ByteBuffer.allocate(IV_LEN + ciphertext.length);
            buf.put(iv);
            buf.put(ciphertext);
            return Base64.getEncoder().encodeToString(buf.array());
        } catch (Exception e) {
            throw new EncryptionException("Encryption failed", e);
        }
    }

    /**
     * Decrypt a ciphertext produced by {@link #encrypt}.
     *
     * @throws EncryptionException if the ciphertext has been tampered with
     *         (GCM authentication tag verification fails) or the key is wrong
     */
    public String decrypt(String encoded, SecretKey key) {
        try {
            byte[] data = Base64.getDecoder().decode(encoded);
            ByteBuffer buf = ByteBuffer.wrap(data);

            byte[] iv         = new byte[IV_LEN];
            byte[] ciphertext = new byte[data.length - IV_LEN];
            buf.get(iv);
            buf.get(ciphertext);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key, new GCMParameterSpec(TAG_LEN, iv));

            byte[] plaintext = cipher.doFinal(ciphertext);
            return new String(plaintext, StandardCharsets.UTF_8);
        } catch (javax.crypto.AEADBadTagException e) {
            throw new EncryptionException("Authentication tag mismatch — ciphertext tampered or wrong key", e);
        } catch (Exception e) {
            throw new EncryptionException("Decryption failed", e);
        }
    }

    public static class EncryptionException extends RuntimeException {
        public EncryptionException(String msg, Throwable cause) { super(msg, cause); }
    }
}
