package target_decoy;

import java.util.Map;

/**
 * Creates/Represents target & decoy database(s)  
 * @author junghyey
 */
public class TargetDecoy {

	
	private String targetFileName; //(original) target protein file
	
	/**
	 * Constructor for the class
	 * @param targetFileName	target protein file to use to generate decoy
	 */
	public TargetDecoy(String targetFileName) {
		this.targetFileName = targetFileName;
	}// TargetDecoy
	
	/**
	 * Generate target-decoy concatenated data file & reports about
	 * target & decoy(using random approach).
	 * @param targetDecoyFileName	full name (path + name) of the concatenated target decoy file to generate
	 * 								
	 */
	public void targetDecoyRandom(String targetDecoyFileName) {
		
	
		//*******************************************Target*******************************************
		DictionaryGenerator targetData = new DictionaryGenerator(targetFileName);
		Map<String, String> targetSeqDict = targetData.getSeqDict();
		Map<String, String> targetInfoDict = targetData.getInfoDict();
		
	
		
		//*******************************************Decoy*********************************************
		/*Instantiate class*/
		RandomDecoy decoyData = new RandomDecoy(targetSeqDict, targetInfoDict);
		Map<String, Integer> zeroRemoved = Counter.countZeroRemoved(targetSeqDict);
		decoyData.setRangeMap(Counter.generateRange(zeroRemoved));

		decoyData.generateDecoyDict();
		decoyData.generateDecoyInfo();

		/*Store decoy sequence & info*/
		Map<String, String> decoySeqDict = decoyData.getDseqDict();
		Map<String, String> decoyInfoDict = decoyData.getDinfoDict();

		
		/*Generate concatenated database file*/
		Database.generateDatabase(targetDecoyFileName,targetSeqDict, 
								  decoySeqDict,targetInfoDict, 
								  decoyInfoDict);
		
		//*******************************************Reports********************************************
		/*Generate reports for target */
		Database.writeDictionary("../random_result/target/target_sequence_r.txt",  targetSeqDict);
		Database.writeDictionary("../random_result/target/target_info_r.txt",  targetInfoDict);
		Database.writeCSV("../random_result/target/target_amino_acid_count_r.csv",  Counter.countAminoAcid(targetSeqDict));
		Database.writeCSV("../random_result/target/target_amino_acid_proportion_r.csv",  Counter.getProportion(targetSeqDict, 6));
		
		
		/*Generate reports for decoy random*/
		Database.writeData("../random_result/decoy/decoy_random.fasta", decoySeqDict, decoyInfoDict);
		Database.writeCSV("../random_result/decoy/decoy_amino_acid_count_r.csv",  Counter.countAminoAcid(decoySeqDict));
		Database.writeCSV("../random_result/decoy/decoy_aminoacid_proportion_r.csv", Counter.getProportion(decoySeqDict, 6));
		
		
	}//targetDecoyRandom
	
	/**
	 * Generate target-decoy concatenated data file & reports about
	 * target & decoy(using deBruijn approach).
	 * @param targetDecoyFileName	full name(path + name) of the target decoy file to generate
	 */
	public void targetDecoyDeBruijn(String targetDecoyFileName) {
		
		//*******************************************Target*******************************************
		DictionaryGenerator targetData  = new DictionaryGenerator(targetFileName);
		
		Map<String, String> targetSeqDict  = targetData.getSeqDict();
		Map<String, String> targetInfoDict = targetData.getInfoDict();
		
		//*******************************************Decoy*********************************************
		/*Instantiate class*/
		DeBruijnDecoy decoyData = new DeBruijnDecoy(targetSeqDict, targetInfoDict);
		Map <String, String> modifiedTargetSeq = DeBruijnDecoy.addDummySeq(targetSeqDict);		
		decoyData.setModifiedSeq(modifiedTargetSeq);

	
		/*Get all the possible three letter (position, position + 1, position +2) combinations*/
		decoyData.countAllCombination();	
		
		// /* Get the new amino acid count to be used for decoy data set  */
		Map<String, Integer> zeroRemoved = Counter.countZeroRemoved(targetSeqDict);
		decoyData.setRangeMap(Counter.generateRange(zeroRemoved));
		
		// /*Generate decoy sequence & info header */
		decoyData.mapCombination(); //map three letter combination to a amino acid
		decoyData.generateDecoyDict();
		decoyData.generateDecoyInfo();
		Map<String, String> decoySeqDict = decoyData.getDseqDict();
		Map<String, String> decoyInfoDict = decoyData.getDinfoDict();

		/* Confirm postcondition: Every generated decoy sequence can be decoded and the decoded
		 * 						  sequence should be equal to the according target protein sequence.				 
		 */
		assert(decoyData.isAllMappingCorrect(targetSeqDict, decoySeqDict));
		
		/*Generate concatenated database file*/
		Database.generateDatabase(targetDecoyFileName,targetSeqDict, 
								  decoySeqDict,targetInfoDict, 
								  decoyInfoDict);
		
		//*******************************************Reports********************************************
		/*Generate reports for target */
		Database.writeCSV("../deBruijn_result/target/target_amino_acid_count_d.csv",  Counter.countAminoAcid(targetSeqDict));
		Database.writeCSV("../deBruijn_result/target/target_amino_acid_proportion_d.csv",  Counter.getProportion(targetSeqDict, 6));

		
		/*Generate reports for decoy random*/
		Database.writeData("../deBruijn_result/decoy/decoy_deBrujin.fasta", decoySeqDict, decoyInfoDict);
		Database.writeCSV("../deBruijn_result/decoy/dummy_added_sequence_d.csv", modifiedTargetSeq);
		Database.writeCSV("../deBruijn_result/decoy/decoy_combination_count_d.csv", decoyData.getCombinationCount());
		Database.writeCSV("../deBruijn_result/decoy/decoy_combination_map_d.csv", decoyData.getCombinationMap());
		Database.writeCSV("../deBruijn_result/decoy/decoy_amino_acid_count_d.csv",  Counter.countAminoAcid(decoySeqDict));
		Database.writeCSV("../deBruijn_result/decoy/decoy_amino_acid_proportion_d.csv", Counter.getProportion(decoySeqDict, 6));

	}//targetDecoyDeBruijn		


	
	/**
	 * Generate target-decoy concatenated data file & reports about
	 * target & decoy(reverse).
	 * @param targetDecoyFile	full name of the target decoy file to generate
	 */
	public void targetDecoyReverse(String targetDecoyFile) {
		
		//*******************************************Target*******************************************
		DictionaryGenerator    targetData     = new DictionaryGenerator(targetFileName);
		Map<String, String> targetSeqDict  = targetData.getSeqDict();
		Map<String, String> targetInfoDict = targetData.getInfoDict();
		
		//*******************************************Decoy*********************************************
		/*Instantiate class*/
		ReverseDecoy decoyData = new ReverseDecoy(targetSeqDict, targetInfoDict);
		
		decoyData.generateDecoyDict();
		decoyData.generateDecoyInfo();
		Map<String, String> decoySeqDict = decoyData.getDseqDict();
		Map<String, String> decoyInfoDict = decoyData.getDinfoDict();

		
		/*Generate concatenated database file*/
		Database.generateDatabase(targetDecoyFile,targetSeqDict, 
								  decoySeqDict,targetInfoDict, 
								  decoyInfoDict);
		
		//*******************************************Reports********************************************
		/*Generate reports for target */
		Database.writeCSV("../reverse_result/target/target_amino_acid_count_rv.csv",  Counter.countAminoAcid(targetSeqDict));
		Database.writeCSV("../reverse_result/target/target_amino_acid_proportion_rv.csv",  Counter.getProportion(targetSeqDict, 6));

		
		/*Generate reports for decoy random*/
		Database.writeData("../reverse_result/decoy/decoy_reverse.fasta", decoySeqDict, decoyInfoDict);
		Database.writeCSV("../reverse_result/decoy/decoy_amino_acid_count_rv.csv",  Counter.countAminoAcid(decoySeqDict));
		Database.writeCSV("../reverse_result/decoy/decoy_amino_acid_proportion_rv.csv", Counter.getProportion(decoySeqDict, 6));

	}//targetDecoyReverse	

}//end of class
