package trova.funghi.authentication;

/**
 * Created by xid73 on 24/06/2017.
 */

public interface OnAuthenticationListener {

    public void onAuthenticationSuccess();
    public void onAuthenticationFailure(Exception failureEx);
    public void onValidationFailure();
}
