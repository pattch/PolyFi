package edge;

import java.awt.image.*;
import java.io.*;
import java.lang.Math;
import javax.imageio.*;	

public class SobelFilter{
	
	static String fileName, outputName;
	static final String FILE_TYPE_PNG = "png";
	static BufferedImage inputFile, outputFile;
	static int width, height;
	static File example, result;	
	
	/* NPU APPROXIMATION START */
	public static double sobel(double[][] window){ 
		double x, y, r = 0; 
		x = ( window[0][0] + 2 * window[0][1] + window[0][2] ); 
		x -= ( window[2][0] + 2 * window[2][1] + window[2][2] );
		y = ( window[0][2] + 2 * window[1][2] + window[2][2] );
		y -= ( window[0][0] + 2 * window[1][1] + window[2][0]);
		r = Math.sqrt( (x*x) + (y*y) );
		if (r > 255.0) r = 0;
		return r;
	}
	/* NPU APPROXIMATION END */

	public static void printRGB(int clr){
		int red = (clr & 0x00FF0000) >> 16; int green = (clr & 0x0000FF00)>>8; int blue = (clr & 0x000000FF);
		System.out.println("Red: "+red+" Green: "+green+" Blue: "+blue );
	}

	public static int getGreyScale(int clr){
		int red = (clr & 0x00FF0000) >> 16; int green = (clr & 0x0000FF00)>>8; int blue = (clr & 0x000000FF);
		if ( (red == blue) && (blue == green) ) return red;
		else return -1;
	}

	public static int setGreyScaleValue(int clr){
		return (clr) + (clr << 8) + (clr << 16);
	}

	public static double[][] buildWindow(int x, int y, BufferedImage srcImg){
		double[][] retVal = new double[5][5];   
		for ( int ypos = -2; ypos <= 2; ypos++ ){
			for (int xpos = -2; xpos <= 2; xpos++ ){
				int currX = xpos + x; int currY = ypos + y;
				if ( (currX >= 0 && currX < width) && (currY >= 0 && currY < height) ){
					int rgbRawValue = srcImg.getRGB(currX, currY);
					int grayScaleValue = getGreyScale(rgbRawValue);
					retVal[xpos + 2][ypos + 2] = grayScaleValue;
				}
				else
					retVal[xpos + 2][ypos + 2] = 255;
			}
		}
		return retVal; 
	}

	public static BufferedImage edgeDetection(BufferedImage srcImg){
		BufferedImage retVal = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
		double[][] window = new double[3][3];
		for (int y = 0; y < height; y++){
			for (int x = 0; x < width; x++ ){
				window = buildWindow(x, y, srcImg);
				// double newValue = sobel(window);
				double newValue = srcImg.getRGB(x,y);
				double sobelValue = sobel(window);
				int sobelRGBValue = setGreyScaleValue((int) sobelValue);
				int grayScaleMag = getGreyScale( (int) newValue);
				int greyscaleValue = setGreyScaleValue(grayScaleMag);
				retVal.setRGB(x, y, sobelRGBValue);
			}
		}
		return retVal;
	}

	public static void main(String[] args){
		fileName = "test.png"; outputName = "result.png";
		example = new File(fileName);
		System.out.println(fileName);
		try { 
			inputFile = ImageIO.read(example);
			width = inputFile.getWidth();
			height = inputFile.getHeight();
			outputFile = edgeDetection(inputFile);
			System.out.println("Success!! Width: " + width + " Height: "  + height);
			
		} 
		catch (IOException e){ /* DO NOTHING */ }

		try {
			result = new File(outputName);
			ImageIO.write(outputFile, FILE_TYPE_PNG, result);
		} 
		catch(IOException e){ /* DO NOTHING */ }

	}

}