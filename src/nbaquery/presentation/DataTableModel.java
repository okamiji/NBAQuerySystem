package nbaquery.presentation;

import java.util.Vector;

import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class DataTableModel extends AbstractTableModel {
		@SuppressWarnings("rawtypes")
		Vector columnNames, rows;
		
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public DataTableModel(){
		columnNames=new Vector();
		columnNames.add("����");
		columnNames.add("��Ա");
		columnNames.add("���");
		columnNames.add("�÷�");
		columnNames.add("����-����");
		columnNames.add("������");
		columnNames.add("����-����");
		columnNames.add("����������");
		columnNames.add("����-����");
		columnNames.add("����������");
		columnNames.add("����");
		columnNames.add("�ϳ�ʱ��");
		
		rows=new Vector();
	}
	
	@SuppressWarnings("unchecked")
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

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Class getColumnClass(int c) {  
		//return getValueAt(0, c).getClass();  
		return String.class;
	}

	@SuppressWarnings("rawtypes")
	public Object getValueAt(int row, int column) {
		// TODO Auto-generated method stub
		return ((Vector)this.rows.get(row)).get(column);
	}
	
	 @SuppressWarnings({ "unchecked", "rawtypes" })
	public void setValueAt(Object value, int row, int column) {  
		 ((Vector)this.rows.get(row)).set(column, value);
		 fireTableCellUpdated(row, column);  
		 
	 }  
	 
	 public boolean isCellEditable(int row, int col) {  
		 return false;  
	 }  

	
}

