package can.ui;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JSeparator;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;

@SuppressWarnings("serial")
public class AboutDialog extends JDialog {
	/**
	 * Create the dialog.
	 */
	public AboutDialog() {
		setTitle("About");
		setAlwaysOnTop(true);
		setType(Type.POPUP);
		setBounds(100, 100, 302, 242);
		getContentPane().setLayout(null);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(10, 43, 267, 13);
		getContentPane().add(separator);
		
		JLabel lblNewLabel = new JLabel("CAN Bus Sniffer Parser Tool");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNewLabel.setBounds(10, 12, 240, 21);
		getContentPane().add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("v1.2");
		lblNewLabel_1.setBounds(244, 17, 46, 14);
		getContentPane().add(lblNewLabel_1);
		
		JPanel panel = new JPanel();
		panel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel.setBounds(10, 51, 267, 113);
		getContentPane().add(panel);
		panel.setLayout(null);
		
		JTextPane txtpnCyberSecurityIs = new JTextPane();
		txtpnCyberSecurityIs.setEditable(false);
		txtpnCyberSecurityIs.setBounds(8, 5, 250, 101);
		panel.add(txtpnCyberSecurityIs);
		txtpnCyberSecurityIs.setText("Input Sniffer CSV output file and generate it to an extensive and clear CSV output file by binary conversion.\r\n\r\n\r\nEmail: Maor.Bracha@elbitsystems.com");
		txtpnCyberSecurityIs.setBackground(UIManager.getColor("Button.background"));
		
		JButton buttonOK = new JButton("OK");
		buttonOK.setBounds(206, 175, 75, 23);
		getContentPane().add(buttonOK);
		buttonOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
				dispose();
			}
		});
		buttonOK.setActionCommand("Cancel");
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(10, 170, 267, 13);
		getContentPane().add(separator_1);
	}
}
