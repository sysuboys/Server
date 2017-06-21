package org.sysuboys.diaryu.business.service;

import java.util.Map;

import org.sysuboys.diaryu.business.model.ExchangeModel;

/**
 * 只是用于获取唯一的map（多线程安全）
 *
 */
public interface ExchangeModelService {

	Map<String, ExchangeModel> get();

}
