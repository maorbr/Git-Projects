package com.hit.sapiens.riskassessment.ui;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;

@SuppressWarnings("serial")
public class RiskRowPopup extends JPopupMenu{

	public RiskRowPopup(JTable table) {
		JMenuItem copy = new JMenuItem("Copy");
		
		copy.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
			    StringSelection stringSelection = new StringSelection((String) table.getValueAt(table.getSelectedRow(), table.getSelectedColumn()));
			    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			    clipboard.setContents(stringSelection, null);
			}
		});
		
		add(copy);
	}
}
