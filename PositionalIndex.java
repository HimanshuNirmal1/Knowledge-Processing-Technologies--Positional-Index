

import java.util.*;

/**
 * LBE04 Positional Index 
   Himanshu Nirmal
   ISTE 612
 */



public class PositionalIndex {
	// attributes
	private String[] myDocs;
	private ArrayList<String> termList;
	private ArrayList<ArrayList<Doc>> docLists;

	// constructor
	public PositionalIndex(String[] docs) {
		// #3
		myDocs = docs;
		termList = new ArrayList<String>();

		docLists = new ArrayList<ArrayList<Doc>>();
		ArrayList<Doc> docList;

		for (int i = 0; i < myDocs.length; i++) {
			String[] words = myDocs[i].split(" ");
			String word;

			for (int j = 0; j < words.length; j++) {
				boolean match = false;
				word = words[j];
				if (!termList.contains(word)) {
					termList.add(word);
					docList = new ArrayList<Doc>();
					Doc doc = new Doc(i, j);
					docList.add(doc);
					docLists.add(docList);
				} else {
					int index = termList.indexOf(word);
					docList = docLists.get(index);

					int k = 0;
					for (Doc did : docList) {
						if (did.docId == i) {
							did.insertPosition(j);
							docList.set(k, did);
							match = true;
							break;
						}
						k++;
					}
					if (!match) {
						Doc doc = new Doc(i, j);
						docList.add(doc);
					}

				}
			}
		}
	}

	// Two-term phrase query
	public ArrayList<Integer> intersect(String query) {
		ArrayList<Integer> intersectList = new ArrayList<Integer>();
		String[] q = query.split(" ");
		int i = 0;
//		ArrayList<Doc> qAL1 = docLists.get(termList.indexOf(q[0]));
//		ArrayList<Doc> qAL2 = docLists.get(termList.indexOf(q[1]));
		//System.out.println("termList"+termList);
		//System.out.println("docLists"+docLists);
		while (i < q.length - 1) {
			ArrayList<Doc> qAL1 = docLists.get(termList.indexOf(q[i]));
			ArrayList<Doc> qAL2 = docLists.get(termList.indexOf(q[i + 1]));
			
			int pAL1 = 0, pAL2 = 0;
			while (pAL1 < qAL1.size() && pAL2 < qAL2.size()) {
				if (qAL1.get(pAL1).docId == qAL2.get(pAL2).docId) {
					ArrayList<Integer> posAL1 = qAL1.get(pAL1).positionList;
					ArrayList<Integer> posAL2 = qAL2.get(pAL2).positionList;
					int pposAL1 = 0, pposAL2 = 0;
					
					while (pposAL1 < posAL1.size()) {
						while (pposAL2 < posAL2.size()) {
							if (posAL1.get(pposAL1) - posAL2.get(pposAL2) == -1 && i == q.length-2) {
								
								intersectList.add(qAL1.get(pAL1).docId);
								//System.out.println("intersectList"+intersectList);
								//i++;
								break;
							}
							pposAL2++;
						}
						pposAL1++;
					}
					pAL1++;
					pAL2++;
				} else if (qAL1.get(pAL1).docId < qAL2.get(pAL2).docId)
					pAL1++;
				else
					pAL2++;
			}
			i++;
		}
//		System.out.println("hmm" + qAL1);
//		System.out.println("hmm" + qAL2);
		//System.out.println("found in" + intersectList);
		return intersectList;
		// return null;
	}

	public String toString() {
		String outString = new String();
		for (int i = 0; i < termList.size(); i++) {
			outString += String.format("%-15s", termList.get(i));
			ArrayList<Doc> docList = docLists.get(i);
			for (int j = 0; j < docList.size(); j++) {
				outString += docList.get(j) + "\t";
			}
			outString += "\n";
		}
		return outString;
	}

	public static void main(String[] args) {
		String[] docs = { "text warehousing over big data", "dimensional data warehouse over big data",
				"nlp before text mining", "nlp before text classification" };

		PositionalIndex pi = new PositionalIndex(docs);
		System.out.println(pi);

		// String query1 = "over big data";
		String query = "text warehousing over big data";
		// #5
		ArrayList<Integer> searchResults	=	new ArrayList<Integer>();
		ArrayList<Integer> result = pi.intersect(query);
		// ArrayList<Integer> result1 = pi.intersect(query1);
		for(Integer i = 0; i<docs.length;i++)
		{
			String str=	docs[i];
			if(str.contains(query))
			{
				searchResults.add(i);
			}
		}
		for (Integer i : searchResults) {
			System.out.println(docs[i.intValue()]);

		}
		System.out.println("Found in : "+searchResults);
		/*
		 * for(Integer i:result1) { System.out.println(docs[i.intValue()]); }
		 */

	}
}


//Doc class
class Doc {
	int docId;
	ArrayList<Integer> positionList;

	public Doc(int did, int position) {
		// #1
		docId = did;
		positionList = new ArrayList<Integer>();
		positionList.add(new Integer(position));
	}

	public void insertPosition(int position) {
		// #2
		positionList.add(new Integer(position));
	}

	public String toString() {
		String docIdString = docId + ":<";
		for (Integer pos : positionList) {
			docIdString += pos + ",";
		}
		docIdString = docIdString.substring(0, docIdString.length() - 1) + ">";
		return docIdString;
	}
}