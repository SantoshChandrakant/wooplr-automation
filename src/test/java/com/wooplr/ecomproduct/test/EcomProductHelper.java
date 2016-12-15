//package com.wooplr.ecomproduct.test;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.testng.Assert;
//
//import com.ubiquity.common.jaxb.ecomm.EcommProductSKUJAXB;
//import com.ubiquity.common.jaxb.ecomm.EcommerceProductFullJAXB;
//
//public class EcomProductHelper {
//
//	/**
//	 * Sets unique values for the request[ProductName, UniqueID, SkuID,
//	 * RetailPrice, SalesPrice]
//	 * 
//	 * @param csvData
//	 * @return productDetail
//	 */
//	public static EcommerceProductFullJAXB getProductDetails(EcommerceProductFullJAXB csvData) {
//
//		if (csvData.getName() != null) {
//			csvData.setName(csvData.getName() + System.currentTimeMillis());
//		}
//		if (csvData.getUniqueId() != null) {
//			csvData.setUniqueId(csvData.getUniqueId() + System.currentTimeMillis());
//		}
//		csvData.setStoreId(5868773039931392L);
//
//		ArrayList<String> image_pid = new ArrayList<String>();
//		image_pid.add("test/p/cgbihi1dxhkhmuhr5x8n");
//		csvData.setImage_pid(image_pid);
//		EcommProductSKUJAXB skuDetail = new EcommProductSKUJAXB();
//		skuDetail.setSkuId("Auto_" + System.currentTimeMillis());
//		skuDetail.setRetailPrice(499.00);
//		skuDetail.setSalesPrice(649.00);
//		skuDetail.setSize("Small");
//		skuDetail.setAvailable(true);
//		List<EcommProductSKUJAXB> skus = new ArrayList<EcommProductSKUJAXB>();
//		skus.add(skuDetail);
//		csvData.setSkus(skus);
//		return csvData;
//	}
//
//	/**
//	 * Sets all product details and returns product bean
//	 * 
//	 * @return productDetailsBean
//	 */
//	public static EcommerceProductFullJAXB getCreateProductBean() {
//
//		EcommerceProductFullJAXB productBean = new EcommerceProductFullJAXB();
//		productBean.setName("AutoProd_" + System.currentTimeMillis());
//		productBean.setDescription("This is automation-test created product");
//		productBean.setManufacture("Flying Machine");
//		productBean.setStoreId(5868773039931392L);
//		productBean.setUniqueId(Long.toString(System.currentTimeMillis()));
//		productBean.setGender("female");
//		productBean.setCategoryName("Accessories");
//		productBean.setSubcategory("Hats and Caps");
//		ArrayList<String> image_pid = new ArrayList<String>();
//		image_pid.add("test/p/cgbihi1dxhkhmuhr5x8n");
//		productBean.setImage_pid(image_pid);
//		productBean.setColor("Blue");
//
//		EcommProductSKUJAXB skuDetail = EcomProductHelper.getSku(System.currentTimeMillis(), 368, 433, "Small");
//		List<EcommProductSKUJAXB> skus = new ArrayList<EcommProductSKUJAXB>();
//		skus.add(skuDetail);
//		productBean.setSkus(skus);
//
//		return productBean;
//	}
//
//	/**
//	 * Set unique values for update
//	 * 
//	 * @param bean
//	 * @return bean
//	 */
//	public static EcommerceProductFullJAXB getUpdateBean(EcommerceProductFullJAXB bean) {
//
//		if (bean.getName() != null) {
//			bean.setName(bean.getName() + System.currentTimeMillis());
//		}
//		if (bean.getUniqueId() != null) {
//			bean.setUniqueId(bean.getUniqueId() + System.currentTimeMillis());
//		}
//		return bean;
//	}
//
//	/**
//	 * Add Skus details to the EcomProductBean
//	 * 
//	 * @param EcomProductBean
//	 * @return EcomProductBean
//	 */
//	public static EcommerceProductFullJAXB addSku(EcommerceProductFullJAXB bean) {
//		List<EcommProductSKUJAXB> skus = new ArrayList<EcommProductSKUJAXB>();
//		skus.add(EcomProductHelper.getSku(System.nanoTime(), 346, 399, "Small"));
//		skus.add(EcomProductHelper.getSku(System.nanoTime(), 446, 499, "Large"));
//		skus.add(EcomProductHelper.getSku(System.nanoTime(), 799, 850, "XL"));
//		bean.setSkus(skus);
//		return bean;
//	}
//
//	/**
//	 * Add sku details to the bean
//	 * 
//	 * @param retailPrice
//	 * @param salesPrice
//	 * @param size
//	 * @return skuJaxB
//	 */
//	public static EcommProductSKUJAXB getSku(Long skuId, double retailPrice, double salesPrice, String size) {
//		EcommProductSKUJAXB skuDetail = new EcommProductSKUJAXB();
//		skuDetail.setSkuId("Auto_" + skuId);
//		skuDetail.setRetailPrice(retailPrice);
//		skuDetail.setSalesPrice(salesPrice);
//		skuDetail.setSize(size);
//		skuDetail.setAvailable(true);
//		return skuDetail;
//	}
//
//	/**
//	 * Validate Created Product
//	 * 
//	 * @param request
//	 * @param response
//	 */
//	public static void validateCreatedProduct(EcommerceProductFullJAXB request, EcommerceProductFullJAXB response) {
//
//		Assert.assertNotNull(response, "Returning Response as null after creating product!!");
//		Assert.assertNotNull(response.getId(), "Returning ProductID as null !!");
//		if (request.getName() != null) {
//			Assert.assertEquals(response.getName(), request.getName(), "Showing wrong ProductName in response !!");
//		}
//		if (request.getCategoryName() != null) {
//			Assert.assertEquals(response.getCategoryName(), request.getCategoryName(),
//					"Showing wrong CategoryName in response !!");
//		}
//		if (request.getSubcategory() != null) {
//			Assert.assertEquals(response.getSubcategory(), request.getSubcategory(),
//					"Showing wrong SubCategory in response !!");
//		}
//		if (request.getDescription() != null) {
//			Assert.assertEquals(response.getDescription(), request.getDescription(),
//					"Showing wrong Description in response !!");
//		}
//		if (request.getManufacture() != null) {
//			Assert.assertEquals(response.getManufacture(), request.getManufacture(),
//					"Showing wrong Manufacture in response !!");
//		}
//		if (request.getColor() != null) {
//			Assert.assertEquals(response.getColor(), request.getColor(), "Showing wrong Color in response !!");
//		}
//		if (request.getGender() != null) {
//			Assert.assertEquals(response.getGender(), request.getGender(), "Showing wrong Gender in response !!");
//		}
//		if (request.getUniqueId() != null) {
//			Assert.assertEquals(response.getUniqueId(), request.getUniqueId(), "Showing wrong UniqueId in response !!");
//		}
//		if (request.getSkus() != null) {
//			Assert.assertEquals(response.getImage_pid().get(0), request.getImage_pid().get(0),
//					"Showing wrong ImagePID in response !!");
//			Assert.assertEquals(response.getStoreId(), request.getStoreId(), "Showing wrong StoreID in response !!");
//
//			Assert.assertNotNull(response.getSkus(), "Skus details coming as null in response !!");
//			Assert.assertEquals(response.getSkus().get(0).getSkuId(), request.getSkus().get(0).getSkuId(),
//					"Showing wrong SkusId in response !!");
//			Assert.assertEquals(response.getSkus().get(0).getSalesPrice(), request.getSkus().get(0).getSalesPrice(),
//					"Showing wrong SalesPrice in response !!");
//			Assert.assertEquals(response.getSkus().get(0).getRetailPrice(), request.getSkus().get(0).getRetailPrice(),
//					"Showing wrong RetailPrice in response !!");
//			Assert.assertEquals(response.getSkus().get(0).getSize(), request.getSkus().get(0).getSize(),
//					"Showing wrong Size in response !!");
//		}
//
//	}
//
//	public static void main(String[] args) {
//
//		System.out.println(System.nanoTime());
//		System.out.println(System.nanoTime());
//		System.out.println(System.nanoTime());
//		System.out.println(System.nanoTime());
//	}
//}
