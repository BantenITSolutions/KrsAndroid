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
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class PortalSistemAkademikActivity extends Activity {
	String var_usr, var_pass;
	EditText usr, psw;
	TextView st;
	JSONArray str_login = null;
	private ProgressDialog pDialog;
	JSONParser jParser = new JSONParser();
	Koneksi lo_Koneksi = new Koneksi();
	String isi = lo_Koneksi.isi_koneksi();
	String link_url = isi + "login.php";

	SessionManager session;
	
    @Override
	public void onBackPressed()
	{
	    Intent intent = new Intent(this, PortalSistemAkademikActivity.class);
	    startActivity(intent);       
	}
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		CekStatusInternet cek = new CekStatusInternet();
		
		if (cek.cek_status(this)) {
			
			requestWindowFeature(Window.FEATURE_NO_TITLE);
	        setContentView(R.layout.main);
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

			session = new SessionManager(getApplicationContext());

			if (session.isLoggedIn() == false) {
				usr = (EditText) findViewById(R.id.txt_username);
				psw = (EditText) findViewById(R.id.txt_pass);
				st = (TextView) findViewById(R.id.txt_alert);

				if (getIntent().getExtras() != null) {
					Bundle b = getIntent().getExtras();
					String pesan = b.getString("alert");
					st.setVisibility(View.VISIBLE);
					st.setText(pesan);
				}

				Button reset = (Button) findViewById(R.id.btn_hapus);
				reset.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						usr.setText(link_url);
						psw.setText("");
					}
				});

				Button submit = (Button) findViewById(R.id.btn_login);
				submit.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						new getLoginData().execute();

					}
				});
			}
			else
			{
				Intent i = new Intent(getApplicationContext(),DashboardActivity.class);
				startActivity(i);
				finish();
			}
		} else {
			cek.hasil(this);
		}
    }

	class getLoginData extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(PortalSistemAkademikActivity.this);
			pDialog.setMessage("Menghubungkan ke server...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		protected String doInBackground(String... args) {
			String username = usr.getText().toString();
			String password = psw.getText().toString();

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("username", username));
			params.add(new BasicNameValuePair("password", password));

			JSONObject json = jParser.makeHttpRequest(link_url, "POST", params);

			Log.d("Create Response", json.toString());

			try {
				int success = json.getInt("success");

				if (success == 1) {
					String username_log = json.getString("username");
					String nama = json.getString("nama_mahasiswa");
					String jurusan = json.getString("jurusan");
					String angkatan = json.getString("angkatan");
					String pembimbing = json.getString("pembimbing");
					String prodi_kode = json.getString("prodi_kode");
					String semester = json.getString("kode_semester");
					String krs_id = json.getString("krs_id");
					String krs_mulai = json.getString("krs_mulai");
					String krs_selesai = json.getString("krs_selesai");
					String smt_ta = json.getString("ket_semester")+" / "+json.getString("ket_ta");
					String ipk = json.getString("ipk");
					String sks = json.getString("sks");
					session.createLoginSession(username_log, nama, jurusan, angkatan, pembimbing, prodi_kode, semester, krs_id, krs_mulai, 
							krs_selesai, smt_ta, ipk,sks);
					Intent i = new Intent(getApplicationContext(),DashboardActivity.class);
					startActivity(i);
					finish();
				} else if (success == 0) {
					Intent i = new Intent(getApplicationContext(),PortalSistemAkademikActivity.class);
					Bundle b = new Bundle();
					b.putString("alert", "Username atau Password salah...");
					i.putExtras(b);
					startActivity(i);
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
        		PortalSistemAkademikActivity.this.finish();
				startActivity(exit);
        		return true;
        	default:
        		return super.onOptionsItemSelected(item);
        }
    }
}