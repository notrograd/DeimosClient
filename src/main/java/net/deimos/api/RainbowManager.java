package net.deimos.api;

import java.awt.*;

public class RainbowManager {
    private long startTime;
    private int red, green, blue;
    private static final int CYCLE_DURATION = 6500;

    public RainbowManager() {
        this.startTime = System.currentTimeMillis();
    }

    public int getRainbowColor() {
        long time = System.currentTimeMillis() - startTime;
        float hue = (time % CYCLE_DURATION) / (float) CYCLE_DURATION;
        int rgb = Color.HSBtoRGB(hue, 1.0f, 1.0f);

        red = (rgb >> 16) & 0xFF;
        green = (rgb >> 8) & 0xFF;
        blue = rgb & 0xFF;

        return rgb;
    }

    public Color getRainbowColorAsColor() {
        getRainbowColor(); // Update RGB values
        return new Color(red, green, blue);
    }

    public float[] getRainbowColorAsFloat() {
        getRainbowColor(); // Update RGB values
        return new float[] {
                red / 255.0f,
                green / 255.0f,
                blue / 255.0f,
                1.0f  // Alpha value (fully opaque)
        };
    }

    // Getters for RGB values
    public int getRed() {
        return red;
    }

    public int getGreen() {
        return green;
    }

    public int getBlue() {
        return blue;
    }

    public void reset() {
        startTime = System.currentTimeMillis();
    }
}