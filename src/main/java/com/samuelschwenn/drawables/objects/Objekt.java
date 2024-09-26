package com.samuelschwenn.drawables.objects;

import com.samuelschwenn.interfaces.Drawable;
import com.samuelschwenn.util.CoordsDouble;
import com.samuelschwenn.util.CoordsInt;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.io.Serializable;

import static com.samuelschwenn.Main.loop;
import static com.samuelschwenn.visuals.GameScreen.spaceBetweenLinesPixels;
import static com.samuelschwenn.visuals.GameScreen.titleBarSizePixels;

public abstract class Objekt implements Drawable, Serializable {
    protected int strength;
    @Getter
    protected int maxHealth;
    @Getter
    protected int health;
    @Setter
    @Getter
    protected CoordsInt position;
    @Getter
    protected ObjectType type;
    @Getter
    protected double spawntime;

    public Objekt(int pStrength, int pHealth, CoordsInt position, ObjectType type) {
        strength = pStrength;
        health = pHealth;
        maxHealth = health;
        this.position = position;
        this.type = type;
        spawntime = (double) System.currentTimeMillis() / 1000;
        loop.registerDrawable(this);
    }

    public Objekt(){
        loop.registerDrawable(this);
    }

    public void setHealth(int health) {
        this.health = health;
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
        g2.drawImage(this.
                getImage(), (int) pixelPosition.x(), (int) pixelPosition.y() + titleBarSizePixels, null);
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

    abstract protected Image getImage();

    abstract protected CoordsDouble getDrawnPosition();
}

