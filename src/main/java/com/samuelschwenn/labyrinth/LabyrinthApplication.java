package com.samuelschwenn.labyrinth;

import com.samuelschwenn.labyrinth.game_app.sounds.SFX;
import com.samuelschwenn.labyrinth.game_app.sounds.Sound;
import com.samuelschwenn.labyrinth.game_logic.UpdateLoop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class LabyrinthApplication {
	public static UpdateLoop loop;
	public static final Sound sound = new Sound();
	public static final SFX sfx = new SFX();
	public static Boolean source = false;

	public static void main(String[] args) {
		SpringApplicationBuilder builder = new SpringApplicationBuilder(LabyrinthApplication.class);
		builder.headless(false);
		ConfigurableApplicationContext context = builder.run(args);

		loop = context.getBean(UpdateLoop.class);
		Thread loop_thread = new Thread(loop);
		loop_thread.start();
	}

}
