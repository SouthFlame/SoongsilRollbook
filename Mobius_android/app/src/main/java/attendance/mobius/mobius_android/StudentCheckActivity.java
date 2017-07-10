package attendance.mobius.mobius_android;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

public class StudentCheckActivity extends AppCompatActivity implements OrientationListener {

    private static Context CONTEXT;

    LoadJspForInput loadJspForInput;


    CustomPreference pref = new CustomPreference(this);

    String resultPost = "";
    int mId;
    String mMotion = "";
    String mAccess;
    String mIpAddr;
    String mResult ="";
    @BindView(R.id.motion_sensor_result)
    TextView _motionText;

    @BindView(R.id.student_lecture_name)
    TextView _lectureText;

    @BindView(R.id.send_stu_check_btn)
    Button _sendBtn;

    int selectedNum;




    @OnClick(R.id.eraser_stu_check__btn)
    void onEraser(View view) {
        if(mMotion.length() < 1)
            return;

        mMotion = mMotion.substring(0, mMotion.length()-1);
        _motionText.setText(mMotion);

    }



    @OnClick(R.id.send_stu_check_btn)
    void onSend(View view) {
        loadJspForInput = new LoadJspForInput(mId, mAccess, mIpAddr, mMotion);
        loadJspForInput.execute();

        checkUp();
    }

    @OnClick(R.id.cancel_stu_check_btn)
    void onCancel(View view) {
        Intent i = new Intent(StudentCheckActivity.this, LectureListActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_check);
        CONTEXT = this;
        ButterKnife.bind(this);

        mId = (int)pref.getValue("id",0);
        selectedNum = (int) pref.getValue("lectureChoiceNum",0);
        _lectureText.setText(pref.getValue(selectedNum+"lecName","안왔당"));
        mAccess = pref.getValue(selectedNum+"lecAccess","null");

        WifiManager wifiMgr = (WifiManager) getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
        int ip = wifiInfo.getIpAddress();
        mIpAddr = Formatter.formatIpAddress(ip);

    }






    protected void onResume() {
        super.onResume();
        if (OrientationManager.isSupported()) {
            OrientationManager.startListening(this);
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        if (OrientationManager.isListening()) {
            OrientationManager.stopListening();
        }
    }

    public static Context getContext() {
        return CONTEXT;
    }

    @Override
    public void onOrientationChanged(float azimuth,
                                     float pitch, float roll) {
//        ((TextView) findViewById(R.id.azimuth)).setText(
//                String.valueOf(azimuth));
//        ((TextView) findViewById(R.id.pitch)).setText(
//                String.valueOf(pitch));
//        ((TextView) findViewById(R.id.roll)).setText(
//                String.valueOf(roll));
    }


    @Override
    public void onBottomUp() {
//        Toast.makeText(this, "Bottom UP", Toast.LENGTH_SHORT).show();
        if(mMotion.length() > 10)
            return;
        mMotion += "D";
        _motionText.setText(mMotion);
    }

    @Override
    public void onLeftUp() {
//        Toast.makeText(this, "Left UP", Toast.LENGTH_SHORT).show();
        if(mMotion.length() > 10)
            return;
        mMotion += "L";
        _motionText.setText(mMotion);
    }

    @Override
    public void onRightUp() {
//        Toast.makeText(this, "Right UP", Toast.LENGTH_SHORT).show();
        if(mMotion.length() > 10)
            return;
        mMotion += "R";
        _motionText.setText(mMotion);
    }

    @Override
    public void onTopUp() {
//        Toast.makeText(this, "Top UP", Toast.LENGTH_SHORT).show();
        if(mMotion.length() > 10)
            return;
        mMotion += "U";
        _motionText.setText(mMotion);
    }










    public void checkUp() {

//        Log.d(TAG, "JoinUp");


        _sendBtn.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(StudentCheckActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Receiving Data...");
        progressDialog.show();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
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

                        if(mResult.equals("Y"))
                            Toast.makeText(getApplicationContext(), "출석 체크 완료", Toast.LENGTH_SHORT).show();
                        else if(mResult.equals("N"))
                            Toast.makeText(getApplicationContext(), "결석 입니다.", Toast.LENGTH_SHORT).show();
                        else if(mResult.equals("L"))
                            Toast.makeText(getApplicationContext(), "지각 입니다.", Toast.LENGTH_SHORT).show();
                        else if(mResult.equals("Out"))
                            Toast.makeText(getApplicationContext(), "범위 밖에 있습니다.", Toast.LENGTH_SHORT).show();
                        else if(mResult.equals("Incorrect"))
                            Toast.makeText(getApplicationContext(), "모션 불일치!!!.", Toast.LENGTH_SHORT).show();



                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onSignupSuccess();
                        // onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 2500);
    }


    public void onSignupSuccess() {
        _sendBtn.setEnabled(true);
        setResult(RESULT_OK, null);

//        Toast.makeText(getApplicationContext(), "출석 체크 완료", Toast.LENGTH_SHORT).show();

        finish();

        Intent i = new Intent(StudentCheckActivity.this,LectureListActivity.class);
        startActivity(i);

    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Error Occured", Toast.LENGTH_LONG).show();

        _sendBtn.setEnabled(true);
    }

    public boolean validate() {
        if(mResult.equals(""))
            return false;

        return true;
    }


    private class LoadJspForInput extends AsyncTask<Void, String, Void> {
        int id;
        String access;
        String ipAddr;
        String motion;
        public LoadJspForInput(int id, String access, String ipAddr,String motion) {
            super();
            this.id = id;
            this.access = access;
            this.ipAddr = ipAddr;
            this.motion = motion;

        }

        @Override
        protected Void doInBackground(Void... params) {
            startCheck(id, access, ipAddr, motion);
            return null;
        }
        protected void startCheck(int id,  String access, String ipAddr, String motion) {
            try {
                HttpClient client = new DefaultHttpClient();


                String postURL = pref.getValue("serverIP","http://192.168.0.14:8000/") +"start_attendanceCheck/";

                ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

                params.add(new BasicNameValuePair("st_id", String.valueOf(id)));
                params.add(new BasicNameValuePair("lec_access", access));
                params.add(new BasicNameValuePair("st_ipAddr", ipAddr));
                params.add(new BasicNameValuePair("motion", motion));

                Log.i("ZXCVBN_ID",String.valueOf(id));
                Log.i("ZXCVBN_ACCESS",access);

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

            mResult = jObj.getString("result");

            res = true;
        } catch (JSONException e) {
            e.printStackTrace();
            Log.i("NO-JSON","안들어왔니");
            res = false;

        }



        return res;
    }







}
