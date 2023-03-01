package newThings;

import java.util.Iterator;
import java.util.List;

import com.github.theholywaffle.teamspeak3.api.wrapper.Client;

public class TeEvent {

	private Long chatID;
	private String message;
	private Boolean isCommand;
	private TelegramCommands command;
	private EventStatus eventStatus;

	private String recipient;
	private String submitter;

	public TeEvent(Long chatID, Boolean isCommand, String submitter, String message) {
		this.chatID = chatID;
		this.isCommand = isCommand;
		this.message = message;
		this.submitter = submitter;

	}

	private void setCommand() {
		System.out.println("setting command: " + isCommand);
		if (isCommand) {
			System.out.println("setting command Message: " + message);
			this.command = getTelegramCommand(message);
		}

	}

	public TelegramCommands getCommand() {
		return command;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	private TelegramCommands getTelegramCommand(String commandText) {
		TelegramCommands output = null;
		commandText = commandText.replace("/", "");
		commandText = commandText.replace("@Gehtschonts3bot", "");
		System.out.println("commandText is: " + commandText);

		switch (eventStatus) {
		case waitingOnUser:
			recipient = commandText.replace("/", "");
			break;
		case waitingOnMessage:
			break;

		default:
			for (TelegramCommands commands : TelegramCommands.values())

				if (commandText.contains(commands.toString())) {
					output = commands;
				}
		}

		return output;

	}

	public boolean isCommand() {
		return this.isCommand;
	}

	public EventStatus getEventStatus() {
		return eventStatus;
	}

	public void setEventStatus(EventStatus eventStatus) {
		this.eventStatus = eventStatus;
		if (isCommand) {
			setCommand();
		}
	}

	public String getRecipient() {
		return recipient;
	}

	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}

	public String getSubmitter() {
		return submitter;
	}

	public void setSubmitter(String submitter) {
		this.submitter = submitter;
	}

	public void setCommand(TelegramCommands command) {
		this.command = command;
	}

}
