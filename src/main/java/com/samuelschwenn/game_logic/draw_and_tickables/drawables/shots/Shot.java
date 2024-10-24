package com.samuelschwenn.game_logic.draw_and_tickables.drawables.shots;

import com.samuelschwenn.game_logic.draw_and_tickables.Drawable;
import com.samuelschwenn.game_logic.draw_and_tickables.Tickable;
import com.samuelschwenn.game_logic.util.CoordsDouble;
import com.samuelschwenn.game_logic.util.CoordsInt;

import static com.samuelschwenn.Main.loop;
import static com.samuelschwenn.game_app.visuals.game_visuals.GameScreen.spaceBetweenLinesPixels;
import static com.samuelschwenn.game_app.visuals.game_visuals.GameScreen.titleBarSizePixels;

public abstract class Shot implements Drawable, Tickable {
    protected double progress;
    protected final double duration;
    protected final CoordsInt startingPositionPixels;
    protected final CoordsInt targetPositionPixels;
    protected float opacity;

    public Shot(double duration, CoordsDouble startingPosition, CoordsDouble targetPosition, float startingOpacity) {
        this.duration = duration;
        this.progress = 0;
        this.startingPositionPixels = adaptForPixels(startingPosition);
        this.targetPositionPixels = adaptForPixels(targetPosition);
        this.opacity = startingOpacity;
        loop.registerTickable(this);
        loop.registerDrawable(this, loop.getDrawables().indexOf(loop.getBasis()));
    }

    private CoordsInt adaptForPixels(CoordsDouble coords) {
        return coords.multipliedBy(spaceBetweenLinesPixels).add(new CoordsDouble((double) spaceBetweenLinesPixels / 2, (double) spaceBetweenLinesPixels / 2 + titleBarSizePixels)).toCoordsInt();
    }

    @Override
    public float getOpacity() {
        return opacity;
    }
}
