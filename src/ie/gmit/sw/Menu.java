//Menu
package ie.gmit.sw;

import java.util.Map;
import java.util.Scanner;

public class Menu 
{
	//Call the scanner class and declare variables
	private Scanner s = new Scanner(System.in);
	private String wiliLocation;
	private String queryFile;
	private int ngram;
			
	//print out the print menu option for the user
	public void printMenu()
	{	
	
		System.out.println("|***************************************************|");
		System.out.println("|* GMIT - Dept. Computer Science & Applied Physics *|");
		System.out.println("|*                                                 *|");
		System.out.println("|*             Text Language Detector              *|");
		System.out.println("|*                                                 *|");
		System.out.println("|***************************************************|");
		System.out.println("Enter WiLI Data Location: ");
		wiliLocation = s.next();
		
		try {
	
			Parser p = new Parser(wiliLocation, 5);
			Database db = new Database();
			p.setDb(db);
			Thread t = new Thread(p);
			t.start();
			System.out.println("The Database is being Created!!");
			t.join();
			System.out.println("The Database is Created!!");
			
			db.resize(300);
			
			//String s = "This is an example of the english language";
			
			System.out.println("Enter Query File Location: ");
			queryFile = s.next();
			
			Map<Integer, LanguageEntry> query = p.analyseQuery(queryFile);
			System.out.println("Processing Query...Please Wait...");
			System.out.println("This Text Appears to be written in "+ db.getLanguage(query));
			
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		
		
	}
	
		
}