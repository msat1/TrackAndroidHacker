package com.appzdevelopers.trackandhackerf;

import java.util.List;
import java.util.Set;

import android.bluetooth.BluetoothDevice;
import android.net.wifi.ScanResult;

public class Utils {
	List<ScanResult> wifiList;
	Set<BluetoothDevice> pairedDevices;
	String networkOperatorName;
	static String eMail;

	public List<ScanResult> getWifiList() {
		return wifiList;
	}

	public void setWifiList(List<ScanResult> wifiList) {
		this.wifiList = wifiList;
	}

	public Set<BluetoothDevice> getPairedDevices() {
		return pairedDevices;
	}

	public void setPairedDevices(Set<BluetoothDevice> pairedDevices) {
		this.pairedDevices = pairedDevices;
	}

	public String getNetworkOperatorName() {
		return networkOperatorName;
	}

	public void setNetworkOperatorName(String networkOperatorName) {
		this.networkOperatorName = networkOperatorName;
	}

	public static String geteMail() {
		return eMail;
	}

	public static void seteMail(String eMail) {
		Utils.eMail = eMail;
	}

}
