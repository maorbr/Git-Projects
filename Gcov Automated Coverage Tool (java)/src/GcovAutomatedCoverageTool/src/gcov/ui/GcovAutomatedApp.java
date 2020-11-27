package gcov.ui;

import java.awt.EventQueue;
import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.border.TitledBorder;
import gcov.beans.GcovFile;
import gcov.engine.GcovCmdExecutor;
import gcov.engine.GcovCoverageMerger;
import gcov.engine.GcovDirectoryTool;
import gcov.utils.FileTolListConvertor;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JLabel;
import javax.swing.UIManager;
import java.awt.Color;
import javax.swing.JTextPane;
import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.Toolkit;
import java.awt.Button;
import java.awt.SystemColor;

@SuppressWarnings("serial")
public class GcovAutomatedApp extends JFrame {

	private static final String DEBUG_DIR_PATH = "\\CortexM3\\ARM_GCC_541\\Debug";
	private static final String MAIN_FUNC_PATH = "\\main.c";
	private static final String GCOV_MERGE_PATH = "\\GCOV_MERGE";
	private List<String> allSelectedFilesNames;
	private List<File> allSelectedGcnoFiles;
	private List<File> allSelectedGcdaFiles;
	private List<File> allGcdaFilesAfterFilter;
	private List<File> SourceFiles;
	private List<GcovFile> onlySelectedGcdaList;
	private List<GcovFile> gcovFilesList;
	private static int successCounter = 0;
	private static int faildCount = 0;
	private String projectVerison;
	private File projectFolder;
	private JFileChooser chooser;
	private JPanel panel;
	private JPanel contentPane;
	private JPanel panelSourceFile;
	private JPanel selectedGcnoPanel;
	private JPanel selectedGcdaPanel;
	private JList<String> leftList;
	private JList<String> rightList;
	private JList<String> listGcdaTable;
	private JButton btnLoadProject;
	private JButton btnMove;
	private JButton btnSelectAll;
	private JButton btnCreateGcda;
	private JButton btnExecute;
	private JScrollPane rightScrollPane;
	private JMenu mnReset;
	private JMenu mnHelp;
	private JMenuBar menuBar;
	private JMenuItem mntmExit;
	private JMenuItem mntmAbout;
	private JTextPane textPanePath;
	private JTextField textFieldComPort;
	private JTextField textFieldBaud;
	private JLabel lblComPort;
	private JLabel lblBaud;
	private JLabel lblProjectVersion;
	private JLabel lblVersionNumberLabel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GcovAutomatedApp frame = new GcovAutomatedApp();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public GcovAutomatedApp() {
		setResizable(false);
		setIconImage(Toolkit.getDefaultToolkit().getImage(GcovAutomatedApp.class.getResource("/gear.png")));
		setTitle("Gcov Automated Tool v1.8");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 780, 675);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setLocationRelativeTo(null);

		menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		mnReset = new JMenu("File");
		menuBar.add(mnReset);

		mntmExit = new JMenuItem("Exit");
		mntmExit.setIcon(new ImageIcon(
				GcovAutomatedApp.class.getResource("/javax/swing/plaf/metal/icons/ocean/minimize-pressed.gif")));
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(JFrame.EXIT_ON_CLOSE);
			}
		});
		mnReset.add(mntmExit);

		JMenu mnTools = new JMenu("Tool");
		menuBar.add(mnTools);

		JMenuItem mntmGcovCoverageRankMenuItem = new JMenuItem("Gcov Code Coverage Rank");
		mntmGcovCoverageRankMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GcovCoverageDialog dialog = new GcovCoverageDialog();
				dialog.setVisible(true);
				dialog.setLocationRelativeTo(null);
			}
		});
		mntmGcovCoverageRankMenuItem.setIcon(new ImageIcon(
				GcovAutomatedApp.class.getResource("/com/sun/java/swing/plaf/windows/icons/ListView.gif")));
		mnTools.add(mntmGcovCoverageRankMenuItem);

		mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);

		mntmAbout = new JMenuItem("About..");
		mntmAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AboutDialog dialog = new AboutDialog();
				dialog.setVisible(true);
				dialog.setLocationRelativeTo(null);
			}
		});
		mntmAbout.setIcon(
				new ImageIcon(GcovAutomatedApp.class.getResource("/javax/swing/plaf/metal/icons/ocean/menu.gif")));
		mnHelp.add(mntmAbout);
		setContentPane(contentPane);
		contentPane.setLayout(null);

		panelSourceFile = new JPanel();
		panelSourceFile.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Source Files:",
				TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panelSourceFile.setBounds(23, 68, 727, 428);
		contentPane.add(panelSourceFile);
		panelSourceFile.setLayout(null);

		JScrollPane leftScrollPane = new JScrollPane();
		leftScrollPane.setBounds(21, 28, 165, 380);
		panelSourceFile.add(leftScrollPane);

		leftList = new JList<String>();
		leftList.setSelectionModel(new DefaultListSelectionModel() {
			@Override
			public void setSelectionInterval(int index0, int index1) {
				if (super.isSelectedIndex(index0)) {
					super.removeSelectionInterval(index0, index1);
				} else {
					super.addSelectionInterval(index0, index1);
				}
			}
		});
		leftScrollPane.setViewportView(leftList);

		btnSelectAll = new JButton("Select All");
		leftScrollPane.setColumnHeaderView(btnSelectAll);
		btnSelectAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					List<String> allFiles = new ArrayList<>();

					for (int i = 0; i < leftList.getModel().getSize(); i++) {
						allFiles.add(leftList.getModel().getElementAt(i));
					}
					rightList.setListData(allFiles.toArray(new String[SourceFiles.size()]));

				} catch (Exception e) {
					JOptionPane.showMessageDialog(contentPane, "No project is loaded,\nBrowse for project first.",
							"Loading Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		selectedGcnoPanel = new JPanel();
		selectedGcnoPanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"),
				"Selected Source Files:", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		selectedGcnoPanel.setBounds(247, 28, 177, 380);
		panelSourceFile.add(selectedGcnoPanel);
		selectedGcnoPanel.setLayout(null);

		rightScrollPane = new JScrollPane();
		rightScrollPane.setBounds(6, 16, 165, 356);
		selectedGcnoPanel.add(rightScrollPane);

		rightList = new JList<String>();
		rightScrollPane.setViewportView(rightList);

		JButton btnClearAll = new JButton("Clear All");
		btnClearAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DefaultListModel<String> model = new DefaultListModel<String>();
				model.clear();
				rightList.setModel(model);
			}
		});
		rightScrollPane.setColumnHeaderView(btnClearAll);

		btnMove = new JButton("");
		btnMove.setIcon(new ImageIcon(GcovAutomatedApp.class.getResource("/arrow.png")));
		btnMove.setBounds(189, 174, 56, 120);
		panelSourceFile.add(btnMove);

		selectedGcdaPanel = new JPanel();
		selectedGcdaPanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"),
				"Selected .gcda Files:", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		selectedGcdaPanel.setBounds(430, 28, 278, 380);
		panelSourceFile.add(selectedGcdaPanel);
		selectedGcdaPanel.setLayout(null);

		JScrollPane scrollPaneGcda = new JScrollPane();
		scrollPaneGcda.setBounds(6, 16, 266, 356);
		selectedGcdaPanel.add(scrollPaneGcda);

		listGcdaTable = new JList<String>();
		listGcdaTable.setForeground(new Color(0, 0, 128));
		listGcdaTable.setSelectionModel(new DefaultListSelectionModel() {
			@Override
			public void setSelectionInterval(int index0, int index1) {
				if (super.isSelectedIndex(index0)) {
					super.removeSelectionInterval(index0, index1);
				} else {
					super.addSelectionInterval(index0, index1);
				}
			}
		});
		scrollPaneGcda.setViewportView(listGcdaTable);

		JButton btnRefresh = new JButton("");
		scrollPaneGcda.setColumnHeaderView(btnRefresh);
		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					initializePaths();
					fetchNamesToGcdaList(listGcdaTable, allGcdaFilesAfterFilter);
				} catch (Exception e2) {
					// TODO: handle exception
				}
			}
		});
		btnRefresh.setIcon(new ImageIcon(GcovAutomatedApp.class.getResource("/refresh.png")));

		JLabel lblGcdaFiles = new JLabel("");
		lblGcdaFiles.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblGcdaFiles.setForeground(new Color(0, 0, 255));
		lblGcdaFiles.setBounds(434, 33, 167, 14);
		panelSourceFile.add(lblGcdaFiles);
		btnMove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					if (leftList.getSelectedValue() != null) {
						rightList.setListData(leftList.getSelectedValuesList().toArray(new String[SourceFiles.size()]));
					}
				} catch (Exception e) {
					JOptionPane.showMessageDialog(contentPane, "No project is loaded,\nBrowse for project first.",
							"Loading Error", JOptionPane.ERROR_MESSAGE);
				}

			}
		});

		btnLoadProject = new JButton("");
		btnLoadProject.setIcon(new ImageIcon(GcovAutomatedApp.class.getResource("/preview.png")));
		btnLoadProject.setBounds(635, 25, 115, 26);
		contentPane.add(btnLoadProject);

		JLabel lblProject = new JLabel("Project Folder:");
		lblProject.setForeground(new Color(75, 0, 130));
		lblProject.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblProject.setBounds(23, 4, 178, 14);
		contentPane.add(lblProject);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(23, 25, 602, 26);
		contentPane.add(scrollPane);

		textPanePath = new JTextPane();
		textPanePath.setEditable(false);
		textPanePath.setBackground(new Color(220, 220, 220));
		textPanePath.setFont(new Font("Arial", Font.PLAIN, 12));
		textPanePath.setForeground(new Color(0, 128, 0));
		scrollPane.setViewportView(textPanePath);

		panel = new JPanel();
		panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Configuration",
				TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel.setBounds(23, 510, 260, 94);
		contentPane.add(panel);
		panel.setLayout(null);

		lblComPort = new JLabel("COM Port:");
		lblComPort.setBounds(6, 16, 69, 20);
		panel.add(lblComPort);

		lblBaud = new JLabel("Baud: ");
		lblBaud.setBounds(7, 41, 46, 20);
		panel.add(lblBaud);

		textFieldComPort = new JTextField();
		textFieldComPort.setBounds(72, 16, 37, 20);
		panel.add(textFieldComPort);
		textFieldComPort.setText("8");
		textFieldComPort.setHorizontalAlignment(SwingConstants.CENTER);
		textFieldComPort.setColumns(10);

		textFieldBaud = new JTextField();
		textFieldBaud.setBounds(72, 41, 178, 20);
		panel.add(textFieldBaud);
		textFieldBaud.setHorizontalAlignment(SwingConstants.CENTER);
		textFieldBaud.setText("115200");
		textFieldBaud.setColumns(10);

		Button buttonDM = new Button("Device Manager");
		buttonDM.setForeground(new Color(165, 42, 42));
		buttonDM.setFont(new Font("Dialog", Font.BOLD, 12));
		buttonDM.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GcovCmdExecutor.openDeviceManager();
			}
		});
		buttonDM.setBounds(115, 16, 135, 20);
		panel.add(buttonDM);

		lblProjectVersion = new JLabel("Project Version:");
		lblProjectVersion.setHorizontalAlignment(SwingConstants.LEFT);
		lblProjectVersion.setBounds(6, 66, 96, 19);
		panel.add(lblProjectVersion);

		lblVersionNumberLabel = new JLabel("");
		lblVersionNumberLabel.setHorizontalAlignment(SwingConstants.LEFT);
		lblVersionNumberLabel.setForeground(SystemColor.controlDkShadow);
		lblVersionNumberLabel.setBounds(108, 68, 88, 14);
		panel.add(lblVersionNumberLabel);

		btnExecute = new JButton("Execute");
		btnExecute.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnExecute.setForeground(new Color(34, 139, 34));
		btnExecute.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					if (SourceFiles != null && textFieldComPort.getText().chars().allMatch(Character::isDigit)
							&& !textFieldComPort.getText().isEmpty()
							&& textFieldBaud.getText().chars().allMatch(Character::isDigit)
							&& !textFieldBaud.getText().isEmpty() && listGcdaTable.getSelectedValue() != null) {
						do {
							fetchGcdaToList();
							initializeGcovFilesList();
						} while (checkIfThereIsDupSelection());

						initializeOnlySelectedGcdaList();

						for (GcovFile gfile : onlySelectedGcdaList) {
							if (!(gfile.getSourcePath().isEmpty()) && !(gfile.getGcnoPath().isEmpty())
									&& !(gfile.getGcdaPath().isEmpty())) {
								GcovCmdExecutor.createReport("\"" + gfile.getSourcePath() + "\"",
										"\"" + gfile.getGcnoPath() + "\"", "\"" + gfile.getGcdaPath() + "\"");
								File file = new File(projectFolder.getAbsolutePath() + DEBUG_DIR_PATH + "\\"
										+ gfile.getFileName() + ".gcov");
								if (file.exists()) {
									successCounter++;
								} else {
									JOptionPane.showMessageDialog(contentPane,
											"Error: gcovREPORT.exe faild for \"" + gfile.getFileName()
													+ ".gcov\" and file was not created.",
											"Gcov Report Error", JOptionPane.ERROR_MESSAGE);
								}
							}
						}

						Path pathLastGcov = GcovDirectoryTool.createNewDirectoryAndCopyGcovFilesToIt(
								allSelectedGcdaFiles, projectFolder.getAbsolutePath() + DEBUG_DIR_PATH);

						// Hybrid mode query
						boolean isHybridMode = false;
						int returnValue = 0;
						returnValue = JOptionPane.showConfirmDialog(null, "Are you running in hybrid mode?",
								"Are you sure?", JOptionPane.YES_NO_OPTION);
						if (returnValue == JOptionPane.YES_OPTION) {
							isHybridMode = true;
						} else if (returnValue == JOptionPane.NO_OPTION) {
							isHybridMode = false;
						}

						if (!GcovCoverageMerger.GcovFileMerger(
								projectFolder.getAbsolutePath() + DEBUG_DIR_PATH + GCOV_MERGE_PATH,
								projectFolder.getAbsolutePath() + DEBUG_DIR_PATH + "\\" + pathLastGcov.getFileName(),
								allSelectedGcdaFiles, projectVerison, isHybridMode)) {
							JOptionPane.showMessageDialog(contentPane, "The last generated report file Folder: "
									+ pathLastGcov.getFileName()
									+ " has a code line count which does not match merged coverage file/history counter or main validity check was faild.\nFor creating a new code merge file, first delete the old merge file.",
									"Merge operation fail!", JOptionPane.ERROR_MESSAGE);
						}
					} else {
						JOptionPane.showMessageDialog(contentPane,
								"1. Check if project is loaded.\n2. Check if COM port is valid.\n3. Check if Brud is valid.\n",
								"Execute Error", JOptionPane.ERROR_MESSAGE);
					}
				} catch (IOException e) {
					JOptionPane.showMessageDialog(contentPane, "Some file not found or input of invalid path.",
							"Execute Error", JOptionPane.ERROR_MESSAGE);
				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(contentPane, "Number Format Exception", "Execute Error",
							JOptionPane.ERROR_MESSAGE);
				} catch (Exception e) {
					System.out.println(e.getMessage() + " : " + e.getStackTrace());
				} finally {
					faildCount = onlySelectedGcdaList.size() - successCounter;
					JOptionPane.showMessageDialog(contentPane,
							"Completed: " + successCounter + " gcov files were created.\nFailed: " + faildCount
									+ " gcov files were not created.",
							"Completed Message", JOptionPane.INFORMATION_MESSAGE);

					faildCount = 0;
					successCounter = 0;
					gcovFilesList.clear();
				}

			}
		});
		btnExecute.setIcon(new ImageIcon(GcovAutomatedApp.class.getResource("/iconfinder.png")));
		btnExecute.setBounds(515, 524, 235, 68);
		contentPane.add(btnExecute);

		btnCreateGcda = new JButton("Create GCDA Files");
		btnCreateGcda.setIcon(
				new ImageIcon(GcovAutomatedApp.class.getResource("/javax/swing/plaf/metal/icons/ocean/file.gif")));
		btnCreateGcda.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (SourceFiles != null && textFieldComPort.getText().chars().allMatch(Character::isDigit)
						&& !textFieldComPort.getText().isEmpty()
						&& textFieldBaud.getText().chars().allMatch(Character::isDigit)
						&& !textFieldBaud.getText().isEmpty()) {

					GcovCmdExecutor.createGcdaFiles(textFieldComPort.getText(), textFieldBaud.getText());
				} else {
					JOptionPane.showMessageDialog(contentPane,
							"1. Check if project is loaded.\n2. Check if COM port is valid.\n3. Check if Brud is valid.\n",
							"Execute Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnCreateGcda.setBounds(313, 524, 178, 68);
		contentPane.add(btnCreateGcda);

		btnLoadProject.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				projectFolder = getSourceFolderPath();
				if (!projectFolder.getAbsolutePath().contains(" ")) {
					try {
						readAllFilesToList(projectFolder.getAbsolutePath() + "//Src");
						fetchNamesToSourceList(leftList, SourceFiles);
						textPanePath.setText(projectFolder.getAbsolutePath());
						textPanePath.setForeground(new Color(0, 128, 0));
						projectVerison = getProjectVersion();
						lblVersionNumberLabel.setText(getProjectVersion());
					} catch (Exception e) {

					}
				} else {
					JOptionPane.showMessageDialog(contentPane,
							"Path:" + projectFolder.getAbsolutePath() + " is invalid!, path cannot contain spaces.",
							"Invalid Path", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}

	private void fetchNamesToSourceList(JList<String> jlist, List<File> list) {

		List<String> fileNames = new ArrayList<>();

		for (File file : list) {
			fileNames.add(file.getName());
		}

		DefaultListModel<String> dlm = new DefaultListModel<>();

		Collections.sort(fileNames, String.CASE_INSENSITIVE_ORDER);

		for (String entity : fileNames) {
			dlm.addElement(entity.substring(0, entity.length() - 2));
		}
		jlist.setModel(dlm);
	}

	private void readAllFilesToList(String path) {
		SourceFiles = new ArrayList<File>();

		GcovDirectoryTool.walkForSourceFile(path, SourceFiles);
		SourceFiles.add(new File(projectFolder.getAbsolutePath() + MAIN_FUNC_PATH));
	}

	private File getSourceFolderPath() {

		File path = null;
		String userDir = System.getProperty("user.home");
		chooser = new JFileChooser(userDir + "\\Desktop");

		chooser.setDialogTitle("Browse For Project Folder");
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		chooser.setAcceptAllFileFilterUsed(false);
		//
		if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			path = chooser.getCurrentDirectory();
			path = chooser.getSelectedFile();
		} else {
			System.out.println("No Selection ");
		}
		return path;
	}

	private void initializePaths() {
		allSelectedFilesNames = new ArrayList<>();
		gcovFilesList = new ArrayList<>();
		for (int i = 0; i < rightList.getModel().getSize(); i++) {
			allSelectedFilesNames.add(rightList.getModel().getElementAt(i));
		}

		String debugFolderPath = projectFolder.getAbsolutePath() + DEBUG_DIR_PATH;

		List<File> allGcnoFiles = new ArrayList<>();
		GcovDirectoryTool.walkForGcnoFile(debugFolderPath, allGcnoFiles);

		allSelectedGcnoFiles = new ArrayList<>();

		for (File fileGcno : allGcnoFiles) {
			for (String fileName : allSelectedFilesNames) {
				if (fileName != null) {
					if (fileGcno.getName().substring(0, fileGcno.getName().length() - 5).equals(fileName)) {
						allSelectedGcnoFiles.add(fileGcno);
					}
				}
			}
		}

		List<File> allGcdaFiles = new ArrayList<>();
		GcovDirectoryTool.walkForGcdaFile(debugFolderPath, allGcdaFiles);

		allGcdaFilesAfterFilter = new ArrayList<>();

		for (File fileGcda : allGcdaFiles) {
			for (String fileName : allSelectedFilesNames) {
				if (fileName != null) {
					if (GcovDirectoryTool.gcdaFileRename(fileGcda.getName()).equals(fileName)) {
						allGcdaFilesAfterFilter.add(fileGcda);
					}
				}
			}
		}
	}

	private String getGcnoPath(String fileName, List<File> list) {
		String path = "";
		for (File file : list) {
			if (file.getName().substring(0, file.getName().length() - 5).equals(fileName)) {
				path = file.getAbsolutePath();
				break;
			}
		}
		return path;
	}

	private String getGcdaPath(String fileName, List<File> list) {
		String path = "";
		for (File file : list) {
			if (file.getName().contains(fileName)) {
				path = file.getAbsolutePath();
			}
		}
		return path;
	}

	private void initializeGcovFilesList() {
		for (File file : SourceFiles) {
			for (String fileName : allSelectedFilesNames) {
				if (file.getName().substring(0, file.getName().length() - 2).equals(fileName)) {
					gcovFilesList.add(new GcovFile(fileName, file.getAbsolutePath(),
							getGcnoPath(fileName, allSelectedGcnoFiles), getGcdaPath(fileName, allSelectedGcdaFiles)));
				}
			}
		}
	}

	private void fetchGcdaToList() {

		List<String> allSelectedGcdaFilesString = new ArrayList<>();

		if (listGcdaTable.getSelectedValue() != null) {
			allSelectedGcdaFilesString = listGcdaTable.getSelectedValuesList();
		}
		Collections.sort(allSelectedGcdaFilesString, String.CASE_INSENSITIVE_ORDER);

		allSelectedGcdaFiles = new ArrayList<>();

		for (File file : allGcdaFilesAfterFilter) {
			for (String fileName : allSelectedGcdaFilesString) {
				if (file.getName().equals(fileName)) {
					allSelectedGcdaFiles.add(file);
				}
			}
		}
	}

	private void fetchNamesToGcdaList(JList<String> jlist, List<File> list) {

		HashSet<String> fileNames = new HashSet<>();

		for (File file : list) {
			fileNames.add(file.getName());
		}

		DefaultListModel<String> dlm = new DefaultListModel<>();

		List<String> fileNamesArray = new ArrayList<>(fileNames);

		Collections.sort(fileNamesArray, String.CASE_INSENSITIVE_ORDER);
		Collections.reverse(fileNamesArray);

		for (String entity : fileNamesArray) {
			dlm.addElement(entity.substring(0, entity.length()));
		}
		jlist.setModel(dlm);
	}

	private String getProjectVersion() {

		String majorVer = null;
		String mainorVer = null;
		try {
			List<String> verList = new ArrayList<>();

			if (!projectFolder.getAbsolutePath().contains("Hyb")) {
				verList = FileTolListConvertor.convetFileToList(projectFolder.getAbsolutePath() + "//Src//drvTypes.h");
			} else {
				String path = projectFolder.getAbsolutePath().substring(0, projectFolder.getAbsolutePath().length() - 9)
						+ ".cydsn";
				verList = FileTolListConvertor.convetFileToList(path + "//Src//drvTypes.h");
			}

			for (String line : verList) {
				if (line.contains("SMARTSIM_MAJOR_VER")) {
					majorVer = line.substring(28, 29);
				}
				if (line.contains("SMARTSIM_MAINOR_VER") && isNumeric(line.substring(29, 30))) {
					mainorVer = line.substring(28, 30);
				}
				if (line.contains("SMARTSIM_MAINOR_VER") && !isNumeric(line.substring(29, 30))) {
					mainorVer = line.substring(28, 29);
				}
				if (line.contains("LG_SIM_MAJOR_VER")) {
					majorVer = line.substring(26, 27);
				}
				if (line.contains("LG_SIM_MAINOR_VER") && isNumeric(line.substring(27, 28))) {
					mainorVer = line.substring(26, 28);
				}
				if (line.contains("LG_SIM_MAINOR_VER") && !isNumeric(line.substring(27, 28))) {
					mainorVer = line.substring(26, 27);
				}
				if (line.contains("SPDU_MAJOR_VER")) {
					majorVer = line.substring(24, 25);
				}
				if (line.contains("SPDU_MAINOR_VER") && isNumeric(line.substring(25, 26))) {
					mainorVer = line.substring(24, 26);
				}
				if (line.contains("SPDU_MAINOR_VER") && !isNumeric(line.substring(25, 26))) {
					mainorVer = line.substring(24, 25);
				}
			}

		} catch (IOException e) {
			JOptionPane.showMessageDialog(contentPane,
					"Can't Load Project Version \"drvTypes.h\" File not found in project folder.\n", "Execute Error",
					JOptionPane.ERROR_MESSAGE);
		}
		return majorVer + "." + mainorVer;
	}

	private static boolean isNumeric(String str) {
		for (char c : str.toCharArray()) {
			if (!Character.isDigit(c))
				return false;
		}
		return true;
	}

	private boolean checkIfThereIsDupSelection() {
		List<String> allSelectedGcdaFilesString = new ArrayList<>();
		List<String> nonDupList = new ArrayList<>();
		boolean dupFlag = false;

		if (listGcdaTable.getSelectedValue() != null) {
			allSelectedGcdaFilesString = listGcdaTable.getSelectedValuesList();
		}

		for (String fileName : allSelectedGcdaFilesString) {
			String name = GcovDirectoryTool.gcdaFileRename(fileName);
			if (!nonDupList.contains(name)) {
				nonDupList.add(GcovDirectoryTool.gcdaFileRename(name));
			} else {
				JOptionPane.showMessageDialog(contentPane, "Please for each source name, select only one gcda! ",
						"Duplicate Gcna File Selection", JOptionPane.INFORMATION_MESSAGE);
				listGcdaTable.clearSelection();
				dupFlag = true;
				break;
			}
		}
		return dupFlag;
	}

	private void initializeOnlySelectedGcdaList() {

		onlySelectedGcdaList = new ArrayList<>();

		for (GcovFile gfile : gcovFilesList) {
			for (String name : listGcdaTable.getSelectedValuesList()) {
				if (gfile.getFileName().equals(GcovDirectoryTool.gcdaFileRename(name))) {
					onlySelectedGcdaList.add(gfile);
				}
			}
		}
	}
}
