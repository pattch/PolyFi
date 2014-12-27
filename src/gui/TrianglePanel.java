package gui;

import java.awt.Graphics;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JPanel;
import tri.*;

public class TrianglePanel extends JPanel {
	private List<Triangle> triangles;
	
	public TrianglePanel() {
		this(new LinkedList<Triangle>());
	}
	
	public TrianglePanel(List<Triangle> triangles) {
		this.triangles = triangles;
	}
	
	@Override
	public void paintComponent(Graphics g) {		
		for(Triangle t : triangles) {
			paintTriangle(t,g);
		}
	}
	
	private void paintTriangle(Triangle t, Graphics g) {
		if(t != null) {
			Point[] pts = new Point[3];
			pts[0] = t.getA();
			pts[1] = t.getB();
			pts[2] = t.getC();
			
			for(int i = 0; i < 3; i++) {
				if(pts[i] != null && pts[(i + 1) % 3] != null)
					g.drawLine((int)pts[i].getX(), (int)pts[i].getY(),
						(int)pts[(i + 1) % 3].getX(), (int)pts[(i + 1) % 3].getY());
			}
		}
	}
}
