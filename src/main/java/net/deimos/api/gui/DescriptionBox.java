package net.deimos.api.gui;

import net.deimos.api.interfaces.IClient;
import net.minecraft.client.gui.DrawContext;

import java.awt.Color;

public class DescriptionBox implements IClient {
    private static final int PADDING = 3;
    private static final int MAX_WIDTH = 150;
    private static final Color BACKGROUND_COLOR = new Color(40, 40, 40, 255);
    private static final Color BORDER_COLOR = new Color(47, 79, 101, 255);

    private String currentDescription = null;
    private int x, y;
    private int width, height;

    public void render(DrawContext context, float mouseX, float mouseY) {
        if (currentDescription == null || currentDescription.isEmpty()) {
            return;
        }

        String[] lines = wrapText(currentDescription, MAX_WIDTH);
        width = 0;
        for (String line : lines) {
            int lineWidth = client.textRenderer.getWidth(line);
            width = Math.max(width, lineWidth);
        }
        width += PADDING * 2;
        height = lines.length * 10 + PADDING * 2;

        int screenWidth = client.getWindow().getScaledWidth();
        int screenHeight = client.getWindow().getScaledHeight();

        x = screenWidth - width - 10;
        y = screenHeight - height - 10;

        context.fill(x, y, x + width, y + height, BACKGROUND_COLOR.getRGB());

        context.fill(x, y, x + width, y + 1, BORDER_COLOR.getRGB());
        context.fill(x, y + height - 1, x + width, y + height, BORDER_COLOR.getRGB());
        context.fill(x, y, x + 1, y + height, BORDER_COLOR.getRGB());
        context.fill(x + width - 1, y, x + width, y + height, BORDER_COLOR.getRGB());

        for (int i = 0; i < lines.length; i++) {
          context.drawText(client.textRenderer, lines[i], x + PADDING, y + PADDING + i * 10, Color.WHITE.getRGB(), true);
//           FontAgent.osans.drawText(context.getMatrices(), Text.of(lines[i]), x + PADDING, y + PADDING + i * 10, 255f);
        }
    }

    public void setDescription(String description) {
        this.currentDescription = description;
    }

    public void clearDescription() {
        this.currentDescription = null;
    }

    private String[] wrapText(String text, int maxWidth) {
        if (text == null || text.isEmpty()) {
            return new String[0];
        }

        String[] words = text.split(" ");
        StringBuilder currentLine = new StringBuilder();
        java.util.List<String> lines = new java.util.ArrayList<>();

        for (String word : words) {
            String testLine = currentLine.toString().isEmpty() ? word : currentLine + " " + word;
            if (client.textRenderer.getWidth(testLine) <= maxWidth) {
                if (!currentLine.toString().isEmpty()) {
                    currentLine.append(" ");
                }
                currentLine.append(word);
            } else {
                if (!currentLine.toString().isEmpty()) {
                    lines.add(currentLine.toString());
                    currentLine = new StringBuilder();
                }

                if (client.textRenderer.getWidth(word) > maxWidth) {
                    StringBuilder partialWord = new StringBuilder();
                    for (char c : word.toCharArray()) {
                        if (client.textRenderer.getWidth(partialWord.toString() + c) > maxWidth) {
                            lines.add(partialWord.toString());
                            partialWord = new StringBuilder();
                        }
                        partialWord.append(c);
                    }
                    if (partialWord.length() > 0) {
                        currentLine = partialWord;
                    }
                } else {
                    currentLine.append(word);
                }
            }
        }

        if (!currentLine.toString().isEmpty()) {
            lines.add(currentLine.toString());
        }

        return lines.toArray(new String[0]);
    }
}