package cultoftheunicorn.marvel;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.opencv.cultoftheunicorn.marvel.R;

public class HomeActivity extends AppCompatActivity {
    TextView txt;
    SessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        session = new SessionManager(getApplicationContext());
        if(!session.checkLogin()){finish();}

        Button vwstud= findViewById(R.id.vwstud);
        Button attendence = findViewById(R.id.attendence);
        Button registration =findViewById(R.id.registration);
        Button cancel =findViewById(R.id.cancel);
        txt=(TextView) findViewById(R.id.txt);
        vwstud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(HomeActivity.this, ViewAttActivity.class);
                startActivity(intent);
            }
        });
        attendence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(HomeActivity.this, StudRegistrationActivity.class);
                startActivity(intent);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.logoutUser();
                finish();
            }
        });
        Integer colorFrom = getResources().getColor(R.color.colorPrimary);
        Integer colorTo = getResources().getColor(R.color.colorAccent);
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                txt.setTextColor((Integer)animator.getAnimatedValue());
            }

        });
        colorAnimation.start();
    }
    @Override
    public void onBackPressed() {
        finish();
    }
}