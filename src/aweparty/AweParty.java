package aweparty;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class AweParty {

	private static final String OK = "YES";
	private static final String KO = "NO";

	private static boolean checkConfiguration(int[] acquaintances) {

		MergeSort.mergeSort(acquaintances);

		// DEBUG
		// System.out.println(" ordinato: " + printArray(acquaintances));

		int i, peopleNum, sum, maxNumberOfAcq, currentValue;
		int w, b, s, c;
		peopleNum = acquaintances.length;
		maxNumberOfAcq = peopleNum - 1;

		sum = 0;

		for (i = 0; i < peopleNum; i++) {
			currentValue = acquaintances[i];

			if (currentValue > maxNumberOfAcq)
				return false;

			if (currentValue < 0)
				return false;

			sum += currentValue;
		}

		// DEBUG
		// System.out.println(" somma: " + sum);

		if (sum % 2 != 0)
			return false;

		w = acquaintances.length;
		b = 0;
		s = 0;
		c = 0;
		for (int k = 1; k <= peopleNum; k++) {

			b += acquaintances[k - 1];
			c += w - 1;

			while (w > k && acquaintances[w - 1] <= k) {
				s += acquaintances[w - 1];
				c -= k;
				w--;
			}

			if (b > c + s)
				return false;
			else if (w == k)
				return true;
		}

		return true;

	}

	public static void main(String[] args) throws IOException {

		BufferedReader br;
		String line;
		int i, j, numTests, numPersons;
		int[] acquaintances;
		String[] inputs, outputs;

		br = new BufferedReader(new InputStreamReader(System.in));
		line = br.readLine();

		if (line != null) {
			numTests = Integer.parseInt(line);
			outputs = new String[numTests];

			// DEBUG
			// long timeInit = System.currentTimeMillis();

			for (i = 0; i < numTests; i++) {

				// DEBUG
				// System.out.println("iterazione while dei test: " + i);

				line = br.readLine();
				numPersons = Integer.parseInt(line);
				acquaintances = new int[numPersons];

				line = br.readLine();
				inputs = line.split(" ", numPersons);

				for (j = 0; j < inputs.length; j++) {
					// DEBUG
					// System.out.println(" iterazione while parse array: "+j);

					acquaintances[j] = Integer.parseInt(inputs[j]);
				}

				if (checkConfiguration(acquaintances))
					outputs[i] = OK;
				else
					outputs[i] = KO;

			}

			// DEBUG
			// System.out.println("dimensioni output: " + outputs.length);

			for (int s = 0; s < outputs.length; s++)
				System.out.println(outputs[s]);

			// DEBUG
			// long timeFine = System.currentTimeMillis();
			// System.out.println("time: " + (timeFine-timeInit) + " ms");

		}

	}

}

class MergeSort {
	private static void mergeSort(int[] a, int[] vectorTemp, int left, int right) {
		if (left < right) {
			int center = (left + right) / 2;
			mergeSort(a, vectorTemp, left, center);
			mergeSort(a, vectorTemp, center + 1, right);
			merge(a, vectorTemp, left, center + 1, right);
		}
	}

	public static void mergeSort(int[] a) {
		int vectorTemp[];
		vectorTemp = new int[a.length];
		mergeSort(a, vectorTemp, 0, a.length - 1);
	}

	private static void merge(int[] a, int[] vectorAux, int posLeft,
			int posRight, int posEnd) {
		int endLeft = posRight - 1;
		int posAux = posLeft;
		int numElemen = posEnd - posLeft + 1;

		while (posLeft <= endLeft && posRight <= posEnd) {
			if ((a[posLeft]) >= (a[posRight])) // modificato (<) per avere
												// ordine decrescente
				vectorAux[posAux++] = a[posLeft++];
			else
				vectorAux[posAux++] = a[posRight++];
		}

		while (posLeft <= endLeft)
			vectorAux[posAux++] = a[posLeft++];

		while (posRight <= posEnd)
			vectorAux[posAux++] = a[posRight++];

		for (int i = 0; i < numElemen; i++, posEnd--)
			a[posEnd] = vectorAux[posEnd];
	}
}
