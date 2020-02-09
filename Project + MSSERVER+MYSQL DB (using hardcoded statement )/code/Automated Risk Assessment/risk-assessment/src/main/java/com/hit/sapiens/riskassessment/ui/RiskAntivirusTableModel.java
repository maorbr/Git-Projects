package com.hit.sapiens.riskassessment.ui;

import java.util.List;
import javax.swing.table.AbstractTableModel;
import com.hit.sapiens.riskassessment.beans.AntivirusRiskEvent;

@SuppressWarnings("serial")
public class RiskAntivirusTableModel extends AbstractTableModel {

	private static final int CATEGORY_COL = 0;
	private static final int RISK_NAME_COL = 1;
	private static final int FILE_NAME_COL = 2;

	private String[] columnNames = { "Category", "Risk Name", "File Name"};

	private List<AntivirusRiskEvent> riskEvents;

	public RiskAntivirusTableModel(List<AntivirusRiskEvent> riskEvents) {
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
			return riskEvents.get(row).getCategory();
		case RISK_NAME_COL:
			return riskEvents.get(row).getVirusName();
		case FILE_NAME_COL:
			return riskEvents.get(row).getFileName();
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

