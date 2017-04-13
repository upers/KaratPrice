package com.karat.models;

public class RouteManager {
	private String uri;
	private String[] uriArr;
	
	public RouteManager(String uri) {
		this.uri = uri;
		if (uri != null)
			uriArr= uri.split("/");
		else
			uriArr = new String[1];
	}
	
	public String getUri() {
		return uri;
	}
	
	public String getRouteElement(int elementNumber) {
		if (elementNumber > uriArr.length - 1 || elementNumber < 1)
			return null;
		
		return uriArr[elementNumber];
	}
	
	public int getRouteElementsAmount() {
		return uriArr.length - 1;
	}
	
	@Override
	public String toString() {
		return uri;
	}
}
