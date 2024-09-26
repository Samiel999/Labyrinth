package com.samuelschwenn.drawables.objects.monster;

import com.samuelschwenn.drawables.objects.ObjectType;
import com.samuelschwenn.logic.LogicRepresentation;
import com.samuelschwenn.util.CoordsInt;
import com.samuelschwenn.util.Direction;
import lombok.NoArgsConstructor;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.samuelschwenn.util.Math.mirrorImage;
import static com.samuelschwenn.visuals.GameScreen.spaceBetweenLinesPixels;

@NoArgsConstructor
public class BombCarrier extends Monster {
    private static Image image;
    static {
        try {
            image = ImageIO.read(Objects.requireNonNull(BombCarrier.class.getClassLoader().getResourceAsStream("images/Bombenschiff.png")));
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

    public BombCarrier(CoordsInt position) {
        super(30, 35, position, 1, 1, 40, ObjectType.BombCarrier, true);
    }

    @Override
    public void updateMonsterPath(LogicRepresentation logicRepresentation) {
        updateFlyingMonsterPath(logicRepresentation);
    }

    @Override
    public Image getImage() {
        return directionalImages.get(getDirection());
    }
}
