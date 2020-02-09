package com.hit.services;

import com.hit.algorithm.IAlgoCache;
import com.hit.algorithm.LRUAlgoCacheImpl;
import com.hit.algorithm.NRUAlgoCacheImpl;
import com.hit.algorithm.RandomAlgoCacheImpl;
import com.hit.dao.DaoFileImpl;
import com.hit.dao.IDao;
import com.hit.dm.DataModel;
import com.hit.memory.CacheUnit;
import com.hit.util.CLI;

public class CacheUnitService<T> {

	private IDao<Long, DataModel<T>> dao;
	private CacheUnit<T> cacheUnit;
	private IAlgoCache<Long, DataModel<T>> algo;
	private  int swapCount = 0;
	private  int requestCount = 0;
	private  int requestCountDM = 0;

	public CacheUnitService() {

		String selectedAlgo = CLI.getSelectedAlgo();
		int capacity = CLI.getCapacityAlgo();
		
		
		if (selectedAlgo.equals("LRU")) {
			algo = new LRUAlgoCacheImpl<>(capacity);
		} else if (selectedAlgo.equals("MRU")) {
			algo = new NRUAlgoCacheImpl<>(capacity);
		} else
			algo = new RandomAlgoCacheImpl<>(capacity);

		cacheUnit = new CacheUnit<T>(algo);
		dao = new DaoFileImpl<>("datasource.txt",20);
	}

	public boolean update(DataModel<T>[] dataModels) {
		
		DataModel<T>[] dataModelsUpdate = cacheUnit.putDataModels(dataModels);
		
		for(DataModel<T> dm:dataModelsUpdate)
		{
			dao.save(dm);
		}
		requestCount++;
		swapCount++;
		requestCountDM += dataModels.length;
		return true;
	}

	public boolean delete(DataModel<T>[] dataModels) {

		Long[] ids = new Long[dataModels.length];

		for (int i = 0; i < ids.length; i++) {
			ids[i] = dataModels[i].getDataModelId();
		}
		cacheUnit.removeDataModels(ids);

		for (int i = 0; i < dataModels.length; i++) {
			dao.delete(dataModels[i]);
		}

		requestCount++;
		requestCountDM += dataModels.length;
		return true;
	}

	public DataModel<T>[] get(DataModel<T>[] dataModels) {
		DataModel<T> dataModelsFromIDao = null;
		Long[] ids = new Long[dataModels.length];
		for (int i = 0; i < ids.length; i++) {
			ids[i] = dataModels[i].getDataModelId();
		}

		DataModel<T>[] dataModelsFromcacheUnit = cacheUnit.getDataModels(ids);

		for (int i = 0; i < dataModelsFromcacheUnit.length; i++) {
			if (dataModelsFromcacheUnit[i] == null) {
				dataModelsFromIDao = dao.find(ids[i]);
				if (dataModelsFromIDao != null) {
					swapCount++;
					dataModelsFromcacheUnit[i] = new DataModel<T>(dataModelsFromIDao.getDataModelId(), dataModelsFromIDao.getContent());
				}
			}
		}

		requestCount++;	
		requestCountDM += dataModels.length;

		return dataModelsFromcacheUnit;
	}

	public String getStatistics() {
		StringBuilder str = new StringBuilder();
		str.append("Capacity: ");
		str.append(CLI.getCapacityAlgo());
		str.append("\n");
		str.append("Algorithm: ");
		str.append(CLI.getSelectedAlgo());
		str.append("\n");
		str.append("Total number of requests: ");
		str.append(this.requestCount);
		str.append("\n");
		str.append("Total number of DataModels (GET/DELETE/UPDATE requests): ");
		str.append(this.requestCountDM);
		str.append("\n");
		str.append("Total number of DataModels swaps (from Cache to Disk): ");
		str.append(this.swapCount);
		str.append("\n");

		return str.toString();
	}
}
