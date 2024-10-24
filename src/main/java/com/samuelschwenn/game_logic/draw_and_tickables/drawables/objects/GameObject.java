package com.samuelschwenn.game_logic.draw_and_tickables.drawables.objects;

import com.samuelschwenn.game_app.visuals.register.ImageRegister;
import com.samuelschwenn.game_logic.draw_and_tickables.Drawable;
import com.samuelschwenn.game_logic.util.CoordsDouble;
import com.samuelschwenn.game_logic.util.CoordsInt;
import lombok.Getter;
import lombok.Setter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

import static com.samuelschwenn.Main.loop;
import static com.samuelschwenn.game_app.visuals.game_visuals.GameScreen.spaceBetweenLinesPixels;
import static com.samuelschwenn.game_app.visuals.game_visuals.GameScreen.titleBarSizePixels;

@Getter
@Setter
public abstract class GameObject implements Drawable, Serializable {
    protected int strength;
    protected int maxHealth;
    protected int health;
    protected CoordsInt position;
    protected double spawnTime;
    protected String imagePath;
    private ImageRegister imageRegister;

    public GameObject() {
        maxHealth = 0;
        spawnTime = (double) System.currentTimeMillis() / 1000;
        imageRegister = ImageRegister.getInstance();
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

    public Image getImage() {
        if(imageRegister.getFromRegister(imagePath) != null){
            return imageRegister.getFromRegister(imagePath);
        }
        return getImageInit();
    }

    public Image getImageInit(){
        if(imagePath == null){
            return null;
        }
        try(
                InputStream stream = GameObject.class.getClassLoader().getResourceAsStream("images/"+imagePath);
                InputStream nonNullStream = Objects.requireNonNull(stream)
        ){
            BufferedImage image = ImageIO.read(nonNullStream);
            Image scaledImage = image.getScaledInstance(spaceBetweenLinesPixels, spaceBetweenLinesPixels, Image.SCALE_SMOOTH);
            ImageRegister.getInstance().addToRegister(imagePath, scaledImage);
            return scaledImage;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
        CoordsDouble pixelPosition = this.getDrawnPosition().multipliedBy(spaceBetweenLinesPixels);
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
            CoordsDouble objektPosition = getDrawnPosition().multipliedBy(spaceBetweenLinesPixels);
            graphics2D.setColor(Color.red);
            graphics2D.drawLine((int) (objektPosition.x()+ 2), (int) (objektPosition.y() + 2 + titleBarSizePixels), (int) (objektPosition.x() + width), (int) (objektPosition.y() + 2 + titleBarSizePixels));
        }
    }

    abstract protected CoordsDouble getDrawnPosition();
}

