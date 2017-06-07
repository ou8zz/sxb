package com.sxb.util;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.logging.LogFactory;

import com.sxb.model.LessLevel;

/**
 * @description convenient to handle email string.
 * @author <a href="mailto:ou8zz@sina.com">OLE</a>
 * @date Jul 29, 2011  9:48:26 PM
 * @version 3.0
 */
public class RegexUtil {
	public static final Pattern mailtoPattern = Pattern.compile("mailto:([a-zA-Z0-9\\.]+@[a-zA-Z0-9\\.]+\\.[a-zA-Z0-9]+)");
	public static final Pattern emailPattern = Pattern.compile("\\b[a-zA-Z0-9\\.]+(@)([a-zA-Z0-9\\.]+)(\\.)([a-zA-Z0-9]+)\\b");
	
	public static String encodeEmail(String str) {
		Matcher mailtoMatch = mailtoPattern.matcher(str);
		while (mailtoMatch.find()) {
			String email = mailtoMatch.group(1);
			String hexed = encode(email);
			str = str.replaceFirst("mailto:" + email, "mailto:" + hexed);
		}

		return obfuscateEmail(str);
	}

	public static String obfuscateEmail(String str) {
		Matcher emailMatch = emailPattern.matcher(str);
		while (emailMatch.find()) {
			String at = emailMatch.group(1);

			str = str.replaceFirst(at, "-AT-");

			String dot = emailMatch.group(2) + emailMatch.group(3)
					+ emailMatch.group(4);
			String newDot = emailMatch.group(2) + "-DOT-" + emailMatch.group(4);

			str = str.replaceFirst(dot, newDot);
		}
		return str;
	}

	public static ArrayList<String> getMatches(Pattern pattern, String match, int group) {
		ArrayList<String> matches = new ArrayList<String>();
		Matcher matcher = pattern.matcher(match);
		while (matcher.find()) {
			matches.add(matcher.group(group));
		}
		return matches;
	}

	public static String encode(String email) {
		StringBuffer result = new StringBuffer();
		try {
			char[] hexString = Hex.encodeHex(email.getBytes("UTF-8"));
			for (int i = 0; i < hexString.length; i++) {
				if (i % 2 == 0) {
					result.append("%");
				}
				result.append(hexString[i]);
			}
		} catch (UnsupportedEncodingException e) {
			return email;
		}

		return result.toString();
	}
	
	public static boolean verify(String regex, String str) {
		Pattern p = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
		return p.matcher(str).matches();
	}
	
	public static boolean notEmpty(Object str) {
		return (str == null || "".equals(str)) ? false:true;
	}
	
	public static boolean isEmpty(Object str) {
		return (str == null || str.equals("")) ? true:false;
	}
	
	/**
     * 格式化成小数点(#.##)
     * @param float
     * @param str
     * @return String
     */ 
	public static String formatDecimal(Float f, String str) {
		DecimalFormat df = new DecimalFormat(str);
        return df.format(f);
    }
	
	/**
	 * 随机根据配置
	 * 读取配置等级wr.less11 至 wr.less33
	 * 随机数量获取格式json {"less":200,"min":2,"max":30}
	 * less 随机数量最大上限，如果count超过该数值则原数据返回
	 * min 每次随机累加最小数量
	 * max 每次随机累加最大数量
	 * @param count	随机人数
	 * @return count 随机数
	 */
	public static int watchCount(int count) {
		try {
			if(!"true".equals(ResourceUtil.getConf("wr.enable").trim())) return 0;		// 是否启用
			for(int i=11; i<20; i++) {
				String str = ResourceUtil.getConf("wr.less"+i);
				if(RegexUtil.isEmpty(str)) return 0;
				LessLevel lessLevel = JsonUtils.fromJson(str, LessLevel.class);
				int less = lessLevel.getLess();
				if(count < less) {
					int wrmin = lessLevel.getMin();
					int wrmax = lessLevel.getMax();
					Random rand = new Random();
				    int randomNum = rand.nextInt((wrmax - wrmin) + 1) + wrmin;
					return randomNum;
				}
			}
		} catch (Exception e) {
			LogFactory.getLog(RegexUtil.class).error("watchCount is error", e);
		}
    	return 0;
	}
	
	public static void main(String [] a) {
		System.out.println(RegexUtil.watchCount(600));
	}
}