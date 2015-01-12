package process;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.List;

import tri.DelaunayTriangulation;
import tri.Point;
import tri.Triangle;
import tri.Triangulation;

public class ImageProcessor {
	protected PointMaker pointMaker;
	protected Triangulation triangulation;
	protected Image rawImage, processedImage;
	
	public ImageProcessor() {
		this.triangulation = new DelaunayTriangulation();
	}
	
	public ImageProcessor(Image imageToBeProcessed) {
//		double width = imageToBeProcessed.getWidth(null);
//		double height = imageToBeProcessed.getHeight(null);
		this.pointMaker = new StickyPointMaker(imageToBeProcessed);
		this.rawImage = imageToBeProcessed;
	}
	
	/**
	 * 				Resizes Image to fit the display.
	 * 
	 * @param imageToBeProcessed
	 * 				The Image that will be resized.
	 * 
	 * @return 
	 * 				Resized image, automatically scaled.
	 */
	
	public static Image resizeImage(Image imageToBeProcessed) {
		Dimension screenResolution = Toolkit.getDefaultToolkit().getScreenSize();
		int imgWidth = imageToBeProcessed.getWidth(null), imgHeight  = imageToBeProcessed.getHeight(null);
		int screenPadding = 35;
		double screenWidth = screenResolution.getWidth() - screenPadding, screenHeight = screenResolution.getHeight()- screenPadding;
		if (imgWidth < screenWidth || imgHeight < screenHeight)
		    return imageToBeProcessed;
		double scaleRatio = (imgWidth/screenWidth  > imgHeight/screenHeight) ? imgWidth/screenWidth : imgHeight/screenHeight;
		imgWidth = (int)(imgWidth/scaleRatio);
		imgHeight = (int)(imgHeight/scaleRatio);
		BufferedImage resizedImage = new BufferedImage(imgWidth, imgHeight, ((BufferedImage) imageToBeProcessed).getType());
		Graphics2D g = resizedImage.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
			    RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.drawImage(imageToBeProcessed, 0,0,imgWidth,imgHeight,null);  
		g.dispose();
	    return resizedImage;
	}
	
	public void setImage(Image imageToBeProcessed) {
		this.rawImage = imageToBeProcessed;
	}
	
	public Image processImage() {
		this.triangulation = new DelaunayTriangulation(pointMaker.makePoints(rawImage));
		this.processedImage = renderTriangles(this.rawImage);
		return this.processedImage;
	}
	
	public Image getProcessedImage() {
		return this.processedImage;
	}
	
	public Image getRawImage() {
		return this.rawImage;
	}
	
	public Image renderTriangles(Image imageToBeRendered) {
		List<Triangle> triangles = this.triangulation.getTriangulation();
		BufferedImage copy = new BufferedImage(rawImage.getWidth(null), rawImage.getHeight(null),
				BufferedImage.TYPE_INT_RGB);
		
		Graphics g = copy.getGraphics();
		g.drawImage(imageToBeRendered,0,0,null);
		TriangleRenderer.render(g, imageToBeRendered, triangles);
		
		return copy;
	}
	
//    private void gregsPaintTriangle(Triangle t, Graphics g) {
//		int xCoord[] = new int[3];
//		int yCoord[] = new int[3];
//		float centerX, centerY;
//		Color averageColor;
//		if (t.getC() != null) {
//		    xCoord[0] = (int) t.getA().getX();
//		    xCoord[1] = (int) t.getB().getX();
//		    xCoord[2] = (int) t.getC().getX();
//		    yCoord[0] = (int) t.getA().getY();
//		    yCoord[1] = (int) t.getB().getY();
//		    yCoord[2] = (int) t.getC().getY();
//		    centerX = ((xCoord[0] + xCoord[1] + xCoord[2])) / 3;
//		    centerY = ((yCoord[0] + yCoord[1] + yCoord[2])) / 3;
//		    averageColor = new Color(((BufferedImage) rawImage).getRGB((int) centerX,
//			    (int) centerY));
//		    averageColor = new Color(
//		    		averageColor.getRed(),
//		    		averageColor.getGreen(),
//		    		averageColor.getBlue(),
//		    		255
//		    		);
//		    g.setColor(averageColor);
//		    g.fillPolygon(xCoord, yCoord, 3);
//		}
//    }

	public void addPoint(Point point) {
		triangulation.insertPoint(point);
	}

	public Image refreshTriangles() {
//		this.processedImage = renderTriangles(this.rawImage);
		this.processedImage = TriangleRenderer.render(this.rawImage, this.processedImage, this.triangulation.getLastUpdatedTriangles());
		return this.processedImage;
	}
}