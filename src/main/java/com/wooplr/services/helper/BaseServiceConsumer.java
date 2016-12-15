package com.wooplr.services.helper;
//package com.san.services.helper;
//
//import java.security.SecureRandom;
//import java.security.cert.X509Certificate;
//import java.util.HashMap;
///*import java.util.Date;*/
//import java.util.List;
//import java.util.Map;
//import java.util.Map.Entry;
//
//import javax.net.ssl.HostnameVerifier;
//import javax.net.ssl.HttpsURLConnection;
//import javax.net.ssl.SSLContext;
//import javax.net.ssl.SSLSession;
//import javax.net.ssl.TrustManager;
//import javax.net.ssl.X509TrustManager;
//import javax.ws.rs.Path;
//import javax.ws.rs.Produces;
//import javax.ws.rs.core.MediaType;
//import javax.ws.rs.core.UriBuilder;
//
//import com.san.base.controller.Logging;
//import com.sun.jersey.api.client.Client;
//import com.sun.jersey.api.client.ClientResponse;
//import com.sun.jersey.api.client.WebResource;
//import com.sun.jersey.api.client.WebResource.Builder;
//import com.sun.jersey.api.client.config.ClientConfig;
//import com.sun.jersey.api.client.config.DefaultClientConfig;
//import com.sun.jersey.api.json.JSONConfiguration;
//import com.sun.jersey.client.urlconnection.HTTPSProperties;
//import com.sun.jersey.client.urlconnection.URLConnectionClientHandler;
//
//public class BaseServiceConsumer {
//	
//	private MediaType responseMediaType = null;
//	public Client restClient = null;
//	public WebResource res = null;
//	private String baseServiceEndPoint = null;
//	private MediaType requestMediaType = null;
//	
//	
//	@Path("patch")
//	public static class PatchResource {
//	       //@PATCH
//	       @Produces(MediaType.APPLICATION_JSON)
//	       public String getPatch(String request) {
//	           return "Patched " + request;
//	       }
//	  }
//	
//	
//	public static void configHttpsClient(ClientConfig config ){
//		SSLContext sc = null;
//		
//		TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager(){
//			public X509Certificate[] getAcceptedIssuers(){return null;}
//			public void checkClientTrusted(X509Certificate[] certs, String authType){}
//			public void checkServerTrusted(X509Certificate[] certs, String authType){}
//		}};
//		try {
//			sc = SSLContext.getInstance("TLS");
//			sc.init(null, trustAllCerts, new SecureRandom());
//			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		config.getProperties().put(HTTPSProperties.PROPERTY_HTTPS_PROPERTIES, new HTTPSProperties( new HostnameVerifier()
//		{
//			//@Override
//			public boolean verify(String hostname, SSLSession session)
//			{
//				return true;
//			}
//		}, sc));
//	}
//	
//	public BaseServiceConsumer(MediaType mediaType, String baseServiceEndPoint ){
//		
//		this.responseMediaType = mediaType;
//		this.baseServiceEndPoint = baseServiceEndPoint;
//		ClientConfig config = new DefaultClientConfig();
//		config.getProperties().put(URLConnectionClientHandler.PROPERTY_HTTP_URL_CONNECTION_SET_METHOD_WORKAROUND, true);
//		if(this.responseMediaType.equals(MediaType.APPLICATION_JSON_TYPE)){
//			config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, true);
//		}
//		else if(responseMediaType.getSubtype().equals("x-protobuf")){
//			//config.getClasses().add(ProtobufMessageBodyReaderWriter.class);
//		}
//		this.restClient=Client.create(config);
//		//config.
//		//this.restClient;
//		
//	
//	}
//	
//	/**
//	 * @param mediaType
//	 * @param baseServiceEndPoint
//	 * @param requestMediaType
//	 */
//	public BaseServiceConsumer(MediaType mediaType, String baseServiceEndPoint, MediaType requestMediaType ){
//		
//		this.responseMediaType = mediaType;
//		this.baseServiceEndPoint = baseServiceEndPoint;
//		this.requestMediaType = requestMediaType;
//		ClientConfig config = new DefaultClientConfig();
//		config.getProperties().put(URLConnectionClientHandler.PROPERTY_HTTP_URL_CONNECTION_SET_METHOD_WORKAROUND, true);
//		if(this.responseMediaType.equals(MediaType.APPLICATION_JSON_TYPE)){
//			config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, true);
//		}
//		else if(responseMediaType.getSubtype().equals("x-protobuf")){
//			//config.getClasses().add(ProtobufMessageBodyReaderWriter.class);
//		}
//		this.restClient=Client.create(config);
//		
//	}
//	public BaseServiceConsumer(MediaType responseMediaType, String baseServiceEndPoint, MediaType requestMediaType, boolean isSecuredPool ){
//		this.responseMediaType = responseMediaType;
//		this.baseServiceEndPoint = baseServiceEndPoint;
//		this.requestMediaType = requestMediaType;
//		ClientConfig config = new DefaultClientConfig();
//		
//		if(this.responseMediaType.equals(MediaType.APPLICATION_JSON_TYPE)){
//			config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, true);
//		}
//		if(this.responseMediaType.getSubtype().equals("x-protobuf")){
//			config = new DefaultClientConfig();
//			//config.getClasses().add(ProtobufMessageBodyReaderWriter.class);
//		}
//		if(isSecuredPool){
//			configHttpsClient(config);
//		}
//		this.restClient=Client.create(config);
//		
//	}
//	
//	public  void initWebResource(String operation, String valueToAppend, Map<String, String>parameters) throws Exception{
//		if(restClient==null)
//			throw new Exception("Unable to initialize WebResource. restClient is not initialized ");
//		this.res=restClient.resource(constructEndPoint(operation, valueToAppend, parameters).build());
//		
//	}
//	
//	private  void initWebResource(UriBuilder uriBuilder) throws Exception{
//		if(restClient==null)
//			throw new Exception("Unable to initialize WebResource. restClient is not initialized ");
//		this.res=restClient.resource(uriBuilder.build());
//		
//		
//	}
//	protected Object executePost(Object request, List<NameValue> headerValues, Class<?> response ){
//		Builder builder = constructBuilder(request,headerValues);
//		Logging.log("making REST service call at "+this.res.getURI().toString());
//		return builder.accept(responseMediaType).post(response);
//		//return builder.accept(responseMediaType).method("PATCH",response);
//		
//	}
//	
//	protected Map<String,Object> executePostWithStatus(Object request, List<NameValue> headerValues, Class<?> response ){
//		Builder builder = constructBuilder(request,headerValues);
//		Logging.log("making REST service call at "+this.res.getURI().toString());
//		Map<String,Object> respMap=new HashMap<String,Object>();
//		Object resp= builder.accept(responseMediaType).post(response);
//		ClientResponse statusResponse=this.res.get(ClientResponse.class);
//		respMap.put("statuCode",statusResponse.getStatus());
//		respMap.put("response",resp);
//	    return respMap;
//	}
//	
//	protected Map<String,Object> executePostWithHeader(Object request, List<NameValue> headerValues, Class<?> response ){
//		Builder builder = constructBuilder(request,headerValues);
//		Logging.log("making REST service call at "+this.res.getURI().toString());
//		Map<String,Object> respMap=new HashMap<String,Object>();
//		Object resp= builder.accept(responseMediaType).post(response);
//		ClientResponse statusResponse=this.res.get(ClientResponse.class);
//		respMap.put("Header",statusResponse.getHeaders());
//		respMap.put("response",resp);
//	    return respMap;
//	}
//	
//	protected Map<String,Object> executePutWithStatus(Object request, List<NameValue> headerValues, Class<?> response ){
//		Builder builder = constructBuilder(request,headerValues);
//		Logging.log("making REST service call at "+this.res.getURI().toString());
//		Map<String,Object> respMap=new HashMap<String,Object>();
//		Object resp= builder.accept(this.responseMediaType).put(response);
//		ClientResponse statusResponse=this.res.get(ClientResponse.class);
//		respMap.put("statuCode",statusResponse.getStatus());
//		respMap.put("response",resp);
//	    return respMap;
//	}
//	
//	protected Map<String,Object> executeDeleteStatus(List<NameValue> headerValues, Class<?> response ){
//		Builder builder = constructBuilder(null,headerValues );
//		Logging.log("making REST service call at "+this.res.getURI().toString());
//		Map<String,Object> respMap=new HashMap<String,Object>();
//		builder.accept(this.responseMediaType).delete(response);
//		ClientResponse statusResponse=this.res.get(ClientResponse.class);
//		respMap.put("statuCode",statusResponse.getStatus());
//	    return respMap;
//	}
//	
//	protected Object executePut(Object request, List<NameValue> headerValues, Class<?> response ){
//		Builder builder = constructBuilder(request,headerValues );
//		Logging.log("making REST service call at "+this.res.getURI().toString());
//		return builder.accept(this.responseMediaType).put(response);
//	}
//	
//	protected Object executePatch(Object request, List<NameValue> headerValues, Class<?> response ){
//		Builder builder = constructBuilder(request,headerValues );
//		Logging.log("making REST service call at "+this.res.getURI().toString());
//		//return builder.accept(this.responseMediaType).put(response);
//		return builder.accept(responseMediaType).method("PATCH",response);
//	}
//	
//	protected Object executeDelete( List<NameValue> headerValues, Class<?> response ){
//		Builder builder = constructBuilder(null,headerValues );
//		Logging.log("making REST service call at "+this.res.getURI().toString());
//		return builder.accept(this.responseMediaType).delete(response);
//	}
//		
//	protected Object executeGet(List<NameValue> headerValues, Class<?> response ){
//		Builder builder = constructBuilder(null,headerValues);
//		Logging.log("making REST service call at "+this.res.getURI().toString());
//		if(response ==null)
//			return builder.accept(MediaType.WILDCARD_TYPE).get(ClientResponse.class);
//		else 
//			return builder.accept(this.responseMediaType).get(response);
//	}
//	
//	protected Map<String,Object> executeGetWithStatus(List<NameValue> headerValues, Class<?> response ){
//		Builder builder = constructBuilder(null,headerValues);
//		Logging.log("making REST service call at "+this.res.getURI().toString());
//		Map<String,Object> respMap = new HashMap<String,Object>();
//		if(response ==null){
//			builder.accept(MediaType.WILDCARD_TYPE).get(ClientResponse.class);
//			ClientResponse statusResponse=this.res.get(ClientResponse.class);
//			respMap.put("statuCode",statusResponse.getStatus());
//		    return respMap;
//			}
//		else {
//			builder.accept(this.responseMediaType).get(response);
//			ClientResponse statusResponse=this.res.get(ClientResponse.class);
//			respMap.put("statuCode",statusResponse.getStatus());
//		    return respMap;
//		}
//	}
//	
//	protected Builder constructBuilder(Object request, List<NameValue> headerValues){
//		Builder builder = null;
//	
//		if(request != null)
//			builder = this.res.entity(request, requestMediaType!=null?requestMediaType:responseMediaType);
//		else{
//			//	builder = this.res..type(MediaType.TEXT_PLAIN_TYPE);
//			builder = res.getRequestBuilder();
//			
//		}
//		if(headerValues!=null)    		
//			for(NameValue n : headerValues){
//				builder.header(n.getName(), n.getValue());
//			}
//		return builder;
//	}
//	protected UriBuilder constructEndPoint(String operationName, String valueToAppend, Map<String,String> parameters ){
//		UriBuilder builder = UriBuilder
//		.fromUri(baseServiceEndPoint);
//		String[] values=null;
//		
//		if (valueToAppend!=null) {
//			
//			values= valueToAppend.split(",");
//			for (int i = 0; i < values.length; i++) {
//				operationName = operationName!=null?operationName+(values[i]!=null?"/"+values[i]:""):values[i]!=null?"/"+values[i]:"";
//			}
//			
//		}else{
//			operationName = operationName!=null?operationName+(valueToAppend!=null?"/"+valueToAppend:""):valueToAppend!=null?"/"+valueToAppend:"";
//		}
//		
//		
//		//operationName = operationName!=null?operationName+(valueToAppend!=null?"/"+valueToAppend:""):valueToAppend!=null?"/"+valueToAppend:"";
//		if(operationName!="")
//			builder.path(operationName);
//		if(parameters != null && parameters.size() !=0) {
//			for(Entry<String, String> entry :parameters.entrySet()){
//				builder.queryParam(entry.getKey(), entry.getValue());
//			}
//		}
//		return builder;
//	}
//	public MediaType getRequestMediaType() {
//		return requestMediaType;
//	}
//	public void setRequestMediaType(MediaType requestMediaType) {
//		this.requestMediaType = requestMediaType;
//	}
//	/*protected static <S> Gson getGson(Class<S> respClass) {
//		GsonBuilder gb = new GsonBuilder();
//		gb.registerTypeAdapter(Date.class, new DateLongAdapter()).create();
//		if(respClass != null) {
//			Object typeAdapter = GsonTypeAdapterRegistry.get(respClass);
//			if(typeAdapter != null) {
//				gb.registerTypeAdapter(respClass, typeAdapter);
//			}
//		}
//		return gb.create();
//	}*/
//	/*  public static void main(String a[]) throws Exception{
//    	Map<String, String> t = new HashMap<String, String>();
//    	t.put("t1", "10");
//    	t.put("t2", "50");
//    	BaseServiceConsumer consumer = new BaseServiceConsumer();
//    	consumer.baseServiceEndPoint = "www.qa.ebay.com";
//    	URI build = consumer.constructEndPoint("service","test",t).build();
//    	System.out.println(build.toString());
//    }*/
//	
//	
//	
//}
