package data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import util.DataNode;

public class BlockReportManager 
{

	private static BlockReportManager manager = null;
	private Map<Integer,Set<DataNode>> blockData = null;
	
	
	
	private BlockReportManager()
	{
		blockData = new HashMap<Integer, Set<DataNode>>();
	}
	
	
	
	public static BlockReportManager getBlockReportManager()
	{
		if(manager == null) manager = new BlockReportManager();
		
		return manager;
	}
	
	
	
	public synchronized List<Integer> handleBlockReport(int id,int ip,int port,List<Integer> blocks)
	{
		Iterator<Integer> blockIterator = blocks.iterator();
		DataNode dn = new DataNode(id, ip, port);
		DataNodeManager.getDataNodeManager().handleDataNodeDetection(dn);
		while(blockIterator.hasNext())
		{
			Integer block = blockIterator.next();
			Set<DataNode> nodes = blockData.get(block);
			if(nodes==null)
			{
			    nodes  = new HashSet<DataNode>();
			}
			
						
			nodes.add(dn);
			
			blockData.put(block, nodes);
			
		}
		return null;
	}
	
	
	public synchronized DataNode [] getBlockLocations(Integer blockNo)
	{
		Set<DataNode> nodes  =  blockData.get(blockNo);
		DataNode [] nodeList = new DataNode[nodes.size()];
		
		nodeList = nodes.toArray(nodeList);
		
		return nodeList;
	}
	
	
}
