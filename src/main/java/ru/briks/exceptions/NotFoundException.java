package ru.briks.exceptions;

/**
 * @author EGlushkov
 * Date: 12.05.2026
 * Time: 14:02
 */

public class NotFoundException extends RuntimeException{
    public NotFoundException(String message) {
        super(message);
    }
}
