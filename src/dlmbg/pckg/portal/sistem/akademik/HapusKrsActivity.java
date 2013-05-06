package dlmbg.pckg.portal.sistem.akademik;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class HapusKrsActivity extends Activity {
	String var_usr, var_pass;
	EditText usr, psw;
	TextView st;
	TextView txt_hapus_krs_detail;
	JSONArray str_login = null;
	private ProgressDialog pDialog;
	JSONParser jParser = new JSONParser();
	Koneksi lo_Koneksi = new Koneksi();
	String isi = lo_Koneksi.isi_koneksi();
	String link_url = isi + "hapus_krs.php";

	SessionManager session;
	
    @Override
	public void onBackPressed()
	{
	    Intent intent = new Intent(this, KrsActivity.class);
	    startActivity(intent);       
	}
    public void onCreate(Bundle savedInstanceState) {
    	 super.onCreate(savedInstanceState);
         
         CekStatusInternet cek = new CekStatusInternet();
 		
 		if (cek.cek_status(this)) {
 	        requestWindowFeature(Window.FEATURE_NO_TITLE);
 	        setContentView(R.layout.hapus_krs);

	        Bundle b = getIntent().getExtras();
			final String krs_detail = b.getString("krs_detail");
			String nama_mk = b.getString("nama_mk");
			String sks_mk = b.getString("sks_mk");
			String smt_mk = b.getString("smt_mk");

 	        TextView txt_hapus_mk = (TextView) findViewById(R.id.tampil_hapus_mk);
 	        TextView txt_hapus_sks = (TextView) findViewById(R.id.tampil_hapus_sks);
 	        TextView txt_hapus_smt = (TextView) findViewById(R.id.tampil_hapus_smt);
 	        txt_hapus_krs_detail = (TextView) findViewById(R.id.tampil_hapus_krs_detail);
 	        
 	        txt_hapus_mk.setText(nama_mk);
 	        txt_hapus_sks.setText(sks_mk);
 	        txt_hapus_smt.setText(smt_mk);
 	        txt_hapus_krs_detail.setText(krs_detail);
			
 	        Button btn_krs = (Button) findViewById(R.id.btn_hapus_krs);
 	        btn_krs.setText("Hapus Pilihan mata Kuliah");
 	
 	        btn_krs.setOnClickListener(new View.OnClickListener() {
 				@Override
 				public void onClick(View view) {
 		 	        new kirimData().execute();
 				}
 			});
 	        
 			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
 	        
 		}
 		else
 		{
 			cek.hasil(this);
 		}
    }

	class kirimData extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(HapusKrsActivity.this);
			pDialog.setMessage("Menghubungkan ke server...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		protected String doInBackground(String... args) {
			String krs_detail = txt_hapus_krs_detail.getText().toString();

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("krsdtKrsId", krs_detail));

			
			JSONObject json = jParser.makeHttpRequest(link_url, "POST", params);

			Log.d("Create Response", json.toString());

			try {
				int success = json.getInt("success");

				if (success == 1) {
					Intent i = new Intent(getApplicationContext(),KrsActivity.class);
					startActivity(i);
					finish();
				} else if (success == 0) {
					finish();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute(String file_url) {
			pDialog.dismiss();
		}
	}
 
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.opt_menu, menu);
        return true;
    }
    
    @SuppressWarnings("deprecation")
	public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        	case R.id.url:
        		AlertDialog alertDialog;
        		alertDialog = new AlertDialog.Builder(this).create();
        		alertDialog.setTitle("Portal Sistem Akademik");
        		alertDialog.setMessage("Aplikasi SIAKAD berbasis Android ini merupakan salah satu dari sekian banyak proyek 2M" +
        				" serta segelintir penelitian yang saya kerjakan di kampus. Semoga aplikasi ini bisa bermanfaat untuk " +
        				" kita semua.\n\nSalam, Gede Lumbung\nhttp://gedelumbung.com");
        		alertDialog.setButton("#OKOK", new DialogInterface.OnClickListener() {
        		    @Override
        		    public void onClick(final DialogInterface dialog, final int which) {
        		        dialog.dismiss();
        		    }
        		});
        		alertDialog.show();
        		return true;
        	case R.id.keluar:
				Intent exit = new Intent(Intent.ACTION_MAIN);
				exit.addCategory(Intent.CATEGORY_HOME); exit.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        		HapusKrsActivity.this.finish();
				startActivity(exit);
        		return true;
        	default:
        		return super.onOptionsItemSelected(item);
        }
    }
}