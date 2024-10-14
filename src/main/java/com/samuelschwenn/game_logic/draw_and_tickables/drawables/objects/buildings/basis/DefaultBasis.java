package com.samuelschwenn.game_logic.draw_and_tickables.drawables.objects.buildings.basis;

import com.samuelschwenn.game_logic.draw_and_tickables.drawables.objects.ObjectType;
import com.samuelschwenn.game_logic.util.CoordsInt;
import lombok.NoArgsConstructor;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

import static com.samuelschwenn.game_app.visuals.GameScreen.spaceBetweenLinesPixels;

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
        super(0, 200, position, ObjectType.DefaultBasis, image);
    }
}
