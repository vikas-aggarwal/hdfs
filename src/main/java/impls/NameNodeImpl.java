package impls;

import java.util.Iterator;
import java.util.List;

import util.ConfigLoader;
import util.DataNode;

import com.google.protobuf.InvalidProtocolBufferException;

import static data.Constants.*;
import data.BlockAssignmentService;
import data.BlockReportManager;
import data.DataNodeManager;
import data.FileHandlerService;
import data.FsImage;
import dataSerializer.SerializerDeserializer;
import interfaces.INameNode;

public class NameNodeImpl implements INameNode 
{
	
	public NameNodeImpl()
	{
		
	}

	public byte[] openFile(byte[] data) {
		
		try 
		{
			SerializerDeserializer.OpenFileRequest openFileRequest  =  SerializerDeserializer.OpenFileRequest.parseFrom(data);
			String fileName 		= openFileRequest.getFileName();
			boolean  forRead  	= openFileRequest.getForRead();
			FsImage fs = FsImage.getFsImage();
			
			if(forRead)
			{
				//Read from fsimage send list of blocks
				if(fs.exists(fileName))
				{
					int [] blocks = fs.getBlocks(fileName);
					SerializerDeserializer.OpenFileResponse.Builder response = SerializerDeserializer.OpenFileResponse.newBuilder();
					response.setStatus(SUCCESS);
					response.setHandle(-1);
					for(int block : blocks){response.addBlockNums(block);}
					
					return response.build().toByteArray();
				}
				else
				{
					SerializerDeserializer.OpenFileResponse.Builder response = SerializerDeserializer.OpenFileResponse.newBuilder();
					response.setStatus(FILE_DOES_NOT_EXISTS);
					response.setHandle(-1);
					
					return response.build().toByteArray();
				}
				
				
			}
			else //write mode
			{
				//Create a in memory handle 
				if(fs.exists(fileName))
				{
					SerializerDeserializer.OpenFileResponse.Builder response = SerializerDeserializer.OpenFileResponse.newBuilder();
					response.setStatus(FILE_ALREADY_EXISTS);
					response.setHandle(-1);
					
					return response.build().toByteArray();
				}
				else
				{
					int handle = FileHandlerService.getFileHandlerService().createNewFileHandle(fileName);
					
					SerializerDeserializer.OpenFileResponse.Builder response = SerializerDeserializer.OpenFileResponse.newBuilder();
					response.setStatus(SUCCESS);
					response.setHandle(handle);
					
					return response.build().toByteArray();
				}
				
			}
			
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		
		
		SerializerDeserializer.OpenFileResponse.Builder response = SerializerDeserializer.OpenFileResponse.newBuilder();
		response.setStatus(UNKNOWN_ERROR);
		response.setHandle(-1);
		
		return response.build().toByteArray();
	}

	public byte[] closeFile(byte[] data) {
		
		System.out.println("CloseFile");
		
		try 
		{
			SerializerDeserializer.CloseFileRequest closeFileRequest = SerializerDeserializer.CloseFileRequest.parseFrom(data);
			int handle  = closeFileRequest.getHandle();
			
			//TODO commit fsimage
			String fileName = FileHandlerService.getFileHandlerService().getFileName(handle);
			
			
			SerializerDeserializer.CloseFileResponse.Builder response  =  SerializerDeserializer.CloseFileResponse.newBuilder();
			response.setStatus(SUCCESS);
			
			return response.build().toByteArray();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		SerializerDeserializer.CloseFileResponse.Builder response  =  SerializerDeserializer.CloseFileResponse.newBuilder();
		response.setStatus(UNKNOWN_ERROR);
		
		return response.build().toByteArray();
	}

	public byte[] getBlockLocations(byte[] data) {

		try 
		{
			SerializerDeserializer.BlockLocationRequest blockLocationRequest  =  SerializerDeserializer.BlockLocationRequest.parseFrom(data);
			List<Integer> blockNums = blockLocationRequest.getBlockNumsList();
			
			
			
			SerializerDeserializer.BlockLocationResponse.Builder blockLocationResponse = SerializerDeserializer.BlockLocationResponse.newBuilder();
			blockLocationResponse.setStatus(SUCCESS);
			
			Iterator<Integer> blockIterator = blockNums.iterator();
			
			
			
			while(blockIterator.hasNext())
			{
				Integer blockNo = blockIterator.next();
				DataNode [] nodes  = BlockReportManager.getBlockReportManager().getBlockLocations(blockNo);
				
				SerializerDeserializer.BlockLocations.Builder blockLocation = SerializerDeserializer.BlockLocations.newBuilder();
				blockLocation.setBlockNumber(blockNo);
				
				for(int i =0; i < nodes.length ; i++)
				{
				
					SerializerDeserializer.DataNodeLocation.Builder dataNode  = SerializerDeserializer.DataNodeLocation.newBuilder();
				
					dataNode.setIp(nodes[i].getIp());
					dataNode.setPort(nodes[i].getPort());
					blockLocation.addLocations(dataNode.build());
				}
			
				blockLocationResponse.addBlockLocations(blockLocation.build());
			}
			
		
			return blockLocationResponse.build().toByteArray();
			
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		

		SerializerDeserializer.BlockLocationResponse.Builder blockLocationResponse = SerializerDeserializer.BlockLocationResponse.newBuilder();
		blockLocationResponse.setStatus(UNKNOWN_ERROR);
		return blockLocationResponse.build().toByteArray();
		
	}

	public byte[] assignBlock(byte[] data) 
	{
		
		
		Integer newBlockNumber = BlockAssignmentService.getBlockAssignmentService().getNewBlockNumber();
		
		return null;
	}

	public byte[] list(byte[] data) {
		
		try 
		{
			SerializerDeserializer.ListFilesRequest listFileRequest = SerializerDeserializer.ListFilesRequest.parseFrom(data);
			String dirName  = listFileRequest.getDirName();
			List<String> files  = FsImage.getFsImage().listFiles(dirName);
			
			
			SerializerDeserializer.ListFilesResponse.Builder listFilesResponse = SerializerDeserializer.ListFilesResponse.newBuilder();
			listFilesResponse.setStatus(SUCCESS);
			
			Iterator<String> fileIter = files.iterator();
			while(fileIter.hasNext())
			{
				String fileName = fileIter.next();
				listFilesResponse.addFileNames(fileName);
			}
			
			return listFilesResponse.build().toByteArray();
			
		} 
		catch (InvalidProtocolBufferException e) 
		{
			e.printStackTrace();
		}
		
		SerializerDeserializer.ListFilesResponse.Builder listFilesResponse = SerializerDeserializer.ListFilesResponse.newBuilder();
		listFilesResponse.setStatus(UNKNOWN_ERROR);
		return listFilesResponse.build().toByteArray();
		
	}

	public byte[] blockReport(byte[] data) {
		
		
		try 
		{
			SerializerDeserializer.BlockReportRequest blockReportRequest = SerializerDeserializer.BlockReportRequest.parseFrom(data);
			int dataNodeId = blockReportRequest.getId();
			List<Integer> blockNumbers = blockReportRequest.getBlockNumbersList();
			SerializerDeserializer.DataNodeLocation nodeLocation =  blockReportRequest.getLocation();
			int ip = nodeLocation.getIp();
			int port = nodeLocation.getPort();
			
			List<Integer> status = BlockReportManager.getBlockReportManager().handleBlockReport(dataNodeId, ip, port, blockNumbers);
			
			SerializerDeserializer.BlockReportResponse.Builder blockReportResponse = SerializerDeserializer.BlockReportResponse.newBuilder();
			blockReportResponse.addAllStatus(status);
			
			blockReportResponse.build().toByteArray();
			
		} 
		catch (InvalidProtocolBufferException e) 
		{
			e.printStackTrace();
		}
		
		SerializerDeserializer.BlockReportResponse.Builder blockReportResponse = SerializerDeserializer.BlockReportResponse.newBuilder();
		return blockReportResponse.build().toByteArray();
	}

	public byte[] heartBeat(byte[] data) {

		try 
		{
			SerializerDeserializer.HeartBeatRequest heartBeatRequest = SerializerDeserializer.HeartBeatRequest.parseFrom(data);
			DataNodeManager.getDataNodeManager().handleDataNodeHeartBeat(heartBeatRequest.getId());
			
			SerializerDeserializer.HeartBeatResponse.Builder heartBeatResponse = SerializerDeserializer.HeartBeatResponse.newBuilder();
			heartBeatResponse.setStatus(SUCCESS);
			return heartBeatResponse.build().toByteArray();
		} 
		catch (InvalidProtocolBufferException e) 
		{
			e.printStackTrace();
		}
		SerializerDeserializer.HeartBeatResponse.Builder heartBeatResponse = SerializerDeserializer.HeartBeatResponse.newBuilder();
		heartBeatResponse.setStatus(UNKNOWN_ERROR);
		return heartBeatResponse.build().toByteArray();
	}

}
