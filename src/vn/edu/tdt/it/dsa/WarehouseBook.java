package vn.edu.tdt.it.dsa;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
//import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;
import java.util.Objects;


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

	public WarehouseBook(File file) throws IOException {
		//sinh vien viet ma tai day
		ArrayList<String> list = new ArrayList<String>();
		list = convertList(file);
		root = read(list);
		//System.out.println(root.getRecord()); 	//debug

	}

	/*
		get String from input file into a List<String>
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
			p.record = pr;

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

	public void process(File file) throws IOException {
		//sinh vien viet ma tai day
	}

	public void process(List<String> events) {
		//sinh vien viet ma tai day
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
			return p;
		else
			return balance(p);
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
		Stack<WarehouseNode> s = new Stack<WarehouseNode>();
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
		search03

			private WarehouseNode search03(WarehouseNode p, int key) {
		if (p == null)
			return null;
		if (getDepth(p.getRecord()) >= key)
			return p;
		else {
			//WarehouseNode t = search(p.right, key);
			if (p.left == null)
				return search03(p.right, key);
			return search03(p.left, key);
		}
	}
	 */
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

	private void setToRoot(ProductRecord pr, ArrayList<WarehouseNode> arr) {
		for (int i = 0; i < arr.size(); i++) {
			if (arr.get(i).getRecord().getProductID() == pr.getProductID())
				pr.setQuantity(pr.getQuantity() + arr.get(i).getRecord().getQuantity());
		}
		root.setRecord(pr);
	}

	/*
		handle 1st event
	 */

	public void handle01 (String s) { 	//1XXXY
		int id = Integer.parseInt(s.substring(1,4));
		int quanity = Integer.parseInt(s.substring(4,s.length()));
		ProductRecord pr = new ProductRecord(id, quanity);
		//WarehouseNode p = new WarehouseNode();
		System.out.print(id + "\n" + quanity); 	//debug

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
			wb.save(new File("warehouse_new.txt"));

			//System.out.println(wb.getHeight(wb.root)); 	//debug
			//wb.handle01(""); //debug
		}catch(Exception ex){
			ex.printStackTrace();
		}

	}
}