package hieu.trankim.android.busmap.map;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

public class DirectionOverlay extends Overlay {

	private List<GeoPoint> listGeoPoint;

	public DirectionOverlay(ArrayList<GeoPoint> listGeoPoint) {
		this.listGeoPoint = listGeoPoint;
	}

	public boolean draw(Canvas canvas, MapView mapView, boolean shadow,
			long when) {
		// TODO Auto-generated method stub
		Projection projection = mapView.getProjection();
		if (shadow == false) {

			for (int i = 1; i < listGeoPoint.size() - 1; i++) {

				Paint paint = new Paint();
				paint.setAntiAlias(true);
				Point point = new Point();
				projection.toPixels(listGeoPoint.get(i), point);
				paint.setColor(Color.BLUE);
				Point point2 = new Point();
				projection.toPixels(listGeoPoint.get(i + 1), point2);
				paint.setStrokeWidth(2);
				canvas.drawLine((float) point.x, (float) point.y,
						(float) point2.x, (float) point2.y, paint);

			}
		}
		return super.draw(canvas, mapView, shadow, when);
	}

	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		// TODO Auto-generated method stub

		super.draw(canvas, mapView, shadow);
	}

}
