//    Sig2BioPAXv4, converts SIG flat files to BioPAX level 3
//    Copyright (C) 2010 Ryan Logan Webb
//    This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.
//    This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
//    You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
package programCode;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.io.*;



public class Line {
	public static String SourceName ="";
    public static String SourceHumanAccession ="";
    public static String SourceMouseAccession ="";
    public static String SourceType ="";
    public static String SourceLocation ="";
    public static String TargetName ="";
    public static String TargetHumanAccession ="";
    public static String TargetMouseAccession ="";
    public static String TargetType ="";
    public static String TargetLocation ="";
    public static String Effect ="";
    public static String TypeofInteraction ="";
    public static String PubMedID ="";
    
    public static String BioSourceDB = "uniprot taxonomy";
    public static String BioSourceID = "9606";
    public static String BioSourceDisplayName = "Human";
    public static String ProteinDB = "Swiss-Prot";
    
    public static String EffectMeaning;
    
    static String inputTemplate;
    static String input;
    
    static int lineCount = 0;
   
    
    public static Scanner s = null;
    public static BufferedReader in = null;

    public static void FakeLine()
    {
                                  SourceName = "man";
				  SourceLocation = "tree" ;
				  TargetName = "worm";
				  TargetLocation = "ground";
				  Effect = "+";
				  TypeofInteraction = "eat";
				  PubMedID = "1234";
				  SourceHumanAccession = "gorilla";
				  SourceMouseAccession = "222";
				  SourceType = "man";
				  TargetHumanAccession = "333";
				  TargetMouseAccession = "444";
				  TargetType = "gorilla";
    }

    
    public static void ReadLine() throws IOException, NullPointerException
    {
    	try {
			lineCount++;
			input = in.readLine();
			s = new Scanner(input);

			  if (inputTemplate.equalsIgnoreCase("source_target"))
			  {
				  SourceName = s.next();
				  SourceLocation = s.next();       	 
				  TargetName = s.next();
				  TargetLocation = s.next();
				  Effect = s.next();
				  TypeofInteraction = s.next();
				  PubMedID = s.next();
				  SourceHumanAccession = "";
				  SourceMouseAccession = "";
				  SourceType = "";
				  TargetHumanAccession = "";
				  TargetMouseAccession = "";
				  TargetType = "";
			      		  
			  }
			  else if (inputTemplate.equalsIgnoreCase("tf_target"))
			  {
				  SourceName = s.next();
				  TargetName = s.next();
				  PubMedID = s.next();
				  TypeofInteraction = "transcription";
				  SourceLocation = "";    	 
				  TargetLocation = "";
				  Effect = "";
				  SourceHumanAccession = "";
				  SourceMouseAccession = "";
				  SourceType = "";
				  TargetHumanAccession = "";
				  TargetMouseAccession = "";
				  TargetType = "";
				  
				  if (SourceName.contains(PubMedID))
				  {
					  SourceName = SourceName.substring(0, (SourceName.length()-PubMedID.length()-1));
				  }
			  }
                          else if(inputTemplate.equalsIgnoreCase("sif"))
			  {
				  SourceName = s.next();
                                  TypeofInteraction = s.next();
				  SourceLocation = "";
				  TargetName = s.next();
				  TargetLocation = "";
				  Effect = "";
				  PubMedID = "";
				  SourceHumanAccession = "";
				  SourceMouseAccession = "";
				  SourceType = "";
				  TargetHumanAccession = "";
				  TargetMouseAccession = "";
				  TargetType = "";

			  }
				  
			  else
			  {
				SourceName = s.next();
				SourceHumanAccession = s.next();
				SourceMouseAccession = s.next();
				SourceType = s.next();
				SourceLocation = s.next();
				TargetName = s.next();
				TargetHumanAccession = s.next();
				TargetMouseAccession = s.next();
				TargetType = s.next();
				TargetLocation = s.next();
				Effect = s.next();
				TypeofInteraction = s.next();
				PubMedID = s.next();
	//			if (s.hasNext()) { throw LongLineException e }
			  }
			  if (Effect.equals("+") || (Effect.equals("1")))
			  {
				  EffectMeaning = "ACTIVATION";
			  }
			  else if (Effect.equals("-") || Effect.equals("_")|| Effect.equals("_"))
			  {
				  EffectMeaning = "INHIBITION";
			  }
			  else
			  {
				  EffectMeaning = "";
			  }
                          if (PubMedID.equals("N.N.")) PubMedID = "";
		} catch (NoSuchElementException e) {
            System.err.println("Line " + lineCount + " shorter than expected. Ignoring line");
            ResetAllToBlank();
		}
		finally {
			ResetNAToBlank();
			System.out.println("Processing Line number " + lineCount);
		}
		
        	       		  
    }
    
    	public static void ResetNAToBlank()
    	{
			  if (SourceName.equals("NA") || SourceName.equals("na")) {SourceName = "";}
			  if (SourceLocation.equals("NA") || SourceLocation.equals("na")) {SourceLocation = "";}      	 
			  if (TargetName.equals("NA") || TargetName.equals("na")) {TargetName = "";}
			  if (TargetLocation.equals("NA") || TargetLocation.equals("na")) {TargetLocation = "";}
			  if (Effect.equals("NA") || Effect.equals("na")) {Effect = "";}
			  if (TypeofInteraction.equals("NA") || TypeofInteraction.equals("na")) {TypeofInteraction = "NA";} // this one is left as NA, because that signals proteinProtein interaction
			  if (PubMedID.equals("NA") || PubMedID.equals("na")) {PubMedID = "";}
			  if (SourceHumanAccession.equals("NA") || SourceHumanAccession.equals("na")) {SourceHumanAccession = "";}
			  if (SourceMouseAccession.equals("NA") || SourceMouseAccession.equals("na")) {SourceMouseAccession = "";}
			  if (SourceType.equals("NA") || SourceType.equals("na")) {SourceType = "";}
			  if (TargetHumanAccession.equals("NA") || TargetHumanAccession.equals("na")) {TargetHumanAccession = "";}
			  if (TargetMouseAccession.equals("NA") || TargetMouseAccession.equals("na")) {TargetMouseAccession = "";}
			  if (TargetType.equals("NA") || TargetType.equals("na")) {TargetType = "";}
    	}
    	public static void ResetAllToBlank()
    	{
    		SourceName ="";
    	    SourceHumanAccession ="";
    	    SourceMouseAccession ="";
    	    SourceType ="";
    	    SourceLocation ="";
    	    TargetName ="";
    	    TargetHumanAccession ="";
    	    TargetMouseAccession ="";
    	    TargetType ="";
    	    TargetLocation ="";
    	    Effect ="";
    	    TypeofInteraction ="";
    	    String PubMedID ="";
    	}
       
        public static void SetupInput(String a, String b) {
            try {
   
                  inputTemplate = a;
             //     s = new Scanner(new BufferedReader(new FileReader(b)));
                  in = new BufferedReader(new FileReader(b));

                }
            catch (IOException e)
            {System.err.println("Caught IOException: " 
                    +  e.getMessage());
            System.exit(0);
            }


    }

    
    public static class UnificationXref
    {

		public static boolean isTargetProtein;
    	public static boolean isSourceProtein;
    	//public static boolean isSourceProteinLocation;
    	//public static boolean isTargetProteinLocation;
    	public static boolean isBioSource;
		public static boolean isPhosphorylatedResidue;
		public static boolean isCytosol;
		public static boolean isExtracellular;
		public static boolean isPhosphorylationReaction;
		public static boolean isUbiquitinationReaction;
		public static boolean isUbiquitinatedResidue;
		public static boolean isDephosphorylationReaction;
		
		public static String UbiquitinatedResidueDB =  "PSI-MI";
		public static String UbiquitinatedResidueID = "MI:0189";
		
		public static String UbiquitinationReactionDB = "PSI-MI";
		public static String UbiquitinationReactionID = "MI:0220";

		
    	public static String DephosphorylationReactionDB = "PSI-MI";
		public static String DephosphorylationReactionID = "MI:0203";
		public static String CleavageReactionDB = "PSI-MI";
		public static String CleavageReactionID = "MI:0194";
		public static boolean isDeUbiquitinationReaction;
		public static String DeUbiquitinationReactionDB = "PSI-MI";
		public static String DeUbiquitinationReactionID = "MI:0204";
		public static boolean isPLCCleavage;
		public static boolean isProteinCleavage;
		public static String ProteinCleavageReactionID = "PSI-MI";
		public static String ProteinCleavageReactionDB = "MI:0570";
		public static boolean isSumolation;
		public static String SumolationDB = "PSI-MI";
		public static String SumolationID = "MI:0566";
		public static boolean isSumolatedResidue;
		public static String SumolatedResidueDB = "PSI-MI";
		public static String SumolatedResidueID = "MI:0554";
		public static String ExtracellularDB = "GO";
		public static String ExtracellularID = "GO:0005576";
	    public static String PhosphorylatedResidueDB = "PSI-MI";
	    public static String PhosphorylatedResidueID = "MI:0170";
	    
	    public static String PhosphorylationReactionDB = "PSI-MI";
	    public static String PhosphorylationReactionID = "MI:0217";
	    
	    public static String CytosolDB = "GO";
	    public static String CytosolID = "GO:0005829";
	    
	    public static String VesicleDB = "GO";
	    public static String VesicleID = "GO:0031982";
		public static boolean isVesicle;
		
		public static String NucleusDB = "GO";
		public static String NucleusID = "GO:0005634";
		public static boolean isNucleus;
		
		public static String MitochondrionDB = "GO";
		public static String MitochondrionID = "GO:0005739";
		public static boolean isMitochondrion;
		
		public static void resetVarsToFalse()
		{
	    	isTargetProtein = false;
	    	isSourceProtein = false;
	    //	isSourceProteinLocation = false;
	    //	isTargetProteinLocation = false;
	    	isBioSource = false;
			isPhosphorylatedResidue = false;
			isCytosol = false;
			isPhosphorylationReaction = false;
			isUbiquitinationReaction = false;
			isDephosphorylationReaction = false;
			isPLCCleavage = false;
			isDeUbiquitinationReaction = false;
			isProteinCleavage = false;
			isSumolation = false;
			isSumolatedResidue = false;
			isExtracellular = false;
			isVesicle = false;
			isNucleus = false;
			isMitochondrion = false;
			isUbiquitinatedResidue = false;
		}
    }
    public static class ProteinReference
    {
    	public static boolean isTargetProtein;
    	public static boolean isSourceProtein;
		public static boolean isPIP2;
		public static boolean isIP3;
		public static boolean isDAG;
		public static String pip2 = "PIP2";
		public static String ip3 = "IP3";
		public static String dag = "DAG";
		public static boolean hasNoXref;
		public static boolean isTranscriptionProduct;
    	
		public static void resetVarsToFalse()
		{
	    	isTargetProtein = false;
	    	isSourceProtein = false;
	    	isPIP2 = false;
	    	isIP3 = false;
	    	isDAG = false;
	    	hasNoXref = false;
	    	isTranscriptionProduct = false;
		}
    }
    public static class Protein
    {
    	public static boolean isTargetProtein;
    	public static boolean isSourceProtein;
    	public static boolean hasCovalentBindingFeature;
    	public static boolean isPhosphorylated;
		public static boolean isUbiquitinated;
		public static boolean hasGDP;
		public static boolean isGEF;
		public static boolean hasGTP;
		public static boolean forceNew;
		public static boolean isDAG;
		public static boolean isIP3;
		public static boolean isPIP2;
		public static boolean isProProtein;
		public static boolean isSumolated;
		public static boolean isTranscription;
    	
		public static void resetVarsToFalse()
		{
	    	isTargetProtein = false;
	    	isSourceProtein = false;
	    	hasCovalentBindingFeature = false;
	    	isPhosphorylated = false;
	    	isUbiquitinated = false;
	    	hasGDP = false;
	    	hasGTP = false;
	    	isGEF = false;
	    	forceNew = false;
	    	isDAG = false;
	    	isIP3 = false;
	    	isPIP2 = false;
	    	isProProtein = false;
	    	isSumolated = false;
	    	isTranscription = false;
		}
    }
    
    public static class CovalentBindingFeature
    {
    	public static boolean isPhosphorylatedTarget;
		public static boolean isUbiquitinatedTarget;
		public static boolean hasGDP;
		public static boolean hasGTP;
		public static boolean isSumolatedTarget;
		public static boolean isProProtein;
		public static void resetVarsToFalse()
		{
	    	isPhosphorylatedTarget = false;
	    	isUbiquitinatedTarget = false;
	    	hasGDP = false;
	    	hasGTP = false;
	    	isSumolatedTarget = false;
	    	isProProtein = false;
		}
    }
    
    public static class SequenceModificationVocabulary
    {
    	public static boolean isPhosphorylatedTarget;
    	public static String phosphorylatedResidue = "phosphorylated residue";
		public static boolean isUbiquitinatedTarget;
		public static String ubiquitinatedResidue = "ubiquitinated lysine";
		public static boolean isGTP;
		public static boolean isGDP;
		public static String gtp = "guanine triphosphate";
		public static String gdp = "guanine diphosphate";
		public static boolean isSumolatedTarget;
		public static String sumolatedResidue = "sumoylated lysine";
		public static boolean isProProtein;
		public static String proProtein = "Pro-protein segment";
    	
		public static void resetVarsToFalse()
		{
	    	isPhosphorylatedTarget = false;
	    	isUbiquitinatedTarget = false;
	    	isGTP = false;
	    	isGDP = false;
	    	isSumolatedTarget = false;
	    	isProProtein = false;
		}
    }
    public static class CellularLocationVocabulary
    {
    	public static boolean isSourceProtein;
    	public static boolean isTargetProtein;
    	
		public static void resetVarsToFalse()
		{
	    	isTargetProtein = false;
	    	isSourceProtein = false;
		}
    }
    public static class BiochemicalReaction
    {
    	public static boolean isSourceProtein;
    	public static boolean isTargetProtein;
    	public static boolean isPhosphorylation;
		public static boolean isUbiquitination;
		public static boolean isGEF;
		public static boolean isDephosphorylation;
		public static boolean isPlcTarget;
		public static boolean isGAP;
		public static boolean isDeUbiquitination;
		public static boolean isProCleavage;
		public static boolean isSumolation;
    	
		public static void resetVarsToFalse()
		{
	    	isTargetProtein = false;
	    	isSourceProtein = false;
	    	isPhosphorylation = false;
	    	isUbiquitination = false;
	    	isGEF = false;
	    	isDephosphorylation = false;
	    	isPlcTarget = false;
	    	isGAP = false;
	    	isDeUbiquitination = false;
	    	isProCleavage = false;
	    	isSumolation = false;
		}
    }
    
    public static class InteractionVocabulary
    {
    	public static boolean isPhosphorylation;
    	public static String phosphorylationReaction = "phosphorylation reaction";
    	public static String ubiquitinationReaction = "ubiquitination reaction";
		public static boolean isUbiquitination;
		public static boolean isGEF;
		public static String gef = "guanine nucleotide exchange";
		public static boolean isDephosphorylation;
		public static String dephosphorylationReaction = "dephosphorylation reaction";
		public static boolean isPlcTarget;
		public static String plc = "cleavage by PLC";
		public static boolean isGAP;
		public static String gap = "GTPase activating protein";
		public static boolean isDeUbiquitination;
		public static String deUbiquitinationReaction = "deubiquitination reaction";
		public static boolean isProteinCleavage;
		public static String proteinCleavage = "cleavage on cysteine residue";
		public static boolean isProCleavage;
		public static String proProteinCleavage = "activating cleavage on pro-protein";
		public static boolean isSumolation;
		public static String sumolation = "sumoylation";
		public static boolean isTranscription;
		public static String transcription = "transcription";
    	
		public static void resetVarsToFalse()
		{
	    	isPhosphorylation = false;
	    	isUbiquitination = false;
	    	isGEF = false;
	    	isDephosphorylation = false;
	    	isPlcTarget = false;
	    	isGAP = false;
	    	isDeUbiquitination = false;
	    	isProteinCleavage = false;
	    	isProCleavage = false; // pro-protein cleavage
	    	isSumolation = false;
	    	isTranscription = false;
		}
    }
    
    public static class Catalysis
    {
    	public static boolean isKinasePhosphorylation;
    	public static String kinasePhosphorylation = "kinase phosphorylation";
    	public static String ubiquitination = "ubiquitination";
    	public static String gef = "guanine nucleotide exchange factor";
		public static boolean isUbiquitination;
		public static boolean isGEF;
		public static boolean isDePhosphorylation;
		public static String dephosphorylation = "dephosphorylation";
		public static boolean isPLC;
		public static String plc = "cleavage with phospholipase c";
		public static boolean isGAP;
		public static String gap = "GTPase activating protein reaction";
		public static boolean isCysProtease;
		public static boolean isDeUbiquitination;
		public static String DeUbiquitination = "Deubiquitination";
		public static String CysProtease = "Cleavage on cysteine residue";
		public static boolean isProtease;
		public static String Protease = "Cleavage";
		public static boolean isProCleavage;
		public static String ProCleavage = "Activating cleavage";
		public static boolean isSumolation;
		public static String Sumolation = "Sumoylation";
		public static boolean hasNoPublicationXref;
    	
		public static void resetVarsToFalse()
		{
	    	isKinasePhosphorylation = false;
	    	isUbiquitination = false;
	    	isGEF = false;
	    	isDePhosphorylation = false;
	    	isPLC = false;
	    	isGAP = false;
	    	isCysProtease = false;
	    	isDeUbiquitination = false;
	    	isProtease = false;
	    	isProCleavage = false;
	    	isSumolation = false;
	    	hasNoPublicationXref = false;
		}
    }
    public static class PublicationXref
    {
    	public static String publicationDB = "Pubmed";
    	
		public static void resetVarsToFalse()
		{
		}
    }
    public static class ComplexAssembly
    {
    	public static boolean isBinding;
		public static String binding = "complex assembly";
		public static boolean hasNoPublicationXref;
    	
    	public static void resetVarsToFalse()
    	{
    		isBinding = false;
    		hasNoPublicationXref = false;
    	}
    	
    }
    
    public static class Complex
    {
    	public static boolean isSource;
		public static boolean isBinding;
		
		public static String complexName;
    	
    	public static void resetVarsToFalse()
    	{
    		isSource = false;
    		isBinding = false;
    	}
    }
    
    public static class Degradation
    {
   
    	
    	public static boolean isCleavage;
		public static boolean isTargetProtein;

		public static void resetVarsToFalse()
    	{
			isCleavage = false;
			isTargetProtein = false;
    	}
    }
    
    public static class TemplateReactionRegulation
    {
    //	public static boolean is
		public static boolean isIndirectRegulation;
		public static String indirectRegulation = "indirect transcriptional regulation";
		public static boolean isDirectTranscription;
		public static String directTranscription = "transcription factor interaction with DNA";
		public static boolean hasNoPublicationXref;
    	
    	public static void resetVarsToFalse()
    	{
    		isIndirectRegulation = false;
    		isDirectTranscription = false;
    		hasNoPublicationXref = false;
    	}
    }
    
    public static class TemplateReaction
    {
    	public static boolean isTranscription;
		public static boolean isTargetProtein;
		public static String transcription = "transcription";

		public static void resetVarsToFalse()
    	{
			isTranscription = false;
			isTargetProtein = false;
    	}   	
    }
  
    public static class RNA
    {
    	public static boolean isTarget;
    	
    	public static void resetVarsToFalse()
    	{
    		isTarget = false;
    	}
    }
    
    public static class DNA
    {
    	public static boolean isTarget;
		public static boolean isTranscription;
    	
    	public static void resetVarsToFalse()
    	{
    		isTarget = false;
    		isTranscription = false;
    	}
    }
    
    public static class MolecularInteraction
    {
    	public static boolean isProteinProtein;
    	public static String proteinProtein = "protein-protein interaction";
		public static boolean hasNoPublicationXref;
    	
    	public static void resetVarsToFalse()
    	{
    		isProteinProtein = false;
    		hasNoPublicationXref = false;
    	}
    }
  }
    





    

