package com.hit.view;

import java.awt.Color;
import java.awt.Container;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SpringLayout;
import javax.swing.UIManager;


public class CacheUnitView {
	private PropertyChangeSupport pcs;
	private CustomPanel customPanel;

	public CacheUnitView() {
		pcs = new PropertyChangeSupport(this);
		customPanel = new CustomPanel();
	}

	public void addPropertyChangeListener(PropertyChangeListener pcl) {
		pcs.addPropertyChangeListener(pcl);

	}

	public void removePropertyChangeListener(PropertyChangeListener pcl) {
		pcs.removePropertyChangeListener(pcl);
	}

	public void start() {
		customPanel.createAndShowGui();
	}

	public <T> void updateUIData(T t) {
		
		if (t.toString().equals("connection succeeded")) {
			customPanel.textArea.setText("Command executed successfully!");
		} else if (t.toString().equals("connection failed")) {
			customPanel.textArea.setText("Command failed!");
		} else if (t.toString().equals("Signed by Maor & Shelly")) {
			customPanel.textArea.setText("Signed by Maor Bracha and Shelly Alfasi");
		} else {
			customPanel.textArea.setText(t.toString());
		}

		customPanel.textArea.invalidate();
	}

	public class CustomPanel extends JPanel implements ActionListener {

		private static final long serialVersionUID = 1027119854423309554L;
		private JFrame frame;
		private JButton loadReqBtn;
		private JButton statisticsBtn;
		private JButton aboutBtn;
		private JLabel label;
		private JTextArea textArea;
		private Icon dirIcon;
		private Icon statIcon;
		private Icon infoIcon;

		public CustomPanel() {
			frame = new JFrame("Cache Unit Project");
			loadReqBtn = new JButton("Load a Request");
			statisticsBtn = new JButton("Show Statistics");
			aboutBtn = new JButton("About Us");			
			label = new JLabel("Cache Unit Project");
			textArea = new JTextArea(6, 38);
			dirIcon = UIManager.getIcon("FileView.directoryIcon");
			statIcon = UIManager.getIcon("FileView.hardDriveIcon");
			infoIcon = UIManager.getIcon("FileChooser.homeFolderIcon");
		}
	    
		public String readFileToString(String filePath) throws IOException {
			StringBuilder contentBuilder = new StringBuilder();
			try (BufferedReader br = new BufferedReader(
					new FileReader(filePath))) {
				String sCurrentLine;
				while ((sCurrentLine = br.readLine()) != null) {
					contentBuilder.append(sCurrentLine).append("\n");
				}
			} catch (FileNotFoundException e) {
				System.out.println("Please Choose json file");
			}

			catch (IOException e) {
				e.printStackTrace();
			}

			return contentBuilder.toString();
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub

		}

		public void createAndShowGui() {
			
			frame.pack();
			frame.setSize(1000, 610);
			frame.setLocationRelativeTo(null);			
			frame.setResizable(false);
			frame.setVisible(true);
			frame.setContentPane(new JLabel(new ImageIcon("../CacheUnitClient/src/main/resources/images/cpu.jpg")));
			SpringLayout layout = new SpringLayout();
			Container contentPane = frame.getContentPane();
			contentPane.setLayout(layout);
			
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			loadReqBtn.setIcon((dirIcon));
			loadReqBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {

					FileDialog fd = new FileDialog(frame, "Choose a file", FileDialog.LOAD);
					fd.setDirectory(".\\resources");
					fd.setTitle("Load a Request");
					fd.setFile("*.Json");
					fd.setVisible(true);

					String filename = fd.getFile();
					if (filename == null)
						System.out.println("You cancelled the choice");
					else
						System.out.println("You choose " + filename);

					PropertyChangeEvent event = null;
					try {
						event = new PropertyChangeEvent(CacheUnitView.this, "loadJsonFile", null, readFileToString(fd.getDirectory() + fd.getFile()));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					pcs.firePropertyChange(event);
				}
			});
			
			statisticsBtn.setIcon((statIcon));
			statisticsBtn.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					PropertyChangeEvent event;
					event = new PropertyChangeEvent(CacheUnitView.this, "statistics", null, 1);
					pcs.firePropertyChange(event);

				}
			});
			
			aboutBtn.setIcon((infoIcon));
			aboutBtn.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					PropertyChangeEvent event = null;
					event = new PropertyChangeEvent(CacheUnitView.this,"about", null, 1);
					pcs.firePropertyChange(event);
				}
			});
				

			label.setFont(new Font("BinnerD", Font.CENTER_BASELINE, 16));
			label.setForeground(Color.BLUE);
			textArea.setText("click the buttons to continue ...");
			textArea.setLineWrap(true);
			textArea.setWrapStyleWord(true);
			textArea.setFont(new Font("Lucida Sans Unicode", Font.PLAIN, 12));

			this.add(label);	
			this.add(textArea);	
			this.add(loadReqBtn);
			this.add(statisticsBtn);
			this.add(aboutBtn);
			frame.add(this);				
			frame.setVisible(true);	
		}
	}
}
