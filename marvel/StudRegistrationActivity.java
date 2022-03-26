package cultoftheunicorn.marvel;

import android.Manifest;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.opencv.cultoftheunicorn.marvel.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class StudRegistrationActivity extends AppCompatActivity {
    private static final int CAMERA_REQUEST = 1888;
    private ImageView imageView;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private static final int STORAGE_PERMISSION_CODE = 101;
    private static final int MY_PERMISSIONS_REQUEST_CODE = 123;
    Button cla;
    private StudDbHandler db;
    Student cd;
    private Spinner studspclass;
    EditText txtrol,txtnm,txtclass,txtmb;
    String rol,name,clas,photo;
    TextView lbllroll;
    private Context context;
    SessionManager session;
    String classname;
    Activity mActivity;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stud_registration);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        classname = user.get(SessionManager.KEY_CC);
        mActivity = StudRegistrationActivity.this;

        // checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,STORAGE_PERMISSION_CODE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            checkPermission();
        }


        imageView = (ImageView)this.findViewById(R.id.studimg);
        Button photoButton = (Button) this.findViewById(R.id.btncapture);
        Button reg = (Button) this.findViewById(R.id.btnregister);
        Button can = (Button) this.findViewById(R.id.btnreset);
        Button updt = (Button) this.findViewById(R.id.btnupdt);
        Button vw = (Button) this.findViewById(R.id.btnvw);

        cla=findViewById(R.id.btnreset);
        txtrol = (EditText) this.findViewById(R.id.txtroll);
        txtnm = (EditText) this.findViewById(R.id.txtname);
        txtmb = (EditText) this.findViewById(R.id.txtmob);
        txtclass = (EditText) this.findViewById(R.id.txtclass);
        lbllroll= (TextView) this.findViewById(R.id.lbllroll);
        studspclass = (Spinner) findViewById(R.id.studclass);
        txtclass.setText(classname);
        db = new StudDbHandler(this);
        restText();
        photoButton.setOnClickListener(new View.OnClickListener()
        {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v)
            {
               /* if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                }
                else
                {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }*/

                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });

        reg.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                String roll=txtrol.getText().toString();
                String name=txtnm.getText().toString();
                String mob=txtmb.getText().toString();
                String clas=txtclass.getText().toString();//String.valueOf(studspclass.getSelectedItem());//
                submitForm(roll,name,clas,mob);
            }
        });
        can.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                restText();
            }
        });
        cla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bak=new Intent(StudRegistrationActivity.this,HomeActivity.class);
                startActivity(bak);
            }
        });
        updt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent updt=new Intent(StudRegistrationActivity.this,UpdateStudActivity.class);
                startActivity(updt);
            }
        });
        vw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent vw=new Intent(StudRegistrationActivity.this,UpdateStudActivity.class);
                startActivity(vw);
            }
        });
    }
   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
*/
  /*  @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_updtStudent) {
            Intent intent=new Intent(StudRegistrationActivity.this,UpdateStudActivity.class);
            startActivity(intent);
            finish();
        }
        if (id == R.id.action_viewStudent) {
          //  Intent intent=new Intent(StudRegistrationActivity.this,SavePicActivity.class);
          //  startActivity(intent);
          //  finish();
        }
        return super.onOptionsItemSelected(item);
    }*/

    private void submitForm(final String strroll,final String strname,final String strclass,final String strmob) {
        if (!validateField(txtrol,  "Enter Student Roll Number")) {
            return;
        }
        else  if (!validateField(txtnm,  "Enter Student Name")) {
            return;
        }
        else  if (!validateField(txtmb,  "Enter Student Mobile No")) {
            return;
        }
        else  if (!validateField(txtclass,  "Enter Class Name")) {
            return;
        }
        /*else if (strclass.equals("Select Class")) {
            Toast.makeText(this," Select Student Class",Toast.LENGTH_LONG).show();
            studspclass.requestFocus();
            studspclass.setFocusable(true);
            return;
        }*/
        else
        {
            String strphoto=strroll+"_"+strclass;
            addColl(strroll, strname, strclass,strmob,strphoto);
        }
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
    private boolean validateField(EditText id,String msg) {
        if (id.getText().toString().trim().isEmpty()) {
            Toast.makeText(this,msg,Toast.LENGTH_LONG).show();
            requestFocus(id);
            return false;
        }
        return true;
    }

    private void addColl(final String strroll,final String strname,final String strclass,final String strmob,final String photo) {
        // add  Student
        Student stud = new Student(strroll,strname,strclass,strmob,photo+".jpg");
        // add them
        String i= db.addStudent(stud);

        if(Integer.parseInt(i)>=1) {

            try{

                imageView.buildDrawingCache();

                Bitmap bmp = imageView.getDrawingCache();
                File storageLoc = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES); //context.getExternalFilesDir(null);

                File file = new File(storageLoc, photo + ".jpg");

                try{
                    FileOutputStream fos = new FileOutputStream(file);
                    bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    fos.close();

                    scanFile(context, Uri.fromFile(file));
                    Log.e("cnf-Er",file.getAbsolutePath()+"asdasd");
                    Toast.makeText(this,file.getAbsolutePath(),Toast.LENGTH_LONG).show();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Log.e("cnf-Er",e.getMessage());
                    Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("IO-Er",e.getMessage());
                    Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
                }

            }catch (Exception e){Log.e("Er",e.getMessage());
            //Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
            }
            imageView.invalidate();
            Toast.makeText(this," New Student Register Successfully",Toast.LENGTH_LONG).show();
            restText();
        }
        else
        {
            Toast.makeText(this,"Registration Failed",Toast.LENGTH_LONG).show();
        }

    }
    private static void scanFile(Context context, Uri imageUri){
        Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        scanIntent.setData(imageUri);
        context.sendBroadcast(scanIntent);
    }

    public void restText()
    {
        txtrol.setText("");
        txtnm.setText("");
        txtmb.setText("");
        txtrol.requestFocus();
        imageView.setImageResource(R.drawable.bg);
        try{
            cd=db.getLastRoll("select stud_roll from tbl_student where stud_class='" + txtclass.getText().toString() + "' order by stud_sr desc limit 1","roll");
            lbllroll.setText("last Roll Number : "+cd.getRoll().toString());
        }catch (Exception e){Log.e("ff",e.getMessage()); Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();}
    }

    // Function to check and request permission.
    public void checkPermission(String permission, int requestCode)
    {
        if (ContextCompat.checkSelfPermission(StudRegistrationActivity.this, permission)
                == PackageManager.PERMISSION_DENIED) {

            // Requesting the permission
            ActivityCompat.requestPermissions(StudRegistrationActivity.this,
                    new String[] { permission },
                    requestCode);
        }
        else {
            Toast.makeText(StudRegistrationActivity.this,
                    "Permission already granted",
                    Toast.LENGTH_SHORT)
                    .show();
        }
    }
    protected void checkPermission(){
        if(ContextCompat.checkSelfPermission(StudRegistrationActivity.this,Manifest.permission.CAMERA)
                + ContextCompat.checkSelfPermission(
                mActivity,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){

            // Do something, when permissions not granted
            if(ActivityCompat.shouldShowRequestPermissionRationale(
                    mActivity,Manifest.permission.CAMERA)
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                    mActivity,Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                // If we should give explanation of requested permissions
                // Show an alert dialog here with request explanation
                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                builder.setMessage("Camera and Write External" +
                        " Storage permissions are required to do the task.");
                builder.setTitle("Do You Want to grant those permissions?");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ActivityCompat.requestPermissions(
                                mActivity,
                                new String[]{
                                        Manifest.permission.CAMERA,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                                },
                                MY_PERMISSIONS_REQUEST_CODE
                        );
                    }
                });
                //   builder.setNeutralButton("Cancel",null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }else{
                // Directly request for required permissions, without explanation
                ActivityCompat.requestPermissions(
                        mActivity,
                        new String[]{
                                Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                        },MY_PERMISSIONS_REQUEST_CODE
                );
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        switch (requestCode){
            case MY_PERMISSIONS_REQUEST_CODE:{
                // When request is cancelled, the results array are empty
                if(
                        (grantResults.length >0) &&
                                (grantResults[0]
                                        + grantResults[1]
                                        == PackageManager.PERMISSION_GRANTED
                                )
                ){
                    // Permissions are granted
                    Toast.makeText(context,"Permissions granted.",Toast.LENGTH_SHORT).show();
                }else {
                    // Permissions are denied
                    Toast.makeText(context,"Permissions denied.",Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }
/*
 @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
            else
            {
                Toast.makeText(this, "camera permission denied! For Continue Please Allow Camera Permission", Toast.LENGTH_LONG).show();
                Intent intent=new Intent(StudRegsitrationActivity.this,second.class);
                startActivity(intent);
                finish();
            }

        }

         if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(StudRegsitrationActivity.this, "Storage Permission Granted", Toast.LENGTH_SHORT)
                        .show();
            } else {
                Toast.makeText(StudRegsitrationActivity.this, "Storage Permission Denied! For Continue Please Allow Storage Permission", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(StudRegsitrationActivity.this,second.class);
                startActivity(intent);
                finish();
            }
        }

    }*/

    @Override    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    @Override
    public void onBackPressed() {
        Intent intent=new Intent(StudRegistrationActivity.this,HomeActivity.class);
        startActivity(intent);
        finish();
    }
}