import java.io.File;


public class LabSix {
	public static String WORK_DIR = "/Users/eric/Desktop/mudd_fall2014/lab/6/";
	public static File baseFile = new File(WORK_DIR+"LennaC.png");
	
	private static LabSixProcessor proc6 = new LabSixProcessor();
	// for histogram functions
	private static ColorHistogramProcessor procH = new ColorHistogramProcessor();
	
	public static void main(String args[]) {
		/*
		problemOne();
		problemTwo();
		problemThree();
		problemFour();
		problemFive();
		*/
		testOne();
	}

	public static void problemOne() {
		File outputFile1 = new File(WORK_DIR+"original.png");
		File outputFile2 = new File(WORK_DIR+"original-histogram.png");
		
		procH.readWorkingImage(baseFile);
		
		procH.writeWorkingImage(outputFile1);
		ImageProcessor.writeImage(procH.histogram(), outputFile2);
	}
	
	public static void problemTwo() {
		File outputFile1 = new File(WORK_DIR+"rotated-hue.png");
		File outputFile2 = new File(WORK_DIR+"rotated-hue-histogram.png");
		
		proc6.readWorkingImage(baseFile);
		proc6.rotateHue(Math.PI/2);
		proc6.writeWorkingImage(outputFile1);
		
		procH.readWorkingImage(outputFile1);
		ImageProcessor.writeImage(procH.histogram(), outputFile2);
	}
	
	
	public static void problemThree() {
		File outputFile1 = new File(WORK_DIR+"increased-intensity.png");
		File outputFile2 = new File(WORK_DIR+"increased-intensity-histogram.png");
		
		proc6.readWorkingImage(baseFile);
		proc6.stretchIntensity();
		proc6.writeWorkingImage(outputFile1);
		
		procH.readWorkingImage(outputFile1);
		ImageProcessor.writeImage(procH.histogram(), outputFile2);
	}
	
	
	public static void problemFour() {
		File outputFile1 = new File(WORK_DIR+"increased-saturation.png");
		File outputFile2 = new File(WORK_DIR+"increased-saturation-histogram.png");
		
		proc6.readWorkingImage(baseFile);
		proc6.increaseSaturation(0.1);
		proc6.writeWorkingImage(outputFile1);
		
		procH.readWorkingImage(outputFile1);
		ImageProcessor.writeImage(procH.histogram(), outputFile2);
	}
	
	public static void problemFive() {
		File outputFile1 = new File(WORK_DIR+"increased-contrast-decreased-saturation.png");
		File outputFile2 = new File(WORK_DIR+"increased-contrast-decreased-histogram.png");
		
		proc6.readWorkingImage(baseFile);
		proc6.stretchIntensity();
		proc6.increaseSaturation(-0.1);
		proc6.writeWorkingImage(outputFile1);
		
		procH.readWorkingImage(outputFile1);
		ImageProcessor.writeImage(procH.histogram(), outputFile2);
	}
	
	public static void testOne() {
		File outputFile1 = new File(WORK_DIR+"rotate-and-back.png");
		File outputFile2 = new File(WORK_DIR+"rotate-and-back-histogram.png");
		
		proc6.readWorkingImage(baseFile);
		proc6.rotateHue(Math.PI/6);
		proc6.rotateHue(-Math.PI/6);
		proc6.writeWorkingImage(outputFile1);
		
		procH.readWorkingImage(outputFile1);
		ImageProcessor.writeImage(procH.histogram(), outputFile2);
	}
}
