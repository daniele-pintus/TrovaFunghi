/**
 * 
 */
package trova.funghi.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import trova.funghi.R;



/**
 * @author xid73
 *
 */
public class SimpleWheelDialog extends Dialog{

	public SimpleWheelDialog(Context context) {
		super(context,R.style.DialogLayoutTheme);
//		super(context);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setCancelable(false);
		setContentView(R.layout.simple_loading_wheel);

	}
}
