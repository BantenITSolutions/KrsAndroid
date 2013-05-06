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
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class KhsActivity extends ListActivity {
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
	    Intent intent = new Intent(this, PortalSistemAkademikActivity.class);
	    startActivity(intent);       
	}

	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        CekStatusInternet cek = new CekStatusInternet();
		
		if (cek.cek_status(this)) {
	        requestWindowFeature(Window.FEATURE_NO_TITLE);
	        setContentView(R.layout.khs);
	        
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	        session = new SessionManager(getApplicationContext());
	        session.checkLogin();
	        
	        HashMap<String, String> user = session.getUserDetails();
	        String sempProdiKode = user.get(SessionManager.KEY_PRODI);
	    	link_url = isi + "/semester.php?sempProdiKode="+sempProdiKode;
	    	
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
			pDialog = new ProgressDialog(KhsActivity.this);
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

					String semester_ditempuh = ar.getString("smt_ket");
					String id_smt = ar.getString("id");
					
					HashMap<String, String> map = new HashMap<String, String>();

					map.put("smt_ket", semester_ditempuh);
					map.put("id", id_smt);

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
							KhsActivity.this, dataMap,
							R.layout.list_style_semester, new String[] {"smt_ket", "id"}, new int[] {R.id.semester_ditempuh, R.id.smt});
                	setListAdapter(adapter);
        			ListView lv = getListView();
        			lv.setVerticalFadingEdgeEnabled(false);
        			lv.setOnItemClickListener(new OnItemClickListener() {

        				@Override
        				public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
        					String kode = ((TextView) view.findViewById(R.id.smt)).getText().toString();
        					Intent in = new Intent(getApplicationContext(), DetailKhsActivity.class);
        					in.putExtra("kode", kode);
        					startActivity(in);
        				}
        			});
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
        		KhsActivity.this.finish();
				startActivity(exit);
        		return true;
        	default:
        		return super.onOptionsItemSelected(item);
        }
    }

}
