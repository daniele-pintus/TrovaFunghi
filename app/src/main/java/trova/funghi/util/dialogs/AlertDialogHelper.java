package trova.funghi.util.dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;

import trova.funghi.R;

/**
 * Created by xid73 on 01/07/2017.
 */

public class AlertDialogHelper {
    public static AlertDialog getAlertDialog(DialogInterface.OnClickListener dialogOnClickListener, Context context){
        return getAlertDialog(dialogOnClickListener, context, 0);
    }
    public static AlertDialog getAlertDialog(DialogInterface.OnClickListener dialogOnClickListener, Context context, int idMsg){
        DialogInterface.OnClickListener _dialogOnClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        };
        if(dialogOnClickListener==null){
            dialogOnClickListener = _dialogOnClickListener;
        }


        final AlertDialog alert = new AlertDialog.Builder(context, R.style.DialogLayoutTheme_MyDialog)
                .setView(R.layout.alertdialog_common)
                .setNeutralButton("OK",dialogOnClickListener)
                .create();
        if(idMsg!=0){
            alert.setMessage(context.getString(idMsg));
        }
        return alert;
    }

    public static AlertDialog getAlertDialogCustomView(
            DialogInterface.OnClickListener dialogOnClickListener, Context context, int idMsg,int customViewId, int lblBtnPositive){
        DialogInterface.OnClickListener _dialogOnClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        };

        LayoutInflater inflater = LayoutInflater.from(context);

        final AlertDialog alert = new AlertDialog.Builder(context, R.style.DialogLayoutTheme_MyDialog)
                .setView(customViewId)
                .setNeutralButton("Annulla",_dialogOnClickListener)
                .setPositiveButton(context.getString(lblBtnPositive),dialogOnClickListener)
                .create();
        if(idMsg!=0){
            alert.setMessage(context.getString(idMsg));
        }
        return alert;
    }
}
