package hw1.riddle;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Riddle {
	
	private static  int[] tempMergArr;
    private static int[] array;
    private static int length;
	
	private static int riddleCountMax(int n, int[] rows, int[] cols, int[] sBar) {
		int sumSBar = 0, sumCols = 0, count = 0;
		
		for(int i = 0; i < sBar.length; i++) {
			System.out.println("iterazione " + i);

			count += cols[i];
			sumSBar += sBar[i];
			sumCols += cols[i];
			
			//DEBUG
			System.out.println(" count:   " + count);
			System.out.println(" sumCols: " + sumCols);
			System.out.println(" sumSBar: " + sumSBar);
			
			if(sBar[i] < cols[i] && sumSBar < sumCols) {
				count -= sumCols - sumSBar;
				sumCols -= sumCols - sumSBar;
				
				//DEBUG
				System.out.println("   count:   " + count);
				System.out.println("   sumSBar: " + sumCols);

				
			}
		}
		return count;
	}

	public static void main(String[]args){
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String str;
		
		int [] rows, cols;
		
		
		try {
			str = br.readLine();

			int n = Integer.parseInt(str);
			cols = new int[n];
			rows = new int[n];
			
			int sum1=0, sum2=0;
			
			for(int i=0; i<n; i++){
				rows[i]= Integer.parseInt(br.readLine());
				sum1 += rows[i];
			}
			for(int i=0; i<n; i++){
				cols[i]= Integer.parseInt(br.readLine());
				sum2 += cols[i];
			}
			
			//DEBUG
			stampaArray(rows);
			System.out.println(sum1);
			stampaArray(cols);
			System.out.println(sum2);

			//stampaArray(array);
			//stampaArray(array2);
			
			sort(cols);
			sort(rows);
			
			
			//DEBUG
			stampaArray(rows);
			System.out.println(sum1);
			stampaArray(cols);
			System.out.println(sum2);
			
			
			if(sum2 <= sum1){
				int [] temp = cols;
				cols = rows;
				rows = temp;
				
				//DEBUG
				System.out.println("invertiti");

				stampaArray(rows);
				stampaArray(cols);
			}
			

			
			int [] SBar = new int[rows.length];
			SBar[rows[0]-1]++; 
			//stampaArray(SBar);
			
			
			//cost of this cycle: O(2n)
			for(int i=1; i<SBar.length; i++){
				//System.out.println(rows[i]);
				if(rows[i]>0){
					if(rows[i] == rows[i-1]){
						SBar[rows[i]-1] = i+1;
					}
					else{
						int index=rows[i-1]-rows[i];
						//System.out.println(index);

						while(index>0){
							SBar[rows[i]+index-2] = SBar[rows[i-1]-1];
							index--;
						}
						SBar[rows[i]-1] = i+1;
					}
				}
				
				else if(rows[i]==0){
					/*
					int index=rows[SBar.length-1]-rows[i];
					while(index>0){
						SBar[rows[i]+index-2] = SBar[rows[i-1]-1];
						index--;
					}
					*/
					int index = 0;
					while (SBar[index] ==0){
						SBar[index] = i;
						index++;
					}
				}
				
				if(i==SBar.length-1 && rows[SBar.length-1] != 0 && SBar[0] == 0){
					int index = 0;
					while (SBar[index] ==0){
						SBar[index] = i+1;
						index++;
					}
				}
				
				
			}
			
			//DEBUG
			System.out.println("-----------------------------");
			System.out.print("R: ");
			stampaArray(rows);
			System.out.print("S: ");
			stampaArray(SBar);
			System.out.print("C: ");
			stampaArray(cols);
			
			

			//RESULT
			System.out.println("Max points: " + riddleCountMax(n, rows, cols, SBar));

			
		}
		catch(Exception e){
			e.printStackTrace();
		}	
	}	
	
	public static void stampaArray(int []v){
		for(int i=0; i<v.length; i++){
			
			System.out.print(v[i] + " ");
		}
		
		System.out.println();
	}
	
	
	public static void sort(int inputArr[]) {
        array = inputArr;
        length = inputArr.length;
        tempMergArr = new int[length];
        doMergeSort(0, array.length - 1);
    }
 
	private static void doMergeSort(int lowerIndex, int higherIndex) {
         
        if (lowerIndex < higherIndex) {
            int middle = lowerIndex + (higherIndex - lowerIndex) / 2;
            // Below step sorts the left side of the array
            doMergeSort(lowerIndex, middle);
            // Below step sorts the right side of the array
            doMergeSort(middle + 1, higherIndex);
            // Now merge both sides
            mergeParts(lowerIndex, middle, higherIndex);
        }
    }
 
    private static void mergeParts(int lowerIndex, int middle, int higherIndex) {
 
        for (int i = lowerIndex; i <= higherIndex; i++) {
            tempMergArr[i] = array[i];
        }
        int i = lowerIndex;
        int j = middle + 1;
        int k = lowerIndex;
        while (i <= middle && j <= higherIndex) {
            if (tempMergArr[i] >= tempMergArr[j]) {
                array[k] = tempMergArr[i];
                i++;
            } else {
                array[k] = tempMergArr[j];
                j++;
            }
            k++;
        }
        while (i <= middle) {
            array[k] = tempMergArr[i];
            k++;
            i++;
        }
 
    }


}
