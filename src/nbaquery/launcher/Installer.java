package nbaquery.launcher;

import org.w3c.dom.Node;

public interface Installer<Installation>
{
	Installation install(Node documentNode, Object... params) throws Exception;
}
