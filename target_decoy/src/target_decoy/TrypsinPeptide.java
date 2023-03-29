package target_decoy;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

/**
 * Represents peptide(s) created using Trypsin
 * @author Hyeyun Jung
 */

public class TrypsinPeptide extends Peptide {
    
    /**
     * 
     * @param enzyme    Trypsin
     * @param seqDict   {@inheritDoc}}
     */
    public TrypsinPeptide(String enzyme, Map <String, String> seqDict){
       super(enzyme,seqDict);

    }// Peptide


    /**
     * {@inheritDoc}}
     */
    @Override
    public ArrayList<String> cleaveSingleProtein(String proteinID, String proteinSeq){

        
        StringBuilder peptideBuilder = new StringBuilder();
        char [] sequence = proteinSeq.toCharArray(); 
        int length = proteinSeq.length();
        ArrayList<String> peptides = new ArrayList<String>();

        /*Generate peptides*/
        for(int i = 0; i < length; i++)
        {
            peptideBuilder.append(sequence[i]);

            /*Cleaves c-termius of K & R amino acid */
            if (sequence[i] == 'K' || sequence[i] == 'R'){
                peptides.add(peptideBuilder.toString()); //add a peptide to the list
                peptideBuilder.setLength(0);
            }// if K or R

        }// for int i

        /*Add the uncleaved peptide (no ending K or R) */
        if(peptideBuilder.length() != 0)
            peptides.add(peptideBuilder.toString());

        /* Confirm postcondition: If you take all the peptides' amino acid sequences 
         *                        and append altogether, we get the original protein sequence 
         *                        (i.e. proteinSeq).
		 */
        peptideBuilder.setLength(0);
        for(String peptide: peptides){
            peptideBuilder.append(peptide);
        }//for
        assert(proteinSeq.equals(peptideBuilder.toString()));

        return peptides;

    }// cleaveProtein

    
    /**
     * {@inheritDoc}}
     */
    @Override
    public void cleaveAllProtein(){
        
        Set <String> idSet = seqDict.keySet();
        
        for (String proteinID : idSet){ 
            peptideDict.put(proteinID, cleaveSingleProtein(proteinID, seqDict.get(proteinID)));
        }
        
    }//cleaveAllProtein()
   
    
    /**
     * {@inheritDoc}}
     */

    @Override
    public void countLength(){

        Set<String> idSet = peptideDict.keySet();

        /*Get peptide lengths & count */
        for (String proteinID: idSet){
            /*Calculate the length of the chosen peptide */
            for (String peptide: peptideDict.get(proteinID)){
                /*Accumulate count */
                if (peptideLengthDict.get(peptide.length()) == null){
                    peptideLengthDict.put(peptide.length(), 1);
                }
                else
                    peptideLengthDict.put(peptide.length(), peptideLengthDict.get(peptide.length()) + 1);
            }//for peptide
        }//for proteinID
         
    }//countLength


    
    /**
     * {@inheritDoc}}
     */
    @Override
    public void calculateMass(ArrayList<String> peptideList){
    
        
        for (String peptide: peptideList){
            double mass = 0.0;
            /*Calculate the mass of the chosen peptide */
            for (int i = 0; i < peptide.length(); i++){
                mass += getAminoMass(peptide.charAt(i));
            }//for i
            /*Accumulate the count */
            if(peptideMassDict.get(mass) == null)
                peptideMassDict.put(mass, 1);
            else
                peptideMassDict.put(mass, peptideMassDict.get(mass) + 1);
        }//for peptide
     
    }//calculateMass

    
    /**
     * {@inheritDoc}}
     */
    @Override
    public void countAllMass(){
        Set <String> idSet = seqDict.keySet();
        for (String proteinID: idSet){
            calculateMass(peptideDict.get(proteinID));
        }//for proteinID
    }//calculateAllMass

  

}//end of class
