package utils;

public enum HttpStatus {
    OK(200),
    POST_INFORMATION_IS_CORRECT(201),
    NOT_FOUND(404);
    private final int code;

    HttpStatus(int code) {
        this.code = code;
    }
    public int getCode() {
        return code;
    }
}
