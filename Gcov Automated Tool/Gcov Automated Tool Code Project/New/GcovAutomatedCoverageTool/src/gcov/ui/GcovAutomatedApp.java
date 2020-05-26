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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.border.TitledBorder;
import gcov.beans.GcovFile;
import gcov.utils.CmdExecutor;
import gcov.utils.DirectoryUtils;
import gcov.utils.GcovUtils;

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

@SuppressWarnings("serial")
public class GcovAutomatedApp extends JFrame {

	private static final String  DEBUG_DIR_PATH = "\\CortexM3\\ARM_GCC_541\\Debug";
	private static final String MAIN_FUNC_PATH = "\\main.c";
	private static final String GCOV_MERGE_PATH = "\\GCOV_MERGE";
	private File projectFolder;
	private JPanel contentPane;
	private JList<String> leftList;
	private JList<String> rightList;
	private JButton btnLoadProject;
	private JButton btnMove;
	private JScrollPane rightScrollPane;
	private List<File> SourceFiles;
	private JPanel panelSourceFile;
	private JMenuBar menuBar;
	private JMenu mnReset;
	private JMenu mnHelp;
	private JMenuItem mntmExit;
	private JFileChooser chooser;
	private JTextPane textPanePath;
	private JLabel lblComPort;
	private JLabel lblBaud; 
	private JTextField textFieldComPort;
	private JTextField textFieldBaud;
	private JButton btnExecute;
	private JMenuItem mntmAbout;
	private JButton btnSelectAll;
	private JButton btnCreateGcda;
	private List<GcovFile> gcovFilesList;
	private JPanel panel;
	private JList<String> listGcda;
	private List<String> allSelectedFilesNames;
	private List<File> allSelectedGcnoFiles;
	private List<File> allSelectedGcdaFiles;
	private List<File> allGcdaFilesNeeded;
	private JPanel selectedGcnoPanel;
	private JPanel selectedGcdaPanel;

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
		setTitle("Gcov Automated Tool 1.2");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 777, 864);
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
		mntmGcovCoverageRankMenuItem.setIcon(new ImageIcon(GcovAutomatedApp.class.getResource("/com/sun/java/swing/plaf/windows/icons/ListView.gif")));
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
		panelSourceFile.setBounds(23, 87, 727, 621);
		contentPane.add(panelSourceFile);
		panelSourceFile.setLayout(null);

		JScrollPane leftScrollPane = new JScrollPane();
		leftScrollPane.setBounds(21, 28, 165, 570);
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
		selectedGcnoPanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Selected Source Files:", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		selectedGcnoPanel.setBounds(247, 28, 177, 570);
		panelSourceFile.add(selectedGcnoPanel);
		selectedGcnoPanel.setLayout(null);

		rightScrollPane = new JScrollPane();
		rightScrollPane.setBounds(6, 16, 165, 547);
		selectedGcnoPanel.add(rightScrollPane);

		rightList = new JList<String>();
		rightScrollPane.setViewportView(rightList);

		btnMove = new JButton("");
		btnMove.setIcon(new ImageIcon(GcovAutomatedApp.class.getResource("/arrow.png")));
		btnMove.setBounds(189, 260, 56, 120);
		panelSourceFile.add(btnMove);
		
		selectedGcdaPanel = new JPanel();
		selectedGcdaPanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Selected .gcda Files:", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		selectedGcdaPanel.setBounds(430, 28, 278, 570);
		panelSourceFile.add(selectedGcdaPanel);
		selectedGcdaPanel.setLayout(null);

		JScrollPane scrollPaneGcda = new JScrollPane();
		scrollPaneGcda.setBounds(6, 16, 266, 546);
		selectedGcdaPanel.add(scrollPaneGcda);

		listGcda = new JList<String>();
		listGcda.setSelectionModel(new DefaultListSelectionModel() {
			@Override
			public void setSelectionInterval(int index0, int index1) {
				if (super.isSelectedIndex(index0)) {
					super.removeSelectionInterval(index0, index1);
				} else {
					super.addSelectionInterval(index0, index1);
				}
			}
		});
		scrollPaneGcda.setViewportView(listGcda);
		
		JButton btnRefresh = new JButton("");
		scrollPaneGcda.setColumnHeaderView(btnRefresh);
		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					initializePaths();
					fetchNamesToGcnaList(listGcda, allGcdaFilesNeeded);	
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
		btnLoadProject.setBounds(635, 29, 115, 50);
		contentPane.add(btnLoadProject);

		JLabel lblProject = new JLabel("Project Folder:");
		lblProject.setForeground(new Color(75, 0, 130));
		lblProject.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblProject.setBounds(23, 11, 178, 14);
		contentPane.add(lblProject);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(23, 29, 602, 50);
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
		panel.setBounds(23, 719, 260, 68);
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
		textFieldComPort.setText("5");
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
				CmdExecutor.openDeviceManager();	
			}
		});
		buttonDM.setBounds(115, 16, 135, 20);
		panel.add(buttonDM);

		btnExecute = new JButton("Execute");
		btnExecute.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnExecute.setForeground(new Color(34, 139, 34));
		btnExecute.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					int count = 0;
					if (SourceFiles != null && textFieldComPort.getText().chars().allMatch(Character::isDigit)
							&& !textFieldComPort.getText().isEmpty()
							&& textFieldBaud.getText().chars().allMatch(Character::isDigit)
							&& !textFieldBaud.getText().isEmpty() && listGcda.getSelectedValue() != null) {
						fetchGcdaToList();
						initializeGcovFilesList();

						for (GcovFile gfile : gcovFilesList) {

							if (!(gfile.getSourcePath().isEmpty()) && !(gfile.getGcnoPath().isEmpty())
									&& !(gfile.getGcnaPath().isEmpty())) {
								count++;
								CmdExecutor.createReport("\"" + gfile.getSourcePath() + "\"", "\"" + gfile.getGcnoPath() + "\"","\"" +
										gfile.getGcnaPath() + "\"");
							}
						}
						Path pathLastGcov = DirectoryUtils.createNewDirectoryAndCopyGcovFilesToIt(projectFolder.getAbsolutePath() + DEBUG_DIR_PATH);
						
						Path pathMergeGcov = Paths.get(projectFolder.getAbsolutePath() + DEBUG_DIR_PATH + GCOV_MERGE_PATH);
						if (!Files.exists(pathMergeGcov)) {
							DirectoryUtils.createNewGcovMergerDirectoryIfNotExisted(projectFolder.getAbsolutePath() + DEBUG_DIR_PATH + GCOV_MERGE_PATH, projectFolder.getAbsolutePath() + DEBUG_DIR_PATH + "\\" + pathLastGcov.getFileName());
						}else {
							GcovUtils.GcovFileMerger(projectFolder.getAbsolutePath() + DEBUG_DIR_PATH + GCOV_MERGE_PATH, projectFolder.getAbsolutePath() + DEBUG_DIR_PATH + "\\" + pathLastGcov.getFileName(), allSelectedFilesNames);
						}

						int faildCount = gcovFilesList.size() - count;
						JOptionPane.showMessageDialog(contentPane,
								"Completed: " + count + " gcov files were created.\nFailed: " + faildCount
										+ " gcov files were not created.",
								"Completed Message", JOptionPane.INFORMATION_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(contentPane,
								"1. Check if project is loaded.\n2. Check if COM port is valid.\n3. Check if Brud is valid.\n",
								"Execute Error", JOptionPane.ERROR_MESSAGE);
					}
				} catch (Exception e) {
					JOptionPane.showMessageDialog(contentPane, "Check if files existed on correct path.\n",
							"Execute Error", JOptionPane.ERROR_MESSAGE);
				}

			}
		});
		btnExecute.setIcon(new ImageIcon(GcovAutomatedApp.class.getResource("/iconfinder.png")));
		btnExecute.setBounds(515, 719, 235, 68);
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

					CmdExecutor.createGcdaFiles(textFieldComPort.getText(), textFieldBaud.getText());
				} else {
					JOptionPane.showMessageDialog(contentPane,
							"1. Check if project is loaded.\n2. Check if COM port is valid.\n3. Check if Brud is valid.\n",
							"Execute Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnCreateGcda.setBounds(308, 719, 178, 68);
		contentPane.add(btnCreateGcda);
		
		btnLoadProject.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					projectFolder = getSourceFolderPath();
					readAllFilesToList(projectFolder.getAbsolutePath() + "//Src");
					fetchNamesToSourceList(leftList, SourceFiles);
					textPanePath.setText(projectFolder.getAbsolutePath());
					textPanePath.setForeground(new Color(0, 128, 0));
				} catch (Exception e) {
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

		DirectoryUtils.walkForSourceFile(path, SourceFiles);
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
		allSelectedGcnoFiles = new ArrayList<>();
		DirectoryUtils.walkForGcnoFile(debugFolderPath, allSelectedGcnoFiles);

		List<File> allGcdaFiles = new ArrayList<>();
		DirectoryUtils.walkForGcnaFile(debugFolderPath, allGcdaFiles);

		allGcdaFilesNeeded = new ArrayList<>();

		for (File fileGcda : allGcdaFiles) {
			for (String fileName : allSelectedFilesNames) {
				if (fileName != null) {
					if (fileGcda.getName().substring(0, fileName.length()).equals(fileName)) {
						allGcdaFilesNeeded.add(fileGcda);
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

		if (listGcda.getSelectedValue() != null) {
			allSelectedGcdaFilesString = listGcda.getSelectedValuesList();
		}
		Collections.sort(allSelectedGcdaFilesString, String.CASE_INSENSITIVE_ORDER);

		allSelectedGcdaFiles = new ArrayList<>();

		for (File file : allGcdaFilesNeeded) {
			for (String fileName : allSelectedGcdaFilesString) {
				if (file.getName().equals(fileName)) {
					allSelectedGcdaFiles.add(file);
				}
			}
		}
	}

	private void fetchNamesToGcnaList(JList<String> jlist, List<File> list) {

		List<String> fileNames = new ArrayList<>();

		for (File file : list) {
			fileNames.add(file.getName());
		}

		DefaultListModel<String> dlm = new DefaultListModel<>();

		Collections.sort(fileNames, String.CASE_INSENSITIVE_ORDER);
		Collections.reverse(fileNames);

		for (String entity : fileNames) {
			dlm.addElement(entity.substring(0, entity.length()));
		}
		jlist.setModel(dlm);
	}
	
}
