package trova.funghi.view.profile;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
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
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

public class ProfileContainerCustomView extends RelativeLayout {
    private Boolean mDisableComponents;

    protected Context context;

    protected final String LOG_TAG = this.getClass().getSimpleName();
    //    DATAs
    private ArrayAdapter<String> adapterProv;
    private ArrayAdapter<String> adapterComun;
    private boolean isEditMode;
    private UserProfile userProfile;

//    LISTENERs
    private IOnProfileContainerDataModifiedViewListener listenerOnModified;

    //    Views
    private View authProgressView;
    private EditText emailView;
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

    public ProfileContainerCustomView(Context context) {
        super(context);
        init(context,null, 0);
    }

    public ProfileContainerCustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs, 0);
    }

    public ProfileContainerCustomView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context,attrs, defStyle);
    }

    private void init(final Context context,AttributeSet attrs, int defStyle) {
        if (isInEditMode()) {
            return;
        }
        this.context=context;
        inflate(context,R.layout.form_profile,this);

        this.authProgressView = this.findViewById(R.id.authProgress);
        this.emailView = (EditText) this.findViewById(R.id.email);
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
// Create the adapter and set it to the AutoCompleteTextView
        adapterProv = new ArrayAdapter<String>(this.context, android.R.layout.simple_list_item_1, new ArrayList<String>(Arrays.asList(new String[]{"none"})));
        adapterComun = new ArrayAdapter<String>(this.context, android.R.layout.simple_list_item_1, new ArrayList<String>(Collections.EMPTY_LIST));
        provinciaView.setAdapter(adapterProv);
        comuneView.setAdapter(adapterComun);
        retrieveXMLAttributes(context,attrs,defStyle);

//        Initialize
        provinciaView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                resFlag = NOTHING_LOADED;
                Comuni comuni = new Comuni();
                comuni.setComuni(Collections.EMPTY_LIST);
                adapterComun.clear();
                adapterComun.addAll(comuni.getComuni());
                adapterComun.notifyDataSetChanged();
            }
        });
        provinciaView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Qui bisogna chiamare il db per la lista dei comuni in base alla provincia e poi abilitare il comuneView
                String selectedProvincia = parent.getAdapter().getItem(position).toString();
                Log.d(LOG_TAG,"Provincia selected:"+selectedProvincia);
                resFlag = resFlag|flagProvincia;
//                ProvinciaDAO dao = new ProvinciaDAO();
//                dao.loadComuniByProvincia(selectedProvincia, new OnDAOExecutionListener() {
//                    @Override
//                    public void onSuccessDAOExecuted(IEntity entity) {
//                        comuneView.requestFocus();
//                        resFlag = resFlag|flagProvincia;
//                        loadProvinceOrComuni(entity);
//                    }
//
//                    @Override
//                    public void onErrorDAOExecuted(ErrorEntity errorEntity) {
//                        Log.e(LOG_TAG,"errorEntity say:"+errorEntity.getDatabaseError());
//                    }
//                });

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
                if(adapterComun.isEmpty() && resFlag == flagProvincia){
                    ProvinciaDAO dao = new ProvinciaDAO();
                    dao.loadComuniByProvincia(provinciaView.getText().toString(), new OnDAOExecutionListener() {
                        @Override
                        public void onSuccessDAOExecuted(IEntity entity) {
                            comuneView.requestFocus();
                            resFlag = resFlag|flagProvincia;
                            loadProvinceOrComuni(entity);
                        }
                        @Override
                        public void onErrorDAOExecuted(ErrorEntity errorEntity) {
                            Log.e(LOG_TAG,"errorEntity say:"+errorEntity.getDatabaseError());
                        }
                    });
                }
            }
        });
        provinciaView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(final TextView v, int actionId, KeyEvent event) {
                if (actionId == R.id.autocompleteProvincia || actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_NULL) {
                    View vw = v.focusSearch(FOCUS_DOWN);
                    if (vw != null) {
                        if(resFlag!=ALL_LOADED){
                            if(resFlag!=flagProvincia){
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
                                }
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

    public void loadProvinceOrComuni(IEntity dataToLoad) {
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

    public void retrieveXMLAttributes(Context context, AttributeSet attrs,int defStyle) {
        boolean mDisableComponents = true;
        // Load attributes
        if(attrs!=null){
            final TypedArray a = getContext().obtainStyledAttributes(
                    attrs, R.styleable.ProfileContainerCustomView, defStyle, 0);
            if(a!=null){
                mDisableComponents = a.getBoolean(R.styleable.ProfileContainerCustomView_disabledComponents,mDisableComponents);
                a.recycle();
            }
        }
        setDisableComponents(mDisableComponents);
    }

    /** enable / disable the edit mode */
    public void setEditMode(boolean isEditMode) {
        this.isEditMode = isEditMode;
        setDisableComponents(!this.isEditMode);
//        if confirm
        Log.d(LOG_TAG,"####>this.isEditMode = "+this.isEditMode);
        if(!this.isEditMode){
            if(this.listenerOnModified!=null){
                Log.e(LOG_TAG,"listenerOnModified:"+listenerOnModified);
                if(validateFieldsForm()){
                    this.listenerOnModified.onSave(this.userProfile);
                }else{
                    this.listenerOnModified.onValidationError(this.userProfile);
                }
            }
        }
    }
    public boolean getDisableComponents() {
        Log.d(LOG_TAG,"[getDisableComponents]");
        return this.mDisableComponents;
    }

    public void setDisableComponents(boolean _disable) {
        Log.d(LOG_TAG,"[setDisableComponents]");
        this.mDisableComponents = _disable;
        this.emailView.setEnabled(false);
        this.provinciaView.setEnabled(!this.mDisableComponents);
        this.comuneView.setEnabled(!this.mDisableComponents);
        this.nomeAliasView.setEnabled(!this.mDisableComponents);
        this.othersView.setEnabled(!this.mDisableComponents);
        this.birthdateView.setEnabled(!this.mDisableComponents);
        for(AppCompatCheckBox curr : checkBoxArrayViews){
            curr.setEnabled(!this.mDisableComponents);
        }
    }

    public void load(UserProfile _userProfile){
        this.userProfile = _userProfile;
        this.emailView.setText(_userProfile.getProfile().getContactEmail());
        this.provinciaView.setText(_userProfile.getProfile().getProvincia());
        this.comuneView.setText(_userProfile.getProfile().getComune());
        this.nomeAliasView.setText(_userProfile.getProfile().getName());
        this.othersView.setText(_userProfile.getProfile().getOthers());
        this.birthdateView.setText(_userProfile.getProfile().getBirthDate());
        Map<String,Boolean> prefFunghi = _userProfile.getProfile().getPrefFunghi();
        for(AppCompatCheckBox curr : checkBoxArrayViews){
            boolean checked = prefFunghi.get(curr.getText().toString());
            curr.setChecked(checked);
        }
        resFlag = ALL_LOADED;
    }

    public void showProgress(boolean show){
        this.authProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    protected void resetErrorsFieldsForm() {
        this.provinciaView.setError(null);
        this.comuneView.setError(null);
        this.nomeAliasView.setError(null);
        this.othersView.setError(null);
        this.birthdateView.setError(null);
    }

    protected boolean validateFieldsForm() {
        // Reset errors.
        this.resetErrorsFieldsForm();

        // Store values at the time of the login attempt.
        String provincia = this.provinciaView.getText().toString();
        String comune = this.comuneView.getText().toString();
        String nomeAlias = this.nomeAliasView.getText().toString();
        String others = this.othersView.getText().toString();
        String birthDate = this.birthdateView.getText().toString();

        boolean cancel = false;
        View focusView = null;


        if (TextUtils.isEmpty(nomeAlias)) {
            this.nomeAliasView.setError(this.context.getString(R.string.error_nomealias_field_required));
            focusView = this.nomeAliasView;
            cancel = true;
        }

        Pattern pattern = Pattern.compile("19\\d{2}|20\\d{2}");
        Matcher matcher = pattern.matcher(birthDate);
        boolean result = matcher.matches();

        if (TextUtils.isEmpty(birthDate) || !result) {
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
                this.comuneView.setError(this.context.getString(R.string.error_comune_field_required));
                focusView = this.provinciaView;
            }
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        }else{
//            prepare the userprofile object
            userProfile.getProfile().setOthers(others);
            Map<String,Boolean> prefFunghi = new HashMap<>();
            for(AppCompatCheckBox currCheck : checkBoxArrayViews){
                prefFunghi.put(currCheck.getText().toString(),currCheck.isChecked());
            }
            userProfile.getProfile().setPrefFunghi(prefFunghi);
//            optional fileds
            userProfile.getProfile().setName(nomeAlias);
            userProfile.getProfile().setBirthDate(birthDate);
            userProfile.getProfile().setProvincia(provincia);
            userProfile.getProfile().setComune(comune);
        }
        return !cancel;
    }

    private boolean isProvinciaAndComuneValid(){
        return (resFlag==ALL_LOADED);
    }

    public IOnProfileContainerDataModifiedViewListener getListenerOnModified() {
        return listenerOnModified;
    }

    public void setListenerOnModified(IOnProfileContainerDataModifiedViewListener listenerOnModified) {
        this.listenerOnModified = listenerOnModified;
    }

    public interface IOnProfileContainerDataModifiedViewListener{
        public void onSave(UserProfile _userProfile);
        public void onValidationError(UserProfile _userProfile);
    }
}
