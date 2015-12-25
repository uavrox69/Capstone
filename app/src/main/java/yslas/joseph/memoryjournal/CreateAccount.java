package yslas.joseph.memoryjournal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Created by Joseph Yslas on 12/22/2015.
 * This Class is used for the create account screen, it will have methods to stores new user information and security questiosn.
 */
public class CreateAccount extends FragmentActivity
{
    String[] securityQuestions = {"Security Question","What city were you born in?","What was your mothers maiden name","What was your first job?",
            "What was your first pets name?","Where did you attend fourth grade at?","What did you want to be when you grew up?",
            "What is your favorite color", "Who was your best friend when you were younger?"};

    public Spinner questionSpin;
    public Spinner questionSpin2;

    private View createView = null;
    private RelativeLayout createArea = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account);
        createArea = (RelativeLayout)findViewById(R.id.text_popup);
        questionSpin = (Spinner)findViewById(R.id.question_spinner);
        questionSpin2 = (Spinner)findViewById(R.id.question_spinner2);
        Button back = (Button) (findViewById(R.id.main_back_button));
        Button createAccount = (Button)(findViewById(R.id.create_button));

        //these two methods are to create and fill the spinners on the create account page
        ArrayAdapter<String>questionAdapt = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,securityQuestions){
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent)
            {
                View v = null;

                // If this is the initial dummy entry, make it hidden
                if (position == 0) {
                    TextView tv = new TextView(getContext());
                    tv.setHeight(0);
                    tv.setVisibility(View.GONE);
                    v = tv;
                }
                else {
                    // Pass convertView as null to prevent reuse of special case views
                    v = super.getDropDownView(position, null, parent);
                }

                // Hide scroll bar because it appears sometimes unnecessarily, this does not prevent scrolling
                parent.setVerticalScrollBarEnabled(false);
                return v;
            }
        };
        questionAdapt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        questionSpin.setAdapter(questionAdapt);

        ArrayAdapter<String>questionAdapt2 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,securityQuestions){
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent)
            {
                View v = null;

                // If this is the initial dummy entry, make it hidden
                if (position == 0) {
                    TextView tv = new TextView(getContext());
                    tv.setHeight(0);
                    tv.setVisibility(View.GONE);
                    v = tv;
                }
                else {
                    // Pass convertView as null to prevent reuse of special case views
                    v = super.getDropDownView(position, null, parent);
                }

                // Hide scroll bar because it appears sometimes unnecessarily, this does not prevent scrolling
                parent.setVerticalScrollBarEnabled(false);
                return v;
            }
        };
        questionAdapt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        questionSpin2.setAdapter(questionAdapt2);

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createMessage();
            }
        });

    }
    /**
    open the message that will state if anything is wrong with account create
    will take in a string of errors, if null all is well
    */
    private void createMessage()
    {
        //if it is open return
        if (createView != null)
        {
            return;
        }
        //create the view
        LayoutInflater inflater = getLayoutInflater();
        createView = inflater.inflate(R.layout.text_popup, null);
        createArea.addView(createView);

        RelativeLayout popup = (RelativeLayout)findViewById(R.id.text_popup);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)popup.getLayoutParams();
        params.setMargins(10,200,0,0);
        popup.setLayoutParams(params);

        //make the other views disappear
        View title = findViewById(R.id.creation_layout);
        title.setVisibility(View.INVISIBLE);

        //close if pressed outside
        findViewById(R.id.mem_journal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeMessage();
            }
        });

    }
    private void closeMessage()
    {
        if (createView == null)
        {
            return;
        }
        createArea.removeView(createView);

        View title = findViewById(R.id.creation_layout);
        title.setVisibility(View.VISIBLE);
    }

}
