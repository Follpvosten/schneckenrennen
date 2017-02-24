package schneckenrennen;

import java.awt.Desktop;
import java.awt.Font;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.event.HyperlinkEvent;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.AbstractTableModel;

/**
 * The currently used main window for this application. Visually shows the
 * racing snails.
 *
 * @author Follpvosten
 */
public class RaceFrame extends javax.swing.JFrame {

    /**
     * Application-wide {@link java.util.Random} object used to generate all the
     * random numbers used in this simulation.
     */
    public static Random Random;

    /**
     * The list of progress bars used for displaying the progress the racing
     * snails have made. Currently, the number of progress bars in this array
     * determines how many snails are racing as well.
     */
    private final JProgressBar[] progressBars;

    /**
     * Thread used to update the racing progress separated from the UI Thread.
     */
    private Thread snailUpdateThread;
    /**
     * Table model used to display the currently participating snails.
     */
    private SnailTableModel tableModel;

    /**
     * The current race the snails are participating in.
     */
    private Rennen currentRace;
    /**
     * The Wettbuero. Is planned to be made exchangable (and maybe also into a
     * list) in a future version.
     */
    private final Wettbuero wettbuero;

    /**
     * Entry point for the actual application. Loads config files, initializes
     * the needed values and sets up the first race.
     */
    public RaceFrame() {
	TranslationManager.loadTranslations();
	ConfigManager.loadDefaultConfigFile();
	Random = new Random();
	initComponents();
	progressBars = new JProgressBar[]{
	    snail1Progress,
	    snail2Progress,
	    snail3Progress,
	    snail4Progress
	};
	// Initialize the Wettbüro
	wettbuero = new Wettbuero(ConfigManager.getBetFactor());
	// Create new race
	setupRace();
    }

    /**
     * Sets up a new snail race, generating a random goal between 100 and 400,
     * selecting a random title and generating random snails.
     */
    private void setupRace() {
	int newGoal = Random.nextInt(400) + 100;
	currentRace
		= new Rennen(
			ConfigManager.getRandomRaceName(Random),
			newGoal
		);
	this.setTitle(TranslationManager.getTranslation("info.raceInfo", currentRace.getName(), newGoal));
	for (JProgressBar progressBar : progressBars) {
	    progressBar.setMaximum(newGoal);
	}

	generateSnails(progressBars.length);
	displaySnails();

	wettbuero.assignNewRennen(currentRace);
    }

    /**
     * Generates a given number of snails and adds them to the current race.
     *
     * @param number The number of snails to be generated.
     */
    private void generateSnails(int number) {
	ArrayList<Integer> usedNameIndices = new ArrayList<>();
	for (int i = 0; i < number; i++) {
	    int nameIndex;
	    do {
		nameIndex = Random.nextInt(ConfigManager.getSnailNames().size());
	    } while (usedNameIndices.contains(nameIndex));
	    usedNameIndices.add(nameIndex);
	    currentRace.addRennschnecke(
		    new Rennschnecke(
			    Random.nextInt(3) + 2,
			    ConfigManager.getSnailNames().get(nameIndex),
			    ConfigManager.getRandomSnailRace(Random)
		    ));
	}
	tableModel = new SnailTableModel(
		currentRace.getSchnecken(),
		TranslationManager.getTranslation("RaceFrame.snailTable.columnNames").split(";")
	);
	snailTable.setModel(tableModel);
    }

    /**
     * Displays the snails in the snail list and visualizes their made progress
     * using the progress bars.
     */
    private void displaySnails() {
	ArrayList<Rennschnecke> snails = currentRace.getSchnecken();
	tableModel.refresh();
	for (int i = 0; i < snails.size(); i++) {
	    progressBars[i].setValue(snails.get(i).getProgress());
	}
    }

    /**
     * The model used to display the currently participating snails in the
     * table meant for this purpose.
     */
    private class SnailTableModel extends AbstractTableModel {

	/**
	 * The snails to display.
	 */
	private final ArrayList<Rennschnecke> data;
	/**
	 * The names of the columns (to be translated soon).
	 */
	private final String[] columnNames;
	/**
	 * Tells the table content to change when the race is over.
	 */
	private boolean endState = false;

	public SnailTableModel(ArrayList<Rennschnecke> data, String[] columnNames) {
	    this.data = data;
	    this.columnNames = columnNames;
	}

	@Override
	public int getRowCount() {
	    return data.size();
	}

	@Override
	public int getColumnCount() {
	    // Display only 3 columns until the race has ended
	    return endState ? 4 : 3;
	}

	@Override
	public String getColumnName(int col) {
	    // Ignore column number 0 when there are only 3 to display
	    return endState ? columnNames[col] : columnNames[col + 1];
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
	    // I can't think of a better way yet
	    if (endState) {
		switch (columnIndex) {
		    case 0:
			return "" + (rowIndex + 1) + ".";
		    case 1:
			return data.get(rowIndex).getName();
		    case 2:
			return data.get(rowIndex).getRace();
		    case 3:
			return data.get(rowIndex).getProgress();
		    default:
			return null;
		}
	    } else {
		switch (columnIndex) {
		    case 0:
			return data.get(rowIndex).getName();
		    case 1:
			return data.get(rowIndex).getRace();
		    case 2:
			return data.get(rowIndex).getProgress();
		    default:
			return null;
		}
	    }
	}

	/**
	 * Refreshes only the progress the snails have made.
	 */
	public void refresh() {
	    for (int i = 0; i < data.size(); i++) {
		fireTableCellUpdated(i, 2);
	    }
	}

	/**
	 * Displays the final result properly
	 */
	public void end() {
	    endState = true;
	    this.fireTableStructureChanged();
	    snailTable.getColumnModel().getColumn(0).setMaxWidth(50);
	    snailTable.getColumnModel().getColumn(3).setMinWidth(90);
	    snailTable.getColumnModel().getColumn(3).setMaxWidth(120);
	    snailTable.sizeColumnsToFit(1);
	    //snailTable.sizeColumnsToFit(0);
	    snailTable.changeSelection(0, 0, false, false);
	}
    }

    /**
     * Stops the running race (if any) and sets up a new one.
     */
    private void resetRace() {
	if (currentRace.isRunning()) {
	    currentRace.stop();
	}
	setupRace();
	betButton.setEnabled(true);
	betMenuItem.setEnabled(true);
    }

    /**
     * Starts, pauses or continues the current race based on its current state.
     */
    private void toggleRace() {
	betButton.setEnabled(false);
	betMenuItem.setEnabled(false);
	if (!currentRace.isRunning() && !currentRace.hasEnded()) {
	    currentRace.start();
	    snailUpdateThread = new Thread() {
		@Override
		public void run() {
		    while (currentRace.isRunning()) {
			currentRace.progress();
			displaySnails();
			try {
			    Thread.sleep(50);
			} catch (InterruptedException ex) {
			    System.out.print("Interrupted: " + ex.getMessage());
			}
		    }
		    if (currentRace.hasEnded()) {
			java.awt.EventQueue.invokeLater(() -> {
			    tableModel.end();
			    JOptionPane.showMessageDialog(RaceFrame.this, wettbuero.generateOutcome());
			});
		    }
		}
	    };
	    snailUpdateThread.start();
	} else {
	    currentRace.stop();
	}
    }

    /**
     * Opens a dialog allowing the user(s) to place a bet.
     */
    private void popupBetDialog() {
	Wettbuero.Wette newWette
		= new WettDialog(
			this,
			currentRace.getSchnecken(),
			wettbuero.getFactor()
		).showDialog();
	if (newWette != null) {
	    wettbuero.placeBet(newWette);
	}
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        snailInfoPanel = new javax.swing.JPanel();
        resetButton = new javax.swing.JButton();
        startButton = new javax.swing.JButton();
        betButton = new javax.swing.JButton();
        snailTableScroll = new javax.swing.JScrollPane();
        snailTable = new javax.swing.JTable();
        snailRacePanel = new javax.swing.JPanel();
        snail1Progress = new javax.swing.JProgressBar();
        snail2Progress = new javax.swing.JProgressBar();
        snail3Progress = new javax.swing.JProgressBar();
        snail4Progress = new javax.swing.JProgressBar();
        mainMenuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        loadConfigMenuItem = new javax.swing.JMenuItem();
        exportMenu = new javax.swing.JMenu();
        quitMenuItem = new javax.swing.JMenuItem();
        raceMenu = new javax.swing.JMenu();
        resetMenuItem = new javax.swing.JMenuItem();
        startMenuItem = new javax.swing.JMenuItem();
        betMenuItem = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();
        aboutMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("schneckenrennen/AutoBundle"); // NOI18N
        setTitle(bundle.getString("RaceFrame.title")); // NOI18N
        setName("Form"); // NOI18N

        snailInfoPanel.setName("snailInfoPanel"); // NOI18N

        resetButton.setText(bundle.getString("RaceFrame.resetButton.text")); // NOI18N
        resetButton.setName("resetButton"); // NOI18N
        resetButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                resetClickedHandler(evt);
            }
        });

        startButton.setText(bundle.getString("RaceFrame.startButton.text")); // NOI18N
        startButton.setName("startButton"); // NOI18N
        startButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                startClickedHandler(evt);
            }
        });

        betButton.setText(bundle.getString("RaceFrame.betButton.text")); // NOI18N
        betButton.setName("betButton"); // NOI18N
        betButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                betButtonActionPerformed(evt);
            }
        });

        snailTableScroll.setName("snailTableScroll"); // NOI18N

        snailTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        snailTable.setEnabled(false);
        snailTable.setFocusable(false);
        snailTable.setName("snailTable"); // NOI18N
        snailTableScroll.setViewportView(snailTable);

        javax.swing.GroupLayout snailInfoPanelLayout = new javax.swing.GroupLayout(snailInfoPanel);
        snailInfoPanel.setLayout(snailInfoPanelLayout);
        snailInfoPanelLayout.setHorizontalGroup(
            snailInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(snailInfoPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(snailTableScroll)
                .addGap(18, 18, 18)
                .addGroup(snailInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(resetButton)
                    .addGroup(snailInfoPanelLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(snailInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(startButton)
                            .addComponent(betButton))))
                .addContainerGap())
        );
        snailInfoPanelLayout.setVerticalGroup(
            snailInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(snailInfoPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(snailInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(snailTableScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(snailInfoPanelLayout.createSequentialGroup()
                        .addComponent(resetButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(startButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(betButton)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        snailRacePanel.setName("snailRacePanel"); // NOI18N

        snail1Progress.setName("snail1Progress"); // NOI18N

        snail2Progress.setName("snail2Progress"); // NOI18N

        snail3Progress.setName("snail3Progress"); // NOI18N

        snail4Progress.setName("snail4Progress"); // NOI18N

        javax.swing.GroupLayout snailRacePanelLayout = new javax.swing.GroupLayout(snailRacePanel);
        snailRacePanel.setLayout(snailRacePanelLayout);
        snailRacePanelLayout.setHorizontalGroup(
            snailRacePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(snailRacePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(snailRacePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(snail1Progress, javax.swing.GroupLayout.DEFAULT_SIZE, 618, Short.MAX_VALUE)
                    .addComponent(snail2Progress, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(snail3Progress, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(snail4Progress, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        snailRacePanelLayout.setVerticalGroup(
            snailRacePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(snailRacePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(snail1Progress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(snail2Progress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(snail3Progress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(snail4Progress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        mainMenuBar.setName("mainMenuBar"); // NOI18N

        fileMenu.setText(bundle.getString("RaceFrame.fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        loadConfigMenuItem.setText(bundle.getString("RaceFrame.loadConfigMenuItem.text")); // NOI18N
        loadConfigMenuItem.setName("loadConfigMenuItem"); // NOI18N
        loadConfigMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadConfigMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(loadConfigMenuItem);

        exportMenu.setText(bundle.getString("RaceFrame.exportMenu.text")); // NOI18N
        exportMenu.setEnabled(false);
        exportMenu.setName("exportMenu"); // NOI18N
        fileMenu.add(exportMenu);

        quitMenuItem.setText(bundle.getString("RaceFrame.quitMenuItem.text")); // NOI18N
        quitMenuItem.setName("quitMenuItem"); // NOI18N
        quitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                quitMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(quitMenuItem);

        mainMenuBar.add(fileMenu);

        raceMenu.setText(bundle.getString("RaceFrame.raceMenu.text")); // NOI18N
        raceMenu.setMargin(new java.awt.Insets(5, 0, 5, 0));
        raceMenu.setName("raceMenu"); // NOI18N

        resetMenuItem.setText(bundle.getString("RaceFrame.resetMenuItem.text")); // NOI18N
        resetMenuItem.setName("resetMenuItem"); // NOI18N
        resetMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetMenuItemActionPerformed(evt);
            }
        });
        raceMenu.add(resetMenuItem);

        startMenuItem.setText(bundle.getString("RaceFrame.startMenuItem.text")); // NOI18N
        startMenuItem.setName("startMenuItem"); // NOI18N
        startMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startMenuItemActionPerformed(evt);
            }
        });
        raceMenu.add(startMenuItem);

        betMenuItem.setText(bundle.getString("RaceFrame.betMenuItem.text")); // NOI18N
        betMenuItem.setName("betMenuItem"); // NOI18N
        betMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                betMenuItemActionPerformed(evt);
            }
        });
        raceMenu.add(betMenuItem);

        mainMenuBar.add(raceMenu);

        helpMenu.setText(bundle.getString("RaceFrame.helpMenu.text")); // NOI18N
        helpMenu.setName("helpMenu"); // NOI18N

        aboutMenuItem.setText(bundle.getString("RaceFrame.aboutMenuItem.text")); // NOI18N
        aboutMenuItem.setName("aboutMenuItem"); // NOI18N
        aboutMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutMenuItemActionPerformed(evt);
            }
        });
        helpMenu.add(aboutMenuItem);

        mainMenuBar.add(helpMenu);

        setJMenuBar(mainMenuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(snailRacePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(snailInfoPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(snailInfoPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(snailRacePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void resetClickedHandler(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_resetClickedHandler
	resetRace();
    }//GEN-LAST:event_resetClickedHandler

    private void startClickedHandler(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_startClickedHandler
	toggleRace();
    }//GEN-LAST:event_startClickedHandler

    private void betButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_betButtonActionPerformed
	popupBetDialog();
    }//GEN-LAST:event_betButtonActionPerformed

    private void resetMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetMenuItemActionPerformed
	resetRace();
    }//GEN-LAST:event_resetMenuItemActionPerformed

    private void startMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startMenuItemActionPerformed
	toggleRace();
    }//GEN-LAST:event_startMenuItemActionPerformed

    private void betMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_betMenuItemActionPerformed
	popupBetDialog();
    }//GEN-LAST:event_betMenuItemActionPerformed

    private void aboutMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutMenuItemActionPerformed
	Font font = new JLabel().getFont();

	// Load HTML and set style to match the application's style
	String html
		= TranslationManager.getTranslation(
			"RaceFrame.help.about",
			font.getFamily(),
			(font.isBold() ? "bold" : "normal"),
			"" + font.getSize() + "pt");

	// Create HTML content pane
	JEditorPane ep = new JEditorPane("text/html", html);

	// Handle link clicks
	ep.addHyperlinkListener((HyperlinkEvent e) -> {
	    if (e.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED)) {
		try {
		    Desktop.getDesktop().browse(e.getURL().toURI());
		} catch (IOException | URISyntaxException ex) {
		    Logger.getLogger(RaceFrame.class.getName()).log(Level.SEVERE, null, ex);
		}
	    }
	});
	ep.setEditable(false);
	ep.setBackground(new JOptionPane().getBackground());

	// Show dialog
	JOptionPane.showMessageDialog(null, ep);
    }//GEN-LAST:event_aboutMenuItemActionPerformed

    private void quitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_quitMenuItemActionPerformed
	dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }//GEN-LAST:event_quitMenuItemActionPerformed

    private void loadConfigMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadConfigMenuItemActionPerformed
	final JFileChooser fc = new JFileChooser(".");
	fc.setMultiSelectionEnabled(false);
	fc.setFileFilter(new FileFilter() {
	    @Override
	    public boolean accept(File f) {
		return f.getName().endsWith(".json");
	    }

	    @Override
	    public String getDescription() {
		return TranslationManager.getTranslation("Config.configFileType");
	    }

	});

	int result = fc.showOpenDialog(this);
	if (result == JFileChooser.APPROVE_OPTION) {
	    String loadResult = ConfigManager.loadSpecificConfigFile(fc.getSelectedFile().getAbsolutePath());
	    if (!loadResult.isEmpty()) {
		JOptionPane.showMessageDialog(this, loadResult);
	    }
	    resetRace();
	}
    }//GEN-LAST:event_loadConfigMenuItemActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
	/* Set the look and feel */
	//<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
	/* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
	 */
	try {
	    for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
		if ("GTK+".equals(info.getName())) {
		    javax.swing.UIManager.setLookAndFeel(info.getClassName());
		    break;
		}
		if (info.getName().contains("Windows")) {
		    javax.swing.UIManager.setLookAndFeel(info.getClassName());
		    break;
		}
	    }
	    /*LookAndFeelInfo[] infos = UIManager.getInstalledLookAndFeels();
            int i = new Random().nextInt(infos.length);
            UIManager.setLookAndFeel(infos[i].getClassName());
            System.out.println(infos[i].getName());*/
	} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
	    java.util.logging.Logger.getLogger(RaceFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	}
	//</editor-fold>

	//</editor-fold>

	/* Create and display the form */
	java.awt.EventQueue.invokeLater(() -> {
	    new RaceFrame().setVisible(true);
	});
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem aboutMenuItem;
    private javax.swing.JButton betButton;
    private javax.swing.JMenuItem betMenuItem;
    private javax.swing.JMenu exportMenu;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JMenuItem loadConfigMenuItem;
    private javax.swing.JMenuBar mainMenuBar;
    private javax.swing.JMenuItem quitMenuItem;
    private javax.swing.JMenu raceMenu;
    private javax.swing.JButton resetButton;
    private javax.swing.JMenuItem resetMenuItem;
    private javax.swing.JProgressBar snail1Progress;
    private javax.swing.JProgressBar snail2Progress;
    private javax.swing.JProgressBar snail3Progress;
    private javax.swing.JProgressBar snail4Progress;
    private javax.swing.JPanel snailInfoPanel;
    private javax.swing.JPanel snailRacePanel;
    private javax.swing.JTable snailTable;
    private javax.swing.JScrollPane snailTableScroll;
    private javax.swing.JButton startButton;
    private javax.swing.JMenuItem startMenuItem;
    // End of variables declaration//GEN-END:variables
}
