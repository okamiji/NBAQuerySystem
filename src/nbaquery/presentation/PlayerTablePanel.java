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
		
		if(((String)upDownBox.getSelectedItem()).equals("����"))
			upDown=true;
		else
			upDown=false;
		
		if(((String)typeBox.getSelectedItem()).equals("ȫ������"))
			upDown=true;
		else
			upDown=false;
		
		strs=bls.searchForPlayers(type,head, upDown, position, league);
		updateTable(strs);
	}
	
}
	
public void boxInitialization(){
	headBox.addItem("����");
	headBox.addItem("���");
	headBox.addItem("��������");
	headBox.addItem("�ȷ�����");
	headBox.addItem("����");
	headBox.addItem("����");
	headBox.addItem("�ڳ�ʱ��");
	headBox.addItem("Ͷ��������");
	headBox.addItem("����������");
	headBox.addItem("����������");
	headBox.addItem("����");
	headBox.addItem("����");
	headBox.addItem("����");
	headBox.addItem("��ñ");
	headBox.addItem("ʧ��");
	headBox.addItem("����");
	headBox.addItem("�÷�");
	headBox.addItem("Ч��");
	headBox.addItem("GmSc");
	headBox.addItem("��ʵ������");
	headBox.addItem("Ͷ��Ч��");
	headBox.addItem("������");
	headBox.addItem("����������");
	headBox.addItem("����������");
	headBox.addItem("������");
	headBox.addItem("������");
	headBox.addItem("��ñ��");
	headBox.addItem("ʧ����");
	headBox.addItem("ʹ����");
	headBox.addItem("��Աλ��");
	headBox.addItem("����");
	
	upDownBox.addItem("����");
	upDownBox.addItem("����");
	
	typeBox.addItem("ȫ������");
	typeBox.addItem("��������");
	
	positionBox.addItem("ȫ��");
	positionBox.addItem("ǰ��");
	positionBox.addItem("�з�");
	positionBox.addItem("����");
	
	leagueBox.addItem("ȫ��");
	leagueBox.addItem("����");
	leagueBox.addItem("����");
}

}

