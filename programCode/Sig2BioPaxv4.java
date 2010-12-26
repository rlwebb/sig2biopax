//    Sig2BioPAXv3, converts SIG flat files to BioPAX level 3
//    Copyright (C) 2010 Ryan Logan Webb
//    This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.
//    This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
//    You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
package programCode;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Set;

import javax.swing.JOptionPane;

import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.sparql.function.library.e;
import com.hp.hpl.jena.ontology.*;
import com.hp.hpl.jena.util.*;

public class Sig2BioPaxv4 {

	
	public static String biopaxString = "http://www.biopax.org/release/biopax-level3.owl#";
	public static String namespaceString = "http://www.mssm.edu/labs/maayan#";
	
    public static int main (String args[]) {
    	
    	String inFile = "input.txt";
      	String outFile = "output.owl";
      	String inFormat = "sig";
    	boolean overwrite = false;
        for (int i = 0; i < args.length; i++)
        {
            if (args[i].startsWith("-in:")) inFile = args[i].substring(4);
            else if (args[i].startsWith("-out:")) outFile = args[i].substring(5);
            else if (args[i].equalsIgnoreCase("-o")) overwrite = true;
            else if (args[i].startsWith("-t:")) inFormat = args[i].substring(3);
        }


        
        PrintWriter out = null;


        try {
            File file = new File(outFile);

           
        	if (!overwrite)
        	{
                    // TODO Fix bug... if file has no . in name, throws error
        		int ind = outFile.lastIndexOf('.');
        		String subst = outFile.substring(0,ind);
        		String subst2 = outFile.substring(ind,outFile.length());
        		String subst3 = subst;
        		
        		if (file.exists())
        		{
	        		for(int j = 0; Character.isDigit(subst.charAt(subst.length()-j-1)); j++)
	        		{        			
	        			subst3 = subst.substring(0,subst.length()-j);
	        		}
	        		outFile = (subst3+subst2);
	        		file = new File(outFile);
        		}

            	for (int i = 1; file.exists(); i++)
            	{
            		outFile = (subst3 + i + subst2);
            		file = new File(outFile);
            	}
        	}
        	out = new PrintWriter(new FileOutputStream(outFile,false));
        }
		catch (IOException e) {
            System.err.println("Caught IOException: " 
                    +  e.getMessage());
            System.exit(0);
            }
    	OntModel biopaxmodel = ModelFactory.createOntologyModel
    	                (OntModelSpec.OWL_MEM,null);
    	

    	
    	biopaxmodel.setNsPrefix("bp",biopaxString);
    	biopaxmodel.setNsPrefix("",namespaceString);
    	
    	String namespaceFileString = "http://www.mssm.edu/labs/maayan";
    	Ontology ont = biopaxmodel.createOntology(namespaceFileString);
    	
    	String biopaxFileString = "http://www.biopax.org/release/biopax-level3.owl";
    	ont.addImport(biopaxmodel.createResource(biopaxFileString));
    	    	Line.SetupInput(inFormat, inFile);
    	
    	Log logger = new Log();
       try {
    	while (true)
        {
    	      Line.ReadLine();

           if (Line.TypeofInteraction.equalsIgnoreCase("phosphorylation")
        		   && (Line.SourceType.equalsIgnoreCase("") || Line.SourceType.equalsIgnoreCase("")))
           {
        	   Line.Catalysis.isKinasePhosphorylation = true;
        	   if (Line.PubMedID.equals("")){Line.Catalysis.hasNoPublicationXref = true;}
                   addNewCatalysis(biopaxmodel);
           }
           if (Line.TypeofInteraction.equalsIgnoreCase("phosphorylation")
        		   && (Line.SourceType.equalsIgnoreCase("kinase") || Line.SourceType.equalsIgnoreCase("receptor")))
           {
        	   Line.Catalysis.isKinasePhosphorylation = true;
        	   if (Line.PubMedID.equals("")){Line.Catalysis.hasNoPublicationXref = true;}
                   addNewCatalysis(biopaxmodel);
           }
           else if (Line.TypeofInteraction.equalsIgnoreCase("dephosphorylation"))
    	   {
    		   Line.Catalysis.isDePhosphorylation = true;
        	   if (Line.PubMedID.equals("")){Line.Catalysis.hasNoPublicationXref = true;}
        	   addNewCatalysis(biopaxmodel);
    	   } 
           else if (Line.TypeofInteraction.equalsIgnoreCase("ubiquitination"))
           {
        	   Line.Catalysis.isUbiquitination = true;
        	   if (Line.PubMedID.equals("")){Line.Catalysis.hasNoPublicationXref = true;}
               addNewCatalysis(biopaxmodel);
           } 
           else if (Line.TypeofInteraction.equalsIgnoreCase("deubiquitination"))
           {
        	   Line.Catalysis.isDeUbiquitination = true;
        	   if (Line.PubMedID.equals("")){Line.Catalysis.hasNoPublicationXref = true;}
               addNewCatalysis(biopaxmodel);
           }
           else if (Line.TypeofInteraction.equalsIgnoreCase("gef"))
           {
        	   Line.Catalysis.isGEF = true;
        	   if (Line.PubMedID.equals("")){Line.Catalysis.hasNoPublicationXref = true;}
        	   addNewCatalysis(biopaxmodel);
           } 
    	   else if (Line.TypeofInteraction.equalsIgnoreCase("binding"))
    	   {
    		   Line.ComplexAssembly.isBinding = true;
        	   if (Line.PubMedID.equals("")){Line.ComplexAssembly.hasNoPublicationXref = true;}
    		   addNewComplexAssembly(biopaxmodel);
    	   } 
    	   else if ((Line.TypeofInteraction.equalsIgnoreCase("cleavage") || Line.TypeofInteraction.equalsIgnoreCase("synthesis"))
    			   && Line.SourceType.equalsIgnoreCase("phospholipase") && Line.SourceName.toLowerCase().contains("plc"))
    	   {
    		   Line.Catalysis.isPLC = true;
        	   if (Line.PubMedID.equals("")){Line.Catalysis.hasNoPublicationXref = true;}
    		   addNewCatalysis(biopaxmodel);
    	   } 
    	   else if (Line.TypeofInteraction.equalsIgnoreCase("gap"))
    	   {
    		   Line.Catalysis.isGAP = true;
        	   if (Line.PubMedID.equals("")){Line.Catalysis.hasNoPublicationXref = true;}
    		   addNewCatalysis(biopaxmodel);
    	   } 
    	   else if (Line.TypeofInteraction.equalsIgnoreCase("cleavage") && Line.SourceType.equalsIgnoreCase("protease") && (Line.Effect.equals("_") || Line.Effect.equals("-")))
    	   {
    		   Line.Catalysis.isProtease = true;
        	   if (Line.PubMedID.equals("")){Line.Catalysis.hasNoPublicationXref = true;}
    		   addNewCatalysis(biopaxmodel);
    	   } 
    	   else if (Line.TypeofInteraction.equalsIgnoreCase("cleavage") && Line.SourceType.equalsIgnoreCase("protease") && Line.Effect.equals("+"))
    	   {
    		   Line.Catalysis.isProCleavage = true;
        	   if (Line.PubMedID.equals("")){Line.Catalysis.hasNoPublicationXref = true;}
    		   addNewCatalysis(biopaxmodel);
    	   } 
    	   else if (Line.TypeofInteraction.equalsIgnoreCase("sumolation") || Line.TypeofInteraction.equalsIgnoreCase("sumoylation"))
    	   {
    		   Line.Catalysis.isSumolation = true;
        	   if (Line.PubMedID.equals("")){Line.Catalysis.hasNoPublicationXref = true;}
    		   addNewCatalysis(biopaxmodel);
    	   } 
    	   else if (Line.TypeofInteraction.equalsIgnoreCase("tfpromoterbinding"))
    	   {
    		   Line.ComplexAssembly.isBinding = true;
        	   if (Line.PubMedID.equals("")){Line.ComplexAssembly.hasNoPublicationXref = true;}
    		   addNewComplexAssembly(biopaxmodel);
    	   }
    	   else if (Line.TypeofInteraction.equalsIgnoreCase("indirecttranscription"))
    	   {
    		   Line.TemplateReactionRegulation.isIndirectRegulation = true;
        	   if (Line.PubMedID.equals("")){Line.TemplateReactionRegulation.hasNoPublicationXref = true;}
    		   addNewTemplateReactionRegulation(biopaxmodel);
    	   }
    	   else if (Line.TypeofInteraction.equalsIgnoreCase("transcription"))
    	   {
    		   Line.TemplateReactionRegulation.isDirectTranscription = true;
        	   if (Line.PubMedID.equals("")){Line.TemplateReactionRegulation.hasNoPublicationXref = true;}
    		   addNewTemplateReactionRegulation(biopaxmodel);
    	   }
    	   else if (Line.TypeofInteraction.equalsIgnoreCase("proteinprotein"))
    	   {
        	   if (Line.PubMedID.equals("")){Line.MolecularInteraction.hasNoPublicationXref = true;}
    		   addNewMolecularInteraction(biopaxmodel);
    	   }
    	   else if (Line.TypeofInteraction.equalsIgnoreCase("NA"))
    	   {
        	   if (Line.PubMedID.equals("")){Line.MolecularInteraction.hasNoPublicationXref = true;}
    		   addNewMolecularInteraction(biopaxmodel);
    	   }

    	   else if (Line.TypeofInteraction.equalsIgnoreCase("activation") || Line.TypeofInteraction.equalsIgnoreCase("inhibition"))
    	   {
    		   if (Line.SourceType.equalsIgnoreCase("TyrKinase") || Line.SourceType.equalsIgnoreCase("SerThrKinase") || Line.SourceType.equalsIgnoreCase("PtdInsKinase") || Line.SourceType.equalsIgnoreCase("Receptor") || Line.SourceType.equalsIgnoreCase("DualKinase"))
    		   {
    			   Line.Catalysis.isKinasePhosphorylation = true;
            	   if (Line.PubMedID.equals("")){Line.Catalysis.hasNoPublicationXref = true;}
    			   addNewCatalysis(biopaxmodel);
    		   }
    		    else if (Line.SourceType.equalsIgnoreCase("adaptor") || Line.SourceType.equalsIgnoreCase("cytoskeletal") || Line.SourceType.equalsIgnoreCase("metalloproteinase") || Line.SourceType.equalsIgnoreCase("adhesion") || Line.SourceType.equalsIgnoreCase("messenger") || Line.SourceType.equalsIgnoreCase("actinbinding") || Line.SourceType.equalsIgnoreCase("receptor"))
    		    {
    		      Line.ComplexAssembly.isBinding = true;
           	   if (Line.PubMedID.equals("")){Line.ComplexAssembly.hasNoPublicationXref = true;}
    		      addNewComplexAssembly(biopaxmodel);
    		    }
    		   else if (Line.SourceType.toLowerCase().contains("gef") && Line.TargetType.equalsIgnoreCase("gtpase"))
    		   {
    			   Line.Catalysis.isGEF = true;
            	   if (Line.PubMedID.equals("")){Line.Catalysis.hasNoPublicationXref = true;}
    			   addNewCatalysis(biopaxmodel);
    		   }
    		   else if (Line.SourceType.toLowerCase().contains("gef") && !Line.TargetType.equalsIgnoreCase("gtpase"))
    		   {
    			   Line.ComplexAssembly.isBinding = true;
            	   if (Line.PubMedID.equals("")){Line.ComplexAssembly.hasNoPublicationXref = true;}
    			   addNewComplexAssembly(biopaxmodel);
    		   }
    		   else if (Line.SourceType.equalsIgnoreCase("e3ligase") && (Line.Effect.equals("_") || Line.Effect.equals("-")))
    		   {
    			   Line.Catalysis.isUbiquitination = true;
            	   if (Line.PubMedID.equals("")){Line.Catalysis.hasNoPublicationXref = true;}
    			   addNewCatalysis(biopaxmodel);
    		   }
    		   else if (Line.SourceName.equalsIgnoreCase("pip2") || Line.SourceName.equalsIgnoreCase("pip3"))
    		   {
    			   Line.ComplexAssembly.isBinding = true;
            	   if (Line.PubMedID.equals("")){Line.ComplexAssembly.hasNoPublicationXref = true;}
    			   addNewComplexAssembly(biopaxmodel);
    		   }
    		   else if (Line.SourceType.equalsIgnoreCase("gtpase"))
    		   {
    			   Line.ComplexAssembly.isBinding = true;
            	   if (Line.PubMedID.equals("")){Line.ComplexAssembly.hasNoPublicationXref = true;}
    			   addNewComplexAssembly(biopaxmodel);
    		   }
    		   else if (Line.SourceType.equalsIgnoreCase("phospholipase"))
    		   {
    			   Line.ComplexAssembly.isBinding = true;
            	   if (Line.PubMedID.equals("")){Line.ComplexAssembly.hasNoPublicationXref = true;}
    			   addNewComplexAssembly(biopaxmodel);
    		   }
    		   else if (Line.SourceName.toLowerCase().contains("GAP") && Line.TargetType.equalsIgnoreCase("gtpase"))
    		   {
    			   Line.Catalysis.isGAP = true;
            	   if (Line.PubMedID.equals("")){Line.Catalysis.hasNoPublicationXref = true;}
    			   addNewCatalysis(biopaxmodel);
    		   }
			    else if (Line.SourceType.equalsIgnoreCase("tyrphosphatase") || Line.SourceType.equalsIgnoreCase("serthrphosphatase") || Line.SourceType.equalsIgnoreCase("ptdinsphosphatase"))
			    {
			      Line.Catalysis.isDePhosphorylation = true;
	        	   if (Line.PubMedID.equals("")){Line.Catalysis.hasNoPublicationXref = true;}
			      addNewCatalysis(biopaxmodel);
			      
			    }
			    else if (Line.SourceType.equalsIgnoreCase("cysprotease"))
			    {
			    	Line.Catalysis.isCysProtease = true;
		        	   if (Line.PubMedID.equals("")){Line.Catalysis.hasNoPublicationXref = true;}
			    	addNewCatalysis(biopaxmodel);
			    }
	    	    else
		    	   {
		    		   logger.LogLine();
		    	   }
    	   }
    	   else
    	   {
    		   logger.LogLine();
    	   }
        }
       }
		catch (IOException e)
		{
            System.err.println("Caught IOException: " 
                    +  e.getMessage());
            System.exit(0);
		}
		catch (NullPointerException e)
		{
		}
 
                
 
    			RDFWriter writer = biopaxmodel.getWriter("RDF/XML-ABBREV");
    			writer.setProperty("xmlbase",namespaceFileString);
    			writer.write(biopaxmodel,out,namespaceString);
    

    		
    		out.close();
                return 1;
    	
    }

	private static int addNewMolecularInteraction(OntModel biopaxmodel) {
   		String thisClass = "MolecularInteraction";
		String individualClass1 = "PublicationXref";
		String individualClass2 = "Protein";
		String individualClass3 = "Protein";
	
		String propertyName1 = "xref";
		String propertyName2 = "participant";
		String propertyName3 = "participant";
	   
		String propertyNameLiteral1 = "displayName";
	    
	    int publicationXrefNum = -1;
	    int proteinNum = -1;
	    int proteinNum2 = -1;
	    
	    
   		List<Set> allPropertyList = new ArrayList<Set>();
   		
   		if (true)
   		{	
   			Set<Resource> checky2 = Biopax.checkSingleLiteral(Line.MolecularInteraction.proteinProtein, propertyNameLiteral1, thisClass, biopaxmodel);
   		    allPropertyList.add(checky2);
   		    

   		    Line.Protein.isSourceProtein = true;
   		    proteinNum = addNewProtein(biopaxmodel);
   	    	String testy4 = (individualClass3+"_"+String.valueOf(proteinNum));  
   	   		Set<Resource> checky4 = Biopax.checkSingleLinked(testy4, propertyName3, thisClass, biopaxmodel);
   		    allPropertyList.add(checky4);
   		    
   	//	    Line.TemplateReaction.isTranscription = Line.TemplateReactionRegulation.isIndirectRegulation;
   		    Line.Protein.isTargetProtein = true;
   		    proteinNum2 = addNewProtein(biopaxmodel);
   		    String testy5 = (individualClass2+"_"+String.valueOf(proteinNum2));
   		    Set<Resource> checky5 = Biopax.checkSingleLinked(testy5, propertyName2, thisClass, biopaxmodel);
   		    allPropertyList.add(checky5);
   		}
		List<Set> allPropertyList2 = new ArrayList<Set>();
   		if (!Line.MolecularInteraction.hasNoPublicationXref)
   		{
		publicationXrefNum = addNewPublicationXref(biopaxmodel);
		String testy = (individualClass1+"_"+publicationXrefNum);  
   		Set<Resource> checky = Biopax.checkSingleLinked(testy, propertyName1, thisClass, biopaxmodel);
	    allPropertyList2.add(checky);
   		}
	
   		
		Resource result = allPropertyListGetIntersection(allPropertyList,biopaxmodel);
   		int num = convertResourceToInt(result, thisClass);

   		
   		Resource result2 = allPropertyListGetIntersection(allPropertyList2,biopaxmodel);
   		int num2 = convertResourceToInt(result2, thisClass);
   		
   		if (num == -1)
   		{
   			num = Biopax.createBiopaxIndividual(thisClass,biopaxmodel);
   	   		String individualName = thisClass + "_" + num;

			String linkedresourceName1 = individualClass1 + "_" + publicationXrefNum;
			String linkedresourceName2 = individualClass2 + "_" + proteinNum;
			String linkedresourceName3 = individualClass3 + "_" + proteinNum2;

			Biopax.addBiopaxProperty(Line.MolecularInteraction.proteinProtein,propertyNameLiteral1,individualName,biopaxmodel);	
	   		if (!Line.MolecularInteraction.hasNoPublicationXref)
	   		{
	   			Biopax.addBiopaxLinkedProperty(propertyName1, linkedresourceName1, individualName, biopaxmodel);
	   		}
			Biopax.addBiopaxLinkedProperty(propertyName2, linkedresourceName2, individualName, biopaxmodel);
			Biopax.addBiopaxLinkedProperty(propertyName3, linkedresourceName3, individualName, biopaxmodel);
   		}
   		else if (num2 == -1 && !Line.MolecularInteraction.hasNoPublicationXref)
   		{
   	  		String individualName2 = thisClass + "_" + num;
   			String linkedresourceName = individualClass1 + "_" + publicationXrefNum;
			Biopax.addBiopaxLinkedProperty(propertyName1, linkedresourceName, individualName2, biopaxmodel);
   		}
   		Line.MolecularInteraction.resetVarsToFalse();
   		return num;
		
	}

	private static int addNewTemplateReactionRegulation(OntModel biopaxmodel) {
   		String thisClass = "TemplateReactionRegulation";
		String individualClass1 = "PublicationXref";
		String individualClass2 = "TemplateReaction";
		String individualClass3 = "Protein";
	
		String propertyName1 = "xref";
		String propertyName2 = "controlled";
		String propertyName3 = "controller";
	    String propertyNameLiteral1 = "displayName";
	    String propertyNameLiteral2 = "controlType";
	    
	    int publicationXrefNum = -1;
	    int templateReactionNum = -1;
	    int proteinNum = -1;
	    
   		List<Set> allPropertyList = new ArrayList<Set>();
	
   		if (Line.TemplateReactionRegulation.isIndirectRegulation)
   		{	
   			Set<Resource> checky2 = Biopax.checkSingleLiteral(Line.TemplateReactionRegulation.indirectRegulation, propertyNameLiteral1, thisClass, biopaxmodel);
   		    allPropertyList.add(checky2);
   		    
   		    if (Line.EffectMeaning.equals("ACTIVATION") || Line.EffectMeaning.equals("INHIBITION"))
   		    {
	   			Set<Resource> checky5 = Biopax.checkSingleLiteral(Line.EffectMeaning, propertyNameLiteral2, thisClass, biopaxmodel);
	   		    allPropertyList.add(checky5);
   		    }
   		    Line.Protein.isSourceProtein = true;
   		    proteinNum = addNewProtein(biopaxmodel);
   	    	String testy4 = (individualClass3+"_"+String.valueOf(proteinNum));  
   	   		Set<Resource> checky4 = Biopax.checkSingleLinked(testy4, propertyName3, thisClass, biopaxmodel);
   		    allPropertyList.add(checky4);
   		    
   		    Line.TemplateReaction.isTranscription = Line.TemplateReactionRegulation.isIndirectRegulation;
   		    Line.TemplateReaction.isTargetProtein = true;
   		    templateReactionNum = addNewTemplateReaction(biopaxmodel);
   		    String testy5 = (individualClass2+"_"+String.valueOf(templateReactionNum));
   		    Set<Resource> checky5 = Biopax.checkSingleLinked(testy5, propertyName2, thisClass, biopaxmodel);
   		    allPropertyList.add(checky5);
   		}
   		else if (Line.TemplateReactionRegulation.isDirectTranscription)
   		{	
   			Set<Resource> checky2 = Biopax.checkSingleLiteral(Line.TemplateReactionRegulation.directTranscription, propertyNameLiteral1, thisClass, biopaxmodel);
   		    allPropertyList.add(checky2);
   		    
   		    if (Line.EffectMeaning.equals("ACTIVATION") || Line.EffectMeaning.equals("INHIBITION"))
   		    {
	   			Set<Resource> checky5 = Biopax.checkSingleLiteral(Line.EffectMeaning, propertyNameLiteral2, thisClass, biopaxmodel);
	   		    allPropertyList.add(checky5);
   		    }
   		    Line.Protein.isSourceProtein = true;
   		    proteinNum = addNewProtein(biopaxmodel);
   	    	String testy4 = (individualClass3+"_"+String.valueOf(proteinNum));  
   	   		Set<Resource> checky4 = Biopax.checkSingleLinked(testy4, propertyName3, thisClass, biopaxmodel);
   		    allPropertyList.add(checky4);
   		    
   		    Line.TemplateReaction.isTranscription = Line.TemplateReactionRegulation.isDirectTranscription;
   		    Line.TemplateReaction.isTargetProtein = true;
   		    templateReactionNum = addNewTemplateReaction(biopaxmodel);
   		    String testy5 = (individualClass2+"_"+String.valueOf(templateReactionNum));
   		    Set<Resource> checky5 = Biopax.checkSingleLinked(testy5, propertyName2, thisClass, biopaxmodel);
   		    allPropertyList.add(checky5);
   		}
		List<Set> allPropertyList2 = new ArrayList<Set>();
   		if (!Line.TemplateReactionRegulation.hasNoPublicationXref)
   		{
			publicationXrefNum = addNewPublicationXref(biopaxmodel);
			String testy = (individualClass1+"_"+publicationXrefNum);  
	   		Set<Resource> checky = Biopax.checkSingleLinked(testy, propertyName1, thisClass, biopaxmodel);
		    allPropertyList2.add(checky);
   		}
	
   		
		Resource result = allPropertyListGetIntersection(allPropertyList,biopaxmodel);
   		int num = convertResourceToInt(result, thisClass);

   		
   		Resource result2 = allPropertyListGetIntersection(allPropertyList2,biopaxmodel);
   		int num2 = convertResourceToInt(result2, thisClass);
   		
   		if (num == -1)
   		{
   			
   			num = Biopax.createBiopaxIndividual(thisClass,biopaxmodel);
   	   		String individualName = thisClass + "_" + num;

			String linkedresourceName1 = individualClass1 + "_" + publicationXrefNum;
			String linkedresourceName2 = individualClass2 + "_" + templateReactionNum;
			String linkedresourceName3 = individualClass3 + "_" + proteinNum;

			if (Line.TemplateReactionRegulation.isIndirectRegulation)
			{
				Biopax.addBiopaxProperty(Line.TemplateReactionRegulation.indirectRegulation,propertyNameLiteral1,individualName,biopaxmodel);	
	   		    if (Line.EffectMeaning.equals("ACTIVATION") || Line.EffectMeaning.equals("INHIBITION"))
	   		    {
	   				Biopax.addBiopaxProperty(Line.EffectMeaning,propertyNameLiteral2,individualName,biopaxmodel);
	   		    }
	   	   		if (!Line.TemplateReactionRegulation.hasNoPublicationXref)
	   	   		{
	   	   			Biopax.addBiopaxLinkedProperty(propertyName1, linkedresourceName1, individualName, biopaxmodel);
	   	   		}
				Biopax.addBiopaxLinkedProperty(propertyName2, linkedresourceName2, individualName, biopaxmodel);
				Biopax.addBiopaxLinkedProperty(propertyName3, linkedresourceName3, individualName, biopaxmodel);
			}
			else if (Line.TemplateReactionRegulation.isDirectTranscription)
			{
				{
					Biopax.addBiopaxProperty(Line.TemplateReactionRegulation.directTranscription,propertyNameLiteral1,individualName,biopaxmodel);	
		   		    if (Line.EffectMeaning.equals("ACTIVATION") || Line.EffectMeaning.equals("INHIBITION"))
		   		    {
		   				Biopax.addBiopaxProperty(Line.EffectMeaning,propertyNameLiteral2,individualName,biopaxmodel);
		   		    }
		   	   		if (!Line.TemplateReactionRegulation.hasNoPublicationXref)
		   	   		{
		   	   			Biopax.addBiopaxLinkedProperty(propertyName1, linkedresourceName1, individualName, biopaxmodel);
		   	   		}
		   	   		Biopax.addBiopaxLinkedProperty(propertyName2, linkedresourceName2, individualName, biopaxmodel);
					Biopax.addBiopaxLinkedProperty(propertyName3, linkedresourceName3, individualName, biopaxmodel);
				}
			}
   		}
   		else if (num2 == -1 && !Line.TemplateReactionRegulation.hasNoPublicationXref)
   		{
   	  		String individualName2 = thisClass + "_" + num;
   			String linkedresourceName = individualClass1 + "_" + publicationXrefNum;
			Biopax.addBiopaxLinkedProperty(propertyName1, linkedresourceName, individualName2, biopaxmodel);
   		}
   		Line.TemplateReactionRegulation.resetVarsToFalse();
   		return num;
	}

	private static int addNewTemplateReaction(OntModel biopaxmodel) {
   		String thisClass = "TemplateReaction";
		String InteractionVocabulary = "InteractionVocabulary";
		String Rna = "Rna";
		String Dna = "Dna";
		String Protein = "Protein";
	
		String propertyName1 = "interactionType";
		String propertyName2 = "template";
		String propertyName3 = "product";
	    String propertyNameLiteral1 = "displayName";
	    
	    int interactionVocabularyNum = -1;
	    int RNANum = -1;
	    int DNANum = -1;
	    int proteinNum = -1;
	    
   		List<Set> allPropertyList = new ArrayList<Set>();
	
   		if (Line.TemplateReaction.isTranscription)
   		{	
   			Set<Resource> checky2 = Biopax.checkSingleLiteral(Line.TemplateReaction.transcription, propertyNameLiteral1, thisClass, biopaxmodel);
   		    allPropertyList.add(checky2);
   		    
   		    Line.InteractionVocabulary.isTranscription = true;
   			interactionVocabularyNum = addNewInteractionVocabulary(biopaxmodel);
   	    	String testy = (InteractionVocabulary+"_"+String.valueOf(interactionVocabularyNum));  
   			Set<Resource> checky = Biopax.checkSingleLinked(testy, propertyName1, thisClass, biopaxmodel);
		    allPropertyList.add(checky);
   		    
   		    Line.Protein.isTargetProtein = true;
   		    Line.Protein.isTranscription = true;
   		    proteinNum = addNewProtein(biopaxmodel);
   	    	String testy4 = (Protein+"_"+String.valueOf(proteinNum));  
   	   		Set<Resource> checky4 = Biopax.checkSingleLinked(testy4, propertyName3, thisClass, biopaxmodel);
   		    allPropertyList.add(checky4);
   		    
 
   		    Line.DNA.isTarget = true;
   		    Line.DNA.isTranscription = true;
   		    DNANum = addNewDNA(biopaxmodel);
   		    String testy5 = (Dna+"_"+String.valueOf(DNANum));
   		    Set<Resource> checky5 = Biopax.checkSingleLinked(testy5, propertyName2, thisClass, biopaxmodel);
   		    allPropertyList.add(checky5);
   		}
   		
		Resource result = allPropertyListGetIntersection(allPropertyList,biopaxmodel);
   		int num = convertResourceToInt(result, thisClass);
   		
   		if (num == -1)
   		{
   			num = Biopax.createBiopaxIndividual(thisClass,biopaxmodel);
   	   		String individualName = thisClass + "_" + num;
			String linkedresourceName1 = InteractionVocabulary + "_" + interactionVocabularyNum;
			String linkedresourceName2 = Dna + "_" + DNANum;
			String linkedresourceName3 = Protein + "_" + proteinNum;

			Biopax.addBiopaxProperty(Line.TemplateReaction.transcription,propertyNameLiteral1,individualName,biopaxmodel);	
			Biopax.addBiopaxLinkedProperty(propertyName1, linkedresourceName1, individualName, biopaxmodel);
			Biopax.addBiopaxLinkedProperty(propertyName2, linkedresourceName2, individualName, biopaxmodel);
			Biopax.addBiopaxLinkedProperty(propertyName3, linkedresourceName3, individualName, biopaxmodel);
   		}
   		Line.TemplateReaction.resetVarsToFalse();
   		return num;
	}

	private static int addNewDNA(OntModel biopaxmodel) {
			String thisClass = "Dna";
			
			String literal = null;
			
			String propertyNameLiteral1 = "displayName";

	   		List<Set> allPropertyList = new ArrayList<Set>();
			
			if (Line.DNA.isTarget && Line.DNA.isTranscription)
			{
				literal = "DNA_of_gene_ " + Line.TargetName;
				Set<Resource> checky5 = Biopax.checkSingleLiteral(literal, propertyNameLiteral1, thisClass, biopaxmodel);
			    allPropertyList.add(checky5);
			}

			Resource result = allPropertyListGetIntersection(allPropertyList,biopaxmodel);
	   		int num = convertResourceToInt(result, thisClass);
	   		
	   		if (num == -1)
	   		{
	   			if (Line.DNA.isTarget && Line.DNA.isTranscription)
	   			{
		   			num = Biopax.createBiopaxIndividual(thisClass,biopaxmodel);
		   	   		String individualName = thisClass + "_" + num;
		
					Biopax.addBiopaxProperty(literal,propertyNameLiteral1,individualName,biopaxmodel);
	   			}
	   		}
	   		return num;
		
	}

	private static int addNewRNA(OntModel biopaxmodel) {
		String thisClass = "Rna";
		
		String literal = null;
		
		String propertyNameLiteral1 = "displayName";

   		List<Set> allPropertyList = new ArrayList<Set>();
		
		if (Line.RNA.isTarget)
		{
			literal = "RNA_of_ " + Line.TargetName;
			Set<Resource> checky5 = Biopax.checkSingleLiteral(literal, propertyNameLiteral1, thisClass, biopaxmodel);
		    allPropertyList.add(checky5);
		}

		Resource result = allPropertyListGetIntersection(allPropertyList,biopaxmodel);
   		int num = convertResourceToInt(result, thisClass);
   		
   		if (num == -1)
   		{
   			if (Line.RNA.isTarget)
   			{
	   			num = Biopax.createBiopaxIndividual(thisClass,biopaxmodel);
	   	   		String individualName = thisClass + "_" + num;
	
				Biopax.addBiopaxProperty(literal,propertyNameLiteral1,individualName,biopaxmodel);
   			}
   		}
   		return num;
	}

	private static int addNewComplexAssembly(OntModel biopaxmodel) {
		String thisClass = "ComplexAssembly";
		String individualClass1 = "PublicationXref";
		String individualClass2 = "Protein";
		String individualClass3 = "Complex";
		
		String propertyName1 = "xref";
		String propertyName2 = "left";
		String propertyName3 = "right";
		
		String propertyNameLiteral1 = "displayName";
		
		int publicationXrefNum = -1;
		int proteinNum = -1;
		int proteinNum2 = -1;
		int complexNum = -1;
		
   		List<Set> allPropertyList = new ArrayList<Set>();
		
		if (Line.ComplexAssembly.isBinding)
		{			
   		    Line.Protein.isSourceProtein = true;
   		    proteinNum = addNewProtein(biopaxmodel);
   	    	String testy2 = (individualClass2+"_"+String.valueOf(proteinNum));  
   	   		Set<Resource> checky2 = Biopax.checkSingleLinked(testy2, propertyName2, thisClass, biopaxmodel);
   		    allPropertyList.add(checky2);
   		    
   		    Line.Protein.isTargetProtein = true;
   		    proteinNum2 = addNewProtein(biopaxmodel);
   	    	String testy3 = (individualClass2+"_"+String.valueOf(proteinNum2));  
   	   		Set<Resource> checky3 = Biopax.checkSingleLinked(testy3, propertyName2, thisClass, biopaxmodel);
   		    allPropertyList.add(checky3);
   		    
   		    Line.Complex.isBinding = Line.ComplexAssembly.isBinding;
   		    complexNum = addNewComplex(biopaxmodel);
   		    String testy4 = (individualClass3+"_"+complexNum);
   		    Set<Resource> checky4 = Biopax.checkSingleLinked(testy4, propertyName3, thisClass, biopaxmodel);
   		    
   			Set<Resource> checky5 = Biopax.checkSingleLiteral(Line.ComplexAssembly.binding, propertyNameLiteral1, thisClass, biopaxmodel);
   		    allPropertyList.add(checky5);	
		}
		List<Set> allPropertyList2 = new ArrayList<Set>();  		
		if (!Line.ComplexAssembly.hasNoPublicationXref)
		{

		publicationXrefNum = addNewPublicationXref(biopaxmodel);
		String testy = (individualClass1+"_"+publicationXrefNum);  
   		Set<Resource> checky = Biopax.checkSingleLinked(testy, propertyName1, thisClass, biopaxmodel);
	    allPropertyList2.add(checky);
		}
	
   		
		Resource result = allPropertyListGetIntersection(allPropertyList,biopaxmodel);
   		int num = convertResourceToInt(result, thisClass);

   		
   		Resource result2 = allPropertyListGetIntersection(allPropertyList2,biopaxmodel);
   		int num2 = convertResourceToInt(result2, thisClass);
 
   		
   		if (num == -1)
   		{
   			num = Biopax.createBiopaxIndividual(thisClass,biopaxmodel);
   	   		String individualName = thisClass + "_" + num;
			String linkedresourceName = individualClass1 + "_" + publicationXrefNum;
			String linkedresourceName2 = individualClass2 + "_" + proteinNum;
			String linkedresourceName3 = individualClass2 + "_" + proteinNum2;
			String linkedresourceName4 = individualClass3 + "_" + complexNum;

			Biopax.addBiopaxProperty(Line.ComplexAssembly.binding,propertyNameLiteral1,individualName,biopaxmodel);
			if (!Line.ComplexAssembly.hasNoPublicationXref)
			{
				Biopax.addBiopaxLinkedProperty(propertyName1, linkedresourceName, individualName, biopaxmodel);
			}
			Biopax.addBiopaxLinkedProperty(propertyName2, linkedresourceName2, individualName, biopaxmodel);
			Biopax.addBiopaxLinkedProperty(propertyName2, linkedresourceName3, individualName, biopaxmodel);
			Biopax.addBiopaxLinkedProperty(propertyName3, linkedresourceName4, individualName, biopaxmodel);
   		}
   		else if (num2 == -1 && !Line.ComplexAssembly.hasNoPublicationXref)
   		{
   	  		String individualName2 = thisClass + "_" + num;
   			String linkedresourceName = individualClass1 + "_" + publicationXrefNum;
			Biopax.addBiopaxLinkedProperty(propertyName1, linkedresourceName, individualName2, biopaxmodel);
   		}
   		Line.ComplexAssembly.resetVarsToFalse();
   		return num;
	}

	private static int addNewComplex(OntModel biopaxmodel) {
		String thisClass = "Complex";
		String individualClass1 = "Protein";
		
		String propertyName1 = "component";
		
		String propertyNameLiteral1 = "displayName";
		
		int proteinNum = -1;
		int proteinNum2 = -1;
		
   		List<Set> allPropertyList = new ArrayList<Set>();
		
		if (Line.Complex.isBinding)
		{			
   		    Line.Protein.isSourceProtein = true;
   		    proteinNum = addNewProtein(biopaxmodel);
   	    	String testy2 = (individualClass1+"_"+proteinNum);  
   	   		Set<Resource> checky2 = Biopax.checkSingleLinked(testy2, propertyName1, thisClass, biopaxmodel);
   		    allPropertyList.add(checky2);
   		    
   		    Line.Protein.isTargetProtein = true;
   		    proteinNum2 = addNewProtein(biopaxmodel);
   	    	String testy3 = (individualClass1+"_"+proteinNum2);  
   	   		Set<Resource> checky3 = Biopax.checkSingleLinked(testy3, propertyName1, thisClass, biopaxmodel);
   		    allPropertyList.add(checky3);
   		    
   		    Line.Complex.complexName = Line.SourceName + "_" + Line.TargetName + "_" + "complex";
   			Set<Resource> checky5 = Biopax.checkSingleLiteral(Line.Complex.complexName, propertyNameLiteral1, thisClass, biopaxmodel);
   		    allPropertyList.add(checky5);	
		}
	
   		
		Resource result = allPropertyListGetIntersection(allPropertyList,biopaxmodel);
   		int num = convertResourceToInt(result, thisClass);
 
   		
   		if (num == -1)
   		{
   			num = Biopax.createBiopaxIndividual(thisClass,biopaxmodel);
   	  		String individualName = thisClass + "_" + num;
			String linkedresourceName = individualClass1 + "_" + proteinNum;
			String linkedresourceName2 = individualClass1 + "_" + proteinNum2;

			Biopax.addBiopaxProperty(Line.Complex.complexName,propertyNameLiteral1,individualName,biopaxmodel);	
			Biopax.addBiopaxLinkedProperty(propertyName1, linkedresourceName, individualName, biopaxmodel);
			Biopax.addBiopaxLinkedProperty(propertyName1, linkedresourceName2, individualName, biopaxmodel);
   		}
   		Line.Complex.resetVarsToFalse();
   		return num;
	}

	private static int addNewProtein(OntModel biopaxmodel) {

   		String individualClass = "Protein";
   		int num = -1;
		String individualClass2 = "ProteinReference";
		String individualClass3 = "CovalentBindingFeature";
		String individualClass4 = "CellularLocationVocabulary";
		
		
		Line.ProteinReference.isSourceProtein = (Line.Protein.isSourceProtein);
		Line.ProteinReference.isTargetProtein = Line.Protein.isTargetProtein;
		if (Line.ProteinReference.isTargetProtein)
		{
			if (Line.TargetHumanAccession.equals(""))
			{
				Line.ProteinReference.hasNoXref = true;
			}
		}
		else if (Line.ProteinReference.isSourceProtein)
		{
			if (Line.SourceHumanAccession.equals(""))
			{
				Line.ProteinReference.hasNoXref = true;
			}
		}
		Line.ProteinReference.isPIP2 = Line.Protein.isPIP2;
		Line.ProteinReference.isIP3 = Line.Protein.isIP3;
		Line.ProteinReference.isDAG = Line.Protein.isDAG;
		Line.ProteinReference.isTranscriptionProduct = Line.Protein.isTranscription;
		int proteinReferenceNum = addNewProteinReference(biopaxmodel);
    	String testy = (individualClass2+"_"+String.valueOf(proteinReferenceNum));  
   		List<Set> allPropertyList = new ArrayList<Set>();
   		Set<Resource> checky = Biopax.checkSingleLinked(testy, "entityReference", "Protein", biopaxmodel);
	    allPropertyList.add(checky);
   		
        int cellularLocationVocabularyNum = -1;
   		if (Line.Protein.isSourceProtein)
   		{	
   			Set<Resource> checky2 = Biopax.checkSingleLiteral(Line.SourceType, "comment", "Protein", biopaxmodel);
   		    allPropertyList.add(checky2);
   		    if (!(Line.SourceLocation.equals("")))
   		    {
	   		    Line.CellularLocationVocabulary.isSourceProtein = true;
	   			cellularLocationVocabularyNum = addNewCellularLocationVocabulary(biopaxmodel);
	   	    	String testy4 = (individualClass4+"_"+String.valueOf(cellularLocationVocabularyNum));  
	   	   		Set<Resource> checky4 = Biopax.checkSingleLinked(testy4, "cellularLocation", "Protein", biopaxmodel);
	   		    allPropertyList.add(checky4);
   		    }
   		}
   		else if (Line.Protein.isTargetProtein)
   		{	
   			Set<Resource> checky2 = Biopax.checkSingleLiteral(Line.TargetType, "comment", "Protein", biopaxmodel);
   		    allPropertyList.add(checky2);
   		    if (!(Line.TargetLocation.equals("")))
   		    {
	   		    Line.CellularLocationVocabulary.isTargetProtein = true;
	   			cellularLocationVocabularyNum = addNewCellularLocationVocabulary(biopaxmodel);
	   	    	String testy4 = (individualClass4+"_"+String.valueOf(cellularLocationVocabularyNum));  
	   	   		Set<Resource> checky4 = Biopax.checkSingleLinked(testy4, "cellularLocation", "Protein", biopaxmodel);
	   		    allPropertyList.add(checky4);
   		    }
   		}
   		
        int covalentBindingFeatureNum = -1;
   		if (Line.Protein.isPhosphorylated == true)
   		{
   			Line.CovalentBindingFeature.isPhosphorylatedTarget = true;
   			covalentBindingFeatureNum = addNewCovalentBindingFeature(biopaxmodel);
   	    	String testy3 = (individualClass3+"_"+String.valueOf(covalentBindingFeatureNum));  
   	   		Set<Resource> checky3 = Biopax.checkSingleLinked(testy3, "feature", "Protein", biopaxmodel);
   		    allPropertyList.add(checky3);
   		}
   		else if (Line.Protein.isUbiquitinated == true)
   		{
   			Line.CovalentBindingFeature.isUbiquitinatedTarget = true;
   			covalentBindingFeatureNum = addNewCovalentBindingFeature(biopaxmodel);
   	    	String testy3 = (individualClass3+"_"+String.valueOf(covalentBindingFeatureNum));  
   	   		Set<Resource> checky3 = Biopax.checkSingleLinked(testy3, "feature", "Protein", biopaxmodel);
   		    allPropertyList.add(checky3);
   		}
   		else if (Line.Protein.hasGTP)
   		{
   			Line.CovalentBindingFeature.hasGTP = true;
   			covalentBindingFeatureNum = addNewCovalentBindingFeature(biopaxmodel);
   	    	String testy3 = (individualClass3+"_"+String.valueOf(covalentBindingFeatureNum));  
   	   		Set<Resource> checky3 = Biopax.checkSingleLinked(testy3, "feature", "Protein", biopaxmodel);
   		    allPropertyList.add(checky3);
   		}
   		else if (Line.Protein.hasGDP)
   		{
   			Line.CovalentBindingFeature.hasGDP = true;
   			covalentBindingFeatureNum = addNewCovalentBindingFeature(biopaxmodel);
   	    	String testy3 = (individualClass3+"_"+String.valueOf(covalentBindingFeatureNum));  
   	   		Set<Resource> checky3 = Biopax.checkSingleLinked(testy3, "feature", "Protein", biopaxmodel);
   		    allPropertyList.add(checky3);
   		}
   		else if (Line.Protein.isSumolated == true)
   		{
   			Line.CovalentBindingFeature.isSumolatedTarget = true;
   			covalentBindingFeatureNum = addNewCovalentBindingFeature(biopaxmodel);
   	    	String testy3 = (individualClass3+"_"+String.valueOf(covalentBindingFeatureNum));  
   	   		Set<Resource> checky3 = Biopax.checkSingleLinked(testy3, "feature", "Protein", biopaxmodel);
   		    allPropertyList.add(checky3);
   		}
   		else if (Line.Protein.isProProtein == true)
   		{
   			Line.CovalentBindingFeature.isProProtein = true;
   			covalentBindingFeatureNum = addNewCovalentBindingFeature(biopaxmodel);
   	    	String testy3 = (individualClass3+"_"+String.valueOf(covalentBindingFeatureNum));  
   	   		Set<Resource> checky3 = Biopax.checkSingleLinked(testy3, "feature", "Protein", biopaxmodel);
   		    allPropertyList.add(checky3);
   		}
	   	
   		Set<Resource> checkySub = new HashSet<Resource>();
   		if (!(Line.Protein.hasGDP || Line.Protein.hasGTP || Line.Protein.isPhosphorylated || Line.Protein.isSumolated || Line.Protein.isUbiquitinated || Line.Protein.isProProtein))
   		{
   			checkySub = Biopax.checkAllLinked("feature", "Protein", biopaxmodel);
   		}
   		Resource result = allPropertyListGetIntersectionThenSubtraction(allPropertyList,checkySub,biopaxmodel);
   		num = convertResourceToInt(result, individualClass);
   		if (num==-1 || Line.Protein.forceNew)
   		{
   			num = Biopax.createBiopaxIndividual(individualClass,biopaxmodel);
   		
	   		String individualName = individualClass + "_" + String.valueOf(num);
	   		
	   		
	   		individualName = individualClass + "_" + num;
			String propertyName = "comment";
			String propertyName4 = "cellularLocation";
	   		individualName = individualClass + "_" + num;
			String linkedresourceName4 = individualClass4 + "_" + cellularLocationVocabularyNum;
			if (Line.Protein.isSourceProtein == true)
			{
				Biopax.addBiopaxProperty(Line.SourceType,"comment",individualName,biopaxmodel);	
	   		    if (!(Line.SourceLocation.equals("")))
	   		    {
	   		    	Biopax.addBiopaxLinkedProperty(propertyName4, linkedresourceName4, individualName, biopaxmodel);
	   		    }
			}
			else if (Line.Protein.isTargetProtein == true)
			{
				Biopax.addBiopaxProperty(Line.TargetType,"comment",individualName,biopaxmodel);
	   		    if (!(Line.TargetLocation.equals("")))
	   		    {
	   		    	Biopax.addBiopaxLinkedProperty(propertyName4, linkedresourceName4, individualName, biopaxmodel);
	   		    }
			}
	   		propertyName = "entityReference";
	   		individualName = individualClass + "_" + num;
			String linkedresourceName = individualClass2 + "_" + proteinReferenceNum;
			Biopax.addBiopaxLinkedProperty(propertyName, linkedresourceName, individualName, biopaxmodel);
			
	   		if (Line.Protein.isPhosphorylated || Line.Protein.isUbiquitinated || Line.Protein.hasGDP || Line.Protein.hasGTP || Line.Protein.isSumolated || Line.Protein.isProProtein)
	   		{
		   		propertyName = "feature";
		   		individualName = individualClass + "_" + num;
				linkedresourceName = individualClass3 + "_" + covalentBindingFeatureNum;
				Biopax.addBiopaxLinkedProperty(propertyName, linkedresourceName, individualName, biopaxmodel);
	   		}
			
   		}
   		
   		Line.Protein.resetVarsToFalse();
		return num;
		
	}
	
	private static Resource allPropertyListGetIntersectionThenSubtraction(
			List<Set> allPropertyList, Set<Resource> checkySub,
			OntModel biopaxmodel) 
	{
	    ListIterator<Set> iter = allPropertyList.listIterator();
	    Set<Resource> r = new HashSet<Resource>();
	    if (iter.hasNext())
	      {r=iter.next();}
   		while (iter.hasNext()) {r.retainAll(iter.next());}
   		
   		r.removeAll(checkySub); // subtracts out the Resources you don't want

         Resource s = null; // model.createResource();

         if (!(r.isEmpty()))
         {
        	Iterator<Resource> getItem = r.iterator();
   		    s=getItem.next();
   		 }
	     return s;         
	
	}

	private static int addNewCatalysis(OntModel biopaxmodel) {

   		String individualClass = "Catalysis";
   		int num = -1;
   		int num2 = -1;
		String individualClass2 = "PublicationXref";
		String individualClass3 = "BiochemicalReaction";
		String individualClass4 = "Protein";
		String individualClass5 = "Degradation";
		
		Set<Resource> checky = null;
		
		int publicationXrefNum = -1;
   		List<Set> allPropertyList = new ArrayList<Set>();
		
		if (!Line.Catalysis.hasNoPublicationXref)
		{
			publicationXrefNum = addNewPublicationXref(biopaxmodel);
	    	String testy = (individualClass2+"_"+String.valueOf(publicationXrefNum));  
	   		checky = Biopax.checkSingleLinked(testy, "xref", "Catalysis", biopaxmodel);
		    allPropertyList.add(checky);
		}
   		
        int proteinNum = -1;
        int biochemicalReactionNum = -1;
        int degradationNum = -1;
   		if (Line.Catalysis.isKinasePhosphorylation)
   		{	
   			Set<Resource> checky2 = Biopax.checkSingleLiteral(Line.Catalysis.kinasePhosphorylation, "displayName", "Catalysis", biopaxmodel);
   		    allPropertyList.add(checky2);
   		    
   		    Line.Protein.isSourceProtein = true;
   		    proteinNum = addNewProtein(biopaxmodel);
   	    	String testy4 = (individualClass4+"_"+String.valueOf(proteinNum));  
   	   		Set<Resource> checky4 = Biopax.checkSingleLinked(testy4, "controller", "Catalysis", biopaxmodel);
   		    allPropertyList.add(checky4);
   		    
   		    Line.BiochemicalReaction.isPhosphorylation = Line.Catalysis.isKinasePhosphorylation;
   		    Line.BiochemicalReaction.isTargetProtein = true;
   		    biochemicalReactionNum = addNewBiochemicalReaction(biopaxmodel);
   		    String testy5 = (individualClass3+"_"+String.valueOf(biochemicalReactionNum));
   		    Set<Resource> checky5 = Biopax.checkSingleLinked(testy5, "controlled", "Catalysis", biopaxmodel);
   		    allPropertyList.add(checky5);
   		}
   		if (Line.Catalysis.isDePhosphorylation)
   		{	
   			Set<Resource> checky2 = Biopax.checkSingleLiteral(Line.Catalysis.dephosphorylation, "displayName", "Catalysis", biopaxmodel);
   		    allPropertyList.add(checky2);
   		    
   		    Line.Protein.isSourceProtein = true;
   		    proteinNum = addNewProtein(biopaxmodel);
   	    	String testy4 = (individualClass4+"_"+String.valueOf(proteinNum));  
   	   		Set<Resource> checky4 = Biopax.checkSingleLinked(testy4, "controller", "Catalysis", biopaxmodel);
   		    allPropertyList.add(checky4);
   		    
   		    Line.BiochemicalReaction.isDephosphorylation = Line.Catalysis.isDePhosphorylation;
   		    Line.BiochemicalReaction.isTargetProtein = true;
   		    biochemicalReactionNum = addNewBiochemicalReaction(biopaxmodel);
   		    String testy5 = (individualClass3+"_"+String.valueOf(biochemicalReactionNum));
   		    Set<Resource> checky5 = Biopax.checkSingleLinked(testy5, "controlled", "Catalysis", biopaxmodel);
   		    allPropertyList.add(checky5);
   		}
   		else if (Line.Catalysis.isUbiquitination)
   		{	
   			Set<Resource> checky2 = Biopax.checkSingleLiteral(Line.Catalysis.ubiquitination, "displayName", "Catalysis", biopaxmodel);
   		    allPropertyList.add(checky2);
   		    
   		    Line.Protein.isSourceProtein = true;
   		    proteinNum = addNewProtein(biopaxmodel);
   	    	String testy4 = (individualClass4+"_"+String.valueOf(proteinNum));  
   	   		Set<Resource> checky4 = Biopax.checkSingleLinked(testy4, "controller", "Catalysis", biopaxmodel);
   		    allPropertyList.add(checky4);
   		    
   		    Line.BiochemicalReaction.isUbiquitination = Line.Catalysis.isUbiquitination;
   		    Line.BiochemicalReaction.isTargetProtein = true;
   		    biochemicalReactionNum = addNewBiochemicalReaction(biopaxmodel);
   		    String testy5 = (individualClass3+"_"+String.valueOf(biochemicalReactionNum));
   		    Set<Resource> checky5 = Biopax.checkSingleLinked(testy5, "controlled", "Catalysis", biopaxmodel);
   		    allPropertyList.add(checky5);
   		}
   		else if (Line.Catalysis.isDeUbiquitination)
   		{	
   			Set<Resource> checky2 = Biopax.checkSingleLiteral(Line.Catalysis.DeUbiquitination, "displayName", "Catalysis", biopaxmodel);
   		    allPropertyList.add(checky2);
   		    
   		    Line.Protein.isSourceProtein = true;
   		    proteinNum = addNewProtein(biopaxmodel);
   	    	String testy4 = (individualClass4+"_"+String.valueOf(proteinNum));  
   	   		Set<Resource> checky4 = Biopax.checkSingleLinked(testy4, "controller", "Catalysis", biopaxmodel);
   		    allPropertyList.add(checky4);
   		    
   		    Line.BiochemicalReaction.isDeUbiquitination = Line.Catalysis.isDeUbiquitination;
   		    Line.BiochemicalReaction.isTargetProtein = true;
   		    biochemicalReactionNum = addNewBiochemicalReaction(biopaxmodel);
   		    String testy5 = (individualClass3+"_"+String.valueOf(biochemicalReactionNum));
   		    Set<Resource> checky5 = Biopax.checkSingleLinked(testy5, "controlled", "Catalysis", biopaxmodel);
   		    allPropertyList.add(checky5);
   		}
   		else if (Line.Catalysis.isGEF)
   		{	
   			Set<Resource> checky2 = Biopax.checkSingleLiteral(Line.Catalysis.gef, "displayName", "Catalysis", biopaxmodel);
   		    allPropertyList.add(checky2);
   		    
   		    Line.Protein.isSourceProtein = true;
   		    proteinNum = addNewProtein(biopaxmodel);
   	    	String testy4 = (individualClass4+"_"+String.valueOf(proteinNum));  
   	   		Set<Resource> checky4 = Biopax.checkSingleLinked(testy4, "controller", "Catalysis", biopaxmodel);
   		    allPropertyList.add(checky4);
   		    
   		    Line.BiochemicalReaction.isUbiquitination = Line.Catalysis.isUbiquitination;
   		    Line.BiochemicalReaction.isTargetProtein = true;
   		    Line.BiochemicalReaction.isGEF = Line.Catalysis.isGEF;
   		    biochemicalReactionNum = addNewBiochemicalReaction(biopaxmodel);
   		    String testy5 = (individualClass3+"_"+String.valueOf(biochemicalReactionNum));
   		    Set<Resource> checky5 = Biopax.checkSingleLinked(testy5, "controlled", "Catalysis", biopaxmodel);
   		    allPropertyList.add(checky5);
   		}
   		else if (Line.Catalysis.isGAP)
   		{	
   			Set<Resource> checky2 = Biopax.checkSingleLiteral(Line.Catalysis.gap, "displayName", "Catalysis", biopaxmodel);
   		    allPropertyList.add(checky2);
   		    
   		    Line.Protein.isSourceProtein = true;
   		    proteinNum = addNewProtein(biopaxmodel);
   	    	String testy4 = (individualClass4+"_"+String.valueOf(proteinNum));  
   	   		Set<Resource> checky4 = Biopax.checkSingleLinked(testy4, "controller", "Catalysis", biopaxmodel);
   		    allPropertyList.add(checky4);
   		    
   		    Line.BiochemicalReaction.isUbiquitination = Line.Catalysis.isUbiquitination;
   		    Line.BiochemicalReaction.isTargetProtein = true;
   		    Line.BiochemicalReaction.isGAP = Line.Catalysis.isGAP;
   		    biochemicalReactionNum = addNewBiochemicalReaction(biopaxmodel);
   		    String testy5 = (individualClass3+"_"+String.valueOf(biochemicalReactionNum));
   		    Set<Resource> checky5 = Biopax.checkSingleLinked(testy5, "controlled", "Catalysis", biopaxmodel);
   		    allPropertyList.add(checky5);
   		}
   		else if (Line.Catalysis.isPLC)
   		{
   			Set<Resource> checky2 = Biopax.checkSingleLiteral(Line.Catalysis.plc, "displayName", "Catalysis", biopaxmodel);
   		    allPropertyList.add(checky2);
   		    
   		    Line.Protein.isSourceProtein = true;
   		    proteinNum = addNewProtein(biopaxmodel);
   	    	String testy4 = (individualClass4+"_"+String.valueOf(proteinNum));  
   	   		Set<Resource> checky4 = Biopax.checkSingleLinked(testy4, "controller", "Catalysis", biopaxmodel);
   		    allPropertyList.add(checky4);
   		    
   		    Line.BiochemicalReaction.isPlcTarget = Line.Catalysis.isPLC;
   		  //  Line.BiochemicalReaction.isTargetProtein = true;
   		    biochemicalReactionNum = addNewBiochemicalReaction(biopaxmodel);
   		    String testy5 = (individualClass3+"_"+String.valueOf(biochemicalReactionNum));
   		    Set<Resource> checky5 = Biopax.checkSingleLinked(testy5, "controlled", "Catalysis", biopaxmodel);
   		    allPropertyList.add(checky5);
   		}
   		if (Line.Catalysis.isCysProtease)
   		{	
   			Set<Resource> checky2 = Biopax.checkSingleLiteral(Line.Catalysis.CysProtease, "displayName", "Catalysis", biopaxmodel);
   		    allPropertyList.add(checky2);
   		    
   		    Line.Protein.isSourceProtein = true;
   		    proteinNum = addNewProtein(biopaxmodel);
   	    	String testy4 = (individualClass4+"_"+String.valueOf(proteinNum));  
   	   		Set<Resource> checky4 = Biopax.checkSingleLinked(testy4, "controller", "Catalysis", biopaxmodel);
   		    allPropertyList.add(checky4);
   		    
   		    Line.Degradation.isCleavage = Line.Catalysis.isCysProtease;
   		    Line.Degradation.isTargetProtein = true;
   		    degradationNum = addNewDegradation(biopaxmodel);
   		    String testy5 = (individualClass5+"_"+String.valueOf(degradationNum));
   		    Set<Resource> checky5 = Biopax.checkSingleLinked(testy5, "controlled", "Catalysis", biopaxmodel);
   		    allPropertyList.add(checky5);
   		}
   		if (Line.Catalysis.isProtease)
   		{	
   			Set<Resource> checky2 = Biopax.checkSingleLiteral(Line.Catalysis.Protease, "displayName", "Catalysis", biopaxmodel);
   		    allPropertyList.add(checky2);
   		    
   		    Line.Protein.isSourceProtein = true;
   		    proteinNum = addNewProtein(biopaxmodel);
   	    	String testy4 = (individualClass4+"_"+String.valueOf(proteinNum));  
   	   		Set<Resource> checky4 = Biopax.checkSingleLinked(testy4, "controller", "Catalysis", biopaxmodel);
   		    allPropertyList.add(checky4);
   		    
   		    Line.Degradation.isCleavage = Line.Catalysis.isProtease;
   		    Line.Degradation.isTargetProtein = true;
   		    degradationNum = addNewDegradation(biopaxmodel);
   		    String testy5 = (individualClass5+"_"+String.valueOf(degradationNum));
   		    Set<Resource> checky5 = Biopax.checkSingleLinked(testy5, "controlled", "Catalysis", biopaxmodel);
   		    allPropertyList.add(checky5);
   		}
   		if (Line.Catalysis.isProCleavage)
   		{	
   			Set<Resource> checky2 = Biopax.checkSingleLiteral(Line.Catalysis.ProCleavage, "displayName", "Catalysis", biopaxmodel);
   		    allPropertyList.add(checky2);
   		    
   		    Line.Protein.isSourceProtein = true;
   		    proteinNum = addNewProtein(biopaxmodel);
   	    	String testy4 = (individualClass4+"_"+String.valueOf(proteinNum));  
   	   		Set<Resource> checky4 = Biopax.checkSingleLinked(testy4, "controller", "Catalysis", biopaxmodel);
   		    allPropertyList.add(checky4);
   		    
   		    Line.BiochemicalReaction.isProCleavage = Line.Catalysis.isProCleavage;
   		    Line.BiochemicalReaction.isTargetProtein = true;
   		    biochemicalReactionNum = addNewBiochemicalReaction(biopaxmodel);
   		    String testy5 = (individualClass3+"_"+String.valueOf(biochemicalReactionNum));
   		    Set<Resource> checky5 = Biopax.checkSingleLinked(testy5, "controlled", "Catalysis", biopaxmodel);
   		    allPropertyList.add(checky5);
   		}
   		else if (Line.Catalysis.isSumolation)
   		{	
   			Set<Resource> checky2 = Biopax.checkSingleLiteral(Line.Catalysis.Sumolation, "displayName", "Catalysis", biopaxmodel);
   		    allPropertyList.add(checky2);
   		    
   		    Line.Protein.isSourceProtein = true;
   		    proteinNum = addNewProtein(biopaxmodel);
   	    	String testy4 = (individualClass4+"_"+String.valueOf(proteinNum));  
   	   		Set<Resource> checky4 = Biopax.checkSingleLinked(testy4, "controller", "Catalysis", biopaxmodel);
   		    allPropertyList.add(checky4);
   		    
   		    Line.BiochemicalReaction.isSumolation = Line.Catalysis.isSumolation;
   		    Line.BiochemicalReaction.isTargetProtein = true;
   		    biochemicalReactionNum = addNewBiochemicalReaction(biopaxmodel);
   		    String testy5 = (individualClass3+"_"+String.valueOf(biochemicalReactionNum));
   		    Set<Resource> checky5 = Biopax.checkSingleLinked(testy5, "controlled", "Catalysis", biopaxmodel);
   		    allPropertyList.add(checky5);
   		}
   		Resource result = allPropertyListGetIntersection(allPropertyList,biopaxmodel);
   		num = convertResourceToInt(result, individualClass);
   		
   		allPropertyList.remove(checky); // now test all without publicationXref
   		Resource result2 = allPropertyListGetIntersection(allPropertyList,biopaxmodel);
   		num2 = convertResourceToInt(result2, individualClass);
   		if (num2!=-1 && num==-1 && !Line.Catalysis.hasNoPublicationXref) // if the only thing different about this catalysis is the publicationXrefNum, add the publicationXrefNum as a new one
   		{
	   		String individualName = individualClass + "_" + String.valueOf(num2);
	   		String linkedresourceName = individualClass2 + "_" + publicationXrefNum;
			String propertyName = "xref";
			Biopax.addBiopaxLinkedProperty(propertyName, linkedresourceName, individualName, biopaxmodel);
			num = num2;
   		}
   		else if (num==-1)    // else if there is something else new, make it a new catalysis
   		{
   			num = Biopax.createBiopaxIndividual(individualClass,biopaxmodel);
   		
	   		String individualName = individualClass + "_" + String.valueOf(num);
		    if (Line.Catalysis.isKinasePhosphorylation == true)
			{
				String propertyName = "xref";
				String propertyName2 = "controlled";
				String propertyName3 = "controller";

				String linkedresourceName = individualClass2 + "_" + publicationXrefNum;
				String linkedresourceName2 = individualClass3 + "_" + biochemicalReactionNum;
				String linkedresourceName3 = individualClass4 + "_" + proteinNum;
	
				Biopax.addBiopaxProperty(Line.Catalysis.kinasePhosphorylation,"displayName",individualName,biopaxmodel);
				if (!Line.Catalysis.hasNoPublicationXref)
				{
					Biopax.addBiopaxLinkedProperty(propertyName, linkedresourceName, individualName, biopaxmodel);
				}
				Biopax.addBiopaxLinkedProperty(propertyName2, linkedresourceName2, individualName, biopaxmodel);
				Biopax.addBiopaxLinkedProperty(propertyName3, linkedresourceName3, individualName, biopaxmodel);
				
			}
		    else if (Line.Catalysis.isUbiquitination)
		    {
				String propertyName = "xref";
				String propertyName2 = "controlled";
				String propertyName3 = "controller";

				String linkedresourceName = individualClass2 + "_" + publicationXrefNum;
				String linkedresourceName2 = individualClass3 + "_" + biochemicalReactionNum;
				String linkedresourceName3 = individualClass4 + "_" + proteinNum;
	
				Biopax.addBiopaxProperty(Line.Catalysis.ubiquitination,"displayName",individualName,biopaxmodel);	
				if (!Line.Catalysis.hasNoPublicationXref)
				{
					Biopax.addBiopaxLinkedProperty(propertyName, linkedresourceName, individualName, biopaxmodel);
				}
				Biopax.addBiopaxLinkedProperty(propertyName2, linkedresourceName2, individualName, biopaxmodel);
				Biopax.addBiopaxLinkedProperty(propertyName3, linkedresourceName3, individualName, biopaxmodel);
		    }
		    else if (Line.Catalysis.isDeUbiquitination)
		    {
				String propertyName = "xref";
				String propertyName2 = "controlled";
				String propertyName3 = "controller";

				String linkedresourceName = individualClass2 + "_" + publicationXrefNum;
				String linkedresourceName2 = individualClass3 + "_" + biochemicalReactionNum;
				String linkedresourceName3 = individualClass4 + "_" + proteinNum;
	
				Biopax.addBiopaxProperty(Line.Catalysis.DeUbiquitination,"displayName",individualName,biopaxmodel);	
				if (!Line.Catalysis.hasNoPublicationXref)
				{
					Biopax.addBiopaxLinkedProperty(propertyName, linkedresourceName, individualName, biopaxmodel);
				}				Biopax.addBiopaxLinkedProperty(propertyName2, linkedresourceName2, individualName, biopaxmodel);
				Biopax.addBiopaxLinkedProperty(propertyName3, linkedresourceName3, individualName, biopaxmodel);
		    }
		    else if (Line.Catalysis.isGEF)
		    {
			    String propertyName = "xref";
				String propertyName2 = "controlled";
				String propertyName3 = "controller";
	
				String linkedresourceName = individualClass2 + "_" + publicationXrefNum;
				String linkedresourceName2 = individualClass3 + "_" + biochemicalReactionNum;
				String linkedresourceName3 = individualClass4 + "_" + proteinNum;
	
				Biopax.addBiopaxProperty(Line.Catalysis.gef,"displayName",individualName,biopaxmodel);	
				if (!Line.Catalysis.hasNoPublicationXref)
				{
					Biopax.addBiopaxLinkedProperty(propertyName, linkedresourceName, individualName, biopaxmodel);
				}				Biopax.addBiopaxLinkedProperty(propertyName2, linkedresourceName2, individualName, biopaxmodel);
				Biopax.addBiopaxLinkedProperty(propertyName3, linkedresourceName3, individualName, biopaxmodel);
		    }
		    else if (Line.Catalysis.isGAP)
		    {
			    String propertyName = "xref";
				String propertyName2 = "controlled";
				String propertyName3 = "controller";
	
				String linkedresourceName = individualClass2 + "_" + publicationXrefNum;
				String linkedresourceName2 = individualClass3 + "_" + biochemicalReactionNum;
				String linkedresourceName3 = individualClass4 + "_" + proteinNum;
	
				Biopax.addBiopaxProperty(Line.Catalysis.gap,"displayName",individualName,biopaxmodel);	
				if (!Line.Catalysis.hasNoPublicationXref)
				{
					Biopax.addBiopaxLinkedProperty(propertyName, linkedresourceName, individualName, biopaxmodel);
				}				Biopax.addBiopaxLinkedProperty(propertyName2, linkedresourceName2, individualName, biopaxmodel);
				Biopax.addBiopaxLinkedProperty(propertyName3, linkedresourceName3, individualName, biopaxmodel);
		    }
		    else if (Line.Catalysis.isDePhosphorylation == true)
			{
				String propertyName = "xref";
				String propertyName2 = "controlled";
				String propertyName3 = "controller";

				String linkedresourceName = individualClass2 + "_" + publicationXrefNum;
				String linkedresourceName2 = individualClass3 + "_" + biochemicalReactionNum;
				String linkedresourceName3 = individualClass4 + "_" + proteinNum;
	
				Biopax.addBiopaxProperty(Line.Catalysis.dephosphorylation,"displayName",individualName,biopaxmodel);	
				if (!Line.Catalysis.hasNoPublicationXref)
				{
					Biopax.addBiopaxLinkedProperty(propertyName, linkedresourceName, individualName, biopaxmodel);
				}				Biopax.addBiopaxLinkedProperty(propertyName2, linkedresourceName2, individualName, biopaxmodel);
				Biopax.addBiopaxLinkedProperty(propertyName3, linkedresourceName3, individualName, biopaxmodel);
				
			}
		    else if (Line.Catalysis.isPLC)
		    {
				String propertyName = "xref";
				String propertyName2 = "controlled";
				String propertyName3 = "controller";

				String linkedresourceName = individualClass2 + "_" + publicationXrefNum;
				String linkedresourceName2 = individualClass3 + "_" + biochemicalReactionNum;
				String linkedresourceName3 = individualClass4 + "_" + proteinNum;
	
				Biopax.addBiopaxProperty(Line.Catalysis.plc,"displayName",individualName,biopaxmodel);	
				if (!Line.Catalysis.hasNoPublicationXref)
				{
					Biopax.addBiopaxLinkedProperty(propertyName, linkedresourceName, individualName, biopaxmodel);
				}				Biopax.addBiopaxLinkedProperty(propertyName2, linkedresourceName2, individualName, biopaxmodel);
				Biopax.addBiopaxLinkedProperty(propertyName3, linkedresourceName3, individualName, biopaxmodel);
		    }
		    else if (Line.Catalysis.isCysProtease)
		    {
				String propertyName = "xref";
				String propertyName2 = "controlled";
				String propertyName3 = "controller";

				String linkedresourceName = individualClass2 + "_" + publicationXrefNum;
				String linkedresourceName2 = individualClass5 + "_" + degradationNum;
				String linkedresourceName3 = individualClass4 + "_" + proteinNum;
	
				Biopax.addBiopaxProperty(Line.Catalysis.CysProtease,"displayName",individualName,biopaxmodel);	
				if (!Line.Catalysis.hasNoPublicationXref)
				{
					Biopax.addBiopaxLinkedProperty(propertyName, linkedresourceName, individualName, biopaxmodel);
				}				Biopax.addBiopaxLinkedProperty(propertyName2, linkedresourceName2, individualName, biopaxmodel);
				Biopax.addBiopaxLinkedProperty(propertyName3, linkedresourceName3, individualName, biopaxmodel);
		    }
		    else if (Line.Catalysis.isProtease)
		    {
				String propertyName = "xref";
				String propertyName2 = "controlled";
				String propertyName3 = "controller";

				String linkedresourceName = individualClass2 + "_" + publicationXrefNum;
				String linkedresourceName2 = individualClass5 + "_" + degradationNum;
				String linkedresourceName3 = individualClass4 + "_" + proteinNum;
	
				Biopax.addBiopaxProperty(Line.Catalysis.Protease,"displayName",individualName,biopaxmodel);	
				if (!Line.Catalysis.hasNoPublicationXref)
				{
					Biopax.addBiopaxLinkedProperty(propertyName, linkedresourceName, individualName, biopaxmodel);
				}				Biopax.addBiopaxLinkedProperty(propertyName2, linkedresourceName2, individualName, biopaxmodel);
				Biopax.addBiopaxLinkedProperty(propertyName3, linkedresourceName3, individualName, biopaxmodel);
		    }
		    else if (Line.Catalysis.isProCleavage == true)
			{
				String propertyName = "xref";
				String propertyName2 = "controlled";
				String propertyName3 = "controller";

				String linkedresourceName = individualClass2 + "_" + publicationXrefNum;
				String linkedresourceName2 = individualClass3 + "_" + biochemicalReactionNum;
				String linkedresourceName3 = individualClass4 + "_" + proteinNum;
	
				Biopax.addBiopaxProperty(Line.Catalysis.ProCleavage,"displayName",individualName,biopaxmodel);	
				if (!Line.Catalysis.hasNoPublicationXref)
				{
					Biopax.addBiopaxLinkedProperty(propertyName, linkedresourceName, individualName, biopaxmodel);
				}				Biopax.addBiopaxLinkedProperty(propertyName2, linkedresourceName2, individualName, biopaxmodel);
				Biopax.addBiopaxLinkedProperty(propertyName3, linkedresourceName3, individualName, biopaxmodel);
				
			}
		    else if (Line.Catalysis.isSumolation)
		    {
				String propertyName = "xref";
				String propertyName2 = "controlled";
				String propertyName3 = "controller";

				String linkedresourceName = individualClass2 + "_" + publicationXrefNum;
				String linkedresourceName2 = individualClass3 + "_" + biochemicalReactionNum;
				String linkedresourceName3 = individualClass4 + "_" + proteinNum;
	
				Biopax.addBiopaxProperty(Line.Catalysis.Sumolation,"displayName",individualName,biopaxmodel);	
				if (!Line.Catalysis.hasNoPublicationXref)
				{
					Biopax.addBiopaxLinkedProperty(propertyName, linkedresourceName, individualName, biopaxmodel);
				}				Biopax.addBiopaxLinkedProperty(propertyName2, linkedresourceName2, individualName, biopaxmodel);
				Biopax.addBiopaxLinkedProperty(propertyName3, linkedresourceName3, individualName, biopaxmodel);
		    }
   		}
   		
   		Line.Catalysis.resetVarsToFalse();
		return num;
		
	}
	
	private static int addNewDegradation(OntModel biopaxmodel) {
		String thisClass = "Degradation";
		String individualClass1 = "InteractionVocabulary";
		String individualClass2 = "Protein";
		
		String propertyName1 = "xref";
		String propertyName2 = "left";
		String propertyName3 = "right";
		
		
		int proteinNum = -1;
		int interactionVocabularyNum = -1;
		
   		List<Set> allPropertyList = new ArrayList<Set>();
		
		if (Line.Degradation.isCleavage)
		{			
   		    Line.Protein.isTargetProtein = true;
   		    proteinNum = addNewProtein(biopaxmodel);
   	    	String testy2 = (individualClass2+"_"+String.valueOf(proteinNum));  
   	   		Set<Resource> checky2 = Biopax.checkSingleLinked(testy2, propertyName2, thisClass, biopaxmodel);
   		    allPropertyList.add(checky2);
   		    
   		    Line.InteractionVocabulary.isProteinCleavage = true;
   		    interactionVocabularyNum = addNewInteractionVocabulary(biopaxmodel);
   	    	String testy3 = (individualClass1+"_"+String.valueOf(interactionVocabularyNum));  
   	   		Set<Resource> checky3 = Biopax.checkSingleLinked(testy3, propertyName1, thisClass, biopaxmodel);
   		    allPropertyList.add(checky3);

		}
   		
		Resource result = allPropertyListGetIntersection(allPropertyList,biopaxmodel);
   		int num = convertResourceToInt(result, thisClass);

 
   		
   		if (num == -1)
   		{
   			num = Biopax.createBiopaxIndividual(thisClass,biopaxmodel);
   	   		String individualName = thisClass + "_" + num;
			String linkedresourceName = individualClass1 + "_" + interactionVocabularyNum;
			String linkedresourceName2 = individualClass2 + "_" + proteinNum;
	
			Biopax.addBiopaxLinkedProperty(propertyName1, linkedresourceName, individualName, biopaxmodel);
			Biopax.addBiopaxLinkedProperty(propertyName2, linkedresourceName2, individualName, biopaxmodel);
   		}
   		Line.Degradation.resetVarsToFalse();
   		return num;
	}

	private static int addNewBiochemicalReaction(OntModel biopaxmodel) {

   		String individualClass = "BiochemicalReaction";
   		int num = -1;
		String individualClass2 = "InteractionVocabulary";
		String individualClass3 = "Protein";
		String individualClass4 = "Protein";
		int interactionVocabularyNum = -1;

   		List<Set> allPropertyList = new ArrayList<Set>();
   		
   		if (Line.BiochemicalReaction.isPhosphorylation)
   		{
   		    Line.InteractionVocabulary.isPhosphorylation = Line.BiochemicalReaction.isPhosphorylation;
   			interactionVocabularyNum = addNewInteractionVocabulary(biopaxmodel);
   	    	String testy = (individualClass2+"_"+String.valueOf(interactionVocabularyNum));  
   			Set<Resource> checky = Biopax.checkSingleLinked(testy, "interactionType", "BiochemicalReaction", biopaxmodel);
		    allPropertyList.add(checky);
   		}
   		else if (Line.BiochemicalReaction.isDephosphorylation)
   		{
   		    Line.InteractionVocabulary.isDephosphorylation = Line.BiochemicalReaction.isDephosphorylation;
   			interactionVocabularyNum = addNewInteractionVocabulary(biopaxmodel);
   	    	String testy = (individualClass2+"_"+String.valueOf(interactionVocabularyNum));  
   			Set<Resource> checky = Biopax.checkSingleLinked(testy, "interactionType", "BiochemicalReaction", biopaxmodel);
		    allPropertyList.add(checky);
   		}
   		else if (Line.BiochemicalReaction.isUbiquitination)
   		{
   		    Line.InteractionVocabulary.isUbiquitination = Line.BiochemicalReaction.isUbiquitination;
   			interactionVocabularyNum = addNewInteractionVocabulary(biopaxmodel);
   	    	String testy = (individualClass2+"_"+String.valueOf(interactionVocabularyNum));  
   			Set<Resource> checky = Biopax.checkSingleLinked(testy, "interactionType", "BiochemicalReaction", biopaxmodel);
		    allPropertyList.add(checky);
   		}
  		else if (Line.BiochemicalReaction.isDeUbiquitination)
   		{
   		    Line.InteractionVocabulary.isDeUbiquitination = Line.BiochemicalReaction.isDeUbiquitination;
   			interactionVocabularyNum = addNewInteractionVocabulary(biopaxmodel);
   	    	String testy = (individualClass2+"_"+String.valueOf(interactionVocabularyNum));  
   			Set<Resource> checky = Biopax.checkSingleLinked(testy, "interactionType", "BiochemicalReaction", biopaxmodel);
		    allPropertyList.add(checky);
   		}
   		else if (Line.BiochemicalReaction.isGEF)
   		{
   		    Line.InteractionVocabulary.isGEF = Line.BiochemicalReaction.isGEF;
   			interactionVocabularyNum = addNewInteractionVocabulary(biopaxmodel);
   	    	String testy = (individualClass2+"_"+String.valueOf(interactionVocabularyNum));  
   			Set<Resource> checky = Biopax.checkSingleLinked(testy, "interactionType", "BiochemicalReaction", biopaxmodel);
		    allPropertyList.add(checky);
   		}
   		else if (Line.BiochemicalReaction.isGAP)
   		{
   		    Line.InteractionVocabulary.isGAP = Line.BiochemicalReaction.isGAP;
   			interactionVocabularyNum = addNewInteractionVocabulary(biopaxmodel);
   	    	String testy = (individualClass2+"_"+String.valueOf(interactionVocabularyNum));  
   			Set<Resource> checky = Biopax.checkSingleLinked(testy, "interactionType", "BiochemicalReaction", biopaxmodel);
		    allPropertyList.add(checky);
   		}
   		else if (Line.BiochemicalReaction.isPlcTarget)
   		{
   		    Line.InteractionVocabulary.isPlcTarget = Line.BiochemicalReaction.isPlcTarget;
   			interactionVocabularyNum = addNewInteractionVocabulary(biopaxmodel);
   	    	String testy = (individualClass2+"_"+String.valueOf(interactionVocabularyNum));  
   			Set<Resource> checky = Biopax.checkSingleLinked(testy, "interactionType", "BiochemicalReaction", biopaxmodel);
		    allPropertyList.add(checky);
   		}
   		else if (Line.BiochemicalReaction.isProCleavage)
   		{
   		    Line.InteractionVocabulary.isProCleavage = Line.BiochemicalReaction.isProCleavage;
   			interactionVocabularyNum = addNewInteractionVocabulary(biopaxmodel);
   	    	String testy = (individualClass2+"_"+String.valueOf(interactionVocabularyNum));  
   			Set<Resource> checky = Biopax.checkSingleLinked(testy, "interactionType", "BiochemicalReaction", biopaxmodel);
		    allPropertyList.add(checky);
   		}
   		else if (Line.BiochemicalReaction.isSumolation)
   		{
   		    Line.InteractionVocabulary.isSumolation = Line.BiochemicalReaction.isSumolation;
   			interactionVocabularyNum = addNewInteractionVocabulary(biopaxmodel);
   	    	String testy = (individualClass2+"_"+String.valueOf(interactionVocabularyNum));  
   			Set<Resource> checky = Biopax.checkSingleLinked(testy, "interactionType", "BiochemicalReaction", biopaxmodel);
		    allPropertyList.add(checky);
   		}
        int proteinNum1 = -1;
        int proteinNum2 = -1;
        int proteinNum3 = -1;

            Line.Protein.isSourceProtein = Line.BiochemicalReaction.isSourceProtein;
   		    Line.Protein.isTargetProtein = Line.BiochemicalReaction.isTargetProtein;
   		    Line.Protein.isPhosphorylated = Line.BiochemicalReaction.isDephosphorylation; // this one has phosphorus in this case
   		    Line.Protein.hasGDP = Line.BiochemicalReaction.isGEF;
   		    Line.Protein.hasGTP = Line.BiochemicalReaction.isGAP;
   		    Line.Protein.isPIP2 = Line.BiochemicalReaction.isPlcTarget;
   		    Line.Protein.isUbiquitinated = Line.BiochemicalReaction.isDeUbiquitination;
   		    Line.Protein.isProProtein = Line.BiochemicalReaction.isProCleavage;
   			proteinNum1 = addNewProtein(biopaxmodel);
   	    	String testy4 = (individualClass4+"_"+String.valueOf(proteinNum1));  
   	   		Set<Resource> checky4 = Biopax.checkSingleLinked(testy4, "left", "BiochemicalReaction", biopaxmodel);
   		    allPropertyList.add(checky4);
   		    
            Line.Protein.isSourceProtein = Line.BiochemicalReaction.isSourceProtein;
   		    Line.Protein.isTargetProtein = Line.BiochemicalReaction.isTargetProtein;
   		    Line.Protein.isPhosphorylated = Line.BiochemicalReaction.isPhosphorylation;
   		    Line.Protein.isUbiquitinated = Line.BiochemicalReaction.isUbiquitination;
   		    Line.Protein.isSumolated = Line.BiochemicalReaction.isSumolation;
   		    Line.Protein.isIP3 = Line.BiochemicalReaction.isPlcTarget;
   		    Line.Protein.hasGTP = Line.BiochemicalReaction.isGEF;
   		    Line.Protein.hasGDP = Line.BiochemicalReaction.isGAP;
 //  		    Line.Protein.forceNew = Line.BiochemicalReaction.isDephosphorylation;
   		    proteinNum2 = addNewProtein(biopaxmodel);
   	    	String testy3 = (individualClass4+"_"+String.valueOf(proteinNum2));  
   	   		Set<Resource> checky3 = Biopax.checkSingleLinked(testy3, "right", "BiochemicalReaction", biopaxmodel);
   		    allPropertyList.add(checky3);
   		    
   		    if (Line.BiochemicalReaction.isPlcTarget)
   		    {
   		    	Line.Protein.isDAG = true;
   		    	proteinNum3 = addNewProtein(biopaxmodel);
   		    	String testy5 = (individualClass4+"_"+proteinNum2);
   		    	Set<Resource> checky5 = Biopax.checkSingleLinked(testy5, "right", "BiochemicalReaction", biopaxmodel);
   		    	allPropertyList.add(checky5);
   		    }
//   	}
   		
   		
   		Resource result = allPropertyListGetIntersection(allPropertyList,biopaxmodel);
   		num = convertResourceToInt(result, individualClass);
   		if (num==-1)
   		{
   			num = Biopax.createBiopaxIndividual(individualClass,biopaxmodel);
   		
	   		String individualName = individualClass + "_" + String.valueOf(num);
	   		
	   		
			String propertyName = "interactionType";
			String propertyName2 = "left";
			String propertyName3 = "right";
	   		individualName = individualClass + "_" + num;
			String linkedresourceName = individualClass2 + "_" + interactionVocabularyNum;
			String linkedresourceName2 = individualClass3 + "_" + proteinNum1;
			String linkedresourceName3 = individualClass4 + "_" + proteinNum2;
			String linkedresourceName4 = individualClass4 + "_" + proteinNum3;

			if (Line.BiochemicalReaction.isPhosphorylation || Line.BiochemicalReaction.isUbiquitination || Line.BiochemicalReaction.isGEF || Line.BiochemicalReaction.isDephosphorylation || Line.BiochemicalReaction.isPlcTarget || Line.BiochemicalReaction.isGAP || Line.BiochemicalReaction.isDeUbiquitination || Line.BiochemicalReaction.isProCleavage || Line.BiochemicalReaction.isSumolation)	
			{
				Biopax.addBiopaxLinkedProperty(propertyName, linkedresourceName, individualName, biopaxmodel);
			Biopax.addBiopaxLinkedProperty(propertyName2, linkedresourceName2, individualName, biopaxmodel);
			

				Biopax.addBiopaxLinkedProperty(propertyName3, linkedresourceName3, individualName, biopaxmodel);
				if (Line.BiochemicalReaction.isPlcTarget)
				{
					Biopax.addBiopaxLinkedProperty(propertyName3, linkedresourceName4, individualName, biopaxmodel);
				}
			}
   		}
   		
   		Line.BiochemicalReaction.resetVarsToFalse();
		return num;
		
	}

	private static int addNewInteractionVocabulary(OntModel biopaxmodel) {
   		String individualClass = "InteractionVocabulary";
   		int num = -1;
		String individualClass2 = "UnificationXref";
		

        List<Set> allPropertyList = new ArrayList<Set>();
		int unificationXrefNum = -1;

	    
		if (Line.InteractionVocabulary.isPhosphorylation == true)
		{
			Set<Resource> checky2 = Biopax.checkSingleLiteral(Line.InteractionVocabulary.phosphorylationReaction, "term", "InteractionVocabulary", biopaxmodel);
			allPropertyList.add(checky2);
			Line.UnificationXref.isPhosphorylationReaction = Line.InteractionVocabulary.isPhosphorylation;
			unificationXrefNum = addNewUnificationXref(biopaxmodel);
	    	String testy = (individualClass2+"_"+String.valueOf(unificationXrefNum));  
	   		Set<Resource> checky = Biopax.checkSingleLinked(testy, "xref", "InteractionVocabulary", biopaxmodel);
		    allPropertyList.add(checky);
		}
		else if (Line.InteractionVocabulary.isUbiquitination)
		{
			Set<Resource> checky2 = Biopax.checkSingleLiteral(Line.InteractionVocabulary.ubiquitinationReaction, "term", "InteractionVocabulary", biopaxmodel);
			allPropertyList.add(checky2);
			Line.UnificationXref.isUbiquitinationReaction = Line.InteractionVocabulary.isUbiquitination;
			unificationXrefNum = addNewUnificationXref(biopaxmodel);
	    	String testy = (individualClass2+"_"+String.valueOf(unificationXrefNum));  
	   		Set<Resource> checky = Biopax.checkSingleLinked(testy, "xref", "InteractionVocabulary", biopaxmodel);
		    allPropertyList.add(checky);
		}
		else if (Line.InteractionVocabulary.isDeUbiquitination)
		{
			Set<Resource> checky2 = Biopax.checkSingleLiteral(Line.InteractionVocabulary.deUbiquitinationReaction, "term", "InteractionVocabulary", biopaxmodel);
			allPropertyList.add(checky2);
			Line.UnificationXref.isDeUbiquitinationReaction = Line.InteractionVocabulary.isDeUbiquitination;
			
			unificationXrefNum = addNewUnificationXref(biopaxmodel);
	    	String testy = (individualClass2+"_"+String.valueOf(unificationXrefNum));  
	   		Set<Resource> checky = Biopax.checkSingleLinked(testy, "xref", "InteractionVocabulary", biopaxmodel);
		    allPropertyList.add(checky);
		}
		else if (Line.InteractionVocabulary.isGEF)
		{
			Set<Resource> checky2 = Biopax.checkSingleLiteral(Line.InteractionVocabulary.gef, "term", "InteractionVocabulary", biopaxmodel);
			allPropertyList.add(checky2);
		}
		else if (Line.InteractionVocabulary.isGAP)
		{
			Set<Resource> checky2 = Biopax.checkSingleLiteral(Line.InteractionVocabulary.gap, "term", "InteractionVocabulary", biopaxmodel);
			allPropertyList.add(checky2);
		}
		else if (Line.InteractionVocabulary.isDephosphorylation)
		{
			Set<Resource> checky2 = Biopax.checkSingleLiteral(Line.InteractionVocabulary.dephosphorylationReaction, "term", "InteractionVocabulary", biopaxmodel);
			allPropertyList.add(checky2);
			Line.UnificationXref.isDephosphorylationReaction = Line.InteractionVocabulary.isDephosphorylation;
			unificationXrefNum = addNewUnificationXref(biopaxmodel);
	    	String testy = (individualClass2+"_"+String.valueOf(unificationXrefNum));  
	   		Set<Resource> checky = Biopax.checkSingleLinked(testy, "xref", "InteractionVocabulary", biopaxmodel);
		    allPropertyList.add(checky);
		}
		else if (Line.InteractionVocabulary.isPlcTarget)
		{
			Set<Resource> checky2 = Biopax.checkSingleLiteral(Line.InteractionVocabulary.plc, "term", "InteractionVocabulary", biopaxmodel);
			allPropertyList.add(checky2);
			Line.UnificationXref.isPLCCleavage = true;
			unificationXrefNum = addNewUnificationXref(biopaxmodel);
	    	String testy = (individualClass2+"_"+String.valueOf(unificationXrefNum));  
	   		Set<Resource> checky = Biopax.checkSingleLinked(testy, "xref", "InteractionVocabulary", biopaxmodel);
		    allPropertyList.add(checky);
		}
		else if (Line.InteractionVocabulary.isProteinCleavage)
		{
			Set<Resource> checky2 = Biopax.checkSingleLiteral(Line.InteractionVocabulary.proteinCleavage, "term", "InteractionVocabulary", biopaxmodel);
			allPropertyList.add(checky2);
			Line.UnificationXref.isProteinCleavage = true; // proProtein cleavage gets same xref as Protein cleavage
			unificationXrefNum = addNewUnificationXref(biopaxmodel);
	    	String testy = (individualClass2+"_"+String.valueOf(unificationXrefNum));  
	   		Set<Resource> checky = Biopax.checkSingleLinked(testy, "xref", "InteractionVocabulary", biopaxmodel);
		    allPropertyList.add(checky);
		}
		else if (Line.InteractionVocabulary.isProCleavage)
		{
			Set<Resource> checky2 = Biopax.checkSingleLiteral(Line.InteractionVocabulary.proProteinCleavage, "term", "InteractionVocabulary", biopaxmodel);
			allPropertyList.add(checky2);
			Line.UnificationXref.isProteinCleavage = true; // proProtein cleavage gets same xref as Protein cleavage
			unificationXrefNum = addNewUnificationXref(biopaxmodel);
	    	String testy = (individualClass2+"_"+String.valueOf(unificationXrefNum));  
	   		Set<Resource> checky = Biopax.checkSingleLinked(testy, "xref", "InteractionVocabulary", biopaxmodel);
		    allPropertyList.add(checky);
		}
		else if (Line.InteractionVocabulary.isSumolation)
		{
			Set<Resource> checky2 = Biopax.checkSingleLiteral(Line.InteractionVocabulary.sumolation, "term", "InteractionVocabulary", biopaxmodel);
			allPropertyList.add(checky2);
			Line.UnificationXref.isSumolation = Line.InteractionVocabulary.isSumolation;
			unificationXrefNum = addNewUnificationXref(biopaxmodel);
	    	String testy = (individualClass2+"_"+String.valueOf(unificationXrefNum));  
	   		Set<Resource> checky = Biopax.checkSingleLinked(testy, "xref", "InteractionVocabulary", biopaxmodel);
		    allPropertyList.add(checky);
		}
		else if (Line.InteractionVocabulary.isTranscription)
		{
			Set<Resource> checky2 = Biopax.checkSingleLiteral(Line.InteractionVocabulary.transcription, "term", "InteractionVocabulary", biopaxmodel);
			allPropertyList.add(checky2);
		}
   		Resource result = allPropertyListGetIntersection(allPropertyList,biopaxmodel);
   		num = convertResourceToInt(result, individualClass);
   		if (num==-1)
   		{
   			num = Biopax.createBiopaxIndividual(individualClass,biopaxmodel);
   		
	   		String individualName = individualClass + "_" + String.valueOf(num);
	   		
	   		
			String propertyName = "xref";

	   		individualName = individualClass + "_" + num;
			String linkedresourceName = individualClass2 + "_" + unificationXrefNum;


				if (Line.InteractionVocabulary.isPhosphorylation == true)
				{		
					Biopax.addBiopaxProperty(Line.InteractionVocabulary.phosphorylationReaction, "term", individualName, biopaxmodel);
					Biopax.addBiopaxLinkedProperty(propertyName, linkedresourceName, individualName, biopaxmodel);
				}
				if (Line.InteractionVocabulary.isUbiquitination == true)
				{		
					Biopax.addBiopaxProperty(Line.InteractionVocabulary.ubiquitinationReaction, "term", individualName, biopaxmodel);
					Biopax.addBiopaxLinkedProperty(propertyName, linkedresourceName, individualName, biopaxmodel);
				}
				if (Line.InteractionVocabulary.isDeUbiquitination)
				{
					Biopax.addBiopaxProperty(Line.InteractionVocabulary.deUbiquitinationReaction, "term", individualName, biopaxmodel);
					Biopax.addBiopaxLinkedProperty(propertyName, linkedresourceName, individualName, biopaxmodel);
				}
				if (Line.InteractionVocabulary.isGEF)
				{
					Biopax.addBiopaxProperty(Line.InteractionVocabulary.gef, "term", individualName, biopaxmodel);
	//				Biopax.addBiopaxLinkedProperty(propertyName, linkedresourceName, individualName, biopaxmodel);
				}
				if (Line.InteractionVocabulary.isGAP)
				{
					Biopax.addBiopaxProperty(Line.InteractionVocabulary.gap, "term", individualName, biopaxmodel);
	//				Biopax.addBiopaxLinkedProperty(propertyName, linkedresourceName, individualName, biopaxmodel);
				}
				if (Line.InteractionVocabulary.isDephosphorylation)
				{		
					Biopax.addBiopaxProperty(Line.InteractionVocabulary.dephosphorylationReaction, "term", individualName, biopaxmodel);
					Biopax.addBiopaxLinkedProperty(propertyName, linkedresourceName, individualName, biopaxmodel);
				}
				if (Line.InteractionVocabulary.isPlcTarget)
				{
					Biopax.addBiopaxProperty(Line.InteractionVocabulary.plc, "term", individualName, biopaxmodel);
					Biopax.addBiopaxLinkedProperty(propertyName, linkedresourceName, individualName, biopaxmodel);
				}
				if (Line.InteractionVocabulary.isProteinCleavage)
				{
					Biopax.addBiopaxProperty(Line.InteractionVocabulary.proteinCleavage, "term", individualName, biopaxmodel);
					Biopax.addBiopaxLinkedProperty(propertyName, linkedresourceName, individualName, biopaxmodel);
				}
				if (Line.InteractionVocabulary.isProCleavage)
				{
					Biopax.addBiopaxProperty(Line.InteractionVocabulary.proProteinCleavage, "term", individualName, biopaxmodel);
					Biopax.addBiopaxLinkedProperty(propertyName, linkedresourceName, individualName, biopaxmodel);
				}
				if (Line.InteractionVocabulary.isTranscription)
				{
					Biopax.addBiopaxProperty(Line.InteractionVocabulary.transcription, "term", individualName, biopaxmodel);
	//				Biopax.addBiopaxLinkedProperty(propertyName, linkedresourceName, individualName, biopaxmodel);
				}
				if (Line.InteractionVocabulary.isSumolation)
				{		
					Biopax.addBiopaxProperty(Line.InteractionVocabulary.sumolation, "term", individualName, biopaxmodel);
					Biopax.addBiopaxLinkedProperty(propertyName, linkedresourceName, individualName, biopaxmodel);
				}
   		}
   		
   		Line.InteractionVocabulary.resetVarsToFalse();
		return num;
		
	}
	

	private static int addNewCellularLocationVocabulary(OntModel biopaxmodel) {
		String individualClass = "CellularLocationVocabulary";
		String individualClass2 = "UnificationXref";

	    List<Set> allPropertyList = new ArrayList<Set>();
	    
	    int unificationXrefNum = -1;
		if ((Line.SourceLocation.equalsIgnoreCase("cytosol") 
				|| Line.SourceLocation.equalsIgnoreCase("vesicles") 
		        || Line.SourceLocation.equalsIgnoreCase("nucleus") || Line.SourceLocation.equalsIgnoreCase("nuclear") 
				|| Line.SourceLocation.equalsIgnoreCase("mitochondria")) && Line.CellularLocationVocabulary.isSourceProtein)
	    {       			
			if (Line.SourceLocation.equalsIgnoreCase("cytosol")) {Line.UnificationXref.isCytosol = true;}				
			else if (Line.SourceLocation.equalsIgnoreCase("vesicles")) {Line.UnificationXref.isVesicle = true;}
			else if (Line.SourceLocation.equalsIgnoreCase("nucleus") || Line.SourceLocation.equalsIgnoreCase("nuclear")) {Line.UnificationXref.isNucleus = true;}				
			else if (Line.SourceLocation.equalsIgnoreCase("mitochondria")) {Line.UnificationXref.isMitochondrion = true;}
		
			unificationXrefNum = addNewUnificationXref(biopaxmodel);
	   		
	  		String testy = (individualClass2+"_"+String.valueOf(unificationXrefNum));   		
	   		Set<Resource> checky = Biopax.checkSingleLinked(testy, "xref", "CellularLocationVocabulary", biopaxmodel);
	
		    allPropertyList.add(checky);
	    }
		else if ((Line.TargetLocation.equalsIgnoreCase("cytosol") 
				|| Line.TargetLocation.equalsIgnoreCase("vesicles") 
		        || Line.TargetLocation.equalsIgnoreCase("nucleus") || Line.TargetLocation.equalsIgnoreCase("nuclear") 
				|| Line.TargetLocation.equalsIgnoreCase("mitochondria")) && Line.CellularLocationVocabulary.isTargetProtein)
			{			
				if (Line.TargetLocation.equalsIgnoreCase("cytosol")) {Line.UnificationXref.isCytosol = true;}				
				else if (Line.TargetLocation.equalsIgnoreCase("vesicles")) {Line.UnificationXref.isVesicle = true;}
				else if (Line.TargetLocation.equalsIgnoreCase("nucleus") || Line.TargetLocation.equalsIgnoreCase("nuclear")) {Line.UnificationXref.isNucleus = true;}				
				else if (Line.TargetLocation.equalsIgnoreCase("mitochondria")) {Line.UnificationXref.isMitochondrion = true;}
			
			unificationXrefNum = addNewUnificationXref(biopaxmodel);
	   		
	  		String testy = (individualClass2+"_"+String.valueOf(unificationXrefNum));   		
	   		Set<Resource> checky = Biopax.checkSingleLinked(testy, "xref", "CellularLocationVocabulary", biopaxmodel);
	
		    allPropertyList.add(checky);
		}

   		
	    if (Line.CellularLocationVocabulary.isTargetProtein)
   		{
	    	Set<Resource> checky2 = Biopax.checkSingleLiteral(Line.TargetLocation, "term", "CellularLocationVocabulary", biopaxmodel);
	    	allPropertyList.add(checky2);
   		}
	    else if (Line.CellularLocationVocabulary.isSourceProtein)
   		{
	    	Set<Resource> checky2 = Biopax.checkSingleLiteral(Line.SourceLocation, "term", "CellularLocationVocabulary", biopaxmodel);
	    	allPropertyList.add(checky2);
   		}
      
        Resource result = allPropertyListGetIntersection(allPropertyList, biopaxmodel);
        int num = convertResourceToInt(result, individualClass);
   		
   		if (num == -1)
   		{
	   		num = Biopax.createBiopaxIndividual(individualClass,biopaxmodel);
	   		String individualName = individualClass + "_" + String.valueOf(num);		
	   		
			if ((Line.SourceLocation.equalsIgnoreCase("cytosol")
					|| Line.SourceLocation.equalsIgnoreCase("vesicles")
			        || Line.SourceLocation.equalsIgnoreCase("nucleus") || Line.SourceLocation.equalsIgnoreCase("nuclear")
					|| Line.SourceLocation.equalsIgnoreCase("mitochondria")) && Line.CellularLocationVocabulary.isSourceProtein)
			{
		   		String propertyName = "xref";
		   		individualName = individualClass + "_" + num;
				String linkedresourceName = individualClass2 + "_" + unificationXrefNum;
				Biopax.addBiopaxLinkedProperty(propertyName, linkedresourceName, individualName, biopaxmodel);
			}
			else if ((Line.TargetLocation.equalsIgnoreCase("cytosol")
					|| Line.TargetLocation.equalsIgnoreCase("vesicles")
			        || Line.TargetLocation.equalsIgnoreCase("nucleus") || Line.TargetLocation.equalsIgnoreCase("nuclear")
					|| Line.TargetLocation.equalsIgnoreCase("mitochondria")) && Line.CellularLocationVocabulary.isTargetProtein)
			{
		   		String propertyName = "xref";
		   		individualName = individualClass + "_" + num;
				String linkedresourceName = individualClass2 + "_" + unificationXrefNum;
				Biopax.addBiopaxLinkedProperty(propertyName, linkedresourceName, individualName, biopaxmodel);
			}
			
	   		individualName = individualClass + "_" + num;
			String propertyName2 = "term";
		    if (Line.CellularLocationVocabulary.isTargetProtein)
	   		{
		    	Biopax.addBiopaxProperty(Line.TargetLocation,propertyName2,individualName,biopaxmodel);
	   		}
		    else if (Line.CellularLocationVocabulary.isSourceProtein)
	   		{
		    	Biopax.addBiopaxProperty(Line.SourceLocation,propertyName2,individualName,biopaxmodel);
	   		}
   		}
   		Line.CellularLocationVocabulary.resetVarsToFalse();

		return num;
	}

	private static int addNewCovalentBindingFeature(OntModel biopaxmodel) {
		String individualClass = "CovalentBindingFeature";
		String individualClass2 = "SequenceModificationVocabulary";
		Line.SequenceModificationVocabulary.isPhosphorylatedTarget = Line.CovalentBindingFeature.isPhosphorylatedTarget;
		Line.SequenceModificationVocabulary.isUbiquitinatedTarget = Line.CovalentBindingFeature.isUbiquitinatedTarget;
		Line.SequenceModificationVocabulary.isGTP = Line.CovalentBindingFeature.hasGTP;
		Line.SequenceModificationVocabulary.isGDP = Line.CovalentBindingFeature.hasGDP;
		Line.SequenceModificationVocabulary.isSumolatedTarget = Line.CovalentBindingFeature.isSumolatedTarget;
		Line.SequenceModificationVocabulary.isProProtein = Line.CovalentBindingFeature.isProProtein;
   		int sequenceModificationVocabularyNum = addNewSequenceModificationVocabulary(biopaxmodel);
   		
   		String testy = (individualClass2+"_"+String.valueOf(sequenceModificationVocabularyNum));   		
   		Set<Resource> checky = Biopax.checkSingleLinked(testy, "modificationType", "CovalentBindingFeature", biopaxmodel);
	    List<Set> allPropertyList = new ArrayList<Set>();

        allPropertyList.add(checky);

        Resource result = allPropertyListGetIntersection(allPropertyList, biopaxmodel);
        int num = convertResourceToInt(result, individualClass);
   		
   		if (num == -1)
   		{
	   		num = Biopax.createBiopaxIndividual(individualClass,biopaxmodel);
	   		String individualName = individualClass + "_" + String.valueOf(num);		
	   		   		
	   		String propertyName = "modificationType";
	   		individualName = individualClass + "_" + num;
			String linkedresourceName = individualClass2 + "_" + sequenceModificationVocabularyNum;
			Biopax.addBiopaxLinkedProperty(propertyName, linkedresourceName, individualName, biopaxmodel);
			
	    }
   		Line.CovalentBindingFeature.resetVarsToFalse();
		return num;
	}

	private static int addNewSequenceModificationVocabulary(OntModel biopaxmodel) {
		String individualClass = "SequenceModificationVocabulary";
		String individualClass2 = "UnificationXref";
	    List<Set> allPropertyList = new ArrayList<Set>();

   		int unificationXrefNum = -1;

   		
   		if (Line.SequenceModificationVocabulary.isPhosphorylatedTarget==true)
   		{
   			Set<Resource> checky2 = Biopax.checkSingleLiteral(Line.SequenceModificationVocabulary.phosphorylatedResidue, "term", individualClass, biopaxmodel);  
   			allPropertyList.add(checky2);
   			Line.UnificationXref.isPhosphorylatedResidue = Line.SequenceModificationVocabulary.isPhosphorylatedTarget;
   			unificationXrefNum = addNewUnificationXref(biopaxmodel);
   	   		
   	   		String testy = (individualClass2+"_"+String.valueOf(unificationXrefNum));   		
   	   		Set<Resource> checky = Biopax.checkSingleLinked(testy, "xref", individualClass, biopaxmodel);

   		    allPropertyList.add(checky);
   		}
   		else if (Line.SequenceModificationVocabulary.isUbiquitinatedTarget==true)
   		{
   			Set<Resource> checky2 = Biopax.checkSingleLiteral(Line.SequenceModificationVocabulary.ubiquitinatedResidue, "term", individualClass, biopaxmodel);  
   			allPropertyList.add(checky2);
   			Line.UnificationXref.isUbiquitinatedResidue = Line.SequenceModificationVocabulary.isUbiquitinatedTarget;
   			unificationXrefNum = addNewUnificationXref(biopaxmodel);
   	   		
   	   		String testy = (individualClass2+"_"+String.valueOf(unificationXrefNum));   		
   	   		Set<Resource> checky = Biopax.checkSingleLinked(testy, "xref", individualClass, biopaxmodel);

   		    allPropertyList.add(checky);
   		}
   		else if (Line.SequenceModificationVocabulary.isSumolatedTarget==true)
   		{
   			Set<Resource> checky2 = Biopax.checkSingleLiteral(Line.SequenceModificationVocabulary.sumolatedResidue, "term", individualClass, biopaxmodel);  
   			allPropertyList.add(checky2);
   			Line.UnificationXref.isSumolatedResidue = Line.SequenceModificationVocabulary.isSumolatedTarget;
   			unificationXrefNum = addNewUnificationXref(biopaxmodel);
   	   		
   	   		String testy = (individualClass2+"_"+String.valueOf(unificationXrefNum));   		
   	   		Set<Resource> checky = Biopax.checkSingleLinked(testy, "xref", individualClass, biopaxmodel);

   		    allPropertyList.add(checky);
   		}
   		else if (Line.SequenceModificationVocabulary.isGTP==true)
   		{
   			Set<Resource> checky2 = Biopax.checkSingleLiteral(Line.SequenceModificationVocabulary.gtp, "term", individualClass, biopaxmodel);  
   			allPropertyList.add(checky2);
   		}
   		else if (Line.SequenceModificationVocabulary.isGDP==true)
   		{
   			Set<Resource> checky2 = Biopax.checkSingleLiteral(Line.SequenceModificationVocabulary.gdp, "term", individualClass, biopaxmodel);  
   			allPropertyList.add(checky2);
   		}
   		else if (Line.SequenceModificationVocabulary.isProProtein==true)
   		{
   			Set<Resource> checky2 = Biopax.checkSingleLiteral(Line.SequenceModificationVocabulary.proProtein, "term", individualClass, biopaxmodel);  
   			allPropertyList.add(checky2);
   		}
        Resource result = allPropertyListGetIntersection(allPropertyList, biopaxmodel);
        int num = convertResourceToInt(result, individualClass);
   		
   		if (num == -1)
   		{
	   		num = Biopax.createBiopaxIndividual(individualClass,biopaxmodel);
	   		String individualName = individualClass + "_" + String.valueOf(num);		
	   		   		

	   		if (Line.SequenceModificationVocabulary.isPhosphorylatedTarget==true)
	   		{
		   		String propertyName = "xref";
		  // 		individualName = individualClass + "_" + num;
				String linkedresourceName = individualClass2 + "_" + unificationXrefNum;
				Biopax.addBiopaxLinkedProperty(propertyName, linkedresourceName, individualName, biopaxmodel);
	   			individualName = individualClass + "_" + num;
				propertyName = "term";
				Biopax.addBiopaxProperty(Line.SequenceModificationVocabulary.phosphorylatedResidue,propertyName,individualName,biopaxmodel);
	   		}
	   		else if (Line.SequenceModificationVocabulary.isUbiquitinatedTarget==true)
	   		{
		   		String propertyName = "xref";
		   //		individualName = individualClass + "_" + num;
				String linkedresourceName = individualClass2 + "_" + unificationXrefNum;
				Biopax.addBiopaxLinkedProperty(propertyName, linkedresourceName, individualName, biopaxmodel);
	   			individualName = individualClass + "_" + num;
	   			propertyName = "term";
	   			Biopax.addBiopaxProperty(Line.SequenceModificationVocabulary.ubiquitinatedResidue, propertyName, individualName, biopaxmodel);
	   		}
	   		else if (Line.SequenceModificationVocabulary.isSumolatedTarget==true)
	   		{
		   		String propertyName = "xref";
		   //		individualName = individualClass + "_" + num;
				String linkedresourceName = individualClass2 + "_" + unificationXrefNum;
				Biopax.addBiopaxLinkedProperty(propertyName, linkedresourceName, individualName, biopaxmodel);
	   			individualName = individualClass + "_" + num;
	   			propertyName = "term";
	   			Biopax.addBiopaxProperty(Line.SequenceModificationVocabulary.sumolatedResidue, propertyName, individualName, biopaxmodel);
	   		}
	   		else if (Line.SequenceModificationVocabulary.isGDP)
	   		{
	   			String propertyName = "term";
	   			Biopax.addBiopaxProperty(Line.SequenceModificationVocabulary.gdp, propertyName, individualName, biopaxmodel);
	   		}
	   		else if (Line.SequenceModificationVocabulary.isGTP)
	   		{
	   			String propertyName = "term";
	   			Biopax.addBiopaxProperty(Line.SequenceModificationVocabulary.gtp, propertyName, individualName, biopaxmodel);
	   		}
	   		else if (Line.SequenceModificationVocabulary.isProProtein)
	   		{
	   			String propertyName = "term";
	   			Biopax.addBiopaxProperty(Line.SequenceModificationVocabulary.proProtein, propertyName, individualName, biopaxmodel);
	   		}
	    }
   		
   		Line.SequenceModificationVocabulary.resetVarsToFalse();

		return num;
	}

	private static int addNewProteinReference(OntModel biopaxmodel) {
   		String individualClass = "ProteinReference";
   		int num = -1;
   		
   		int unificationXrefNum = -1;
		String individualClass2 = "UnificationXref";
		Line.UnificationXref.isSourceProtein = Line.ProteinReference.isSourceProtein;
		Line.UnificationXref.isTargetProtein = Line.ProteinReference.isTargetProtein;
 
   		
   		
   		String individualClass3 = "BioSource";
   		int bioSourceNum = addNewBioSource(biopaxmodel);
   		
   		String literal = "";
   		
   		List<Set> allPropertyList = new ArrayList<Set>();
   	
   		
   		String testy2 = (individualClass3+"_"+String.valueOf(bioSourceNum));
   		Set<Resource> checky2 = Biopax.checkSingleLinked(testy2, "organism", "ProteinReference", biopaxmodel);
	    allPropertyList.add(checky2);
   		
   		if (Line.ProteinReference.isSourceProtein)
   		{
   			if (Line.ProteinReference.isTranscriptionProduct)
   			{
   				literal = "protein_of_gene_" + Line.SourceName;
   	   			Set<Resource> checky3 = Biopax.checkSingleLiteral(literal, "displayName", "ProteinReference", biopaxmodel);
   	   		    allPropertyList.add(checky3);
   			}
   			else
   			{
   				Set<Resource> checky3 = Biopax.checkSingleLiteral(Line.SourceName, "displayName", "ProteinReference", biopaxmodel);
   				allPropertyList.add(checky3);
   			}
   		    if (!Line.ProteinReference.hasNoXref)
   		    {
   		    	Line.UnificationXref.isSourceProtein = true;
	   	  		unificationXrefNum = addNewUnificationXref(biopaxmodel);
	   		    String testy = (individualClass2+"_"+String.valueOf(unificationXrefNum));   		
	   	   		Set<Resource> checky = Biopax.checkSingleLinked(testy, "xref", "ProteinReference", biopaxmodel);
	   		    allPropertyList.add(checky);
   		    }
   		}
   		else if (Line.ProteinReference.isTargetProtein)
   		{
   			if (Line.ProteinReference.isTranscriptionProduct)
   			{
   				literal = "protein_of_gene_" + Line.TargetName;
   	   			Set<Resource> checky3 = Biopax.checkSingleLiteral(literal, "displayName", "ProteinReference", biopaxmodel);
   	   		    allPropertyList.add(checky3);
   			}
   			else
   			{
   				Set<Resource> checky3 = Biopax.checkSingleLiteral(Line.TargetName, "displayName", "ProteinReference", biopaxmodel);
   				allPropertyList.add(checky3);
   			}
   		    
   		    if (!Line.ProteinReference.hasNoXref)
   		    {
   		    	Line.UnificationXref.isTargetProtein = true;
	   	  		unificationXrefNum = addNewUnificationXref(biopaxmodel);
	   		    String testy = (individualClass2+"_"+String.valueOf(unificationXrefNum));   		
	   	   		Set<Resource> checky = Biopax.checkSingleLinked(testy, "xref", "ProteinReference", biopaxmodel);
	   		    allPropertyList.add(checky);
   		    }
   		}
   		else if (Line.ProteinReference.isPIP2)
   		{
   			Set<Resource> checky3 = Biopax.checkSingleLiteral(Line.ProteinReference.pip2, "displayName", "ProteinReference", biopaxmodel);
   		    allPropertyList.add(checky3);
   		}
   		else if (Line.ProteinReference.isIP3)
   		{
   			Set<Resource> checky3 = Biopax.checkSingleLiteral(Line.ProteinReference.ip3, "displayName", "ProteinReference", biopaxmodel);
   		    allPropertyList.add(checky3);
   		}
   		else if (Line.ProteinReference.isDAG)
   		{
   			Set<Resource> checky3 = Biopax.checkSingleLiteral(Line.ProteinReference.dag, "displayName", "ProteinReference", biopaxmodel);
   		    allPropertyList.add(checky3);
   		}


   		Resource result = allPropertyListGetIntersection(allPropertyList,biopaxmodel);
   		num = convertResourceToInt(result, individualClass);
   		if (num==-1)
   		{
   			num = Biopax.createBiopaxIndividual(individualClass,biopaxmodel);
   		
	   		String individualName = individualClass + "_" + String.valueOf(num);
	   		
	   		
	   		individualName = individualClass + "_" + num;
			String propertyName = "displayName";
			if (Line.ProteinReference.isSourceProtein == true)
			{
	   			if (Line.ProteinReference.isTranscriptionProduct)
	   			{
	   				literal = "protein_of_gene_" + Line.SourceName;
	   				Biopax.addBiopaxProperty(literal,"displayName",individualName,biopaxmodel);
	   			}
	   			else
	   			{
	   				Biopax.addBiopaxProperty(Line.SourceName,"displayName",individualName,biopaxmodel);
	   			}
		   		if (!Line.ProteinReference.hasNoXref)
		   		{
					propertyName = "xref";
			   		individualName = individualClass + "_" + num;
					String linkedresourceName = individualClass2 + "_" + unificationXrefNum;
					Biopax.addBiopaxLinkedProperty(propertyName, linkedresourceName, individualName, biopaxmodel);
			
		   		}
			}
			else if (Line.ProteinReference.isTargetProtein == true)
			{
	   			if (Line.ProteinReference.isTranscriptionProduct)
	   			{
	   				literal = "protein_of_gene_" + Line.TargetName;
	   				Biopax.addBiopaxProperty(literal,"displayName",individualName,biopaxmodel);
	   			}
	   			else
	   			{
	   				Biopax.addBiopaxProperty(Line.TargetName,"displayName",individualName,biopaxmodel);
	   			}
		   		if (!Line.ProteinReference.hasNoXref)
		   		{
					propertyName = "xref";
			   		individualName = individualClass + "_" + num;
					String linkedresourceName = individualClass2 + "_" + unificationXrefNum;
					Biopax.addBiopaxLinkedProperty(propertyName, linkedresourceName, individualName, biopaxmodel);
			
		   		}
			}
	   		else if (Line.ProteinReference.isPIP2)
	   		{
	   			Biopax.addBiopaxProperty(Line.ProteinReference.pip2,"displayName",individualName,biopaxmodel);
	   		}
	   		else if (Line.ProteinReference.isIP3)
	   		{
	   			Biopax.addBiopaxProperty(Line.ProteinReference.ip3,"displayName",individualName,biopaxmodel);
	   		}
	   		else if (Line.ProteinReference.isDAG)
	   		{
	   			Biopax.addBiopaxProperty(Line.ProteinReference.dag,"displayName",individualName,biopaxmodel);
	   		}

			
	   		propertyName = "organism";
	   		individualName = individualClass + "_" + num;
			String linkedresourceName2 = individualClass3 + "_" + bioSourceNum;
			Biopax.addBiopaxLinkedProperty(propertyName, linkedresourceName2, individualName, biopaxmodel);
   		}
   		Line.ProteinReference.resetVarsToFalse();

		return num;
		
	}

	private static int addNewUnificationXref(OntModel biopaxmodel) {
		String individualClass = "UnificationXref";
   		Set<Resource> checky1 = new HashSet<Resource>();
   		Set<Resource> checky2 = new HashSet<Resource>();
   		if (Line.UnificationXref.isBioSource)
   		{
	   		checky1 = Biopax.checkSingleLiteral(Line.BioSourceDB, "db", "UnificationXref", biopaxmodel);
	   		checky2 = Biopax.checkSingleLiteral(Line.BioSourceID, "id", "UnificationXref", biopaxmodel);  
   		}
   		else if (Line.UnificationXref.isSourceProtein)
   		{
	   		checky1 = Biopax.checkSingleLiteral(Line.ProteinDB, "db", "UnificationXref", biopaxmodel);
	   		checky2 = Biopax.checkSingleLiteral(Line.SourceHumanAccession, "id", "UnificationXref", biopaxmodel);  
   		}
   		else if (Line.UnificationXref.isTargetProtein)
   		{
	   		checky1 = Biopax.checkSingleLiteral(Line.ProteinDB, "db", "UnificationXref", biopaxmodel);
	   		checky2 = Biopax.checkSingleLiteral(Line.TargetHumanAccession, "id", "UnificationXref", biopaxmodel);  
   		}
   		else if (Line.UnificationXref.isPhosphorylatedResidue)
   		{
	   		checky1 = Biopax.checkSingleLiteral(Line.UnificationXref.PhosphorylatedResidueDB, "db", "UnificationXref", biopaxmodel);
	   		checky2 = Biopax.checkSingleLiteral(Line.UnificationXref.PhosphorylatedResidueID, "id", "UnificationXref", biopaxmodel);  
   		}
   		else if (Line.UnificationXref.isUbiquitinatedResidue)
   		{
   			checky1 = Biopax.checkSingleLiteral(Line.UnificationXref.UbiquitinatedResidueDB, "db", "UnificationXref", biopaxmodel);
	   		checky2 = Biopax.checkSingleLiteral(Line.UnificationXref.UbiquitinatedResidueID, "id", "UnificationXref", biopaxmodel);  
   		}
   		else if (Line.UnificationXref.isSumolatedResidue)
   		{
   			checky1 = Biopax.checkSingleLiteral(Line.UnificationXref.SumolatedResidueDB, "db", "UnificationXref", biopaxmodel);
	   		checky2 = Biopax.checkSingleLiteral(Line.UnificationXref.SumolatedResidueID, "id", "UnificationXref", biopaxmodel);  
   		}
   		else if (Line.UnificationXref.isCytosol)
   			{
   		   		checky1 = Biopax.checkSingleLiteral(Line.UnificationXref.CytosolDB, "db", "UnificationXref", biopaxmodel);
   		   		checky2 = Biopax.checkSingleLiteral(Line.UnificationXref.CytosolID, "id", "UnificationXref", biopaxmodel);  
   			}
   		else if (Line.UnificationXref.isExtracellular)
			{
		   		checky1 = Biopax.checkSingleLiteral(Line.UnificationXref.ExtracellularDB, "db", "UnificationXref", biopaxmodel);
		   		checky2 = Biopax.checkSingleLiteral(Line.UnificationXref.ExtracellularID, "id", "UnificationXref", biopaxmodel);  
			}
   		else if (Line.UnificationXref.isVesicle)
		{
	   		checky1 = Biopax.checkSingleLiteral(Line.UnificationXref.VesicleDB, "db", "UnificationXref", biopaxmodel);
	   		checky2 = Biopax.checkSingleLiteral(Line.UnificationXref.VesicleID, "id", "UnificationXref", biopaxmodel);  
		}
   		else if (Line.UnificationXref.isNucleus)
		{
	   		checky1 = Biopax.checkSingleLiteral(Line.UnificationXref.NucleusDB, "db", "UnificationXref", biopaxmodel);
	   		checky2 = Biopax.checkSingleLiteral(Line.UnificationXref.NucleusID, "id", "UnificationXref", biopaxmodel);  
		}
   		else if (Line.UnificationXref.isMitochondrion)
   		{
	   		checky1 = Biopax.checkSingleLiteral(Line.UnificationXref.MitochondrionDB, "db", "UnificationXref", biopaxmodel);
	   		checky2 = Biopax.checkSingleLiteral(Line.UnificationXref.MitochondrionID, "id", "UnificationXref", biopaxmodel);
   		}
   		else if (Line.UnificationXref.isPhosphorylationReaction)
   		{
   			checky1 = Biopax.checkSingleLiteral(Line.UnificationXref.PhosphorylationReactionDB, "db", "UnificationXref", biopaxmodel);
		   	checky2 = Biopax.checkSingleLiteral(Line.UnificationXref.PhosphorylationReactionID, "id", "UnificationXref", biopaxmodel);  
   		}
   		else if (Line.UnificationXref.isDephosphorylationReaction)
   		{
   			checky1 = Biopax.checkSingleLiteral(Line.UnificationXref.DephosphorylationReactionDB, "db", "UnificationXref", biopaxmodel);
		   	checky2 = Biopax.checkSingleLiteral(Line.UnificationXref.DephosphorylationReactionID, "id", "UnificationXref", biopaxmodel);  
   		}
   		else if (Line.UnificationXref.isUbiquitinationReaction)
   		{
   			checky1 = Biopax.checkSingleLiteral(Line.UnificationXref.UbiquitinationReactionDB, "db", "UnificationXref", biopaxmodel);
   			checky2 = Biopax.checkSingleLiteral(Line.UnificationXref.UbiquitinationReactionID, "id", "UnificationXref", biopaxmodel);
   		}
   		else if (Line.UnificationXref.isDeUbiquitinationReaction)
   		{
   			checky1 = Biopax.checkSingleLiteral(Line.UnificationXref.DeUbiquitinationReactionDB, "db", "UnificationXref", biopaxmodel);
   			checky2 = Biopax.checkSingleLiteral(Line.UnificationXref.DeUbiquitinationReactionID, "id", "UnificationXref", biopaxmodel);
   		}
   		else if (Line.UnificationXref.isPLCCleavage)
   		{
   			checky1 = Biopax.checkSingleLiteral(Line.UnificationXref.CleavageReactionDB, "db", "UnificationXref", biopaxmodel);
   			checky2 = Biopax.checkSingleLiteral(Line.UnificationXref.CleavageReactionID, "id", "UnificationXref", biopaxmodel);
   		}
   		else if (Line.UnificationXref.isProteinCleavage)
   		{
   			checky1 = Biopax.checkSingleLiteral(Line.UnificationXref.ProteinCleavageReactionDB, "db", "UnificationXref", biopaxmodel);
   			checky2 = Biopax.checkSingleLiteral(Line.UnificationXref.ProteinCleavageReactionID, "id", "UnificationXref", biopaxmodel);
   		}
   		else if (Line.UnificationXref.isSumolation)
   		{
   			checky1 = Biopax.checkSingleLiteral(Line.UnificationXref.SumolationDB, "db", "UnificationXref", biopaxmodel);
   			checky2 = Biopax.checkSingleLiteral(Line.UnificationXref.SumolationID, "id", "UnificationXref", biopaxmodel);
   		}
 
	    List<Set> allPropertyList = new ArrayList<Set>();
	    allPropertyList.add(checky1);
	    allPropertyList.add(checky2);
	    Resource result = allPropertyListGetIntersection(allPropertyList, biopaxmodel);
	    int num = convertResourceToInt(result, individualClass);
	    
   		if (num == -1)
   		{
   			num = Biopax.createBiopaxIndividual(individualClass,biopaxmodel);
   			String individualName = individualClass + "_" + num;
    		String propertyName = "db";
    		String propertyName2 = "id";
       		if (Line.UnificationXref.isBioSource)
       		{
       			Biopax.addBiopaxProperty(Line.BioSourceDB,propertyName,individualName,biopaxmodel);
       			Biopax.addBiopaxProperty(Line.BioSourceID, propertyName2, individualName, biopaxmodel);
       		}
       		else if (Line.UnificationXref.isSourceProtein)
       		{
       			Biopax.addBiopaxProperty(Line.ProteinDB,propertyName,individualName,biopaxmodel);
       			Biopax.addBiopaxProperty(Line.SourceHumanAccession, propertyName2, individualName, biopaxmodel);
       		}
       		else if (Line.UnificationXref.isTargetProtein)
       		{
       			Biopax.addBiopaxProperty(Line.ProteinDB,propertyName,individualName,biopaxmodel);
       			Biopax.addBiopaxProperty(Line.TargetHumanAccession, propertyName2, individualName, biopaxmodel);
       		}
       		else if (Line.UnificationXref.isPhosphorylatedResidue)
       		{
       			Biopax.addBiopaxProperty(Line.UnificationXref.PhosphorylatedResidueDB,propertyName,individualName,biopaxmodel);
       			Biopax.addBiopaxProperty(Line.UnificationXref.PhosphorylatedResidueID, propertyName2, individualName, biopaxmodel);
       		}
       		else if (Line.UnificationXref.isUbiquitinatedResidue)
       		{
       			Biopax.addBiopaxProperty(Line.UnificationXref.UbiquitinatedResidueDB,propertyName,individualName,biopaxmodel);
       			Biopax.addBiopaxProperty(Line.UnificationXref.UbiquitinatedResidueID, propertyName2, individualName, biopaxmodel);
       		}
       		else if (Line.UnificationXref.isCytosol)
       			{
       		   		Biopax.addBiopaxProperty(Line.UnificationXref.CytosolDB, "db", individualName, biopaxmodel);
       		   		Biopax.addBiopaxProperty(Line.UnificationXref.CytosolID, "id", individualName, biopaxmodel);  
       			}
       		else if (Line.UnificationXref.isExtracellular)
   			{
   		   		Biopax.addBiopaxProperty(Line.UnificationXref.ExtracellularDB, "db", individualName, biopaxmodel);
   		   		Biopax.addBiopaxProperty(Line.UnificationXref.ExtracellularID, "id", individualName, biopaxmodel);  
   			}
       		else if (Line.UnificationXref.isVesicle)
   			{
   		   		Biopax.addBiopaxProperty(Line.UnificationXref.VesicleDB, "db", individualName, biopaxmodel);
   		   		Biopax.addBiopaxProperty(Line.UnificationXref.VesicleID, "id", individualName, biopaxmodel);  
   			}
       		else if (Line.UnificationXref.isNucleus)
   			{
   		   		Biopax.addBiopaxProperty(Line.UnificationXref.NucleusDB, "db", individualName, biopaxmodel);
   		   		Biopax.addBiopaxProperty(Line.UnificationXref.NucleusID, "id", individualName, biopaxmodel);  
   			}
       		else if (Line.UnificationXref.isMitochondrion)
   			{
   		   		Biopax.addBiopaxProperty(Line.UnificationXref.MitochondrionDB, "db", individualName, biopaxmodel);
   		   		Biopax.addBiopaxProperty(Line.UnificationXref.MitochondrionID, "id", individualName, biopaxmodel);  
   			}
       		else if (Line.UnificationXref.isPhosphorylationReaction)
       		{
       			Biopax.addBiopaxProperty(Line.UnificationXref.PhosphorylationReactionDB, "db", individualName, biopaxmodel);
    		   	Biopax.addBiopaxProperty(Line.UnificationXref.PhosphorylationReactionID, "id", individualName, biopaxmodel);  
       		}
       		else if (Line.UnificationXref.isUbiquitinationReaction)
       		{
       			Biopax.addBiopaxProperty(Line.UnificationXref.UbiquitinationReactionDB, "db", individualName, biopaxmodel);
    		   	Biopax.addBiopaxProperty(Line.UnificationXref.UbiquitinationReactionID, "id", individualName, biopaxmodel); 
       		}
       		else if (Line.UnificationXref.isDeUbiquitinationReaction)
       		{
       			Biopax.addBiopaxProperty(Line.UnificationXref.DeUbiquitinationReactionDB, "db", individualName, biopaxmodel);
    		   	Biopax.addBiopaxProperty(Line.UnificationXref.DeUbiquitinationReactionID, "id", individualName, biopaxmodel); 
       		}
       		else if (Line.UnificationXref.isDephosphorylationReaction)
       		{
     			Biopax.addBiopaxProperty(Line.UnificationXref.DephosphorylationReactionDB, "db", individualName, biopaxmodel);
    		   	Biopax.addBiopaxProperty(Line.UnificationXref.DephosphorylationReactionID, "id", individualName, biopaxmodel); 
       		}
       		else if (Line.UnificationXref.isPLCCleavage)
       		{
     			Biopax.addBiopaxProperty(Line.UnificationXref.CleavageReactionDB, "db", individualName, biopaxmodel);
    		   	Biopax.addBiopaxProperty(Line.UnificationXref.CleavageReactionID, "id", individualName, biopaxmodel); 
       		}
       		else if (Line.UnificationXref.isProteinCleavage)
       		{
     			Biopax.addBiopaxProperty(Line.UnificationXref.ProteinCleavageReactionDB, "db", individualName, biopaxmodel);
    		   	Biopax.addBiopaxProperty(Line.UnificationXref.ProteinCleavageReactionID, "id", individualName, biopaxmodel);	
       		}
       		else if (Line.UnificationXref.isSumolation)
       		{
       			Biopax.addBiopaxProperty(Line.UnificationXref.SumolationDB, "db", individualName, biopaxmodel);
    		   	Biopax.addBiopaxProperty(Line.UnificationXref.SumolationID, "id", individualName, biopaxmodel); 
       		}
       		else if (Line.UnificationXref.isSumolatedResidue)
       		{
       			Biopax.addBiopaxProperty(Line.UnificationXref.SumolatedResidueDB,propertyName,individualName,biopaxmodel);
       			Biopax.addBiopaxProperty(Line.UnificationXref.SumolatedResidueID, propertyName2, individualName, biopaxmodel);
       		}
   		}
   		Line.UnificationXref.resetVarsToFalse();
		return num;
	}

	private static int convertResourceToInt(Resource s, String individualClass) {
		int num;
		if (s==null)
		{
	      num = -1;
		}
		else
		{
		  String bigstring = s.toString();
		  int smallstringend = (namespaceString + individualClass).length();
		  String subby = bigstring.substring(smallstringend+1); // + 1 to account for _ character
		  num = Integer.parseInt(subby);
		}
		return num;
	}

	private static int addNewBioSource(OntModel biopaxmodel) {
		String individualClass = "BioSource";
		String individualClass2 = "UnificationXref";
		Line.UnificationXref.isBioSource = true;
   		int unificationXrefNum = addNewUnificationXref(biopaxmodel);
   		
   		String testy = (individualClass2+"_"+String.valueOf(unificationXrefNum));   		
   		Set<Resource> checky = Biopax.checkSingleLinked(testy, "xref", "BioSource", biopaxmodel);
	    List<Set> allPropertyList = new ArrayList<Set>();

   		
   		Set<Resource> checky2 = Biopax.checkSingleLiteral(Line.BioSourceDisplayName, "displayName", "BioSource", biopaxmodel);  

        allPropertyList.add(checky);
        allPropertyList.add(checky2);
        Resource result = allPropertyListGetIntersection(allPropertyList, biopaxmodel);
        int num = convertResourceToInt(result, individualClass);
   		
   		if (num == -1)
   		{
	   		num = Biopax.createBiopaxIndividual(individualClass,biopaxmodel);
	   		String individualName = individualClass + "_" + String.valueOf(num);		
	   		   		
	   		String propertyName = "xref";
	   		individualName = individualClass + "_" + num;
			String linkedresourceName = individualClass2 + "_" + unificationXrefNum;
			Biopax.addBiopaxLinkedProperty(propertyName, linkedresourceName, individualName, biopaxmodel);
			
	   		individualName = individualClass + "_" + num;
			propertyName = "displayName";
			Biopax.addBiopaxProperty(Line.BioSourceDisplayName,propertyName,individualName,biopaxmodel);
	    }

		return num;
	}
	
	private static int addNewPublicationXref(OntModel biopaxmodel) {
		String individualClass = "PublicationXref";
   		Set<Resource> checky1 = new HashSet<Resource>();
   		Set<Resource> checky2 = new HashSet<Resource>();
	    checky1 = Biopax.checkSingleLiteral(Line.PublicationXref.publicationDB, "db", "PublicationXref", biopaxmodel);
	   	checky2 = Biopax.checkSingleLiteral(Line.PubMedID, "id", "PublicationXref", biopaxmodel);  

 

	    List<Set> allPropertyList = new ArrayList<Set>();
	    allPropertyList.add(checky1);
	    allPropertyList.add(checky2);
	    Resource result = allPropertyListGetIntersection(allPropertyList, biopaxmodel);
	    int num = convertResourceToInt(result, individualClass);
	    
   		if (num == -1)
   		{
   			num = Biopax.createBiopaxIndividual(individualClass,biopaxmodel);
   			String individualName = individualClass + "_" + num;
    		String propertyName = "db";
    		String propertyName2 = "id";

       	    Biopax.addBiopaxProperty(Line.PublicationXref.publicationDB,propertyName,individualName,biopaxmodel);
       	    Biopax.addBiopaxProperty(Line.PubMedID, propertyName2, individualName, biopaxmodel);
   		}
       	Line.PublicationXref.resetVarsToFalse();
		return num;
	}
	
	private static Resource allPropertyListGetIntersection(
			List<Set> allPropertyList, OntModel model) {
	    ListIterator<Set> iter = allPropertyList.listIterator();
	    Set<Resource> r = new HashSet<Resource>();
	    if (iter.hasNext())
	      {r=iter.next();}
   		while (iter.hasNext()) {r.retainAll(iter.next());}

         Resource s = null; // model.createResource();

         if (!(r.isEmpty()))
         {
        	Iterator<Resource> getItem = r.iterator();
   		    s=getItem.next();
   		 }
	     return s;         
	
	}

	}    

