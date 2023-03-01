package newThings;

import java.util.ArrayList;

import com.github.theholywaffle.teamspeak3.api.wrapper.Client;

public class TeUser {

	private String username;
	private Telegram telegram;

	private TelegramCommands eventcommand;
	private EventStatus status = EventStatus.NotUsed;
	private ArrayList<TeEvent> eventList = new ArrayList<TeEvent>();

	private Client client;

	public TeUser(Telegram telegram, String username) {
		super();
		this.telegram = telegram;
		this.username = username;
	}

	public void statusUpdate() {
		// System.out.println("Get eventcommand: " + this.eventcommand);

		if (getLastEvent().getCommand() == TelegramCommands.adminclear) {
			System.out.println(eventList.size());
			eventList.clear();
			System.out.println(eventList.size());
			return;
		}

		if (this.eventcommand == null) {
			// System.out.println("Event == null");
			eventcommand = getLastEvent().getCommand();

			if (eventcommand == null && getLastEvent().isCommand()) {
				eventcommand = TelegramCommands.error;
			} else if (eventcommand == null) {
				eventcommand = TelegramCommands.noCommand;
			}

		}
		System.out.println("UserEvent is: " + eventcommand.toString());
		switch (eventcommand) {
		case privatemessage:
			telegram.privateMessage(this);
			removeLastEvent();
			break;

		case teamspeakstatus:
			telegram.teamspeakStatus(this);
			removeLastEvent();
			break;
		case useronline:
			telegram.userOnline(this);
			removeLastEvent();
			break;

		case adminevent:
			telegram.sendEvents(this);
			removeLastEvent();
			break;

		case error:
			telegram.sendMessageToTelegram("Command not found!!", telegram.getChatID());
			clearfields(eventcommand);
			removeLastEvent();
			break;

		default:
			removeLastEvent();
			eventcommand = null;
			break;
		}
//		} else {
//			// System.out.println("Event == " + eventcommand.toString());
//
//			switch (eventcommand) {
//
//			case privatemessage:
//				privateMessage(this);
//				break;
//
//			default:
//				throw new IllegalArgumentException("Unexpected value: " + eventcommand.toString());
//			}
//		}

	}

	public void addEvent(TeEvent event) {
		if (eventList.contains(event)) {

		} else
			eventList.add(event);
	}

	public void clearfields(TelegramCommands command) {
		switch (command) {
		case privatemessage:
			client = null;
			status = EventStatus.NotUsed;
			eventcommand = null;
			System.out.println("Clear all fields");
			break;

		case teamspeakstatus:
			status = EventStatus.NotUsed;
			eventcommand = null;
			break;

		case useronline:
			status = EventStatus.NotUsed;
			eventcommand = null;
			break;
		case error:
			status = EventStatus.NotUsed;
			eventcommand = null;
			break;

		default:
			status = EventStatus.NotUsed;
			eventcommand = null;
			break;
		}
	}

	@Override
	public boolean equals(Object obj) {

		if (this == obj) {
			return true;
		}

		if (obj == null) {
			return false;
		}

		if (getClass() != obj.getClass()) {
			return false;
		}

		TeUser tempuser = (TeUser) obj;

		return ((tempuser.getUsername().equals(this.getUsername())) && (tempuser.getTelegram() == this.getTelegram()));

	}

	public TeEvent getLastEvent() {
		// System.out.println("List size is: " + eventList.size());
		return eventList.get(eventList.size() - 1);
	}

	private void removeLastEvent() {
		eventList.remove(getLastEvent());
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Telegram getTelegram() {
		return telegram;
	}

	public void setTelegram(Telegram telegram) {
		this.telegram = telegram;
	}

	public EventStatus getStatus() {
		return status;
	}

	public void setStatus(EventStatus status) {
		this.status = status;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public ArrayList<TeEvent> getEventList() {
		return eventList;
	}

}
