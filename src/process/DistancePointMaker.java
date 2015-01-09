package process;

/*
 * This Class takes an Image and generates a set of Points. This Implementation builds
 * upon EdgePointMaker by taking the previously generated Points and ensuring that the
 * generated points are not too close. No two points can be within a [0,minDistance) 
 * radius of each other. The implementation here should ensure this property by sorting the
 * Points by distance from the center of the Image, and then removing points that violate
 * the distance property.
 */

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import tri.Point;

public class DistancePointMaker extends EdgePointMaker {
	protected double minDistance;

	public DistancePointMaker(Image imageToBeProcessed) {
		super(imageToBeProcessed);
	}
	
	@Override
	public Collection<Point> makePoints(Image imageToBeProcessed) {
		super.makePoints(imageToBeProcessed);
		final int centerX = ((BufferedImage)imageToBeProcessed).getWidth() / 2;
		final int centerY = ((BufferedImage)imageToBeProcessed).getWidth() / 2;
		radialSortPoints((List<Point>)points, new Point(centerX, centerY));
		return points;
	}
	
	private void radialSortPoints(List<Point> points, Point centerPoint) {
		final Point center = centerPoint; 
		Collections.sort((List<Point>)points, new Comparator<Point>() {
		
			@Override
			public int compare(Point p1, Point p2) {
				// Calculate the distance of p1, p2 to center
				double dist1 = Math.sqrt(
						((p1.getX() - center.getX()) * (p1.getX() - center.getX())) +
						((p1.getY() - center.getY()) * (p1.getY() - center.getY())));
				double dist2 = Math.sqrt(
						((p2.getX() - center.getX()) * (p2.getX() - center.getX())) +
						((p2.getY() - center.getY()) * (p2.getY() - center.getY())));
				
				
				return (int)(dist1 - dist2);
			}
			
		});
	}
	
	public void setMinDistance(double minDistance) {
		this.minDistance = minDistance;
	}
}
