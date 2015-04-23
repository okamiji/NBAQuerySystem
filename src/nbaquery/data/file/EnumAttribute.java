package nbaquery.data.file;

import java.util.Date;

import nbaquery.data.Image;

public enum EnumAttribute
{
	TEAM_NAME_ABBR(new EnumTable[]{EnumTable.TEAM, EnumTable.PERFORMANCE}, String.class, new boolean[]{true, false}),
	TEAM_NAME(EnumTable.TEAM, String.class),
	TEAM_LOCATION(EnumTable.TEAM, String.class),
	TEAM_MATCH_AREA(EnumTable.TEAM, Character.class),
	TEAM_SECTOR(EnumTable.TEAM, String.class),
	TEAM_HOST(EnumTable.TEAM, String.class),
	TEAM_FOUNDATION(EnumTable.TEAM, Integer.class),
	TEAM_LOGO(EnumTable.TEAM, Image.class),

	PLAYER_NAME(new EnumTable[]{EnumTable.PLAYER, EnumTable.PERFORMANCE}, String.class, new boolean[]{true, false}),
	PLAYER_NUMBER(EnumTable.PLAYER, Integer.class),
	PLAYER_POSITION(new EnumTable[]{EnumTable.PLAYER, EnumTable.PERFORMANCE}, Character.class, new boolean[]{false, false}),
	PLAYER_HEIGHT(EnumTable.PLAYER, String.class),
	PLAYER_WEIGHT(EnumTable.PLAYER, Integer.class),
	PLAYER_BIRTH(EnumTable.PLAYER, Date.class),
	PLAYER_AGE(EnumTable.PLAYER, Integer.class),
	PLAYER_EXP(EnumTable.PLAYER, Integer.class),
	PLAYER_SCHOOL(EnumTable.PLAYER, String.class),
	PLAYER_PORTRAIT(EnumTable.PLAYER, Image.class),
	PLAYER_ACTION(EnumTable.PLAYER, Image.class),
	
	MATCH_ID(new EnumTable[]{EnumTable.MATCH, EnumTable.QUARTER_SCORE, EnumTable.PERFORMANCE}, Integer.class, new boolean[]{true, false, false}),
	MATCH_SEASON(EnumTable.MATCH, String.class),
	MATCH_DATE(EnumTable.MATCH, String.class),
	MATCH_HOST_ABBR(EnumTable.MATCH, String.class),
	MATCH_GUEST_ABBR(EnumTable.MATCH, String.class),
	MATCH_HOST_SCORE(EnumTable.MATCH, Integer.class),
	MATCH_GUEST_SCORE(EnumTable.MATCH, Integer.class),

	QUARTER_NUMBER(EnumTable.QUARTER_SCORE, Integer.class),
	QUARTER_HOST_SCORE(EnumTable.QUARTER_SCORE, Integer.class),
	QUARTER_GUEST_SCORE(EnumTable.QUARTER_SCORE, Integer.class),

	GAME_TIME_MINUTE(EnumTable.PERFORMANCE, Integer.class),
	GAME_TIME_SECOND(EnumTable.PERFORMANCE, Integer.class),
	SHOOT_SCORE(EnumTable.PERFORMANCE, Integer.class),
	SHOOT_COUNT(EnumTable.PERFORMANCE, Integer.class),
	THREE_SHOOT_SCORE(EnumTable.PERFORMANCE, Integer.class),
	THREE_SHOOT_COUNT(EnumTable.PERFORMANCE, Integer.class),
	FOUL_SHOOT_SCORE(EnumTable.PERFORMANCE, Integer.class),
	FOUL_SHOOT_COUNT(EnumTable.PERFORMANCE, Integer.class),
	ATTACK_BOARD(EnumTable.PERFORMANCE, Integer.class),
	DEFENCE_BOARD(EnumTable.PERFORMANCE, Integer.class),
	TOTAL_BOARD(EnumTable.PERFORMANCE, Integer.class),
	ASSIST(EnumTable.PERFORMANCE, Integer.class),
	STEAL(EnumTable.PERFORMANCE, Integer.class),
	CAP(EnumTable.PERFORMANCE, Integer.class),
	MISS(EnumTable.PERFORMANCE, Integer.class),
	FOUL(EnumTable.PERFORMANCE, Integer.class),
	SELF_SCORE(EnumTable.PERFORMANCE, Integer.class);
	
	public final EnumTable[] entity;
	public final Class<?> dataClass;
	public final boolean[] isPrimaryKey;
	
	private EnumAttribute(EnumTable[] entity, Class<?> dataClass, boolean[] isPrimaryKey)
	{
		this.entity = entity;
		this.dataClass = dataClass;
		this.isPrimaryKey = isPrimaryKey;
	}
	
	private EnumAttribute(EnumTable entity, Class<?> dataClass, boolean isPrimaryKey)
	{
		this(new EnumTable[]{entity}, dataClass, new boolean[]{isPrimaryKey});
	}
	
	private EnumAttribute(EnumTable entity, Class<?> dataClass)
	{
		this(entity, dataClass, false);
	}
	
	public EnumTable[] getDeclaredEntity()
	{
		return entity;
	}
}
