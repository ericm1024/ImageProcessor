package labs;

import iproc.ImageProcessor;
import iproc.HistogramProcessor;

import java.io.File;


public class LabFour {
	public static String WORK_DIR = "/Users/eric/Desktop/mudd_fall2014/lab/4/";
	public static File baseFile = new File(WORK_DIR+"LennaG.png");
	
	private static LabFourProcessor proc4 = new LabFourProcessor();
	
	// for histogram functions
	private static HistogramProcessor procH = new HistogramProcessor();
	
	public static void main(String args[]) {
		problemOne();
		problemTwo();
	}
	
	public static void problemOne() {
		File outputFile1 = new File(WORK_DIR+"original-image.png");
		File outputFile2 = new File(WORK_DIR+"original-density-histogram.png");
		File outputFile3 = new File(WORK_DIR+"original-cumulative-histogram.png");
		
		proc4.readWorkingImage(baseFile);
		procH.readWorkingImage(baseFile);
		
		proc4.writeWorkingImage(outputFile1);
		ImageProcessor.writeImage(procH.histogramRgb(), outputFile2);
		ImageProcessor.writeImage(procH.cumulativeHistogramRgb(), outputFile3);
	}
	
		
	public static void problemTwo() {
		File outputFile1 = new File(WORK_DIR+"equalized-image.png");
		File outputFile2 = new File(WORK_DIR+"equalized-density-histogram.png");
		File outputFile3 = new File(WORK_DIR+"equalized-cumulative-histogram.png");
		
		proc4.readWorkingImage(baseFile);
		ImageProcessor.writeImage(proc4.equalize(), outputFile1);	
		procH.readWorkingImage(outputFile1);
		ImageProcessor.writeImage(procH.histogramRgb(), outputFile2);
		ImageProcessor.writeImage(procH.cumulativeHistogramRgb(), outputFile3);
	}
}
