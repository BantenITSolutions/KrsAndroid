package dlmbg.pckg.portal.sistem.akademik;

import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class DashboardActivity extends Activity {
	SessionManager session;
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
	        setContentView(R.layout.dashboard);
	        
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	        session = new SessionManager(getApplicationContext());
	        session.checkLogin();
	        
	        Button btn_khs = (Button) findViewById(R.id.btn_khs);
	        Button btn_krs = (Button) findViewById(R.id.btn_krs);
	        Button btn_transkrip = (Button) findViewById(R.id.btn_transkrip);
	        Button btn_logout = (Button) findViewById(R.id.btn_logout);
	        TextView txt_keterangan = (TextView) findViewById(R.id.txt_keterangan);
	        
	        HashMap<String, String> user = session.getUserDetails();
	        String nim = user.get(SessionManager.KEY_USERNAME);
	        String nama = user.get(SessionManager.KEY_NAMA);
	        String jurusan = user.get(SessionManager.KEY_JURUSAN);
	        String dosen = user.get(SessionManager.KEY_PEMBIMBING);
	        txt_keterangan.setText("NIM : "+nim+"\n NAMA : "+nama+"\n JURUSAN : "+jurusan+"\n DOSEN PA : "+dosen);
	        
	
	        btn_khs.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					Intent i = new Intent(getApplicationContext(), KhsActivity.class);
					startActivity(i);
				}
			});
	
	        btn_krs.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					Intent i = new Intent(getApplicationContext(), KrsActivity.class);
					startActivity(i);		
				}
			});
	
	        btn_transkrip.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					Intent i = new Intent(getApplicationContext(), TranskripActivity.class);
					startActivity(i);		
				}
			});
	
	        btn_logout.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {	
					session.logoutUser();
				}
			});
		}
		else
		{
			cek.hasil(this);
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
        		DashboardActivity.this.finish();
				startActivity(exit);
        		return true;
        	default:
        		return super.onOptionsItemSelected(item);
        }
    }

}
