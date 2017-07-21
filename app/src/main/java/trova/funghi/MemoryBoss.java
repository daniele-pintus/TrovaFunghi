package trova.funghi;

import android.content.ComponentCallbacks2;
import android.content.res.Configuration;
import android.util.Log;

/**
 * taken from:<br>
 * <i>http://stackoverflow.com/questions/4414171/how-to-detect-when-an-android-app-goes-to-the-background-and-come-back-to-the-fo</i>
 */
public class MemoryBoss implements ComponentCallbacks2 {

	@Override
	public void onConfigurationChanged(final Configuration newConfig) {
	}

	@Override
	public void onLowMemory() {
	}

	@Override
	public void onTrimMemory(final int level) {
		if (level == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {
			// We're in the Background
			Log.d(this.getClass().getSimpleName(), "Application going in background");
			CoreApplication.getInstance().setAppInBackground(true);
		}
		// you might as well implement some memory cleanup here and be a nice Android dev.
	}

}
