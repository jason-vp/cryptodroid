package com.controller;

import javax.crypto.spec.PBEKeySpec;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

import javax.crypto.SecretKeyFactory;
import javax.crypto.SecretKey;

import android.util.Log;

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
        keyLength = klength;
	}
	
	private void generarKey() {

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
		
		this.generarKey();
		AESLib cipher = new AESLib(keyLength);
		aux= cipher.cifrar(data, key);
		
		return aux;
	}
	
	public byte[] descifrar(byte[] data) {
		byte[] aux=null;
		
		this.generarKey();
		AESLib cipher = new AESLib(keyLength);
		aux= cipher.descifrar(data, key);
		
		return aux;		
	}
	
	private byte[] leerArchivo(String ruta) {
		byte[] aux=null;
		
		try
		{
		    FileInputStream fis = new FileInputStream(ruta);
		 
		    fis.read(aux);
		    fis.close();
		}
		catch (Exception ex)
		{
		    Log.e("Ficheros", "Error al leer fichero desde memoria interna");
		}
		
		return aux;
	}
	
	private boolean guardarEn(String ruta, byte[] data) {
		boolean ok=false;
		byte[] aux= null;
		
		try
		{
		    FileOutputStream fis = new FileOutputStream(ruta);
		    
		    fis.write(aux);
		    fis.close();
		    ok=true;
		}
		catch (Exception ex)
		{
		    Log.e("Ficheros", "Error al leer fichero desde memoria interna");
		}
		
		return ok;
	}
	
	public boolean cifrarFichero(String rutaOrigen, String rutaDestino) {
		boolean ok= false;
		byte[] datosC = null;
		byte[] aux= null;
		
		aux=leerArchivo(rutaOrigen);
		this.generarSalt();
		datosC=cifrar(aux);
		ok=true;
		if(ok==true) {
			byte[] datosCS = concatena(datosC, salt);
			ok=guardarEn(rutaDestino, datosCS);
		}
		
		return ok;
	}
	
	public boolean descrifrarFichero(String rutaOrigen, String rutaDestino) {
		boolean ok= false;
		byte[] datosCS = null;
		byte[] datosC = null;
		byte[] aux= null;
		
		
		datosCS=leerArchivo(rutaOrigen);
		
		datosC=Arrays.copyOf(datosCS, datosCS.length-32);
		salt=obtenerSalt(datosCS);
		aux=descifrar(datosC);
		ok=true;
		if(ok==true)
			ok=guardarEn(rutaDestino, datosC);
		
		return ok;	
	}
	
	private static byte[] concatena(byte[] a, byte[] b) {
		byte[] c = new byte[a.length + b.length];
		
		for(int i=0; 0<a.length; i++) {
			c[i]=a[i];
		}
		
		for(int i=a.length; 0<b.length; i++) {
			c[i]=b[i];
		}
		
		return c;
	}
	
	private static byte[] obtenerSalt(byte[] a) {
		byte[] aux =new byte[32];
		
		if(a.length>=32) {
			for(int i=0; i<32; i++) {
				aux[i]=a[a.length+i];
			}
		}
		
		return aux;
	}
	
	private void generarSalt() {
		SecureRandom rand = new SecureRandom();
        salt = new byte[32];
        rand.nextBytes(salt);
	}
}
