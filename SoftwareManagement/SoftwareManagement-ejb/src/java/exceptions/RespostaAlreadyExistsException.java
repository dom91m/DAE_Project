package exceptions;

public class RespostaAlreadyExistsException extends Exception {

    public RespostaAlreadyExistsException() {
    }

    public RespostaAlreadyExistsException(String msg) {
        super(msg);
    }
}
