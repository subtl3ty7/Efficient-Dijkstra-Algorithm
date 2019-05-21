package util;

//This is just a base class to derive the actual algorithm classes and make sure they get a name.
public abstract class Algorithm {
	protected abstract String name();

	static public class AlgorithmErrorException extends Exception {
		public AlgorithmErrorException(int id, String out) {
			super(Prompt.getErrorMessage("Instance "  + id + " didn't get solved correctly! Your solution:\n" + out));
        }
	}
}
