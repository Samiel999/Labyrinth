package com.samuelschwenn.labyrinth.game_logic.draw_and_tickables.drawables.shots.classic_shots;


import com.samuelschwenn.labyrinth.game_logic.GlobalUsageController;
import com.samuelschwenn.labyrinth.game_logic.draw_and_tickables.drawables.shots.Shot;
import com.samuelschwenn.labyrinth.game_logic.util.CoordsDouble;

import java.awt.*;

public abstract class ClassicShot extends Shot {
    protected final Color color;
    protected final int thickness;

    public ClassicShot(Color color, int thickness, double duration, CoordsDouble startingPosition, CoordsDouble targetPosition, float startingOpacity) {
        super(duration, startingPosition, targetPosition, startingOpacity);
        this.color = color;
        this.thickness = thickness;
    }

    @Override
    public void draw(Graphics graphics) {
        super.draw(graphics);

        Graphics2D g = (Graphics2D) graphics;
        g.setStroke(new BasicStroke(thickness));
        g.setColor(color);
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Math.clamp(opacity, 0.0f, 1.0f)));

        g.drawLine(startingPositionPixels.x(), startingPositionPixels.y(), targetPositionPixels.x(), targetPositionPixels.y());

        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
    }

    @Override
    public void tick(double timeDelta) {
        if (progress >= duration) {
            GlobalUsageController usageController = GlobalUsageController.getInstance();
            usageController.unregisterDrawable(this);
            usageController.unregisterTickable(this);
        }
        double opacityDifference = timeDelta / duration;
        opacity -= (float) opacityDifference;
        progress += timeDelta;
    }
}
