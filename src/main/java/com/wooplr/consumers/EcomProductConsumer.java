//package com.wooplr.consumers;
//
//import java.util.List;
//
//import javax.ws.rs.client.Entity;
//import javax.ws.rs.core.GenericType;
//import javax.ws.rs.core.MediaType;
//import javax.ws.rs.core.Response;
//
//import org.codehaus.jackson.map.DeserializationConfig.Feature;
//import org.codehaus.jackson.map.ObjectMapper;
//import org.testng.Assert;
//
//import com.google.gson.Gson;
//import com.wooplr.base.util.internal.entity.TestObject;
//import com.wooplr.bean.EcommProductSKUJAXB;
//import com.wooplr.bean.EcommerceProductFullJAXB;
//import com.wooplr.rest.consumer.BaseServiceConsumer;
//import com.wooplr.rest.consumer.ReadingServiceEndPointsProperties;
//
//public class EcomProductConsumer extends BaseServiceConsumer {
//
//	Response response = null;
//	String host = ReadingServiceEndPointsProperties.getServiceEndPoint("Seller_Host");
//	String version = ReadingServiceEndPointsProperties.getServiceEndPoint("Seller_Version");
//	String endpoint = host + "/shop/rest/" + version + "/ecommproduct/".trim();
//	Gson gson = new Gson();
//	String[] SELLER_HEADERS = { "Seller_Authorization" };
//	String[] USER_HEADERS = { "Authorization" };
//
//	/**   
//	 * Create a Product
//	 * 
//	 * @return createdProductDetails
//	 */
//	public EcommerceProductFullJAXB createProduct(TestObject data, EcommerceProductFullJAXB product) {
//
//		String serviceEndPoint = this.endpoint + "full";
//		System.out.println("Create Product endPointURL  >>>" + serviceEndPoint);
//		EcommerceProductFullJAXB productDetail = null;
//		String stringResponse = null;
//   
//		System.out.println("REQUEST: " + gson.toJson(product));
//		Response response = executePOST(serviceEndPoint, SELLER_HEADERS, null,
//				Entity.entity(product, MediaType.APPLICATION_JSON));
//
//		if (data.getException().equals("EXCEPTION")) {
//			Assert.assertEquals(response.getStatus(), 204, "Expected to throw 204 status, but it thrown: "
//					+ response.getStatus() + " Status Message: " + response.getStatusInfo());
//		} else {
//			if (response.getStatus() == 200) {
//				stringResponse = response.readEntity(String.class);
//				ObjectMapper mapper = new ObjectMapper();
//				mapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//				try {
//					productDetail = mapper.readValue(stringResponse, EcommerceProductFullJAXB.class);
//					System.out.println("JSON RESPONSE: " + gson.toJson(productDetail));
//					System.out.println("Created ProductID: " + productDetail.getId().toString());
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				return productDetail;
//			} else {
//				Assert.fail("Failed to create a Product,, Returned Status Code: " + response.getStatus());
//			}
//		}
//		return productDetail;
//	}
//
//	/**
//	 * Get Product details by productID
//	 * 
//	 * @return ProductDetails
//	 */
//	public EcommerceProductFullJAXB getProduct(TestObject data, String productID) {
//
//		String serviceEndPoint = this.endpoint + "id/" + productID + "/full";
//		System.out.println("Get Product endPointURL  >>>" + serviceEndPoint);
//		EcommerceProductFullJAXB productDetail = null;
//		String stringResponse = null;
//
//		Response response = executeGET(serviceEndPoint, SELLER_HEADERS);
//		if (data.getException().equals("EXCEPTION")) {
//			Assert.assertEquals(response.getStatus(), 204, "Expected to throw 204 status, but it thrown: "
//					+ response.getStatus() + " Status Message: " + response.getStatusInfo());
//		} else {
//			if (response.getStatus() == 200) {
//				stringResponse = response.readEntity(String.class);
//				ObjectMapper mapper = new ObjectMapper();
//				mapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//				try {
//					productDetail = mapper.readValue(stringResponse, EcommerceProductFullJAXB.class);
//					System.out.println("JSON RESPONSE: " + gson.toJson(productDetail));
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				return productDetail;
//			} else {
//				Assert.fail("Failed to get product by productID,," + productID + "Returned Status Code: "
//						+ response.getStatus());
//			}
//		}
//		return productDetail;
//	}
//
//	/**
//	 * Update a product
//	 * 
//	 * @param productID
//	 * @param updateRequest
//	 * @return updateProductResponse
//	 */
//	public String updateProduct(String productID, EcommerceProductFullJAXB updateRequest) {
//
//		String serviceEndPoint = this.endpoint + "id/" + productID + "/full";
//		System.out.println("Update Product endPointURL  >>>" + serviceEndPoint);
//		System.out.println("REQUEST: " + gson.toJson(updateRequest));
//		Response response = executePUT(serviceEndPoint, SELLER_HEADERS,
//				Entity.entity(updateRequest, MediaType.APPLICATION_JSON));
//		return response.readEntity(String.class);
//	}
//
//	/**
//	 * Delete a product
//	 * 
//	 * @param productID
//	 * @return deleteResponse
//	 */
//	public Boolean deleteProduct(String productID) {
//
//		String serviceEndPoint = this.host + "/rest/v2/ecommproduct/id/" + productID;
//		System.out.println("Delete Product endPointURL  >>>" + serviceEndPoint);
//
//		Response response = executeDELETE(serviceEndPoint, USER_HEADERS);
//		return response.readEntity(Boolean.class);
//	}
//
//	/**
//	 * Update Sku details
//	 * 
//	 * @param skuBean
//	 * @return updatedSkuBean
//	 */
//	public List<EcommProductSKUJAXB> updateSku(List<EcommProductSKUJAXB> skuBean) {
//		String serviceEndPoint = this.host + "/rest/v2/ecommproduct/updateSku";
//		System.out.println("Update Sku details endPointURL  >>>" + serviceEndPoint);
//		List<EcommProductSKUJAXB> skuBeanList = null;
//    
//		System.out.println("REQUEST: " + gson.toJson(skuBean));
//		Response response = executePOST(serviceEndPoint, USER_HEADERS, null,
//				Entity.entity(skuBean, MediaType.APPLICATION_JSON));
//
//		skuBeanList = response.readEntity(new GenericType<List<EcommProductSKUJAXB>>() {
//		});
//		return skuBeanList;
//	}
//}
