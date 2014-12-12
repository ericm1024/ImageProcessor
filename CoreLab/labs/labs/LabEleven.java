package labs;

import iproc.ImageProcessor;

import java.io.File;

import org.jtransforms.fft.DoubleFFT_2D;

public class LabEleven {
	public static String WORK_DIR = "/Users/eric/Desktop/mudd_fall2014/lab/11/";
	public static String OUT_DIR = WORK_DIR+"out/";
	private static File PANDA = new File(WORK_DIR+"panda.png");
	private ImageProcessor proc = null;
	
	public static void main(String args[]) {
		LabEleven lab = new LabEleven();
		lab.proc = new ImageProcessor(PANDA);
		lab.problemOne("panga");
	}
	
	// single pixel gumby
	private void problemOne(String name) {
		
		
		proc.writeWorkingImage(new File(OUT_DIR+name+"-edge-energy-.png"));
	}
}
