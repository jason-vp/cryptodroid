package com.activities;


import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import android.support.v7.app.ActionBarActivity;
//import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.*;
import com.controller.CipherBox;

//import android.os.Build;

public class TextEncrypterActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_text_encrypter);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
			}
		

		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.text_encrypter, menu);
		
		final Button butcifrar = (Button) findViewById(R.id.btnEncrypt);
		
		butcifrar.setOnClickListener(new OnClickListener() {	
			public void onClick(View v) {	
				Calendar c = Calendar.getInstance();
				Context ctx=getApplicationContext();
				Toast t = Toast.makeText(ctx, "Procesando", Toast.LENGTH_LONG );
				t.show();
				Charset charset = Charset.forName("ISO-8859-1");
				byte[] datos;
				TextView textView = (TextView) findViewById(R.id.etxt2Encrypt);
				TextView pass = (TextView) findViewById(R.id.etxtKey);
				RadioGroup rg = (RadioGroup) findViewById(R.id.radioKey);
				CheckBox guardar= (CheckBox) findViewById(R.id.checkGuardar);
				CipherBox cb = new CipherBox(pass.getText().toString().toCharArray(), Integer.parseInt(((RadioButton)findViewById(rg.getCheckedRadioButtonId() )).getText().toString()));
				System.out.println("\nDatos obtenidos dts :\n");

				datos=cb.cifrar(textView.getText().toString().getBytes(charset));

					String imp = new String(datos, charset);
					textView.setText(imp);
					System.out.println("\nDatos impresos:\n" + imp + "\n");

					if(guardar.isChecked()) {
						SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy:HH:mm:ss");
						String formattedDate = df.format(c.getTime());
						cb.guardar(formattedDate +".txt.crypt", datos, ctx);
						Toast t2 = Toast.makeText(ctx, "Archivo creado en " + "/data/com.activities/files" + formattedDate + ".text.crypt", Toast.LENGTH_SHORT );
						t2.show();
					}
					t.cancel();
				}
		});
		
		final Button butdescifrar = (Button) findViewById(R.id.btnDesencrypt);
		
		butdescifrar.setOnClickListener(new OnClickListener() {	
			public void onClick(View v) {	
				Context ctx=getApplicationContext();
				Toast t = Toast.makeText(ctx, "Procesando", Toast.LENGTH_LONG );
				t.show();
				Calendar c = Calendar.getInstance();
				Charset charset = Charset.forName("ISO-8859-1");
				byte[] datos;
				TextView textView = (TextView) findViewById(R.id.etxt2Encrypt);
				TextView pass = (TextView) findViewById(R.id.etxtKey);
				RadioGroup rg = (RadioGroup) findViewById(R.id.radioKey);
				CheckBox guardar= (CheckBox) findViewById(R.id.checkGuardar);
				CipherBox cb = new CipherBox(pass.getText().toString().toCharArray(), Integer.parseInt(((RadioButton)findViewById(rg.getCheckedRadioButtonId() )).getText().toString()));

				datos=cb.descifrar(textView.getText().toString().getBytes(charset));

				String imp = new String(datos, charset);
				textView.setText(imp);
				System.out.println("\nDatos impresos:\n" + imp + "\n");
					if(guardar.isChecked()) {
						SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy:HH:mm:ss");
						String formattedDate = df.format(c.getTime());
						cb.guardar(formattedDate +".txt", datos, ctx);
						Toast t2 = Toast.makeText(ctx, "Archivo creado en " + "/data/com.activities/files" + formattedDate + ".text", Toast.LENGTH_SHORT );
						t2.show();
					}
					t.cancel();
				}
				
		});
		
		final Button butcopy = (Button) findViewById(R.id.btnCopiar);
		
		butcopy.setOnClickListener(new OnClickListener() {	
			@TargetApi(Build.VERSION_CODES.HONEYCOMB)
			public void onClick(View v) {
				Charset charset = Charset.forName("ISO-8859-1");
				ClipboardManager clipboard = (ClipboardManager)
				        getSystemService(Context.CLIPBOARD_SERVICE);
				TextView textView = (TextView) findViewById(R.id.etxt2Encrypt);
				ClipData clip = ClipData.newPlainText("Texto CryptoDroid",textView.getText().toString());
				clipboard.setPrimaryClip(clip);
				
			}
		});
		
		final Button butpaste = (Button) findViewById(R.id.btnPegar);
		
		butcopy.setOnClickListener(new OnClickListener() {	
			@TargetApi(Build.VERSION_CODES.HONEYCOMB)
			public void onClick(View v) {
				ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
	
				
				String pasteData = "";
				 ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);
				pasteData = item.getText().toString();	
				TextView textView = (TextView) findViewById(R.id.etxt2Encrypt);
				textView.setText(pasteData);
				
			}
		});
		
		final Button butshare = (Button) findViewById(R.id.btnCompartir);
		
		butshare.setOnClickListener(new OnClickListener() {	
			public void onClick(View v) {
				Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND); 
			    sharingIntent.setType("text/plain");
				TextView textView = (TextView) findViewById(R.id.etxt2Encrypt);
			    String shareBody = textView.getText().toString();
			    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
			    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
			    startActivity(Intent.createChooser(sharingIntent, "Share via"));
			}
		});
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_text_encrypter,
					container, false);
			
			return rootView;
		}
	}
	
	/**
	 * Borra el hint cuando se hace el click para introducir la contraseña
	 */	
	public void deleteHint(){
		
		
	}
	
	

}
