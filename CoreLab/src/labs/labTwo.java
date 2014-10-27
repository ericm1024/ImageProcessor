package labs;
import java.io.File;

public class labTwo {
	
	public static String WORK_DIR = "/Users/eric/Desktop/mudd_fall2014/lab/2/";
	public static File baseFile = new File(WORK_DIR+"haswell-e-die-shot.jpg");
	
	private static LabTwoProcessor proc = new LabTwoProcessor();
	
	public static void main(String args[]) {
		problemOne();
		problemTwo();
		problemThree();
	}
	
	public static void problemOne() {
		File outputFile = new File(WORK_DIR+"img1.png");
		proc.readWorkingImage(baseFile);
		
		proc.resize(0.75, 1.5);
		proc.writeWorkingImage(outputFile);
	}
	
	public static void problemTwo() {
		File outputFile = new File(WORK_DIR+"img2.png");
		proc.readWorkingImage(baseFile);
		
		proc.rotate(Math.PI/6);
		proc.writeWorkingImage(outputFile);
	}
	
	public static void problemThree() {
		File outputFile = new File(WORK_DIR+"img3.png");
		proc.readWorkingImage(baseFile);
		
		proc.rotate(-Math.PI/6);
		proc.writeWorkingImage(outputFile);
	}
	
}
