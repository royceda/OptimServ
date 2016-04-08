package com.parser;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;

import com.bean.*;


public class Parser {
	private int n;
	private int R;
	private int S;
	private int us;
	private int p;
	private ArrayList<Server> servers;
	private HashMap<Integer, Row> rows;
	
	
	public Parser(String file) throws NumberFormatException, IOException{
		InputStream fis = new FileInputStream(file);
		InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
		BufferedReader br = new BufferedReader(isr);
		String line = "";
		
		servers = new ArrayList<Server>(); 
		rows = new HashMap<Integer, Row>();
		
		//first line and nb of cases
		if((line = br.readLine()) != null){
			String var[] = line.split(" ");
			//rows, slots, unavaible slots, pools, servers 
			
			R  = Integer.parseInt(var[0]);
			S  = Integer.parseInt(var[1]);
			us = Integer.parseInt(var[2]);
			p  = Integer.parseInt(var[3]);
			n  = Integer.parseInt(var[4]);
		}
		
		//init rows
		for(int i = 0; i<R; i++){
			Row tmp = new Row(i,S);
			rows.put(i, tmp);
		}
		
		
		//loop for unvailable slots
		for(int i=0; i<us; i++){
			if((line = br.readLine()) != null){
				String var[] = line.split(" ");
				rows.get(Integer.parseInt(var[0])).setUnavailable(Integer.parseInt(var[1]));
			}
		}
		
		
		//Servers
		for(int i=0; i<n; i++){
			if((line = br.readLine()) != null){
				String var[] = line.split(" ");
				Server tmp = new Server(i, Integer.parseInt(var[0]), Integer.parseInt(var[1]));
				servers.add(tmp);
			}
		}
	}
}