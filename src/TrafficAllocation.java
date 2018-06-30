import java.util.*;
import java.util.List;
import java.util.Random;

public class TrafficAllocation {
	public static List<Flow> allocateToRandomPath(Network net, List<Traffic> traffics) {
		List<Flow> flows = new ArrayList<>();
		Random rand=new Random();
		for (Traffic traffic : traffics) {
			List<Route> routes = Routing.findRoutes(net, traffic.fromNode, traffic.toNode);
			int index = rand.nextInt(routes.size());
			flows.add(new Flow(traffic, routes.get(index)));
		}
		return flows;
	}

	public static List<Flow> allocateToShortestPath(Network net, List<Traffic> traffics) {
		List<Flow> flows = new ArrayList<>();
		for (Traffic traffic : traffics) {
			List<Route> routes = Routing.findRoutes(net, traffic.fromNode, traffic.toNode);
			int index = indexOfShortestRoute(routes);
			flows.add(new Flow(traffic, routes.get(index)));
		}
		return flows;
	}

	private static int indexOfShortestRoute(List<Route> routes) {
		int shortestLength = Integer.MAX_VALUE;
		int index = -1;
		for (int i=0; i<routes.size(); i++) {
			if (shortestLength > routes.get(i).nodes.size()) {
				shortestLength = routes.get(i).nodes.size();
				index = i;
			}
		}
		return index;
	}
}
