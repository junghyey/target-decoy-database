package target_decoy;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Represents target protein data in dictionary form
 * @author   junghyey 
 */
public class DictionaryGenerator {
	
	/**
	 * proteinSeqDict stores [proteinID, protein sequence] ([K,V] pair)
	 * proteinInfoDict stores [proteinID, protein information heading] ([K,V] pair)
	 */
	private Map<String, String> proteinSeqDict  = new HashMap<String, String>();
	private Map<String, String> proteinInfoDict = new HashMap<String, String>();
	private static char HEADING_SIGNATURE= '>';
	
	
	/**
	 * Constructor for the class: 
	 * Generates dictionaries for protein sequences and protein information.
	 * @param file_name	  String (full file path (path + name))
	 */
	public DictionaryGenerator(String fileName) {

		proteinSeqDict  = new HashMap<String, String>();
		proteinInfoDict = new HashMap<String, String>();

		Scanner fileReader = null; 
		StringBuilder proteinSeqBuilder = new StringBuilder();
		String proteinId = null;
		
		try {
			/*Read a file*/
			File fastaFile = new File(fileName);
			fileReader = new Scanner(fastaFile);
			
			/*Read one line in the file */
			while (fileReader.hasNextLine()) {
				
				String data = fileReader.nextLine(); 
				char [] char_array = data.toCharArray(); // to check for HEADING_SIGNATURE
					
					/* Store the protein information heading in proteinInfoDict*/
					if (char_array[0] == HEADING_SIGNATURE) {
						/*Protein info heading indicates the start of the new protein.*/
						/* 1. Initialize proteinSeqBuilder for a new protein. */
						proteinSeqBuilder.setLength(0); 
						
						/* 2. Find protein id in the protein info heading.*/
						proteinId = idFinder(char_array);
						
						/* 3. Update proteinInfoDict with [K,V] =>  [protein id, protein info]*/
						proteinInfoDict.put(proteinId, data); // information of the protein
					}//if
					
					/* Store the protein amino acid sequence in proteinSeqDict*/
					else {
						/*1. Obtain full amino acid sequence for the according protein*/
						proteinSeqBuilder = proteinSeqBuilder.append(data); //Append each line of the sequence to get the full sequence
						String protein_seq = proteinSeqBuilder.toString(); 
						
						/* 2. Update proteinSeqDict with [K,V] =>  [protein id, protein sequence]*/ 
						proteinSeqDict.put(proteinId, protein_seq); //amino acid sequence of the protein
						}//else 		
					}//while
			}//try
		
		catch (FileNotFoundException fne) { //catch error
				System.err.println("File Not Found");
				System.exit(1);
			}// catch
			
		finally {// if read was successful, close the file.
				if (fileReader != null) 
					fileReader.close();	
			}// finally
	}// TargetDictionary
				

	/**
	 * Finds a proteinID (ex: "Q9NXB0") from a given protein information heading.
	 * @param charArray   protein information heading in char array form 
	 * @return proteinID  String
	 */
	private String idFinder (char [] charArray) {
		
		StringBuilder sb = new StringBuilder();
		int idIndex = 0;
		
		for (; idIndex < charArray.length; idIndex ++) {
			
			if(charArray[idIndex] != '|')
				continue;
			else
				break;
			
		}// for idIndex

		for (int i = idIndex + 1; i < charArray.length; i++){
			if(charArray[i] != '|')
				sb.append(charArray[i]);
			else
				break;
		}//for i
		
		String protein_id = new String(sb);
		
		return protein_id;
		
	}
	
	//***********************************************Getters***********************************************
	/**
	 * @return proteinSeqDict 	a Dictionary (Map<String, String>), where each
	 * 		   					entry stores  [Key: proteinID, Value: target protein sequence]
	 * 
	 */
	public Map<String,String> getSeqDict() {
		return proteinSeqDict;
	}//getSeqDict
	
	/**
	 * @return proteinSeqDict	a Dictionary (Map<String, String>), where each
	 * 		   					entry stores  [Key: proteinID, Value: target protein information heading]
	 * 
	 */
	public Map<String,String> getInfoDict() {
		return proteinInfoDict;
	}//getInfoDict
	
}//end of class
