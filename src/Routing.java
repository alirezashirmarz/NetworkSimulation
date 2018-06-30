import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Routing {
	public static List<Route> findRoutes(Network net, int fromNode, int toNode) {
		List<Route> routes = new ArrayList<>();
		Stack<Integer> path = new Stack<>();
		List<Integer> check = new ArrayList<Integer>();
		path.push(fromNode);
		check.addAll(net.getConnectedNodes(fromNode));
		while (check.size() > 0) {
			Integer node = check.get(check.size() - 1);
			if (node == path.peek()) {
				path.pop();
				check.remove(check.size() - 1);
			} else if (path.contains(node)) {
				check.remove(check.size() - 1);
			} else {
				path.push(node);
				if (node == toNode) {
					routes.add(new Route(net, path));
				} else {
					check.addAll(net.getConnectedNodes(node));
				}
			}
		}
		return routes;
	}
	
	public static void route(Network net, List<Traffic> traffics){
		
	}
}
