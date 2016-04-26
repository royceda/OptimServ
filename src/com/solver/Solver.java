package com.solver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

import com.bean.Row;
import com.bean.Server;
import com.parser.Parser;

import ilog.concert.IloColumnArray;
import ilog.concert.IloException;
import ilog.concert.IloMPModeler;
import ilog.concert.IloNumVar;
import ilog.concert.IloNumVarType;
import ilog.concert.IloRange;
import ilog.cplex.IloCplex;



public class Solver{

	private int n; //num of items
	private int m; //num of bin

	private double bin[]; //size of bins
	private double item[]; //size of items
	private double power[]; //power of item

	//private int x[][]; 
	//private int y[][];


	public Solver(){}
	
	/**
	 * Transform into bin-packing variables
	 * @param servers
	 * @param rows
	 */
	public Solver(Parser parse){
		
		ArrayList<Server> servers = parse.getServers();
		HashMap<Integer, Row> rows = parse.getRows();
		
		n = servers.size();
		
		item = new double[servers.size()];
		power = new double[servers.size()];
		int k = 0;
		for(Iterator<Server> ite = servers.iterator(); ite.hasNext(); ){
			Server tmp = ite.next();
			item[k] = tmp.getSize();
			power[k] = tmp.getCapacity();
			k++;
		}
		
		k = 0;
		for(Iterator<Integer> ite = rows.keySet().iterator(); ite.hasNext();){
			int tmp = ite.next();
			Row current = rows.get(tmp);
			
			ArrayList<Integer> buffer = new ArrayList<Integer>();
			
			//creation of bins
			int j = 0;
			for(int i = 0; i<current.getSize(); i++){
				if(current.getSlots()[i]){
					buffer.add(i-j+1);
					j = i;
					k++;
				}
			}
			
			m = buffer.size();
			bin = new double[buffer.size()];
			int i = 0;
			for(int a : buffer){
				bin[i] = a;
				i++;
			}			
		}
	}

	
	public void solve(){
		try {
			IloCplex cplex = new IloCplex();
			
			IloNumVar[][] varX = new IloNumVar[n][];
			IloNumVar[][] varY = new IloNumVar[m][];
			IloRange[][]  rng = new IloRange[3][]; //3 constraints group
			
			populateByRow(cplex, varX, varY, rng);

			if ( cplex.solve() ) {
				double[] x     = cplex.getValues(varX[0]);
				double[] y     = cplex.getValues(varY[0]);
				double[] slack = cplex.getSlacks(rng[0]);
				
				System.out.println("Solution status = " + cplex.getStatus());
				System.out.println("Solution value  = " + cplex.getObjValue());

				 
				for (int j = 0; j < x.length; ++j) {
					System.out.println("Variable " + j + ": Value = " + x[j]);
				}
				for (int i = 0; i < slack.length; ++i) {
					System.out.println("Constraint " + i + ": Slack = " + slack[i]);
				}
				
				cplex.exportModel("mipex1.lp");
				cplex.end();
			}

			cplex.exportModel("mipex1.lp");
			cplex.end();

		}
		catch (IloException e) {
			System.err.println("Concert exception caught '" + e + "' caught");
		}
	}


	void populateByRow (IloMPModeler  model, IloNumVar[][] varX, IloNumVar[][] varY, IloRange[][]  rng) throws IloException {
	
		//The item i is in the bin j: x[i][j]
		double[] xlb = new double[n]; Arrays.fill(xlb, 0.0);
		double[] xub = new double[n]; Arrays.fill(xub, 1.0);
		
		IloNumVarType[] xt  = new IloNumVarType[n]; Arrays.fill(xt, IloNumVarType.Int);
		IloNumVar[][]   x   = new IloNumVar[n][n];

		for(int i = 0; i<n; i++){
			x[i] = model.numVarArray(n, xlb, xub, xt);
			varX[i] = x[i];
		}
		
		
		//The item i is in the pool p : y[i][p]
		double[] ylb = new double[m]; Arrays.fill(ylb, 0.0);
		double[] yub = new double[m]; Arrays.fill(yub, 1.0);
		
		IloNumVarType[] yt  = new IloNumVarType[n]; Arrays.fill(yt, IloNumVarType.Int);
		IloNumVar[][]   y   = new IloNumVar[n][n];

		for(int i = 0; i<n; i++){
			y[i] = model.numVarArray(n, ylb, yub, yt);
			varY[i] = y[i];
		}
		

		// Objective Function:  maximize 5*x0 + 75*x1 + 25*x2 
		//double[] objvals = {5.0, 75.0, 25.0};
		model.addMinimize(model.scalProd(xub, x[0])); // à bien faire ( bcp de max et min)


		// (KP) constraint
		rng[0] = new IloRange[m];
		double[] line1 = new double[n]; Arrays.fill(line1, 1);
		for(int j = 0; j<m; j++){
			rng[0][j] = model.addLe(model.scalProd(item, x[j]), bin[j]); // !! pas logique   
		}

		// sum(x[i][j], j) = 1 forall i  
		rng[1] = new IloRange[n];
		double[] line2 = new double[n]; Arrays.fill(line2, 1.0);
		for(int i = 0; i<n; i++){
			rng[0][1] = model.addEq(model.scalProd( line2, x[i]), 1.0);
		}
		
		
		// sum(y[i][p], p) = 1 forall i
		rng[2] = new IloRange[n];
		double[] line3 = new double[n]; Arrays.fill(line3, 1.0);	
		for(int i = 0; i<n; i++){
			rng[0][2] = model.addEq(model.scalProd( line2, y[i]), 2.0);
		}
	}	


	
	
}
