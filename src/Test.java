import exercise.DijkstraExercise;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class Test {
	public static void main(String[] args) {
		System.out.println("Efficient Dijkstra");
		System.out.println("");

		Path instances = FileSystems.getDefault().getPath("instances/");
		Path solutions = FileSystems.getDefault().getPath("solutions/");

		DijkstraExercise exercise = new DijkstraExercise(instances, solutions);
		try {
			exercise.run();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
