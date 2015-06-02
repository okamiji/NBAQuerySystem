package nbaquery.data.file.loader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import nbaquery.data.Image;
import nbaquery.data.file.EnumTable;
import nbaquery.data.file.FileTableColumn;
import nbaquery.data.file.FileTableHost;
import nbaquery.data.file.KeywordTable;
import nbaquery.data.file.Tuple;

public class TeamLoader implements FileLoader
{	
	final FileTableHost host;
	
	FileTableColumn team_name;
	FileTableColumn team_name_abbr;
	FileTableColumn team_location;
	FileTableColumn team_match_area;
	FileTableColumn team_sector;
	FileTableColumn team_host;
	FileTableColumn team_foundation;
	FileTableColumn team_logo;
	
	FileTableColumn[] columnCorrespondance;
	
	public TeamLoader(FileTableHost host)
	{
		this.host = host;
		
		this.host.makeProtectedTable(EnumTable.TEAM.toString(), 
				this.host.getTableFromPreset(EnumTable.TEAM));
		
		team_name = host.getColumn("team.team_name");
		team_name_abbr = host.getColumn("team.team_name_abbr");
		team_location = host.getColumn("team.team_location");
		team_match_area = host.getColumn("team.team_match_area");
		team_sector = host.getColumn("team.team_sector");
		team_host = host.getColumn("team.team_host");
		team_foundation = host.getColumn("team.team_foundation");
		team_logo = host.getColumn("team.team_logo");
		
		columnCorrespondance = new FileTableColumn[]{team_name, team_name_abbr, team_location, team_match_area, team_sector, team_host, team_foundation};
	}
	
	public void record(File file, File fileFolder, KeywordTable teamTable) throws Exception
	{
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
		String readLine = null;
		while((readLine = reader.readLine()) != null)
		{
			//if(readLine.charAt(0) == '�U' && readLine.charAt(readLine.length() - 1) == '�U')
			if(readLine.charAt(0) == '\u2551' && readLine.charAt(readLine.length() - 1) == '\u2551')
			{
				Tuple team = teamTable.createTuple();
				String sq = readLine.substring(1, readLine.length() - 1);
				//String[] splitted = sq.split("[��]");
				String[] splitted = sq.split("[\u2502]");
				for(int i = 0; i < splitted.length; i ++)
					columnCorrespondance[i].setAttribute(team, splitted[i].trim());
				String team_abbr_name = (String) team_name_abbr.getAttribute(team);
				if(team_abbr_name != null)
				{
					File vector = new File(fileFolder, team_abbr_name + ".svg");
					if(vector.exists() && vector.isFile())
						team_logo.setAttribute(team, new Image(vector));
				}
			}
		}
		reader.close();
	}
	
	File fileFolder;
	
	public void setRoot(File root)
	{
		this.fileFolder = new File(root, "teams");
		FileMonitor fileMonitor = new FileMonitor(this.fileFolder, this);
		fileMonitor.start();
	}

	@Override
	public void load(File aFile) throws Exception
	{
		if(aFile.getName().equals("teams"))
		{
			KeywordTable teamTable = (KeywordTable) host.getTable("team");
			this.record(aFile, fileFolder, teamTable);
		}
	}
}
