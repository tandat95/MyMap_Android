package adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.vbd.mapexam.R;

import java.util.ArrayList;

import model.SearchResultObject;

/**
 * Created by tandat on 10/28/2017.
 */

public class SearchResultAdapter extends ArrayAdapter {
    private Context context;
    private int resource;
    private ArrayList<SearchResultObject> arrayList;


    public SearchResultAdapter(@NonNull Context context, int resource, @NonNull ArrayList<SearchResultObject> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.arrayList = objects;

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.search_result_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.txtLocationName = (TextView) convertView.findViewById(R.id.textViewLocationName);
            viewHolder.txtLocationAdress = (TextView) convertView.findViewById(R.id.textViewLocationAdress);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        SearchResultObject searchResultObject = arrayList.get(position);
        viewHolder.txtLocationName.setText(searchResultObject.getName());
        viewHolder.txtLocationAdress.setText(searchResultObject.getAdress());

        return convertView;
    }
    public class ViewHolder {
        TextView txtLocationName, txtLocationAdress;
    }
}
