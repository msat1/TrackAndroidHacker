package com.appzdevelopers.trackandhackerf;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

/*
 * Demo of creating an application to open any URL inside the application and clicking on any link from that URl 
 should not open Native browser but  that URL should open in the same screen.

 - Load WebView with progress bar
 */
public class WebViewActivity extends Activity {
	/** Called when the activity is first created. */

	WebView web;
	ProgressBar progressBar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview);
		
		/*AdView adView = (AdView) findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder().addTestDevice(
				AdRequest.DEVICE_ID_EMULATOR).build();

		// Start loading the ad in the background.
		adView.loadAd(adRequest);*/
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			final String links = extras.getString("Links");

			web = (WebView) findViewById(R.id.webview01);
			progressBar = (ProgressBar) findViewById(R.id.progressBar1);

			web.setWebViewClient(new myWebClient());
			web.getSettings().setJavaScriptEnabled(true);
			web.loadUrl(links);
		}
	}

	public class myWebClient extends WebViewClient {
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			progressBar.setVisibility(View.GONE);
		}
	}

	// To handle "Back" key press event for WebView to go back to previous screen.
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && web.canGoBack()) {
			web.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}