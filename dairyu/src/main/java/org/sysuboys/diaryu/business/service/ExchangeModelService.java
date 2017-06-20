package org.sysuboys.diaryu.business.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;
import org.sysuboys.diaryu.business.model.ExchangeModel;

@Service
public class ExchangeModelService implements IExchangeModelService {

	Map<String, ExchangeModel> map = new ConcurrentHashMap<String, ExchangeModel>();

	public Map<String, ExchangeModel> get() {
		return map;
	}

}
