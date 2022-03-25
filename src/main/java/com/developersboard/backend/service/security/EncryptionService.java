package com.developersboard.backend.service.security;

/**
 * This is the contract for the encryption service operations.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
public interface EncryptionService {

  /**
   * Encrypts the text to be sent out.
   *
   * @param text the text
   * @return the encrypted text
   */
  String encrypt(String text);

  /**
   * Decrypts the encryptedText to be sent out.
   *
   * @param encryptedText the encryptedText
   * @return the decrypted uri
   */
  String decrypt(String encryptedText);

  /**
   * Encode text before appending in link.
   *
   * @param text the text
   * @return the encoded text
   */
  String encode(String text);

  /**
   * Decode text before appending in link.
   *
   * @param text the text
   * @return the encoded text
   */
  String decode(String text);
}
