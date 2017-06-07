package com.sxb.util;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

/**
 * @description JSON数据操作 的常用方法
 * <p>
 * 该工具类使用Gson转换引擎 <a href="http://code.google.com/p/google-gson/" target="_blank">Google Gson</a>
 * 下面是使用案例：
 * </p>
 * 
 * <pre>
 * public class User {  
 *  {@literal @SerializedName("pwd")}
 *  private String password;
 *  
 *  {@literal @Expose}
 *  {@literal @SerializedName("uname")}
 *  private String username;
 *  
 *  {@literal @Expose}
 *  private String gender;
 *  
 *  {@literal @Expose}
 *  private String sex;
 *  
 *  public User() {}
 *  
 *  public User(String username, String password, String gender) {
 *    // user constructor code... ... ... 
 *  }
 *  
 *  public String getUsername()
 *  ... ... ...
 *}
 * 
 * List<User> userList = new LinkedList<User>();
 * User jack = new User("Jack", "123456", "Male");
 * User marry = new User("Marry", "888888", "Female");
 *   userList.add(jack);
 *   userList.add(marry);
 *   
 *Type targetType = new TypeToken<List<User>>(){}.getType();
 *String sUserList1 = JSONUtils.toJson(userList, targetType);
 *       sUserList1 ----> [{"uname":"jack","gender":"Male","sex":"Male"},{"uname":"marry","gender":"Female","sex":"Female"}]
 *       
 *String sUserList2 = JSONUtils.toJson(userList, targetType, false);
 *       sUserList2 ----> [{"uname":"jack","pwd":"123456","gender":"Male","sex":"Male"},{"uname":"marry","pwd":"888888","gender":"Female","sex":"Female"}]
 *       
 *String sUserList3 = JSONUtils.toJson(userList, targetType, true);
 *       sUserList3 ----> [{"uname":"jack","gender":"Male","sex":"Male"},{"uname":"marry","gender":"Female","sex":"Female"}]
 * </pre>
 * @author <a href="mailto:ou8zz@sina.com">OLE</a>
 * @date Aug 7, 2011 11:04:25 PM
 * @version 3.0
 */
public class JsonUtils {
private static final Log log = LogFactory.getLog(JsonUtils.class);
	// empty JOSN data
	public static final String EMPTY_JSON = "{}";
	// empty array
	public static final String EMPTY_JSON_ARRAY = "[]";
	// default date format style
	public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
	public static final String SHORT_DATE_PATTERN = "yyyy-MM-dd";
	public static final String DATA_TIME ="HH:mm:ss";
	
	/**
	 * 将给定目标根据指定条件参数转换成JSON格式的字符串。
	 * <p/>
	 * <strong>该方法转换发生错误时，不会抛出任何异常。
	 * 若发生错误，普通返回<code>"{}</code>;
	 *            集合或数组对象返回<code>"[]"</code>
	 *</strong>
	 * 
	 * @param target     目标对象
	 * @param targetType  目标对象类型
	 * @param isSerializeNulls   是否序列化{@code null}值字段
	 * @param datePattern  日期字段的格式化模式
	 * @param excludesFieldsWithoutExpose 是否排除未瓢{@literal @Expose} 注解的字段
	 * @return  目标对象的{@code JSOn} 格式的字符串
	 */
	public static String toJson(Object target, Type targetType, boolean isSerializeNulls, String datePattern, boolean excludesFieldsWithoutExpose) {
		if(target == null) return EMPTY_JSON;
		GsonBuilder builder = new GsonBuilder();
		if(isSerializeNulls) builder.serializeNulls();
		if(isEmpty(datePattern)) datePattern = DEFAULT_DATE_PATTERN;
		builder.setDateFormat(datePattern);
		if(excludesFieldsWithoutExpose)
			builder.excludeFieldsWithoutExposeAnnotation();
		String result = "";
		//对一个含有泛型参数的Map属性的pojo, gson的toJson方法使得map中的数据丢失, MyMapTypeAdapter作为改进.
		Gson gson = builder.registerTypeAdapter(java.util.Date.class,new SQLDateSerializer()).setDateFormat(datePattern).create();
		//registerTypeAdapter(Map.class, new MyMapTypeAdapter()).create();
		try {
			if(targetType != null)
				result = gson.toJson(target, targetType);
			else result = gson.toJson(target);
		}catch(Exception ex) {
			log.warn("target Object is:" + target.getClass().getName() + " occured error when transform!" , ex);
			if(target instanceof Collection<?> || target instanceof Iterator<?>  || target instanceof Enumeration<?> || target.getClass().isArray())
				result = EMPTY_JSON_ARRAY;
			else result = EMPTY_JSON;
		}
		return result;
	}
	
	/**
	 * 将给定的目标对象转换成 {@code JSON} 格式的字符串。
	 * <strong>此方法只用来转换普通的 {@code JavaBean} 对象。</strong>
	 * 
	 * <ul>  
     *   <li>该方法将转换所有非空字段；</li>  
     *   <li>该方法不会转换 {@code null} 值字段；</li>  
     *   <li>该方法会转换所有未标注或已标注 {@literal @Since} 的字段；</li>  
     *   <li>该方法转换时使用默认的 日期/时间 格式化模式 - {@code yyyy-MM-dd HH:mm:ss}.</li>  
     * </ul> 
     * 
	 * @param target
	 * @return
	 */
	public static String toJson(Object target) {
		return toJson(target,null,false,null,false);
	}
	
	/**
	 * 将给定的目标对象转换成 {@code JSON} 格式的字符串。
	 * <strong>此方法只用来转换普通的 {@code JavaBean} 对象。</strong>
	 * <ul>
	 *   <li>该方法只会转换标有 {@literal @Expose} 注解的字段；</li>
	 *   <li>该方法不会转换 {@code null} 值字段；</li>
	 *   <li>该方法会转换所有未标注或已标注 {@literal @Since} 的字段；</li>
	 * </ul>
	 * 
	 * @param target  要转换成 {@code JSON} 的目标对象
	 * @param datePattern  日期字段的格式化模式
	 * @return  目标对象的 {@code JSON} 格式的字符串
	 */
	public static String toJson(Object target, String datePattern) {
		return toJson(target, null, false, datePattern, true);
	}
	
	/**
	 * 将给定的目标对象转换成 {@code JSON} 格式的字符串
	 * <strong>此方法只用来转换普通的 {@code JavaBean} 对象。</strong>
	 * 
	 * <ul>
	 *   <li>该方法不会转换 {@code null} 值字段；</li>
	 *   <li>该方法会转换所有未标注或已标注 {@literal @Since} 的字段；</li>
	 *   <li>该方法转换时使用默认的 日期/时间 格式化模式 - {@code yyyy-MM-dd HH:mm:ss}；</li>
	 * </ul>
	 * 
	 * @param target  要转换成 {@code JSON} 的目标对象
	 * @param excludesFieldsWithoutExpose  是否排除未标注 {@literal @Expose} 注解的字段
	 * @return  目标对象的 {@code JSON} 格式的字符串
	 */
	public static String toJson(Object target, boolean excludesFieldsWithoutExpose) {
		return toJson(target, null, false, null, excludesFieldsWithoutExpose);
	}
	
	/**
	 * 将给定的目标对象转换成 {@code JSON} 格式的字符串
	 * <strong>此方法通常用来转换使用泛型的对象。</strong>
	 * 
	 * <ul>
	 *   <li>该方法将转换所有非空字段；</li>
	 *   <li>该方法不会转换 {@code null} 值字段；</li>
	 *   <li>该方法会转换所有未标注或已标注 {@literal @Since} 的字段；</li>
	 *   <li>该方法转换时使用默认的 日期/时间 格式化模式 - {@code yyyy-MM-dd HH:mm:ss}；</li>
	 * </ul>
	 * 
	 * @param target  要转换成 {@code JSON} 的目标对象
	 * @param targetType  目标对象的类型
	 * @return  目标对象的 {@code JSON} 格式的字符串
	 */
	public static String toJson(Object target, Type targetType) {
		return toJson(target, targetType, false, null, false);
	}
	
	/**
	 * 将给定的目标对象转换成 {@code JSON} 格式的字符串
	 * <strong>此方法通常用来转换使用泛型的对象</strong>
	 * 
	 * <ul>
	 *   <li>该方法不会转换 {@code null} 值字段；</li>
	 *   <li>该方法会转换所有未标注或已标注 {@literal @Since} 的字段；</li>
	 *   <li>该方法转换时使用默认的 日期/时间 格式化模式 - {@code yyyy-MM-dd HH:mm:ss}；</li>
	 * </ul>
	 * 
	 * 
	 * @param target  要转换成 {@code JSON} 的目标对象
	 * @param targetType  目标对象的类型
	 * @param excludesFieldsWithoutExpose  是否排除未标注 {@literal @Expose} 注解的字段
	 * @return  目标对象的 {@code JSON} 格式的字符串
	 */
	public static String toJson(Object target, Type targetType, boolean excludesFieldsWithoutExpose) {
		return toJson(target, targetType, false, null, excludesFieldsWithoutExpose);
	}
	
	/**
	 * 将给定的目标对象转换成 {@code JSON} 格式的字符串
	 * <strong>此方法通常用来转换使用泛型的对象</strong>
	 * 
	 * <ul>
	 *   <li>该方法不会转换 {@code null} 值字段；</li>
	 *   <li>该方法会转换所有未标注或已标注 {@literal @Since} 的字段；</li>
	 *   <li>该方法转换时使用默认的 日期/时间 格式化模式 - {@code yyyy-MM-dd HH:mm:ss}；</li>
	 * </ul>
	 * 
	 * 
	 * @param target  要转换成 {@code JSON} 的目标对象
	 * @param targetType  目标对象的类型
	 * @param datePattern 日期格式
	 * @return  目标对象的 {@code JSON} 格式的字符串
	 */
	public static String toJson(Object target, Type targetType, String datePattern) {
		return toJson(target, targetType, false, datePattern, false);
	}
	
	/**
	 * 将给定的目标对象转换成 {@code JSON} 格式的字符串
	 * <strong>此方法通常用来转换使用泛型的对象</strong>
	 * 
	 * <ul>
	 *   <li>该方法不会转换 {@code null} 值字段；</li>
	 *   <li>该方法会转换所有未标注或已标注 {@literal @Since} 的字段；</li>
	 *   <li>该方法转换时使用默认的 日期/时间 格式化模式 - {@code yyyy-MM-dd HH:mm:ss}；</li>
	 * </ul>
	 * 
	 * 
	 * @param target  要转换成 {@code JSON} 的目标对象
	 * @param targetType  目标对象的类型
	 * @param datePattern 日期格式
	 * @param excludesFieldsWithoutExpose  是否排除未标注 {@literal @Expose} 注解的字段
	 * @return  目标对象的 {@code JSON} 格式的字符串
	 */
	public static String toJson(Object target, Type targetType, String datePattern, boolean excludesFieldsWithoutExpose) {
		return toJson(target, targetType, false, datePattern, excludesFieldsWithoutExpose);
	}
	
	/**
	 * 将给定的 {@code JSON} 字符串转换成指定的类型对象
	 * 
	 * @param <T>  要转换的目标类型
	 * @param json  给定的 {@code JSON} 字符串
	 * @param token  {@code com.google.gson.reflect.TypeToken} 的类型指示类对象
	 * @param datePattern  日期格式模式
	 * @return  给定的 {@code JSON} 字符串表示的指定的类型对象
	 */
	public static <T> T fromJson(String json, TypeToken<T> token, String datePattern) {
		if(isEmpty(json)) return null; 
		GsonBuilder builder = new GsonBuilder();
		if(isEmpty(datePattern))datePattern = DEFAULT_DATE_PATTERN;
		builder.setDateFormat(datePattern);
		
		Gson gson = builder.create();
		try {
			return gson.fromJson(json, token.getType());
			} catch(Exception ex) {
				log.error(json + " could not convert to " + token.getRawType().getName(), ex);
				return null;
				}
	}
	
	/**
	 * 将给定的 {@code JSON} 字符串转换成指定的类型对象
	 * 
	 * @param <T>  要转换的目标类型
	 * @param json  给定的 {@code JSON} 字符串
	 * @param token  {@code com.google.gson.reflect.TypeToken} 的类型指示类对象
	 * @return  给定的 {@code JSON} 字符串表示的指定的类型对象
	 */
	public static <T> T fromJson(String json, TypeToken<T> token) {
		return fromJson(json, token, null);
	}
	
	/**
	 * 将给定的 {@code JSON} 字符串转换成指定的类型对象
	 * <strong>此方法通常用来转换普通的 {@code JavaBean}对象</strong>
	 * 
	 * @param <T>  要转换的目标类型
	 * @param json  给定的 {@code JSON} 字符串
	 * @param clazz  要转换的目标类
	 * @param datePattern  日期格式模式
	 * @return  给定的 {@code JSON} 字符串表示的指定的类型对象
	 */
	public static <T> T fromJson(String json, Class<T> clazz, String datePattern) {
		if(isEmpty(json)) return null;
		GsonBuilder builder = new GsonBuilder();
		if(isEmpty(datePattern)) datePattern = DEFAULT_DATE_PATTERN;
		Gson gson = builder.create();
		try {
			return gson.fromJson(json, clazz);
		}catch(Exception ex) {
			log.error(json + " could not convert to " + clazz.getName(), ex);
			return null;
		}
	}
	
	/**
	 * 将给定的 {@code JSON} 字符串转换成指定的类型对象
	 * <strong>此方法通常用来转换普通的 {@code JavaBean}对象</strong>
	 * 
	 * 
	 * @param <T>  要转换的目标类型
	 * @param json  给定的 {@code JSON} 字符串
	 * @param clazz  要转换的目标类
	 * @return  给定的 {@code JSON} 字符串表示的指定的类型对象
	 */
	public static <T> T fromJson(String json, Class<T> clazz) {
		return fromJson(json, clazz, null);
	}
	
	/**
	 * 将给定的 {@code JSON} 字符串转换成指定的类型对象
	 * 
	 * 
	 * @param <T>  要转换的目标类型
	 * @param json  给定的 {@code JSON} 字符串
	 * @return  给定的 {@code JSON} 字符串表示的指定的类型对象
	 */
	public static <T> T fromJson(String json, Type type) {
		return new Gson().fromJson(json, type);
	}
	
	private static boolean isEmpty(String str) {
		return (str == null || str.equals("")) ? true:false;
	}
}
