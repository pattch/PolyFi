package gui;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class TestGui {
    public static void testTriangleFrame() {
	TriangleFrame tf = new TriangleFrame();
	tf.showFrame();
    }

    private static void gregsTestTriangleFrame() {
	TriangleFrame tf = new TriangleFrame(getImage("Color Image"));
	tf.showFrame();
    }

    private static BufferedImage getImage(String title) {  
	JFileChooser imageSelector = new JFileChooser(
		System.getProperty("user.dir") + "/images");
	imageSelector.setDialogTitle(title);
	while (imageSelector.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) {
	    JOptionPane.showMessageDialog(null, "Please Select a " + title);
	}

	try {
	    return ImageIO.read(imageSelector.getSelectedFile());
	} catch (IOException e) {
	    e.printStackTrace();
	}
	return null;
    }

    /*
     * Sam !, Your code is still working just fine, i made sure not to modify
     * everything too much lol, if you want to continue work on your test you
     * should be able to by commenting my method below out and uncomment your
     * code.
     */

    public static void main(String[] args) {
//	 testTriangleFrame();
	gregsTestTriangleFrame();
    }

}
