package com.hit.dm;

import java.io.Serializable;

public class DataModel<T> implements Serializable {

	private static final long serialVersionUID = 7319320398588842585L;
	private Long id;
	private T content;

	public DataModel(Long id, T content) {
		this.id = id;
		this.content = content;
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this.hashCode() == obj.hashCode())
			return true;
		return false;
	}

	@Override
	public String toString() {
		return new String((byte[]) content);
	}

	public Long getDataModelId() {
		return this.id;

	}

	public void setDataModelId(Long id) {
		this.id = id;
	}

	public T getContent() {
		return this.content;
	}

	public void setContent(T content) {
		this.content = content;
	}

}
