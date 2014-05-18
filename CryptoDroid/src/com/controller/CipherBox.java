package com.controller;

import javax.crypto.spec.PBEKeySpec;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

import javax.crypto.SecretKeyFactory;
import javax.crypto.SecretKey;

import android.content.Context;
import android.util.Log;

import com.libraries.AESLib;



public class CipherBox {
	
	private char[] password;
	private byte[] salt;
	static final int IT = 5;
	private int keyLength;
	private byte[] key;
	public static String SECRET_KEY_TYPE = "AES";
	
	public CipherBox(char[] pass, int klength) {
		password = pass;
        keyLength = klength;
        salt = new byte[32];
	}
	
	private void generarKey() {

        PBEKeySpec pbeKeySpec = new PBEKeySpec(password, salt, IT, keyLength);
        SecretKeyFactory secretKeyFactory;
		try {
			secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
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
		byte[] datosC=null;
		
		this.generarSalt();
		this.generarKey();
		AESLib cipher = new AESLib(keyLength);
		datosC= cipher.cifrar(data, key);
		byte[] datosCS = new byte[datosC.length+32];
		datosCS=concatena(datosC, salt);
		System.out.println("\nSALT >>>>>>>>>>>>>>> " + Arrays.toString(salt) + "\n");
		System.out.println("\nKEY >>>>>>>>>>>>>>> " + Arrays.toString(key) + "\n");
		
		return datosCS;
	}
	
	public byte[] descifrar(byte[] dataCS) {
		byte[] dataC= null;
		byte[] aux= null;
		
		salt=obtenerSalt(dataCS);
		this.generarKey();
		System.out.println("\nSALT >>>>>>>>>>>>>>> " + Arrays.toString(salt) + "\n");
		System.out.println("\nKEY >>>>>>>>>>>>>>> " + Arrays.toString(key) + "\n");
		dataC=new byte[dataCS.length-32];
		dataC=Arrays.copyOf(dataCS, dataCS.length-32);
		AESLib cipher = new AESLib(keyLength);
		aux= cipher.descifrar(dataC, key);
		
		return aux;		
	}
	
	private byte[] leerArchivo(String ruta) throws IOException {
        RandomAccessFile aFile = new RandomAccessFile(ruta, "r");
        FileChannel inChannel = aFile.getChannel();
        MappedByteBuffer buffer = inChannel.map(FileChannel.MapMode.READ_ONLY, 0, inChannel.size());
        buffer.load(); 
        for (int i = 0; i < buffer.limit(); i++)
        {
            System.out.print((char) buffer.get());
        }
        buffer.clear(); // do something with the data and clear/compact it.
        byte[] aux= new byte[buffer.capacity()];
        buffer.get(aux, 0, aux.length);
        inChannel.close();
        aFile.close();
        
        return aux;
	}
	
	public boolean guardarEn(String ruta, byte[] data) {
		boolean ok=false;
		File fichero = new File (ruta);
		
		try
		{
		    try {
		        // A partir del objeto File creamos el fichero físicamente
		        if (fichero.createNewFile())
		        	System.out.println("El fichero se ha creado correctamente");
		        else
		        	System.out.println("Fichero sobreescrito?");
		        } catch (IOException ioe) {
		        ioe.printStackTrace();
		        }
		    FileOutputStream fis = new FileOutputStream(ruta);
		    fis.write(data);
		    fis.close();
		    ok=true;
		}
		catch (Exception ex)
		{
		    Log.e("Ficheros", "Error al leer fichero desde memoria interna");
		}
		
		return ok;
	}
	
	public void guardar(String filename, byte[] data, Context ctx) {

		FileOutputStream outputStream;
		
		try {
		  outputStream = ctx.openFileOutput(filename, Context.MODE_PRIVATE);
		  outputStream.write(data);
		  outputStream.close();
		} catch (Exception e) {
		  e.printStackTrace();
		}
	}
	
	public boolean cifrarFichero(String rutaOrigen, String rutaDestino) throws IOException {
		boolean ok=false;
		byte[] datosCS = null;
		byte[] aux= null;
		
		aux=leerArchivo(rutaOrigen);
		//datosCS = new byte[aux.length+32];
		datosCS=cifrar(aux);

		ok=guardarEn(rutaDestino, datosCS);

		return ok;
	}
	
	public boolean descifrarFichero(String rutaOrigen, String rutaDestino) throws IOException {
		boolean ok= false;
		byte[] datosCS = null;
		byte[] datosC = null;
		byte[] aux = null;
		
		
		datosCS=leerArchivo(rutaOrigen);
		//aux=new byte[datosCS.length-32];
		aux=descifrar(datosCS);
		System.out.println("\nSALT >>>>>>>>>>>>>>> " + Arrays.toString(salt) + "\n");
		System.out.println("\nKEY >>>>>>>>>>>>>>> " + Arrays.toString(key) + "\n");

		ok=guardarEn(rutaDestino, aux);
		
		return ok;	
	}
	
	private static byte[] concatena(byte[] a, byte[] b) {
		byte[] c = new byte[a.length + b.length];
		
		for(int i=0; i<a.length; i++) {
			c[i]=a[i];
		}
		
		for(int i=0; i<32; i++) {
			c[a.length+i]=b[i];	
		}
		
		return c;
	}
	
	private static byte[] obtenerSalt(byte[] a) {
		byte[] aux =new byte[32];
		
		if(a.length>=32) {
			for(int i=0; i<32; i++) {
				aux[i]=a[a.length-32+i];	
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
