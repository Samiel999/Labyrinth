package com.samuelschwenn.labyrinth.game_logic;

import com.samuelschwenn.labyrinth.game_logic.draw_and_tickables.Drawable;
import com.samuelschwenn.labyrinth.game_logic.draw_and_tickables.Tickable;
import com.samuelschwenn.labyrinth.game_logic.draw_and_tickables.drawables.objects.buildings.basis.Basis;
import com.samuelschwenn.labyrinth.game_logic.draw_and_tickables.drawables.objects.buildings.basis.DefaultBasis;
import com.samuelschwenn.labyrinth.game_logic.level.Level;
import com.samuelschwenn.labyrinth.game_logic.util.CoordsInt;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.samuelschwenn.labyrinth.game_logic.draw_and_tickables.drawables.objects.GameObject.instantiate;

public class GlobalUsageController {
    private static GlobalUsageController instance;

    @Getter
    private List<Drawable> drawables = new CopyOnWriteArrayList<>();
    @Getter
    private List<Tickable> tickables = new CopyOnWriteArrayList<>();
    @Getter
    @Setter
    private int current_save;
    @Getter
    @Setter
    private int current_level = 1;
    @Setter
    @Getter
    private Basis basis;
    @Getter
    @Setter
    private double money = 0;
    @Setter
    private int wall_count;

    private GlobalUsageController() {
    }
    public static GlobalUsageController getInstance() {
        if (instance == null) {
            instance = new GlobalUsageController();
        }
        return instance;
    }


    public void registerTickable(Tickable t){
        tickables.add(t);
    }
    public void unregisterTickable(Tickable t){
        tickables.remove(t);
    }
    public void resetTickables(){tickables = new CopyOnWriteArrayList<>();}

    public void registerDrawable(Drawable d){
        drawables.add(d);
    }
    public void registerDrawable(Drawable d, int index) {
        drawables.add(index, d);
    }
    public void unregisterDrawable(Drawable d){
        drawables.remove(d);
    }
    public void resetDrawables(){drawables = new CopyOnWriteArrayList<>();}

    public void resetBasis(){
        basis = (DefaultBasis) instantiate(DefaultBasis.class, new CoordsInt(0,0));
    }

    public void resetLogicRepresentation(Level level){
        LogicRepresentation.resetInstance();
        LogicRepresentation.getInstance().initializeWithLevel(level);
    }
}
