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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class InputKrsActivity extends ListActivity {
	private CheckBox cekPilih;
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
	    Intent intent = new Intent(this, KrsActivity.class);
	    startActivity(intent);       
	}

	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        CekStatusInternet cek = new CekStatusInternet();
		
		if (cek.cek_status(this)) {
	        requestWindowFeature(Window.FEATURE_NO_TITLE);
	        setContentView(R.layout.krs);
	        Button btn_krs = (Button) findViewById(R.id.btn_in_krs);
	        btn_krs.setText("Simpan KRS");
	
	        btn_krs.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					Intent i = new Intent(getApplicationContext(), KhsActivity.class);
					startActivity(i);
				}
			});
	        
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	        session = new SessionManager(getApplicationContext());
	        session.checkLogin();
	    	HashMap<String, String> user = session.getUserDetails();
	        String nim = user.get(SessionManager.KEY_USERNAME);
	        String prodi = user.get(SessionManager.KEY_PRODI);
	        String smt = user.get(SessionManager.KEY_SEMESTER);
	    	link_url = isi + "/tampil_jadwal.php?nim="+nim+"&prodi="+prodi+"&smt="+smt;
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
			pDialog = new ProgressDialog(InputKrsActivity.this);
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
					String kode_mk = ar.getString("kode_mk");
					
					HashMap<String, String> map = new HashMap<String, String>();

					map.put("nama_mk_pilih", nama_mk);
					map.put("semester_pilih", semester);
					map.put("sks_pilih", jum_sks);
					map.put("kode_mk", kode_mk);

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
							InputKrsActivity.this, dataMap,
							R.layout.list_jadwal, new String[] {"nama_mk_pilih", "semester_pilih","sks_pilih","kode_mk"}, new int[] {R.id.nama_mk_pilih, R.id.semester_pilih, R.id.sks_pilih, R.id.kode_mk_list});
                	setListAdapter(adapter);
                	ListView lv = getListView();
        			lv.setVerticalFadingEdgeEnabled(false);
        			lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        				public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
        			    	String nama_mk = ((TextView) view.findViewById(R.id.nama_mk_pilih)).getText().toString();
        			    	String kode_mk = ((TextView) view.findViewById(R.id.kode_mk_list)).getText().toString();
        			    	String sks_mk = ((TextView) view.findViewById(R.id.sks_pilih)).getText().toString();
        			    	String smt_mk = ((TextView) view.findViewById(R.id.semester_pilih)).getText().toString();
        			        session = new SessionManager(getApplicationContext());
        			        session.checkLogin();
        			    	HashMap<String, String> get_id = session.getUserDetails();
        			        String krs_id = get_id.get(SessionManager.KEY_KRS_ID);

        			        Bundle b = getIntent().getExtras();
        					String jum_sks = b.getString("jum_sks");
        					int jum_sementara = Integer.parseInt(jum_sks)+Integer.parseInt(sks_mk);
        			    	HashMap<String, String> user = session.getUserDetails();
        			        final String max_sks = user.get(SessionManager.KEY_MAX_SKS);
        			        
        					if(jum_sementara>Integer.parseInt(max_sks))
        					{
            					Toast.makeText(getApplicationContext(), "Beban maksimal SKS tidak mencukupi", Toast.LENGTH_SHORT).show();
        					}
        					else
        					{

            			        Intent in = new Intent(getApplicationContext(), DetailMataKuliahActivity.class);
            					in.putExtra("nama_mk", nama_mk);
            					in.putExtra("kode_mk", kode_mk);
            					in.putExtra("sks_mk", sks_mk);
            					in.putExtra("smt_mk", smt_mk);
            					in.putExtra("krs_id", krs_id);
            					Toast.makeText(getApplicationContext(), nama_mk, Toast.LENGTH_SHORT).show();
            					startActivity(in);
        						
        					}
        					  
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
        		InputKrsActivity.this.finish();
				startActivity(exit);
        		return true;
        	default:
        		return super.onOptionsItemSelected(item);
        }
    }

}
