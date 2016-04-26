package com.bean;

import java.util.Arrays;

import com.parser.Parser;

public class Row {
	private int id;
	private int size;
	private boolean slots[];

	public Row(){}
	
	
	
	public Row(int id, int size){
		this.setId(id);
		this.size = size;
		slots = new boolean[size];
		Arrays.fill(slots, true);
	}
	
	
	public void setUnavailable(int n){
		slots[n] = false;
	}
	
	public boolean isAvailable(int n){
		return slots[n];
	}
	
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public boolean[] getSlots() {
		return slots;
	}
	public void setSlots(boolean slots[]) {
		this.slots = slots;
	}



	public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
	}
	
	
}
