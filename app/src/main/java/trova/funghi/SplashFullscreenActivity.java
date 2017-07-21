package trova.funghi;

import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

import trova.funghi.authentication.AuthenticationEmailPasswordManager;
import trova.funghi.authentication.OnAuthenticationListener;
import trova.funghi.constants.AppIntents;
import trova.funghi.constants.IConstants;
import trova.funghi.util.SystemUiHider;
import trova.funghi.util.dialogs.AlertDialogHelper;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class SplashFullscreenActivity extends FragmentActivity {
	protected final String LOG_TAG = this.getClass().getSimpleName();
	/**
	 * Whether or not the system UI should be auto-hidden after
	 * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
	 */
	private static final boolean AUTO_HIDE = true;

	/**
	 * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
	 * user interaction before hiding the system UI.
	 */
	private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

	/**
	 * If set, will toggle the system UI visibility upon interaction. Otherwise,
	 * will show the system UI visibility upon interaction.
	 */
	private static final boolean TOGGLE_ON_CLICK = true;

	/**
	 * The flags to pass to {@link SystemUiHider#getInstance}.
	 */
	private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

	/**
	 * The instance of the {@link SystemUiHider} for this activity.
	 */
	private SystemUiHider mSystemUiHider;

	
	// This snippet hides the system bars.
		private void hideSystemUI() {
			if (Build.VERSION.SDK_INT < 16) {
	            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
	                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
	        }else{
	        	View decorView = getWindow().getDecorView();
	    		// Hide the status bar.
	    		int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
	    		decorView.setSystemUiVisibility(uiOptions);
	        }
			
			// Remember that you should never show the action bar if the
			// status bar is hidden, so hide that too if necessary.
			android.app.ActionBar actionBar = this.getActionBar();
			actionBar.hide();
		}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.d(LOG_TAG,"Result code is["+resultCode+"]");
		switch (resultCode){
			case IConstants.STATE_AUTH_ACCOUNT_SIGNEDIN:
			case IConstants.STATE_AUTH_ACCOUNT_REGISTERED :
				Log.d(LOG_TAG,"STATE_AUTH_ACCOUNT_SIGNEDIN or STATE_AUTH_ACCOUNT_REGISTERED");
				authFlowReloadProfileOrReauthenticate();
				break;
			case SplashFullscreenActivity.RESULT_CANCELED:
				Log.d(LOG_TAG,"RESULT_CANCELED");
				handlerSignout.removeCallbacks(mSignoutRunnable);
				handlerSignout.postDelayed(mSignoutRunnable,2000L);
				authFlowExit();
				break;
		}
	}

	Handler handlerSignout = new Handler();
	Runnable mSignoutRunnable = new Runnable() {
		@Override
		public void run() {
			CoreApplication.getInstance().getFirebaseAuth().signOut();
		}
	};

	private void authFlow(){
		Log.d(LOG_TAG,"authFlow");
        // Check if user is signed in (non-null) and update UI accordingly.
         boolean isCurrentUserPresent = CoreApplication.getInstance().isCurrentUserPresent();
        if (!isCurrentUserPresent){
			authFlowRequired();
        }else{
			Log.d(LOG_TAG,"Account is present(we are logged-in). Now checks if email was verified.");
            authFlowReloadProfileOrReauthenticate();
        }
    }

	private void authFlowExit(){
		this.finish();
	}

	private void authFlowRequired(){
		Log.d(LOG_TAG,"call AuthenticationActivity");
		Intent intentAuth = new Intent(this.getApplicationContext(), AuthenticationActivity.class);
		startActivityForResult(intentAuth, IConstants.STATE_AUTH_REQUESTED);
	}

	public void authAfterUserReloaded(){
		if(CoreApplication.getInstance().getFirebaseAuth().getCurrentUser().isEmailVerified()){
			Intent intent = new Intent(AppIntents.ENTRY_POINT_APP);
			Log.d(LOG_TAG,"getIntent().getExtras()="+getIntent().getExtras());
			PendingIntent pi = PendingIntent.getActivity(CoreApplication.getInstance(), new Random().nextInt(), intent, PendingIntent.FLAG_ONE_SHOT);
			try {
				pi.send();
				overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.fade_out);
			} catch (CanceledException e) {
				Log.e(LOG_TAG, e.getMessage());
			}
			SplashFullscreenActivity.this.finish();
		}else{
			AlertDialog alertDialog = AlertDialogHelper.getAlertDialog(null,SplashFullscreenActivity.this);
			alertDialog.setMessage(this.getString(R.string.msg_email_is_not_verifyed));
			alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
				@Override
				public void onDismiss(DialogInterface dialog) {
					AuthenticationEmailPasswordManager authManagerEmailPwd = new AuthenticationEmailPasswordManager(CoreApplication.getInstance().getFirebaseAuth());
					authManagerEmailPwd.setOnAuthenticationListener(new OnAuthenticationSignoutListener());
					authManagerEmailPwd.signout();
				}
			});
			alertDialog.show();
		}
	}
	private void authFlowReloadProfileOrReauthenticate(){
		Log.d(LOG_TAG,"into authFlowReloadProfileOrReauthenticate");
		boolean isCurrentUserPresent = CoreApplication.getInstance().isCurrentUserPresent();
		if(isCurrentUserPresent){
			AuthenticationEmailPasswordManager authManager = new AuthenticationEmailPasswordManager(CoreApplication.getInstance().getFirebaseAuth());
			authManager.setOnAuthenticationListener(new OnReloadUserAuthenticationListener());
			authManager.reloadUser();
		}else{
			authFlow();
		}
	}

	Handler mWaitHandler = new Handler();
	Runnable mWaitRunnable = new Runnable() {
			@Override
			public void run() {
				authFlow();
			}
		};

		/**
		 * Schedules a call to hide() in [delay] milliseconds, canceling any
		 * previously scheduled calls.
		 */
        private void startAuthenticationFlow(int delayMillis) {
			mWaitHandler.removeCallbacks(mWaitRunnable);
			mWaitHandler.postDelayed(mWaitRunnable, delayMillis);
		}

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(LOG_TAG,"onStart");
    }

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        Log.d(LOG_TAG,"onCreate");
		Log.d(LOG_TAG, "Started intent:["+this.getIntent()+"]");
        // Get token
//		String token = FirebaseInstanceId.getInstance().getToken();
//		Log.d(LOG_TAG, "FireBase token:["+token+"]");
		FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        setContentView(R.layout.splash_fullscreen);
		final View controlsView = findViewById(R.id.fullscreen_content_controls);
		final View contentView = findViewById(R.id.fullscreen_content);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
        Log.d(LOG_TAG,"onPostCreate");
		// Trigger the initial hide() shortly after the activity has been
		// created, to briefly hint to the user that UI controls
		// are available.
        startAuthenticationFlow(AUTO_HIDE_DELAY_MILLIS);
		delayedHide(100);
	}

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(LOG_TAG,"onStop");
    }

    /**
	 * Touch listener to use for in-layout UI controls to delay hiding the
	 * system UI. This is to prevent thgotoMain(AUTO_HIDE_DELAY_MILLIS);e jarring behavior of controls going away
	 * while interacting with activity UI.
	 */
	View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
		@Override
		public boolean onTouch(View view, MotionEvent motionEvent) {
			if (AUTO_HIDE) {
				delayedHide(AUTO_HIDE_DELAY_MILLIS);
			}
			return false;
		}
	};

	Handler mHideHandler = new Handler();
	Runnable mHideRunnable = new Runnable() {
		@Override
		public void run() {
			mSystemUiHider.hide();
			// Remember that you should never show the action bar if the
			// status bar is hidden, so hide that too if necessary.
//			android.app.ActionBar actionBar = SplashFullscreenActivity.this.getActionBar();
//			actionBar.hide();
		}
	};

	/**
	 * Schedules a call to hide() in [delay] milliseconds, canceling any
	 * previously scheduled calls.
	 */
	private void delayedHide(int delayMillis) {
//		mHideHandler.removeCallbacks(mHideRunnable);
//		mHideHandler.postDelayed(mHideRunnable, delayMillis);
	}

	private class OnReloadUserAuthenticationListener implements OnAuthenticationListener{

		@Override
		public void onAuthenticationSuccess() {
			authAfterUserReloaded();
		}

		@Override
		public void onAuthenticationFailure(Exception failureEx) {
			AlertDialog alertDialog = AlertDialogHelper.getAlertDialog(null,SplashFullscreenActivity.this);
			alertDialog.setMessage(CoreApplication.getInstance().getString(R.string.err_reload_user));
			alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
				@Override
				public void onDismiss(DialogInterface dialog) {
					authFlow();
				}
			});
			alertDialog.show();
		}

		@Override
		public void onValidationFailure() {

		}
	}

	private class OnAuthenticationSignoutListener implements OnAuthenticationListener {

		@Override
		public void onAuthenticationSuccess() {
			Log.d(LOG_TAG,"onAuthenticationSuccess");
			SplashFullscreenActivity.this.authFlowRequired();
		}

		@Override
		public void onAuthenticationFailure(Exception failureEx) {
			Log.d(LOG_TAG,"onAuthenticationFailure");
			Log.e(LOG_TAG,"Failure due to:["+failureEx.getMessage()+"]");
			SplashFullscreenActivity.this.finish();
		}

		@Override
		public void onValidationFailure() {
			Log.d(LOG_TAG,"onValidationFailure");
			SplashFullscreenActivity.this.finish();
		}
	}
}
