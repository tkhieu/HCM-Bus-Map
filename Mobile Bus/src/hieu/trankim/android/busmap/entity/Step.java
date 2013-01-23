package hieu.trankim.android.busmap.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Step implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3747959404981839602L;
	private String direction;
	private double price;
	private double len;
	private String note;
	private String to;
	private String waiting;
	private String trans;
	private ArrayList<ArrayList<LatLng>> locations;

	public Step(){
		direction = "";
		price = 0;
		len = 0;
		note = "";
		to = "";
		waiting = "";
		locations = new ArrayList<ArrayList<LatLng>>();
		trans = "";
	}
	
	public String getDirection() {
		return direction;
	}
	public void setDirection(String direction) {
		this.direction = direction;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public double getLen() {
		return len;
	}
	public void setLen(double len) {
		this.len = len;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getWaiting() {
		return waiting;
	}
	public void setWaiting(String waiting) {
		this.waiting = waiting;
	}
	public ArrayList<ArrayList<LatLng>> getLocations() {
		return locations;
	}
	public void setLocations(ArrayList<ArrayList<LatLng>> locations) {
		this.locations = locations;
	}

	public void setTrans(String trans) {
		this.trans = trans;
	}

	public String getTrans() {
		return trans;
	}
	
}
