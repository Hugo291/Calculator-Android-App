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
        public static final int LEVEL_1_THRESHOLD = 10;
        public static final int LEVEL_2_THRESHOLD = 20;
        public static final int LEVEL_3_THRESHOLD = 35;
        public static final int LEVEL_4_THRESHOLD = 60;
        public static final int LEVEL_5_THRESHOLD = 80;
        public static final int LEVEL_6_THRESHOLD = 100;

        public static final int LEVEL_1_MAX_VALUE = 15;
        public static final int LEVEL_2_MAX_VALUE = 50;
        public static final int LEVEL_3_MAX_VALUE = 100;
        public static final int LEVEL_4_MAX_VALUE = 120;
        public static final int LEVEL_5_MAX_VALUE = 150;
        public static final int LEVEL_6_MAX_VALUE = 200;
        public static final int LEVEL_7_MAX_VALUE = 300;

        private Level() {} // Prevent instantiation
    }

    /**
     * Game settings
     */
    public static final class Game {
        public static final int MAX_ANSWER_LENGTH = 3;
        public static final int VICTORY_ROUND_COUNT = 15;
        public static final int INITIAL_TIMER_SECONDS = 10;
        public static final int TIMER_BONUS_SECONDS = 10;

        private Game() {} // Prevent instantiation
    }

    /**
     * Animation durations (in milliseconds)
     */
    public static final class Animation {
        public static final int BUTTON_PRESS_DURATION = 100;
        public static final int BUTTON_RELEASE_DURATION = 300;
        public static final int ZOOM_IN_DURATION = 200;

        private Animation() {} // Prevent instantiation
    }

    private GameConfig() {} // Prevent instantiation of main class
}