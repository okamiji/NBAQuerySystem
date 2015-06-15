package nbaquery.data.sql.loader.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import nbaquery.data.Image;
import nbaquery.data.sql.MutableSqlRow;
import nbaquery.data.sql.MutableSqlTable;
import nbaquery.data.sql.SqlTableColumn;
import nbaquery.data.sql.SqlTableHost;

public class TeamLoader implements SqlFileLoader
{	
	final SqlTableHost host;
	
	SqlTableColumn team_name;
	SqlTableColumn team_name_abbr;
	SqlTableColumn team_location;
	SqlTableColumn team_match_area;
	SqlTableColumn team_sector;
	SqlTableColumn team_host;
	SqlTableColumn team_foundation;
	SqlTableColumn team_logo;
	
	SqlTableColumn[] columnCorrespondance;
	
	public TeamLoader(SqlTableHost host)
	{
		this.host = host;
		
		team_name = (SqlTableColumn) host.getTable("team").getColumn("team_name");
		team_name_abbr = (SqlTableColumn) host.getTable("team").getColumn("team_name_abbr");
		team_location = (SqlTableColumn) host.getTable("team").getColumn("team_location");
		team_match_area = (SqlTableColumn) host.getTable("team").getColumn("team_match_area");
		team_sector = (SqlTableColumn) host.getTable("team").getColumn("team_sector");
		team_host = (SqlTableColumn) host.getTable("team").getColumn("team_host");
		team_foundation = (SqlTableColumn) host.getTable("team").getColumn("team_foundation");
		team_logo = (SqlTableColumn) host.getTable("team").getColumn("team_logo");
		
		columnCorrespondance = new SqlTableColumn[]{team_name, team_name_abbr, team_location, team_match_area, team_sector, team_host, team_foundation};
	}
	
	public void record(File file, File fileFolder, MutableSqlTable teamTable) throws Exception
	{
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
		String readLine = null;
		while((readLine = reader.readLine()) != null)
		{
			//if(readLine.charAt(0) == '�U' && readLine.charAt(readLine.length() - 1) == '�U')
			if(readLine.charAt(0) == '\u2551' && readLine.charAt(readLine.length() - 1) == '\u2551')
			{
				MutableSqlRow team = teamTable.createRow();
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
				team.submit();
			}
		}
		reader.close();
	}
	
	File fileFolder;
	
	public void setRoot(File root) throws Exception
	{
		this.fileFolder = new File(root, "teams");
		SqlFileMonitor fileMonitor = new SqlFileMonitor(this.fileFolder, this, host);
		fileMonitor.start();
	}

	@Override
	public void load(File aFile) throws Exception
	{
		if(aFile.getName().equals("teams"))
		{
			MutableSqlTable teamTable = (MutableSqlTable) host.getTable("team");
			this.record(aFile, fileFolder, teamTable);
		}
	}

	@Override
	public String getLoaderName() {
		return "team";
	}
}
