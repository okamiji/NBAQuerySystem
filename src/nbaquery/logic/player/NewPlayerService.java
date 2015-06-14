package nbaquery.logic.player;

import nbaquery.data.Table;

public interface NewPlayerService
{
	public boolean shouldRedoQuery(Object host);
	
	/**
	 * ��ѯ��Ա��Ϣ
	 * @param isGross trueΪȫ����Ϣ falseΪ������Ϣ
	 * @param index ����������columnNames�����˳��
	 * @param isUp trueΪ���� falseΪ����
	 * @param position ��Աλ��
	 * @param league ��Ա����
	 * @return ����ѯ����Ա��Ϣ���
	 */
	public Table searchForPlayers(boolean isGross, String[] head, boolean isUp, String position, String league);
	
	public Table searchForPlayers(boolean isGross, String[] fields, boolean[] sortDescend, String position, String league);
	
	/**
	 * ��ѯ�����ȵ���Ա��Ϣ
	 * @param head ����Ҫ�������Ŀ��hotPlayerColumnNames�����е�index
	 * @return ǰ����HotPlayers����Ϣ�������������������ѡ������ݣ�
	 */
	public Table searchForTodayHotPlayers(String head);
	/**
	 * ��ѯ���������Ա��Ϣ
	 * @param head ����Ҫ�������Ŀ��progressPlayerColumnNames�����е�index
	 * @return ǰ�������������Ա����Ϣ
	 */
	public Table searchForProgressPlayers(String head);
	/**
	 * ��ѯ����������Ա��Ϣ
	 * @param head ����Ҫ�������Ŀ��hotPlayerColumnNames�����е�index
	 * @return ǰ����HotPlayers����Ϣ�������������������ѡ������ݣ�
	 */
	public Table searchForSeasonHotPlayers(String head);
	/**
	 * ��ѯһ����Ա����Ϣ<br>
	 * <i><b>����: </b>�����κβ������Ѿ������˱�Ҫ������ֵ��</i>
	 * @param playerName ��ѯ����Ա����
	 * @return ������Ա������Ϣ
	 */
	@Deprecated
	public Table searchForOnePlayerTable(String playerName);
}
