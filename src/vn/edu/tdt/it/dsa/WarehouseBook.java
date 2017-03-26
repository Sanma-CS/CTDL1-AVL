package vn.edu.tdt.it.dsa;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static java.lang.Math.abs;


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

	private int size;

    public int getSize(){
		return size;
	}

    public WarehouseBook(){
		root = null;
		size = 0;
	}

	public WarehouseBook(File file) throws IOException {
		ArrayList<String> list;
		list = toList(file);
		root = readList(list);
	}

	/*
		get String from input file into a List<String>
	 */
	private ArrayList<String> toList(File file) throws  IOException {
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
		return result;
	}

	/*
		read list into BSTree
		UPDATE SIZE LATER
	 */
	private WarehouseNode readList(ArrayList<String> list) {
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
				p.setLeft(readList(list));
				p.setRight(readList(list));
				list.remove(0);
			}
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
        process(toList(file));
	}

	public void process(List<String> events) {
        for (String event : events) {
            switch (event.charAt(0)) {
                case '0':
                    return;
                case '1':
                    handle01(event);
                    break;
                case '2':
                    handle02(event);
                    break;
                default:
                    return;
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
		if (p.getRecord() == null)
			return null;
		else if (key == p.getRecord().getProductID()) {
			return p;
		}
		else if (key < p.getRecord().getProductID()){
			return search(p.getLeft(), key);
		}
		else  {
			return search(p.getRight(), key);
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
			p.setLeft(insert(p.getLeft(), pr));
		}
		else  {
			p.setRight(insert(p.getRight(), pr));
		}
		return p;
	}

	public void delete(ProductRecord key) {
        root = delete(root, key, 1000);
    }

	private WarehouseNode delete(WarehouseNode p, ProductRecord check, int minVal) {

		if (p == null) throw new RuntimeException();
		else if (check.getProductID() < p.getRecord().getProductID() && abs(check.getProductID() - p.getRecord().getProductID()) < minVal) {
            minVal = abs(check.getProductID() - p.getRecord().getProductID());
			p.setLeft(delete(p.getLeft(), check, minVal));
		}
		else if (check.getProductID() > p.getRecord().getProductID() && abs(check.getProductID() - p.getRecord().getProductID()) < minVal) {
            minVal = abs(check.getProductID() - p.getRecord().getProductID());
			p.setRight(delete(p.getRight(), check, minVal));
		}
		else {
			if (p.getLeft() == null)
				return p.getRight();
			else if (p.getRight() == null)
				return  p.getLeft();
			else {
				// get data from the rightmost node in the left subtree
				p.record = retrieveData(p.getLeft());
				//delete the rightmost node in the left subtree
                p.setLeft(delete(p.getLeft(), p.getRecord(), minVal));
 			}
		}
		return  p;
	}

	private ProductRecord retrieveData (WarehouseNode p) {
		while (p.getRight() != null)
			p = p.getRight();
		return  p.getRecord();
	}

	/*
		handle 1st event
	 */

	public void handle01 (String s) {
		ProductRecord pr = new ProductRecord(Integer.parseInt(s.substring(1,4)),
                Integer.parseInt(s.substring(4,s.length())));

		if(search(pr.getProductID()) == null) {
			insert(pr);
		}
		else {
			search(pr.getProductID()).getRecord().setQuantity(
			        search(pr.getProductID()).getRecord().getQuantity() +
                    pr.getQuantity());
		}
	}

	public void handle02 (String s) {
		delete(new ProductRecord(Integer.parseInt(s.substring(1,4)),
                Integer.parseInt(s.substring(4,s.length()))));
	}

	@Override
	public String toString() {
		if (root.getRecord() == null)
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

			if (p.getLeft() != null || p.getRight()!= null) {
				h += " ( ";
				h += toString(p.getLeft()) + " ";
				h += toString(p.getRight());
				h += " )";
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