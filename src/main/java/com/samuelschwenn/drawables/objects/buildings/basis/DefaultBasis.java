package com.samuelschwenn.drawables.objects.buildings.basis;

import com.samuelschwenn.drawables.objects.ObjectType;
import com.samuelschwenn.util.CoordsInt;
import lombok.NoArgsConstructor;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

import static com.samuelschwenn.visuals.GameScreen.spaceBetweenLinesPixels;

@NoArgsConstructor
public class DefaultBasis extends Basis{
    private static final Image image;
    static {
        try {
            image = ImageIO.read(Objects.requireNonNull(Basis.class.getClassLoader().getResourceAsStream("images/Basis.png"))).getScaledInstance(spaceBetweenLinesPixels, spaceBetweenLinesPixels, Image.SCALE_SMOOTH);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public DefaultBasis(CoordsInt position) {
        super(0, 200, position, ObjectType.DefaultBasis);
    }

    @Override
    public Image getImage() {
        return image;
    }

//    public static Image getStaticImage() {
//        return image;
//    }
}
