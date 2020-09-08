package lse;

import java.util.*;
import java.io.*;

/**
 * This class builds an index of keywords. Each keyword maps to a set of pages in
 * which it occurs, with frequency of occurrence in each page.
 *
 */
public class LittleSearchEngine {
	
	/**
	 * This is a hash table of all keywords. The key is the actual keyword, and the associated value is
	 * an array list of all occurrences of the keyword in documents. The array list is maintained in 
	 * DESCENDING order of frequencies.
	 */
	HashMap<String,ArrayList<Occurrence>> keywordsIndex;
	
	/**
	 * The hash set of all noise words.
	 */
	HashSet<String> noiseWords;
	
	/**
	 * Creates the keyWordsIndex and noiseWords hash tables.
	 */
	public LittleSearchEngine() {
		keywordsIndex = new HashMap<String,ArrayList<Occurrence>>(1000,2.0f);
		noiseWords = new HashSet<String>(100,2.0f);
	}
	
	/**
	 * Scans a document, and loads all keywords found into a hash table of keyword occurrences
	 * in the document. Uses the getKeyWord method to separate keywords from other words.
	 * 
	 * @param docFile Name of the document file to be scanned and loaded
	 * @return Hash table of keywords in the given document, each associated with an Occurrence object
	 * @throws FileNotFoundException If the document file is not found on disk
	 */
	public HashMap<String,Occurrence> loadKeywordsFromDocument(String docFile) 
	throws FileNotFoundException {
		
		HashMap<String,Occurrence> MyMap = new HashMap<String,Occurrence>();
		Scanner scan = new Scanner (new File(docFile));
		
			while(scan.hasNext()) {
				String str = scan.next();
				if (str.length() == 0) {
					continue;
				}
				
				String keyword = getKeyword(str);
				if(keyword == null) {
					continue;
				}
				
				Occurrence occur = MyMap.get(keyword);
				if (occur == null) {
					MyMap.put(keyword, new Occurrence(docFile, 1));
				}
				
				else 
				{
					occur.frequency++;
					MyMap.put(keyword, occur);
				}
		}
		scan.close();
		return MyMap;
	}
	
	/**
	 * Merges the keywords for a single document into the master keywordsIndex
	 * hash table. For each keyword, its Occurrence in the current document
	 * must be inserted in the correct place (according to descending order of
	 * frequency) in the same keyword's Occurrence list in the master hash table. 
	 * This is done by calling the insertLastOccurrence method.
	 * 
	 * @param kws Keywords hash table for a document
	 */
	public void mergeKeywords(HashMap<String,Occurrence> kws) {
		for(String keyword: kws.keySet()) {
			Occurrence occur = kws.get(keyword);
			
			if(!keywordsIndex.containsKey(keyword)) {
				ArrayList<Occurrence> OccurList = new ArrayList<Occurrence>();
				OccurList.add(occur);
				keywordsIndex.put(keyword, OccurList);
			}
			
			else
			{
			keywordsIndex.get(keyword).add(occur);
			insertLastOccurrence(keywordsIndex.get(keyword));
			}
		}
	}
	
	/**
	 * Given a word, returns it as a keyword if it passes the keyword test,
	 * otherwise returns null. A keyword is any word that, after being stripped of any
	 * trailing punctuation(s), consists only of alphabetic letters, and is not
	 * a noise word. All words are treated in a case-INsensitive manner.
	 * 
	 * Punctuation characters are the following: '.', ',', '?', ':', ';' and '!'
	 * NO OTHER CHARACTER SHOULD COUNT AS PUNCTUATION
	 * 
	 * If a word has multiple trailing punctuation characters, they must all be stripped
	 * So "word!!" will become "word", and "word?!?!" will also become "word"
	 * 
	 * See assignment description for examples
	 * 
	 * @param word Candidate word
	 * @return Keyword (word without trailing punctuation, LOWER CASE)
	 */
	public String getKeyword(String word) {
		boolean Findic = false;
		boolean Sindic = false;
		String Phold = word.toLowerCase();
		int negative = -1;
		
			for(int i = 0; i < Phold.length(); i++) {
				char C = Phold.charAt(i);
			
				if(Character.isLetter(C) && Findic == true) {
					return null;
				}
			
				if(!Character.isLetter(C)) {
					Findic = true;
				}
			
				if(!Character.isLetter(C) && Sindic == false) {
					Sindic = true;
					negative = i;
				}
		}
		
		if(negative == 0) {
			return null;
		}
		
		String NewWord = ""; //this is the string that will be concatenated with the substrings from the given string word
		
		if(negative == -1) {
			NewWord = Phold;
		}
		
		int Pholdptr = negative;
		
		if(negative > 0) {
			boolean good = true;
			while(Pholdptr < Phold.length() && good) {
				char C = Phold.charAt(Pholdptr);
					
				if(C != '!' && C != ';' && C != ':' && C != '?' && C != ',' && C != '.') {
					good = false;
				}
			Pholdptr++;
			}
			
			if(good) {
				NewWord = Phold.substring(0, negative);
			}
			
			else 
			{
				return NewWord = null;
			}
		}
		
		if(noiseWords.contains(NewWord)) {
			return null;
		}
		
		return NewWord;
	}
	
	/**
	 * Inserts the last occurrence in the parameter list in the correct position in the
	 * list, based on ordering occurrences on descending frequencies. The elements
	 * 0..n-2 in the list are already in the correct order. Insertion is done by
	 * first finding the correct spot using binary search, then inserting at that spot.
	 * 
	 * @param occs List of Occurrences
	 * @return Sequence of mid point indexes in the input list checked by the binary search process,
	 *         null if the size of the input list is 1. This returned array list is only used to test
	 *         your code - it is not used elsewhere in the program.
	 */
	public ArrayList<Integer> insertLastOccurrence(ArrayList<Occurrence> occs) {
		if(occs.size() == 1) {
			return null;
		}
		
		ArrayList<Integer> mids = new ArrayList<Integer>(); //this is the arraylist of midpoint indexes according to the description
		Occurrence Toccur = occs.get(occs.size() - 1); //last element in the occurrence array list
		//binary search needs min, max and mid. Min = first index, so 0.
		//max would be the size of the list - 2 since 
		//mid would be min and max summed divided by 2.
		
		int min = 0;
		int max = occs.size() - 2;
		int mid = (min + max) / 2;
		
		while(min <= max) {
			mid = (min + max) / 2;
			mids.add(mid);
			
			if(occs.get(mid).frequency == Toccur.frequency) {
				break;
			}
			
			else if (occs.get(mid).frequency > Toccur.frequency){
				min = mid + 1;
				continue;
			}
			
			else if (occs.get(mid).frequency < Toccur.frequency){
				max = mid - 1;
				continue;
			}
		}
		
		occs.add(mid + 1, occs.remove(occs.size() - 1));
		if(max < min) {
			occs.add(min, occs.remove(occs.size() - 1));
		}
		return mids;
	}
	
	/**
	 * This method indexes all keywords found in all the input documents. When this
	 * method is done, the keywordsIndex hash table will be filled with all keywords,
	 * each of which is associated with an array list of Occurrence objects, arranged
	 * in decreasing frequencies of occurrence.
	 * 
	 * @param docsFile Name of file that has a list of all the document file names, one name per line
	 * @param noiseWordsFile Name of file that has a list of noise words, one noise word per line
	 * @throws FileNotFoundException If there is a problem locating any of the input files on disk
	 */
	public void makeIndex(String docsFile, String noiseWordsFile) 
	throws FileNotFoundException {
		// load noise words to hash table
		Scanner sc = new Scanner(new File(noiseWordsFile));
		while (sc.hasNext()) {
			String word = sc.next();
			noiseWords.add(word);
		}
		
		// index all keywords
		sc = new Scanner(new File(docsFile));
		while (sc.hasNext()) {
			String docFile = sc.next();
			HashMap<String,Occurrence> kws = loadKeywordsFromDocument(docFile);
			mergeKeywords(kws);
		}
		sc.close();
	}
	
	/**
	 * Search result for "kw1 or kw2". A document is in the result set if kw1 or kw2 occurs in that
	 * document. Result set is arranged in descending order of document frequencies. 
	 * 
	 * Note that a matching document will only appear once in the result. 
	 * 
	 * Ties in frequency values are broken in favor of the first keyword. 
	 * That is, if kw1 is in doc1 with frequency f1, and kw2 is in doc2 also with the same 
	 * frequency f1, then doc1 will take precedence over doc2 in the result. 
	 * 
	 * The result set is limited to 5 entries. If there are no matches at all, result is null.
	 * 
	 * See assignment description for examples
	 * 
	 * @param kw1 First keyword
	 * @param kw1 Second keyword
	 * @return List of documents in which either kw1 or kw2 occurs, arranged in descending order of
	 *         frequencies. The result size is limited to 5 documents. If there are no matches, 
	 *         returns null or empty array list.
	 */
	public ArrayList<String> top5search(String kw1, String kw2) {
		ArrayList<String> Top5Res = new ArrayList<String>();
		
		kw1 = kw1.toLowerCase();
		kw2 = kw2.toLowerCase();
		
		ArrayList<Occurrence> L1 = keywordsIndex.get(kw1);
		ArrayList<Occurrence> L2 = keywordsIndex.get(kw2);
		
		//!keywordsIndex.containsKey(kw1) && (!keywordsIndex.containsKey(kw2))
		//this is the condition where both strings are not present, so they both are not keywords 
		//according to the above description, it will return null
		//otherwise the method ought to return the list of strings, so Top5Res.
		if((kw1 == null && kw2 == null) || (!keywordsIndex.containsKey(kw1) && (!keywordsIndex.containsKey(kw2)) || keywordsIndex.isEmpty())){
			System.out.println("The two strings were not present");
			return null;
		}
		
		//this is the condition where key2 is present but key1 isn't
		else if(keywordsIndex.containsKey(kw2) && (!keywordsIndex.containsKey(kw1))) {
			for(int i = 0; i < L2.size(); i++) {
				Occurrence occur = L2.get(i);
				if(Top5Res.size() < 5) { //less than 5 because the result size is limited to 5 docs
					Top5Res.add(occur.document);
				}
			}
			
			System.out.println("The second key is present, but the first key is not"); //print statement to make sure of what happened
			return Top5Res;
		}
		
		//this is the condition where key1 is present but key2 isn't
		else if(keywordsIndex.containsKey(kw1) && (!keywordsIndex.containsKey(kw2))) {
			for(int i = 0; i < L1.size(); i++) {
				Occurrence occur = L1.get(i);
				if(Top5Res.size() < 5) { //less than 5 because the result size is limited to 5 docs
					Top5Res.add(occur.document);
				}
			}
					
			System.out.println("The first key is present, but the second key is not"); //print statement to make sure of what happened
			return Top5Res;
		}
		
		//this is the condition where both strings given are keywords
		else 
		{
			ArrayList<Occurrence> occurs = new ArrayList<Occurrence>();
			occurs.addAll(keywordsIndex.get(kw1));
			occurs.addAll(keywordsIndex.get(kw2));
			for(int i = 0; i < 5 && !occurs.isEmpty(); i++) {
				int j = 0;
				int k = -1;
				for(j = 0; j < occurs.size() && occurs.get(j) != null; j++) {
					
					if(k == -1) {
						if(!Top5Res.contains(occurs.get(j).document)) {
							k = j;
						}
					}
					
					else if(occurs.get(j).frequency > occurs.get(k).frequency) {
						if(!Top5Res.contains(occurs.get(j).document)) {
							k = j;
						}
					}
					
					else if(occurs.get(j).frequency == occurs.get(k).frequency) {
						if(keywordsIndex.get(kw1).contains(occurs.get(j))) {
							if(!Top5Res.contains(occurs.get(j).document)) {
								k = j;
							}
						}
					}
					
					if(k != -1) {
						Top5Res.add(occurs.remove(k).document);
					}
				}
			}
		}
		return Top5Res;
	}
}
