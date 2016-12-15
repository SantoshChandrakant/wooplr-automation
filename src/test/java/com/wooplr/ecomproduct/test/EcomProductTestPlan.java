//package com.wooplr.ecomproduct.test;
//
//import java.lang.reflect.Method;
//import java.util.Iterator;
//import java.util.LinkedHashMap;
//import java.util.List;
//
//import org.testng.Assert;
//import org.testng.annotations.DataProvider;
//import org.testng.annotations.Test;
//
//import com.google.appengine.repackaged.com.google.gson.Gson;
//import com.san.base.util.WooplrCsvUtil;
//import com.ubiquity.common.jaxb.ecomm.EcommProductSKUJAXB;
//import com.ubiquity.common.jaxb.ecomm.EcommerceProductFullJAXB;
//import com.wooplr.base.controller.TestPlan;
//import com.wooplr.base.controller.TestRetryAnalyzer;
//import com.wooplr.base.util.internal.entity.TestObject;
//import com.wooplr.consumers.EcomProductConsumer;
//
///**
// * @author Santosh C
// * 
// */
//@Test(groups = { "SANITY" }, retryAnalyzer = TestRetryAnalyzer.class)
//public class EcomProductTestPlan extends TestPlan {
//
//	final static String DATAPROVIDER_NAME = "ECOM_PRODUCT";
//	final static String CSV_DIR = "./src/test/java/com/wooplr/ecomproduct/test/";
//	final static String CSV_FILENAME = "EcomProduct_TestData.csv";
//	final static String CSV_PATH = CSV_DIR + CSV_FILENAME;
//	EcommerceProductFullJAXB request = null;
//	EcomProductConsumer consumer = new EcomProductConsumer();
//
//	@DataProvider(name = DATAPROVIDER_NAME)
//	public static Iterator<Object[]> getInfo(Method method) {
//		Iterator<Object[]> objectsFromCsv = null;
//		try {
//			String fileName = CSV_PATH;
//			LinkedHashMap<String, Class<?>> entityClazzMap = new LinkedHashMap<String, Class<?>>();
//			LinkedHashMap<String, String> methodFilter = new LinkedHashMap<String, String>();
//			methodFilter.put(TestObject.TEST_TITLE, method.getName());
//			entityClazzMap.put("TestObject", TestObject.class);
//			entityClazzMap.put("EcommerceProductFullJAXB", EcommerceProductFullJAXB.class);
//
//			objectsFromCsv = WooplrCsvUtil.getObjectsFromCsv(EcomProductTestPlan.class, entityClazzMap, fileName, null,
//					methodFilter);
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return objectsFromCsv;
//	}
//
//	/**
//	 * Create a EcomProduct and validate created product from created response
//	 * 
//	 * @param data
//	 */
//	@Test(groups = { "createProduct", "Sanity" }, dataProvider = DATAPROVIDER_NAME)
//	public void createProduct(TestObject testData, EcommerceProductFullJAXB csvData) {
//
//		request = EcomProductHelper.getProductDetails(csvData);
//		EcommerceProductFullJAXB response = consumer.createProduct(testData, request);
//		EcomProductHelper.validateCreatedProduct(request, response);
//		consumer.deleteProduct(response.getId().toString());
//	}
//
//	/**
//	 * Create a EcomProduct and getProduct by productID
//	 * 
//	 * @param data
//	 */
//	@Test(groups = { "getProduct", "Sanity" }, dataProvider = DATAPROVIDER_NAME)
//	public void getProduct(TestObject testData, EcommerceProductFullJAXB csvData) {
//
//		request = EcomProductHelper.getProductDetails(csvData);
//		EcommerceProductFullJAXB response = consumer.createProduct(testData, request);
//		EcommerceProductFullJAXB getByProductIdResponse = consumer.getProduct(testData, response.getId().toString());
//		EcomProductHelper.validateCreatedProduct(request, getByProductIdResponse);
//
//		consumer.deleteProduct(response.getId().toString());
//	}
//
//	/**
//	 * Try to get product details by giving invalid productID, it should throw
//	 * proper exception
//	 * 
//	 * @param data
//	 */
//	@Test(groups = { "getByInvalidProductId", "Sanity" }, dataProvider = DATAPROVIDER_NAME)
//	public void getByInvalidProductId(TestObject testData, EcommerceProductFullJAXB csvData) {
//		consumer.getProduct(testData, "123456789");
//	}
//
//	/**
//	 * Create a EcomProduct and update it
//	 * 
//	 * @param data
//	 */
//	@Test(groups = { "updateProduct", "Sanity" }, dataProvider = DATAPROVIDER_NAME)
//	public void updateProduct(TestObject testData, EcommerceProductFullJAXB csvData) {
//
//		request = EcomProductHelper.getCreateProductBean();
//		EcommerceProductFullJAXB createProductResponse = consumer.createProduct(testData, request);
//		String productID = createProductResponse.getId().toString();
//
//		EcommerceProductFullJAXB updateBean = EcomProductHelper.getUpdateBean(csvData);
//
//		String updateResp = consumer.updateProduct(productID, updateBean);
//		Assert.assertEquals(updateResp, productID, " Update product is returning wrong productID");
//
//		EcommerceProductFullJAXB responseAfterUpdate = consumer.getProduct(testData, productID);
//
//		EcomProductHelper.validateCreatedProduct(updateBean, responseAfterUpdate);
//
//		consumer.deleteProduct(productID);
//	}
//
//	/**
//	 * Create a EcomProduct and Delete it
//	 * 
//	 * @param data
//	 */
//	@Test(groups = { "deleteProduct", "Sanity" }, dataProvider = DATAPROVIDER_NAME)
//	public void deleteProduct(TestObject testData, EcommerceProductFullJAXB csvData) {
//
//		request = EcomProductHelper.getCreateProductBean();
//		EcommerceProductFullJAXB createProductResponse = consumer.createProduct(testData, request);
//		Assert.assertNotNull(createProductResponse, "Resposne is null after creating a Product!!");
//		String productID = createProductResponse.getId().toString();
//
//		Boolean deleteProductResponse = consumer.deleteProduct(productID);
//		System.out.println("deleteProductResponse: " + deleteProductResponse);
//		Assert.assertTrue(deleteProductResponse, "Not returning TRUE after deleting a product !!");
//		testData.setException("EXCEPTION");
//		consumer.getProduct(testData, productID);
//	}
//
//	/**
//	 * Create a EcomProduct with multiple Sku details and update them
//	 * 
//	 * @param data
//	 */
//	@Test(groups = { "updateSkuDetails", "Sanity" }, dataProvider = DATAPROVIDER_NAME)
//	public void updateSkuDetails(TestObject testData, EcommerceProductFullJAXB csvData) {
//
//		request = EcomProductHelper.addSku(EcomProductHelper.getCreateProductBean());
//		EcommerceProductFullJAXB createdProduct = consumer.createProduct(testData, request);
//		System.out.println("ID: " + createdProduct.getId());
//
//		List<EcommProductSKUJAXB> skusJaxB = createdProduct.getSkus();
//		for (EcommProductSKUJAXB ecommProductSKUJAXB : skusJaxB) {
//			ecommProductSKUJAXB.setRetailPrice(199);
//			ecommProductSKUJAXB.setSalesPrice(299);
//			ecommProductSKUJAXB.setSize("XXL");
//			skusJaxB.add(ecommProductSKUJAXB);
//		}
//		Gson gson = new Gson();
//		System.out.println("UPDATE REQUEST: " + gson.toJson(skusJaxB));
//	}
//}