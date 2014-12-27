package gui;

import java.awt.Container;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

import tri.*;

public class TriangleFrame {
	private List<Triangle> triangles;
	private List<Point> points;
	private JFrame frame;
	private static int numPoints = 100;
	private int width = 750,
				height = 750;
	
	public TriangleFrame() {
		this(getRandomPoints(numPoints, 750, 750));
	}
	
	public TriangleFrame(List<Point> points) {
		this.makeTriangulation(points);
		frame = new JFrame("Triangles");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setSize(width,height);
		Container c = frame.getContentPane();
		c.setLayout(null);
		
		JPanel tp = new TrianglePanel(triangles);
		tp.setSize(width,height);
		tp.setLocation(0,0);
		c.add(tp);
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
	
	public static List<Point> getRandomPoints(int numPoints, int width, int height) {
		List<Point> pts = new ArrayList<Point>();
		
		Random r = new Random();
		
		for(int i = 0; i < numPoints; i++) {
			double x =  10 + (r.nextDouble() * (width - 50)),
					y = 10 + (r.nextDouble() * (height - 50)),
					z = 0;
			pts.add(new Point(x,y,z));
		}
		
		return pts;
	}
}
