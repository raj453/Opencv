package cultoftheunicorn.marvel;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
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

import java.util.HashMap;

public class UpdateStudActivity extends AppCompatActivity {
    private static final int CAMERA_REQUEST = 1888;
    private ImageView imageView;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private StudDbHandler db;
    Student cd;
    private Spinner studspclass;
    EditText txtrol,txtnm,txtclass,txtmb;
    String rol,name,clas,photo;
    TextView lbllroll;
    Context context;
    SessionManager session;
    String classname;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_stud);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        classname = user.get(SessionManager.KEY_CC);


        imageView = (ImageView)this.findViewById(R.id.studimg);
        Button btndel = (Button) this.findViewById(R.id.btndelete);
        Button btnsearch = (Button) this.findViewById(R.id.btnsearch);
        Button updt = (Button) this.findViewById(R.id.btnupdate);
        Button can = (Button) this.findViewById(R.id.btnreset);
        txtrol = (EditText) this.findViewById(R.id.txtroll);
        txtnm = (EditText) this.findViewById(R.id.txtname);
        txtclass = (EditText) this.findViewById(R.id.txtclass);
        txtmb = (EditText) this.findViewById(R.id.txtmob);
        studspclass = (Spinner) findViewById(R.id.studclass);
        txtclass.setText(classname);
        db = new StudDbHandler(this);
        restText();
        btndel.setOnClickListener(new View.OnClickListener()
        {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v)
            {
                deleteStud();
            }
        });

        updt.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                String roll=txtrol.getText().toString();
                String name=txtnm.getText().toString();
                String clas=txtclass.getText().toString();//String.valueOf(studspclass.getSelectedItem());//
                String mob=txtmb.getText().toString();
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
        btnsearch.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                try {
                    String roll = txtrol.getText().toString();
                    if(!roll.equals("")) {
                        cd = db.getSingleRow("select stud_roll,stud_name,stud_class,stud_mob,stud_photo from tbl_student where stud_roll='" + roll + "' limit 1");
                        txtnm.setText(cd.getName());
                        txtmb.setText(cd.getMob());
                      /*  int pos=0;
                        if(cd.getClas().equals("TYCO"))
                            pos=1;
                        if(cd.getClas().equals("TYIF"))
                            pos=2;
                        studspclass.setSelection(pos);*/
                        // Toast.makeText(getApplicationContext(),cd.getPhoto(),Toast.LENGTH_LONG).show();
                    }else
                    {
                        txtrol.requestFocus();
                        txtnm.setText("");
                        // studspclass.setSelection(0);
                    }
                }catch (Exception e){
                    Log.e("Error",e.getMessage());
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                }

            }
        });
        txtrol.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if(!s.equals("") ) {
                    try {
                        String roll = txtrol.getText().toString();
                        if(!roll.equals("")) {
                            cd = db.getSingleRow("select stud_roll,stud_name,stud_class,stud_mob,stud_photo from tbl_student where stud_roll='" + roll + "' and stud_class='" + txtclass.getText().toString() + "' limit 1");
                            txtnm.setText(cd.getName());
                            txtmb.setText(cd.getMob());
                           /* int pos=0;
                            if(cd.getClas().equals("TYCO"))
                                pos=1;
                            if(cd.getClas().equals("TYIF"))
                                pos=2;
                            studspclass.setSelection(pos);*/
                            Toast.makeText(getApplicationContext(),cd.getPhoto(),Toast.LENGTH_LONG).show();
                        }else
                        {
                            txtrol.requestFocus();
                            txtnm.setText("");
                            txtmb.setText("");
                            //  studspclass.setSelection(0);
                        }
                    }catch (Exception e){Log.e("Error",e.getMessage());   Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();}
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void afterTextChanged(Editable s) {

            }
        });
    }
    private void deleteStud(){
        String roll=txtrol.getText().toString();
        String name=txtnm.getText().toString();
        String clas=txtclass.getText().toString();//String.valueOf(studspclass.getSelectedItem());//
        String mob=txtmb.getText().toString();
        if(!roll.equals("") && !name.equals("")) {
            try {
                Student stud = new Student(roll, name, clas, mob, photo + ".jpg");
                db.deleteOne(stud);
                Toast.makeText(getApplicationContext(), "Student Delete Successfully", Toast.LENGTH_LONG).show();
                txtrol.requestFocus();
                txtrol.setText("");
                txtnm.setText("");
                txtmb.setText("");
            }catch (Exception e){Log.e("Del Error",e.getMessage());}
    }else {
            txtrol.requestFocus();
            Toast.makeText(getApplicationContext(),"Please Enter Student roll Number",Toast.LENGTH_LONG).show();

        }
    }
 /*   @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_updtStudent) {
            Intent intent=new Intent(UpdateStudActivity.this,UpdateStudActivity.class);
            startActivity(intent);
            finish();
        }
        if (id == R.id.action_viewStudent) {
            //Intent intent=new Intent(MainActivity.this,LoginActivity.class);
            //  startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
*/
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
      /*  else if (strclass.equals("Select Class")) {
            Toast.makeText(this," Select Student Class",Toast.LENGTH_LONG).show();
            studspclass.requestFocus();
            studspclass.setFocusable(true);
            return;
        }
*/
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
        int i= db.updateStudent(stud);
        Toast.makeText(this,String.valueOf(i),Toast.LENGTH_LONG).show();
        if(i>0)
        {
            Toast.makeText(this,"Student Details Updated Successfully",Toast.LENGTH_LONG).show();
            restText();
        }
        else
        {
            Toast.makeText(this,"Please Check Details",Toast.LENGTH_LONG).show();
        }
     /*  if(i>=1) {
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

           }catch (Exception e){Log.e("Er",e.getMessage());   Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();}

            Toast.makeText(this," New Student Register Successfully",Toast.LENGTH_LONG).show();
             restText();
        }
       else
         {
            Toast.makeText(this,"Registration Failed",Toast.LENGTH_LONG).show();
         }*/

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
        //txtclass.setSelection(0);
        txtrol.requestFocus();
        imageView.setImageResource(R.drawable.bg);
    }
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
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
        Intent intent=new Intent(UpdateStudActivity.this,StudRegistrationActivity.class);
        startActivity(intent);
        finish();
    }
}