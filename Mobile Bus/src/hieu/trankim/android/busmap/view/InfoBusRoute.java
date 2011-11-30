package hieu.trankim.android.busmap.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.greengar.hackathon.app.R;

public class InfoBusRoute extends LinearLayout {

	private TextView txtTime, txtPrice, txtDistance;
	public InfoBusRoute(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		View view = LayoutInflater.from(context).inflate(R.layout.info_routes, this);
		txtTime = (TextView) view.findViewById(R.id.txtTime);
		txtPrice = (TextView) view.findViewById(R.id.txtPrice);
		txtDistance = (TextView) view.findViewById(R.id.txtDistance);
	}
	
	public void setValue(String time, String price, String distance){
		txtTime.setText(time);
		txtPrice.setText(price);
		txtDistance.setText(distance);
		
	}

}
