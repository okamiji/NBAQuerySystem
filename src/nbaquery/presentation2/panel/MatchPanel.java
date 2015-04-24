package nbaquery.presentation2.panel;

import java.awt.Color;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import nbaquery.presentation.combobox.ComboBoxFactory;
import nbaquery.logic.match.MatchService;
import nbaquery.presentation2.addedcard.Card;
import nbaquery.presentation2.addon.DateComboBox;
import nbaquery.presentation2.card.CardCreator;
import nbaquery.presentation2.card.CardLocation;
import nbaquery.presentation2.util.Button;
import nbaquery.presentation2.util.CardType;

@SuppressWarnings("serial")
public class MatchPanel extends ConcisePanel {

	boolean view_all;
	static MatchPanel mp;
	
	public MatchPanel(CardType type_, boolean view_all_) {		
		super(type_, view_all_);
		
		type = type_;
		view_all = view_all_;
		mp = this;
	}
	
	static DateComboBox dateComboBox = new DateComboBox()
	{
		public void update()
		{
			mp.responseMouseClicked();
		}
	};
	
	public void run(){
		super.run();
	    add_cards();
	    super.set_scr();
	    
		search_panel.setLayout(null);
		search_panel.setBackground(new Color(0, 0, 0, 0));
		search_panel.setBounds(40, 25, 570, 60);
		
		valueBox = ComboBoxFactory.getInstance().createComboBox(20, 15, 100, 24, 
				new String[]{
				"赛季",
				"比赛日期",
				"主场队伍缩写",
				"客场队伍缩写",
				"主场队伍得分",
				"客场队伍得分",
				});
		search_panel.add(valueBox);
		
		if(ConcisePara.match_index >= 1){
			valueBox.setSelectedIndex(ConcisePara.match_index - 1);
		}
		else{
			valueBox.setSelectedIndex(0);
		}
		
		descendButton = new JButton();
		descendButton.setIcon(new ImageIcon("Img2/descend.png"));
		descendButton.setContentAreaFilled(false);
		descendButton.setBorder(null);
		descendButton.setBounds(420, 15, 24, 24);
		search_panel.add(descendButton);
		
		ascendButton = new JButton();
		ascendButton.setIcon(new ImageIcon("Img2/ascend.png"));
		ascendButton.setContentAreaFilled(false);
		ascendButton.setBorder(null);
		ascendButton.setBounds(420, 15, 24, 24);
		search_panel.add(ascendButton);
		ascendButton.setVisible(false);
		
		ascendButton.setVisible(!ConcisePara.match_isUp);
		descendButton.setVisible(ConcisePara.match_isUp);
		
		descendButton.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				descendButton.setVisible(false);
				ascendButton.setVisible(true);
				ConcisePara.match_isUp = false;
				responseMouseClicked();
			}
		});
		ascendButton.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				ascendButton.setVisible(false);
				descendButton.setVisible(true);
				ConcisePara.match_isUp = true;
				responseMouseClicked();
			}
		});
		
		update();
		
		ItemListener l = new ItemListener(){
			@Override
			public void itemStateChanged(ItemEvent e) {
				responseMouseClicked();
			}
	    };
	    
	    valueBox.addItemListener(l);
	    
	    dateComboBox.setBounds(140, 15, 210, 24);
	    dateComboBox.setFont(getFont());
	    
	    search_panel.add(dateComboBox);
	    
	    dateComboBox.calendar.setText("+");
	    dateComboBox.calendar.setFont(dateComboBox.calendar.getFont().deriveFont(15.0f));
	    dateComboBox.clear.setText("-");
	    dateComboBox.clear.setFont(dateComboBox.clear.getFont().deriveFont(15.0f));
	}
	
	public void update()
	{
		//valueBox.setSelectedIndex(ConcisePara.match_index);
	}
	
	public void responseMouseClicked() {
		
		ConcisePara.match_index = valueBox.getSelectedIndex() + 1;
		ConcisePara.match_item_name = (String)(valueBox.getSelectedItem());
		ConcisePara.view_all = false;
		
		
		
		PanelSet.set_concise_invisible();
		ConcisePanelFactory.create_panel(type, view_all, false);
		
	}
	
	private void add_cards(){
		MatchService ms = PanelSet.ms;
	//	String[][] str = ms.searchForMatchs(0, true);
		
		String[][] str;
		if(dateComboBox.getDate() != null && dateComboBox.getSeason() != null)
			str = ms.searchForMatchsByDateAndSeason(dateComboBox.getDate(), dateComboBox.getSeason());
		else str = ms.searchForMatchs(ConcisePara.match_index, ConcisePara.match_isUp);
		CardCreator creator = new CardCreator();
		ArrayList<Card> card_list = creator.create_needed_cards(ConcisePara.type, str, ConcisePara.view_all);
		CardLocation location = new CardLocation(ConcisePara.type);
		scr_height = location.get_total_height(card_list.size());
		for(int i=0; i<card_list.size(); i++){
			Card card = card_list.get(i);
			card_panel.add(card);
			card.setLocation(card.width, card.height);
		}
	}

}
