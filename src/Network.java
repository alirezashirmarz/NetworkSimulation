import java.io.FileNotFoundException;
import java.io.File;
import java.util.Scanner;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Network {
	public boolean[][] connectivity;
	public int[][] bandwidth;
	public int[][] cost;
	public double[][] delay;
	public double[][] lost;
	private int[][] usage;
	private int numNodes;

	public Network(int numNodes) {
		connectivity = new boolean[numNodes][numNodes];
		bandwidth = new int[numNodes][numNodes];
		cost = new int[numNodes][numNodes];
		delay = new double[numNodes][numNodes];
		lost = new double[numNodes][numNodes];
		usage = new int[numNodes][numNodes];
		this.numNodes = numNodes;
	}

	public void connect(int fromNode, int toNode, int bandwidth, int cost, double delay, double lost) {
		if (fromNode >= numNodes || toNode >= numNodes) {
			System.err.println("Error in Network.connect(): fromNode or toNode exceeds the number of nodes.");
		}
		connectivity[fromNode][toNode] = connectivity[toNode][fromNode] = true;
		this.bandwidth[fromNode][toNode] = this.bandwidth[toNode][fromNode] = bandwidth;
		this.cost[fromNode][toNode] = this.cost[toNode][fromNode] = cost;
		this.delay[fromNode][toNode] = this.delay[toNode][fromNode] = delay;
		this.lost[fromNode][toNode] = this.lost[toNode][fromNode] = lost;
	}

	public boolean isConnected(int fromNode, int toNode) {
		return connectivity[fromNode][toNode];
	}

	public List<Integer> getConnectedNodes(int node) {
		List<Integer> connectedNodes = new ArrayList<>();
		for (int i = 0; i < numNodes; i++) {
			if (connectivity[node][i] == true)
				connectedNodes.add(i);
		}
		return connectedNodes;
	}

	public void resetUsage() {
		for (int i = 0; i < numNodes; i++) {
			for (int j = 0; j < numNodes; j++) {
				usage[i][j] = 0;
			}
		}
	}

	public boolean requestFlow(Flow flow) {
		boolean possible = true; // request possibility
		double cumCost = 0; // cumulative cost
		double cumDelay = 0; // cumulative delay
		double cumLost = 0; // cumulative lost
		// check if flow is possible
		for (int i = 0; i < flow.nodes.size(); i++) {
			for (int j = 0; j < flow.nodes.get(i).size() - 1; j++) {
				int from = flow.nodes.get(i).get(j);
				int to = flow.nodes.get(i).get(j + 1);
				cumCost += cost[from][to] * ((double) flow.bandwidth * flow.allocates.get(i) / bandwidth[from][to]);
				cumDelay += delay[from][to];
				cumLost = 1 - ((1 - cumLost) * (1 - lost[from][to]));
				if (usage[from][to] + flow.bandwidth > bandwidth[from][to] || cumCost > flow.acceptCost
						|| cumDelay > flow.acceptDelay || cumLost > flow.acceptLost)
					possible = false;
			}
		}
		// if flow is possible, allocate requested bandwidth
		if (possible) {
			for (int i = 0; i < flow.nodes.size(); i++) {
				for (int j = 0; j < flow.nodes.get(i).size() - 1; j++) {
					int from = flow.nodes.get(i).get(j);
					int to = flow.nodes.get(i).get(j + 1);
					usage[from][to] += flow.bandwidth * flow.allocates.get(i);
				}
			}
		}
		return possible;
	}

	public void read(String filePath) {
		System.out.println("The read model is run!");
		String str1, str2;
		boolean flag;
		int counter;
		int bw, int_cost;
		double d_delay, d_lost;
		try {
			File file = new File(filePath);
			Scanner sc = new Scanner(file);
			while (sc.hasNextLine()) {
				str1 = sc.nextLine();
			
				if (str1.equals("====== The Number of Node  ========")) {

					str1 = sc.nextLine();
					System.out.println("The number of nodes is: " + str1);
					numNodes = Integer.parseInt(str1);
					str1 = sc.nextLine();
					
				}

				else if (str1.equals("====== Connectivity ========")) {
					str1 = sc.nextLine();
					str1 = sc.nextLine();
					str1 = str1.trim();
					
					for (int i = 0; i < numNodes; i++) {
						for (int j = 0; j < numNodes; j++) {
							if (!str1.equals("")) {

								str2 = str1.split(" ")[j];
								flag = str2.equals("1") ? true : false;
								connectivity[i][j] = flag;
								
							}

						}
						str1 = sc.nextLine();
						str1 = str1.trim();
					
					}

				} else if (str1.equals("====== Bandwidth ========")) {
					counter = 0;
					str1 = sc.nextLine();
					str1 = sc.nextLine();
					str1 = str1.trim();
					
					for (int i = 0; i < numNodes; i++) {
						for (int j = 0; j < str1.split(" ").length; j++) {
							if (!str1.equals("")) {

								str2 = str1.split(" ")[j];
								if (!str2.trim().equals("") && counter < numNodes) {
									bw = Integer.parseInt(str2);
									bandwidth[i][counter] = bw;
									counter++;
									
								}
							}

						}
						counter = 0;
						str1 = sc.nextLine();
						str1 = str1.trim();
						

					}

				} else if (str1.equals("====== Cost ========")) {

					counter = 0;
					str1 = sc.nextLine();
					str1 = sc.nextLine();
					str1 = str1.trim();
				
					for (int i = 0; i < numNodes; i++) {
						for (int j = 0; j < str1.split(" ").length; j++) {
							if (!str1.equals("")) {

								str2 = str1.split(" ")[j];
								if (!str2.trim().equals("") && counter < numNodes) {
									int_cost = Integer.parseInt(str2);
									cost[i][counter] = int_cost;
									counter++;
								
								}
							}

						}
						counter = 0;
						str1 = sc.nextLine();
						str1 = str1.trim();
						

					}

				} else if (str1.equals("====== Delay ========")) {

					counter = 0;
					str1 = sc.nextLine();
					str1 = sc.nextLine();
					str1 = str1.trim();
					
					for (int i = 0; i < numNodes; i++) {
						for (int j = 0; j < str1.split(" ").length; j++) {
							if (!str1.equals("")) {

								str2 = str1.split(" ")[j];
								if (!str2.trim().equals("") && counter < numNodes) {
									d_delay = Double.parseDouble(str2);
									delay[i][counter] = d_delay;
									counter++;
									
								}
							}

						}
						counter = 0;
						str1 = sc.nextLine();
						str1 = str1.trim();
						

					}

				} else if (str1.equals("====== Lost ========")) {

					counter = 0;
					str1 = sc.nextLine();
					str1 = sc.nextLine();
					str1 = str1.trim();
					
					for (int i = 0; i < numNodes; i++) {
						for (int j = 0; j < str1.split(" ").length; j++) {
							if (!str1.equals("")) {

								str2 = str1.split(" ")[j];
								if (!str2.trim().equals("") && counter < numNodes) {
									d_lost = Double.parseDouble(str2);
									lost[i][counter] = d_lost;
									counter++;
									
								}
							}

						}
						counter = 0;
						// System.out.println("this is i: " + i);
						if (i < numNodes - 1)
							str1 = sc.nextLine();
						else
							System.out.println("The Read of Doc is finished!");
						str1 = str1.trim();
						// System.out.println(str1);

					}

				}
			}
			sc.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void write(String filePath) {
		System.out.println("The random model is run!");
		try {
			PrintWriter writer = new PrintWriter(filePath);
			writer.println("====== The Number of Node  ========");
			writer.println(numNodes);
			System.out.println("The number of nodes is : "+numNodes);
			writer.println("\n====== Connectivity ========\n");
			for (int i = 0; i < numNodes; i++) {
				String line = "";
				for (int j = 0; j < numNodes; j++) {
					line += connectivity[i][j] == true ? " 1" : " 0";
				}
				writer.println(line);
			}
			writer.println("\n====== Bandwidth ========\n");
			for (int i = 0; i < numNodes; i++) {
				String line = "";
				for (int j = 0; j < numNodes; j++) {
					line += String.format(" %3d", bandwidth[i][j]);
				}
				writer.println(line);
			}
			writer.println("\n====== Cost ========\n");
			for (int i = 0; i < numNodes; i++) {
				String line = "";
				for (int j = 0; j < numNodes; j++) {
					line += String.format(" %3d", cost[i][j]);
				}
				writer.println(line);
			}
			writer.println("\n====== Delay ========\n");
			for (int i = 0; i < numNodes; i++) {
				String line = "";
				for (int j = 0; j < numNodes; j++) {
					line += String.format(" %.3f", delay[i][j]);
				}
				writer.println(line);
			}
			writer.println("\n====== Lost ========\n");
			for (int i = 0; i < numNodes; i++) {
				String line = "";
				for (int j = 0; j < numNodes; j++) {
					line += String.format(" %.3f", lost[i][j]);
				}
				writer.println(line);
			}
			System.out.println("The Random model was generated!");
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}
