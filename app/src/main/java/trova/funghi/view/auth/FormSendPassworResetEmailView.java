package trova.funghi.view.auth;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import trova.funghi.R;
import trova.funghi.persistence.entity.IEntity;

/**
 * Created by xid73 on 26/06/2017.
 */

public class FormSendPassworResetEmailView extends AFormAuthView {
    protected final String LOG_TAG = this.getClass().getSimpleName();
    public FormSendPassworResetEmailView(Context context) {
        super(context);
    }

    public FormSendPassworResetEmailView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FormSendPassworResetEmailView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /** It retrives the custom view */
    @Override
    protected int getContentViewId() {
        return R.layout.form_send_password_reset_email;
    }

    @Override
    protected boolean validateFieldsForm() {
        // Reset errors.
        this.emailView.setError(null);

        // Store values at the time of the login attempt.
        String email = this.emailView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid emailView address.
        if (TextUtils.isEmpty(email)) {
            this.emailView.setError(this.context.getString(R.string.error_email_field_required));
            focusView = this.emailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            this.emailView.setError(this.context.getString(R.string.error_invalid_email));
            focusView = this.emailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        }
        return !cancel;
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    public String getErrMessage(int idMsg){
        return this.context.getString(idMsg,this.emailView.getText().toString());
    }

    @Override
    protected void resetFieldsFormValues() {
        this.emailView.setText(null);
    }

    @Override
    protected void resetErrorsFieldsForm() {
        this.emailView.setError(null);
    }

    @Override
    public void loadData(IEntity dataToLoad) {

    }

    @Override
    protected void executeAuthFlowOperation(View v) {
        IOnAuthenticationFlowListener authListener = this.getOnAuthenticationFlowListener();
        if(this.validateFieldsForm()){
            if(authListener!=null){
                authListener.onExecuteSendPasswordResetEmail(this,this.emailView.getText().toString());
            }else{
                Log.d(LOG_TAG,"IOnAuthenticationFlowListener is null");
            }
        }
    }
}
