package com.samuelschwenn.labyrinth.game_app.util;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public final class ImageUtil {
    public static BufferedImage mirrorImage(Image image){
        AffineTransform at = new AffineTransform();
        at.concatenate(AffineTransform.getScaleInstance(-1, 1));
        at.concatenate(AffineTransform.getTranslateInstance(-image.getWidth(null) ,0));
        return createTransformed(image, at);
    }

    private static BufferedImage createTransformed(Image image, AffineTransform transform) {
        BufferedImage newImage = new BufferedImage(
                image.getWidth(null), image.getHeight(null),
                BufferedImage.TYPE_INT_ARGB
        );
        Graphics2D g = newImage.createGraphics();
        g.transform(transform);
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return newImage;
    }
}
