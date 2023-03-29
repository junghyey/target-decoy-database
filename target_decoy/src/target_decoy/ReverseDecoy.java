package target_decoy;

import java.util.Map;
import java.util.Set;

public class ReverseDecoy extends DecoyGenerator {

    public ReverseDecoy(Map<String, String> targetSeqDict, Map<String, String> targetInfoDict) {
        super(targetSeqDict, targetInfoDict);
    }//ReverseDecoy

    @Override
    protected String generateDecoy(String proteinID) {
        String sequence = targetSeqDict.get(proteinID);
        StringBuilder reversed = new StringBuilder();

         //start from the end
        for (int position = sequence.length() - 1; position >= 0; position--){
            reversed.append(sequence.charAt(position));
        }//for position
        return reversed.toString();
    }//generateDecoy

    @Override
    public void generateDecoyDict() {
        Set<String> idSet = targetSeqDict.keySet();
		 
        for (String proteinID: idSet) {
            String decoyProtein = generateDecoy(proteinID);
            decoySeqDict.put(proteinID, decoyProtein);
        }// for proteinID
        
        /* Confirm postcondition: The decoy proteinIDs and target protein IDs are the same. 
         *						   i.e. Have the same key sets. 
         */
        assert(decoySeqDict.keySet().equals(idSet));
        
    }//generateDecoyDict


    
}
