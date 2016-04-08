package com.bean;

public class Server {
	private int id;
	private int capacity;
	private int size;
	
	
	public Server(){}
	
	public Server(int id, int cap, int size){
		this.id = id;
		this.setCapacity(cap);
		this.setSize(size);
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
	
}
