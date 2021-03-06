package nbaquery.presentation2.util;


import javax.swing.JButton;
import javax.swing.JPanel;

import nbaquery.presentation.resource.ImageIconResource;

@SuppressWarnings("serial")
public class Button extends JButton{
	public Button(String path1, String path2, JPanel added_panel){
		added_panel.add(this);
		
		if(!"".equals(path1)){
			this.setIcon(ImageIconResource.getImageIcon(path1));
		}
		if(!"".equals(path2)){
			this.setRolloverIcon(ImageIconResource.getImageIcon(path2));
		}
	//	this.setBackground(Color.BLACK);
		this.setOpaque(false);
		this.setContentAreaFilled(false);
		this.setBorderPainted(false);
		
		added_panel.validate();
		added_panel.repaint();
	}
}

