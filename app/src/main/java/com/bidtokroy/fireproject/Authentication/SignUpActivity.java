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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    private Button signUpButton;
    private EditText emailEdiTtext2,passwordEditText2;
    private TextView alert;
    private static final String TAG = "TAG" ;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private DatabaseReference myDatabese;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        signUpButton = (Button) findViewById(R.id.signup_button);
        emailEdiTtext2 = (EditText) findViewById(R.id.signup_email_edittext);
        passwordEditText2 = (EditText) findViewById(R.id.signup_password_edittext);

        alert = (TextView) findViewById(R.id.signup_alert_textview);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        myDatabese = FirebaseDatabase.getInstance().getReference();
        /*if(mAuth.getCurrentUser() != null){
            startActivity(new Intent(SignUpActivity.this,ProfileActivity.class));
            finish();
        }*/

        signUpButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        signUpButton.setAlpha((float)0.5);
                        break;
                    case MotionEvent.ACTION_UP:
                        validateandCreateAccount(emailEdiTtext2.getText().toString().trim(),passwordEditText2.getText().toString().trim());
                        break;
                    case MotionEvent.ACTION_MOVE:

                        break;
                }
                return false;
            }
        });

    }

    private void validateandCreateAccount(String emails,String passs) {
        Log.d(TAG,emails +"\n"+passs);
        if(emails.isEmpty() && passs.isEmpty()){
            Log.d(TAG,"Email & Password Field Should not be Empty!!\n");
            alert.setText("*"+"Email & Password Field Should not be Empty!!");
        }else if(emails.isEmpty()){
            Log.d(TAG,"Email Field Should not be Empty!!\n");
            alert.setText("*"+"Email Field Should not be Empty!!");
        }else if(passs.isEmpty()){
            Log.d(TAG,"Password Field Should not be Empty!!\n");
            alert.setText("*"+"Password Field Should not be Empty!!");
        } else{
            // Log.d(TAG,email +"\n"+pass);
            if(passs.length()<6){
                alert.setText("*"+"Password length should be at least 6!!");
            }else{
                createAccount(emails,passs);
            }
        }
    }

    private void createAccount(String Useremail, String password) {
        progressDialog.setMessage("Progressing.......");
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(Useremail, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(SignUpActivity.this, "Signup failed.",Toast.LENGTH_SHORT).show();
                            Log.d(TAG,"Exception : "+task.getException().getMessage());
                            progressDialog.dismiss();

                        }else{
                            FirebaseUser user = mAuth.getCurrentUser();
                            String UserEmail = user.getEmail();

                            String UserName="";
                            for(int i = 0;i<UserEmail.length();i++){
                                if(UserEmail.charAt(i)=='@')
                                    break;
                                UserName+=UserEmail.charAt(i);
                            }
                            //myDatabese.child("USERS").child(UserName).setValue(nicknameEditText.getText().toString().trim());
                            //myDatabese.child("REPORT_TABLE").child(UserName).setValue("NULL");
                            //myDatabese.child("ADMIN_MESSAGES").child("USER_MESSAGES").child(UserName).setValue("NULL");
                            startActivity(new Intent(SignUpActivity.this,MainActivity.class));
                            finish();
                        }

                    }
                });
    }
}
