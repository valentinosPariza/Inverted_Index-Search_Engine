package invertedIndex;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class represents an object of a word registration which is used to hold information about a word.It
 * holds the String of the word ,the document ids where can be found and the appearances of the word in every
 * document.
 * 
 * @author valentinos pariza
 *
 */
public class WordRegistration implements Comparable<WordRegistration>,Serializable{

	/**
	 * Serializable id of this class
	 */
	private static final long serialVersionUID = -4618474130276492523L;

	private String word;			// The word as a STring which an object of this class represents
	
	private int appearances;		// The number of all the appearances of this word in the files
	
	private LinkedListWithSkipPointers<WordAppearsInDoc> appearDocs;	// A linked list which inside has the files where
																		// the word was found and at what position exactly	

	
	/**
	 * 
	 * An inner class that represents the document which a word can be found inside and the positions in the
	 * specific document that the word can be found.
	 * 
	 * @author valentinos pariza
	 *
	 */
	private static class WordAppearsInDoc implements Comparable<WordAppearsInDoc>,Serializable
	{
		
	  /**
		 * Serializable id of this class
		 */
		private static final long serialVersionUID = -4740391858955328886L;

		private int documentID;					// the document id which the String was found in
	  
	   private ArrayList<Integer> positions;	   // he positions in the document where the word was found
		
	  
	  /**
	   * Checks if this object about a document contains the occurrence of the word at the position as indicated by parameter.
	   * 
	   * @param position The position that we want to know if this document have
	   * @return true if this object contains the occurrence of the word at position as indicated by parameter else false
	   */
	  public boolean contains(int position)
	  {
		  return findIndex(position)>=0;
	  }
	  
	  
	  /**
	   * Returns the number of positions of a word in this document object 
	   * 
	   * @return the number of occurences of the word in this document file
	   */
	  public int size()
	  {
		  return this.positions.size();
	  }
	  
	  
	  /**
	   * Merges two objects type of WordAppearsInDoc into one and returns the numbers of new elements
	   * that have been added because of the merge.
	   * 
	   * @param appearInfo an object type of #WordAppearsInDoc that is going to be merged
	   * @return the number of new elements added to the second object that will be the base of the merge
	   */
	  public int merge(WordAppearsInDoc appearInfo)
	  {
		  if(appearInfo==null)
			  return 0;
		 
		  if(appearInfo.documentID!=this.documentID)	// case of different document ids->cannot operate the merge
			  return 0;
		  
		  int numberOfAdds=0;
		  for(int element : appearInfo.positions)
		  {
			  if(this.add(element))
				  numberOfAdds++;
		  }
		  
		  return numberOfAdds;
	  }
	  
	  
	  
	  /**
	   * Constructor for creating an object of  this class with an id for the document as indicated by parameter
	   * @param id the documentID of that the object created will represent
	   */
	  public WordAppearsInDoc(int id) throws NegativeNumberException
	  {
		  if(id<0 )
			  throw new NegativeNumberException("Negative document id.");
		  
		  
		  this.documentID=id;					    // the document id that the object represents
		  this.positions=new ArrayList<Integer>();	// the list of positions
	  }

	  
	  /**
	   * 
	   * @param id the documentID of that the object created will represent
	   * @param position One occurrence position of a word in the document with id as indicated by parameter
	   */
	  public WordAppearsInDoc(int id,int position)
	  {
		  this(id);		
		  
		  if(position<0)
		  throw new NegativeNumberException("Negative position of a word in a document.");
		  
		  this.positions.add(position);		// add the position to the list with the occurrence positions
	  }
	  
	  
	  /**
	   * Finds the index in the list of the position of a word in a file
	   * 
	   * @param position The position in the document that this object represents which we want to find
	   * @return The index in the list with the occurrence positions where this position as a number exists or
	   * 		 -1 if doesn't exist
	   */
	  public int findIndex(int position)
	  {
		  
		  if(this.positions==null || position<0)
			  return -1;
		  
		  int l=0,r=this.positions.size()-1;
		  
		  int mid=0;
		  
		  // binary search for  finding a position.The list is sorted in ascending order
		  while(l<=r)
		  {
			   mid=l+(r-l)/2;
			  
			  if(this.positions.get(mid)>position)
				r=mid-1;
			  
			  else if(this.positions.get(mid)<position)
				  l=mid+1;
			  
			  else return mid;
			  
		  }
		  
		  // indicate that the element was not found
		  return -1;
		  
	  }
	  
	  
	  /**
	   * 
	   * @param position The position of a word in this document object that we want to delete 
	   * @return true if it was deleted or false otherwise
	   */
	  public boolean delete(int position)
	  {
		  
		  if(this.positions==null || position<0)
			  return false;
		  
		  int index=findIndex(position);	// find the index of the element
		  
		  if(index<0)				// if it wasn't found return false
			  return false;
		  
		  this.positions.remove(index);	// remove from list this element
		  
		  return true;
		  
	  }
	  
	  
	  
	  /**
	   * Adds an occurrence position of a word in this object-document
	   * 
	   * @param position The position of a word in the document that this object represents 
	   * @return true if this position was added or false otherwise
	   */
	  public boolean add(int position)
	  {
		  
		  if(position<0)
			    return false;
		  
		  if(this.positions==null)			// if the list haven't been created yet
		  {
			  this.positions=new ArrayList<Integer>();
			  this.positions.add(position);
			  return true;
		  }
		  
		  
		  int size=this.positions.size();
		  int l=0,r=size-1;
		  int mid=0;
		  
		  // search for finding the most appropriate index to insert the element
		  while(l<r)
		  {
			  mid=l+(r-l)/2;
			  
			  if(this.positions.get(mid)>position)
				r=mid-1;
			  
			  else if(this.positions.get(mid)<position)
				  l=mid+1;
			  
			// already has been placed inside (before)
			  else return false;
		  }
		  
		  // stop at l==r
		  // use r as indicator
		  
		  if(this.positions.get(l)>position)
		  {
			  for(r=l-1;r>=0;r--)
			  {
				  if(this.positions.get(r)<position)
				  {
					  // if it is bigger than the element then it has to be added right of it
					  
					  break;
				  }
			  }
			   r++;	// in case when the position is the smallest and has to be inserted at the beginning
		  }
		  else
		  {
			  for(r=l+1;r<size;r++)
			  {
				  if(this.positions.get(r)>position)
				  {
					  break;
				  }
			  }
		  }
		  
		  this.positions.add(r, position);
		  
		  return true;
			  
	  }
	  
	  
	 /**
	  * Returns the document id that this object represents
	  * 
	  * @return The document id that this object represents
	  */
	  public int getDocumentID()
	  {
		  return this.documentID;
	  }
	  
	  
	  /**
	   * This method returns an array of integers which is a corresponding array to the list with all the occurrence-positions
	   * 		of a word in the document that this object represents
	   * 
	   * @return an array of integers which is a corresponding array to the list with all the occurrence-positions
	   * 		of a word in the document that this object represents
	   */
	  public int[] getWordPositions()
	  {
		  
		  if(this.positions!=null)	// if the list doesn't exist
		  {
			  int size=this.positions.size();
			  
			  if(size==0)			// if the list doesn't have any positions inside
				  	return null;
			  
			  int[] thePositions=new int[size];
			  
			  
			  int i=0;
			  for(int a : positions)
			  {
				  thePositions[i]=a;
				  i++;
			  }
			  
			  return thePositions;
			  
		  }
	  
		  return null;
	  }
	  
	  /**
	   * Creates a String representation of the object of this class which invokes this and returns it
	   * 
	   * @return a String representation of this object
	   * 
	   */
	  public String toString()
	  {
		  
		  StringBuilder builder=new StringBuilder("Documnet ID : "+this.documentID+"\nThe positions of the words : \n");
		  
		  for(int position : positions)
			 builder.append(position+" / ");
		  
		  return builder.toString();
		  
	  }
	  
	  
	  /**
	   * This method checks whether the object invoking this method is equal to the object given
	   * as an argument to the method and return the result.
	   * 
	   * 
	   * @param otherObject an object which is checked for equality with the object invoking the method
	   * @return true if the two object are equal or false if not
	   */
	  public boolean equals(Object otherObject)
	  {
		  
		  if(otherObject==null || otherObject.getClass()!=getClass())
			  return false;
		  
		  WordAppearsInDoc docObject=(WordAppearsInDoc)otherObject;
		  
		 return this.documentID==docObject.documentID && this.positions.equals(docObject.positions);
		  
	  }


	  /**
	   * Implementation of the {@link Comparable#compareTo(Object)} method from interface {@link Comparable}.
	   * This method compares the object invoking the method with the object given as an argument and returns
	   * negative number if the object invoking the method is less than the argument-object,positive number 
	   * if it is greater and 0 if they are equal.The comparison is made comparing their document ids.Actually it
	   * returns the difference of their documentIDs (documentID(invoking) - documentID(argument))
	   * 
	   * @ returns 0 if they are equal,-1 if the object invoking is less than the argument-object, or 1
	   * if it is greater
	   */
	  @Override
		public int compareTo(WordAppearsInDoc obj) {
		
		if(obj==null)
			return 1;
		
		return this.documentID-obj.documentID;
	    }
	  
	}
	
	
	/**
	 * This class represents a special version of LinkedList which is used for storing the objects type of
	 * {@link WordAppearsInDoc} or of any descendants Classes of it, inside.Also this linked list uses a technique
	 * of some pointers called skip-Pointers,that instead of referencing at the next node of the list,they 
	 * referencing-pointing at a node k steps forward(if that distance is actually can point to a node and the end of
	 *  the list haven't come ).
	 * 
	 * @author valentinos pariza
	 *
	 * @param <E> type of WordAppearsInDoc or of any descendant Class of it
	 */
   private static class LinkedListWithSkipPointers<E extends WordAppearsInDoc> implements Serializable
	{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -3911503576729338326L;


		private  int numberOfSkipPointers=1;			// the number of skip pointers that this list have
																
		
		Node<E>  head;				// the head of the list
		int size;					// the size of this list
		
		
		/**
		 * This class represents the nodes in the linked list of type {@link LinkedListWithSkipPointers}
		 * 
		 * @param <E> type of WordAppearsInDoc or of any descendant Class of it 
		 * @author valentinos pariza
		 * 
		 */
		private static class Node<E extends WordAppearsInDoc> implements Serializable
		{
			/**
			 * 
			 */
			private static final long serialVersionUID = -8197651089790406238L;
			
			Node<E> next;				// The next node that this node points
			Node<E> skipPointer;		// The node that skip pointer points to 
			E data;						// The data of this node
			
			
			public Node(E data)
			{
				this.data=data;
				this.next=null;
				this.skipPointer=null;
			}
			
		}

		
		/**
		 * Returns the element in this linkedlist that exists in the position as indicated by the argument
		 * given or null if no element exists at the given position.The indexes start from zero.
		 * 
		 * @param index The index of an element in the list
		 * @return the object at position index of this list or null if nothing exists at that position
		 */
		public E get(int index)
		{
			if(index<0)
				return null;
			
			Node<E> node=this.head;
			
			int i=0;			// Suppose that the list begins from zero index
			while(node!=null)
			{
				if(i==index)
					return node.data;
				
				node=node.next;
				i++;
			}
			
			return null;
		}
		
		
		/**
		 * Constructor of an object of this class {@link LinkedListWithSkipPointers}
		 * 
		 */
		public LinkedListWithSkipPointers()
		{
			this.head=null;
			this.size=0;
		}
		
		
		/**
		 * Returns the occurrence positions of an element of this list which has document id as indicated by
		 * the parameter 
		 * 
		 * @param docID The document ID of an object type of {@link WordAppearsInDoc} 
		 * @return an array of integers which inside has all the occurrence positions of a word in a specified 
		 * 			document ID
		 */
		public int[] getPositions(int docID)
		{
			
			if(docID<0)
				return null;
			
			Node<E> node=this.head;
			
			while(node!=null && node.data.getDocumentID()<=docID)
			{
				
				if(node.data.getDocumentID()==docID)
					return node.data.getWordPositions();
				
				if(node.skipPointer!=null && node.skipPointer.data.getDocumentID()<=docID)
					node=node.skipPointer;
				else
					node=node.next;
				
			}
			
			return null;
			
		}
		
		
		
		/**
		 * Checks if there is an object type of E (which extends {@link WordAppearsInDoc}) 
		 * with docID attribute as indicated by the parameter and (if it is found)  ,it checks 
		 * if an entry of a position of the word in that object found, is equal to the parameter
		 *  position.
		 * 
		 * @param docID The document id that an object type of {@link WordAppearsInDoc} represents
		 * @param position The position that a word exists in a document 
		 * @return true if it was found or false otherwise
		 * 
		 */
		public boolean contains(int docID,int position)
		{
			
			if(this.head==null)
				return false;
			
			Node<E> node=this.head;
			
			while(node!=null && node.data.getDocumentID()<=docID)
			{
				if(node.data.getDocumentID()==docID)
				{
					return node.data.contains(position);
				}
				
				node=node.next;
				
			}
			
			return false;
			
		}
		
		
		/**
		 * This method inserts a document id of a document and the position of a word in that document 
		 * that was found into the linked list.If the document exists(the document id exists) it merges
		 * the list with the positions of a word in the document object(specified by parameter docID) with the
		 * new position given. 
		 * 
		 * @param docID The document id of a document
		 * @param position The position of a word in a document with id as indicated by parameter docID
		 * @return true if it was inserted or false otherwise
		 */
		public boolean insert(int docID,int position)
		{
			
			
				if(docID<0 || position<0)
					return false;
				
				if(this.head==null)
				{
					this.head=new Node<E>((E)new WordAppearsInDoc(docID,position));
					this.size++;
					return true;
				}
				
				
				if(this.head.data.getDocumentID()>docID)
				{
					Node<E> newNode=new Node<E>((E)new WordAppearsInDoc(docID,position));
					
					newNode.next=this.head;
					this.head=newNode;
					
					this.size++;
					
					return true;
				}
				else if(this.head.data.getDocumentID()==docID)	// just place the new Position of the word found in document
				{
					
					return this.head.data.add(position);
					
				}
				
				
				Node<E> node=this.head;
				
				while(node.next!=null && node.next.data.getDocumentID()<docID)
				{
						node=node.next;
				}
				
				
				
				
				// if it doesn't exist place it in the correct position where it has stopped
				if( node.next==null || node.next.data.getDocumentID()>docID)
					{
					
					 	Node<E> newNode=new Node<E>((E)new WordAppearsInDoc(docID,position));
						newNode.next=node.next;
						node.next=newNode;
						
						this.size++;
						return true;
					}
				else 
				{
					return node.next.data.add(position);
				}
				
		}
		
		
		
		/**
		 * Inserts an object type of E at the List.If this object already exists it merges its attributes 
		 * with the one that exists in the List, in order the final object to be stored in the list to
		 *  be an union of the two merged.
		 * 
		 * @param data An object of type WordAppearsInDoc or any descendant class of it
		 * @return true if the object was inserted or false otherwise
		 */
		public boolean insert(E data)
		{
			if(data==null)
				return false;
			
			
			Node<E> newNode=new Node<E>(data);
			
			if(this.head==null)
			{
				this.head=newNode;
				this.size++;
				return true;
			}
			
			if(this.head.data.getDocumentID()>data.getDocumentID())		// the case where the element is placed 
			{															// at first position
				newNode.next=this.head;
				this.head=newNode;
				this.size++;
				return true;
			}
			else if(this.head.data.getDocumentID()==data.getDocumentID())
			{
				return this.head.data.merge(data)>0;	
			}											
			
			
			Node<E> node=this.head;
			
			while(node.next!=null && node.next.data.getDocumentID()<data.getDocumentID())
			{
				node=node.next;
			}
			
			
			// ignore it if already exists
			if(node.next==null || node.next.data.getDocumentID()>data.getDocumentID())
				{
					newNode.next=node.next;
					node.next=newNode;
					this.size++;
					
					return true;
				}
			else return node.next.data.merge(data)>0;
			
		}
		
		
		/**
		 * Delete the object type of {@link WordAppearsInDoc} which has id as indicated by the parameter.
		 * If this object doesn't exist returns 0 (indicating that the document id haven't been deleted)
		 *  and doesn't change or delete anything,or the document has been deleted but nothing
		 *  positions were deleted else returns a positive number indicating the number of positions that
		 *   were deleted.
		 * 
		 * @param docID The document id of an object type of {@link WordAppearsInDoc}
		 * @return 0 if the object type of {@link WordAppearsInDoc} hasn't been deleted from the list
		 * 			or it has been deleted but no positions of it were deleted or any positive number 
		 * 			indicating the numbers of positions deleted + the object type of {@link WordAppearsInDoc} deleted. 
		 */
		public int delete(int docID)
		{
			if(docID<0 || this.head==null)
				return 0;
			
			int deletedNumberOfAppears=0;
			
			if(this.head.data.getDocumentID()==docID)
			{
				deletedNumberOfAppears=this.head.data.size();
				this.head=this.head.next;
				this.size--;
				return deletedNumberOfAppears;
			}
			
			Node<E> node=this.head;
			
			
			while(node.next!=null && node.next.data.getDocumentID()<docID)
			{
				node=node.next;
			}
			
			if(node.next!=null && node.next.data.getDocumentID()==docID)
			{
				deletedNumberOfAppears=node.next.data.size();
				node.next=node.next.next;
				this.size--;
			}
			
			return deletedNumberOfAppears;
		}
		

		
		/**
		 * This method applies the skip pointers to the nodes of the linked list object, type of this class.
		 * 
		 * @return void
		 * 
		 */
		public void applySkipPointers()
		{
			
			if(this.head==null || numberOfSkipPointers<=0)
				return ;
			
			int distanceSkip=this.size/numberOfSkipPointers;
			// the distance skip is the number of the elements in the list divided by the number of skipPointers
			
			// Useless distance skips
			if(distanceSkip<=1)
				return ;
			
			int i=1;
			Node<E> previousSkip=this.head;
			Node<E> currentSkip=this.head;
			
			
			// place the current node to the appropriate skip position
			while(currentSkip!=null && i<=distanceSkip)
			{
				
				currentSkip=currentSkip.next;
				
				i++;
			}
			
			if(currentSkip==null)	// cannot set skipPointers.Few elements in the list
				return ;
			
			
			while(currentSkip!=null )
			{
				previousSkip.skipPointer=currentSkip;		// every node in the list
				previousSkip=previousSkip.next;				// should point at an element in the correct
				currentSkip=currentSkip.next;				// distance skip
			}
			
			
			while(previousSkip!=null)
			{
				previousSkip.skipPointer=null;
				previousSkip=previousSkip.next;
			}
			
		}
		
		
		/**
		 * Set to null all the skip pointers so none of them can be used in any way in any method.
		 * 
		 * @return void
		 */
		public void deleteSkipPointers()
		{
			
			Node<E> node=this.head;
			
			while(node!=null)
			{
				node.skipPointer=null;
				node=node.next;
			}
			
		}

		
		/**
		 * This method sets the number of skipPointers for a {@link LinkedListWithSkipPointers} object.
		 * 
		 * @param numberOfSkipPointers The number of skip pointers for a {@link LinkedListWithSkipPointers} object.
		 */
		public void setNumberOfSkipPointers(int numberOfSkipPointers)
		{
			this.numberOfSkipPointers=(numberOfSkipPointers<=0 || numberOfSkipPointers>this.size)? (int) Math.sqrt(this.size) : numberOfSkipPointers;
		}
		
		
		/**
		 * This method sets the number of the skip pointers in this {@link LinkedListWithSkipPointers} object
		 *  with a special way chosen by the option given as an argument.
		 *  Option number Meanings :
		 *  
		 *  0 : Defines the number of skip pointers by the square root of the number given as argument
		 *  
		 *  -1 : Defines the number of skip pointers by the cubic root of the number given as argument
		 *  
		 *  -2 : Defines the number of skip pointers by the log() = ln() (logarithm with base e) of the 
		 *  	  number given as argument
		 *  
		 *  -3 : Defines the number of skip pointers by the log10() t of the number given as argument
		 *  
		 *   (any positive number )>0 : Defines the number of skip pointers by the exact number given as argument
		 * 
		 * @param option An option which defines the  number of skip pointers in this  
		 * 			{@link LinkedListWithSkipPointers} object
		 * 
		 */
		public void setNumberOfSkipPointersByPredifinedOptions(int option)
		{
			if(option==0)
				this.numberOfSkipPointers=(int)(Math.ceil(Math.sqrt(this.size)));
			else if(option==-1)
				this.numberOfSkipPointers=(int)(Math.ceil(Math.pow(this.size, 1.0/3.0)));
			else if(option==-2)
				this.numberOfSkipPointers=(int)(Math.ceil(Math.log(this.size)));
			else if(option==-3)
				this.numberOfSkipPointers=(int)(Math.ceil(Math.log10(this.size)));
			else if(option>=1)
				this.numberOfSkipPointers=option;
			
		}
		
		
		
		/**
		 * This method makes the operation AND between two lists and returns the result of the common document
		 * ids that occur in both of the lists by updating the ArrayList given as a parameter.
		 * 
		 * @param list The second list  type of {@link LinkedListWithSkipPointers} on which the operation AND
		 * 			 will be made (in association with the list invokes the method)
		 * @param documentIDsAND an ArrayList<Integer> that inside will be placed all the common document ids
		 * 			that the two lists both have
		 * @return true if the operation was made or false otherwise
		 */
		public boolean ListAND(LinkedListWithSkipPointers<E> list,ArrayList<Integer> documentIDsAND)
		{
			
			if(list==null || documentIDsAND==null)
				return false;
			
			documentIDsAND.clear();		// clear the arraylist that was given
			
			Node<E> n1=this.head;
			Node<E> n2=list.head;
			
			
			
			// while none of the pointers has gone to null in the attempt to reach the other pointer
			while(n1!=null && n2!=null)
			{
				
				int compareResult=n1.data.compareTo(n2.data);
				
				
				// if n1.data.getgetDocumentID < n2.data.getgetDocumentID then forward the pointer n1 to reach n2
				if(compareResult<0)
				{
					
					if(n1.skipPointer!=null && n1.skipPointer.data.getDocumentID()<=n2.data.getDocumentID())
					{
						n1=n1.skipPointer;
					}
					else 
					{
						while(n1!=null && n1.data.getDocumentID()<n2.data.getDocumentID())
						{
							n1=n1.next;
						}
						
					}
						
				}
				else if(compareResult>0) // if n2.data.getgetDocumentID < n1.data.getgetDocumentID then forward the pointer n2 to reach n1
				{
				
					if(n2.skipPointer!=null && n2.skipPointer.data.getDocumentID()<=n1.data.getDocumentID())
					{
						n2=n2.skipPointer;
					}
					else 
					{
						while(n2!=null && n2.data.getDocumentID()<n1.data.getDocumentID())
						{
							n2=n2.next;
						}
						
					}
					
				}
				else // if n1.data.getgetDocumentID == n2.data.getgetDocumentID then forward both of them to the next place
				{	 // and add their common document ID in the list with the document ids
					documentIDsAND.add(n1.data.getDocumentID());
					
					n1=n1.next;
					n2=n2.next;
				}
				
				
				
			}
			
		
			return true;
		}
		
		
		
		/**
		 * This method updates the document IDs in an object of list type of {@link ArrayList<Integer>}  
		 * given as an argument by deleting all the document ids that doesn't exist in the 
		 * object invoking this method and by leaving untouched the document ids that exist.
		 * 
		 * @param documentIDs an ArrayList<Integer> which inside has all the document ids that have to be updated
		 * 		  in order to be left inside the list,all the document ids that also exist int the object invoking
		 * 		  the method
		 * @return true if the examination was succesfully made or false otherwise
		 */
		public boolean examineTheExistenceOfSpecificIDs(ArrayList<Integer> documentIDs)
		{
			
			if(documentIDs==null )
				return false;
		
			
			if(documentIDs.size()<=0)
				return false;
			
			Node<E> node=this.head;
			
			int index=0;
			int currentID=documentIDs.get(0);
			
			while(node!=null && index<documentIDs.size())
			{
				
				if(node.data.getDocumentID()==currentID)
				{
					
					if((++index)<documentIDs.size())
					{
						currentID=documentIDs.get(index);	
						
					}
					
				}
				else if(node.data.getDocumentID()>currentID)
				{
					documentIDs.remove(index);		// remove the id from the arraylist -> indicating that it wasn't 
													// found because the available document ids have passed through
					if(index<documentIDs.size())	// the current id
						currentID=documentIDs.get(index);
					
					continue ;	// Continue to next iteration in order to skip the change of the node
				}										
				
				if(node.skipPointer!=null && node.skipPointer.data.getDocumentID()< currentID)
					node=node.skipPointer;
				else 
				{
					node=node.next;
				}
				
			}
			
			return true;
			
		}
		
		
		/**
		 * This method performs the operation AND on many lists and updates an ArrayList<Integer>>
		 * with the DocumentIDs that all the lists have in common.
		 * 
		 * @param documentIDsAND
		 * @param list A list of lists type of LinkedListWithSkipPointers<E> which on them the operation AND
		 * 		  will be made
		 * @return true if the operation has a positive result or false otherwise
		 */
		public boolean ListAND(ArrayList<Integer> documentIDsAND,ArrayList<LinkedListWithSkipPointers<E>> list)
		{
			if(documentIDsAND==null || list==null )
				return false;
			
			int length=list.size();
			
			if(length<1)
				return false;
			
			if(length==1)
			{
				int temp[]=list.get(0).getDocIDs();
				
				for(int i=0;i<temp.length;i++)
					documentIDsAND.add(temp[i]);
				
				return true;
			}
			
			
			  if(!list.get(0).ListAND(list.get(1), documentIDsAND))
				  return false;
			
			  
				
				for(int i=2;i<length;i++)
				{
					if(documentIDsAND.size()<=0 || !list.get(i).examineTheExistenceOfSpecificIDs(documentIDsAND))
							return false;
				}
			
			
				 
			return true;
		}
		
		
		/**
		 * This method makes the operation OR between two lists and returns the result of a union set of document
		 * ids that occur in each of the lists separately by updating the ArrayList given as a parameter.
		 * 
		 * @param list The second list  type of {@link LinkedListWithSkipPointers} on which the operation OR
		 * 			 will be made (in association with the list invokes the method)
		 * 
		 * @param documentIDsOR an ArrayList<Integer> that inside will be placed all the document ids
		 * 			that the two lists have
		 * 
		 * @return true if the operation was made or false otherwise
		 */
		public boolean ListOR(ArrayList<Integer> documentIDsOR,ArrayList<LinkedListWithSkipPointers<E>> list)
		{
			
			if(list==null ||(list.size())<=0 || documentIDsOR==null)
				return false;
			
			documentIDsOR.clear();
			int length=list.size();
			
			int i=0,element=0;
			Node<E> node=null;
			
			
			
			for( i=0;i<length;i++)
			{
				
				node=list.get(i).head;
				
				while(node!=null)	// The union of all the document ids 
				{
				    element=node.data.getDocumentID();
					int j=0;
					int size=documentIDsOR.size();
					
					boolean found=false;
				    
					for(j=0;j<size;j++)
				    {
				    	if(documentIDsOR.get(j)==element)
				    	{
				    		found=true;
				    		break;
				    	}
				    	else if(element<documentIDsOR.get(j))
				    		break;
				    }
				    
				    if(!found)
				    	documentIDsOR.add(j, element);;
				    
					
					
				    		
					node=node.next;
				}
				
				
				
			}
			
			return true;
		}
		
		private static int binarySearchOnSortedArrayList(ArrayList<Integer> list,int element)
		{
			int r=0;
			if(list==null || (r=list.size())<=0)
			return -1;
			
			int l=0;
			r--;
			
			int mid=0;
			int middleElement=0;
			
			while(l<r)
			{
				mid=l+(r-l)/2;
				middleElement=list.get(mid);
				
				if(middleElement<element)
					l=mid+1;
				else if(element<middleElement)
					r=mid-1;
				else return mid;
				
			}
			
			
			return -1;
			
		}
		
		/**
		 * This method returns an integer array which contains all the document 
		 * ids that this list contain
		 * 
		 * @return an integer array which contains all the document ids that this list contain
		 */
		public int[] getDocIDs()
		{
			if(this.head==null )
				return null;
			
			int temp[]=new int[this.size];
			
			int i=0;
			Node<E> node=this.head;
			
			while(node!=null)
			{
				temp[i]=node.data.getDocumentID();
				node=node.next;
				i++;
			}
			
			return temp;
		}
		
		
		/**
		 * This method merges two lists of this class.
		 * 
		 * @param list The second list to be merged
		 * @return The number of positions that have been added to the object which invoked the method
		 */
		public int mergeLists(LinkedListWithSkipPointers<E> list)
		{
			if(list==null)
				return 0;
			
			Node<E> node=list.head;
			int numberOfAssigns=0;
			
			while(node!=null)
			{		
					if(this.insert(node.data))
					{
						numberOfAssigns+=(node.data.size());
					}
					
			}
			
			return numberOfAssigns;
		}
		
		
		/**
		 * This method creates and returns a String representation of an object of this class
		 * 
		 * @return a String representation of an object of this class
		 */
		public String toString()
		{
			
			if(this.head==null)
				return "There aren't any appears of this word in any documents.\n\n";
			
			Node<E> node=this.head;
			
			StringBuilder builder=new StringBuilder();
			
			int i=0;
			while(node!=null)
			{
				builder.append((++i)+" ) ");
				builder.append(node.data.toString()+"\n\n");
				node=node.next;
			}
			
			
			return builder.toString();
			
		}
		
		
		
	}
	
	
	

	/**
	 * Constructor of an object of this class.
	 * 
	 * 
	 * @param word The word as a String that this object will represent
	 * @throws NullPointerException If the String given as a parameter is null
	 */
	 public WordRegistration(String word) throws NullPointerException
	  {
		  if(word==null)
			  throw new NullPointerException("Null String word.");
		  
		  this.word=word;
		  this.appearances=0;
		  appearDocs=new LinkedListWithSkipPointers<WordAppearsInDoc>();
		  
	  }
	 
	
	/**
	 * Returns The number of appearances of the word in documents.
	 * 
	 * @return The number of appearances of the word in documents
	 */
	 public int getNumberOfAppearances()
	 {
		 return this.appearances;
	 }
	 
	 
	 /**
	  * Returns an array of integers that contains the positions of the occurrences of the word in the
	  * document with id specified by the parameter.
	  * 
	  * @param docID The document id that we want the appearances of the word in this document
	  * 
	  * @return An array of integers which are the positions in the document (specified by the document id given as 
	  *         as an argument) of the word that this object holds
	  */
	 public int[] getAppearancesInDocumentId(int docID)
	 {
		 return this.appearDocs.getPositions(docID);
	 }
	
	
	/**
	 * Returns the String word ,that this registration holds information about.
	 * 
	 * @return the word that this object holds information about its occurences
	 */
	public String getWord()
	{	
		return this.word;
	}
	
	
	
	/**
	 * Returns an array of integers which contains all the document ids of this object.
	 * 
	 * @return an integer array with all the document ids of this object
	 */
	public int[] getDocumentsIDs()
	{
		return this.appearDocs.getDocIDs();
	}
	

	
	/**
	 * 
	 * This method takes an array of {@link WordRegistration} objects  and applies on their lists
	 * the AND operation(includes all the lists).It updates the ArrayList<Integer> which is given 
	 * as an argument,in order after the end of the method it will contain all the documentIDs of 
	 *  which are appeared in each WordRegistration object in the array
	 * 
	 * @param registrationsWords an array with objects type of WordRegistration
	 * @param documentIDs an ArrayList<Integer> which in it will be placed the common documentIDs of all the WordRegistration 
	 * 		   objects given in the array
	 * 
	 * @param includeTheObjectCalling If this is true then the object invoking the method will be included in the operation
	 * 		 if this is false it won't be included  
	 * @return true if the operation has a positive result or false otherwise
	 */
	public boolean documentsListsAND(WordRegistration[] registrationsWords,ArrayList<Integer> documentIDs,boolean includeTheObjectCalling)
	{
		if(documentIDs==null || registrationsWords==null)
			return false;
		
		ArrayList<LinkedListWithSkipPointers<WordAppearsInDoc>> list=null;
		
		if(includeTheObjectCalling)
		{
			list=new ArrayList<LinkedListWithSkipPointers<WordAppearsInDoc>>(registrationsWords.length+1);
			list.add(this.appearDocs);
		}
		// the length of the registrationsWords+1 because the object which invokes the method is included
		else 
			list=new ArrayList<LinkedListWithSkipPointers<WordAppearsInDoc>>(registrationsWords.length);
		
		
		
		documentIDs.clear();
		
		int i=0;
		
		for(i=0;i<registrationsWords.length;i++)
		{
			list.add(registrationsWords[i].appearDocs);
			i++;
		}
		
			return this.appearDocs.ListAND(documentIDs, list);
			
			
	}
	

	/**
	 * 
	 * This method takes an array of {@link WordRegistration} objects  and applies on their lists
	 * the OR operation(includes all the lists).It updates the ArrayList<Integer> which is given 
	 * as an argument,in order after the end of the method it will contain all the documentIDs of 
	 *  all the WordRegistration objects in the array given.
	 * 
	 * @param registrationsWords an array with objects type of WordRegistration
	 * @param documentIDs an ArrayList<Integer> which in it will be placed all the documentIDs of all the WordRegistration 
	 * 		   objects given in the array
	 * 
	 * @param includeTheObjectCalling If this is true then the object invoking the method will be included in the operation
	 * 		 if this is false it won't be included  
	 * @return true if the operation has a positive result or false otherwise
	 */
	public boolean documentsListsOR(WordRegistration[] registrationsWords,ArrayList<Integer> documentIDs,boolean includeTheObjectCalling)
	{
		if(documentIDs==null || registrationsWords==null)
			return false;
		
		ArrayList<LinkedListWithSkipPointers<WordAppearsInDoc>> list=null;
		
		if(includeTheObjectCalling)
		{
			list=new ArrayList<LinkedListWithSkipPointers<WordAppearsInDoc>>(registrationsWords.length+1);
			list.add(this.appearDocs);
		}
		// the length of the registrationsWords+1 because the object which invokes the method is included
		else 
			list=new ArrayList<LinkedListWithSkipPointers<WordAppearsInDoc>>(registrationsWords.length);
		
		documentIDs.clear();
		
		int i=0;
			
		
		for(WordRegistration element : registrationsWords)
		{
			list.add(element.appearDocs);
			
		}
		
		
			return this.appearDocs.ListOR(documentIDs,list);
	
	}
	
	
	
	/**
	 * Checks whether the word that this object represents 
	 * has an occurrence entry of finding this word at 
	 * document with id -> docID and at position in that
	 * document -> position.
	 * 
	 * @param docID The document id of a document
	 * @param position A position of a word in a document 
	 * @return true if this object contains such an entry or false otherwise
	 */
	public boolean containsDocIDAndPosition(int docID,int position)
	{
		return this.appearDocs.contains(docID, position);
	}


	/**
	 * Tries to insert an occurrence entry of the word that this object represents
	 * by inserting the document id and the position in that document where
	 * the word was found.
	 * 
	 * @param docID The document id of the word found
	 * @param position The position in the document where the word was found
	 * @return true if it was inserted or false otherwise
	 */
	public boolean insertEntryAtDocumentList(int docID,int position)
	{
		if(appearDocs.insert(docID, position))
		{
			this.appearances++;
			return true;
		}
		
		return false;
	}
	
	
	
	/**
	 * This method deletes an entry of a document id in this object
	 * 
	 * @param docID The document id to be deleted
	 * @return true if it was deleted or false otherwise
	 */
	public boolean deleteEntryFromDocumentList(int docID)
	{
		int numberOfDeletes=this.appearDocs.delete(docID);
		
		if(numberOfDeletes>0)
			this.appearances-=numberOfDeletes;		// the number of changes that is returned
													// shows how many positions where removed
		return numberOfDeletes>0;					// due to deletion of the document entry
	}
	
	
	
	/**
	 * This method sets the number of skip pointers to the object invoking
	 *  this method.More specific it sets the number ofskip pointers on the 
	 *  object type of {@link LinkedListWithSkipPointers} that exists inside 
	 *  of this object.
	 * 
	 * @param option The option that sets a number od skip pointers on this object 
	 * 		  in a special way
	 * @return void
	 */
	public void setTheNumberOfSkipPointers(int numberOfSkipPointers)
	{
		this.appearDocs.setNumberOfSkipPointers(numberOfSkipPointers);
	}
	
	
	/**
	 * This method sets the number of skip pointers based on a 
	 * specific option, to the object invoking this method.
	 * More specific it sets the number of skip pointers on the object type of 
	 * {@link LinkedListWithSkipPointers} that exists inside of this object.
	 * 
	 * @param option The option that sets a number of skip pointers on this object 
	 * 		  in a special way
	 * @return void
	 */
	public void setTheNumberOfSkipPointersByOptions(int option)
	{
		this.appearDocs.setNumberOfSkipPointersByPredifinedOptions(option);
	}
	
	
	/**
	 * This method applies the skip pointers to the object invoking this method.
	 * More specific it applies the skip pointers on the object type of 
	 * {@link LinkedListWithSkipPointers} that exists inside of this object.
	 * 
	 * @return void
	 */
	public void applySkipPointers()
	{
		this.appearDocs.applySkipPointers();
	}
	
	
	/**
	 * This method is the implementation of the method {@link Comparable#compareTo(Object)} which compares two
	 * objects type of the class {@link WordRegistration} and returns the result of the comparison.
	 * The comparison is made on the String words that they both have and represent. 
	 * 
	 * @return a negative number if the object which invokes the method is smaller than the object given as an
	 * 		   argument,1 if it is greater and 0 if they are equal
	 * 
	 */
	@Override
	public int compareTo(WordRegistration wordRegistrationObject) {
		
		if(wordRegistrationObject==null)
			return 1;
		
		return this.word.compareToIgnoreCase(wordRegistrationObject.word);
	}
	
	
	/**
	 * This method merges the two word-registrations(The one that invokes the method and the other that
	 * is given as a parameter) and returns true if the merge was made successfully or false if not. 
	 * 
	 * @param otherObject The object which will be merged on the object that invokes the method 
	 * @return true if the merge was made and at least one elements of them where merged
	 */
	public boolean mergeWordRegistrations(WordRegistration otherObject)
	{
		if(otherObject==null || otherObject.word.compareToIgnoreCase(this.word)!=0)
			return false;
		
		int addFurther=this.appearDocs.mergeLists(otherObject.appearDocs);
		
		if(addFurther>0)
		{
			this.appearances+=addFurther;
		}
		
		return addFurther>0;
		
	}
	
	
	/**
	 * This method deletes all the skip pointers in this object .To be more specific it deletes all the skip pointers
	 *  of the object type of {@link LinkedListWithSkipPointers} ,which exists inside of the object invoking 
	 *  the method .
	 * 
	 * @return void 
	 */
	public void deleteSkipPointers()
	{
		this.appearDocs.deleteSkipPointers();
	}
	
	
	/**
	 * This method creates and returns a String representation of an object of this class which invokes this method
	 * 
	 * @return a String representation of the object which invokes the method 
	 */
	public String toString()
	{
		StringBuilder builder=new StringBuilder("Word : ");
		builder.append(this.word+"\nWord Appearances In documents : ");
		builder.append(this.appearances+"\n+++++ Word Appearances +++++\n");
		builder.append(this.appearDocs.toString());
		return builder.toString();
	}
	
	

}
	

