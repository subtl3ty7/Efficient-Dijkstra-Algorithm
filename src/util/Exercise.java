package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.cert.CollectionCertStoreParameters;
import java.util.ArrayList;
import java.util.Collections;

//This class takes care of managing the input and output directories and calls the algorithm
public abstract class Exercise {
	protected Path instancesBase;
	protected Path solutionsBase;

	public abstract String  name();
	public abstract void run() throws IOException;
	protected abstract void runWithFile(BufferedReader reader, Solution sol) throws IOException, Algorithm.AlgorithmErrorException;

	public void setInstancesBase(Path p) {instancesBase = p;}
	public void setSolutionsBase(Path p) {solutionsBase = p;}
	public void pathsFromPackageName(Path iPath, Path sPath, String pName) {
		String name = pName.split("\\.")[0];
		setInstancesBase(Paths.get(iPath.getFileName().toString(), name));
		setSolutionsBase(Paths.get(sPath.getFileName().toString(), name));
	}

	protected void promptInstances(Solution sol, Path instancesBase, Path solutionsBase, String filter) throws IOException {
		ArrayList<Path> instances = new ArrayList<Path>();

		try {
			Files.walk(instancesBase).forEach(f -> {
				if (f.getFileName().toString().endsWith(filter))
					instances.add(f);
			});
		} catch(IOException e) {
			throw new IOException(Prompt.getFolderNotFoundMessage(instancesBase.getFileName().toString() + "/"));
		}

        Collections.sort(instances);
		while(true) {
			int choice = Prompt.promptPaths("These data are available to choose:", instances);
			
			if(Prompt.terminate(choice))
				return;
			
			sol.clear();
            BufferedReader reader = null;
            Path fpath = instances.get(choice - 1);
			try {
			    reader = Files.newBufferedReader(fpath);
				runWithFile(reader, sol);
			} catch (Algorithm.AlgorithmErrorException e) {
				System.out.println(e.getMessage());
				System.out.println();
				continue;
			} catch (IOException e) {
				throw new IOException(Prompt.getFileNotFoundMessage(instancesBase.getFileName().toString() + "/" + fpath.getFileName()));
			} finally {
			    if(reader != null)
			        reader.close();
            }

			Path solutionPath = Paths.get(solutionsBase.toString(), instances.get(choice - 1).getFileName().toString());
            sol.write(solutionPath);
            System.out.println();
		}	
	}
}
