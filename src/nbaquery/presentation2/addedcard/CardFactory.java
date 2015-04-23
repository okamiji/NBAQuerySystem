package nbaquery.presentation2.addedcard;

import nbaquery.presentation2.util.CardType;

public class CardFactory {
	public static Card create(CardType type, Object obj, boolean view_all){
		Card card = null;
		if(!view_all){
			switch(type){
			case PLAYER_RECT:
				card = new PlayerRectCard();break;
			case PLAYER_FLAT:
				card = new PlayerFlatCard();break;
			case TEAM_RECT:
				card = new TeamRectCard();break;
			case TEAM_FLAT:
				card = new TeamFlatCard();break;
			case MATCH_RECT:
				card = new MatchRectCard();break;
			case MATCH_FLAT:
				card = new MatchFlatCard();break;
			case MATCH_of_PLAYER:
				card = new MatchofPlayerCard();break;
			default:
				break;
			}
		}
		else{
			switch(type){
			case PLAYER_FLAT:
				card = new MoreFlatCard();break;
			case TEAM_FLAT:
				card = new MoreFlatCard();break;
			case MATCH_FLAT:
				card = new MoreFlatCard();break;
			case PLAYER_RECT:
				card = new MoreRectCard();break;
			case TEAM_RECT:
				card = new MoreRectCard();break;
			case MATCH_RECT:
				card = new MoreRectCard();break;
			case PLAYER_of_MATCH:
				card = new PlayerofMatchCard();break;
			default:
				break;
			}
		}
		card.create_card(obj);
		return card;
	}
}
