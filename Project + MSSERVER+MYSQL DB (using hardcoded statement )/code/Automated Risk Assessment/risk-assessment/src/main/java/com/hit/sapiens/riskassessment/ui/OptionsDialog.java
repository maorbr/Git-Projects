package com.hit.sapiens.riskassessment.ui;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class OptionsDialog extends JDialog {
	/**
	 * Create the dialog.
	 */
	public OptionsDialog(JFrame mainFrame) {
		setTitle("Options");
		setIconImage(Toolkit.getDefaultToolkit().getImage(OptionsDialog.class.getResource("/javax/swing/plaf/metal/icons/ocean/computer.gif")));
		setAlwaysOnTop(true);
		setType(Type.POPUP);
		setResizable(false);
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(null);
		
		JPanel panelOptions = new JPanel();
		panelOptions.setToolTipText("");
		panelOptions.setBounds(0, 238, 444, 33);
		getContentPane().add(panelOptions);
		panelOptions.setLayout(null);
		
		JButton buttonSave = new JButton("Save");
		buttonSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				dispose();
			}
		});
		buttonSave.setBounds(285, 5, 75, 23);
		buttonSave.setActionCommand("OK");
		panelOptions.add(buttonSave);
		
		JButton buttonCancel = new JButton("Cancel");
		buttonCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
				dispose();
			}
		});
		buttonCancel.setBounds(364, 5, 75, 23);
		buttonCancel.setActionCommand("Cancel");
		panelOptions.add(buttonCancel);
	}
}
