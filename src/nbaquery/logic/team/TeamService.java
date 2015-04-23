package nbaquery.logic.team;

public interface TeamService
{
	/**
	 * 查询球队信息
	 * @param isGross true为全局信息 false为场均信息
	 * @param index 排序依据在columnNames里面的顺序
	 * @param isUp true为升序 false为降序
	 * @return 所查询的球队信息数组
	 */
	public String[][] searchForTeams(boolean isGross, int index, boolean isUp);
	/**
	 * 查询赛季热门球队信息
	 * @param index 排序在hotTeamColumnNames数组中的index
	 * @return 前五名热门球队信息
	 */
	public String[][] searchForSeasonHotTeams(int index);
	/**
	 * 查询特定球队的信息
	 * @param teamName 球队名字
	 * @return 所查询的球队的基本信息
	 */
	public String[] searchForOneTeam(String teamNameOrAbbr, boolean isAbbr);
}
