package target_decoy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.math.BigDecimal;
import java.math.RoundingMode;
/**
 * Contains tools for counting.
 * @author junghyey
 *
 */
public final class Counter {
	
	/**
	 * Generates a dictionary where keys are alphabet A to Z
	 * and values are all initialized to zero.
	 * @return aminoCountDict	a dictionary [keys]: alphabet A to Z
	 * 									   	 [value]: 0 
	 */
	public static  Map<String, Integer> getEmptyCount(){
	
		Map<String, Integer> aminoCountDict = new HashMap<String, Integer>();
		
		for(char c = 'A'; c <= 'Z'; c++) {
			aminoCountDict.put(Character.toString(c), 0);
		}//for c
		
		/* Confirm post condition: 1. There are 26 entries in the count dictionary
		 * 						   2. All entry values are 0
		 */
		Set<String> keys = aminoCountDict.keySet();
		assert(keys.size() == 26 ); // post condition 1.
		for (String key: keys) {//post condition 2.
			assert(aminoCountDict.get(key) == 0);
		}// for post condition 2.
		
		return aminoCountDict;	
	}//getEmptyCount
	
	/**
	 * Gets the abundance of each amino acid across a given protein sequence data.
	 * @param proteinSeqDict	a dictionary where each entry has a form:
	 * 							[Key: proteinID , Value: protein amino acid sequence]
	 * @return aminoCountDict	a dictionary [keys]: amino acid (alphabet A to Z)
	 * 										 [value]: total count indicating how many
	 * 										 the [key] amino acid there are in the set of 
	 * 										 all the proteins in proteinSeqDict.
	 */
	
	public static Map<String, Integer> countAminoAcid (Map<String, String> proteinSeqDict) {
		
		Map<String, Integer> aminoCountDict = getEmptyCount();
		Set<String> idSet = proteinSeqDict.keySet();
	
		/* Count how many the [key] amino acid there are in the set of 
		 *  all the proteins in proteinSeqDict. 
		 */
		for (String proteinID: idSet) { 
			
			/* 1. Get the amino acid sequence of the protein*/
			String aminoAcidSeq = proteinSeqDict.get(proteinID);
			char [] aminoAcidArr = aminoAcidSeq.toCharArray();
			
			/* 2. Get the abundance of each amino acid */
			for (char aminoAcid: aminoAcidArr) { 
				aminoCountDict.put(Character.toString(aminoAcid), aminoCountDict.get(Character.toString(aminoAcid)) + 1);
			}//for aminoAcid
		}// for keys
		

		/*Confirm post condition: There are 26 entries in the count dictionary. */
		assert(aminoCountDict.keySet().size() == 26);
		return aminoCountDict;
	}// count
	

	/**
	 * Gets the total number of amino acid in a given protein sequence data.
	 * @param protein_seq_dict	a dictionary where each entry has a form:
	 * 							[proteinID : protein amino acid sequence]
	 * @return totalCount	    the total number of amino acid(s)
	 */
	public static int getTotalAminoCount(Map<String, String> proteinSeqDict) {
		
	Set<String> keys = proteinSeqDict.keySet();
	int totalCount = 0; //initialize total count
		
		for (String key: keys) { 
			String aminoAcidSeq = proteinSeqDict.get(key); //get the protein amino acid sequence
			totalCount += aminoAcidSeq.length(); //accumulate the length of the protein sequence
		}// for keys
		
		return totalCount;
	}//getTotal
	
	
	

	/**
	 * Calculates a proportion (decimal) of each amino acid (A-Z) in a protein sequence data.
	 * @param proteinSeqDict  protein sequence data (each entry's form: [proteinID : target protein sequence])
	 * @param sigFig		  decides how many decimal places to keep 
	 * @throws IllegalArgumentException if sigFig  <= 0
	 * @return proportionDict proportion dictionary of each amino acid in the given data 
	 * 						  (each entry's form: [amino acid : proportion(number of decimal places: sigFig)) ])
	 * 						  and additional entry ([Total : total proportion])
	 */
	public static Map<String, Double> getProportion(Map<String, String> proteinSeqDict, int sigFig){
		
		/*Enforce the precondition: The number of significant figures should be greater than 0*/
		if(sigFig <= 0)
			throw new IllegalArgumentException("The number of significant figures should be greater than 0");
		
		/* Get total amino acid count & count of each amino acid */
		Map<String, Integer> countDict = countAminoAcid(proteinSeqDict);
		int total = getTotalAminoCount(proteinSeqDict);
		Set<String> aminoAcids = countDict.keySet();
		Map<String ,Double> proportionDict = new HashMap<String, Double> ();
		double totalProportion = 0;
		/*
		 * Calculate the proportion of each amino acid in the given protein
		 * sequence data (in dictionary format)
		 */
		for (String aminoAcid: aminoAcids) {
			double count = countDict.get(aminoAcid); //get as double to calculate proportion 
			
			/* Save the result [amino acid : proportion (sigFig decimal places) ]*/
			BigDecimal proportion = new BigDecimal(count / total);
			double proportionSigFig = proportion.setScale(sigFig , RoundingMode.HALF_UP).doubleValue();
			proportionDict.put(aminoAcid,proportionSigFig);
			totalProportion+=proportionSigFig;
		}// for aminoAcid

		proportionDict.put("Total", totalProportion);
		return proportionDict;
		
	}// for getProportion
	
	

	/**
	 * Generates a count for each amino acid based on a total number and a proportion dictionary.
	 * i.e. count = total * proportion (for that amino acid)
	 * @param  total			  a total number of amino acids
	 * @param  targetPropDict     proportion of each amino acid from the entire sequence data set.
	 * @throws IllegalArgumentException if there exists a negative proportion
	 * @return newAminoCount      a dictionary [Keys: amino acid, Value: total * proportion (for that amino acid)]
	 */
	
	public static Map<String, Integer> getProportionedCount (int total, Map<String, Double> referencePropDict){
		
		
		/*Get all amino acid types*/
		Set<String> referenceKeys =  referencePropDict.keySet();
		Set<String> aminoAcids    = new HashSet<String> (referenceKeys);
		aminoAcids.addAll(referenceKeys);
		aminoAcids.remove("total");
		
		Map<String,Integer> newAminoCount = Counter.getEmptyCount(); //to store proportioned count
		for (String aminoAcid : aminoAcids) {
			if (referencePropDict.get(aminoAcid) < 0)
				throw new IllegalArgumentException ("Received a negative proportion for" + aminoAcid);
		}// for aminoAcid
		
		/*
		 * Calculate a proportioned count (total * proportion (for the chosen amino acid)) for
		 * every amino acid.
		 */
		
		int tempTotal = 0; //temporary to get the sum of all amino acid counts
		for (String aminoAcid: aminoAcids) {
			double proportion = referencePropDict.get(aminoAcid);	
			BigDecimal proportionCount = new BigDecimal(total * proportion);
			//Round to whole number since count should be a nonnegative integer.
			int newCount = (int) proportionCount.setScale(0, RoundingMode.HALF_UP).doubleValue();
			newAminoCount.put(aminoAcid, newCount);
			tempTotal+= newCount;
		}// for aminoAcid
		
		/* Confirm postcondition: The sum of all amino acid counts is equal to 
		 *						  given total.
		 */
		assert(total == tempTotal);
		return newAminoCount;
		
	}//getProportionedCount
	
	/**
	 * Generates a range of a amino acid based on a reference count. 
	 * (i.e. Designates a amino acid to integer based on a count.)
	 * @param referenceCountDict	a zero removed dictionary 
	 * 								[keys]: amino acid (alphabet A to Z)
	 * 								[value]: total count indicating how many
	 * 								the [key] amino acid there are in the set of 
	 * 							    all the proteins in proteinSeqDict.
	 * 									   
	 * @return rangeDict			a dictionary containing range of each amino acid
	 * 								[Key: amino acid, Value: range]
	 * 								100 aminoacids total, A = 30 counts, B = 40 counts, C = 10 counts, D = 20 counts
	 * 								A range (out of 100 aminoacids):  1(inclusive)-30(exclusive), B range: 30-40 ...etc...
	 */
	public static Map<String, ArrayList<Integer>> generateRange (Map<String, Integer> referenceCountDict){
		
		Set <String> aminoAcids = referenceCountDict.keySet();
		Map<String, ArrayList<Integer>> rangeDict = new HashMap<> ();
		
		/* range = [start, end] */
		int previous = 1;//start
		int current  = 0;//end
		
		/*Calculate the range for each amino acid based*/
		for (String aminoAcid: aminoAcids) {
			/* previous's end number + count of the chosen amino acid = end of the chosen amino acid */
			current  = previous + referenceCountDict.get(aminoAcid);

			/* Generate the range */
			ArrayList<Integer> range = new ArrayList<Integer>();
			range.add(0, previous); // start is the previous amino acid's end 
			range.add(1, current);

			previous = current; //end becomes the start of the next protein
			
			rangeDict.put(aminoAcid, range); //store the range in the rangeDict
		}// for aminoAcid
		
		return rangeDict;
	}//generateRange

	/**
 	* Creates a amino acid count dictionary where there is no entry with a value of 0.
 	* @param proteinSeqDict	 protein sequence data (each entry's form: [proteinID : target protein sequence])
 	* @return aminoCountDict a dictionary [keys]: amino acid (alphabet A to Z)
	 * 						[value]: total count indicating how many
	 * 						the [key] amino acid there are in the set of 
	 * 						all the proteins in proteinSeqDict.
	*/
	public static Map<String, Integer> countZeroRemoved(Map<String, String> proteinSeqDict) {
		Map<String, Integer> aminoCountDict = countAminoAcid(proteinSeqDict);
		Iterator<Map.Entry<String, Integer>> iterator = aminoCountDict.entrySet().iterator();

		while(iterator.hasNext()){
			if(iterator.next().getValue() == 0)
				iterator.remove();

		}//while

		return aminoCountDict;

	}//countZeroRemoved

	/**
	 * Prints out each entry of a dictionary as the following form: [Key]:   , [Value]: 
	 * @param <K> Variable type (String, Integer....etc..) for the dictionary's keys.
	 * @param <V> Variable type (String, Integer....etc..) for the dictionary's values.
	 * @param dictionary	
	 */
	public static <K, V> void displayDictionary(Map <K,V> dictionary) {
		
		for (Map.Entry<K,V> entry : dictionary.entrySet()) {
		
			System.out.println("[Key]: " + entry.getKey() + ", [Value]: " + entry.getValue());
		}// for Map.Entry
		
	}//displayCount
	
}//end of Counter class
	
	
		

	
	
	
	


