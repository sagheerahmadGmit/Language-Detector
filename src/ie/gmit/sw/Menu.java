//Menu
package ie.gmit.sw;

import java.io.File;
import java.util.Map;
import java.util.Scanner;

/**
 * @author Sagheer Ahmad
 * @version 1.0
 * @since 1.8
 * 
 * The Class Menu.
 */
public class Menu {

	/** The s. */
	// Call the scanner class and declare variables
	private Scanner s = new Scanner(System.in);

	/** The wili location. */
	private String wiliLocation;

	/** The query file. */
	private String queryFile;

	/** The ngram. */
	private int ngram;
	
	/** The choice */
	private int choice;

	/**
	 * Prints the menu. 
	 * print out the print menu option for the user. 
	 * Ask the user to enter the Wili File Location. 
	 * start and join the thread. 
	 * Enter in the query file. 
	 * Analyse the file and see what language it is and print out the result. 
	 * Also allows the user to change the ngrams used. 
	 */
	// print out the print menu option for the user
	public void printMenu() {
		// Menu
		System.out.println("|***************************************************|");
		System.out.println("|* GMIT - Dept. Computer Science & Applied Physics *|");
		System.out.println("|*                                                 *|");
		System.out.println("|*             Text Language Detector              *|");
		System.out.println("|*                                                 *|");
		System.out.println("|***************************************************|");
		
		// Ask the user to enter the Wili File Location
		do {
			System.out.println("Enter WiLI Data Location: ");
			wiliLocation = s.next();
		}while(!new File(wiliLocation).isFile());
		

		try {
			
			//change the number of ngrams used
			do
			{
				System.out.println("\nThe default ngram is 3(Recommended)");
				System.out.println("If you like to Change this press 1 or press 2 to use default: ");
				choice = s.nextInt();
			}while(!(choice == 1) && !(choice == 2));
			
			if(choice == 1)
			{
				System.out.println("\nPlease enter the new ngrams: ");
				ngram = s.nextInt();
			}
			else
			{
				System.out.println("\nDefault Ngram is being used(3)!");
				ngram = 3;
			}

			//call the parser class
			Parser p = new Parser(wiliLocation, ngram);
			Database db = new Database();
			p.setDb(db);
			// declare thread
			Thread t = new Thread(p);
			// start the thread to begin execution
			t.start();
			System.out.println("\nThe Database is being Created...");
			// allows one thread to wait until another thread completes its execution
			t.join();
			System.out.println("The Database is Created!!");

			// resize the database
			db.resize(300);

			// ask the user to entry the query file - Language they want to detect
			do {
				System.out.println("Enter Query File Location: ");
				queryFile = s.next();
			}while(!new File(queryFile).isFile());
			

			// Analyse the file and see what language it is and print out the result
			Language query = p.analyseQuery(queryFile);
			System.out.println("Processing Query...Please Wait...");
			System.out.println("This Text Appears to be written in " + query);

		} catch (Exception e) {
			// TODO: handle exception
		}

	}

}