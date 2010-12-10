//    Sig2BioPAXv4, converts SIG flat files to BioPAX level 3
//    Copyright (C) 2010 Ryan Logan Webb
//    This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.
//    This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
//    You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
package programCode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.SimpleSelector;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;


public class Biopax {
  public static String biopaxString = "http://www.biopax.org/release/biopax-level3.owl#";
  
  public static String namespaceString = "http://www.mssm.edu/labs/maayan#";

  public static int createBiopaxIndividual (String individualClass, OntModel model) {
	  Resource res = model.createResource(biopaxString+individualClass);
	  StmtIterator iter = model.listStatements(null, null, (RDFNode) null);

	 Statement s = null;
	 int newNum = -1;
	 int biggestNum = -1;
	 while (iter.hasNext())
	 {
		 s = iter.nextStatement();
         if (s.getSubject().toString().startsWith(namespaceString+individualClass+"_"))
		 {
			 String bigstring = s.getSubject().toString();
			 int smallstringend = (namespaceString + individualClass).length();
			 String subby = bigstring.substring(smallstringend+1); // + 1 to account for _ character
			 newNum = Integer.parseInt(subby);
			 if (newNum > biggestNum)
			 {
				 biggestNum = newNum;
			 }
		 }
	 }
	 biggestNum++;
	 String biggestNumString = String.valueOf(biggestNum);
	 model.createIndividual(namespaceString + individualClass + "_" + biggestNumString,res);
	 return biggestNum;
  }
  public static void addBiopaxProperty(String propertyLiteral, String propertyName, String individualName, OntModel model) {
	  Individual ind = model.getIndividual(namespaceString + individualName);
	  Property prop = model.createProperty(biopaxString+propertyName);
	  ind.addProperty(prop,propertyLiteral);
  }
  
  public static void addBiopaxLinkedProperty (String propertyName, String linkedresourceName,
		                                      String individualName, OntModel model) {
	  Individual ind = model.getIndividual(namespaceString+individualName);
	  Property prop = model.createProperty(biopaxString + propertyName);
	  Resource res = model.createResource(namespaceString + linkedresourceName);
	  try {
		ind.addProperty(prop,res);
	} catch (NullPointerException e) {
		// TODO Auto-generated catch block
		System.err.println("Caught Exception: " 
                +  e.getMessage() + "individual name: " + individualName);
        System.exit(0);
	}
  }
  
  public static Set<Resource> checkSingleLiteral (String propertyLiteral, String propertyCheckName, String individualClass, OntModel model)
  {
	  // returns a set of resources with Subject of type individualClass 
	  // that have a property of propertyCheckName with literal value propertyLiteral
	  Property prop = model.createProperty(biopaxString + propertyCheckName);
	  Resource res = model.createResource(namespaceString+individualClass);
	  Literal lit = model.createLiteral(propertyLiteral);
	  
	// do all filtering in the selects method
	  StmtIterator iter = model.listStatements(null, prop, lit);
	  Set<Resource> set1 = new HashSet<Resource>();
	  Statement s = null;
	  while (iter.hasNext())
	  {
          s = iter.nextStatement();
          if (s.getSubject().toString().startsWith(namespaceString+individualClass))
          {
		    set1.add(s.getSubject());
          }
	  }
	  return set1;
  }


  public static Set<Resource> checkSingleLinked (String resourceLinked, String propertyCheckName, String individualName, OntModel model)
  {
	  Property prop = model.createProperty(biopaxString + propertyCheckName);

	  Resource lit = model.createResource(namespaceString+resourceLinked);

	  StmtIterator iter = model.listStatements(null, prop, lit);
	  Set<Resource> set1 = new HashSet<Resource>();
	  Statement s = null;
	  while (iter.hasNext())
	  {
          s = iter.nextStatement();
          if (s.getSubject().toString().startsWith(namespaceString+individualName))
          {

		    	set1.add(s.getSubject());
//		    
          }
	  }
	  return set1;
  }
public static Set<Resource> checkAllLinked(String propertyCheckName, String individualName, OntModel model) {
		  Property prop = model.createProperty(biopaxString + propertyCheckName);

	//	  Resource lit = model.createResource(namespaceString+resourceLinked);

		  StmtIterator iter = model.listStatements(null, prop, (RDFNode) null);
		  Set<Resource> set1 = new HashSet<Resource>();
		  Statement s = null;
		  while (iter.hasNext())
		  {
	          s = iter.nextStatement();
	          if (s.getSubject().toString().startsWith(namespaceString+individualName))
	          {
			    	set1.add(s.getSubject());
//			    
	          }
		  }
		  return set1;
	  }
}


