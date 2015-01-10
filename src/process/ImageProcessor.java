package process;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.List;

import tri.*;

public class ImageProcessor {
	private PointMaker pointMaker;
	private Triangulation triangulation;
	private Image rawImage, processedImage;
	
	public ImageProcessor() {
		this.triangulation = new DelaunayTriangulation();
	}
	
	public ImageProcessor(Image imageToBeProcessed) {
		double width = ((BufferedImage)imageToBeProcessed).getWidth();
		double height = ((BufferedImage)imageToBeProcessed).getHeight();
		if(width > 1600 && height < width) {
			Image resizedImage = resizeImage(imageToBeProcessed, 1600, (int)((1600 / width) * height));
			this.pointMaker = new StickyPointMaker(resizedImage);
			this.rawImage = resizedImage;
		} else if(height > 900 && width < height) {
			Image resizedImage = resizeImage(imageToBeProcessed, (int)((900 / height) * width), 900);
			this.pointMaker = new StickyPointMaker(resizedImage);
			this.rawImage = resizedImage;
		} else {
			this.pointMaker = new StickyPointMaker(imageToBeProcessed);
			this.rawImage = imageToBeProcessed;
		}
	}
	
	public Image resizeImage(Image imageToBeProcessed, int newWidth, int newHeight) {
		int prevWidth = ((BufferedImage)imageToBeProcessed).getWidth(),
				prevHeight  = ((BufferedImage)imageToBeProcessed).getHeight();
		
		BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, ((BufferedImage) imageToBeProcessed).getType());
		Graphics2D g = resizedImage.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
			    RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.drawImage(imageToBeProcessed, 0, 0, newWidth, newHeight, 0, 0, prevWidth, prevHeight, null);  
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
		BufferedImage copy = new BufferedImage(
				((BufferedImage)rawImage).getWidth(), ((BufferedImage)rawImage).getHeight(),
				BufferedImage.TYPE_INT_RGB);
		
		Graphics g = copy.getGraphics();
		g.drawImage(imageToBeRendered,0,0,null);
		
		for(Triangle t : triangles) {
			gregsPaintTriangle(t,g);
		}
		
		return copy;
	}
	
    private void gregsPaintTriangle(Triangle t, Graphics g) {
		int xCoord[] = new int[3];
		int yCoord[] = new int[3];
		float centerX, centerY;
		Color averageColor;
		if (t.getC() != null) {
		    xCoord[0] = (int) t.getA().getX();
		    xCoord[1] = (int) t.getB().getX();
		    xCoord[2] = (int) t.getC().getX();
		    yCoord[0] = (int) t.getA().getY();
		    yCoord[1] = (int) t.getB().getY();
		    yCoord[2] = (int) t.getC().getY();
		    centerX = ((xCoord[0] + xCoord[1] + xCoord[2])) / 3;
		    centerY = ((yCoord[0] + yCoord[1] + yCoord[2])) / 3;
		    averageColor = new Color(((BufferedImage) rawImage).getRGB((int) centerX,
			    (int) centerY));
		    averageColor = new Color(
		    		averageColor.getRed(),
		    		averageColor.getGreen(),
		    		averageColor.getBlue(),
		    		255
		    		);
		    g.setColor(averageColor);
		    g.fillPolygon(xCoord, yCoord, 3);
		}
    }

	public void addPoint(Point point) {
		triangulation.insertPoint(point);
	}

	public Image refreshTriangles() {
		this.processedImage = renderTriangles(this.rawImage);
		return this.processedImage;
	}
}