package target_decoy;

/**
 * This class contains methods for generating peptides and
 * storing those peptides into files.
 * @author junghyey
 */
public class PeptideGenerator {
    
    /**
	 * proteinSeqDict stores [proteinID, protein sequence] ([K,V] pair)
	 * proteinInfoDict stores [proteinID, protein information heading] ([K,V] pair)
	 */
	private DictionaryGenerator data;
    private Peptide peptide;

    /**
     * Constructor for the class.
     * @param fileName  A full file path (file path + file name) of
     *                  the protein file to generate peptides of.
     */
	public PeptideGenerator(String fileName) {
        data = new DictionaryGenerator(fileName);
        
		
	}// PeptideGenerator

  /**
   * Generates a peptide using an enzyme.
   * @param peptide     any child class of Peptide class
   */
    public void generatePeptide(Peptide peptide){
		/*Generate peptides */
        this.peptide = peptide;
		peptide.cleaveAllProtein();
        peptide.countLength();
		peptide.countAllMass();
    }//generatePeptide;


    /**
     * Saves the generated peptide in txt, saves each peptide's mass in csv, 
     * and saves each peptide's length in csv.
     * @param fileAddress   a user designated file path to save these files (only path, no file name)
     * @param prefix        a user designated prefix for a file name
     * @param suffix        a user designated suffix for a file name
     * prefix+"peptides"+suffix+".txt" (generated peptide), prefix+"peptide_mass"+suffix+".csv" (peptide mass), 
     * and prefix+"peptide_length"+suffix+".csv"(pepetide length) are generated.
     */
    public void writePeptide(String fileAddress, String prefix, String suffix){
		Database.writeDictionary( fileAddress + prefix + "peptides" + suffix+ ".txt", peptide.getPeptideDict());
		Database.writeCSV( fileAddress + prefix + "peptide_mass" + suffix+ ".csv", peptide.getMassDict());
		Database.writeCSV( fileAddress + prefix +"peptide_length" + suffix + ".csv", peptide.getLengthDict());
    }//writePeptide

    //***********************************************Setters***********************************************
    /**
     * @param peptide   any child class of Peptide class
     */
    public void setPeptide(Peptide peptide){
        this.peptide = peptide;
    }
    
    /**
     * @param data     a DictionaryGenerator that stores 
     *                 [Key: ProteinID] and [Value:Protein Sequence]
     */
    public void setData (DictionaryGenerator data){
        this.data = data;
    }//setData

    //***********************************************Getters***********************************************
    /**
     * @return data    a DictionaryGenerator that stores 
     *                 [Key: ProteinID] and [Value:Protein Sequence]
     */
    public DictionaryGenerator getData (){

        return data;

    }//getData

    /**
     * @return peptide  a type of Peptide class
     */
    public Peptide getPeptide(){
        return peptide;
    }//getPeptide


}//end of class


