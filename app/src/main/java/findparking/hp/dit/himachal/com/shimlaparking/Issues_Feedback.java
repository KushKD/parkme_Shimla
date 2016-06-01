package findparking.hp.dit.himachal.com.shimlaparking;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class Issues_Feedback extends Activity {

   public String Parking_ID = null;
    String[] issue_type,Complaint_Type;
    Button cancel , submit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issues__feedback);

        Bundle bundle = getIntent().getExtras();
        Parking_ID = bundle.getString("ID");

       // Toast.makeText(getApplicationContext(),Parking_ID,Toast.LENGTH_LONG).show();

        issue_type = getResources().getStringArray(R.array.Issue_Type);
        Complaint_Type = getResources().getStringArray(R.array.Complaint_Type);

        cancel = (Button)findViewById(R.id.cancel);
        submit = (Button)findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Your feedback/complaint have been submitted successfully.",Toast.LENGTH_LONG).show();
                Issues_Feedback.this.finish();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Issues_Feedback.this.finish();
            }
        });

        Spinner IssueType = (Spinner) findViewById(R.id.testspinner);
        Spinner Complaint_Type = (Spinner) findViewById(R.id.complainttype);



    }
}
