package newThings;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class TeUpdate extends TelegramLongPollingBot {

	MainController mainController;

	public TeUpdate() {

		System.out.println("TeUpdate created");

	}

	public TeUpdate(MainController mainController) {
		this.mainController = mainController;
		System.out.println("TeUpdate created");

	}

	@Override
	public void onUpdateReceived(Update update) {
		// TODO Auto-generated method stub
		System.out.println("income");
		mainController.onTelegramUpdate(update);

	}

	@Override
	public String getBotUsername() {
		// TODO Auto-generated method stub
		return "Gehtschonts3bot";
	}

	@Override
	public String getBotToken() {
		// TODO Auto-generated method stub
		return "668418475:AAEhZps96wB-Cjo7iJdQ42V6yIX1IjTRNqk";
	}

	public void sendtotelegram(String text, Long chatID) {
		SendMessage sendMessage = new SendMessage();
		// Test Chat
		sendMessage.setChatId(chatID.toString());
		sendMessage.setText(text);
		try {
			execute(sendMessage);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
