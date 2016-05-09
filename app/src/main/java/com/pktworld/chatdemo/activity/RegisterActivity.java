package com.pktworld.chatdemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.pktworld.chatdemo.R;
import com.pktworld.chatdemo.util.Utils;

/**
 * Created by ubuntu1 on 4/5/16.
 */
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = RegisterActivity.class.getSimpleName();
    private Button btnSignUp;
    private EditText editName, editEmail, editPassword, editConfPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        btnSignUp = (Button)findViewById(R.id.btnSignUp);
        editName = (EditText)findViewById(R.id.editName);
        editEmail = (EditText)findViewById(R.id.editEmail);
        editPassword = (EditText)findViewById(R.id.editPassword);
        editConfPassword = (EditText)findViewById(R.id.editConfPassword);

        btnSignUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btnSignUp){

            if (validate()){
                if (Utils.isConnected(RegisterActivity.this)){
                    register();
                }
            }
        }
    }


    private void register(){
        ParseUser user = new ParseUser();
        user.setUsername(editEmail.getText().toString().trim());
        user.setPassword(editPassword.getText().toString().trim());
        user.setEmail(editEmail.getText().toString().trim());
        user.put("name",editName.getText().toString().trim());
        user.put("profileImage", "http://newprabhat.bugs3.com/images/add_picture_movikit.png");

        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    Utils.showToastMessage(RegisterActivity.this,getString(R.string.unable_to_process));
                }
            }
        });
    }


    private boolean validate(){
        if (editName.getText().toString().trim().length() == 0 || editName.getText().toString().trim().isEmpty()){
            Utils.showToastMessage(RegisterActivity.this,"Please enter Name");
            return false;
        }else if (editEmail.getText().toString().trim().length() == 0 || editEmail.getText().toString().trim().isEmpty()){
            Utils.showToastMessage(RegisterActivity.this,"Please enter Email");
            return false;
        }else if (editPassword.getText().toString().trim().length() == 0 || editPassword.getText().toString().trim().isEmpty()){
            Utils.showToastMessage(RegisterActivity.this,"Please enter password");
            return false;
        }else if (editConfPassword.getText().toString().trim().length() == 0 || editConfPassword.getText().toString().trim().isEmpty()){
            Utils.showToastMessage(RegisterActivity.this,"Please re enter password");
            return false;
        }else if (!editConfPassword.getText().toString().equals(editPassword.getText().toString())){
            Utils.showToastMessage(RegisterActivity.this,"Password should be same");
            return false;
        }else {
            return true;
        }
    }
}
