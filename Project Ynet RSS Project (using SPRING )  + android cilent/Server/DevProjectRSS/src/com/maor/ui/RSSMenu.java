package com.maor.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import com.maor.system.RSSSystem;

public class RSSMenu {
	private final RSSSystem rssSystem;
	private JTextArea globalTextArea;

	public RSSMenu(RSSSystem rssSystem) {
		this.rssSystem = rssSystem;
	}

	public void showMenu() {

		Icon fetchNewsBtnIcon, pauseBtnIcon, resumeBtnIcon, byAuthorBtnIcon, exitBtnIcon;

		fetchNewsBtnIcon = UIManager.getIcon("FileView.fileIcon");
		pauseBtnIcon = UIManager.getIcon("Tree.collapsedIcon");
		resumeBtnIcon = UIManager.getIcon("Tree.expandedIcon");
		byAuthorBtnIcon = UIManager.getIcon("FileView.hardDriveIcon");
		exitBtnIcon = UIManager.getIcon("OptionPane.errorIcon");

		JFrame windowsFrame = new JFrame("RSS Project");

		JLabel label = new JLabel("Ynet RSS news:");
		label.setBounds(550, 45, 150, 30);
		label.setFont(new Font("Ariel", Font.BOLD, 18));
		label.setForeground(Color.RED);

		JLabel labelStatus = new JLabel("Automatically update service is: ON");
		labelStatus.setBounds(190, 45, 250, 30);
		labelStatus.setFont(new Font("Ariel", Font.BOLD, 12));
		labelStatus.setForeground(Color.BLUE);

		JButton fetchNewsBtn = new JButton("Fetch news");
		fetchNewsBtn.setBounds(50, 20, 125, 30);

		JButton pauseBtn = new JButton("Pause");
		pauseBtn.setBounds(185, 20, 95, 30);

		JButton resumeBtn = new JButton("Resume");
		resumeBtn.setBounds(290, 20, 110, 30);

		JButton byAuthorBtn = new JButton("By Author");
		byAuthorBtn.setBounds(410, 20, 120, 30);

		JButton exitBtn = new JButton("Exit");
		exitBtn.setBounds(1207, 20, 95, 40);

		globalTextArea = new JTextArea();
		globalTextArea.setBounds(50, 70, 1255, 730);
		globalTextArea.setText("click the buttons to continue ...");
		globalTextArea.setLineWrap(true);
		globalTextArea.setWrapStyleWord(true);
		globalTextArea.setEditable(false);
		globalTextArea.setFont(new Font("Lucida Sans Unicode", Font.PLAIN, 18));

		JScrollPane scroll = new JScrollPane(globalTextArea);
		scroll.setBounds(50, 70, 1255, 730);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		fetchNewsBtn.setIcon((fetchNewsBtnIcon));
		pauseBtn.setIcon((pauseBtnIcon));
		resumeBtn.setIcon((resumeBtnIcon));
		byAuthorBtn.setIcon((byAuthorBtnIcon));
		exitBtn.setIcon((exitBtnIcon));

		windowsFrame.add(label);
		windowsFrame.add(labelStatus);
		windowsFrame.add(fetchNewsBtn);
		windowsFrame.add(pauseBtn);
		windowsFrame.add(resumeBtn);
		windowsFrame.add(byAuthorBtn);
		windowsFrame.add(exitBtn);
		windowsFrame.getContentPane().add(scroll);
		windowsFrame.setResizable(false);
		windowsFrame.setSize(1348, 900);
		windowsFrame.setLayout(null);
		windowsFrame.setVisible(true);

		fetchNewsBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				displayRssFetcherOnGui();
			}
		});

		pauseBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RSSSystem.rssTask.stopRssTask();
				labelStatus.setText("Automatically update service is: OFF");
			}
		});

		resumeBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RSSSystem.rssTask.startRssTask();
				labelStatus.setText("Automatically update service is: ON");
			}
		});

		byAuthorBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String authorName = JOptionPane.showInputDialog(null, "Please Enter author name: ", "Input Author",
							JOptionPane.INFORMATION_MESSAGE);
					if (!(authorName.isEmpty())) {
						globalTextArea.setText(rssSystem.displayRssFetcher(authorName));
					}
				} catch (NullPointerException ex) {
					System.out.println("Message: " + ex.getMessage());
				}
			}
		});

		exitBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
	}

	public void displayRssFetcherOnGui() {
		globalTextArea.setText(rssSystem.displayRssFetcher());
	}
}
