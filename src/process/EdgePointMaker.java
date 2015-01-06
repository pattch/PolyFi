package process;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import tri.Point;

import filter.*;

public class EdgePointMaker implements PointMaker {
	/**
	 * 				This represents the upper bound of colors considered to be an edge
     *            	point. Setting this to a darker grey will allow less points to be 
     *            	considered edge points.
	 */
	public static final Color MAX_COLOR = Color.WHITE;
	
	/**
     *            	This represents the lower bound of colors considered to be an edge
     *            	point. Setting this to a darker grey will allow more
     *            	points to be considered edge points.
	 */
	public static final Color MIN_COLOR = Color.LIGHT_GRAY;
	
	/*
     *            Instead of passing through the entire image pixel by pixel I
     *            skip pixels, this variable is how many pixels to skip.
     */
	public static final int IMAGE_QUALITY = 2; 

	private Collection<Point> points;
	private Image rawImage;
	
	@Override
	public Collection<Point> makePoints(Image imageToBeProcessed) {
		BufferedImage colorImage = (BufferedImage)imageToBeProcessed;
		
		BufferedImage greyImage = new BufferedImage(colorImage.getWidth(),
			colorImage.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
		
		greyImage.getGraphics().drawImage(colorImage, 0, 0, null);
		greyImage = SobelFilter.filter(greyImage);
		
		/*
		 *        This is the integer representation of a RGB value, every pixel
		 *        color is stored here, and compared to minColor and maxColor.
		 *        Since the image is Black and White the only colors will be
     	 *        black -> white. If it is found to be a color between minColor
     	 *        and maxColor it is considered a edge point.
		 */
		int intColor;
		
		List<Point> builtPoints = new ArrayList<Point>();
		
		for (int yCoord = 0; yCoord < greyImage.getHeight(); yCoord += IMAGE_QUALITY) {
		    for (int xCoord = 0; xCoord < greyImage.getWidth(); xCoord += IMAGE_QUALITY) {
				intColor = greyImage.getRGB(xCoord, yCoord);
				
				if (intColor <= MAX_COLOR.getRGB()
					&& intColor >= MIN_COLOR.getRGB())
					
				    builtPoints.add(new Point(xCoord, yCoord));
		    }
		}
		
		return builtPoints;
    }
	
	public EdgePointMaker(Image imageToBeProcessed) {
		this.rawImage = imageToBeProcessed;
	}
	
	public void setImage(Image imageToBeProcessed) {
		this.rawImage = imageToBeProcessed;
	}
	
	public Collection<Point> makePoints() {
		if(this.rawImage == null)
			return null;
		this.points = makePoints(this.rawImage);
		return this.points;
	}
	
	public Collection<Point> getPoints() {
		return this.points;
	}
}
