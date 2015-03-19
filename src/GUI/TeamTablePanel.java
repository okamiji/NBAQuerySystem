package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
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




public class TeamTablePanel  extends JPanel implements TableModelListener {
	JTable table;
	TeamTableModel tableModel;
	TeamTablePanel panel=this;
	JComboBox headBox,upDownBox,typeBox;
	Presentation_Stub ps=new Presentation_Stub();
	String[][] strs=null;
	
	public TeamTablePanel(){
		setSize(800,420);
		tableModel=new TeamTableModel();
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

		headBox = new JComboBox();
		headBox.setBounds(624, 221, 82, 24);
		add(headBox);
	
		upDownBox = new JComboBox();
		upDownBox.setBounds(718, 221, 68, 24);
		add(upDownBox);
		
		JButton button = new JButton("\u7B5B\u9009");
		button.setBounds(624, 292, 105, 27);
		button.addActionListener(new searchListener());
		add(button);
		
		typeBox = new JComboBox();
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
				Vector v=new Vector();
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
		String head=null;
		boolean upDown=false;
		boolean type=false;
		head=(String) headBox.getSelectedItem();
		
		if(((String)upDownBox.getSelectedItem()).equals("����"))
			upDown=true;
		else
			upDown=false;
		
		if(((String)typeBox.getSelectedItem()).equals("ȫ������"))
			upDown=true;
		else
			upDown=false;
		
		strs=ps.searchForTeams(type,head, upDown);
		updateTable(strs);
	}
	
}
	
public void boxInitialization(){
	headBox.addItem("�������");
	headBox.addItem("��������");
	headBox.addItem("Ͷ��������");
	headBox.addItem("Ͷ�����ִ���");
	headBox.addItem("����������");
	headBox.addItem("���ֳ�����");
	headBox.addItem("����������");
	headBox.addItem("����������");
	headBox.addItem("����������");
	headBox.addItem("������");
	headBox.addItem("������");
	headBox.addItem("������");
	headBox.addItem("��ñ��");
	headBox.addItem("ʧ����");
	headBox.addItem("������");
	headBox.addItem("�����÷�");
	headBox.addItem("Ͷ��������");
	headBox.addItem("����������");
	headBox.addItem("����������");
	headBox.addItem("ʤ��");
	headBox.addItem("�����غ�");
	headBox.addItem("����Ч��");
	headBox.addItem("����Ч��");
	headBox.addItem("����Ч��");
	headBox.addItem("����Ч��");
	headBox.addItem("����Ч��");
	
	upDownBox.addItem("����");
	upDownBox.addItem("����");
	
	typeBox.addItem("ȫ������");
	typeBox.addItem("��������");
	
}

}

