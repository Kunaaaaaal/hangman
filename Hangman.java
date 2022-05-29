import java.io.*;
import java.awt.*;
import java.util.*;
import javax.swing.*;
import java.awt.event.*;

public class Hangman implements ActionListener {

//Code Elements
	String illegalChar = ".1,8!?2:;(3){4}9[5]@*~/\\6–#$7&^%0|`";
	Random shuffle = new Random();
	static int vibrationLength = 5;
    static int vibrationSpeed = 2;

	String incorrectGuesses = "";
	String selectedWord;
	String progressDone;
	String progress;
	String command;
	String input;

	char [] wordToGuess;
	char [] guesses;
	String [] wordFile;

	int att = 1;
	int wins = 0;
	int losses = 0;
	int lives = 6;
	int gameState = 0;
	int gameInstance = 0;
	int keyboardState = 0;

//GUIs
	/*-----Frames-----*/
	JFrame mainWindow = new JFrame("HANGMAN");
	ImageIcon icon = new ImageIcon("Files/Images/GUI/Others/iconHangman.png");
	/*-----MenuBar-----*/
	JMenuBar menuBar = new JMenuBar();
	JMenu fileMenu = new JMenu("MENU");
	JMenuItem menuHome = new JMenuItem ("HOME");
	JMenuItem menuResume = new JMenuItem ("RESUME");
	JMenuItem menuStart = new JMenuItem ("RESTART");
	JLabel scoreCount = new JLabel();
	/*-----Home-----*/
	JLabel title = new JLabel(new ImageIcon("Files/Images/GUI/Others/homeTitle.png"));
	JLabel hangmanAnimation = new JLabel(new ImageIcon("Files/Images/GUI/Others/homeAnimation.gif"));
	JButton buttonNewGame = new JButton(new ImageIcon("Files/Images/GUI/ButtonSprites/buttonNewGame.png"));
	JButton buttonResume = new JButton(new ImageIcon("Files/Images/GUI/ButtonSprites/buttonResume.png"));
	JButton buttonOptions = new JButton(new ImageIcon("Files/Images/GUI/ButtonSprites/buttonOptions.png"));
	JButton buttonExit = new JButton(new ImageIcon("Files/Images/GUI/ButtonSprites/buttonExit.png"));
	JLabel eye = new JLabel(new ImageIcon("Files/Images/GUI/Others/homeEye.png"));
	JLabel credits = new JLabel("CREATED BY: KUNAL-NAINA-MAHEEP-MANOJ");
	/*-----Game-----*/
	JLabel hint = new JLabel();
	JLabel lineTop = new JLabel();
	JLabel attemptCount = new JLabel("Attempt #" + att);
	JLabel lifeCount = new JLabel("Lives left: " + lives);
	JLabel progressVisual = new JLabel();
	JLabel progressWord = new JLabel();
	JLabel lineBottom = new JLabel();
	JLabel guide = new JLabel("PLEASE ENTER A LETTER: ");
	JButton buttonKeyboard = new JButton(new ImageIcon("Files/Images/GUI/ButtonSprites/buttonKeyboard.png"));
	JTextField entry = new JTextField();
	JButton submit = new JButton("SUBMIT");
	JButton endPlayAgain = new JButton("PLAY AGAIN");
	JButton endHome = new JButton("LEAVE");
	/*-----On-Screen Keyboard-----*/
	JPanel keyboardWindow = new JPanel();
	JPanel keyPanel = new JPanel();

//Window
	public Hangman() {

	//Frame
		/*-----Game Window-----*/
		mainWindow.setSize(600,500);
		mainWindow.setResizable(false);
		mainWindow.setIconImage(icon.getImage());
		mainWindow.getContentPane().setBackground(Color.WHITE);
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainWindow.setLocationRelativeTo(null);
		mainWindow.setLayout(null);
		
	//Action Listeners
		buttonResume.addActionListener(this);
		buttonNewGame.addActionListener(this);
		buttonExit.addActionListener(this);
		entry.addActionListener(this);
		buttonKeyboard.addActionListener(this);
		submit.addActionListener(this);
		endPlayAgain.addActionListener(this);
		endHome.addActionListener(this);

	//Hover Effect Mouse Listeners 
		buttonResume.addMouseListener(new java.awt.event.MouseAdapter() {
   			public void mouseEntered(java.awt.event.MouseEvent evt) {
       			buttonResume.setIcon(new ImageIcon("Files/Images/GUI/ButtonHovers/hoverResume.png"));
    		}

			public void mouseExited(java.awt.event.MouseEvent evt) {
        		buttonResume.setIcon(new ImageIcon("Files/Images/GUI/ButtonSprites/buttonResume.png"));
    		}	
		});

		buttonNewGame.addMouseListener(new java.awt.event.MouseAdapter() {
   			public void mouseEntered(java.awt.event.MouseEvent evt) {
       			buttonNewGame.setIcon(new ImageIcon("Files/Images/GUI/ButtonHovers/hoverNewGame.png"));
    		}

			public void mouseExited(java.awt.event.MouseEvent evt) {
        		buttonNewGame.setIcon(new ImageIcon("Files/Images/GUI/ButtonSprites/buttonNewGame.png"));
    		}	
		});

		buttonExit.addMouseListener(new java.awt.event.MouseAdapter() {
   			public void mouseEntered(java.awt.event.MouseEvent evt) {
       			buttonExit.setIcon(new ImageIcon("Files/Images/GUI/ButtonHovers/hoverExit.png"));
    		}

			public void mouseExited(java.awt.event.MouseEvent evt) {
        		buttonExit.setIcon(new ImageIcon("Files/Images/GUI/ButtonSprites/buttonExit.png"));
    		}	
		});

		buttonKeyboard.addMouseListener(new java.awt.event.MouseAdapter() {
   			public void mouseEntered(java.awt.event.MouseEvent evt) {
       			buttonKeyboard.setIcon(new ImageIcon("Files/Images/GUI/ButtonHovers/hoverKeyboard.png"));
    		}

			public void mouseExited(java.awt.event.MouseEvent evt) {
        		buttonKeyboard.setIcon(new ImageIcon("Files/Images/GUI/ButtonSprites/buttonKeyboard.png"));
    		}	
		});


	//Menu Bar
		menuBar.setVisible(false);
		mainWindow.setJMenuBar(menuBar);
		menuBar.add(fileMenu);
		fileMenu.add(menuHome);
		fileMenu.add(menuResume);
		fileMenu.add(menuStart);
		menuBar.add(Box.createHorizontalGlue());
		menuBar.add(scoreCount);

		menuHome.addActionListener(this);
		menuResume.addActionListener(this);
		menuStart.addActionListener(this);

	//On-Screen Keyboard Window
		mainWindow.add(keyboardWindow);
		keyboardWindow.setBounds(-1, 350, 600, 140);
		keyboardWindow.setBackground(Color.decode("#f5f5f5"));
		keyboardWindow.setVisible(false);
		keyboardWindow.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.BLACK, 1), BorderFactory.createEmptyBorder(4, 5, 5, 5)));
		/*-----Key Container-----*/
		keyboardWindow.add(keyPanel);
		keyPanel.setBackground(Color.decode("#f5f5f5"));
		keyPanel.setPreferredSize(new Dimension(327, 79));
		createButtons(keyPanel);

		startHomeScreen();
	}

//Keyboard Buttons Initialization
	public void createButtons(JPanel keyPanel) {
		JButton[] buttons = new JButton[26];
		String[] letters = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
				"K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
				"W", "X", "Y", "Z" };

		for (int i = 0; i < buttons.length; i++) {
			buttons[i] = new JButton(letters[i]);
			buttons[i].setPreferredSize(new Dimension(30, 20));
			buttons[i].setFocusPainted(false);
			buttons[i].setBorder(BorderFactory.createRaisedBevelBorder());
			buttons[i].setBackground(Color.decode("#fafafa"));
			buttons[i].setMargin(new Insets(0, 0, 0, 0));
			buttons[i].setFont(new Font("Calibri", Font.BOLD, 11));
			buttons[i].setActionCommand(letters[i]);
			buttons[i].addActionListener(this);
			buttons[i].addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseEntered (java.awt.event.MouseEvent me) {
				JButton event = (JButton) me.getSource();
				event.setBorder(BorderFactory.createLineBorder(Color.decode("#f5f5f5"), 2));
			}
			public void mousePressed (java.awt.event.MouseEvent me) {
				JButton event = (JButton) me.getSource();
				event.setBorder(BorderFactory.createLoweredBevelBorder());
				event.setContentAreaFilled(false);
			}

			public void mouseExited (java.awt.event.MouseEvent me) {
				JButton event = (JButton) me.getSource();
				event.setBorder(BorderFactory.createRaisedBevelBorder());
				event.setContentAreaFilled(true);
			}
			});

			keyPanel.add(buttons[i]);
		}
	}

//First Home Screen
	public void startHomeScreen() {

		mainWindow.getContentPane().setBackground(Color.WHITE);

	//Title
		title.setBounds(40, 52, 500, 80);
		title.setEnabled(true);
		title.setVisible(true);
		mainWindow.add(title);

	//Hangman Animation
		hangmanAnimation.setBounds(420, 32, 60, 85);
		hangmanAnimation.setEnabled(true);
		hangmanAnimation.setVisible(true);
		mainWindow.add(hangmanAnimation);

	//Resume Button
		buttonResume.setVisible(false);

	//New Game Button
		buttonNewGame.setBounds(119, 170, 330, 42);
		buttonNewGame.setContentAreaFilled(false);
		buttonNewGame.setBorderPainted(false);
		buttonNewGame.setOpaque(false);
		buttonNewGame.setEnabled(true);
		buttonNewGame.setVisible(true);
		mainWindow.add(buttonNewGame);

	//Options Button
		buttonOptions.setBounds(127, 220, 315, 20);
		buttonOptions.setContentAreaFilled(false);
		buttonOptions.setBorderPainted(false);
		buttonOptions.setOpaque(false);
		buttonOptions.setEnabled(false);
		buttonOptions.setVisible(true);
		mainWindow.add(buttonOptions);

	//Exit Button
		buttonExit.setBounds(148, 265, 280, 20);
		buttonExit.setContentAreaFilled(false);
		buttonExit.setBorderPainted(false);
		buttonExit.setOpaque(false);
		buttonExit.setEnabled(true);
		buttonExit.setVisible(true);
		mainWindow.add(buttonExit);

	//Credits
		credits.setFont(new Font("Calibri", Font.PLAIN, 11));
		credits.setBounds(25, 363, 290, 100);
		credits.setEnabled(true);
		credits.setVisible(true);
		mainWindow.add(credits);

	//Eye
		eye.setBounds(5, 362, 20, 100);
		eye.setEnabled(true);
		eye.setVisible(true);
		mainWindow.add(eye);

	//Loads Main Window After Everything's Done
		mainWindow.setVisible(true);
		
	}

//Home Screen
	public void homeScreen() {

	mainWindow.getContentPane().setBackground(Color.WHITE);
	
	//Disables Other GUIs
		menuHome.setEnabled(false);

		hint.setVisible(false);
		lineTop.setVisible(false);
		attemptCount.setVisible(false);
		lifeCount.setVisible(false);
		progressVisual.setVisible(false);
		progressWord.setVisible(false);
		lineBottom.setVisible(false);
		guide.setVisible(false);
		entry.setVisible(false);
		buttonKeyboard.setVisible(false);
		submit.setVisible(false);
		endPlayAgain.setVisible(false);
		endHome.setVisible(false);

	//Title
		title.setBounds(20, 52, 250, 60);
		title.setEnabled(true);
		title.setVisible(true);
		mainWindow.add(title);

	//Hangman Animation
		hangmanAnimation.setBounds(270, 32, 60, 85);
		hangmanAnimation.setEnabled(true);
		hangmanAnimation.setVisible(true);
		mainWindow.add(hangmanAnimation);

	//Resume Button
		buttonResume.setBounds(132, 170, 71, 20);
		buttonResume.setContentAreaFilled(false);
		buttonResume.setBorderPainted(false);
		buttonResume.setOpaque(false);
		buttonResume.setEnabled(true);
		buttonResume.setVisible(true);
		mainWindow.add(buttonResume);	

	//New Game Button
		buttonNewGame.setBounds(119, 210, 101, 22);
		buttonNewGame.setContentAreaFilled(false);
		buttonNewGame.setBorderPainted(false);
		buttonNewGame.setOpaque(false);
		buttonNewGame.setEnabled(true);
		buttonNewGame.setVisible(true);
		mainWindow.add(buttonNewGame);

	//Options Button
		buttonOptions.setBounds(127, 250, 84, 20);
		buttonOptions.setContentAreaFilled(false);
		buttonOptions.setBorderPainted(false);
		buttonOptions.setOpaque(false);
		buttonOptions.setEnabled(false);
		buttonOptions.setVisible(true);
		mainWindow.add(buttonOptions);

	//Exit Button
		buttonExit.setBounds(148, 290, 40, 20);
		buttonExit.setContentAreaFilled(false);
		buttonExit.setBorderPainted(false);
		buttonExit.setOpaque(false);
		buttonExit.setEnabled(true);
		buttonExit.setVisible(true);
		mainWindow.add(buttonExit);

	//Credits
		credits.setFont(new Font("Calibri", Font.PLAIN, 9));
		credits.setBounds(25, 363, 290, 20);
		credits.setEnabled(true);
		credits.setVisible(true);
		mainWindow.add(credits);

	//Eye
		eye.setBounds(5, 362, 5, 20);
		eye.setEnabled(true);
		eye.setVisible(true);
		mainWindow.add(eye);
		
	}

//Game Initialization
	public void newGame() {
		
		gameState = 0;
		mainWindow.getContentPane().setBackground(Color.WHITE);
		
		menuHome.setEnabled(true);
		menuResume.setVisible(false);
		menuStart.setText("Restart");

	//Removes Unneeded GUI
		title.setVisible(false);
		hangmanAnimation.setVisible(false);
		buttonResume.setVisible(false);
		buttonNewGame.setVisible(false);
		buttonOptions.setVisible(false);
		buttonExit.setVisible(false);
		eye.setVisible(false);
		credits.setVisible(false);
		
		endPlayAgain.setVisible(false);
		endHome.setVisible(false);
	
	//Resets Game Information
		att = 1;
		lives = 6;
		incorrectGuesses = "";

	//Hint
		getRandomList();
		hint.setBounds(28, 10, 500, 30);
		hint.setHorizontalAlignment(SwingConstants.CENTER); 

	//Top Line
		lineTop.setText("----------------------------------------------------------------------------------------------------------------------------------------");
		lineTop.setBounds(20, 30, 720, 30);

	//Attempt Count
		attemptCount.setBounds(25, 50, 290, 30);

	//Life Count
		lifeCount.setBounds(490, 50, 290, 30);

	//Hangman
		progressVisual.setBounds(240, 82, 100, 150);
		printHangman();

	//Word Progression
		progressWord.setText("Word to Guess: " + progress);
		progressWord.setBounds(34, 240, 480, 30);
		progressWord.setHorizontalAlignment(SwingConstants.CENTER);

	//Bottom Line
		lineBottom.setText("----------------------------------------------------------------------------------------------------------------------------------------");
		lineBottom.setBounds(20, 260, 720, 30);

	//Guide
		guide.setBounds(24, 280, 320, 30);
		guide.setHorizontalAlignment(SwingConstants.LEFT);

	//Display Keyboard Button
		buttonKeyboard.setBounds(5, 349, 22, 14);
		buttonKeyboard.setContentAreaFilled(false);
		buttonKeyboard.setBorderPainted(false);
		buttonKeyboard.setFocusPainted(false);
		buttonKeyboard.setOpaque(true);

	//Input
		entry.setText("Enter your guess");
		entry.setMargin(new Insets(0, 10, 0, 10));
		entry.setBounds(180, 315, 110, 30);

	//Submission Button
		submit.setBounds(293, 315, 90, 29);
		submit.setFocusPainted(false);
	
	//Play Again Button
		endPlayAgain.setBounds(25, 315, 110, 29);
		endPlayAgain.setFocusPainted(false);

	//Home Button
		endHome.setBounds(150, 315, 80, 29);
		endHome.setFocusPainted(false);

	//Adds Game GUI to Window
		mainWindow.add(hint);
		mainWindow.add(lineTop);
		mainWindow.add(attemptCount);
		mainWindow.add(lifeCount);
		mainWindow.add(progressVisual);
		mainWindow.add(progressWord);
		mainWindow.add(lineBottom);
		mainWindow.add(guide);
		mainWindow.add(entry);
		mainWindow.add(buttonKeyboard);
		mainWindow.add(submit);
		mainWindow.add(endPlayAgain);
		mainWindow.add(endHome);

	//Enables Game GUI
		hint.setVisible(true);
		lineTop.setVisible(true);
		attemptCount.setVisible(true);
		lifeCount.setVisible(true);
		progressVisual.setVisible(true);
		progressWord.setVisible(true);
		lineBottom.setVisible(true);
		guide.setVisible(true);
		entry.setVisible(true);
		buttonKeyboard.setVisible(true);
		submit.setVisible(true);

		guide.setText("Please enter a letter: ");
		guide.setForeground(Color.darkGray);
	
	//Initializes Words and Updates Variables
		printWord();
		updateVariables();

	}

//Resume Game GUI
	public void resumeGame() {

		mainWindow.getContentPane().setBackground(Color.WHITE);
		
		menuHome.setEnabled(true);
		menuResume.setVisible(false);
	
	//Removes Home GUI
		title.setVisible(false);
		hangmanAnimation.setVisible(false);
		buttonResume.setVisible(false);
		buttonNewGame.setVisible(false);
		buttonOptions.setVisible(false);
		buttonExit.setVisible(false);
		eye.setVisible(false);
		credits.setVisible(false);

	//Enables Game GUI
		hint.setVisible(true);
		lineTop.setVisible(true);
		attemptCount.setVisible(true);
		lifeCount.setVisible(true);
		progressVisual.setVisible(true);
		progressWord.setVisible(true);
		lineBottom.setVisible(true);
		buttonKeyboard.setVisible(true);
		guide.setVisible(true);
		buttonKeyboard.setBounds(5, 350, 22, 14);
		
		/*-----Checks if Game is Complete-----*/
		if (gameState == 1 || gameState == 2) {
			entry.setVisible(false);
			submit.setVisible(false);
			buttonKeyboard.setVisible(false);
			endPlayAgain.setVisible(true);
			endHome.setVisible(true);
		}
		else {
			entry.setVisible(true);
			submit.setVisible(true);
			buttonKeyboard.setVisible(true);
			endPlayAgain.setVisible(false);
			endHome.setVisible(false);
		}

	}

//Information Updater
	public void updateVariables() {
		printHangman();
		reprintWord();
		entry.setText(null);
		scoreCount.setText("W: " + wins + " L: " + losses + "  ");
		progressWord.setText("Word to Guess: " + progress);
		attemptCount.setText("Attempt #" + att);
		lifeCount.setText("Lives left: " + lives);
	}

//Random Word Selector from WordList
	public String getWord() {
		wordFile = textFile();

		int n = wordFile.length;
		int r = shuffle.nextInt(n);
		String word = wordFile[r];
		
		return word;
	}

//Selects Random Word List
	public String getRandomList() {
		String chosenList = "";

        int i = shuffle.nextInt(5);
        switch (i) {
			/*-----Word Banks-----*/
			case 0:
            String ourPlanet = "Files/WordLists/ourPlanets.txt";
            chosenList = ourPlanet;
            hint.setText("Hint: A Planet in our Solar System (Easy)");
            break;

			case 1:
            String basicWords = "Files/WordLists/basicWords.txt";
            chosenList = basicWords;
            hint.setText("Hint: An English Word (Random)");
            break;

			case 2:
            String dataTypes = "Files/WordLists/dataTypes.txt";
            chosenList = dataTypes;
            hint.setText("Hint: A Java Data Type (Medium)");
            break;

			case 3:
            String fruits = "Files/WordLists/fruitList.txt";
            chosenList = fruits;
            hint.setText("Hint: A Popular Fruit (Easy)");
            break;
        }

		return chosenList;
	}

//File Reader
	public String[] textFile() {
		BufferedReader reader = null;
		String wordBank = getRandomList();
		ArrayList<String> wordList = new ArrayList<String>();
		try {
			reader = new BufferedReader(new FileReader(wordBank));
			String s = null;
			while ((s = reader.readLine()) != null) {
				wordList.add(s);
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
			System.exit(-1);
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				System.out.println(e.getMessage());
				System.exit(-1);
			}
		} return wordList.toArray(new String[wordList.size()]);
	}

//Replaces Selected Word With Question Marks
	public void printWord() {
		progress = "";
		selectedWord = getWord();
		progressDone = selectedWord.replaceAll(".(?!$)", "$0 ");
		wordToGuess = selectedWord.toLowerCase().toCharArray();
		guesses = new char[wordToGuess.length];
		for (int i = 0; i < guesses.length; i++) {
			guesses[i] = '?';
			progress += guesses[i] + " ";
		}
	}

//Updates Progress
	public void reprintWord() {
		progress = "";
		for (int i = 0; i < guesses.length; i++) {
			progress += guesses[i] + " ";
		}
	}

//Displays Guessed Word
	public void displayIncorrect() {
		progressWord.setText("Incorrect Guesses: " + incorrectGuesses.toLowerCase());
		javax.swing.Timer timer = new javax.swing.Timer (3000, e -> {
			progressWord.setText("WORD TO GUESS: " + progress);
		});
		timer.setRepeats(false);
		timer.start();
	}

//Action Listener
	public void actionPerformed (ActionEvent e) {
	
	//Menu Home Action
		if (e.getSource() == menuHome || e.getSource() == endHome) {
		//Removes Menu Bar
			menuBar.setVisible(false);
			keyboardWindow.setVisible(false);
			keyboardState = 0;
		
			menuResume.setVisible(true);
			mainWindow.setTitle("Home");
			System.out.println("\nGoing to Home Screen...");
			homeScreen();
		}

	//Menu Resume Action
		if (e.getSource() == menuResume || e.getSource() == buttonResume) {
		//Adds Menu Bar
			menuBar.setVisible(true);
			mainWindow.setTitle("Game #" + gameInstance);
			System.out.println("\nResuming Game...");
			resumeGame();
		}

	//Menu New Game Action
		else if (e.getSource() == menuStart || e.getSource() == buttonNewGame || e.getSource() == endPlayAgain) {
		//Adds Menu Bar
			menuBar.setVisible(true);

			gameInstance++;
			keyboardState = 0;
			mainWindow.setTitle("Game #" + gameInstance);
			System.out.println("\nStarting New Game...");
			newGame();
		}

	//Menu Exit Action
		else if (e.getSource() == buttonExit) {
			mainWindow.setTitle("Exit");
			System.out.println("\nExiting Game...");
			JOptionPane.showMessageDialog(mainWindow, "   Thanks for playing! Stay safe!", "Message", JOptionPane.PLAIN_MESSAGE);
			System.exit(0);
		}

	//Show On-screen Keyboard Button Action
		else if (e.getSource() == buttonKeyboard) {
			if (keyboardState == 0) {
				keyboardWindow.setVisible(true);
				entry.setVisible(false);
				submit.setVisible(false);
				buttonKeyboard.setBounds(5, 250, 22, 14);
				keyboardState = 1;
			}
			else if (keyboardState == 1) {
				keyboardWindow.setVisible(false);
				entry.setVisible(true);
				submit.setVisible(true);
				buttonKeyboard.setBounds(5, 349, 22, 14);
				keyboardState = 0;
			}
		}

	//Submit Button Actions
		else if (e.getSource() == submit || e.getSource() == entry) {
			
		//Gets Input From TextField
			input = entry.getText();

		//Runs if Selected Word is Incomplete
			if (!Arrays.equals(guesses, wordToGuess)) {

			//Error Handler
				if (input.length() != 1 || illegalChar.contains(input) || incorrectGuesses.contains(input.toUpperCase()) || progress.toUpperCase().contains(input.toUpperCase())) {
					/*-----Secret Phrases-----*/
					if (input.equalsIgnoreCase("answer")) {
						guide.setForeground(Color.darkGray);
						guide.setText("The word is: \"" + selectedWord + "\". You cheater ha ;)");
						updateVariables();
					}
					else if (input.equalsIgnoreCase("heal")) {
						lives = 6;
						guide.setForeground(Color.PINK);
						guide.setText("Healed. ( ͡° ͜ʖ ͡°)");
						updateVariables();
					}
					else if (input.equalsIgnoreCase("incorrect")) {
						updateVariables();
						displayIncorrect();
					}
					else if (input.equalsIgnoreCase("end")) {
						lives = 0;
						updateVariables();
					}
					/*-----Errors-----*/
					else if (input.trim().isEmpty()) {
						guide.setForeground(Color.GRAY);
						guide.setText("Blank input detected! Try again. ");
						updateVariables();
					}
					else if (illegalChar.contains(input.trim())) {
						guide.setForeground(Color.GRAY);
						guide.setText("Illegal character detected! Try again. ");
						updateVariables();
					}
					else if (input.length() > 1) {
						guide.setForeground(Color.GRAY);
						guide.setText("Multiple inputs detected! Try again. ");
						updateVariables();
					}
					else if (incorrectGuesses.contains(input.toUpperCase())) {
						guide.setForeground(Color.GRAY);
						guide.setText("Letter is already guessed! Try another one.");
						updateVariables();
						displayIncorrect();
					}
					else if (progress.toUpperCase().contains(input.toUpperCase())) {
						guide.setForeground(Color.GRAY);
						guide.setText("Letter is already guessed! Try another one.");
						updateVariables();
					}
					else {
						System.out.println("\nUnexpected Input Error Found.");
						System.exit(-1);
					}
				}

			//Guess Checker (Correct)
				else if (selectedWord.toLowerCase().contains(input.toLowerCase())) {
					att++;
					guide.setForeground(Color.darkGray);
					for (int i = 0; i < wordToGuess.length; i++) {
						if (input.toLowerCase().charAt(0) == wordToGuess[i]) {
							guesses[i] = input.toLowerCase().charAt(0);
							guide.setText("Letter \"" + input.toUpperCase() + "\" is correct. Good job!");
						}
					}
					updateVariables();
				}

			//Guess Checker (Incorrect)
				else if (!selectedWord.toLowerCase().contains(input.toLowerCase())) {
					att++;
					lives--;
					guide.setForeground(Color.darkGray);
					guide.setText("Letter \"" + input.toUpperCase() + "\" does not exist. Try again!");

					vibrate(mainWindow);

				//Updates Incorrect Guesses
					if (!selectedWord.toUpperCase().contains(input.toUpperCase())) {
						incorrectGuesses += input.toUpperCase();
						if (lives != 0 && !Arrays.equals(guesses, wordToGuess)) {
							incorrectGuesses += ", ";
						}
					}

					updateVariables();
				}

			//Game Over
				if (lives == 0) {
					entry.setVisible(false);
					submit.setVisible(false);
					buttonKeyboard.setVisible(false);
					keyboardWindow.setVisible(false);
					endPlayAgain.setVisible(true);
					endHome.setVisible(true);

					losses++;
					gameState = 2;
					updateVariables();
					progressWord.setText("Word to Guess: " + progressDone);
					guide.setForeground(Color.RED);
					guide.setText("Game over! You've used up all your lives.");
					JOptionPane.showMessageDialog(mainWindow, " You died! The word was \"" + selectedWord + "\".", "Message", JOptionPane.PLAIN_MESSAGE);
					menuStart.setText("New Game");
				}

			}

		//Runs if Selected Word is Complete
			if (Arrays.equals(guesses, wordToGuess) || input.trim().equalsIgnoreCase(selectedWord)) {
				entry.setVisible(false);
				submit.setVisible(false);
				buttonKeyboard.setVisible(false);
				keyboardWindow.setVisible(false);
				endPlayAgain.setVisible(true);
				endHome.setVisible(true);

				wins++;
				gameState = 1;
				updateVariables();
				progressWord.setText("Word to Guess: " + progressDone);
				guide.setForeground(Color.GREEN);
				guide.setText("Well done, you get to live another day!");
				JOptionPane.showMessageDialog(mainWindow, "You got the word in " + att + " attempts with " + lives + " lives left. Congratulations!", "Message", JOptionPane.PLAIN_MESSAGE);
				menuStart.setText("New Game");
			}

		}

	//Keyboard Input Handler
		else if (e.getActionCommand().length() == 1) {

			keyboardWindow.setVisible(false);
			entry.setVisible(true);
			submit.setVisible(true);
			buttonKeyboard.setBounds(5, 349, 22, 14);
			keyboardState = 0;

			command = e.getActionCommand();

			if (!Arrays.equals(guesses, wordToGuess)) {
			
			//Error Handler
				if (incorrectGuesses.contains(command.toUpperCase()) || progress.contains(command.toLowerCase())) {
					if (incorrectGuesses.contains(command.toUpperCase())) {
						guide.setForeground(Color.GRAY);
						guide.setText("Letter is already guessed! Try another one.");
						updateVariables();
						displayIncorrect();
					}
					else if (progress.contains(command.toLowerCase())) {
						guide.setForeground(Color.GRAY);
						guide.setText("Letter is already guessed! Try another one.");
						updateVariables();
					}
				}

			//Guess Checker (Correct)
				else if (selectedWord.toLowerCase().contains(command.toLowerCase())) {
					att++;
					guide.setForeground(Color.darkGray);
					for (int i = 0; i < wordToGuess.length; i++) {
						if (command.toLowerCase().charAt(0) == wordToGuess[i]) {
							guesses[i] = command.toLowerCase().charAt(0);
							guide.setText("Letter \"" + command.toUpperCase() + "\" is correct. Good job!");
						}
					}
					updateVariables();
				}

			//Guess Checker (Incorrect)
				else if (!selectedWord.toLowerCase().contains(command.toLowerCase())) {
					att++;
					lives--;
					guide.setForeground(Color.darkGray);
					guide.setText("Letter \"" + command.toUpperCase() + "\" does not exist. Try again!");

					vibrate(mainWindow);

				//Updates Incorrect Guesses
					if (!selectedWord.toUpperCase().contains(command.toUpperCase())) {
						incorrectGuesses += command.toUpperCase();
						if (lives != 0 && !Arrays.equals(guesses, wordToGuess)) {
							incorrectGuesses += ", ";
						}
					}

					updateVariables();
				}

			//Game Over
				if (lives == 0) {
					entry.setVisible(false);
					submit.setVisible(false);
					buttonKeyboard.setVisible(false);
					keyboardWindow.setVisible(false);
					endPlayAgain.setVisible(true);
					endHome.setVisible(true);

					losses++;
					gameState = 2;
					updateVariables();
					progressWord.setText("Word to Guess: " + progressDone);
					guide.setForeground(Color.RED);
					guide.setText("Game over! You've used up all your lives.");
					JOptionPane.showMessageDialog(mainWindow, " You died! The word was \"" + selectedWord + "\".", "Message", JOptionPane.PLAIN_MESSAGE);
					menuStart.setText("New Game");
				}
			}

		//Runs if Selected Word is Complete
			if (Arrays.equals(guesses, wordToGuess)) {
				entry.setVisible(false);
				submit.setVisible(false);
				buttonKeyboard.setVisible(false);
				keyboardWindow.setVisible(false);
				endPlayAgain.setVisible(true);
				endHome.setVisible(true);

				wins++;
				gameState = 1;
				updateVariables();
				progressWord.setText("Word to Guess: " + progressDone);
				guide.setForeground(Color.GREEN);
				guide.setText("Well done, you get to live another day!");
				JOptionPane.showMessageDialog(mainWindow, "You got the word in " + att + " attempts with " + lives + " lives left. Congratulations!", "Message", JOptionPane.PLAIN_MESSAGE);
				menuStart.setText("New Game");
			}

		}

	}

//Vibrate Window
	public static void vibrate(JFrame mainWindow) {
        try {
            final int originalX = mainWindow.getLocationOnScreen().x;
            final int originalY = mainWindow.getLocationOnScreen().y;
            for (int i = 0; i < vibrationLength; i++) {
                Thread.sleep(10);
                mainWindow.setLocation(originalX, originalY + vibrationSpeed);
                Thread.sleep(10);
                mainWindow.setLocation(originalX, originalY - vibrationSpeed);
                Thread.sleep(10);
                mainWindow.setLocation(originalX + vibrationSpeed, originalY);
                Thread.sleep(10);
                mainWindow.setLocation(originalX, originalY);
            }
        } 
        
        catch (Exception err) {
            err.printStackTrace();
        }
   }

//Hangman Art
	public void printHangman() {
		switch (lives) {
			case 6:
				progressVisual.setIcon(new ImageIcon("Files/Images/HangmanArt/start.png"));
			break;

			case 5:
				progressVisual.setIcon(new ImageIcon("Files/Images/HangmanArt/1.png"));
			break;

			case 4:
				progressVisual.setIcon(new ImageIcon("Files/Images/HangmanArt/2.png"));
			break;

			case 3:
				progressVisual.setIcon(new ImageIcon("Files/Images/HangmanArt/3.png"));
			break;

			case 2:
				progressVisual.setIcon(new ImageIcon("Files/Images/HangmanArt/4.png"));
			break;

			case 1:
				progressVisual.setIcon(new ImageIcon("Files/Images/HangmanArt/5.png"));
			break;

			default:
				progressVisual.setIcon(new ImageIcon("Files/Images/HangmanArt/game_over.png"));
			break;
		}
	}

}



