package com.wooplr.base.driver.web;


public enum BrowserType {
	FireFox("*firefox"),
	Chrome("*chrome"),
	InternetExplore("*iexplore"),
	Safari("*safari"),
	Opera("*opera"),
	Android("*android"),
	IPhone("*iphone"),
	IPad("*ipad"),
	PhantomJS("*phantomjs"),
	HtmlUnit("*htmlunit");
	
	public static BrowserType getBrowserType(String type) {
		if (type == null) {
			return BrowserType.FireFox;
		}
		for (BrowserType browserType : BrowserType.values()) {
			if (browserType.getType().contains(type.toLowerCase())) {
				return browserType;
			}
		}
		return BrowserType.FireFox;
	}
	
	private String type;
	
	BrowserType(String type) {
		this.type = type;
	}
	
	public String getType(){
		return this.type;
	}

}
