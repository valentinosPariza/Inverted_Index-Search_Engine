package invertedIndex;

public interface BinaryTree <E extends Comparable> 
{
	
	/**
	 * This abstractmethod is about inserting an object in  the tree.
	 * 
	 * @param data The object-element to be inserted in the tree.
	 * @return true if it was inserted or false otherwise
	 */ 
	public boolean insert(E data) ;
	
	
	
	/**
	 * This method deletes the object in the tree that is equal to the object givane as an argument
	 * 
	 * @param data The object to delete from the tree
	 * @return True if it was deleted or false otherwise
	 */
	public void delete(E data);
	
	
	/**
	 * This method calculates the size of the tree which is the number of the nodes in the tree 
	 * and returns it.
	 * 
	 * @return the number of the elements(data) in the tree
	 */
	public int getSize();
	
	/**
	 * This method calculates the height of the tree and returns it.
	 * 
	 * @return the height of the Tree
	 */
	public int getHeight();
	
	
	
	
}
