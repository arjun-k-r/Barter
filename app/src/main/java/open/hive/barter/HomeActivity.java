package open.hive.barter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import open.hive.barter.classes.Barter;

public class HomeActivity extends AppCompatActivity  implements View.OnClickListener{

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    private RecyclerView itemFeed;
    private FirebaseRecyclerAdapter<Barter, BarterViewHolder> firebaseRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("posts");

        if (firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        FirebaseUser user = firebaseAuth.getCurrentUser();

        itemFeed = findViewById(R.id.itemFeed);
        itemFeed.setHasFixedSize(true);
        itemFeed.setLayoutManager(new LinearLayoutManager(this));
        Query databaseQuery = databaseReference.orderByKey();
        FirebaseRecyclerOptions databaseOptions = new FirebaseRecyclerOptions.Builder<Barter>().setQuery(databaseQuery, Barter.class).build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Barter, BarterViewHolder>(databaseOptions) {
            @Override
            protected void onBindViewHolder(@NonNull BarterViewHolder holder, int position, @NonNull Barter model) {

                holder.setTitle(model.getTitle());
                holder.setDesc(model.getDesc());
                holder.setImage(getApplication(), model.getImage());

            }

            @Override
            public BarterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_feed, parent, false);
                return new BarterViewHolder(view);
            }

            @Override
            public int getItemCount() {
                return super.getItemCount();
            }
        };
        itemFeed.setAdapter(firebaseRecyclerAdapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseRecyclerAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseRecyclerAdapter.stopListening();
    }

    public static class BarterViewHolder extends RecyclerView.ViewHolder{

        View item_view;

        public BarterViewHolder(View itemView) {
            super(itemView);

            item_view = itemView;
        }

        public void setTitle(String title){

            TextView item_title = item_view.findViewById(R.id.itemTitle);
            item_title.setText(title);
        }

        public void setDesc(String desc){

            TextView item_desc = item_view.findViewById(R.id.itemDesc);
            item_desc.setText(desc);
        }

        public void setImage(Context ctx, String image){
            ImageView item_img = item_view.findViewById(R.id.itemImg);
            Picasso.with(ctx).load(image).into(item_img);
        }
    }

    @Override
    public void onClick(View v) {

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
