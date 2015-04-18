package nbaquery.logic.player;

public interface PlayerService
{
	/**
	 * ��ѯ��Ա��Ϣ
	 * @param isGross trueΪȫ����Ϣ falseΪ������Ϣ
	 * @param index ����������columnNames�����˳��
	 * @param isUp trueΪ���� falseΪ����
	 * @param position ��Աλ��
	 * @param league ��Ա����
	 * @return ����ѯ����Ա��Ϣ����
	 */
	public String[][] searchForPlayers(boolean isGross, int index, boolean isUp, String position, String league);
	/**
	 * ��ѯ�����ȵ���Ա��Ϣ
	 * @param head ����Ҫ�������Ŀ��hotPlayerColumnNames�����е�index
	 * @return ǰ����HotPlayers����Ϣ�������������������ѡ������ݣ�
	 */
	public String[][] searchForTodayHotPlayers(int head);
	/**
	 * ��ѯ���������Ա��Ϣ
	 * @param head ����Ҫ�������Ŀ��progressPlayerColumnNames�����е�index
	 * @return ǰ�������������Ա����Ϣ
	 */
	public String[][] searchForProgressPlayers(int head);
	/**
	 * ��ѯ����������Ա��Ϣ
	 * @param head ����Ҫ�������Ŀ��hotPlayerColumnNames�����е�index
	 * @return ǰ����HotPlayers����Ϣ�������������������ѡ������ݣ�
	 */
	public String[][] searchForSeasonHotPlayers(int head);
	/**
	 * ��ѯһ����Ա����Ϣ
	 * @param playerName ��ѯ����Ա����
	 * @return ������Ա������Ϣ
	 */
	public String[] searchForOnePlayer(String playerName);
}
