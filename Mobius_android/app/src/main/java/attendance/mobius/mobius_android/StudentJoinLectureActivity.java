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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.protocol.HTTP;
import cz.msebera.android.httpclient.util.EntityUtils;

import static android.content.Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP;

public class StudentJoinLectureActivity extends AppCompatActivity {
    LoadJspForInput loadJspForInput;
    private static final String TAG = "StudentJoinLecture";
    String resultPost;
    Boolean joinOk = false;
    CustomPreference pref = new CustomPreference(this);
    int lectureCount;
    ArrayList<LectureInfo> lectureInfos = new ArrayList<LectureInfo>();
    LectureInfo lectureInfo;

    @BindView(R.id.student_join_access_num)
    EditText _accessText;

    @BindView(R.id.join_lecture_ok_btn)
    Button _joinBtn;

    @BindView(R.id.join_lecture_cancel_btn)
    Button _cancelBtn;

    @OnClick(R.id.join_lecture_ok_btn)
    void clickOk(View view) {

        loadJspForInput = new LoadJspForInput(
                (int)pref.getValue("id",0),
                _accessText.getText().toString());

        loadJspForInput.execute();


//        Toast.makeText(getBaseContext(), String.valueOf((int)pref.getValue("lectureCount",0)), Toast.LENGTH_LONG).show();

        joinup();

    }

    @OnClick(R.id.join_lecture_cancel_btn)
    void clickCancel(View view) {
        finish();
        Intent i = new Intent(StudentJoinLectureActivity.this,LectureListActivity.class);
        startActivity(i);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_join_lecture);


        ButterKnife.bind(this);

        lectureCount = (int)pref.getValue("lectureCount",0);

        for(int i = 0; i < pref.getValue("lectureCount",0); i++) {
            Log.i(i+"ndLECTUREIDXXX",String.valueOf( pref.getValue(i+"lecId",0)));
            Log.i(i+"ndLECTURENAMEXXX",pref.getValue(i+"lecName",""));
            Log.i(i+"ndLECTURENACCESSXXX",pref.getValue(i+"lecAccess",""));
            Log.i(i+"ndLECTUREROOMXXX",pref.getValue(i+"lecRoom",""));
            Log.i(i+"ndLECTURETIMEXXX",pref.getValue(i+"lecTime",""));

        }

    }

    @Override
    public void onBackPressed() {
        finish();
        Intent i = new Intent(StudentJoinLectureActivity.this,LectureListActivity.class);
        startActivity(i);
        super.onBackPressed();
    }

    public void joinup() {

        Log.d(TAG, "JoinUp");


        _joinBtn.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(StudentJoinLectureActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Joining Lecture...");
        progressDialog.show();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                if(!JsonParsing("")) {
//                    onSignupFailed();
                    JsonParsing("");
//                    return;
//                }
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

                        pref.put(lectureCount+"lecId", lectureInfo.getLecId());
                        pref.put(lectureCount+"lecName", lectureInfo.getLecName());
                        pref.put(lectureCount+"lecAccess", lectureInfo.getLecAccess());
                        pref.put(lectureCount+"lecRoom", lectureInfo.getLecRoom());
                        pref.put(lectureCount+"lecTime", lectureInfo.getLecTime());


                        pref.put("lectureCount",lectureCount+1);

                        for(int i = 0; i < pref.getValue("lectureCount",0); i++) {
                            Log.i(i+"ndLECTUREIDXXX",String.valueOf( pref.getValue(i+"lecId",0)));
                            Log.i(i+"ndLECTURENAMEXXX",pref.getValue(i+"lecName",""));
                            Log.i(i+"ndLECTURENACCESSXXX",pref.getValue(i+"lecAccess",""));
                            Log.i(i+"ndLECTUREROOMXXX",pref.getValue(i+"lecRoom",""));
                            Log.i(i+"ndLECTURETIMEXXX",pref.getValue(i+"lecTime",""));

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
        _joinBtn.setEnabled(true);
        setResult(RESULT_OK, null);

        Toast.makeText(getApplicationContext(), "수업 참가 완료", Toast.LENGTH_SHORT).show();

        finish();

        Intent i = new Intent(StudentJoinLectureActivity.this,LectureListActivity.class);
        startActivity(i);

    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Error Occured", Toast.LENGTH_LONG).show();

        _joinBtn.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        if(!joinOk)
            return false;

        String access = _accessText.getText().toString();

        if(access.isEmpty() || access.length() <2) {
            _accessText.setError("at least 2 characters");
            valid = false;
        } else {
            _accessText.setError(null);
        }



        return valid;
    }





    private class LoadJspForInput extends AsyncTask<Void, String, Void> {
        int id;
        String accessNum;

        public LoadJspForInput(int id, String accessNum) {
            super();
            this.id = id;
            this.accessNum = accessNum;

        }

        @Override
        protected Void doInBackground(Void... params) {
            createLecturePost(id, accessNum);
            return null;
        }
        protected void createLecturePost(int id, String accessNum) {
            try {
                HttpClient client = new DefaultHttpClient();


                String postURL = pref.getValue("serverIP","http://192.168.0.14:8000/") +"stu_add_subject/";

                ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("id", String.valueOf(id)));

                params.add(new BasicNameValuePair("access", accessNum));

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
            Log.i("lec_result_fxxx", result);

            if(result.equals("ok"))
                joinOk = true;

            if(joinOk)
                Log.i("fxxx_JOINOK","OKOKOK");

            JSONArray lectures = jObj.getJSONArray("lecture");

            for(int i = 0; i < lectures.length(); i++) {

                int lec_id = lectures.getJSONObject(i).getInt("lec_id");
                String lec_name = lectures.getJSONObject(i).getString("lec_name").toString();
                String lec_access = lectures.getJSONObject(i).getString("lec_access").toString();
                String lec_room = lectures.getJSONObject(i).getString("lec_room").toString();
                String lec_time= lectures.getJSONObject(i).getString("lec_time").toString();

                Log.i("lec_id_fxxx", String.valueOf(lec_id));
                Log.i("lec_name_fxxx", lec_name);
                Log.i("lec_access_fxxx", lec_access);


                lectureInfos.add(new LectureInfo(lec_id, lec_name, lec_access, lec_room, lec_time));
                lectureInfo = new LectureInfo(lec_id, lec_name, lec_access, lec_room, lec_time);
            }


            res = true;
        } catch (JSONException e) {
            e.printStackTrace();
            Log.i("NO-JSON","안들어왔니");
            res = false;

        }

        return res;
    }



}
