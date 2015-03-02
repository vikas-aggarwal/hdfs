package main;

import impls.NameNodeImpl;
import interfaces.INameNode;

import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import util.ConfigLoader;

public class Main 
{
	public static void main(String[] args) 
	{
	
		try
		{
			ConfigLoader.loadConfig();
			String name  = "NameNode";
			NameNodeImpl nameNode  = new NameNodeImpl();
			INameNode nameNodeStub = (INameNode) UnicastRemoteObject.exportObject(nameNode,0);
			Registry registry = LocateRegistry.getRegistry();
            registry.bind(name,  nameNodeStub);
            System.out.println("Server Started");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
