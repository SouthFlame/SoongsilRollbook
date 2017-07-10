package attendance.mobius.mobius_android;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.lylc.widget.circularprogressbar.CircularProgressBar;

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
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.protocol.HTTP;
import cz.msebera.android.httpclient.util.EntityUtils;

public class ProfessorCheckActivity extends AppCompatActivity {

    CustomPreference pref = new CustomPreference(this);
    LoadJspForInput loadJspForInput;

    String resultPost = "";
    int mId;
    String mMotion = "";
    String mAccess;
    int cnt = 0;
    int intervarl = 0;

    String mIpAddr;

    int minutes = 2;
    int seconds = 0;
    int selectedNum;

    @BindView(R.id.professor_lecture_access)
    TextView _accessText;

    @BindView(R.id.selected_time)
    TextView _selectd_time;
    @BindView(R.id.professor_lecture_name)
    TextView _lectureName;
    @BindView(R.id.spinner_minute)
    Spinner _minuteSpinner;
    @BindView(R.id.spinner_second)
    Spinner _secondSpinner;
    @BindView(R.id.btn_start)
    Button _btnStart;
    @BindView(R.id.selected_motion)
    TextView _motionText;

    @BindView(R.id.left_btn)
    ImageButton _btnLeft;

    @BindView(R.id.right_btn)
    ImageButton _btnRight;

    @BindView(R.id.up_btn)
    ImageButton _btnUp;

    @BindView(R.id.down_btn)
    ImageButton _btnDown;

    @OnClick(R.id.left_btn)
    void onLeft(View view) {
        if(mMotion.length() > 10)
            return;
        mMotion += "L";
        _motionText.setText(mMotion);
        overridePendingTransition(0, 1);

    }

    @OnClick(R.id.right_btn)
    void onRight(View view) {
        if(mMotion.length() > 10)
            return;
        mMotion += "R";
        _motionText.setText(mMotion);

    }

    @OnClick(R.id.up_btn)
    void onUp(View view) {
        if(mMotion.length() > 10)
            return;
        mMotion += "U";
        _motionText.setText(mMotion);

    }

    @OnClick(R.id.down_btn)
    void onDown(View view) {
        if(mMotion.length() > 10)
            return;
        mMotion += "D";
        _motionText.setText(mMotion);

    }

    @OnClick(R.id.eraser_btn)
    void onEraser(View view) {
        if(mMotion.length() < 1)
            return;

        mMotion = mMotion.substring(0, mMotion.length()-1);
        _motionText.setText(mMotion);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_check);
        ButterKnife.bind(this);


        mId = (int)pref.getValue("id",0);
        selectedNum = (int) pref.getValue("lectureChoiceNum",0);
        _lectureName.setText(pref.getValue(selectedNum+"lecName","안왔당"));
        mAccess = pref.getValue(selectedNum+"lecAccess","null");
        _accessText.setText(mAccess);
        WifiManager wifiMgr = (WifiManager) getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
        int ip = wifiInfo.getIpAddress();
        mIpAddr = Formatter.formatIpAddress(ip);

        Log.i("==IPADDR==", mIpAddr);

        ArrayAdapter minuteAdapter = ArrayAdapter.createFromResource(this, R.array.minute,
                android.R.layout.simple_spinner_dropdown_item);
        _minuteSpinner.setAdapter(minuteAdapter);

        ArrayAdapter secondAdapter = ArrayAdapter.createFromResource(this, R.array.seconds,
                android.R.layout.simple_spinner_dropdown_item);
        _secondSpinner.setAdapter(secondAdapter);


        _minuteSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                minutes = Integer.parseInt( _minuteSpinner.getSelectedItem().toString());
                _selectd_time.setText(minutes+"분" + seconds+ "초");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                minutes = 3;
                _selectd_time.setText(minutes+"분" + seconds+ "초");

            }
        });

        _secondSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                seconds = Integer.parseInt( _secondSpinner.getSelectedItem().toString());
                _selectd_time.setText(minutes+"분" + seconds+ "초");
                _selectd_time.setText(minutes+"분" + seconds+ "초");

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                seconds = 0;
            }
        });




        _btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                loadJspForInput = new LoadJspForInput(mId, mMotion, minutes*60 + seconds, mAccess, mIpAddr);
                loadJspForInput.execute();


                final CircularProgressBar c2 = (CircularProgressBar) findViewById(R.id.circularprogressbar2);
                c2.setSubTitle("출석중");
//                c2.animateProgressTo( (minutes*60 + seconds), 0, (minutes*60 + seconds) * 1000
                    c2.animateProgressTo( 0 , 100, (minutes*60 + seconds) * 1000
                        , new CircularProgressBar.ProgressAnimationListener() {

                            @Override
                            public void onAnimationStart() {
//                                setProgress( (minutes*60 + seconds)/360 * 100 );
//                                setProgress( 100 );
                                c2.setTitle("출석체크 진행중");
                                c2.setSubTitle("ing..");

                            }

                            @Override
                            public void onAnimationProgress(int progress) {
//                                intervarl++;

//                                if( intervarl % ( 100 / (minutes*60 + seconds)) == 0 )
//                                    cnt++;

//                                c2.setTitle((minutes*60 + seconds)-cnt + "초 남음");
                                c2.setTitle( progress + "%");







                            }

                            @Override
                            public void onAnimationFinish() {
                                c2.setTitle("출석체크 종료");

                                c2.setSubTitle("done");
                            }
                        });
            }
        });




    }

    private class LoadJspForInput extends AsyncTask<Void, String, Void> {
        int id;
        String motion;
        int limitTime;
        String access;
        String ipAddr;
        public LoadJspForInput(int id, String motion, int limitTime, String access, String ipAddr) {
            super();
            this.id = id;
            this.motion = motion;
            this.limitTime = limitTime;
            this.access = access;
            this.ipAddr = ipAddr;

        }

        @Override
        protected Void doInBackground(Void... params) {
            startCheck(id, motion, limitTime, access, ipAddr);
            return null;
        }
        protected void startCheck(int id, String motion, int limitTime, String access, String ipAddr) {
            try {
                HttpClient client = new DefaultHttpClient();


                String postURL =pref.getValue("serverIP","http://192.168.0.14:8000/") +"start_timecnt/";


                ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

                params.add(new BasicNameValuePair("id", String.valueOf(id)));
                params.add(new BasicNameValuePair("motion", motion));
                params.add(new BasicNameValuePair("limitsec", String.valueOf(limitTime)));
                params.add(new BasicNameValuePair("access", access));
                params.add(new BasicNameValuePair("pro_ipAddr", ipAddr));

                Log.i("ZXCVBN_ID",String.valueOf(id));
                Log.i("ZXCVBN_MOTION",motion);
                Log.i("ZXCVBN_LIMITTIME",String.valueOf(limitTime));
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
                    JsonParsing("");

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

            res = true;
        } catch (JSONException e) {
            e.printStackTrace();
            Log.i("NO-JSON","안들어왔니");
            res = false;

        }



        return res;
    }













}
