package newThings;

import java.util.ArrayList;
import java.util.Iterator;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import com.github.theholywaffle.teamspeak3.api.wrapper.Message;

public class MainController {

	private TeUpdate teupdate;
	private Teamspeak teamspeak;
	private ArrayList<Telegram> telegramlist = new ArrayList<Telegram>();

	public MainController() {
		init();
	}

	private void init() {
		// Create TelegramEventListener
		teupdate = new TeUpdate(this);
		try {
			TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
			telegramBotsApi.registerBot(teupdate);
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
		teamspeak = new Teamspeak(this);
		System.out.println("startup done");

	}

	public void onTelegramUpdate(Update update) {
		Boolean existing = false;
		Long chatID = update.getMessage().getChatId();
		Telegram telegram = getTelegram(chatID);
		System.out.println("got into reurn with: " + telegram);
		if (telegram != null) {
			System.out.println("?????????????????????????????????????????");
			telegram.setTeamspeak(teamspeak);
			telegram.newUpdate(update);
		} else {
			System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			telegramlist.add(new Telegram(chatID, teupdate));
			onTelegramUpdate(update);
		}

//		for (Iterator iterator = telegramlist.iterator(); iterator.hasNext();) {
//			Telegram telegram2 = (Telegram) iterator.next();
//			if (telegram2.getChatID() == telegram.getChatID()) {
//				existing = true;
//				// now do soemthing with telegram if it exists
//				telegram2.newUpdate(update);
//			}
//		}
//		if (existing == false) {
//			//telegram doesnt exists
//			telegramlist.add(telegram);
//			
//		}

	}

	private Telegram getTelegram(Long chatID) {
		System.out.println("List size is: " + telegramlist.size());
		for (Iterator iterator = telegramlist.iterator(); iterator.hasNext();) {
			Telegram telegram = (Telegram) iterator.next();
			System.out.println("chatID = " + chatID + " telegram.chatID = " + telegram.getChatID());
			if (chatID.equals(telegram.getChatID())) {
				System.out.println("got into reurn with: " + telegram);
				return telegram;
			}
		}
		return null;
	}

}
