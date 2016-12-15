package com.san.base.util;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import com.wooplr.base.controller.Logging;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vinay Kumar on 27-05-2015.
 */
public class TableUtil {

	/***
	 * Returns List of Cell elements
	 *
	 * @param id
	 * @param driver
	 * @return
	 */
	public List<WebElement> getTableCellElements(String id, WebDriver driver) {
		List<WebElement> cellElements = new ArrayList<WebElement>();
		// Grab the table
		WebElement table = driver.findElement(By.id(id));
		// Now get all the TR elements from the table
		List<WebElement> allRows = table.findElements(By.tagName("tr"));
		// And iterate over them, getting the cells
		for (WebElement row : allRows) {
			cellElements = row.findElements(By.tagName("td"));
		}
		return cellElements;
	}

	/***
	 * returns cell headers
	 *
	 * @param id
	 * @param driver
	 * @return
	 */
	public List<WebElement> getTableCellHeaders(String id, WebDriver driver) {
		List<WebElement> columns = new ArrayList<WebElement>();
		// Grab the table
		WebElement table = driver.findElement(By.id(id));
		// Now get all the TR elements from the table
		List<WebElement> allRows = table.findElements(By.tagName("tr"));
		// And iterate over them, getting the cells
		for (WebElement row : allRows) {
			columns = row.findElements(By.tagName("div"));
			break;
		}
		return columns;
	}

	/*
	 * public String getCellXpath(String cellValue, String id, WebDriver driver)
	 * {
	 * 
	 * String xpath = ""; List<WebElement> columns = new
	 * ArrayList<WebElement>(); // Grab the table WebElement table =
	 * driver.findElement(By.id(id)); // Now get all the TR elements from the
	 * table List<WebElement> allRows = table.findElements(By.tagName("tr")); //
	 * And iterate over them, getting the cells int rowNo = 0; int columnNo = 0;
	 * for (WebElement row : allRows) { rowNo = rowNo + 1; columns =
	 * row.findElements(By.tagName("td")); for (WebElement column : columns) {
	 * columnNo = columnNo + 1; return "/
	 *//*
		 * [@id='" + id + "']/table/tbody/tr[" + (rowNo) + "]/td[" + columnNo +
		 * "]"; }
		 * 
		 * }
		 * 
		 * return xpath;
		 * 
		 * }
		 */
	/*
	 * public String getCellXpath1(String cellValue, String id, WebDriver
	 * driver) { WebElement table = driver.findElement(By.id(id)); return "/
	 *//*
		 * [@id='" + id + "']/table/tbody/tr[td//text()[contains(., '" +
		 * cellValue + "')]]"; }
		 */

	/***
	 * returns row Count
	 *
	 * @param id
	 * @param driver
	 * @return
	 */
	public int getRowCount(String id, WebDriver driver) {
		WebElement table = driver.findElement(By.id(id));
		List<WebElement> allRows = table.findElements(By.tagName("tr"));
		return allRows.size();
	}

	/***
	 * returns Column count .
	 *
	 * @param id
	 * @param driver
	 * @return
	 */
	public int getColumnCount(String id, WebDriver driver) {
		List<WebElement> cell = new ArrayList<WebElement>();
		int columnIndex = 1;
		// Grab the table
		WebElement table = driver.findElement(By.id(id));
		// Now get all the TR elements from the table
		List<WebElement> allRows = table.findElements(By.tagName("tr"));
		// And iterate over them, getting the cells
		for (WebElement row : allRows) {
			cell = row.findElements(By.tagName("td"));
		}
		return cell.size();
	}

	/***
	 * returns Cell count
	 *
	 * @param id
	 * @param driver
	 * @return
	 */
	public int getCellCount(String id, WebDriver driver) {
		int columnIndex = 1;
		// Grab the table
		WebElement table = driver.findElement(By.id(id));
		// Now get all the TR elements from the table
		List<WebElement> allRows = table.findElements(By.tagName("tr"));
		// And iterate over them, getting the cells
		for (WebElement row : allRows) {
			List<WebElement> cell = row.findElements(By.tagName("td"));
			for (WebElement column : cell) {
				columnIndex = columnIndex + 1;
			}
		}
		return columnIndex;
	}

	/***
	 * Returns Cell value
	 *
	 * @param id
	 * @param driver
	 * @param row
	 * @param column
	 * @return
	 */
	public String getCellValue(String id, WebDriver driver, int row, int column) {
		String value = driver.findElement(
				By.xpath("//*[@id='" + id + "']/tbody/tr[" + row
						+ "]/td[" + column + "]")).getText();
		return value;
	}
	
	public String getCellValue1(String id, WebDriver driver, int row, int column) {
		String value = driver.findElement(
				By.xpath("//*[@id='" + id + "']/tbody/tr[" + row
						+ "]/td[" + column + "]/div/div/div/input")).getAttribute("value");
		return value;
	}
	
	public int getColumnIndex(String id, WebDriver driver, String columnName) {

		List<WebElement> thList = driver.findElements(By.xpath(".//*[@id='"
				+ id + "']/thead/tr/th"));

		int i = 0;
		for (i = 0; i < thList.size(); i++) {
			int count = 0;

			if (thList.get(i).getText().contains(columnName)) {
				break;
			} else
				count++;
		}

		return i + 1;

	}

	
	/*
	 * This method will validate the data in respective column parameters to
	 * pass: Table id, driver, Column name, data to validate
	 * 
	 * It will validate the data in the column, if fails it will display
	 * assertion error and tells the Requisition number
	 */
	public void validateColumnData(String id, WebDriver driver,
			String columnName, String expected) {
		List<WebElement> thList = driver.findElements(By.xpath(".//*[@id='"
				+ id + "']/thead/tr/th"));

		int j = 0;
		for (j = 0; j < thList.size(); j++) {
			int count = 0;

			if (thList.get(j).getText().contains(columnName)) {
				break;
			} else
				count++;
		}

		int col = j + 1;
		TableUtil td = new TableUtil();
		int rowCount = td.getRowCount(id, driver);
		String actual = null;

		for (int i = 1; i < rowCount; i++) {
			actual = td.getCellValue(id, driver, i, col);
			String req = driver
					.findElement(
							By.xpath(".//*[@id='" + id + "']/tbody/tr[" + i
									+ "]/td[3]")).getText();
			Logging.log("Validating in Row No: " + i);
			Logging.log("Searched: " + actual + ",, Found: " + expected);
			Assert.assertEquals(actual.toUpperCase(), expected.toUpperCase(),
					"For Requisition: " + req);

		}
	}
	
	public WebElement getCellPath(String id, WebDriver driver, int row, int column) {
        WebElement value = driver.findElement(
                By.xpath("//*[@id='" + id + "']/tbody/tr[" + row
                        + "]/td[" + column + "]/a/span"));
        return value;
    }

	public void validateColumnData1(String id, WebDriver driver,
			String columnName, String expected) {
		List<WebElement> thList = driver.findElements(By.xpath(".//*[@id='"
				+ id + "']/thead/tr/th"));

		int j = 0;
		for (j = 0; j < thList.size(); j++) {
			int count = 0;

			if (thList.get(j).getText().contains(columnName)) {
				break;
			} else
				count++;
		}

		int col = j + 1;
		System.out.println("col: " + col);
		TableUtil td = new TableUtil();
		int rowCount = td.getRowCount(id, driver);
		String actual = null;
		System.out.println("rowCount: " + rowCount);

		for (int i = 2; i < rowCount; i++) {
			actual = td.getCellValue(id, driver, i, col);
			String req = driver
					.findElement(
							By.xpath(".//*[@id='" + id + "']/tbody/tr[" + i
									+ "]/td[3]")).getText();
			Logging.log("Searched by: " + actual + " ,, Found: " + expected);
			Assert.assertEquals(actual.toUpperCase(), expected.toUpperCase(),
					"For Requisition: " + req);

		}
	}

	
}
