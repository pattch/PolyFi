package process;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import tri.BoundingBox;
import tri.Point;
import tri.Triangle;
import tri.Triangulation;

public class TriangleRenderer {
	
	/**
	 * Render a List of Triangles to a Graphics context.
	 * 
	 * @param g
	 * 				The Graphics context to render to.
	 * @param colorImage
	 * 				The reference Image to poll colors from.
	 * @param triangles
	 * 				The Triangles to be rendered.
	 */
	public static void render(Graphics g, Image colorImage, Collection<Triangle> triangles) {
		setAntialias(g,true);
		for(Triangle t : triangles) 
			renderTriangle(g,colorImage,t);
	}
	
	/**
	 * Render an Iterator of Triangles to a Graphics context.
	 * 
	 * @param g
	 * 				The Graphics context to render to.
	 * @param colorImage
	 * 				The reference Image to poll colors from.
	 * @param triangles
	 * 				The Triangles to be rendered.
	 */
	public static void render(Graphics g, Image colorImage, Iterator<Triangle> triangles) {
		setAntialias(g,true);
		while(triangles.hasNext())
			renderTriangle(g,colorImage,triangles.next());
	}
	
	/**
	 * Render a single triangle to a Graphics context. Reads the color at the middle point of
	 * the triangle from the Graphics context, and renders the triangle with that color to the
	 * Graphics context.
	 * 
	 * @param g
	 * 				The Graphics context to render to.
	 * @param t
	 * 				The Triangle to be rendered.
	 * @param colorImage
	 * 				The reference image to poll colors from.
	 */
    public static void renderTriangle(Graphics g, Image colorImage, Triangle t) {
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
		    averageColor = new Color(((BufferedImage) colorImage).getRGB((int) centerX,
			    (int) centerY));
		    g.setColor(averageColor);
		    g.fillPolygon(xCoord, yCoord, 3);
		}
    }
	
	/**
	 * Renders just the effected Triangles regarding the last update of the Triangulation to a new Image Object
	 * 
	 * @param rawImage
	 * 				The rawImage to be rendered on top of.
	 * @param previousImage
	 * 				The last Image that was rendered.
	 * @param updatedTriangles
	 * 				The Iterator that contains a list of updated triangles. To be used to determine
	 * 				the area that will be rendered to. 
	 * @param alpha
	 * 				Indicates whether the triangles will be rendered with low opacity -> (true) 
	 * 				or full opacity -> (false)
	 * @return
	 * 				The final rendered Image.
	 */
	public static Image render(Image rawImage, Image previousImage, Iterator<Triangle> updatedTriangles) {// Copy Previously Rendered Image to new Image
		BufferedImage copy = new BufferedImage(rawImage.getWidth(null), rawImage.getHeight(null),
				BufferedImage.TYPE_INT_RGB);
		
		Graphics g = copy.getGraphics();
		g.drawImage(previousImage,0,0,null);
		List<Triangle> triangles = new ArrayList<Triangle>();
		while(updatedTriangles.hasNext()) {
			Triangle t = updatedTriangles.next();
			triangles.add(t);
		}
		
		render(g, rawImage, triangles);
		
		return copy;
	}
	
	/**
	 * Render a Triangulation on top of a Raw Image.
	 * 
	 * @param rawImage
	 * 				The image to be drawn on top of. If null, should render on a black background.
	 * @param triangles
	 * 				The Triangulation to be rendered on top of the Image.
	 * @param alpha
	 * 				Indicates whether the triangles will be rendered with low opacity -> (true) 
	 * 				or full opacity -> (false)
	 * @return
	 * 				The final rendered Image.
	 */
	public static Image renderTriangles(Image rawImage, Triangulation triangulation, boolean alpha) {
		return null;
	}
	
	/**
	 * Set whether the Graphics Context renders with Antialiasing
	 * 
	 * @param g
	 * 				The Graphics Context that will be rendered to
	 * @param AA
	 * 				Tells whether the Graphics Context will use Antialiasing
	 */
	public static void setAntialias(Graphics g, boolean AA) {
		Graphics2D g2 = (Graphics2D)g;
		RenderingHints rh =
				AA ? new RenderingHints(
						RenderingHints.KEY_ANTIALIASING,
						RenderingHints.VALUE_ANTIALIAS_ON
				) : new RenderingHints(
						RenderingHints.KEY_ANTIALIASING,
						RenderingHints.VALUE_ANTIALIAS_OFF
				);
						
		g2.setRenderingHints(rh);
	}
}
