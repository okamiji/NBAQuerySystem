package nbaquery.logic.team;

import nbaquery.data.Table;

public interface NewTeamService
{
	public boolean shouldRedoQuery(Object host);
	
	/**
	 * ��ѯ�����Ϣ
	 * @param isGross trueΪȫ����Ϣ falseΪ������Ϣ
	 * @param index ����������columnNames�����˳��
	 * @param isUp trueΪ���� falseΪ����
	 * @return ����ѯ�������Ϣ����
	 */
	public Table searchForTeams(boolean isGross, String[] keywords, boolean descend);
	
	public Table searchForTeams(boolean isGross, String[] keywords, boolean descend[]);
	/**
	 * ��ѯ�������������Ϣ
	 * @param index ������hotTeamColumnNames�����е�index
	 * @return ǰ�������������Ϣ
	 */
	public Table searchSeasonHotTeams(String keywords);
	
	/**
	 * ��ѯ�������������Ϣ
	 * @param index ������hotTeamColumnNames�����е�index
	 * @return ǰ�������������Ϣ
	 */
	public Table searchTodayHotTeams(String keywords);
	
	/**
	 * ��ѯ�ض���ӵ���Ϣ
	 * @param teamNameOrAbbr ������ֻ���д
	 * @param isAbbr �Ƿ�Ϊ�����д
	 * @return ����ѯ����ӵĻ�����Ϣ
	 */
	@Deprecated
	public Table searchInfoByName(String teamNameOrAbbr, boolean isAbbr);
}
