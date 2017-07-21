package trova.funghi.util.filters;

import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;

/**
 * Created by xid73 on 11/07/2017.
 */

public class LineFilter implements InputFilter {
    protected final String LOG_TAG = this.getClass().getSimpleName();
    private int maxLine;
    public LineFilter(int _maxLine){
        maxLine = _maxLine;
    }

    public static int countOccurrences(String string, char charAppearance) {
        int count = 0;
        for (int i = 0; i < string.length(); i++) {
            if (string.charAt(i) == charAppearance) {
                count++;
            }
        }
        return count;
    }
    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        int idx = countOccurrences(dest.toString(),'\n');
        if(source.toString().indexOf("\n")>-1){
            idx++;
        }
        Log.d(LOG_TAG,"idx"+idx);
        Log.d(LOG_TAG,"dest len:"+dest.length());
        Log.d(LOG_TAG,"dest value:"+dest.toString());
        if(idx>=maxLine){
            Log.d(LOG_TAG,"rows.length>=maxLine"+idx);
            return "";
        }
        return null;
    }
}
