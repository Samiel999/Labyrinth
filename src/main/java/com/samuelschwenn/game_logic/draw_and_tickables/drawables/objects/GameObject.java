package com.samuelschwenn.game_logic.draw_and_tickables.drawables.objects;

import com.samuelschwenn.game_logic.draw_and_tickables.Drawable;
import com.samuelschwenn.game_logic.util.CoordsDouble;
import com.samuelschwenn.game_logic.util.CoordsInt;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

import static com.samuelschwenn.Main.loop;
import static com.samuelschwenn.game_app.visuals.GameScreen.spaceBetweenLinesPixels;
import static com.samuelschwenn.game_app.visuals.GameScreen.titleBarSizePixels;

@Getter
@Setter
public abstract class GameObject implements Drawable, Serializable {
    protected int strength;
    protected int maxHealth;
    protected int health;
    protected CoordsInt position;
    protected double spawnTime;
    protected Image image;

    public GameObject() {
        maxHealth = 0;
        image = null;
        spawnTime = (double) System.currentTimeMillis() / 1000;
        loop.registerDrawable(this);
    }

    protected abstract GameObject build(CoordsInt position);

    public static GameObject instantiate(Class<? extends GameObject> objectType, CoordsInt position) {
        GameObject object;
        try {
            object = objectType.getDeclaredConstructor().newInstance();
            object = object.build(position);
        }catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        return object;
    }



    public void setHealth(int health) {
        this.health = health;
        if(maxHealth == 0){
            maxHealth = health;
        }
        if(this.health <= 0){
            die();
        }
    }

    public void die(){
        loop.unregisterDrawable(this);
    }

    @Override
    public void draw(Graphics g) {
        Drawable.super.draw(g);
        if (this.getDrawnPosition().equals(new CoordsDouble(-1, -1))) return;
        CoordsDouble pixelPosition = this.getDrawnPosition().scale(spaceBetweenLinesPixels);
        Graphics2D g2 = (Graphics2D) g;
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, this.getOpacity()));
        g2.drawImage(
                this.getImage(),
                (int) pixelPosition.x(),
                (int) pixelPosition.y() + titleBarSizePixels,
                null
        );
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
        renderLifeBar((Graphics2D) g);
    }

    private void renderLifeBar(Graphics2D graphics2D) {
        if(health < maxHealth) {
            double lifeInPercent = (double) health / maxHealth;
            int width = (int) (lifeInPercent * (spaceBetweenLinesPixels - 4));
            graphics2D.setStroke(new BasicStroke(3));
            CoordsDouble objektPosition = getDrawnPosition().scale(spaceBetweenLinesPixels);
            graphics2D.setColor(Color.red);
            graphics2D.drawLine((int) (objektPosition.x()+ 2), (int) (objektPosition.y() + 2 + titleBarSizePixels), (int) (objektPosition.x() + width), (int) (objektPosition.y() + 2 + titleBarSizePixels));
        }
    }

    protected Image getImage(){
        return image;
    }

    abstract protected CoordsDouble getDrawnPosition();
}

