package util;

public class DataNode 
{
	private int id;
	private int ip;
	private int port;
	
	
	public DataNode(int id,int ip,int port)
	{
		this.id = id;
		this.ip = ip;
		this.port = port;
	}
	
	
	public int getId() {
		return id;
	}
	@Override
	public boolean equals(Object obj) 
	{
		if(obj instanceof DataNode)
		{
			if(id == ((DataNode)obj).getId())
			{
				return true;
			}
		}
		return false;
	}


	public void setId(int id) {
		this.id = id;
	}
	public int getIp() {
		return ip;
	}
	public void setIp(int ip) {
		this.ip = ip;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	
	

	
}
