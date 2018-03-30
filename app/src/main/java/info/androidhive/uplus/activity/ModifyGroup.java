package info.androidhive.uplus.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import info.androidhive.uplus.ModifyWorker;
import info.androidhive.uplus.R;
import info.androidhive.uplus.SaveGroupLocal;
import info.androidhive.uplus.groupdetails;

public class ModifyGroup extends AppCompatActivity {
    public static Activity ModifyPage;
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
    String file;
    Button btnAdd;
    Button btn_cancel;
    ImageView btnImgGroup;
    String targetAmount, targetType, perPersonType, gId, ModGroupAmount, groupBalance;
    Spinner sp4;
    Spinner sp3;
    String uploadedGroupName="";
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    android.support.v7.widget.Toolbar toolbar;
    //IMAGE CROPPING LIBRARY CODE
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group);

        //get group id from Group Details
        txtGroup = (EditText)findViewById(R.id.grpName);
        Intent id = getIntent();
        gId = id.getStringExtra("ModGroupId");
        final String gpName = id.getStringExtra("ModGroupName");
        ModGroupAmount=id.getStringExtra("ModGroupAmount");
        groupBalance = id.getStringExtra("groupBalance");
        txtAMounts=(EditText)findViewById(R.id.txtAmount);
        txtAMounts.setText(ModGroupAmount);
        txtGroup.setText(gpName);

        //variables
        txtPerPersons=(EditText)findViewById(R.id.txtPerPerson);
        txtCollections=(EditText)findViewById(R.id.txtAmount);
        btnAdd=(Button)findViewById(R.id.btnNext);

        if(gId.isEmpty()){
            finish();
        }

        ModifyPage=this;
        firebaseStorage=FirebaseStorage.getInstance();
        storageReference=firebaseStorage.getReference().child("group_photos");
        btnImgGroup=(ImageView) findViewById(R.id.btnImgGroup);
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

        //CODE TO ADD SPINNER DATA
        sp3 = (Spinner) findViewById(R.id.spnPerPerson);
        final EditText txtPerPerson=(EditText)findViewById(R.id.txtPerPerson);
        ArrayAdapter<CharSequence> adp3 = ArrayAdapter.createFromResource(this,R.array.PerPerson, android.R.layout.simple_list_item_1);

        adp3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp3.setAdapter(adp3);
        sp3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
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
        sp4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
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
                    .setAspectRatio(1 ,1)
                    .setMinCropResultSize(200,200)
                    .setMaxCropResultSize(2000,2000)
                    .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                mImageUri = result.getUri();
                Uri selectedImage=data.getData();
//                selected last part of image path
                progress = new ProgressDialog(ModifyGroup.this);
                progress.setMessage("Uploading ...");
                progress.setCancelable(false);
                progress.show();
                //Toast.makeText(this, "image selected", Toast.LENGTH_SHORT).show();
                StorageReference reference=storageReference.child(mImageUri.getLastPathSegment());

                reference.putFile(mImageUri).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        btnAdd.setEnabled(true);
                        uploadedGroupName= String.valueOf(taskSnapshot.getDownloadUrl());
                        //Toast.makeText(AddGroup.this,"image Uploaded",Toast.LENGTH_LONG).show();
                        progress.dismiss();
                    }
                });
                try {
                    bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),mImageUri);
                    //uploadImage();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                btnImgGroup.setImageURI(mImageUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    // CHECK FOR A NETWORK
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager=(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    //function to modify group
    public void AddGroup()
    {
        if(uploadedGroupName!=null && uploadedGroupName.length()>10){
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
            String method ="modify";
            String adminId=SharedPrefManager.getInstance(getApplicationContext()).getUserId();
            String bankId ="2";
            String adminPhone ="078755556";
            String getThem="";
            //validate data from the user
            Toast.makeText(this,targetAmount,Toast.LENGTH_LONG).show();
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
                                //add data tto local database
                                saveGroupLocal=new SaveGroupLocal(this);
                                int current=saveGroupLocal.returnCurrentId();
                                current=current+1;
                                String groupId=String.valueOf(current);
                                state=saveGroupLocal.addGroupLocal(groupId,grpName,targetType,targetAmount,perPersonType,perPerson,adminId,adminPhone,bankId,"0");
                                if(state)
                                {
                                    Toast.makeText(getApplicationContext(),"Group Saved to Local Database",Toast.LENGTH_LONG).show();
                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(),"Group Not Added Please check you connecction",Toast.LENGTH_LONG).show();
                                }
                            }
                            else
                            {
                                progress = new ProgressDialog(ModifyGroup.this);
                                progress.setTitle("Creating Group");
                                progress.setMessage("Please wait while we are Modifying group...");
                                progress.show();
                                ModifyWorker modifyWorker=new ModifyWorker(this,progress);
                                modifyWorker.execute("modify",grpName,sentString,targetAmount,perPersonType,perPerson,adminId,gId,uploadedGroupName, groupBalance);

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
        else{
            Toast.makeText(this,"Please wait for image to upload or if not upload it",Toast.LENGTH_LONG).show();
        }


    }

}
