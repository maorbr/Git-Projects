package gcov.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import gcov.engine.GcovCoverageMerger;

import javax.swing.ImageIcon;
import javax.swing.JTextPane;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.io.File;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.border.TitledBorder;
import javax.swing.UIManager;
import java.awt.Toolkit;
import javax.swing.border.BevelBorder;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

@SuppressWarnings("serial")
public class GcovCoverageDialog extends JDialog {
	private File projectFolder;
	private JTextPane CoverageTextPane;
	private JFileChooser chooser;
	private JLabel valueCoveragelbl;
	private JPanel contentPane;
	private JLabel fileNameLabel;
	private final JPanel contentPanel = new JPanel();

	/**
	 * Create the dialog.
	 */
	public GcovCoverageDialog() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				GcovCoverageDialog.class.getResource("/com/sun/java/swing/plaf/windows/icons/ListView.gif")));
		setTitle("Gcov Code Coverage Rank");
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JPanel panel = new JPanel();
		panel.setToolTipText("");
		panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "File path:", TitledBorder.LEADING,
				TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel.setBounds(4, 13, 371, 59);
		contentPanel.add(panel);
		panel.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(6, 16, 359, 36);
		panel.add(scrollPane);

		CoverageTextPane = new JTextPane();
		scrollPane.setViewportView(CoverageTextPane);
		CoverageTextPane.setForeground(Color.BLUE);
		CoverageTextPane.setFont(new Font("Arial", Font.PLAIN, 12));
		CoverageTextPane.setEditable(false);
		CoverageTextPane.setBackground(Color.WHITE);

		JButton CoverageButton = new JButton("");
		CoverageButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					projectFolder = getSourceFolderPath();
					if (projectFolder != null) {
						CoverageTextPane.setText(projectFolder.getAbsolutePath());
						fileNameLabel.setText(projectFolder.getName());
						CoverageTextPane.setForeground(new Color(0, 128, 0));
						double averageCoverage = GcovCoverageMerger.CoverageCalculator(projectFolder.getAbsolutePath());
						syncCoverageLabel(averageCoverage);
					}

				} catch (Exception e) {
					JOptionPane.showMessageDialog(contentPane, "File may be corrupted or not accessible.",
							"Loading Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		CoverageButton.setIcon(new ImageIcon(GcovAutomatedApp.class.getResource("/preview.png")));
		CoverageButton.setBounds(378, 21, 50, 48);
		contentPanel.add(CoverageButton);

		JLabel lblFilePath = new JLabel("File path:");
		lblFilePath.setForeground(new Color(75, 0, 130));
		lblFilePath.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblFilePath.setBounds(10, 11, 178, 14);
		contentPanel.add(lblFilePath);

		JLabel lblAverageCoverage = new JLabel("Average Code Coverage:");
		lblAverageCoverage.setForeground(Color.BLACK);
		lblAverageCoverage.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblAverageCoverage.setBounds(47, 145, 245, 59);
		contentPanel.add(lblAverageCoverage);

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel_1.setBounds(301, 145, 74, 59);
		contentPanel.add(panel_1);
		panel_1.setLayout(null);

		valueCoveragelbl = new JLabel("");
		valueCoveragelbl.setHorizontalAlignment(SwingConstants.CENTER);
		valueCoveragelbl.setBounds(3, 3, 68, 53);
		panel_1.add(valueCoveragelbl);
		valueCoveragelbl.setFont(new Font("Tahoma", Font.PLAIN, 26));
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "File Name:", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_2.setBounds(4, 79, 202, 47);
		contentPanel.add(panel_2);
		panel_2.setLayout(null);
		
		fileNameLabel = new JLabel("");
		fileNameLabel.setBounds(6, 16, 186, 24);
		panel_2.add(fileNameLabel);
		fileNameLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						setVisible(false);
						dispose();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
	}

	private File getSourceFolderPath() {

		File path = null;
		String userDir = System.getProperty("user.home");
		chooser = new JFileChooser(userDir + "\\Desktop");

		chooser.setDialogTitle("Browse For Project Folder");
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

		FileNameExtensionFilter filter = new FileNameExtensionFilter("GCOV FILES", "gcov");
		chooser.setFileFilter(filter);

		if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			path = chooser.getCurrentDirectory();
			path = chooser.getSelectedFile();
		} else {
			System.out.println("No Selection ");
		}
		return path;
	}

	private void syncCoverageLabel(double coverageRank) {
		if (coverageRank < 0.5) {
			valueCoveragelbl.setText((int) (100 * coverageRank) + "%");
			valueCoveragelbl.setForeground(new Color(255, 0, 0));
		} else if (coverageRank >= 0.5 && coverageRank <= 0.8) {
			valueCoveragelbl.setText((int) (100 * coverageRank) + "%");
			valueCoveragelbl.setForeground(new Color(255, 140, 0));
		} else if (coverageRank > 0.8 && coverageRank <= 0.99) {
			valueCoveragelbl.setText((int) (100 * coverageRank) + "%");
			valueCoveragelbl.setForeground(new Color(50, 205, 50));
		}else {
			valueCoveragelbl.setText((int) (100 * coverageRank) + "%");
			valueCoveragelbl.setForeground(new Color(127, 255, 0));
		}
	}
}
