package com.activities;


import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.Arrays;
import com.controller.CipherBox;

import android.os.Bundle;
import android.os.Environment;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class FileExplorerActivity extends Activity {
	
	private TextView path_actual;
	private List<String> items = null;
	private ListView listview;
	private File[] files = null;
	private String path_raiz = null;
	private String archivo = null;
	View popupview;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.file_explorer);
		
		//path_raiz = Environment.getExternalStorageDirectory().getAbsolutePath();
		path_raiz = "/";
		path_actual = (TextView)findViewById(R.id.label_path);
		path_actual.setText(path_raiz);

		listar_directorio((String) path_actual.getText());
		
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			@SuppressLint("NewApi") public void onItemClick(AdapterView<?> a, View v, int position, long id) {
				if(files[position].isDirectory()) {
					path_actual.setText(path_actual.getText() +"/"+ files[position].getName());
					listar_directorio(files[position].getAbsolutePath());					
				} else {
					// Cifrar el archivo
					
					//Obtencion de la dimensión de la pantalla
					Display d = getWindowManager().getDefaultDisplay();
					Point p = new Point();
					d.getSize(p);
					
				//	setContentView(R.layout.pop_up);
					LayoutInflater inflator = (LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
					popupview = inflator.inflate(R.layout.pop_up,  null);
					View parent = inflator.inflate(R.layout.activity_file_encrypter, null);
					final PopupWindow pw = new PopupWindow(popupview, p.x , p.y/2, true);
					
					pw.showAtLocation(parent, Gravity.CENTER_VERTICAL, 0, 0);
					Button close = (Button)popupview.findViewById(R.id.btnCancel);
					close.setOnClickListener(new OnClickListener(){
						@Override
						public void onClick(View popupview){
							pw.dismiss();
						}
					});
					 
					archivo=files[position].getAbsolutePath();
					final Button butcifrar = (Button) popupview.findViewById(R.id.btnEncrypt);
					
					butcifrar.setOnClickListener(new OnClickListener() {	
						public void onClick(View v) {	
							Context ctx=getApplicationContext();
							Toast t = Toast.makeText(ctx, "Procesando", Toast.LENGTH_LONG );
							t.show();
							TextView pass = (TextView) popupview.findViewById(R.id.etxtKey);
							RadioGroup rg = (RadioGroup) popupview.findViewById(R.id.radioKey);
							CipherBox cb = new CipherBox(pass.getText().toString().toCharArray(), Integer.parseInt(((RadioButton)popupview.findViewById(rg.getCheckedRadioButtonId() )).getText().toString()));
							try {
								cb.cifrarFichero(archivo, archivo+".crypt");
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} 

								t.cancel();
								t = Toast.makeText(ctx, "Archivo cifrado creado en " + archivo + ".crypt", Toast.LENGTH_SHORT );
								t.show();
								pw.dismiss();
								
								
							}
					});
					
					
					final Button butdescifrar = (Button) popupview.findViewById(R.id.btnDesencrypt);
					
					butdescifrar.setOnClickListener(new OnClickListener() {	
						public void onClick(View v) {	
							String sucio = archivo;
							archivo= sucio.substring(0, sucio.length()-6);
							Context ctx=getApplicationContext();
							Toast t = Toast.makeText(ctx, "Procesando", Toast.LENGTH_LONG );
							t.show();
							TextView pass = (TextView) popupview.findViewById(R.id.etxtKey);
							RadioGroup rg = (RadioGroup) popupview.findViewById(R.id.radioKey);
							CipherBox cb = new CipherBox(pass.getText().toString().toCharArray(), Integer.parseInt(((RadioButton)popupview.findViewById(rg.getCheckedRadioButtonId() )).getText().toString()));
							try {
								cb.descifrarFichero(archivo + ".crypt", archivo);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} 

							t.cancel();
							t = Toast.makeText(ctx, "Archivo descifrado creado en " + archivo , Toast.LENGTH_SHORT );
							t.show();
							pw.dismiss();
							}
							
					});
					
				}
			}
		});
	}
	
	// Cuando el usuario pulsa la tecla back
	@Override
	public void onBackPressed() {
		if(!path_actual.getText().equals(path_raiz)) {
			//Borrar ultimo directorio del path_actual
			String p = (String) path_actual.getText();
			int i;
			for(i=p.length()-1; p.charAt(i) != '/' ; i--);
			
			//Guardar el cambio
			path_actual.setText(p.substring(0,i));
			listar_directorio((String)path_actual.getText());			
		} else { // Es el directorio raiz
			super.onBackPressed();
		}
	}

	/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.file_explorer, menu);
		return true;
	}*/

	// Crea y lista los archivos/carpetas del directorio
	public void listar_directorio(String absolutePath) {
		
		File directory = new File(absolutePath);
		files = directory.listFiles();
		items = new ArrayList<String>();
		
		// Sort Files Array (Directories first, alphabetical order)
		Arrays.sort(files, new Comparator<File>() {
			@Override
			public int compare(File f1, File f2) {
				int result = -2;
				
				if (f1.isDirectory() && f2.isFile())
					result = -1;
				if (f1.isFile() && f2.isDirectory())
					result = 1;
				if ((f1.isDirectory() && f2.isDirectory()) || f1.isFile() && f2.isFile())
					result = f1.getName().compareToIgnoreCase(f2.getName());
					
				return result;
			}
		});
		
		// Create string array with file names
		for(File file:files) {
			if (file.isDirectory()){
				items.add(file.getName() + "/");
			}
			else
				items.add(file.getName());
		}
		
		// Meter los items en el listview
		listview = (ListView) findViewById(R.id.listView1);
		listview.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, items));
	}
}
