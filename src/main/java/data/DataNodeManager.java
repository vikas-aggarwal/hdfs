package data;

import java.util.Set;
import java.util.TreeSet;

import util.DataNode;

public class DataNodeManager 
{
	
	static DataNodeManager dm = null;
	Set<DataNode> dataNodes = null;
	
	private DataNodeManager()
	{
		dataNodes = new TreeSet<DataNode>();
	}
	
	
	public static DataNodeManager getDataNodeManager()
	{
		if(dm == null) dm = new DataNodeManager();
		
		return dm;
	}
	
	public void handleDataNodeHeartBeat(int dataNodeId)
	{
		
	}
	
	public synchronized void handleDataNodeDetection(DataNode d)
	{
		dataNodes.add(d);
	}
	
}
