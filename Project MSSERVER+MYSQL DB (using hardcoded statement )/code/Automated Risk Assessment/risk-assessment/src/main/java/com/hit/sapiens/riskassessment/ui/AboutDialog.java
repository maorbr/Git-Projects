package com.hit.sapiens.riskassessment.ui;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JSeparator;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.ImageIcon;

@SuppressWarnings("serial")
public class AboutDialog extends JDialog {
	/**
	 * Create the dialog.
	 */
	public AboutDialog() {
		setTitle("About");
		setIconImage(Toolkit.getDefaultToolkit().getImage(OptionsDialog.class.getResource("/Actions-help-about-icon.png")));
		setAlwaysOnTop(true);
		setType(Type.POPUP);
		setResizable(false);
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(null);
		
		JLabel label = new JLabel("");
		label.setIcon(new ImageIcon(AboutDialog.class.getResource("/iconfinder.png")));
		label.setBounds(388, 12, 24, 28);
		getContentPane().add(label);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(10, 43, 424, 13);
		getContentPane().add(separator);
		
		JLabel lblNewLabel = new JLabel("Automated Risk Assessment");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNewLabel.setBounds(10, 12, 246, 21);
		getContentPane().add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("v1.0");
		lblNewLabel_1.setBounds(252, 17, 46, 14);
		getContentPane().add(lblNewLabel_1);
		
		JPanel panel = new JPanel();
		panel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel.setBounds(34, 51, 378, 172);
		getContentPane().add(panel);
		panel.setLayout(null);
		
		JTextPane txtpnCyberSecurityIs = new JTextPane();
		txtpnCyberSecurityIs.setEditable(false);
		txtpnCyberSecurityIs.setBounds(8, 3, 362, 164);
		panel.add(txtpnCyberSecurityIs);
		txtpnCyberSecurityIs.setText("Cyber security is all about risk management. Automating the risk evaluation process and turning cyber risks into business risks by reading a database table and identifying a specific pattern of events once a trigger has been made the frontend application will present a higher risk score build into a risk matrix.\r\n\r\nCopyright © 2019 by\r\n  º Maor Barcha, maorbr89@gmail.com\r\n  º Johnny Jonathan, Johnny.Jonathan@sapiens.com");
		txtpnCyberSecurityIs.setBackground(UIManager.getColor("Button.background"));
		
		JButton buttonOK = new JButton("OK");
		buttonOK.setBounds(359, 237, 75, 23);
		getContentPane().add(buttonOK);
		buttonOK.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
				dispose();
			}
		});
		buttonOK.setActionCommand("Cancel");
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(10, 229, 424, 13);
		getContentPane().add(separator_1);
	}
}
