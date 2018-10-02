package com.appzdevelopers.trackandhackerf;

import java.util.Set;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class BluetoothAdapterView extends Activity {

	public Utils sUtils = new Utils();
	private ProgressDialog mProgress;
	Set<BluetoothDevice> pairedDevices;

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

		final BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();
		mProgress = new ProgressDialog(BluetoothAdapterView.this);
		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgress.setTitle("Testing Bluetooth Connections");
		mProgress.setMessage("Please Wait....Scanning");
		mProgress.show();
		sUtils.setPairedDevices(pairedDevices);

		this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				try {

					
					Thread.sleep(4000);

					pairedDevices = bluetooth.getBondedDevices();
				
					for (BluetoothDevice device : pairedDevices) {
						btArrayAdapter.add(device.getName());

					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});

		bluetooth.disable();

		
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
	}
}
