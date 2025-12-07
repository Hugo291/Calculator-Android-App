package com.pixelma.calculator.Utils;

/**
 * Centralized configuration for game constants
 * Eliminates magic numbers throughout the codebase
 */
public class GameConfig {

    /**
     * Operator constants
     */
    public static final class Operators {
        public static final int PLUS = 0;
        public static final int MINUS = 1;
        public static final int MULTIPLY = 2;
        public static final int DIVIDE = 3;
        public static final int RANDOM = 4;
        public static final int ALL = 5;

        private Operators() {} // Prevent instantiation
    }

    /**
     * Level configuration
     */
    public static final class Level {

        private Level() {} // Prevent instantiation
    }

    /**
     * Game settings
     */
    public static final class Game {
        public static final int MAX_ANSWER_LENGTH = 3;
        public static final int VICTORY_ROUND_COUNT = 1;
        public static final int TIMER_BONUS_SECONDS = 10;

        private Game() {} // Prevent instantiation
    }

    private GameConfig() {} // Prevent instantiation of main class
}