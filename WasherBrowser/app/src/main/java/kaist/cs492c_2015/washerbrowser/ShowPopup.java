package kaist.cs492c_2015.washerbrowser;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class ShowPopup extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        setTitle("Washer Browser");
        setContentView(R.layout.popupdialog);
        TextView durationTitle = (TextView) findViewById(R.id.durationTitle);
        durationTitle.setText(getIntent().getStringExtra("customdata"));
        durationTitle.setTextSize(20);

        Button ok = (Button)findViewById(R.id.popOkB);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button cancel = (Button)findViewById(R.id.popCancelB);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}