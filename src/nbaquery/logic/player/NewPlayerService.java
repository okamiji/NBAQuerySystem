package nbaquery.logic.player;

import nbaquery.data.Table;

public interface NewPlayerService
{
	public boolean shouldRedoQuery(Object host);
	
	/**
	 * 查询球员信息
	 * @param isGross true为全局信息 false为场均信息
	 * @param index 排序依据在columnNames里面的顺序
	 * @param isUp true为升序 false为降序
	 * @param position 球员位置
	 * @param league 球员联盟
	 * @return 所查询的球员信息表格
	 */
	public Table searchForPlayers(boolean isGross, String[] head, boolean isUp, String position, String league);
	/**
	 * 查询今日热点球员信息
	 * @param head 所需要排序的项目在hotPlayerColumnNames数组中的index
	 * @return 前五名HotPlayers的信息（包括姓名和五个排序选项的数据）
	 */
	public Table searchForTodayHotPlayers(String head);
	/**
	 * 查询进步最大球员信息
	 * @param head 所需要排序的项目在progressPlayerColumnNames数组中的index
	 * @return 前五名进步最快球员的信息
	 */
	public Table searchForProgressPlayers(String head);
	/**
	 * 查询赛季热门球员信息
	 * @param head 所需要排序的项目在hotPlayerColumnNames数组中的index
	 * @return 前五名HotPlayers的信息（包括姓名和五个排序选项的数据）
	 */
	public Table searchForSeasonHotPlayers(String head);
	/**
	 * 查询一个球员的信息<br>
	 * <i><b>弃用: </b>其他任何操作都已经返回了必要的属性值。</i>
	 * @param playerName 查询的球员姓名
	 * @return 返回球员基本信息
	 */
	@Deprecated
	public Table searchForOnePlayerTable(String playerName);
}
