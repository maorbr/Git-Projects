package com.hit.client;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import com.hit.view.CacheUnitView;

public class CacheUnitClientObserver implements PropertyChangeListener {
	private CacheUnitClient cacheUnitClient;
	private CacheUnitView cacheUnitView;

	public CacheUnitClientObserver() {
		cacheUnitClient = new CacheUnitClient();
	}

	public void propertyChange(PropertyChangeEvent e) {
		String request = null;
		cacheUnitView = (CacheUnitView) e.getSource();

		if (e.getPropertyName() == "loadJsonFile") {
			request = cacheUnitClient.send(e.getNewValue().toString());
			System.out.println("Server reply to [Load a Request] button:\n" + request);
		} else if (e.getPropertyName() == "statistics") {
			request = cacheUnitClient.send("{\"headers\":{\"action\":\"STATISTICS\"},\"body\":[]}\n");
			System.out.println("Server reply to [Show Statistics] button:\n" + request);
		}else if (e.getPropertyName() == "about") {
			request = "Signed by: \n1. Shelly Alfasiþ \n2. Maor Bracha";
			System.out.println("Server reply to [About us] button:\n" + request);
		}
		cacheUnitView.updateUIData(request);
	}


}