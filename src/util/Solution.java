package util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public abstract class Solution<S> {
	protected ArrayList<Integer> id = new ArrayList<Integer>();
	protected ArrayList<String> algorithmName = new ArrayList<String>();
	protected ArrayList<String> instanceType = new ArrayList<String>();
	protected ArrayList<Long> times = new ArrayList<Long>();
	protected ArrayList<S> solutions = new ArrayList<S>();
	protected String delim = ",";
	
	public void write(Path path) throws IOException {
		ArrayList<String> lines = new ArrayList<String>();
		lines.add(header());
		for(int i = 0; i < solutions.size(); ++i)
			lines.add(line(i));

		String solFolder = path.toAbsolutePath().getParent().getFileName().toString() + "/";
		if(!Files.exists(path.toAbsolutePath().getParent()))
		    throw new IOException(Prompt.getFolderNotFoundMessage(solFolder));

		if(Files.exists(path)) {
		    try {
                Files.delete(path);
            } catch (IOException e) {
                throw new IOException(Prompt.getFileNotFoundMessage(solFolder + path.getFileName()));
            }
        }

        try {
            Files.write(path, lines);
        } catch (IOException e) {
            throw new IOException(Prompt.getFileNotFoundMessage(solFolder + path.getFileName()));
        }

		System.out.println("Saving Solution in:\n " + path.toAbsolutePath());
	}
	
	public boolean addSolution(Integer instanceNumber, String aName, String iType, long time, S values, S solution) {
	    boolean ret = true;
		if(!check(values, solution))
			ret = false;
		
		id.add(instanceNumber);
		algorithmName.add(aName);
		instanceType.add(iType);
		times.add(time);
		solutions.add(values);

		return ret;
	}
	
	public String toString() {
		String text = "";
		for(int i = 0; i < solutions.size(); ++i)
			text += line(i) + "\n";
		
		return text;
	}
	
	public abstract boolean check(S values, S solution);
	
	public void clear() {
		id.clear();
		algorithmName.clear();
		instanceType.clear();
		times.clear();
		solutions.clear();
	}

	public int numSolutions() {return solutions.size();}
	
	protected String header() {
		String head = "";
		head += "id" + delim + "name" + delim + "instance_type" + delim + "ns";
		return head;
	}
	
	protected String line(Integer i) {
		String line = "";
		line += id.get(i) + delim;
		line += algorithmName.get(i) + delim;
        line += instanceType.get(i) + delim;
		line += times.get(i);
		return line;
	}

	public abstract String solutionToString(int id);
}
