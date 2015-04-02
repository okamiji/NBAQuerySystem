package nbaquery.presentation;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

@SuppressWarnings({"serial", "rawtypes", "unchecked"})
public class PlayerTableModel extends AbstractTableModel{
	Vector columnNames,rows;
	
	public PlayerTableModel(){
		columnNames=new Vector();
		columnNames.add("赛季");
		columnNames.add("姓名");
		columnNames.add("球队");
		columnNames.add("参赛场数");
		columnNames.add("先发场数");
		columnNames.add("篮板");
		columnNames.add("助攻");
		columnNames.add("在场时间");
		columnNames.add("投篮命中率");
		columnNames.add("三分命中率");
		columnNames.add("罚球命中率");
		columnNames.add("进攻");
		columnNames.add("防守");
		columnNames.add("抢断");
		columnNames.add("盖帽");
		columnNames.add("失误");
		columnNames.add("犯规");
		columnNames.add("得分");
		columnNames.add("效率");
		columnNames.add("GmSc");
		columnNames.add("真实命中率");
		columnNames.add("投篮效率");
		columnNames.add("篮板率");
		columnNames.add("进攻篮板率");
		columnNames.add("防守篮板率");
		columnNames.add("助攻率");
		columnNames.add("抢断率");
		columnNames.add("盖帽率");
		columnNames.add("失误率");
		columnNames.add("使用率");
		columnNames.add("球员位置");
		columnNames.add("联盟");
		columnNames.add("分/板/助");
		columnNames.add("上场时间");
		
		rows=new Vector();
	}
/*
 * 球员名称，所属球队，参赛场数，先发场数，篮板数，助攻数，在
场时间，投篮命中率，三分命中率，罚球命中率，进攻数，防守数，抢断数，盖
帽数，失误数，犯规数，得分，效率，GmSc 效率值，真实命中率，投篮效率，
篮板率，进攻篮板率，防守篮板率，助攻率，抢断率，盖帽率，失误率，使用率
等，并且可以依据以上数据中的任何一项对全部数据进行升降序操作。
而且可以
通过球员位置（前锋，中锋，后卫），球员联盟（东部，西部及各自分区），排序
依据（得分，篮板，助攻，得分/篮板/助攻（加权比为 1:1:1），盖帽，抢断，犯
规，失误，分钟，效率，投篮，三分，罚球，两双（特指得分、篮板、助攻、抢
断、盖帽中任何两项 ））
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
