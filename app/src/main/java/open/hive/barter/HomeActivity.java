package open.hive.barter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity  implements View.OnClickListener{

    private FirebaseAuth firebaseAuth;

    private TextView textViewUserEmail;

    private Button buttonLogout, btnUploadAct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        FirebaseUser user = firebaseAuth.getCurrentUser();

        textViewUserEmail = findViewById(R.id.textViewUserEmail);
        buttonLogout = findViewById(R.id.buttonLogout);
        btnUploadAct = findViewById(R.id.btnUploadAct);

        textViewUserEmail.setText("Welcome "+user.getEmail());
        buttonLogout.setOnClickListener(this);
        btnUploadAct.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v == buttonLogout){
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }else if (v == btnUploadAct){
            startActivity(new Intent(this, ItemsUploadedActivity.class));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
