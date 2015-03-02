package data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import util.ConfigLoader;

public class FsImage 
{
  private static FsImage fs = null;
  private String fsImageRootPath = ConfigLoader.fsImagePath;
  
	private FsImage()
	{
		
	}
	
	
	
	public static FsImage getFsImage()
	{
		if(fs==null) fs = new FsImage();
		return fs;
	}
	
	
	public boolean exists(String fileName)
	{
		File file = new File(fsImageRootPath+"/"+fileName);
		
		return file.exists();
	}
	
	public int[] getBlocks(String fileName)
	{
		List<Integer> blockList = new ArrayList<Integer>();
		
		if(exists(fileName))
		{
			File file = null;
			BufferedReader br =null;
			try
			{
				file  = new File(fsImageRootPath+"/"+fileName);
				br = new BufferedReader(new FileReader(file));
				String blockNo = null;
				
				while((blockNo =br.readLine()) != null)
				{
					blockList.add(Integer.parseInt(blockNo));
				}
				
				int noOfBlocks = blockList.size();
				int finalBlockArray [] = new int[noOfBlocks];
				for(int i =0;i<noOfBlocks;i++)
				{
					finalBlockArray[i] = blockList.get(i);
				}
				
				return finalBlockArray;
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			finally
			{
				if(br!=null)
				{
					try {
						br.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			
		}
		return null;
	}
	
	
	
	public List<String> listFiles(String dirName)
	{
		List<String> files = new ArrayList<String>();
		
		File dir = new File(fsImageRootPath+"/"+dirName);
		if(dir.isDirectory())
		{
			String [] fileList = dir.list();
			for(String fileName : fileList)
			{
				files.add(fileName);
			}
		}
		
		return files;
	}
	
	
	public void commitNewFile(String fileName,List<Integer> blocks)
	{
		File file = new File(fsImageRootPath+"/"+fileName);
		BufferedWriter bw = null;
		try 
		{
			bw = new BufferedWriter(new FileWriter(file));
			Iterator<Integer> blockIterator = blocks.iterator();
			while(blockIterator.hasNext())
			{
				bw.write(String.valueOf(blockIterator.next()));
				bw.newLine();
			}
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		finally
		{
			if(bw!=null)
			{
				try 
				{
					bw.flush();
					bw.close();
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
				
			}
		}
	}
	
	
}
