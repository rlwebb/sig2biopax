## Sig2BioPAX: Java tool for converting flat text files to BioPAX Level 3 format ##
### Abstract ###
The World Wide Web plays a critical role in enabling researchers to exchange, search, process, visualize, integrate and analyze experimental data. Such efforts can be further enhanced through the development of the concept of the semantic web. The semantic web idea is to enable machines to understand data through the development of protocol free data exchange formats such as Resource Description Framework ([RDF](http://en.wikipedia.org/wiki/Resource_Description_Framework)) and the Web Ontology Language ([OWL](http://en.wikipedia.org/wiki/Web_Ontology_Language)). These standards provide formal descriptors of objects, object properties and their relationships within a specific knowledge domain. However, the overhead of converting datasets typically stored in data tables such as Excel or other types of spreadsheets into [RDF](http://en.wikipedia.org/wiki/Resource_Description_Framework)  or [OWL](http://en.wikipedia.org/wiki/Web_Ontology_Language)  formats is not trivial for non-specialists and as such produce a barrier to seamless data exchange between researchers, databases and analysis tools. This problem is particular of need and importance in the field of network systems biology where biochemical interactions between genes and their products are abstracted to networks. For the purpose of converting biochemical interactions into the [BioPAX](http://www.biopax.org)  format, the leading standard developed by the computational systems biology community, we developed an open-source command line tool that takes as input tabular data describing different types of biochemical interactions. The tool converts such interactions into the [BioPAX](http://www.biopax.org) level 3 [OWL](http://en.wikipedia.org/wiki/Web_Ontology_Language)  format. We used the tool to convert several existing and novel mammalian networks of protein interactions, signaling pathways and transcriptional regulatory networks into [BioPAX](http://www.biopax.org)  and deposited these into [PathwayCommons](http://www.pathwaycommons.org) a repository for consolidating and organizing biochemical networks. Our command line tool sig2biopax is a useful resource that can enable experimental and computational systems biologists to contribute their identified networks for integration and reuse with the research community.
### Running Sig2BioPAXv4 ###
Sig2BioPAXv4 is packaged as an executable JAR file. You must have Java Virtual Machine installed on your computer. JVM is available from [http://www.java.com/getjava](http://www.java.com/getjava). To run the GUI (graphical user interface) version of Sig2BioPAXv4, simply double click the sig2biopaxv4.jar, which is the distribution file. To use the command line version of the program open a command prompt and navigate to the folder containing the [Sig2BioPAXv4.jar](http://sig2biopax.googlecode.com/files/Sig2BioPAXv4.jar). Enter the command: <br><code>java –jar sig2biopaxv4.jar -cmd args</code>, where args are the arguments you wish to supply as described below. For example, to use input file foo.txt, output file bar.owl, with the overwrite option, the command is: <code>java -jar sig2biopaxv4.jar -cmd -in: foo.txt -out:bar.owl -o</code>

If no arguments are used, the default input is input.txt, the default output is output.owl, and a sig input template, as well as the non-overwriting option will be used.<br>
<h4>Input file types</h4>
The command line tool may be accessed by using the command line argument <code>–cmd</code>. In the command line tool, there are four different options which may be fed into the program as command-line arguments separated by spaces. The four options are: <br>
1. <b>Input File name</b>. This is specified by the syntax <code>-in:filename</code>, where <code>filename</code>  is the path to the input file. If no input file is specified, the default input.txt will be automatically attempted by the program. The file may be specified with either: name only, or directory structure + name. If name only, the program will search for the file in the same directory as the EXE.  IMPORTANT – if the directory has spaces in the name, this argument must be surrounded by double quotation marks, “”.<br>
2. <b>Output File name</b>. This is specified by the syntax <code>-out:filename</code>, where <code>filename</code> is the path to the output file. If no output file is specified, the default output.owl will be used. The file may be specified with either: name only, or directory structure + name. If name only, the program will create the output file in the same directory as the EXE.  If directory + name, you must create the directory yourself first or an exception will be thrown. IMPORTANT – if the directory has spaces in the name, this argument must surrounded by the double quotation marks, “”.<br>
3. <b>Overwrite Output: -o</b> . This switch, if given, will cause the program to erase either the default output file or the output file that was specified previously. The default is OFF, i.e., don't overwrite. In this case, a number will be appended onto the end of the output file name.<br>
4. <b>Input Template Name</b>. This is specified by the syntax <code>-t:type</code>, where <code>type</code> is the typename of the desired input template. Currently there are three supported templates. First, the default, sig. This option will parse files having the following line syntax:<br>
<code>SN SHA SMA ST SL TN THA TMA TT TL E TOI PID</code><br>
<code>KEY: </code><br>
<code>SN = SourceName: Name of source molecule</code><br>
<code>SHA = SourceHumanAccession: Source Swiss-Prot human accession number</code><br>
<code>SMA = SourceMouseAccession: Source Swiss-Prot mouse accession number</code><br>
<code>ST = SourceType: Type of source molecule</code><br>
<code>SL = SourceLocation: Location of source molecule in the cell</code><br>
<code>TN = TargetName: Name of target molecule</code><br>
<code>THA = TargetHumanAccession: target Swiss-Prot human accession number</code><br>
<code>TMA = TargetMouseAccession: target Swiss-Prot mouse accession number</code><br>
<code>TT = TargetType: Type of target molecule</code><br>
<code>TL = TargetLocation: Location of target molecule in the cell</code><br>
<code>E = Effect: Effect of source on target. + (activating), _ (deactivating), or 0 (neutral) </code><br>
<code>TOI = TypeOfInteraction: Reaction type definition</code><br>
<code>PID = PubMedID: ID of article that identified this reaction </code><br>

The second format can be chosen using the string argument <code>source_target</code>. This option tells the program to parse the input file as having only six columns: <br>
<code>SN SL TN TL E TOI PID</code> <br>
The third format is <code>tf_target</code>. This format is for converting transcription-factor target-gene interaction pairs. The field names (columns) are <code>SourceName TargetName and PubMedID</code>. Sometimes, in this format, the PubMedID comes appended to the <code>SourceName</code> like this: <code>SourceName-PubMedID</code>. If this is the case, Sig2BioPAX will strip off the <code>PubMedID</code> from the <code>SourceName</code>.<br>
<h3>Real example</h3>
The focal adhesome network sig file from the <a href='http://www.adhesome.org'>adhesome.org</a> web site can be converted into BioPAX Level 3 by typing on the command-line:<br>
<code>java –jar sig2biopaxv4.jar -cmd –in:fa.sig –out:fa.owl –o</code><br>
The input and output files can be viewed here: <a href='http://sig2biopax.googlecode.com/files/fa.sig'>fa.sig</a> and <a href='http://sig2biopax.googlecode.com/files/fa.owl'>fa.owl</a>

<h3>Contact</h3>
avi dot maayan at mssm dot edu and <br>
ryan dot webb at mssm dot edu<br>
