package com.libraries;

public class AESLib {
	
	/** CONSTANTES **
	*****************/
	
	private static byte Nr; // Numero de rondas totales. 128->10; 192->12; 256->14
	private static byte Nk; // Numero de columnas de la matriz de clave. 128->4; 192->6; 256->8
	private static byte Nb; // Numero de columnas de la matriz de estado. Siempre será 4 (bloque 128bits)

	// Matriz de sustitución bytesub
	private static short[][] scaja = {
		{0x63, 0x7C, 0x77, 0x7B, 0xF2, 0x6B, 0x6F, 0xC5, 0x30, 0x01, 0x67, 0x2B, 0xFE, 0xD7, 0xAB, 0x76},
		{0xCA, 0x82, 0xC9, 0x7D, 0xFA, 0x59, 0x47, 0xF0, 0xAD, 0xD4, 0xA2, 0xAF, 0x9C, 0xA4, 0x72, 0xC0},
		{0xB7, 0xFD, 0x93, 0x26, 0x36, 0x3F, 0xF7, 0xCC, 0x34, 0xA5, 0xE5, 0xF1, 0x71, 0xD8, 0x31, 0x15},
		{0x04, 0xC7, 0x23, 0xC3, 0x18, 0x96, 0x05, 0x9A, 0x07, 0x12, 0x80, 0xE2, 0xEB, 0x27, 0xB2, 0x75},
		{0x09, 0x83, 0x2C, 0x1A, 0x1B, 0x6E, 0x5A, 0xA0, 0x52, 0x3B, 0xD6, 0xB3, 0x29, 0xE3, 0x2F, 0x84},
		{0x53, 0xD1, 0x00, 0xED, 0x20, 0xFC, 0xB1, 0x5B, 0x6A, 0xCB, 0xBE, 0x39, 0x4A, 0x4C, 0x58, 0xCF},
		{0xD0, 0xEF, 0xAA, 0xFB, 0x43, 0x4D, 0x33, 0x85, 0x45, 0xF9, 0x02, 0x7F, 0x50, 0x3C, 0x9F, 0xA8},
		{0x51, 0xA3, 0x40, 0x8F, 0x92, 0x9D, 0x38, 0xF5, 0xBC, 0xB6, 0xDA, 0x21, 0x10, 0xFF, 0xF3, 0xD2},
		{0xCD, 0x0C, 0x13, 0xEC, 0x5F, 0x97, 0x44, 0x17, 0xC4, 0xA7, 0x7E, 0x3D, 0x64, 0x5D, 0x19, 0x73},
		{0x60, 0x81, 0x4F, 0xDC, 0x22, 0x2A, 0x90, 0x88, 0x46, 0xEE, 0xB8, 0x14, 0xDE, 0x5E, 0x0B, 0xDB},
		{0xE0, 0x32, 0x3A, 0x0A, 0x49, 0x06, 0x24, 0x5C, 0xC2, 0xD3, 0xAC, 0x62, 0x91, 0x95, 0xE4, 0x79},
		{0xE7, 0xC8, 0x37, 0x6D, 0x8D, 0xD5, 0x4E, 0xA9, 0x6C, 0x56, 0xF4, 0xEA, 0x65, 0x7A, 0xAE, 0x08},
		{0xBA, 0x78, 0x25, 0x2E, 0x1C, 0xA6, 0xB4, 0xC6, 0xE8, 0xDD, 0x74, 0x1F, 0x4B, 0xBD, 0x8B, 0x8A},
		{0x70, 0x3E, 0xB5, 0x66, 0x48, 0x03, 0xF6, 0x0E, 0x61, 0x35, 0x57, 0xB9, 0x86, 0xC1, 0x1D, 0x9E},
		{0xE1, 0xF8, 0x98, 0x11, 0x69, 0xD9, 0x8E, 0x94, 0x9B, 0x1E, 0x87, 0xE9, 0xCE, 0x55, 0x28, 0xDF},
		{0x8C, 0xA1, 0x89, 0x0D, 0xBF, 0xE6, 0x42, 0x68, 0x41, 0x99, 0x2D, 0x0F, 0xB0, 0x54, 0xBB, 0x16}};
	
	// Matriz inversa de sustitución bytesub
	private static short[][] inv_scaja = {
		{0x52, 0x09, 0x6A, 0xD5, 0x30, 0x36, 0xA5, 0x38, 0xBF, 0x40, 0xA3, 0x9E, 0x81, 0xF3, 0xD7, 0xFB},
		{0x7C, 0xE3, 0x39, 0x82, 0x9B, 0x2F, 0xFF, 0x87, 0x34, 0x8E, 0x43, 0x44, 0xC4, 0xDE, 0xE9, 0xCB},
		{0x54, 0x7B, 0x94, 0x32, 0xA6, 0xC2, 0x23, 0x3D, 0xEE, 0x4C, 0x95, 0x0B, 0x42, 0xFA, 0xC3, 0x4E},
		{0x08, 0x2E, 0xA1, 0x66, 0x28, 0xD9, 0x24, 0xB2, 0x76, 0x5B, 0xA2, 0x49, 0x6D, 0x8B, 0xD1, 0x25},
		{0x72, 0xF8, 0xF6, 0x64, 0x86, 0x68, 0x98, 0x16, 0xD4, 0xA4, 0x5C, 0xCC, 0x5D, 0x65, 0xB6, 0x92},
		{0x6C, 0x70, 0x48, 0x50, 0xFD, 0xED, 0xB9, 0xDA, 0x5E, 0x15, 0x46, 0x57, 0xA7, 0x8D, 0x9D, 0x84},
		{0x90, 0xD8, 0xAB, 0x00, 0x8C, 0xBC, 0xD3, 0x0A, 0xF7, 0xE4, 0x58, 0x05, 0xB8, 0xB3, 0x45, 0x06},
		{0xD0, 0x2C, 0x1E, 0x8F, 0xCA, 0x3F, 0x0F, 0x02, 0xC1, 0xAF, 0xBD, 0x03, 0x01, 0x13, 0x8A, 0x6B},
		{0x3A, 0x91, 0x11, 0x41, 0x4F, 0x67, 0xDC, 0xEA, 0x97, 0xF2, 0xCF, 0xCE, 0xF0, 0xB4, 0xE6, 0x73},
		{0x96, 0xAC, 0x74, 0x22, 0xE7, 0xAD, 0x35, 0x85, 0xE2, 0xF9, 0x37, 0xE8, 0x1C, 0x75, 0xDF, 0x6E},
		{0x47, 0xF1, 0x1A, 0x71, 0x1D, 0x29, 0xC5, 0x89, 0x6F, 0xB7, 0x62, 0x0E, 0xAA, 0x18, 0xBE, 0x1B},
		{0xFC, 0x56, 0x3E, 0x4B, 0xC6, 0xD2, 0x79, 0x20, 0x9A, 0xDB, 0xC0, 0xFE, 0x78, 0xCD, 0x5A, 0xF4},
		{0x1F, 0xDD, 0xA8, 0x33, 0x88, 0x07, 0xC7, 0x31, 0xB1, 0x12, 0x10, 0x59, 0x27, 0x80, 0xEC, 0x5F},
		{0x60, 0x51, 0x7F, 0xA9, 0x19, 0xB5, 0x4A, 0x0D, 0x2D, 0xE5, 0x7A, 0x9F, 0x93, 0xC9, 0x9C, 0xEF},
		{0xA0, 0xE0, 0x3B, 0x4D, 0xAE, 0x2A, 0xF5, 0xB0, 0xC8, 0xEB, 0xBB, 0x3C, 0x83, 0x53, 0x99, 0x61},
		{0x17, 0x2B, 0x04, 0x7E, 0xBA, 0x77, 0xD6, 0x26, 0xE1, 0x69, 0x14, 0x63, 0x55, 0x21, 0x0C, 0x7D}};
	
	// Constantes para generar las subclaves
	private static int Rcon[] = {
		0x8d, 0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, 0x80, 0x1b, 0x36, 0x6c, 0xd8, 0xab, 0x4d, 0x9a, 
		0x2f, 0x5e, 0xbc, 0x63, 0xc6, 0x97, 0x35, 0x6a, 0xd4, 0xb3, 0x7d, 0xfa, 0xef, 0xc5, 0x91, 0x39, 
		0x72, 0xe4, 0xd3, 0xbd, 0x61, 0xc2, 0x9f, 0x25, 0x4a, 0x94, 0x33, 0x66, 0xcc, 0x83, 0x1d, 0x3a, 
		0x74, 0xe8, 0xcb, 0x8d, 0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, 0x80, 0x1b, 0x36, 0x6c, 0xd8, 
		0xab, 0x4d, 0x9a, 0x2f, 0x5e, 0xbc, 0x63, 0xc6, 0x97, 0x35, 0x6a, 0xd4, 0xb3, 0x7d, 0xfa, 0xef, 
		0xc5, 0x91, 0x39, 0x72, 0xe4, 0xd3, 0xbd, 0x61, 0xc2, 0x9f, 0x25, 0x4a, 0x94, 0x33, 0x66, 0xcc, 
		0x83, 0x1d, 0x3a, 0x74, 0xe8, 0xcb, 0x8d, 0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, 0x80, 0x1b, 
		0x36, 0x6c, 0xd8, 0xab, 0x4d, 0x9a, 0x2f, 0x5e, 0xbc, 0x63, 0xc6, 0x97, 0x35, 0x6a, 0xd4, 0xb3, 
		0x7d, 0xfa, 0xef, 0xc5, 0x91, 0x39, 0x72, 0xe4, 0xd3, 0xbd, 0x61, 0xc2, 0x9f, 0x25, 0x4a, 0x94, 
		0x33, 0x66, 0xcc, 0x83, 0x1d, 0x3a, 0x74, 0xe8, 0xcb, 0x8d, 0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 
		0x40, 0x80, 0x1b, 0x36, 0x6c, 0xd8, 0xab, 0x4d, 0x9a, 0x2f, 0x5e, 0xbc, 0x63, 0xc6, 0x97, 0x35, 
		0x6a, 0xd4, 0xb3, 0x7d, 0xfa, 0xef, 0xc5, 0x91, 0x39, 0x72, 0xe4, 0xd3, 0xbd, 0x61, 0xc2, 0x9f, 
		0x25, 0x4a, 0x94, 0x33, 0x66, 0xcc, 0x83, 0x1d, 0x3a, 0x74, 0xe8, 0xcb, 0x8d, 0x01, 0x02, 0x04, 
		0x08, 0x10, 0x20, 0x40, 0x80, 0x1b, 0x36, 0x6c, 0xd8, 0xab, 0x4d, 0x9a, 0x2f, 0x5e, 0xbc, 0x63, 
		0xc6, 0x97, 0x35, 0x6a, 0xd4, 0xb3, 0x7d, 0xfa, 0xef, 0xc5, 0x91, 0x39, 0x72, 0xe4, 0xd3, 0xbd, 
		0x61, 0xc2, 0x9f, 0x25, 0x4a, 0x94, 0x33, 0x66, 0xcc, 0x83, 0x1d, 0x3a, 0x74, 0xe8, 0xcb, 0x8d };

	// Matriz para operación mixcolumns
	private static byte matriz_mixcolumns[][] = {
		{0x02, 0x03, 0x01, 0x01},
		{0x01, 0x02, 0x03, 0x01},
		{0x01, 0x01, 0x02, 0x03},
		{0x03, 0x01, 0x01, 0x02}};
	
	// Matriz para la operación inversa de mixcolumns
	private static byte inv_matriz_mixcolumns[][] = {
		{0x0E, 0x0B, 0x0D, 0x09},
		{0x09, 0x0E, 0x0B, 0x0D},
		{0x0D, 0x09, 0x0E, 0x0B},
		{0x0B, 0x0D, 0x09, 0x0E}};
	
	/** CONSTRUCTOR **
	*****************/
	
	/**
	 * Constructor
	 * @param long_key Longitud de clave (128, 192, 256)
	 */
	public AESLib(int long_key) {
		Nk = (byte) (long_key/32); // Nº de columnas de la matriz de clave
		Nr = (byte) (Nk+6); // Nº de rondas (Nr=Nk+6)
		Nb = 4; // Nº columnas de la matriz de estado (siempre es 4)
	}

	/** FUNCIONES COMPLEMENTARIAS **
	********************************/
	
	/**
	 * Calcula la operacion XOR entre dos arrays de bytes del mismo tamaño
	 * @param a		Primer array de bytes
	 * @param b		Segundo array de bytes
	 * @return		Un array de bytes con el resultado de aplicar la XOR a 'a' y 'b'
	 */
	private static byte[] calcularXORArray(byte[] a, byte[] b) {
		int tam = a.length;
		byte[] tmp = new byte[tam];
		
		for(int i=0; i < tam ;i++) {
			tmp[i] = (byte) (a[i] ^ b[i]);
		}
		return tmp;
	}
	
	/**
	 * Rota UNA posicion a la IZQUIERDA un array de bytes
	 * @param fila	Array de bytes a rotar
	 * @return		Array con un byte rotado una posición a la izquierda
	 */
	private static byte[] RotLeft(byte[] fila) {
		
		byte primer_valor = fila[0];
		int i=0;
		
		while(i<fila.length-1) {
			fila[i] = fila[i+1];
			i++;
		}
		fila[i] = primer_valor;
		return fila;
	}
	
	/**
	 * Rota UNA posicion a la DERECHA un array de bytes
	 * @param fila		Array de bytes a rotar
	 * @return			Array con un byte rotado una posición a la derecha
	 */
	private static byte[] RotRight(byte[] fila) {
		
		byte tam = (byte) fila.length;
		byte ultimo_valor = fila[tam-1];
		int i=tam-1;
		
		while(i>0) {
			fila[i] = fila[i-1];
			i--;
		}
		fila[0] = ultimo_valor;
		return fila;
	}
	
	/**
	 * Aplica la s-caja a cada uno de los bytes del array que se le pasa como parámetro.
	 * Los elementos de la s_caja son cadenas de 16bits (short).
	 * 0xFF convierte el decimal con signo (short) en un decimal de 0 a 255
	 * @param b		Array de bytes a aplicar la s_caja
	 * @return		Array de bytes con la sustitución con la s_caja
	 */
	private static byte[] Sub(byte[] b) {
		byte[] tmp = new byte[b.length];
		
		for(int i=0;i<tmp.length;i++) {
			tmp[i] = (byte) (scaja[(b[i] & 0x00f0) >> 4][b[i] & 0x000f] & 0xFF);
		}
		return tmp;
	}
	
	/**
	 * Calcula la operación mixcolumn para cada par.
	 * @param a		Byte de la matriz_estado
	 * @param b		Byte de la matriz_mixcolumns (0x01, 0x02 ó 0x03)
	 * @return		Resultado de aplicar la operación mixcolumn al par
	 */
	private byte calcularByteColumn(byte a, byte b) {
		byte tmp = 0;
		
		switch(b) {
			case 0x01: 	tmp = a;
						break;
			case 0x02:	tmp = (byte) (a << 1);
						if((a & 0x80) == 0x80) // 0x80 = 10000000 ; Comprobar que el primer bit no sea 0
							tmp ^= 0x1b;
						break;
			case 0x03:	tmp = (byte) (a << 1);
						if((a & 0x80) == 0x80) // 0x80 = 10000000 ; Comprobar que el primer bit no sea 0
							tmp ^= 0x1b;
						tmp = (byte) (tmp ^ a);
		}
		return tmp;
	}
	
	/** CIFRAR **
	*************/
	
	/**
	 * Genera las subclaves para cada ronda.
	 * @param K		Vector de 4*Nk bytes
	 * @param Nb	Nº Columnas matriz de estado
	 * @param Nk	Nº Columnas matriz de clave
	 * @param Nr	Nº Rondas a aplicar
	 * @return 		Devuelve una matriz de matrices. Ej. Para Nr=10 (+1 ronda inicial) tendra 44 filas (11 matrices)
	 */
	private static byte[][] crearSubclaves(byte[] K) {

		byte[][] W = new byte[(Nr+1)*Nb][4];
		byte i = 0;
		
		while(i<Nk) {
			W[i][0] = K[4*i];
			W[i][1] = K[4*i + 1];
			W[i][2] = K[4*i + 2];
			W[i][3] = K[4*i + 3];
			i++;
		}
		
		for(i=Nk; i<Nb*(Nr+1) ;i++) {
			byte[] tmp = new byte[4];
			for(int k = 0;k<4;k++)
				tmp[k] = W[i-1][k];
			if(i%Nk == 0) {
				tmp = Sub(RotLeft(tmp));
				tmp[0] = (byte) (tmp[0] ^ (Rcon[i/Nk]));
			}
			if(Nk>6 && i%Nk==4)
				tmp = Sub(tmp);
			
			W[i] = calcularXORArray(W[i-Nk], tmp);
		}
		
		return W;

	}
	
	/**
	 * Por cada elemento de la matriz de estados se divide en dos partes de 4bits (fila y columna) y se busca el nuevo valor
	 * en la s_caja.
	 * @param matriz_estado		Matriz estado
	 * @return					Resultado de aplicar la operación byteSub a la matriz estado
	 */	
	private static byte[][] byteSub(byte[][] matriz_estado) {
		// Cada elem de la matriz tiene 8bits		
		byte[][] tmp = new byte[matriz_estado.length][matriz_estado[0].length];
		
		for(int fil=0; fil<matriz_estado.length ;fil++)
			tmp[fil] = Sub(matriz_estado[fil]);
		
		return tmp;
	}
	
	/**
	 * Rotación a izquierda tantos bytes como indique el numero de fila.
	 * @param matriz_estado		Matriz de estado
	 * @return					Resultado de aplicar la operación shiftRow a la matriz de estado
	 */
	private byte[][] shiftRow(byte[][] matriz_estado) {
		byte num_filas = (byte) matriz_estado.length;
		byte[][] tmp = new byte[num_filas][matriz_estado[0].length];
		
		// La primera fila nunca rota
		tmp[0] = matriz_estado[0];
		
		// Rotamos las filas 2, 3 y 4; 1, 2 y 3 veces respectivamente
		for(byte fil=0; fil<num_filas ;fil++){
			for(byte cont=0; cont<fil ;cont++)
				tmp[fil] = RotLeft(matriz_estado[fil]);
		}
		
		return tmp;
	}
	
	/**
	 * Aplica la operación XOR a cada uno de los elementos de matriz_estado con la matriz_subclaves (Por columnas).
	 * La matriz de subclaves contiene (Nr+1)*4 filas.
	 * @param matriz_estado		Matriz de estado
	 * @param matriz_subclaves	Matriz de subclaves
	 * @param ronda				Nº de ronda actual
	 * @return					Resultado de aplicar la operación XOR a cada columna de las matrices estado y subclaves.
	 */
	private static byte[][] addRoundKey(byte[][] matriz_estado, byte[][] matriz_subclaves, int ronda) {
		byte[][] tmp = new byte[matriz_estado.length][matriz_estado[0].length];
		byte Nb = (byte) matriz_estado.length; // Nº columnas matriz estado
		
		for (int col = 0; col < Nb; col++) {
			for (int fil = 0; fil < 4; fil++)
				tmp[fil][col] = (byte) (matriz_estado[fil][col] ^ matriz_subclaves[ronda * Nb + col][fil]);
		}
		return tmp;
	}
	
	/**
	 * Devuelve la nueva matriz_estado despues de realizar la operación mixcolumn a toda la matriz estado anterior.
	 * @param matriz_estado		Matriz de estado
	 * @return					Resultado de aplicar mixColumns a la matriz de estado.
	 */
	private byte[][] mixColumns(byte[][] matriz_estado) {
		byte Nb = (byte) matriz_estado[0].length; 		// Nº columnas de la matriz_estado
		byte num_filas = (byte) matriz_estado.length; 	// Nº filas de la matriz_estado
		byte[][] tmp = new byte[num_filas][Nb];
		
		for(int col=0; col<Nb ;col++) {
			for(int fil=0; fil<num_filas ;fil++) {
				tmp[fil][col] = (byte) (calcularByteColumn(matriz_estado[0][col], matriz_mixcolumns[fil][0])
						  			  ^ calcularByteColumn(matriz_estado[1][col], matriz_mixcolumns[fil][1])
						  			  ^ calcularByteColumn(matriz_estado[2][col], matriz_mixcolumns[fil][2])
						  			  ^ calcularByteColumn(matriz_estado[3][col], matriz_mixcolumns[fil][3]));
			}
		}
		return tmp;
	}
	
	/**
	 * Cifra un bloque de datos de 128bits.
	 * @param bloque_datos		Array de datos a encriptar. Deben ser 128bits.
	 * @param matriz_subclaves	Matriz de subclaves
	 * @param Nr				Nº totales de rondas
	 * @return					Array con el bloque cifrado.
	 */
	private byte[] cifrarBloque(byte[] bloque_datos, byte[][] matriz_subclaves) {
		
		byte[] result = new byte[bloque_datos.length];
		byte[][] matriz_estado = new byte[4][4]; // Matriz cuadrada 4x4 (4*4*8=128bits)
		
		// Transforma el bloque de datos en una matriz
		for (int i = 0; i < bloque_datos.length; i++)
			matriz_estado[i/4][i % 4] = bloque_datos[i%4*4+i/4];
		
		matriz_estado = addRoundKey(matriz_estado, matriz_subclaves, 0); // Ronda inicial
		for(int ronda=1; ronda<Nr ;ronda++) {
			matriz_estado = byteSub(matriz_estado);
			matriz_estado = shiftRow(matriz_estado);
			matriz_estado = mixColumns(matriz_estado);
			matriz_estado = addRoundKey(matriz_estado, matriz_subclaves, ronda);
		}
		// Ultima ronda
		matriz_estado = byteSub(matriz_estado);
		matriz_estado = shiftRow(matriz_estado);
		matriz_estado = addRoundKey(matriz_estado, matriz_subclaves, Nr);
		
		// Transformar la matriz de estado en un array
		for (int i = 0; i < bloque_datos.length; i++)
			result[i%4*4+i/4] = matriz_estado[i/4][i%4];
		
		return result;
	}
	
	/**
	 * Cifra array de datos con AES128, AES192 y AES256 modo ECB. El relleno es PKCS#5,
	 * PKCS#5 -> Rellenar con tantos bytes como se necesiten y el valor de los bytes será el mismo
	 * 			 que el número de los bytes introducidos.
	 * @param data	Datos para cifrar. Sin limite de tamaño.
	 * @param key	Clave. Puede ser de 128, 192 o 256 bits.
	 * @return		Datos ya cifrados
	 */
	public byte[] cifrar(byte[] data, byte[] key) {
		byte[][] matriz_subclaves = crearSubclaves(key); // Genera las matrices subclaves de 128bits
		byte[] bloque = new byte[16];
		int tam_data = data.length;
		int tam_bloque = bloque.length;
		int padding=16; // Nº de bytes que hay que rellenar (Como minimo un bloque de 16bytes)
		if(tam_data%16!=0)// Si el bloque es multiplo de 16 entonces no habra que rellenar. (padding=0)
			padding = 16 - tam_data%16;
		byte[] result = new byte[tam_data + padding];
		int num_bloques = tam_data/16; // Nº de bloques de 128bits(16bytes) que podemos llenar con data[].
				
		for(int i=0; i<num_bloques ;i++) {
			System.arraycopy(data, i*16, bloque, 0, tam_bloque);
			bloque = cifrarBloque(bloque,matriz_subclaves);
			System.arraycopy(bloque, 0, result, i*16, tam_bloque);
		}
		
		// Añadir el relleno (siempre hay relleno)
		if(tam_data%16!=0) {// Bloque con datos y padding
			System.arraycopy(data, num_bloques*tam_bloque, bloque, 0, tam_bloque-padding);
			for(int i=tam_bloque-padding;i<tam_bloque;i++)
				bloque[i] = (byte) padding;
					
		} else { // Bloque entero de padding
			for(int i=tam_bloque;i<tam_bloque;i++)
				bloque[i] = (byte) padding;
		}
		
		bloque = cifrarBloque(bloque,matriz_subclaves);
		System.arraycopy(bloque, 0, result, num_bloques*16, tam_bloque);
		
		return result;
	}
	
	/** DESCIFRAR **
	****************/
	
	/**
	 * Rotación a derecha tantos bytes como indique el numero de fila
	 * @param matriz_estado		Matriz de estado
	 * @return					Resultado de aplicar la operación shiftRow a la inversa
	 */
	private static byte[][] invShiftRow(byte[][] matriz_estado) {
		byte num_filas = (byte) matriz_estado.length;
		byte[][] tmp = new byte[num_filas][matriz_estado[0].length];
		
		// La primera fila nunca rota
		tmp[0] = matriz_estado[0];
		
		// Rotamos las filas 2, 3 y 4; 1, 2 y 3 veces respectivamente
		for(byte fil=0; fil<num_filas ;fil++){
			for(byte cont=0; cont<fil ;cont++)
				tmp[fil] = RotRight(matriz_estado[fil]);
		}
		
		return tmp;
	}
	
	/**
	 * Calcula la operación mixcolumn inversa para cada par.
	 * @param a		Byte de la matriz_estado
	 * @param b		Byte de la inv_matriz_mixcolumns
	 * @return		Resultado de aplicar la operación mixColum para cada par
	 */
	private static byte invCalcularByteColumn(byte a, byte b) {
		byte aa = a, bb = b, r = 0, t;
		while (aa != 0) {
			if ((aa & 1) != 0)
				r = (byte) (r ^ bb);
			t = (byte) (bb & 0x80);
			bb = (byte) (bb << 1);
			if (t != 0)
				bb = (byte) (bb ^ 0x1b);
			aa = (byte) ((aa & 0xff) >> 1);
		}
		return r;
	}
	/**
	 * Devuelve la nueva matriz_estado despues de realizar la operación mixcolumn a toda la matriz estado anterior.
	 * @param matriz_estado		Matriz de estado
	 * @return					Resultado de aplicar la operación mixColum para toda la matriz de estado
	 */
	private static byte[][] invMixColumns(byte[][] matriz_estado) {
		byte Nb = (byte) matriz_estado[0].length; 		// Nº columnas de la matriz_estado
		byte num_filas = (byte) matriz_estado.length; 	// Nº filas de la matriz_estado
		byte[][] tmp = new byte[num_filas][Nb];
		
		for(int col=0; col<Nb ;col++) {
			for(int fil=0; fil<num_filas ;fil++) {
				tmp[fil][col] = (byte) (invCalcularByteColumn(matriz_estado[0][col], inv_matriz_mixcolumns[fil][0])
						  			  ^ invCalcularByteColumn(matriz_estado[1][col], inv_matriz_mixcolumns[fil][1])
						  			  ^ invCalcularByteColumn(matriz_estado[2][col], inv_matriz_mixcolumns[fil][2])
						  			  ^ invCalcularByteColumn(matriz_estado[3][col], inv_matriz_mixcolumns[fil][3]));
			}
		}
		return tmp;
	}
	
	/**
	 * Aplica la inv_scaja a cada uno de los bytes del array que se le pasa como parámetro.
	 * Los elementos de la s_caja son cadenas de 16bits (short).
	 * 0xFF convierte el decimal con signo (short) en un decimal de 0 a 255
	 * @param b		Array de bytes a aplicar la s_caja a la inversa
	 * @return		Array de bytes con la sustitución con la s_caja	a la inversa
	 */
	private static byte[] invSub(byte[] b) {
		byte[] tmp = new byte[b.length];
		
		for(int i=0;i<tmp.length;i++) {
			tmp[i] = (byte) (inv_scaja[(b[i] & 0x00f0) >> 4][b[i] & 0x000f] & 0xFF);
		}
		return tmp;
	}
	
	/**
	 * Por cada elemento de la matriz de estados se divide en dos partes de 4bits (fila y columna) y se busca el nuevo valor
	 * en la inv_scaja.
	 * @param matriz_estado		Matriz de estado
	 * @return					Resultado de aplicar la operación byteSub a toda la matriz de estado
	 */
	private static byte[][] invByteSub(byte[][] matriz_estado) {
		// Cada elem de la matriz tiene 8bits
		byte[][] tmp = new byte[matriz_estado.length][matriz_estado[0].length];
		
		for(int fil=0; fil<matriz_estado.length ;fil++)
			tmp[fil] = invSub(matriz_estado[fil]);
		
		return tmp;
	}
	
	/**
	 * Descifra un bloque de datos de 16bytes
	 * @param bloque			Bloque de 16bytes a descifrar
	 * @param matriz_subclaves	Matriz de subclaves
	 * @return					Bloque de 16bytes cifrado
	 */
	private static byte[] descifrarBloque(byte[] bloque, byte[][] matriz_subclaves) {
		
		byte[] tmp = new byte[bloque.length];
		byte[][] matriz_estado = new byte[4][Nb];

		for (int i = 0; i < bloque.length; i++)
			matriz_estado[i/4][i %4] = bloque[i%4*4+i/4];

		matriz_estado = addRoundKey(matriz_estado, matriz_subclaves, Nr); // Ronda inicial
		matriz_estado = invShiftRow(matriz_estado);
		matriz_estado = invByteSub(matriz_estado);
		
		for (int round = Nr-1; round >=1; round--) {
			matriz_estado = addRoundKey(matriz_estado, matriz_subclaves, round);
			matriz_estado = invMixColumns(matriz_estado);
			matriz_estado = invShiftRow(matriz_estado);
			matriz_estado = invByteSub(matriz_estado);						
		}

		matriz_estado = addRoundKey(matriz_estado, matriz_subclaves, 0);

		for (int i = 0; i < tmp.length; i++)
			tmp[i%4*4+i/4] = matriz_estado[i / 4][i%4];

		return tmp;
	}
	
	/**
	 * Descifra todo un conjunto de datos.
	 * @param data	Array de bytes con el datagrama a descifrar
	 * @param key	Clave usada a la hora de cifrar
	 * @return		Array de bytes con el datagrama descifrado
	 */
	public byte[] descifrar(byte[] data, byte[] key){

		int i;
		byte[] tmp = new byte[data.length];
		byte[] bloque = new byte[16];
		byte[][] matriz_subclaves = crearSubclaves(key); // Genera las matrices subclaves de 128bits

		for (i = 0; i < data.length; i++) {
			if (i > 0 && i % 16 == 0) {
				bloque = descifrarBloque(bloque, matriz_subclaves);
				System.arraycopy(bloque, 0, tmp, i - 16, bloque.length);
			}
			if (i < data.length)
				bloque[i % 16] = data[i];
		}
		bloque = descifrarBloque(bloque, matriz_subclaves);
		System.arraycopy(bloque, 0, tmp, i - 16, bloque.length);

		tmp = borrarPadding(tmp);

		return tmp;
	}
	
	/**
	 * Elimina el padding añadido en la operación de cifrado
	 * @param data		Array de datos con padding a eliminar
	 * @return			Array de datos con padding eliminado
	 */
	private static byte[] borrarPadding(byte[] data) {
		
		int tam_data = data.length;
		byte num_padding = data[tam_data-1];// El ultimo byte tiene el numero de padding
		boolean borrar=true;
		
		// Comprobar que los ultimos bytes sean == a num_padding (Comprobar que es correcto)
		for(int i=tam_data-1; i>tam_data-1-num_padding ;i--) {
			if(data[i] != num_padding)
				borrar=false;				
		}
		byte[] tmp = new byte[data.length-num_padding];	
		if(borrar)
			System.arraycopy(data,0,tmp,0,tam_data-num_padding);
		
		return tmp;
	}
	
}