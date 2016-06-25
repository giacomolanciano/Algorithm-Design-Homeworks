package hw1.riddle;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class MainTester {
	private static  int[] tempMergArr;
    private static int[] array;
    private static int length;
	
	/*
	public static void main(String[]args){
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String str;
		
		int [] rows, cols;
		
		
		try {
			str = br.readLine();

			int n = Integer.parseInt(str);
			cols = new int[n];
			rows = new int[n];
			
			int[] array = new int[n+1];
			int[] array2 = new int[n+1];

			int sum1=0, sum2=0;
			
			for(int i=0; i<n; i++){
				rows[i]= Integer.parseInt(br.readLine());
				sum1 += rows[i];
				array[rows[i]]++;
			}
			for(int i=0; i<n; i++){
				cols[i]= Integer.parseInt(br.readLine());
				sum2 += cols[i];
				array2[cols[i]]++;
			}
						
			stampaArray(rows);
			System.out.println(sum1);
			stampaArray(cols);
			System.out.println(sum2);

			//stampaArray(array);
			//stampaArray(array2);

			
			if(sum2 <= sum1){
				sort(cols);
				sort2(rows);
				System.out.println("Max points: " + countMax(n,cols,rows, array));
			}
			else{
				sort(rows);
				sort2(cols);
				System.out.println("Max points: " + countMax(n,rows,cols, array2));
			}
			

			
		}
		catch(Exception e){
			e.printStackTrace();
		}	
	}
	*/
	
	
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
						
			stampaArray(rows);
			System.out.println(sum1);
			stampaArray(cols);
			System.out.println(sum2);

			//stampaArray(array);
			//stampaArray(array2);
			
			sort(cols);
			sort(rows);
			
			
			stampaArray(rows);
			System.out.println(sum1);
			stampaArray(cols);
			System.out.println(sum2);
			
			int count =0;
			
			if(sum2 <= sum1){
				int [] temp = cols;
				cols = rows;
				rows = temp;
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
			
			
			
			
			System.out.println("-----------------------------");
			
			stampaArray(rows);
			stampaArray(SBar);
			stampaArray(cols);
			
			
			int jolly = 0;
			
			
			for(int i=0; i< SBar.length; i++){
				
				
				if(SBar[i] <= cols[i] && jolly >0){
					if(jolly > cols[i]-SBar[i]){
						count += SBar[i];
						
						jolly -= cols[i]-SBar[i];
					}
					else{
						count += SBar[i];
						jolly = 0;
					}
					
				}
				else if(SBar[i] <= cols[i]  && jolly == 0){
					count += SBar[i];
				}
				else{
					jolly += SBar[i] - cols[i];
					count += SBar[i];
				}
				System.out.println("Max points: " + count);
				System.out.println("jolly: " + jolly);


			}
			
			
			//System.out.println("remainders: " + remainders);

			//count += remainders;
			System.out.println("Max points: " + (count-jolly));

			
		}
		catch(Exception e){
			e.printStackTrace();
		}	
	}
	
	public static String provaTest(BufferedReader br){
		String str;
		int count =0;

		
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
						
			
			sort(cols);
			sort(rows);
			
			
			
			if(sum2 <= sum1){
				int [] temp = cols;
				cols = rows;
				rows = temp;
				//System.out.println("invertiti");

				//stampaArray(rows);
				//stampaArray(cols);
			}
			

			
			int [] SBar = new int[rows.length];
			if(rows[0]>0)
				SBar[rows[0]-1]++; 
			else return 0 + "";
			
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
			
			/*
			int jolly = 0;
			
			
			for(int i=0; i< SBar.length; i++){
				
				
				if(SBar[i] <= cols[i] && jolly >0){
					if(jolly > cols[i]-SBar[i]){
						count += SBar[i];
						
						jolly -= cols[i]-SBar[i];
					}
					else{
						count += SBar[i];
						jolly = 0;
					}
					
				}
				else if(SBar[i] <= cols[i]  && jolly == 0){
					count += SBar[i];
				}
				else{
					jolly += SBar[i] - cols[i];
					count += SBar[i];
				}
				//System.out.println("Max points: " + count);
				//System.out.println("jolly: " + jolly);


			}
			
			
			count = count - jolly;
			*/
			
			
			
			
			//SOLUZIONE DEFINITIVA
			count = riddleCountMax(n, rows, cols, SBar);
			
			
			
			
		}
		catch(Exception e){
			e.printStackTrace();
		}	
		
		return count + "";

	}
	
	
	
	//SOLUZIONE DEFINITIVA
	private static int riddleCountMax(int n, int[] rows, int[] cols, int[] sBar) {
		int sumSBar = 0, sumCols = 0, count = 0;
		
		for(int i = 0; i < sBar.length; i++) {
			//System.out.println("iterazione " + i);

			count += cols[i];
			sumSBar += sBar[i];
			sumCols += cols[i];
			
			//DEBUG
			//System.out.println(" count:   " + count);
			//System.out.println(" sumCols: " + sumCols);
			//System.out.println(" sumSBar: " + sumSBar);
			
			if(sBar[i] < cols[i] && sumSBar < sumCols) {
				count -= sumCols - sumSBar;
				sumCols -= sumCols - sumSBar;
				
				//DEBUG
				//System.out.println("   count:   " + count);
				//System.out.println("   sumSBar: " + sumCols);

				
			}
		}
		return count;
	}
	
	
	
	
	
	
	
	
	
	
	/*
	public static String provaTest(BufferedReader br){
		String str;
		String res = "";
		int [] rows, cols;
		
		
		try {
			str = br.readLine();

			int n = Integer.parseInt(str);
			cols = new int[n];
			rows = new int[n];
			
			int[] array = new int[n+1];
			int[] array2 = new int[n+1];

			int sum1=0, sum2=0;
			
			for(int i=0; i<n; i++){
				rows[i]= Integer.parseInt(br.readLine());
				sum1 += rows[i];
				array[rows[i]]++;
			}
			for(int i=0; i<n; i++){
				cols[i]= Integer.parseInt(br.readLine());
				sum2 += cols[i];
				array2[cols[i]]++;
			}			
			

			//stampaArray(array);
			//stampaArray(array2);

			
			if(sum2 <= sum1){
				sort(cols);
				sort2(rows);
				res = countMax(n,cols,rows, array) + "";
			}
			else{
				sort(rows);
				sort2(cols);
				res = countMax(n,rows,cols, array2)+ "";
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return res;
	}
	*/
	
	
	public static int countMax(int n, int[]rows, int []cols, int[] array){
		
		//first number of sequence greater then zero
		int index = 0;
		while(cols[index] <= 0){
			index++;
		}
		
		int count=0, i=0, jolly=0;
		
		while(i<n && index < n){
			int remainders = n-index;
			
			if(rows[i] < remainders){
				count += rows[i];
				jolly += (remainders)-rows[i];
			}
			else if(rows[i] == remainders){
				count += rows[i];
			}
			else{
				if(jolly > rows[i]-remainders){
					count += rows[i];
					jolly -= rows[i]-remainders;
				}
				else{
					count += remainders +jolly;
					jolly = 0;
				}
			}
			
			int temp = cols[index];
			cols[index] -= (i+1);
			
			if(cols[index] == 0){
				index += array[temp];
			}
			else{
				cols[index] = temp;
			}
			i++;			
		}
		
		if(index >= n && i<n){
			while(i<n && jolly > 0){
				if(jolly > rows[i] ){
					count += rows[i];
					jolly -= rows[i];
					i++;
				}
				else{
					count += jolly;
					return count;
				}
			}
		}
		return count;
	}
	
	
	public static int minimo(int a, int b){
		if(a>b) return b;
		else return a;
	}
	
	public static int massimo(int a, int b){
		if(a>=b) return a;
		else return b;
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
    
    public static void sort2(int inputArr[]) {
        array = inputArr;
        length = inputArr.length;
        tempMergArr = new int[length];
        doMergeSort2(0, array.length - 1);
    }
 
	private static void doMergeSort2(int lowerIndex, int higherIndex) {
         
        if (lowerIndex < higherIndex) {
            int middle = lowerIndex + (higherIndex - lowerIndex) / 2;
            // Below step sorts the left side of the array
            doMergeSort2(lowerIndex, middle);
            // Below step sorts the right side of the array
            doMergeSort2(middle + 1, higherIndex);
            // Now merge both sides
            mergeParts2(lowerIndex, middle, higherIndex);
        }
    }
 
    private static void mergeParts2(int lowerIndex, int middle, int higherIndex) {
 
        for (int i = lowerIndex; i <= higherIndex; i++) {
            tempMergArr[i] = array[i];
        }
        int i = lowerIndex;
        int j = middle + 1;
        int k = lowerIndex;
        while (i <= middle && j <= higherIndex) {
            if (tempMergArr[i] <= tempMergArr[j]) {
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
