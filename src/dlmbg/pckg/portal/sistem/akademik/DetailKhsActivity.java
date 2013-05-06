package dlmbg.pckg.portal.sistem.akademik;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

public class DetailKhsActivity extends ListActivity {
	SessionManager session;
	private ProgressDialog pDialog;
	JSONParser jParser = new JSONParser();
	Koneksi lo_Koneksi = new Koneksi();
	String isi = lo_Koneksi.isi_koneksi();
	String link_url = "";
	JSONArray str_json = null;

	ArrayList<HashMap<String, String>> dataMap = new ArrayList<HashMap<String, String>>();
	public void onBackPressed()
	{
	    Intent intent = new Intent(this, KhsActivity.class);
	    startActivity(intent);       
	}

	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        CekStatusInternet cek = new CekStatusInternet();
		
		if (cek.cek_status(this)) {
	        requestWindowFeature(Window.FEATURE_NO_TITLE);
	        setContentView(R.layout.list_detail_khs);
	        
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	        session = new SessionManager(getApplicationContext());
	        session.checkLogin();
	    	HashMap<String, String> user = session.getUserDetails();
	        String nim = user.get(SessionManager.KEY_USERNAME);
	        Bundle b = getIntent().getExtras();
			String kode = b.getString("kode");
	    	link_url = isi + "/khs.php?nim="+nim+"&semester="+kode;
	        new getListInfo().execute();
	        
		}
		else
		{
			cek.hasil(this);
		}
	}


	class getListInfo extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(DetailKhsActivity.this);
			pDialog.setMessage("Menghubungkan ke server...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		protected String doInBackground(String... args) {

			JSONObject json = jParser.AmbilJson(link_url);

			try {
				str_json = json.getJSONArray("info");
				
				for(int i = 0; i < str_json.length(); i++)
				{
					JSONObject ar = str_json.getJSONObject(i);

					String nama_mk = ar.getString("nama_mk");
					String semester_ditempuh = ar.getString("semester_ditempuh");
					String jum_sks = ar.getString("jum_sks");
					String grade = ar.getString("grade");
					
					HashMap<String, String> map = new HashMap<String, String>();

					map.put("nama_mk", nama_mk);
					map.put("semester_ditempuh", semester_ditempuh);
					map.put("jum_sks", jum_sks);
					map.put("grade", grade);

					dataMap.add(map);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute(String file_url) {
			pDialog.dismiss();
			runOnUiThread(new Runnable() {
                public void run() {
                	ListAdapter adapter = new SimpleAdapter(
							DetailKhsActivity.this, dataMap,
							R.layout.list_style_transkrip, new String[] {"nama_mk", "semester_ditempuh","jum_sks","grade"}, new int[] {R.id.nama_mk, R.id.semester_ditempuh, R.id.jum_sks, R.id.grade});
                	setListAdapter(adapter);
                }
            });
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
        		DetailKhsActivity.this.finish();
				startActivity(exit);
        		return true;
        	default:
        		return super.onOptionsItemSelected(item);
        }
    }

}
