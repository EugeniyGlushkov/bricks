package ru.bricks;

import org.slf4j.Logger;

import java.util.Properties;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author: eglushkov
 */
public class Main {
    public static final Logger log = getLogger(Main.class);

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            log.debug("Debug in main method - " + i);
        }
    }
}
