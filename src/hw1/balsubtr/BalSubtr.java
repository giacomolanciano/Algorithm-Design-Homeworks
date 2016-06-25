package hw1.balsubtr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedHashSet;

public class BalSubtr {

	private Network network;
	private double[] subtreesSize;

	public BalSubtr(Network network) {
		this.network = network;
		this.subtreesSize = new double[network.getNodes() + 1];

		countSubtreesSize(network.getRoot(), this.subtreesSize);
	}

	public Network getNetwork() {
		return network;
	}

	public double[] getSubtreesSize() {
		return subtreesSize;
	}

	private static double countSubtreesSize(Node node, double[] subtreesSize) {
		int counter = 1;

		if (node.getChildren().size() == 0) {
			subtreesSize[node.getIndex()] = 1;

			// DEBUG
			// System.out.println("passo base");

			return 1;
		}

		// DEBUG
		// System.out.println("passo ricorsivo");

		for (Node n : node.getChildren()) {
			counter += countSubtreesSize(n, subtreesSize);
		}
		subtreesSize[node.getIndex()] = counter;
		return counter;
	}

	public static void main(String[] args) throws IOException {

		BufferedReader br;
		String line;
		int numNodes, first, second, i;
		Node[] nodes;
		Edge auxEdge;
		Edge[] edges;
		Network network;
		String[] endNodes = new String[2];
		Node father, son;

		br = new BufferedReader(new InputStreamReader(System.in));
		line = br.readLine();

		if (line != null) {

			numNodes = Integer.parseInt(line);
			nodes = new Node[numNodes + 1];
			edges = new Edge[numNodes];

			for (int j = 1; j <= numNodes; j++) {
				nodes[j] = new Node(j);
			}
			network = new Network(nodes[1], numNodes);

			line = br.readLine();
			i = 1;
			while (i <= numNodes - 1) {
				endNodes = line.split(" ", 2);

				// DEBUG
				// System.out.println("nell'array endNodes: " + endNodes[0] +
				// " " + endNodes[1]);

				first = Integer.parseInt(endNodes[0]);
				second = Integer.parseInt(endNodes[1]);

				if (first <= second) {
					auxEdge = new Edge(first, second);
				} else {
					auxEdge = new Edge(second, first);
				}

				// DEBUG
				// System.out.println("edge creato: " + auxEdge);

				edges[i] = auxEdge;
				father = nodes[auxEdge.getFather()];
				son = nodes[auxEdge.getSon()];

				father.addChild(son);

				if (i != numNodes - 1)
					line = br.readLine();
				i++;
			}

			BalSubtr problem = new BalSubtr(network);
			double max = 0;
			double current = 0;
			Edge maxEdge = null;
			double[] subtreesSize = problem.getSubtreesSize();

			// DEBUG
			// for(int k : subtreesSize) {
			// System.out.println("subtree: " + k);
			// }
			// System.out.println("\n");
			
			
			//DEBUG
			//double[] edgesValue = new double[numNodes];			

			i = 1;
			while (i < edges.length) {

				int sonIndex = edges[i].getSon();
				double subtreeSon = subtreesSize[sonIndex];
				double totalNodes = problem.getNetwork().getNodes();

				current = subtreeSon * (totalNodes - subtreeSon);

				// DEBUG
				// System.out.println("current: " + current);
				//edgesValue[i] = current;

				if (current > max) {
					max = current;
					maxEdge = edges[i];
				} else if (current == max && maxEdge != null) {
					if (edges[i].toString().compareTo(maxEdge.toString()) < 0)
						maxEdge = edges[i];
				}
				i++;
			}

			System.out.println(maxEdge);
			
			
			//DEBUG
//			System.out.println("\nLista max edge:");
//			for(i = 1; i < edgesValue.length; i++) {
//				if(edgesValue[i] == max)
//					System.out.println(edges[i]);
//			}

		}

	}

}

class Node {

	private int index;
	private LinkedHashSet<Node> children;

	public Node(int index) {
		this.index = index;
		this.children = new LinkedHashSet<>();
	}

	public int getIndex() {
		return index;
	}

	public LinkedHashSet<Node> getChildren() {
		return children;
	}

	public void addChild(Node n) {
		children.add(n);
	}


	@Override
	public String toString() {
		return "Node [index=" + index + ", childrenNum=" + children.size()
				+ "]";
	}

}

class Edge {

	private int father;
	private int son;

	public Edge(int father, int son) {
		this.father = father;
		this.son = son;
	}

	public int getFather() {
		return father;
	}

	public int getSon() {
		return son;
	}

	
	@Override
	public String toString() {
		return father + " " + son;
	}

}

class Network {

	private Node root;
	private int nodes;

	public Network(Node root, int nodes) {
		this.root = root;
		this.nodes = nodes;
	}

	public Node getRoot() {
		return root;
	}

	public int getNodes() {
		return nodes;
	}

	
	@Override
	public String toString() {
		return "Network [root=" + root + ", nodes=" + nodes + "]";
	}

}
