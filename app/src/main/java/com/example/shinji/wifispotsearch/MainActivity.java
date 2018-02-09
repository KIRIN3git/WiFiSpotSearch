package com.example.shinji.wifispotsearch;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

public class MainActivity extends AppCompatActivity {

	private final int PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Log.w( "DEBUG_DATA", "onCreate Build.VERSION.SDK_INT = " + Build.VERSION.SDK_INT);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			// 既に許可されているか確認
			if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
					!= PackageManager.PERMISSION_GRANTED) {

				// 許可されていなかったらリクエストする
				// ダイアログが表示される
				requestPermissions(
						new String[]{
								Manifest.permission.ACCESS_COARSE_LOCATION
						},
						PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION);
				return;
			}
			else{
				logScanResults();
			}
		}
	}

	@Override
	public void onRequestPermissionsResult(
			int requestCode,
			@NonNull String[] permissions,
			@NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);

		if (requestCode == PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION
				&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {
			// 許可された場合
			logScanResults();
		} else {
			// 許可されなかった場合
			// 何らかの対処が必要
		}
	}

	private void logScanResults() {

		// システムレベルのサービスのハンドルを取得
		WifiManager manager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);

		if (manager.getWifiState() == WifiManager.WIFI_STATE_ENABLED) {

			Log.w( "DEBUG_DATA", "WIFI_STATE_ENABLED");

			// APをスキャン
			manager.startScan();
			// スキャン結果を取得
			List<ScanResult> apList = manager.getScanResults();
			String[] aps = new String[apList.size()];


			for (int i = 0; i < apList.size(); i++) {
				aps[i] = "SSID:" + apList.get(i).SSID + "\n"
						+ apList.get(i).frequency + "MHz " + apList.get(i).level + "dBm";

				Log.w( "DEBUG_DATA", "aps[i] " + aps[i] ) ;
			}
		}
	}
}
