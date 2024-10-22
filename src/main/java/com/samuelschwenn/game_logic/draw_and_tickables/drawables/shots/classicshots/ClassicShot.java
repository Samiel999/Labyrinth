package com.samuelschwenn.game_logic.draw_and_tickables.drawables.shots.classicshots;


import com.samuelschwenn.game_logic.draw_and_tickables.drawables.shots.Shot;
import com.samuelschwenn.game_logic.util.CoordsDouble;

import java.awt.*;

import static com.samuelschwenn.Main.loop;

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
        System.out.println(timeDelta);
        if (progress >= duration) {
            loop.unregisterDrawable(this);
            loop.unregisterTickable(this);
        }
        double opacityDifference = timeDelta / duration;
        opacity -= (float) opacityDifference;
        progress += timeDelta;
    }
}
