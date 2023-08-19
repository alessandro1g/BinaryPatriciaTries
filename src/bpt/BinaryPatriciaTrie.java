package bpt;

import bpt.UnimplementedMethodException;
import java.util.ArrayList; 

import java.util.Iterator;

/**
 * <p>{@code BinaryPatriciaTrie} is a Patricia Trie over the binary alphabet &#123;	 0, 1 &#125;. By restricting themselves
 * to this small but terrifically useful alphabet, Binary Patricia Tries combine all the positive
 * aspects of Patricia Tries while shedding the storage cost typically associated with tries that
 * deal with huge alphabets.</p>
 *
 * @author YOUR NAME HERE!
 */
public class BinaryPatriciaTrie {

    /* We are giving you this class as an example of what your inner node might look like.
     * If you would prefer to use a size-2 array or hold other things in your nodes, please feel free
     * to do so. We can *guarantee* that a *correct* implementation exists with *exactly* this data
     * stored in the nodes.
     */
    private static class TrieNode {
        private TrieNode left, right;
        private String str;
        private boolean isKey;

        // Default constructor for your inner nodes.
        TrieNode() {
            this("", false);
        }

        // Non-default constructor.
        TrieNode(String str, boolean isKey) {
            left = right = null;
            this.str = str;
            this.isKey = isKey;
        }
    }

    private TrieNode root;
    private int size;

    /**
     * Simple constructor that will initialize the internals of {@code this}.
     */
    public BinaryPatriciaTrie() {
        this.root = new TrieNode();
        this.size = 0;
    }

    /**
     * Searches the trie for a given key.
     *
     * @param key The input {@link String} key.
     * @return {@code true} if and only if key is in the trie, {@code false} otherwise.
     */
    
    /*
     * Search steps: 
     * we need to iterate over the current node string:
     * 		Conditions: 
     * 			empty string
     * 			matched character
     * 			unmatched character
     * 			
     */
    
    public TrieNode findNext(char c, TrieNode node) {
    	/*
    	 * returns a node or null 
    	 */
    	TrieNode l = node.left;
    	TrieNode r = node.right;
    	if(l != null) {
    		char lc = l.str.charAt(0);
    		if (lc == c) {
    			return l;
    		}
    	}
    	if(r != null) {
    		char rc = r.str.charAt(0);
    		if (rc == c) {
    			return r;
    		}
    	}
    	return null;
    }
    public TrieNode searchHelper(String key) {
    	TrieNode curr = root;
    	// I need to find the correct node to traverse to
    	int n = key.length();
    	
    	int i = 0;
    	while(i < n) {
    		//null check
    		curr = findNext(key.charAt(i),curr);
    		if (curr == null) {
    			return null;
    		}
    		for(int j = 0; j < curr.str.length(); j++) {
    			if(i>=n) {
    				return null;
    				
    			}
    			if(curr.str.charAt(j) != key.charAt(i)) {
    				return null;
    			}
    			i++;
    		}
    	}
    	return curr;
    	
    }
    
    public boolean search(String key) {
    	TrieNode curr = searchHelper(key);
    	if (curr == null) {
    		return false;
    	}
    	if (curr.isKey == true) {
    		return true;
    	}else {
    		return false;
    	}
    	
    }

    /**
     * Inserts key into the trie.
     *
     * @param key The input {@link String}  key.
     * @return {@code true} if and only if the key was not already in the trie, {@code false} otherwise.
     */
    public boolean insert(String key) {
    	System.out.println("insert: " + key);
    	
    	TrieNode curr = root;
    	// I need to find the correct node to traverse to
    	int n = key.length();
    	
    	int i = 0;
    	while(i < n) {
    		//null check
    		int ip = i;
    		TrieNode prev = curr;
    		curr = findNext(key.charAt(i),curr);
    		if (curr == null) {
    			this.size ++;
    			if (prev.left == null && key.charAt(i)=='0') {
    				prev.left = new TrieNode(key.substring(i),true);
    				return true;
    			}else {
    				prev.right = new TrieNode(key.substring(i),true);
    				return true;
    			}
    			// at this point we have found the end of the line so we need to figure 
    		}
    		
    		for(int j = 0; j < curr.str.length(); j++) {
    			int l = curr.str.length();
    			if(i>=n) {
    				// im at the end of the key and will need to split it
    				String f = curr.str.substring(0,j);
    				String s = curr.str.substring(j);
    				//curr.str = f;
    				TrieNode temp = new TrieNode(s, curr.isKey);
    				temp.left = curr.left;
    				temp.right = curr.right;
    				if(s.charAt(0)=='0') {
    					curr.left = temp;
        				curr.right = null;
    					
    				}else {
    					curr.right = temp;
        				curr.left = null;
    				}
    				this.size += 1;
    				
    				curr.isKey = true;
    				
    				curr.str = f;
    				return true; 
    				
    			}
    			if(curr.str.charAt(j) != key.charAt(i)) {
    				// we need to split at j-1
    				String keep = curr.str.substring(0,(i-ip));
    				String cont = curr.str.substring((i-ip));
    				String newr = key.substring(i);
    				//System.out.println(ip);
    				TrieNode lNode; 
    				TrieNode rNode; 
    				if (cont.charAt(0)=='1') {
    					//this means cont goes right
    					rNode = new TrieNode(cont, curr.isKey);
    					lNode = new TrieNode(newr, true);
    					rNode.left =curr.left;
    					rNode.right = curr.right;


    					
    				}else {
    					//this means cont goes left
    					rNode = new TrieNode(newr, true);
    					lNode = new TrieNode(cont, curr.isKey);
    					lNode.left =curr.left;
    					lNode.right = curr.right;
    				}
    				curr.isKey = false;
    				curr.left = lNode;
    				curr.right = rNode;
    				curr.str = keep;
    				this.size += 1;
    				
    				return true;
    			}
    			i++;
    		}
    		if (i==n && curr.isKey == false) {
    			curr.isKey = true;
    			this.size += 1;
    			return true;
    		}
    		
    	}
    	return false;
    	
    }


    /**
     * Deletes key from the trie.
     *
     * @param key The {@link String}  key to be deleted.
     * @return {@code true} if and only if key was contained by the trie before we attempted deletion, {@code false} otherwise.
     */
    public boolean delete(String key) {
    	System.out.println("delete: " + key);
    	TrieNode curr = root;
    	int n = key.length();
    	
    	int i = 0;
    	while(i < n) {
    		//null check
    		int ip = i;
    		TrieNode prev = curr;
    		curr = findNext(key.charAt(i),curr);
    		if (curr == null) {
    			return false;
    		}
    		for(int j = 0; j < curr.str.length(); j++) {
    			if(i>=n) {
    				return false;
    			}
    			if(curr.str.charAt(j) != key.charAt(i)) {
    				return false;
    			}
    			i++;
    		}
    		if(i==n&&curr.isKey ==false) {
    			return false;
    		}
    		if (i == n && curr.isKey ==true) {
    			// we need to delete curr 
    			// condition 1: if the node has more than 1 child 
    				// just set isKey to false
    			
    			
    			if (curr.left != null && curr.right != null) {
    				curr.isKey = false;
        		// condition 3: if the node has no children
    			// delete it
    			}else if(curr.left == null && curr.right == null) {
    				if(prev.left != null && prev.left.str.equals(curr.str)) {
    					//delete left 
    					prev.left = null;
    					if (prev.isKey != true) {
    						TrieNode temp = prev.right;
    						if (temp!= null && prev.str.length()!=0) {
    							prev.left = temp.left;
        						prev.right = temp.right;
        						prev.str = prev.str + temp.str;
        						prev.isKey = temp.isKey;
    						}else {
    							prev.left = null;
    						}
    						
    					}
    					
    				}else if(prev.right != null && prev.right.str.equals(curr.str)) {
    					//delete right
    					prev.right = null;
    					if (prev.isKey != true && prev.str.length()!=0) {
    						TrieNode temp = prev.left;
    						prev.left = temp.left;
    						prev.right = temp.right;
    						prev.str = prev.str + temp.str;
    						prev.isKey = temp.isKey;
    					}else {
    						prev.right = null;
    					}
    					
    					
    					
    				}
    			// condition 2: if the node has only one child 
    				// merge it
    			}else {
    				if (curr.left != null) { 
    					TrieNode temp = curr.left; 
    					curr.left = temp.left;
    					curr.right = temp.right;
    					curr.isKey = temp.isKey;
    					curr.str = curr.str + temp.str;
    				}else {
        				TrieNode temp = curr.right; 
        				curr.left = temp.left;
        				curr.right = temp.right;
        				curr.isKey = temp.isKey;
        				curr.str = curr.str + temp.str;
        				
    				}
    				
    			}
    			this.size --;
    		}
    		
    	}
    	
        return true;
    }

    /**
     * Queries the trie for emptiness.
     *
     * @return {@code true} if and only if {@link #getSize()} == 0, {@code false} otherwise.
     */
    public boolean isEmpty() {
        if (this.size == 0) {
        	return true;
        }else {
        	return false;
        }
    }

    /**
     * Returns the number of keys in the tree.
     *
     * @return The number of keys in the tree.
     */
    public int getSize() {
        return this.size;
    }

    /**
     * <p>Performs an <i>inorder (symmetric) traversal</i> of the Binary Patricia Trie. Remember from lecture that inorder
     * traversal in tries is NOT sorted traversal, unless all the stored keys have the same length. This
     * is of course not required by your implementation, so you should make sure that in your tests you
     * are not expecting this method to return keys in lexicographic order. We put this method in the
     * interface because it helps us test your submission thoroughly and it helps you debug your code! </p>
     *
     * <p>We <b>neither require nor test </b> whether the {@link Iterator} returned by this method is fail-safe or fail-fast.
     * This means that you  do <b>not</b> need to test for thrown {@link java.util.ConcurrentModificationException}s and we do
     * <b>not</b> test your code for the possible occurrence of concurrent modifications.</p>
     *
     * <p>We also assume that the {@link Iterator} is <em>immutable</em>, i,e we do <b>not</b> test for the behavior
     * of {@link Iterator#remove()}. You can handle it any way you want for your own application, yet <b>we</b> will
     * <b>not</b> test for it.</p>
     *
     * @return An {@link Iterator} over the {@link String} keys stored in the trie, exposing the elements in <i>symmetric
     * order</i>.
     */
    public Iterator<String> inorderTraversal() {
    	ArrayList<String> it = new ArrayList<String>();
    	
    	ArrayList a = itHelp(root,it,"");
    	System.out.println(a.toString());
    	return a.iterator();
    }


    
    public ArrayList itHelp (TrieNode curr, ArrayList lst, String str) {
    	
    	if (curr != null) {
    		str += curr.str;
    		lst = itHelp(curr.left,lst,str);
    		if(curr.isKey) {
    			lst.add(str);
    		}
    		
    		lst = itHelp(curr.right,lst,str);
    	}
    	return lst;
    	
    }

    /**
     * Finds the longest {@link String} stored in the Binary Patricia Trie.
     * @return <p>The longest {@link String} stored in this. If the trie is empty, the empty string &quot;&quot; should be
     * returned. Careful: the empty string &quot;&quot;is <b>not</b> the same string as &quot; &quot;; the latter is a string
     * consisting of a single <b>space character</b>! It is also <b>not the same as the</b> null <b>reference</b>!</p>
     *
     * <p>Ties should be broken in terms of <b>value</b> of the bit string. For example, if our trie contained
     * only the binary strings 01 and 11, <b>11</b> would be the longest string. If our trie contained
     * only 001 and 010, <b>010</b> would be the longest string.</p>
     */
    public String getLongest() {
    	if(root.left ==null && root.right == null) {
    		return root.str;
    	}
    	 // ERASE THIS LINE AFTER YOU IMPLEMENT THE METHOD!
    	ArrayList<String> it = new ArrayList<String>();
    	it = itHelp(root,it,"");
    	String ret = "";
    	for (String i: it) {
    		System.out.println(i);
    		if (ret.length()<=i.length()) {
    			ret = i;
    		}
    	}
    	return ret;
    }

    /**
     * Makes sure that your trie doesn't have splitter nodes with a single child. In a Patricia trie, those nodes should
     * be pruned.
     * @return {@code true} iff all nodes in the trie either denote stored strings or split into two subtrees, {@code false} otherwise.
     */
    public boolean isJunkFree(){
        return isEmpty() || (isJunkFree(root.left) && isJunkFree(root.right));
    }

    private boolean isJunkFree(TrieNode n){
        if(n == null){   // Null subtrees trivially junk-free
            return true;
        }
        if(!n.isKey){   // Non-key nodes need to be strict splitter nodes
            return ( (n.left != null) && (n.right != null) && isJunkFree(n.left) && isJunkFree(n.right) );
        } else {
            return ( isJunkFree(n.left) && isJunkFree(n.right) ); // But key-containing nodes need not.
        }
    }
    
    public static void main(String[] args) {
    	BinaryPatriciaTrie rt = new BinaryPatriciaTrie();
    	/*
        rt.insert("11");
        rt.insert("001");
        rt.insert("00100");
        rt.insert("1101");
        rt.insert("1111000");
        rt.insert("111100110001");
        rt.insert("001000");
        rt.insert("001001");
    	System.out.println(rt.getSize());
    	System.out.println(rt.search("111100110001"));
    	
    	
    	
    	
    	System.out.println(rt.search("111100110001"));
    	*/
    	
    	System.out.println(rt.delete("0"));
    	System.out.println(rt.getSize());
    	System.out.println(rt.insert("0"));
    	System.out.println(rt.getSize());
    	System.out.println(rt.insert("00"));
    	System.out.println(rt.getSize());
    	System.out.println(rt.insert("01"));
    	System.out.println(rt.getSize());
    	System.out.println(rt.delete("0"));
    	System.out.println(rt.getSize());
        
    	System.out.println(rt.insert("0"));
    	System.out.println(rt.getSize());
    	System.out.println(rt.delete("01"));
    	System.out.println(rt.getSize());
    	System.out.println(rt.delete("0"));
    	System.out.println(rt.getSize());
        
    	System.out.println(rt.insert("0"));
    	System.out.println(rt.getSize());
    	System.out.println(rt.insert("01"));
    	System.out.println(rt.getSize());
    	System.out.println(rt.delete("00"));
    	System.out.println(rt.getSize());
    	System.out.println(rt.delete("0"));
    	System.out.println(rt.getSize());
        
    	System.out.println(rt.delete("01"));
    	System.out.println(rt.getSize());
    	
    	//rt.insert("000");
    	Iterator<String> a= rt.inorderTraversal();
    	while(a.hasNext()) {
    		System.out.println(a.next());
    	}
    	
    	
    	BinaryPatriciaTrie rit = new BinaryPatriciaTrie();
    	rit.insert("0101");	
    	rit.insert("1101");
    	rit.insert("11");
    	rit.insert("0");
    	rit.inorderTraversal();
    	
    	
    	
    }
    
}
