package cultoftheunicorn.marvel;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.opencv.cultoftheunicorn.marvel.R;

public class LoginActivity extends AppCompatActivity {
    SessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button submit = findViewById(R.id.submit);
        Button close = findViewById(R.id.close);
        final Spinner user =findViewById(R.id.tclass);
        final Spinner ssubj =findViewById(R.id.tsubj);
        final EditText password=findViewById(R.id.txtpass);
        session = new SessionManager(getApplicationContext());
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String clas=String.valueOf(user.getSelectedItem());
                String subj=String.valueOf(ssubj.getSelectedItem());
                String pass=password.getText().toString().trim();
                if(((clas.equals("TYCO") &&(pass.equals("TYCO"))) || (clas.equals("TYIF") && (pass.equals("TYIF")))) && (!subj.equals("")) ) {
                    if(clas.equals("TYCO"))
                        session.createLoginSession("TYCO",subj);
                    if(clas.equals("TYIF"))
                        session.createLoginSession("TYIF",subj);
                    Intent intent= new Intent(LoginActivity.this,HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    Toast.makeText(LoginActivity.this, "Please Enter Valid Details ", Toast.LENGTH_LONG).show();
                }
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}