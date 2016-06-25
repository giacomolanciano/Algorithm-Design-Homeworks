package riddle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import roadwork.FlowEdge;
import roadwork.FlowNetwork;
import roadwork.FordFulkerson;
import roadwork.StdOut;

public class RiddleMaxFlow {

	private static final double BASE_CAPACITY = 1.0;

	public static void main(String[] args) throws IOException {

		BufferedReader br;
		String line;
		FlowNetwork network;
		FlowEdge edge;
		FordFulkerson FFAlgorithm;
		double maxFlow;
		int n, numVertex, i, j, sumRowsCols, s, t;
		double currentCapacity;

		br = new BufferedReader(new InputStreamReader(System.in));
		line = br.readLine();

		if (line != null) {

			n = Integer.parseInt(line);
			sumRowsCols = n * 2;
			numVertex = sumRowsCols + 2; // = #rows + #cols + source + sink

			network = new FlowNetwork(numVertex);

			// rows
			for (i = 1; i <= n; i++) {
				line = br.readLine();
				currentCapacity = Double.parseDouble(line);

				edge = new FlowEdge(0, i, currentCapacity);
				network.addEdge(edge);

				for (j = n + 1; j <= sumRowsCols; j++) {
					edge = new FlowEdge(i, j, BASE_CAPACITY);
					network.addEdge(edge);
				}
			}

			// cols
			for (j = n + 1; j <= sumRowsCols; j++) {
				line = br.readLine();
				currentCapacity = Double.parseDouble(line);

				edge = new FlowEdge(j, numVertex - 1, currentCapacity);
				network.addEdge(edge);
			}

			// DEBUG
			System.out.println("\nnetwork:");
			StdOut.println(network);

			s = 0;
			t = numVertex - 1;
			FFAlgorithm = new FordFulkerson(network, s, t);
			maxFlow = FFAlgorithm.value();

			// DEBUG
			StdOut.println("Max flow from " + s + " to " + t + " is " + maxFlow);
			for (int v = 0; v < network.V(); v++) {
				for (FlowEdge e : network.adj(v)) {
					if ((v == e.from()) && e.flow() > 0)
						StdOut.println("   " + e);
				}
			}

		}
	}

}
