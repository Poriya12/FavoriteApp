package com.home.launch;


import android.os.Bundle;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Bundle bundle = getIntent().getExtras();

		if(bundle != null && bundle.getString("LAUNCH").equals("YES")) {
			startService(new Intent(MainActivity.this, ServiceFloating.class));
		}

		Button launch = (Button)findViewById(R.id.button1);
		launch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startService(new Intent(MainActivity.this, ServiceFloating.class));
			}
		});

		Button stop = (Button)findViewById(R.id.button2);
		stop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				stopService(new Intent(MainActivity.this, ServiceFloating.class));
			}
		});

		
		
		
	}

	@Override
	protected void onResume() {
		Bundle bundle = getIntent().getExtras();

		if(bundle != null && bundle.getString("LAUNCH").equals("YES")) {
			startService(new Intent(MainActivity.this, ServiceFloating.class));
		}
		super.onResume();
	}
}
