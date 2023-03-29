package target_decoy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
/**
 *  Represents decoy protein data created using "deBrujin amino acid" approach.
 *  Reference for this approach: J. Proteome Res. 2020, 19, 3, 1029–1036
 *								 Publication Date:February 3, 2020
 *								 https://doi.org/10.1021/acs.jproteome.9b00555
 * @author junghyey
 *
 */
public class DeBruijnDecoy extends DecoyGenerator {
	
	private final static String DUMMY_SEQUENCE = "__";
	private Map<String, ArrayList<Integer>> rangeMap;
	
	/**
	 * combinationCount stores [Three letter combination,  count] ([K,V] pair)
	 * combinationMap stores  [Three letter combination, mapped amino acid] ([K,V] pair)
	 */
	private Map<String, Integer> combinationCount;
	private Map<String, String> combinationMap;
	private Map<String, String> modifiedTargetSeq;
	
	/**
	 * Constructor for the class
	 * {@inheritDoc}
	 * Initialize additional class variables.
	 */
	public DeBruijnDecoy(Map<String, String> targetSeqDict, Map<String, String> targetInfoDict) {
		super(targetSeqDict, targetInfoDict);
		combinationCount  = new HashMap<String, Integer>();
		combinationMap    = new HashMap<String, String>();
		modifiedTargetSeq = new HashMap<String, String>();
		rangeMap	      = new HashMap<String, ArrayList<Integer>>();
	}//ThreeDecoy

	 
	/**
	 * Adds "__" prefix to each target protein's (assigned in the constructor) amino acid sequence.
	 * @return	newTargetDict	a dictionary of modified target protein sequence data 
	 * 							each protein's amino acid sequence begins with "__"
	 */
	public static Map<String, String> addDummySeq(Map<String, String> targetSeqDict) {
		 Set<String> idSet = targetSeqDict.keySet();
		 Map<String, String> newTargetDict = new HashMap<String, String> ();
		 StringBuilder seqBuilder = new StringBuilder();
		 for (String proteinID: idSet) {
			 String proteinSeq = targetSeqDict.get(proteinID);
			 seqBuilder.append(DUMMY_SEQUENCE);
			 seqBuilder.append(proteinSeq);
			 newTargetDict.put(proteinID, seqBuilder.toString());
			 seqBuilder.setLength(0);
		 }//for proteinID
		 
		 
		 return newTargetDict;
	}//addPrefix
	
	
	
	/**
	 * Helper function for countAllCombination:
	 * Finds all possible three letter combinations of the form (position,position+1, position+2)
	 * from a protein amino acid sequence and accumulates the count of that combination. 
	 * Updates combinationCount. [Key: three letter combination , Value: abundance]
	 * @param proteinSeq	a protein's amino acid sequence
	 */
	private void countCombination(String proteinSeq) {
		char [] seqArr= proteinSeq.toCharArray();
		StringBuilder combinationBuilder = new StringBuilder();
		
		/* Find all the possible three letter combinations form: (position, position +1, position_2) */
		for (int position = 0; position + 2 < proteinSeq.length(); position++) {
			
			/*Create the three letter combination*/
			combinationBuilder.append(seqArr[position]);    // first letter
			combinationBuilder.append(seqArr[position + 1]);// second letter
			combinationBuilder.append(seqArr[position + 2]);// third letter
			String chosenCombination = combinationBuilder.toString();
			
			/* Store the combination in combinationCount and accumulate the count*/
			if(combinationCount.get(chosenCombination) == null) // does not exist add entry
				combinationCount.put(chosenCombination, 1); // new combination, count = 1 
			else
				//existing combination, increment the count
				combinationCount.put(chosenCombination, combinationCount.get(chosenCombination) + 1); 
			
			combinationBuilder.setLength(0); //initialize StringBuilder
			
		}// for position
		
	}//countCombination
	
	/**
	 * Finds all possible three amino acid combinations (of the form position, position + 1, position +2)
	 * from targetSeqDict and calculates the abundance of each three
	 * letter combination in the given  target sequence data. Stores these information as dictionary:
	 * [Key: three amino acid combination, 
	 *  Value: total count indicating how many the [key] amino acid 
	 *  	   there are in the set of all the proteins in proteinSeqDict.
	 *  ]
	 */
	public void countAllCombination () {
		 Set<String> idSet = modifiedTargetSeq.keySet();
		 for(String proteinID: idSet) 
			 countCombination(modifiedTargetSeq.get(proteinID));
	}//countAllCombination
	

	
	/**
	 * Encodes (or map) every three letter(= amino acid) combination to
	 * a single aminoAcid based on rangeMap
	 */
	
	public void mapCombination() {
		
		//Map <String, Integer> originalRefCount = referenceCount; //copy the initial referenceCount
		
		Set<String> combinationSet = combinationCount.keySet(); //get all combination
		Random getRandNum = new Random();

		/*Set the range for randum number generation*/
		int max =  Counter.getTotalAminoCount(targetSeqDict);
		int min = 1;

		int randNum = 0; //initialize
		String aminoAcid = null; //initialize

		/*Convert keySet to keylist (sorted in alphbetical order) */
		Set<String> keys = rangeMap.keySet();
		keys.stream().sorted();
		List<String> keyList = new ArrayList<>(keys);
		
		/* Map a combination (three letter) of the combinationSet
		 * to a single amino acid. (A combination cannot be mapped to two different amino acids).
		 */
		for (String combination: combinationSet) {
			
			if(combination.charAt(2) == 'R')
				combinationMap.put(combination, "R");
			else if(combination.charAt(2) == 'K')
				combinationMap.put(combination, "K");
			else{

				do{
					randNum = getRandNum.nextInt((max - min) + 1) + min;
					aminoAcid = whichAminoAcid(randNum, 0 , rangeMap.size(), keyList);
				}while(aminoAcid.equals("K") || aminoAcid.equals("R"));
	
				/* Map the combination to the chosen amino acid (ex: ABC --> A)  */
				combinationMap.put(combination, aminoAcid);
			}
			
			
		}// for combination
		
	}//mapCombination

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String generateDecoy(String proteinID) {
		
		/*Enforce the precondition: The given proteinID should be a valid key.*/
		if (!modifiedTargetSeq.keySet().contains(proteinID))
			throw new IllegalArgumentException("Invalid protein id: " + proteinID);
		
		/* Get the target protein */
		String targetProteinSeq =  modifiedTargetSeq.get(proteinID); 
		char [] seqArr= targetProteinSeq.toCharArray(); 
		int targetLength = targetProteinSeq.length(); 
		
		/* Prepare for the decoy protein building */
		StringBuilder decoyBuilder = new StringBuilder(); 
		StringBuilder combinationBuilder = new StringBuilder();
		
		/*Create a decoy protein */
		for (int position = 0; position + 2 < targetLength; position++) {
		
			/*Get the three letter combination in the protein sequence.*/
			combinationBuilder.append(seqArr[position]);    // first letter
			combinationBuilder.append(seqArr[position + 1]);// second letter
			combinationBuilder.append(seqArr[position + 2]);// third letter
			
			/*Convert the three letter combination to the according amino acid based on combinationMap.*/
			String mappedAminoAcid = combinationMap.get(combinationBuilder.toString()); 
			decoyBuilder.append(mappedAminoAcid);
			
			combinationBuilder.setLength(0); //initialize for a new combination
		}// for position
		
		/* Confirm postcondition: The length of the target protein sequence is greater than
		 * 						  that of the built decoy protein sequence by 2.
		 */
		assert(modifiedTargetSeq.get(proteinID).length() - decoyBuilder.toString().length() ==  2 );
		
		return decoyBuilder.toString();
		
	}//generateDecoy
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void generateDecoyDict() {
		Set<String> idSet = modifiedTargetSeq.keySet();
		 
		for (String proteinID: idSet) {
			String decoyProtein = generateDecoy(proteinID);
			decoySeqDict.put(proteinID, decoyProtein);
			}// for proteinID
		 
		/* Confirm postcondition: The decoy proteinIDs and target protein IDs are the same. 
		 *						  i.e. Have the same key sets. 
		 */
		assert(decoySeqDict.keySet().equals(idSet));
	}//decoyDictGenerator()
	

	/**
	 * Helper method for isAllMappingCorrect
	 * Checks if a decoy protein generated by DeBruijn was properly generated from a given target protein.
	 * (Looks for the mapping process)
	 * @param  targetSequence	target protein sequence
	 * @param  decoySequence 	decoy sequence created based on targetSeqeunce using three decoy method
	 * @return boolean			true if properly created, else false
	 */
	private boolean isMappingCorrect (String targetSequence ,String decoySequence) {
		char [] tSeqArr = targetSequence.toCharArray(); 
		char [] dSeqArr = decoySequence.toCharArray();
		
		/*List to store the result of comparison of each amino acid*/
		ArrayList <Boolean> checkResult  = new ArrayList<Boolean> ();	
		StringBuilder combinationBuilder = new StringBuilder();
		
		/* Find all the possible three letter combinations form: (position, position +1, position_2) */
		for (int position = 0; position + 2 < targetSequence.length(); position++) {
			
			/*Get the three letter combination*/
			combinationBuilder.append(tSeqArr[position]);    // first letter
			combinationBuilder.append(tSeqArr[position + 1]);// second letter
			combinationBuilder.append(tSeqArr[position + 2]);// third letter
			
			/*
			 * ex) Map: __A = J , _AB = K, ABC = L
			 * ex) target sequence: __ABC
			 * 						|
			 * 						J
			 * 					compare with the according decoy sequence	
			 * ex) decoy sequence:  JKL	
			 */
			checkResult.add(combinationMap.get(combinationBuilder.toString()).equals(Character.toString(dSeqArr[position]))); //append the result
			
			combinationBuilder.setLength(0);
		}//for position
		
		if (checkResult.contains(false)) // if any of amino acid is unmatched
			return false;
		else
			return true;
	}//checkMapping

	
	/**
	 * Checks if decoy proteins generated by DeBruijn were properly generated from a given target proteins.
	 * (Looks for the mapping process)
	 * @param  targetSeqDict	target sequence data used for decoySeqDict generation
	 * @param  decoySeqDict		decoy sequence data created using three decoy method.
	 * @throws IllegalArgumentException if the given target sequence data and the decoy sequence 
	 * 									data do not contain same protein ids.
	 * @return boolean			true if properly created, else false
	 */
	
	public boolean isAllMappingCorrect (Map<String, String> TargetSeqDict, Map<String, String> decoySeqDict) {
		
		/*Enforce the precondition: The given target sequence data and the decoy sequence data contain same protein ids*/
		if(!targetSeqDict.keySet().equals(decoySeqDict.keySet())) {
			throw new IllegalArgumentException ("The given target sequence data and the decoy sequence data do not contain same protein ids ");
		}
		
		Set <String> idSet = modifiedTargetSeq.keySet();
		
		/* Check for every protein*/
		for (String proteinID: idSet) {
			if(!isMappingCorrect(modifiedTargetSeq.get(proteinID), decoySeqDict.get(proteinID)))
				return false;
		}// for proteinID
		return true;
		
	}//isMappingCorrect



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
		


	//***********************************************Setters***********************************************	
	/**
	 * @param combinationCount   each entry of a dictionary looks like
	 * 							 [Key: combination, Value: total count]
	 * 							 
	 */
	public void setCombinationCount(Map<String, Integer> combinationCount) {
		this.combinationCount = combinationCount;
	}//setCombinationDict

	
	/**
	 * @param combinationMap	 each entry of a dictionary looks like 
	 * 							 [Key: combination, Value: amino acid]
	 * 						
	 */
	public void setCombinationMap (Map<String, String> combinationMap) {
		this.combinationMap = combinationMap;
	}//setCombinationMap

	/**
	 * @param modifiedTargetSeq  each entry of a dictionary looks like
	 * 							 [Key: protein id, Value: dummy sequence + original target protein sequence]
	 * 							 
	 */
	
	public void setModifiedSeq (Map<String, String> modifiedTargetSeq) {
		this.modifiedTargetSeq = modifiedTargetSeq;
	}//setModifiedSeq
	
	/**
 	* @param rangeMap	a Dictionary (Map<String, ArrayList<Integer>>), where each
	 * 		   			entry stores  [Key: amino acid, Value: range for that amino acid]
 	*/
	 public void setRangeMap (Map<String ,ArrayList<Integer>> rangeMap){
		this.rangeMap = rangeMap;
	}//setRangeMap

	
	//***********************************************Getters***********************************************
	/**
	 * @return combinationCount a Dictionary (Map<String, Integer>), where each
	 * 		   					entry stores  [Key: combination, Value: total count]
	 */
	public Map<String, Integer> getCombinationCount() {
		return combinationCount;
	}//getCombinationDict
	
	/**
	 * @return combinationMap  a Dictionary (Map<String, String>), where each
	 * 		   				   entry stores  [Key: combination, Value: amino acid]
	 */
	public Map<String, String> getCombinationMap (){
		return combinationMap;
	}//mapCombination
	
	/**
	 * @return modifiedTargetSeq a Dictionary (Map<String, String>), where each
	 * 		   				   entry stores  [Key: protein id , Value: dummy sequence + original target protein sequence]
	 */
	public Map<String, String> getModifiedTarget (){
		return modifiedTargetSeq;
	}//getModifiedTarget

	/**
 	 * @return RangeMap		a Dictionary (Map<String, ArrayList<Integer>>), where each
	 * 		   				entry stores  [Key: amino acid, Value: range for that amino acid]
 	 */
	  public Map<String ,ArrayList<Integer>> getRangeMap(){
		return rangeMap;
	}//getRangeMap

}//end of class
