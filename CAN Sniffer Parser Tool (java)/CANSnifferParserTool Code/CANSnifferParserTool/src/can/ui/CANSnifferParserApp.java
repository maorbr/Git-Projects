package can.ui;

import java.awt.EventQueue;
import javax.swing.JFrame;
import java.awt.Toolkit;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import com.core.CSVSinfferEditor;
import can.ui.AboutDialog;
import javax.swing.UIManager;
import java.awt.Color;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JTextPane;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Font;
import javax.swing.border.EtchedBorder;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.awt.event.ActionEvent;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

@SuppressWarnings("serial")
public class CANSnifferParserApp extends JDialog {

	private JFrame frmCanBusSniffer;
	private JFileChooser chooser;
	private File inputFile;
	private JTextPane inputFileTextPane;
	private JLabel inputFileNameLabel;
	private JLabel outputFileNameLabel;
	private String outputFilePath;
	private String fileOutputName;
	private static final String[] CSV_HEADER = { "Time", "Header", "half0_0", "half0_1", "half0_2", "half0_3",
			"half1_3", "half1_2", "half1_1", "half1_0", "Priority_Index", "H_U", "Class_Type", "Address",
			"Data_Item_Type", "Data_Item_Number", "Counter", "Att" };
	private static final String[] CSV_HEADER_FINAL = { "Time", "Header", "half0_0", "half0_1", "half0_2", "half0_3",
			"half1_3", "half1_2", "half1_1", "half1_0", "Priority Index", "H/U", "Class Type", "Address",
			"Data Item Type", "Data Item Number", "Counter", "Att" };

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CANSnifferParserApp window = new CANSnifferParserApp();
					window.frmCanBusSniffer.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public CANSnifferParserApp() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmCanBusSniffer = new JFrame();
		frmCanBusSniffer.setResizable(false);
		frmCanBusSniffer.setIconImage(
				Toolkit.getDefaultToolkit().getImage(CANSnifferParserApp.class.getResource("/Android_Setting.png")));
		frmCanBusSniffer.setTitle("CAN Bus Sniffer Parser Tool");
		frmCanBusSniffer.setBounds(100, 100, 675, 212);
		frmCanBusSniffer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmCanBusSniffer.getContentPane().setLayout(null);
		frmCanBusSniffer.setLocationRelativeTo(null);

		JPanel panel = new JPanel();
		panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel.setBounds(8, 9, 656, 146);
		frmCanBusSniffer.getContentPane().add(panel);
		panel.setLayout(null);

		JPanel inputFilePanel = new JPanel();
		inputFilePanel.setBounds(6, 16, 580, 65);
		panel.add(inputFilePanel);
		inputFilePanel.setLayout(null);
		inputFilePanel.setToolTipText("");
		inputFilePanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Input CSV File:",
				TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));

		JScrollPane inputFileScrollPane = new JScrollPane();
		inputFileScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		inputFileScrollPane.setBounds(10, 18, 560, 34);
		inputFilePanel.add(inputFileScrollPane);

		inputFileTextPane = new JTextPane();
		inputFileTextPane.setBackground(new Color(240, 240, 240));
		inputFileTextPane.setEditable(false);
		inputFileScrollPane.setViewportView(inputFileTextPane);

		JButton inputFileButton = new JButton("");
		inputFileButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					inputFile = getSourceFolderPath();
					if (inputFile != null) {
						inputFileTextPane.setText(inputFile.getAbsolutePath());
						inputFileNameLabel.setText(inputFile.getName());
						fileOutputName = inputFile.getName().substring(0, inputFile.getName().length() - 4) + "_output_"
								+ new Date().getTime();
						outputFilePath = ".\\outputs\\" + fileOutputName + inputFile.getName()
								.substring(inputFile.getName().length() - 4, inputFile.getName().length());
						outputFileNameLabel.setText(fileOutputName);
						inputFileButton.setForeground(new Color(0, 128, 0));
					}

				} catch (Exception e) {
					JOptionPane.showMessageDialog(getContentPane(), "File may be corrupted or not accessible.",
							"Loading Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		inputFileButton.setBounds(591, 22, 57, 57);
		panel.add(inputFileButton);
		inputFileButton.setIcon(new ImageIcon(CANSnifferParserApp.class.getResource("/preview.png")));

		JButton convertButton = new JButton("Convert");
		convertButton.setIcon(new ImageIcon(CANSnifferParserApp.class.getResource("/iconfinder.png")));
		convertButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					parseCSVToReadableCSV();

					JOptionPane.showMessageDialog(getContentPane(), "Output CSV file was successfully created.\n",
							"Completed Message", JOptionPane.INFORMATION_MESSAGE);
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(getContentPane(),
							"Check if files existed or accessible. \nError: " + e1.getMessage(), "Loading Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		convertButton.setBounds(495, 96, 153, 43);
		panel.add(convertButton);

		JPanel outputFileNamePanel = new JPanel();
		outputFileNamePanel.setBounds(250, 92, 240, 47);
		panel.add(outputFileNamePanel);
		outputFileNamePanel.setLayout(null);
		outputFileNamePanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Output File Name:",
				TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));

		outputFileNameLabel = new JLabel("");
		outputFileNameLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		outputFileNameLabel.setBounds(6, 16, 224, 24);
		outputFileNamePanel.add(outputFileNameLabel);

		JPanel inputFileNamePanel = new JPanel();
		inputFileNamePanel.setBounds(6, 92, 240, 47);
		panel.add(inputFileNamePanel);
		inputFileNamePanel.setLayout(null);
		inputFileNamePanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Input File Name:",
				TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));

		inputFileNameLabel = new JLabel("");
		inputFileNameLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		inputFileNameLabel.setBounds(6, 16, 224, 24);
		inputFileNamePanel.add(inputFileNameLabel);

		JMenuBar menuBar = new JMenuBar();
		frmCanBusSniffer.setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(JFrame.EXIT_ON_CLOSE);
			}
		});
		mntmExit.setIcon(new ImageIcon(
				CANSnifferParserApp.class.getResource("/javax/swing/plaf/metal/icons/ocean/minimize-pressed.gif")));
		mnFile.add(mntmExit);

		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);

		JMenuItem mntmAbout = new JMenuItem("About..");
		mntmAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AboutDialog dialog = new AboutDialog();
				dialog.setVisible(true);
				dialog.setLocationRelativeTo(null);
			}
		});
		mntmAbout.setIcon(
				new ImageIcon(CANSnifferParserApp.class.getResource("/javax/swing/plaf/metal/icons/ocean/menu.gif")));
		mnHelp.add(mntmAbout);
	}

	private File getSourceFolderPath() {

		File path = null;
		String userDir = System.getProperty("user.home");
		chooser = new JFileChooser(userDir + "\\Desktop");

		chooser.setDialogTitle("Browse For CSV File");
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

		FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV FILES", "csv");
		chooser.setFileFilter(filter);

		if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			path = chooser.getCurrentDirectory();
			path = chooser.getSelectedFile();
		} else {
			System.out.println("No Selection ");
		}
		return path;
	}

	private void parseCSVToReadableCSV() throws IOException {
		CSVSinfferEditor csvSinfferEditor = new CSVSinfferEditor();
		csvSinfferEditor.writeHeaderToNewFile(inputFile.getAbsolutePath(), outputFilePath, CSV_HEADER);
		csvSinfferEditor.parseBeanToCSV(outputFilePath, csvSinfferEditor.parseCSVToBean(outputFilePath),
				CSV_HEADER_FINAL);
	}
}
