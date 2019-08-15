package product.store.exceptions;

public class InvalidProductDetailsException extends RuntimeException {

    public InvalidProductDetailsException() {
        super(ErrorMessages.INVALID_PRODUCT_EXCEPTION);
    }


    public InvalidProductDetailsException(Throwable ex) {
        super(ErrorMessages.INVALID_PRODUCT_EXCEPTION, ex);
    }
}
