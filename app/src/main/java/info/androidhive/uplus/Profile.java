package info.androidhive.uplus;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import id.zelory.compressor.Compressor;
import info.androidhive.uplus.activity.HomeActivity;
import info.androidhive.uplus.activity.SharedPrefManager;
import info.androidhive.uplus.activity.UpdateProfile;

public class Profile extends AppCompatActivity {
    private static final int GALLERY_REQUEST=1;
    EditText editText;
    Button btnUpdate;
    String userId,userName;
    ImageButton fabbtnUpload;
    ImageView imageView;
    ProgressDialog progress;
    String uploadedUserImage="";
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    Bitmap thumb_bitmap = null;
    private StorageReference thumbImageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        //get data from intent extras
        Intent intent=getIntent();
        userId=intent.getStringExtra("userId");
        String userName=intent.getStringExtra("userName");
        String finalData="userId:"+userId+"\n"+"userName:"+userName;
        //Toast.makeText(getApplicationContext(),finalData,Toast.LENGTH_LONG).show();
        editText=(EditText)findViewById(R.id.txtProfile);
        editText.setText(userName);
        firebaseStorage=FirebaseStorage.getInstance();
        //storageReference=firebaseStorage.getReference().child("users_photos");
        thumbImageRef = FirebaseStorage.getInstance().getReference().child("users_photos");

        btnUpdate=(Button)findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateProfile();
            }
        });
        imageView=(ImageView)findViewById(R.id.imgProfile);
        fabbtnUpload=(ImageButton)findViewById(R.id.fabbtnUpload);
        fabbtnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,GALLERY_REQUEST);
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 100 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)){
            Intent intent=getIntent();
            finish();
            startActivity(intent);
        }else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},100);
            }
        }
    }
    //END OF IMPLEMEMNTED CODES

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GALLERY_REQUEST&&resultCode==RESULT_OK)
        {
            Uri imageUri=data.getData();
            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1 ,1)
                    .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                final Uri mImageUri = result.getUri();

                File thugmb_filePathUri = new File(mImageUri.getPath());

                try{
                    thumb_bitmap = new Compressor(this)
                            .setMaxWidth(200)
                            .setMaxHeight(200)
                            .setQuality(50)
                            .compressToBitmap(thugmb_filePathUri);

                }catch (IOException e){
                    e.printStackTrace();
                }

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
                final byte[] thumb_byte = byteArrayOutputStream.toByteArray();


                progress = new ProgressDialog(Profile.this);
                progress.setMessage("Uploading...");
                progress.show();


                final StorageReference thumb_filePath = thumbImageRef.child(userId + ".jpg");

                UploadTask uploadTask = thumb_filePath.putBytes(thumb_byte);


                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        Toast.makeText(Profile.this, "Error Uploading your profile picture try again.", Toast.LENGTH_LONG).show();
                        progress.dismiss();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        uploadedUserImage= String.valueOf(taskSnapshot.getDownloadUrl());
                        progress.dismiss();
                    }
                });



//                reference.putFile(mImageUri).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//
//                        uploadedUserImage= String.valueOf(taskSnapshot.getDownloadUrl());
//
//
//                    }
//                });
                imageView.setImageURI(mImageUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    public void UpdateProfile()
    {
        final String txtName=editText.getText().toString();
        if(!TextUtils.isEmpty(txtName))
        {
            final Intent intent=new Intent(this,HomeActivity.class);

            try
            {
                if(uploadedUserImage!=null && uploadedUserImage.length()>10){
                    final ProgressDialog progress;
                    progress = new ProgressDialog(this);
                    progress.setMessage("Creating profile...");
                    progress.show();

                    UpdateProfile updateProfile=new UpdateProfile(getApplicationContext(),progress);
                    updateProfile.execute("verify",userId,txtName,uploadedUserImage);//save user Info
                    SharedPrefManager.getInstance(getApplicationContext()).login(userId,userName);
                    intent.putExtra("Load","1");
                    progress.dismiss();
                    finish();
                    startActivity(intent);
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Profile Image");
                    builder.setMessage("Are you sure you want without your profile image?");
                    builder.setCancelable(true);
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            final ProgressDialog progress;
                            progress = new ProgressDialog(Profile.this);
                            progress.setMessage("Creating profile...");
                            progress.show();

                            UpdateProfile updateProfile=new UpdateProfile(getApplicationContext(),progress);
                            updateProfile.execute("verify",userId,txtName,"https://uplus.rw/frontassets/img/logo_main_3.png");//save user Info
                            SharedPrefManager.getInstance(getApplicationContext()).login(userId,userName);
                            intent.putExtra("Load","1");
                            progress.dismiss();
                            finish();
                            startActivity(intent);
                            dialog.cancel();
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getApplicationContext(), "Upload your profile image",Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                        }
                    });
                    AlertDialog exit = builder.create();
                    exit.show();

                }
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Please add a profile Name",Toast.LENGTH_LONG).show();
        }

    }
}
