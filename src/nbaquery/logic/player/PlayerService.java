package nbaquery.logic.player;

public interface PlayerService
{
	/**
	 * 查询球员信息
	 * @param isGross true为全局信息 false为场均信息
	 * @param index 排序依据在columnNames里面的顺序
	 * @param isUp true为升序 false为降序
	 * @param position 球员位置
	 * @param league 球员联盟
	 * @return 所查询的球员信息数组
	 */
	public String[][] searchForPlayers(boolean isGross, int index, boolean isUp, String position, String league);
	/**
	 * 查询今日热点球员信息
	 * @param head 所需要排序的项目在hotPlayerColumnNames数组中的index
	 * @return 前五名HotPlayers的信息（包括姓名和五个排序选项的数据）
	 */
	public String[][] searchForTodayHotPlayers(int head);
	/**
	 * 查询进步最大球员信息
	 * @param head 所需要排序的项目在progressPlayerColumnNames数组中的index
	 * @return 前五名进步最快球员的信息
	 */
	public String[][] searchForProgressPlayers(int head);
	/**
	 * 查询赛季热门球员信息
	 * @param head 所需要排序的项目在hotPlayerColumnNames数组中的index
	 * @return 前五名HotPlayers的信息（包括姓名和五个排序选项的数据）
	 */
	public String[][] searchForSeasonHotPlayers(int head);
	/**
	 * 查询一个球员的信息
	 * @param playerName 查询的球员姓名
	 * @return 返回球员基本信息
	 */
	public String[] searchForOnePlayer(String playerName);
}
