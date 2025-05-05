package net.deimos.api;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.deimos.api.i.IClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.*;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

public class RenderUtil implements IClient {
    public static void cleanup() {
        RenderSystem.enableDepthTest();
        GlStateManager._disableBlend();
        GlStateManager._enableCull();
        glDisable(GL_LINE_SMOOTH);
    }

    public static void setup() {
        RenderSystem.disableDepthTest();
        GlStateManager._enableBlend();
        GlStateManager._blendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager._disableCull();
        glEnable(GL_LINE_SMOOTH);
    }

    public static @NotNull MatrixStack matrixFrom(double x, double y, double z) {
        MatrixStack matrices = new MatrixStack();
        Camera camera = client.gameRenderer.getCamera();
        matrices.translate(x - camera.getPos().x, y - camera.getPos().y, z - camera.getPos().z);
        return matrices;
    }

    // Util
    public static Box moveToZero(Box box) {
        return box.offset(getMinVec(box).negate());
    }
    public static Vec3d getMinVec(Box box) {
        return new Vec3d(box.minX, box.minY, box.minZ);
    }

    // crazy
    public static void drawBoxFill(Box box, Color color, Direction... excludeDirs) {
        setup();
        MatrixStack matrices = matrixFrom(box.minX, box.minY, box.minZ);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        vertexBoxQuads(matrices, buffer, moveToZero(box), color, excludeDirs);
        BufferRenderer.drawWithGlobalProgram(buffer.end());
        cleanup();
    }

    public static void vertexBoxQuads(MatrixStack matrices, VertexConsumer vertexConsumer, Box box, Color quadColor, Direction... excludeDirs) {
        float x1 = (float) box.minX;
        float y1 = (float) box.minY;
        float z1 = (float) box.minZ;
        float x2 = (float) box.maxX;
        float y2 = (float) box.maxY;
        float z2 = (float) box.maxZ;

        if (!ArrayUtils.contains(excludeDirs, Direction.DOWN)) {
            vertexQuad(matrices, vertexConsumer, x1, y1, z1, x2, y1, z1, x2, y1, z2, x1, y1, z2, quadColor);
        }

        if (!ArrayUtils.contains(excludeDirs, Direction.WEST)) {
            vertexQuad(matrices, vertexConsumer, x1, y1, z2, x1, y2, z2, x1, y2, z1, x1, y1, z1, quadColor);
        }

        if (!ArrayUtils.contains(excludeDirs, Direction.EAST)) {
            vertexQuad(matrices, vertexConsumer, x2, y1, z1, x2, y2, z1, x2, y2, z2, x2, y1, z2, quadColor);
        }

        if (!ArrayUtils.contains(excludeDirs, Direction.NORTH)) {
            vertexQuad(matrices, vertexConsumer, x1, y1, z1, x1, y2, z1, x2, y2, z1, x2, y1, z1, quadColor);
        }

        if (!ArrayUtils.contains(excludeDirs, Direction.SOUTH)) {
            vertexQuad(matrices, vertexConsumer, x2, y1, z2, x2, y2, z2, x1, y2, z2, x1, y1, z2, quadColor);
        }

        if (!ArrayUtils.contains(excludeDirs, Direction.UP)) {
            vertexQuad(matrices, vertexConsumer, x1, y2, z2, x2, y2, z2, x2, y2, z1, x1, y2, z1, quadColor);
        }
    }

    // Note removed culling cause it was causing issues
    public static void vertexQuad(MatrixStack matrices, VertexConsumer vertexConsumer, float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3, float x4, float y4, float z4, Color quadColor) {
        vertexConsumer.vertex(matrices.peek().getPositionMatrix(), x1, y1, z1).color(quadColor.getRed(), quadColor.getGreen(), quadColor.getBlue(), quadColor.getAlpha());
        vertexConsumer.vertex(matrices.peek().getPositionMatrix(), x2, y2, z2).color(quadColor.getRed(), quadColor.getGreen(), quadColor.getBlue(), quadColor.getAlpha());
        vertexConsumer.vertex(matrices.peek().getPositionMatrix(), x3, y3, z3).color(quadColor.getRed(), quadColor.getGreen(), quadColor.getBlue(), quadColor.getAlpha());
        vertexConsumer.vertex(matrices.peek().getPositionMatrix(), x4, y4, z4).color(quadColor.getRed(), quadColor.getGreen(), quadColor.getBlue(), quadColor.getAlpha());
        vertexConsumer.vertex(matrices.peek().getPositionMatrix(), x4, y4, z4).color(quadColor.getRed(), quadColor.getGreen(), quadColor.getBlue(), quadColor.getAlpha());
        vertexConsumer.vertex(matrices.peek().getPositionMatrix(), x3, y3, z3).color(quadColor.getRed(), quadColor.getGreen(), quadColor.getBlue(), quadColor.getAlpha());
        vertexConsumer.vertex(matrices.peek().getPositionMatrix(), x2, y2, z2).color(quadColor.getRed(), quadColor.getGreen(), quadColor.getBlue(), quadColor.getAlpha());
        vertexConsumer.vertex(matrices.peek().getPositionMatrix(), x1, y1, z1).color(quadColor.getRed(), quadColor.getGreen(), quadColor.getBlue(), quadColor.getAlpha());
    }

    public static void vertexBoxLines(MatrixStack matrices, VertexConsumer vertexConsumer, Box box, Color quadColor, Direction... excludeDirs) {
        float x1 = (float) box.minX;
        float y1 = (float) box.minY;
        float z1 = (float) box.minZ;
        float x2 = (float) box.maxX;
        float y2 = (float) box.maxY;
        float z2 = (float) box.maxZ;

        boolean exDown = ArrayUtils.contains(excludeDirs, Direction.DOWN);
        boolean exWest = ArrayUtils.contains(excludeDirs, Direction.WEST);
        boolean exEast = ArrayUtils.contains(excludeDirs, Direction.EAST);
        boolean exNorth = ArrayUtils.contains(excludeDirs, Direction.NORTH);
        boolean exSouth = ArrayUtils.contains(excludeDirs, Direction.SOUTH);
        boolean exUp = ArrayUtils.contains(excludeDirs, Direction.UP);

        if (!exDown) {
            vertexLine(matrices, vertexConsumer, x1, y1, z1, x2, y1, z1, quadColor);
            vertexLine(matrices, vertexConsumer, x2, y1, z1, x2, y1, z2, quadColor);
            vertexLine(matrices, vertexConsumer, x2, y1, z2, x1, y1, z2, quadColor);
            vertexLine(matrices, vertexConsumer, x1, y1, z2, x1, y1, z1, quadColor);
        }

        if (!exWest) {
            if (exDown) vertexLine(matrices, vertexConsumer, x1, y1, z1, x1, y1, z2, quadColor);
            vertexLine(matrices, vertexConsumer, x1, y1, z2, x1, y2, z2, quadColor);
            vertexLine(matrices, vertexConsumer, x1, y1, z1, x1, y2, z1, quadColor);
            if (exUp) vertexLine(matrices, vertexConsumer, x1, y2, z1, x1, y2, z2, quadColor);
        }

        if (!exEast) {
            if (exDown) vertexLine(matrices, vertexConsumer, x2, y1, z1, x2, y1, z2, quadColor);
            vertexLine(matrices, vertexConsumer, x2, y1, z2, x2, y2, z2, quadColor);
            vertexLine(matrices, vertexConsumer, x2, y1, z1, x2, y2, z1, quadColor);
            if (exUp) vertexLine(matrices, vertexConsumer, x2, y2, z1, x2, y2, z2, quadColor);
        }

        if (!exNorth) {
            if (exDown) vertexLine(matrices, vertexConsumer, x1, y1, z1, x2, y1, z1, quadColor);
            if (exEast) vertexLine(matrices, vertexConsumer, x2, y1, z1, x2, y2, z1, quadColor);
            if (exWest) vertexLine(matrices, vertexConsumer, x1, y1, z1, x1, y2, z1, quadColor);
            if (exUp) vertexLine(matrices, vertexConsumer, x1, y2, z1, x2, y2, z1, quadColor);
        }

        if (!exSouth) {
            if (exDown) vertexLine(matrices, vertexConsumer, x1, y1, z2, x2, y1, z2, quadColor);
            if (exEast) vertexLine(matrices, vertexConsumer, x2, y1, z2, x2, y2, z2, quadColor);
            if (exWest) vertexLine(matrices, vertexConsumer, x1, y1, z2, x1, y2, z2, quadColor);
            if (exUp) vertexLine(matrices, vertexConsumer, x1, y2, z2, x2, y2, z2, quadColor);
        }

        if (!exUp) {
            vertexLine(matrices, vertexConsumer, x1, y2, z1, x2, y2, z1, quadColor);
            vertexLine(matrices, vertexConsumer, x2, y2, z1, x2, y2, z2, quadColor);
            vertexLine(matrices, vertexConsumer, x2, y2, z2, x1, y2, z2, quadColor);
            vertexLine(matrices, vertexConsumer, x1, y2, z2, x1, y2, z1, quadColor);
        }
    }

    public static void drawBoxOutline(BlockPos blockPos, Color color, float lineWidth, Direction... excludeDirs) {
        drawBoxOutline(new Box(blockPos), color, lineWidth, excludeDirs);
    }

    public static void drawBoxOutline(Box box, Color color, float lineWidth, Direction... excludeDirs) {
        setup();
        MatrixStack matrices = matrixFrom(box.minX, box.minY, box.minZ);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.begin(VertexFormat.DrawMode.LINES, VertexFormats.LINES);
        RenderSystem.disableCull();
        RenderSystem.setShader(GameRenderer::getRenderTypeLinesProgram);
        RenderSystem.lineWidth(lineWidth);
        vertexBoxLines(matrices, buffer, moveToZero(box), color, excludeDirs);
        BufferRenderer.drawWithGlobalProgram(buffer.end());
        RenderSystem.enableCull();
        cleanup();
    }

    public static void vertexLine(MatrixStack matrices, VertexConsumer vertexConsumer, float x1, float y1, float z1, float x2, float y2, float z2, Color c) {
        Matrix4f model = matrices.peek().getPositionMatrix();
        Vector3f normalVec = getNormal(x1, y1, z1, x2, y2, z2);

        vertexConsumer.vertex(model, x1, y1, z1).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).normal(matrices.peek(), normalVec.x(), normalVec.y(), normalVec.z());
        vertexConsumer.vertex(model, x2, y2, z2).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).normal(matrices.peek(), normalVec.x(), normalVec.y(), normalVec.z());
    }

    // normals
    public static Vector3f getNormal(float x1, float y1, float z1, float x2, float y2, float z2) {
        float xNormal = x2 - x1;
        float yNormal = y2 - y1;
        float zNormal = z2 - z1;
        float normalSqrt = MathHelper.sqrt(xNormal * xNormal + yNormal * yNormal + zNormal * zNormal);
        return new Vector3f(xNormal / normalSqrt, yNormal / normalSqrt, zNormal / normalSqrt);
    }

    public static void rect(MatrixStack matrices, float x, float y, float width, float height, int color) {
        // Calculate the rectangle's boundaries
        float x1 = x;
        float y1 = y;
        float x2 = x + width;
        float y2 = y + height;

        // Retrieve the position matrix
        Matrix4f matrix4f = matrices.peek().getPositionMatrix();

        // Extract ARGB color components and normalize them to 0-1 range
        float alpha = (float) (color >> 24 & 255) / 255.0f;
        float red = (float) (color >> 16 & 255) / 255.0f;
        float green = (float) (color >> 8 & 255) / 255.0f;
        float blue = (float) (color & 255) / 255.0f;

        // Prepare the buffer builder
        BufferBuilder buffer = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);

        // Enable blending and set the shader
        RenderSystem.enableBlend();
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);

        // Define the rectangle vertices with color
        buffer.vertex(matrix4f, x1, y1, 0.0f).color(red, green, blue, alpha);
        buffer.vertex(matrix4f, x1, y2, 0.0f).color(red, green, blue, alpha);
        buffer.vertex(matrix4f, x2, y2, 0.0f).color(red, green, blue, alpha);
        buffer.vertex(matrix4f, x2, y1, 0.0f).color(red, green, blue, alpha);

        // Draw the rectangle
        BufferRenderer.drawWithGlobalProgram(buffer.end());

        // Disable blending
        RenderSystem.disableBlend();
    }

    public static void minusrect(MatrixStack matrices, float x, float y, float width, float height, int color) {
        float a = (float) (color >> 24 & 255) / 255.0F;
        float r = (float) (color >> 16 & 255) / 255.0F;
        float g = (float) (color >> 8 & 255) / 255.0F;
        float b = (float) (color & 255) / 255.0F;

        BufferBuilder bufferBuilder = Tessellator.getInstance()
                .begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        RenderSystem.enableBlend();

        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        RenderSystem.blendFuncSeparate(
                GlStateManager.SrcFactor.ONE_MINUS_DST_COLOR,
                GlStateManager.DstFactor.ONE_MINUS_SRC_COLOR,
                GlStateManager.SrcFactor.ONE,
                GlStateManager.DstFactor.ZERO
        );

        bufferBuilder.vertex(matrices.peek().getPositionMatrix(), x, y, 0.0F).color(r, g, b, a);
        bufferBuilder.vertex(matrices.peek().getPositionMatrix(), x, y + height, 0.0F).color(r, g, b, a);
        bufferBuilder.vertex(matrices.peek().getPositionMatrix(), x + width, y + height, 0.0F).color(r, g, b, a);
        bufferBuilder.vertex(matrices.peek().getPositionMatrix(), x + width, y, 0.0F).color(r, g, b, a);

        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
        RenderSystem.disableBlend();
    }

}