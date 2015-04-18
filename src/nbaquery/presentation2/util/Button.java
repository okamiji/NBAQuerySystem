package nbaquery.presentation2.util;

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Button extends JButton{
	public Button(String path1, String path2, JPanel added_panel){
		added_panel.add(this);
		this.setBackground(Color.BLACK);
		this.setOpaque(false);
//		this.setContentAreaFilled(false);
		this.setBorderPainted(false);
	}
}

