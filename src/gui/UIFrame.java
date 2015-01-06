package gui;

import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import process.ImageProcessor;
import tri.Point;

public class UIFrame extends ImageFrame /*implements MouseListener*/{
	protected ImageProcessor processor;
	protected boolean mouseClicked = false;
	
	public UIFrame() {
		this(getImage());
	}

	public UIFrame(Image i) {
		super(i);
		processor = new ImageProcessor(i);
		setImage(processor.processImage());
		addListeners();
	}

	/**
	 * 				Ask the user for an Image from the file system.
	 * 
	 * @return
	 * 				An Image selected by the user from the file system.
	 */
	private static Image getImage() {
		JFileChooser imageSelector = new JFileChooser(
			System.getProperty("user.dir") + "/images");
		
		imageSelector.setDialogTitle("");
		while (imageSelector.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) 
		    JOptionPane.showMessageDialog(null, "Please Select an Image");
	
		try {
		    return ImageIO.read(imageSelector.getSelectedFile());
		} catch (IOException e) {
		    e.printStackTrace();
		}
		return null;
	}

	/**
	 * 				Add Mouse Listeners to the ImagePanel in order to capture mouse events,
	 * 				and ultimately add points to the triangulation in the ImageProcessor
	 */
	private void addListeners() {
		imgPanel.addMouseListener(new MouseListener() {
		    @Override
		    public void mouseReleased(MouseEvent e) {
		    	mouseClicked = false;
		    }
		    @Override
		    public void mousePressed(MouseEvent e) {
				mouseClicked = true;

				if ((e.getX() > 0) && (e.getX() < ((BufferedImage)processor.getRawImage()).getWidth())
						&& (e.getY() > 0) && (e.getY() < ((BufferedImage)processor.getRawImage()).getHeight())) {
					
				    processor.addPoint(new Point(e.getX(), e.getY()));
				    imgPanel.setImage(processor.refreshTriangles());
				    imgPanel.repaint();
				}
		    }
		    @Override
		    public void mouseExited(MouseEvent e) {}
		    @Override
		    public void mouseEntered(MouseEvent e) {}
		    @Override
		    public void mouseClicked(MouseEvent arg0) {}
		});
		
		imgPanel.addMouseMotionListener(new MouseMotionListener() {
		    @Override
		    public void mouseMoved(MouseEvent e) {}

		    @Override
		    public void mouseDragged(MouseEvent e) {
		    	if (mouseClicked // Redundant?
						&& ((e.getX() > 0) && (e.getX() < ((BufferedImage)processor.getRawImage()).getWidth())
					    && (e.getY() > 0)
					    && (e.getY() < ((BufferedImage)processor.getRawImage()).getHeight()))) {
					processor.addPoint(new Point(e.getX(), e.getY()));
				    imgPanel.setImage(processor.refreshTriangles());
				    imgPanel.repaint();
			    }
			}
		});
	}
}
