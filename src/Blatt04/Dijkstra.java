package Blatt04;

import java.util.*;

public class Dijkstra{
    WeightedGraph g;


    public Dijkstra(WeightedGraph g){
	this.g = g;
    }

    public HashMap<Integer, Integer> distFromStartVertex(Integer s){
	HashMap<Integer, Integer> dist =  new HashMap<Integer, Integer>();
	PriorityQueue<Integer> Q = new PriorityQueue<>((e1,e2) -> dist.get(e1)-dist.get(e2));
	HashSet<Integer> P = new HashSet<Integer>();
	dist.put(s,0);
	Q.add(s);
	while (! Q.isEmpty()){
	    Integer v =  Q.poll();
	    if (!P.contains(v)){
		P.add(v);
		for (int u: g.getNeighbors(v)){
		    int du = dist.get(v)+g.edgeLength(u,v);
		    if (dist.containsKey(u)){
			if (du<dist.get(u)) {
			    dist.put(u,du);
			    Q.add(u);
			}
		    }
		    else {
			dist.put(u,du);
			Q.add(u);
		    }
		}
	    }
	}
	
	return dist;
    }

    public Integer distFromStartToEnd(int s, int t){
	HashMap<Integer, Integer> dist =  new HashMap<Integer, Integer>();
	PriorityQueue<Integer> Q = new PriorityQueue<>((e1,e2) -> dist.get(e1)-dist.get(e2));
	HashSet<Integer> P = new HashSet<Integer>();
	dist.put(s,0);
	Q.add(s);
	while (! Q.isEmpty()){
	    Integer v =  Q.poll();
	    if (v.equals(t)){
		return dist.get(v);
	    }
	    //System.out.println("Extracted"+v);
	    if (!P.contains(v)){
		P.add(v);
		//System.out.println("Added"+v);
		for (Integer u: g.getNeighbors(v)){
		    //update of d-value for neighbors of v 
		    int du = dist.get(v)+g.edgeLength(u,v);
		    if (dist.containsKey(u)){
			if (du<dist.get(u)) {
			    dist.put(u,du);
			    //System.out.println(u+" "+du);
			    Q.add(u);
			}
		    }
		    else {
			dist.put(u,du);
			//System.out.println(u+" "+du);
			Q.add(u);
		    }
		}
	    }
	}
	return Integer.MAX_VALUE;

    }

	public Integer bidirectionalDijkstra(Integer s, Integer t) {
		//initialise dist, Q and P
		HashMap<Integer, Integer> distS = new HashMap<Integer, Integer>();
		HashMap<Integer, Integer> distT = new HashMap<Integer, Integer>();
		PriorityQueue<Integer> Qs = new PriorityQueue<>((e1, e2) -> distS.get(e1) - distS.get(e2));
		PriorityQueue<Integer> Qt = new PriorityQueue<>((e1, e2) -> distT.get(e1) - distT.get(e2));
		HashSet<Integer> Ps = new HashSet<Integer>();
		HashSet<Integer> Pt = new HashSet<Integer>();
		//initialise for s and t
		distS.put(s, 0);
		distT.put(t, 0);
		Qs.add(s);
		Qt.add(t);
		//while both queues are not empty
		while (!Qs.isEmpty()&&!Qt.isEmpty()) {
			//extract min from Qs and Qt
			Integer u;
			PriorityQueue tempQ;
			Set tempP;
			HashMap<Integer, Integer> tempDist;
			//check if both queues poll same element
			if (Qs.peek().equals(Qt.peek())) {
				//find min dist
				Integer minDist = distS.get(Qs.peek()) + distT.get(Qt.peek());
				for (Integer ps : Ps) {
					for (Integer pt : Pt) {
						if (g.adjacent(ps, pt)) {
							minDist=Math.min(minDist, distS.get(ps) + distT.get(pt) + g.edgeLength(ps, pt));
						}
					}
				}
				return minDist;
			}
			//check if peek element in other P
			if (Ps.contains(Qt.peek())) {
				Integer minDist = distS.get(Qt.peek()) + distT.get(Qt.peek());
				for (Integer ps : Ps) {
					for (Integer pt : Pt) {
						if (g.adjacent(ps, pt)) {
							minDist=Math.min(minDist, distS.get(ps) + distT.get(pt) + g.edgeLength(ps, pt));
						}
					}
				}
				return minDist;
			}
			if (Pt.contains(Qs.peek())) {
				Integer minDist = distS.get(Qs.peek()) + distT.get(Qs.peek());
				for (Integer ps : Ps) {
					for (Integer pt : Pt) {
						if (g.adjacent(ps, pt)) {
							minDist=Math.min(minDist, distS.get(ps) + distT.get(pt) + g.edgeLength(ps, pt));
						}
					}
				}
				return minDist;
			}


			//choose the queue with the smaller element
			else if (distS.get(Qs.peek())<distT.get(Qt.peek())){
				//select from Qs
				u = Qs.poll();
				tempQ = Qs;
				tempP = Ps;
				tempDist = distS;
			}
			else {
				//select from Qt
				u = Qt.poll();
				tempQ = Qt;
				tempP = Pt;
				tempDist = distT;
			}
			if (tempP.contains(u)) {
				continue;
			}
			tempP.add(u);

			for (Integer v : g.getNeighbors(u)) {
				//update of dist for neighbors of v
				int du = tempDist.get(u)+g.edgeLength(u,v);
				if (tempDist.containsKey(v)) {
					if (du < tempDist.get(v)) {
						tempDist.put(v, du);
						tempQ.add(v);
					}
				} else {
					tempDist.put(v, du);
					tempQ.add(v);
				}
			}
		}
		return Integer.MAX_VALUE;
	}

	public Integer bidirectionalDijkstraAlt(Integer s, Integer t) {
		//initialise dist, Q and P
		HashMap<Integer, Integer> distS = new HashMap<Integer, Integer>();
		HashMap<Integer, Integer> distT = new HashMap<Integer, Integer>();
		PriorityQueue<Integer> Qs = new PriorityQueue<>((e1, e2) -> distS.get(e1) - distS.get(e2));
		PriorityQueue<Integer> Qt = new PriorityQueue<>((e1, e2) -> distT.get(e1) - distT.get(e2));
		HashSet<Integer> Ps = new HashSet<Integer>();
		HashSet<Integer> Pt = new HashSet<Integer>();
		//initialise for s and t
		distS.put(s, 0);
		distT.put(t, 0);
		Qs.add(s);
		Qt.add(t);
		int count = 0;
		//while both queues are not empty
		while (!Qs.isEmpty()&&!Qt.isEmpty()) {
			//extract min from Qs and Qt
			Integer u;
			PriorityQueue tempQ;
			Set tempP;
			HashMap<Integer, Integer> tempDist;
			//check if both queues poll same element
			if (Qs.peek().equals(Qt.peek())) {
				//find min dist
				Integer minDist = distS.get(Qs.peek()) + distT.get(Qt.peek());
				for (Integer ps : Ps) {
					for (Integer pt : Pt) {
						if (g.adjacent(ps, pt)) {
							minDist=Math.min(minDist, distS.get(ps) + distT.get(pt) + g.edgeLength(ps, pt));
						}
					}
				}
			}

			//check if peek element in other P
			if (Ps.contains(Qt.peek())) {
				Integer minDist = distS.get(Qt.peek()) + distT.get(Qt.peek());
				for (Integer ps : Ps) {
					for (Integer pt : Pt) {
						if (g.adjacent(ps, pt)) {
							minDist=Math.min(minDist, distS.get(ps) + distT.get(pt) + g.edgeLength(ps, pt));
						}
					}
				}
				return minDist;
			}
			if (Pt.contains(Qs.peek())) {
				Integer minDist = distS.get(Qs.peek()) + distT.get(Qs.peek());
				for (Integer ps : Ps) {
					for (Integer pt : Pt) {
						if (g.adjacent(ps, pt)) {
							minDist=Math.min(minDist, distS.get(ps) + distT.get(pt) + g.edgeLength(ps, pt));
						}
					}
				}
				return minDist;
			}


			//choose the queue with the smaller element
			if (count++%2==0){
				//select from Qs
				u = Qs.poll();
				tempQ = Qs;
				tempP = Ps;
				tempDist = distS;
			}
			else {
				//select from Qt
				u = Qt.poll();
				tempQ = Qt;
				tempP = Pt;
				tempDist = distT;
			}
			if (tempP.contains(u)) {
				continue;
			}
			tempP.add(u);

			for (Integer v : g.getNeighbors(u)) {
				//update of dist for neighbors of v
				int du = tempDist.get(u)+g.edgeLength(u,v);
				if (tempDist.containsKey(v)) {
					if (du < tempDist.get(v)) {
						tempDist.put(v, du);
						tempQ.add(v);
					}
				} else {
					tempDist.put(v, du);
					tempQ.add(v);
				}
			}
		}
		return Integer.MAX_VALUE;
	}

    public Integer heuristicDijkstra(Integer s, Integer t) {
		if (g.degree(s)<g.degree(t)) {
			return distFromStartToEnd(s, t);
		}
		else {
			return distFromStartToEnd(t, s);
		}
	}

}
