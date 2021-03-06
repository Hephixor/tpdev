package com.indahouse.skylab.calendarupmc.classes;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.indahouse.skylab.calendarupmc.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CheckboxAdapter extends ArrayAdapter {
    Context context;
    List<Boolean> checkboxState;
    List<String> checkboxItems;
    HashMap<String,String> keysToUrls;
    SharedPreferences sharedPreferences;

    public CheckboxAdapter(Context context, HashMap<String,String> keys, List<String> resource) {

        
        super(context, R.layout.form_checkbox_item, resource);
        this.context = context;
        this.checkboxItems = resource;
        this.keysToUrls = keys;
        this.sharedPreferences = context.getApplicationContext().getSharedPreferences(context.getResources().getString(R.string.shared_pref),Context.MODE_PRIVATE);
        this.checkboxState = stateFromPrefs(resource);

    }


    //Check if a master is checked
    private ArrayList<Boolean> stateFromPrefs(List<String> resource){
        ArrayList<Boolean> tmpState = new ArrayList<Boolean>(Collections.nCopies(resource.size(), false));
        HashMap<String,?> map = (HashMap)sharedPreferences.getAll();
        for (String str : map.keySet()) {
            if(map.get(str).equals("true")){tmpState.set(Integer.valueOf(str),true); }
        }
        return tmpState;
    }

    //Standard method
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(convertView==null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.form_checkbox_item, parent, false);
        }

        TextView textView = (TextView) convertView.findViewById(R.id.textView1);
        final CheckBox cb = (CheckBox) convertView.findViewById(R.id.checkBox1);

        //Toggling checkbox and storing value in SharedPrefs
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setChecked(position, !checkboxState.get(position));
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(String.valueOf(position),String.valueOf(checkboxState.get(position)));
                editor.commit();
                String key = String.valueOf(position);
                String val = sharedPreferences.getString(key,"defaultValue");
            }
        });
        textView.setText(checkboxItems.get(position));
        cb.setChecked(checkboxState.get(position));
        return convertView;
    }

    //Toggle state of a checkbox
    private void setChecked( int position, boolean state) {
        checkboxState.set(position, state);
        notifyDataSetChanged();
    }

    //Get states of checkboxes
    public List<Boolean> getBools(){
        return checkboxState;
    }

    //Get keys of list
    public List<String> getCheckboxItems() { return checkboxItems; }

    //Check if the list contains a checked element
    public boolean hasCheck(){
        for (Boolean b: checkboxState) {
            if(b.equals(true)){
                return true;
            }
        }
        return false;
    }
}