package com.appzdevelopers.trackandhackerf;

import java.util.List;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class AppPermission extends Activity {

	static String[] appName;
	static String[] packageName;

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

		PackageManager p = this.getPackageManager();
		final List<PackageInfo> appinstall = p
				.getInstalledPackages(PackageManager.GET_PERMISSIONS
						| PackageManager.GET_RECEIVERS
						| PackageManager.GET_SERVICES
						| PackageManager.GET_PROVIDERS);
		appName = new String[appinstall.size()];
		packageName = new String[appinstall.size()];

		int j = 0;

		for (PackageInfo pInfo : appinstall) {

			packageName[j] = pInfo.packageName.toString();

			appName[j] = pInfo.applicationInfo.loadLabel(p).toString();
			++j;
		}
		for (int i = 0; i < appName.length; i++) {

			btArrayAdapter.add(appName[i]);
		}

		listview.setAdapter(btArrayAdapter);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				Intent intent = new Intent(
						android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
				intent.setData(Uri.parse("package:" + packageName[arg2]));
				startActivity(intent);
			}
		});
	}
}
