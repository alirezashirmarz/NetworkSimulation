import java.util.List;

public class Test {

	static boolean Read_File = false;
	static String file_path = "output/Net3.txt";

	public static void main(String[] args) {
		int numNodes = 10;
		int numConnections = 12; // this should be > n-1
		int minBandwidth = 10;
		int maxBandwidth = 100;
		int minCost = 1;
		int maxCost = 10;
		double minDelay = 0.001;
		double maxDelay = 0.1;
		double minLost = 0.001;
		double maxLost = 0.1;

		Network net = new Network(numNodes);
		if (Read_File == true)
			net.read(file_path);
		else {
			net = NetworkGenerator.generate(numNodes, numConnections, minBandwidth, maxBandwidth, minCost, maxCost,
					minDelay, maxDelay, minLost, maxLost);
			net.write(file_path);
		}

		int maxTime = 10;
		double reqRate = 0.5;
		int minReqBandwidth = minBandwidth / 3;
		int maxReqBandwidth = maxBandwidth / 3;
		double costRate = 0.5;
		int minAcceptCost = minCost * numNodes / 2;
		int maxAcceptCost = maxCost * numNodes / 2;
		double delayRate = 0.5;
		double minAcceptDelay = minDelay * numNodes / 2;
		double maxAcceptDelay = maxDelay * numNodes / 2;
		double lostRate = 0.5;
		double minAcceptLost = minLost * numNodes / 2;
		double maxAcceptLost = maxLost * numNodes / 2;

		List<Traffic> traffics = TrafficGenerator.generate(maxTime, numNodes, reqRate, minReqBandwidth, maxReqBandwidth,
				costRate, minAcceptCost, maxAcceptCost, delayRate, minAcceptDelay, maxAcceptDelay, lostRate,
				minAcceptLost, maxAcceptLost);
		TrafficGenerator.saveTraffic(traffics, "output/traffics3.txt");

		List<Route> routes = Routing.findRoutes(net, 1, 2);
		System.out.println(routes);

		List<Flow> flows = TrafficAllocation.allocateToShortestPath(net, traffics);
		int success = 0;
		int time = 0;
		for (int i = 0; i < traffics.size(); i++) {
			if (traffics.get(i).time != time) {
				time = traffics.get(i).time;
				net.resetUsage();
			}
			if (net.requestFlow(flows.get(i)) == true)
				success++;
		}
		System.out.println(success + " of " + traffics.size() + " requests successfully passed.");
		System.out.println("alireza version!!");
	}

}
