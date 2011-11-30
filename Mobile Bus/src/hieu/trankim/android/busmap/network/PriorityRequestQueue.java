package hieu.trankim.android.busmap.network;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class PriorityRequestQueue {
	public final String TAG = getClass().getSimpleName();
	
	public static final int MAX_REQUESTS = 3;

	private ArrayList<Request> queue = new ArrayList<Request>(MAX_REQUESTS);
	
	private List<Request> oldRequests = new LinkedList<Request>();
	
	public PriorityRequestQueue() {}
	
	public void addRequest(Request request) {
		synchronized (queue) {
			// clear old requests
			Request oldRequest = null;
			for (int i = 0; i < queue.size(); ++i) {
				oldRequest = queue.get(i);
				if (oldRequest.priority == Request.PRIORITY_CAN_DISCARD) {
					queue.remove(i);
					i--;
				}
			}
			
			switch (request.priority) {
			
			case Request.PRIORITY_HIGHEST:
				queue.add(0, request);
				break;
				
			case Request.PRIORITY_NORMAL:
				queue.add(request);
				break;
				
			case Request.PRIORITY_CAN_DISCARD_SIMILIAR_REQUESTS:
				oldRequests.clear();
				for (Request req : queue) {
					if (req.priority == Request.PRIORITY_CAN_DISCARD_SIMILIAR_REQUESTS) {
						oldRequests.add(req);
					}
				}
				queue.removeAll(oldRequests);
				
				queue.add(request);
				break;
			}
			
			// check for total number of requests is larger than MAX_NUM_OF_REQUESTS
			if (queue.size() > MAX_REQUESTS) {
				queue.clear();
				queue.add(request);
			}
		}
	}
	
	public boolean isEmpty() {
		return queue.isEmpty();
	}
	
	public Request getNextRequest() {
		if (queue.size() > 0) {
			return queue.remove(0);
		}
		
		return null;
	}
	
	public void clear() {
		queue.clear();
	}
}
