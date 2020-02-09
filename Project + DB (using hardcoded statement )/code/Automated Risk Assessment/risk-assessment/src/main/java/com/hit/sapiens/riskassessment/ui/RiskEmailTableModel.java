package com.hit.sapiens.riskassessment.ui;

import java.util.List;
import javax.swing.table.AbstractTableModel;
import com.hit.sapiens.riskassessment.beans.EmailRiskEvent;

@SuppressWarnings("serial")
public class RiskEmailTableModel extends AbstractTableModel {
	
	private static final int MSG_COL = 0;
	private static final int CATEGORY_COL = 1;
	private static final int ID_COL = 2;

	private String[] columnNames = { "Risk Name", "Category", "ID"};

	private List<EmailRiskEvent> riskEvents;

	public RiskEmailTableModel(List<EmailRiskEvent> riskEvents) {
		this.riskEvents = riskEvents;
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
		return riskEvents.size();
	}

	@Override
	public String getColumnName(int col) {
		return columnNames[col];
	}

	@Override
	public Object getValueAt(int row, int col) {
		switch (col) {
		case CATEGORY_COL:
			return riskEvents.get(row).getMsg();
		case MSG_COL:
			return riskEvents.get(row).getCategory();
		case ID_COL:
			return riskEvents.get(row).getId();
		default:
			return riskEvents.get(row).getCategory();
		}
	}
	
	@Override
	public Class<?> getColumnClass(int columnIndex) {
	    if (riskEvents.isEmpty()) {
	        return Object.class;
	    }
	    return getValueAt(0, columnIndex).getClass();
	}
}

