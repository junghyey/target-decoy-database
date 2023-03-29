package target_decoy;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 * This class contains useful methods for concatenating target and 
 * decoy data and saving the concatenated data into a single file.
 * @author junghyey
 * 
 */
public final class Database {
	
	private final static int COUNT_BIAS = -1;
	
	/**
	 * Puts new line after 60 characters (amino acid characters)
	 * @param aminoSequence				a String with length > 0
	 * @throws IllegalArgumentException received a a sequence with length <= 0
	 * @return formattedSequence		a new amino acid sequence where new line
	 * 									is included after 60 amino acid characters.
	 */
	private static String formatter (String aminoSequence) {
		int length = aminoSequence.length();
		
		/*Enforce the precondition: The given amino acid sequence's length should be > 0*/
		if (length <= 0)
			throw new IllegalArgumentException("The length of the amino acid sequence is <= 0");
		
		int count  = 0; //counter to find every 60th position
		
		StringBuilder seqBuilder = new StringBuilder(aminoSequence); // for a new sequence with new line
	
		/* Insert a new line after every 60th amino acid*/
		for (int position = 0; position < length; position++) {
			
			if(count == 60) {
				seqBuilder.insert(position, System.lineSeparator()); // lineSeparator for platform-dependency
				count = COUNT_BIAS; // start new count 
			}//if count
			
			count++; // increment until 60th amino acid position
		}// for position
		
		return seqBuilder.toString();
	}//formatter
	
	/**
	 * Writes a given dictionary to a file with a given name. Creates a new file with a given file name
	 * if it does not exist or otherwise overwrites to it.
	 * @param <K> Variable type (String, Integer....etc..) for the dictionary's keys.
	 * @param <V> Variable type (String, Integer....etc..) for the dictionary's values.
	 * @param dictionary a map(dictionary) with type <K,V>
	 * @param fileName   full file path (path + name) 
	 */
	public static <K, V> void writeDictionary(String fileName, Map <K, V> dictionary) {
		
		try {
			FileWriter dataWriter = new FileWriter(fileName);
			/*
			 * Write the every entry of the given dictionary into the given file.
			 * Each entry will be written as [Key]: key, [Value]: value. 
			 */
			for (Map.Entry<K,V> entry : dictionary.entrySet()) {
				dataWriter.write( "[Key]: " + entry.getKey() + ", [Value]: " + entry.getValue());
				dataWriter.write(System.lineSeparator());
			}// for Map.Entry
			dataWriter.close();	 //close after finish writing
		}//try
		catch(IOException ioe) {
			System.err.println("File error");
		}//catch
		
	}//displayCount

	
/**
	 * Writes a given dictionary to a file (csv) with a given name.
	 * Creates a new file with a given file name
	 * if it does not exist or otherwise overwrites to it.
	 * @param <K> Variable type (String, Integer....etc..) for the dictionary's keys.
	 * @param <V> Variable type (String, Integer....etc..) for the dictionary's values.
	 * @param dictionary a map(dictionary) with type <K,V>
	 * @param fileName   full file path (path + name) 
	 */
	public static <K,V> void writeCSV (String fileName, Map <K, V> dictionary) {
		
		try {
			FileWriter dataWriter = new FileWriter(fileName);
			dataWriter.write( "key,value");
			dataWriter.write(System.lineSeparator());
			/*
			 * Write the every entry of the given dictionary into the given file.
			 * Each entry will be written as [Key]: key, [Value]: value. 
			 */
			for (Map.Entry<K,V> entry : dictionary.entrySet()) {
				dataWriter.write(entry.getKey() + "," + entry.getValue());
				dataWriter.write(System.lineSeparator());
			}// for Map.Entry
			dataWriter.close();	 //close after finish writing
		}//try
		catch(IOException ioe) {
			System.err.println("File error");
		}//catch
		
	}//displayCount
	
	/**
	 * Concatenates a given target & decoy data and saves it as a single file with a given file name.
	 * @param  fileName			a user-designated full file path (path + name)
	 * @param  targetSeqDict	target sequence data (each entry's form: [proteinID : target protein sequence])
	 * @param  decoySeqDict		decoy sequence data  (each entry's form: [proteinID : decoy protein sequence])
	 * @param  targetInfoDict	target info data (each entry's form: [proteinID : target protein info])
	 * @param  decoyInfoDict	decoy info data  (each entry's form: [proteinID : decoy protein info])
	 * @throws IllegalArgumentException if proteinIDs(keys) of info dictionary and sequence dictionary are not equal.
	 */
	public static void generateDatabase(String fileName, Map<String, String> targetSeqDict, 
										Map<String, String> decoySeqDict, Map<String, String> targetInfoDict, 
										Map<String, String> decoyInfoDict) {
	
		/*Enforce the precondition: Sequence dictionary and info dictionary have the same proteins*/
		if (!targetSeqDict.keySet().equals(targetInfoDict.keySet()))
			throw new IllegalArgumentException("Sequence dictionary and info dictionary do not have same proteins");
		
		Set<String> idSet = targetInfoDict.keySet();
		
		StringBuilder targetInfo = new StringBuilder ();
		StringBuilder targetSeq  = new StringBuilder ();
		StringBuilder decoyInfo  = new StringBuilder ();
		StringBuilder decoySeq   = new StringBuilder ();
		try {
			FileWriter dataWriter = new FileWriter(fileName);
			
			/* Concatenate the entire target protein database & decoy protein database*/
			for (String proteinID: idSet) {
				/* Initialize variables */
				targetInfo.setLength(0);
				targetSeq.setLength(0);
				decoyInfo.setLength(0);
				decoySeq.setLength(0);
				
				/* Get values of each dictionary using the proteinID key */
				targetInfo.append(targetInfoDict.get(proteinID)); //get target protein's info
				targetSeq.append(targetSeqDict.get(proteinID));	  //get target protein's amino acid sequence
				decoyInfo.append (decoyInfoDict.get(proteinID));  //get decoy protein's info
				decoySeq.append(decoySeqDict.get(proteinID));     //get decoy protein's amino acid sequence
				
				/* Write in the file with the order:
				 * > target protein info 
				 * target protein sequence (60 amino acid per line)
				 * > according target protein's decoy protein info
				 * decoy protein sequence  (60 amino acid per line)
				 * */
				/*Target*/
				dataWriter.write(targetInfo.toString());  
				dataWriter.write(System.lineSeparator()); 
				dataWriter.write(formatter(targetSeq.toString()));	
				dataWriter.write(System.lineSeparator());
				
				/*Decoy*/
				dataWriter.write(decoyInfo.toString());
				dataWriter.write(System.lineSeparator());
				dataWriter.write(formatter(decoySeq.toString()));
				dataWriter.write(System.lineSeparator());	
			}// for proteinID
			
			
			dataWriter.close(); //close after finish writing 
		}//try
		catch(IOException ioe){
			System.err.println("File Error");
		}//catch
		
	}//generateDatabse

	/**
	 * Generates a combination map from a file.
	 * @param fileName 	the name of the combination map file. (ABC, A) 			   
	 * @return combinationMap	a Dictionary containing combination mapping info
	 * 							[Key]:   Three combination of peptide (ex: ACD)
	 * 							[Value]: Peptide Abbreviation(A for Alanine..etc..) 
	 * 							The same three combination gets mapped to the same peptide.
	 */
	public static Map<String, String> combinationFromFile(String fileName){
				
		Scanner fileReader = null; 
		Map<String,String> combinationMap = new HashMap<>();
		
		try {
			/*Read a file*/
			File combinationMapFile = new File(fileName);
			fileReader = new Scanner(combinationMapFile);
			/*Read one line in the file */
			while (fileReader.hasNextLine()) {
				String line = fileReader.nextLine();
				String [] splitted = line.split(",");
				combinationMap.put(splitted[0], splitted[1]);
			}
		}//try
		
		catch (FileNotFoundException fne) { //catch error
				System.err.println("File Not Found");
				System.exit(1);
			}// catch
			
		finally {// if read was successful, close the file.
				if (fileReader != null) 
					fileReader.close();	
			}// finally


		combinationMap.remove("key");
		return combinationMap;
	}

	/**
	 * Writes given protein info & sequence dictionaries to a file with a given name.
	 * Creates a new file with a given file name
	 * if it does not exist or otherwise overwrites to it.
	 * @param fileName		a user-designated file name (file path)
	 * @param seqDict		a protein sequence dictionary to be written on a file
	 * @param infoDict		a protein information dictionary to written on a file
	 */
	
	public static void writeData(String fileName, Map<String, String> seqDict, Map<String, String> infoDict) {
	
		/*Enforce the precondition: Sequence dictionary and info dictionary have the same proteins*/
		if (!seqDict.keySet().equals(infoDict.keySet()))
			throw new IllegalArgumentException("Sequence dictionary and info dictionary do not have same proteins");
		
		Set<String> idSet = seqDict.keySet();
		
		StringBuilder proteinInfo = new StringBuilder ();
		StringBuilder proteinSeq  = new StringBuilder ();
		try {
			FileWriter dataWriter = new FileWriter(fileName);
			
			/* Concatenate the entire target protein database & decoy protein database*/
			for (String proteinID: idSet) {
				/* Initialize variables */
				proteinInfo.setLength(0);
				proteinSeq.setLength(0);
				
				/* Get values of each dictionary using the proteinID key */
				proteinInfo.append(infoDict.get(proteinID)); //get protein's info
				proteinSeq.append(seqDict.get(proteinID));	  //get protein's amino acid sequence
				
				/* Write in the file with the order:
				 * >  protein info 
				 * protein sequence (60 amino acid per line)
				
				 * */
				/*Target*/
				dataWriter.write(proteinInfo.toString());  
				dataWriter.write(System.lineSeparator()); 
				dataWriter.write(formatter(proteinSeq.toString()));	
				dataWriter.write(System.lineSeparator());
			}// for proteinID
			
			
			dataWriter.close(); //close after finish writing 
		}//try
		catch(IOException ioe){
			System.err.println("File Error");
		}//catch
		
	}//generateDatabse
	
	
}//end of class
