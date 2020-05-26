package com.hit.dao;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import com.hit.dm.DataModel;

public class DaoFileImpl<T> implements IDao<Long, DataModel<T>> {
	private String filePath;
	private int capacity = 5;
	private Map<Long, DataModel<T>> stream;

	public DaoFileImpl(String filePath) {
		this.filePath = filePath;
		bulidMap();
	}

	public DaoFileImpl(String filePath, int capacity) {
		this.filePath = filePath;
		this.capacity = capacity;
		bulidMap();
	}

	private void bulidMap() {
		stream = new LinkedHashMap<Long, DataModel<T>>(capacity);
		for (int i = 0; i < capacity; i++)
			stream.put((long) i, new DataModel<T>((long) i, null));
		try {
			writeMaptoFile();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void writeMaptoFile() throws FileNotFoundException, IOException {
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filePath));
		out.writeObject(stream);
		out.close();
	}

	@SuppressWarnings("unchecked")
	private void readFromFileToMap() throws FileNotFoundException, IOException,
			ClassNotFoundException {
		ObjectInputStream in = new ObjectInputStream(new FileInputStream(filePath));
		stream = (Map<Long, DataModel<T>>) in.readObject();
		in.close();
	}

	@Override
	public void save(DataModel<T> entity) {

		try {
			readFromFileToMap();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (stream.get(entity.getDataModelId()) != null) {
			stream.put(entity.getDataModelId(), entity);
			
			try {
				writeMaptoFile();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void delete(DataModel<T> entity) {
		try {
			readFromFileToMap();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (stream.get(entity.getDataModelId()) != null) {
			stream.put(entity.getDataModelId(), null);
			try {
				writeMaptoFile();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public DataModel<T> find(Long id) {
		try {
			readFromFileToMap();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return stream.get(id);
	}
}
