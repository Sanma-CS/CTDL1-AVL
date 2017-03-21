package vn.edu.tdt.it.dsa;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


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
		String content = sc.useDelimiter("\\A").next();
		sc.close();
		String[] cv = content.split("\\s+");
		ArrayList<String> list = new ArrayList<>(Arrays.asList(cv));
		//System.out.println(list);	//debug
		return list;

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
		if (p.record == null)
			return null;
		else if (key == p.record.getProductID()) {
			return p;
		}
		else if (key < p.record.getProductID()){
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

	public void insert (int ID, int quanity) {
		root = insert(root, ID, quanity);
	}

	private WarehouseNode insert (WarehouseNode p, int ID, int quanity) {
		WarehouseNode n = new WarehouseNode();
		ProductRecord pr = new ProductRecord(ID, quanity);
		if (p.record == null) {
			n.record = pr;
			return n;
		}
		else if (ID == p.record.getProductID()) {
			return p;
		}
		else if (ID < p.record.getProductID()) {
			p.left = insert(p.left, ID, quanity);
		}
		else  {
			p.right = insert(p.right, ID, quanity);
		}
		return  p;
	}

	/*
		handle 1st event
	 */

	public void handle01 (String s) {
		int id = Integer.parseInt(s.substring(1,4));
		int quanity = Integer.parseInt(s.substring(4,s.length()));
		//WarehouseNode p = new WarehouseNode();
		//System.out.print(id + "\n" + quanity); 	//debug

		if(search(id) == null) {
			insert(id, quanity);
		}
		else {
			//p = search(id);
			search(id).record.setQuantity(search(id).record.getQuantity() + quanity);
		}
	}

	@Override
	public String toString() {
		//sinh vien viet ma tai day
		//System.out.println(toString(root));		//debug
		if (root.record == null)
			return  null;
		else
			return toString(root);
	}

	private String toString(WarehouseNode p) {
		String h = "";

		if (p == null) {
			h += "N";
		}
		else {
			h += p.record.toString();

			if (p.left != null || p.right!= null) {
				h += " ( ";
				h += toString(p.left) + " ";
				h += toString(p.right);
				h += " )";
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
			wb.save(new File("warehouse_new.txt"));

			//System.out.println(wb.getSize()); 	//debug
			//wb.handle01("17234"); //debug
		}catch(Exception ex){
			ex.printStackTrace();
		}

	}
}