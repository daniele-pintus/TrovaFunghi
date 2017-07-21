package trova.funghi.view.auth;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import trova.funghi.CoreApplication;
import trova.funghi.R;
import trova.funghi.persistence.entity.Comuni;
import trova.funghi.persistence.entity.ErrorEntity;
import trova.funghi.persistence.entity.IEntity;
import trova.funghi.persistence.entity.Province;
import trova.funghi.persistence.entity.UserProfile;
import trova.funghi.persistence.entity.dao.OnDAOExecutionListener;
import trova.funghi.persistence.entity.dao.ProvinciaDAO;
import trova.funghi.util.dialogs.AlertDialogHelper;
import trova.funghi.util.filters.LineFilter;

/**
 * Created by xid73 on 25/06/2017.
 */

public class FormSignupView extends AFormAuthView {
    protected final String LOG_TAG = this.getClass().getSimpleName();
    //    DATAs
    private ArrayAdapter<String> adapterProv;
    private ArrayAdapter<String> adapterComun;

//    Views
    private TextView nomeAliasView;
    private TextView othersView;
    private AppCompatCheckBox[] checkBoxArrayViews;
    private TextView birthdateView;
    private AutoCompleteTextView provinciaView;
    private AutoCompleteTextView comuneView;
    private static final int flagProvincia = 1<<1;
    private static final int flagComune = 1<<2;
    private static final int NOTHING_LOADED = 0;
    private static final int ALL_LOADED = flagComune|flagProvincia;
    private int resFlag;

    public FormSignupView(Context context) {
        super(context);
    }

    public FormSignupView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FormSignupView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init(final Context context) {
        super.init(context);
        if (isInEditMode()) {
            return;
        }
        resFlag = NOTHING_LOADED;
        this.nomeAliasView = (TextView) this.findViewById(R.id.nomeAlias);
        this.othersView = (TextView) this.findViewById(R.id.others);
        InputFilter[] inFilters = this.othersView.getFilters();
        InputFilter[] inFilters2 = new InputFilter[inFilters.length+1];
        LineFilter lineFilter = new LineFilter(othersView.getMaxLines());
        for(int i = 0;i<inFilters.length;i++){
            inFilters2[i] = inFilters[i];        }
        inFilters2[inFilters.length] = lineFilter;
        this.othersView.setFilters(inFilters2);
        this.birthdateView = (TextView) this.findViewById(R.id.birthdate);
        this.checkBoxArrayViews = new AppCompatCheckBox[4];
        this.checkBoxArrayViews[0] = (AppCompatCheckBox)this.findViewById(R.id.porcino);
        this.checkBoxArrayViews[1] = (AppCompatCheckBox)this.findViewById(R.id.ovulo);
        this.checkBoxArrayViews[2] = (AppCompatCheckBox)this.findViewById(R.id.galletti);
        this.checkBoxArrayViews[3] = (AppCompatCheckBox)this.findViewById(R.id.russola);
        // Get a reference to the AutoCompleteTextView in the layout
        provinciaView = (AutoCompleteTextView) findViewById(R.id.autocompleteProvincia);
        comuneView = (AutoCompleteTextView) findViewById(R.id.autocompleteComune);
        comuneView.setEnabled(false);
// Create the adapter and set it to the AutoCompleteTextView
        adapterProv = new ArrayAdapter<String>(this.context, android.R.layout.simple_list_item_1, new ArrayList<String>(Arrays.asList(new String[]{"none"})));
        adapterComun = new ArrayAdapter<String>(this.context, android.R.layout.simple_list_item_1, new ArrayList<String>(Collections.EMPTY_LIST));
        provinciaView.setAdapter(adapterProv);
        comuneView.setAdapter(adapterComun);
        provinciaView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                resFlag = NOTHING_LOADED;
                if(comuneView.isEnabled()){
                    Comuni comuni = new Comuni();
                    comuni.setComuni(Collections.EMPTY_LIST);
                    adapterComun.clear();
                    adapterComun.addAll(comuni.getComuni());
                    adapterComun.notifyDataSetChanged();
                    comuneView.setText("");
                }
                comuneView.setEnabled(false);
            }
        });
        provinciaView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Qui bisogna chiamare il db per la lista dei comuni in base alla provincia e poi abilitare il comuneView
                String selectedProvincia = parent.getAdapter().getItem(position).toString();
                Log.d(LOG_TAG,"Provincia selected:"+selectedProvincia);
                ProvinciaDAO dao = new ProvinciaDAO();
                dao.loadComuniByProvincia(selectedProvincia, new OnDAOExecutionListener() {
                    @Override
                    public void onSuccessDAOExecuted(IEntity entity) {
                        comuneView.setEnabled(true);
                        comuneView.requestFocus();
                        resFlag = resFlag|flagProvincia;
                        loadData(entity);
                    }

                    @Override
                    public void onErrorDAOExecuted(ErrorEntity errorEntity) {
                        Log.e(LOG_TAG,"errorEntity say:"+errorEntity.getDatabaseError());
                    }
                });

            }
        });
        comuneView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedComune = parent.getAdapter().getItem(position).toString();
                Log.d(LOG_TAG,"Comune selected:"+selectedComune);
                resFlag = resFlag|flagComune;
            }
        });
        comuneView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                resFlag = flagProvincia;
            }
        });
        provinciaView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(final TextView v, int actionId, KeyEvent event) {
                if (actionId == R.id.autocompleteProvincia || actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_NULL) {
                    View vw = v.focusSearch(FOCUS_DOWN);
                    if (vw != null) {
                        if(!vw.isEnabled()){
//                            Check if no view has focus:
                            if (v.hasFocus()) {
                                InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                            }
                            AlertDialog alertDialog = AlertDialogHelper.getAlertDialog(null,context);
                            alertDialog.setMessage(context.getResources().getString(R.string.error_provincia_field_required));
                            alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                    InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.showSoftInput(v,InputMethodManager.SHOW_IMPLICIT);
                                    v.requestFocus();
                                }
                            });
                            alertDialog.show();
                        }else{
                            if (!vw.requestFocus(FOCUS_FORWARD)) {
                                throw new IllegalStateException("focus search returned a view " +
                                        "that wasn't able to take focus!");
                            }else{
                                Toast.makeText(context,"FOCUS_FORWARDed to id"+ CoreApplication.getInstance().getResources().getResourceName(vw.getId()),Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }
                return false;
            }
        });

        comuneView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(final TextView v, int actionId, KeyEvent event) {
                if(resFlag!=ALL_LOADED){
                    if (v.hasFocus()) {
                        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                    ArrayAdapter ada = (ArrayAdapter)provinciaView.getAdapter();
                    AlertDialog alertDialog = AlertDialogHelper.getAlertDialog(null,context);
                    alertDialog.setMessage(context.getResources().getString(R.string.error_comune_field_required));
                    alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.showSoftInput(v,InputMethodManager.SHOW_IMPLICIT);
                            v.requestFocus();
                        }
                    });
                    alertDialog.show();
                }
                return false;
            }
        });
    }

    /** It retrives the custom view */
    @Override
    protected int getContentViewId() {
        return R.layout.form_signup;
    }

    @Override
    protected boolean validateFieldsForm() {
        // Reset errors.
        this.resetErrorsFieldsForm();

        // Store values at the time of the login attempt.
        String email = this.emailView.getText().toString();
        String password = this.passwordView.getText().toString();
        String provincia = this.provinciaView.getText().toString();
        String comune = this.comuneView.getText().toString();
        String nomeAlias = this.nomeAliasView.getText().toString();
        String others = this.othersView.getText().toString();
        String birthDate = this.birthdateView.getText().toString();

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


        /*
        if (TextUtils.isEmpty(nomeAlias)) {
            this.nomeAliasView.setError(this.context.getString(R.string.error_nomealias_field_required));
            focusView = this.nomeAliasView;
            cancel = true;
        }

        if (TextUtils.isEmpty(birthDate)) {
            this.birthdateView.setError(this.context.getString(R.string.error_birthdate_field_required));
            focusView = this.birthdateView;
            cancel = true;
        }

        if(!isProvinciaAndComuneValid()){
            if(resFlag==flagProvincia){
                this.comuneView.setError(this.context.getString(R.string.error_comune_field_required));
                focusView = this.comuneView;
            }
            if(resFlag==flagComune){
                this.provinciaView.setError(this.context.getString(R.string.error_provincia_field_required));
                focusView = this.provinciaView;
            }
            if(resFlag==NOTHING_LOADED){
                this.provinciaView.setError(this.context.getString(R.string.error_provincia_field_required));
                this.provinciaView.setError(this.context.getString(R.string.error_comune_field_required));
                focusView = this.provinciaView;
            }
            cancel = true;
        }
        */
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        }else{
//            prepare the userprofile object
            UserProfile userProfile = CoreApplication.getInstance().getDefaultUserProfile();
            userProfile.getProfile().setContactEmail(email);
            userProfile.getProfile().setOthers(others);
            Map<String,Boolean> prefFunghi = new HashMap<>();
            for(AppCompatCheckBox currCheck : checkBoxArrayViews){
                prefFunghi.put(currCheck.getText().toString(),currCheck.isChecked());
            }
            userProfile.getProfile().setPrefFunghi(prefFunghi);
//            optional fileds
//            userProfile.getProfile().setName(nomeAlias);
//            userProfile.getProfile().setBirthDate(birthDate);
//            userProfile.getProfile().setProvincia(provincia);
//            userProfile.getProfile().setComune(comune);
        }
        return !cancel;
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 8;
    }

    private boolean isProvinciaAndComuneValid(){
        return (resFlag==ALL_LOADED);
    }


    @Override
    protected void resetFieldsFormValues() {
        this.passwordView.setText(null);
        this.emailView.setText(null);
        this.provinciaView.setText(null);
        this.comuneView.setText(null);
        this.nomeAliasView.setText(null);
        this.othersView.setText(null);
        this.birthdateView.setText(null);
        for(AppCompatCheckBox curr : checkBoxArrayViews){
            curr.setChecked(false);
        }
    }

    @Override
    protected void resetErrorsFieldsForm() {
        this.passwordView.setError(null);
        this.emailView.setError(null);
        this.provinciaView.setError(null);
        this.comuneView.setError(null);
        this.nomeAliasView.setError(null);
        this.othersView.setError(null);
        this.birthdateView.setError(null);
    }

    @Override
    public void loadData(IEntity dataToLoad) {
        if(dataToLoad.getEntityName().equals(Province.CHILD_PROVINCE)){
            this.adapterProv.clear();
            this.adapterProv.addAll(((Province)dataToLoad).getDesc()) ;
            this.adapterProv.notifyDataSetChanged();
        }else if(dataToLoad.getEntityName().equals(Comuni.CHILD_COMUNI)){
            this.adapterComun.clear();
            this.adapterComun.addAll(((Comuni)dataToLoad).getComuni()) ;
            this.adapterComun.notifyDataSetChanged();
        }

    }

    @Override
    protected void executeAuthFlowOperation(View v) {
        IOnAuthenticationFlowListener authListener = this.getOnAuthenticationFlowListener();
        if(this.validateFieldsForm()){
            if(authListener!=null){
                authListener.onExecuteSignup(this,this.emailView.getText().toString(),this.passwordView.getText().toString());
            }else{
                Log.d(LOG_TAG,"IOnAuthenticationFlowListener is null");
            }
        }
    }
}
