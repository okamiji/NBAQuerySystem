package nbaquery.presentation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JButton;

import nbaquery.logic.IBusinessLogic;

@SuppressWarnings("serial")
public class PlayerTablePanel  extends JPanel implements TableModelListener {
	JTable table;
	PlayerTableModel tableModel;
	PlayerTablePanel panel=this;
	JComboBox<String> positionBox,headBox,leagueBox,upDownBox,typeBox;
	IBusinessLogic bls;
	String[][] strs=null;
	
	public PlayerTablePanel(IBusinessLogic bls){
		this.bls = bls;
		setSize(800,420);
		tableModel=new PlayerTableModel();
		table=new JTable(tableModel);
		table.getModel().addTableModelListener(this);
		setLayout(null);

		setVisible(true);
		table.setFillsViewportHeight(true);  
		table.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(10, 13, 600, 397);
		add(scrollPane);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		JLabel label = new JLabel("\u6392\u5E8F\u4F9D\u636E");
		label.setBounds(624, 190, 72, 18);
		add(label);
		
		JLabel lblNewLabel = new JLabel("\u7403\u5458\u4F4D\u7F6E");
		lblNewLabel.setBounds(624, 13, 72, 18);
		add(lblNewLabel);

		JLabel label_1 = new JLabel("\u7403\u5458\u8054\u76DF");
		label_1.setBounds(624, 68, 72, 18);
		add(label_1);
		
		
		headBox = new JComboBox<String>();
		headBox.setBounds(624, 221, 82, 24);
		add(headBox);
	
		positionBox = new JComboBox<String>();
		positionBox.setBounds(624, 37, 105, 24);
		add(positionBox);
		
		leagueBox = new JComboBox<String>();
		leagueBox.setBounds(624, 92, 105, 24);
		add(leagueBox);
		
		upDownBox = new JComboBox<String>();
		upDownBox.setBounds(718, 221, 68, 24);
		add(upDownBox);
		
		JButton button = new JButton("\u7B5B\u9009");
		button.setBounds(624, 292, 105, 27);
		button.addActionListener(new searchListener());
		add(button);
		
		typeBox = new JComboBox<String>();
		typeBox.setBounds(624, 153, 105, 24);
		add(typeBox);
		
		JLabel label_2 = new JLabel("\u6570\u636E\u7C7B\u578B");
		label_2.setBounds(624, 129, 72, 18);
		add(label_2);
		int columncount = this.table.getColumnCount();
        for (int i = 1; i < columncount; i++) {
            this.table.getColumnModel().getColumn(i).setPreferredWidth(80);
        }
        table.getTableHeader().setReorderingAllowed(false); 
        table.repaint();
		boxInitialization();
	}

	public void tableChanged(TableModelEvent e) {
		// TODO Auto-generated method stub
		int row = e.getFirstRow();  
        int column = e.getColumn();  
        TableModel model = (TableModel)e.getSource();  
        String columnName = model.getColumnName(column);  
        Object data = model.getValueAt(row, column); 
        
	}
	

	public void updateTable(String[][] strs){
		while(tableModel.getRowCount()>0){
			 tableModel.removeRow(tableModel.getRowCount()-1);
		}
		if(strs!=null)
			for(int i=0;i<strs.length;i++){
				Vector<String> v=new Vector<String>();
				for(int j=0;j<strs[i].length;j++){
					v.add(strs[i][j]);}
				tableModel.addRow(v);
			}
		table.revalidate();
		repaint();
	}
	
class searchListener implements ActionListener{

	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		String head=null,position=null,league=null;
		boolean upDown=false;
		boolean type=false;
		head=(String) headBox.getSelectedItem();
		position=(String) positionBox.getSelectedItem();
		league=(String) leagueBox.getSelectedItem();
		
		if(((String)upDownBox.getSelectedItem()).equals("降序"))
			upDown=true;
		else
			upDown=false;
		
		if(((String)typeBox.getSelectedItem()).equals("全局数据"))
			upDown=true;
		else
			upDown=false;
		
		strs=bls.searchForPlayers(type,head, upDown, position, league);
		updateTable(strs);
	}
	
}
	
public void boxInitialization(){
	headBox.addItem("姓名");
	headBox.addItem("球队");
	headBox.addItem("参赛场数");
	headBox.addItem("先发场数");
	headBox.addItem("篮板");
	headBox.addItem("助攻");
	headBox.addItem("在场时间");
	headBox.addItem("投篮命中率");
	headBox.addItem("三分命中率");
	headBox.addItem("罚球命中率");
	headBox.addItem("进攻");
	headBox.addItem("防守");
	headBox.addItem("抢断");
	headBox.addItem("盖帽");
	headBox.addItem("失误");
	headBox.addItem("犯规");
	headBox.addItem("得分");
	headBox.addItem("效率");
	headBox.addItem("GmSc");
	headBox.addItem("真实命中率");
	headBox.addItem("投篮效率");
	headBox.addItem("篮板率");
	headBox.addItem("进攻篮板率");
	headBox.addItem("防守篮板率");
	headBox.addItem("助攻率");
	headBox.addItem("抢断率");
	headBox.addItem("盖帽率");
	headBox.addItem("失误率");
	headBox.addItem("使用率");
	headBox.addItem("球员位置");
	headBox.addItem("联盟");
	
	upDownBox.addItem("升序");
	upDownBox.addItem("降序");
	
	typeBox.addItem("全局数据");
	typeBox.addItem("场均数据");
	
	positionBox.addItem("全部");
	positionBox.addItem("前锋");
	positionBox.addItem("中锋");
	positionBox.addItem("后卫");
	
	leagueBox.addItem("全部");
	leagueBox.addItem("东部");
	leagueBox.addItem("西部");
}

}

