package riddle;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class TesterTheory {

	public static void main(String... arg) throws IOException {
		String pathIn = "C:\\Users\\Giacomo\\Documents\\Università\\Algorithm Design\\homeworks\\01\\theory\\test\\in";
		String pathOut = "C:\\Users\\Giacomo\\Documents\\Università\\Algorithm Design\\homeworks\\01\\theory\\test\\out";
		listFilesForFolder(new File(pathIn), new File(pathOut), pathIn, pathOut);
	}

	public static void listFilesForFolder(File folderIn, File folderOut, String pathIn, String pathOut)
			throws IOException {
		File[] listOfFilesIn = folderIn.listFiles();
		File[] listOfFilesOut = folderOut.listFiles();
		int i;
		String in2;
		int testPassati=0;
		
		double tempoTotale=0;
		
		for (i = 0; i < listOfFilesIn.length; i++) {
			File fileIn = listOfFilesIn[i];
			File fileOut = listOfFilesOut[i];
			if (fileIn.isFile()) {
				System.out.println(fileIn.getName());
				FileReader in = new FileReader(pathIn + "/" + fileIn.getName());
				BufferedReader br = new BufferedReader(in);
				long startTime = System.currentTimeMillis();
				// pass BufferedReader object to roadwork algorithm instead of
				// BufferedReader br = new BufferedReader(new
				// InputStreamReader(System.in));
				// and return String object YES or NO.
				String mioOut = MainTester.provaTest(br);
				long endTime = System.currentTimeMillis();
				double duration = ((double) ((double) endTime - (double) startTime)); 
				
				tempoTotale = tempoTotale+duration;
				
				FileReader out = new FileReader(pathOut + "/" + fileOut.getName());
				br = new BufferedReader(out);
				String outProf = "";
				while ((in2 = br.readLine()) != null) {
					outProf = outProf + (in2);
				}
				in.close();
				out.close();
				int k = i + 1;
				
								
				if (mioOut.equals(outProf)){
					System.out.println("test: " + k + " time execution:" + duration + "ms" + " mioOut: " + mioOut
							+ " outputProf: " + outProf + "	\n-->UGUALE\n");
					testPassati++;
				}else{
					System.out.println("test: " + k + " time execution:" + duration + " mioOut: " + mioOut
							+ " outputProf: " + outProf + " \n-->DIVERSO\n");
				}
				
			}
		}
		
		System.out.println("\n\n*** TEST SUPERATI: "+testPassati+" ***");
		System.out.println("\n*** TEMPO TOTALE: "+tempoTotale+" ***");
	}

}
