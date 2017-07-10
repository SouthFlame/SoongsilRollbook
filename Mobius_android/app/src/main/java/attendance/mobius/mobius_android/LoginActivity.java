package attendance.mobius.mobius_android;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DecompressingHttpClient;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.message.BufferedHeader;
import cz.msebera.android.httpclient.protocol.HTTP;
import cz.msebera.android.httpclient.util.EntityUtils;

import static java.lang.Thread.sleep;


public class LoginActivity extends AppCompatActivity {

    GlobalStorage globalStorage = ((GlobalStorage)getApplication());


    LoadJspForInput loadJspForInput;
    CustomPreference pref = new CustomPreference(this);
    String resultPost = "";
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    boolean loginOk = false;
    String param;
    String sign;
    int lectureCount;
    UserInfo userInfo = new UserInfo();
    ArrayList<LectureInfo> lectureInfos = new ArrayList<LectureInfo>();

    @BindView(R.id.input_email)
    EditText _idText;
    @BindView(R.id.input_password)
    EditText _passwordText;
    @BindView(R.id.btn_login)
    Button _loginButton;
    @BindView(R.id.link_signup)
    TextView _signupLink;

    @OnClick(R.id.link_signup)
    void clickSignup(View view) {
        // TODO call server...
        // Start the Signup activity

        Toast.makeText(getApplicationContext(), "Signup Click", Toast.LENGTH_LONG).show();

        Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivityForResult(intent, REQUEST_SIGNUP);
        finish();
//        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    @OnClick(R.id.btn_login)
    void clickLogin(View view) {

        boolean isProfessor = true;

        String id = _idText.getText().toString();
        String pwd = _passwordText.getText().toString();

        loadJspForInput = new LoadJspForInput(id, pwd);
        loadJspForInput.execute();
//        Toast.makeText(getBaseContext(), resultPost, Toast.LENGTH_LONG).show();


//
//        try {
//            Thread.sleep(300);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//



        login();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        pref.put("serverIP","http://192.168.0.14:8000/");

        Log.i("==INITDATA==",
                pref.getValue("id","NULL")
                );

    }



    public void login() {
        Log.d(TAG, "Login");



        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this
//                , R.style.AppTheme_Dark_Dialog
        );

        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                if(!JsonParsing("")) {
//                    onLoginFailed();
//
//                    return;
//                }

                JsonParsing("");

            }
        }, 1500);


        // TODO: Implement your own authentication logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed

                        if (!validate()) {
                            onLoginFailed();
                            progressDialog.dismiss();

                            return;
                        }


                        for (int i = 0; i < lectureCount; i++) {
                            pref.put(i+"lecId", lectureInfos.get(i).getLecId());
                            pref.put(i+"lecName", lectureInfos.get(i).getLecName());
                            pref.put(i+"lecAccess", lectureInfos.get(i).getLecAccess());
                            pref.put(i+"lecRoom", lectureInfos.get(i).getLecRoom());
                            pref.put(i+"lecTime", lectureInfos.get(i).getLecTime());
                        }

                        pref.put("id",userInfo.getUserId());
                        pref.put("userId",userInfo.getUserId());

                        pref.put("userName",userInfo.getUserName());

                        if(userInfo.getIsPro())
                            pref.put("position","pro");
                        else
                            pref.put("position","stu");

                        pref.put("lectureCount",lectureCount);

                        onLoginSuccess();
                        // onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 2500);




    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);

        Intent intent = new Intent(getApplicationContext(), LectureListActivity.class);
        startActivityForResult(intent, REQUEST_SIGNUP);
        finish();
        overridePendingTransition(0, 1);

    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        if(loginOk == false)
            return false;

        String email = _idText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty()
//                || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
                )
        {
            _idText.setError("enter a valid ID");
            valid = false;
        } else {
            _idText.setError(null);
        }

        if (password.isEmpty() || password.length() < 1 || password.length() > 10) {
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

        public LoadJspForInput(String id, String pwd) {
            super();
            this.id = id;
            this.pwd = pwd;
        }

        @Override
        protected Void doInBackground(Void... params) {
            checkValidId(id,pwd);
            return null;
        }
        protected void checkValidId(String id, String pwd) {
            try {
                HttpClient client = new DecompressingHttpClient();


                String postURL = pref.getValue("serverIP","http://192.168.0.14:8000/") + "test/";

                ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

                params.add(new BasicNameValuePair("id", id));
                params.add(new BasicNameValuePair("pwd", pwd));

                UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params,
                        HTTP.UTF_8);

                HttpPost post = new HttpPost(postURL);

                post.setEntity(ent);

                HttpResponse responsePOST = client.execute(post);

                HttpEntity resEntity = responsePOST.getEntity();

                if(resEntity != null) {
                    resultPost = EntityUtils.toString(resEntity, HTTP.UTF_8);
                    Log.i("RESPONSE", resultPost);





                } else {

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

            param = jObj.getString("param");
            sign= jObj.getString("sign");
            userInfo.setUserId(jObj.getInt("user_id"));
            userInfo.setUserName(jObj.getString("user_name"));

            if(param.equals("ok"))
                loginOk = true;

            if(sign.equals("stu"))
                userInfo.setIsPro(false);
            else
                userInfo.setIsPro(true);

            Log.i("username_deok", userInfo.getUserName());
            Log.i("param_deok", param);
            Log.i("sign_deok", sign);


            JSONArray lectures = jObj.getJSONArray("lecture");
            lectureCount = lectures.length();
            for(int i = 0; i < lectures.length(); i++) {

                int lec_id = lectures.getJSONObject(i).getInt("lec_id");
                String lec_name = lectures.getJSONObject(i).getString("lec_name").toString();
                String lec_access = lectures.getJSONObject(i).getString("lec_access").toString();
                String lec_room = lectures.getJSONObject(i).getString("lec_room").toString();
                String lec_time= lectures.getJSONObject(i).getString("lec_time").toString();

                Log.i("lec_id_deok", String.valueOf(lec_id));
                Log.i("lec_name_deok", lec_name);
                Log.i("lec_access_deok", lec_access);


                lectureInfos.add(new LectureInfo(lec_id, lec_name, lec_access, lec_room, lec_time));

            }
            ((GlobalStorage)getApplication()).setLectureInfos(lectureInfos);
            ((GlobalStorage)getApplication()).setUserInfo(userInfo);
//            globalStorage.setUserInfo(userInfo);
//            globalStorage.setLectureInfos(lectureInfos);

            res = true;
        } catch (JSONException e) {
            e.printStackTrace();
            Log.i("NO-JSON","안들어왔니");
            res = false;

        }





        return res;
    }



}


