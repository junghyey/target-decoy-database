package target_decoy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents the peptides created using an enzyme.
 * @author junghyey
 */
public abstract class Peptide {

    	
	/**
	 * Variables to store peptide information
	 */
    protected String enzyme; //protease
    protected Map<String, String> seqDict; //sequences to cleave 
    protected Map<String, ArrayList<String>> peptideDict; //list of peptides for each protein
    protected Map<Integer, Integer> peptideLengthDict; //peptide length counts (ex: There are 8 peptides with peptide length 9)
    protected Map<Double, Integer> peptideMassDict; //peptide mass counts (ex: There are 8 peptides with peptide 1371.33 Da)

    /**
     * Constructor for the class
     * @param enzyme    the name of the protease to use to cleave proteins
     * @param seqDict   the protein sequences to cleave in dictionary format:
     *                  each entry stores  [Key: proteinID, Value: protein sequence]                
     */
    public Peptide(String enzyme, Map <String, String> seqDict){
        this.enzyme  = enzyme;
        this.seqDict = seqDict;
        peptideDict  = new HashMap <> ();
        peptideLengthDict = new HashMap<> ();
        peptideMassDict = new HashMap<>();
    }// Peptide

    /**
     * Cleaves a single protein (based on role of the given enzyme (protease))
     * @param proteinSeq    amino acid sequence of the protein to cleave
     * @return list of peptides (result of protein cleavage)
     */
    public abstract ArrayList<String> cleaveSingleProtein(String proteinID, String proteinSeq);

    /**
     * Cleaves all the proteins in seqDict (class variable)
     * and stores the peptides in peptideDict (class variable).
     */
    public abstract void cleaveAllProtein();

     /**
     * Calculates length of the peptides and counts how many peptides
     * have length X.  (X is arbitrary)
     * Stores the result in peptideLengthDict  (class variable).
     */
    public abstract void countLength();

    /**
     * Helper for the countMass function
     * Calculates the mass of the given list of peptides (of a protein).
     * @param peptideList list of peptides (of a protein)
     */
    public abstract void calculateMass(ArrayList<String> peptideList);

    /**
     * Counts how many peptides have mass X.  (X is arbitrary)
     * @param peptideList list of peptides (of a protein)
     * Stores the result in peptideMassDict  (class variable)
     */
    public abstract void countAllMass();

    /**
     * Gets the mass of a given amino acid
     * @param aminoAcid the alphabet abrreviation of the amino acid
     * @return mass     the mass of the given amino acid in double
     *                  - 1 if there is no matched mass for the given amino acid.
     */
    public double getAminoMass(char aminoAcid){
        switch (aminoAcid){
            case 'A': 
                return 71.03711;
            case 'C': 
                return 103.00919;
            case 'D': 
                return 115.02694;             
            case 'E': 
                return 129.04259;             
            case 'F': 
                return 147.06841;             
            case 'G': 
                return 57.02146;             
            case 'H': 
                return 137.05891;
            case 'I': 
                return 113.08406;
            case 'K': 
                return 128.09496;             
            case 'L': 
                return 113.08406;             
            case 'M': 
                return 131.04049;             
            case 'N': 
                return 114.04293;          
            case 'P': 
                return 97.05276;
            case 'Q': 
                return 128.05858;
            case 'R': 
                return 156.10111;             
            case 'S': 
                return 87.03203;             
            case 'T': 
                return 101.04768;   
            case 'U':
                return 150.95;            
            case 'V': 
                return 99.06841; 
            case 'W':
                return 186.07931;
            case 'Y':
                return 163.06333;    
        }//switch

        //if no match
        System.out.println("unmatch : " + aminoAcid);
        return -1;

    }//getAminoMass

   //***********************************************Setters***********************************************

   /**
    * @param enzyme type of protease
    */
    public void setEnzyme (String enzyme){
        this.enzyme = enzyme;
    }//setEnzyme

  
    /**
     * @param enzyme a Dictionary (Map<String, String>), where each
	 * 		   		entry stores  [Key: proteinID, Value: protein sequence]
     */
    public void setSeqDict(Map<String, String> seqDict){
        this.seqDict = seqDict;
    }//setProteinSeq

    /**
    * @param peptideDict a Dictionary (Map<String, String>), where each
	* 		   		     entry stores  [Key: proteinID, Value:  list of peptides]
    */
    public void setPeptideDict  (Map<String, ArrayList<String>> peptideDict){
        this.peptideDict = peptideDict;
    }//setPeptideDict
    
    /**
    * @param peptideLengthDict a Dictionary (Map<String, Integer>), where each
	* 		   		           entry stores  [Key: peptide length, Value: the number of peptides with 
    *                                       the Key length]
    */
    public void setLengthDict(Map<Integer, Integer> peptideLengthDict){
        this.peptideLengthDict = peptideLengthDict;
    }//setLengthDict

    /**
     * @param peptideMassDict  a Dictionary (Map<Double, Integer>), where each
	 * 		   		           entry stores  [Key: peptide mass, Value: the number of peptides with 
     *                                        the Key mass]
     */
    public void setMassDict(Map<Double, Integer> peptideMassDict){
        this.peptideMassDict = peptideMassDict;
    }//setMassDict

    //***********************************************Getters***********************************************
    /**
     * @return peptideDict  a Dictionary (Map<String, String>), where each
	 * 		   		        entry stores  [Key: proteinID, Value:  list of peptides]
     */
    public Map<String, ArrayList<String>> getPeptideDict(){
        return peptideDict;
    }//getPeptideDict

    /**
     * @return peptideLengthDict  a Dictionary (Map<String, String>), where each
	 * 		   		        entry stores  [Key: peptide length, Value:  the number of peptides with 
     *                                     the Key length]
     */
    public Map<Integer, Integer> getLengthDict(){
        return peptideLengthDict;
    }//getLengthDict

    
    /**
     * @return peptideMassDict  a Dictionary (Map<String, String>), where each
	 * 		   		            entry stores  [Key: peptide mass, Value:  the number of peptides with 
     *                                     the Key mass]
     */
    public Map<Double, Integer> getMassDict(){
        return peptideMassDict;
    }//getMassDict
    
        
    /**
     * @return enzyme   the name of the protease for the class 
     */
    public String getEnzyme (){
        return enzyme;
    }//getEnzyme

         
    /**
     * @return enzyme   a Dictionary (Map<String, String>), where each
	 * 		   		    entry stores  [Key: protein ID, Value: protein amino acid sequence]
     */
    public Map<String, String> getSeqDict(){
        return seqDict;
    }


    

}
