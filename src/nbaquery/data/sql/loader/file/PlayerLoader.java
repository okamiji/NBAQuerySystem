package nbaquery.data.sql.loader.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.TreeMap;

import nbaquery.data.Image;
import nbaquery.data.sql.MutableSqlRow;
import nbaquery.data.sql.MutableSqlTable;
import nbaquery.data.sql.SqlTableColumn;
import nbaquery.data.sql.SqlTableHost;


public class PlayerLoader implements SqlFileLoader
{
	TreeMap<String, SqlTableColumn> keyToColumnMap = new TreeMap<String, SqlTableColumn>();
	
	final SqlTableHost host;
	
	SqlTableColumn player_name;
	SqlTableColumn player_number;
	SqlTableColumn player_position;
	SqlTableColumn player_height;
	SqlTableColumn player_weight;
	SqlTableColumn player_birth;
	SqlTableColumn player_age;
	SqlTableColumn player_exp;
	SqlTableColumn player_school;
	
	SqlTableColumn portrait;
	SqlTableColumn action;
	
	public PlayerLoader(SqlTableHost host)
	{
		this.host = host;
		

		player_name = (SqlTableColumn) host.getTable("player").getColumn("player_name");
		player_number = (SqlTableColumn)host.getTable("player").getColumn("player_number");
		player_position = (SqlTableColumn)host.getTable("player").getColumn("player_position");
		player_height = (SqlTableColumn)host.getTable("player").getColumn("player_height");
		player_weight = (SqlTableColumn)host.getTable("player").getColumn("player_weight");
		player_birth = (SqlTableColumn)host.getTable("player").getColumn("player_birth");
		player_age = (SqlTableColumn)host.getTable("player").getColumn("player_age");
		player_exp = (SqlTableColumn)host.getTable("player").getColumn("player_exp");
		player_school = (SqlTableColumn)host.getTable("player").getColumn("player_school");
		
		portrait = (SqlTableColumn)host.getTable("player").getColumn("player_portrait");
		action = (SqlTableColumn)host.getTable("player").getColumn("player_action");
		
		keyToColumnMap.put("name", player_name);
		keyToColumnMap.put("number", player_number);
		keyToColumnMap.put("position", player_position);
		keyToColumnMap.put("height", player_height);
		keyToColumnMap.put("weight", player_weight);
		keyToColumnMap.put("birth", player_birth);
		keyToColumnMap.put("age", player_age);
		keyToColumnMap.put("exp", player_exp);
		keyToColumnMap.put("school", player_school);
	}
	
	public void record(File file, File actionFolder, File portraitFolder, MutableSqlTable playerTable) throws Exception
	{
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
		String readLine = null;
		MutableSqlRow player = playerTable.createRow();
		while((readLine = reader.readLine()) != null)
		{
			//if(readLine.charAt(0) == '�U' && readLine.charAt(readLine.length() - 1) == '�U')
			if(readLine.charAt(0) == '\u2551' && readLine.charAt(readLine.length() - 1) == '\u2551')
			{
				String sq = readLine.substring(1, readLine.	length() - 1);
				//int splitter = sq.indexOf('��');
				int splitter = sq.indexOf('\u2502');
				if(splitter < 0) continue;
				String key = sq.substring(0, splitter - 1).toLowerCase().trim();
				String value = sq.substring(splitter + 1, sq.length() - 1).trim();
				SqlTableColumn column = this.keyToColumnMap.get(key);
				if(column != null) column.setAttribute(player, value);
			}
		}
		reader.close();
		
		File portraitFile = new File(portraitFolder, file.getName() + ".png");
		if(portraitFile.exists() && portraitFile.isFile()) portrait.setAttribute(player, new Image(portraitFile));
		
		File actionFile = new File(actionFolder, file.getName() + ".png");
		if(actionFile.exists() && actionFile.isFile()) action.setAttribute(player, new Image(actionFile));
		player.submit();
	}

	File fileFolder;
	File actionFolder;
	File portraitFolder;
	
	public void setRoot(File root) throws Exception
	{
		fileFolder = new File(root, "players");
		actionFolder = new File(fileFolder, "action");
		portraitFolder = new File(fileFolder, "portrait");
		SqlFileMonitor fileMonitor = new SqlFileMonitor(new File(fileFolder, "info"), this, host);
		fileMonitor.start();
	}
	
	@Override
	public void load(File aFile) throws Exception
	{
		MutableSqlTable playerTable = (MutableSqlTable) host.getTable("player");
		this.record(aFile, actionFolder, portraitFolder, playerTable);
	}

	@Override
	public String getLoaderName() {
		return "player";
	}

	@Override
	public void shouldLock(boolean l) {
		MutableSqlTable playerTable = (MutableSqlTable) host.getTable("player");
		playerTable.setTableLocked(l);
	}
}
