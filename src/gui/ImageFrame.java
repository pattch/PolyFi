package gui;

import java.awt.Container;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import tri.Point;

public class ImageFrame extends JFrame  {
	protected ImagePanel imgPanel;
	
	/**
	 * 				Create a Frame that contains an ImagePanel
	 * 
	 * @param img
	 * 				The Image to be contained, drawn in the ImagePanel
	 */
	public ImageFrame(Image img) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(((BufferedImage)img).getWidth(), ((BufferedImage)img).getHeight());
		imgPanel = new ImagePanel(img);
		imgPanel.setSize(((BufferedImage)img).getWidth(), ((BufferedImage)img).getHeight());
		Container c = getContentPane();
		c.add(imgPanel);
	}
	
	/**
	 * 				Set/update the Image this Panel will contain, draw. Repaints the Panel
	 * 
	 * @param img
	 * 				The Image the ImagePanel will now contain, draw
	 */
	public void setImage(Image img) {
		imgPanel.setImage(img);
		imgPanel.repaint();
		repaint();
	}
	
	/**
	 * 				Get the ImagePanel that this ImageFrame contains
	 * 
	 * @return
	 * 				The ImagePanel that this ImageFrame contains
	 */
	public ImagePanel getPanel() {
		return this.imgPanel;
	}
}
