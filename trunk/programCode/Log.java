//    Sig2BioPAXv4, converts SIG flat files to BioPAX level 3
//    Copyright (C) 2010 Ryan Logan Webb
//    This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.
//    This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
//    You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
package programCode;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;


public class Log {
	
	public String fileName = "log.txt";
	public PrintStream out = null;

	public Log()
	{
		try {
		out = new PrintStream(new FileOutputStream(fileName,false));
		}
	    catch (FileNotFoundException ex){}
		
	}
	public void finalize() throws Throwable
	{
		out.close();
	}
	
	public void LogLine()
	{
		out.print(Line.SourceName);
        out.print(" ");
		out.print(Line.SourceHumanAccession);
        out.print(" ");
		out.print(Line.SourceMouseAccession);
        out.print(" ");
		out.print(Line.SourceType);
        out.print(" ");
		out.print(Line.SourceLocation);
        out.print(" ");
		out.print(Line.TargetName);
        out.print(" ");
		out.print(Line.TargetHumanAccession);
        out.print(" ");
		out.print(Line.TargetMouseAccession);
        out.print(" ");
		out.print(Line.TargetType);
        out.print(" ");
		out.print(Line.TargetLocation);
        out.print(" ");
		out.print(Line.Effect);
        out.print(" ");
		out.print(Line.TypeofInteraction);
        out.print(" ");
		out.print(Line.PubMedID);
		out.println();
	}
}
