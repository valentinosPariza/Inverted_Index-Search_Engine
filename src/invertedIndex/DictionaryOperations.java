package invertedIndex;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.StringTokenizer;

public class DictionaryOperations {
	
public static final int DEFAULT_NUMBER_FOR_SIMULATIONS=1000;
public static final int ELEMENTS_TO_USE_FOR_TEST=500;

private BinarySearchTree < WordRegistration > dictionaryTree;
	
/**
 * Constructor for an object of this class which offers many operations on the data-entries in the tree.
 * 
 * @param fileName The name of the file from where the tree will be loaded
 * @throws IOException When reading from the file
 * @throws ClassNotFoundException If the tree read from the file doesn't implements the class Serializable
 * @throws NullPointerException if the name given is null
 * @throws FileNotFoundException if the name of the file given doesn't correspond to an actual binary file,or can't be opened
 */
public DictionaryOperations(String fileName) throws FileNotFoundException, NullPointerException, ClassNotFoundException, IOException
{
	
	this.loadDictionaryTree(fileName);
}
	
/**
 * 
 * This method loads the BinaryTree with the word-entries from a file .
 * 
 * @param fileName the name of the file from where the tree is loaded 
 * @throws IOException When reading from the file
 * @throws ClassNotFoundException If the tree read from the file doesn't implements the class Serializable
 * @throws NullPointerException if the name given is null
 * @throws FileNotFoundException if the name of the file given doesn't correspond to an actual binary file,or can't be opened
 */
private void loadDictionaryTree(String fileName) throws FileNotFoundException, IOException,NullPointerException, ClassNotFoundException
{
	if(fileName==null)
		throw new NullPointerException();
	
	ObjectInputStream inputStream=null;
	
	inputStream=new ObjectInputStream(new FileInputStream(fileName));
	
	this.dictionaryTree=(BinarySearchTree < WordRegistration>)inputStream.readObject();
	
	inputStream.close();
}

/**
 * This method prints information about a WordRegistration object that corresponds to the word-string that is passed
 * as an argument
 * 
 * @param word The String word which corresponds to a WordRegistration,if exists
 */
public void printInformationAboutWordRegistration(String word)
{
	if(word==null || word.length()<=0)
		return ;
	
	
	WordRegistration element=this.dictionaryTree.find(word);
	
	if(element==null)
		System.out.println("No word registration exists with this String word\n");
	else 
		System.out.println(element);
	
}


/**
 * This method takes an array of Strings which are the names of some words and applies on them the AND operation
 * on the WordOperation objects which correspond to these words
 * 
 * @param words An array of Strings that are words which the method applies on them the AND operation
 */
public void findCombinationOfWordsOperationAND(String[] words)
{
	if(words==null || words.length<=0)
		return ;
	
	ArrayList<WordRegistration> list=new ArrayList<WordRegistration>();
	
	ArrayList<Integer> documentIDs=new ArrayList<Integer>();
	
	boolean wordsFound=true;
	WordRegistration element=null;
	
	for(int i=0;i<words.length;i++)
	{
		element=this.dictionaryTree.find(words[i]);
		
		if(element==null)
		{
			wordsFound=false;
			break;
		}
		else
			list.add(element);
		
	}
	
	if(wordsFound==false || list.size()<=0)
	{
		System.out.println("The words couldn't be found together in a file because a word that you requested don't exist"
				+ "in the dictionary.");
		return ;
	}
	
	
	boolean status=list.get(0).documentsListsAND(list.toArray(new WordRegistration[list.size()]), documentIDs, false);
	
	
	if(!status || documentIDs.size()==0)
	{
		System.out.println("No words could be found together in any files");
	}
	else
	{
		System.out.println("The words :\n");
		
		for(int i=0;i<words.length;i++)
		{
			if(words[i]!=null)
			{
				System.out.println((i+1)+") "+words[i]);
			}
		}
		
		System.out.println();
		System.out.println("The document ids where the words found together are :");
		
		for(int i=0;i<documentIDs.size();i++)
		{
			
			System.out.print(documentIDs.get(i)+" / ");
			
			if(i!=0 && i%15==0)
			{
				System.out.println();
			}
		}
	}
	
	System.out.println();
	System.out.println();
	
}

/**
 * 
 * @param words
 */
public void findCombinationOfWordsOperationOR(String[] words)
{
	if(words==null || words.length<=0)
		return ;
	
	ArrayList<WordRegistration> list=new ArrayList<WordRegistration>();
	
	ArrayList<Integer> documentIDs=new ArrayList<Integer>();
	
	
	WordRegistration element=null;
	
	for(int i=0;i<words.length;i++)
	{
		element=this.dictionaryTree.find(words[i]);
		
		if(element!=null)
		{
			list.add(element);
		}
				
	}
	
	if(list.size()<=0)
	{
		System.out.println("None of the words where in the dictionary.\n");
		return ;
	}
	
	
	
	boolean status=list.get(0).documentsListsOR(list.toArray(new WordRegistration[list.size()]), documentIDs, false);
	
		
	if(!status || documentIDs.size()==0)
	{
		System.out.println("No words could be found in any files");
	}
	else
	{
		System.out.println("The words :\n");
		
		for(int i=0;i<words.length;i++)
		{
			if(words[i]!=null)
			{
				System.out.println((i+1)+") "+words[i]);
			}
		}
		
		System.out.println();
		System.out.println("The document ids where the words found are :\n");
		
		for(int i=0;i<documentIDs.size();i++)
		{
			System.out.print(documentIDs.get(i)+" / ");
			
			if(i!=0 && i%20==0)
			{
				System.out.println();
			}
		}
	}
	
	System.out.println();
	System.out.println();
	
}



/**
 * This method takes the name of a binary file to store the BinaryTree object that is encapsulated in this object
 * and tries to save it there.At the end returns whether it managed to do it or not. 
 * 
 * @param fileName The name of the file where the method tries to store the BinaryTree
 * @return true if the tree was successfully stored or false otherwise
 */
public boolean storeDictionaryTreeInFile(String fileName)
{
	if(fileName==null || fileName.length()<=0)
		return false;
	
	try 
	{
		ObjectOutputStream outputStream=new ObjectOutputStream(new FileOutputStream(fileName));
		
		outputStream.writeObject(this.dictionaryTree);
		
		outputStream.close();
	} 
	catch (IOException e) {
		return false;
	}

	return true;
}


/**
 * This method takes a String which represent a prefix and prints all the word-entries from the tree that start from 
 * this prefix String.
 * 
 * @param prefix a String which represent the starting subString of the words to search for
 */
public void printTheWordRegistrationWhichStartFrom(String prefix)
{
	if(prefix==null || prefix.length()<=0)
		return ;
	
	
	
	WordRegistration[] registrations=this.dictionaryTree.findWordRegistersWhichWordsStartWith(prefix);
	
	if(registrations==null || registrations.length<=0)
	{
		System.out.println("\nNo words starting from this prefix where found.\n");
		// No words found to start with this prefix
	}
	else
	{
		
		// Print the words that were found from the BinaryTree
		
		System.out.println("\nThe word registrations that have word with prefix \""+prefix+"\" are :");
		
		for(int i=0;i<registrations.length;i++)
		{
			System.out.println("Registration "+(i+1));
			System.out.println(registrations[i]);
			
		}
	
	
		System.out.println("More synoptical the words are : \n");
	
		for(int i=0;i<registrations.length;i++)
		{
		
			System.out.print(" | "+registrations[i].getWord());
			if(i!=0 && i%10==0)
			{
			System.out.println(" |");
			}
		}
		
		System.out.println();
	
	}
	
}


/**
 * This method takes a String which represent a suffix and prints all the word-entries from the tree that end with 
 * this suffix subString.
 * 
 * @param suffix a String which represent the ending subString of the words to search for
 */
public void printTheWordRegistrationWhichEndWith(String suffix)
{
	if(suffix==null || suffix.length()<=0)
		return ;
	
	WordRegistration[] registrations=this.dictionaryTree.findWordRegistersWhichWordsEndWith(suffix);
	
	if(registrations==null || registrations.length<=0)
	{
		System.out.println("\nNo words ending with this suffix were found.\n");
		
		// No words found to start with this prefix
	}
	else
	{
		
		// Print the words that were found from the BinaryTree
		
		System.out.println("\nThe word registrations that have word with suffix \""+suffix+"\" are :");
		
		for(int i=0;i<registrations.length;i++)
		{
			System.out.println("Registration "+(i+1));
			System.out.println(registrations[i]);
			
		}
		
		System.out.println("More synoptical the words are : \n");
		
		for(int i=0;i<registrations.length;i++)
		{
		
			System.out.print(" | "+registrations[i].getWord());
			if(i!=0 && i%10==0)
			{
			System.out.println(" |");
			}
		}
		
		System.out.println();
		
	}
	
	
}

/**
 * This method takes a String which represent a suffix and prints all the word-entries from the tree that end with 
 * this suffix subString.
 * 
 * @param aString a String which represent a subString of the words to search for(if it the words include it)
 */
public void printTheWordRegistrationWhichContain(String aString)
{
	if(aString==null || aString.length()<=0)
		return ;
	
	WordRegistration[] registrations=this.dictionaryTree.findWordRegistersWhichWordsContain(aString);
	
	if(registrations==null || registrations.length<=0)
	{
		System.out.println("\nNo words conatining this string were found.\n");
	}
	else
	{
		System.out.println("\nThe word registrations that contain the word \""+aString+"\" are :");
		
		for(int i=0;i<registrations.length;i++)
		{
			System.out.println("Registration "+(i+1));
			System.out.println(registrations[i]);
			
		}
		
		System.out.println("More synoptical the words are : \n");
		
		for(int i=0;i<registrations.length;i++)
		{
		
			System.out.print(" | "+registrations[i].getWord());
			if(i!=0 && i%10==0)
			{
			System.out.println(" |");
			}
		}
		
		System.out.println();
		
	}
	
}


/**
 * This method takes a String which represent a phrase and prints of this phrase exists in the tree dictionary and
 * ,in what document file and where exactly in the document files
 * 
 * @param phrase a String which represent a phrase (many words which are seperated by whitespaces)
 */
public void searchPhrases(String phrase)
{
	if(phrase==null || phrase.length()<=0)
		return ;
	
	StringTokenizer tokenizer=new StringTokenizer(phrase);
	
	WordRegistration registrations[]=new WordRegistration[tokenizer.countTokens()];
	
	
	for(int i=0;i<registrations.length;i++)
	{
		registrations[i]=this.dictionaryTree.find(tokenizer.nextToken());
		
		if(registrations[i]==null)
		{
			System.out.println("This phrase doesn't exist in the dictionary tree.");
			return ;
		}
	}
	
	ArrayList<Integer> documentIds=new ArrayList<Integer>();
	
	if(!registrations[0].documentsListsAND(registrations, documentIds, false) || documentIds.size()<=0)
	{
		System.out.println("This phrase doesn't exist in the dictionary tree.");
	}
	
	
	
	int size=documentIds.size();
	
	boolean foundAlmostOne=false;
	
	int i=0,j=0,q=0;
	
	
	
	for( i=0;i<size;i++)
	{
		// we examine the first word at the phrase if all the rest words are following immediately  
		
		int docID=documentIds.get(i);
		
		
		int appearPositions[]=registrations[0].getAppearancesInDocumentId(docID);
		
		
		for(j=0;j<appearPositions.length;j++)
		{
			int position=appearPositions[j];
			
			boolean found=true;
			
			
			for(q=1;q<registrations.length;q++)
			{
				
				if(!registrations[q].containsDocIDAndPosition(docID,position+1))
				{
					
					found=false;
					break;
				}
				position++;
				
			}
			
		
			
			if(found)
			{
				System.out.println("The phrase was found at document with id = "+docID+" and at position = "+appearPositions[j]+"\n");
				foundAlmostOne=true;
			}
			
		}
		
	}
	
	if(!foundAlmostOne)
	{
		System.out.println("This phrase doesn't exist in the dictionary tree.");
	}
	
}




/**
 * This method takes a fileName and add the words-String of the text file to the dictionary BinarySearchTree of
 * the object of this class.
 * 
 * @param fileName The name of text file 
 * @return true if the text file was added to the tree or false if not
 */
public boolean addNewDocument(String fileName)
{
	if(fileName==null || fileName.length()==0)
		return false;
	
	Scanner inputStream=null;
	
	try
	{
		inputStream=new Scanner(new FileInputStream(fileName));
	} 
	catch (FileNotFoundException e) {
		
		return false;
	}
	
	int currentDocumentID=0;
	
	
	if(inputStream.hasNext())								// if the file isn't blank then take the next available
	{														// document id for the document
		currentDocumentID=dictionaryTree.getNextAvailableDocumentID();
		
	}
	
	int counter=0;
	
	while(inputStream.hasNext())
	{
		dictionaryTree.insert(inputStream.next(), currentDocumentID, counter);
		counter++;
	}
	
	dictionaryTree.setTheNumberOfSkipPointers(0); // set the number of the skip pointers base on the sqrt(n)
	dictionaryTree.applySkipPointers();
	
	inputStream.close();
	
	return true;
	
}

/**
 * Prints the menu of the interface of this program-class
 */
private static void printMenu()
{
	System.out.println("1) Load a dictionary(Tree).");
	System.out.println("2) Search for a word in the dictionary.");
	System.out.println("3) Search for a set of words in common documents(Operation AND).");
	System.out.println("4) Search for words in the tree in all the documents(Operation OR).");
	System.out.println("5) Wild card searches.");
	System.out.println("6) Phrase search");
	System.out.println("7) Add new document to the dictionary ");
	System.out.println("8) Store the dictionary to a file.");
	System.out.println("9) Run a test which shows the importance of the skip pointers and what is the unique number of "
			+ "pointers.");
	System.out.println("10) Exit the dictionary-operations mode.");
}

/**
 * Checks if a String represents a correct wild-card option search. 
 * 
 * @param aString the STring to check if it is a correct wild-card option search
 * @return true if the String is a correct representation of a wild-card option or false if it isn't
 */
private static boolean isCorrectWildCardOption(String aString)
{
	
	int length=aString.length();
	
	// we suppose that at least one character and the * symbol must be given
	if(aString==null || length<=1)
	return false;
	
	if(length==2 && aString.charAt(0)=='*' && aString.charAt(length-1)=='*')
		return false;
	
	if(aString.charAt(0)!='*' && aString.charAt(length-1)!='*')
		return false;
	
	
	for(int i=1;i<length-1;i++)         // skip the first and the last characters and check if there is a star 
	{ 									// in the word except the first and last characters.We suppose that
		if(aString.charAt(i)=='*')		// a star character in the middle is not correct  
			return false;
		
	}
	
	return true;
	
}

/**
 * This method runs a simulation which shows the importance of skip pointers in the program .And also shows
 *  which is the ideal way of calculating the number of skip pointers for n data in a List.
 * 
 */
public void runTestAboutSkipPointers()
{
	
	if(this.dictionaryTree==null)
		return ;
	
	
	ArrayList<WordRegistration> registrations=new ArrayList<WordRegistration>(20);
	
	WordRegistration temp=null;
	
	int i=0;
	// fill the array with random registrations
	for( i=0;i<ELEMENTS_TO_USE_FOR_TEST;i++)
	{
		
		do 
		{
			temp=this.dictionaryTree.getRandomWordRegistration();
			
			
		}while(temp==null);
		
		registrations.add(temp);
		temp=null;
	}
	
	
	int numberOfTests=(int)(Math.random()*ELEMENTS_TO_USE_FOR_TEST+DEFAULT_NUMBER_FOR_SIMULATIONS);
	
	ArrayList<WordRegistration> chosenRegistrations=new ArrayList<WordRegistration>();
	long[] sum=new long[9];
	int j=0;
	int randomNumber=0;
	
	for(i=0;i<numberOfTests;i++)
	{
		
		// from all the  registrations choose some of them which are going to be used in an AND operation
		randomNumber=(int)(Math.random()*registrations.size());
		
		for(j=0;j<randomNumber;j++)
		{
			// choose some of them
			chosenRegistrations.add(registrations.get( (int)(Math.random()*registrations.size()) ));
		}
		
		this.testOperationANDWithSkipPointers(chosenRegistrations.toArray(new WordRegistration[chosenRegistrations.size()]), sum);
		chosenRegistrations.clear();
	}
	
	this.testOperationANDWithSkipPointers(sum, numberOfTests);
	this.dictionaryTree.setTheNumberOfSkipPointers(0);
	this.dictionaryTree.applySkipPointers();
	
}

/**
 * This method tests the AND operation on a specific set of WordRegistration entries,calculates the estimated time
 * to execute each situation of different numbers of skip pointers and updates the array of the sums.
 * 
 * @param registrations The word registration to test with the operation AND on a set of situations of different
 * 			numbers of skip pointers
 * @param sum an array that contains all the sums times of execution for each option calculation for the number of 
 * skip pointers 
 */
private  void testOperationANDWithSkipPointers(WordRegistration[] registrations,long[] sum)
{
	if(registrations==null || registrations.length<=0|| sum==null || sum.length<9  )
		return ;
	
	ArrayList<Integer> list=new ArrayList<Integer>(100);
	long startTime=0;
	long endTime=0;
	
	this.dictionaryTree.deleteSkipPointers();
	this.dictionaryTree.setTheNumberOfSkipPointers(0);
	this.dictionaryTree.applySkipPointers();
	startTime=System.currentTimeMillis();
	registrations[0].documentsListsAND(registrations, list, false);
	endTime=System.currentTimeMillis();
	sum[0]+=endTime-startTime;
	
	
	this.dictionaryTree.setTheNumberOfSkipPointers(-1);
	this.dictionaryTree.applySkipPointers();
	startTime=System.currentTimeMillis();
	registrations[0].documentsListsAND(registrations, list, false);
	endTime=System.currentTimeMillis();
	sum[1]+=endTime-startTime;
	
	
	this.dictionaryTree.setTheNumberOfSkipPointers(-2);
	this.dictionaryTree.applySkipPointers();
	startTime=System.currentTimeMillis();
	registrations[0].documentsListsAND(registrations, list, false);
	endTime=System.currentTimeMillis();
	sum[2]+=endTime-startTime;
	

	this.dictionaryTree.setTheNumberOfSkipPointers(-3);
	this.dictionaryTree.applySkipPointers();
	startTime=System.currentTimeMillis();
	registrations[0].documentsListsAND(registrations, list, false);
	endTime=System.currentTimeMillis();
	sum[3]+=endTime-startTime;
	
	
	
	this.dictionaryTree.setTheNumberOfSkipPointers(2);
	this.dictionaryTree.applySkipPointers();
	startTime=System.currentTimeMillis();
	registrations[0].documentsListsAND(registrations, list, false);
	endTime=System.currentTimeMillis();
	sum[4]+=endTime-startTime;
	
	
	this.dictionaryTree.setTheNumberOfSkipPointers(4);
	this.dictionaryTree.applySkipPointers();
	startTime=System.currentTimeMillis();
	registrations[0].documentsListsAND(registrations, list, false);
	endTime=System.currentTimeMillis();
	sum[5]+=endTime-startTime;
	
	
	this.dictionaryTree.setTheNumberOfSkipPointers(8);
	this.dictionaryTree.applySkipPointers();
	startTime=System.currentTimeMillis();
	registrations[0].documentsListsAND(registrations, list, false);
	endTime=System.currentTimeMillis();
	sum[6]+=endTime-startTime;
	
	
	this.dictionaryTree.setTheNumberOfSkipPointers(16);
	this.dictionaryTree.applySkipPointers();
	startTime=System.currentTimeMillis();
	registrations[0].documentsListsAND(registrations, list, false);
	endTime=System.currentTimeMillis();
	sum[7]+=endTime-startTime;
	
	
	this.dictionaryTree.deleteSkipPointers();
	startTime=System.currentTimeMillis();
	registrations[0].documentsListsAND(registrations, list, false);
	endTime=System.currentTimeMillis();
	sum[8]+=endTime-startTime;
	
	
	
}

/**
 * This method prints the average time that a method of calculating the number of skip pointers on n data ,accomplishes.
 * 
 * @param sum an array that contains all the sums times of execution for each option calculation for the number of 
 * skip pointers 
 * @param numberOfTests The number of tests that tested
 */
private void testOperationANDWithSkipPointers(long[] sums,int numberOfTests)
{
	if(sums==null || numberOfTests<=0)
		return ;
	
	
	
	System.out.println("Testing option of no having any skip pointers :");
	System.out.println("The time that it wanted to complete is ");
	System.out.println((sums[8]/(double)numberOfTests)+" ms");
	System.out.println((sums[8]/(double)numberOfTests)/1000+" s");
	System.out.println();
	
	System.out.println("Testing option : sqrt(n)");
	System.out.println("The time that it wanted to complete is ");
	System.out.println((sums[0]/(double)numberOfTests)+" ms");
	System.out.println((sums[0]/(double)numberOfTests)/1000+" s");
	System.out.println();
	
	System.out.println("Testing option : n^(1/3)");
	System.out.println("The time that it wanted to complete is ");
	System.out.println((sums[1]/(double)numberOfTests)+" ms");
	System.out.println((sums[1]/(double)numberOfTests)/1000+" s");
	System.out.println();
	
	System.out.println("Testing option : log(n) -> ln(n)");
	System.out.println("The time that it wanted to complete is ");
	System.out.println((sums[2]/(double)numberOfTests)+" ms");
	System.out.println((sums[2]/(double)numberOfTests)/1000+" s");
	System.out.println();
	
	System.out.println("Testing option : log10(n)");
	System.out.println("The time that it wanted to complete is ");
	System.out.println((sums[3]/(double)numberOfTests)+" ms");
	System.out.println((sums[3]/(double)numberOfTests)/1000+" s");
	System.out.println();
	
	System.out.println("Testing option : n/2");
	System.out.println("The time that it wanted to complete is ");
	System.out.println((sums[4]/(double)numberOfTests)+" ms");
	System.out.println((sums[4]/(double)numberOfTests)/1000+" s");
	System.out.println();
	
	System.out.println("Testing option : n/4");
	System.out.println("The time that it wanted to complete is ");
	System.out.println((sums[5]/(double)numberOfTests)+" ms");
	System.out.println((sums[5]/(double)numberOfTests)/1000+" s");
	System.out.println();
	
	System.out.println("Testing option : n/8");
	System.out.println("The time that it wanted to complete is ");
	System.out.println((sums[6]/(double)numberOfTests)+" ms");
	System.out.println((sums[6]/(double)numberOfTests)/1000+" s");
	System.out.println();
	
	System.out.println("Testing option : n/16");
	System.out.println("The time that it wanted to complete is ");
	System.out.println((sums[7]/(double)numberOfTests)+" ms");
	System.out.println((sums[7]/(double)numberOfTests)/1000+" s");
	System.out.println();
	
	
}


/**
 * This method is the  method that runs the interface of this second program.All the manipulation of 
 * the operations of the BinarySearchTree with words or also known the dictionary is made here.
 * 
 * 
 */
public static void runInterfaceOfDictionaryOperations()
{
	DictionaryOperations dictionary=null;
	int option=0;
	Scanner keyboard=new Scanner(System.in);
	String inputString=null;
	
	
	
	do 
	{
		printMenu();
		
		System.out.println("\nGive your option :");
		
		boolean appropriateInput=false;
		while(!appropriateInput)
		{
			try
			{
				option=keyboard.nextInt();
				appropriateInput=true;
			}
			catch(InputMismatchException e)
			{
				keyboard.nextLine();
				System.out.println("Not a correct representation of a number.Trt again.\n");
			}
		}
		
		keyboard.nextLine();
		
		switch (option)
		{
			case 1 :
					  boolean replace=true;
				
					if(dictionary!=null)	// if the dictionary tree havn't been loaded yet
					{
						System.out.println("There is other tree loaded.Do you want to replace it?");
						inputString=keyboard.next();
						keyboard.nextLine();
						
						while(!inputString.equalsIgnoreCase("yes") && !inputString.equalsIgnoreCase("no"))
						{
							System.out.println("Answer only with \"yes\" or \"no\".");
							inputString=keyboard.next();
							keyboard.nextLine();
						}
						
						replace=!inputString.equalsIgnoreCase("no");
					}
					
					
					if(replace) // if the user wants its tree to be replaced
					{
						System.out.println("Give the name of the file where the dictionary is: ");
						inputString=keyboard.nextLine();
						
						try 
						{
							dictionary=new  DictionaryOperations(inputString);
							System.out.println("The file was successfully loaded.\n");
						} 
						catch (NullPointerException | ClassNotFoundException | IOException e) {
							System.out.println("The file couldn't be loaded.\n");
						}
						
						
					}
					
						
				
					 break;
					 
			case 2 :
						if(dictionary!=null)
						{
							System.out.println("Enter the word to find information about :");
							inputString=keyboard.next();
							keyboard.nextLine();
							
							dictionary.printInformationAboutWordRegistration(inputString);
						}
						else System.out.println("Cannot do the operation.No dictionary is loaded.");
						
				 break;
				 
			case 3 : if(dictionary!=null)
					{
				
					appropriateInput=false;
					System.out.println("Give the words seperated by space or tabs and the operator AND and when you finish press enter.");
					while((inputString=keyboard.nextLine()).length()<=0)
					{
						System.out.println("Blank String is not allowed.Try again.\n");
					}
	
					StringTokenizer tokenizer=new StringTokenizer(inputString," \tAND");
					int number=tokenizer.countTokens();
					String[] words=new String[number];
				
					for(int i=0;i<number;i++)
					{
						words[i]=tokenizer.nextToken();
					}
			
					
						dictionary.findCombinationOfWordsOperationAND(words);
					
					}
					else System.out.println("Cannot do the operation.No dictionary is loaded.");
			
					break;
				
				
			case 4 :
				
				
				if(dictionary!=null)
				{
					
					appropriateInput=false;
					System.out.println("Give the words seperated by space or tabs and the operator OR and when you finish press enter.");
					while((inputString=keyboard.nextLine()).length()<=0)
					{
						System.out.println("Blank String is not allowed.Try again.\n");
					}
		
					StringTokenizer tokenizer=new StringTokenizer(inputString," \tOR");
					int number=tokenizer.countTokens();
					String[] words=new String[number];
					
					for(int i=0;i<number;i++)
					{
						words[i]=tokenizer.nextToken();
					}
				
					
						dictionary.findCombinationOfWordsOperationOR(words);
				}
				else System.out.println("Cannot do the operation.No dictionary is loaded.");
				
				 break;
				 
		
				 
			case 5 :
				
					if(dictionary!=null)
					{
						
						// We suppose that the words which are going to be stored in the dictionary ,don't contain
						// asterisks
						System.out.println("The available wild cards for search are :");
						System.out.println("word* : The words that end with word");
						System.out.println("*word : The words that start with word");
						System.out.println("*word* : The words that contain the word.");
						
						System.out.println("Give the wild card : ");
						inputString=keyboard.next();
						keyboard.nextLine();
						while(!isCorrectWildCardOption(inputString))
						{
							System.out.println("The string isn't a correct wildcard.Try again.");
							inputString=keyboard.next();
							keyboard.nextLine();
						}
						
						int length=inputString.length();
						
						if(inputString.charAt(0)=='*' && inputString.charAt(length-1)=='*')
						{
							dictionary.printTheWordRegistrationWhichContain(inputString.substring(1,length-1));
						}
						else if(inputString.charAt(0)=='*')
						{
							dictionary.printTheWordRegistrationWhichEndWith(inputString.substring(1, length));
						}
						else 
						{
							dictionary.printTheWordRegistrationWhichStartFrom(inputString.substring(0, length-1));
							
						}
						
						
					}
					else System.out.println("Cannot do the operation.No dictionary is loaded.");
				
				 break;
				 
			case 6 :
				
				 if(dictionary!=null)
				 {
				
						System.out.println("Enter a phrase-sentence to search in the dictionary :");
						while((inputString=keyboard.nextLine()).length()<=0)
						{
							System.out.println("Give a sentence.A blank sentence is not acceptable.");
						}
					
						dictionary.searchPhrases(inputString);
						
						
				 }
				else System.out.println("Cannot do the operation.No dictionary is loaded.");
				 
				 
						break;
				 
			case 7 :
					if(dictionary!=null)
					{
						System.out.println("Give the name of the file which has the document to add to the dictionary.");
						inputString=keyboard.next();
						keyboard.nextLine();
						
						if(dictionary.addNewDocument(inputString))
						{
							System.out.println("The document "+inputString+" was successfully added.");
						}
						else System.out.println("The document "+inputString+" couldn't  added.");
					
					}
					else System.out.println("Cannot do the operation.No dictionary is loaded.");
				
				 break;
				 
			case 8 :
				
				if(dictionary!=null)
				 {
					System.out.println("Give the name of the file where you want to store the dictionary.");
					inputString=keyboard.next();
					keyboard.nextLine();
					
					if(dictionary.storeDictionaryTreeInFile(inputString))
					{
						System.out.println("The dictionary was saved successfully to file "+inputString+".");
					}
					else System.out.println("The file couldn't be saved to "+inputString+".");
				 }
				else System.out.println("Cannot do the operation.No dictionary is loaded.");
				
				 break;
				 
			case 9 :
					if(dictionary!=null)
					{
						dictionary.runTestAboutSkipPointers();
					 System.out.println();
					}
					else System.out.println("Cannot do the test.No dictionary is loaded.");
				 break;
				 
			case 10 :System.out.println("Exiting the dictionary-Operations mode.");
				 break;
				 
			default :System.out.println("Undefined option.Try again.\n");
					break;
					 
		}
		
		
		System.out.println();
		
	
	}while(option!=10);
	
	
}


public static void main(String[] args)
{
	runInterfaceOfDictionaryOperations();
}

	
}
