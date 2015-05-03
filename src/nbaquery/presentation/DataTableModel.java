package nbaquery.presentation;

import java.util.Vector;

import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class DataTableModel extends AbstractTableModel {
		Vector columnNames, rows;
		
	public DataTableModel(){
		columnNames=new Vector();
		columnNames.add("排名");
		columnNames.add("球员");
		columnNames.add("球队");
		columnNames.add("得分");
		columnNames.add("命中-出手");
		columnNames.add("命中率");
		columnNames.add("命中-三分");
		columnNames.add("三分命中率");
		columnNames.add("命中-罚球");
		columnNames.add("罚球命中率");
		columnNames.add("场次");
		columnNames.add("上场时间");
		
		rows=new Vector();
	}
	
	public void addRow(Vector<String> v){
		 rows.add(v); 
	 }
	
	public void removeRow(int r){
		 rows.remove(r); 
	 }

	public int getColumnCount() {
		// TODO Auto-generated method stub
		return this.columnNames.size();
	}

	public int getRowCount() {
		// TODO Auto-generated method stub
		return this.rows.size();
	}
	

	public String getColumnName(int column) {
		// TODO Auto-generated method stub
		return (String)this.columnNames.get(column);
	}

	public Class getColumnClass(int c) {  
		//return getValueAt(0, c).getClass();  
		return String.class;
	}

	public Object getValueAt(int row, int column) {
		// TODO Auto-generated method stub
		return ((Vector)this.rows.get(row)).get(column);
	}
	
	 public void setValueAt(Object value, int row, int column) {  
		 ((Vector)this.rows.get(row)).set(column, value);
		 fireTableCellUpdated(row, column);  
		 
	 }  
	 
	 public boolean isCellEditable(int row, int col) {  
		 return false;  
	 }  

	
}

