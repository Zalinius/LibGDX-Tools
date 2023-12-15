package com.darzalgames.libgdxtools.save;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.zalinius.questgiver.core.data.DialogChoiceData;
import com.zalinius.questgiver.core.data.scenario.EndingData;
import com.zalinius.questgiver.core.data.scenario.QuestData;
import com.zalinius.questgiver.core.data.scenario.Speaker;
import com.zalinius.questgiver.core.data.scenario.TextboxData;
import com.zalinius.questgiver.core.data.scenario.Time;
import com.zalinius.questgiver.core.data.scenario.TimeOfDay;
import com.zalinius.questgiver.core.gamelogic.people.Hero;
import com.zalinius.questgiver.core.gamelogic.people.HeroController;
import com.zalinius.questgiver.core.gamelogic.task.Task;

public class ScenarioData {

	private String name;
	private Map<Time, List<TextboxData>> gossips;	
	private final Map<Time, List<QuestData>> quests;
	private EndingGroup goodEndings;
	private EndingGroup badEndings;
	private List<EndingData> allEndings; //We also maintain a list of all endings to keep their priority (the order they're made in)
	private List<Hero> heroes;
	private Map<Time, Task> tasks;

	public final static String NEWS = "manager_news";

	public ScenarioData(String name) {
		this.name = name;
		gossips = new HashMap<>();
		quests = new HashMap<>();
		tasks = new HashMap<>();
		goodEndings = new EndingGroup();
		badEndings = new EndingGroup();
		allEndings = new ArrayList<>();
		heroes = new ArrayList<>();
	}


	public void addGossipWithTextbox(int day, TimeOfDay timeOfDay, TextboxData gossip) {
		Time time = new Time(day, timeOfDay);
		gossips.putIfAbsent(time, new ArrayList<>());
		gossips.get(time).add(gossip);
	}
	public void addManagerGossip(int day, TimeOfDay timeOfDay, String gossipKey) {
		addGossipWithTextbox(day, timeOfDay, new TextboxData(gossipKey, Speaker.MANAGER));
	}
	public void addGossipWithSpeaker(int day, TimeOfDay timeOfDay, String gossipKey, Speaker speaker) {
		addGossipWithTextbox(day, timeOfDay, new TextboxData(gossipKey, speaker));
	}
	public void addSeveralGossips(int day, TimeOfDay timeOfDay, Speaker speaker, int numberOfMessages, String gossipKeyBase) {
		addSeveralGossips(day, timeOfDay, speaker, 1, numberOfMessages, gossipKeyBase);
	}
	public void addSeveralGossips(int day, TimeOfDay timeOfDay, Speaker speaker, int startIndex, int endingIndexInclusive, String gossipKeyBase) {
		for (int i = startIndex; i <= endingIndexInclusive; i++) {
			addGossipWithTextbox(day, timeOfDay, new TextboxData(gossipKeyBase + i, speaker));
		}
	}
	public void addSeveralGossips(int day, TimeOfDay timeOfDay, String heroName, int startIndex, int endingIndexInclusive, String gossipKeyBase) {
		for (int i = startIndex; i <= endingIndexInclusive; i++) {
			addGossipWithTextbox(day, timeOfDay, new TextboxData(gossipKeyBase + i, heroName));
		}
	}

	@Override
	public String toString() {
		return "Scenario " + name;
	}

	public Map<Time, List<TextboxData>> getGossip() {
		return gossips;
	}

	public void addQuest(QuestData questData, int day) {
		Time time = new Time(day, TimeOfDay.AM);
		quests.putIfAbsent(time, new ArrayList<>());
		quests.get(time).add(questData);	
	}

	public Map<Time, List<QuestData>> getQuests() {
		return quests;
	}

	public void addTask(int day, TimeOfDay timeOfDay, String descriptionKey) {
		Task task = new Task(descriptionKey);
		Time time = new Time(day, timeOfDay);
		tasks.put(time, task);	
		List<TextboxData> gossipsOfTheDay = gossips.get(time);
		try {
			gossipsOfTheDay.get(gossipsOfTheDay.size()-1).setTask(descriptionKey);
		} catch (NullPointerException e) {
			Gdx.app.error("ScenarioData", "Cannot add a Task at " + time + " since it does not have a scripted Gossip.");
		    throw e;
		}
	}

	public void addDialogueChoice(int day, TimeOfDay timeOfDay, DialogChoiceData dialogueChoiceData) {
		Time time = new Time(day, timeOfDay);
		List<TextboxData> gossipsOfTheDay = gossips.get(time);
		try {
			gossipsOfTheDay.get(gossipsOfTheDay.size()-1).setDialogueChoiceData(dialogueChoiceData);
		} catch (NullPointerException e) {
			Gdx.app.error("ScenarioData", "Cannot add a DialogChoice at " + time + " since it does not have a scripted Gossip.");
		    throw e;
		}
	}

	public Map<Time, Task> getTasks() {
		return tasks;
	}

	public void addEnding(EndingData ending) {
		EndingGroup group = ending.isVictory() ? goodEndings : badEndings;
		group.add(ending);
		allEndings.add(ending);
	}
	
	public void addEndingWithSameIndexAsPrevious(EndingData ending) {
		EndingGroup group = ending.isVictory() ? goodEndings : badEndings;
		group.addWithSameIndexAsPrevious(ending);
		allEndings.add(ending);
	}

	public List<EndingData> getAllEndings() {
		return allEndings;
	}

	public int getNumberOfGoodEndings() {
		return goodEndings.endings.get(goodEndings.endings.size()-1).getEndingNumber() + 1;
	}

	public int getNumberOfBadEndings() {
		if (badEndings.endings.isEmpty()) {
			return 0; 
		}
		return badEndings.endings.get(badEndings.endings.size()-1).getEndingNumber() + 1;
	}

	public void addHero(Hero hero) {
		heroes.add(hero);
	}

	public List<Hero> getHeroes() {
		return heroes;
	}

	public static List<TextboxData> getEntriesAsList(Speaker speaker, String... messageKeys) {
		List<TextboxData> entries = new ArrayList<>();
		for (int i = 0; i < messageKeys.length; i++) {
			entries.add(new TextboxData(messageKeys[i], speaker));	
		}
		return entries;
	}

	public static List<TextboxData> getEntriesAsList(Speaker speaker, int numberOfMessages, String messageKeyBase) {
		List<TextboxData> entries = new ArrayList<>();
		for (int i = 0; i < numberOfMessages; i++) {
			entries.add(new TextboxData(messageKeyBase + (i+1), speaker));	
		}
		return entries;
	}


	public static List<TextboxData> getEntriesAsList(Speaker speaker, int startIndex, int endIndexInclusive, String messageKeyBase) {
		List<TextboxData> entries = new ArrayList<>();
		for (int i = startIndex; i <= endIndexInclusive; i++) {
			entries.add(new TextboxData(messageKeyBase + (i), speaker));	
		}
		return entries;
	}
	
	public static boolean isHeroInGroup(Hero hero, List<HeroController> heroes) {
		return heroes.stream().map(HeroController::getHeroName).anyMatch(name -> name.equalsIgnoreCase(hero.name));
	}
	
	private class EndingGroup {
		private List<EndingData> endings;
		private int index;
		
		public EndingGroup() {
			super();
			this.endings = new ArrayList<>();
			this.index = 0;
		}
		
		public void add(EndingData data) {
			endings.add(data);
			data.setEndingNumber(index);
			index++;
		}
		
		public void addWithSameIndexAsPrevious(EndingData data) {
			endings.add(data);
			data.setEndingNumber(endings.get(endings.size() - 2).getEndingNumber());
		}
		
	}
}
