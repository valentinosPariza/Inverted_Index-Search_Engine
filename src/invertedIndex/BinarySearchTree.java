package invertedIndex;

import java.io.Serializable;
import java.util.ArrayList;
/**
 *  A class for representing and implementing a Binary search tree.This class uses the interface
 *  {@link BinarySearchTreeInterface} for implementing from an Abstract data type Binary Tree.This 
 *  implementation of the BinarySearchTree doesn't let duplicate elements to exist. The implementation
 *  of this Tree is special because the elements that this tree can store must be descendant of the 
 *  class {@link WordRegistration}
 * 
 * @author valentinos Pariza
 *
 * @param <E> The generic type which is a descendant type of the class WordRegistration
 */
public class BinarySearchTree <E extends WordRegistration  > implements Serializable,BinaryTree<E>
{



	/**
	 * 
	 */
	private static final long serialVersionUID = -7430572878827098827L;
	
	
	
	private BinaryTreeNode<E> root;				// The root of the tree
	
	private int nextAvailableDocumentID=1;     // The next available document id to take
	
	
	/**
	 * This method returns the next available document id for a new document to be inserted.
	 * 
	 * @return The next available document id which is available for a new document
	 */
	public int getNextAvailableDocumentID()
	{
		if(this.nextAvailableDocumentID>=Integer.MAX_VALUE-1)
			this.nextAvailableDocumentID=1;
		
		return nextAvailableDocumentID++;
	}
	
	/**
	 * This method returns the maximum document id that have been given so far.
	 * 
	 * @return The maximum document id that have been given so far
	 */
	public int getMaxDocumentIDExisted()
	{
		return nextAvailableDocumentID;
	}
	
	/**
	 * 
	 * Private class that implements the Binary Tree node.It is serializable tree node
	 * 
	 * @author valentinos pariza
	 *
	 * @param <E>
	 */
	private static class BinaryTreeNode<E extends WordRegistration> implements Serializable
	{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -7646728594460745602L;
		
		
		private E data;							// the data of the Tree
		private BinaryTreeNode<E> left;			// the left child of the tree
		private BinaryTreeNode<E> right;		// the right child of the tree
		
		/**
		 * This constructor creates a BinaryTreeNode object.
		 * 
		 * @param data an object which is of a descendant type of the class WordRegistration
		 */
		public BinaryTreeNode(E data)
		{
			this.data=data;
			this.right=null; 
			this.left=null;
			
		}
		
		/**
		 * Returns a String representation of the object invoking this method
		 * 
		 * @return a String representation of the object invoking the method
		 */
		public String toString()
		{
			return data.toString();
		}
		
		// Method for comparing the nodes for data and references
		/**
		 * This method checks if the object invoking the method and the object passed as an argument are equal.
		 * The result of this check is returned.
		 * 
		 * @return true if the objects are equal otherwise false
		 */
		public boolean equals(Object otherObject)
		{
			if(otherObject==null || otherObject.getClass()!=getClass())
				return false;
			
			BinaryTreeNode<E> aTreeNode=(BinaryTreeNode<E>)otherObject;
			
			// We suppose that two Tree nodes are equal if and only if their data are equal
			// and also their references are equal
			return this.data.equals(aTreeNode.data) && this.right==aTreeNode.right
				    && this.left==aTreeNode.left;
			
		}
		
		
	}
	
	
	
	/**
	 * Constructor for a BinaryTreeNode object;
	 * @return void
	 */
	public BinarySearchTree()
	{
		
		this.root=null;
	}
	
	/**
	 * Inserts the object type of E to the Tree.If the element-object already exists,it merges the data of the two elements.
	 * 
	 * @param data The object to be inserted in the Tree
	 * @return true if the object was inserted successfully otherwise false
	 */
	@Override
	public boolean insert(E data)
	{
		
		if(data==null)		// if there is not an actual data to store
			return false;
		
		 BinaryTreeNode<E> newTreeNode=new BinaryTreeNode<E>(data);
		 	
		 BinaryTreeNode<E> treeNode=this.root;
			
	     boolean inserted=false;
			
		 while(!inserted && treeNode!=null)
		  {
				
			if(treeNode.data.compareTo(data)>0) // It means that the element must be placed at right of this treenode
			{
				if(treeNode.left==null)
				{
					treeNode.left=newTreeNode;
					inserted=true;
				}
				else treeNode=treeNode.left;
			}
			else if(treeNode.data.compareTo(data)<0)	// It means that the element must be placed at right of this treenode 
			{
				if(treeNode.right==null)
				{
					treeNode.right=newTreeNode;
					inserted=true;
				}
				else treeNode=treeNode.right; 
					
		    }
			else
			{
			  return treeNode.data.mergeWordRegistrations(data);	// if the element actually exists,merge their 
			  														// data
			}
				
		  }
			
		 
		 if(!inserted)	// if it hasn't been inserted yet
		  {
			this.root=newTreeNode;
		  }
		
		
		
		return true;
	}
	
	
	/**
	 * This method inserts an element in the tree but instead taking an element it takes just a few data that should be
	 * inserted in the tree or update a registration in the tree(if the element already exists).
	 * 
	 * @param word The word as a String to be inserted in the tree
	 * @param docID The document id where the word appeared
	 * @param position The position in the specific document where the word appeared
	 * @returntrue if the object was inserted successfully otherwise false
	 */
	public boolean insert(String word,int docID,int position)
	{
		
		if(word==null || docID<=0 || docID>=nextAvailableDocumentID ||position<0)
			return false;
		
		 	
		 BinaryTreeNode<E> treeNode=this.root;
			
	     boolean inserted=false;
			
		 while(!inserted && treeNode!=null)
		  {
				
			if(treeNode.data.getWord().compareToIgnoreCase(word)>0)
			{
				if(treeNode.left==null)		// if there is no element with the name as indicated by parameter word	
				{							// and word is lexicographically smaller than the last word of the element examined 
											// insert the element at left of it
					treeNode.left=(BinaryTreeNode<E>) new BinaryTreeNode<WordRegistration>(new WordRegistration(word));
					treeNode.left.data.insertEntryAtDocumentList(docID, position);
					inserted=true;
				}
				else treeNode=treeNode.left;					// else move to the left of the element for smaller elements
			}
			else if(treeNode.data.getWord().compareToIgnoreCase(word)<0)  // if there is no element with the name as indicated by parameter word	
			{															   // and word is lexicographically greater than the last word of the element examined
																			// insert the element at right of it
				if(treeNode.right==null)
				{
					treeNode.right=(BinaryTreeNode<E>) new BinaryTreeNode<WordRegistration>(new WordRegistration(word));
					treeNode.right.data.insertEntryAtDocumentList(docID, position);
					inserted=true;
				}
				else treeNode=treeNode.right; 					// else move to the right of the element for greater elements
					
		    }
			else
			{
				
			  return 	treeNode.data.insertEntryAtDocumentList(docID, position);		// else insert the data to the 
			}																			// the existed registration
				
		  }
			
		 if(!inserted)					// if it hasn't been inserted yet,it means that the tree is empty.So insert it at the root
		  {
			this.root=(BinaryTreeNode<E>) new BinaryTreeNode<WordRegistration>(new WordRegistration(word));
			this.root.data.insertEntryAtDocumentList(docID, position);

		  }
		
		
		
		return true;
	}
	
	
	/**
	 * Finds the smallest element of the tree and returns it.
	 * 
	 * @return the object with the least value in the tree with criteria of comparison the method compareTo of it
	 */
	public E findMin()
	{
		if(this.root==null)
			return null;
		
		else return this.findMin(this.root);
	}
	
	
	/**
	 * Finds the greatest element of the tree and returns it.
	 * 
	 *  @return the object with the least value in the tree with criteria of comparison the method compareTo of it
	 */
	public E findMax()
	{
		if(this.root==null)
			return null;
		
		else return this.findMax(this.root);
	}
	

	
	/**
	 * Finds the smallest element of the subTree with root the node given as a parameter and returns it.
	 * 
	 *  @param node The root of a subtree which we want to find the smallest element in it.
	 *  @return The object with the least value in the tree with criteria of comparison the method compareTo of it.
	 */
	private  E  findMin(BinaryTreeNode<E> node)
	{
		if(node==null)
			return null;
		
		while(node.left!=null)
			node=node.left;
		
		return node.data;
		
	}
	
	
	
	/**
	 * Finds the greatest element of the subTree with root the node given as a parameter and returns it.
	 * 
	 *  @param node The root of a subtree which we want to find the greatest element in it.
	 *  @return The object with the bigest value in the tree with criteria of comparison the method compareTo of it.
	 */
	private  E  findMax(BinaryTreeNode<E> node)
	{
		if(node==null)
			return null;
		
		while(node.right!=null)
			node=node.right;
		
		return node.data;
		
	}
	
	
	/**
	 * Deletes the smallest element in this subtree with root the node type of #BinaryTreeNode which is given as a parameter.
	 * 
	 * @param node the root of the subtree from we want to delete the smallest element.
	 * @return The subtree after of the deletion of the element from the tree
	 */
	private BinaryTreeNode<E> deleteMin(BinaryTreeNode<E> node)
	{
		if(node==null)
			return null;
		
		BinaryTreeNode<E> start=node;       // the subroot of the subtree indicated at the beginning by node reference
		BinaryTreeNode<E> previous=node;	// the previous Tree Node that was examined
		
		while(node.left!=null)
		{
			previous=node;
			node=node.left;
		}
		
		// if the less data are in the subroot then return the right subtree of the first subroot
		if(start==node)
			return node.right;
		else
		{									
			previous.left=node.right;		// set the left reference which was on the node with the less data 
		}									// to the node right of the node with the less data (if exists) 
		
		return start;						// return the subtree which the node with the less data deleted
		
	}
	
	
	/**
	 * Deletes the greatest element in this subtree with root the node type of #BinaryTreeNode which is given as a parameter.
	 * 
	 * @param node the root of the subtree from we want to delete the greatest element.
	 * @return The subtree after of the deletion of the greatest element from the tree
	 */
	private BinaryTreeNode<E> deleteMax(BinaryTreeNode<E> node)
	{
		if(node==null)
			return null;
		
		
		BinaryTreeNode<E> start=node;       // the subroot of the subtree indicated at the beginning by node reference
		BinaryTreeNode<E> previous=node;	// the previous Tree Node that was examined
		
		while(node.right!=null)
		{
			previous=node;
			node=node.right;
		}
		
		// if the max data are in the subroot then return the left subtree of the first subroot
		if(start==node)
			return node.left;
		else
		{									
			previous.right=node.left;		// set the right reference which was on the node with the max data 
		}									// to the node left of the node with the max data (if exists) 
		
		return start;						// return the subtree which the node with the max data deleted
		
	}
	
	/**
	 * Deletes the registration which corresponds to the element given as a parameter 
	 * 
	 * @param data The element that we want to delete from he tree
	 * @return void
	 */
	@Override
	public void delete(E data)
	{
		if(data==null || this.root==null)
			return;
		
		this.delete(data.getWord());
		
	}
	
	/**
	 * Deletes the registration which has an attribute of word that is equal to the String given as a parameter
	 * 
	 * @param wordOfWordRegister The String of a word that the registration we want to delete have.
	 *  
	 * @return void
	 */
	public void delete(String wordOfWordRegister)
	{
	
		if(wordOfWordRegister==null || this.root==null)
			return;
		
		// if the element to compare is equal with the data of the root of the tree
		if(wordOfWordRegister.compareToIgnoreCase(this.root.data.getWord())==0)
		{
			if(this.root.left==null)
				this.root=this.root.right;
			
			else if(this.root.right==null)
				this.root=this.root.left;
			
			else
			{
				this.root.data=this.findMin(this.root.right);
				this.root.left=this.deleteMin(this.root.right);	
			}
		}
		
		
		BinaryTreeNode<E> previous=this.root;
		BinaryTreeNode<E> node=previous;
		boolean wentLeft=false;
		
		int compareResult=0;
		
		while(node!=null)
		{
			
			compareResult=node.data.getWord().compareToIgnoreCase(wordOfWordRegister);
			
			if(compareResult>0)
			{
				wentLeft=true;
				
				previous=node;
				node=node.left;
			}
			else if(compareResult<0)
			{
				wentLeft=false;
				
				previous=node;
				node=node.right;
			}
			else  break;
			
		}
		
		// examine the situation where the element which we try to delete doesn't exist
		if(node==null)
			return;
		
		// check the situation where the node which we want to delete(its data) has null left reference or both right and left 
		if(node.left==null)
		{
			if(wentLeft)
			{
				previous.left=node.right;
			}
			else 
			{
				previous.right=node.right;
			}
		}
		else if(node.right==null) // check the situation where the node which we want to delete(its data) has null left reference 
		{
			if(wentLeft)
			{
				previous.left=node.left;
			}
			else 
			{
				previous.right=node.left;
			}
		}
		else  // check the situation where both right and left children of the node are not null
		{
				node.data=this.findMin(node.right);
				node.left=this.deleteMin(node.right);	
		}
		
	}

	
	/**
	 * Returns the size of the tree,which is the number os the elements that it holds 
	 * 
	 * @return the size of the Tree - The number of the registrations that it holds
	 */
	@Override
	public int getSize() {
		
		if (this.root==null)
			return 0;
		
		
		return this.getSize(this.root);
	}

	
	/**
	 * The size of the subtree with sub-root as indicated by the parameter node.Which means the number of the 
	 * elements on this subtree.Also known as the grade of the subtree.
	 * 
	 * @param node The root of the subtree that we want to calculate the size of it
	 * @return The size of the tree
	 */
	private int getSize(BinaryTreeNode<E> node)
	{
		if(node==null)
			return 0;
		
		return 1+getSize(node.left)+getSize( node.right);
		
	}
	
	
	/**
	 * The height of a Tree(The height of the root of the Tree).
	 * 
	 * @return Returns the height of the tree 
	 * 
	 */
	@Override
	public int getHeight() {
		
		if(this.root==null)		// Suppose that a tree without any nodes has height -1
			return -1;			// Suppose also that a tree with one node has height 0
		
		
		return	this.getHeight(this.root);
	}

	
	/**
	 * The height of a subtree with root , a node that is given as a parameter.
	 * 
	 * @param node The root of a subtree 
	 * @return The height of the subtree with root the node given as a parameter.
	 */
	private int getHeight(BinaryTreeNode<E> node)
	{
		if(node==null) return -1; // indicates that there is not a node
		
		int heightFromLeft=getHeight(node.left);
		
		int heightFromRight=getHeight( node.right);
		
		return 1 + ( (heightFromLeft <= heightFromRight)? heightFromRight : heightFromLeft );
		// The height of a node is the maximum height of its child plus one.We suppose that at the end the -1 one will
		// ensure that we calculate the correct height of the tree
	}
	
	
	/**
	 * Find a {@link WordRegistration} object from the word(String) given as a parameter, that the tree holds.
	 * 
	 * @param word
	 * @return the object type of {@link WordRegistration} that holds inforamtion about the word as indicated by parameter word
	 */
	public E find(String word) 
	{
		
	  BinaryTreeNode<E> node=this.root;
	  
	  int compareResult=0;
	  
	  while(node!=null)
	  {
		  compareResult=node.data.getWord().compareToIgnoreCase(word);
		  
		  if(compareResult<0)
		  {
			  node=node.right;
		  }
		  else if(compareResult>0)
		  {
			  node=node.left;
		  }
		  else return node.data;
		  
	  }
		
		return null;  // indicating that the word registration that holds information about the word wasn't found
	}
	
	
	/**
	 * Finds all the word registrations which their words start with the prefix String indicated by parameter.
	 * 
	 * @param prefix The string that the words we search for must start with
	 * @return An array with all the registrations which represent String which start with the prefix indicated 
	 * 			by parameter
	 */
	public E[] findWordRegistersWhichWordsStartWith(String prefix)
	{
		if(prefix==null)
			return null;
		
		ArrayList<E> list=new ArrayList<E>();
		
		BinaryTreeNode<E> node=this.root;
		int prefixLength=prefix.length();
		
		findWordRegistersWhichWordsStartWith(prefix, list, prefixLength, node);
		
		return (E[]) list.toArray(new WordRegistration[list.size()]);
	}
	
	
	
	/**
	 * Find all the word registrations which their words start with the prefix String indicated by parameter.
	 * 
	 * @param prefix The string that the words we search for must start with
	 * @param list an #ArrayList<E> that will store inside all the appears of the registrations which start with the prefix
	 * @param prefixLength The length of the prefix 
	 * @param node The node from we start the earch for all the registrations which start with the prefix
	 * 
	 * @return void
	 */
	private void findWordRegistersWhichWordsStartWith(String prefix,ArrayList<E> list,int prefixLength,BinaryTreeNode<E> node)
	{
		
		if(node==null)
			return ;
		
		  int compareResult=0;
		  int len=node.data.getWord().length();
		  int min=(len<=prefixLength)? len : prefixLength;
		  
		 // compare the prefixes of the two word Strings compared
		compareResult=node.data.getWord().substring(0,min).compareToIgnoreCase(prefix);
			  
		if(compareResult<0)
		{
			findWordRegistersWhichWordsStartWith(prefix, list, prefixLength, node.right);
		}
		else if(compareResult>0)
		{
			  findWordRegistersWhichWordsStartWith(prefix, list, prefixLength, node.left);
		}
		else
		{
			// Expand in all directions in order to find all the Strings in WordRegistrations that start with the same subString
			list.add( node.data);
			findWordRegistersWhichWordsStartWith(prefix, list, prefixLength, node.right);
			findWordRegistersWhichWordsStartWith(prefix, list, prefixLength, node.left);
		}	  
		
	}
	
	
	
	
	/**
	 * Finds all the word registrations which their words end with the suffix String indicated by parameter.
	 * 
	 * @param suffic The string that the words we search for must end with
	 * @return An array with all the registrations which represent Strings which end with the suffix indicated 
	 * 			by parameter
	 */
	public E[] findWordRegistersWhichWordsEndWith(String suffix)
	{
		if(suffix==null)
			return null;
		
		
		ArrayList<E> list=new ArrayList<E>();
		
		BinaryTreeNode<E> node=this.root;
		int postfixLength=suffix.length();
		
		findWordRegistersWhichWordsEndWith(suffix, list, postfixLength, node);
		
		return (E[]) list.toArray(new WordRegistration[list.size()]);
	}
	
	
	
	/**
	 * Finds all the word registrations which their words end with the suffix String indicated by parameter.
	 * 
	 * @param suffix The string that the words we search for must start with
	 * @param list an #ArrayList<E> that will store inside all the appears of the registrations which end with the suffix
	 * @param suffixLength The length of the prefix 
	 * @param node The node from we start the search for all the registrations which end with the prefix
	 * 
	 * @return void
	 */
	private void findWordRegistersWhichWordsEndWith(String suffix,ArrayList<E> list,int suffixLength,BinaryTreeNode<E> node)
	{
		
		if(node==null)
			return ;
		
		  int compareResult=0;
		  
		  
		 // compare the postfixes of the two word Strings compared
		String wordOfTheWordRegistration=node.data.getWord();
		int len=wordOfTheWordRegistration.length();
	
		
		int min=(len<=suffixLength)? len : suffixLength;
		
			  
		if(wordOfTheWordRegistration.substring(len-min).compareToIgnoreCase(suffix)==0)
			list.add( node.data);
		
		// Expand in all directions in order to find all the Strings in WordRegistrations that end with the same subString
		findWordRegistersWhichWordsEndWith(suffix, list, suffixLength, node.right);
		findWordRegistersWhichWordsEndWith(suffix, list, suffixLength, node.left);
			  
		
	}
	
	
	

	/**
	 * Finds all the word registrations which their words contain the String indicated by parameter aString.
	 * 
	 * @param aString The string that the words we search for must contain
	 * @return An array with all the registrations which represent Strings which contain with the String indicated 
	 * 			by parameter aString
	 */
	public E[] findWordRegistersWhichWordsContain(String aString)
	{
		if(aString==null)
			return null;
		
		ArrayList<E> list=new ArrayList<E>();
		
		BinaryTreeNode<E> node=this.root;
		int stringLength=aString.length();
		
		findWordRegistersWhichWordsContain(aString.toLowerCase(), list, stringLength, node);
		
		return  (E[]) list.toArray(new WordRegistration[list.size()]);
	}
	
	/**
	 * Finds all the word registrations which their words contain the String indicated by parameter aString.
	 * 
	 * @param aString The string that the words we search for, must \contain it.
	 * @param list an #ArrayList<E> that will store inside all the appears of the registrations which contain the String
	 * @param stringLength The length of the aString 
	 * @param node The node from we start the search for all the registrations which contain the String
	 * 
	 * @return void
	 */
	private void findWordRegistersWhichWordsContain(String aString,ArrayList<E> list,int stringLength,BinaryTreeNode<E> node)
	{
		
		if(node==null)
			return ;
		
		// check if the String examined is contained in the String word of the WordRegistration examined
		if(node.data.getWord().toLowerCase().indexOf(aString)>=0)
			list.add(node.data);
		
		
			// Expand in all directions in order to find all the Strings in WordRegistrations that contain the String
			
		findWordRegistersWhichWordsContain(aString, list, stringLength, node.right);
		findWordRegistersWhichWordsContain(aString, list, stringLength, node.left);
		 
	}
	
	
	/**
	 * This method applies the skip pointers to all the registrations.
	 * 
	 * @param treeNode The node of the subtree from we start applying the skipPointers
	 * 
	 * @return void
	 */
	private void applySkipPointers(BinaryTreeNode<E> treeNode)
	{
		if(treeNode==null)
			return ;
		
		treeNode.data.applySkipPointers();
		
		applySkipPointers( treeNode.left);
		applySkipPointers( treeNode.right);
		
	}
	
	/**
	 * This method applies the skip pointers to all the nodes in the tree.
	 * 
	 * @return void
	 */
	public void applySkipPointers()
	{
		applySkipPointers(this.root);
	}
	
	
	/**
	 * This method applies the number of the skip pointers to every WordRegistration object in the tree
	 * 
	 * @param option The option that chooses the technique of calculating the number of skipPointers to use
	 */
	public void setTheNumberOfSkipPointers(int option)
	{
		this.setTheNumberOfSkipPointers(this.root, option);
	}
	
	/**
	 * This method sets the number of the skipPointers as indicated by the integer number option which represents
	 * the technique of how calculating the number of skip pointers for the data about document ids inside the 
	 * WordRegistration objects.It starts setting the number of skipPointers for each object starting from the BinaryTreeNode
	 * node in the tree and applies the same method for all its descendants nodes.
	 * 
	 * @param treeNode the node of the tree from where it starts to set the number of skipPointers
	 * @param option The option that chooses the technique of calculating the number of skipPointers to use
	 * 
	 */
	private void setTheNumberOfSkipPointers(BinaryTreeNode<E> treeNode,int option)
	{
		if(treeNode==null)
			return ;
		
		treeNode.data.setTheNumberOfSkipPointersByOptions(option);
		
		setTheNumberOfSkipPointers( treeNode.left,option);
		setTheNumberOfSkipPointers( treeNode.right,option);
	}
	
	
	/**
	 * This method deletes the skip pointers on all the objects of type WordRegistration in the tree.It sets the skip-pointers
	 * to null so they cannot  be used.
	 * @return void
	 */
	public void deleteSkipPointers()
	{
		this.deleteSkipPointers(this.root);
	}
	
	
	/**
	 * This method deletes the skip pointers on all the objects(of type WordRegistration) inside the nodes that are descendants
	 * of the node of type treeNode that is given as an argument, in the tree.It sets the skip-pointers to null so 
	 * they cannot  be used.
	 * 
	 * @param treeNode The node from where the program starts to delete the skip pointers in the tree
	 * @return void
	 */
	private void deleteSkipPointers(BinaryTreeNode<E> treeNode)
	{
		if(treeNode==null)
			return ;
		
		treeNode.data.deleteSkipPointers();
		
		deleteSkipPointers( treeNode.left);
		deleteSkipPointers( treeNode.right);
	}
	
	
	/**
	 * This method finds a random WordRegistrationobject in the tree and returns it.
	 * 
	 * @return a reference to an object of type WordRegistration
	 */
	 public WordRegistration getRandomWordRegistration() {
		 
			int pos=(int)(Math.random()*this.getSize());
			
			Index index=new Index();
			index.position=pos;
			return getElement(this.root,index);
		}
	
	 // A class that is going to be used for representing an index
	private static class Index{
		int position=0;
	}
	
	
	/**
	 * This method returns the element at the index as indicated by the argument.The index is based at inorder
	 * traversal.This means that the index represents the data of the node at the index-th node that was traversaled
	 * during the inorder traversal.
	 * 
	 * @param treeNode the node from where it starts the inorder traversal search
	 * @param index an object that holds the index in the tree
	 * @return an object of type WordRegistration that corresponds to index that was asked
	 */
	private WordRegistration getElement(BinaryTreeNode<E> treeNode,Index index)
	{
		if(treeNode==null || index==null || index.position<0)
			return null;
		
		
		
		WordRegistration o1=getElement(treeNode.left,index);
		
		if(index.position<=0)
			return treeNode.data;
		
		index.position--;
		
		return (o1!=null)? o1 : getElement(treeNode.right,index);
		
		
	}
	
	
}
