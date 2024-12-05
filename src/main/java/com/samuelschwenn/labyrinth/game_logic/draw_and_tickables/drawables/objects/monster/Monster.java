package com.samuelschwenn.labyrinth.game_logic.draw_and_tickables.drawables.objects.monster;

import com.samuelschwenn.labyrinth.game_logic.GlobalUsageController;
import com.samuelschwenn.labyrinth.game_logic.LogicRepresentation;
import com.samuelschwenn.labyrinth.game_logic.draw_and_tickables.Tickable;
import com.samuelschwenn.labyrinth.game_logic.draw_and_tickables.drawables.objects.GameObject;
import com.samuelschwenn.labyrinth.game_logic.draw_and_tickables.drawables.objects.buildings.basis.DefaultBasis;
import com.samuelschwenn.labyrinth.game_logic.draw_and_tickables.drawables.objects.monster.walking.Boss1;
import com.samuelschwenn.labyrinth.game_logic.util.CoordsDouble;
import com.samuelschwenn.labyrinth.game_logic.util.CoordsInt;
import com.samuelschwenn.labyrinth.game_logic.util.Direction;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.io.Serializable;
import java.util.List;

import static com.samuelschwenn.labyrinth.LabyrinthApplication.loop;
import static com.samuelschwenn.labyrinth.game_app.util.ImageUtil.mirrorImage;
import static com.samuelschwenn.labyrinth.game_app.util.SoundUtils.playSFX;
import static com.samuelschwenn.labyrinth.game_logic.util.LoopType.game_over;

public abstract class Monster extends GameObject implements Tickable, Serializable {
    @Setter
    protected float movingSpeed;
    @Setter
    protected int attackSpeed;
    @Getter
    protected int stepsToGoal;
    @Setter
    protected double bounty;
    protected double pathWeight;
    protected List<CoordsInt> pathNodes;
    @Getter
    protected CoordsDouble drawnPosition;
    protected double attackCooldown = attackSpeed;

    protected double timeDelta;

    public Monster() {
        super();
        stepsToGoal = 250;
        GlobalUsageController.getInstance().registerTickable(this);
    }

    public void tick(double timeDelta) {
        if (position.equals(new CoordsInt(-1, -1))) return;
        this.timeDelta = timeDelta;
        moveTowardsPositionOrMakeMove();
    }

    public void moveTowardsPositionOrMakeMove() {
        Direction directionToMove = drawnPosition.getDirectionTo(position.toCoordsDouble());
        if (directionToMove == null) {
            makeMoveOrAttack();
        } else {
            moveDrawnPositionInDirection(directionToMove);
        }
    }

    public abstract void makeMoveOrAttack();

    public void attack(double timeDelta, GameObject object) {
        attackCooldown -= timeDelta;
        if (attackCooldown <= 0) {
            object.setHealth(object.getHealth() - strength);
            if (object.getClass().equals(DefaultBasis.class)) {
                playSFX(10);
            }
            attackCooldown = attackSpeed;
        }
    }

    public void moveDrawnPositionInDirection(Direction directionToMove) {
        CoordsDouble normalizedDirection = CoordsDouble.getNormalized(directionToMove);

        CoordsDouble totalLeftToMove = position.subtract(drawnPosition);
        CoordsDouble absoluteMovementInDirection = normalizedDirection.multipliedBy((float) timeDelta * movingSpeed);
        CoordsDouble difference = totalLeftToMove.subtract(absoluteMovementInDirection);

        if (movingFurtherThanPositionInDirection(directionToMove, difference)) {
            this.drawnPosition = position.toCoordsDouble();
            return;
        }
        this.drawnPosition = drawnPosition.add(absoluteMovementInDirection);
    }

    private boolean movingFurtherThanPositionInDirection(Direction direction, CoordsDouble difference) {
        if (direction.equals(Direction.EAST) || direction.equals(Direction.NORTH)) {
            return difference.maxCoords(new CoordsDouble(0, 0)).equals(new CoordsDouble(0, 0));
        } else {
            return difference.minCoords(new CoordsDouble(0, 0)).equals(new CoordsDouble(0, 0));
        }
    }

    public float getOpacity() {
        return 1;
    }

    @Override
    public Image getImage() {
        return switch (getDirection()) {
            case WEST -> mirrorImage(super.getImage());
            case EAST, NORTH, SOUTH -> super.getImage();
        };
    }

    public Direction getDirection() {
        CoordsDouble current_position = (pathNodes.get(1).equals(position) ? position : pathNodes.get(1)).toCoordsDouble();
        Direction direction = current_position.getDirectionTo(drawnPosition);
        if (direction == null) {
            pathNodes.remove(1);
            return getDirection();
        }
        return direction;
    }

    @Override
    public void setPosition(CoordsInt position) {
        super.setPosition(position);
        this.drawnPosition = position.toCoordsDouble();
    }

    @Override
    public void die() {
        super.die();
        GlobalUsageController gC = GlobalUsageController.getInstance();
        gC.unregisterTickable(this);
        gC.setMoney(gC.getMoney() + bounty);
        if (this.getClass().equals(Boss1.class)) playSFX(9);
        else playSFX(1);

        checkForGameOver();
    }

    private void checkForGameOver() {
        if (LogicRepresentation.getInstance().getMonsterList().isEmpty() && LogicRepresentation.getInstance().getLevel().getMonstersToSpawn().isEmpty()) {
            loop.update(game_over);
        }
    }

    public abstract void updateMonsterPath();
}
