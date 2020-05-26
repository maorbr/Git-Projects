package com.hit.memory;

import com.hit.algorithm.IAlgoCache;
import com.hit.dm.DataModel;

public class CacheUnit<T>{
	private IAlgoCache<Long, DataModel<T>> algo;

	public CacheUnit(IAlgoCache<Long, DataModel<T>> algo) {
		this.algo = algo;
	}

	@SuppressWarnings("unchecked")
	public DataModel<T>[] getDataModels(Long[] ids) {

		DataModel<T>[] dataModelArray = new DataModel[ids.length];
		for (int i = 0; i < ids.length; i++) {
			dataModelArray[i] = algo.getElement(ids[i]);
		}
		return dataModelArray;
	}

	public DataModel<T>[] putDataModels(DataModel<T>[] datamodels) {
		for (int i = 0; i < datamodels.length; i++) {
			algo.putElement(datamodels[i].getDataModelId(), datamodels[i]);
		}
		return datamodels;
	}

	public void removeDataModels(Long[] ids) {
		for (int i = 0; i < ids.length; i++) {
			algo.removeElement(ids[i]);
		}
	}

	public IAlgoCache<Long, DataModel<T>> getAlgo() {
		return algo;
	}

}
