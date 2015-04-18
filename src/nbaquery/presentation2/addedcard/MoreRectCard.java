package nbaquery.presentation2.addedcard;

import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;

import nbaquery.presentation2.panel.ConcisePanelFactory;
import nbaquery.presentation2.panel.ConcisePara;
import nbaquery.presentation2.panel.PanelSet;

@SuppressWarnings("serial")
class MoreRectCard extends RectCard {

	JLabel label_name;
	
	public void create_card(final Object obj){
		
		super.create_card(obj);
		
		label_name = new JLabel();
		label_name.setText("�鿴����");
		label_name.setFont(new Font("΢���ź�",Font.PLAIN, 12));	
		label_name.setBounds(110, 10, 140, 50);
		shadow_label.add(label_name);
		
		this.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				PanelSet.set_concise_invisible();
				
				CardType type = ConcisePara.type;
				ConcisePara.view_all = true;
				ConcisePanelFactory.create_panel(type, ConcisePara.view_all);
			}
		});
	}
}
