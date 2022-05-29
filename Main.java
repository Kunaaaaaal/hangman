import javax.swing.*;

class Main {
	public static void main (String [] args) {

	//Changes Default Button from Spacebar to Enter Key 
		UIManager.put("Button.focusInputMap", new UIDefaults.LazyInputMap(new Object[] {
			"ENTER", "pressed",
			"released ENTER", "released"
		}));

		new Hangman();
		
	}
}