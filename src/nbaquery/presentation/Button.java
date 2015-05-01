package nbaquery.presentation;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Button extends JButton{
	public Button(ImageIcon icon1, ImageIcon icon2, JPanel added_panel){
		added_panel.add(this);
		
		if(icon1 != null){
			this.setIcon(icon1);
		}
		if(icon2 != null){
			this.setRolloverIcon(icon2);
		}
	//	this.setBackground(Color.BLACK);
		this.setOpaque(false);
		this.setContentAreaFilled(false);
		this.setBorderPainted(false);
		
		added_panel.validate();
		added_panel.repaint();
	}
}

