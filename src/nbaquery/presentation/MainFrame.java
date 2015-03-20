package nbaquery.presentation;

import javax.swing.JFrame;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JMenuItem;

import nbaquery.logic.IBusinessLogic;


public class MainFrame {

	private JFrame frame;
	JPanel mainPanel;

	IBusinessLogic bls;
	
	/**
	 * Create the application.
	 */
	public MainFrame(IBusinessLogic bls) {
		initialize();
		this.bls = bls;
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 966, 554);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("NBAData");
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu mnNewMenu = new JMenu("\u6587\u4EF6");
		menuBar.add(mnNewMenu);
		
		JMenuItem menuItem = new JMenuItem("\u6DFB\u52A0\u6570\u636E");
		mnNewMenu.add(menuItem);
		
		JMenu menu = new JMenu("\u8BBE\u7F6E");
		menuBar.add(menu);
		
		JMenu menu_1 = new JMenu("...");
		menuBar.add(menu_1);
		frame.getContentPane().setLayout(null);
		
		JButton btnNewButton = new JButton("\u7403\u961F\u6570\u636E");
		btnNewButton.setBounds(0, 54, 100, 85);
		btnNewButton.addActionListener(new TeamListener());
		frame.getContentPane().add(btnNewButton);
		
		JButton button_0= new JButton("\u7403\u5458\u6570\u636E");
		button_0.setBounds(0, 168, 100, 85);
		button_0.addActionListener(new PlayerListener());
		frame.getContentPane().add(button_0);
		
		JButton button_1 = new JButton("\u5176\u4ED6\u529F\u80FD");
		button_1.setBounds(0, 284, 100, 85);
		frame.getContentPane().add(button_1);
		
		mainPanel = new JPanel();
		mainPanel.setBounds(120, 10, 814, 458);
		frame.getContentPane().add(mainPanel);
		mainPanel.setLayout(null);
	}
	
	public void setVisible(boolean visible)
	{
		this.frame.setVisible(visible);
	}
	
	class PlayerListener implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			mainPanel.removeAll();
			PlayerTablePanel d=new PlayerTablePanel(bls);
			mainPanel.add(d);
			mainPanel.paintComponents(mainPanel.getGraphics());
			mainPanel.repaint();
		}
		
	}
	
	class TeamListener implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			mainPanel.removeAll();
			TeamTablePanel d=new TeamTablePanel(bls);
			mainPanel.add(d);
			mainPanel.paintComponents(mainPanel.getGraphics());
			mainPanel.repaint();
		}
		
	}
}
