package com.controller;

import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.SecretKey;
import com.libraries.AESLib;


public class CipherBox {
	
	private char[] password;
	private byte[] salt;
	static final int IT = 5;
	private int keyLength;
	private byte[] key;
	public static String SECRET_KEY_TYPE = "AES";
	
	CipherBox(char[] pass, int klength) {
		password = pass;
        SecureRandom rand = new SecureRandom();
        salt = new byte[32];
        rand.nextBytes(salt);
        keyLength = klength;
        PBEKeySpec pbeKeySpec = new PBEKeySpec(password, salt, IT, keyLength);
        SecretKeyFactory secretKeyFactory;
		try {
			secretKeyFactory = SecretKeyFactory.getInstance("AES/GCM/NoPadding");
			SecretKey secretKey = secretKeyFactory.generateSecret(pbeKeySpec);
			key=secretKey.getEncoded();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	public byte[] cifrar(byte[] data) {
		byte[] aux=null;
		
		AESLib cipher = new AESLib(keyLength);
		aux= cipher.cifrar(data, key);
		
		return aux;
	}
	
	public byte[] descifrar(byte[] data) {
		byte[] aux=null;
		
		AESLib cipher = new AESLib(keyLength);
		aux= cipher.descifrar(data, key);
		
		return aux;		
	}
	
	private byte[] leerArchivo(String ruta) {
		byte[] aux=null;
		
		return aux;
	}
	
	private boolean guardarEn(String ruta, byte[] data) {
		boolean aux = false;
		
		return aux;
	}
	
	public boolean cifrarFichero(String rutaOrigen, String rutaDestino) {
		boolean ok= false;
		byte[] datosC = null;
		byte[] aux= null;
		
		aux=leerArchivo(rutaOrigen);
		datosC=cifrar(aux);
		ok=true;
		if(ok==true)
			ok=guardarEn(rutaDestino, datosC);
		
		return ok;
	}
	
	public boolean descrifrarFichero(String rutaOrigen, String rutaDestino) {
		boolean ok= false;
		byte[] datosC = null;
		byte[] aux= null;
		
		aux=leerArchivo(rutaOrigen);
		datosC=descifrar(aux);
		ok=true;
		if(ok==true)
			ok=guardarEn(rutaDestino, datosC);
		
		return ok;	
	}
}
