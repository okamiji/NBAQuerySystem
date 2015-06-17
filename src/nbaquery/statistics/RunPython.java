package nbaquery.statistics;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class RunPython {
	public static void run_python(String file_path){
		try {
			Process pr = Runtime.getRuntime().exec("python " + file_path);  
			BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				System.out.println(line);
			}
			in.close();
			pr.waitFor();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void run_python(String file_path, String... argv){
		try {
			String command = "python " + file_path;
			for(String s : argv){
				command += " " + s;
			}
//			System.out.println(command);
			
			Process pr = Runtime.getRuntime().exec(command);  
			BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				System.out.println(line);
			}
			pr.waitFor();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
