/**
 * 
 */
package com.greengar.hackathon.app.entity;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.greengar.hackathon.app.R;

/**
 * @author DavidTroc
 *
 */
public class RoutesAdapter extends BaseAdapter {

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	private Context context;
	private ArrayList<Step> steps;
	
	public RoutesAdapter(Context context, ArrayList<Step> steps){
		this.context = context;
		this.steps = steps;
	}
	public int getCount() {
		// TODO Auto-generated method stub
		return steps.size();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	public Object getItem(int index) {
		// TODO Auto-generated method stub
		return steps.get(index);
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	public View getView(int position, View convertView, ViewGroup parent){
		// TODO Auto-generated method stub
		View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.bus_routes_listview_item, null);
        }
        
        Step step = steps.get(position);
        if(step != null){
        	TextView txt = (TextView)v.findViewById(R.id.txt);
        	txt.setText("");
        	TextView txt1 = (TextView) v.findViewById(R.id.txt1);
        	txt1.setText("");
        	TextView txt2 = (TextView) v.findViewById(R.id.txt2);
        	txt2.setVisibility(View.GONE);
        	TextView txt3 = (TextView) v.findViewById(R.id.txt3);
        	txt3.setVisibility(View.GONE);
        	ImageView image = (ImageView) v.findViewById(R.id.imgCar); 
        	TextView txtWaitiing = (TextView) v.findViewById(R.id.txtWaiting);
        	txtWaitiing.setVisibility(View.GONE);
        	TextView txtWaitiing1 = (TextView) v.findViewById(R.id.txtWaiting1);
        	txtWaitiing1.setVisibility(View.GONE);
        	if(step.getTrans().compareTo("bus") == 0){
        		txt.setText("Xe buýt");
        		image.setBackgroundResource(R.drawable.bus);
        		txtWaitiing.setVisibility(View.VISIBLE);
        		txtWaitiing.setText(step.getWaiting() + " chờ");        		
        		txtWaitiing1.setVisibility(View.VISIBLE);
        		txtWaitiing1.setText("Đi xe");
        		txt1.setText(step.getTo());
        		String[] str = step.getNote().split("<br/>");
        		txt2.setVisibility(View.VISIBLE);
        		txt2.setText(str[0]);
        		if(str.length > 1){
        			txt3.setVisibility(View.VISIBLE);
        			txt3.setText(str[1]);
        		}
        	}else{
        		txt.setText("Đi bộ");
        		image.setBackgroundResource(R.drawable.people);
        		txt1.setText(String.format("%.2f km", step.getLen() / 1000));
        		txt2.setVisibility(View.VISIBLE);
        		txt2.setText(step.getTo());
        		txt3.setVisibility(View.VISIBLE);
        		txt3.setText(step.getNote());
        	}
        }
        
        return v;
	}

}
