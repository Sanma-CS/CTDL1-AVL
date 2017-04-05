package vn.edu.tdt.it.dsa;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;


public class WarehouseBook {

	protected static class WarehouseNode {
		private ProductRecord record;
		private WarehouseNode left, right;
		private int balance;


		public ProductRecord getRecord() {
			return record;
		}
		public void setRecord(ProductRecord record) {
			this.record = record;
		}
		public WarehouseNode getLeft() {
			return left;
		}
		public void setLeft(WarehouseNode left) {
			this.left = left;
		}
		public WarehouseNode getRight() {
			return right;
		}
		public void setRight(WarehouseNode right) {
			this.right = right;
		}
		public int getBalance() {
			return balance;
		}
		public void setBalance(int balance) {
			this.balance = balance;
		}
	}

	private WarehouseNode root;

	@SuppressWarnings("unused")
	private int size;

	@SuppressWarnings("unused")
	public int getSize(){
		return size;
	}

	@SuppressWarnings("unused")
	public WarehouseBook(){
		root = null;
		size = 0;
	}

	/**
	 * Deserialize binary search tree from file with given format
	 * @param file Warehouse data
     * @exception IOException Throw exception if data format not following XXXYY with XXX is ProductID and YY is Quantity
	 */
	public WarehouseBook(File file) throws IOException {
		ArrayList<String> list;
		list = convertToList(file);
		root = buildBST(list);
	}

	/**
	* Read data from file, split all character and node data into separate instances
    * @param file Warehouse data
    * @return ArrayList<String> List of parsed data
    * @exception IOException Throw exception if data format not following XXXYY with XXX is ProductID and YY is Quantity
	* */
	private ArrayList<String> convertToList(File file) throws  IOException {
        // Separate all special character "(",")","N"
		String content = new Scanner(file).nextLine()
                .replaceAll("\\s*[N]\\s*"," N ")
				.replaceAll("\\s*[(]\\s*"," ( ")
                .replaceAll("\\s*[)]\\s*"," ) ");

		ArrayList<String> list = new ArrayList<>(Arrays.asList(content.split("\\s+")));
		ArrayList<String> result = new ArrayList<>();

		for (String item : list) {
			if (item.length() <= 5) {
				result.add(item);
			} else if (item.length() == 10) {
				result.add(item.substring(0,5));
				result.add(item.substring(5,item.length()));
			} else throw new IOException("Unknown input format");
		}
		return result;
	}

	/**
     * Generate a binary search tree from given list
     * @param list Input data
     * @return WarehouseNode Root of binary search tree with reference attached
     * */
	private WarehouseNode buildBST(ArrayList<String> list) {
		WarehouseNode p = new WarehouseNode();
		try {
			if (list.get(0).equals("N")) {
				list.remove(0);
				return null;
			}

			p.setRecord(new ProductRecord(Integer.parseInt(list.get(0).substring(0,3)),
                    Integer.parseInt(list.get(0).substring(3,5))));

			list.remove(0);

			if(list.get(0).equals("(")) {
				list.remove(0);
				p.setLeft(buildBST(list));
				p.setRight(buildBST(list));
				list.remove(0);
			}
		}
		catch(IndexOutOfBoundsException exception) {
			// Do nothing
		}
		return p;

	}

	/**
     * Serialize current binary search tree to file with given format
     * @param file Output file
     * */
	public void save(File file) throws IOException {
		PrintWriter out = new PrintWriter(file.getPath());
		out.print(toString());
		out.close();
	}

	/**
     * Reading events from file to process
     * @param file Event file (max = 2^100)
     * */
	public void process(File file) throws IOException {

        ArrayList<String> events = new ArrayList<>();
		Scanner sc = new Scanner(file);
//		String text = "";
		while (sc.hasNext()) {
//			text += sc.next() + " ";
			//System.out.println(text);
            events.add(sc.next());
		}
        sc.close();
		//System.out.println(text);
//		List<String> list = new ArrayList<>(Arrays.asList(text.split("\\s+")));
		//System.out.println(list);
		process(events);
	}

	/**
     * Event handler
     * @param events Event list
     * */
	public void process(List<String> events) {
		/*for (int i = 0; i < events.size(); i++) {
			int event_case = Integer.parseInt(events.get(i).substring(0,1));
			if (event_case == 0)
				break;
			else {
				switch (event_case) {
					case 1:
						handle01(events.get(i));
						break;
					case 2:
						handle02(events.get(i));
						break;
					case 3:
						handle03();
						break;
					case 4:
						handle04();
						break;
					case 5:
						handle05(events.get(i));
						break;
					case 6:
						handle06(events.get(i));
						break;
					default:
						System.out.println("invaild input");
						//break;
				}
			}
		}*/

		for (String event : events) {
            if ("0".equals(event)) {
                return;
            }
            switch (event.charAt(0)) {
                case '1':
                    handle01(event);
                    break;
                case '2':
                    handle02(event);
                    break;
                case '3':
                    handle03();
                    break;
                case '4':
                    handle04();
                    break;
                case '5':
                    handle05(event);
                    break;
                case '6':
                    handle06(event);
                    break;
                default:
                    System.out.println("Invalid input");
                    break;
            }
        }
	}

	/**
     * Search {@link WarehouseNode} function with key is {@link ProductRecord}.productID
     * <p>This function will call recursive search function</p>
     * @param productID {@link ProductRecord} to find with productID
     * @return WarehouseNode Search result, equals null if not found
     * */
	private WarehouseNode search (int productID) {
		return search(root, productID);
	}

	/**
     * Recursive function to search {@link WarehouseNode} with key is productID
     * @param p Current iterating {@link WarehouseNode}
     * @param productID productID to search
     * */
	private WarehouseNode search(WarehouseNode p, int productID) {
		if (p == null)
			return null;
		else if (productID == p.getRecord().getProductID()) {
			return p;
		}
		else if (productID < p.getRecord().getProductID()){
			return search(p.getLeft(), productID);
		}
		else  {
			return search(p.getRight(), productID);
		}
	}

	/**
     * Insert function to insert new {@link WarehouseNode} into current binary search tree
     * @param pr {@link ProductRecord} of new {@link WarehouseNode} to be inserted
     * @param flag Enable balance factor (AVL mode)
     * */
	private void insert (ProductRecord pr, boolean flag) {
		root = insert(root, pr, flag);
	}

	/**
     * Recursive function to insert new {@link WarehouseNode} into current binary search tree
     * @param p Current iterating {@link WarehouseNode}
     * @param pr {@link ProductRecord} of new {@link WarehouseNode} to be inserted
     * @param flag Enable balance factor (AVL mode)
     * */
	private WarehouseNode insert (WarehouseNode p, ProductRecord pr, boolean flag) {

		if (p == null) {
			WarehouseNode n = new WarehouseNode();
			n.setRecord(pr);
			return n;
		}
		else if (pr.getProductID() == p.getRecord().getProductID()) {
			return p;
		}
		else if (pr.getProductID() < p.getRecord().getProductID()) {
            p.setLeft(insert(p.getLeft(), pr, flag));
		}
		else  {
			p.setRight(insert(p.getRight(), pr, flag));
		}
		if (!flag)
			return p;	// Binary search tree insert mode
		else
			return balance(p); // AVL tree insert mode
	}

	/**
     * Delete function to delete {@link WarehouseNode} from current binary search tree
     * @param key {@link ProductRecord} to delete
     * */
	private void delete(ProductRecord key) {
		root = delete(root, key);
	}

	/**
     * Recursive function to delete {@link WarehouseNode} from current binary search tree
     * @param p Current iterating {@link WarehouseNode}
     * @param check {@link ProductRecord} to delete
     * */
	private WarehouseNode delete(WarehouseNode p, ProductRecord check) {

		if (p == null) throw new RuntimeException();
		else if (check.getProductID() < p.getRecord().getProductID()) {
			p.setLeft(delete(p.getLeft(), check));
		}
		else if (check.getProductID() > p.getRecord().getProductID()) {
			p.setRight(delete(p.getRight(), check));
		}
		else {
			if (p.getLeft() == null)
				return p.getRight();
			else if (p.getRight() == null)
				return  p.getLeft();
			else {
				// Retrieve data from the rightmost node in the left subtree
				p.setRecord(retrieveData(p.getLeft()));
				// Delete the rightmost node in the left subtree
				p.setLeft(delete(p.getLeft(), p.getRecord()));
 			}
		}
		return  p;
	}

	/**
     * Utility function to retrieve data when deleting {@link WarehouseNode} from current binary search tree
     * @param p Current {@link WarehouseNode} to be retrieved
     * @return ProductRecord Data of current {@link WarehouseNode}
     * */
	private ProductRecord retrieveData (WarehouseNode p) {
		while (p.getRight() != null)
			p = p.getRight();
		return p.getRecord();
	}

	/**
     * Search {@link WarehouseNode} whose difference is minimal
     * @param pr {@link ProductRecord} to search
     * @return WarehouseNode Search result, equals null if not found
     * */
	private WarehouseNode search02 (ProductRecord pr) {
		if (root == null)
			return  null;

		WarehouseNode node = root;
		Stack<WarehouseNode> s = new Stack<>();
		WarehouseNode min = new WarehouseNode();
		int minVal = 1000;

		while(node!=null) {
			s.push(node);
			node = node.getLeft();
		}

		while (s.size() > 0) {
			node = s.pop();
			if (Math.abs(node.getRecord().getProductID() - pr.getProductID()) < minVal) {
				min = node;
				minVal = Math.abs(node.getRecord().getProductID() - pr.getProductID());
			}
			if (node.getRight() != null) {
				node = node.getRight();
				while (node != null) {
					s.push(node);
					node = node.getLeft();
				}
			}
		}
		return  min;
	}

	/**
     * Find height of {@link WarehouseNode}
     * @param p {@link WarehouseNode} to calculate height
     * @return int Height of {@link WarehouseNode}
     * */
    private int getHeight(WarehouseNode p) {
        return  getHeight(p, 1);
    }

    /**
     * Recursive function to calculate height of {@link WarehouseNode}
     * @param p Current iterating {@link WarehouseNode}
     * @param level Current {@link WarehouseNode} level
     * */
	private int getHeight(WarehouseNode p, int level) {
		if (p == null)
			return 0;

		int uplevel = getHeight(p.left,  level + 1);
		if (uplevel != 0)
			return uplevel + 1;

		uplevel = getHeight(p.right, level+1);
		return  uplevel + 1;
	}

    /**
     * Calculate balance factor of current {@link WarehouseNode}
     * @param p {@link WarehouseNode} to be calculated
     * @return int Balance of {@link WarehouseNode}
     * */
	private int balanceFactor(WarehouseNode p) {
		p.setBalance(getHeight(p.left) - getHeight(p.right));
		return p.getBalance();
	}

	/**
     * Rotate current {@link WarehouseNode} to right position
     * @param p {@link WarehouseNode} to be right rotated
     * @return WarehouseNode New {@link WarehouseNode} position
     * */
	private WarehouseNode rotateRight(WarehouseNode p) {
		WarehouseNode g = p.getLeft();
		p.setLeft(g.getRight());
		g.setRight(p);
		return g;
	}

    /**
     * Rotate current {@link WarehouseNode} to left position
     * @param p {@link WarehouseNode} to be left rotated
     * @return WarehouseNode New {@link WarehouseNode} position
     * */
	private WarehouseNode rotateLeft(WarehouseNode p) {
		WarehouseNode g = p.getRight();
        p.setRight(g.getLeft());
        g.setLeft(p);
		return g;
	}

	/**
     * Balance {@link WarehouseNode} in case AVL tree is unbalanced
     * @param p Current {@link WarehouseNode} to be balanced
     * */
	private WarehouseNode balance(WarehouseNode p) {
		//p.setBalance(balanceFactor(p));
		if (balanceFactor(p) < -1) {
			if (balanceFactor(p.right) > 0) {
				p.right = rotateRight(p.right);
			}
			p = rotateLeft(p);
		}
		else if (balanceFactor(p) > 1) {
			if(balanceFactor(p.left) < 0) {
				p.left = rotateLeft(p.left);
			}
			p = rotateRight(p);
		}
		return p;
	}

	/**
     * Function to return depth of {@link WarehouseNode} given {@link ProductRecord}
     * @param pr {@link ProductRecord} to find
     * @return int Depth of {@link WarehouseNode} given pr
     * */
    private int getDepth(ProductRecord pr) {
        return getDepth(root, pr,1);
    }

    /**
     * Recursive function to return depth of {@link WarehouseNode} given {@link ProductRecord}
     * @param p Current iterating {@link WarehouseNode}
     * @param pr {@link ProductRecord} to find
     * @param level Current level
     * */
	private int getDepth(WarehouseNode p, ProductRecord pr, int level) {
		if (p == null)
			return 0;
		if (p.getRecord() == pr)
			return level;
		int downLevel = getDepth(p.getLeft(), pr, level + 1);
		if (downLevel != 0)
			return downLevel;
		downLevel = getDepth(p.getRight(), pr, level + 1);
		return downLevel;
	}

	/**
     * Function to delete all {@link WarehouseNode}  whose depth is equal or greater than depth
     * @param depth Depth to delete
     * */
    private void delete01(int depth) {
        root = delete01(root, depth);
    }

    /**
     * Recursive function to delete all {@link WarehouseNode} whose depth is equal or greater than check
     * @param p Current iterating {@link WarehouseNode}
     * @param check Depth to delete
     * */
	private WarehouseNode delete01(WarehouseNode p, int check) {
		if (p == null)
			return null;
		if (getDepth(p.getRecord()) >= check)
			p = null;
		else {
			// Retrieve data from the rightmost node in the left subtree
            p.setLeft(delete01(p.getLeft(), check));
            p.setRight(delete01(p.getRight(), check));
		}
		return p;
	}

	/**
     * Set {@link WarehouseNode} having {@link ProductRecord} to be root of current binary search tree
     * @param pr {@link ProductRecord} to search
     * @param arr Current {@link ProductRecord} list
     * @return WarehouseNode {@link WarehouseNode} to be root
     * */
	private WarehouseNode setToRoot(ProductRecord pr, ArrayList<ProductRecord> arr) {
		WarehouseNode p = new WarehouseNode();
		for (int i = 0; i < arr.size(); i++) {
			if (arr.get(i).getProductID() == pr.getProductID()) {
				if (pr.getQuantity() + arr.get(i).getQuantity() < 99)
					pr.setQuantity(pr.getQuantity() + arr.get(i).getQuantity());
				arr.remove(i);
			}
		}
		p.setRecord(pr);
		return p;
	}

	/**
     * Iterative ReversePostOrder tree traversal
     * @param p Root of current binary search tree to traverse
     * @return ArrayList<ProductRecord> List of {@link ProductRecord}
     * */
	private ArrayList<ProductRecord> reversePostOrder(WarehouseNode p) {
		ArrayList<ProductRecord> list = new ArrayList<>();
		Stack<WarehouseNode> s = new Stack<>();

		if (p == null)
			return list;

		s.push(p);
		WarehouseNode prev = null;
		while (!s.isEmpty()) {
			WarehouseNode current = s.peek();

//			 Go down the tree in search of a leaf an if
//				so process it and pop stack otherwise move down
			if (prev == null || prev.getLeft() == current || prev.getRight() == current) {
				if (current.getRight() != null)
					s.push(current.getRight());
				else if (current.getLeft() != null)
					s.push(current.getLeft());
				else {
					s.pop();
					list.add(current.getRecord());
				}
			}

			else if(current.getRight() == prev) {
				if (current.getLeft() != null)
					s.push(current.getLeft());
				else {
					s.pop();
					list.add(current.getRecord());
				}
			}
			else if (current.getLeft() == prev) {
				s.pop();
				list.add(current.getRecord());
			}
			prev = current;
		}
		return list;
	}

    /**
     * Iterative ReversePreOrder tree traversal
     * @param p Root of current binary search tree to traverse
     * @return ArrayList<ProductRecord> List of {@link ProductRecord}
     * */
	private ArrayList<ProductRecord> reversePreOrder (WarehouseNode p) {
		ArrayList<ProductRecord> list = new ArrayList<>();
		Stack<WarehouseNode> s = new Stack<>();

		if (p == null)
			return list;
		s.push(p);
		while (!s.isEmpty()) {
			WarehouseNode current = s.peek();
			list.add(current.getRecord());
			s.pop();


//			 Left child is pushed first so that right is processed first
			if (current.getLeft() != null)
				s.push(current.getLeft());
			if (current.getRight() != null)
				s.push(current.getRight());
		}
		return list;
	}

    /**
     * Iterative InOrder tree traversal
     * @param p Root of current binary search tree to traverse
     * @return ArrayList<ProductRecord> List of {@link ProductRecord}
     * */
	private ArrayList<ProductRecord> inOrder(WarehouseNode p) {
		ArrayList<ProductRecord> list = new ArrayList<>();
		Stack<WarehouseNode> s = new Stack<>();

		if (p == null)
			return list;
		while (p != null) {
			s.push(p);
			p = p.getLeft();
		}

		while (!s.isEmpty()) {
			p = s.pop();
			list.add(p.getRecord());
			if (p.getRight() != null) {
				p = p.getRight();

				while (p != null) {
					s.push(p);
					p = p.getLeft();
				}
			}
		}
		return list;
	}

	/**
     * Handle event 1
     * <p>Insert {@link WarehouseNode} or add quantity to exist {@link WarehouseNode}</p>
     * <p>Format: 1XXXY</p>
     * <ul>
     *     <li><b>XXX</b> : {@link WarehouseNode} to be inserted or added</li>
     *     <li><b>Y</b> : Quantity of {@link WarehouseNode}'s {@link ProductRecord}</li>
     * </ul>
     * */
	public void handle01 (String s) {
		int id = Integer.parseInt(s.substring(1,4));
		int quantity = Integer.parseInt(s.substring(4,s.length()));
		ProductRecord pr = new ProductRecord(id, quantity);

		if(search(id) == null) {
			insert(pr, false);
		}
		else {
			if (search(id).getRecord().getQuantity() + quantity > 99) {
                search(id).getRecord().setQuantity(99);
            } else {
                search(id).getRecord().setQuantity(search(id).getRecord().getQuantity() + quantity);
            }
		}
	}

	/**
     * Handle event 2
     * <p>Remove quantity of {@link WarehouseNode}'s {@link ProductRecord}</p>
     * <p>If {@link WarehouseNode}'s {@link ProductRecord} equals 0, remove {@link WarehouseNode} of current tree</p>
     * <p>Format: 2XXXY</p>
     * <ul>
     *     <li><b>XXX</b> : {@link WarehouseNode} to be removed or subtracted</li>
     *     <li><b>Y</b> : Quantity of {@link WarehouseNode}'s {@link ProductRecord}</li>
     * </ul>
     * */
	public void handle02 (String s) { 	//2XXXY
		int id = Integer.parseInt(s.substring(1,4));
		int quantity = Integer.parseInt(s.substring(4,s.length()));
		ProductRecord check = new ProductRecord(id, quantity);

		WarehouseNode p = search02(check);
		if (p == null) throw new NullPointerException();
		else {
			if ((p.getRecord().getQuantity() - check.getQuantity()) <= 0) {
				delete(p.getRecord());
			}
			else {
				p.getRecord().setQuantity(p.getRecord().getQuantity() - check.getQuantity());
			}
		}


	}

	/**
     * Handle event 3
     * <p>Simple AVL restructure</p>
     * <p>Format: 3</p>
     * */
	public void handle03 () {
		ArrayList<ProductRecord> arrlist = inOrder(root);
		int mid = arrlist.size()/2;

		while (root != null)
			delete(root.getRecord());

		WarehouseNode p = new WarehouseNode();
		p.setRecord(arrlist.get(mid));
		WarehouseNode l = null;
		WarehouseNode r = null;

		for (int i = 0; i < mid; i++) {
			l = insert(l, arrlist.get(i), true);
		}
		for (int i = mid+1; i< arrlist.size(); i++ ) {
			r = insert(r, arrlist.get(i), true);
		}

		p.setLeft(l);
		p.setRight(r);
		root = p;
	}

    /**
     * Handle event 4
     * <p>Complex AVL restructure</p>
     * <p>Format: 4</p>
     * */
	public void handle04() {
		ArrayList<ProductRecord> arrlist = reversePreOrder(root);
		while (root != null)
			delete(root.getRecord());

        for (ProductRecord pr : arrlist) insert(pr, true);
	}

	/**
     * Handle event 5
     * <p>Special import: set imported {@link WarehouseNode} to be root of current binary search tree with added quantity or create new {@link WarehouseNode} in case not found</p>
     * <p>Format: 5XXXY</p>
     * <ul>
     *     <li><b>XXX</b> : {@link WarehouseNode} to be processed</li>
     *     <li><b>Y</b> : Quantity to be update</li>
     * </ul>
     * */
	public void handle05 (String s) {
		ArrayList<ProductRecord> records = reversePostOrder(root);

		int id = Integer.parseInt(s.substring(1,4));
		int quantity = Integer.parseInt(s.substring(4,s.length()));
		ProductRecord pr = new ProductRecord(id, quantity);

		while (root != null)
			delete(root.getRecord());

		root = setToRoot(pr, records);

        for (ProductRecord record : records) insert(record, false);

	}

	/**
     * Handle event 6
     * <p>Remove all {@link WarehouseNode} whose depth is equal or greater than depth</p>
     * <p>Format: 6Z</p>
     * <ul>
     *     <li><b>Z</b> : Depth to be deleted</li>
     * </ul>
     * */
	public void handle06 (String s) {
		int depth = Integer.parseInt(s.substring(1,s.length()));
		if (depth == 0)
			root = null;
		else
			delete01(depth);
	}

	/**
     * Override original toString() methods.
     * <p>Tree deserialization utility</p>
     * */
	@Override
	public String toString() {
		if (root == null)
			return  "";
		else
			return toString(root);
	}

	/**
     * Recursive function to print tree structure to defined format
     * @param p Current iterating {@link WarehouseNode}
     * */
	private String toString(WarehouseNode p) {
		String h = "";

		if (p == null) {
			h += "N";
		}
		else {
			h += p.getRecord().toString();

			if (p.left != null || p.right!= null) {
				h += " (";
				h += toString(p.left) + " ";
				h += toString(p.right);
				h += ")";
			}
		}

		return h;
	}

	public static void main(String[] args){
		try{
			WarehouseBook wb = new WarehouseBook(new File("warehouse.txt"));
			wb.process(new File("events.txt"));
			wb.save(new File("warehouse_new.txt"));
		}catch(Exception ex){
			ex.printStackTrace();
		}

	}
}