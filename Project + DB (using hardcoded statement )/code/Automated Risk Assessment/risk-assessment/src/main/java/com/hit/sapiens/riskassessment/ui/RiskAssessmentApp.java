package com.hit.sapiens.riskassessment.ui;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JTable;
import com.hit.sapiens.riskassessment.beans.Antivirus;
import com.hit.sapiens.riskassessment.beans.AntivirusCategory;
import com.hit.sapiens.riskassessment.beans.AntivirusRiskEvent;
import com.hit.sapiens.riskassessment.beans.Email;
import com.hit.sapiens.riskassessment.beans.EmailRiskEvent;
import com.hit.sapiens.riskassessment.dao.AntivirusDAO;
import com.hit.sapiens.riskassessment.dao.DBSystem;
import com.hit.sapiens.riskassessment.dao.DBTable;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.awt.event.ActionEvent;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.JList;
import java.awt.Color;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.JLabel;
import javax.swing.border.EtchedBorder;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import java.awt.Font;
import javax.swing.event.ListSelectionListener;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;
import javax.swing.event.ListSelectionEvent;
import java.awt.SystemColor;
import java.awt.Toolkit;
import javax.swing.JSeparator;
import javax.swing.border.EmptyBorder;

public class RiskAssessmentApp {
	private AntivirusDAO antivirusDAO = null;
	private JFrame mainFrame;
	private JList<String> listRisksUsersAnti;
	private JList<String> listRisksUsersEmail;
	private JPanel panelPieChartStat;
	private JTable tableDetailsAnti;
	private JTable tableDetailsEmail;
	private JLabel lblShowUserSelectedAnti, lblShowUserSelectedEmail;
	private JLabel lblCountMalwareAnti, lblCountVirusAnti, lblCountEmail;
	private JLabel labelVirusAmountStat, labelMalwareAmountStat, labelEmailAmountStat;
	private JLabel lblDbStatusStat, lblDbStatusAnti, lblDbStatusEmail;
	private JLabel lblServerLoginStat, lblServerLoginAnti, lblServerLoginEmail;
	private JLabel lblDbTableStatValue, lblDbTableAntiValue, lblDbTableEmailValue;
	private DefaultPieDataset pieChartDataset;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					RiskAssessmentApp window = new RiskAssessmentApp();
					window.mainFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public RiskAssessmentApp() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		mainFrame = new JFrame();
		mainFrame.setResizable(false);
		mainFrame.setIconImage(
				Toolkit.getDefaultToolkit().getImage(RiskAssessmentApp.class.getResource("/antivirus-icon.png")));
		mainFrame.setTitle("Automated Risk Assessment");
		mainFrame.setBounds(100, 100, 800, 612);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setLocationRelativeTo(null);

		JMenuBar menuBar = new JMenuBar();
		menuBar.setBackground(new Color(240, 255, 255));
		mainFrame.setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenu mnFromDB = new JMenu("Load From DB...");
		mnFromDB.setIcon(new ImageIcon(
				RiskAssessmentApp.class.getResource("/javax/swing/plaf/metal/icons/ocean/hardDrive.gif")));
		mnFile.add(mnFromDB);

		JMenu mnMysql = new JMenu("MySQL");
		mnFromDB.add(mnMysql);

		JMenuItem mntmAntivirusMySql = new JMenuItem("Antivirus Table");
		mnMysql.add(mntmAntivirusMySql);
		mntmAntivirusMySql.setIcon(new ImageIcon(RiskAssessmentApp.class.getResource("/DBConnect.png")));

		JMenuItem mntmEmailMySql = new JMenuItem("Email Table");
		mntmEmailMySql.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				loadDataFromDB(DBSystem.MY_SQL, DBTable.EMAIL);
			}
		});
		mntmEmailMySql.setIcon(new ImageIcon(RiskAssessmentApp.class.getResource("/DBConnect.png")));
		mnMysql.add(mntmEmailMySql);

		JMenu mnMSSQL = new JMenu("MSSQL Server");
		mnFromDB.add(mnMSSQL);

		JMenuItem mntmAntivirusMsSql = new JMenuItem("Antivirus Table");
		mntmAntivirusMsSql.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				loadDataFromDB(DBSystem.MS_SQL_SERVER, DBTable.ANTIVIRUS);
			}
		});
		mntmAntivirusMsSql.setIcon(new ImageIcon(RiskAssessmentApp.class.getResource("/DBConnect.png")));
		mnMSSQL.add(mntmAntivirusMsSql);
		mntmAntivirusMySql.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				loadDataFromDB(DBSystem.MY_SQL, DBTable.ANTIVIRUS);
			}
		});

		JMenu mnFromFile = new JMenu("Load From File...");
		mnFromFile.setIcon(new ImageIcon(
				RiskAssessmentApp.class.getResource("/com/sun/java/swing/plaf/windows/icons/NewFolder.gif")));
		mnFile.add(mnFromFile);

		JMenuItem mntmFaltDbCsvAntivirus = new JMenuItem("Antivirus CSV / Text File");
		mntmFaltDbCsvAntivirus.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				loadDataFromDB(DBSystem.FLAT_DB, DBTable.ANTIVIRUS);
			}
		});
		mntmFaltDbCsvAntivirus.setIcon(
				new ImageIcon(RiskAssessmentApp.class.getResource("/com/sun/java/swing/plaf/windows/icons/File.gif")));
		mnFromFile.add(mntmFaltDbCsvAntivirus);

		JMenuItem mntmFaltDbCsvEmail = new JMenuItem("Email CSV / Text File");
		mntmFaltDbCsvEmail.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				loadDataFromDB(DBSystem.FLAT_DB, DBTable.EMAIL);
			}
		});
		mntmFaltDbCsvEmail.setIcon(
				new ImageIcon(RiskAssessmentApp.class.getResource("/javax/swing/plaf/metal/icons/ocean/file.gif")));
		mnFromFile.add(mntmFaltDbCsvEmail);

		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.setIcon(
				new ImageIcon(RiskAssessmentApp.class.getResource("/javax/swing/plaf/metal/icons/ocean/maximize.gif")));
		mntmExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(JFrame.EXIT_ON_CLOSE);
			}
		});

		JSeparator separator = new JSeparator();
		mnFile.add(separator);
		mnFile.add(mntmExit);

		JMenu mnTools = new JMenu("Tools");
		menuBar.add(mnTools);

		JMenuItem mntmOptions = new JMenuItem("Options");
		mntmOptions.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				OptionsDialog dialog = new OptionsDialog(mainFrame);
				dialog.setVisible(true);
				dialog.setLocationRelativeTo(null);
			}
		});
		mntmOptions.setIcon(
				new ImageIcon(RiskAssessmentApp.class.getResource("/javax/swing/plaf/metal/icons/ocean/computer.gif")));
		mnTools.add(mntmOptions);

		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);

		JMenuItem mntmAbout = new JMenuItem("About Automated Risk Assessment");
		mntmAbout.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AboutDialog dialog = new AboutDialog();
				dialog.setVisible(true);
				dialog.setLocationRelativeTo(null);
			}
		});
		mntmAbout.setIcon(new ImageIcon(RiskAssessmentApp.class.getResource("/Actions-help-about-icon.png")));
		mnHelp.add(mntmAbout);

		JTabbedPane mainPane = new JTabbedPane(SwingConstants.TOP);
		mainPane.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
		mainFrame.getContentPane().add(mainPane, BorderLayout.CENTER);

		JPanel panelStat = new JPanel();
		mainPane.addTab("Statistics", new ImageIcon(RiskAssessmentApp.class.getResource("/chart-icon.png")), panelStat,
				null);
		panelStat.setLayout(null);
		panelStat.setLayout(null);

		JPanel panelRiskScoreStat = new JPanel();
		panelRiskScoreStat.setForeground(Color.DARK_GRAY);
		panelRiskScoreStat.setLayout(null);
		panelRiskScoreStat.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panelRiskScoreStat.setBounds(0, 0, 783, 488);
		panelStat.add(panelRiskScoreStat);

		panelPieChartStat = new JPanel();
		panelPieChartStat.setForeground(UIManager.getColor("Button.background"));
		panelPieChartStat.setBounds(420, 134, 355, 268);
		panelRiskScoreStat.add(panelPieChartStat);
		panelPieChartStat.setBorder(new EmptyBorder(0, 0, 0, 0));
		panelPieChartStat.setBackground(UIManager.getColor("Button.background"));

		JLabel lblRiskScoreStat = new JLabel("Risk Score");
		lblRiskScoreStat.setForeground(new Color(128, 0, 128));
		lblRiskScoreStat.setFont(new Font("Tahoma", Font.BOLD, 25));
		lblRiskScoreStat.setBounds(10, 11, 160, 36);
		panelRiskScoreStat.add(lblRiskScoreStat);

		JSeparator separatorRiskScoreStat = new JSeparator();
		separatorRiskScoreStat.setBounds(10, 57, 763, 13);
		panelRiskScoreStat.add(separatorRiskScoreStat);
		
		JPanel panel = new JPanel();
		panel.setBorder(new EmptyBorder(0, 0, 0, 0));
		panel.setBounds(14, 120, 412, 294);
		panelRiskScoreStat.add(panel);
		panel.setLayout(null);

		JPanel panelBuisnessRiskStat = new JPanel();
		panelBuisnessRiskStat.setBounds(4, 16, 400, 271);
		panel.add(panelBuisnessRiskStat);
		panelBuisnessRiskStat.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panelBuisnessRiskStat.setLayout(null);
		
				JButton btnCleanStatistics = new JButton("Clean Statistics");
				btnCleanStatistics.setBounds(10, 234, 380, 27);
				panelBuisnessRiskStat.add(btnCleanStatistics);
				btnCleanStatistics.setIcon(new ImageIcon(RiskAssessmentApp.class.getResource("/javax/swing/plaf/metal/icons/ocean/collapsed.gif")));
				btnCleanStatistics.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						cleanPieChartStat();
					}
				});

		JLabel lblAmountStat = new JLabel("Amount");
		lblAmountStat.setBounds(10, 11, 73, 36);
		panelBuisnessRiskStat.add(lblAmountStat);
		lblAmountStat.setFont(new Font("Tahoma", Font.PLAIN, 18));

		JLabel lblBuisnessRiskStat = new JLabel("Buisness Risk");
		lblBuisnessRiskStat.setBounds(240, 11, 120, 36);
		panelBuisnessRiskStat.add(lblBuisnessRiskStat);
		lblBuisnessRiskStat.setFont(new Font("Tahoma", Font.PLAIN, 18));

		JSeparator separatorBuisnessRiskStat = new JSeparator();
		separatorBuisnessRiskStat.setBounds(10, 46, 380, 13);
		panelBuisnessRiskStat.add(separatorBuisnessRiskStat);

		JLabel lblVirusStat = new JLabel("Virus");
		lblVirusStat.setForeground(new Color(220, 20, 60));
		lblVirusStat.setBounds(250, 60, 85, 14);
		panelBuisnessRiskStat.add(lblVirusStat);
		lblVirusStat.setFont(new Font("Tahoma", Font.BOLD, 18));

		labelVirusAmountStat = new JLabel("0");
		labelVirusAmountStat.setForeground(new Color(0, 0, 0));
		labelVirusAmountStat.setBounds(25, 60, 85, 14);
		panelBuisnessRiskStat.add(labelVirusAmountStat);
		labelVirusAmountStat.setFont(new Font("Tahoma", Font.BOLD, 18));

		JLabel lblMalwareStat = new JLabel("Malware");
		lblMalwareStat.setForeground(new Color(255, 99, 71));
		lblMalwareStat.setBounds(250, 115, 85, 14);
		panelBuisnessRiskStat.add(lblMalwareStat);
		lblMalwareStat.setFont(new Font("Tahoma", Font.BOLD, 18));

		labelMalwareAmountStat = new JLabel("0");
		labelMalwareAmountStat.setForeground(new Color(0, 0, 0));
		labelMalwareAmountStat.setBounds(25, 115, 85, 14);
		panelBuisnessRiskStat.add(labelMalwareAmountStat);
		labelMalwareAmountStat.setFont(new Font("Tahoma", Font.BOLD, 18));

		JLabel lblEmailStat = new JLabel("Email");
		lblEmailStat.setForeground(new Color(65, 105, 225));
		lblEmailStat.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblEmailStat.setBounds(250, 170, 85, 14);
		panelBuisnessRiskStat.add(lblEmailStat);

		labelEmailAmountStat = new JLabel("0");
		labelEmailAmountStat.setForeground(Color.BLACK);
		labelEmailAmountStat.setFont(new Font("Tahoma", Font.BOLD, 18));
		labelEmailAmountStat.setBounds(25, 170, 85, 14);
		panelBuisnessRiskStat.add(labelEmailAmountStat);

		lblDbStatusStat = new JLabel("Offline");
		lblDbStatusStat.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblDbStatusStat.setForeground(Color.RED);
		lblDbStatusStat.setBounds(730, 492, 45, 14);
		panelStat.add(lblDbStatusStat);

		JLabel lblDBConnectionStat = new JLabel("Database Connection:");
		lblDBConnectionStat.setHorizontalAlignment(SwingConstants.TRAILING);
		lblDBConnectionStat.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblDBConnectionStat.setBounds(595, 492, 130, 14);
		panelStat.add(lblDBConnectionStat);

		JLabel lblLoginStat = new JLabel("Server:");
		lblLoginStat.setForeground(new Color(0, 0, 0));
		lblLoginStat.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblLoginStat.setBounds(475, 492, 45, 14);
		panelStat.add(lblLoginStat);

		lblServerLoginStat = new JLabel("");
		lblServerLoginStat.setBounds(517, 492, 90, 14);
		panelStat.add(lblServerLoginStat);

		JLabel lblDbTableStat = new JLabel("DB Table:");
		lblDbTableStat.setForeground(Color.BLACK);
		lblDbTableStat.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblDbTableStat.setBounds(323, 492, 55, 14);
		panelStat.add(lblDbTableStat);

		lblDbTableStatValue = new JLabel("");
		lblDbTableStatValue.setBounds(375, 492, 86, 14);
		panelStat.add(lblDbTableStatValue);

		JPanel panelAnti = new JPanel();
		mainPane.addTab("Antivirus", new ImageIcon(RiskAssessmentApp.class.getResource("/antivirusTab.png")), panelAnti,
				null);
		panelAnti.setLayout(null);

		JLabel lblDBConnectionAnti = new JLabel("Database Connection:");
		lblDBConnectionAnti.setHorizontalAlignment(SwingConstants.TRAILING);
		lblDBConnectionAnti.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblDBConnectionAnti.setBounds(595, 492, 130, 14);
		panelAnti.add(lblDBConnectionAnti);

		JPanel panelRisksAnti = new JPanel();
		panelRisksAnti.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panelRisksAnti.setBounds(0, 0, 783, 488);
		panelAnti.add(panelRisksAnti);
		panelRisksAnti.setLayout(null);

		JPanel panelUserRiskAnti = new JPanel();
		panelUserRiskAnti.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Users:",
				TitledBorder.LEADING, TitledBorder.TOP, null, Color.GRAY));
		panelUserRiskAnti.setBounds(611, 101, 163, 375);
		panelRisksAnti.add(panelUserRiskAnti);
		panelUserRiskAnti.setLayout(null);

		JScrollPane scrollPaneUsersAnti = new JScrollPane();
		scrollPaneUsersAnti.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPaneUsersAnti.setBounds(10, 22, 144, 342);
		panelUserRiskAnti.add(scrollPaneUsersAnti);

		listRisksUsersAnti = new JList<>();
		listRisksUsersAnti.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				updateAntivirusTableLabels(lblCountMalwareAnti, lblShowUserSelectedAnti, AntivirusCategory.MALWARE);
				updateAntivirusTableLabels(lblCountVirusAnti, lblShowUserSelectedAnti, AntivirusCategory.VIRUS);
			}
		});
		scrollPaneUsersAnti.setViewportView(listRisksUsersAnti);
		listRisksUsersAnti.setForeground(UIManager.getColor("List.foreground"));
		listRisksUsersAnti.setBackground(UIManager.getColor("List.background"));

		JPanel panelDetailsAnti = new JPanel();
		panelDetailsAnti.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Details:",
				TitledBorder.LEADING, TitledBorder.TOP, null, Color.GRAY));
		panelDetailsAnti.setBounds(10, 101, 594, 375);
		panelRisksAnti.add(panelDetailsAnti);
		panelDetailsAnti.setLayout(null);

		JScrollPane scrollPaneDetailsAnti = new JScrollPane();
		scrollPaneDetailsAnti.setBounds(11, 16, 572, 349);
		panelDetailsAnti.add(scrollPaneDetailsAnti);

		tableDetailsAnti = new JTable();
		tableDetailsAnti.setAutoCreateRowSorter(true);
		scrollPaneDetailsAnti.setViewportView(tableDetailsAnti);

		final RiskRowPopup pop = new RiskRowPopup(tableDetailsAnti);

		tableDetailsAnti.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent mouseEvent) {
				if (SwingUtilities.isLeftMouseButton(mouseEvent)) {
					pop.show(mouseEvent.getComponent(), mouseEvent.getX(), mouseEvent.getY());
				}
			}
		});

		JPanel panelUserChoosenAnti = new JPanel();
		panelUserChoosenAnti.setBackground(SystemColor.scrollbar);
		panelUserChoosenAnti.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
		panelUserChoosenAnti.setBounds(10, 33, 594, 62);
		panelRisksAnti.add(panelUserChoosenAnti);
		panelUserChoosenAnti.setLayout(null);

		JButton btnShowDetailsAnti = new JButton("Show Details");
		btnShowDetailsAnti.setForeground(new Color(0, 0, 0));
		btnShowDetailsAnti.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					antivirusDAO.getAntivirusEvents();
					if (listRisksUsersAnti.getSelectedValue() != null) {
						Antivirus antivirus = antivirusDAO.getAntivirusByUser(listRisksUsersAnti.getSelectedValue());
						fetchEventsToAntivirusDetailsTable(tableDetailsAnti, antivirus.getRiskEvents());
					}

				} catch (Exception e) {
					JOptionPane.showMessageDialog(mainFrame,
							"No DB / File has been received for import.\nPlease reconnect to the correct DB.",
							"Loading Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnShowDetailsAnti.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnShowDetailsAnti.setBounds(234, 2, 120, 59);
		panelUserChoosenAnti.add(btnShowDetailsAnti);

		JPanel panelMalwareAnti = new JPanel();
		panelMalwareAnti.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panelMalwareAnti.setBounds(26, 11, 193, 40);
		panelUserChoosenAnti.add(panelMalwareAnti);
		panelMalwareAnti.setLayout(null);

		JLabel lblMalwareAnti = new JLabel("Malware:");
		lblMalwareAnti.setForeground(new Color(255, 99, 71));
		lblMalwareAnti.setBounds(10, 0, 100, 40);
		panelMalwareAnti.add(lblMalwareAnti);
		lblMalwareAnti.setFont(new Font("Tahoma", Font.BOLD, 20));

		lblCountMalwareAnti = new JLabel("");
		lblCountMalwareAnti.setHorizontalAlignment(SwingConstants.TRAILING);
		lblCountMalwareAnti.setBounds(105, 0, 75, 40);
		panelMalwareAnti.add(lblCountMalwareAnti);
		lblCountMalwareAnti.setFont(new Font("Tahoma", Font.PLAIN, 20));

		JPanel panelVIrusAnti = new JPanel();
		panelVIrusAnti.setLayout(null);
		panelVIrusAnti.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panelVIrusAnti.setBounds(369, 11, 193, 40);
		panelUserChoosenAnti.add(panelVIrusAnti);

		JLabel lblVirusAnti = new JLabel("Virus:");
		lblVirusAnti.setForeground(new Color(220, 20, 60));
		lblVirusAnti.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblVirusAnti.setBounds(10, 0, 100, 40);
		panelVIrusAnti.add(lblVirusAnti);

		lblCountVirusAnti = new JLabel("");
		lblCountVirusAnti.setHorizontalAlignment(SwingConstants.TRAILING);
		lblCountVirusAnti.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblCountVirusAnti.setBounds(105, 0, 75, 40);
		panelVIrusAnti.add(lblCountVirusAnti);

		lblShowUserSelectedAnti = new JLabel("");
		lblShowUserSelectedAnti.setForeground(new Color(0, 0, 255));
		lblShowUserSelectedAnti.setHorizontalAlignment(SwingConstants.CENTER);
		lblShowUserSelectedAnti.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblShowUserSelectedAnti.setBounds(180, 7, 260, 25);
		panelRisksAnti.add(lblShowUserSelectedAnti);

		JButton btnShowAllEventsAnti = new JButton("Show All Events");
		btnShowAllEventsAnti.setForeground(new Color(0, 128, 0));
		btnShowAllEventsAnti.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					antivirusDAO.getAntivirusEvents();
					fetchEventsToAntivirusDetailsTable(tableDetailsAnti, antivirusDAO.getAllAntivirusRiskEvents());
				} catch (Exception e) {
					JOptionPane.showMessageDialog(mainFrame,
							"No DB / File has been received for import.\nPlease reconnect to the correct DB.",
							"Loading Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnShowAllEventsAnti.setBounds(614, 33, 160, 62);
		panelRisksAnti.add(btnShowAllEventsAnti);

		lblDbStatusAnti = new JLabel("Offline");
		lblDbStatusAnti.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblDbStatusAnti.setForeground(new Color(255, 0, 0));
		lblDbStatusAnti.setBounds(730, 492, 45, 14);
		panelAnti.add(lblDbStatusAnti);

		JLabel lblLoginAnti = new JLabel("Server:");
		lblLoginAnti.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblLoginAnti.setBounds(475, 492, 45, 14);
		panelAnti.add(lblLoginAnti);

		lblServerLoginAnti = new JLabel("");
		lblServerLoginAnti.setBounds(517, 492, 90, 14);
		panelAnti.add(lblServerLoginAnti);

		JLabel lblDbTableAnti = new JLabel("DB Table:");
		lblDbTableAnti.setForeground(Color.BLACK);
		lblDbTableAnti.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblDbTableAnti.setBounds(323, 492, 55, 14);
		panelAnti.add(lblDbTableAnti);

		lblDbTableAntiValue = new JLabel("");
		lblDbTableAntiValue.setBounds(375, 492, 86, 14);
		panelAnti.add(lblDbTableAntiValue);

		JPanel panelEmail = new JPanel();
		mainPane.addTab("Email", new ImageIcon(RiskAssessmentApp.class.getResource("/spam-filter-icon.png")),
				panelEmail, null);
		panelEmail.setLayout(null);

		JPanel panelBoardEmail = new JPanel();
		panelBoardEmail.setLayout(null);
		panelBoardEmail.setBounds(0, 0, 783, 506);
		panelEmail.add(panelBoardEmail);

		JPanel panelTotalEmail = new JPanel();
		panelTotalEmail.setLayout(null);
		panelTotalEmail.setForeground(Color.DARK_GRAY);
		panelTotalEmail.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panelTotalEmail.setBounds(0, 0, 783, 488);
		panelBoardEmail.add(panelTotalEmail);

		JPanel panelEmailDetails = new JPanel();
		panelEmailDetails.setLayout(null);
		panelEmailDetails.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Details:",

				TitledBorder.LEADING, TitledBorder.TOP, null, Color.GRAY));
		panelEmailDetails.setBounds(10, 101, 594, 375);
		panelTotalEmail.add(panelEmailDetails);

		JScrollPane scrollPaneDetailsEmail = new JScrollPane();
		scrollPaneDetailsEmail.setBounds(11, 16, 572, 349);
		panelEmailDetails.add(scrollPaneDetailsEmail);

		tableDetailsEmail = new JTable();
		scrollPaneDetailsEmail.setViewportView(tableDetailsEmail);
		tableDetailsEmail.setAutoCreateRowSorter(true);

		JPanel panelEmailUsers = new JPanel();
		panelEmailUsers.setLayout(null);
		panelEmailUsers.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Users:",

				TitledBorder.LEADING, TitledBorder.TOP, null, Color.GRAY));
		panelEmailUsers.setBounds(611, 101, 163, 375);
		panelTotalEmail.add(panelEmailUsers);

		JScrollPane scrollPaneEmailUsers = new JScrollPane();
		scrollPaneEmailUsers.setBounds(10, 22, 144, 342);
		panelEmailUsers.add(scrollPaneEmailUsers);

		listRisksUsersEmail = new JList<>();
		listRisksUsersEmail.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				updateEmailTableLabels(lblCountEmail, lblShowUserSelectedEmail);
			}
		});

		listRisksUsersEmail.setForeground(Color.BLACK);
		listRisksUsersEmail.setBackground(Color.WHITE);
		scrollPaneEmailUsers.setViewportView(listRisksUsersEmail);

		JButton buttonEmailShowAll = new JButton("Show All Events");
		buttonEmailShowAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					antivirusDAO.getEmailEvents();
					fetchEventsToEmailDetailsTable(tableDetailsEmail, antivirusDAO.getAllEmailRiskEvents());
				} catch (Exception e) {
					JOptionPane.showMessageDialog(mainFrame,
							"No DB / File has been received for import.\nPlease reconnect to the correct DB.",
							"Loading Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		buttonEmailShowAll.setForeground(new Color(0, 128, 0));
		buttonEmailShowAll.setBounds(614, 33, 160, 62);
		panelTotalEmail.add(buttonEmailShowAll);

		JPanel panelEmailShowDetails = new JPanel();
		panelEmailShowDetails.setLayout(null);
		panelEmailShowDetails.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
		panelEmailShowDetails.setBackground(SystemColor.scrollbar);
		panelEmailShowDetails.setBounds(120, 33, 355, 62);
		panelTotalEmail.add(panelEmailShowDetails);

		JButton buttonEmailShowDetails = new JButton("Show Details");
		buttonEmailShowDetails.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					antivirusDAO.getEmailEvents();
					if (listRisksUsersEmail.getSelectedValue() != null) {
						Email email = antivirusDAO.getEmailByUser(listRisksUsersEmail.getSelectedValue());
						fetchEventsToEmailDetailsTable(tableDetailsEmail, email.getRiskEvents());
					}

				} catch (Exception e) {
					JOptionPane.showMessageDialog(mainFrame,
							"No DB / File has been received for import.\nPlease reconnect to the correct DB.",
							"Loading Error", JOptionPane.ERROR_MESSAGE);
				}

			}
		});
		buttonEmailShowDetails.setForeground(Color.BLACK);
		buttonEmailShowDetails.setFont(new Font("Tahoma", Font.BOLD, 12));
		buttonEmailShowDetails.setBounds(234, 2, 120, 59);
		panelEmailShowDetails.add(buttonEmailShowDetails);

		JPanel panelEmailShowDetailsBoard = new JPanel();
		panelEmailShowDetailsBoard.setLayout(null);
		panelEmailShowDetailsBoard.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panelEmailShowDetailsBoard.setBounds(26, 11, 193, 40);
		panelEmailShowDetails.add(panelEmailShowDetailsBoard);

		JLabel labelEmailShowDetails = new JLabel("Email");
		labelEmailShowDetails.setForeground(new Color(60, 105, 225));
		labelEmailShowDetails.setFont(new Font("Tahoma", Font.BOLD, 20));
		labelEmailShowDetails.setBounds(10, 0, 100, 40);
		panelEmailShowDetailsBoard.add(labelEmailShowDetails);

		lblCountEmail = new JLabel("");
		lblCountEmail.setHorizontalAlignment(SwingConstants.TRAILING);
		lblCountEmail.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblCountEmail.setBounds(105, 0, 75, 40);
		panelEmailShowDetailsBoard.add(lblCountEmail);

		lblShowUserSelectedEmail = new JLabel("");
		lblShowUserSelectedEmail.setHorizontalAlignment(SwingConstants.CENTER);
		lblShowUserSelectedEmail.setForeground(Color.BLUE);
		lblShowUserSelectedEmail.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblShowUserSelectedEmail.setBounds(143, 7, 355, 25);
		panelTotalEmail.add(lblShowUserSelectedEmail);

		lblDbStatusEmail = new JLabel("Offline");
		lblDbStatusEmail.setForeground(Color.RED);
		lblDbStatusEmail.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblDbStatusEmail.setBounds(730, 492, 45, 14);
		panelBoardEmail.add(lblDbStatusEmail);

		JLabel lblDBConnectionEmail = new JLabel("Database Connection:");
		lblDBConnectionEmail.setHorizontalAlignment(SwingConstants.TRAILING);
		lblDBConnectionEmail.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblDBConnectionEmail.setBounds(595, 492, 130, 14);
		panelBoardEmail.add(lblDBConnectionEmail);

		JLabel lblLoginEmail = new JLabel("Server:");
		lblLoginEmail.setForeground(Color.BLACK);
		lblLoginEmail.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblLoginEmail.setBounds(475, 492, 45, 14);
		panelBoardEmail.add(lblLoginEmail);

		lblServerLoginEmail = new JLabel("");
		lblServerLoginEmail.setBounds(517, 492, 90, 14);
		panelBoardEmail.add(lblServerLoginEmail);

		JLabel lblDbTableEmail = new JLabel("DB Table:");
		lblDbTableEmail.setForeground(Color.BLACK);
		lblDbTableEmail.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblDbTableEmail.setBounds(323, 492, 55, 14);
		panelBoardEmail.add(lblDbTableEmail);

		lblDbTableEmailValue = new JLabel("");
		lblDbTableEmailValue.setBounds(375, 492, 86, 14);
		panelBoardEmail.add(lblDbTableEmailValue);

		pieChartDataset = new DefaultPieDataset();

	}

	private void updateAntivirusTableLabels(JLabel jAmountLabel, JLabel jUserLabel,
			AntivirusCategory antivirusCategory) {
		try {
			String userName = listRisksUsersAnti.getSelectedValue();
			jUserLabel.setText(userName);

			Antivirus antivirus = antivirusDAO.getAntivirusByUser(userName);
			if (antivirusCategory.equals(AntivirusCategory.MALWARE)) {
				jAmountLabel.setText(String.valueOf(antivirus.getAmountByCategory(AntivirusCategory.MALWARE)));
			} else {
				jAmountLabel.setText(String.valueOf(antivirus.getAmountByCategory(AntivirusCategory.VIRUS)));
			}
		} catch (Exception e) {
			e.fillInStackTrace();
		}
	}

	private void updateEmailTableLabels(JLabel jAmountLabel, JLabel jUserLabel) {
		try {
			String userName = listRisksUsersEmail.getSelectedValue();
			jUserLabel.setText(userName);

			Email email = antivirusDAO.getEmailByUser(userName);

			jAmountLabel.setText(String.valueOf(email.getAmount()));

		} catch (Exception e) {
			e.fillInStackTrace();
		}
	}

	private void fetchUsersToList(JList<String> jlist, DBTable dbTable) {
		Set<String> setUsers = null;
		if (dbTable.equals(DBTable.ANTIVIRUS)) {
			setUsers = antivirusDAO.getAllAntivirusUserNames();

		} else {
			setUsers = antivirusDAO.getAllEmailUserNames();

		}

		DefaultListModel<String> dlm = new DefaultListModel<>();

		List<String> users = new ArrayList<>(setUsers);
		Collections.sort(users, String.CASE_INSENSITIVE_ORDER);

		for (String user : users) {
			dlm.addElement(user);
		}
		jlist.setModel(dlm);
	}

	private void fetchEventsToAntivirusDetailsTable(JTable table, List<AntivirusRiskEvent> riskEventsList) {
		RiskAntivirusTableModel model = new RiskAntivirusTableModel(riskEventsList);
		table.setModel(model);
		table.getColumnModel().getColumn(2).setPreferredWidth(320);
	}

	private void fetchEventsToEmailDetailsTable(JTable table, List<EmailRiskEvent> riskEventsList) {
		RiskEmailTableModel model = new RiskEmailTableModel(riskEventsList);
		table.setModel(model);
		table.getColumnModel().getColumn(2).setPreferredWidth(320);
	}

	private void loadDataFromDB(DBSystem dbSystem, DBTable dbTable) {
		try {
			if (dbSystem.equals(DBSystem.MY_SQL)) {
				antivirusDAO = new AntivirusDAO(DBSystem.MY_SQL, dbTable);
			} else if (dbSystem.equals(DBSystem.MS_SQL_SERVER)) {
				antivirusDAO = new AntivirusDAO(DBSystem.MS_SQL_SERVER, dbTable);
			} else {
				try {
					antivirusDAO = new AntivirusDAO(DBSystem.FLAT_DB, dbTable);
				} catch (IOException e) {
					JOptionPane.showMessageDialog(mainFrame,
							"CSV/Text File is already opened or file is missing/corrupted.\nCheck if configurations file:\"flatdb.antivirus.configurations\" is missing/corrupt.\nError: "
									+ e,
							"File Not Found", JOptionPane.ERROR_MESSAGE);
				}
			}
			if (antivirusDAO.getAntivirusEvents() != null || antivirusDAO.getEmailEvents() != null) {
				updateStatusBar(dbSystem, dbTable);

				if (dbTable.equals(DBTable.ANTIVIRUS)) {
					fetchEventsToAntivirusDetailsTable(tableDetailsAnti, antivirusDAO.getAllAntivirusRiskEvents());
					fetchUsersToList(listRisksUsersAnti, dbTable);
					labelVirusAmountStat
							.setText(String.valueOf(antivirusDAO.getSumOfAntivirusRiskEvents(AntivirusCategory.VIRUS)));
					labelMalwareAmountStat.setText(
							String.valueOf(antivirusDAO.getSumOfAntivirusRiskEvents(AntivirusCategory.MALWARE)));
				} else {
					fetchEventsToEmailDetailsTable(tableDetailsEmail, antivirusDAO.getAllEmailRiskEvents());
					fetchUsersToList(listRisksUsersEmail, dbTable);
					labelEmailAmountStat.setText(String.valueOf(antivirusDAO.getSumOfEmailRiskEvents()));
				}
				creatingPieChart(dbTable);

			}
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(mainFrame,
					"Properties file \"risk_assessment.properties\" is missing or corrupted.\n Error: " + e,
					"File Not Found", JOptionPane.ERROR_MESSAGE);
		} 
		catch (SQLServerException e) {
			JOptionPane.showMessageDialog(mainFrame,
					"Login failed for user.\n Error: " + e,
					"Login failed", JOptionPane.ERROR_MESSAGE);
		}catch (IOException e) {
			JOptionPane.showMessageDialog(mainFrame,
					"File \"risk_assessment.properties\" file is missing, or corrupt.\n Error: " + e, "File Not Found",
					JOptionPane.ERROR_MESSAGE);
		} catch (ClassNotFoundException e) {
			JOptionPane.showMessageDialog(mainFrame, "JDBC Driver Not Found Exception.\nError: " + e, "Driver Error",
					JOptionPane.ERROR_MESSAGE);
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(mainFrame,
					"Connection refused: Make sure that an instance of SQL Server is running on the host and accepting TCP/IP connections at the port.\nMake sure that TCP connections to the port are not blocked by a firewall.\nError: "
							+ e,
					"DB Connection Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	private void updateStatusBar(DBSystem dbSystem, DBTable dbTable) {
		if (dbSystem.equals(DBSystem.MY_SQL)) {
			if (dbTable.equals(DBTable.ANTIVIRUS)) {
				lblServerLoginStat.setText(DBSystem.MY_SQL.toString());
				lblServerLoginStat.setForeground(new Color(70, 130, 180));
				lblDbTableStatValue.setText(DBTable.ANTIVIRUS.toString());
				lblDbTableStatValue.setForeground(new Color(205, 92, 92));
				lblServerLoginAnti.setText(DBSystem.MY_SQL.toString());
				lblServerLoginAnti.setForeground(new Color(70, 130, 180));
				lblDbTableAntiValue.setText(DBTable.ANTIVIRUS.toString());
				lblDbTableAntiValue.setForeground(new Color(205, 92, 92));
				lblServerLoginEmail.setText(DBSystem.MY_SQL.toString());
				lblServerLoginEmail.setForeground(new Color(70, 130, 180));
				lblDbTableEmailValue.setText(DBTable.ANTIVIRUS.toString());
				lblDbTableEmailValue.setForeground(new Color(205, 92, 92));
				lblDbStatusAnti.setText("Online");
				lblDbStatusAnti.setForeground(new Color(0, 100, 0));
				lblDbStatusStat.setText("Online");
				lblDbStatusStat.setForeground(new Color(0, 100, 0));
				lblDbStatusEmail.setText("Online");
				lblDbStatusEmail.setForeground(new Color(0, 100, 0));
			} else {
				lblServerLoginStat.setText(DBSystem.MY_SQL.toString());
				lblServerLoginStat.setForeground(new Color(70, 130, 180));
				lblDbTableStatValue.setText(DBTable.EMAIL.toString());
				lblDbTableStatValue.setForeground(new Color(205, 92, 92));
				lblServerLoginAnti.setText(DBSystem.MY_SQL.toString());
				lblServerLoginAnti.setForeground(new Color(70, 130, 180));
				lblDbTableAntiValue.setText(DBTable.EMAIL.toString());
				lblDbTableAntiValue.setForeground(new Color(205, 92, 92));
				lblServerLoginEmail.setText(DBSystem.MY_SQL.toString());
				lblServerLoginEmail.setForeground(new Color(70, 130, 180));
				lblDbTableEmailValue.setText(DBTable.EMAIL.toString());
				lblDbTableEmailValue.setForeground(new Color(205, 92, 92));
				lblDbStatusAnti.setText("Online");
				lblDbStatusAnti.setForeground(new Color(0, 100, 0));
				lblDbStatusStat.setText("Online");
				lblDbStatusStat.setForeground(new Color(0, 100, 0));
				lblDbStatusEmail.setText("Online");
				lblDbStatusEmail.setForeground(new Color(0, 100, 0));
			}

		} else if (dbSystem.equals(DBSystem.MS_SQL_SERVER)) {
			if (dbTable.equals(DBTable.ANTIVIRUS)) {
				lblServerLoginStat.setText(DBSystem.MS_SQL_SERVER.toString());
				lblServerLoginStat.setForeground(new Color(70, 130, 180));
				lblDbTableStatValue.setText(DBTable.ANTIVIRUS.toString());
				lblDbTableStatValue.setForeground(new Color(205, 92, 92));
				lblServerLoginAnti.setText(DBSystem.MS_SQL_SERVER.toString());
				lblServerLoginAnti.setForeground(new Color(70, 130, 180));
				lblDbTableAntiValue.setText(DBTable.ANTIVIRUS.toString());
				lblDbTableAntiValue.setForeground(new Color(205, 92, 92));
				lblServerLoginEmail.setText(DBSystem.MS_SQL_SERVER.toString());
				lblServerLoginEmail.setForeground(new Color(70, 130, 180));
				lblDbTableEmailValue.setText(DBTable.ANTIVIRUS.toString());
				lblDbTableEmailValue.setForeground(new Color(205, 92, 92));
				lblDbStatusAnti.setText("Online");
				lblDbStatusAnti.setForeground(new Color(0, 100, 0));
				lblDbStatusStat.setText("Online");
				lblDbStatusStat.setForeground(new Color(0, 100, 0));
				lblDbStatusEmail.setText("Online");
				lblDbStatusEmail.setForeground(new Color(0, 100, 0));
			} else {
				lblServerLoginStat.setText(DBSystem.MS_SQL_SERVER.toString());
				lblServerLoginStat.setForeground(new Color(70, 130, 180));
				lblDbTableStatValue.setText(DBTable.EMAIL.toString());
				lblDbTableStatValue.setForeground(new Color(205, 92, 92));
				lblServerLoginAnti.setText(DBSystem.MS_SQL_SERVER.toString());
				lblServerLoginAnti.setForeground(new Color(70, 130, 180));
				lblDbTableAntiValue.setText(DBTable.EMAIL.toString());
				lblDbTableAntiValue.setForeground(new Color(205, 92, 92));
				lblServerLoginEmail.setText(DBSystem.MS_SQL_SERVER.toString());
				lblServerLoginEmail.setForeground(new Color(70, 130, 180));
				lblDbTableEmailValue.setText(DBTable.EMAIL.toString());
				lblDbTableEmailValue.setForeground(new Color(205, 92, 92));
				lblDbStatusAnti.setText("Online");
				lblDbStatusAnti.setForeground(new Color(0, 100, 0));
				lblDbStatusStat.setText("Online");
				lblDbStatusStat.setForeground(new Color(0, 100, 0));
				lblDbStatusEmail.setText("Online");
				lblDbStatusEmail.setForeground(new Color(0, 100, 0));

			}
		} else {
			if (dbTable.equals(DBTable.ANTIVIRUS)) {
				lblServerLoginStat.setText(DBSystem.FLAT_DB.toString());
				lblServerLoginStat.setForeground(new Color(70, 130, 180));
				lblDbTableStatValue.setText(DBTable.ANTIVIRUS.toString());
				lblDbTableStatValue.setForeground(new Color(205, 92, 92));
				lblServerLoginAnti.setText(DBSystem.FLAT_DB.toString());
				lblServerLoginAnti.setForeground(new Color(70, 130, 180));
				lblDbTableAntiValue.setText(DBTable.ANTIVIRUS.toString());
				lblDbTableAntiValue.setForeground(new Color(205, 92, 92));
				lblServerLoginEmail.setText(DBSystem.FLAT_DB.toString());
				lblServerLoginEmail.setForeground(new Color(70, 130, 180));
				lblDbTableEmailValue.setText(DBTable.ANTIVIRUS.toString());
				lblDbTableEmailValue.setForeground(new Color(205, 92, 92));
				lblDbStatusAnti.setText("Offline");
				lblDbStatusAnti.setForeground(Color.RED);
				lblDbStatusStat.setText("Offline");
				lblDbStatusStat.setForeground(Color.RED);
				lblDbStatusEmail.setText("Offline");
				lblDbStatusEmail.setForeground(Color.RED);
			} else {
				lblServerLoginStat.setText(DBSystem.FLAT_DB.toString());
				lblServerLoginStat.setForeground(new Color(70, 130, 180));
				lblDbTableStatValue.setText(DBTable.EMAIL.toString());
				lblDbTableStatValue.setForeground(new Color(205, 92, 92));
				lblServerLoginAnti.setText(DBSystem.FLAT_DB.toString());
				lblServerLoginAnti.setForeground(new Color(70, 130, 180));
				lblDbTableAntiValue.setText(DBTable.EMAIL.toString());
				lblDbTableAntiValue.setForeground(new Color(205, 92, 92));
				lblServerLoginEmail.setText(DBSystem.FLAT_DB.toString());
				lblServerLoginEmail.setForeground(new Color(70, 130, 180));
				lblDbTableEmailValue.setText(DBTable.EMAIL.toString());
				lblDbTableEmailValue.setForeground(new Color(205, 92, 92));
				lblDbStatusAnti.setText("Offline");
				lblDbStatusAnti.setForeground(Color.RED);
				lblDbStatusStat.setText("Offline");
				lblDbStatusStat.setForeground(Color.RED);
				lblDbStatusEmail.setText("Offline");
				lblDbStatusEmail.setForeground(Color.RED);

			}
		}
	}

	private void creatingPieChart(DBTable dbTable) {
		Color backgroundColor = UIManager.getColor("Button.background");
		int count = 0;

		if (dbTable.equals(DBTable.ANTIVIRUS)) {
			pieChartDataset.setValue("Virus",
					Double.valueOf(antivirusDAO.getSumOfAntivirusRiskEvents(AntivirusCategory.VIRUS)));
			pieChartDataset.setValue("Malware",
					Double.valueOf(antivirusDAO.getSumOfAntivirusRiskEvents(AntivirusCategory.MALWARE)));
			count++;
		} else {
			pieChartDataset.setValue("Email", Double.valueOf(antivirusDAO.getSumOfEmailRiskEvents()));
			count++;
		}

		JFreeChart chart = ChartFactory.createPieChart3D("", pieChartDataset, false, false, false);
		chart.setBackgroundPaint(backgroundColor);
		chart.setBorderPaint(backgroundColor);

		final PiePlot3D plot = (PiePlot3D) chart.getPlot();
		plot.setBackgroundPaint(backgroundColor);
		plot.setBaseSectionOutlinePaint(new Color(0, 255, 255));
		plot.setBaseSectionPaint(backgroundColor);
		plot.setStartAngle(210);
		plot.setForegroundAlpha(0.90f);
		plot.setInteriorGap(0.02);
		plot.setOutlineVisible(false);
		plot.setShadowPaint(null);
		plot.setCircular(true);

		for (int i = 0; i < count; i++) {
			if (dbTable.equals(DBTable.ANTIVIRUS)) {
				plot.setSectionPaint(pieChartDataset.getKey(i), new Color(220, 20, 60));
			} else {
				plot.setSectionPaint(pieChartDataset.getKey(i), new Color(60, 105, 225));
			}

		}
		count = 0;

		panelPieChartStat.setLayout(new BorderLayout());
		ChartPanel CP = new ChartPanel(chart);
		panelPieChartStat.add(CP, BorderLayout.CENTER);
		panelPieChartStat.validate();
	}

	private void cleanPieChartStat() {
		pieChartDataset.clear();
		labelVirusAmountStat.setText(String.valueOf(0));
		labelMalwareAmountStat.setText(String.valueOf(0));
		labelEmailAmountStat.setText(String.valueOf(0));
		panelPieChartStat.validate();

	}
}
