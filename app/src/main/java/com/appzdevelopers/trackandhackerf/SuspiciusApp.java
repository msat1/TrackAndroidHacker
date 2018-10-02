package com.appzdevelopers.trackandhackerf;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

public class SuspiciusApp extends Activity {

	private static final String TAG = "MyActivity";
	// CustomListViewAdapter1 adapter;
	private ArrayList<String> list = new ArrayList<String>();
	private ArrayList<String> list1 = new ArrayList<String>();
	HashSet<String> listToSet;
	ArrayList<String> list2;
	String[] appName;
	String[] permissionName;
	String[] number;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.suspicius);

		final PackageManager pm = getPackageManager();
		int j = 0, l = 0;

		StringBuffer permissions = new StringBuffer();
		List<PackageInfo> appList = pm
				.getInstalledPackages(PackageManager.GET_PERMISSIONS);
		appName = new String[appList.size()];
		permissionName = new String[appList.size()];
		
		for (PackageInfo pi : appList) {

			if ((pi.requestedPermissions == null || pi.packageName
					.equals("android"))
					|| (pi.applicationInfo != null && (pi.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0))
				continue;

			for (int i = 0; i < pi.requestedPermissions.length; i++) {
				

				permissions.append(pi.requestedPermissions[i] + "\n");

				permissionName[i] = permissions.toString();

			}

			Log.i(TAG, "permisssion--" + permissionName[j]);

			Log.i(TAG, "----------------------------------");

			appName[j] = pm.getApplicationLabel(pi.applicationInfo).toString();
			++j;
		}

		for (String s : appName) {
			if (s != null) {
				list.add(s);
			}
		}
		for(String s:permissionName){
			if(s!=null) {
				listToSet=new HashSet<String>();
				listToSet.add(s);
				list1.addAll(listToSet);
			}
		}

		ListView lv = (ListView) findViewById(R.id.listView1);

		ListViewAdapter adapter = new ListViewAdapter(this, list,
				list1);

		lv.setAdapter(adapter);
		permissionName = null;
	}
}
