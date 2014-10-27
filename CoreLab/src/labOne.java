import java.io.File;
import java.awt.image.BufferedImage;

public class labOne {
	
	public static String WORK_DIR = "/Users/eric/Desktop/mudd_fall2014/lab/1/";
	public static File baseFile = new File(WORK_DIR+"nature.jpg");
	
	private static LabOneProcessor proc = new LabOneProcessor();
	
	public static void main(String args[]) {
		problemOne();
		problemTwo();
		problemThree();
		problemFour();
		problemFive();
		problemSix();
	}
	
	public static void problemOne() {
		File outputFile = new File(WORK_DIR+"image_1.png");
		proc.readWorkingImage(baseFile);
		
		proc.writeWorkingImage(outputFile);
	}
	
	public static void problemTwo() {
		File outputFile = new File(WORK_DIR+"image_2.png");
		proc.readWorkingImage(baseFile);
		
		proc.negateImage();
		proc.writeWorkingImage(outputFile);		
	}
	
	public static void problemThree() {
		File outputFile = new File(WORK_DIR+"image_3.png");
		proc.readWorkingImage(baseFile);
		
		BufferedImage histogram = proc.histogramC(LabOneProcessor.DENSITY);
		ImageProcessor.writeImage(histogram, outputFile);
	}
	
	public static void problemFour() {
		File outputFile = new File(WORK_DIR+"image_4.png");
		proc.readWorkingImage(baseFile);
		
		BufferedImage histogram = proc.histogramC(LabOneProcessor.CUMULATIVE);
		ImageProcessor.writeImage(histogram, outputFile);
	}
	
	public static void problemFive() {
		File outputFile = new File(WORK_DIR+"image_5.png");
		proc.readWorkingImage(baseFile);
		
		proc.negateImage();
		BufferedImage histogram = proc.histogramC(LabOneProcessor.DENSITY);
		ImageProcessor.writeImage(histogram, outputFile);
	}
	
	public static void problemSix() {
		File outputFile = new File(WORK_DIR+"image_6.png");
		proc.readWorkingImage(baseFile);
		
		proc.negateImage();
		BufferedImage histogram = proc.histogramC(LabOneProcessor.CUMULATIVE);
		ImageProcessor.writeImage(histogram, outputFile);
	}
}
