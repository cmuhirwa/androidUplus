package info.androidhive.uplus.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;
import info.androidhive.uplus.*;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class AddGroup extends AppCompatActivity {
    public static Activity fafa;
    private static final int GALLERY_REQUEST=1;
    BottomSheetDialog dialog;
    private Uri mImageUri;
    ProgressDialog progress;
    private Bitmap bitmap;
    private String uploadURL="http://67.205.139.137/api/group.php";
    Spinner spnAmount,spnPerPerson,spnCollection;
    Boolean state;
    SaveGroupLocal saveGroupLocal;
    EditText txtAMounts,txtPerPersons,txtCollections,txtGroup;
    Button btnAdd, btn_cancel,btnParty,btnWedding,btnBDParty,btnBride,btnMerry,btnFundRaising,btnOther;
    ImageView btnImgGroup;
    String targetAmount, targetType, perPersonType, file, uploadedGroupName="";
    Spinner sp4, sp3;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    Bitmap thumb_bitmap = null;
    private StorageReference thumbImageRef;


    android.support.v7.widget.Toolbar toolbar;
    //IMAGE CROPPING LIBRARY CODE
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group);
        fafa    =this;
        firebaseStorage =FirebaseStorage.getInstance();
        //storageReference=firebaseStorage.getReference().child("group_photos");
        thumbImageRef   = FirebaseStorage.getInstance().getReference().child("group_photos");

        btnImgGroup     =(ImageView) findViewById(R.id.btnImgGroup);
        btnImgGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,GALLERY_REQUEST);
            }
        });
        try
        {
            toolbar=(Toolbar)findViewById(R.id.newGroupToolbar);
            setSupportActionBar(toolbar);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        file="test.jpg";
        init_modal_bottomsheet();
        //CODE TO ADD SPINNER DATA
        sp3 = (Spinner) findViewById(R.id.spnPerPerson);
        final EditText txtPerPerson=(EditText)findViewById(R.id.txtPerPerson);
        ArrayAdapter<CharSequence> adp3 = ArrayAdapter.createFromResource(this,R.array.PerPerson, android.R.layout.simple_list_item_1);

        adp3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp3.setAdapter(adp3);
        sp3.setOnItemSelectedListener(new OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                String ss = sp3.getSelectedItem().toString();
                String data=parent.getItemAtPosition(position).toString();
                //check the selected field
                int index=sp3.getSelectedItemPosition();
                if(index ==2)
                {

                    //txtPerPerson.setVisibility(View.GONE);
                    txtPerPerson.setEnabled(false);
                    txtPerPerson.setText("");
                    txtPerPerson.setHint("Any");
                    perPersonType="Any";
                }
                else if(index==0)
                {
                    //txtPerPerson.setVisibility(View.VISIBLE);
                    txtPerPerson.setEnabled(true);
                    txtPerPerson.setFocusable(true);
                    txtPerPerson.setHint("RWF");
                    perPersonType="Exactly";
                }
                else if(index==1)
                {
                    txtPerPerson.setEnabled(true);
                    txtPerPerson.setFocusable(true);
                    txtPerPerson.setHint("RWF");
                    perPersonType="Atleast";
                }
                //Toast.makeText(getBaseContext(), "Second :"+ss, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });
        //END OF CODE TO ADD SPINNER DATA

        //code to add data to the next spinner

        sp4 = (Spinner) findViewById(R.id.spnAmount);
        final EditText txtAmount=(EditText)findViewById(R.id.txtAmount);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.targetAmount, android.R.layout.simple_list_item_1);

        adp3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp4.setAdapter(adapter);
        sp4.setOnItemSelectedListener(new OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                int index=sp4.getSelectedItemPosition();
                String ss = sp4.getSelectedItem().toString();
                String data=parent.getItemAtPosition(position).toString();
                //check the selected field
                if(index ==1)
                {
                    //txtAmount.setVisibility(View.GONE);
                    txtAmount.setEnabled(false);
                    txtAmount.setText("");
                    txtAmount.setHint("Any");
                    targetType="Any";

                }
                else if(index==0)
                {
                    //txtAmount.setVisibility(View.VISIBLE);
                    txtAmount.setEnabled(true);
                    txtAmount.setFocusable(true);
                    txtAmount.setHint("RWF");
                    targetType="Exactly";
                }
                //Toast.makeText(getBaseContext(), "Second :"+ss, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        //end of code to add data to the next spinnner

        //variables
        txtAMounts=(EditText)findViewById(R.id.txtAmount);
        txtPerPersons=(EditText)findViewById(R.id.txtPerPerson);
        txtCollections=(EditText)findViewById(R.id.txtAmount);
        txtGroup=(EditText)findViewById(R.id.grpName);
        btnAdd=(Button)findViewById(R.id.btnNext);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddGroup();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GALLERY_REQUEST&&resultCode==RESULT_OK)
        {
            Uri imageUri=data.getData();
            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(4 ,3)
                    .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                //mImageUri = result.getUri();


                final Uri mImageUri = result.getUri();

                File thugmb_filePathUri = new File(mImageUri.getPath());

                try{
                    thumb_bitmap = new Compressor(this)
                            .setMaxWidth(1200)
                            .setMaxHeight(900)
                            .setQuality(100)
                            .compressToBitmap(thugmb_filePathUri);

                }catch (IOException e){
                    e.printStackTrace();
                }
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
                final byte[] thumb_byte = byteArrayOutputStream.toByteArray();




                Uri selectedImage=data.getData();
//                selected last part of image path
                progress = new ProgressDialog(AddGroup.this);
                progress.setMessage("Uploading ...");
                progress.setCancelable(false);
                progress.show();

                final StorageReference thumb_filePath = thumbImageRef.child(mImageUri.getLastPathSegment());

                //Toast.makeText(this, "image selected", Toast.LENGTH_SHORT).show();
                //StorageReference reference=storageReference.child(mImageUri.getLastPathSegment());
                UploadTask uploadTask = thumb_filePath.putBytes(thumb_byte);


                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        Toast.makeText(AddGroup.this, "Error Uploading your profile picture try again.", Toast.LENGTH_LONG).show();
                        progress.dismiss();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        btnAdd.setEnabled(true);
                        uploadedGroupName= String.valueOf(taskSnapshot.getDownloadUrl());
                        progress.dismiss();
                    }
                });

                btnImgGroup.setImageURI(mImageUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }


    public String imageToString(Bitmap bitmap)
    {
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] imgByte=byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgByte,Base64.DEFAULT);
    }
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager=(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    //function to send data to the background task
    public void AddGroup()
    {
        if(uploadedGroupName!=null && uploadedGroupName.length()>10){
            //Toast.makeText(getApplicationContext(),"Image is : "+uploadedGroupName, Toast.LENGTH_LONG).show();
            Boolean boolData;
            String grpName=txtGroup.getText().toString();
            targetAmount=txtAMounts.getText().toString();
            spnPerPerson=(Spinner)findViewById(R.id.spnPerPerson);
            String perPerson=txtPerPersons.getText().toString();
            String sentString;
            switch (targetType)
            {
                case "Exactly":
                    sentString="fixed";
                    break;
                case "Any":
                    sentString="any";
                    targetAmount="0";
                    break;
                default:
                    sentString=targetType;
            }
            switch (perPersonType)
            {
                case "Exactly":
                    perPersonType="fixed";
                    break;
                case "Any":
                    perPersonType="any";
                    perPerson="0";
                    break;
                case "Atleast":
                    perPersonType="min";
            }
            String errors="";
            String method ="register";
            String adminId=SharedPrefManager.getInstance(getApplicationContext()).getUserId();
            if(grpName.length()<3)
            {
                errors="Please enter valid Group Name";
                boolData=false;
            }
            else if(targetAmount.isEmpty())
            {
                errors="Please Enter Valid Target Amount";
                boolData=false;
            }
            else if(perPerson=="")
            {
                errors="Please Enter Contribution Per Person Amount";
                boolData=false;
            }
            else
            {
                boolData=true;
            }

            if(grpName.length()>=3)
            {
                if(targetAmount!="")
                {
                    if(perPerson!="")
                    {
                        if(boolData==true)
                        {
                            if(isNetworkAvailable()==false)
                            {
                                Toast.makeText(getApplicationContext(),"No internet connection!",Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                progress = new ProgressDialog(AddGroup.this);
                                progress.setTitle("Creating Group");
                                progress.setMessage("Please wait...");
                                progress.show();
                                BackgroundWorker bgWorker=new BackgroundWorker(this,progress,mImageUri);
                                bgWorker.execute(method,grpName,uploadedGroupName,sentString,targetAmount,perPersonType,perPerson,adminId);
                            }
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),errors,Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Please Add valid Per Person Amount",Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Please Add valid Group Amount",Toast.LENGTH_LONG).show();
                }
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Please Add valid Group Name",Toast.LENGTH_LONG).show();
            }
        }
        else
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Group Image");
            builder.setMessage("Are you sure you want without a group image?");
            builder.setCancelable(true);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getApplicationContext(),"No Image", Toast.LENGTH_LONG).show();
                    Boolean boolData;
                    String grpName=txtGroup.getText().toString();
                    targetAmount=txtAMounts.getText().toString();
                    spnPerPerson=(Spinner)findViewById(R.id.spnPerPerson);
                    String perPerson=txtPerPersons.getText().toString();
                    String sentString;
                    switch (targetType)
                    {
                        case "Exactly":
                            sentString="fixed";
                            break;
                        case "Any":
                            sentString="any";
                            targetAmount="0";
                            break;
                        default:
                            sentString=targetType;
                    }
                    switch (perPersonType)
                    {
                        case "Exactly":
                            perPersonType="fixed";
                            break;
                        case "Any":
                            perPersonType="any";
                            perPerson="0";
                            break;
                        case "Atleast":
                            perPersonType="min";
                    }
                    String errors="";
                    String method ="register";
                    String adminId=SharedPrefManager.getInstance(getApplicationContext()).getUserId();
                    String bankId ="2";
                    String adminPhone ="078755556";
                    String getThem="";
                    //validate data from the user
                    //Toast.makeText(this,targetAmount,Toast.LENGTH_LONG).show();
                    if(grpName.length()<3)
                    {
                        errors="Please enter valid Group Name";
                        boolData=false;
                    }
                    else if(targetAmount.isEmpty())
                    {
                        errors="Please Enter Valid Target Amount";
                        boolData=false;
                    }
                    else if(perPerson=="")
                    {
                        errors="please Enter Valid Per Person Amount";
                        boolData=false;
                    }
                    else
                    {
                        boolData=true;
                    }

                    if(grpName.length()>=3)
                    {
                        if(targetAmount!="")
                        {
                            if(perPerson!="")
                            {
                                if(boolData==true)
                                {
                                    if(isNetworkAvailable()==false)
                                    {
                                        //add data to local database
//                                        saveGroupLocal=new SaveGroupLocal(AddGroup.this);
//                                        int current=saveGroupLocal.returnCurrentId();
//                                        current=current+1;
//                                        String groupId=String.valueOf(current);
//                                        state=saveGroupLocal.addGroupLocal(groupId,grpName,targetType,targetAmount,perPersonType,perPerson,adminId,adminPhone,bankId,"0");
//                                        if(state)
//                                        {
//                                            Toast.makeText(getApplicationContext(),"Group Saved to Local Database",Toast.LENGTH_LONG).show();
//                                        }
//                                        else
//                                        {
//                                            Toast.makeText(getApplicationContext(),"Group Not Added Please check you connecction",Toast.LENGTH_LONG).show();
//                                        }
                                        Toast.makeText(getApplicationContext(),"No internet connection!",Toast.LENGTH_LONG).show();
                                    }
                                    else
                                    {
                                        progress = new ProgressDialog(AddGroup.this);
                                        progress.setTitle("Creating Group");
                                        progress.setMessage("Please wait...");
                                        progress.show();
                                        BackgroundWorker bgWorker=new BackgroundWorker(AddGroup.this,progress,mImageUri);
                                        bgWorker.execute(method,grpName,"https://uplus.rw/frontassets/img/logo_main_3.png",sentString,targetAmount,perPersonType,perPerson,adminId);
                                    }
                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(),errors,Toast.LENGTH_SHORT).show();
                                }
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"Please Add valid Per Person Amount",Toast.LENGTH_LONG).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Please Add valid Group Amount",Toast.LENGTH_LONG).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Please Add valid Group Name",Toast.LENGTH_LONG).show();
                    }
                    dialog.cancel();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getApplicationContext(), "Upload your group image",Toast.LENGTH_SHORT).show();
                    dialog.cancel();
                }
            });
            AlertDialog exit = builder.create();
            exit.show();
        }


    }
    public void getBack()
    {
        Intent t=new Intent(getApplicationContext(),HomeActivity.class);
        startActivity(t);
    }

    public void init_modal_bottomsheet() {
        View modalbottomsheet = getLayoutInflater().inflate(R.layout.modal_bottomsheet, null);

        dialog = new BottomSheetDialog(this);
        dialog.setContentView(modalbottomsheet);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
        btn_cancel = (Button) modalbottomsheet.findViewById(R.id.btn_cancel);
        btn_cancel = (Button) modalbottomsheet.findViewById(R.id.btn_cancel);
        btnParty=(Button) modalbottomsheet.findViewById(R.id.btnParty);
        btnWedding=(Button) modalbottomsheet.findViewById(R.id.btnWedding);
        btnBDParty=(Button) modalbottomsheet.findViewById(R.id.btnBDParty);
        btnBride=(Button) modalbottomsheet.findViewById(R.id.btnBride);
        btnMerry=(Button) modalbottomsheet.findViewById(R.id.btnMerry);
        btnFundRaising=(Button) modalbottomsheet.findViewById(R.id.btnFundRaising);
        btnOther=(Button) modalbottomsheet.findViewById(R.id.btnOther);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnParty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtGroup.setHint(btnParty.getText().toString()+" name here!");
                dialog.dismiss();
            }
        });

        btnWedding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtGroup.setHint(btnWedding.getText().toString()+" name here!");
                dialog.dismiss();
            }
        });
        btnBDParty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtGroup.setHint(btnBDParty.getText().toString()+" name here!");
                dialog.dismiss();
            }
        });
        btnBride.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtGroup.setHint(btnBride.getText().toString()+" name here!");
                dialog.dismiss();
            }
        });
        btnMerry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtGroup.setHint(btnMerry.getText().toString()+" name here!");
                dialog.dismiss();
            }
        });

        btnFundRaising.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtGroup.setHint(btnFundRaising.getText().toString()+" name here!");
                dialog.dismiss();
            }
        });

        btnOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });
        //txtGroup
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        return;
    }
}
