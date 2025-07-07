package com.darzalgames.libgdxtools.maingame;

import java.util.function.BiConsumer;

import com.badlogic.gdx.scenes.scene2d.Actor;

public class GetOnStage {

	private GetOnStage() {}

	private static BiConsumer<Actor, String> addActorToStage;

	static void setAddActorToStageFunction(BiConsumer<Actor, String> addActorToStage) {
		GetOnStage.addActorToStage = addActorToStage;
	}

	public static void addActorToStage(Actor actor, String stageName) {
		addActorToStage.accept(actor, stageName);
	}

}
