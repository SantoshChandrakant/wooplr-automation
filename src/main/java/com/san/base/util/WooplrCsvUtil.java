package com.san.base.util;

import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.wooplr.base.exception.SpireException;
import com.wooplr.base.util.internal.entity.TestObject;

/**
 * Created by Vinay Kumar on 25-05-2015.
 * <p/>
 * SpireCsvUtil Class parses or reads the Csv file and stores the object with respective class objects and fields .
 */
public class WooplrCsvUtil {

    private static final Map<Class<?>, Class<?>> PRIMITIVE_TYPE_MAP = new HashMap<Class<?>, Class<?>>();

    /***
     * It returns the Objects from csv file as a Iterator of Objects array
     *
     * @param clazz,         Class object of the Use Class
     * @param entityClazzMap Map of class objects in the csv file
     * @param filename       Csv file name to be provided
     * @param fields         Fields that are in the CSV file or null you can pass
     * @return Iterator Objects array ..
     * @throws Exception Generic Exception .
     */
    public static Iterator<Object[]> getObjectsFromCsv(Class<?> clazz,
                                                       LinkedHashMap<String, Class<?>> entityClazzMap,
                                                       String filename, String[] fields, LinkedHashMap methodFilter) throws Exception {
        Iterator<Object[]> dataIterator = getDataFromCSVFile(clazz,
                filename, fields, true, methodFilter);
        List<Object[]> list = getEntityData(dataIterator, entityClazzMap);
        return list.iterator();
    }


    /***
     * Returns data from the csv file provided .
     *
     * @param clazz       Class
     * @param filename    CSV file name to be provided
     * @param fields      fields in the csv file that provided
     * @param readHeaders todo . .future usage ..
     * @return
     */
    public static Iterator<Object[]> getDataFromCSVFile(Class<?> clazz, String filename, String[] fields, boolean readHeaders, LinkedHashMap methodFilter) {
        return getDataFromCSVFile(clazz, filename, fields, readHeaders, null, methodFilter);
    }


    /***
     * It collects the data from the CSV file .
     *
     * @param clazz
     * @param filename
     * @param fields
     * @param readHeaders
     * @param delimiter
     * @return
     */
    public static Iterator<Object[]> getDataFromCSVFile(Class<?> clazz, String filename, String[] fields, boolean readHeaders, String delimiter, LinkedHashMap methodFilter) {
        List<Object[]> sheetData = new ArrayList<Object[]>();
        InputStream is = null;
        try {
            /*if (clazz != null)
                is = clazz.getClassLoader().getResourceAsStream(filename);
            else*/
                is = new FileInputStream(filename);

            if (is == null)
                return new ArrayList<Object[]>().iterator();

            // Get the sheet
            String[][] csvData = SpireCommonUtil.read(is, delimiter);
            if (readHeaders) {
                List<Object> rowData = new ArrayList<Object>();
                //if fields are null, read the fields from the first row data
                if (fields == null) {
                    for (int j = 0; j < csvData[0].length; j++) {
                        rowData.add(csvData[0][j]);
                    }
                } else {
                    for (int i = 0; i < fields.length; i++) {
                        rowData.add(fields[i]);
                    }
                }
                sheetData.add(rowData.toArray(new Object[rowData.size()]));
            }

            int testTitleColumnIndex = -1;
            int testSiteColumnIndex = -1;
            // Search for Title & Site column
            for (int i = 0; i < csvData[0].length; i++) {
                if (testTitleColumnIndex == -1 && TestObject.TEST_TITLE.equalsIgnoreCase(csvData[0][i])) {
                    testTitleColumnIndex = i;
                } else if (testSiteColumnIndex == -1 && TestObject.TEST_CASE_ID.equalsIgnoreCase(csvData[0][i])) {
                    testSiteColumnIndex = i;
                }
                if (testTitleColumnIndex != -1 && testSiteColumnIndex != -1)
                    break;
            }
            // Let's check for blank rows first
            // The first row is the header
            StringBuffer sbBlank = new StringBuffer();
            for (int i = 1; i < csvData.length; i++) {
                if (testTitleColumnIndex != -1 && testSiteColumnIndex != -1 && (csvData[i][testTitleColumnIndex].trim().length() == 0 || csvData[i][testSiteColumnIndex].trim().length() == 0)) {
                    sbBlank.append(i + 1).append(',');
                }
            }
            if (sbBlank.length() > 0) {
                sbBlank.deleteCharAt(sbBlank.length() - 1);
                throw new SpireException("Blank TestTitle and/or Site value(s) found on Row(s) " + sbBlank.toString() + ".");
            }

            Set<String> uniqueDataSet = new TreeSet<String>();
            // The first row is the header
            for (int i = 1; i < csvData.length; i++) {

                // Check for duplicate Title & Site
                if (testTitleColumnIndex != -1 && testSiteColumnIndex != -1) {
                    String uniqueString = csvData[i][testTitleColumnIndex] + "$$$$####$$$$" + csvData[i][testSiteColumnIndex];
                    if (uniqueDataSet.contains(uniqueString))
                        throw new SpireException("Duplicate TestTitle and Site combination found in the csv file " + "with TestTitle = {" + csvData[i][testTitleColumnIndex] + "} " + "and Site = {" + csvData[i][testSiteColumnIndex] + "}");

                    uniqueDataSet.add(uniqueString);
                }

                Map<String, Object> rowDataMap = new HashMap<String, Object>();
                List<Object> rowData = new ArrayList<Object>();
                // Create the mapping between headers and column data
                for (int j = 0; j < csvData[i].length; j++) {
                    rowDataMap.put(csvData[0][j], csvData[i][j]);
                }

                if (fields == null) {

                	for (int j = 0; j < (csvData.length)-1; j++) {
                        if (methodFilter.get(TestObject.TEST_TITLE).toString().equalsIgnoreCase(csvData[i][1])) {
                        	
                            // Fix for null values not getting created when number of columns in a row is less than expected.
                            if (csvData[i].length > j) {
                                for (int temp = 0; temp < csvData[i].length; temp++) {
                                //    System.out.println("data " + csvData[i][temp]);
                                    rowData.add(csvData[i][temp]);
                                }
                            } else
                                rowData.add(null);
                            break;
                        }
                    }
                } else {
                    for (int k = 0; k < fields.length; k++) {
                        rowData.add(getValue(rowDataMap, fields[k]));
                    }
                }
                // End
                if (!rowData.isEmpty()) {
                    sheetData.add(rowData.toArray(new Object[rowData.size()]));
                }
            }


            if ((!readHeaders && sheetData.isEmpty()) || (readHeaders && sheetData.size() <= 1)) {
                System.out.print("Warning!!!! ,No matching data found on csv file: " + filename);
                //todo.. logging of warning
            }


        } catch (Throwable e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                    return null;
                    //todo.. logging of exception or warning
                }
            }
        }
        return sheetData.iterator();
    }

    /***
     * returns the value from the given Map with respective key .
     *
     * @param map Map with String and Object generics
     * @param key String value
     * @return Object
     */
    public static Object getValue(Map<String, Object> map, String key) {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if ((entry.getKey() == null && key == null)
                    || (entry.getKey() != null && entry.getKey()
                    .equalsIgnoreCase(key)))
                return entry.getValue();
        }
        return null;
    }

    /***
     * It iterates the from the data iterator and entityClass map objects , stores the respective
     * data in the provided class objects and returs the List of Objects array
     *
     * @param dataIterator   data of the class Objects
     * @param entityClazzMap Map with Class Name and class object
     * @return List of Objects array .
     * @throws Exception
     */
    public static List<Object[]> getEntityData(Iterator<Object[]> dataIterator, LinkedHashMap<String, Class<?>> entityClazzMap) throws Exception {
        List<Object[]> list = new ArrayList<Object[]>();
        // Get the headers
        Object[] headerArray = null;
        if (dataIterator.hasNext()) {
            headerArray = dataIterator.next();
        }

        while (dataIterator.hasNext()) {

            Object[] rowDataArray = dataIterator.next();
            Map<String, Object> map = new LinkedHashMap<String, Object>();

            List<Object> rowData = new ArrayList<Object>();
            for (int j = 0; j < rowDataArray.length; j++) {
                String header = (String) headerArray[j];
                map.put(header, rowDataArray[j]);
            }

            Map<String, Boolean> temp = new HashMap<String, Boolean>();
            if (entityClazzMap != null) {
                for (Map.Entry<String, Class<?>> entry : entityClazzMap.entrySet()) {
                    temp.put(entry.getKey(), Boolean.TRUE);
                    rowData.add(readObject(entry.getValue(), entry.getKey(),
                            map));
                }
            }

            for (int i = rowDataArray.length - 1; i >= 0; i--) {

                int docIdx = ((String) headerArray[i]).indexOf(".");
                if (docIdx < 0 && temp.get(headerArray[i]) == null) {
                    rowData.add(0, rowDataArray[i]);
//				} else if (temp.get(((String) headerArray[i]).substring(0,
//						docIdx)) == null) {
//					rowData.add(0, rowDataArray[i]);
                }
            }

            list.add(rowData.toArray(new Object[]{rowData.size()}));
        }
        return list;
    }

    /***
     * @param clz
     * @param objectName
     * @param dataMap
     * @return
     * @throws Exception
     */
    public static Object readObject(Class<?> clz, String objectName,
                                    Map<String, Object> dataMap) throws Exception {
        Object object = null;
        if (clz == null)
            return null;
        if (objectName == null)
            objectName = clz.getSimpleName();
        Map<String, Object> fieldMap = getFieldsNeedToBeSet(dataMap, objectName);
        Map<String, Object> datamap = getFieldsDataNeedToBeSet(dataMap,
                objectName);

        for (String fieldName : fieldMap.keySet()) {
            String first = "" + fieldName.charAt(0);
            String realfieldName = fieldName.replaceFirst(first,
                    first.toLowerCase());
            Object fieldValue = null;
            Class<?> type = null;
            try {
                Class<?>[] parameterTypes = new Class<?>[]{};
                Method method = clz
                        .getMethod("get" + fieldName, parameterTypes);
                type = method.getReturnType();
                fieldValue = _readFieldValueObject(type,
                        method.getGenericReturnType(), datamap, fieldName);
            } catch (NoSuchMethodException ex) {
                try {
                    Class<?>[] parameterTypes = new Class<?>[]{};
                    Method method = clz.getMethod("is" + fieldName,
                            parameterTypes);
                    type = method.getReturnType();
                    fieldValue = _readFieldValueObject(type,
                            method.getGenericReturnType(), datamap, fieldName);
                } catch (NoSuchMethodException ex2) {
                    try {
                        Field field = clz.getDeclaredField(realfieldName);
                        fieldValue = _readFieldValueObject(field.getType(), field.getGenericType(), datamap, fieldName);
                    } catch (NoSuchFieldException ex3) {
                        if (clz.isArray()) {
                            fieldValue = _readFieldValueObject(clz,
                                    clz, datamap, null);
                            return fieldValue;
                        } else if (isPrimitive(clz)) {
                            fieldValue = _readFieldValueObject(clz,
                                    null, datamap, objectName);
                            return fieldValue;
                        }
                        try {
                            fieldValue = _readFieldValueObject(String.class, String.class, datamap, fieldName);
                            type = String.class;
                        } catch (Exception e) {
                            //todo.. logging of exception or warning
                        }
                    }
                }
            }
            // execute the Setter Method
            try {
                if (fieldValue != null) {

                    if (object == null) {
                        try {
                            object = clz.newInstance();
                        } catch (InstantiationException e) {
                            Class<?>[] parameterTypes = new Class<?>[1];
                            parameterTypes[0] = fieldValue.getClass();
                            Constructor<?> constructor = clz.getDeclaredConstructor(parameterTypes);
                            constructor.setAccessible(true);
                            object = constructor.newInstance(fieldValue);
                            return object;
                        }
                    }
                }
                if (fieldValue != null) {

                    if (object == null)
                        object = clz.newInstance();
                    try {
                        Class<?>[] parameterTypes = new Class<?>[1];
                        parameterTypes[0] = type;
                        Method method = object.getClass().getMethod(
                                "set" + fieldName, parameterTypes);
                        method.invoke(object, fieldValue);
                    } catch (Exception ex) {
                        Field field2 = object.getClass().getDeclaredField(
                                realfieldName);
                        field2.setAccessible(true);
                        field2.set(object, fieldValue);
                    }
                }
            } catch (Exception e) {
                //todo.. logging of exception or warning
            }
        }

        return object;
    }

    /***
     * @param map
     * @param key
     * @return
     */
    public static Map<String, Object> getFieldsNeedToBeSet(
            Map<String, Object> map, String key) {
        Map<String, Object> result = new LinkedHashMap<String, Object>();
        String lastKey = "";
        for (String key2 : map.keySet()) {
            if (key2.equalsIgnoreCase(key))
                result.put(key2, map.get(key2));
            if (key2.toLowerCase().startsWith(key.toLowerCase() + ".")) {
                String newkey = key2.substring(key.length() + 1);
                if (newkey.contains("."))
                    newkey = newkey.substring(0, newkey.indexOf("."));
                if (!newkey.equalsIgnoreCase(lastKey))
                    result.put(newkey, map.get(key2));
            }
        }
        return result;
    }

    /***
     * @param map
     * @param key
     * @return
     */
    public static Map<String, Object> getFieldsDataNeedToBeSet(
            Map<String, Object> map, String key) {
        Map<String, Object> result = new LinkedHashMap<String, Object>();

        for (String key2 : map.keySet()) {
            if (key2.equalsIgnoreCase(key)) {
                if (map.get(key2) != null)
                    result.put(key2, map.get(key2).toString());
            }
            if (key2.toLowerCase().startsWith(key.toLowerCase() + ".")) {
                if (map.get(key2) != null)
                    result.put(key2.substring(key.length() + 1), map.get(key2)
                            .toString());
            }
        }
        return result;
    }

    /***
     * @param fieldClz
     * @param type
     * @param dataMap
     * @param combinedFieldName
     * @return
     * @throws Exception
     */
    private static Object _readFieldValueObject(Class<?> fieldClz, Type type,
                                                Map<String, Object> dataMap, String combinedFieldName)
            throws Exception {
        Object fieldValue = null;

        if (fieldClz.isArray()) {
            int size = SpireCommonUtil.getArraySize(dataMap, combinedFieldName);
            if (size > 0) {
                fieldValue = Array.newInstance(fieldClz.getComponentType(),
                        size);
                for (int j = 0; j < size; j++) {
                    if (combinedFieldName == null)
                        Array.set(
                                fieldValue,
                                j,
                                readFieldValue(fieldClz.getComponentType(),
                                        "" + j, dataMap));
                    else
                        Array.set(
                                fieldValue,
                                j,
                                readFieldValue(fieldClz.getComponentType(),
                                        combinedFieldName + "." + j, dataMap));
                }
            }
        } else if (List.class.isAssignableFrom(fieldClz)) {
            ArrayList list = ArrayList.class.newInstance();
            int size = SpireCommonUtil.getArraySize(dataMap, combinedFieldName);
            if (size > 0) {
                fieldValue = list;
                Class<?> itemClz = getListItemType(type);
                for (int j = 0; j < size; j++) {
                    list.add(readFieldValue(itemClz, combinedFieldName + "."
                            + j, dataMap));
                }
            }
        } else if (fieldClz.isAssignableFrom(Set.class)) {
            Set list = LinkedHashSet.class.newInstance();
            int size = SpireCommonUtil.getArraySize(dataMap, combinedFieldName);
            if (size > 0) {
                fieldValue = list;
                Class<?> itemClz = getListItemType(type);
                for (int j = 0; j < size; j++) {
                    list.add(readFieldValue(itemClz, combinedFieldName + "."
                            + j, dataMap));
                }
            }
        } else {
            fieldValue = readFieldValue(fieldClz, combinedFieldName, dataMap);
        }

        return fieldValue;
    }


    private static boolean isPrimitive(Class<?> clz) {
        if (clz.isPrimitive())
            return true;
        else if (clz.getCanonicalName().equals(
                "java.lang." + clz.getSimpleName()))
            return true;
        else
            return false;
    }


    /***
     * @param fieldClz
     * @param fieldName
     * @param dataMap
     * @return
     * @throws Exception
     */
    private static Object readFieldValue(Class<?> fieldClz, String fieldName,
                                         Map<String, Object> dataMap) throws Exception {
        Object fieldValue = null;
        String tempValue = (String) getValue(dataMap, fieldName);
        // Return null when field is atomic and value is null or blank
        if ((tempValue == null || tempValue.length() == 0)
                && (fieldClz.isEnum()
                || fieldClz.getName().equals("java.util.Calendar")
                || fieldClz.getName().equals("java.math.BigDecimal") || isPrimitive(fieldClz)))
            return null;

        if (fieldClz.isEnum()) {
            try {
                fieldValue = fieldClz.getMethod("valueOf", String.class)
                        .invoke(fieldClz, tempValue);
            } catch (Exception e) {
                //todo.. logging of exception or warning
            }
        } else if (fieldClz.getName().equals("java.util.Calendar")) {
            Calendar calendar = Calendar.getInstance();
            try {
                calendar.setTime(new SimpleDateFormat("MM/dd/yyyy").parse(tempValue));
            } catch (ParseException ex) {
                try {
                    calendar.setTime(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(tempValue));
                } catch (ParseException ex2) {
                    calendar.setTime(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSz").parse(tempValue));

                }

            }
            fieldValue = calendar;
        } else if (fieldClz.getName().equals("java.math.BigDecimal")) {
            fieldValue = new BigDecimal(tempValue);
        } else if (isPrimitive(fieldClz)) {
            Constructor<?> constructor;
            try {
                if (fieldClz.getName().equals("java.lang.String")) {
                    fieldValue = tempValue;
                } else {
                    if (PRIMITIVE_TYPE_MAP.containsKey(fieldClz))
                        constructor = PRIMITIVE_TYPE_MAP.get(fieldClz)
                                .getConstructor(String.class);
                    else
                        constructor = fieldClz.getConstructor(String.class);

                    fieldValue = constructor.newInstance(tempValue);
                }
            } catch (Exception e) {
                //todo.. logging of exception or warning
            }
        } else {
            fieldValue = readObject(fieldClz, fieldName, dataMap);
        }

        return fieldValue;
    }

    /***
     * @param type
     * @return
     * @throws ClassNotFoundException
     */
    private static Class<?> getListItemType(Type type)
            throws ClassNotFoundException {

        Class<?> itemClz = null;
        if (type instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) type;
            itemClz = Class.forName(pt.getActualTypeArguments()[0].toString()
                    .substring("class ".length()));
        }
        return itemClz;
    }


}
