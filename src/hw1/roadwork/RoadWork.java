package hw1.roadwork;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;

public class RoadWork {

	private static final String OK = "YES";
	private static final String KO = "NO";
	private static final char CROSS = '+';
	private static final char BUILDING = 'c';
	private static final char BLANK = ' ';
	private static final int DOUBLE = 2;
	private static final double CAPACITY = 1.0;

	private static boolean hasNorthNode(int node, int width, char[][] cityCenter) {
		if (node <= width)
			return false;

		MatrixLocation loc = getVertexCoordinates(node, width);
		int row = loc.getRow();
		int col = loc.getCol();
		if (!validateMatrixLocation(new MatrixLocation(row - 1, col),
				cityCenter))
			return false;
		if (cityCenter[row - 1][col] == BLANK)
			return false;

		return true;
	}

	private static int getNorthNode(int node, int width) {
		return node - width;
	}

	private static boolean hasSouthNode(int node, int width, int height,
			char[][] cityCenter) {
		int maxNodeValue = height * width;
		if (node <= maxNodeValue && node > maxNodeValue - width)
			return false;

		MatrixLocation loc = getVertexCoordinates(node, width);
		int row = loc.getRow();
		int col = loc.getCol();
		if (!validateMatrixLocation(new MatrixLocation(row + 1, col),
				cityCenter))
			return false;
		if (cityCenter[row + 1][col] == BLANK)
			return false;

		return true;
	}

	private static int getSouthNode(int node, int width) {
		return node + width;
	}

	private static boolean hasEastNode(int node, int width, char[][] cityCenter) {
		int reminder = node % width;
		if (reminder == 0)
			return false;

		MatrixLocation loc = getVertexCoordinates(node, width);
		int row = loc.getRow();
		int col = loc.getCol();
		if (!validateMatrixLocation(new MatrixLocation(row, col + 1),
				cityCenter))
			return false;
		if (cityCenter[row][col + 1] == BLANK)
			return false;

		return true;
	}

	private static int getEastNode(int node) {
		return node + 1;
	}

	private static boolean hasWestNode(int node, int width, char[][] cityCenter) {
		int reminder = node % width;
		if (reminder == 1)
			return false;

		MatrixLocation loc = getVertexCoordinates(node, width);
		int row = loc.getRow();
		int col = loc.getCol();
		if (!validateMatrixLocation(new MatrixLocation(row, col - 1),
				cityCenter))
			return false;
		if (cityCenter[row][col - 1] == BLANK)
			return false;

		return true;
	}

	private static int getWestNode(int node) {
		return node - 1;
	}

	private static boolean validateMatrixLocation(MatrixLocation loc,
			char[][] cityCenter) {

		int row = loc.getRow();
		int col = loc.getCol();

		if (row < 0 || row >= cityCenter.length)
			return false;

		if (col < 0 || col >= cityCenter[0].length)
			return false;

		return true;

	}

	private static boolean isBoundary(int node, int width, int height) {

		if (node <= width)
			return true;

		int maxNodeValue = height * width;
		if (node <= maxNodeValue && node >= maxNodeValue - width)
			return true;

		int reminder = node % width;
		if (reminder == 0 || reminder == 1)
			return true;

		return false;
	}

	private static boolean isBoundary(int row, int col, int numRows, int numCols) {

		if (row == 0 || row == numRows - 1)
			return true;

		if (col == 0 || col == numCols - 1)
			return true;

		return false;

	}

	private static boolean isInVerticalRoad(char[][] cityCenter, int row,
			int col) {

		if (row == 0)
			return false;

		if (cityCenter[row - 1][col] == CROSS)
			return true;

		return false;
	}

	private static MatrixLocation getVertexCoordinates(int vertexNumber,
			int width) {
		int temp;
		double divResult;
		boolean multipleOfWidth = vertexNumber % width == 0;

		// DEBUG
		// System.out.print("mult: "+ multipleOfWidth + " ");

		if (multipleOfWidth)
			temp = vertexNumber / width;
		else {
			divResult = vertexNumber / width;

			// DEBUG
			// System.out.print("div: "+ divResult + " ");

			temp = ((int) divResult) + 1;
		}

		int rowIndex = temp - 1;

		// DEBUG
		// System.out.print("temp: "+ temp + " " + "rowIndex: "+rowIndex + " ");

		int lowerBound = width * rowIndex + 1;
		int colIndex = vertexNumber - lowerBound;
		return new MatrixLocation(rowIndex * 2, colIndex * 2);
	}

	private static int getVertexNumber(int row, int col, int width) {
		int rowIndex = row / 2;
		int colIndex = col / 2;
		int lowerBound = width * rowIndex + 1;
		return lowerBound + colIndex;
	}

	private static int getBuildingNorth(int row, int col, int width) {
		return getVertexNumber(row - 1, col, width);
	}

	private static int getBuildingSouth(int row, int col, int width) {
		return getVertexNumber(row + 1, col, width);
	}

	private static int getBuildingEast(int row, int col, int width) {
		return getVertexNumber(row, col + 1, width);
	}

	private static int getBuildingWest(int row, int col, int width) {
		return getVertexNumber(row, col - 1, width);
	}

	private static void printGrid(char[][] matrix) {
		System.out.println("city center:");
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				System.out.print(matrix[i][j]);
			}
			System.out.println();
		}
	}

	private static String printArray(char[] v) {
		String result = "";
		for (char x : v) {
			result += x + " ";
		}
		return result;
	}

	public static void main(String[] args) throws IOException {

		BufferedReader br;
		String line;
		String[] gridInfo;
		char[][] cityCenter;
		char[] cityCenterLine;
		int i, width, height, numLines, numColumns, numVertex, numBuildings, doubleVertex;
		FlowNetwork network;
		FlowEdge edge;
		FordFulkerson FFAlgorithm;
		double maxFlow;
		HashSet<MatrixLocation> buildingsLocations;

		br = new BufferedReader(new InputStreamReader(System.in));
		line = br.readLine();

		if (line != null) {

			gridInfo = line.split(" ", DOUBLE);
			height = Integer.parseInt(gridInfo[0]);
			width = Integer.parseInt(gridInfo[1]);

			numLines = (height * DOUBLE) - 1;
			numColumns = (width * DOUBLE) - 1;
			cityCenterLine = new char[numColumns];
			cityCenter = new char[numLines][];

			line = br.readLine();
			i = 0;
			numBuildings = 0;
			buildingsLocations = new HashSet<MatrixLocation>();
			while (i < numLines) {
				line.getChars(0, line.length(), cityCenterLine, 0);

				// DEBUG
				// System.out.println("\ncityCenterLine: " +
				// printArray(cityCenterLine));

				for (int c = 0; c < cityCenterLine.length; c++) {
					if (cityCenterLine[c] == BUILDING) {
						if (!isBoundary(i, c, numLines, numColumns)) {
							cityCenterLine[c] = BLANK;
							buildingsLocations.add(new MatrixLocation(i, c));
							numBuildings++;
						}
					}
				}

				cityCenter[i] = Arrays.copyOf(cityCenterLine,
						cityCenterLine.length);

				// DEBUG
				// System.out.println("city center["+ i + "]:" +
				// printArray(cityCenter[i]));

				if (i != numLines - 1)
					line = br.readLine();
				i++;
			}

			// DEBUG
			printGrid(cityCenter);
			System.out.println("\nbuildings: " + buildingsLocations);

			// numVertex = height * width + numBuildings + DOUBLE;
			doubleVertex = (height * width);
			numVertex = doubleVertex * DOUBLE + numBuildings + DOUBLE;
			network = new FlowNetwork(numVertex);

			// inserisco edge dei buildings
			i = 1;
			for (MatrixLocation loc : buildingsLocations) {
				int row = loc.getRow();
				int col = loc.getCol();
				int vertex = numVertex - 1 - i;

				edge = new FlowEdge(0, vertex, CAPACITY);
				network.addEdge(edge);

				if (isInVerticalRoad(cityCenter, row, col)) {
					int north = getBuildingNorth(row, col, width);
					int south = getBuildingSouth(row, col, width);

					edge = new FlowEdge(vertex, north, CAPACITY);
					network.addEdge(edge);
					edge = new FlowEdge(north + doubleVertex, vertex, CAPACITY);
					network.addEdge(edge);

					edge = new FlowEdge(vertex, south, CAPACITY);
					network.addEdge(edge);
					edge = new FlowEdge(south + doubleVertex, vertex, CAPACITY);
					network.addEdge(edge);

				} else {
					int east = getBuildingEast(row, col, width);
					int west = getBuildingWest(row, col, width);

					edge = new FlowEdge(vertex, east, CAPACITY);
					network.addEdge(edge);
					edge = new FlowEdge(east + doubleVertex, vertex, CAPACITY);
					network.addEdge(edge);

					edge = new FlowEdge(vertex, west, CAPACITY);
					network.addEdge(edge);
					edge = new FlowEdge(west + doubleVertex, vertex, CAPACITY);
					network.addEdge(edge);

				}

				i++;
			}

			// inserisco edge degli altri nodi
			for (i = 1; i < doubleVertex + 1; i++) {

				int duplicated = i + doubleVertex;

				edge = new FlowEdge(i, duplicated, CAPACITY);
				network.addEdge(edge);

				if (isBoundary(i, width, height)) {
					edge = new FlowEdge(duplicated, numVertex - 1, CAPACITY);
					network.addEdge(edge);
				}

				if (hasNorthNode(i, width, cityCenter)) {
					edge = new FlowEdge(duplicated, getNorthNode(i, width),
							CAPACITY);
					network.addEdge(edge);
				}
				if (hasSouthNode(i, width, height, cityCenter)) {
					edge = new FlowEdge(duplicated, getSouthNode(i, width),
							CAPACITY);
					network.addEdge(edge);
				}
				if (hasEastNode(i, width, cityCenter)) {
					edge = new FlowEdge(duplicated, getEastNode(i), CAPACITY);
					network.addEdge(edge);
				}
				if (hasWestNode(i, width, cityCenter)) {
					edge = new FlowEdge(duplicated, getWestNode(i), CAPACITY);
					network.addEdge(edge);
				}

			}

			// DEBUG
			System.out.println("\nnetwork:");
			StdOut.println(network);

			FFAlgorithm = new FordFulkerson(network, 0, numVertex - 1);
			maxFlow = FFAlgorithm.value();

			// THM: max num disjoint paths s->t = max flow
			if (numBuildings <= maxFlow)
				System.out.println(OK);
			else
				System.out.println(KO);

		}

	}

}

class MatrixLocation {

	private int row, col;

	public MatrixLocation(int row, int col) {
		super();
		this.row = row;
		this.col = col;
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + row;
		result = prime * result + col;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MatrixLocation other = (MatrixLocation) obj;
		if (row != other.row)
			return false;
		if (col != other.col)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "[" + row + ", " + col + "]";
	}

}