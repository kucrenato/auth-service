package com.bci.auth_service.commons.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EncryptorUtilTest {

    @DisplayName("Debe encriptar y desencriptar correctamente un texto simple")
    @Test
    void encryptAndDecrypt_success() {
        String original = "texto-secreto";
        String encrypted = EncryptorUtil.encrypt(original);
        assertNotNull(encrypted);
        assertNotEquals(original, encrypted);

        String decrypted = EncryptorUtil.decrypt(encrypted);
        assertEquals(original, decrypted);
    }

    @DisplayName("Debe lanzar excepción al desencriptar un texto inválido")
    @Test
    void decrypt_invalidValue_throwsException() {
        String invalid = "no-es-un-texto-encriptado";
        Exception exception = assertThrows(RuntimeException.class, () -> EncryptorUtil.decrypt(invalid));
        assertTrue(exception.getMessage().contains("Error al desencriptar"));
    }

    @DisplayName("Debe lanzar excepción al encriptar un valor nulo")
    @Test
    void encrypt_nullValue_throwsException() {
        Exception exception = assertThrows(RuntimeException.class, () -> EncryptorUtil.encrypt(null));
        assertTrue(exception.getMessage().contains("Error al encriptar"));
    }
}

