package open.hive.barter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnSignIn;
    private EditText emailLogin;
    private EditText passLogin;
    private TextView tvSignUp;

    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null){
            //Home activity
            finish();
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
        }

        btnSignIn = findViewById(R.id.btnLogin);
        emailLogin = findViewById(R.id.emailLoginText);
        passLogin = findViewById(R.id.passLoginText);
        tvSignUp = findViewById(R.id.regLink);

        btnSignIn.setOnClickListener(this);
        tvSignUp.setOnClickListener(this);

    }

    private void userLogin(){
        String email = emailLogin.getText().toString().trim();
        String pass = passLogin.getText().toString().trim();

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

        Toast.makeText(this, "signing in", Toast.LENGTH_LONG).show();

        firebaseAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            //user successfully registered
                            Toast.makeText(LoginActivity.this, "signed in successfully", Toast.LENGTH_LONG).show();
                            //open Home activity
                            finish();
                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                        }
                        else {
                            Toast.makeText(LoginActivity.this, "Could not sign in. Please try again", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {

        if (v == btnSignIn){
            //login
            userLogin();
        }

        if (v == tvSignUp){
            startActivity(new Intent(this, MainActivity.class));
        }

    }
}
