/**
 * 
 */
package trova.funghi.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
import trova.funghi.R;

/**
 * @author xid73
 *
 */
public class CustomItemTextView extends TextView {

	/**
	 * @param context
	 */
	public CustomItemTextView(Context context) {
		this(context, null,R.attr.customItemTextViewStyle);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public CustomItemTextView(Context context, AttributeSet attrs) {
		this(context, attrs,R.attr.customItemTextViewStyle);
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyleAttr
	 */
	public CustomItemTextView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.init();
	}
	
	protected void init(){
		if (isInEditMode()) {
			return;
		}
	}

}
