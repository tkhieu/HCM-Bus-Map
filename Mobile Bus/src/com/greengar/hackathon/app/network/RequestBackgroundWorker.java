package com.greengar.hackathon.app.network;

import android.util.Log;


public class RequestBackgroundWorker extends Thread {
	public static final String TAG = "RequestBackgroundWorker";
	
	private static PriorityRequestQueue queue = null;
	private static RequestBackgroundWorker worker = null;
	
	private volatile boolean running = true;
	
	public static void startWaitingForRequest() {
		if (worker == null) {
			worker = new RequestBackgroundWorker();
		}
	}
	
	private RequestBackgroundWorker() {
		queue = new PriorityRequestQueue();
		start();
	}
	
	public static void queueRequest(Request request) {
		if (worker != null) {
			synchronized (worker) {
				queue.addRequest(request);
				worker.notify();
			}
		}
	}
	
	public void run() {
		Log.w(TAG, "Start RequestBackgroundWorker");
		
		while (running) {
			synchronized (this) {
				if (queue.isEmpty()) {
					try {
						wait();
					} catch (InterruptedException e) {}
				} 
			}
			
			if (!running) {
				break;
			}
			
			Request request = queue.getNextRequest();
			if (request != null) { 
				request.send();
			} 
		}
		
		Log.w(TAG, "Stop RequestBackgroundWorker");
		worker = null;
	}
	
	public static void stopThread() {
		if (worker != null) {
			Log.w(TAG, "Try to stop RequestBackgroundWorker");
			synchronized (worker) {
				queue.clear();
				worker.running = false;
				worker.notify();
			}
			
			worker.interrupt();
		}
	}
}
