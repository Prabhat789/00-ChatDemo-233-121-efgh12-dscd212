package com.pktworld.chatdemo.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.pktworld.chatdemo.R;
import com.pktworld.chatdemo.util.ApplicationConstant;
import com.pktworld.chatdemo.util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ubuntu1 on 10/3/16.
 */
public class LoginActivity extends Activity implements View.OnClickListener {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private ProgressDialog mProgressDialog;
    private EditText editUserName, editPassword;
    private Button btnLogin, btnRegister;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean saveLogin;
    private CheckBox saveLoginCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        btnLogin = (Button)findViewById(R.id.btnLogin);
        btnRegister = (Button)findViewById(R.id.btnRegister);
        editUserName = (EditText)findViewById(R.id.editUsername);
        editPassword = (EditText)findViewById(R.id.editPassword);
        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);


        saveLoginCheckBox = (CheckBox) findViewById(R.id.checkBox);
        loginPreferences = getSharedPreferences(ApplicationConstant.LOGIN_PREFERENCE, MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        saveLogin = loginPreferences.getBoolean(ApplicationConstant.SAVE_LOGIN, false);

        if (saveLogin == true) {
            editUserName.setText(loginPreferences.getString(ApplicationConstant.USERNAME, ""));
            editPassword.setText(loginPreferences.getString(ApplicationConstant.PASSWORD, ""));
            saveLoginCheckBox.setChecked(true);
        }

        setupUI(findViewById(R.id.LoginContainer));
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


    }
    public void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager)  getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

    public void setupUI(View view) {
        if(!(view instanceof EditText)) {

            view.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                    editUserName.clearFocus();
                    editPassword.clearFocus();
                    hideSoftKeyboard();
                    return false;
                }

            });
        }

        if (view instanceof ViewGroup) {

            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v == btnLogin){
            if (Utils.isConnected(LoginActivity.this)){
                submitForm();
            }
        }else if (v == btnRegister){
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
          //  Utils.showToastMessage(LoginActivity.this,"Work in progress..");
        }
    }

    private void submitForm() {
        if (!validateUsername()) {
            return;
        }
        if (!validatePassword()) {
            return;
        }

        if (Utils.isConnected(LoginActivity.this)){
            if (saveLoginCheckBox.isChecked()) {

                loginPrefsEditor.putBoolean(ApplicationConstant.SAVE_LOGIN, true);
                loginPrefsEditor.putString(ApplicationConstant.USERNAME, editUserName.getText().toString());
                loginPrefsEditor.putString(ApplicationConstant.PASSWORD, editPassword.getText().toString());
                loginPrefsEditor.commit();

            } else {
                loginPrefsEditor.clear();
                loginPrefsEditor.commit();
            }
            login(editUserName.getText().toString(), editPassword.getText().toString());
        }

    }

    private void login(String username, String password) {

        mProgressDialog = ProgressDialog.show(this, "", getResources().getString(R.string.loading), true);

        ParseUser.logInInBackground(username, password, new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                List<String> list = new ArrayList<String>();
                mProgressDialog.dismiss();
                if (user != null) {
                    Intent intent = new Intent(LoginActivity.this, GroupListActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    Utils.showToastMessage(LoginActivity.this, getResources().getString(R.string.user_not_found));
                }
            }
        });
    }

    private boolean validateUsername(){
        String username = editUserName.getText().toString().trim();
        if (username.isEmpty()) {
            Utils.showToastMessage(this,"Enter valid User Name");
            requestFocus(editUserName);
            return false;
        } else {
            //inputLayoutUserName.setErrorEnabled(false);
        }

        return  true;
    }

    private boolean validatePassword(){
        String username = editPassword.getText().toString().trim();
        if (username.isEmpty()) {
            Utils.showToastMessage(this,"Enter valid Password");
            requestFocus(editPassword);
            return false;
        } else {
            // inputLayoutPassword.setErrorEnabled(false);
        }

        return  true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
}
