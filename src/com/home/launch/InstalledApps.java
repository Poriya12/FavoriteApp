package com.home.launch;

import android.app.Activity;
import android.app.ListActivity;

import java.util.ArrayList;
import java.util.List;


import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class InstalledApps extends ListActivity {
	private PackageManager packageManager = null;
	private List<ApplicationInfo> applist = null;
	private ApplicationAdapter listadaptor = null;
	private ServiceFloating service;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.apps_activity_main);
		service=new ServiceFloating();
		packageManager = getPackageManager();

		new LoadApplications().execute();
	}

	

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		ApplicationInfo app = applist.get(position);
		
		
		try {
			Bundle bundle=new Bundle();
			bundle.putString("action",app.packageName);
			//bundle.putInt("appicon", app.icon);
			
			Intent intent=new Intent(this,ServiceFloating.class);
			intent.putExtras(bundle);
			
			/*Intent intent = packageManager
					.getLaunchIntentForPackage(app.packageName);
			*/
			
			stopService(intent);
			if (null != intent) {
				startService(intent);
				
				
				
			}
			
			Intent startMain = new Intent(Intent.ACTION_MAIN);
			startMain.addCategory(Intent.CATEGORY_HOME);
			startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(startMain);
		} catch (ActivityNotFoundException e) {
			Toast.makeText(InstalledApps.this, e.getMessage(),
					Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			Toast.makeText(InstalledApps.this, e.getMessage(),
					Toast.LENGTH_LONG).show();
		}
	}

	private List<ApplicationInfo> checkForLaunchIntent(List<ApplicationInfo> list) {
		ArrayList<ApplicationInfo> applist = new ArrayList<ApplicationInfo>();
		for (ApplicationInfo info : list) {
			try {
				if (null != packageManager.getLaunchIntentForPackage(info.packageName)) {
					applist.add(info);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return applist;
	}

	private class LoadApplications extends AsyncTask<Void, Void, Void> {
		private ProgressDialog progress = null;

		@Override
		protected Void doInBackground(Void... params) {
			applist = checkForLaunchIntent(packageManager.getInstalledApplications(PackageManager.GET_META_DATA));
			listadaptor = new ApplicationAdapter(InstalledApps.this,
					R.layout.adapter_row, applist);

			return null;
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected void onPostExecute(Void result) {
			setListAdapter(listadaptor);
			progress.dismiss();
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			progress = ProgressDialog.show(InstalledApps.this, null,
					"Loading...");
			super.onPreExecute();
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);
		}
	}
}
