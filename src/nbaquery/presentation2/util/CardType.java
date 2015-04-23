package nbaquery.presentation2.util;

public enum CardType {

	PLAYER_RECT(true, false),
	PLAYER_FLAT(false, true),
	TEAM_RECT(true, false),
	TEAM_FLAT(false, true),
	MATCH_RECT(true, false),
	MATCH_FLAT(false, true),
	MORE_RECT(true, false),
	MORE_FLAT(false, true),
	MATCH_of_PLAYER(true, false),
	PLAYER_of_MATCH(true, false);
	
	public final boolean isRect, isFlat;
	
	private CardType(boolean isRect, boolean isFlat)
	{
		this.isFlat = isFlat;
		this.isRect = isRect;
	}

}
