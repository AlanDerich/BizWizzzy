package com.example.derich.bizwiz.credentials;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.example.derich.bizwiz.PreferenceHelper;
import com.example.derich.bizwiz.R;
import com.example.derich.bizwiz.activities.NetworkStateChecker;
import com.example.derich.bizwiz.activities.UserActivity;
import com.example.derich.bizwiz.helper.InputValidation;
import com.example.derich.bizwiz.sql.DatabaseHelper;
import com.example.derich.bizwiz.utils.PreferenceUtils;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    public static SharedPreferences sharedPreferences;
    private final AppCompatActivity activity = LoginActivity.this;

    private NestedScrollView nestedScrollView;

    private TextInputLayout textInputLayoutUsername;
    private TextInputLayout textInputLayoutPassword;

    private TextInputEditText textInputEditTextUsername;
    private TextInputEditText textInputEditTextPassword;

    private AppCompatButton appCompatButtonLogin;

    private AppCompatTextView textViewLinkForgotPassword;
    private AppCompatTextView textViewLinkSyncUsers;

     private InputValidation inputValidation;
    private DatabaseHelper databaseHelper;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String Name = "nameKey" ;
    public static final String Email = "emailKey";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sharedPreferences = getSharedPreferences(getPackageName() + "_preferences", MODE_PRIVATE);
        registerReceiver(new NetworkStateChecker(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        getSupportActionBar().hide();

        initViews();
        initListeners();
        initObjects();
    }
    private void initViews(){
        nestedScrollView = findViewById(R.id.nestedScrollView);

        textInputLayoutUsername =  findViewById(R.id.textInputLayoutUsername);
        textInputLayoutPassword =  findViewById(R.id.textInputLayoutPassword);

        textInputEditTextUsername =  findViewById(R.id.textInputEditTextUsername);
        textInputEditTextPassword =  findViewById(R.id.textInputEditTextPassword);

        appCompatButtonLogin =  findViewById(R.id.appCompatButtonLogin);


        textViewLinkForgotPassword =  findViewById(R.id.forgotPassword);
        textViewLinkSyncUsers =  findViewById(R.id.syncUsers);
        PreferenceUtils utils = new PreferenceUtils();

        if (utils.getEmail(this) != null ){
            Intent intent = new Intent(LoginActivity.this, UserActivity.class);
            startActivity(intent);
        }else{

        }
    }

    private void initListeners(){
        appCompatButtonLogin.setOnClickListener(this);
        textViewLinkForgotPassword.setOnClickListener(this);
        textViewLinkSyncUsers.setOnClickListener(this);
    }

    private void initObjects(){
        databaseHelper = new DatabaseHelper(activity);
        inputValidation = new InputValidation(activity);
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.appCompatButtonLogin:
                verifyFromSQLite();
                break;
            case R.id.syncUsers:
                Intent intent = new Intent(getApplicationContext(), UserSyncronization.class);
                startActivity(intent);
                break;
            case R.id.forgotPassword:
                Intent intentPassword = new Intent(getApplicationContext(), ForgotPassword.class);
                startActivity(intentPassword);
                break;
        }
    }

    private void verifyFromSQLite(){
        if (!inputValidation.isInputEditTextFilled(textInputEditTextUsername, textInputLayoutUsername, getString(R.string.error_message_email))) {
            return;
        }

        if (!inputValidation.isInputEditTextFilled(textInputEditTextPassword, textInputLayoutPassword, getString(R.string.error_message_email))) {
            return;
        }
        String email = textInputEditTextUsername.getText().toString().trim();
        String password = textInputEditTextPassword.getText().toString().trim();

        if (databaseHelper.checkUser(email, password)) {
            PreferenceUtils.saveEmail(email, this);
            PreferenceUtils.savePassword(password, this);
            PreferenceHelper.setUsername(email);
            Intent accountsIntent = new Intent(activity, UserActivity.class);
            accountsIntent.putExtra("EMAIL", textInputEditTextUsername.getText().toString().trim());
            sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(Name, email);
            editor.apply();
            emptyInputEditText();
            startActivity(accountsIntent);
            finish();
        } else {
            Snackbar.make(nestedScrollView, getString(R.string.error_valid_email_password), Snackbar.LENGTH_LONG).show();
        }
    }

    private void emptyInputEditText(){
        textInputEditTextUsername.setText(null);
        textInputEditTextPassword.setText(null);
    }
}