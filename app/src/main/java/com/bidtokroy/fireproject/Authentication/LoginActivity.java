package com.bidtokroy.fireproject.Authentication;


import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bidtokroy.fireproject.MainActivity;
import com.bidtokroy.fireproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private Button loginButton,createAccountButton;
    private EditText emailEdiTtext,passwordEditText;
    private TextView alert;
    private static final String TAG = "TAG" ;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //-------------------------------Initialize UI element-----------------------------------
        loginButton = (Button) findViewById(R.id.login_button);
        createAccountButton = (Button) findViewById(R.id.login_create_account_button);
        emailEdiTtext = (EditText) findViewById(R.id.login_email_edittext);
        passwordEditText = (EditText) findViewById(R.id.login_password_edittext);
        alert = (TextView) findViewById(R.id.login_alert_textview);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();

        //---------------------------------------------------------------------------------------

        if(mAuth.getCurrentUser() != null) {
            mAuth.signOut();
        }


        loginButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        loginButton.setAlpha((float)0.5);
                        break;
                    case MotionEvent.ACTION_UP:
                        validateandLogin(emailEdiTtext.getText().toString().trim(),passwordEditText.getText().toString().trim());
                        break;
                    case MotionEvent.ACTION_MOVE:

                        break;
                }
                return false;
            }
        });

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,SignUpActivity.class));
            }
        });
    }

    private void validateandLogin(String email,String pass) {
        Log.d(TAG,email +"\n"+pass);
        if(email.isEmpty() && pass.isEmpty()){
            Log.d(TAG,"Email & Password Field Should not be Empty!!\n");
            alert.setText("*"+"Email & Password Field Should not be Empty!!");
        }else if(email.isEmpty()){
            Log.d(TAG,"Email Field Should not be Empty!!\n");
            alert.setText("*"+"Email Field Should not be Empty!!");
        }else if(pass.isEmpty()){
            Log.d(TAG,"Password Field Should not be Empty!!\n");
            alert.setText("*"+"Password Field Should not be Empty!!");
        }else{
            // Log.d(TAG,email +"\n"+pass);
            if(pass.length()<6){
                alert.setText("*"+"Password length should be at least 6!!");
            }else{
                login(email,pass);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        progressDialog.dismiss();
    }

    private void login(String email, String pass) {
        progressDialog.setMessage("Progressing.......");
        progressDialog.show();
        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Login failed.",Toast.LENGTH_SHORT).show();
                            Log.d(TAG,"Exception : "+task.getException().getMessage());
                            progressDialog.dismiss();

                        }else{
                            finish();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));

                        }
                    }
                });
    }
}

