package process;

import java.awt.Image;
import java.util.Collection;

import tri.Point;

public interface PointMaker {
	/**
	 * @param imageToBeProcessed
	 * 				The unprocessed Image to Populate points from
	 * @return 
	 * 				A collection of representative points of the given Image,
	 * 				should 
	 */
	public Collection<Point> makePoints(Image imageToBeProcessed);
	
	public Collection<Point> getPoints();
	
	public void setImage(Image imageToBeProcessed);
}
