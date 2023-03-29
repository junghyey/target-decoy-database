package target_decoy;



/**
 * Generates target decoy concatenated data file.
 * @author junghyey
 *
 */

public class TargetDecoySim {

	/*main method starts here*/
	public static void main(String[] args) {

		TargetDecoy targetDecoySim = new TargetDecoy("human_swiss_prot_target.fasta");

		targetDecoySim.targetDecoyDeBruijn("../deBruijn_result/concat_target_decoy_deBruijn.fasta");
		targetDecoySim.targetDecoyRandom("../random_result/concat_target_decoy_random.fasta");
		targetDecoySim.targetDecoyReverse("../reverse_result/concat_target_decoy_reverse.fasta");


	

	
	}//main

}//end of class



