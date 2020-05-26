package gcov.ui;

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
import javax.swing.ImageIcon;

@SuppressWarnings("serial")
public class AboutDialog extends JDialog {
	/**
	 * Create the dialog.
	 */
	public AboutDialog() {
		setTitle("About");
		setAlwaysOnTop(true);
		setType(Type.POPUP);
		setBounds(100, 100, 482, 451);
		getContentPane().setLayout(null);
		
		JLabel label = new JLabel("");
		label.setIcon(new ImageIcon(AboutDialog.class.getResource("/com/sun/java/swing/plaf/motif/icons/Inform.gif")));
		label.setBounds(410, 12, 24, 28);
		getContentPane().add(label);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(10, 43, 424, 13);
		getContentPane().add(separator);
		
		JLabel lblNewLabel = new JLabel("Gcov Automated Tool");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNewLabel.setBounds(10, 12, 193, 21);
		getContentPane().add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("v1.2");
		lblNewLabel_1.setBounds(200, 17, 46, 14);
		getContentPane().add(lblNewLabel_1);
		
		JPanel panel = new JPanel();
		panel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel.setBounds(10, 51, 424, 305);
		getContentPane().add(panel);
		panel.setLayout(null);
		
		JTextPane txtpnCyberSecurityIs = new JTextPane();
		txtpnCyberSecurityIs.setEditable(false);
		txtpnCyberSecurityIs.setBounds(8, 3, 413, 300);
		panel.add(txtpnCyberSecurityIs);
		txtpnCyberSecurityIs.setText("Coverage process, the code coverage is based on GCC capability to instrument the code running on the target, with code coverage counters. Every block of code is allocated a counter and when this block is executed the counter is incremented.\r\nGCC creates a gcno file during compilation for every instrumented module. The gcno file contains information that enables the matching of the run-time counters ( gcda file) with the source code. Downloading the counters to a gcda file and matching them to the gcno , enables us to create coverage report.\r\nThe gcno and gcda contains checksum and version information in order to insure compatibility.\r\n\r\nThe process involves three steps:\r\n1. Target Instrumentation\r\n2. Download coverage counters\r\n3. Create Report\r\n\r\nEmail: Maor.Bracha@elbitsystems.com");
		txtpnCyberSecurityIs.setBackground(UIManager.getColor("Button.background"));
		
		JButton buttonOK = new JButton("OK");
		buttonOK.setBounds(381, 378, 75, 23);
		getContentPane().add(buttonOK);
		buttonOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
				dispose();
			}
		});
		buttonOK.setActionCommand("Cancel");
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(10, 365, 424, 13);
		getContentPane().add(separator_1);
	}
}
