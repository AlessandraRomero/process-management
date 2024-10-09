package exception;

public class ParteNotFoundException extends RuntimeException {
  
	private static final long serialVersionUID = 1L;

	public ParteNotFoundException(Long id) {
        super("Parte não encontrada: " + id);
    }
}