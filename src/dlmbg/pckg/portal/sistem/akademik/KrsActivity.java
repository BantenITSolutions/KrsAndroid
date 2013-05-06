package dlmbg.pckg.portal.sistem.akademik;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
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
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class KrsActivity extends ListActivity {
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
	        
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	        session = new SessionManager(getApplicationContext());
	        session.checkLogin();
	    	HashMap<String, String> user = session.getUserDetails();
	        String nim = user.get(SessionManager.KEY_USERNAME);
	        String smt = user.get(SessionManager.KEY_SEMESTER);

	        final String awal_krs = user.get(SessionManager.KEY_AWAL_KRS);
	        final String akhir_krs = user.get(SessionManager.KEY_AKHIR_KRS);
	        final String ket_ta = user.get(SessionManager.KEY_TA);
	        
	    	link_url = isi + "/krs_saved.php?nim="+nim+"&smt="+smt;
	        new getListInfo().execute();
	        
	        requestWindowFeature(Window.FEATURE_NO_TITLE);
	        setContentView(R.layout.krs);
	        Button btn_krs = (Button) findViewById(R.id.btn_in_krs);
	        btn_krs.setText("Input / Edit KRS");

	        TextView ket_smt_ta = (TextView) findViewById(R.id.ket_smt_ta);
	        ket_smt_ta.setText(ket_ta);
	
	        btn_krs.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {

			        Calendar c = Calendar.getInstance(); 
			        
			        DateFormat df = new SimpleDateFormat("yyyy-MM-dd"); 
			        java.util.Date startDate;
			        java.util.Date endDate;
			        java.util.Date tgl_sekarang;
			        try {
				        tgl_sekarang = c.getTime();
						startDate = df.parse(awal_krs);
						endDate = df.parse(akhir_krs);
						
				        if (tgl_sekarang.after(startDate) && tgl_sekarang.before(endDate))  
				        {
						    Toast.makeText(getApplicationContext(), "Silahkan memilih mata kuliah", Toast.LENGTH_SHORT).show();
							Intent i = new Intent(getApplicationContext(), InputKrsActivity.class);
							startActivity(i);
				        }
				        else if (tgl_sekarang.after(endDate))  
				        {
						    Toast.makeText(getApplicationContext(), "Jadwal KRS telah selesai", Toast.LENGTH_SHORT).show();
				        }
				        else if (tgl_sekarang.before(startDate))  
				        {
						    Toast.makeText(getApplicationContext(), "Jadwal KRS belum dimulai", Toast.LENGTH_SHORT).show();
				        }
				        
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
			});
	        
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
			pDialog = new ProgressDialog(KrsActivity.this);
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
					String semester = ar.getString("semester");
					String jum_sks = ar.getString("sks");
					String kode_krs_detail = ar.getString("kode_krs_detail");
					
					HashMap<String, String> map = new HashMap<String, String>();

					map.put("nama_mk", nama_mk);
					map.put("semester", semester);
					map.put("sks", jum_sks);
					map.put("kode_krs_detail", kode_krs_detail);

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
							KrsActivity.this, dataMap,
							R.layout.list_style_krs_tersimpan, new String[] {"nama_mk", "semester","sks","kode_krs_detail"}, 
							new int[] {R.id.nama_mk, R.id.semester, R.id.sks, R.id.krs_detail});
                	setListAdapter(adapter);
                	ListView lv = getListView();
        			lv.setVerticalFadingEdgeEnabled(false);
        			lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        				public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
        			    	String nama_mk = ((TextView) view.findViewById(R.id.nama_mk)).getText().toString();
        			    	String krs_detail = ((TextView) view.findViewById(R.id.krs_detail)).getText().toString();
        			    	String sks_mk = ((TextView) view.findViewById(R.id.sks)).getText().toString();
        			    	String smt_mk = ((TextView) view.findViewById(R.id.semester)).getText().toString();
        			        session = new SessionManager(getApplicationContext());
        			        session.checkLogin();
        			    	HashMap<String, String> get_id = session.getUserDetails();
        			        String krs_id = get_id.get(SessionManager.KEY_KRS_ID);
        			        Intent in = new Intent(getApplicationContext(), HapusKrsActivity.class);
        					in.putExtra("nama_mk", nama_mk);
        					in.putExtra("krs_detail", krs_detail);
        					in.putExtra("sks_mk", sks_mk);
        					in.putExtra("smt_mk", smt_mk);
        					in.putExtra("krs_id", krs_id);
        					Toast.makeText(getApplicationContext(), nama_mk, Toast.LENGTH_SHORT).show();
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
        		KrsActivity.this.finish();
				startActivity(exit);
        		return true;
        	default:
        		return super.onOptionsItemSelected(item);
        }
    }

}
