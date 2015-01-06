package gui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

import process.ImageProcessor;

import tri.DelaunayTriangulation;
import tri.Point;
import tri.Triangle;
import filter.SobelFilter;

public class TriangleFrame {
    private List<Triangle> triangles;
    private List<Point> points;
    private JFrame frame;
    private static int numPoints = 100;
    private int width = 750, height = 750;
    private static BufferedImage colors;
    private HashSet<String> isPressed = new HashSet<String>();
    private static final String CLICKED = "clicked";

    public TriangleFrame() {
	this(getRandomPoints(numPoints, 750, 750));
    }

    public TriangleFrame(BufferedImage colorImage) {
	this(getEdgePoints(colorImage));
    }

    /**
     * Hello Sam, Since we do not yet have a function which makes an image Black
     * and White the user must provide a Black and White image for the Sobel
     * Filter.
     * 
     * The code below was written around 1 am so excuse the mess. I take all
     * pixels that are between the colors grey and white, and add their points
     * to the list of points.
     * 
     * I understand that most of the variables below are not parameters, but it
     * looks nicer this way :D
     * 
     * @param colorImage
     *            This image is used by the panel to color in the triangles.
     * 
     * @param noColorImage
     *            this image is passed to the SobelFilter for conversion.
     * 
     * @param qualityOfImage
     *            Instead of passing through the entire image pixel by pixel I
     *            skip pixels, this variable is how many pixels to skip.
     * 
     * @param intColor
     *            This is the integer representation of a RGB value, every pixel
     *            color is stored here, and compared to minColor and maxColor.
     *            Since the image is Black and White the only colors will be
     *            black -> white. If it is found to be a color between minColor
     *            and maxColor it is considered a edge point.
     * @param minColor
     *            This represents the minimum color considered to be an edge
     *            point. Changing this value can lower or raise the quality of
     *            the Image. Setting this to a darker grey will allow more
     *            points to be considered edge points.
     * 
     * @param maxColor
     *            Similar to minColor.
     * 
     */
    private static List<Point> getEdgePoints(BufferedImage colorImage) {
	BufferedImage noColorImage = new BufferedImage(colorImage.getWidth(),
		colorImage.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
	noColorImage.getGraphics().drawImage(colorImage, 0, 0, null);
	noColorImage = SobelFilter.filter(noColorImage);
	int intColor;
	int qualityOfImage = 2; // Lower = Higher Quality
	Color maxColor = Color.WHITE;
	Color minColor = Color.GRAY;
	List<Point> points = new ArrayList<Point>();
	for (int yCoord = 0; yCoord < noColorImage.getHeight(); yCoord += qualityOfImage) {
	    for (int xCoord = 0; xCoord < noColorImage.getWidth(); xCoord += qualityOfImage) {
		intColor = noColorImage.getRGB(xCoord, yCoord);
		if (intColor <= maxColor.getRGB()
			&& intColor >= minColor.getRGB())
		    points.add(new Point(xCoord, yCoord));
	    }
	}
	colors = colorImage;
	return points;
    }

    public TriangleFrame(List<Point> points) {
	this.makeTriangulation(points);
	frame = new JFrame("Triangles");
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	if (colors == null)
	    frame.setSize(width, height);
	else
	    frame.setSize(colors.getWidth(), colors.getHeight() + 35);
	frame.setLocationRelativeTo(null);
	Container c = frame.getContentPane();
	c.setLayout(null);

	TrianglePanel tp = new TrianglePanel(triangles);
	if (colors == null)
	    tp.setSize(width, height);
	else
	    tp.setSize(colors.getWidth(), colors.getHeight() + 35);
	tp.setLocation(0, 0);
	if (colors != null)
	    tp.setImageForColors(colors);
	c.add(tp);
	addListener(tp);
    }

    private void addListener(final TrianglePanel tp) {
	tp.addMouseListener(new MouseListener() {

	    @Override
	    public void mouseReleased(MouseEvent e) {
	    	isPressed.remove(CLICKED);
	    }

	    @Override
	    public void mousePressed(MouseEvent e) {
		isPressed.add(CLICKED);

		if ((e.getX() > 0) && (e.getX() < colors.getWidth())
			&& (e.getY() > 0) && (e.getY() < colors.getHeight())) {
		    points.add(new Point(e.getX(), e.getY()));
		    makeTriangulation(points);
		    tp.setTriangles(triangles);
		    tp.repaint();
		}
	    }

	    @Override
	    public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	    }

	    @Override
	    public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	    }

	    @Override
	    public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub

	    }
	});
	tp.addMouseMotionListener(new MouseMotionListener() {

	    @Override
	    public void mouseMoved(MouseEvent e) {
	    }

	    @Override
	    public void mouseDragged(MouseEvent e) {
		if (isPressed.contains(CLICKED)) {
		    if ((e.getX() > 0) && (e.getX() < colors.getWidth())
			    && (e.getY() > 0)
			    && (e.getY() < colors.getHeight())) {
			points.add(new Point(e.getX(), e.getY()));
			makeTriangulation(points);
			tp.setTriangles(triangles);
			tp.repaint();
		    }
		}
	    }
	});

    }

    public void showFrame() {
	frame.setVisible(true);
    }

    public List<Triangle> getTriangles() {
    	return this.triangles;
    }

    public List<Point> getPoints() {
    	return this.points;
    }

    private void makeTriangulation(List<Point> points) {
		this.points = points;
		DelaunayTriangulation dt = new DelaunayTriangulation(points);
		this.triangles = dt.getTriangulation();
    }

    public void randomizePoints(int numPoints) {
    	this.makeTriangulation(getRandomPoints(numPoints, width, height));
    }

    public static List<Point> getRandomPoints(int numPoints, int width,
	    int height) {
	List<Point> pts = new ArrayList<Point>();

	Random r = new Random();

	for (int i = 0; i < numPoints; i++) {
	    double x = 10 + (r.nextDouble() * (width - 50)), y = 10 + (r
		    .nextDouble() * (height - 50));
	    pts.add(new Point(x, y));
	}

	return pts;
    }
}
