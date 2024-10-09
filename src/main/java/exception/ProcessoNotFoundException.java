package exception;

public class ProcessoNotFoundException  extends RuntimeException{
	 
	private static final long serialVersionUID = 1L;

	public ProcessoNotFoundException(Long id) {
	        super("Processo não encontrado: " + id);
	    }
	}

	

	
