package nbaquery.presentation.combobox;

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
        /*
           listBox.setForeground(new Color(1,1,1,0.8f));
           listBox.setBackground(new Color(0,0,0,0.3f));
           */
        listBox.setForeground(new Color(1, 1, 1));
        listBox.setBackground(new Color(31, 109, 97));
        
        box.setOpaque(true);
        
       // this.comboBox.setForeground(new Color(1, 1, 0));
       // this.comboBox.setBackground(new Color(0, 0, 0, 0));
        
       // this.box.setBackground(new Color(0, 0, 0, 0));
        
       // this.currentValuePane.setBackground(new Color(0, 0, 0, 0));
       // comboBox.getParent().setBackground(new Color(0, 0, 0, 0));
         }
       
         /**
          * 该方法返回右边的按钮
          */
       protected JButton createArrowButton() {
           return null;
         }
}