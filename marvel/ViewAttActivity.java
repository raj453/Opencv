package cultoftheunicorn.marvel;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.opencv.cultoftheunicorn.marvel.R;

import java.util.HashMap;
import java.util.List;

public class ViewAttActivity extends AppCompatActivity {
    SessionManager session;
    String classname;
    String subject;
    StudentAtt att;
    List<StudentAtt> attlist;
    private StudDbHandler db;
    //TableLayout tableLayout;
    private TableLayout tableLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_att);

        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        classname = user.get(SessionManager.KEY_CC);
        subject = user.get(SessionManager.KEY_SUBJECT);
        db = new StudDbHandler(this);
    try {
   // attlist = db.allStudentsAtt("select * from tbl_student_att where stud_class='" + classname + "' and stud_subj='" + subject + "' order by att_sr desc");
String sql="select att_dttime,stud_roll,stud_class,stud_subj,att_status from tbl_student_att where stud_class='" + classname + "' and stud_subj='" + subject + "' order by att_sr desc";
      /*  attlist = db.allStudentsAtt(sql);
        //txtnm.setText(cd.getName());
      //  String atts=att.getDt()+" "+att.getRoll()+" "+att.getClas()+" "+att.getSubj()+" "+att.getStatus();
    Toast.makeText(getApplicationContext(), attlist.toString()+ " "+sql, Toast.LENGTH_LONG).show();
    Log.d("Data Att", attlist.toString());*/

      //  BuildTable(sql);
        getTbl(sql);

        Cursor res = db.getAllData(sql);
        StringBuffer buffer = new StringBuffer();
        while (res.moveToNext()){
            buffer.append("Date Time: "+ res.getString(0)+"n");
            buffer.append("Roll No: "+ res.getString(1)+"n");
            buffer.append("Class Name: "+ res.getString(2)+"n");
            buffer.append("Subject Name: "+ res.getString(3)+"n");
            buffer.append("Status: "+ res.getString(4)+"n");

        }

    //    Toast.makeText(getApplicationContext(), buffer.toString(), Toast.LENGTH_LONG).show();
        Log.d("Data Att", buffer.toString());

    }catch (Exception e){    Log.e("Data Att error", e.getMessage());
}
    }

private  void getTbl(String sql)
{
    TableLayout tableLayout=(TableLayout)findViewById(R.id.main_table);
    // Add header row
    TableRow rowHeader = new TableRow(this);
    rowHeader.setBackgroundColor(Color.parseColor("#c0c0c0"));
    rowHeader.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
            TableLayout.LayoutParams.WRAP_CONTENT));
    String[] headerText={"Sr.No","Date Time","Roll No","Class","Subject","Status"};
    for(String c:headerText) {
        TextView tv = new TextView(this);
        tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT));
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(18);
        tv.setPadding(5, 5, 5, 5);
        tv.setText(c);
        rowHeader.addView(tv);
        rowHeader.setBackgroundResource(R.color.lightGray);
    }
    tableLayout.addView(rowHeader);


    int i=0;
    Cursor cursor = db.getAllData(sql);
    if(cursor.getCount() > 0){

        while (cursor.moveToNext()) {
            i++;
            // Read columns data
            String sr= String.valueOf(i);
            String dt= cursor.getString(cursor.getColumnIndex("att_dttime"));
            String roll= cursor.getString(cursor.getColumnIndex("stud_roll"));
            String clas= cursor.getString(cursor.getColumnIndex("stud_class"));
            String subj= cursor.getString(cursor.getColumnIndex("stud_subj"));
            String status= cursor.getString(cursor.getColumnIndex("att_status"));

            // dara rows
            TableRow row = new TableRow(this);
            row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));
            String[] colText={sr,dt,roll,clas,subj,status};
            for(String text:colText) {
                TextView tv = new TextView(this);
                tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                tv.setGravity(Gravity.CENTER);
                tv.setTextSize(16);
                tv.setPadding(5, 5, 5, 5);
                tv.setText(text);
                row.addView(tv);
                row.setBackgroundResource(R.color.white);
            }
            tableLayout.addView(row);

        }
    }
}
    @Override
    public void onBackPressed() {
        finish();
    }
}