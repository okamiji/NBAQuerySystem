package GUI;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

public class TeamTableModel extends AbstractTableModel{
	Vector columnNames,rows;
	
	public TeamTableModel(){
		columnNames=new Vector();
		columnNames.add("�������");
		columnNames.add("��������");
		columnNames.add("Ͷ��������");
		columnNames.add("Ͷ�����ִ���");
		columnNames.add("����������");
		columnNames.add("���ֳ�����");
		columnNames.add("����������");
		columnNames.add("����������");
		columnNames.add("����������");
		columnNames.add("������");
		columnNames.add("������");
		columnNames.add("������");
		columnNames.add("��ñ��");
		columnNames.add("ʧ����");
		columnNames.add("������");
		columnNames.add("�����÷�");
		columnNames.add("Ͷ��������");
		columnNames.add("����������");
		columnNames.add("����������");
		columnNames.add("ʤ��");
		columnNames.add("�����غ�");
		columnNames.add("����Ч��");
		columnNames.add("����Ч��");
		columnNames.add("����Ч��");
		columnNames.add("����Ч��");
		columnNames.add("����Ч��");
		rows=new Vector();
	}
/*
 * ������ƣ�����������Ͷ����������Ͷ�����ִ�����������������
���ֳ����������������������������������������������������������������
����������������ñ����ʧ�������������������÷֣�Ͷ�������ʣ����������ʣ�
���������ʣ�ʤ�ʣ������غϣ�����Ч�ʣ�����Ч�ʣ�����Ч�ʣ�����Ч�ʣ���
����
 */
	public void addRow(Vector v){
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
		return getValueAt(0, c).getClass();  
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
