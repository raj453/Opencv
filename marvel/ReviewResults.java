package cultoftheunicorn.marvel;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

// uncomment when you enable firebase
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;

import org.opencv.cultoftheunicorn.marvel.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

class Attendees {

    public String names;
    public String date;

    public Attendees() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Attendees(String names, String date) {
        this.names  = names;
        this.date = date;
    }

}

public class ReviewResults extends AppCompatActivity implements ReviewListAdapter.ClickListener {

    private List<String> commitList = new ArrayList<>();
    SessionManager session;
    String classname;
    String subject;
    private StudDbHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_results);
        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        classname = user.get(SessionManager.KEY_CC);
        subject = user.get(SessionManager.KEY_SUBJECT);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        db = new StudDbHandler(this);

        Button commitButton = (Button) findViewById(R.id.button);
        commitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commit();
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Review and Mark");
        }

        List<String> reviewList = Arrays.asList(getIntent().getStringArrayExtra("list"));

        ReviewListAdapter reviewListAdapter = new ReviewListAdapter(this, reviewList);
        reviewListAdapter.setClickListener(this);
        recyclerView.setAdapter(reviewListAdapter);

        //Setting LayoutManager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        /*//For adding dividers in the list
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(this, R.drawable.line_divider));
        recyclerView.addItemDecoration(dividerItemDecoration);*/


    }

    @Override
    public void onItemClick(String name) {
        if(commitList.contains(name))
            commitList.remove(name);
        else
            commitList.add(name);
    }

    public void commit() {

        if(commitList.size() != 0) {
//            Enable firebase and then uncomment the following lines

//            FirebaseDatabase database = FirebaseDatabase.getInstance();
//            DatabaseReference myRef = database.getReference("attendence");

//            convert to a comma separated string
//            this has to be the worst way to push data to a db
            DateFormat df = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
            Date dateobj = new Date();
            String dt=df.format(dateobj);
            String i="";
           StringBuilder sb = new StringBuilder();
            for (String s : commitList) {
                sb.append(s);
               // sb.append(",");
            //    Toast.makeText(getApplicationContext(), classname+" "+subject+" "+ " "+ dt, Toast.LENGTH_LONG).show();
                StudentAtt studatt = new StudentAtt(dt,sb.toString(),classname,subject,"present");
                // add them
                 i= db.addStudentAtt(studatt);
            }

            // StudentAtt studatt = new StudentAtt(strroll,strname,strclass,strmob,photo+".jpg");
            // add them
          //  String i= db.addStudent(studatt);

            if(Integer.parseInt(i)>=1)
            {

                Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
            }
            else
            {
               Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();

            }

//            Attendees at = new Attendees(sb.toString(), (new Date()).toString());
//            String key = myRef.push().getKey();
//            myRef.child(key).setValue(at);

          //  Toast.makeText(getApplicationContext(), "Enable firebase for this to work", Toast.LENGTH_LONG).show();
//            finish();


//            System.out.println(sb.toString());

        }
        else {
            Toast.makeText(getApplicationContext(), "Please select at least one student", Toast.LENGTH_SHORT).show();
        }
    }
}
