package invertedIndex;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class DictionaryHandler {
	
	
	/**
	 * This method takes an array of Strings that represent the fileNames and returns the tree that was created by the 
	 * words of each text-file that was successfully loaded and read.Also after the end of the method the parameter
	 * problematicFiles which has an ArrayList<String> object (passed as an argument) is updated with all the 
	 * fileNames that were problematic and couldn't be opened or read.
	 * 
	 * @param fileNames an array that contains all the fileNames that are going to be used for loading the contents of the files
	 * @param problematicFiles A list with all the problematic files that occur after the end of teh method
	 * @return the tree that is created by the files-texts that were read
	 */
	public static  BinarySearchTree <WordRegistration> createTheDictionaryTree(String fileNames[],ArrayList<String> problematicFiles)
	{
		if(fileNames==null || fileNames.length<=0 || problematicFiles==null)
		 return null;
		
		
		BinarySearchTree <WordRegistration> tree=new BinarySearchTree <WordRegistration>();		// create the tree 
		
		Scanner inputStream=null;
		
		boolean hasReadAnyFiles=false;
		
		int currentDocumentID=0;			
		
		for(int i=0;i<fileNames.length;i++)
		{
			
			
			if(fileNames[i]==null)		// if a fileName is null ignore it
				continue;
			
			
			try 
			{
				inputStream=new Scanner(new FileInputStream(fileNames[i]));
			} 
			catch (FileNotFoundException e) 		// if there are problems with opening a file ignore them
			{
				problematicFiles.add(fileNames[i]);		// put the name of the problematic file to the list with all the problematic files
				continue;								// skip this file which produced the problem
			}
			
			
			int counter=0;
			
			if(inputStream.hasNext())								// if the file isn't blank then take 
			{
				currentDocumentID=tree.getNextAvailableDocumentID();
				hasReadAnyFiles=true;
			}
			
			while(inputStream.hasNext())
			{
				tree.insert(inputStream.next(), currentDocumentID, counter);
				counter++;
			}
			
			inputStream.close();
		}
		
		
		return (hasReadAnyFiles) ? tree : null ;
		
	}
	
	
	/**
	 * This method writes an object of type BinarySearchTree <WordRegistration> in a binary file.And returns
	 * whether this operation was successfully completed or not.
	 * 
	 * @param tree an object of type BinarySearchTree <WordRegistration> 
	 * @param fileName The name of the file in which the object will be written
	 * @return true if the object was written successfully to the file or false if not
	 */
	public static  boolean writeTheTreeToFile(BinarySearchTree <WordRegistration> tree,String fileName)
	{
		if(fileName==null || fileName.length()<=0 || tree==null)
			return false;
		
		
		ObjectOutputStream outputStream=null;
		
		try
		{
			outputStream=new ObjectOutputStream(new FileOutputStream(fileName));
			
			outputStream.writeObject(tree);	
			
			outputStream.close();
			
		} 
		catch (IOException e) 
		{
			return false;
		}
		
		
		
		return true;
		
	}
	
	/**
	 * This method takes the name of a directory ,reads all of the text files (as text files) in the directory
	 * ,creates an object of type BinaryTree and returns it.
	 * 
	 * 
	 * @param directoryName The name of the directory which contains the text files to be read
	 * @param problematicFileNames An ArrayList<String> object which after the end of the mewthod it contains all
	 * 		  the names of the files in the directory which couldn't be read.
	 * @return the tree that was created by the text files that were read from the directory which was given
	 * 			as an argument in the parameter directoryName
	 */
	public static BinarySearchTree <WordRegistration> readAllDocumentsFromDirectoryAndCreateDictionaryTree(String directoryName,ArrayList<String> problematicFileNames)
	{
		if(directoryName==null || directoryName.length()==0)
			return null;
		
		File file=new File(directoryName);
		
		if(!file.isDirectory())
			return null;
		
		File files[]=file.listFiles();
		
		
		
		ArrayList<String> documentList=new ArrayList<String>();
		
		for(File aFile : files)
			if(!aFile.isDirectory())
				documentList.add(aFile.getAbsolutePath());
		
	
			
		return createTheDictionaryTree(documentList.toArray(new String[documentList.size()]), problematicFileNames);
		
			
	}
	
	

	
	/**
	 * The interface method of this class that handles the interface for creating the BinaryTree which contains the entries
	 * objects about the words.It is a simple main that uses simple methods and libraries of the java to 
	 * create the interface of this program.
	 * 
	 */
	public static void interfaceOFDictionaryHandler()
	{
		
		Scanner keyboard=new Scanner(System.in);
		
		int option=0;
		BinarySearchTree <WordRegistration> tree=null;
		String inputString=null;
		
		do 
		{
			
			System.out.println("------------------- Menu of Dictionary Interface -------------------\n");
			System.out.println("1 ) Create The Tree and put the Tree in a file.");
			System.out.println("2 ) Exit the menu.\n");
			
			boolean fine=false;
			System.out.println("Give your option : \n");
			
			while(!fine)
			{
				try
				{
					option=keyboard.nextInt();
					fine=true;
				}
				catch(InputMismatchException e)
				{
					keyboard.nextLine();
					System.out.println("Give a correct representation of a number\n\n");
				}
			}
			keyboard.nextLine();
			
			
			if(option==1)
			{
				boolean usePreviousTree=false;
				
				if(tree!=null)
				{
					System.out.println("Do you want to keep the previous tree that was read?");
					System.out.println("Enter yes or no.");
					
					inputString=keyboard.next();
					keyboard.nextLine();
					
					while(!inputString.equalsIgnoreCase("yes") && !inputString.equalsIgnoreCase("no"))
					{
						System.out.println("Enter only yes or no .Try agin.");
						inputString=keyboard.next();
						keyboard.nextLine();
						
					}
					
					usePreviousTree=inputString.equalsIgnoreCase("yes");
					
				}
				
				if(!usePreviousTree)
				{
					// list where all the problematic files will be placed
					
					ArrayList<String> problematicFiles=new ArrayList<String>();
					
					System.out.println("Give the name or a path of a directory : ");
					 inputString=keyboard.nextLine().trim();
					tree=readAllDocumentsFromDirectoryAndCreateDictionaryTree(inputString, problematicFiles);
					
					if(problematicFiles.size()>0)
					{
						System.out.println("The problematic files are : ");
						for(int i=0;i<problematicFiles.size();i++)
							System.out.println(i+") "+problematicFiles.get(i));
					}
					
					if(tree!=null)
						System.out.println("You have created the tree succesfully.\n");
					else
						System.out.println("The tree haven't been created.\n");
					
				}
				
				
				
				if(tree!=null)
				{
					
					System.out.println("Give a fileName where you want to place the tree : \n");
				
					// Before writing the BinaryTree to a binary file ,apply the skip pointers to that BinaryTree.
					tree.setTheNumberOfSkipPointers(0);
					tree.applySkipPointers();
					
					inputString=keyboard.nextLine().trim();
					
					if(!writeTheTreeToFile( tree,inputString))
					{
						System.out.println("The tree couldn't be written to the file "+inputString+".\n");
					}
					else System.out.println("The tree was  written to the file "+inputString+".\n");
					
				}
				
						
				
			}
			
			
		}while(option!=2);
		
		keyboard.close();
		System.out.println("Exiting the Menu of Dictionary Interface  ");
		
	}
	
	public static void main(String[] args)
	{
		interfaceOFDictionaryHandler();
		
		
	}

}
