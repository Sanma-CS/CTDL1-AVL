package vn.edu.tdt.it.dsa;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;
//import java.util.Objects;


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
	private int size; //have to update

	public int getSize(){
		return size;	//have to update later
	}

	public WarehouseBook(){
		root = null;
		size = 0;
	}

	public WarehouseBook(String string) throws IOException {
		string = string.replaceAll("\\s*[N]\\s*"," N ")
				.replaceAll("\\s*[(]\\s*"," ( ").replaceAll("\\s*[)]\\s*"," ) ");

		ArrayList<String> list = new ArrayList<>(Arrays.asList(string.split("\\s+")));
		ArrayList<String> result = new ArrayList<>();

		for (String item : list) {
			if (item.length() <= 5) {
				result.add(item);
			} else if (item.length() == 10) {
				result.add(item.substring(0,5));
				result.add(item.substring(5,item.length()));
			} else throw new IOException("Unknown input format");
		}
		//System.out.println(list);	//debug
		root = read(result);
	}

	public WarehouseBook(File file) throws IOException {
		//sinh vien viet ma tai day
		ArrayList<String> list = new ArrayList<String>();
		list = convertList(file);
		root = read(list);
		//System.out.println(root.getRecord()); 	//debug

	}

	/*
		get String from input file into a ArrayList<String>
	 */
	private ArrayList<String> convertList (File file) throws  IOException {
		Scanner sc = new Scanner(file);
		String content = sc.nextLine().replaceAll("\\s*[N]\\s*"," N ")
				.replaceAll("\\s*[(]\\s*"," ( ").replaceAll("\\s*[)]\\s*"," ) ");
		sc.close();
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
		//System.out.println(list);	//debug
		return result;
	}

	/*
		read list into BSTree
		UPDATE SIZE LATER
	 */
	private WarehouseNode read(ArrayList<String> list) {
		WarehouseNode p = new WarehouseNode();
		try {
			if (list.get(0).equals("N")) {
				list.remove(0);
				return null;
			}

			int id = Integer.parseInt(list.get(0).substring(0,3));
			//System.out.println(id);		//debug
			int quantiny = Integer.parseInt(list.get(0).substring(3,5));
			//System.out.println(quantiny);		//debug
			ProductRecord pr = new ProductRecord(id, quantiny);
			p.setRecord(pr);

			list.remove(0);

			if(list.get(0).equals("(")) {
				list.remove(0);
				p.left = read(list);
				p.right = read(list);
				list.remove(0);
			}
			//System.out.println(list);	//debug
		}
		catch(IndexOutOfBoundsException exception) {
			exception.getMessage();
		}
		return p;

	}

	public void save(File file) throws IOException {
		//sinh vien viet ma tai day
		String h = toString();
		//System.out.println(h);	//debug
		PrintWriter out = new PrintWriter(file.getPath());
		out.print(h);
		out.close();



	}

	public WarehouseNode getRoot() {
		return root;
	}

	public void process(File file) throws IOException {
		//sinh vien viet ma tai day

		Scanner sc = new Scanner(file);
		String text = "";
		while (sc.hasNext()) {
			text += sc.next() + " ";
			//System.out.println(text);

		}
		//System.out.println(text);
		List<String> list = new ArrayList<>(Arrays.asList(text.split("\\s+")));
		//System.out.println(list);
		process(list);


	}

	public void process(List<String> events) {
		//sinh vien viet ma tai day

		for (int i = 0; i < events.size(); i++) {
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
		}
	}

	/*
		search key in BST, key is productID
		public method: from root
		private method: current WarehouseNode - CAN ONLY USE IN BST
	 */

	public WarehouseNode search (int key) {
		return  search(root, key);
	}

	private WarehouseNode search(WarehouseNode p, int key) {
		if (p == null)
			return null;
		else if (key == p.getRecord().getProductID()) {
			return p;
		}
		else if (key < p.getRecord().getProductID()){
			return search(p.left, key);
		}
		else  {
			return search(p.right, key);
		}
	}

	/*
		add the new node
		public method: from root
		private method: return a new WarehouseNode with ID & quanity
	 */

	public void insert (ProductRecord pr, boolean flag) {
		root = insert(root, pr, flag);
	}

	private WarehouseNode insert (WarehouseNode p, ProductRecord pr, boolean flag) {
		//WarehouseNode n = new WarehouseNode();
		if (p == null) {
			WarehouseNode n = new WarehouseNode();
			n.setRecord(pr);
			return n;
		}
		else if (pr.getProductID() == p.getRecord().getProductID()) {
			return p;
		}
		else if (pr.getProductID() < p.getRecord().getProductID()) {
			p.left = insert(p.left, pr, flag);
		}
		else  {
			p.right = insert(p.right, pr, flag);
		}
		if (!flag)
			return p;	//inset BST
		else
			return balance(p); //inset AVL
	}

	/*
		delete
	 */

	public  void delete(ProductRecord key) {
		root = delete(root, key);
	}

	private WarehouseNode delete(WarehouseNode p, ProductRecord check) {

		if (p == null) throw new RuntimeException();
		else if (check.getProductID() < p.getRecord().getProductID()) {
			p.left = delete(p.left, check);
		}
		else if (check.getProductID() > p.getRecord().getProductID()) {
			p.right = delete(p.right, check);
		}
		else {
			if (p.left == null)
				return p.getRight();
			else if (p.right == null)
				return  p.getLeft();
			else {
				// get data from the rightmost node in the left subtree
				p.record = retrieveData(p.left);
				//delete the rightmost node in the left subtree
				p.left = delete(p.left, p.getRecord());
 			}
		}
		return  p;
	}

	private ProductRecord retrieveData (WarehouseNode p) {
		while (p.right != null)
			p = p.right;
		return  p.getRecord();
	}

	/*
		search02
		inorder traversal
		using stack: min -> max
		return the most truenode
		>>> use it for the 2nd event.
	 */

	private WarehouseNode search02 (ProductRecord pr) {
		if (root == null)
			return  null;

		WarehouseNode node = root;
		Stack<WarehouseNode> s = new Stack<>();
		WarehouseNode min = new WarehouseNode();
		//min.record = pr;
		//int minVal = Math.abs(root.getRecord().getProductID() - pr.getProductID());
		int minVal = 1000;

		while(node!=null) {
			s.push(node);
			node = node.left;
		}

		while (s.size() > 0) {
			node = s.pop();
			if (Math.abs(node.getRecord().getProductID() - pr.getProductID()) < minVal) {
				min = node;
				minVal = Math.abs(node.getRecord().getProductID() - pr.getProductID());

			}
			if (node.right != null) {
				node = node.right;
				while (node != null) {
					s.push(node);
					node = node.left;
				}
			}
		}
		return  min;

	}


	/*
			getHeight()
	 */

	private int getHeight(WarehouseNode p, int level) {
		if (p == null)
			return 0;

		int uplevel = getHeight(p.left,  level + 1);
		if (uplevel != 0)
			return uplevel + 1;

		uplevel = getHeight(p.right, level+1);
		return  uplevel + 1;
	}

	public  int getHeight(WarehouseNode p) {
		return  getHeight(p, 1);
	}

	/*
		balanceFactor
	 */

	private int balanceFactor(WarehouseNode p) {
		p.setBalance(getHeight(p.left) - getHeight(p.right));
		return p.getBalance();
	}

	/*
		rotate right & left
	 */

	private WarehouseNode rotateRight(WarehouseNode p) {
		WarehouseNode g = p.left;
		p.left = g.right;
		g.right = p;
		return g;
	}

	private WarehouseNode rotateLeft(WarehouseNode p) {
		WarehouseNode g = p.right;
		p.right = g.left;
		g.left = p;
		return g;

	}

	/*
		balance
	 */
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

	/*
		GET depth
	 */

	private int getDepth(WarehouseNode p, ProductRecord pr, int level) {
		if (p == null)
			return 0;
		if (p.getRecord() == pr) //updating: (Objects.equals(p.getRecord().toString(), pr.toString()))
			return level;
		int downlevel = getDepth(p.left, pr, level + 1);
		if (downlevel != 0)
			return downlevel;
		downlevel = getDepth(p.right, pr, level + 1);
		return downlevel;
	}
	public int getDepth(ProductRecord pr) {
		return getDepth(root, pr,1);
	}

	/*
		delete01
		delete all node has depth >= check
	*/

	private WarehouseNode delete01(WarehouseNode p, int check) {

		if (p == null)
			return null;
		if (getDepth(p.getRecord()) >= check)
			p = null;
		else {
			// get data from the rightmost node in the left subtree
			//p.record = retrieveData(p.left);
			p.left = delete01(p.left, check);
			p.right = delete01(p.right, check);
		}
		return  p;
	}

	public  void delete01(int key) {
		root = delete01(root, key);
	}

	/*
		Precedence WareHouseNode
		setToRoot()
		for handling 5th event

	 */

	private WarehouseNode setToRoot(ProductRecord pr, ArrayList<ProductRecord> arr) {
		if (arr.isEmpty()) throw new NullPointerException();
		WarehouseNode p = new WarehouseNode();
		for (int i = 0; i < arr.size(); i++) {
			if (arr.get(i).getProductID() == pr.getProductID()) {
				pr.setQuantity(pr.getQuantity() + arr.get(i).getQuantity());
				arr.remove(i);	//so you don't set the root again
			}
		}
		p.setRecord(pr);
		return p;
	}

	/*
		reversePostOrder
		RLN traversal
		save to an arraylist.
	 */

	private ArrayList<ProductRecord> reversePostOrder(WarehouseNode p) {
		ArrayList<ProductRecord> list = new ArrayList<>();
		Stack<WarehouseNode> s = new Stack<>();

		if (p == null)
			return list;

		s.push(p);
		WarehouseNode prev = null;
		while (!s.isEmpty()) {
			WarehouseNode current = s.peek();

			/* go down the tree in search of a leaf an if
				so process it and pop stack otherwise move down */
			if (prev == null || prev.left == current || prev.right == current) {
				if (current.right != null)
					s.push(current.right);
				else if (current.left != null)
					s.push(current.left);
				else {
					s.pop();
					list.add(current.getRecord());
				}
			}

			else if(current.right == prev) {
				if (current.left != null)
					s.push(current.left);
				else {
					s.pop();
					list.add(current.getRecord());
				}
			}
			else if (current.left == prev) {
				s.pop();
				list.add(current.getRecord());
			}
			prev = current;
		}
		return list;
	}

	/*
		reverse Preorder
		NRL traversal

	 */

	private ArrayList<ProductRecord> reversePreOrder (WarehouseNode p) {
		ArrayList<ProductRecord> list = new ArrayList<>();
		Stack<WarehouseNode> s = new Stack<>();

		if (p == null)
			return list;
		s.push(p);	//can't get it (?!)
		while (!s.isEmpty()) {
			WarehouseNode current = s.peek();
			//System.out.println(current.getRecord() + " "); 	debug
			//list.add(current.getRecord());
			s.pop();

			/* left child is pushed first so that right is processed first */
			if (current.left != null)
				s.push(current.left);
			if (current.right != null)
				s.push(current.right);

			list.add(current.getRecord());
			//s.pop();
		}
		return list;
	}

	/* private ArrayList reversePreOrder() {
		return reversePreOrder(root);
	} */

	/*
		inOrder()
		LNR traversal
		TEST LATER!!!!!
		get an array: min -> max
	 */
	private ArrayList<ProductRecord> inOrder(WarehouseNode p) {
		ArrayList<ProductRecord> list = new ArrayList<>();
		Stack<WarehouseNode> s = new Stack<>();

		if (p == null)
			return list;
		while (p != null) {
			s.push(p);
			p = p.left;
		}

		while (!s.isEmpty()) {
			p = s.pop();
			list.add(p.getRecord());
			if (p.right != null) {
				p = p.right;

				while (p != null) {
					s.push(p);
					p = p.left;
					//list.add(p.getRecord());
				}
			}
		}
		return list;
	}

	/*
		handle 1st event
	 */
	public void handle01 (String s) { 	//1XXXY
		int id = Integer.parseInt(s.substring(1,4));
		int quanity = Integer.parseInt(s.substring(4,s.length()));
		ProductRecord pr = new ProductRecord(id, quanity);
		//WarehouseNode p = new WarehouseNode();
		//System.out.print(id + "\n" + quanity); 	//debug

		if(search(id) == null) {
			insert(pr, false);
		}
		else {
			//p = search(id);
			search(id).getRecord().setQuantity(search(id).getRecord().getQuantity() + quanity);
		}
	}

	/*
		handle 2nd event
	 */
	public void handle02 (String s) { 	//2XXXY
		int id = Integer.parseInt(s.substring(1,4));
		int quanity = Integer.parseInt(s.substring(4,s.length()));
		ProductRecord check = new ProductRecord(id, quanity);

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

	/*
		handle 3rd event
	 */
	public void handle03 () {
		ArrayList<ProductRecord> arrlist = inOrder(root);
		int mid = arrlist.size()/2;

		//System.out.println(arrlist.get(mid).toString());	//debug
		//System.out.println(mid);	//debug
		while (root != null)
			delete(root.getRecord());

		WarehouseNode p = new WarehouseNode();
		p.setRecord(arrlist.get(mid));
		WarehouseNode l = null;
		WarehouseNode r = null;
		//arrlist.remove(mid);
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

	/*
		handle 4th event
	 */
	public void handle04() {
		ArrayList<ProductRecord> arrlist = reversePreOrder(root);
		while (root != null)
			delete(root.getRecord());

		for (int i = 0; i < arrlist.size(); i++)
			insert(arrlist.get(i), true);

	}

	/*
		handle 5th event
	 */
	public void handle05 (String s) {
		ArrayList<ProductRecord> arrlist = reversePostOrder(root);

		int id = Integer.parseInt(s.substring(1,4));
		int quanity = Integer.parseInt(s.substring(4,s.length()));
		ProductRecord pr = new ProductRecord(id, quanity);

		while (root != null)
			delete(root.getRecord());

		//System.out.println(arrlist);	//debug
		root = setToRoot(pr, arrlist);
		//System.out.println(arrlist);	//debug

		for (int i = 0; i < arrlist.size(); i++)
			insert(arrlist.get(i), false);
			//arrlist.remove(i);

	}

	/*
		handle 6th event
	 */

	public void handle06 (String s) {
		int depth = Integer.parseInt(s.substring(1,s.length()));
		if (depth == 0)
			root = null;
		else
			delete01(depth);
			//System.out.println(p.record.toString());
	}

	@Override
	public String toString() {
		//sinh vien viet ma tai day
		//System.out.println(toString(root));		//debug
		if (root == null)
			return  "";
		else
			return toString(root);
	}

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



		//System.out.println(h + " ) ");		//debug
		return h;

	}

	public static void main(String[] args){
		//vi du ham main de chay
		try{
			WarehouseBook wb = new WarehouseBook(new File("warehouse.txt"));
			wb.process(new File("events.txt"));
			//System.out.print(wb.getDepth(wb.root.left.getRecord()));		//debug
			//wb.handle06("63");		//debug
			//System.out.println(wb.reversePostOrder(wb.root));	//debug
			//wb.handle05("57191"); 		//debug
			//System.out.println(wb.reversePreOrder(wb.root)); 	//debug
			//wb.handle04();
			//System.out.println(wb.inOrder(wb.root));	//debug
			//wb.handle03();	//debug
			wb.save(new File("warehouse_new.txt"));

			//System.out.println(wb.getHeight(wb.root)); 	//debug
		}catch(Exception ex){
			ex.printStackTrace();
		}

	}
}