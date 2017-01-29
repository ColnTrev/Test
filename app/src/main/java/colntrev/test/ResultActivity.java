package colntrev.test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
    }

    public void goTrend(View view) {
        Intent trend = new Intent(this, TrendActivity.class);
        startActivity(trend);
    }

    public void goUpdate(View view) {
        Intent update = new Intent(this, UpdateActivity.class);
        startActivity(update);
    }
}
