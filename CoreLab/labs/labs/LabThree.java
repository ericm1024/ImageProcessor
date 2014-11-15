package labs;

import iproc.ImageProcessor;

import java.io.File;

public class LabThree {
	public static String WORK_DIR = "/Users/eric/Desktop/mudd_fall2014/lab/3/";
	public static File baseFile = new File(WORK_DIR+"Paolina1.png");
	
	private static ImageProcessor proc = new ImageProcessor();
	
	public static void main(String args[]) {
		problemOne();
		problemTwo();
		problemThree(0.03f);
		problemFour(0.08f);
	}
	
	
	// a basic greyscale histogram of the Paolina image.
	public static void problemOne() {
		File outputFile1 = new File(WORK_DIR+"problem-one-Paolina.png");
		File outputFile2 = new File(WORK_DIR+"problem-one-histogram.png");
		
		proc.readWorkingImage(baseFile);
		proc.writeWorkingImage(outputFile1);	
		ImageProcessor.writeImage(proc.greyHisto(), outputFile2);
	}
	
	
	// a linearly stretched histogram of the Paolina image
	public static void problemTwo() {
		File outputFile1 = new File(WORK_DIR+"problem-two-Paolina.png");
		File outputFile2 = new File(WORK_DIR+"problem-two-histogram.png");
		
		proc.readWorkingImage(baseFile);			
		ImageProcessor.writeImage(proc.stretchMinMax(), outputFile1);
		ImageProcessor.writeImage(proc.stretchedGreyHisto(), outputFile2);
	}
	
	
	// a histogram of the Paolina image with the top and bottom
	// cutoffPercent of points removed
	public static void problemThree(float cutoffPercent) {
		File outputFile1 = new File(WORK_DIR+"problem-three-Paolina.png");
		File outputFile2 = new File(WORK_DIR+"problem-three-histogram.png");
		
		proc.readWorkingImage(baseFile);
		
		ImageProcessor.writeImage(proc.stretchCutoff(cutoffPercent), outputFile1);
		ImageProcessor.writeImage(proc.cutoffHisto(cutoffPercent), outputFile2);
	}
	
	public static void problemFour(float cutoffPercent) {
		File myFile = new File(WORK_DIR+"haswell-e-die-shot.jpg");
		File outputFile1 = new File(WORK_DIR+"problem-four-original.png");
		File outputFile2 = new File(WORK_DIR+"problem-four-original-histogram.png");
		File outputFile3 = new File(WORK_DIR+"problem-four-cutoff.png");
		File outputFile4 = new File(WORK_DIR+"problem-four-cutoff-histogram.png");		

		proc.readWorkingImage(myFile);
		
		proc.makeGreyscale();
		proc.writeWorkingImage(outputFile1);	
		ImageProcessor.writeImage(proc.greyHisto(), outputFile2);
		
		ImageProcessor.writeImage(proc.stretchCutoff(cutoffPercent), outputFile3);
		ImageProcessor.writeImage(proc.cutoffHisto(cutoffPercent), outputFile4);
	}	
}
