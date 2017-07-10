package attendance.mobius.mobius_android;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LectureListActivity extends AppCompatActivity {

    GlobalStorage globalStorage = (GlobalStorage)getApplication();
    CustomPreference pref = new CustomPreference(this);
    String mUserName;


    @BindView(R.id.lecture_list_btn_new_lecture)
    ImageButton _newBtn;

    @BindView(R.id.lecture_list_username)
    TextView _nameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture_list);

        ButterKnife.bind(this);

        _nameText.setText(pref.getValue("userName","에러")+"님");

//        name.setText(  ((GlobalStorage)getApplication()).getUserInfo().getUserName());

        _newBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                if(pref.getValue("position","pro").equals("pro")) {
                    Intent i = new Intent(LectureListActivity.this,ProfessorCreateLectureActivity.class);
                    startActivity(i);
                }
                else {
                    Intent i = new Intent(LectureListActivity.this,StudentJoinLectureActivity.class);
                    startActivity(i);
                }
            }
        });


        ListView listview;
        ListViewAdapter adapter;

        adapter = new ListViewAdapter();

        listview = (ListView) findViewById(R.id.lecture_listview);
        listview.setAdapter(adapter);

//        _nameText.setText(String.valueOf(pref.getValue("lectureCount",0)));

//        Toast.makeText(getBaseContext(), String.valueOf((int)pref.getValue("lectureCount",0)), Toast.LENGTH_LONG).show();

        for(int i = 0; i < pref.getValue("lectureCount",0); i++) {
            adapter.addItem(pref.getValue(i+"lecName",""), pref.getValue(i+"lecRoom",""), pref.getValue(i+"lecTime",""));
        }

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int pos, long id) {
                //get item
                ListViewItem item = (ListViewItem) parent.getItemAtPosition(pos);

                String name = item.getLectureName();
                String room = item.getLectureRoom();
                String time = item.getLectureTime();

                pref.put("lectureChoiceNum", pos);

                Intent i;
                if(pref.getValue("position","").equals("pro"))
                    i = new Intent(LectureListActivity.this, ProfessorCheckActivity.class);
                else
                    i = new Intent(LectureListActivity.this, StudentCheckActivity.class);

                startActivity(i);

            }
        });

    }


}
