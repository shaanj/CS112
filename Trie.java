package trie;

import java.util.ArrayList;

/**
 * This class implements a Trie. 
 * 
 * @author Sesh Venugopal
 *
 */
public class Trie {
	
	// prevent instantiation
	private Trie() { }
	
	/**
	 * Builds a trie by inserting all words in the input array, one at a time,
	 * in sequence FROM FIRST TO LAST. (The sequence is IMPORTANT!)
	 * The words in the input array are all lower case.
	 * 
	 * @param allWords Input array of words (lowercase) to be inserted.
	 * @return Root of trie with all words inserted from the input array
	 */
	public static TrieNode buildTrie(String[] allWords) {
		
		//sees if the allWords array has at least one string
		if(allWords.length == 0) {
			return null;
		}
		
		//Creating the root of tree
		TrieNode root = new TrieNode(null, null, null);
		TrieNode ptr = null;
		
		//looping through the allWords array
		for(int i = 0; i < allWords.length; i++) {
			Indexes newIndex;
			
				if(i == 0) {
					newIndex = new Indexes(i, (short) 0, (short) (allWords[0].length() - 1));
					root.firstChild = new TrieNode(newIndex, null, null);
					ptr = root.firstChild;
				} 
				
				else 
				{
					ptr = root.firstChild; //this is the parent
					TrieNode prev = root;    //previous node that originally points to the root node
					TrieNode PrefPtr = null; //pointer to the prefix
					boolean gtChild = true;  //boolean to see if data goes to the child node
					boolean gtPrefix = false;//boolean to get the prefix
					boolean place = false;   //boolean for placing new data
					
						while(ptr != null) {
						//storing the longest common prefix
						String w1 = allWords[ptr.substr.wordIndex].substring(0, ptr.substr.endIndex+1);
						String w2 = allWords[i];
						
						int CLPrefix = ComPref(w1,w2); //ComPref functions to see if the prefixes from the 2 words
													   //have the same starting letters or not.
						
						//if the ComPref is -1, the words will become 
						//siblings with each other, since the first letter
						//in each word is not the same.
							if(CLPrefix == -1) {
								prev = ptr;
								ptr = ptr.sibling;
								gtChild = false;
							}
							
							
							else {
								if (CLPrefix - 1 == ptr.substr.endIndex) {
									prev = ptr;
									ptr = ptr.firstChild;
									gtChild = true;
									gtPrefix = true;
									PrefPtr = prev;
								}
								
								else {
									Indexes Pref = null;
									TrieNode word = null;
									
									if (gtPrefix == false) {
										Pref = new Indexes(ptr.substr.wordIndex, (short) 0, (short) (CLPrefix - 1));
										Indexes newAdd = new Indexes(i, (short) CLPrefix, (short) (allWords[i].length() - 1));
										Indexes updated = new Indexes(ptr.substr.wordIndex, (short) CLPrefix, (short) ptr.substr.endIndex);
										ptr = new TrieNode(updated, ptr.firstChild, ptr.sibling);
										word = new TrieNode(newAdd, null, null);
										
										TrieNode newPref = new TrieNode(Pref, ptr, ptr.sibling);
										ptr.sibling = word;
										if(gtChild == false) {
											prev.sibling = newPref;
										}
										
										else {
											prev.firstChild = newPref;
										}
										
										place = true;
										break;
									}
									
									else {
										if (CLPrefix - 1 != PrefPtr.substr.endIndex) {
											Pref = new Indexes(ptr.substr.wordIndex, (short) (PrefPtr.substr.endIndex + 1), (short) (CLPrefix - 1));
											Indexes newAdd = new Indexes(i, (short) CLPrefix, (short) (allWords[i].length() - 1));
											Indexes updated = new Indexes(ptr.substr.wordIndex, (short) CLPrefix, (short) ptr.substr.endIndex);
											ptr = new TrieNode(updated, ptr.firstChild, ptr.sibling);
											word = new TrieNode(newAdd, null, null);
											
											TrieNode newPref2 = new TrieNode(Pref, ptr, ptr.sibling);
											ptr.sibling = word;
											if(gtChild == false) {
												prev.sibling = newPref2;
											}
											else {
												prev.firstChild = newPref2;
											}
											place = true;
											break;
										}
										
										else {
											prev = ptr;
											ptr = ptr.sibling;
											gtChild = false;
										}
									}
								}
							}
						}
						if (place == false) {
							if (gtPrefix) {
								Indexes newAdd = new Indexes(i, (short) (PrefPtr.substr.endIndex + 1), (short) (allWords[i].length() - 1));
								TrieNode w2 = new TrieNode(newAdd, null, null);
								prev.sibling = w2;
							}
							
							else {
								Indexes newAdd = new Indexes(i, (short) 0, (short) (allWords[i].length() - 1));
								TrieNode w2 = new TrieNode(newAdd, null, null);
								prev.sibling = w2;
							}
						}
				}
		}
		return root;
	}
	//this is a helper method to find the largest common prefix in
	//two given words. If they are of varying sizes, it will go until 
	//the length of the shorter word.
	private static int ComPref(String word1, String word2) {
		int CPrefS = 0;
		int MinL = Math.min(word1.length(), word2.length());
		
		for (int i = 0; i < MinL; i++) {
			if (word1.charAt(0) != word2.charAt(0)) {
				return -1;
			}
			
			if(word1.charAt(i) == word2.charAt(i)) {
				CPrefS++;
			}
			
			else {
				return CPrefS;
			}
		}
		return CPrefS;
	}
	
	/**
	 * Given a trie, returns the "completion list" for a prefix, i.e. all the leaf nodes in the 
	 * trie whose words start with this prefix. 
	 * For instance, if the trie had the words "bear", "bull", "stock", and "bell",
	 * the completion list for prefix "b" would be the leaf nodes that hold "bear", "bull", and "bell"; 
	 * for prefix "be", the completion would be the leaf nodes that hold "bear" and "bell", 
	 * and for prefix "bell", completion would be the leaf node that holds "bell". 
	 * (The last example shows that an input prefix can be an entire word.) 
	 * The order of returned leaf nodes DOES NOT MATTER. So, for prefix "be",
	 * the returned list of leaf nodes can be either hold [bear,bell] or [bell,bear].
	 *
	 * @param root Root of Trie that stores all words to search on for completion lists
	 * @param allWords Array of words that have been inserted into the trie
	 * @param prefix Prefix to be completed with words in trie
	 * @return List of all leaf nodes in trie that hold words that start with the prefix, 
	 * 			order of leaf nodes does not matter.
	 *         If there is no word in the tree that has this prefix, null is returned.
	 */
	public static ArrayList<TrieNode> completionList(TrieNode root,
										String[] allWords, String prefix) {
		if(root == null) {
			return null;
		}
		
		//efficient search = skipping over certain subtrees
		//looking for the word burn, don't look in any subtree other 
		//than the one starting with B.
		
		TrieNode Temp = root;
		ArrayList<TrieNode> output = new ArrayList<TrieNode>();
		
		while(Temp != null) {
			
			if (Temp.substr == null) {
				Temp = Temp.firstChild;
			}
			
			String curr = allWords[Temp.substr.wordIndex];
			
			if (curr.indexOf(prefix) == 0 || prefix.indexOf(curr.substring(0, Temp.substr.endIndex + 1)) == 0) {
				
				if(Temp.firstChild != null) {
					output.addAll(completionList(Temp.firstChild, allWords, prefix));
					Temp = Temp.sibling;
				} 
				
				else 
				{
					output.add(Temp);
					Temp = Temp.sibling;
				}
			}
			
			else 
			{
				Temp = Temp.sibling;
			}
		}
		
		if(output.isEmpty()) {
			return null;
		}
		
		return output;
	}
	
	public static void print(TrieNode root, String[] allWords) {
		System.out.println("\nTRIE\n");
		print(root, 1, allWords);
	}
	
	private static void print(TrieNode root, int indent, String[] words) {
		if (root == null) {
			return;
		}
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		
		if (root.substr != null) {
			String pre = words[root.substr.wordIndex]
							.substring(0, root.substr.endIndex+1);
			System.out.println("      " + pre);
		}
		
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		System.out.print(" ---");
		if (root.substr == null) {
			System.out.println("root");
		} else {
			System.out.println(root.substr);
		}
		
		for (TrieNode ptr=root.firstChild; ptr != null; ptr=ptr.sibling) {
			for (int i=0; i < indent-1; i++) {
				System.out.print("    ");
			}
			System.out.println("     |");
			print(ptr, indent+1, words);
		}
	}
 }
