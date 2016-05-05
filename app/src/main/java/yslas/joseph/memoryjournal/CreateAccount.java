package yslas.joseph.memoryjournal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Joseph Yslas on 12/22/2015.
 * This Class is used for the create account screen, it will have methods to stores new user information and security questiosn.
 */
public class CreateAccount extends FragmentActivity
{
    String[] securityQuestions = {"Security Question","What city were you born in?","What was your mothers maiden name?","What was your first job?",
            "What was your first pets name?","Where did you attend fourth grade at?","What did you want to be when you grew up?",
            "What is your favorite color", "Who was your best friend when you were younger?"};

    public Spinner questionSpin;
    public Spinner questionSpin2;
    public String errors = "";
    public UserAccount user;

    private View createView = null;
    private Database db;
    boolean creationSuc = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account);
        questionSpin = (Spinner)findViewById(R.id.question_spinner);
        questionSpin2 = (Spinner)findViewById(R.id.question_spinner2);
        Button back = (Button) (findViewById(R.id.main_back_button));
        Button createAccount = (Button)(findViewById(R.id.create_button));
        db = MainActivity.currInstance.getDb();

        //these two methods are to create and fill the spinners on the create account page
        final ArrayAdapter<String>questionAdapt = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,securityQuestions){
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
        questionAdapt2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        questionSpin2.setAdapter(questionAdapt2);

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo verify passwords the same, nothing left blank, and change the database
                String email = ((EditText)findViewById(R.id.email)).getText().toString() ;
                String userName = ((EditText)findViewById(R.id.u_name)).getText().toString() ;
                String password = ((EditText)findViewById(R.id.password)).getText().toString();
                String passwordRetype = ((EditText)findViewById(R.id.retype_password)).getText().toString();
                String security_Q1= "";
                String security_Q2= "";
                String securityA1 = ((EditText)findViewById(R.id.s_question_answer)).getText().toString();
                String securityA2 = ((EditText)findViewById(R.id.s_question_answer2)).getText().toString();

                //Log.d("account", securityA2 + " + " +securityA1);

                //Checking for account creation errors
                if ( email.isEmpty())
                {
                    errors+= "Email is not complete\n";
                }
                if ( userName.isEmpty() )
                {
                    errors+="User name is not Complete\n";
                }
                if ( password.isEmpty()|| passwordRetype.isEmpty())
                {
                    errors+="Password is not complete\n";
                }
                if ( password.compareTo(passwordRetype) != 0)
                {
                    errors+="Passwords do not match\n";
                }
                if (questionSpin.getSelectedItemPosition() == 0)
                {
                    errors+="Question one is not selected\n";
                }
                else {
                    security_Q1 = securityQuestions[questionSpin.getSelectedItemPosition()];
                    //Log.d("account", "Security position" + security_Q1);
                }
                if (questionSpin2.getSelectedItemPosition() == 0)
                {
                    errors+="Question two is not selected\n";
                }
                else {
                    security_Q2 = securityQuestions[questionSpin2.getSelectedItemPosition()];
                }
                if ( securityA1.isEmpty() || securityA2.isEmpty() )
                {
                    errors+="Answers is not complete\n";
                }
                if (db.emailExists(email))
                    errors+="Email already exists";
                //Log.d("message",errors);
                createMessage();
                //no errors create the account
                if (creationSuc)
                {
                    db.insertAccount(email.toLowerCase(), userName, password, security_Q1, security_Q2, securityA1.toLowerCase(), securityA2.toLowerCase());
                    user= db.getAccount(email);
                    //may not need with changes
                    //MainActivity.currInstance.fillAccount(user);
                    user.testAccount();
                    creationSuc = false;
                    finish();

                }
            }
        });

    }

    /**
    open the message that will state if anything is wrong with account create
    will take in a string of errors, if null all is well
    */
    private void createMessage()
    {
        Toast message;
        if (!errors.isEmpty()) {
            message = Toast.makeText(this, errors, Toast.LENGTH_LONG);
            message.setGravity(Gravity.CENTER, 0, 0);
            message.show();
            errors = "";
        }
        else
        {
            message = Toast.makeText(this, "Account creation complete", Toast.LENGTH_LONG);
            message.setGravity(Gravity.CENTER, 0, 0);
            message.show();
            creationSuc = true;
        }

    }


}
