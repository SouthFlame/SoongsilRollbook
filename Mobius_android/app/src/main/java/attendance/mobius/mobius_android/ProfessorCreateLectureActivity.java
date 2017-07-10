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

public class ProfessorCreateLectureActivity extends AppCompatActivity {

    LoadJspForInput loadJspForInput;
    CustomPreference pref = new CustomPreference(this);
    ArrayList<LectureInfo> lectureInfos = new ArrayList<LectureInfo>();
    LectureInfo lectureInfo;
    int lectureCount;

    String resultPost;
    boolean createOk;

    @BindView(R.id.create_lecture_ok_btn)
    Button _ceateButton;

    @BindView(R.id.create_lecture_name)
    EditText _lectureName;

    @BindView(R.id.create_lecture_room)
    EditText _lectureRoom;

    @BindView(R.id.create_lecture_time)
    EditText _lectureTime;


    @OnClick(R.id.create_lecture_ok_btn)
    void clickCreate(View view) {
        // TODO call server...
        // Start the Signup activity
        overridePendingTransition(0, 1);


        loadJspForInput = new LoadJspForInput(
                (int)pref.getValue("id",0) ,
                _lectureName.getText().toString(),
                _lectureRoom.getText().toString(),
                _lectureTime.getText().toString()
                );
        loadJspForInput.execute();
//
//        try {
//            Thread.sleep(200);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        JsonParsing("");
//
//        if(createOk) {
//
//            Toast.makeText(getApplicationContext(), "Create Click", Toast.LENGTH_LONG).show();
//
//            finish();
//            Intent i = new Intent(ProfessorCreateLectureActivity.this,LectureListActivity.class);
//            startActivity(i);
//        }

        createUp();
    }


    @OnClick(R.id.create_lecture_cancel_btn)
    void clickCancel(View view) {
        // TODO call server...
        // Start the Signup activity
        overridePendingTransition(0, 1);

//        Toast.makeText(getApplicationContext(), "Cancel Click", Toast.LENGTH_LONG).show();

        finish();
        Intent i = new Intent(ProfessorCreateLectureActivity.this,LectureListActivity.class);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {

        finish();
        Intent i = new Intent(ProfessorCreateLectureActivity.this,LectureListActivity.class);
        startActivity(i);

        super.onBackPressed();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_create_lecture);

        ButterKnife.bind(this);

        lectureCount = (int)pref.getValue("lectureCount",0);



    }



    public void createUp() {

//        Log.d(TAG, "JoinUp");


        _ceateButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(ProfessorCreateLectureActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Lecture...");
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
        _ceateButton.setEnabled(true);
        setResult(RESULT_OK, null);

        Toast.makeText(getApplicationContext(), "수업 생성 완료", Toast.LENGTH_SHORT).show();

        finish();

        Intent i = new Intent(ProfessorCreateLectureActivity.this,LectureListActivity.class);
        startActivity(i);

    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Error Occured", Toast.LENGTH_LONG).show();

        _ceateButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        if(!createOk)
            return false;

        String name = _lectureName.getText().toString();
        String room = _lectureRoom.getText().toString();
        String time = _lectureTime.getText().toString();


        if(name.isEmpty() || name.length() <2) {
            _lectureName.setError("at least 2 characters");
            valid = false;
        } else {
            _lectureName.setError(null);
        }

        if(room.isEmpty() || room.length() <2) {
            _lectureRoom.setError("at least 2 characters");
            valid = false;
        } else {
            _lectureRoom.setError(null);
        }

        if(time.isEmpty() || time.length() <2) {
            _lectureTime.setError("at least 2 characters");
            valid = false;
        } else {
            _lectureTime.setError(null);
        }

        return valid;

    }






    private class LoadJspForInput extends AsyncTask<Void, String, Void> {
        int id;
        String name;
        String room;
        String time;

        public LoadJspForInput(int id,String name, String room, String time) {
            super();
            this.id = id;
            this.name = name;
            this.room = room;
            this.time = time;

        }

        @Override
        protected Void doInBackground(Void... params) {
            createLecturePost(id, name, room, time);
            return null;
        }
        protected void createLecturePost(int id, String name, String room, String time) {
            try {
                HttpClient client = new DefaultHttpClient();


                String postURL = pref.getValue("serverIP","http://192.168.0.14:8000/") +"pro_add_subject/";


                ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("id", String.valueOf(id)));
                params.add(new BasicNameValuePair("lec_name", name));
                params.add(new BasicNameValuePair("lec_room", room));
                params.add(new BasicNameValuePair("lec_time", time));

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
                createOk = true;
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
