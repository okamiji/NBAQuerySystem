package nbaquery.presentation2.util;

public enum HotspotType {
	DAILY_PLAYER(true, false),
	SEASON_PLAYER(true, false),
	SEASON_TEAM(false, true),
	PROGRESS_PLAYER(true, false);
	
	public final boolean isPlayer, isTeam;
	
	private HotspotType(boolean isPlayer, boolean isTeam)
	{
		this.isPlayer = isPlayer;
		this.isTeam = isTeam;
	}
}

