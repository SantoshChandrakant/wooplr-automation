package com.wooplr.base.driver.web;


public interface IScreenshotListener {

	//below method was commented and insted exposed onScreenshotCaptured method.
	/*public void doScreenCapture(String pageId, String rlogId, String pageTitle,
			String imgType, String imgPath);*/
	
	public void onScreenshotCaptured(ScreenShot screenshot); 

}
