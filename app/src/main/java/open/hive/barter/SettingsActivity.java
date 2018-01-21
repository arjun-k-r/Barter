package open.hive.barter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btnHome;
    private FirebaseAuth firebaseAuth;

    private EditText etName, etAddress;
    private Button btnSaveSettings;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth == null){
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        databaseReference = FirebaseDatabase.getInstance().getReference();


        btnHome = findViewById(R.id.btnHome);
        etName = findViewById(R.id.etName);
        etAddress = findViewById(R.id.etAddress);
        btnSaveSettings = findViewById(R.id.btnSaveSettings);

        btnHome.setOnClickListener(this);
        btnSaveSettings.setOnClickListener(this);
    }

    private void saveUserInfo(){
        String name = etName.getText().toString().trim();
        String add = etAddress.getText().toString().trim();

        UserInfor userInfor = new UserInfor(name, add);

        FirebaseUser user = firebaseAuth.getCurrentUser();

        databaseReference.child(user.getUid()).setValue(userInfor);

        Toast.makeText(this, "Information saved", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {
        if (v == btnHome){
            finish();
            startActivity(new Intent(this, HomeActivity.class));
        }

        if (v == btnSaveSettings){
            saveUserInfo();
        }
    }
}
