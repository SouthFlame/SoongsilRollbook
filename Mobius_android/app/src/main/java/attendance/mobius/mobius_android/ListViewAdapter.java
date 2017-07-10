package attendance.mobius.mobius_android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by user on 2016-11-12.
 */

public class ListViewAdapter extends BaseAdapter{

    private ArrayList<ListViewItem> listViewItemList = new ArrayList<ListViewItem>();

    public ListViewAdapter() {

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item, parent, false);
        }

        TextView nameView = (TextView)convertView.findViewById(R.id.listview_lecture_name);
        TextView roomView = (TextView)convertView.findViewById(R.id.listview_lecture_room);
        TextView timeView = (TextView)convertView.findViewById(R.id.listview_lecture_time);

        ListViewItem listViewItem = listViewItemList.get(position);

        nameView.setText(listViewItem.getLectureName());
        roomView.setText(listViewItem.getLectureRoom());
        timeView.setText(listViewItem.getLectureTime());

        return convertView;
    }


    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    @Override
    public Object getItem(int pos) {
        return listViewItemList.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }


    public void addItem(String name, String room, String time) {
        ListViewItem item = new ListViewItem();

        item.setLectureName(name);
        item.setLectureRoom(room);
        item.setLectureTime(time);

        listViewItemList.add(item);
    }

}
