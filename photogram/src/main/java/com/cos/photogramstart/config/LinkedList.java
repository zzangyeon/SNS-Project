package com.cos.photogramstart.config;

import java.util.Iterator;

public class LinkedList {
	
	Node head;
	Node tail;
	int size = 0;
	
	public class Node{
		
		Object data;
		Node next;
		
		public Node(Object data) {
			this.data = data;
		}
	}
	
	public void addFirst(Object input) {
		Node newNode = new Node(input);
		newNode.next = head;
		head = newNode;
		size++;
		if(head.next == null) {
			tail = head;
		}
	}
	
	public void addLast(Object input) {
		Node newNode = new Node(input);
		if(head == null) {
			addFirst(input);
		}else {
			tail.next = newNode;
			tail = newNode;
			size++;
		}
	}
	Node node(int index) {//내부적인것이라 외부에서 사용하면 안된다!
		//head노드를 우선 알아야한다!
		Node x = head;
		
		for (int i = 0; i < index; i++) {
			x = x.next;
		}
		return x;
	}
	public void add(int index, Object data) {
		if(index == 0) {
			addFirst(data);
		}else {
			Node temp1 = node(index-1);
			Node temp2 = temp1.next;
			Node newNode = new Node(data);
			temp1.next = newNode;
			newNode.next = temp2;
			size++;
			if(temp2 == null) {
				tail = newNode;
			}
		}
	}
	public String toString() {
		if(head == null) {
			return "[]";
		}
		Node temp = head;
		String str = "[";
		while(temp.next != null) {
			str += temp.data+", ";
			temp = temp.next;
		}
		str += temp.data;
		return str+"]";
	}
	
	public static void main(String[] args) {
		LinkedList numbers = new LinkedList();
		numbers.addFirst(10);
		numbers.addLast(20);
		numbers.addLast(30);
		numbers.add(2, 111);
		
		
		System.out.println(numbers);
		
		System.out.print("hello");
	}
}












