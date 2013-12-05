package com.gueei.applocker;

import gueei.binding.Binder;
import gueei.binding.Command;
import gueei.binding.Observable;
import gueei.binding.observables.BooleanObservable;
import gueei.binding.observables.StringObservable;
import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

public class LockScreenActivity extends Activity {
	public static final String BlockedPackageName = "locked package name";
	public static final String BlockedActivityName = "locked activity name";
	public static final String ACTION_APPLICATION_PASSED = "com.gueei.applocker.applicationpassedtest";
	public static final String EXTRA_PACKAGE_NAME = "com.gueei.applocker.extra.package.name";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    Wallpaper.set(WallpaperManager.getInstance(this).getFastDrawable());
	    Binder.setAndBindContentView(this, R.layout.lockscreen, this);
	}
	
	public final Observable<Drawable> Wallpaper = new Observable<Drawable>(Drawable.class);
	public final Command Number = new Command(){
		@Override
		public void Invoke(View view, Object... args) {
			if ((args.length<1)||!(args[0] instanceof Integer)) return;
			Integer number = (Integer)args[0];
			Password.set(Password.get() + number);
		}
	};
	
	public final Command Clear = new Command(){
		@Override
		public void Invoke(View view, Object... args) {
			Password.set("");
		}
	};
	public final Command Verify = new Command(){
		@Override
		public void Invoke(View view, Object... args) {
			if (verifyPassword()){
				Passed.set(true);
				test_passed();
			}else{
				Passed.set(false);
				Password.set("");
			}
		}
	};
	public final BooleanObservable Passed = new BooleanObservable(false);

	private void test_passed() {
		this.sendBroadcast(
				new Intent()
					.setAction(ACTION_APPLICATION_PASSED)
					.putExtra(
							EXTRA_PACKAGE_NAME, getIntent().getStringExtra(BlockedPackageName)));
		finish();
	}
    
    public boolean verifyPassword(){
    	if (Password.get() == null) return false;
    	return Password.get().equals(AppLockerPreference.getInstance(this).getPassword());
    }
    
    public final StringObservable Password = new StringObservable("");

    @Override
	public void onBackPressed() {
    	Intent intent = new Intent();
    	intent
    		.setAction(Intent.ACTION_MAIN)
    		.addCategory(Intent.CATEGORY_HOME)
    		.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    	startActivity(intent);
    	finish();
	}
}
