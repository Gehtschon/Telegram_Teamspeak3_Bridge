package newThings;

import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.telegram.telegrambots.meta.api.objects.Update;

import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import com.github.theholywaffle.teamspeak3.api.wrapper.Message;

public class Telegram {

	// Make Events userbased, make a list of user which have events
	// PrivateMessage edit Message witch From: Message:

	TeUpdate teUpdate;

	private Long chatID;

	private ArrayList<TeUser> userList = new ArrayList<TeUser>();
	private Teamspeak teamspeak;

	public Telegram(Long chatID, TeUpdate teupdate) {
		this.chatID = chatID;
		this.teUpdate = teupdate;

	}

	public Long getChatID() {
		return chatID;
	}

	public void setChatID(Long chatID) {
		this.chatID = chatID;
	}

	public void sendMessageToTelegram(String Message, Long chatID) {
		teUpdate.sendtotelegram(Message, chatID);
	}

	public void newUpdate(Update update) {
		System.out.println("got new update");
		TeEventBuilder eventBuilder = new TeEventBuilder(update, this);
		// System.out.println("eventBuilder is: " + eventBuilder);
		TeEvent event = eventBuilder.getTeEvent();
		// System.out.println("List contains this now: " + event);
		// System.out.println("Eventstatus is: " + this.eventcommand);
		TeUser teuser = new TeUser(this, event.getSubmitter());
		if (userList.contains(teuser)) {
			System.out.println("User Exist");
		} else {
			userList.add(teuser);
			System.out.println("User Created");
		}
		userList.get(userList.lastIndexOf(teuser)).addEvent(event);
		userList.get(userList.lastIndexOf(teuser)).getLastEvent()
				.setEventStatus(userList.get(userList.lastIndexOf(teuser)).getStatus());
		userList.get(userList.lastIndexOf(teuser)).statusUpdate();
		// System.out.println("Eventstatus is: " + this.eventcommand);
	}

//	private void statusUpdate() {
//		// System.out.println("Get eventcommand: " + this.eventcommand);
//
//		if (getLastEvent().getCommand() == TelegramCommands.adminclear) {
//			System.out.println(eventList.size());
//			eventList.clear();
//			System.out.println(eventList.size());
//			return;
//		}
//
//		if (this.eventcommand == null) {
//			// System.out.println("Event == null");
//			TelegramCommands command = getLastEvent().getCommand();
//
//			if (command == null && getLastEvent().isCommand()) {
//				command = TelegramCommands.error;
//			} else if (command == null) {
//				command = TelegramCommands.noCommand;
//			}
//
//			switch (command) {
//			case privatemessage:
//				privateMessage();
//				break;
//
//			case teamspeakstatus:
//				teamspeakStatus();
//				break;
//			case useronline:
//				userOnline();
//				break;
//
//			case adminevent:
//				sendEvents();
//				break;
//
//			case error:
//				sendMessageToTelegram("Command not found!!", chatID);
//				removeLastEvent();
//				break;
//
//			default:
//				removeLastEvent();
//				break;
//			}
//		} else {
//			// System.out.println("Event == " + eventcommand.toString());
//
//			switch (eventcommand) {
//
//			case privatemessage:
//				privateMessage();
//				break;
//
//			default:
//				throw new IllegalArgumentException("Unexpected value: " + eventcommand.toString());
//			}
//		}
//
//	}

	public void userOnline(TeUser user) {
		sendMessageToTelegram(teamspeak.getOnlineUsers(), chatID);
		user.clearfields(TelegramCommands.useronline);

	}

	public void teamspeakStatus(TeUser user) {
		sendMessageToTelegram(teamspeak.getTeamspeakStatus(), chatID);
		user.clearfields(TelegramCommands.teamspeakstatus);

	}

	public void sendEvents(TeUser user) {
//		for (Iterator iterator = eventList.iterator(); iterator.hasNext();) {
//			TeEvent teEvent = (TeEvent) iterator.next();
//			sendMessageToTelegram(teEvent.toString(), chatID);
//		}
		
		for (Iterator iterator = userList.iterator(); iterator.hasNext();) {
			TeUser teUser = (TeUser) iterator.next();
			ArrayList<TeEvent> list = teUser.getEventList();
			for (Iterator iterator2 = list.iterator(); iterator2.hasNext();) {
				TeEvent teEvent = (TeEvent) iterator2.next();
				String message = teUser.getUsername();
				message = message + ":" + "\n";
				message = message + teEvent.toString();
				sendMessageToTelegram(teEvent.toString(), chatID);
			}
		}
		user.clearfields(TelegramCommands.adminevent);

	}

	public void privateMessage(TeUser user) {
		// this.eventcommand = TelegramCommands.privatemessage;

		switch (user.getStatus()) {
		case NotUsed:
			sendMessageToTelegram(teamspeak.getOnlineUsers(), chatID);
			user.setStatus(EventStatus.waitingOnUser);
			break;
		case waitingOnUser:
			String tempuser = user.getLastEvent().getRecipient();
			if(tempuser == null) {
				tempuser = "";
			}
			Client client = teamspeak.getClientByName(tempuser);
			if(client != null) {
				user.setClient(teamspeak.getClientByName(tempuser));
				sendMessageToTelegram("Enter a Message", chatID);
				user.setStatus(EventStatus.waitingOnMessage);
				break;
			}else {
				sendMessageToTelegram("User not found, try again!", chatID);
				user.setStatus(EventStatus.NotUsed);
				privateMessage(user);
				break;
			}

		case waitingOnMessage:
			if (user.getClient() != null) {
				String message = "From: " + user.getLastEvent().getSubmitter();
				message = message + "\n";
				message = message + "Message: " + user.getLastEvent().getMessage();
				teamspeak.sendClientMessage(message, user.getClient());
			}
			user.clearfields(TelegramCommands.privatemessage);
			break;

		default:
			throw new IllegalArgumentException("Unexpected value: " + user.getStatus().toString());
		}

	}

	public void setTeamspeak(Teamspeak teamspeak) {
		this.teamspeak = teamspeak;

	}

}
