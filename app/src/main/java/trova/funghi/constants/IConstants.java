/**
 * 
 */
package trova.funghi.constants;

import android.net.Uri;

/**
 * @author xid73
 *
 */
public interface IConstants {

	public static Uri baseResourceUri = Uri.parse("android.resource://"+AppIntents.NAME_SPACE_APP+"/");

	public static final String FILE_PREFERENCE = AppIntents.NAME_SPACE_APP+"_pref";

	public static  final int STATE_AUTH_REQUESTED = 2000;
	public static  final int STATE_AUTH_EXIT = 2001;
	public static  final int STATE_AUTH_INI_SIGNIN = 2002;
	public static  final int STATE_AUTH_ACCOUNT_SIGNEDIN = 2003;
	public static  final int STATE_AUTH_INI_SIGNIN_COMPLETE = 2004;
	public static  final int STATE_AUTH_INI_REGISTER = 2005;
	public static  final int STATE_AUTH_ACCOUNT_REGISTERED = 2006;
	public static  final int STATE_AUTH_COMPLETE = 2007;
	public static  final int STATE_AUTH_ACCOUNT_SIGNEDOUT = 2008;

}
