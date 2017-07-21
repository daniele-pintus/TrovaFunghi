package trova.funghi.view.auth;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import trova.funghi.R;
import trova.funghi.persistence.entity.IEntity;

public class FormSigninView extends AFormAuthView {
    protected final String LOG_TAG = this.getClass().getSimpleName();

    private TextView emailSigninBtn;

    public FormSigninView(Context context) {
        super(context);
    }

    public FormSigninView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FormSigninView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init(Context context) {
        super.init(context);
    }

    @Override
    protected void executeAuthFlowOperation(View view) {
        IOnAuthenticationFlowListener authListener = this.getOnAuthenticationFlowListener();
        if(this.validateFieldsForm()){
            if(authListener!=null){
                authListener.onExecuteSignin(this,this.emailView.getText().toString(),this.passwordView.getText().toString());
            }else{
                Log.d(LOG_TAG,"IOnAuthenticationFlowListener is null");
            }
        }
    }

    /** It retrives the custom view */
    @Override
    protected int getContentViewId() {
        return R.layout.form_signin;
    }

    @Override
    protected boolean validateFieldsForm() {
        // Reset errors.
        this.emailView.setError(null);
        this.passwordView.setError(null);

        // Store values at the time of the login attempt.
        String email = this.emailView.getText().toString();
        String password = this.passwordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid passwordView, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            this.passwordView.setError(this.context.getString(R.string.error_password_field_required));
            focusView = this.passwordView;
            cancel = true;
        }else if(!isPasswordValid(password)){
            this.passwordView.setError(this.context.getString(R.string.error_invalid_password));
            focusView = this.passwordView;
            cancel = true;
        }

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

    private boolean isPasswordValid(String password) {
        return password.length() >= 8;
    }

    @Override
    protected void resetFieldsFormValues() {
        this.emailView.setText(null);
        this.passwordView.setText(null);
    }

    @Override
    protected void resetErrorsFieldsForm() {
        this.emailView.setError(null);
        this.passwordView.setError(null);
    }

    @Override
    public void loadData(IEntity dataToLoad) {

    }
}

