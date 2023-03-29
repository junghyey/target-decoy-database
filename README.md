# target-decoy-database

## Introduction
target-decoy-database is a project for generating concatenated target decoy databse. This project is inspired by the research paper [1] "Target-decoy search strategy for increased confidence in large-scale protein identifications by mass spectrometry". target-decoy-database contains java code & sample outputs for concatenated target-decoy database generation. It generates decoy data using three approaches: [1] random (completely), deBrujin[2], and [1] reverse. 

## Set up & Run
1. Generate random_result, deBruijn_result, reverse_result directories (outside source code file (src)) </br> Each directory should also contain subdirectories "target" and "decoy"
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

## References
[1] Elias, J., Gygi, S. Target-decoy search strategy for increased confidence in 
large-scale protein identifications by mass spectrometry.
Nat Methods 4, 207–214 (2007). https://doi.org/10.1038/nmeth1019

[2] J. Proteome Res. 2020, 19, 3, 1029–1036
Publication Date:February 3, 2020
https://doi.org/10.1021/acs.jproteome.9b00555

