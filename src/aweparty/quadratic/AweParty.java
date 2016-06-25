package aweparty.quadratic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class AweParty {

	private static final String OK = "YES";
	private static final String KO = "NO";
	private static boolean checkConfiguration(int[] acquaintances) {

		// time complexity: O( (n/2)*(n+1) ) --> quadratic w.r.t. #nodes, but
		// linear w.r.t. #edges
		// (n/2)*(n+1)
		// = somma primi n numeri interi
		// = # massimo di edge di un grafo non diretto semplice, se n = #nodi

		MergeSort.mergeSort(acquaintances);

		// DEBUG
		//System.out.println(" ordinato: " + printArray(acquaintances));

		int i, peopleNum, sum, maxNumberOfAcq, minNumberOfAcq, maxValue, notZeros, zeros;
		peopleNum = acquaintances.length;
		maxNumberOfAcq = peopleNum - 1;
		
		
		maxValue = acquaintances[0];
		sum = 0;
		minNumberOfAcq = 0;
		notZeros = 0;

		for (i = 0; i < peopleNum; i++) {
			int currentValue = acquaintances[i];

			if (currentValue > maxNumberOfAcq)
				return false;

			if (currentValue != 0)
				notZeros++;

			if (currentValue == maxNumberOfAcq)
				minNumberOfAcq++;

			sum += currentValue;
		}

		if (sum % 2 != 0)
			return false;

		if (notZeros < maxValue + 1)
			return false;

		// DEBUG
		//System.out.println(" minNumberOfAcq: " + minNumberOfAcq);		
		

		for (int j = 0; j < peopleNum; j++) {
			if (acquaintances[j] < minNumberOfAcq)
				return false;
		}
		
		
		zeros = peopleNum - notZeros;
		i = 0;
		while (zeros != peopleNum) {

			// DEBUG
			//System.out.println("  iterazione for: " + i);
			
			
			if(i != 0)
				MergeSort.mergeSort(acquaintances);
			
			// DEBUG
			//System.out.println("  ordinato: " + printArray(acquaintances));
			
			
			int iterations = acquaintances[0];
			acquaintances[0] = 0;
			zeros++;

			if (acquaintances[iterations] == 0) {
				return false;
			} 
			
			
			int j = iterations, k = 1;
			while (j > 0) {

				// DEBUG
				//System.out.println("   iterazione while:  " + j);
				//System.out.println("   cella considerata: " + k);

				
					acquaintances[k]--;
					if(acquaintances[k] == 0)
						zeros++;
					
					k++;
					j--;

				
				// DEBUG
				//System.out.println("   array: " + printArray(acquaintances));

			}
			
			i++;

		}

		return true;

	}

	private static boolean areAllZeros(int[] array) {
		for (int i : array) {
			if (i != 0)
				return false;
		}
		return true;
	}

	private static String printArray(int[] v) {
		String result = "";
		for (int x : v) {
			result += x + " ";
		}
		return result;
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
			
			//DEBUG
			long timeInit = System.currentTimeMillis();

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

			//DEBUG
			long timeFine = System.currentTimeMillis();
			System.out.println("time: " + (timeFine-timeInit) + " ms");
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
