package nbaquery.presentation;

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicComboBoxUI;

public class MyBasicComboBoxUI extends BasicComboBoxUI
{
	JComboBox<?> box;
	public MyBasicComboBoxUI(JComboBox<?> box)
	{
		this.box = box;
	}
	
    public void installUI(JComponent comboBox) {
        super.installUI(box);
           listBox.setForeground(new Color(1,1,1,0.8f));
           listBox.setBackground(new Color(0,0,0,0.3f));
         }
       
         /**
          * �÷��������ұߵİ�ť
          */
       protected JButton createArrowButton() {
           return null;
         }
}