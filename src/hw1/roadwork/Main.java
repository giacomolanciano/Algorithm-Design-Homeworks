package hw1.roadwork;

public class Main {

	private static MatrixLocation getVertexCoordinates(int vertexNumber, int width) {
		int temp;
		double divResult;
		boolean multipleOfWidth = vertexNumber%width == 0;
		
		//DEBUG
		//System.out.print("mult: "+ multipleOfWidth + " ");
		
		if(multipleOfWidth)
			temp = vertexNumber/width;
		else {
			divResult = vertexNumber/width;
			
			//DEBUG
			//System.out.print("div: "+ divResult + " ");
			
			temp = ((int) divResult)+1;
		}
		
		int rowIndex = temp-1;
		
		//DEBUG
		System.out.print("temp: "+ temp + " " + "rowIndex: "+rowIndex + " ");
		
		int lowerBound = width * rowIndex + 1;
		int colIndex = vertexNumber-lowerBound;
		return new MatrixLocation(rowIndex*2, colIndex*2);
	}
	
	
	private static void add(int x) {
		x++;
	}
	
	
	public static void main(String[] args) {
		
		int width = 7;
		int heigth = 10;
		int limit = width*heigth;
		
		System.out.println("width: " + width + " heigth: " + heigth);
		
		for(int i = 1; i <= limit; i++) {
			
			System.out.println("coord of "+ i +": " + getVertexCoordinates(i, width));
			
			
		}
		
		
		System.out.println();
		System.out.println(1/3);
		
		
		System.out.println();
		
		int x = 10;
		System.out.println("before add, x = " + x);
		add(x);
		System.out.println("after add,  x = " + x);	
		
	
		
	}

}
