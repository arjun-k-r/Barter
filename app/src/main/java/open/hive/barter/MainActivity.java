package open.hive.barter;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btnReg;
    private EditText emailText, passText;
    private TextView tvLink;

    private ProgressBar progressBar;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null){
            //Home activity
            finish();
            startActivity(new Intent(MainActivity.this, HomeActivity.class));
        }

        progressBar = new ProgressBar(this,null,android.R.attr.progressBarStyleLarge);
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100,100);
//        params.addRule(RelativeLayout.CENTER_IN_PARENT);
//        layout.addView

        btnReg = findViewById(R.id.btnReg);
        emailText = findViewById(R.id.emailText);
        passText = findViewById(R.id.passText);
        tvLink = findViewById(R.id.loginLink);

        btnReg.setOnClickListener(this);
        tvLink.setOnClickListener(this);
    }

    private void registerUser(){
        String email = emailText.getText().toString().trim();
        String pass = passText.getText().toString().trim();

        if (TextUtils.isEmpty(email)){
            //check if email field is empty
            Toast.makeText(this, "please enter your email", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(pass)){
            //check if password field is empty
            Toast.makeText(this, "please enter your password", Toast.LENGTH_LONG).show();
            return;
        }

        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Registering");
        pDialog.setCancelable(false);
        pDialog.show();
//        progressBar.setVisibility(View.VISIBLE);
        firebaseAuth.createUserWithEmailAndPassword(email,pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            //user successfully registered
                            pDialog.dismiss();
                            Toast.makeText(MainActivity.this, "registered successfully", Toast.LENGTH_LONG).show();
                            //open settings activity
                            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                        }
                        else {
                            Toast.makeText(MainActivity.this, "Could not register. Please try again", Toast.LENGTH_LONG).show();
                        }
//                        progressBar.setVisibility(View.GONE);
                    }
                });
    }

    @Override
    public void onClick(View v) {
        if(v == btnReg){
            registerUser();
        }

        if (v == tvLink){
            //open login activity
            startActivity(new Intent(this, LoginActivity.class));
        }
    }
}
