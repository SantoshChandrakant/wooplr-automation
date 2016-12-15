package com.wooplr.base.reporter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;

import com.wooplr.base.controller.AbstractPageListener;
import com.wooplr.base.driver.web.IPage;
import com.wooplr.base.reporter.pluginmodel.Method;
import com.wooplr.base.reporter.pluginmodel.Page;
import com.wooplr.base.reporter.pluginmodel.Plugin;
import com.wooplr.base.reporter.pluginmodel.Reportplugins;
import com.wooplr.base.reporter.pluginmodel.Test;


public class PluginsUtil {
	private static final Logger logger = Logger.getLogger(PluginsUtil.class);
	private static Map<String, AbstractPageListener> pageListenerMap = Collections
			.synchronizedMap(new HashMap<String, AbstractPageListener>());

	private static final PluginsUtil _instance = new PluginsUtil();

	public static synchronized PluginsUtil getInstance() {
		return _instance;
	}

	private Reportplugins _mauiPlugins = null;

	public Reportplugins getMauiplugins() {
		return _mauiPlugins;
	}

	public List<AbstractPageListener> getPageListeners() {
		List<AbstractPageListener> tempPageListenerList = Collections
				.synchronizedList(new ArrayList<AbstractPageListener>());
		tempPageListenerList.addAll(pageListenerMap.values());

		return tempPageListenerList;
	}

	public void invokePageListeners(String testMethodSignature, IPage page,
			boolean isPageLoad) {

		if (_mauiPlugins == null)
			return;

		List<AbstractPageListener> pageListenerList = new ArrayList<AbstractPageListener>();

		for (Plugin plugin : _mauiPlugins.getPlugin()) {
			if (isPageListenerApplicable(plugin, testMethodSignature, page
					.getClass().getCanonicalName()))
				pageListenerList.add(pageListenerMap.get(plugin.getClassName()
						.trim()));
		}

		for (AbstractPageListener listener : pageListenerList) {
			try {
				if (isPageLoad)
					listener.onPageLoad(page);
				else
					listener.onPageUnload(page);
			} catch (Throwable e) {
				logger.error(e);
			}
		}
	}

	public boolean isPageListenerApplicable(Plugin plugin,
			String testMethodSignature, String pageClassName) {
		if (testMethodSignature == null)
			return true;// for null context sinario
		boolean testFound = false;
		for (Test test : plugin.getTest()) {
			if (testMethodSignature.matches(test.getClassName() + "\\.\\w.*")) {
				// Take care of test class level pages
				for (Page page : test.getPage()) {
					if (pageClassName.matches(page.getClassName())) {
						// return true;
						testFound = true;
						break;
					}
				}

				if (testFound) {
					// Now let's look at the method level pages
					for (Method method : test.getMethod()) {
						if (testMethodSignature.matches(test.getClassName()
								+ "\\." + method.getName() + ".*")) {
							for (Page page : method.getPage()) {
								if (pageClassName.matches(page.getClassName()))
									return true;
							}
							return false;
						}
					}
					return true;
				}
			}
		}

		return false;
	}

	public boolean isTestResultEffected(String pageListenerClassName) {
		AbstractPageListener listener = pageListenerMap
				.get(pageListenerClassName);
		if (listener != null)
			return listener.isTestResultEffected();
		return false;
	}

	public void loadPlugins(File path) {
		logger.info("Loading MAUI Plugins from " + path + " ...");

		InputStream is = null;
		try {
			is = new FileInputStream(path);
			JAXBContext jc = JAXBContext
					.newInstance("com.spire.maui.reporter.pluginmodel");
			Unmarshaller u = jc.createUnmarshaller();
			_mauiPlugins = (Reportplugins) u.unmarshal(is);

			for (Plugin plugin : _mauiPlugins.getPlugin()) {
				try {
					pageListenerMap
							.put(plugin.getClassName().trim(),
									(AbstractPageListener) Class.forName(
											plugin.getClassName().trim())
											.newInstance());
				} catch (Exception e) {
					logger.error("Unable to load MAUI Plugins.", e);
				}
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
				}
			}
		}
	}
}
