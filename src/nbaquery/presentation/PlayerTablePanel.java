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
	boolean type=false;
	JLabel playerLabel,lblName,lblTeam;
	JScrollPane scrollPane;
	Color white = new Color(245,245,245);
	Color background=new Color(33,122,197);
	
	public PlayerTablePanel(final PlayerService ps){
	
		this.ps = ps;
		setSize(800,570);
		setLayout(null);
		setVisible(true);
		tableModel=new PlayerTableModel();
		table=new JTable(tableModel){ 
			// ����jtable�ĵ�Ԫ��Ϊ͸����
			   public Component prepareRenderer(TableCellRenderer renderer,
					     int row, int column) {
					    Component c = super.prepareRenderer(renderer, row, column);
					    if (c instanceof JComponent) {
					     ((JComponent) c).setOpaque(false);
					    }
					    return c;
					   }
					  };
					  
		table.getModel().addTableModelListener(this);
		
		table.setFillsViewportHeight(true);  
		table.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		
		scrollPane = new JScrollPane(table);
		scrollPane.setBounds(14, 53, 600, 500);
		
		DefaultTableCellRenderer render = new DefaultTableCellRenderer();
		render.setOpaque(false);
		table.setDefaultRenderer(Object.class, render);
		//������ʾ��Χ
		Dimension viewSize = new Dimension();
		viewSize.width = table.getColumnModel().getTotalColumnWidth();;
		viewSize.height = 10*table.getRowHeight();
		table.setPreferredScrollableViewportSize(viewSize);
		//��JScrollPane����Ϊ͸��
		scrollPane.getViewport().setOpaque(false);  //jScrollPanel Ϊtable��ŵ�������һ����Swing��    //  �����ʱ�����Զ����ɣ�ԭ����Ϊ��jScrollPane1 = new javax.swing.JScrollPane();
		scrollPane.setOpaque(false);     //���м��viewport����Ϊ͸��
		scrollPane.setViewportView(table); //װ�ر�� 
		
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
		searchPanel.setBounds(14, 13, 844,40);
		//searchPanel.setOpaque(false);//͸��
		add(searchPanel);
		searchPanel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("\u4F4D\u7F6E");
		lblNewLabel.setBounds(169, 3, 71, 18);
		searchPanel.add(lblNewLabel);
		
		
		positionBox = new JComboBox<String>();
		positionBox.setBounds(218, 0, 100, 24);
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
	                return super.createArrowButton();
	            }
	        });
	        searchPanel.add(positionBox);
		
		
		
		
		JLabel label_1 = new JLabel("\u8054\u76DF");
		label_1.setBounds(279, 3, 30, 18);
		searchPanel.add(label_1);
					
		leagueBox = new JComboBox<String>();
		leagueBox.setBounds(332, 0, 100, 24);
		searchPanel.add(leagueBox);
					
		typeBox = new JComboBox<String>();
		typeBox.setBounds(523, 0, 100, 24);
		searchPanel.add(typeBox);
					
		JLabel label_2 = new JLabel("\u7C7B\u578B");
		label_2.setBounds(446, 3, 63, 18);
		searchPanel.add(label_2);
					
		searchButton = new JButton("\u68C0\u7D22");
		searchButton.setBounds(767, 3, 63, 27);
		searchPanel.add(searchButton);
					
		searchField = new JTextField("����Ҫ��ѯ����Ϣ");
		searchField.setBounds(0, 0, 158, 27);
		searchPanel.add(searchField);
		searchField.setColumns(10);
		
		searchPanel.setOpaque(false);
		
		JPanel playerPanel = new JPanel();
		playerPanel.setBounds(628, 53, 230, 430);
		playerPanel.setOpaque(false);
		playerPanel.setLayout(null);
		add(playerPanel);
		
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
		typeBox.setOpaque(false);
		add(scrollPane);
		SearchListener s = new SearchListener();
		s.actionPerformed((ActionEvent)searchButton.getAction());
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
		SearchListener s = new SearchListener();
		s.actionPerformed((ActionEvent)searchButton.getAction());
	}
}

class SearchListener implements ActionListener{

	public void actionPerformed(ActionEvent arg0) {
		position=(String) positionBox.getSelectedItem();
		league=(String) leagueBox.getSelectedItem();
		
		if(((String)typeBox.getSelectedItem()).equals("ȫ������"))
			upDown=true;
		else
			upDown=false;
		
		strs=ps.searchForPlayers(type,head, upDown, position, league);
		upDown=false;
		
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

