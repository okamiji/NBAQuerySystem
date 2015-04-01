package nbaquery.presentation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JButton;

import nbaquery.data.Table;
import nbaquery.logic.IBusinessLogic;
import nbaquery.presentation.PlayerTablePanel.ClickAdapter;
import nbaquery.presentation.PlayerTablePanel.ClickListener;
import nbaquery.presentation.PlayerTablePanel.SearchListener;
import nbaqueryBusinessLogicService.TeamService;

@SuppressWarnings("serial")
public class TeamTablePanel  extends JPanel implements TableModelListener {
	JTable table;
	TeamTableModel tableModel;
	TeamTablePanel panel=this;
	JComboBox<String> typeBox;
	TeamService ts;
	String[][] strs=null;
	JButton button,searchButton;
	JTextField searchField;

	String head=null;
	boolean upDown=true;
	boolean type=false;
	
	
	public TeamTablePanel(final TeamService ts){
		this.ts = ts;
		setSize(900,640);
		tableModel=new TeamTableModel();
		table=new JTable(tableModel);
		table.getModel().addTableModelListener(this);
		setLayout(null);

		setVisible(true);
		table.setFillsViewportHeight(true);  
		table.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(14, 53, 600, 390);
		add(scrollPane);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		int columncount = this.table.getColumnCount();
        for (int i = 1; i < columncount; i++) {
            this.table.getColumnModel().getColumn(i).setPreferredWidth(80);
        }
        table.getTableHeader().setReorderingAllowed(false); 
        table.setColumnSelectionAllowed (true);  
        table.setRowSelectionAllowed (true);  
        final JTableHeader header = table.getTableHeader();  
        //表头增加监听 
        header.addMouseListener (new MouseAdapter() {  
                public void mouseReleased (MouseEvent e) {  
                    if (! e.isShiftDown())  
                        table.clearSelection();  
                    //获取点击的列索引  
                    int pick = header.columnAtPoint(e.getPoint());  
                    head=table.getColumnName(pick);
                    strs=ts.searchForTeams(type,head,upDown);
                    updateTable(strs);
                    upDown=!upDown;
                    //System.out.println(upDown);
                    //设置选择模型  
                    table.addColumnSelectionInterval (pick, pick);  
                }  
            });  
        table.repaint();
		
		JPanel searchPanel = new JPanel();
		searchPanel.setBounds(14, 13, 593, 40);
		add(searchPanel);
		searchPanel.setLayout(null);
		
		searchField = new JTextField();
		searchField.setText("\u8F93\u5165\u8981\u67E5\u8BE2\u7684\u4FE1\u606F");
		searchField.setBounds(0, 0, 126, 27);
		searchPanel.add(searchField);
		searchField.setColumns(10);
		searchField.addFocusListener(new ClickAdapter());
		
		searchButton = new JButton("\u68C0\u7D22");
		searchButton.setBounds(530, 0, 63, 27);
		searchPanel.add(searchButton);
		
		typeBox = new JComboBox<String>();
		typeBox.setBounds(201, 1, 105, 24);
		searchPanel.add(typeBox);
		boxInitialization();
		JLabel label_2 = new JLabel("\u6570\u636E\u7C7B\u578B");
		label_2.setBounds(134, 4, 72, 18);
		searchPanel.add(label_2);
		searchButton.addActionListener(new ClickListener());
		
		SearchListener s = new SearchListener();
		s.actionPerformed((ActionEvent)searchButton.getAction());
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

class ClickAdapter implements FocusListener {

	@Override
	public void focusGained(FocusEvent arg0) {
		// TODO Auto-generated method stub
		if (searchField.getText().equals("输入要查询的信息"))
			searchField.setText("");
	}

	@Override
	public void focusLost(FocusEvent arg0) {
		// TODO Auto-generated method stub
		if (searchField.getText().equals(""))
				searchField.setText("输入要查询的信息");
	}
}

class ClickListener implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		SearchListener s = new SearchListener();
		s.actionPerformed((ActionEvent)searchButton.getAction());
	}
}

class SearchListener implements ActionListener{

	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		if(((String)typeBox.getSelectedItem()).equals("全局数据"))
			upDown=true;
		else
			upDown=false;
		strs=ts.searchForTeams(type,head, upDown);
		updateTable(strs);
	}
	
}
	
public void boxInitialization(){
	typeBox.addItem("全局数据");
	typeBox.addItem("场均数据");
}
}

