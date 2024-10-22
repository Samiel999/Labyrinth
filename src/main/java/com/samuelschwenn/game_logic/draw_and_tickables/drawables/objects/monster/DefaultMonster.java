package com.samuelschwenn.game_logic.draw_and_tickables.drawables.objects.monster;

import com.samuelschwenn.game_logic.draw_and_tickables.drawables.objects.GameObject;
import com.samuelschwenn.game_logic.util.CoordsInt;
import com.samuelschwenn.game_logic.util.Direction;
import lombok.NoArgsConstructor;
import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.samuelschwenn.game_app.util.ImageUtil.mirrorImage;
import static com.samuelschwenn.game_app.visuals.GameScreen.spaceBetweenLinesPixels;

@NoArgsConstructor
public class DefaultMonster extends Monster{
    private static Image image;
    static {
        try {
            image = ImageIO.read(Objects.requireNonNull(BombCarrier.class.getClassLoader().getResourceAsStream("images/DefaultMonster.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private static final Map<Direction, Image> directionalImages = new HashMap<>();
    static {
        image = image.getScaledInstance(spaceBetweenLinesPixels, spaceBetweenLinesPixels, Image.SCALE_SMOOTH);
        directionalImages.put(Direction.EAST, image);
        directionalImages.put(Direction.WEST, mirrorImage(image));
        directionalImages.put(Direction.NORTH, image);
        directionalImages.put(Direction.SOUTH, image);
    }

    @Override
    protected GameObject build(CoordsInt position) {
        setStrength(10);
        setHealth(20);
        setPosition(position);
        setMovingSpeed(2);
        setAttackSpeed(4);
        setBounty(8);
        setFlying(false);
        setImage(image);
        return this;
    }

    @Override
    public void updateMonsterPath() {
        updateWalkingMonsterPath();
    }

    @Override
    public Image getImage() {
        return directionalImages.get(getDirection());
    }
}
