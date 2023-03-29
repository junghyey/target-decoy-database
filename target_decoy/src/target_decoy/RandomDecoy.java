package target_decoy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * Represents decoy protein data created using "random" approach.
 * @author junghyey
 */

public class RandomDecoy extends DecoyGenerator{
	
	private Map<String, ArrayList<Integer>> rangeMap;
	/**
	 * Constructor for the class, inherits parent class constructor
	 * {@inheritDoc} 
	 */
	public RandomDecoy(Map<String, String> targetSeqDict, Map<String, String> targetInfoDict)
	{
		super(targetSeqDict, targetInfoDict);
		rangeMap	      = new HashMap<String, ArrayList<Integer>>();
	}//RandomDecoy
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String generateDecoy(String proteinID){
		
		/*Enforce the precondition: The given proteinID should be a valid key.*/
		if (!targetSeqDict.keySet().contains(proteinID))
			throw new IllegalArgumentException("Invalid protein id: " + proteinID);
		
		/*Get the target protein's length */
		String targetProteinSeq =  targetSeqDict.get(proteinID); 
		int targetLength = targetProteinSeq.length();

		/*Set the range for randum number generation*/
		int max = Counter.getTotalAminoCount(targetSeqDict); 
		int min = 1;
		
		/*Prepare for the decoy protein building */
		StringBuilder  decoyBuilder = new StringBuilder(); // for decoy protein amino acid sequence
		Random getRandNum = new Random();

		int randNum = 0; //initialize
		/*Convert keySet to keylist (sorted in alphbetical order) */
		Set<String> keys = rangeMap.keySet();
		keys.stream().sorted();
		List<String> keyList = new ArrayList<>(keys);
		
		/*Create a decoy protein having the length of the given target protein */
		for (int count = 0; count < targetLength; count++) {
			/* 1. Get random number */
			randNum = getRandNum.nextInt((max - min) + 1) + min;
			decoyBuilder.append(whichAminoAcid(randNum, 0 , rangeMap.size(), keyList));
		}//for count
		 
		String decoyProtein = decoyBuilder.toString(); 

		/* Confirm postcondition: The length of the decoy protein and that of target protein 
		 * 						  are equal to each other.
		 */
		assert (decoyProtein.length() == targetLength);
		return decoyProtein;
	}//decoyGenerator
	


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void generateDecoyDict(){
	
		 Set<String> idSet = targetSeqDict.keySet();
		 
		 for (String proteinID: idSet) {
			 String decoyProtein = generateDecoy(proteinID);
			 decoySeqDict.put(proteinID, decoyProtein);
		 }// for proteinID
		 
		 /* Confirm postcondition: The decoy proteinIDs and target protein IDs are the same. 
		  *						   i.e. Have the same key sets. 
		  */
		 assert(decoySeqDict.keySet().equals(idSet));
	}//decoyDictGenerator()
	

		/**
	 * Decides amino acid based on the chosen number & given range (rangeDict) 
	 * # implemented using binary search algorithm
	 * @param num	number 
	 * @param low   start index of the given keys List (generally 0)
	 * @param high  last index of the given keys List (generally length of the keys List)
	 * @param keys	list of keys of  rangeMap (which is list of amino acids)
	 * @return amino acid in String (one of "A", "B" ....etc..)
	 */
	
	protected String whichAminoAcid(int num, int low, int high, List <String> keys)
	{
		int mid = (low + high) / 2 ; 
		String key = keys.get(mid); //start from the middle
		ArrayList<Integer> chosenRange = rangeMap.get(key); //get the chosen range

		if (num >= chosenRange.get(0) && num < chosenRange.get(1))
			return key;
		else if (num < chosenRange.get(0)) //num is on the left side
			return whichAminoAcid(num, low, mid - 1, keys);
		else 									  //num is on the right side
			return whichAminoAcid(num, mid + 1, high , keys);
		}// whichAminoAcid
		

	/**
 	* Sets the rangeMap to the given parameter
 	* @param rangeMap	a Dictionary (Map<String, ArrayList<Integer>>), where each
	 * 		   			entry stores  [Key: amino acid, Value: range for that amino acid]
 	*/
	 public void setRangeMap (Map<String ,ArrayList<Integer>> rangeMap){
		this.rangeMap = rangeMap;
	}//setRangeMap

	
	/**
 	 * Returns the current version of rangeMap 
 	 * @return setRangeMap		a Dictionary (Map<String, ArrayList<Integer>>), where each
	 * 		   					entry stores  [Key: amino acid, Value: range for that amino acid]
 	 */
	  public Map<String ,ArrayList<Integer>> getRangeMap(){
		return rangeMap;
	}//getRangeMap


}//RandomDecoy
