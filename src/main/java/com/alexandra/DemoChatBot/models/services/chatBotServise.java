package com.alexandra.DemoChatBot.models.services;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import com.fasterxml.jackson.databind.ser.std.StdKeySerializers.Default;

import Handles.handleTesting;
import Handles.model;

@Service
public class chatBotServise {

	public chatBotServise() {
		try {
			handleTesting handle= new handleTesting();
			model modelo=new model();
			
			TelegramBotsApi telegramBot= new TelegramBotsApi(DefaultBotSession.class);
		    telegramBot.registerBot(modelo);
		
		}catch(Exception e) {
			e.printStackTrace();
		}
	
	}


}
