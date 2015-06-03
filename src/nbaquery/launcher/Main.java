package nbaquery.launcher;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import nbaquery.data.TableHost;
import nbaquery.logic.match.MatchService;
import nbaquery.logic.player.PlayerService;
import nbaquery.logic.team.TeamService;
import nbaquery.presentation2.addon.GoodLookingScrollBar;
import nbaquery.presentation2.main.MainFrame;
//import nbaquery.presentation.MainFrame;

public class Main
{
	
	Installer<? extends TableHost> dataInstaller = null;
	Node dataNode = null;
	
	Installer<?> logicInstaller = null;
	Node logicNode = null;
	
	public void loadConfiguration() throws Exception
	{
		Configuration config = new Configuration(new File("config.xml"));
		Document dom = config.getDocument();
		NodeList nodelist = dom.getChildNodes();
		
		XmlEventNotifier notifier = new XmlEventNotifier();

		notifier.registerListener("data", new XmlEventListener()
		{
			@Override
			public void onEncounter(Node node) throws Exception {
				@SuppressWarnings("unchecked")
				Class<? extends Installer<TableHost>> dataInstallerClazz = (Class<? extends Installer<TableHost>>) 
					Class.forName(node.getAttributes().getNamedItem("installer").getTextContent());
				dataInstaller = dataInstallerClazz.newInstance();
				dataNode = node;
			}
		});
		
		notifier.registerListener("logic", new XmlEventListener()
		{
			@Override
			public void onEncounter(Node node) throws Exception
			{
				@SuppressWarnings("unchecked")
				Class<? extends Installer<?>> logicInstallerClazz = (Class<? extends Installer<?>>)
					Class.forName(node.getAttributes().getNamedItem("installer").getTextContent());
				logicInstaller = logicInstallerClazz.newInstance();
				logicNode = node;
			}
		});
		
		notifier.registerListener("interface", new XmlEventListener()
		{
			@Override
			public void onEncounter(Node node) throws Exception
			{
			
			}
		});
		
		for(int i = 0; i < nodelist.getLength(); i ++)
			if(nodelist.item(i).getNodeType() == Node.ELEMENT_NODE)
			{
				notifier.parse(nodelist.item(i));
				break;
			}
	}
	
	public TableHost host;
	public void loadDataLayer() throws Exception
	{
		host = this.dataInstaller.install(dataNode);
	}
	
	public TeamService teamService;
	public PlayerService playerService;
	public MatchService matchService;
	public void loadLogicLayer() throws Exception
	{
		this.logicInstaller.install(logicNode, this);
	}
	
	MainFrame mainFrame;
	public void loadPresentation()
	{
		mainFrame = new MainFrame(this.playerService, this.teamService, this.matchService);
		mainFrame.run();
		
		try
		{
			GoodLookingScrollBar.scrollSlide = ImageIO.read(new File("Img2/scrollslide.png".replace('/', File.separatorChar)));
			GoodLookingScrollBar.upArrow = ImageIO.read(new File("Img2/upArrow.png".replace('/', File.separatorChar)));
			GoodLookingScrollBar.downArrow = ImageIO.read(new File("Img2/downArrow.png".replace('/', File.separatorChar)));
		}
		catch(IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//mainFrame.setVisible(true);
	}
	
	public void launch() throws Exception
	{
		
		this.loadConfiguration();
		this.loadDataLayer();
		
		while(true) try
		{
			this.loadLogicLayer();
			break;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			break;//System.out.println("Error detected while loading, retrying.");
		}
		this.loadPresentation();
	}
	
	public static void main(String[] arguments) throws Exception
	{
		Main main = new Main();
		main.launch();
	}
	
}
