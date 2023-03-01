package newThings;

import org.telegram.telegrambots.meta.api.objects.Update;

public class TeEventBuilder {

	Telegram telegram;

	TeEvent event;

	Long chatID;
	String submitter;
	String message;
	Boolean isCommand;

	public TeEventBuilder(Update update, Telegram telegram) {
		this.telegram = telegram;
		this.chatID = update.getMessage().getChatId();
		this.message = update.getMessage().getText();
		this.isCommand = update.getMessage().isCommand();
		this.submitter = update.getMessage().getFrom().getUserName();
		this.event = createTeEvent();
	}

	private TeEvent createTeEvent() {
		TeEvent event = new TeEvent(chatID, isCommand, submitter, message);
		return event;
	}

	public TeEvent getTeEvent() {
		return event;
	}

}
