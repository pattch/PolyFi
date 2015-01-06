package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JPanel;

import tri.Point;
import tri.Triangle;

public class TrianglePanel extends JPanel {
    private List<Triangle> triangles;
    private BufferedImage imageForColors;

    public TrianglePanel() {
    	this(new LinkedList<Triangle>());
    }

    public TrianglePanel(List<Triangle> triangles) {
    	this.triangles = triangles;
    }

    @Override
    public void paintComponent(Graphics g) {
		clearScreen(g);
		for (Triangle t : triangles) {
		    paintTriangle(t, g);
		}
    }

    public void setImageForColors(BufferedImage colors) {
    	imageForColors = colors;
    }

    public void setTriangles(List<Triangle> triangles) {
    	this.triangles = triangles;
    }

    private void clearScreen(Graphics g) {
		Color previousColor = g.getColor();
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		g.setColor(previousColor);
    }

    private void paintTriangle(Triangle t, Graphics g) {
		if (imageForColors != null) {
		    gregsPaintTriangele(t, g);
		    return;
		}
		if (t != null) {
		    Point[] pts = new Point[3];
		    pts[0] = t.getA();
		    pts[1] = t.getB();
		    pts[2] = t.getC();
	
		    for (int i = 0; i < 3; i++) {
			if (pts[i] != null && pts[(i + 1) % 3] != null)
			    g.drawLine((int) pts[i].getX(), (int) pts[i].getY(),
				    (int) pts[(i + 1) % 3].getX(),
				    (int) pts[(i + 1) % 3].getY());
		    }
		}
    }

    private void gregsPaintTriangele(Triangle t, Graphics g) {
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
	    averageColor = new Color(imageForColors.getRGB((int) centerX,
		    (int) centerY));
	    g.setColor(averageColor);
	    g.fillPolygon(xCoord, yCoord, 3);
	}
    }
}
