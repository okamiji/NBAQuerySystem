package GUI;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

public class TeamTableModel extends AbstractTableModel{
	Vector columnNames,rows;
	
	public TeamTableModel(){
		columnNames=new Vector();
		columnNames.add("球队名称");
		columnNames.add("比赛场数");
		columnNames.add("投篮命中数");
		columnNames.add("投篮出手次数");
		columnNames.add("三分命中数");
		columnNames.add("三分出手数");
		columnNames.add("罚球命中数");
		columnNames.add("进攻篮板数");
		columnNames.add("防守篮板数");
		columnNames.add("篮板数");
		columnNames.add("助攻数");
		columnNames.add("抢断数");
		columnNames.add("盖帽数");
		columnNames.add("失误数");
		columnNames.add("犯规数");
		columnNames.add("比赛得分");
		columnNames.add("投篮命中率");
		columnNames.add("三分命中率");
		columnNames.add("罚球命中率");
		columnNames.add("胜率");
		columnNames.add("进攻回合");
		columnNames.add("进攻效率");
		columnNames.add("防守效率");
		columnNames.add("篮板效率");
		columnNames.add("抢断效率");
		columnNames.add("助攻效率");
		rows=new Vector();
	}
/*
 * 球队名称，比赛场数，投篮命中数，投篮出手次数，三分命中数，
三分出手数，罚球命中数，罚球出手数，进攻篮板数，防守篮板数，篮板数，助
攻数，抢断数，盖帽数，失误数，犯规数，比赛得分，投篮命中率，三分命中率，
罚球命中率，胜率，进攻回合，进攻效率，防守效率，篮板效率，抢断效率，助
攻率
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
