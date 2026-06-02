package com.venkat.backend.lld.security;

import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;

import static org.junit.jupiter.api.Assertions.*;

class EncryptionServiceTest {

    private final AesEncryptionService aes = new AesEncryptionService();
    private final KeyDerivationService kdf = new KeyDerivationService();

    // ── AES-GCM round-trip ────────────────────────────────────────────

    @Test
    void encrypt_decrypt_roundTrip_returnsOriginalPlaintext() {
        byte[] salt = kdf.generateSalt();
        SecretKey key = kdf.deriveKey("test-passphrase", salt);

        String plaintext  = "Hello, World! This is sensitive PII.";
        String ciphertext = aes.encrypt(plaintext, key);
        String decrypted  = aes.decrypt(ciphertext, key);

        assertEquals(plaintext, decrypted);
    }

    @Test
    void sameInput_producesDifferentCiphertexts_dueToRandomIv() {
        byte[] salt = kdf.generateSalt();
        SecretKey key = kdf.deriveKey("test-passphrase", salt);

        String ct1 = aes.encrypt("secret", key);
        String ct2 = aes.encrypt("secret", key);

        // Each encryption uses a fresh random IV → different ciphertext
        assertNotEquals(ct1, ct2,
                "Non-deterministic encryption: same input must produce different ciphertexts");
    }

    @Test
    void wrongKey_throwsEncryptionException() {
        byte[] salt1 = kdf.generateSalt();
        byte[] salt2 = kdf.generateSalt();
        SecretKey key1 = kdf.deriveKey("passphrase-A", salt1);
        SecretKey key2 = kdf.deriveKey("passphrase-B", salt2);

        String ciphertext = aes.encrypt("secret data", key1);
        // Decrypting with a different key must fail (GCM auth tag mismatch)
        assertThrows(AesEncryptionService.EncryptionException.class,
                () -> aes.decrypt(ciphertext, key2),
                "Decryption with wrong key must throw EncryptionException");
    }

    @Test
    void tamperedCiphertext_throwsEncryptionException() {
        byte[] salt = kdf.generateSalt();
        SecretKey key = kdf.deriveKey("test-passphrase", salt);

        String ciphertext = aes.encrypt("original", key);
        // Flip the last character to simulate tampering
        char last = ciphertext.charAt(ciphertext.length() - 1);
        String tampered = ciphertext.substring(0, ciphertext.length() - 1) + (last == 'A' ? 'B' : 'A');

        assertThrows(AesEncryptionService.EncryptionException.class,
                () -> aes.decrypt(tampered, key),
                "Tampered ciphertext must be rejected by GCM authentication tag check");
    }

    // ── Field-level encryption ────────────────────────────────────────

    @Test
    void fieldEncryption_roundTrip() {
        FieldEncryptionService svc = new FieldEncryptionService("my-app-passphrase");

        String email     = "alice@example.com";
        String encrypted = svc.encryptField(email);
        String decrypted = svc.decryptField(encrypted);

        assertEquals(email, decrypted, "Field-level round-trip must recover original value");
        assertNotEquals(email, encrypted, "Encrypted field must not be stored in plaintext");
        assertTrue(encrypted.contains(":"), "Wire format must contain salt separator ':'");
    }

    @Test
    void fieldEncryption_differentInstanceSamePlaintextDifferentCiphertext() {
        // Two services = two keys (different salts) → different ciphertexts
        FieldEncryptionService svc1 = new FieldEncryptionService("passphrase");
        FieldEncryptionService svc2 = new FieldEncryptionService("passphrase");

        String ct1 = svc1.encryptField("alice@example.com");
        String ct2 = svc2.encryptField("alice@example.com");

        assertNotEquals(ct1, ct2,
                "Different service instances (different salts) must produce different ciphertexts");
    }
}
