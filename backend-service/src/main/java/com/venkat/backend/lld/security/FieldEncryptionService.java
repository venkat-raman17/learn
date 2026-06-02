package com.venkat.backend.lld.security;

import javax.crypto.SecretKey;
import java.util.Base64;

/**
 * LLD Kata: Field-Level Encryption Service
 *
 * Composite service that models encrypting individual PII database columns
 * (e.g. email, phone, SSN) rather than the entire row.
 *
 * Pattern used in practice:
 *   - Each "column" can have its own key (key-per-column minimizes blast radius)
 *   - Salt is stored alongside the ciphertext (not secret, prevents rainbow tables)
 *   - Wire format: base64(salt) + ":" + encrypted(plaintext)
 *
 * Use cases:
 *   - Storing email in encrypted form in the DB
 *   - GDPR "right to erasure": delete the key → data is cryptographically erased
 *   - Field-level search: deterministic encryption (different algorithm) for
 *     equality checks — but NOT implemented here (non-deterministic AES-GCM is safer)
 *
 * Limitation: non-deterministic encryption means you cannot do `WHERE email = ?`
 * on encrypted fields. Solutions: blind index (HMAC of value) or deterministic
 * encryption with AES-SIV mode.
 */
public class FieldEncryptionService {

    private final AesEncryptionService aes = new AesEncryptionService();
    private final KeyDerivationService kdf = new KeyDerivationService();

    private final SecretKey key;
    private final byte[]    salt;

    /** Create a service with a passphrase. The key is derived once and reused. */
    public FieldEncryptionService(String passphrase) {
        this.salt = kdf.generateSalt();
        this.key  = kdf.deriveKey(passphrase, salt);
    }

    /**
     * Encrypt a single field value.
     *
     * @return wire format: base64(salt) + ":" + base64(iv+ciphertext+tag)
     */
    public String encryptField(String plaintext) {
        String saltB64      = Base64.getEncoder().encodeToString(salt);
        String ciphertext   = aes.encrypt(plaintext, key);
        return saltB64 + ":" + ciphertext;
    }

    /**
     * Decrypt a value produced by {@link #encryptField}.
     * The salt embedded in the wire format is ignored here (key was already derived in constructor).
     */
    public String decryptField(String encoded) {
        int sep = encoded.indexOf(':');
        if (sep < 0) throw new IllegalArgumentException("Invalid encoded field — missing salt separator");
        String ciphertext = encoded.substring(sep + 1);
        return aes.decrypt(ciphertext, key);
    }
}
