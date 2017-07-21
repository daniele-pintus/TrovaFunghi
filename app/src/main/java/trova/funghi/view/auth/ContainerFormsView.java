package trova.funghi.view.auth;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import trova.funghi.R;
import trova.funghi.persistence.entity.IEntity;

public class ContainerFormsView extends RelativeLayout {

    protected final String LOG_TAG = this.getClass().getSimpleName();

    public enum AuthenticationMode{
        SIGNIN(R.id.formSignin,R.string.lbl_title_sign_in,"SIGNUP",R.string.lbl_link_signup),
        SIGNUP(R.id.formSignup,R.string.lbl_title_sign_up,"SIGNIN",R.string.lbl_link_signin),
        RESETPWD(R.id.formSendPasswordResetEmail,R.string.lbl_title_send_pwd_reset_email,"SIGNIN",R.string.lbl_link_signin);

        private int idForm;
        private int idTitle;
        private String gotoOtherMode;
        private int idLabelLinkgotoMode;
        AuthenticationMode(int idForm, int idTitle, String gotoOtherMode, int idLabelLinkgotoMode){
            this.idForm = idForm;
            this.idTitle = idTitle;
            this.gotoOtherMode = gotoOtherMode;
            this.idLabelLinkgotoMode = idLabelLinkgotoMode;
        }

        protected int getIdForm(){
            return this.idForm;
        }
        protected int getidTitle(){
            return this.idTitle;
        }
        protected String getGotoOtherMode(){ return this.gotoOtherMode;}
        protected int getIdLabelLinkgotoMode(){return this.idLabelLinkgotoMode;}
    }

    private OnContainerAuthenticationFlowListener listener;
    private WeakReference<OnContainerAuthListener> wRefOnContainerAuthListener;
    private View allFormsAuth;
    private View selectedView;
    private View authProgress;
    private TextView titleAuthView;
    private TextView gotoSign;
    private TextView gotoResetPwdView;
    private Button authAbortBtn;

    private View allFormsAuthContainer;

    public ContainerFormsView(Context context) {
        super(context);
        init(context,null);
    }

    public ContainerFormsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public ContainerFormsView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context,attrs);
    }

    protected void init(Context context, AttributeSet attrs){
        if (isInEditMode()) {
            return;
        }
        inflate(context,R.layout.container_forms_authentication,this);
        this.allFormsAuth = this.findViewById(R.id.allFormsAuth);
        this.allFormsAuthContainer = this.allFormsAuth.findViewById(R.id.allFormsAuthContainer);
        this.authProgress = this.findViewById(R.id.authProgress);
        this.gotoSign = (TextView)this.allFormsAuth.findViewById(R.id.gotoSign);
        this.gotoResetPwdView = (TextView)this.allFormsAuth.findViewById(R.id.gotoResePwd);
        wRefOnContainerAuthListener = new WeakReference<OnContainerAuthListener>(null);
        this.listener = new OnContainerAuthenticationFlowListener();
        this.authAbortBtn = (Button)this.findViewById(R.id.authAbort);
        this.authAbortBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onAbort();
            }
        });
        setListenerForFormView();
        retrieveXMLAttributes(context,attrs);
    }

    public void retrieveXMLAttributes(Context context, AttributeSet attrs) {
        if(attrs!=null){
            TypedArray customAttributes = context.obtainStyledAttributes(attrs, R.styleable.ContainerFormsView);
            if(customAttributes != null) {
                // Get the custom properties from the XML to set in its properties
                int roundedStyle = customAttributes.getInt(R.styleable.ContainerFormsView_authentication_mode, AuthenticationMode.SIGNIN.ordinal());
                AuthenticationMode style = AuthenticationMode.values()[roundedStyle];
                setAuthenticationMode(style);
                customAttributes.recycle();
            }
        }else{
            setAuthenticationMode(AuthenticationMode.SIGNIN);
        }
    }

    public void setAuthenticationMode(final AuthenticationMode style) {
        for(AuthenticationMode current : AuthenticationMode.values()){
            this.allFormsAuth.findViewById(current.getIdForm()).setVisibility(View.GONE);
        }
        this.selectedView = this.allFormsAuthContainer.findViewById(style.getIdForm());
        this.selectedView.setVisibility(View.VISIBLE);
        this.titleAuthView = (TextView) this.findViewById(R.id.titleAuth);
        this.titleAuthView.setText(getContext().getString(style.getidTitle()));
        this.gotoSign.setText(getContext().getString(style.getIdLabelLinkgotoMode()));
        this.gotoSign.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setAuthenticationMode(AuthenticationMode.valueOf(style.getGotoOtherMode()));
            }
        });
        this.gotoResetPwdView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setAuthenticationMode(AuthenticationMode.RESETPWD);
            }
        });
    }

    public void loadData(IEntity dataToLoad){
        ViewGroup allForms = (ViewGroup)this.allFormsAuthContainer;
        int countChild = allForms.getChildCount();
        for(int i = 0;i<countChild;i++){
            AFormAuthView form = (AFormAuthView)allForms.getChildAt(i);
            form.loadData(dataToLoad);
        }
    }

    public void setListenerForFormView() {
        ViewGroup allForms = (ViewGroup)this.allFormsAuthContainer;
        int countChild = allForms.getChildCount();
        for(int i = 0;i<countChild;i++){
            AFormAuthView form = (AFormAuthView)allForms.getChildAt(i);
            form.setOnAuthenticationFlowListener(this.listener);
        }
    }

    public void updateUI(int idMsg, AlertDialog alertDialog){
        if(((AFormAuthView)this.selectedView)!=null){
            alertDialog.setMessage(((AFormAuthView)this.selectedView).getErrMessage(idMsg));
        }
        alertDialog.show();
    }

    public void setOnContainerAuthListener(OnContainerAuthListener _onContainerAuthListener){
        this.wRefOnContainerAuthListener = new WeakReference<OnContainerAuthListener>(_onContainerAuthListener);
    }

    protected OnContainerAuthListener getOnContainerAuthListener(){
        return this.wRefOnContainerAuthListener.get();
    }

    public interface OnContainerAuthListener{
        public void onExecuteSignin(String email, String password);

        public void onExecuteSignup(String email, String password);

        public void onExecuteSendPasswordResetEmail(String email);

        public void onAbort();
    }

    private class OnContainerAuthenticationFlowListener implements AFormAuthView.IOnAuthenticationFlowListener{
        protected final String LOG_TAG = this.getClass().getSimpleName();
        @Override
        public void onExecuteSignin(View selectedView,String email, String password) {
            ContainerFormsView.this.selectedView = selectedView;
            ContainerFormsView.this.showProgress(true);
            OnContainerAuthListener onContainerAuthListener = ContainerFormsView.this.getOnContainerAuthListener();
            Log.d(LOG_TAG,"onExecuteSignin listener is:"+onContainerAuthListener);
            onContainerAuthListener.onExecuteSignin(email,password);
        }

        @Override
        public void onExecuteSignup(View selectedView, String email, String password) {
            ContainerFormsView.this.selectedView = selectedView;
            ContainerFormsView.this.showProgress(true);
            OnContainerAuthListener onContainerAuthListener = ContainerFormsView.this.getOnContainerAuthListener();
            Log.d(LOG_TAG,"onExecuteSignup listener is:"+onContainerAuthListener);
            onContainerAuthListener.onExecuteSignup(email,password);
        }

        @Override
        public void onExecuteSendPasswordResetEmail(View selectedView, String email) {
            ContainerFormsView.this.selectedView = selectedView;
            ContainerFormsView.this.showProgress(true);
            OnContainerAuthListener onContainerAuthListener = ContainerFormsView.this.getOnContainerAuthListener();
            Log.d(LOG_TAG,"onExecuteSendPasswordResetEmail listener is:"+onContainerAuthListener);
            onContainerAuthListener.onExecuteSendPasswordResetEmail(email);
        }

        public void onAbort(){
            OnContainerAuthListener onContainerAuthListener = ContainerFormsView.this.getOnContainerAuthListener();
            Log.d(LOG_TAG,"onExecuteSendPasswordResetEmail listener is:"+onContainerAuthListener);
            onContainerAuthListener.onAbort();
        }

    }

    public void showProgress(boolean show){
        authProgress.setVisibility(show ? View.VISIBLE : View.GONE);
//        selectedView.setVisibility(show ? View.GONE : View.VISIBLE);
        allFormsAuth.setVisibility(show ? View.GONE : View.VISIBLE);
    }
}
