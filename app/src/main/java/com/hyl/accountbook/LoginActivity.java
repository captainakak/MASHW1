package com.hyl.accountbook;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hyl.util.pubFun;
import com.hyl.dao.DBOpenHelper;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

/**
 * @programName: LoginActivity.java
 * @programFunction: the login page
 * @createDate: 2018/09/19
 * @author: AnneHan
 * @version:
 * xx.   yyyy/mm/dd   ver    author    comments
 * 01.   2018/09/19   1.00   AnneHan   New Create
 */

public class LoginActivity extends AppCompatActivity {

    private EditText editPhone;
    private EditText editPwd;
    private Button btnLogin;
    private FirebaseAuth mAuth;

    protected void onCreate(Bundle savedInstanceState){
        FirebaseApp.initializeApp(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        editPhone = (EditText) findViewById(R.id.editPhone);
        editPwd = (EditText) findViewById(R.id.editPwd);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        // ...
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();

    }

    /**
     * login event
     * @param v
     */

    public void OnMyLoginClick(View v) {
        if (pubFun.isEmpty(editPhone.getText().toString()) || pubFun.isEmpty(editPwd.getText().toString())) {
            Toast.makeText(this, "手机号或密码不能为空！", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(editPhone.getText().toString(), editPwd.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("login", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
//                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                            // ...
                        }

                        // ...
                    }
                });

    }



    /**
     * Jump to the registration interface
     * @param v
     */
    public void OnMyRegistClick(View v)  {
        Intent intent=new Intent(LoginActivity.this,RegistActivity.class);
        //intent.putExtra("info", "No66778899");
        LoginActivity.this.startActivity(intent);
    }

    /**
     * Jump to reset password interface
     * @param v
     */
    public void OnMyResPwdClick(View v){
        Intent intent=new Intent(LoginActivity.this,ResPwdActivity.class);
        LoginActivity.this.startActivity(intent);
    }
}