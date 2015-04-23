package nbaquery.logic.team;

public interface TeamService
{
	/**
	 * ��ѯ�����Ϣ
	 * @param isGross trueΪȫ����Ϣ falseΪ������Ϣ
	 * @param index ����������columnNames�����˳��
	 * @param isUp trueΪ���� falseΪ����
	 * @return ����ѯ�������Ϣ����
	 */
	public String[][] searchForTeams(boolean isGross, int index, boolean isUp);
	/**
	 * ��ѯ�������������Ϣ
	 * @param index ������hotTeamColumnNames�����е�index
	 * @return ǰ�������������Ϣ
	 */
	public String[][] searchForSeasonHotTeams(int index);
	/**
	 * ��ѯ�ض���ӵ���Ϣ
	 * @param teamName �������
	 * @return ����ѯ����ӵĻ�����Ϣ
	 */
	public String[] searchForOneTeam(String teamNameOrAbbr, boolean isAbbr);
}
