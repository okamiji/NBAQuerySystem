package nbaquery_test.auto;

public interface TestFacadeToken
{
	public String[] getTokens();
	
	public int params();
	
	public void change(Console facade, String[] parameters, int beginIndex);
}
