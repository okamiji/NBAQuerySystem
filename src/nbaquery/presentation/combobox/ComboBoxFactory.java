package nbaquery.presentation.combobox;

import javax.swing.JComboBox;

public abstract class ComboBoxFactory
{
	protected boolean isUISupported;
	
	private ComboBoxFactory()
	{
		String operatingSystem = System.getProperty("os.name");
		if(operatingSystem.startsWith("Windows") || operatingSystem.startsWith("windows"))
		{
			String operatingSystemVersion = System.getProperty("os.version");
			int compare = operatingSystemVersion.compareTo("6.1");
			if(compare > 0) isUISupported = true;
			else isUISupported = false;
		}
		else isUISupported = false;
	}
	
	public JComboBox<String> createComboBox(int boundX, int boundY, int boundWidth, int boundHeight, String[] comboBoxItem)
	{
		JComboBox<String> comboBox = new JComboBox<String>();
		comboBox.setBounds(boundX, boundY, boundWidth, boundHeight);
		if(isUISupported) comboBox.setUI(new MyBasicComboBoxUI(comboBox));
		if(comboBoxItem != null) for(String item : comboBoxItem) comboBox.addItem(item);
		return comboBox;
	}
	
	protected static ComboBoxFactory instance;
	
	public static ComboBoxFactory getInstance()
	{
		if(instance == null) instance = new ComboBoxFactory(){};
		return instance;
	}
}
