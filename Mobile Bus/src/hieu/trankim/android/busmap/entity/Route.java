package hieu.trankim.android.busmap.entity;

import java.io.Serializable;
import java.util.ArrayList;

public class Route implements Serializable{
	private static final long serialVersionUID = 1L;
	private String price;
	private double len;
	private String to;
	private ArrayList<Step> steps;
	private String duration;
	private String trans;
	public int isNull = -1;
	
	public  Route(){
		price = "";
		len = 0;
		to = "";
		steps = new ArrayList<Step>();
		duration = "";
		trans = "";
	}
	
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public double getLen() {
		return len;
	}
	public void setLen(double len) {
		this.len = len;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public ArrayList<Step> getSteps() {
		return steps;
	}
	public void setSteps(ArrayList<Step> steps) {
		this.steps = steps;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getDuration() {
		return duration;
	}

	public void setTrans(String trans) {
		this.trans = trans;
	}

	public String getTrans() {
		return trans;
	}
}
