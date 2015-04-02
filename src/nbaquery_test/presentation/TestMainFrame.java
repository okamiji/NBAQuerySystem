package nbaquery_test.presentation;

import java.awt.EventQueue;

import nbaquery.presentation.MainFrame;

public class TestMainFrame
{
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame window = new MainFrame(new Presentation_Stub(), new Presentation_Stub());
					window.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
