package com.appzdevelopers.trackandhackerf;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class LiveNewsActivity extends ListActivity {

	List headlines;
	List links;

	private ProgressDialog mProgress;
	private Handler handler;
	private ProgressDialog progress;
	private Context context;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rss);

		AdView adView = (AdView) findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder().addTestDevice(
				AdRequest.DEVICE_ID_EMULATOR).build();

		// Start loading the ad in the background.
		adView.loadAd(adRequest);

		ConnectivityManager cm = (ConnectivityManager) getApplicationContext()
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		final NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

		if (null != activeNetwork) {
			if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI
					&& activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)

				System.out.println("Internet ON");
		} else {
			System.out.println("Internet OFF");

			mProgress = new ProgressDialog(LiveNewsActivity.this);
			mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mProgress.setTitle("No Internet Connection");
			mProgress
					.setMessage("Please Connect To Internet For Latest Updates...");
			mProgress.show();

			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					mProgress.dismiss();
					LiveNewsActivity.this.finish();
				}
			}, 5000);

		}
		context = LiveNewsActivity.this;
		progress = new ProgressDialog(this);
		progress.setTitle("GSM Arena");
		progress.setMessage("Downloading Latest Updates...!!");
		progress.setCancelable(false);
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);

		handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {

				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						ArrayAdapter adapter = new ArrayAdapter(
								LiveNewsActivity.this,
								android.R.layout.simple_list_item_1, headlines);
						setListAdapter(adapter);
					}
				});
				progress.dismiss();
				super.handleMessage(msg);
			}
		};
		progress.show();
		new Thread() {
			public void run() {

				headlines = new ArrayList();
				links = new ArrayList();

				try {
					URL url = new URL(
							"http://feeds.feedburner.com/TheHackersNews");
					XmlPullParserFactory factory = XmlPullParserFactory
							.newInstance();
					factory.setNamespaceAware(false);
					XmlPullParser xpp = factory.newPullParser();
					// We will get the XML from an input stream
					xpp.setInput(getInputStream(url), "UTF_8");
					/*
					 * We will parse the XML content looking for the "<title>"
					 * tag which appears inside the "<item>" tag. However, we
					 * should take in consideration that the rss feed name also
					 * is enclosed in a "<title>" tag. As we know, every feed
					 * begins with these lines:
					 * "<channel><title>Feed_Name</title>...." so we should skip
					 * the "<title>" tag which is a child of "<channel>" tag,
					 * and take in consideration only "<title>" tag which is a
					 * child of "<item>"
					 * 
					 * In order to achieve this, we will make use of a boolean
					 * variable.
					 */
					boolean insideItem = false;
					// Returns the type of current event: START_TAG,
					// END_TAG, etc..
					int eventType = xpp.getEventType();
					while (eventType != XmlPullParser.END_DOCUMENT) {
						if (eventType == XmlPullParser.START_TAG) {
							if (xpp.getName().equalsIgnoreCase("item")) {
								insideItem = true;
							} else if (xpp.getName().equalsIgnoreCase("title")) {
								if (insideItem)
									headlines.add(xpp.nextText()); // extract
																	// the
																	// headline
							} else if (xpp.getName().equalsIgnoreCase("link")) {
								if (insideItem)
									links.add(xpp.nextText()); // extract
																// the
																// link
																// of
																// article
							}
						} else if (eventType == XmlPullParser.END_TAG
								&& xpp.getName().equalsIgnoreCase("item")) {
							insideItem = false;
						}
						eventType = xpp.next(); // move to next
												// element
					}
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (XmlPullParserException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}

				handler.sendEmptyMessage(0);
			}
		}.start();
	}

	public InputStream getInputStream(URL url) {
		try {
			return url.openConnection().getInputStream();
		} catch (IOException e) {
			return null;
		}
	}

	protected void onListItemClick(ListView l, View v, final int position,
			long id) {

		Intent i = new Intent(LiveNewsActivity.this, WebViewActivity.class);
		i.putExtra("Links", (String) links.get(position));
		startActivity(i);

		/*
		 * Uri uri = Uri.parse((String) links.get(position)); Intent intent =
		 * new Intent(Intent.ACTION_VIEW, uri); startActivity(intent);
		 */
	}

}