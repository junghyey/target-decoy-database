package target_decoy;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Helper(Parent) class for other decoy generator classes.
 * @author junghyey
 */
public abstract class DecoyGenerator {
	
	/**
	 * Variables for the given target protein data
	 */
	protected Map<String, String> targetSeqDict;
	protected Map<String, String> targetInfoDict;
	
	// * rangeMap ex) 100 aminoacids total, A = 30 counts, B = 40 counts, C = 10 counts, D = 20 counts
	//				  A range (out of 100 aminoacids):  1 -30, B range: 30-40 ...etc....
	//				  minimun inclusive,maximum exclusive
	
	/**
	 * Variables for the decoy protein data.
	 */
	protected Map<String, String> decoySeqDict;
	protected Map<String, String> decoyInfoDict;
	protected final static String DECOY_PREFIX = ">XXX_";
	
	/**
	 * Constructor for the class
	 * Stores the target protein data information & update the class variables.
	 * @param  targetSeqDict target sequence data (each entry's form: [proteinID : target protein sequence])	
	 * @param  targetInfoDict	target info data (each entry's form: [proteinID : target protein info])
	 */
	public DecoyGenerator(Map<String, String> targetSeqDict, Map<String, String> targetInfoDict)
	{
		this.targetInfoDict  = targetInfoDict;
		this.targetSeqDict   = targetSeqDict;
		decoySeqDict         = new HashMap<String, String>();
		decoyInfoDict 		 = new HashMap<String, String>();
		
	}//DecoyGenerator
	
	/**
	 * Generates a single decoy protein sequence given the target proteinID.
	 * @param proteinID	  a valid proteinID 
	 * @return a decoy protein corresponding to the target protein with the given proteinID.
	 */
	protected abstract String generateDecoy(String proteinID);
	
	/**
	 * Generate decoy protein sequences corresponding to all target proteins from targetSeqDict.
	 * Stores decoy protein sequences in the decoySeqDict where each entry is
	 * [proteinID, decoy protein sequence]
	 */
	public abstract void generateDecoyDict();
	
	
	/**
	 * Generate decoy protein information header ("XXX_" + target protein info heading) 
	 * corresponding to all target proteins from targetInfoDict.
	 * Stores decoy protein information in the decoyInfoDict where each entry is
	 * [proteinID, decoy protein information]
	 */
 	public void generateDecoyInfo () {
 		/*Get all the protein ids*/
		Set<String> idSet = targetInfoDict.keySet();
		StringBuilder infoBuilder = new StringBuilder();

		/*Generate decoy information header for all decoy proteins*/
		 for (String proteinID: idSet) {

			 /* 1. Create decoy protein information heading ("XXX_" + target protein info heading)*/
			 String targetInfo = targetInfoDict.get(proteinID);
			 infoBuilder.append(DECOY_PREFIX); //add default decoy prefix
			 infoBuilder.append(targetInfo.substring(1)); //get after ">"
			 String decoyInfo = infoBuilder.toString();
			 
			 /* 2. Store it into the decoyInfoDict*/
			 decoyInfoDict.put(proteinID, decoyInfo);
			
			 infoBuilder.setLength(0); //initialize after storing.
		 }// for proteinID	 
		 
		 /* Confirm postcondition: The decoy proteinIDs and target protein IDs are the same. 
		  *						   i.e. Have the same key sets. 
		  */
		 assert(targetInfoDict.keySet().equals(decoyInfoDict.keySet()));
	}//decoyInfoGenerator()




	//***********************************************Getters***********************************************
 	/**
 	 * Returns the current version of decoySeqDict
 	 * @return decoySeqDict		a Dictionary (Map<String, String>), where each
	 * 		   					entry stores  [Key: proteinID, Value: decoy protein sequence]
 	 */
 	
	public Map<String, String> getDseqDict(){
		return decoySeqDict;
	}//getDseqDict
	
	/**
 	 * Returns the current version of decoyInfoDict
 	 * @return decoySeqDict		a Dictionary (Map<String, String>), where each
	 * 		   					entry stores  [Key: proteinID, Value: decoy protein information]
 	 */
	public Map<String,String> getDinfoDict(){
		return decoyInfoDict;
	}//getDinfoDict
	
 	/**
 	 * Returns the current version of targetSeqDict
 	 * @return decoySeqDict		a Dictionary (Map<String, String>), where each
	 * 		   					entry stores  [Key: proteinID, Value: target protein sequence]
 	 */
 	
	  public Map<String, String> getTseqDict(){
		return targetSeqDict;
	}//getTseqDict
	
		
 	/**
 	 * Returns the current version of targetInfoDict
 	 * @return targetInfoDict	a Dictionary (Map<String, String>), where each
	 * 		   					entry stores  [Key: proteinID, Value: target protein information]
 	 */
 	
	  public Map<String, String> getTinfoDict(){
		return targetInfoDict;
	}//getTinfoDict


	//***********************************************Setters***********************************************

	/**
 	* Sets the targetSeqDict to the given parameter
 	* @param targetSeqDict	 a Dictionary (Map<String, String>), where each
	 * 		   				 entry stores  [Key: proteinID, Value: target protein sequence]
 	*/
	 public void setTseqDict(Map<String ,String> targetSeqDict){
		this.targetSeqDict = targetSeqDict;
	}//setTseqDict
	
	/**
 	* Sets the targetInfoDict to the given parameter
 	* @param targetInfoDict	 a Dictionary (Map<String, String>), where each
	 * 		   				 entry stores  [Key: proteinID, Value: target protein information]
 	*/
	 public void setTinfoDict(Map<String ,String> targetInfoDict){
		this.targetInfoDict = targetInfoDict;
	}//setTseqDict
	
	/**
 	* Sets the decoySeqDict to the given parameter
 	* @param decoySeqDict	 a Dictionary (Map<String, String>), where each
	 * 		   				 entry stores  [Key: proteinID, Value: decoy protein sequence]
 	*/
	 public void setDseqDict(Map<String ,String> decoySeqDict){
		this.decoySeqDict = decoySeqDict;
	}//setDseqDict

	/**
 	* Sets the decoyInfoDict to the given parameter
 	* @param decoyInfoDict	 a Dictionary (Map<String, String>), where each
	 * 		   				 entry stores  [Key: proteinID, Value: decoy protein information]
 	*/
	 public void setDinfoDict (Map<String ,String> decoyInfoDict){
		this.decoyInfoDict = decoyInfoDict;
	}//setDinfoDict

	
	
}//end of class
