package friends;

import java.util.ArrayList;

import structures.Queue;
import structures.Stack;


public class Friends {

	/**
	 * Finds the shortest chain of people from p1 to p2.
	 * Chain is returned as a sequence of names starting with p1,
	 * and ending with p2. Each pair (n1,n2) of consecutive names in
	 * the returned chain is an edge in the graph.
	 * 
	 * @param g Graph for which shortest chain is to be found.
	 * @param p1 Person with whom the chain originates
	 * @param p2 Person at whom the chain terminates
	 * @return The shortest chain from p1 to p2. Null or empty array list if there is no
	 *         path from p1 to p2
	 */
	public static ArrayList<String> shortestChain(Graph g, String p1, String p2) {
		ArrayList<String> shortchain = new ArrayList<String>();
		
		if(p1.contentEquals(p2)) {
			return shortchain;
			//this happens only when person 1 and person 2 match.
			//meaning that there is no path from p1 to p2, so return the empty 
			//array list 
		}
		
		//need boolean variables to know when p1 and p2 are seen in the members of g
		//they default to false to assume they are not there, only finding them will make
		//them true.
		
		boolean p1There = false;
		boolean p2There = false;
		
		//iterate through g to see if both p1 and p2 are present
		
		for(int i = 0; i < g.members.length; i++) {
			if(g.members[i].name.equals(p1)) {
				p1There = true;
			}
			
			if(g.members[i].name.equals(p2)) {
				p2There = true;
			}
		}
		
		//conditions if only one of them is present
		//there can't be a path from p1 to p2 if one of them is not present
		
		if(!p1There) {
			return shortchain;
		}
		
		if(!p2There) {
			return shortchain;
		}
		
		boolean seen[] = new boolean[g.members.length];
		int previous[] = new int[g.members.length];
		Queue<Integer> Squeue = new Queue<Integer>();
		int p1number = -1;
		int p2number = -1;
		
		for(int i = 0; i < g.members.length; i++) {
			if(g.members[i].name.equals(p1)) {
				p1number = i;
				previous[i] = -1;
				Squeue.enqueue(i);
				seen[i] = true;
				break;
			}
		}
		
		while(!Squeue.isEmpty()) {
			int curr = Squeue.dequeue();
			
			for(Friend f = g.members[curr].first; f != null; f = f.next) {
				if(!seen[f.fnum]) {
					previous[f.fnum] = curr;
					seen[f.fnum] = true;
					
					
					if(g.members[f.fnum].name.equals(p2)) {
						p2number = f.fnum;
						shortchain.add(g.members[p2number].name);
						break;
					}
					
					Squeue.enqueue(f.fnum);
				}
			}
			
			if(p2number != -1) {
				break;
			}
		}
		
		if(p2number == -1) {
			return shortchain;
		}
		
		int here = p2number;
		
		while(here != p1number) {
			here = previous[here];
			shortchain.add(0, g.members[here].name);
		}
		
		return shortchain;
	}
	
	/**
	 * Finds all cliques of students in a given school.
	 * 
	 * Returns an array list of array lists - each constituent array list contains
	 * the names of all students in a clique.
	 * 
	 * @param g Graph for which cliques are to be found.
	 * @param school Name of school
	 * @return Array list of clique array lists. Null or empty array list if there is no student in the
	 *         given school
	 */
	public static ArrayList<ArrayList<String>> cliques(Graph g, String school) {
		//make the variables for an arraylist of array lists to be returned
		//as well as a boolean variable to make sure that a student's school
		//is there.
		
		ArrayList<ArrayList<String>> Cliques = new ArrayList<ArrayList<String>>();
		boolean here = false;
		
		//this for loop will go through the members of the graph to see if they are students 
		//and to see if the school entered is valid
		
		for(int i = 0; i < g.members.length; i++) {
			if(g.members[i].student == true) {
				if(g.members[i].school.equals(school)){
					here = true;
					break;
				}
			}
		}	
		
		if(!here) {
			return Cliques;			//this condition means that the school was not found
		}
		
		boolean seen[] = new boolean[g.members.length];
		Queue<Integer> Squeue = new Queue<Integer>();
		
		for(int i = 0; i < g.members.length; i++) {
			ArrayList<String> currNames = new ArrayList<String>();
			
			if(g.members[i].student && !seen[i]) {
				if(g.members[i].school.equals(school)) {
					currNames.add(g.members[i].name);
					seen[i] = true;
					Squeue.enqueue(i);
				}
			}
			
			
			while(!Squeue.isEmpty()) {
				int curr = Squeue.dequeue();
				
				for(Friend f = g.members[curr].first; f != null; f = f.next) {
					if(!seen[f.fnum] && g.members[f.fnum].student && g.members[f.fnum].school.equals(school)) {
						seen[f.fnum] = true;
						currNames.add(g.members[f.fnum].name);
						
						if(curr != f.fnum){
							Squeue.enqueue(f.fnum);
						}
					}
				}
			}
			
			if(!currNames.isEmpty()) {
				Cliques.add(currNames);
			}
			
		}
	
		return Cliques;
		
	}
	
	/**
	 * Finds and returns all connectors in the graph.
	 * 
	 * @param g Graph for which connectors needs to be found.
	 * @return Names of all connectors. Null or empty array list if there are no connectors.
	 */
	public static ArrayList<String> connectors(Graph g) {
		ArrayList<String> Connectors = new ArrayList<String>();
		
		for(int i = 0; i < g.members.length; i++) {
			int FriendNum = 0;
			
			for(Friend f = g.members[i].first; f != null; f = f.next) {
				FriendNum++;
			}
			
			if(FriendNum == 1 && !Connectors.contains(g.members[g.members[i].first.fnum].name)) {
				Connectors.add(g.members[g.members[i].first.fnum].name);
			}
		}
		return Connectors;
		
	}
}

