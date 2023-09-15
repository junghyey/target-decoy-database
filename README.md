# target-decoy-database

## Part1: Target Decoy Database Creation
### Introduction
target-decoy-database is a project for generating a concatenated target decoy database. This project is inspired by the research paper [1] "Target-decoy search strategy for increased confidence in large-scale protein identifications by mass spectrometry". target-decoy-database contains java code & sample outputs for concatenated target-decoy database generation. It generates decoy data using three approaches: [1] random (completely), deBruijn[2], and [1] reverse. 

### Set up & Run
1. (Skip if the directories already exist) Generate random_result, deBruijn_result, reverse_result directories (path: target-decoy-database>target_decoy) </br> Each directory should also contain subdirectories "target" and "decoy"
![structure](https://user-images.githubusercontent.com/102386164/228535078-d8b08346-830b-48c0-bfaa-6725218d3224.png) </br>
2. Download the human protein file from: https://www.uniprot.org/   Proteins>Human>Reviewed(Swiss-Prot) </br> Save the protein file as "human_swiss_prot_target" (FASTA file) in the src folder (Check the structure figure in 1.)</br>
3. To compile: </br>
Working directory: target_decoy>src 
```bash
bash compile_all.sh
```
4. To run:</br>
Working directory: target_decoy>src
```bash
java target_decoy/TargetDecoySim
```

### About Files

1. Concatenated target decoy database </br>
The concatenated database would look like: </br></br>
\>sp|ProteinID|something about target protein (target protein info)</br>
target protein sequence </br>
\>XXX_sp|ProteinID|something about decoy protein (decoy protein info)</br>
decoy protein sequence </br>

2. Report files </br> 
amino_acid_count files: total count of amino acid across all the proteins
amino_acid_proportion files: an amino acid/ total amino acid count </br> 
3. Peptide files </br>
Cleavage based on "Trypsin" peptide </br> 
decoy_peptide_length: lengths of the peptides created   length, count </br> 
decoy_peptide_mass: mass of the peptides created        mass, count </br> 
decoy_peptides: created peptides                        proteinID, [list of peptides]  </br> 

## Part2: Comet Data Analysis

### Comet Sample Result
1. Ran comet with:
DataBase: concat_target_decoy_reverse.fasta
DataBase: concat_target_decoy_deBruijn.fasta
ProteinFile: 	f.MSV000079841/ccms_peak/PEAK/b1906_293T_proteinID_01A_QE3_122212.mzXML from [https://massive.ucsd.edu/ProteoSAFe/dataset_files.jsp?task=9101dafdf9d8484e86fac78cf7024b9f#%7B%22table_sort_history%22%3A%22main.collection_asc%22%2C%22main.collection_input%22%3A%22ccms_peak%7C%7CEXACT%22%7D][https://massive.ucsd.edu/ProteoSAFe/dataset_files.jsp?] 

from sample_results. These results are saved in data_analysis/comet_sample_result
3. 


## References
[1] Elias, J., Gygi, S. Target-decoy search strategy for increased confidence in 
large-scale protein identifications by mass spectrometry.
Nat Methods 4, 207–214 (2007). https://doi.org/10.1038/nmeth1019

[2] J. Proteome Res. 2020, 19, 3, 1029–1036
Publication Date:February 3, 2020
https://doi.org/10.1021/acs.jproteome.9b00555

