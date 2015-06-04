package nbaquery.logic;

import java.util.ArrayList;
import java.util.TreeMap;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import nbaquery.data.TableHost;
import nbaquery.launcher.Installer;
import nbaquery.launcher.Main;
import nbaquery.launcher.XmlEventListener;
import nbaquery.launcher.XmlEventNotifier;
import nbaquery.logic.match.NewMatchService;
import nbaquery.logic.player.NewPlayerService;
import nbaquery.logic.team.NewTeamService;

public class PipelineInstaller implements Installer<Object>
{
	TableHost tableHost;
	
	TreeMap<String, Node> pipelines = new TreeMap<String, Node>();
	TreeMap<String, Object> pipelineObjects = new TreeMap<String, Object>();
	
	@Override
	public Object install(Node documentNode, Object... params) throws Exception
	{
		Main main = (Main) params[0];
		this.tableHost = main.host;
		pipelines.clear();
		pipelineObjects.clear();
		
		XmlEventNotifier notifier = new XmlEventNotifier();
		notifier.registerListener("pipeline", new XmlEventListener()
		{
			@Override
			public void onEncounter(Node node) throws Exception
			{
				Element element = (Element) node;
				pipelines.put(element.getAttribute("name"), node);
			}
		});
		
		notifier.registerListener("player_service", new ServiceListener());
		notifier.registerListener("team_service", new ServiceListener());
		notifier.registerListener("match_service", new ServiceListener());
		notifier.parse(documentNode);
		
		main.playerService = (NewPlayerService) this.buildDependency("player_service");
		main.teamService = (NewTeamService) this.buildDependency("team_service");
		main.matchService = (NewMatchService) this.buildDependency("match_service");
		
		return null;
	}

	class ServiceListener implements XmlEventListener
	{
		@Override
		public void onEncounter(Node node) throws Exception
		{
			pipelines.put(node.getNodeName(), node);
		}
	}
	
	public Object buildDependency(String objectName) throws Exception
	{
		Object pipelineObject = this.pipelineObjects.get(objectName);
		if(pipelineObject != null) return pipelineObject;
		
		Element element = (Element) this.pipelines.get(objectName);
		Class<?> theClass = Class.forName(element.getAttribute("class"));
		final ArrayList<Object> arguments = new ArrayList<Object>();
		arguments.add(this.tableHost);
		
		XmlEventNotifier notifier = new XmlEventNotifier();
		notifier.registerListener("dependency", new XmlEventListener()
		{
			@Override
			public void onEncounter(Node node) throws Exception {
				Object depend = buildDependency(node.getTextContent());
				arguments.add(depend);
			}
		});
		notifier.parse(element);
		pipelineObject = theClass.getConstructors()[0]
					.newInstance(arguments.toArray(new Object[0]));
		return pipelineObject;
	}
}
