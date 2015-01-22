package com.home.launch;

import java.util.ArrayList;
import java.util.List;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.PopupWindow;
import android.widget.Toast;

public class ServiceFloating extends Service implements
		android.view.View.OnClickListener, OnLongClickListener {

	private WindowManager windowManager;
	public static ImageView mAppicon;
	private PopupWindow pwindo;
	public static String action_intent = "abc";
	private PackageManager packageManager = null;

	boolean mHasDoubleClicked = false;
	long lastPressTime;
	private Boolean _enable = true;
	private WindowManager.LayoutParams params;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		packageManager = getPackageManager();
		windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

		mAppicon = new ImageView(this);

		// if(mAppicon==null)
		mAppicon.setImageResource(R.drawable.favorite);

		// final WindowManager.LayoutParams
		params = new WindowManager.LayoutParams(
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_PHONE,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
				PixelFormat.TRANSLUCENT);

		params.gravity = Gravity.TOP | Gravity.LEFT;
		params.x = 0;
		params.y = 100;

		windowManager.addView(mAppicon, params);

		try {
			mAppicon.setOnTouchListener(new View.OnTouchListener() {
				private WindowManager.LayoutParams paramsF = params;
				private int initialX;
				private int initialY;
				private float initialTouchX;
				private float initialTouchY;

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:

						// Get current time in nano seconds.
						long pressTime = System.currentTimeMillis();

						// If double click...
						if (pressTime - lastPressTime <= 300) {

							ServiceFloating.this.stopSelf();
							mHasDoubleClicked = true;
						} else { // If not double click....
							mHasDoubleClicked = false;
						}
						lastPressTime = pressTime;
						initialX = paramsF.x;
						initialY = paramsF.y;
						initialTouchX = event.getRawX();
						initialTouchY = event.getRawY();
						break;
					case MotionEvent.ACTION_UP:
						break;
					case MotionEvent.ACTION_MOVE:
						paramsF.x = initialX
								+ (int) (event.getRawX() - initialTouchX);
						paramsF.y = initialY
								+ (int) (event.getRawY() - initialTouchY);
						Log.i("X & Y", "X:" + paramsF.x + "  Y: " + paramsF.y);
						windowManager.updateViewLayout(mAppicon, paramsF);
						break;
					}
					return false;
				}
			});
		} catch (Exception e) {
			// TODO: handle exception
		}

		if (mHasDoubleClicked)
			stopSelf();
		mAppicon.setOnClickListener(this);
		mAppicon.setOnLongClickListener(this);

	}

	@Override
	@Deprecated
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);

		if (intent != null) {
			action_intent = intent.getStringExtra("action");
			try {
				mAppicon.setImageDrawable(packageManager
						.getApplicationIcon(action_intent));
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	void OnClickListener(View view) {

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mAppicon != null)
			windowManager.removeView(mAppicon);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (action_intent == null) {
			Intent installedapp = new Intent(getApplicationContext(),
					InstalledApps.class);
			installedapp.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(installedapp);
		} else {
			Intent installedapp = packageManager
					.getLaunchIntentForPackage(action_intent);

			installedapp.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(installedapp);

		}
	}

	@Override
	public boolean onLongClick(View v) {
		// TODO Auto-generated method stub
		Intent installedapp = new Intent(getApplicationContext(),
				InstalledApps.class);
		installedapp.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(installedapp);
		return false;
	}

}