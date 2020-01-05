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
		//Menu
		System.out.println("|***************************************************|");
		System.out.println("|* GMIT - Dept. Computer Science & Applied Physics *|");
		System.out.println("|*                                                 *|");
		System.out.println("|*             Text Language Detector              *|");
		System.out.println("|*                                                 *|");
		System.out.println("|***************************************************|");
		//Ask the user to enter the Wili File Location
		System.out.println("Enter WiLI Data Location: ");
		wiliLocation = s.next();
		
		try {
	
			Parser p = new Parser(wiliLocation, 3);
			Database db = new Database();
			p.setDb(db);
			//declare thread
			Thread t = new Thread(p);
			//start the thread to begin execution
			t.start();
			System.out.println("The Database is being Created...");
			//allows one thread to wait until another thread completes its execution
			t.join();
			System.out.println("The Database is Created!!");
			
			//resize the database
			db.resize(300);
			
			//ask the user to entry the query file - Language they want to detect
			System.out.println("Enter Query File Location: ");
			queryFile = s.next();
			
			//Analyse the file and see what language it is and print out the result
			Language query = p.analyseQuery(queryFile);
			System.out.println("Processing Query...Please Wait...");
			System.out.println("This Text Appears to be written in "+ query);
			
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		
		
	}
	
		
}