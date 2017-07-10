package attendance.mobius.mobius_android;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.protocol.HTTP;
import cz.msebera.android.httpclient.util.EntityUtils;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";
    CustomPreference pref = new CustomPreference(this);

    LoadJspForInput loadJspForInput;
    String resultPost;
    Boolean registerOk = false;
    @BindView(R.id.input_register_name)
    EditText _nameText;
    @BindView(R.id.input_register_id)
    EditText _idText;
    @BindView(R.id.input_register_pwd)
    EditText _passwordText;
    @BindView(R.id.btn_register_ok)
    Button _signupButton;
    @BindView(R.id.link_login)
    TextView _loginLink;

    String pos = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ButterKnife.bind(this);

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = _nameText.getText().toString();
                String id = _idText.getText().toString();
                String pwd = _passwordText.getText().toString();

                loadJspForInput = new LoadJspForInput(id, pwd,name, pos);
                loadJspForInput.execute();

//                try {
//                    Thread.sleep(200);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//                JsonParsing("");

                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
            Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(i);
            finish();

            }
        });



        //Spinner
        final Spinner positionSpinner = (Spinner) findViewById(R.id.spinner_register_position);
        ArrayAdapter positionAdapter = ArrayAdapter.createFromResource(this, R.array.position,
                android.R.layout.simple_spinner_dropdown_item);
        positionSpinner.setAdapter(positionAdapter);

        positionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                pos = positionSpinner.getSelectedItem().toString();
                if( pos.equals("교수") )
                    pos = "pro";
                else
                    pos = "stu";
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                pos = "pro";
            }
        });



    }


    public void signup() {

        Log.d(TAG, "Signup");

        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                if(!JsonParsing("")) {
//                    onSignupFailed();
//
//                    return;
//                }

                JsonParsing("");
            }
        }, 1500);


        // TODO: Implement your own signup logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        if (!validate()) {
                            onSignupFailed();
                            progressDialog.dismiss();

                            return;
                        }


                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onSignupSuccess();
                        // onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 2500);
    }


    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);

        Toast.makeText(getApplicationContext(), "회원 등록 완료", Toast.LENGTH_SHORT).show();

        Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(i);
        finish();

    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Signup failed", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        if(!registerOk)
            return false;

        String name = _nameText.getText().toString();
        String id = _idText.getText().toString();
        String password = _passwordText.getText().toString();

        if (name.isEmpty() || name.length() < 2) {
            _nameText.setError("at least 2 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (id.isEmpty() || id.length() < 2
//                ||
//                !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
                ) {
            _idText.setError("enter a valid Id Number");
            valid = false;
        } else {
            _idText.setError(null);
        }

        if (password.isEmpty() || password.length() < 2 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }



    private class LoadJspForInput extends AsyncTask<Void, String, Void> {
        String id;
        String pwd;
        String name;
        String position;
        public LoadJspForInput(String id, String pwd, String name, String position) {
            super();
            this.id = id;
            this.pwd = pwd;
            this.name = name;
            this.position = position;

        }

        @Override
        protected Void doInBackground(Void... params) {
            postRegistInfo(id, pwd, name, position);
            return null;
        }
        protected void postRegistInfo(String id, String pwd, String name, String pos) {
            try {
                HttpClient client = new DefaultHttpClient();


                String postURL = pref.getValue("serverIP","http://192.168.0.14:8000/") +"registerUser/";


                ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

                params.add(new BasicNameValuePair("id", id));
                params.add(new BasicNameValuePair("pwd", pwd));
                params.add(new BasicNameValuePair("name", name));
                params.add(new BasicNameValuePair("position", pos));

                UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params,
                        HTTP.UTF_8);

                HttpPost post = new HttpPost(postURL);

                post.setEntity(ent);

                HttpResponse responsePOST = client.execute(post);

                HttpEntity resEntity = responsePOST.getEntity();

                if(resEntity != null) {
                    resultPost = EntityUtils.toString(resEntity);
                    Log.i("RESPONSE", resultPost);

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }




    private boolean JsonParsing(String str) {
        boolean res = false;

        try {
            JSONObject jObj = new JSONObject(resultPost);

            String result = jObj.getString("result");
            if(result.equals("ok"))
                registerOk = true;

            res = true;
        } catch (JSONException e) {
            e.printStackTrace();
            Log.i("NO-JSON","안들어왔니");
            res = false;

        }



        return res;
    }




}
