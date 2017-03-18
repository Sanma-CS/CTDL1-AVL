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
	private int size;
	
	public int getSize(){
		return size;
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
	
	public void process(File file) throws IOException{
		//sinh vien viet ma tai day
	}
	
	public void process(List<String> events){
		//sinh vien viet ma tai day
	}


	@Override
	public String toString(){
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
		}catch(Exception ex){
			ex.printStackTrace();
		}

	}
}