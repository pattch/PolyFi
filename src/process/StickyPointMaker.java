package process;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import tri.Point;

public class StickyPointMaker extends EdgePointMaker {
	protected int pointCount = 20000;
	protected double stickiness = 5;

	public StickyPointMaker(Image imageToBeProcessed) {
		super(imageToBeProcessed);
	}
	
	@Override
	public Collection<Point> makePoints(Image imageToBeProcessed) {
		// Generate random points.
		Random r = new Random();
		Collection<Point> builtPoints = new ArrayList<Point>();
		
		int width = imageToBeProcessed.getWidth(null), height = imageToBeProcessed.getHeight(null);
		
		for(int i = 0; i < pointCount; i++) {
			builtPoints.add(new Point(
						r.nextDouble() * width,
						r.nextDouble() * height,
						0
					));
		}
		
		// Make the random points move closer to the  edges of the image.
		
		setImageQuality(3);
		super.makePoints(imageToBeProcessed);
		
		for(Point p : builtPoints) {
			movePointByNetForces(p, points);
			
			if(p.getX() > width)
				p.setX(width);
			if(p.getX() < 0)
				p.setX(0);
			if(p.getY() > height)
				p.setY(height);
			if(p.getY() < 0)
				p.setY(0);
		}
		
		points.addAll(builtPoints);
		
		// Finally, for completeness, add the corners
		
		points.add(new Point(0,0,0));
		points.add(new Point(width,0,0));
		points.add(new Point(0,height,0));
		points.add(new Point(width,height,0));
		
		return points;
	}
	
	private void movePointByNetForces(Point ptMoved, Collection<Point> points) {
		double sumX = 0, sumY = 0;
		for(Point p : points) {
			double distX = ptMoved.getX() - p.getX(),
					distY = ptMoved.getY() - p.getY();
			sumX += (distX >= 0) ? ((stickiness) / (distX * distX)) : -((stickiness) / (distX * distX));
			sumY += (distY >= 0) ? ((stickiness) / (distY * distY)) : -((stickiness) / (distY * distY));
		}
		
		ptMoved.setX(sumX + ptMoved.getX());
		ptMoved.setY(sumY + ptMoved.getY());
	}
	
	public void setStickiness(double stickiness) {
		this.stickiness = stickiness;
	}
	
	public double getStickiness() {
		return this.stickiness;
	}
}
