//Runner
package ie.gmit.sw;

import java.util.*; 
/**
 * @author Sagheer Ahmad
 * @version 1.0
 * @since 1.8
 * 
 * The Class Runner.
 */
public class Runner {

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @throws Throwable the throwable.
	 * 
	 * Call the menu class and the printMenu function.
	 */
	public static void main(String[] args) throws Throwable {
		
		//Call the menu class and the printMenu function
		//The printMenu function outputs the menu for the user and allows
		//the user to enter valid details to detect the language
		
		new Menu().printMenu();
		
	}

}
