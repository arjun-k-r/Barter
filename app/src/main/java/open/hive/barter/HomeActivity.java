package open.hive.barter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class HomeActivity extends AppCompatActivity  implements View.OnClickListener{

    private FirebaseAuth firebaseAuth;

    private TextView textViewUserEmail;

    private Button buttonLogout;

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

        textViewUserEmail.setText("Welcome "+user.getEmail());
        buttonLogout.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v == buttonLogout){
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_notification){
            //open notifications activity
        }else if (item.getItemId() == R.id.action_list){
            //open list activity
            startActivity(new Intent(this, ItemsUploadedActivity.class));

        }else if (item.getItemId() == R.id.action_settings){
            //open settings activity
            startActivity(new Intent(this, SettingsActivity.class));

        }else if (item.getItemId() == R.id.action_logout){
            //logout
            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Are you sure?")
                    .setContentText("You'll be logged out!")
                    .setConfirmText("Yes,log out!")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                            firebaseAuth.signOut();
                            finish();
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        }
                    })
                    .show();

        }
        return super.onOptionsItemSelected(item);
    }
}
