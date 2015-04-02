package nbaquery.presentation;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
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
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
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
	boolean isGlobal=true;
	JLabel playerLabel,lblName,lblTeam,lblLeague,lblPosition,playerImageLabel;
	JScrollPane scrollPane;
	Color white = new Color(245,245,245);
	Color background=new Color(33,122,197);
	
	public PlayerTablePanel(final PlayerService ps){
		this.ps = ps;
		setSize(865,570);
		setLayout(null);
		setVisible(true);
		tableModel=new PlayerTableModel();
		table=new JTable(tableModel);
					  
		table.getModel().addTableModelListener(this);
		
		table.setFillsViewportHeight(true);  
		table.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		
		scrollPane = new JScrollPane(table);
		scrollPane.setBounds(14, 63, 600, 500);
		
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		int columncount = this.table.getColumnCount();
        for (int i = 0; i < columncount; i++) {
            this.table.getColumnModel().getColumn(i).setPreferredWidth(95);
        }
       
        table.getTableHeader().setReorderingAllowed(false); 
        table.setColumnSelectionAllowed (false);  
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
                    System.out.println(pick);
                    SearchListener s = new SearchListener();
            		s.actionPerformed((ActionEvent)searchButton.getAction());
                    upDown=!upDown;
                    //System.out.println(upDown);
                    //����ѡ��ģ��  
                    //table.addColumnSelectionInterval(pick, pick);
                }
            });  
        
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener(){	
        	public void valueChanged(ListSelectionEvent e) {
				// TODO Auto-generated method stub
				int row=table.getSelectedRow();
				if(row==-1)
					return;
				//String name=(String) table.getValueAt(row, 0);
				String name = "Beno Udrih";
				String team=(String) table.getValueAt(row, 1);
				String pos=(String) table.getValueAt(row, 29);
				String league=(String) table.getValueAt(row, 30);
				ImageIcon icon=new ImageIcon("D:/����һ����/players/portrait/"+name+".png");
				
				playerImageLabel.setIcon(icon);
				playerImageLabel.repaint();
				lblName.setText(name);
				lblTeam.setText(team);
				lblPosition.setText(pos);
				lblLeague.setText(league);
			}
        }
        );
        table.repaint();        
		
		JPanel searchPanel = new JPanel();
		searchPanel.setBounds(14, 13, 786,40);
		add(searchPanel);
		searchPanel.setLayout(null);
		
		JLabel positionLabel = new JLabel(" ");
		positionLabel.setBounds(168, 0, 700, 30);
		positionLabel.setIcon(new ImageIcon("C:/Users/С��/Desktop/����ҵUI/����.png"));
		
		positionBox = new JComboBox<String>();
		positionBox.setBounds(231, 3, 113, 24);
		positionBox.setOpaque(false);
	    positionBox.setUI(new BasicComboBoxUI() {
	       public void installUI(JComponent comboBox) {
	           super.installUI(positionBox);
	              listBox.setForeground(Color.WHITE);
	              listBox.setSelectionBackground(new Color(0,0,0,0));
	              listBox.setSelectionForeground(Color.BLACK);
	            }
	          
	            /**
	             * �÷��������ұߵİ�ť
	             */
	          protected JButton createArrowButton() {
	              return null;
	            }
	        });
	    searchPanel.add(positionBox);
	    
		leagueBox = new JComboBox<String>();
		leagueBox.setBounds(410, 3, 113, 24);
		leagueBox.setUI(new BasicComboBoxUI() {
		       public void installUI(JComponent comboBox) {
		           super.installUI(leagueBox);
		              listBox.setForeground(Color.WHITE);
		              listBox.setSelectionBackground(new Color(0,0,0,0));
		              listBox.setSelectionForeground(Color.BLACK);
		            }
		          
		            /**
		             * �÷��������ұߵİ�ť
		             */
		          protected JButton createArrowButton() {
		              return null;
		            }
		        });
		searchPanel.add(leagueBox);
					
		typeBox = new JComboBox<String>();
		typeBox.setBounds(595, 3, 120, 24);
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
					
		searchButton = new JButton("",new ImageIcon("C:/Users/С��/Desktop/����ҵUI/search.png"));
		searchButton.setBounds(729, 3, 35, 40);
		searchButton.setFocusPainted(false);
		searchButton.setBorderPainted(false);
		searchButton.setContentAreaFilled(false);
		searchPanel.add(searchButton);
		
		searchField = new JTextField("����Ҫ��ѯ����Ϣ");
		searchField.setBounds(2, 2, 160, 30);
		JLabel searchLabel=new JLabel("");
		searchLabel.setIcon(new ImageIcon("C:/Users/С��/Desktop/����ҵUI/searchfield.png"));
		searchLabel.setBounds(0, 0, 165, 34);
		searchPanel.add(searchField);
		searchPanel.add(searchLabel);
		searchField.setColumns(10);
		
		searchPanel.setOpaque(false);
		
		
		
		JPanel playerPanel = new JPanel();
		playerPanel.setBounds(615, 103, 240, 470);
		playerPanel.setOpaque(false);
		playerPanel.setLayout(null);
		add(playerPanel);
		
		playerLabel = new JLabel("");
		playerLabel.setBounds(0, 0, 240, 470);
		playerLabel.setIcon(new ImageIcon("C:/Users/С��/Desktop/����ҵUI/��Ա��Ϣ.png"));
		playerPanel.add(playerLabel);
		
		playerImageLabel = new JLabel("");
		playerImageLabel.setBounds(0,65,230,200);
		playerPanel.add(playerImageLabel);
		
		lblName = new JLabel("Name");
		lblName.setBounds(60, 301, 300, 18);
		playerPanel.add(lblName);
		
		lblTeam = new JLabel("Team");
		lblTeam.setBounds(60, 334, 300, 18);
		playerPanel.add(lblTeam);

		lblLeague = new JLabel("League");
		lblLeague.setBounds(60, 367, 99, 18);
		playerPanel.add(lblLeague);
		
		lblPosition = new JLabel("Position");
		lblPosition.setBounds(60, 400, 99, 18);
		playerPanel.add(lblPosition);
		
		JLabel tableLabel=new JLabel("");
		tableLabel.setIcon(new ImageIcon("C:/Users/С��/Desktop/����ҵUI/tableShadow.png"));
		tableLabel.setBounds(9, 58, 610, 510);
		
		searchField.addFocusListener(new ClickAdapter());
		searchButton.addActionListener(new ClickListener());
		
		boxInitialization();
		typeBox.setOpaque(false);
		add(scrollPane);
		this.add(tableLabel);
		SearchListener s = new SearchListener();
		s.actionPerformed((ActionEvent)searchButton.getAction());
		searchPanel.add(positionLabel);
		this.table.setRowHeight(30);
	}
	
	/*public void resizeTable(boolean bool) { 
        Dimension containerwidth = null; 
        if (!bool) { 
            //��ʼ��ʱ����������СΪ��ѡ��С��ʵ�ʴ�СΪ0 
            containerwidth = scrollPane.getPreferredSize(); 
        } else { 
            //������ʾ�������������С�ı䣬ʹ��ʵ�ʴ�С��������ѡ��С 
            containerwidth = scrollPane.getSize(); 
        } 
        //������������ 
        int allwidth = table.getIntercellSpacing().width; 
        for (int j = 0; j < table.getColumnCount(); j++) { 
           
            //�����ͷ�Ŀ�� 
            int headerwidth = table. 
              getTableHeader(). 
              getDefaultRenderer().getTableCellRendererComponent( 
              table, table.getColumnModel(). 
              getColumn(j).getIdentifier(), false, false, 
              -1, j).getPreferredSize().width; 
            //�����п� 
            table.getColumnModel(). 
              getColumn(j).setPreferredWidth(headerwidth); 
            //�����������ȸ�ֵ���ǵ�Ҫ���ϵ�Ԫ��֮����������1������ 
            allwidth += table.getIntercellSpacing().width; 
        } 
        allwidth += table.getIntercellSpacing().width; 
        //������ʵ�ʿ�ȴ�С�������Ŀ�ȣ�����Ҫ�����ֶ���Ӧ�������ñ������Ӧ 
        if (allwidth > containerwidth.width) { 
            table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); 
        } else { 
            table. 
              setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS); 
        } 
}*/
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
		//if()
		SearchListener s = new SearchListener();
		s.actionPerformed((ActionEvent)searchButton.getAction());
	}
}

class SearchListener implements ActionListener{

	public void actionPerformed(ActionEvent arg0) {
		position=(String) positionBox.getSelectedItem();
		league=(String) leagueBox.getSelectedItem();
		
		if(((String)typeBox.getSelectedItem()).equals("ȫ������"))
			isGlobal=true;
		else
			isGlobal=false;	
		
		strs=ps.searchForPlayers(isGlobal,head, upDown, position, league);
		updateTable(strs);
	}
}

public void boxInitialization(){
	
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

