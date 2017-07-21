/**
 * 
 */
package trova.funghi;

import android.app.Application;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import trova.funghi.handler.BaseUncaughtExceptionHandler;
import trova.funghi.persistence.entity.Account;
import trova.funghi.persistence.entity.IEntity;
import trova.funghi.persistence.entity.Profile;
import trova.funghi.persistence.entity.UserProfile;
import trova.funghi.persistence.entity.dao.OnDAOExecutionListener;

/**
 * @author xid73
 *
 */
public class CoreApplication extends Application{

	protected final String LOG_TAG = this.getClass().getSimpleName();
	private static CoreApplication mInstance;
	private boolean isAppInBackground = false;
	private Boolean isTablet;
	private FirebaseAuth firebaseAuth;
	private static UserProfile userProfile;

	public static CoreApplication getInstance() {
		if(CoreApplication.userProfile==null){
			Map<String,IEntity> mapCollector = new HashMap<String,IEntity>(0);
			CoreApplication.userProfile = new UserProfile(mapCollector);
			userProfile.setProfile(new Profile("xxx@xxx.xxx"));
			userProfile.setRegisteredAccount(new Account());
		}
		return mInstance;
	}

	/**
	 * WARNING do not use as a data UserProfile already filled with correct info.<br>
	 * Instead use {@link trova.funghi.persistence.entity.dao.UserProfileDAO#loadUserProfileEntity(OnDAOExecutionListener)}
	 * @return a default UserProfile usefull only to prepare an ogbject to fillin. It do not contain the correct UserProfile
	 */
	public UserProfile getDefaultUserProfile(){
		return this.userProfile;
	}
	
	@Override
	public void onCreate() {
		Log.d(LOG_TAG, "[onCreate] - creating core application");
		Thread.setDefaultUncaughtExceptionHandler(new BaseUncaughtExceptionHandler());
		mInstance = this;
		super.onCreate();

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			MemoryBoss  mMemoryBoss = new MemoryBoss();
			registerComponentCallbacks(mMemoryBoss);
		}
		// [START initialize_auth]
		this.firebaseAuth = FirebaseAuth.getInstance();
	}

	public FirebaseAuth getFirebaseAuth(){
		return this.firebaseAuth;
	}

	public boolean isDebuggable(){
		boolean debuggable = false;
		try {
			int flags = mInstance.getPackageManager().getApplicationInfo(mInstance.getPackageName(), 0).flags;
			debuggable = (flags & ApplicationInfo.FLAG_DEBUGGABLE) == ApplicationInfo.FLAG_DEBUGGABLE;
		} catch (NameNotFoundException e) { }
		return debuggable;
	}
	
	/** set the current application as in background and notify all the opened activities */
	public void setAppInBackground(boolean isAppInBackground) {
		Log.d(LOG_TAG, "[setAppInBackground] - " + (isAppInBackground ? "true" : "false"));
		this.isAppInBackground = isAppInBackground;
	}
	
	public int getStatusBarHeight() {
		int result = 0;
		int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			result = getResources().getDimensionPixelSize(resourceId);
		} 
		return result;
	} 
	
	/** return true if the app is in background */
	public boolean isAppInBackground() {
		return isAppInBackground;
	}
	
	public boolean hasMobileConnection() {
		// Package manager
		boolean hasPhone = getPackageManager().hasSystemFeature(PackageManager.FEATURE_TELEPHONY);
		Log.d(LOG_TAG, "[hasMobileConnection] - PackageManager hasPhone: "+hasPhone);

		return hasPhone;
	}
	
	public boolean isTablet() {
		if (isTablet == null) {
			int deviceSizeMask = getResources().getConfiguration().screenLayout
					& Configuration.SCREENLAYOUT_SIZE_MASK;			

			if (deviceSizeMask == Configuration.SCREENLAYOUT_SIZE_XLARGE
					|| (deviceSizeMask == Configuration.SCREENLAYOUT_SIZE_LARGE)) {
				isTablet = true;
			} else {
				isTablet = false;
			}

		}
		return isTablet;
	}
	
	public boolean isIntentSafe(Intent intent){
		PackageManager packageManager = this.getPackageManager();
		List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);
		return activities.size() > 0;
	}

	public boolean isCurrentUserPresent(){
		if(this.firebaseAuth.getCurrentUser()==null){
			return false;
		}
		return true;
	}
}
