package open.hive.barter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class UploadActivity extends AppCompatActivity implements View.OnClickListener{

    private static final int PICK_IMAGE_REQUEST = 24;

    private ImageView imgSelector;
    private EditText etTitle, etDesc;
    private Button btnUpload;

    private FirebaseAuth firebaseAuth;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;

    private Uri filepath;

    private SweetAlertDialog sweetAlertDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        storageReference = FirebaseStorage.getInstance().getReference();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("posts");

        imgSelector = findViewById(R.id.imgSelector);
        etTitle = findViewById(R.id.etTitle);
        etDesc = findViewById(R.id.etDesc);
        btnUpload = findViewById(R.id.btnUpload);

        imgSelector.setOnClickListener(this);
        btnUpload.setOnClickListener(this);
    }

    private void showFileSelector(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select an image"), PICK_IMAGE_REQUEST);
    }

    private void uploadPost(){

        String title = etTitle.getText().toString().trim();
        String desc = etDesc.getText().toString().trim();

        if (TextUtils.isEmpty(title)){
            //check if email field is empty
            Toast.makeText(this, "please add a title", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(desc)){
            //check if password field is empty
            Toast.makeText(this, "please add a description", Toast.LENGTH_LONG).show();
            return;
        }

        if (filepath != null){

            sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
            sweetAlertDialog.setTitle("Uploading");

            StorageReference riversRef = storageReference.child("images/"+filepath.getLastPathSegment());

            // Register observers to listen for when the download is done or if it fails
            riversRef.putFile(filepath).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                    sweetAlertDialog.dismiss();
                    Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Uri downloadUrl = taskSnapshot.getDownloadUrl();

                    DatabaseReference newPost = databaseReference.push();
                    newPost.child("title").setValue(etTitle.getText().toString());
                    newPost.child("desc").setValue(etDesc.getText().toString());
                    newPost.child("image").setValue(downloadUrl.toString());

                    // Handle successful uploads
                    sweetAlertDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Upload successful", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(), ItemsUploadedActivity.class));

                }
            })
            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                    sweetAlertDialog.setTitleText(((int)progress)+"% uploaded ...");
                    sweetAlertDialog.show();
                }
            });
        }else {
            //display error toast
            Toast.makeText(this, "No image selected", Toast.LENGTH_LONG).show();
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            filepath = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath);
                imgSelector.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v == imgSelector){
            //select image from phone storage
            showFileSelector();
        }else if (v == btnUpload){
            //upload image to cloud storage
            uploadPost();
        }
    }
}
