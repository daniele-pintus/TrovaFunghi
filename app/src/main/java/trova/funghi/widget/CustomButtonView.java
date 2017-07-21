/**
 * 
 */
package trova.funghi.widget;

import trova.funghi.R;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * @author xid73
 *
 */
public class CustomButtonView extends Button{

	public CustomButtonView(Context context, AttributeSet attrs,int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.init(context);
	}

	public CustomButtonView(Context context, AttributeSet attrs) {
		this(context, attrs, R.attr.customButtonViewStyle);
	}

	public CustomButtonView(Context context) {
		this(context, null, R.attr.customButtonViewStyle);
	}


	protected void init(Context context){
		if (isInEditMode()) {
			return;
		}
	}
	
}
