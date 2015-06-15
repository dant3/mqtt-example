package com.github.dant3.mqttexample.utils.avatars;

public final class MaterialPalette {
    public static final int RED = 0xfff44336;
    public static final int PINK = 0xffe91e63;
    public static final int PURPLE = 0xff9c27b0;
    public static final int DEEP_PURPLE = 0xff673ab7;

    public static final int INDIGO = 0xff3f51b5;
    public static final int BLUE = 0xff2196f3;
    public static final int LIGHT_BLUE = 0xff03a9f4;
    public static final int CYAN = 0xff00bcd4;
    public static final int TEAL = 0xff009688;

    public static final int GREEN = 0xff4caf50;
    public static final int LIGHT_GREEN = 0xff8bc34a;
    public static final int LIME = 0xffcddc39;
    public static final int YELLOW = 0xfffbc02d; // dark variant
    public static final int AMBER = 0xffffc107;

    // ---
    public static final int ORANGE = 0xffff9800;
    public static final int DEEP_ORANGE = 0xffff5722;
    public static final int BROWN = 0xff795548;
    public static final int GREY = 0xff9e9e9e;
    public static final int BLUE_GREY = 0xff607d8b;

    public static final int[] ALL_COLORS = new int[] {
            RED, PINK, PURPLE, DEEP_PURPLE,
            INDIGO, BLUE, LIGHT_BLUE, CYAN, TEAL,
            GREEN, LIGHT_GREEN, LIME, YELLOW, AMBER,
            ORANGE, DEEP_ORANGE, BROWN, GREY, BLUE_GREY
    };

    private MaterialPalette() { throw new UnsupportedOperationException(); }
}
