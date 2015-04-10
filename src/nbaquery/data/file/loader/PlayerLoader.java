package nbaquery.data.file.loader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.TreeMap;

import nbaquery.data.Image;
import nbaquery.data.file.EnumTable;
import nbaquery.data.file.FileTableColumn;
import nbaquery.data.file.FileTableHost;
import nbaquery.data.file.KeywordTable;
import nbaquery.data.file.Tuple;

public class PlayerLoader implements FileLoader
{
	TreeMap<String, FileTableColumn> keyToColumnMap = new TreeMap<String, FileTableColumn>();
	
	final FileTableHost host;
	
	FileTableColumn player_name;
	FileTableColumn player_number;
	FileTableColumn player_position;
	FileTableColumn player_height;
	FileTableColumn player_weight;
	FileTableColumn player_birth;
	FileTableColumn player_age;
	FileTableColumn player_exp;
	FileTableColumn player_school;
	
	FileTableColumn portrait;
	FileTableColumn action;
	
	public PlayerLoader(FileTableHost host)
	{
		this.host = host;
		
		this.host.makeProtectedTable(EnumTable.PLAYER.toString(), 
				this.host.getTableFromPreset(EnumTable.PLAYER));
		
		player_name = host.getColumn("player.player_name");
		player_number = host.getColumn("player.player_number");
		player_position = host.getColumn("player.player_position");
		player_height = host.getColumn("player.player_height");
		player_weight = host.getColumn("player.player_weight");
		player_birth = host.getColumn("player.player_birth");
		player_age = host.getColumn("player.player_age");
		player_exp = host.getColumn("player.player_exp");
		player_school = host.getColumn("player.player_school");
		
		portrait = host.getColumn("player.player_portrait");
		action = host.getColumn("player.player_action");
		
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
	
	public void record(File file, File actionFolder, File portraitFolder, KeywordTable playerTable) throws Exception
	{
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
		String readLine = null;
		Tuple player = playerTable.createTuple();
		while((readLine = reader.readLine()) != null)
		{
			if(readLine.charAt(0) == '¨U' && readLine.charAt(readLine.length() - 1) == '¨U')
			{
				String sq = readLine.substring(1, readLine.length() - 1);
				int splitter = sq.indexOf('©¦');
				if(splitter < 0) continue;
				String key = sq.substring(0, splitter - 1).toLowerCase().trim();
				String value = sq.substring(splitter + 1, sq.length() - 1).trim();
				FileTableColumn column = this.keyToColumnMap.get(key);
				if(column != null) column.setAttribute(player, value);
			}
		}
		reader.close();
		
		File portraitFile = new File(portraitFolder, file.getName() + ".png");
		if(portraitFile.exists() && portraitFile.isFile()) portrait.setAttribute(player, new Image(portraitFile));
		
		File actionFile = new File(actionFolder, file.getName() + ".png");
		if(actionFile.exists() && actionFile.isFile()) action.setAttribute(player, new Image(actionFile));
	}

	File fileFolder;
	File actionFolder;
	File portraitFolder;
	
	public void setRoot(File root)
	{
		fileFolder = new File(root, "players");
		actionFolder = new File(fileFolder, "action");
		portraitFolder = new File(fileFolder, "portrait");
		FileMonitor fileMonitor = new FileMonitor(new File(fileFolder, "info"), this);
		fileMonitor.start();
	}
	
	@Override
	public void load(File aFile) throws Exception
	{
		KeywordTable playerTable = (KeywordTable) host.getTable("player");
		this.record(aFile, actionFolder, portraitFolder, playerTable);
	}
}
