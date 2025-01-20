package com.darzalgames.libgdxtools.steam;

import com.codedisaster.steamworks.SteamFriends.PersonaChange;
import com.darzalgames.libgdxtools.ui.input.universaluserinput.inputpriority.InputPriorityManager;
import com.codedisaster.steamworks.SteamFriendsCallback;
import com.codedisaster.steamworks.SteamID;
import com.codedisaster.steamworks.SteamResult;

public class FriendsCallback implements SteamFriendsCallback {

	@Override
	public void onSetPersonaNameResponse(boolean success, boolean localSuccess, SteamResult result) { /* To be implemented when needed */ }

	@Override
	public void onPersonaStateChange(SteamID steamID, PersonaChange change) { /* To be implemented when needed */ }

	@Override
	public void onGameOverlayActivated(boolean active) {
		if (active) {
			InputPriorityManager.pauseIfNeeded();
		}
	}

	@Override
	public void onGameLobbyJoinRequested(SteamID steamIDLobby, SteamID steamIDFriend) { /* To be implemented when needed */ }

	@Override
	public void onAvatarImageLoaded(SteamID steamID, int image, int width, int height) { /* To be implemented when needed */ }

	@Override
	public void onFriendRichPresenceUpdate(SteamID steamIDFriend, int appID) { /* To be implemented when needed */ }

	@Override
	public void onGameRichPresenceJoinRequested(SteamID steamIDFriend, String connect) { /* To be implemented when needed */ }

	@Override
	public void onGameServerChangeRequested(String server, String password) { /* To be implemented when needed */ }

}
