package newThings;

import java.util.Iterator;
import java.util.List;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.github.theholywaffle.teamspeak3.TS3Query.FloodRate;
import com.github.theholywaffle.teamspeak3.api.event.ChannelCreateEvent;
import com.github.theholywaffle.teamspeak3.api.event.ChannelDeletedEvent;
import com.github.theholywaffle.teamspeak3.api.event.ChannelDescriptionEditedEvent;
import com.github.theholywaffle.teamspeak3.api.event.ChannelEditedEvent;
import com.github.theholywaffle.teamspeak3.api.event.ChannelMovedEvent;
import com.github.theholywaffle.teamspeak3.api.event.ChannelPasswordChangedEvent;
import com.github.theholywaffle.teamspeak3.api.event.ClientJoinEvent;
import com.github.theholywaffle.teamspeak3.api.event.ClientLeaveEvent;
import com.github.theholywaffle.teamspeak3.api.event.ClientMovedEvent;
import com.github.theholywaffle.teamspeak3.api.event.PrivilegeKeyUsedEvent;
import com.github.theholywaffle.teamspeak3.api.event.ServerEditedEvent;
import com.github.theholywaffle.teamspeak3.api.event.TS3Listener;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;

public class Teamspeak {

	public static final TS3Config config = new TS3Config();
	public static TS3Query query;
	public static TS3Api api;
	private MainController mainController;

	public Teamspeak() {
		init();

	}

	public Teamspeak(MainController mainController) {
		this.mainController = mainController;
		init();
	}

	public static void init() {
		config.setHost(ID.ip);
//		config.setQueryPort(10011);
		config.setFloodRate(FloodRate.DEFAULT);
		// config.setEnableCommunicationsLogging(true);
		query = new TS3Query(config);
		query.connect();
		api = query.getApi();
		api.login(ID.username, ID.password);
		api.selectVirtualServerById(1);
		api.setNickname(ID.nickname);
		final int botID = api.whoAmI().getId();
		api.moveClient(botID, api.getChannelByNameExact("Botchanel", true).getId());
		api.sendChannelMessage(ID.nickname + " is online!");
		api.registerAllEvents();
		api.addTS3Listeners(new TS3Listener() {

			@Override
			public void onTextMessage(TextMessageEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onServerEdit(ServerEditedEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPrivilegeKeyUsed(PrivilegeKeyUsedEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onClientMoved(ClientMovedEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onClientLeave(ClientLeaveEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onClientJoin(ClientJoinEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onChannelPasswordChanged(ChannelPasswordChangedEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onChannelMoved(ChannelMovedEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onChannelEdit(ChannelEditedEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onChannelDescriptionChanged(ChannelDescriptionEditedEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onChannelDeleted(ChannelDeletedEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onChannelCreate(ChannelCreateEvent e) {
				// TODO Auto-generated method stub

			}
		});
	}

	private long serverStatus() {
		try {
			return api.getServerInfo().getUptime();
		} catch (Exception e) {
			// TODO: handle exception
			return 0;
		}

	}
	
	
	public String getTeamspeakStatus() {
		String message = "";
		Long uptime = serverStatus();
		if (uptime == 0) {
			message = "Server is down";
		} else {
			int h = (int) (uptime / 3600);
			int d = h / 24;
			h = h - d * 24;
			int m = (int) (uptime % 3600 / 60);
			int s = (int) (uptime % 60);
			message = "TeamSpeakServer is up since: " + "\n" + "Days: " + d + "\n" + "Hours: " + h + "\n"
					+ "Minutes:" + m + "\n" + "Seconds: " + s;
		}
		
		return message;

	}

	

	private List<Client> getOnlineUser() {
		List<Client> clients = api.getClients();
		return clients;

	}
	
	public String getOnlineUsers() {
		List<Client> clients = getOnlineUser();
		String message = "";
		String user = "";
		if (clients.isEmpty()) {
			message = "No users online";
		} else {
			for (Iterator iterator = clients.iterator(); iterator.hasNext();) {
				Client client = (Client) iterator.next();
				// ToDO Create String with all Users (, As Spaces)
				if (client.isServerQueryClient()) {
					user = "Bot: " + client.getNickname();
				} else {
					user = "/" + client.getNickname();
				}
				message = message + "\n" + user;
			}
		}


		return message;

	}

	public Client getClientByName(String name) {
		return api.getClientByNameExact(name, true);

	}

	public void sendChannelMessage(String text, String usereName) {
		api.sendChannelMessage("Telegram " + usereName + " Message:" + text);

	}

	public void sendClientMessage(String message, Client client) {
		int clientID = client.getId();
		api.sendPrivateMessage(clientID, message);
	}

}
