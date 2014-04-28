package com.activities;


import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import com.libraries.AESLib;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class FileExplorerActivity extends Activity {
	
	private TextView path_actual;
	private List<String> items = null;
	private ListView listview;
	private File[] files = null;
	private String path_raiz = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.file_explorer);
		
		path_raiz = Environment.getRootDirectory().getAbsolutePath();
		path_actual = (TextView)findViewById(R.id.label_path);
		path_actual.setText(path_raiz);

		listar_directorio((String) path_actual.getText());
		
		listview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> a, View v, int position, long id) {
				if(files[position].isDirectory()) {
					path_actual.setText(path_actual.getText() +"/"+ files[position].getName());
					listar_directorio(files[position].getAbsolutePath());					
				} else {
					// Cifrar el archivo
					
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
