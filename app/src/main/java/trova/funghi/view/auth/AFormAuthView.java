package trova.funghi.view.auth;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import trova.funghi.R;
import trova.funghi.persistence.entity.IEntity;

public abstract class AFormAuthView extends LinearLayout {

    protected final String LOG_TAG = this.getClass().getSimpleName();
    protected EditText passwordView;
    protected EditText emailView;
    protected Button authBtn;
    protected Button authResetBtn;
//    protected TextView gotoSign;
    protected Context context;


    protected IOnAuthenticationFlowListener onAuthenticationFlowListener;
    public AFormAuthView(Context context) {
        super(context);
        init(context);
    }

    public AFormAuthView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AFormAuthView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /** It retrives the custom view */
    protected abstract int getContentViewId();

    protected abstract boolean validateFieldsForm();
    protected abstract void resetFieldsFormValues();
    protected abstract void resetErrorsFieldsForm();
    public abstract void loadData(IEntity dataToLoad);

    protected String getErrMessage(int idMsg){
        return this.context.getString(idMsg);
    }

    protected void init(Context context){

        if (isInEditMode()) {
            return;
        }
        this.context=context;
        inflate(context,getContentViewId(),this);

        this.emailView = (EditText) this.findViewById(R.id.email);
        this.passwordView = (EditText) this.findViewById(R.id.password);
        if(this.passwordView !=null){
            this.passwordView.setError(null);
        }
        this.authBtn = (Button) this.findViewById(R.id.authBtn);
        this.emailView.setError(null);
        this.authBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                hideInputMethodManager();
                executeAuthFlowOperation(v);
            }
        });
        this.authResetBtn = (Button) this.findViewById(R.id.authResetBtn);
        this.authResetBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                hideInputMethodManager();
                resetFieldsFormValues();
            }
        });
    }

    public void hideInputMethodManager(){
        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.getWindowToken(), 0);
    }
    protected abstract void executeAuthFlowOperation(View v);

    protected IOnAuthenticationFlowListener getOnAuthenticationFlowListener() {
        return onAuthenticationFlowListener;
    }

    protected void setOnAuthenticationFlowListener(IOnAuthenticationFlowListener onAuthenticationFlowListener) {
        this.onAuthenticationFlowListener = onAuthenticationFlowListener;
    }

    public interface IOnAuthenticationFlowListener{
        public void onExecuteSignin(View selectedView,String email,String password);
        public void onExecuteSignup(View selectedView,String email,String password);
        public void onExecuteSendPasswordResetEmail(View selectedView,String email);
    }
}
