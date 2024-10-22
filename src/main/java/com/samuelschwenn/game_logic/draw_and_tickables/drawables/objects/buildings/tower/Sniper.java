package com.samuelschwenn.game_logic.draw_and_tickables.drawables.objects.buildings.tower;

import com.samuelschwenn.game_logic.draw_and_tickables.drawables.objects.GameObject;
import com.samuelschwenn.game_logic.draw_and_tickables.drawables.objects.monster.Monster;
import com.samuelschwenn.game_logic.util.CoordsInt;
import lombok.NoArgsConstructor;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

import static com.samuelschwenn.game_app.visuals.GameScreen.spaceBetweenLinesPixels;

@NoArgsConstructor
public class Sniper extends Tower {
    private static final Image image;

    static {
        try {
            image = ImageIO.read(Objects.requireNonNull(Sniper.class.getClassLoader().getResourceAsStream("images/ScharfschuetzenTurm.png"))).getScaledInstance(spaceBetweenLinesPixels, spaceBetweenLinesPixels, Image.SCALE_SMOOTH);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected GameObject build(CoordsInt position) {
        setStrength(20);
        setHealth(25);
        setPosition(position);
        setReach(8);
        setAttackSpeed(10);
        setCost(50);
        setImage(image);
        return this;
    }

    @Override
    public Monster[] shoot() {
        return shootNormal();
    }

    public static Image getStaticImage() {
        return image;
    }
}
