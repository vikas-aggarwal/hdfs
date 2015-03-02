package data;

public class FileHandlerService 
{
	private static FileHandlerService fh = null;
	
	private FileHandlerService()
	{
		//TODO load file handler data
	}
	
	
	public static FileHandlerService getFileHandlerService()
	{
		if(fh==null) fh = new FileHandlerService();
		
		return fh;
	}
	
	
	public int createNewFileHandle(String fileName)
	{
		
		return -1;
	}
	
	public String getFileName(int handle)
	{
		return null;
	}
	
}
