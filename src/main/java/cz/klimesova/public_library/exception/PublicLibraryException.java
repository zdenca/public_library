package cz.klimesova.public_library.exception;

/**
 * Created by Zdenca on 6/5/2017.
 */
public class PublicLibraryException extends RuntimeException {
    public PublicLibraryException() {
    }

    public PublicLibraryException(String message) {
        super(message);
    }

    public PublicLibraryException(String message, Throwable cause) {
        super(message, cause);
    }

    public PublicLibraryException(Throwable cause) {
        super(cause);
    }

    public PublicLibraryException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
