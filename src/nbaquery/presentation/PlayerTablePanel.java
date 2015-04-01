package nbaquery.presentation;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.GroupLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JButton;

import nbaquery.data.Column;
import nbaquery.data.Table;
import nbaquery.logic.IBusinessLogic;
import nbaquery.presentation.MainFrame.PlayerListener;
import nbaquery.presentation.MainFrame.TeamListener;
import nbaqueryBusinessLogicService.PlayerService;

import javax.swing.JTextField;

@SuppressWarnings("serial")
public class PlayerTablePanel  extends JPanel implements TableModelListener {
	JTable table;
	PlayerTableModel tableModel;
	PlayerTablePanel panel=this;
	JComboBox<String> positionBox,leagueBox,typeBox;
	PlayerService ps;
	String[][] strs=null;
	private JTextField searchField;
	JButton searchButton;
	String head=null,position=null,league=null;
	boolean upDown=true;
	boolean type=false;
	JLabel playerLabel,lblName,lblTeam;
	
	public PlayerTablePanel(final PlayerService ps){
		this.ps = ps;
		setSize(900,640);
		tableModel=new PlayerTableModel();
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
        table.setColumnSelectionAllowed (false);  
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
                    SearchListener s = new SearchListener();
            		s.actionPerformed((ActionEvent)searchButton.getAction());
                    upDown=!upDown;
                    //System.out.println(upDown);
                    //设置选择模型  
                    //table.addColumnSelectionInterval(pick, pick);
                }
            });  
        
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener(){	
        	public void valueChanged(ListSelectionEvent e) {
				// TODO Auto-generated method stub
				int row=table.getSelectedRow();
				String name=(String) table.getValueAt(row, 0);
				String team=(String) table.getValueAt(row, 1);
				playerLabel.setIcon(new ImageIcon("D:/data/players/portrait/"+name+".png"));
				playerLabel.repaint();
				lblName.setText(name);
				lblTeam.setText(team);
			}
        }
        );
        table.repaint();        
		
		JPanel searchPanel = new JPanel();
		searchPanel.setBounds(14, 13, 600, 40);
		add(searchPanel);
		searchPanel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("\u4F4D\u7F6E");
		lblNewLabel.setBounds(150, 3, 30, 18);
		searchPanel.add(lblNewLabel);
		
			positionBox = new JComboBox<String>();
			positionBox.setBounds(194, 0, 71, 24);
			searchPanel.add(positionBox);
			
					JLabel label_1 = new JLabel("\u8054\u76DF");
					label_1.setBounds(279, 3, 30, 18);
					searchPanel.add(label_1);
					
					leagueBox = new JComboBox<String>();
					leagueBox.setBounds(323, 0, 71, 24);
					searchPanel.add(leagueBox);
					
					typeBox = new JComboBox<String>();
					typeBox.setBounds(452, 0, 71, 24);
					searchPanel.add(typeBox);
					
					JLabel label_2 = new JLabel("\u7C7B\u578B");
					label_2.setBounds(408, 3, 30, 18);
					searchPanel.add(label_2);
					
					searchButton = new JButton("\u68C0\u7D22");
					searchButton.setBounds(537, 0, 63, 27);
					searchPanel.add(searchButton);
					
					searchField = new JTextField("输入要查询的信息");
					searchField.setBounds(0, 0, 126, 27);
					searchPanel.add(searchField);
					searchField.setColumns(10);
					
					JPanel playerPanel = new JPanel();
					playerPanel.setBounds(628, 13, 230, 430);
					add(playerPanel);
					playerPanel.setLayout(null);
					
					playerLabel = new JLabel("");
					playerLabel.setBounds(0, 0, 230, 245);
					playerPanel.add(playerLabel);
					
					JLabel lblNewLabel_1 = new JLabel("\u7403\u5458\u59D3\u540D");
					lblNewLabel_1.setBounds(0, 296, 99, 18);
					playerPanel.add(lblNewLabel_1);
					
					JLabel label = new JLabel("\u6240\u5C5E\u7403\u961F");
					label.setBounds(0, 327, 99, 18);
					playerPanel.add(label);
					
					lblName = new JLabel("Name");
					lblName.setBounds(117, 296, 99, 18);
					playerPanel.add(lblName);
					
					lblTeam = new JLabel("Team");
					lblTeam.setBounds(117, 327, 99, 18);
					playerPanel.add(lblTeam);
					
					
					searchField.addFocusListener(new ClickAdapter());
					searchButton.addActionListener(new ClickListener());
		boxInitialization();
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
		position=(String) positionBox.getSelectedItem();
		league=(String) leagueBox.getSelectedItem();
		
		if(((String)typeBox.getSelectedItem()).equals("全局数据"))
			upDown=true;
		else
			upDown=false;
		
		strs=ps.searchForPlayers(type,head, upDown, position, league);
		upDown=false;
		
		updateTable(strs);
	}
}

public void boxInitialization(){
	
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

