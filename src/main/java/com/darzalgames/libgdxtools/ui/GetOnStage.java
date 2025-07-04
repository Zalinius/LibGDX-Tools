package com.darzalgames.libgdxtools.ui;

import java.util.function.BiConsumer;

import com.badlogic.gdx.scenes.scene2d.Actor;

public class GetOnStage {

	private GetOnStage() {}

	private static BiConsumer<Actor, String> addActorStage;

	public static void initialize(BiConsumer<Actor, String> addActorStage) {
		GetOnStage.addActorStage = addActorStage;
	}

	public static void addActorToStage(Actor actor, String stageName) {
		addActorStage.accept(actor, stageName);
	}

}
