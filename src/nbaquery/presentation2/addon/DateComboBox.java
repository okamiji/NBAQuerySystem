package nbaquery.presentation2.addon;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class DateComboBox extends JPanel
{
	public final JFrame dateContainer = new JFrame();
	public final JLabel display = new JLabel();
	public final DateSelector dateSelector = new DateSelector();
	
	public final JButton calendar = new JButton();
	public final JButton clear = new JButton();
	
	String season, date;
	
	public DateComboBox()
	{
		dateContainer.setUndecorated(true);
		dateContainer.getContentPane().setBackground(Color.BLACK);
		dateContainer.setAlwaysOnTop(true);
		
		dateContainer.add(dateSelector);
		dateSelector.calendarSelector.table.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent me)
			{
				if(me.getClickCount() == 2)
				{
					dateContainer.setVisible(false);
					season = dateSelector.fromSeason.getText() + "-" + dateSelector.toSeason.getText();
					date = (dateSelector.month + 1) + "-" + dateSelector.calendarSelector.date;
					display.setText(season + " " + date);
					clear.setEnabled(true);
					update();
				}
			}
		});
		this.setLayout(null);
		display.setHorizontalAlignment(JLabel.CENTER);
		this.add(display);
		this.add(clear);
		this.add(calendar);
		
		clear.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				season = null;
				date = null;
				display.setText("所有比赛数据");
				update();
			}
		});
		
		display.setText("所有比赛数据");
		
		calendar.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				if(dateContainer.isVisible())
				{
					dateContainer.setVisible(false);
					clear.setEnabled(true);
				}
				else
				{
					popupDateContainer();
					clear.setEnabled(false);
				}
			}
		});
	}
	
	public void paint(Graphics g)
	{
		super.paint(g);
		clear.setSize(this.getHeight(), this.getHeight());
		clear.setLocation(this.getWidth() - clear.getWidth(), 0);
		
		calendar.setSize(this.getHeight(), this.getHeight());
		calendar.setLocation(clear.getX() - calendar.getWidth(), 0);
		
		display.setSize(calendar.getX(), this.getHeight());
		display.setLocation(0, 0);
	}
	
	public String getSeason()
	{
		return this.season;
	}
	
	public String getDate()
	{
		return this.date;
	}
	
	public void update()
	{
		
	}
	
	public void popupDateContainer()
	{
		dateContainer.setVisible(true);
		dateContainer.setSize(this.getWidth(), (int) (this.getWidth() * 0.7));
		Point loc = this.getLocationOnScreen();
		dateContainer.setLocation(loc.x, loc.y + this.getHeight());
	}
	
	public static void main(String[] arguments) throws Exception
	{
		JFrame jframe = new JFrame();
		jframe.setLayout(null);
		jframe.setSize(600, 480);
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		DateComboBox calendar = new DateComboBox();
		calendar.setLocation(100, 200);
		calendar.setSize(400, 30);
		jframe.add(calendar);
		jframe.setVisible(true);
		while(true)
		{
			jframe.repaint();
			Thread.sleep(10);
		}
	}
}
