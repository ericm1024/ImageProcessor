package labs;
import ImageProcessor;
import iproc.HistogramProcessor;

import java.io.File;


public class LabFive {
	public static String WORK_DIR = "/Users/eric/Desktop/mudd_fall2014/lab/5/";
	public static File baseFile = new File(WORK_DIR+"LennaG.png");
	
	private static LabFiveProcessor proc5 = new LabFiveProcessor();
	// for histogram functions
	private static HistogramProcessor procH = new HistogramProcessor();
	
	public static void main(String args[]) {
		problemOne();
		problemTwo();
		problemThree();
	}
	
	public static void problemOne() {
		File outputFile1 = new File(WORK_DIR+"original-image.png");
		File outputFile2 = new File(WORK_DIR+"original-density-histogram.png");
		File outputFile3 = new File(WORK_DIR+"original-cumulative-histogram.png");
		
		proc5.readWorkingImage(baseFile);
		procH.readWorkingImage(baseFile);
		
		proc5.writeWorkingImage(outputFile1);
		ImageProcessor.writeImage(procH.histogram(), outputFile2);
		ImageProcessor.writeImage(procH.cumulativeHistogram(), outputFile3);
	}
	
		
	public static void problemTwo() {
		File outputFile1 = new File(WORK_DIR+"output-image-1.png");
		File outputFile3 = new File(WORK_DIR+"output-1-density-histogram.png");
		File outputFile5 = new File(WORK_DIR+"output-1-cumulative-histogram.png");
		
		proc5.readWorkingImage(baseFile);
		ImageProcessor.writeImage(proc5.histogramSpec(problemTwoHistogram()), outputFile1);
		
		procH.readWorkingImage(outputFile1);
		ImageProcessor.writeImage(procH.histogram(), outputFile3);
		ImageProcessor.writeImage(procH.cumulativeHistogram(), outputFile5);
	}

	
	public static void problemThree() {
		File outputFile1 = new File(WORK_DIR+"output-image-2.png");
		File outputFile3 = new File(WORK_DIR+"output-2-density-histogram.png");
		File outputFile5 = new File(WORK_DIR+"output-2-cumulative-histogram.png");
		
		proc5.readWorkingImage(baseFile);
		ImageProcessor.writeImage(proc5.histogramSpec(problemThreeHistogram()), outputFile1);
		
		procH.readWorkingImage(outputFile1);
		ImageProcessor.writeImage(procH.histogram(), outputFile3);
		ImageProcessor.writeImage(procH.cumulativeHistogram(), outputFile5);
	}
	
	
	private static double[] problemTwoHistogram() {
		double[] histogram = new double[ImageProcessor.NUM_COLORS];
		
		for (int i = 0; i < histogram.length; i++) {
			if (i < ImageProcessor.NUM_COLORS/2) {
				histogram[i] = i+1;
			} else {
				histogram[i] = ImageProcessor.NUM_COLORS - i;
			}
		}
		LabFiveProcessor.normalize(histogram);
		return histogram;
	}
	
	
	private static double[] problemThreeHistogram() {
		double[] histogram = new double[ImageProcessor.NUM_COLORS];
		
		for (int i = 0; i < histogram.length; i++) {
			if (i < ImageProcessor.NUM_COLORS/2) {
				histogram[i] = ImageProcessor.NUM_COLORS/2 - i;
			} else {
				histogram[i] = i - ImageProcessor.NUM_COLORS/2;
			}
		}
		LabFiveProcessor.normalize(histogram);
		return histogram;
	}
}
