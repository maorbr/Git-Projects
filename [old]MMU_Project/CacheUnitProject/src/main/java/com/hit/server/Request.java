package com.hit.server;

import java.io.Serializable;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class Request<T> implements Serializable {
	
	private static final long serialVersionUID = 8553301636784820828L;
	private T body;
	private Map<String, String> headers;

	public Request(Map<String, String> headers, T body) {
		setHeaders(headers);
		setBody(body);
	}
	
	public Map<String,String> getHeaders(){
		return headers;
		
	}
	
	public void setHeaders(Map<String,String> headers)
	{
		this.headers = headers;
	}
	
	public T getBody(){
		return body;
		
	}
	
	public void setBody(T body){
		this.body = body;
	}
	
	@Override
	public String toString(){
		GsonBuilder gsonBuilder = new GsonBuilder();
		Gson gson = gsonBuilder.create();
		return gson.toJson(this);
		
	}


}
