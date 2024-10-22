package com.samuelschwenn.game_logic.draw_and_tickables.drawables.objects.buildings.wall;

import com.samuelschwenn.game_logic.draw_and_tickables.drawables.objects.GameObject;
import com.samuelschwenn.game_logic.util.CoordsInt;
import lombok.NoArgsConstructor;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

import static com.samuelschwenn.game_app.visuals.GameScreen.spaceBetweenLinesPixels;

@NoArgsConstructor
public class DefaultWall extends Wall {
    private static final Image image;

    static {
        try {
            image = ImageIO.read(Objects.requireNonNull(DefaultWall.class.getClassLoader().getResourceAsStream("images/Mauer.png"))).getScaledInstance(spaceBetweenLinesPixels, spaceBetweenLinesPixels, Image.SCALE_SMOOTH);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected GameObject build(CoordsInt position) {
        setHealth(10);
        setPosition(position);
        setCost(0);
        setImage(image);
        return this;
    }

    public static Image getStaticImage() {
        return image;
    }
}
