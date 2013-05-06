package dlmbg.pckg.portal.sistem.akademik;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class CekStatusInternet {
	public boolean cek_status(Context cek) {
		ConnectivityManager cm = (ConnectivityManager) cek.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();

		if (info != null && info.isConnected()) {
			return true;
		} else {
			return false;
		}
	}
	public void hasil(Context tampil)
	{
		Toast.makeText(tampil,"Tidak ada koneksi internet yang tersedia...",Toast.LENGTH_LONG).show();
	}
}
