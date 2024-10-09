package exception;

public class AcaoNotFoundException extends RuntimeException {
 
	private static final long serialVersionUID = 1L;

	public AcaoNotFoundException(Long id) {
        super("Ação não encontrada: " + id);
    }
}