package com.appzdevelopers.trackandhackerf;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.pm.ServiceInfo;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.Spannable;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.digitalaria.gama.wheel.Wheel;
import com.digitalaria.gama.wheel.WheelAdapter;
import com.digitalaria.gama.wheel.WheelAdapter.OnItemClickListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

@SuppressLint("NewApi")
public class MainActivity extends Activity implements TabListener {

	private ListView mListView;
	private LocationManager locationManager;
	boolean isGPSEnabled;
	String longitude, latitude;
	Tab tab1, tab2, tab3;

	private Wheel wheel;
	private Resources res;
	private int[] icons = { R.drawable.wifi1, R.drawable.blue,
			R.drawable.network, R.drawable.gps1, R.drawable.trojan1,
			R.drawable.testall1, R.drawable.permission2 };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_main);
		setContentView(R.layout.main);
		init();

		AdView adView = (AdView) findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder().addTestDevice(
				AdRequest.DEVICE_ID_EMULATOR).build();

		// Start loading the ad in the background.
		adView.loadAd(adRequest);

		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		tab1 = actionBar.newTab();
		tab1.setText("Installed Apps");
		tab1.setTabListener(this);
		actionBar.addTab(tab1, 0, false);

		/*tab2 = actionBar.newTab();
		tab2.setText("Anti-Theft");
		tab2.setTabListener(this);
		actionBar.addTab(tab2, 1, false);*/

		tab2 = actionBar.newTab();
		tab2.setText("App-Info");
		tab2.setTabListener(this);
		actionBar.addTab(tab2, 1, false);

		int SDK_INT = android.os.Build.VERSION.SDK_INT;

		if (SDK_INT > 8) {

			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();

			StrictMode.setThreadPolicy(policy);

		}
		/*
		 * final String[] contents = { "Wi-Fi", "Bluetooth", "2G/3G/4G", "GPS",
		 * "Mock Location", "Trojan Virus", "Email", "Test All"
		 * ,"Suspicious Apps"};
		 * 
		 * Integer[] imageId = { R.drawable.wifi, R.drawable.bluetooth,
		 * R.drawable.networkop, R.drawable.gps, R.drawable.mock,
		 * R.drawable.trojan, R.drawable.mail, R.drawable.all };
		 * 
		 * mListView = (ListView) findViewById(R.id.listView1);
		 * 
		 * mListView.setBackgroundColor(Color.TRANSPARENT);
		 * mListView.setScrollBarStyle(ScrollView.INVISIBLE);
		 * 
		 * CustomListViewAdapter adapter = new CustomListViewAdapter(
		 * MainActivity.this, contents, imageId);
		 * 
		 * mListView.setAdapter(adapter);
		 */

		/*
		 * mListView.setOnItemClickListener(new
		 * AdapterView.OnItemClickListener() {
		 * 
		 * @Override public void onItemClick(AdapterView<?> parent, View view,
		 * int position, long id) {
		 * 
		 * switch (position) {
		 * 
		 * case 0: Spannable span =
		 * Spannable.Factory.getInstance().newSpannable(
		 * Html.fromHtml("<h5>This will analyze and show following details:</h5>"
		 * + "Current Wi-Fi network connected to your device.<br>" +
		 * "<br>List of W-Fi networks available around your device.<br>" +
		 * "<br>List of Wi-Fi networks accessed and saved by your device.<br>" +
		 * "<br>List of Wi-Fi networks (suspicious) which may be trying to hack your device data."
		 * ));
		 * 
		 * AlertDialog.Builder dialog=new
		 * AlertDialog.Builder(MainActivity.this); dialog.setCancelable(true);
		 * dialog.setMessage(span); dialog.setTitle("WiFi:");
		 * dialog.setPositiveButton("OK",new OnClickListener() {
		 * 
		 * @Override public void onClick(DialogInterface arg0, int arg1) {
		 * arg0.dismiss();
		 * 
		 * Intent wifiIntent=new Intent(MainActivity.this,WifiList.class);
		 * 
		 * startActivity(wifiIntent);
		 * 
		 * } }); dialog.setNegativeButton("NOTNOW",new OnClickListener() {
		 * 
		 * @Override public void onClick(DialogInterface arg0, int arg1) { } });
		 * dialog.show(); break; case 1:
		 * 
		 * 
		 * Spannable span1 =
		 * Spannable.Factory.getInstance().newSpannable(Html.fromHtml
		 * ("<h5>This will analyze and show following details:</h5>" +
		 * "Current Bluetooth device connected to your device.<br>" +
		 * "<br>List of Bluetooth devices available around your device.<br>" +
		 * "<br>List of Bluetooth devices accessed and saved by your device.<br>"
		 * +
		 * "<br>List of Bluetooth devices (suspicious) which may be trying to hack your device data."
		 * ));
		 * 
		 * 
		 * 
		 * 
		 * 
		 * AlertDialog.Builder dialog1=new
		 * AlertDialog.Builder(MainActivity.this); dialog1.setCancelable(true);
		 * dialog1.setMessage(span1);
		 * 
		 * dialog1.setTitle("Bluetooth:"); dialog1.setPositiveButton("OK",new
		 * OnClickListener() {
		 * 
		 * @Override public void onClick(DialogInterface arg2, int arg3) {
		 * arg2.dismiss(); final BluetoothAdapter bluetooth =
		 * BluetoothAdapter.getDefaultAdapter(); if(!bluetooth.isEnabled()) {
		 * bluetooth.enable(); } Intent btIntent=new
		 * Intent(MainActivity.this,BluetoothAdapterView.class);
		 * startActivity(btIntent);
		 * 
		 * } }); dialog1.setNegativeButton("NOTNOW",new OnClickListener() {
		 * 
		 * @Override public void onClick(DialogInterface arg0, int arg1) { } });
		 * dialog1.show();
		 * 
		 * break; case 2:
		 * 
		 * 
		 * Spannable span2 =
		 * Spannable.Factory.getInstance().newSpannable(Html.fromHtml
		 * ("<h5>This will analyze and show following details:</h5>" +
		 * "Current 2G/3G/4G network connected to your device.<br>" +
		 * "<br>List of 2G/3G/4G networks available around your device.<br>" +
		 * "<br>List of 2G/3G/4G networks accessed and saved by your device.<br>"
		 * +
		 * "<br>List of 2G/3G/4G networks (suspicious) which may be trying to hack your device data."
		 * ));
		 * 
		 * 
		 * 
		 * 
		 * final TelephonyManager
		 * telephone=(TelephonyManager)getSystemService(TELEPHONY_SERVICE);
		 * 
		 * AlertDialog.Builder dialog2=new
		 * AlertDialog.Builder(MainActivity.this); dialog2.setCancelable(true);
		 * dialog2.setMessage(span2);
		 * 
		 * 
		 * dialog2.setTitle("2G/3G/4G:"); dialog2.setPositiveButton("OK",new
		 * OnClickListener() {
		 * 
		 * @Override public void onClick(DialogInterface arg4, int arg5) {
		 * arg4.dismiss();
		 * Toast.makeText(getApplicationContext(),telephone.getNetworkOperatorName
		 * (),Toast.LENGTH_SHORT).show(); } });
		 * dialog2.setNegativeButton("NOTNOW",new OnClickListener() {
		 * 
		 * @Override public void onClick(DialogInterface arg0, int arg1) { } });
		 * dialog2.show();
		 * 
		 * break; case 3: AlertDialog.Builder dialog3=new
		 * AlertDialog.Builder(MainActivity.this); dialog3.setCancelable(true);
		 * dialog3
		 * .setMessage("To get current location please enable GPS in your mobile"
		 * );
		 * 
		 * dialog3.setTitle("GPS:"); dialog3.setPositiveButton("OK",new
		 * OnClickListener() {
		 * 
		 * @Override public void onClick(DialogInterface arg6, int arg7) {
		 * arg6.dismiss(); List<Address> locations=null;
		 * 
		 * String provider = Settings.Secure.getString(getContentResolver(),
		 * Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
		 * if(!provider.contains("gps")){ //if gps is disabled final Intent poke
		 * = new Intent(); poke.setClassName("com.android.settings",
		 * "com.android.settings.widget.SettingsAppWidgetProvider");
		 * poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
		 * poke.setData(Uri.parse("3")); MainActivity.this.sendBroadcast(poke);
		 * 
		 * 
		 * }
		 * 
		 * 
		 * locationManager = (LocationManager)
		 * getSystemService(LOCATION_SERVICE);
		 * 
		 * 
		 * 
		 * // Define a listener that responds to location updates
		 * 
		 * LocationListener locationListener = new LocationListener() { public
		 * void onLocationChanged(Location location) { // Called when a new
		 * location is found by the network location provider.
		 * 
		 * longitude = String.valueOf(location.getLongitude()); latitude =
		 * String.valueOf(location.getLatitude()); Log.d("msg",
		 * "changed Loc : "+longitude + ":"+latitude); }
		 * 
		 * public void onStatusChanged(String provider, int status, Bundle
		 * extras) {}
		 * 
		 * public void onProviderEnabled(String provider) {}
		 * 
		 * public void onProviderDisabled(String provider) {} };
		 * 
		 * // getting GPS status isGPSEnabled =
		 * locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		 * if(!isGPSEnabled) { System.out.println("isGPSEnabled false");
		 * AlertDialog.Builder builder = new
		 * AlertDialog.Builder(MainActivity.this); builder.setMessage(
		 * "Your GPS seems to be disabled, do you want to enable it?")
		 * .setCancelable(false) .setPositiveButton("Yes", new
		 * DialogInterface.OnClickListener() { public void
		 * onClick(DialogInterface dialog,int id) { startActivity(new
		 * Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)); }
		 * }) .setNegativeButton("No", new DialogInterface.OnClickListener() {
		 * public void onClick(DialogInterface dialog,int id) { dialog.cancel();
		 * } }); AlertDialog alert = builder.create(); alert.show(); } // check
		 * if GPS enabled if(isGPSEnabled){
		 * 
		 * Location location =
		 * locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		 * 
		 * if(location != null) { longitude =
		 * String.valueOf(location.getLongitude()); latitude =
		 * String.valueOf(location.getLatitude()); Log.d("msg",
		 * "Loc : "+longitude + ":"+latitude);
		 * 
		 * locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
		 * 0, 0, locationListener); }else {
		 * 
		 * location =
		 * locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER
		 * );
		 * 
		 * if(location != null) { longitude =
		 * String.valueOf(location.getLongitude()); latitude =
		 * String.valueOf(location.getLatitude());
		 * 
		 * locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER
		 * , 0, 0, locationListener); }else { longitude = "0.00f"; latitude =
		 * "0.00f"; } }
		 * 
		 * Geocoder geo=new Geocoder(getApplicationContext()); try {
		 * locations=geo
		 * .getFromLocation(Double.parseDouble(latitude),Double.parseDouble
		 * (longitude), 2); } catch (IOException e) { // TODO Auto-generated
		 * catch block e.printStackTrace(); } String street =null; if(locations
		 * != null && locations.size() > 0 ){ Address address =
		 * locations.get(0); // Thoroughfare seems to be the street name without
		 * numbers street = address.getAddressLine(0); }
		 * Toast.makeText(getApplicationContext
		 * (),street,Toast.LENGTH_SHORT).show();
		 * 
		 * }
		 * 
		 * 
		 * 
		 * 
		 * if(provider.contains("gps")){ //if gps is enabled final Intent poke =
		 * new Intent(); poke.setClassName("com.android.settings",
		 * "com.android.settings.widget.SettingsAppWidgetProvider");
		 * poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
		 * poke.setData(Uri.parse("3")); MainActivity.this.sendBroadcast(poke);
		 * } }
		 * 
		 * }); dialog3.setNegativeButton("NOTNOW",new OnClickListener() {
		 * 
		 * @Override public void onClick(DialogInterface arg0, int arg1) { } });
		 * dialog3.show(); break; case 4:
		 * 
		 * Spannable span3 =
		 * Spannable.Factory.getInstance().newSpannable(Html.fromHtml
		 * ("<h5>This will guide you to enable Mock location on your device:</h5>"
		 * +
		 * "After clicking on OK, it will open Developer Options in your device.<br>"
		 * + "<br>Please check for check box next to Allow Mock Locations.<br>"
		 * + "<br>If it is unchecked, please check/enable it.<br>" +
		 * "<br>It would provide mock location data to some apps which may be trying to track your location."
		 * ));
		 * 
		 * AlertDialog.Builder dialog4=new
		 * AlertDialog.Builder(MainActivity.this); dialog4.setCancelable(true);
		 * dialog4.setMessage(span3);
		 * 
		 * dialog4.setTitle("MOCK LOCATION:");
		 * dialog4.setPositiveButton("OK",new OnClickListener() {
		 * 
		 * @Override public void onClick(DialogInterface arg6, int arg7) {
		 * arg6.dismiss();
		 * 
		 * int SDK_INT = android.os.Build.VERSION.SDK_INT;
		 * 
		 * if (SDK_INT==10){
		 * 
		 * Intent i3=new Intent(Settings.ACTION_APPLICATION_SETTINGS);
		 * startActivity(i3); } else if(SDK_INT>10) { Intent i3=new
		 * Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS);
		 * startActivity(i3); } } }); dialog4.setNegativeButton("NOTNOW",new
		 * OnClickListener() {
		 * 
		 * @Override public void onClick(DialogInterface arg0, int arg1) { } });
		 * dialog4.show(); break; case 5:
		 * 
		 * Spannable span4 =
		 * Spannable.Factory.getInstance().newSpannable(Html.fromHtml(
		 * "<h5>This will assist you to locate Trojan Viruses (if any) present on your device:</h5>"
		 * +
		 * "After clicking on OK, it will open Security Settings in your device.<br>"
		 * + "<br>Please scroll down and check for Device Administrators.<br>" +
		 * "<br>Tap on it to open and check for any suspicious app or any ununsual data showing up there.<br>"
		 * +
		 * "<br>In case any assistance, please contact us immediately and we will help you out"
		 * ));
		 * 
		 * AlertDialog.Builder dialog5=new
		 * AlertDialog.Builder(MainActivity.this); dialog5.setCancelable(true);
		 * dialog5.setMessage(span4);
		 * 
		 * dialog5.setTitle("Trojan Virus:"); dialog5.setPositiveButton("OK",new
		 * OnClickListener() {
		 * 
		 * @Override public void onClick(DialogInterface arg6, int arg7) {
		 * arg6.dismiss(); Intent i3=new
		 * Intent(Settings.ACTION_SECURITY_SETTINGS); startActivity(i3);
		 * 
		 * } }); dialog5.setNegativeButton("NOTNOW",new OnClickListener() {
		 * 
		 * @Override public void onClick(DialogInterface arg0, int arg1) { } });
		 * dialog5.show();
		 * 
		 * break; case 6:
		 * 
		 * Spannable span5 =
		 * Spannable.Factory.getInstance().newSpannable(Html.fromHtml
		 * ("<h5>This will analyze and show following details:</h5>" +
		 * "Email ID synced with your device.<br>" +
		 * "<br><b>Amazing Feature Coming Soon!!</b>"));
		 * 
		 * 
		 * 
		 * AlertDialog.Builder dialog6=new
		 * AlertDialog.Builder(MainActivity.this); dialog6.setCancelable(true);
		 * dialog6.setMessage(span5);
		 * 
		 * dialog6.setTitle("EMAIL:"); dialog6.setPositiveButton("OK",new
		 * OnClickListener() {
		 * 
		 * @Override public void onClick(DialogInterface arg6, int arg7) {
		 * arg6.dismiss(); Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API
		 * level 8+ Account[] accounts =
		 * AccountManager.get(MainActivity.this).getAccounts(); for (Account
		 * account : accounts) { if
		 * (emailPattern.matcher(account.name).matches()) { String possibleEmail
		 * = account.name;
		 * Toast.makeText(getApplicationContext(),possibleEmail,Toast
		 * .LENGTH_SHORT).show(); } } } });
		 * dialog6.setNegativeButton("NOTNOW",new OnClickListener() {
		 * 
		 * @Override public void onClick(DialogInterface arg0, int arg1) { } });
		 * dialog6.show();
		 * 
		 * break; case 7:
		 * 
		 * Spannable span6 =
		 * Spannable.Factory.getInstance().newSpannable(Html.fromHtml(
		 * "<h5>This will analyze, scan whole device and show following details:</h5>"
		 * + "Current/accessed connections established by your device.<br>" +
		 * "<br>Connections available around your device.<br>" +
		 * "<br>Current location/locations visited by your device<br>" +
		 * "<br>Trojan Virus (if any)" ));
		 * 
		 * AlertDialog.Builder dialog7=new
		 * AlertDialog.Builder(MainActivity.this); dialog7.setCancelable(true);
		 * dialog7.setMessage(span6);
		 * 
		 * dialog7.setTitle("TEST ALL:"); dialog7.setPositiveButton("OK",new
		 * OnClickListener() {
		 * 
		 * @Override public void onClick(DialogInterface arg6, int arg7) {
		 * arg6.dismiss();
		 * 
		 * Intent test=new Intent(MainActivity.this,TestAll.class);
		 * test.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		 * startActivity(test); overridePendingTransition(0,0); } });
		 * dialog7.setNegativeButton("NOTNOW",new OnClickListener() {
		 * 
		 * @Override public void onClick(DialogInterface arg0, int arg1) { } });
		 * dialog7.show(); break;
		 * 
		 * case 8: Intent test=new Intent(MainActivity.this,SuspiciusApp.class);
		 * test.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		 * startActivity(test); overridePendingTransition(0,0); break; default:
		 * break; } } });
		 */
	}

	private void init() {
		res = getApplicationContext().getResources();
		wheel = (Wheel) findViewById(R.id.wheel);

		wheel.setItems(getDrawableFromData(icons));
		wheel.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(WheelAdapter<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				System.out.println("pos--" + position);
				switch (position) {

				case 0:
					Spannable span = Spannable.Factory
							.getInstance()
							.newSpannable(
									Html.fromHtml("<h5>This will analyze and show following details:</h5>"
											+ "Current Wi-Fi network connected to your device.<br>"
											+ "<br>List of W-Fi networks available around your device.<br>"
											+ "<br>List of Wi-Fi networks accessed and saved by your device.<br>"
											+ "<br>List of Wi-Fi networks (suspicious) which may be trying to hack your device data."));

					AlertDialog.Builder dialog = new AlertDialog.Builder(
							MainActivity.this);
					dialog.setCancelable(true);
					dialog.setMessage(span);
					dialog.setTitle("WiFi:");
					dialog.setPositiveButton("OK", new OnClickListener() {

						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							arg0.dismiss();

							Intent wifiIntent = new Intent(MainActivity.this,
									WifiList.class);

							startActivity(wifiIntent);

						}
					});
					dialog.setNegativeButton("NOTNOW", new OnClickListener() {

						@Override
						public void onClick(DialogInterface arg0, int arg1) {
						}
					});
					dialog.show();
					break;

				case 1:
					Spannable span1 = Spannable.Factory
							.getInstance()
							.newSpannable(
									Html.fromHtml("<h5>This will analyze and show following details:</h5>"
											+ "Current Bluetooth device connected to your device.<br>"
											+ "<br>List of Bluetooth devices available around your device.<br>"
											+ "<br>List of Bluetooth devices accessed and saved by your device.<br>"
											+ "<br>List of Bluetooth devices (suspicious) which may be trying to hack your device data."));

					AlertDialog.Builder dialog1 = new AlertDialog.Builder(
							MainActivity.this);
					dialog1.setCancelable(true);
					dialog1.setMessage(span1);

					dialog1.setTitle("Bluetooth:");
					dialog1.setPositiveButton("OK", new OnClickListener() {

						@Override
						public void onClick(DialogInterface arg2, int arg3) {
							arg2.dismiss();
							final BluetoothAdapter bluetooth = BluetoothAdapter
									.getDefaultAdapter();
							if (!bluetooth.isEnabled()) {
								bluetooth.enable();
							}
							Intent btIntent = new Intent(MainActivity.this,
									BluetoothAdapterView.class);
							startActivity(btIntent);

						}
					});
					dialog1.setNegativeButton("NOTNOW", new OnClickListener() {

						@Override
						public void onClick(DialogInterface arg0, int arg1) {
						}
					});
					dialog1.show();

					break;

				case 2:
					Spannable span2 = Spannable.Factory
							.getInstance()
							.newSpannable(
									Html.fromHtml("<h5>This will analyze and show following details:</h5>"
											+ "Current 2G/3G/4G network connected to your device.<br>"
											+ "<br>List of 2G/3G/4G networks available around your device.<br>"
											+ "<br>List of 2G/3G/4G networks accessed and saved by your device.<br>"
											+ "<br>List of 2G/3G/4G networks (suspicious) which may be trying to hack your device data."));

					final TelephonyManager telephone = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

					AlertDialog.Builder dialog2 = new AlertDialog.Builder(
							MainActivity.this);
					dialog2.setCancelable(true);
					dialog2.setMessage(span2);

					dialog2.setTitle("2G/3G/4G:");
					dialog2.setPositiveButton("OK", new OnClickListener() {

						@Override
						public void onClick(DialogInterface arg4, int arg5) {
							arg4.dismiss();
							Toast.makeText(getApplicationContext(),
									telephone.getNetworkOperatorName(),
									Toast.LENGTH_SHORT).show();
						}
					});
					dialog2.setNegativeButton("NOTNOW", new OnClickListener() {

						@Override
						public void onClick(DialogInterface arg0, int arg1) {
						}
					});
					dialog2.show();

					break;

				case 3:
					AlertDialog.Builder dialog3 = new AlertDialog.Builder(
							MainActivity.this);
					dialog3.setCancelable(true);
					dialog3.setMessage("To get current location please enable GPS in your mobile");

					dialog3.setTitle("GPS:");
					dialog3.setPositiveButton("OK", new OnClickListener() {

						@Override
						public void onClick(DialogInterface arg6, int arg7) {
							arg6.dismiss();
							List<Address> locations = null;

							String provider = Settings.Secure.getString(
									getContentResolver(),
									Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
							/*
							 * if(!provider.contains("gps")){ //if gps is
							 * disabled final Intent poke = new Intent();
							 * poke.setClassName("com.android.settings",
							 * "com.android.settings.widget.SettingsAppWidgetProvider"
							 * ); poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
							 * poke.setData(Uri.parse("3"));
							 * MainActivity.this.sendBroadcast(poke);
							 * 
							 * 
							 * }
							 */

							locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

							// Define a listener that responds to location
							// updates

							LocationListener locationListener = new LocationListener() {
								public void onLocationChanged(Location location) {
									// Called when a new location is found by
									// the network location provider.

									longitude = String.valueOf(location
											.getLongitude());
									latitude = String.valueOf(location
											.getLatitude());
									Log.d("msg", "changed Loc : " + longitude
											+ ":" + latitude);
								}

								public void onStatusChanged(String provider,
										int status, Bundle extras) {
								}

								public void onProviderEnabled(String provider) {
								}

								public void onProviderDisabled(String provider) {
								}
							};

							// getting GPS status
							isGPSEnabled = locationManager
									.isProviderEnabled(LocationManager.GPS_PROVIDER);
							if (!isGPSEnabled) {
								System.out.println("isGPSEnabled false");
								AlertDialog.Builder builder = new AlertDialog.Builder(
										MainActivity.this);
								builder.setMessage(
										"Your GPS seems to be disabled, do you want to enable it?")
										.setCancelable(false)
										.setPositiveButton(
												"Yes",
												new DialogInterface.OnClickListener() {
													public void onClick(
															DialogInterface dialog,
															int id) {
														startActivity(new Intent(
																android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
													}
												})
										.setNegativeButton(
												"No",
												new DialogInterface.OnClickListener() {
													public void onClick(
															DialogInterface dialog,
															int id) {
														dialog.cancel();
													}
												});
								AlertDialog alert = builder.create();
								alert.show();
							}
							// check if GPS enabled
							if (isGPSEnabled) {

								Location location = locationManager
										.getLastKnownLocation(LocationManager.GPS_PROVIDER);

								if (location != null) {
									longitude = String.valueOf(location
											.getLongitude());
									latitude = String.valueOf(location
											.getLatitude());
									Log.d("msg", "Loc : " + longitude + ":"
											+ latitude);

									locationManager.requestLocationUpdates(
											LocationManager.GPS_PROVIDER, 0, 0,
											locationListener);
								} else {

									location = locationManager
											.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

									if (location != null) {
										longitude = String.valueOf(location
												.getLongitude());
										latitude = String.valueOf(location
												.getLatitude());

										locationManager
												.requestLocationUpdates(
														LocationManager.NETWORK_PROVIDER,
														0, 0, locationListener);
									} else {
										longitude = "0.00f";
										latitude = "0.00f";
									}
								}

								Geocoder geo = new Geocoder(
										getApplicationContext());
								try {
									locations = geo.getFromLocation(
											Double.parseDouble(latitude),
											Double.parseDouble(longitude), 2);
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								String street = null;
								if (locations != null && locations.size() > 0) {
									Address address = locations.get(0);
									// Thoroughfare seems to be the street name
									// without numbers
									street = address.getAddressLine(0);
								}
								Toast.makeText(getApplicationContext(), street,
										Toast.LENGTH_SHORT).show();

							}

							if (provider.contains("gps")) { // if gps is enabled
								final Intent poke = new Intent();
								poke.setClassName("com.android.settings",
										"com.android.settings.widget.SettingsAppWidgetProvider");
								poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
								poke.setData(Uri.parse("3"));
								MainActivity.this.sendBroadcast(poke);
							}
						}

					});
					dialog3.setNegativeButton("NOTNOW", new OnClickListener() {

						@Override
						public void onClick(DialogInterface arg0, int arg1) {
						}
					});
					dialog3.show();
					break;
				/*
				 * case 4:
				 * 
				 * Spannable span3 =
				 * Spannable.Factory.getInstance().newSpannable(Html.fromHtml(
				 * "<h5>This will guide you to enable Mock location on your device:</h5>"
				 * +
				 * "After clicking on OK, it will open Developer Options in your device.<br>"
				 * +
				 * "<br>Please check for check box next to Allow Mock Locations.<br>"
				 * + "<br>If it is unchecked, please check/enable it.<br>" +
				 * "<br>It would provide mock location data to some apps which may be trying to track your location."
				 * ));
				 * 
				 * AlertDialog.Builder dialog4=new
				 * AlertDialog.Builder(MainActivity.this);
				 * dialog4.setCancelable(true); dialog4.setMessage(span3);
				 * 
				 * dialog4.setTitle("MOCK LOCATION:");
				 * dialog4.setPositiveButton("OK",new OnClickListener() {
				 * 
				 * @Override public void onClick(DialogInterface arg6, int arg7)
				 * { arg6.dismiss();
				 * 
				 * int SDK_INT = android.os.Build.VERSION.SDK_INT;
				 * 
				 * if (SDK_INT==10){
				 * 
				 * Intent i3=new Intent(Settings.ACTION_APPLICATION_SETTINGS);
				 * startActivity(i3); } else if(SDK_INT>10) { Intent i3=new
				 * Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS);
				 * startActivity(i3); } } });
				 * dialog4.setNegativeButton("NOTNOW",new OnClickListener() {
				 * 
				 * @Override public void onClick(DialogInterface arg0, int arg1)
				 * { } }); dialog4.show(); break;
				 */

				case 4:
					Spannable span4 = Spannable.Factory
							.getInstance()
							.newSpannable(
									Html.fromHtml("<h5>This will assist you to locate Trojan Viruses (if any) present on your device:</h5>"
											+ "After clicking on OK, it will open Security Settings in your device.<br>"
											+ "<br>Please scroll down and check for Device Administrators.<br>"
											+ "<br>Tap on it to open and check for any suspicious app or any ununsual data showing up there.<br>"
											+ "<br>In case any assistance, please contact us immediately and we will help you out"));

					AlertDialog.Builder dialog5 = new AlertDialog.Builder(
							MainActivity.this);
					dialog5.setCancelable(true);
					dialog5.setMessage(span4);

					dialog5.setTitle("Trojan Virus:");
					dialog5.setPositiveButton("OK", new OnClickListener() {

						@Override
						public void onClick(DialogInterface arg6, int arg7) {
							arg6.dismiss();
							Intent i3 = new Intent(
									Settings.ACTION_SECURITY_SETTINGS);
							startActivity(i3);

						}
					});
					dialog5.setNegativeButton("NOTNOW", new OnClickListener() {

						@Override
						public void onClick(DialogInterface arg0, int arg1) {
						}
					});
					dialog5.show();

					break;
				/*
				 * case 6:
				 * 
				 * Spannable span5 =
				 * Spannable.Factory.getInstance().newSpannable(Html.fromHtml(
				 * "<h5>This will analyze and show following details:</h5>" +
				 * "Email ID synced with your device.<br>" +
				 * "<br><b>Amazing Feature Coming Soon!!</b>"));
				 * 
				 * 
				 * 
				 * AlertDialog.Builder dialog6=new
				 * AlertDialog.Builder(MainActivity.this);
				 * dialog6.setCancelable(true); dialog6.setMessage(span5);
				 * 
				 * dialog6.setTitle("EMAIL:");
				 * dialog6.setPositiveButton("OK",new OnClickListener() {
				 * 
				 * @Override public void onClick(DialogInterface arg6, int arg7)
				 * { arg6.dismiss(); Pattern emailPattern =
				 * Patterns.EMAIL_ADDRESS; // API level 8+ Account[] accounts =
				 * AccountManager.get(MainActivity.this).getAccounts(); for
				 * (Account account : accounts) { if
				 * (emailPattern.matcher(account.name).matches()) { String
				 * possibleEmail = account.name;
				 * Toast.makeText(getApplicationContext
				 * (),possibleEmail,Toast.LENGTH_SHORT).show(); } } } });
				 * dialog6.setNegativeButton("NOTNOW",new OnClickListener() {
				 * 
				 * @Override public void onClick(DialogInterface arg0, int arg1)
				 * { } }); dialog6.show();
				 * 
				 * break;
				 */

				case 5:
					Spannable span6 = Spannable.Factory
							.getInstance()
							.newSpannable(
									Html.fromHtml("<h5>This will analyze, scan whole device and show following details:</h5>"
											+ "Current/accessed connections established by your device.<br>"
											+ "<br>Connections available around your device.<br>"
											+ "<br>Current location/locations visited by your device<br>"
											+ "<br>Trojan Virus (if any)"));

					AlertDialog.Builder dialog7 = new AlertDialog.Builder(
							MainActivity.this);
					dialog7.setCancelable(true);
					dialog7.setMessage(span6);

					dialog7.setTitle("TEST ALL:");
					dialog7.setPositiveButton("OK", new OnClickListener() {

						@Override
						public void onClick(DialogInterface arg6, int arg7) {
							arg6.dismiss();

							Intent test = new Intent(MainActivity.this,
									TestAll.class);
							test.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
							startActivity(test);
							overridePendingTransition(0, 0);
						}
					});
					dialog7.setNegativeButton("NOTNOW", new OnClickListener() {

						@Override
						public void onClick(DialogInterface arg0, int arg1) {
						}
					});
					dialog7.show();
					break;

				case 6:
					Intent test = new Intent(MainActivity.this,
							LiveNewsActivity.class);
					test.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
					startActivity(test);
					overridePendingTransition(0, 0);
					break;

				default:
					break;
				}
			}
		});
	}

	private Drawable[] getDrawableFromData(int[] data) {
		Drawable[] ret = new Drawable[data.length];
		for (int i = 0; i < data.length; i++) {
			ret[i] = res.getDrawable(data[i]);
		}
		return ret;
	}

	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTabSelected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		switch (arg0.getPosition()) {

		case 0:
			Intent permission1 = new Intent(MainActivity.this,
					AppPermission.class);
			startActivity(permission1);
			break;

		/*case 1:
			String url = "https://play.google.com/store/apps/details?id=com.appzdevelopers.androiddevicetrackerf";
			Intent i = new Intent(Intent.ACTION_VIEW);
			i.setData(Uri.parse(url));
			startActivity(i);

			break;*/

		case 1:
			Spannable span = Spannable.Factory
					.getInstance()
					.newSpannable(
							Html.fromHtml("<h5 style='align:center'><u>Track Android Hacker</u></h5>"
									+ "<p>'TRACK ANDROID HACKER' ? AWARD WINNING SECURITY APP BY 'APPZ DEVELOPERS'."
									+ "<br><br>"
									+ "After a thorough research by a group of American University students on Android Security lapses, we have formulated, designed and developed this app. This will protect your privacy and private data from being hacked."
									+ "<br><br>"
									+

									"It will scan all possible (hacker?s) entry points and detect any malicious activities or anyone trying to steal your private data via Internet (Wi-Fi, 2G/3G/4G), Bluetooth, GPS, EMAIL etc."
									+ "<br><br>"
									+

									"Amazing Feature for ?EMAIL Security? Coming Soon."
									+ "<br><br>"
									+

									"<b>Real Time Track Android Hacker? Coming Soon.</b>"
									+ "<br><br>"
									+

									"We will continue improving the app based on your feedback."
									+ "<br><br>"
									+ " Please do mail us about any issue/feedback at appzdevelopers@gmail.com and we will be glad to work on it.</p>"));
			final AlertDialog.Builder adb = new AlertDialog.Builder(
					MainActivity.this);
			adb.setTitle("App Info");
			adb.setMessage(span);
			adb.setPositiveButton("OK", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {

				}
			});

			adb.show();

			break;
		}

	}

	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub

	}

}
