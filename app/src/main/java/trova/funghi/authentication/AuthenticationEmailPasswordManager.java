package trova.funghi.authentication;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.lang.ref.WeakReference;

public class AuthenticationEmailPasswordManager {
    protected final String LOG_TAG = this.getClass().getSimpleName();
    private FirebaseAuth fbAuth;
    private WeakReference<OnAuthenticationListener> wRefOnAuthenticationListener;
    public AuthenticationEmailPasswordManager(FirebaseAuth fbAuth){
        this.fbAuth = fbAuth;
    }

    public void setOnAuthenticationListener(OnAuthenticationListener onAuthenticationListener){
        this.wRefOnAuthenticationListener = new WeakReference<OnAuthenticationListener>(onAuthenticationListener);
    }

    public void sendEmailVerification(){
        Log.d(LOG_TAG,"sendEmailVerification");
        OnCompleteListener<Void> listenerSendEmail = new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    OnAuthenticationListener listener = wRefOnAuthenticationListener.get();
                    if(listener!=null){
                        listener.onAuthenticationSuccess();
                    }
                }else{
                    OnAuthenticationListener listener = wRefOnAuthenticationListener.get();
                    if(listener!=null){
                        listener.onAuthenticationSuccess();
                    }
                }
            }
        };
        this.fbAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(listenerSendEmail);
    }

    public void deleteAccount(){
        Log.d(LOG_TAG,"deleteAccount");
        OnCompleteDeleteAccountListener onCompleteDeleteAccountListener = new OnCompleteDeleteAccountListener(this.wRefOnAuthenticationListener);
        FirebaseUser fbCurrUser = this.fbAuth.getCurrentUser();
        fbCurrUser.delete().addOnCompleteListener(onCompleteDeleteAccountListener);
    }

    public void reauthenticate(String password){
        if(password==null|| password.trim().equals("")){
            password="none";
        }
        Log.d(LOG_TAG,"reauthenticate");
        OnCompleteReauthenticateListener onCompleteReloadUserListener = new OnCompleteReauthenticateListener(this.wRefOnAuthenticationListener);
        FirebaseUser fbCurrUser = this.fbAuth.getCurrentUser();
        this.fbAuth.getCurrentUser().reauthenticate(EmailAuthProvider.getCredential(fbCurrUser.getEmail(),password)).addOnCompleteListener(onCompleteReloadUserListener);
    }

    public void reloadUser(){
        Log.d(LOG_TAG,"reloadUser");
        OnCompleteReloadUserListener onCompleteReloadUserListener = new OnCompleteReloadUserListener(this.wRefOnAuthenticationListener);
        this.fbAuth.getCurrentUser().reload().addOnCompleteListener(onCompleteReloadUserListener);
    }

    public void sendPasswordResetEmail(String email){
        Log.d(LOG_TAG,"sendPasswordResetEmail");
        OnCompleteSendPasswordResetEmailListener onCompleteSigninListener = new OnCompleteSendPasswordResetEmailListener(this.wRefOnAuthenticationListener);
        this.fbAuth.sendPasswordResetEmail(email).addOnCompleteListener(onCompleteSigninListener);
    }

    public void signin(String email, String password){
        Log.d(LOG_TAG,"signin");
        OnCompleteSigninListener onCompleteSigninListener = new OnCompleteSigninListener(this.wRefOnAuthenticationListener);
        this.fbAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(onCompleteSigninListener);
    }

    public void signup(String email, String password){
        Log.d(LOG_TAG,"signup");
        OnCompleteSigninListener onCompleteSigninListener = new OnCompleteSigninListener(this.wRefOnAuthenticationListener);
        this.fbAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(onCompleteSigninListener);
    }

    public void signout(){
        Log.d(LOG_TAG,"signout");
        this.fbAuth.signOut();
        OnAuthenticationListener listener = this.wRefOnAuthenticationListener.get();
        if(listener!=null){
            listener.onAuthenticationSuccess();
        }
    }

    private class OnCompleteDeleteAccountListener implements OnCompleteListener<Void>{
        WeakReference<OnAuthenticationListener> refListener;
        public OnCompleteDeleteAccountListener(WeakReference<OnAuthenticationListener> _refListener){
            this.refListener = _refListener;
        }
        @Override
        public void onComplete(@NonNull Task<Void> task) {
            if(task.isSuccessful()){
                Log.d(LOG_TAG,"task.isSuccessful() "+task.isSuccessful());
                OnAuthenticationListener listener = this.refListener.get();
                if(listener!=null){
                    listener.onAuthenticationSuccess();
                }
            }else{
                Log.d(LOG_TAG,"task not successful "+task.isSuccessful());
                OnAuthenticationListener listener = this.refListener.get();
                if(listener!=null){
                    listener.onAuthenticationFailure(task.getException());
                }
            }
        }
    }

    private class OnCompleteReauthenticateListener implements OnCompleteListener<Void>{
        WeakReference<OnAuthenticationListener> refListener;
        public OnCompleteReauthenticateListener(WeakReference<OnAuthenticationListener> _refListener){
            this.refListener = _refListener;
        }
        @Override
        public void onComplete(@NonNull Task<Void> task) {
            if(task.isSuccessful()){
                Log.d(LOG_TAG,"task.isSuccessful() "+task.isSuccessful());
                OnAuthenticationListener listener = this.refListener.get();
                if(listener!=null){
                    listener.onAuthenticationSuccess();
                }
            }else{
                Log.d(LOG_TAG,"task not successful "+task.isSuccessful());
                OnAuthenticationListener listener = this.refListener.get();
                if(listener!=null){
                    listener.onAuthenticationFailure(task.getException());
                }
            }
        }
    }

    private class OnCompleteReloadUserListener implements OnCompleteListener<Void>{
        WeakReference<OnAuthenticationListener> refListener;
        public OnCompleteReloadUserListener(WeakReference<OnAuthenticationListener> _refListener){
            this.refListener = _refListener;
        }
        @Override
        public void onComplete(@NonNull Task<Void> task) {
            if(task.isSuccessful()){
                Log.d(LOG_TAG,"task.isSuccessful() "+task.isSuccessful());
                OnAuthenticationListener listener = this.refListener.get();
                if(listener!=null){
                    listener.onAuthenticationSuccess();
                }
            }else{
                Log.d(LOG_TAG,"task not successful "+task.isSuccessful());
                OnAuthenticationListener listener = this.refListener.get();
                if(listener!=null){
                    listener.onAuthenticationFailure(task.getException());
                }
            }
        }
    }

    private class OnCompleteSendPasswordResetEmailListener implements OnCompleteListener<Void>{
        WeakReference<OnAuthenticationListener> refListener;
        public OnCompleteSendPasswordResetEmailListener(WeakReference<OnAuthenticationListener> _refListener){
            this.refListener = _refListener;
        }

        @Override
        public void onComplete(@NonNull Task<Void> task) {
            if(task.isSuccessful()){
                Log.d(LOG_TAG,"task.isSuccessful() "+task.isSuccessful());
                OnAuthenticationListener listener = this.refListener.get();
                if(listener!=null){
                    listener.onAuthenticationSuccess();
                }
            }else{
                Log.d(LOG_TAG,"task not successful "+task.isSuccessful());
                OnAuthenticationListener listener = this.refListener.get();
                if(listener!=null){
                    listener.onAuthenticationFailure(task.getException());
                }
            }
        }
    }

    private class OnCompleteSigninListener implements OnCompleteListener<AuthResult>{
        WeakReference<OnAuthenticationListener> refListener;
        public OnCompleteSigninListener(WeakReference<OnAuthenticationListener> _refListener){
            this.refListener = _refListener;
        }

        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            if(task.isSuccessful()){
                Log.d(LOG_TAG,"task.isSuccessful() "+task.isSuccessful());
                OnAuthenticationListener listener = this.refListener.get();
                if(listener!=null){
                    listener.onAuthenticationSuccess();
                }
            }else{
                Log.d(LOG_TAG,"task not successful "+task.isSuccessful());
                OnAuthenticationListener listener = this.refListener.get();
                if(listener!=null){
                    listener.onAuthenticationFailure(task.getException());
                }
            }
        }
    }
}
