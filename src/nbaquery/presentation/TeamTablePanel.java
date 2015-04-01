package nbaquery.presentation;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.plaf.basic.BasicComboBoxUI;
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
	boolean type=true;
	
	
	public TeamTablePanel(final TeamService ts){
		this.ts = ts;
		setSize(865,570);
		tableModel=new TeamTableModel();
		table=new JTable(tableModel);
		table.getModel().addTableModelListener(this);
		setLayout(null);

		setVisible(true);
		table.setFillsViewportHeight(true);  
		table.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(14, 63, 780, 500);
		add(scrollPane);
		this.table.setRowHeight(30);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		int columncount = this.table.getColumnCount();
        for (int i = 1; i < columncount; i++) {
            this.table.getColumnModel().getColumn(i).setPreferredWidth(95);
        }
        table.getTableHeader().setReorderingAllowed(false); 
        table.setColumnSelectionAllowed (true);  
        table.setRowSelectionAllowed (true);  
        final JTableHeader header = table.getTableHeader();  
        //��ͷ���Ӽ��� 
        header.addMouseListener (new MouseAdapter() {  
                public void mouseReleased (MouseEvent e) {  
                    if (! e.isShiftDown())  
                        table.clearSelection();  
                    //��ȡ�����������  
                    int pick = header.columnAtPoint(e.getPoint());  
                    head=table.getColumnName(pick);
                    strs=ts.searchForTeams(type,head,upDown);
                    updateTable(strs);
                    upDown=!upDown;
                    //System.out.println(upDown);
                    //����ѡ��ģ��  
                    table.addColumnSelectionInterval (pick, pick);  
                }  
            });  
        table.repaint();
		
		JPanel searchPanel = new JPanel();
		searchPanel.setBounds(14, 13, 786, 40);
		searchPanel.setOpaque(false);//͸��
		add(searchPanel);
		searchPanel.setLayout(null);
		
		JLabel positionLabel = new JLabel(" ");
		positionLabel.setBounds(168, 0, 700, 30);
		positionLabel.setIcon(new ImageIcon("C:/Users/С��/Desktop/����ҵUI/����2.png"));
		
		searchField = new JTextField("����Ҫ��ѯ����Ϣ");
		searchField.setBounds(2, 2, 160, 30);
		JLabel searchLabel=new JLabel("");
		searchLabel.setIcon(new ImageIcon("C:/Users/С��/Desktop/����ҵUI/searchfield.png"));
		searchLabel.setBounds(0, 0, 165, 34);
		searchPanel.add(searchField);
		searchPanel.add(searchLabel);
		searchField.setColumns(10);
		searchField.addFocusListener(new ClickAdapter());
		
		searchButton = new JButton("",new ImageIcon("C:/Users/С��/Desktop/����ҵUI/GUI/search.png"));
		searchButton.setBounds(729, 3, 35, 40);
		searchButton.setFocusPainted(false);
		searchButton.setBorderPainted(false);
		searchButton.setContentAreaFilled(false);
		searchPanel.add(searchButton);
		
		typeBox = new JComboBox<String>();
		typeBox.setBounds(229, 2, 118, 24);
		typeBox.setUI(new BasicComboBoxUI() {
		       public void installUI(JComponent comboBox) {
		           super.installUI(typeBox);
		              listBox.setForeground(Color.WHITE);
		              listBox.setSelectionBackground(new Color(0,0,0,0));
		              listBox.setSelectionForeground(Color.BLACK);
		            }
		          
		            /**
		             * �÷��������ұߵİ�ť
		             */
		          protected JButton createArrowButton() {
		              return null;
		        	  //return super.createArrowButton();
		            }
		        });
		searchPanel.add(typeBox);
		boxInitialization();
		searchButton.addActionListener(new ClickListener());
		
		JLabel tableLabel=new JLabel("");
		tableLabel.setIcon(new ImageIcon("C:/Users/С��/Desktop/����ҵUI/tableShadow3.png"));
		tableLabel.setBounds(9, 58, 790, 510);
		this.add(tableLabel);
		
		SearchListener s = new SearchListener();
		s.actionPerformed((ActionEvent)searchButton.getAction());
		
		searchPanel.add(positionLabel);
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
		if (searchField.getText().equals("����Ҫ��ѯ����Ϣ"))
			searchField.setText("");
	}

	@Override
	public void focusLost(FocusEvent arg0) {
		// TODO Auto-generated method stub
		if (searchField.getText().equals(""))
				searchField.setText("����Ҫ��ѯ����Ϣ");
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
		if(((String)typeBox.getSelectedItem()).equals("ȫ������"))
			type=true;
		else
			type=false;
		strs=ts.searchForTeams(type,head, upDown);
		updateTable(strs);
	}
}
	
public void boxInitialization(){
	typeBox.addItem("ȫ������");
	typeBox.addItem("��������");
}
}

