package iproc.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import iproc.Pixel;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.Test;

public class PixelTest {	
	
	// helper functions
	private BufferedImage getGreyTestImage() {
		File f = new File("/Users/eric/Desktop/resources/lenna.png");
		try {
			return ImageIO.read(f);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
		
	private BufferedImage getColorTestImage() {
		File f = new File("/Users/eric/Desktop/resources/lennaC.png");
		try {
			return ImageIO.read(f);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/*
	 * We would like to use the color enumerations we have in RawPixel,
	 * but we want these tests to stand alone.
	 * 
	 * colr should be 0 for red, 1 for green, 2 for blue, and 3 for grey
	 */
	private void testGetColor(int color) {
		// I promise this function is less gross than it looks
		BufferedImage[] images = {getGreyTestImage(), getColorTestImage()};
		for (BufferedImage b : images) {
			Pixel pix = new Pixel(b);
			for (int i = 0; i < b.getHeight(); i++) {
				for (int j = 0; j < b.getWidth(); j++) {
					Color c = new Color(b.getRGB(i,j));
					pix.moveTo(i,j);
					
					// we'll grab the expected and actual values for each of these
					// in the switch statement, then do the actual assertEquals after
					int val = 0;
					int cmpVal = 0;
					float floatVal = 0;
					float cmpFloatVal = 0;
					
					switch(color) {
						case 0: // red
							val = c.getRed();
							floatVal = val/(float)255;
							
							cmpVal = pix.getRed();
							cmpFloatVal = pix.getRedFloat();
							break;
						case 1: // green
							val = c.getGreen();
							floatVal = val/(float)255;
							
							cmpVal = pix.getGreen();
							cmpFloatVal = pix.getGreenFloat();
							break;
						case 2: // blue
							val = c.getBlue();
							floatVal = val/(float)255;
							
							cmpVal = pix.getBlue();
							cmpFloatVal = pix.getBlueFloat();						
							break;
						case 3: // grey
							val = c.getRed() + c.getGreen() + c.getBlue(); 
							val /= 3;
							floatVal = (c.getRed() + c.getGreen() + c.getBlue())/(float)255;
							floatVal /= 3.0f;
							
							cmpVal = pix.getGrey();
							cmpFloatVal = pix.getGreyFloat();	
							break;
						default:
							break;
					}
					assertEquals(cmpVal, val);
					assertEquals(cmpFloatVal,floatVal,1e-5);
				}
			}
		}
	}
	
	private void testSetColor(int color) {
		// write me
	}
		
	// moveTo ... we test this first because a lot of other tests depend on it
	@Test
	public void test_moveTo_() {
		BufferedImage testImage = getGreyTestImage();
		Pixel pix1 = new Pixel(testImage);
		
		for (int i = 0; i < testImage.getHeight(); i++) {
			for (int j = 0; j < testImage.getWidth(); j++) {
				pix1.moveTo(i,j);
				assertTrue(pix1.getX() == i);
				assertTrue(pix1.getY() == j);
			}
		}
	}
		
	// equals
	@Test
	public void test_equals_() {
		BufferedImage testImage1 = getGreyTestImage();
		Pixel pix1 = new Pixel(testImage1);
		Pixel pix2 = new Pixel(testImage1);
		
		// test that equality is reflexive and symmetric
		for (int i = 0; i < testImage1.getHeight(); i++) {
			for (int j = 0; j < testImage1.getWidth(); j++) {
				pix1.moveTo(i, j);
				pix2.moveTo(i, j);
				
				assertTrue(pix1.equals(pix1));
				assertTrue(pix1.equals(pix2));
				assertTrue(pix2.equals(pix2));
				assertTrue(pix2.equals(pix1));
			}
		}
		
		BufferedImage testImage2 = getColorTestImage();
		pix1 = new Pixel(testImage2);
		pix2 = new Pixel(testImage2);
		
		// same tests for differrent image
		for (int i = 0; i < testImage2.getHeight(); i++) {
			for (int j = 0; j < testImage2.getWidth(); j++) {
				pix1.moveTo(i, j);
				pix2.moveTo(i, j);
				
				assertTrue(pix1.equals(pix1));
				assertTrue(pix1.equals(pix2));
				assertTrue(pix2.equals(pix2));
				assertTrue(pix2.equals(pix1));
			}
		}
		
		// negative test that pixels from different images are not the same.
		pix1 = new Pixel(testImage1);
		assertFalse(pix1.equals(pix2));
		assertFalse(pix2.equals(pix1));
	}
	
	// hash code
	@Test
	public void test_hashCode_() {
		// hash code runs the same sorts of tests as equality
		BufferedImage testImage1 = getGreyTestImage();
		Pixel pix1 = new Pixel(testImage1);
		Pixel pix2 = new Pixel(testImage1);
		
		for (int i = 0; i < testImage1.getHeight(); i++) {
			for (int j = 0; j < testImage1.getWidth(); j++) {
				pix1.moveTo(i, j);
				pix2.moveTo(i, j);
				
				assertTrue(pix1.hashCode() == pix1.hashCode());
				assertTrue(pix1.hashCode() == pix2.hashCode());
				assertTrue(pix2.hashCode() == pix2.hashCode());
			}
		}
		
		BufferedImage testImage2 = getColorTestImage();
		pix1 = new Pixel(testImage2);
		pix2 = new Pixel(testImage2);
		
		// same tests for differrent image		
		for (int i = 0; i < testImage2.getHeight(); i++) {
			for (int j = 0; j < testImage2.getWidth(); j++) {
				pix1.moveTo(i, j);
				pix2.moveTo(i, j);
				
				assertTrue(pix1.hashCode() == pix1.hashCode());
				assertTrue(pix1.hashCode() == pix2.hashCode());
				assertTrue(pix2.hashCode() == pix2.hashCode());
			}
		}
		
		// we can't really do definitive negative tests with hash code...
	}
	
	// inImage
	@Test
	public void test_inImage_() {
		BufferedImage testImage = getGreyTestImage();
		Pixel pix1 = new Pixel(testImage);
		
		for (int i = 0; i < testImage.getHeight(); i++) {
			for (int j = 0; j < testImage.getWidth(); j++) {
				assertTrue(pix1.inImage(i,j));
			}
		}
		
		for (int i = -1; i >= -testImage.getHeight(); i--) {
			for (int j = -1; j >= -testImage.getWidth(); j--) {
				assertFalse(pix1.inImage(i,j));
			}
		}
	}
	
	// get red (int/float)
	@Test
	public void test_getRed_() {
		testGetColor(0);
	}
	
	// get green (int/float)
	@Test
	public void test_getGreen_() {
		testGetColor(1);
	}
	
	// get blue (int/float)
	@Test
	public void test_getBlue_() {
		testGetColor(2);
	}
	
	// get grey (int/float)
	@Test
	public void test_getGrey_() {
		testGetColor(3);
	}

	// greyscale
	@Test
	public void test_greyscale_() {
		BufferedImage test = getColorTestImage();
		BufferedImage controll = getColorTestImage();
		Pixel testp = new Pixel(test);
		Pixel controllp = new Pixel(controll);
		
		
		for (int i = 0; i < test.getWidth(); i++) {
			for (int j = 0; j < test.getHeight(); j++) {
				testp.moveTo(i,j).greyscale();
			}
		}
		
		for (int i = 0; i < test.getWidth(); i++) {
			for (int j = 0; j < test.getHeight(); j++) {
				testp.moveTo(i,j);
				controllp.moveTo(i,j);
				assertEquals(testp.getRed(), controllp.getGrey());
				assertEquals(testp.getGreen(), controllp.getGrey());
				assertEquals(testp.getBlue(), controllp.getGrey());
			}
		}
	}
	
	// set red (int/float)
	@Test
	public void test_setRed_() {
		testSetColor(0);
	}
	
	// set green (int/float)
	@Test
	public void test_setGreen_() {
		testSetColor(1);
	}
	
	// set blue (int/float)
	@Test
	public void test_setBlue_() {
		testSetColor(2);
	}
	
	// set grey (int/float)
	@Test
	public void test_setGrey_() {
		testSetColor(3);
	}
}