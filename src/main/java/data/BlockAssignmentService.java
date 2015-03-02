package data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import util.ConfigLoader;

public class BlockAssignmentService 
{

	static BlockAssignmentService bas = null;
	
	private BlockAssignmentService()
	{
		
	}
	
	
	
	
	public static BlockAssignmentService getBlockAssignmentService()
	{
		if(bas==null) bas = new BlockAssignmentService();
		
		return bas;
	}
	
	
	public synchronized Integer getNewBlockNumber()
	{
		Integer blockNumber = -1;
		try
		{
			
			File f = new File(ConfigLoader.blockCounterFile);
			if(!f.exists())
			{
				blockNumber = 1;
			}
			else
			{
				BufferedReader br = new BufferedReader(new FileReader(f));
				blockNumber = Integer.parseInt(br.readLine());
				blockNumber++;
				br.close();

				f.delete();
			}

			BufferedWriter bw = new BufferedWriter(new FileWriter(f));
			bw.write(blockNumber);
			bw.flush();
			bw.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return blockNumber;
		
	}
	
	
	
}
