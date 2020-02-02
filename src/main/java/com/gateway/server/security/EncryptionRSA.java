package com.gateway.server.security;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.math.BigInteger;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

import com.gateway.server.utils.Constants;

@Component
public class EncryptionRSA {

	public byte[] rsaEncrypt(byte[] data) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
		PublicKey pubKey = readKeyFromFile("/public.key");
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, pubKey);
		byte[] cipherData = cipher.doFinal(data);
		return cipherData;
	}


	public String encrypt(String rawText, String publicKeyPath, String transformation, String encoding)
			throws IOException, GeneralSecurityException {

		X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(IOUtils.toByteArray(new FileInputStream(publicKeyPath)));

		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.ENCRYPT_MODE, KeyFactory.getInstance("RSA").generatePublic(x509EncodedKeySpec));

		return Base64.encodeBase64String(cipher.doFinal(rawText.getBytes("UTF-8")));
	}


	public String decrypt(String cipherText) throws IOException, GeneralSecurityException {

		PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(IOUtils.toByteArray(new FileInputStream(Constants.SECURITY_CERTIFICATE)));
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.DECRYPT_MODE, KeyFactory.getInstance("RSA").generatePrivate(pkcs8EncodedKeySpec));

		return new String(cipher.doFinal(Base64.decodeBase64(cipherText)), "UTF-8");
	}

	public String decrypt(String cipherText, PrivateKey privateKey, String transformation, String encoding)
			throws IOException, GeneralSecurityException {

		Cipher cipher = Cipher.getInstance(transformation);
		cipher.init(Cipher.DECRYPT_MODE, privateKey);

		return new String(cipher.doFinal(Base64.decodeBase64(cipherText)), encoding);
	}


	public byte[] rsaDecrypt(byte[] data)  throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
		PrivateKey privKey = readPrivateKeyFromFile("/private.key");
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, privKey);
		byte[] cipherData = cipher.doFinal(data);
		return cipherData;

	}


	PublicKey readKeyFromFile(String keyFileName) throws IOException {
		URL url = getClass().getResource(keyFileName); 
		InputStream in = url.openStream(); 
		ObjectInputStream oin =
				new ObjectInputStream(new BufferedInputStream(in));
		try {

			BigInteger m = (BigInteger) oin.readObject();
			BigInteger e = (BigInteger) oin.readObject();
			RSAPublicKeySpec keySpec = new RSAPublicKeySpec(m, e);
			KeyFactory fact = KeyFactory.getInstance("RSA");
			PublicKey pubKey = fact.generatePublic(keySpec);
			return pubKey;
		} catch (Exception e) {
			throw new RuntimeException("Spurious serialisation error", e);
		} finally {
			oin.close();
		}
	}
	PrivateKey readPrivateKeyFromFile(String keyFileName) throws IOException {
		URL url = getClass().getResource(keyFileName); 
		InputStream in = url.openStream(); 
		ObjectInputStream oin =
				new ObjectInputStream(new BufferedInputStream(in));
		try {

			BigInteger m = (BigInteger) oin.readObject();
			BigInteger e = (BigInteger) oin.readObject();
			RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(m,e);
			KeyFactory fact = KeyFactory.getInstance("RSA");
			PrivateKey priKey = fact.generatePrivate(keySpec);
			return priKey;
		} catch (Exception e) {
			throw new RuntimeException("Spurious serialisation error", e);
		} finally {
			oin.close();
		}
	}

}
