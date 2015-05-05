package nbaquery.presentation3.match;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;

import nbaquery.data.Image;
import nbaquery.data.Row;
import nbaquery.data.Table;
import nbaquery.presentation.resource.JSVGComponentResource;
import nbaquery.presentation3.DetailedInfoContainer;

@SuppressWarnings("serial")
public class MatchComponent extends Component
{
	int match_id;
	
	String host_name_abbr;
	String guest_name_abbr;
	
	Component host_logo;
	Component guest_logo;
	
	String host_score;
	String guest_score;
	
	String match_season;
	String match_date;
	
	boolean host_win, shouldStack;
	DetailedInfoContainer infoContainer;
	Row match;
	
	public MatchComponent(DetailedInfoContainer infoContainer, Row match, boolean shouldStack)
	{
		this.shouldStack = shouldStack;
		this.infoContainer = infoContainer;
		this.match = match;
	
		Table matchTable = match.getDeclaredTable();
		match_id = (Integer)matchTable.getColumn("match_id").getAttribute(match);
		
		host_name_abbr = (String)matchTable.getColumn("match_host_abbr").getAttribute(match);
		guest_name_abbr = (String)matchTable.getColumn("match_guest_abbr").getAttribute(match);
		
		Image host_logo = (Image) matchTable.getColumn("match_host_image").getAttribute(match);
		Image guest_logo = (Image) matchTable.getColumn("match_guest_image").getAttribute(match);
		
		if(host_logo != null) this.host_logo = JSVGComponentResource.createJSVGComponent(host_logo.toString());
		if(guest_logo != null) this.guest_logo = JSVGComponentResource.createJSVGComponent(guest_logo.toString());
		
		int host_score_value = (Integer) matchTable.getColumn("match_host_score").getAttribute(match);
		int guest_score_value = (Integer) matchTable.getColumn("match_guest_score").getAttribute(match);
		
		host_score = Integer.toString(host_score_value);
		guest_score = Integer.toString(guest_score_value);
		
		host_win = host_score_value >= guest_score_value; 
		
		match_season = (String) matchTable.getColumn("match_season").getAttribute(match);
		match_date = (String) matchTable.getColumn("match_date").getAttribute(match);
		timeLabel.setText(match_season.concat(" / ").concat(match_date));
		
		this.addMouseListener(mouseAdapter);
	}
	
	public final MouseAdapter mouseAdapter = new MouseAdapter()
	{
		public void mouseClicked(MouseEvent me)
		{
			infoContainer.displayMatchInfo(match, shouldStack);
		}
	};
	
	public int padding = 4;
	public final JLabel scoreLabel = new JLabel();
	public final JLabel nameLabel = new JLabel();
	public final JLabel timeLabel = new JLabel();
	
	public static Font scoreLabelFont;
	public static Font plainTextFont;
	
	public boolean shouldDisplayTime = false;
	
	public void paint(Graphics g)
	{
		int width = this.getWidth();
		int height = this.getHeight();
		
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, width - 1, height - 1);
		
		Graphics host_logo_area = g.create(padding, padding, height - padding * 2, height - padding * 2);
		host_logo_area.setColor(Color.WHITE);
		host_logo_area.fillRect(0, 0, height - padding * 2, height - padding * 2);
		
		if(host_logo != null) synchronized(host_logo)
		{
			host_logo.setSize(height - padding * 2, height - padding * 2);
			host_logo.repaint();
			host_logo.paint(host_logo_area);
		}
		
		Graphics guest_logo_area = g.create(width - height + padding, padding, height - padding * 2, height - padding * 2);
		guest_logo_area.setColor(Color.WHITE);
		guest_logo_area.fillRect(0, 0, height - padding * 2, height - padding * 2);
		
		if(guest_logo != null) synchronized(guest_logo)
		{
			guest_logo.setSize(height - padding * 2, height - padding * 2);
			guest_logo.repaint();
			guest_logo.paint(guest_logo_area);
		}
		
		this.scoreLabel.setSize((width / 2) - height - padding * 2, height / 2);
		
		Graphics host_score_area = g.create(padding + height - padding * 2, height / 2, this.scoreLabel.getWidth(), this.scoreLabel.getHeight());
		Graphics guest_score_area = g.create(width - height + padding - this.scoreLabel.getWidth(), height / 2, this.scoreLabel.getWidth(), this.scoreLabel.getHeight());
		
		if(host_win) this.scoreLabel.setFont(scoreLabelFont.deriveFont(Font.BOLD));
		else this.scoreLabel.setFont(scoreLabelFont);
		this.scoreLabel.setText(host_score);
		this.scoreLabel.setHorizontalAlignment(JLabel.LEFT);
		this.scoreLabel.paint(host_score_area);
		
		if(!host_win) this.scoreLabel.setFont(scoreLabelFont.deriveFont(Font.BOLD));
		else this.scoreLabel.setFont(scoreLabelFont);
		this.scoreLabel.setText(guest_score);
		this.scoreLabel.setHorizontalAlignment(JLabel.RIGHT);
		this.scoreLabel.paint(guest_score_area);
		
		this.scoreLabel.setFont(scoreLabelFont);
		this.scoreLabel.setHorizontalAlignment(JLabel.CENTER);
		this.scoreLabel.setText(":");
		this.scoreLabel.setSize(width, height / 2);
		this.scoreLabel.paint(g.create(0, height / 2, width, height / 2));
		
		int beginName = height / 3;
		
		if(shouldDisplayTime)
		{
			System.out.println(timeLabel.getText());
			this.timeLabel.setSize(width, height / 4);
			this.timeLabel.setFont(plainTextFont);
			this.timeLabel.setHorizontalAlignment(JLabel.CENTER);
			this.timeLabel.setVerticalAlignment(JLabel.CENTER);
			this.timeLabel.paint(g.create(0, 0, width, height / 4));
			beginName = height / 4;
		}
		
		this.nameLabel.setFont(plainTextFont);
		this.nameLabel.setSize((width / 2) - height - padding * 2, height / 2 - beginName);
		Graphics host_name_area = g.create(padding + height - padding * 2, beginName, this.nameLabel.getWidth(), this.nameLabel.getHeight());
		Graphics guest_name_area = g.create(width - height + padding - this.nameLabel.getWidth(), beginName, this.nameLabel.getWidth(), this.nameLabel.getHeight());
		
		this.nameLabel.setText(host_name_abbr);
		this.nameLabel.setHorizontalAlignment(JLabel.LEFT);
		this.nameLabel.paint(host_name_area);
		
		this.nameLabel.setText(guest_name_abbr);
		this.nameLabel.setHorizontalAlignment(JLabel.RIGHT);
		this.nameLabel.paint(guest_name_area);
	}
}
