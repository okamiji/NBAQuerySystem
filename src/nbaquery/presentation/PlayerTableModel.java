package nbaquery.presentation;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

@SuppressWarnings({"serial", "rawtypes", "unchecked"})
public class PlayerTableModel extends AbstractTableModel{
	Vector columnNames,rows;
	
	public PlayerTableModel(){
		columnNames=new Vector();
		columnNames.add("����");
		columnNames.add("����");
		columnNames.add("���");
		columnNames.add("��������");
		columnNames.add("�ȷ�����");
		columnNames.add("����");
		columnNames.add("����");
		columnNames.add("�ڳ�ʱ��");
		columnNames.add("Ͷ��������");
		columnNames.add("����������");
		columnNames.add("����������");
		columnNames.add("����");
		columnNames.add("����");
		columnNames.add("����");
		columnNames.add("��ñ");
		columnNames.add("ʧ��");
		columnNames.add("����");
		columnNames.add("�÷�");
		columnNames.add("Ч��");
		columnNames.add("GmSc");
		columnNames.add("��ʵ������");
		columnNames.add("Ͷ��Ч��");
		columnNames.add("������");
		columnNames.add("����������");
		columnNames.add("����������");
		columnNames.add("������");
		columnNames.add("������");
		columnNames.add("��ñ��");
		columnNames.add("ʧ����");
		columnNames.add("ʹ����");
		columnNames.add("��Աλ��");
		columnNames.add("����");
		columnNames.add("��/��/��");
		columnNames.add("�ϳ�ʱ��");
		
		rows=new Vector();
	}
/*
 * ��Ա���ƣ�������ӣ������������ȷ�������������������������
��ʱ�䣬Ͷ�������ʣ����������ʣ����������ʣ���������������������������
ñ����ʧ���������������÷֣�Ч�ʣ�GmSc Ч��ֵ����ʵ�����ʣ�Ͷ��Ч�ʣ�
�����ʣ����������ʣ����������ʣ������ʣ������ʣ���ñ�ʣ�ʧ���ʣ�ʹ����
�ȣ����ҿ����������������е��κ�һ���ȫ�����ݽ��������������
���ҿ���
ͨ����Աλ�ã�ǰ�棬�з棬����������Ա���ˣ����������������Է�����������
���ݣ��÷֣����壬�������÷�/����/��������Ȩ��Ϊ 1:1:1������ñ�����ϣ���
�棬ʧ�󣬷��ӣ�Ч�ʣ�Ͷ�������֣�������˫����ָ�÷֡����塢��������
�ϡ���ñ���κ����� ����
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
