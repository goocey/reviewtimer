package com.example.reviewtimer;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.NumberPicker;
import android.media.MediaPlayer;
import java.io.IOException;
import android.view.Menu;

public class MainActivity extends Activity {
	TextView timer;
	Button stop, start, settimer;
	NumberPicker hour, minute, second;
	MyCountDownTimer cdt = null;
	MediaPlayer startsound = null;
	MediaPlayer finish = null;
	MediaPlayer review_exit = null;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		timer = (TextView) findViewById(R.id.textView1);
		start = (Button) findViewById(R.id.button1);
		stop = (Button) findViewById(R.id.button2);
		hour = (NumberPicker) findViewById(R.id.numberPicker1);
		minute = (NumberPicker) findViewById(R.id.numberPicker2);
		second = (NumberPicker) findViewById(R.id.NumberPicker01);
		settimer = (Button) findViewById(R.id.button3);
		final MyCountDownTimer cdt = new MyCountDownTimer(1000 * 60 * 15, 1000);
		
		startsound = MediaPlayer.create(getApplicationContext(), R.raw.start);
		finish = MediaPlayer.create(getApplicationContext(), R.raw.finish);
		review_exit = MediaPlayer.create(getApplicationContext(), R.raw.review_exit);

		// hour minuteの設定
		hour.setMaxValue(60);
		hour.setMinValue(0);
		minute.setMaxValue(59);
		minute.setMinValue(0);
		second.setMaxValue(59);
		second.setMinValue(0);
		

		// CountDownの初期値
		start.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// カウントダウン開始
				cdt.start();
				startsound.start();
			}
		});

		stop.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// カウントダウン中止
				cdt.cancel();
				review_exit.start();
			}
		});
		settimer.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				cdt.cancel();
				int min_int = minute.getValue();
				int hour_int = hour.getValue();
				int sec_int = second.getValue();
				long totaltime = (min_int * 60 * 60 + hour_int * 60 + sec_int) * 1000;
				MyCountDownTimer cdt = new MyCountDownTimer(totaltime, 1000);
			}
		});
	}

	public class MyCountDownTimer extends CountDownTimer {

		MediaPlayer last5 = null;
		long minute = 0;
		long second = 0;
		long check = 4;
		boolean checkpoint_show = false; 
		public MyCountDownTimer(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
			this.last5 = MediaPlayer.create(getApplicationContext(), R.raw.last5);
		}

		@Override
		public void onFinish() {
			// カウントダウン完了後に呼ばれる
			timer.setText("0");
			finish.start(); // 再生

			Toast.makeText(getApplicationContext(), R.string.time_up_message,
					Toast.LENGTH_LONG).show();
		}

		@Override
		public void onTick(long millisUntilFinished) {
			// インターバル(countDownInterval)毎に呼ばれる
			timer.setText(Long.toString(millisUntilFinished / 1000 / 60) + ":"
					+ Long.toString(millisUntilFinished / 1000 % 60));
			second = millisUntilFinished / 1000 % 60;
			minute = millisUntilFinished / 1000 / 60;
			if (minute == check && !checkpoint_show) {
				checkpoint_show = true;
				Toast.makeText(getApplicationContext(), R.string.checkpoint_message,
						Toast.LENGTH_LONG).show();
				this.last5.start();
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
