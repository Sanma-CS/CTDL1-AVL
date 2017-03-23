package vn.edu.tdt.it.dsa;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
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
		if (p.getRecord() == null)
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

	public void insert (ProductRecord pr) {
		root = insert(root, pr);
	}

	private WarehouseNode insert (WarehouseNode p, ProductRecord pr) {
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
			p.left = insert(p.left, pr);
		}
		else  {
			p.right = insert(p.right, pr);
		}
		return p;
	}

	/*
		delete
	 */

	public  void delete(ProductRecord check) {
		root = delete(root, check);
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
			
	 */


	/*
		handle 1st event
	 */

	public void handle01 (String s) {
		int id = Integer.parseInt(s.substring(1,4));
		int quanity = Integer.parseInt(s.substring(4,s.length()));
		ProductRecord pr = new ProductRecord(id, quanity);
		//WarehouseNode p = new WarehouseNode();
		System.out.print(id + "\n" + quanity); 	//debug

		if(search(id) == null) {
			insert(pr);
		}
		else {
			//p = search(id);
			search(id).getRecord().setQuantity(search(id).getRecord().getQuantity() + quanity);
		}
	}

	@Override
	public String toString() {
		//sinh vien viet ma tai day
		//System.out.println(toString(root));		//debug
		if (root.getRecord() == null)
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
			h += p.getRecord().toString();

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
//			wb.process(new File("events.txt"));
			wb.save(new File("warehouse_new.txt"));

			//System.out.println(wb.getSize()); 	//debug
			//wb.handle01(""); //debug
		}catch(Exception ex){
			ex.printStackTrace();
		}

	}
}