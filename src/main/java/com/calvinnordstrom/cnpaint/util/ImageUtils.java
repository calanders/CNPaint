package com.calvinnordstrom.cnpaint.util;

import com.calvinnordstrom.cnpaint.view.control.ColorControl;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

/**
 * A utility class that provides methods for basic {@code Image} utilities and
 * manipulation.
 * <p>
 * This class provides methods for pixel manipulation.
 */
public class ImageUtils {
    private ImageUtils() {}

    /**
     * Converts an object of {@code Image} to its subclass
     * {@code WritableImage}.
     * <p>
     * The {@code width} and {@code height} of the image
     * are retained during this conversion.
     *
     * @param image the {@code Image} to convert
     * @return the {@code WritableImage}
     */
    public static WritableImage toWritableImage(Image image) {
        return new WritableImage(image.getPixelReader(),
                (int) image.getWidth(), (int) image.getHeight());
    }

    /**
     * Retrieves the {@code Color} based on the specified {@code MouseEvent}.
     * <p>
     * When the button of the {@code MouseEvent} is equal to
     * {@code MouseButton.PRIMARY}, the primary color determined by the
     * {@code ColorControl} will be returned.
     * <p>
     * When the button of the {@code MouseEvent} is equal to
     * {@code MouseButton.SECONDARY}, the secondary color determined by the
     * {@code ColorControl} will be returned.
     *
     * @param event the {@code MouseEvent} to evaluate
     * @return the primary {@code Color} if the event's button is primary,
     * or the secondary {@code Color} if the event's button is secondary.
     */
    public static Color getColor(MouseEvent event) {
        ColorControl colorControl = (ColorControl) ServiceLocator.getInstance().getNode("color_control");
        if (event.getButton().equals(MouseButton.PRIMARY)) {
            return colorControl.getPrimaryColor();
        } else if (event.getButton().equals(MouseButton.SECONDARY)) {
            return colorControl.getSecondaryColor();
        }
        return colorControl.getPrimaryColor();
    }

    public static void drawLine(PixelWriter pixelWriter, double x0, double y0, double x1, double y1, Color color) {
        double dx = x1 - x0;
        double dy = y1 - y0;
        double distance = Math.max(Math.abs(dx), Math.abs(dy));

        for (int i = 0; i <= distance; i++) {
            double t = i / distance;
            int x = (int) Math.round(x0 + t * dx);
            int y = (int) Math.round(y0 + t * dy);
            pixelWriter.setColor(x, y, color);
        }
    }
}
