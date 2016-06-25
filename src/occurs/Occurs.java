package occurs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Occurs {
	
	private static final int MODULO = 1000000;

	private String sequence, pattern;
	private int[][] table;

	public Occurs(String sequence, String pattern) {
		this.sequence = sequence;
		this.pattern = pattern;
	}

	public int countMatches() {
		table = new int[sequence.length() + 1][pattern.length() + 1];

		for (int row = 0; row < table.length; row++)
			for (int col = 0; col < table[row].length; col++)
				table[row][col] = countMatchesFor(row, col);

		return table[sequence.length()][pattern.length()];
	}

	private int countMatchesFor(int sequenceDigitsLeft, int patternDigitsLeft) {
		if (patternDigitsLeft == 0)
			return 1;

		if (sequenceDigitsLeft == 0)
			return 0;

		char currSequenceDigit = sequence.charAt(sequence.length() - sequenceDigitsLeft);
		char currPatternDigit = pattern.charAt(pattern.length() - patternDigitsLeft);

		int result = 0;

		if (currSequenceDigit == currPatternDigit)
			result += table[sequenceDigitsLeft - 1][patternDigitsLeft - 1];

		result += table[sequenceDigitsLeft - 1][patternDigitsLeft] ;

		return result % MODULO;
	}

	public static void main(String[] args) throws IOException {

		BufferedReader br;
		String line, sequence, pattern;
		int i, numTests;
		int[] outputs;
		Occurs problem;

		br = new BufferedReader(new InputStreamReader(System.in));
		line = br.readLine();

		if (line != null) {
			numTests = Integer.parseInt(line);
			outputs = new int[numTests];

			i = 1;
			while (i <= numTests) {

				// DEBUG
				//System.out.println("iterazione while dei test: " + i);

				line = br.readLine();
				sequence = line;

				// DEBUG
				//System.out.println(" sequence: " + sequence);

				line = br.readLine();
				pattern = line;

				// DEBUG
				//System.out.println(" pattern: " + pattern);

				problem = new Occurs(sequence, pattern);
				outputs[i - 1] = problem.countMatches();

				i++;

			}

			for (int o : outputs)
				System.out.println(o);

		}

	}

}
