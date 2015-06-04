package nbaquery.presentation3;

import java.awt.Font;

import org.w3c.dom.Node;

import nbaquery.launcher.Installer;
import nbaquery.launcher.Main;
import nbaquery.presentation3.match.MatchComponent;

public class PresentationInstaller implements Installer<Object>
{

	@Override
	public Object install(Node documentNode, Object... params) throws Exception {
		Main main = (Main) params[0];
		new MainFrame(main.playerService, main.teamService,
				main.matchService).setVisible(true);
		
		MatchComponent.scoreLabelFont = new Font(Font.MONOSPACED, Font.PLAIN, 0).deriveFont(20.0f);
		MatchComponent.plainTextFont = new Font(Font.MONOSPACED, Font.PLAIN, 0).deriveFont(12.0f);
		return null;
	}

}
