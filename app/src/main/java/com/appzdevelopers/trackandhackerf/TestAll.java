package com.appzdevelopers.trackandhackerf;

import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.Patterns;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class TestAll extends Activity{

	private WifiManager mWifi;
	public Utils sUtils=new Utils();
	private ProgressDialog mProgress;
	List<ScanResult> list;
	Set<BluetoothDevice> pairedDevices;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mProgress=ProgressDialog.show(TestAll.this,"Testing All Connections","Please Wait....Scanning",true);

		setContentView(R.layout.list_view);

		AdView adView = (AdView) findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder().addTestDevice(
				AdRequest.DEVICE_ID_EMULATOR).build();

		// Start loading the ad in the background.
		adView.loadAd(adRequest);

		ListView listview=(ListView)findViewById(R.id.listView2);

		final ArrayAdapter<String> btArrayAdapter = new ArrayAdapter<String>(
				this, android.R.layout.simple_list_item_1);

		mWifi = (WifiManager)getSystemService(WIFI_SERVICE);
		if(!mWifi.isWifiEnabled()) {
			mWifi.setWifiEnabled(true);// Turn on Wifi/
		}
		mWifi.startScan();

		this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(2000);

				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				list = mWifi.getScanResults();

				sUtils.setWifiList(list);

			}
		});
		final BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();
		if(!bluetooth.isEnabled()) {
			bluetooth.enable();
		}
		this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(2000);

				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				pairedDevices = bluetooth.getBondedDevices();

				sUtils.setPairedDevices(pairedDevices);
			}
		});

		Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
		Account[] accounts = AccountManager.get(TestAll.this).getAccounts();
		for (Account account : accounts) {
			if (emailPattern.matcher(account.name).matches()) {
				String possibleEmail = account.name;
				sUtils.seteMail(possibleEmail);
			}
		}

		TelephonyManager telephone=(TelephonyManager)getSystemService(TELEPHONY_SERVICE);
		sUtils.setNetworkOperatorName(telephone.getNetworkOperatorName());

		String wifi=getResources().getString(R.string.wifi);
		btArrayAdapter.add(wifi);
		for(ScanResult s:list){

			btArrayAdapter.add(s.SSID);


		}
		btArrayAdapter.add("");
		String bluetooth1=getResources().getString(R.string.bluetooth);
		btArrayAdapter.add(bluetooth1);
		for(BluetoothDevice device:pairedDevices){
			btArrayAdapter.add(device.getName());


		}
		btArrayAdapter.add("");
		String email=getResources().getString(R.string.email);
		btArrayAdapter.add(email);
		btArrayAdapter.add(sUtils.geteMail());
		btArrayAdapter.add("");
		String network=getResources().getString(R.string.network);
		btArrayAdapter.add(network);
		btArrayAdapter.add(sUtils.getNetworkOperatorName());
		btArrayAdapter.add("");
		btArrayAdapter.add("*Keep Checking for updates*");
		listview.setAdapter(btArrayAdapter);
		
		
		
		/*
		StringBuffer appNameAndPermissions=new StringBuffer();
		PackageManager pm = getPackageManager();
		List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
		for (ApplicationInfo applicationInfo : packages) {
		Log.d("test", "App: " + applicationInfo.name + " Package: " + applicationInfo.packageName);
		try {
		PackageInfo packageInfo = pm.getPackageInfo(applicationInfo.packageName, PackageManager.GET_PERMISSIONS);
		              appNameAndPermissions.append(packageInfo.packageName+"*:\n");
		 //Get Permissions
		String[] requestedPermissions = packageInfo.requestedPermissions;
		 if(requestedPermissions != null) {
		 for (int i = 0; i < requestedPermissions.length; i++) {
		 Log.d("test", requestedPermissions[i]);
		appNameAndPermissions.append(requestedPermissions[i]+"\n");
		}
		 appNameAndPermissions.append("\n");
		}
		} catch (NameNotFoundException e) {
		 e.printStackTrace();
		}}*/
		
		
		
		
		
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(15000);

				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//mWifi.setWifiEnabled(false);// Turn off Wifi/
				bluetooth.disable();
				mProgress.dismiss();
			}
		};
		new Thread(runnable).start();
	}
}
