package com.appzdevelopers.trackandhackerf;

import java.util.List;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class WifiList extends Activity {

	private WifiManager mWifi;
	private ProgressDialog mProgress;
	List<ScanResult> list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.list_view);

		AdView adView = (AdView) findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder().addTestDevice(
				AdRequest.DEVICE_ID_EMULATOR).build();

		// Start loading the ad in the background.
		adView.loadAd(adRequest);

		ListView listview = (ListView) findViewById(R.id.listView2);
		
		final ArrayAdapter<String> btArrayAdapter = new ArrayAdapter<String>(
				this, android.R.layout.simple_list_item_1);

		mWifi = (WifiManager) WifiList.this
				.getSystemService(Context.WIFI_SERVICE);
		if (!mWifi.isWifiEnabled()) {
			mWifi.setWifiEnabled(true);
		}
		mProgress = new ProgressDialog(WifiList.this);
		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgress.setTitle("Testing Wi-Fi Connections");
		mProgress.setMessage("Please Wait....Scanning");
		mProgress.show();
		mWifi.startScan();
		this.runOnUiThread(new Runnable() {

			@Override
			public void run() { 
				try {
					
					Thread.sleep(4000);

					list = mWifi.getScanResults();
					for (ScanResult s : list) {
						System.out.println("wifilist---------" + s.SSID);
						btArrayAdapter.add(s.SSID + "\n");
						//mProgress.dismiss();
					}
					

				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
		});
		
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(10000);
					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				mProgress.dismiss();
			}
		};
		new Thread(runnable).start();
		listview.setAdapter(btArrayAdapter);
		
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

			}
		});
	}
	
}
