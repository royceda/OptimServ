import java.io.IOException;

import com.parser.Parser;
import com.solver.Solver;

public class Main {

	public static void main(String[] args) throws NumberFormatException, IOException {
		// TODO Auto-generated method stub
		
		Parser parse = new Parser("./input/server.in");
		Solver sol = new Solver(parse);
		sol.solve();
		System.out.println("ok");
	}
}
