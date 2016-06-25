package hw1.roadwork.allinone;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Pattern;

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

		if (multipleOfWidth)
			temp = vertexNumber / width;
		else {
			divResult = vertexNumber / width;

			temp = ((int) divResult) + 1;
		}

		int rowIndex = temp - 1;

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

	public static void main(String[] args) throws IOException {

		BufferedReader br;
		//Scanner sc;
		String line;
		String[] gridInfo;
		char[][] cityCenter;
		char[] cityCenterLine;
		int i, width, height, numLines, numColumns, numVertex, numBuildings, doubleVertex, row, col, vertex, duplicated;
		int north, south, east, west;
		FlowNetwork network;
		FordFulkerson FFAlgorithm;
		double maxFlow;
		HashSet<MatrixLocation> buildingsLocations;

		br = new BufferedReader(new InputStreamReader(System.in));
		line = br.readLine();
		
		//sc = new Scanner(System.in);
		//line = sc.nextLine();
		

		gridInfo = line.split(" ", DOUBLE);
		height = Integer.parseInt(gridInfo[0]);
		width = Integer.parseInt(gridInfo[1]);

		numLines = (height * DOUBLE) - 1;
		numColumns = (width * DOUBLE) - 1;
		cityCenterLine = new char[numColumns];
		cityCenter = new char[numLines][];

		line = br.readLine();
		
		//line = sc.nextLine();
		
		i = 0;
		numBuildings = 0;
		buildingsLocations = new HashSet<MatrixLocation>();
		while (i < numLines) {
			line.getChars(0, line.length(), cityCenterLine, 0);

			for (int c = 0; c < cityCenterLine.length; c++) {
				if (cityCenterLine[c] == BUILDING) {
					if (!isBoundary(i, c, numLines, numColumns)) {
						cityCenterLine[c] = BLANK;
						buildingsLocations.add(new MatrixLocation(i, c));
						numBuildings++;
					}
				}
			}

			cityCenter[i] = Arrays
					.copyOf(cityCenterLine, cityCenterLine.length);

			if (i != numLines - 1)
				line = br.readLine();
				//line = sc.nextLine();
			i++;
		}
		//sc.close();
		
		

		doubleVertex = (height * width);
		numVertex = doubleVertex * DOUBLE + numBuildings + DOUBLE;
		network = new FlowNetwork(numVertex);

		i = 1;
		for (MatrixLocation loc : buildingsLocations) {
			row = loc.getRow();
			col = loc.getCol();
			vertex = numVertex - 1 - i;

			network.addEdge(new FlowEdge(0, vertex, CAPACITY));

			if (isInVerticalRoad(cityCenter, row, col)) {
				north = getBuildingNorth(row, col, width);
				south = getBuildingSouth(row, col, width);

				network.addEdge(new FlowEdge(vertex, north, CAPACITY));
				
				network.addEdge(new FlowEdge(north + doubleVertex, vertex, CAPACITY));

				network.addEdge(new FlowEdge(vertex, south, CAPACITY));
				
				network.addEdge(new FlowEdge(south + doubleVertex, vertex, CAPACITY));


			} else {
				east = getBuildingEast(row, col, width);
				west = getBuildingWest(row, col, width);

				network.addEdge(new FlowEdge(vertex, east, CAPACITY));
				
				network.addEdge(new FlowEdge(east + doubleVertex, vertex, CAPACITY));
				
				network.addEdge(new FlowEdge(vertex, west, CAPACITY));
				
				network.addEdge(new FlowEdge(west + doubleVertex, vertex, CAPACITY));

			}

			i++;
		}

		for (i = 1; i < doubleVertex + 1; i++) {

			duplicated = i + doubleVertex;

			network.addEdge(new FlowEdge(i, duplicated, CAPACITY));

			if (isBoundary(i, width, height)) {
				network.addEdge(new FlowEdge(duplicated, numVertex - 1, CAPACITY));
			}

			if (hasNorthNode(i, width, cityCenter)) {
				network.addEdge(new FlowEdge(duplicated, getNorthNode(i, width),CAPACITY));
			}
			if (hasSouthNode(i, width, height, cityCenter)) {
				network.addEdge(new FlowEdge(duplicated, getSouthNode(i, width),CAPACITY));
			}
			if (hasEastNode(i, width, cityCenter)) {
				network.addEdge(new FlowEdge(duplicated, getEastNode(i), CAPACITY));
			}
			if (hasWestNode(i, width, cityCenter)) {
				network.addEdge(new FlowEdge(duplicated, getWestNode(i), CAPACITY));
			}

		}

		FFAlgorithm = new FordFulkerson(network, 0, numVertex - 1);
		maxFlow = FFAlgorithm.value();

		// THM: max num edge-disjoint paths s->t = max flow
		if (numBuildings <= maxFlow)
			System.out.println(OK);
		else
			System.out.println(KO);

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

class Bag<Item> implements Iterable<Item> {
	private Node<Item> first;
	private int N;

	private static class Node<Item> {
		private Item item;
		private Node<Item> next;
	}

	public Bag() {
		first = null;
		N = 0;
	}

	public boolean isEmpty() {
		return first == null;
	}

	public int size() {
		return N;
	}

	public void add(Item item) {
		Node<Item> oldfirst = first;
		first = new Node<Item>();
		first.item = item;
		first.next = oldfirst;
		N++;
	}

	public Iterator<Item> iterator() {
		return new ListIterator<Item>(first);
	}

	@SuppressWarnings("hiding")
	private class ListIterator<Item> implements Iterator<Item> {
		private Node<Item> current;

		public ListIterator(Node<Item> first) {
			current = first;
		}

		public boolean hasNext() {
			return current != null;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}

		public Item next() {
			if (!hasNext())
				throw new NoSuchElementException();
			Item item = current.item;
			current = current.next;
			return item;
		}
	}

}

class FlowEdge {
	private final int v; // from
	private final int w; // to
	private final double capacity; // capacity
	private double flow; // flow

	public FlowEdge(int v, int w, double capacity) {
		if (v < 0)
			throw new IndexOutOfBoundsException(
					"Vertex name must be a nonnegative integer");
		if (w < 0)
			throw new IndexOutOfBoundsException(
					"Vertex name must be a nonnegative integer");
		if (!(capacity >= 0.0))
			throw new IllegalArgumentException(
					"Edge capacity must be nonnegaitve");
		this.v = v;
		this.w = w;
		this.capacity = capacity;
		this.flow = 0.0;
	}

	public FlowEdge(int v, int w, double capacity, double flow) {
		if (v < 0)
			throw new IndexOutOfBoundsException(
					"Vertex name must be a nonnegative integer");
		if (w < 0)
			throw new IndexOutOfBoundsException(
					"Vertex name must be a nonnegative integer");
		if (!(capacity >= 0.0))
			throw new IllegalArgumentException(
					"Edge capacity must be nonnegaitve");
		if (!(flow <= capacity))
			throw new IllegalArgumentException("Flow exceeds capacity");
		if (!(flow >= 0.0))
			throw new IllegalArgumentException("Flow must be nonnnegative");
		this.v = v;
		this.w = w;
		this.capacity = capacity;
		this.flow = flow;
	}

	public FlowEdge(FlowEdge e) {
		this.v = e.v;
		this.w = e.w;
		this.capacity = e.capacity;
		this.flow = e.flow;
	}

	public int from() {
		return v;
	}

	public int to() {
		return w;
	}

	public double capacity() {
		return capacity;
	}

	public double flow() {
		return flow;
	}

	public int other(int vertex) {
		if (vertex == v)
			return w;
		else if (vertex == w)
			return v;
		else
			throw new IllegalArgumentException("Illegal endpoint");
	}

	public double residualCapacityTo(int vertex) {
		if (vertex == v)
			return flow; // backward edge
		else if (vertex == w)
			return capacity - flow; // forward edge
		else
			throw new IllegalArgumentException("Illegal endpoint");
	}

	public void addResidualFlowTo(int vertex, double delta) {
		if (vertex == v)
			flow -= delta; // backward edge
		else if (vertex == w)
			flow += delta; // forward edge
		else
			throw new IllegalArgumentException("Illegal endpoint");
		if (Double.isNaN(delta))
			throw new IllegalArgumentException("Change in flow = NaN");
		if (!(flow >= 0.0))
			throw new IllegalArgumentException("Flow is negative");
		if (!(flow <= capacity))
			throw new IllegalArgumentException("Flow exceeds capacity");
	}

	public String toString() {
		return v + "->" + w + " " + flow + "/" + capacity;
	}

}

class FlowNetwork {
	private static final String NEWLINE = System.getProperty("line.separator");

	private final int V;
	private int E;
	private Bag<FlowEdge>[] adj;

	@SuppressWarnings("unchecked")
	public FlowNetwork(int V) {
		if (V < 0)
			throw new IllegalArgumentException(
					"Number of vertices in a Graph must be nonnegative");
		this.V = V;
		this.E = 0;
		adj = (Bag<FlowEdge>[]) new Bag[V];
		for (int v = 0; v < V; v++)
			adj[v] = new Bag<FlowEdge>();
	}

	public FlowNetwork(int V, int E) {
		this(V);
		if (E < 0)
			throw new IllegalArgumentException(
					"Number of edges must be nonnegative");
		for (int i = 0; i < E; i++) {
			int v = StdRandom.uniform(V);
			int w = StdRandom.uniform(V);
			double capacity = StdRandom.uniform(100);
			addEdge(new FlowEdge(v, w, capacity));
		}
	}

	public FlowNetwork(In in) {
		this(in.readInt());
		int E = in.readInt();
		if (E < 0)
			throw new IllegalArgumentException(
					"Number of edges must be nonnegative");
		for (int i = 0; i < E; i++) {
			int v = in.readInt();
			int w = in.readInt();
			if (v < 0 || v >= V)
				throw new IndexOutOfBoundsException("vertex " + v
						+ " is not between 0 and " + (V - 1));
			if (w < 0 || w >= V)
				throw new IndexOutOfBoundsException("vertex " + w
						+ " is not between 0 and " + (V - 1));
			double capacity = in.readDouble();
			addEdge(new FlowEdge(v, w, capacity));
		}
	}

	public int V() {
		return V;
	}

	public int E() {
		return E;
	}

	private void validateVertex(int v) {
		if (v < 0 || v >= V)
			throw new IndexOutOfBoundsException("vertex " + v
					+ " is not between 0 and " + (V - 1));
	}

	public void addEdge(FlowEdge e) {
		int v = e.from();
		int w = e.to();
		validateVertex(v);
		validateVertex(w);
		adj[v].add(e);
		adj[w].add(e);
		E++;
	}

	public Iterable<FlowEdge> adj(int v) {
		validateVertex(v);
		return adj[v];
	}

	public Iterable<FlowEdge> edges() {
		Bag<FlowEdge> list = new Bag<FlowEdge>();
		for (int v = 0; v < V; v++)
			for (FlowEdge e : adj(v)) {
				if (e.to() != v)
					list.add(e);
			}
		return list;
	}

	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append(V + " " + E + NEWLINE);
		for (int v = 0; v < V; v++) {
			s.append(v + ":  ");
			for (FlowEdge e : adj[v]) {
				if (e.to() != v)
					s.append(e + "  ");
			}
			s.append(NEWLINE);
		}
		return s.toString();
	}

}

class FordFulkerson {
	private static final double FLOATING_POINT_EPSILON = 1E-11;

	private boolean[] marked; // marked[v] = true iff s->v path in residual
								// graph
	private FlowEdge[] edgeTo; // edgeTo[v] = last edge on shortest residual
								// s->v path
	private double value; // current value of max flow

	public FordFulkerson(FlowNetwork G, int s, int t) {
		validate(s, G.V());
		validate(t, G.V());
		if (s == t)
			throw new IllegalArgumentException("Source equals sink");
		if (!isFeasible(G, s, t))
			throw new IllegalArgumentException("Initial flow is infeasible");

		value = excess(G, t);
		while (hasAugmentingPath(G, s, t)) {

			double bottle = Double.POSITIVE_INFINITY;
			for (int v = t; v != s; v = edgeTo[v].other(v)) {
				bottle = Math.min(bottle, edgeTo[v].residualCapacityTo(v));
			}

			for (int v = t; v != s; v = edgeTo[v].other(v)) {
				edgeTo[v].addResidualFlowTo(v, bottle);
			}

			value += bottle;
		}

		assert check(G, s, t);
	}

	public double value() {
		return value;
	}

	public boolean inCut(int v) {
		validate(v, marked.length);
		return marked[v];
	}

	private void validate(int v, int V) {
		if (v < 0 || v >= V)
			throw new IndexOutOfBoundsException("vertex " + v
					+ " is not between 0 and " + (V - 1));
	}

	private boolean hasAugmentingPath(FlowNetwork G, int s, int t) {
		edgeTo = new FlowEdge[G.V()];
		marked = new boolean[G.V()];

		Queue<Integer> queue = new Queue<Integer>();
		queue.enqueue(s);
		marked[s] = true;
		while (!queue.isEmpty() && !marked[t]) {
			int v = queue.dequeue();

			for (FlowEdge e : G.adj(v)) {
				int w = e.other(v);

				if (e.residualCapacityTo(w) > 0) {
					if (!marked[w]) {
						edgeTo[w] = e;
						marked[w] = true;
						queue.enqueue(w);
					}
				}
			}
		}

		return marked[t];
	}

	private double excess(FlowNetwork G, int v) {
		double excess = 0.0;
		for (FlowEdge e : G.adj(v)) {
			if (v == e.from())
				excess -= e.flow();
			else
				excess += e.flow();
		}
		return excess;
	}

	private boolean isFeasible(FlowNetwork G, int s, int t) {

		for (int v = 0; v < G.V(); v++) {
			for (FlowEdge e : G.adj(v)) {
				if (e.flow() < -FLOATING_POINT_EPSILON
						|| e.flow() > e.capacity() + FLOATING_POINT_EPSILON) {
					System.err
							.println("Edge does not satisfy capacity constraints: "
									+ e);
					return false;
				}
			}
		}

		if (Math.abs(value + excess(G, s)) > FLOATING_POINT_EPSILON) {
			System.err.println("Excess at source = " + excess(G, s));
			System.err.println("Max flow         = " + value);
			return false;
		}
		if (Math.abs(value - excess(G, t)) > FLOATING_POINT_EPSILON) {
			System.err.println("Excess at sink   = " + excess(G, t));
			System.err.println("Max flow         = " + value);
			return false;
		}
		for (int v = 0; v < G.V(); v++) {
			if (v == s || v == t)
				continue;
			else if (Math.abs(excess(G, v)) > FLOATING_POINT_EPSILON) {
				System.err.println("Net flow out of " + v
						+ " doesn't equal zero");
				return false;
			}
		}
		return true;
	}

	private boolean check(FlowNetwork G, int s, int t) {

		if (!isFeasible(G, s, t)) {
			System.err.println("Flow is infeasible");
			return false;
		}

		if (!inCut(s)) {
			System.err.println("source " + s
					+ " is not on source side of min cut");
			return false;
		}
		if (inCut(t)) {
			System.err.println("sink " + t + " is on source side of min cut");
			return false;
		}

		double mincutValue = 0.0;
		for (int v = 0; v < G.V(); v++) {
			for (FlowEdge e : G.adj(v)) {
				if ((v == e.from()) && inCut(e.from()) && !inCut(e.to()))
					mincutValue += e.capacity();
			}
		}

		if (Math.abs(mincutValue - value) > FLOATING_POINT_EPSILON) {
			System.err.println("Max flow value = " + value
					+ ", min cut value = " + mincutValue);
			return false;
		}

		return true;
	}

}

final class In {

	private static final String CHARSET_NAME = "UTF-8";

	private static final Locale LOCALE = Locale.US;

	private static final Pattern WHITESPACE_PATTERN = Pattern
			.compile("\\p{javaWhitespace}+");

	private static final Pattern EMPTY_PATTERN = Pattern.compile("");

	private static final Pattern EVERYTHING_PATTERN = Pattern.compile("\\A");

	private Scanner scanner;

	public In() {
		scanner = new Scanner(new BufferedInputStream(System.in), CHARSET_NAME);
		scanner.useLocale(LOCALE);
	}

	public In(Socket socket) {
		if (socket == null)
			throw new NullPointerException("argument is null");
		try {
			InputStream is = socket.getInputStream();
			scanner = new Scanner(new BufferedInputStream(is), CHARSET_NAME);
			scanner.useLocale(LOCALE);
		} catch (IOException ioe) {
			throw new IllegalArgumentException("Could not open " + socket);
		}
	}

	public In(URL url) {
		if (url == null)
			throw new NullPointerException("argument is null");
		try {
			URLConnection site = url.openConnection();
			InputStream is = site.getInputStream();
			scanner = new Scanner(new BufferedInputStream(is), CHARSET_NAME);
			scanner.useLocale(LOCALE);
		} catch (IOException ioe) {
			throw new IllegalArgumentException("Could not open " + url);
		}
	}

	public In(File file) {
		if (file == null)
			throw new NullPointerException("argument is null");
		try {
			scanner = new Scanner(file, CHARSET_NAME);
			scanner.useLocale(LOCALE);
		} catch (IOException ioe) {
			throw new IllegalArgumentException("Could not open " + file);
		}
	}

	public In(String name) {
		if (name == null)
			throw new NullPointerException("argument is null");
		try {
			File file = new File(name);
			if (file.exists()) {
				scanner = new Scanner(file, CHARSET_NAME);
				scanner.useLocale(LOCALE);
				return;
			}

			URL url = getClass().getResource(name);

			if (url == null) {
				url = new URL(name);
			}

			URLConnection site = url.openConnection();

			InputStream is = site.getInputStream();
			scanner = new Scanner(new BufferedInputStream(is), CHARSET_NAME);
			scanner.useLocale(LOCALE);
		} catch (IOException ioe) {
			throw new IllegalArgumentException("Could not open " + name);
		}
	}

	public In(Scanner scanner) {
		if (scanner == null)
			throw new NullPointerException("argument is null");
		this.scanner = scanner;
	}

	public boolean exists() {
		return scanner != null;
	}

	public boolean isEmpty() {
		return !scanner.hasNext();
	}

	public boolean hasNextLine() {
		return scanner.hasNextLine();
	}

	public boolean hasNextChar() {
		scanner.useDelimiter(EMPTY_PATTERN);
		boolean result = scanner.hasNext();
		scanner.useDelimiter(WHITESPACE_PATTERN);
		return result;
	}

	public String readLine() {
		String line;
		try {
			line = scanner.nextLine();
		} catch (NoSuchElementException e) {
			line = null;
		}
		return line;
	}

	public char readChar() {
		scanner.useDelimiter(EMPTY_PATTERN);
		String ch = scanner.next();
		assert ch.length() == 1 : "Internal (Std)In.readChar() error!"
				+ " Please contact the authors.";
		scanner.useDelimiter(WHITESPACE_PATTERN);
		return ch.charAt(0);
	}

	public String readAll() {
		if (!scanner.hasNextLine())
			return "";

		String result = scanner.useDelimiter(EVERYTHING_PATTERN).next();
		// not that important to reset delimeter, since now scanner is empty
		scanner.useDelimiter(WHITESPACE_PATTERN); // but let's do it anyway
		return result;
	}

	public String readString() {
		return scanner.next();
	}

	public int readInt() {
		return scanner.nextInt();
	}

	public double readDouble() {
		return scanner.nextDouble();
	}

	public float readFloat() {
		return scanner.nextFloat();
	}

	public long readLong() {
		return scanner.nextLong();
	}

	public short readShort() {
		return scanner.nextShort();
	}

	public byte readByte() {
		return scanner.nextByte();
	}

	public boolean readBoolean() {
		String s = readString();
		if (s.equalsIgnoreCase("true"))
			return true;
		if (s.equalsIgnoreCase("false"))
			return false;
		if (s.equals("1"))
			return true;
		if (s.equals("0"))
			return false;
		throw new InputMismatchException();
	}

	public String[] readAllStrings() {

		String[] tokens = WHITESPACE_PATTERN.split(readAll());
		if (tokens.length == 0 || tokens[0].length() > 0)
			return tokens;
		String[] decapitokens = new String[tokens.length - 1];
		for (int i = 0; i < tokens.length - 1; i++)
			decapitokens[i] = tokens[i + 1];
		return decapitokens;
	}

	public String[] readAllLines() {
		ArrayList<String> lines = new ArrayList<String>();
		while (hasNextLine()) {
			lines.add(readLine());
		}
		return lines.toArray(new String[0]);
	}

	public int[] readAllInts() {
		String[] fields = readAllStrings();
		int[] vals = new int[fields.length];
		for (int i = 0; i < fields.length; i++)
			vals[i] = Integer.parseInt(fields[i]);
		return vals;
	}

	public double[] readAllDoubles() {
		String[] fields = readAllStrings();
		double[] vals = new double[fields.length];
		for (int i = 0; i < fields.length; i++)
			vals[i] = Double.parseDouble(fields[i]);
		return vals;
	}

	public void close() {
		scanner.close();
	}

	public static int[] readInts(String filename) {
		return new In(filename).readAllInts();
	}

	public static double[] readDoubles(String filename) {
		return new In(filename).readAllDoubles();
	}

	public static String[] readStrings(String filename) {
		return new In(filename).readAllStrings();
	}

	public static int[] readInts() {
		return new In().readAllInts();
	}

	public static double[] readDoubles() {
		return new In().readAllDoubles();
	}

	public static String[] readStrings() {
		return new In().readAllStrings();
	}

}

class Queue<Item> implements Iterable<Item> {
	private Node<Item> first; // beginning of queue
	private Node<Item> last; // end of queue
	private int N; // number of elements on queue

	private static class Node<Item> {
		private Item item;
		private Node<Item> next;
	}

	public Queue() {
		first = null;
		last = null;
		N = 0;
	}

	public boolean isEmpty() {
		return first == null;
	}

	public int size() {
		return N;
	}

	public Item peek() {
		if (isEmpty())
			throw new NoSuchElementException("Queue underflow");
		return first.item;
	}

	public void enqueue(Item item) {
		Node<Item> oldlast = last;
		last = new Node<Item>();
		last.item = item;
		last.next = null;
		if (isEmpty())
			first = last;
		else
			oldlast.next = last;
		N++;
	}

	public Item dequeue() {
		if (isEmpty())
			throw new NoSuchElementException("Queue underflow");
		Item item = first.item;
		first = first.next;
		N--;
		if (isEmpty())
			last = null; // to avoid loitering
		return item;
	}

	public String toString() {
		StringBuilder s = new StringBuilder();
		for (Item item : this)
			s.append(item + " ");
		return s.toString();
	}

	public Iterator<Item> iterator() {
		return new ListIterator<Item>(first);
	}

	@SuppressWarnings("hiding")
	private class ListIterator<Item> implements Iterator<Item> {
		private Node<Item> current;

		public ListIterator(Node<Item> first) {
			current = first;
		}

		public boolean hasNext() {
			return current != null;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}

		public Item next() {
			if (!hasNext())
				throw new NoSuchElementException();
			Item item = current.item;
			current = current.next;
			return item;
		}
	}

}

final class StdIn {

	private static final String CHARSET_NAME = "UTF-8";

	private static final Locale LOCALE = Locale.US;

	private static final Pattern WHITESPACE_PATTERN = Pattern
			.compile("\\p{javaWhitespace}+");

	private static final Pattern EMPTY_PATTERN = Pattern.compile("");

	private static final Pattern EVERYTHING_PATTERN = Pattern.compile("\\A");

	private static Scanner scanner;

	private StdIn() {
	}

	public static boolean isEmpty() {
		return !scanner.hasNext();
	}

	public static boolean hasNextLine() {
		return scanner.hasNextLine();
	}

	public static boolean hasNextChar() {
		scanner.useDelimiter(EMPTY_PATTERN);
		boolean result = scanner.hasNext();
		scanner.useDelimiter(WHITESPACE_PATTERN);
		return result;
	}

	public static String readLine() {
		String line;
		try {
			line = scanner.nextLine();
		} catch (NoSuchElementException e) {
			line = null;
		}
		return line;
	}

	public static char readChar() {
		scanner.useDelimiter(EMPTY_PATTERN);
		String ch = scanner.next();
		assert ch.length() == 1 : "Internal (Std)In.readChar() error!"
				+ " Please contact the authors.";
		scanner.useDelimiter(WHITESPACE_PATTERN);
		return ch.charAt(0);
	}

	public static String readAll() {
		if (!scanner.hasNextLine())
			return "";

		String result = scanner.useDelimiter(EVERYTHING_PATTERN).next();
		scanner.useDelimiter(WHITESPACE_PATTERN); // but let's do it anyway
		return result;
	}

	public static String readString() {
		return scanner.next();
	}

	public static int readInt() {
		return scanner.nextInt();
	}

	public static double readDouble() {
		return scanner.nextDouble();
	}

	public static float readFloat() {
		return scanner.nextFloat();
	}

	public static long readLong() {
		return scanner.nextLong();
	}

	public static short readShort() {
		return scanner.nextShort();
	}

	public static byte readByte() {
		return scanner.nextByte();
	}

	public static boolean readBoolean() {
		String s = readString();
		if (s.equalsIgnoreCase("true"))
			return true;
		if (s.equalsIgnoreCase("false"))
			return false;
		if (s.equals("1"))
			return true;
		if (s.equals("0"))
			return false;
		throw new InputMismatchException();
	}

	public static String[] readAllStrings() {

		String[] tokens = WHITESPACE_PATTERN.split(readAll());
		if (tokens.length == 0 || tokens[0].length() > 0)
			return tokens;

		String[] decapitokens = new String[tokens.length - 1];
		for (int i = 0; i < tokens.length - 1; i++)
			decapitokens[i] = tokens[i + 1];
		return decapitokens;
	}

	public static String[] readAllLines() {
		ArrayList<String> lines = new ArrayList<String>();
		while (hasNextLine()) {
			lines.add(readLine());
		}
		return lines.toArray(new String[0]);
	}

	public static int[] readAllInts() {
		String[] fields = readAllStrings();
		int[] vals = new int[fields.length];
		for (int i = 0; i < fields.length; i++)
			vals[i] = Integer.parseInt(fields[i]);
		return vals;
	}

	public static double[] readAllDoubles() {
		String[] fields = readAllStrings();
		double[] vals = new double[fields.length];
		for (int i = 0; i < fields.length; i++)
			vals[i] = Double.parseDouble(fields[i]);
		return vals;
	}

	static {
		resync();
	}

	private static void resync() {
		setScanner(new Scanner(new java.io.BufferedInputStream(System.in),
				CHARSET_NAME));
	}

	private static void setScanner(Scanner scanner) {
		StdIn.scanner = scanner;
		StdIn.scanner.useLocale(LOCALE);
	}

	public static int[] readInts() {
		return readAllInts();
	}

	public static double[] readDoubles() {
		return readAllDoubles();
	}

	public static String[] readStrings() {
		return readAllStrings();
	}

}

final class StdOut {

	private static final String CHARSET_NAME = "UTF-8";

	private static final Locale LOCALE = Locale.US;

	private static PrintWriter out;

	static {
		try {
			out = new PrintWriter(new OutputStreamWriter(System.out,
					CHARSET_NAME), true);
		} catch (UnsupportedEncodingException e) {
			System.out.println(e);
		}
	}

	private StdOut() {
	}

	public static void close() {
		out.close();
	}

	public static void println() {
		out.println();
	}

	public static void println(Object x) {
		out.println(x);
	}

	public static void println(boolean x) {
		out.println(x);
	}

	public static void println(char x) {
		out.println(x);
	}

	public static void println(double x) {
		out.println(x);
	}

	public static void println(float x) {
		out.println(x);
	}

	public static void println(int x) {
		out.println(x);
	}

	public static void println(long x) {
		out.println(x);
	}

	public static void println(short x) {
		out.println(x);
	}

	public static void println(byte x) {
		out.println(x);
	}

	public static void print() {
		out.flush();
	}

	public static void print(Object x) {
		out.print(x);
		out.flush();
	}

	public static void print(boolean x) {
		out.print(x);
		out.flush();
	}

	public static void print(char x) {
		out.print(x);
		out.flush();
	}

	public static void print(double x) {
		out.print(x);
		out.flush();
	}

	public static void print(float x) {
		out.print(x);
		out.flush();
	}

	public static void print(int x) {
		out.print(x);
		out.flush();
	}

	public static void print(long x) {
		out.print(x);
		out.flush();
	}

	public static void print(short x) {
		out.print(x);
		out.flush();
	}

	public static void print(byte x) {
		out.print(x);
		out.flush();
	}

	public static void printf(String format, Object... args) {
		out.printf(LOCALE, format, args);
		out.flush();
	}

	public static void printf(Locale locale, String format, Object... args) {
		out.printf(locale, format, args);
		out.flush();
	}

}

final class StdRandom {

	private static Random random; // pseudo-random number generator
	private static long seed; // pseudo-random number generator seed

	static {
		seed = System.currentTimeMillis();
		random = new Random(seed);
	}

	private StdRandom() {
	}

	public static void setSeed(long s) {
		seed = s;
		random = new Random(seed);
	}

	public static long getSeed() {
		return seed;
	}

	public static double uniform() {
		return random.nextDouble();
	}

	public static int uniform(int n) {
		if (n <= 0)
			throw new IllegalArgumentException("Parameter N must be positive");
		return random.nextInt(n);
	}

	public static double random() {
		return uniform();
	}

	public static int uniform(int a, int b) {
		if (b <= a)
			throw new IllegalArgumentException("Invalid range");
		if ((long) b - a >= Integer.MAX_VALUE)
			throw new IllegalArgumentException("Invalid range");
		return a + uniform(b - a);
	}

	public static double uniform(double a, double b) {
		if (!(a < b))
			throw new IllegalArgumentException("Invalid range");
		return a + uniform() * (b - a);
	}

	public static boolean bernoulli(double p) {
		if (!(p >= 0.0 && p <= 1.0))
			throw new IllegalArgumentException(
					"Probability must be between 0.0 and 1.0");
		return uniform() < p;
	}

	public static boolean bernoulli() {
		return bernoulli(0.5);
	}

	public static double gaussian() {
		double r, x, y;
		do {
			x = uniform(-1.0, 1.0);
			y = uniform(-1.0, 1.0);
			r = x * x + y * y;
		} while (r >= 1 || r == 0);
		return x * Math.sqrt(-2 * Math.log(r) / r);

	}

	public static double gaussian(double mu, double sigma) {
		return mu + sigma * gaussian();
	}

	public static int geometric(double p) {
		if (!(p >= 0.0 && p <= 1.0))
			throw new IllegalArgumentException(
					"Probability must be between 0.0 and 1.0");
		return (int) Math.ceil(Math.log(uniform()) / Math.log(1.0 - p));
	}

	public static int poisson(double lambda) {
		if (!(lambda > 0.0))
			throw new IllegalArgumentException(
					"Parameter lambda must be positive");
		if (Double.isInfinite(lambda))
			throw new IllegalArgumentException(
					"Parameter lambda must not be infinite");

		int k = 0;
		double p = 1.0;
		double L = Math.exp(-lambda);
		do {
			k++;
			p *= uniform();
		} while (p >= L);
		return k - 1;
	}

	public static double pareto() {
		return pareto(1.0);
	}

	public static double pareto(double alpha) {
		if (!(alpha > 0.0))
			throw new IllegalArgumentException(
					"Shape parameter alpha must be positive");
		return Math.pow(1 - uniform(), -1.0 / alpha) - 1.0;
	}

	public static double cauchy() {
		return Math.tan(Math.PI * (uniform() - 0.5));
	}

	public static int discrete(double[] a) {
		if (a == null)
			throw new NullPointerException("argument array is null");
		double EPSILON = 1E-14;
		double sum = 0.0;
		for (int i = 0; i < a.length; i++) {
			if (!(a[i] >= 0.0))
				throw new IllegalArgumentException("array entry " + i
						+ " must be nonnegative: " + a[i]);
			sum = sum + a[i];
		}
		if (sum > 1.0 + EPSILON || sum < 1.0 - EPSILON)
			throw new IllegalArgumentException(
					"sum of array entries does not approximately equal 1.0: "
							+ sum);

		// the for loop may not return a value when both r is (nearly) 1.0 and
		// when the
		// cumulative sum is less than 1.0 (as a result of floating-point
		// roundoff error)
		while (true) {
			double r = uniform();
			sum = 0.0;
			for (int i = 0; i < a.length; i++) {
				sum = sum + a[i];
				if (sum > r)
					return i;
			}
		}
	}

	public static double exp(double lambda) {
		if (!(lambda > 0.0))
			throw new IllegalArgumentException("Rate lambda must be positive");
		return -Math.log(1 - uniform()) / lambda;
	}

	public static void shuffle(Object[] a) {
		if (a == null)
			throw new NullPointerException("argument array is null");
		int N = a.length;
		for (int i = 0; i < N; i++) {
			int r = i + uniform(N - i); // between i and N-1
			Object temp = a[i];
			a[i] = a[r];
			a[r] = temp;
		}
	}

	public static void shuffle(double[] a) {
		if (a == null)
			throw new NullPointerException("argument array is null");
		int N = a.length;
		for (int i = 0; i < N; i++) {
			int r = i + uniform(N - i); // between i and N-1
			double temp = a[i];
			a[i] = a[r];
			a[r] = temp;
		}
	}

	public static void shuffle(int[] a) {
		if (a == null)
			throw new NullPointerException("argument array is null");
		int N = a.length;
		for (int i = 0; i < N; i++) {
			int r = i + uniform(N - i); // between i and N-1
			int temp = a[i];
			a[i] = a[r];
			a[r] = temp;
		}
	}

	public static void shuffle(Object[] a, int lo, int hi) {
		if (a == null)
			throw new NullPointerException("argument array is null");
		if (lo < 0 || lo > hi || hi >= a.length) {
			throw new IndexOutOfBoundsException("Illegal subarray range");
		}
		for (int i = lo; i <= hi; i++) {
			int r = i + uniform(hi - i + 1); // between i and hi
			Object temp = a[i];
			a[i] = a[r];
			a[r] = temp;
		}
	}

	public static void shuffle(double[] a, int lo, int hi) {
		if (a == null)
			throw new NullPointerException("argument array is null");
		if (lo < 0 || lo > hi || hi >= a.length) {
			throw new IndexOutOfBoundsException("Illegal subarray range");
		}
		for (int i = lo; i <= hi; i++) {
			int r = i + uniform(hi - i + 1); // between i and hi
			double temp = a[i];
			a[i] = a[r];
			a[r] = temp;
		}
	}

	public static void shuffle(int[] a, int lo, int hi) {
		if (a == null)
			throw new NullPointerException("argument array is null");
		if (lo < 0 || lo > hi || hi >= a.length) {
			throw new IndexOutOfBoundsException("Illegal subarray range");
		}
		for (int i = lo; i <= hi; i++) {
			int r = i + uniform(hi - i + 1); // between i and hi
			int temp = a[i];
			a[i] = a[r];
			a[r] = temp;
		}
	}

}
