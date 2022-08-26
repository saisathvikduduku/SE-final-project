package wandering.woods;

/*
 * Author 1: Sai sathvik 
 * Email Id: saisathvikduduku@lewisu.edu
 * 
 * Author 2: Gopi
 * Email Id: gopisandeepyerra@lewisu.edu
 * 
 * Author 3: Sailusha 
 * Email Id: sailushaijjada@lewisu.edu
 * 
 * Author 4: Deepak uppu
 * Email Id: deepakuppu@lewisu.edu
 * 
 * Author 5: Vikas
 * Email Id: vikasmothe@lewisu.edu
 */

import javax.swing.JOptionPane;

public class Application {

	public static void main(String[] args) {
		String[] optionsToChoose = { "GradeK-2", "Grade 3-5", "Grade 6-8" };

		String getGameLevel = (String) JOptionPane.showInputDialog(null, "Wandering in Woods", "Choose Game Level",
				JOptionPane.QUESTION_MESSAGE, null, optionsToChoose, optionsToChoose[2]);

		if (getGameLevel.equals("GradeK-2")) {
			ApplicationBasic.run(null);
		} else if (getGameLevel.equals("Grade 3-5")) {
			ApplicationMedium.run(null);
		} else if (getGameLevel.equals("Grade 6-8")) {
			ApplicationHigh.run(null);
		}

	}
}
