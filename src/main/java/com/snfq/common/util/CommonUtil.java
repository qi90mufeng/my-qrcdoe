package com.snfq.common.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snfq.common.constant.ErrorCode;
import com.snfq.common.exception.BizException;


public class CommonUtil {
	private static Logger log = LoggerFactory.getLogger(CommonUtil.class);
	
	private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	private static DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
	public static final long millOfOneDay = 86400000;

	/**
	 * 乘法 d1*d2
	 * 
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static Double mathMultiply(Double d1, Double d2) {
		return (BigDecimal.valueOf(d1).multiply(BigDecimal.valueOf(d2))).doubleValue();
	}

	/**
	 * 除法 d1/d2
	 * 
	 * @param d1
	 * @param d2
	 * @param scale
	 * @return
	 */
	public static Double mathDivide(Double d1, Double d2, int scale) {
		return (BigDecimal.valueOf(d1).divide(BigDecimal.valueOf(d2), MathContext.DECIMAL128)).setScale(scale, RoundingMode.HALF_UP).doubleValue();
	}

	/**
	 * 减法 d1-d2
	 * 
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static Double mathSubtract(Double d1, Double d2) {
		return (BigDecimal.valueOf(d1).subtract(BigDecimal.valueOf(d2))).doubleValue();
	}

	/**
	 * 加法 d1+d2
	 * 
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static Double mathAdd(Double d1, Double d2) {
		return (BigDecimal.valueOf(d1).add(BigDecimal.valueOf(d2))).doubleValue();
	}

	public static Integer getAgeFromPersonCard(String personCard) {
		int currentYear = Calendar.getInstance().get(Calendar.YEAR);
		int personYear = 0;
		if (personCard.length() == 18) {
			personYear = Integer.valueOf(personCard.substring(6, 10));
		}
		if (personCard.length() == 16) {
			personYear = Integer.valueOf(personCard.substring(6, 8)) + 1900;
		}
		if (personYear != 0) {
			return currentYear - personYear;
		}
		return null;
	}
	
	public static Integer getSexFromPersonCard(String personCard) {
		//外围判断身份证有效性
		char c = personCard.charAt(personCard.length()-2);
        int sex = Integer.parseInt(String.valueOf(c));
        if(sex%2==0){
            return 2; //女性
        }else{
            return 1; //男性
        }
	}

	/**
	 * 
	 * @param list
	 * @param type 1 正序，-1 倒序
	 * @param params
	 * @return
	 */
	public static <T> List<T> sort(List<T> list, final int type, String... params) {
		int plen = params.length;
		if (plen == 1) {
			final String f = params[0];
			Collections.sort(list, new Comparator<T>() {
				public int compare(T o1, T o2) {
					try {
						Method m = o1.getClass().getMethod("get" + f.substring(0, 1).toUpperCase() + f.substring(1), null);
						Object r1 = m.invoke(o1, null);
						Object r2 = m.invoke(o2, null);
						if (r1 == null || r2 == null) {
							throw new BizException(m.getName() + " return null", ErrorCode.ERROR_PARAM_LOSE.getErrorCode());
						}
						if (m.getReturnType().equals(String.class)) {
							return ((String) r1).compareTo((String) r2) * type;
						} else if (m.getReturnType().equals(Integer.class)) {
							return ((Integer) r1).compareTo((Integer) r2) * type;
						} else if (m.getReturnType().equals(Double.class)) {
							return ((Double) r1).compareTo((Double) r2) * type;
						} else if (m.getReturnType().equals(Long.class)) {
							return ((Long) r1).compareTo((Long) r2) * type;
						} else if (m.getReturnType().equals(Short.class)) {
							return ((Short) r1).compareTo((Short) r2) * type;
						} else if (m.getReturnType().equals(Float.class)) {
							return ((Float) r1).compareTo((Float) r2) * type;
						} else if (m.getReturnType().equals(Byte.class)) {
							return ((Byte) r1).compareTo((Byte) r2) * type;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					return 0;
				}
			});
			return list;
		} else {
			final String f = params[0];
			list = sort(list, f);
			List<T> total = new ArrayList<T>();
			Map<Object, List<T>> map = new LinkedHashMap<Object, List<T>>();
			for (T t : list) {
				try {
					Method m = t.getClass().getMethod("get" + f.substring(0, 1).toUpperCase() + f.substring(1), null);
					Object pv = m.invoke(t, null);
					List<T> tmpList = map.get(pv);
					if (tmpList == null) {
						tmpList = new ArrayList<T>();
						map.put(pv, tmpList);
					}
					tmpList.add(t);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			for (Map.Entry<Object, List<T>> entry : map.entrySet()) {
				total.addAll(sort(entry.getValue(), Arrays.copyOfRange(params, 1, params.length)));
			}
			return total;
		}
	}

	/**
	 * 多重排序 升序
	 * 
	 * @param list
	 * @param params 字段列表
	 * @return
	 */
	public static <T> List<T> sort(List<T> list, String... params) {
		int plen = params.length;
		if (plen == 1) {
			final String f = params[0];
			Collections.sort(list, new Comparator<T>() {
				public int compare(T o1, T o2) {
					try {
						Method m = o1.getClass().getMethod("get" + f.substring(0, 1).toUpperCase() + f.substring(1), null);
						Object r1 = m.invoke(o1, null);
						Object r2 = m.invoke(o2, null);
						if (r1 == null || r2 == null) {
							throw new BizException(m.getName() + " return null", ErrorCode.ERROR_PARAM_LOSE.getErrorCode());
						}
						if (m.getReturnType().equals(String.class)) {
							return ((String) r1).compareTo((String) r2);
						} else if (m.getReturnType().equals(Integer.class)) {
							return ((Integer) r1).compareTo((Integer) r2);
						} else if (m.getReturnType().equals(Double.class)) {
							return ((Double) r1).compareTo((Double) r2);
						} else if (m.getReturnType().equals(Long.class)) {
							return ((Long) r1).compareTo((Long) r2);
						} else if (m.getReturnType().equals(Short.class)) {
							return ((Short) r1).compareTo((Short) r2);
						} else if (m.getReturnType().equals(Float.class)) {
							return ((Float) r1).compareTo((Float) r2);
						} else if (m.getReturnType().equals(Byte.class)) {
							return ((Byte) r1).compareTo((Byte) r2);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					return 0;
				}
			});
			return list;
		} else {
			final String f = params[0];
			list = sort(list, f);
			List<T> total = new ArrayList<T>();
			Map<Object, List<T>> map = new LinkedHashMap<Object, List<T>>();
			for (T t : list) {
				try {
					Method m = t.getClass().getMethod("get" + f.substring(0, 1).toUpperCase() + f.substring(1), null);
					Object pv = m.invoke(t, null);
					List<T> tmpList = map.get(pv);
					if (tmpList == null) {
						tmpList = new ArrayList<T>();
						map.put(pv, tmpList);
					}
					tmpList.add(t);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			for (Map.Entry<Object, List<T>> entry : map.entrySet()) {
				total.addAll(sort(entry.getValue(), Arrays.copyOfRange(params, 1, params.length)));
			}
			return total;
		}
	}


	public static long convertIp2Number(String ip) {
		if (ip == null || !ip.matches("\\d+\\.\\d+\\.\\d+\\.\\d+")) {
			return -1;
		}
		String[] strs = ip.split("\\.");

		long[] ns = new long[4];
		for (int i = 0; i < strs.length; i++) {
			ns[i] = Long.valueOf(strs[i]);
			if (ns[i] > 255 || ns[i] < 0) {
				return -1;
			}
		}

		return ns[0] * 16777216 + ns[1] * 65536 + ns[2] * 256 + ns[3];
	}

	public static int getDayOfWeek(Date date) {
		if (date == null) {
			return -1;
		}
		Calendar c = Calendar.getInstance();
		c.setTime(date);

		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
		dayOfWeek = dayOfWeek - 2;
		if (dayOfWeek < 0) {
			dayOfWeek = dayOfWeek + 7;
		}
		return dayOfWeek;
	}

	public static int getDayOfWeek(Calendar c) {
		if (c == null) {
			return -1;
		}
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
		dayOfWeek = dayOfWeek - 2;
		if (dayOfWeek < 0) {
			dayOfWeek = dayOfWeek + 7;
		}
		return dayOfWeek;
	}

	/**
	 * 从指定的数组里随便返回一个对象
	 * 
	 * @param objs
	 * @return
	 */
	public static <T> T getRandomFromArray(T[] objs) {
		if (objs == null || objs.length == 0) {
			return null;
		}
		int index = (int) (Math.random() * objs.length);
		return objs[index];
	}

	/**
	 * 从字符串中获取一个 double
	 * 
	 * @param input
	 * @return
	 */
	public static Double getDoubleFromStr(String input) {
		if (input == null) {
			return null;
		}
		Pattern p = Pattern.compile("\\d+\\.\\d+");
		Matcher m = p.matcher(input);
		while (m.find()) {
			String str = m.group();
			return Double.valueOf(str);
		}
		return null;
	}



	/**
	 * 判断某输入字符串是否为数字格式
	 * 
	 * @param input
	 * @return
	 */
	public static Boolean isNumber(String input) {
		try {
			Double.valueOf(input);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 将数组转换成List
	 * 
	 * @param array
	 * @return
	 */
	public static <T> List<T> convertArrayToList(T[] array) {
		if (array == null || array.length == 0) {
			return null;
		}
		List<T> list = new ArrayList<T>(array.length);
		for (T t : array) {
			if (t == null) {
				continue;
			}
			list.add(t);
		}
		return list;
	}

	/**
	 * 将java属性转换成对应数据库字段形式,如 inputName > input_name , lastLoginTime > last_login_time
	 * 
	 * @return
	 */
	public static String convertJavaField2DB(String input) {
		if (input == null) {
			return null;
		}
		for (char c : input.toCharArray()) {
			int asscii = (int) c;
			if (asscii >= 65 && asscii <= 90) {
				input = input.replace(String.valueOf(c), "_" + (char) (asscii + 32));
			}
		}
		return input;
	}

	/**
	 * 判断输入是否为 email
	 * 
	 * @param input
	 * @return
	 */
	public static boolean isEmail(String input) {
		if (input == null) {
			return false;
		}
		return input.matches("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$");
	}

	/**
	 * 判断是否为空字符串
	 * 
	 * @param input
	 * @return
	 */
	public static Boolean isEmpty(String input) {
		if (input == null || input.matches("\\s*") || input.trim().equals("null")) {
			return true;
		}
		return false;
	}

	public static Boolean isEmpty(Integer input) {
		if (input == null) {
			return true;
		}
		return false;
	}

	public static Boolean isEmpty(Collection<?> collection) {
		if (collection == null || collection.size() == 0) {
			return true;
		}
		return false;
	}

	public static Boolean isEmpty(BigDecimal input) {
		if (input == null) {
			return true;
		}
		return false;
	}

	/**
	 * 判断输入字符串是否为手机号
	 * 
	 * @param phone
	 * @return
	 */
	public static Boolean isPhone(String phone) {
		if (phone == null) {
			return false;
		}
		if (phone.matches("1\\d{10}")) {
			return true;
		}
		return false;
	}

	public static Boolean isNotEmpty(Collection<?> collection) {
		return !isEmpty(collection);
	}

	public static Boolean isNotEmpty(Double input) {
		return !isEmpty(input);
	}

	public static Boolean isNotEmpty(BigDecimal input) {
		return !isEmpty(input);
	}

	private static boolean isEmpty(Double input) {
		return input == null;
	}

	public static Boolean isNotEmpty(Integer input) {
		return !isEmpty(input);
	}

	public static Boolean isNotEmpty(Map<?, ?> collection) {
		return !isEmpty(collection);
	}

	public static Boolean isEmpty(Map<?, ?> map) {
		if (map == null) {
			return true;
		}
		if (map.size() == 0) {
			return true;
		}
		return false;
	}

	public static Boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}

	public static Boolean isNull(Object obj) {
		return obj == null;
	}

	public static Boolean isNotNull(Object obj) {
		return obj != null;
	}

	private static Map<String, DateFormat> formats = new ConcurrentHashMap<String, DateFormat>();

	/**
	 * 时间格式化
	 * 
	 * @param date 需要格式化的时间
	 * @param format 时间格式
	 * @return
	 */
	public static String format(Date date, String format) {
		if (date == null) {
			return "";
		}
		DateFormat df = formats.get(format);
		if (formats.get(format) == null) {
			df = new SimpleDateFormat(format);
			formats.put(format, df);
		}
		return df.format(date);
	}

	/**
	 * 默认的时间格式化
	 * 
	 * @param date
	 * @return
	 */
	public static String format(Date date) {
		return format(date, "yyyy-MM-dd");
	}

	private static Map<String, NumberFormat> numberFs = new ConcurrentHashMap<String, NumberFormat>();

	/**
	 * 时间格式化
	 * 
	 * @param date 需要格式化的时间
	 * @param format 时间格式
	 * @return
	 */
	public static String format(Number number, String format) {
		if (number == null) {
			return "";
		}
		NumberFormat df = numberFs.get(format);
		if (numberFs.get(format) == null) {
			df = new DecimalFormat(format);
			numberFs.put(format, df);
		}
		return df.format(number);
	}

	public static String format(Number number) {
		return format(number, "0");
	}

	/**
	 * 将时间格式化成日期
	 * 
	 * @param date
	 * @return
	 */
	public static String formatDate(Date date) {
		return format(date, "yyyy-MM-dd");
	}

	/**
	 * 将时间格式化成完整的表达形式
	 * 
	 * @param date
	 * @return
	 */
	public static String formatTime(Date date) {
		return format(date, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 字符串截取
	 * 
	 * @param input
	 * @param size
	 * @return
	 */
	public static String substring(String input, Integer size) {
		if (input == null) {
			return "";
		}
		input = input.trim();
		if (input.length() > size) {
			input = input.substring(0, size) + "...";
		}
		return input;
	}


	/*----------------------list 与 string 互转处理 ---------------- */

	private static final String defaultSplit = ",";

	/**
	 * 将用默认分隔的字符串转换成 List
	 * 
	 * @param input
	 * @return
	 */
	public static List<String> convertStr2List(String input) {
		return convertStr2List(input, defaultSplit);
	}

	/**
	 * 将用指定字符分隔的字符串转换成 List
	 * 
	 * @param input
	 * @param split 分隔符
	 * @return
	 */
	public static List<String> convertStr2List(String input, String split) {
		if (input == null) {
			return null;
		}
		List<String> list = new ArrayList<String>();
		String[] strs = input.split(split);
		for (String str : strs) {
			if (!isEmpty(str)) {
				list.add(str.trim());
			}
		}
		return list;
	}

	public static Set<String> convertStr2Set(String input, String split) {
		if (input == null) {
			return null;
		}
		Set<String> list = new LinkedHashSet<String>();
		String[] strs = input.split(split);
		for (String str : strs) {
			if (!isEmpty(str)) {
				list.add(str.trim());
			}
		}
		return list;
	}

	public static List<Integer> convertStr2Intlist(String input, String split) {
		if (input == null) {
			return null;
		}
		List<Integer> list = new ArrayList<Integer>();
		String[] strs = input.split(split);
		for (String str : strs) {
			if (!isEmpty(str)) {
				list.add(Integer.valueOf(str));
			}
		}
		return list;
	}

	public static List<Integer> convertStr2IntList(String input) {
		if (input == null) {
			return new ArrayList<Integer>(0);
		}
		String[] strs = input.split(defaultSplit);
		List<Integer> list = new ArrayList<Integer>(strs.length);
		for (String s : strs) {
			if (s == null || !s.matches("\\d+")) {
				continue;
			}
			list.add(Integer.valueOf(s));
		}
		return list;
	}

	/**
	 * 将字符串集合转换成字符串,如: a,b,c,d
	 * 
	 * @param input
	 * @param split 分隔字符串
	 * @return
	 */
	public static String convertList2Str(List<String> input, String split) {
		if (input == null || input.size() <= 1) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < input.size(); i++) {
			sb.append(input.get(i).trim());
			if (i < input.size() - 1) {
				sb.append(split);
			}
		}
		return sb.toString();
	}

	/**
	 * 将字符串集合转换成字符串,如: ,a,b,c,d,
	 * 
	 * @param input
	 * @return
	 */
	public static String convertList2Str(List<? extends Object> input) {
		if (input == null) {
			return null;
		}
		StringBuffer sb = new StringBuffer(defaultSplit);
		for (Object string : input) {
			if (string == null || string.toString().matches("\\s*")) {
				continue;
			}
			sb.append(string.toString().trim());
			sb.append(defaultSplit);
		}
		return sb.toString();
	}

	/**
	 * 将字符串集合转换成字符串,如: ,a,b,c,d,
	 * 
	 * @param input
	 * @return
	 */
	public static String convertList2Str(String[] input) {
		if (input == null) {
			return null;
		}
		StringBuffer sb = new StringBuffer(defaultSplit);
		for (String string : input) {
			if (isEmpty(string)) {
				continue;
			}
			sb.append(string.trim());
			sb.append(defaultSplit);
		}
		return sb.toString();
	}

	/**
	 * 从字符串提取数字
	 * 
	 * @param input
	 * @return
	 */
	public static Integer getNumberFromStr(String input) {
		if (input == null) {
			return null;
		}
		Pattern p = Pattern.compile("\\d+");
		Matcher m = p.matcher(input);
		while (m.find()) {
			String str = m.group();
			return Integer.valueOf(str);
		}
		return null;
	}

	/**
	 * 从字符串提取数字
	 * 
	 * @param input
	 * @return
	 */
	public static List<Integer> getNumbersFromStr(String input) {
		if (input == null) {
			return null;
		}
		List<Integer> list = new ArrayList<Integer>();
		Pattern p = Pattern.compile("\\d+");
		Matcher m = p.matcher(input);
		while (m.find()) {
			String str = m.group();
			list.add(Integer.valueOf(str));
		}

		return list;
	}

	/**
	 * 获取匹配的字符串列表
	 * 
	 * @param input
	 * @param regex
	 * @return
	 */
	public static List<String> getMatchedStrs(String input, String regex) {
		if (input == null) {
			return null;
		}
		List<String> list = new ArrayList<String>();
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(input);
		while (m.find()) {
			String str = m.group();
			list.add(str.trim());
		}
		return list;
	}

	/**
	 * 获取匹配的字符串
	 * 
	 * @param input
	 * @param regex
	 * @return
	 */
	public static String getFirstMatchedStrs(String input, String regex) {
		if (input == null) {
			return null;
		}
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(input);
		while (m.find()) {
			return m.group();
		}
		return null;
	}

	public static String join(List<? extends Object> list, String split) {
		if (split == null || split.matches("\\s*")) {
			split = defaultSplit;
		}
		if (list == null || list.size() == 0) {
			return "";
		}

		StringBuilder sb = new StringBuilder();
		int i = 0;
		for (Object object : list) {
			if (object == null || isEmpty(object.toString())) {
				continue;
			}
			sb.append(object.toString());
			if (i != list.size() - 1) {
				sb.append(split);
			}
			i++;
		}

		String result = sb.toString();
		if (result.endsWith(split)) {
			result = result.substring(0, result.length() - split.length());
		}
		return result;
	}

	public static String join(Object[] list, String split) {
		if (split == null || split.matches("\\s*")) {
			split = defaultSplit;
		}
		if (list == null || list.length == 0) {
			return "";
		}

		StringBuilder sb = new StringBuilder(split);
		int i = 0;
		for (Object object : list) {
			if (object == null || isEmpty(object.toString())) {
				continue;
			}
			sb.append(object.toString());
			sb.append(split);
			i++;
		}
		String result = sb.toString();
		return result;
	}

	private final static double PI = 3.14159265358979323; // 圆周率
	private final static double R = 6371229; // 地球的半径
	private static NumberFormat nfKm = new DecimalFormat("#.#公里");
	private static NumberFormat nfKmS = new DecimalFormat("#公里");
	private static NumberFormat nfM = new DecimalFormat("0米");

	private static NumberFormat nfOnePoint = new DecimalFormat("#.#");
	private static NumberFormat nfZeroPoint = new DecimalFormat("0");

	public static String formatDistance(Double distance) {
		if (distance > 1000) {
			distance = distance / 1000d;
			if (distance > 100) {
				return nfKmS.format(distance);
			} else {
				return nfKm.format(distance);
			}
		} else {
			return nfM.format(distance);
		}
	}

	/**
	 * 将距离格式化
	 * 
	 * @param distance
	 * @return
	 */
	public static String[] formatDistanceSplit(Double distance) {
		if (distance > 1000) {
			distance = distance / 1000d;
			if (distance > 100) {
				return new String[] { nfZeroPoint.format(distance), "公里" };
			} else {
				return new String[] { nfOnePoint.format(distance), "公里" };
			}
		} else {
			return new String[] { nfZeroPoint.format(distance), "米" };
		}
	}

	/**
	 * 获取两坐标间的距离数，单位：米
	 * 
	 * @param lon1
	 * @param lat1
	 * @param lon2
	 * @param lat2
	 * @return
	 */
	public static Double getDistance(Double lon1, Double lat1, Double lon2, Double lat2) {
		if (lon1 == null || lat1 == null || lon2 == null || lat2 == null) {
			return -1d;
		}
		double x, y, distance;
		x = (lon2 - lon1) * PI * R * Math.cos(((lat1 + lat2) / 2) * PI / 180) / 180;
		y = (lat2 - lat1) * PI * R / 180;
		distance = Math.hypot(x, y);
		return distance;
	}

	private static NumberFormat percent = new DecimalFormat("#%");

	/**
	 * 以百分比形式输出
	 * 
	 * @param numerator 分子
	 * @param denominator 分母
	 * @return
	 */
	public static String getPersentStr(Integer numerator, Integer denominator) {
		if (numerator == null || denominator == null) {
			return "0%";
		}
		if (denominator == 0 || numerator == 0) {
			return "0%";
		}
		return percent.format(Double.valueOf(numerator) / Double.valueOf(denominator));
	}

	/**
	 * 除法操作
	 * 
	 * @param numerator 分子
	 * @param denominator分母
	 * @return
	 */
	public static Double divide(Integer numerator, Integer denominator) {
		if (denominator == 0 || numerator == 0) {
			return 0d;
		}
		return Double.valueOf(numerator) / Double.valueOf(denominator);
	}

	/**
	 * 酒店评论百分比像素计算
	 * 
	 * @param num
	 * @param totalNum
	 * @param maxWidth
	 * @return
	 */
	public Integer getCommentWidthPx(Integer num, Integer totalNum, Integer maxWidth) {
		if (num == null || totalNum == null || maxWidth == null) {
			return 0;
		}
		return (int) (Double.valueOf(num) / Double.valueOf(totalNum) * maxWidth);
	}

	/**
	 * 获取两个时间点间的时间差，以通俗形式返回
	 * 
	 * @param sample
	 * @param date
	 * @return
	 */
	public static String getSimpleDateDistance(Date sample, Date date) {
		if (sample == null || date == null) {
			return "";
		}
		long mill = Math.abs(date.getTime() - sample.getTime());
		long days = mill / 86400000l;
		if (days < 1) {
			return "今天";
		}
		if (days < 7) {
			return days + "天前";
		}
		if (days < 30) {
			return days / 7 + "周前";
		}
		if (days < 365) {
			return days / 30 + "个月前";
		}
		return days / 365 + "年前";
	}

	/**
	 * 字符串截取
	 * 
	 * @param input 输入源
	 * @param start 开始位置
	 * @param end 结束位置
	 * @return
	 */
	public static String substr(String input, String start, String end) {
		if (input == null) {
			return null;
		}
		int index = input.indexOf(start);
		int index2 = input.indexOf(end, index + start.length());
		if (index == -1 || index2 == -1 || index >= index2) {
			return null;
		}

		return input.substring(index + start.length(), index2);
	}

	/**
	 * 将全角字符替换成半角
	 * 
	 * @param input
	 * @return
	 */
	public static String replaceQuanJiao(String input) {
		if (input == null) {
			return null;
		}
		input = input.replaceAll("１", "1");
		input = input.replaceAll("２", "2");
		input = input.replaceAll("３", "3");
		input = input.replaceAll("４", "4");
		input = input.replaceAll("５", "5");
		input = input.replaceAll("６", "6");
		input = input.replaceAll("７", "7");
		input = input.replaceAll("８", "8");
		input = input.replaceAll("９", "9");
		input = input.replaceAll("０", "0");

		input = input.replaceAll("－", "-");
		input = input.replaceAll("＋", "+");

		input = input.replaceAll("；", ";");
		input = input.replaceAll("\\s+", "");
		return input;
	}

	/**
	 * 获取获取数字字符串
	 * 
	 * @param length 长度
	 * @return
	 */
	public static String getRandomNumber(Integer length) {
		if (length == null || length <= 0) {
			throw new NullPointerException();
		}

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			sb.append((int) (Math.random() * 10));
		}
		return sb.toString();
	}


	/**
	 * 从Ext表单获取电话信息时，调用此方法
	 * 
	 * @param record 必须包括 tel tel1 tel2 三个参数，以及相应的 get set 方法
	 */
	public static void setExtTel4Save(Object record) {
		try {
			Method getTel1 = record.getClass().getMethod("getTel1");
			Method getTel2 = record.getClass().getMethod("getTel2");

			Method setTel = record.getClass().getMethod("setTel", String.class);

			String tel1 = (String) getTel1.invoke(record);
			String tel2 = (String) getTel2.invoke(record);

			if (CommonUtil.isNotEmpty(tel1) && !tel1.matches("\\d+")) {
				throw new BizException("区号必须是数字", ErrorCode.ERROR_PARAM_ILLEGE.getErrorCode());
			}

			if (CommonUtil.isNotEmpty(tel2) && !tel2.matches("\\d+")) {
				throw new BizException("电话号码必须是数字", ErrorCode.ERROR_PARAM_ILLEGE.getErrorCode());
			}

			if (CommonUtil.isNotEmpty(tel1) && CommonUtil.isNotEmpty(tel2)) {
				setTel.invoke(record, tel1.trim() + "-" + tel2.trim());
			} else if (CommonUtil.isNotEmpty(tel2)) {
				setTel.invoke(record, tel2.trim());
			} else {
				setTel.invoke(record, new String(""));
			}
		} catch (BizException e) {
			throw new BizException(e);
		} catch (Exception e) {
			log.error("缺少相应的get或set方法");
		}
	}

	/**
	 * 从对象的电话信息输出到页面时，调用此方法
	 * 
	 * @param record 必须包括 tel tel1 tel2 三个参数，以及相应的 get set 方法
	 */
	public static void setExtTel4Detail(Object record) {
		try {
			Method getTel = record.getClass().getMethod("getTel");

			Method setTel1 = record.getClass().getMethod("setTel1", String.class);
			Method setTel2 = record.getClass().getMethod("setTel2", String.class);

			String tel = (String) getTel.invoke(record);

			// 电话号码处理
			if (tel != null) {
				String[] tels = tel.split("-");
				if (tels.length == 2) {
					setTel1.invoke(record, tels[0]);
					setTel2.invoke(record, tels[1]);
				} else {
					setTel2.invoke(record, tel);
				}
			}
		} catch (Exception e) {
			log.error("缺少相应的get或set方法");
		}
	}

	public static void setExtFax4Save(Object record) {
		try {
			Method getFax1 = record.getClass().getMethod("getFax1");
			Method getFax2 = record.getClass().getMethod("getFax2");

			Method setFax = record.getClass().getMethod("setFax", String.class);

			String fax1 = (String) getFax1.invoke(record);
			String fax2 = (String) getFax2.invoke(record);

			if (CommonUtil.isNotEmpty(fax1) && !fax1.matches("\\d+")) {
				throw new BizException("区号必须是数字", ErrorCode.ERROR_PARAM_ILLEGE.getErrorCode());
			}

			if (CommonUtil.isNotEmpty(fax2) && !fax2.matches("\\d+")) {
				throw new BizException("电话号码必须是数字", ErrorCode.ERROR_PARAM_ILLEGE.getErrorCode());
			}

			if (CommonUtil.isNotEmpty(fax1) && CommonUtil.isNotEmpty(fax2)) {
				setFax.invoke(record, fax1.trim() + "-" + fax2.trim());
			} else if (CommonUtil.isNotEmpty(fax2)) {
				setFax.invoke(fax2.trim());
			}
		} catch (BizException e) {
			throw new BizException(e);
		} catch (Exception e) {
			log.error("缺少相应的get或set方法");
		}
	}

	public static void setExtFax4Detail(Object record) {
		try {
			Method getFax = record.getClass().getMethod("getFax");

			Method setFax1 = record.getClass().getMethod("setFax1", String.class);
			Method setFax2 = record.getClass().getMethod("setFax2", String.class);

			String fax = (String) getFax.invoke(record);

			// 电话号码处理
			if (fax != null) {
				String[] faxs = fax.split("-");
				if (faxs.length == 2) {
					setFax1.invoke(record, faxs[0]);
					setFax2.invoke(record, faxs[1]);
				} else {
					setFax2.invoke(record, fax);
				}
			}
		} catch (Exception e) {
			log.error("缺少相应的get或set方法");
		}
	}

	public static Double cacAvg(Integer[] numbers) {
		if (numbers == null || numbers.length == 0) {
			return 0d;
		}
		int total = 0;
		int size = 0;
		for (Integer i : numbers) {
			if (i != null) {
				total += i;
				size++;
			}
		}

		if (size == 0) {
			return 0d;
		}

		return (double) total / (double) size;
	}

	public static Double cacAvg(List<Integer> numbers) {
		if (numbers == null || numbers.size() == 0) {
			return 0d;
		}
		int total = 0;
		int size = 0;
		for (Integer i : numbers) {
			if (i != null) {
				total += i;
				size++;
			}
		}

		if (size == 0) {
			return 0d;
		}

		return (double) total / (double) size;
	}


	public static Double multi(Double d1, Double d2) {
		if (d1 == null || d2 == null) {
			return 0d;
		}
		return d1 * d2;
	}

	public static Integer reduce(Integer i1, Integer i2) {
		if (i1 == null || i2 == null) {
			return 0;
		}
		return i1 - i2;
	}

	public static Boolean containAndEquals(Map<Object, Object> map, Object key, Object value) {
		if (map == null || key == null || value == null) {
			return false;
		}
		Object v = map.get(key);
		if (v != null && v.equals(value)) {
			return true;
		}
		return false;
	}

	public static Boolean mulityOf2(Integer num) {
		if (num == null) {
			return false;
		}
		return num % 2 == 0;
	}


	/**
	 * 获取N天后的日期
	 * 
	 * @param day
	 * @param afterDays 天数
	 * @return
	 */
	public static Date dayToPre(Date day, Integer preDays) {
		return new Date(day.getTime() - millOfOneDay * preDays);
	}

	/**
	 * 获取N天前的日期
	 * 
	 * @param day
	 * @param afterDays 天数
	 * @return
	 */
	public static Date dayToAfter(Date day, Integer afterDays) {
		return new Date(day.getTime() + millOfOneDay * afterDays);
	}

	/**
	 * 取当天时间，时分秒为当天最后一毫秒
	 * 
	 * @param day
	 * @return
	 */
	public static Date dayToEnd(Date day) {
		if (day == null) {
			return null;
		}
		try {
			return df2.parse(df.format(day) + " 23:59:59.999");
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 取当前日期，不含时分秒
	 * 
	 * @param day
	 * @return
	 */
	public static Date dayToShort(Date day) {
		if (day == null) {
			return null;
		}
		try {
			return df.parse(df.format(day));
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 下一天
	 * 
	 * @param day
	 * @return
	 */
	public static Date dayToNext(Date day) {
		if (day == null) {
			return null;
		}
		return new Date(day.getTime() + 86400000l);
	}

	/**
	 * 前一天
	 * 
	 * @param day
	 * @return
	 */
	public static Date dayToPre(Date day) {
		if (day == null) {
			return null;
		}
		return new Date(day.getTime() - 86400000l);
	}

	public static List<String> splitWithBlanks(String input, String split) {
		int start = 0;
		int end = 0;
		List<String> result = new ArrayList<String>();
		for (;;) {
			end = input.indexOf(split, start);
			if (end == -1) {
				result.add(input.substring(start));
				break;
			}
			String tmp = input.substring(start, end);
			result.add(tmp.trim());

			start = start + tmp.length() + split.length();
		}
		return result;
	}

	// ### Error updating database. Cause:
	// com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException:
	// Duplicate entry '4455' for key 'username'";
	public static String getDuplicateFieldWithErrormsg(String errorStack) {
		if (errorStack == null) {
			return null;
		}
		String[] strs = errorStack.split("\n");
		for (String str : strs) {
			int index = str.indexOf("Duplicate entry");
			if (index != -1) {
				str = str.substring(index);
				str = str.substring(0, str.lastIndexOf("'"));
				str = str.substring(str.lastIndexOf("'") + 1);
				return str;
			}
		}
		return null;
	}

	/**
	 * 获取IP地址
	 */
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	/**
	 * 判断是否手机号码 
	 */
	public static boolean isMobileNO(String mobiles) {

		if (isEmpty(mobiles)) {
			return false;
		}

		Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,2,5-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	/**
	 * 
	 * 
	 */
	public static String showMoneyChineseString(BigDecimal money) {

		double moneyT = money.doubleValue() / 10000;

		int moneyZ = (int) Math.floor(moneyT);

		if ((moneyT - moneyZ) > 0) {

			DecimalFormat df = new DecimalFormat("0.00");

			String ds = df.format(moneyT);

			return ds;

		} else {

			DecimalFormat df = new DecimalFormat("0");

			String ds = df.format(moneyT);

			return ds;
		}

	}

	/**
	 * 显示成百分比
	 */
	public static String percent(BigDecimal scales) {

		String resc = String.valueOf(ceil(scales, 2).multiply(new BigDecimal(100)));

		resc = resc.substring(0, resc.indexOf(".")) + "%";

		return resc;

	}

	/**
	 * @param d
	 * @param len
	 * @return
	 */
	public static BigDecimal ceil(BigDecimal d, int len) {
		DecimalFormat formater = new DecimalFormat();
		formater.setMaximumFractionDigits(len);
		formater.setGroupingSize(0);
		formater.setRoundingMode(RoundingMode.FLOOR);
		String str = formater.format(d);
		if (str.indexOf(".") == -1) {
			str += ".";
		}
		int strLenth = str.substring(str.indexOf(".") + 1).length();
		if (strLenth < len) {
			for (int i = strLenth; i < len; i++)
				str += "0";
		}
		return BigDecimal.valueOf(Double.parseDouble(str));
	}

	public static String getBigDecimalStr(BigDecimal d) {

		if (d == null) {
			return "0.00";
		}

		DecimalFormat myformat = new DecimalFormat("0.00");

		String str = myformat.format(d);

		return str;
	}

	/**
	 * 
	 * @Description: 隐藏用户名 @param: @param username @param: @return @return: String @throws
	 */
	public static String hideUsernameChar(String username) {

		if (username == null) {

			return null;

		} else {

			if (isMobileNO(username)) {

				return hidePhoneChar(username, 4);

			} else {
				String bb = username.substring(1, username.length() - 1);

				String cc = "";

				for (int i = 0; i < bb.length(); i++) {

					if (i == 0) {
						cc = bb.replace(String.valueOf(bb.charAt(i)), "*");
					} else {
						cc = cc.replace(String.valueOf(bb.charAt(i)), "*");
					}
				}

				return username.replace(bb, cc);
			}

		}
	}

	/**
	 * 
	 * @Description: 隐藏手机号码用 @param: @param str @param: @param len @param: @return @return: String @throws
	 */
	public static String hidePhoneChar(String str, int len) {
		if (str == null)
			return null;
		char[] chars = str.toCharArray();
		for (int i = 3; i < chars.length - 4; i++) {
			chars[i] = '*';
		}
		str = new String(chars);
		return str;
	}

	/**
	 * BigDecimal 减法
	 */
	public static BigDecimal subtract(BigDecimal b1, BigDecimal b2) {

		return ceil(b1.subtract(b2), 4);
	}

	/**
	 * BigDecimal 加法 
	 */
	public static BigDecimal add(BigDecimal b1, BigDecimal b2) {
		return ceil(b1.add(b2), 4);
	}

	/**
	 * BigDecimal 乘法 
	 */
	public static BigDecimal multiply(BigDecimal b1, BigDecimal b2) {

		return ceil(b1.multiply(b2), 4);
	}

	/**
	 * BigDecimal 除法 
	 */
	public static BigDecimal divide(BigDecimal b1, BigDecimal b2) {
		if (b1 == null || b2 == null || b2.intValue() == 0) {
			return BigDecimal.ZERO;
		}
		return ceil(b1.divide(b2, 4), 4);
	}

	/**
	 * double型截取4位
	 * 
	 * @param d
	 * @param len
	 * @return
	 */
	public static double ceil_d(double d, int len) {
		DecimalFormat formater = new DecimalFormat();
		formater.setMaximumFractionDigits(len);
		formater.setGroupingSize(0);
		formater.setRoundingMode(RoundingMode.FLOOR);
		String str = formater.format(d);
		if (str.indexOf(".") == -1) {
			str += ".";
		}
		int strLenth = str.substring(str.indexOf(".") + 1).length();
		if (strLenth < len) {
			for (int i = strLenth; i < len; i++)
				str += "0";
		}
		return Double.parseDouble(str);
	}

	/**
	 * 
	 * @Title: getLastSecIntegralTime 
	 */
	public static Date getLastSecIntegralTime(Date d) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(d.getTime());
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	/**
	 * 
	 * @Title: rollDay 
	 */
	public static Date rollDay(Date d, int day) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		cal.add(Calendar.DAY_OF_MONTH, day);
		System.out.println(cal.getTime());
		return cal.getTime();
	}

	public static Date rollMon(Date d, int mon) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		cal.add(Calendar.MONTH, mon);
		return cal.getTime();
	}

	public static Object avg(Object... input) {
		if (input == null || input.length == 0 || input[0] == null) {
			return null;
		}
		Class cls = input[0].getClass();
		if (cls == Integer.class) {
			Integer total = 0;
			for (Object obj : input) {
				if (obj != null) {
					total += ((Integer) obj);
				}
			}

		}

		return null;
	}

	public static String readFileContent(String path) {

		try {

			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path), "utf-8"));
			String tmp = null;
			StringBuilder sb = new StringBuilder();
			while ((tmp = br.readLine()) != null) {
				sb.append(tmp).append("\r\n");
			}
			br.close();
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	
	/**
	 * 随机生成文件名,输送到其他方接口
	 * 
	 * @param userId 用户ID
	 * @param suffix 带点后缀名     .jpg
	 * @param replace 是否将uuid中的-去掉
	 * @return
	 */
	public static String generateName(String userId,String suffix,boolean replace){
		StringBuffer fileName = new StringBuffer();
		
		if (isNotEmpty(userId)) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			String date = sdf.format(new Date());
			fileName.append("/").append(userId.substring(0, 6)).append("/").append(userId.substring(6, 8))
			.append("/").append(userId.substring(8, 10)).append("/").append(userId.substring(10, 12))
			.append("/").append(date.substring(0, 4)).append("/").append(date.substring(4, 6)).append("/")
			.append(date.substring(6, 8)).append("/");
		}
		UUID uuid = UUID.randomUUID();
		
		if (replace) {
			fileName.append(uuid.toString().replaceAll("-", ""));
		}else {
			fileName.append(uuid.toString());
		}
		if (isNotEmpty(suffix)) {
			fileName.append(suffix);
		}
		return fileName.toString();
	}
	
	public static String nullToStr(Object obj){
		if(obj == null){
			return "";
		}else{
			return obj.toString().trim();
		}
	}
	
	/** 
	   * 获取用户真实IP地址，不使用request.getRemoteAddr();的原因是有可能用户使用了代理软件方式避免真实IP地址, 
	   * 
	   * 可是，如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP值，究竟哪个才是真正的用户端的真实IP呢？ 
	   * 答案是取X-Forwarded-For中第一个非unknown的有效IP字符串。 
	   * 
	   * 如：X-Forwarded-For：192.168.1.110, 192.168.1.120, 192.168.1.130, 
	   * 192.168.1.100 
	   * 
	   * 用户真实IP为： 192.168.1.110 
	   * 
	   * @param request 
	   * @return 
	   */ 
	  public static String getIpAddress(HttpServletRequest request) { 
	    String ip = request.getHeader("x-forwarded-for"); 
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	      ip = request.getHeader("Proxy-Client-IP"); 
	    } 
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	      ip = request.getHeader("WL-Proxy-Client-IP"); 
	    } 
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	      ip = request.getHeader("HTTP_CLIENT_IP"); 
	    } 
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	      ip = request.getHeader("HTTP_X_FORWARDED_FOR"); 
	    } 
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	      ip = request.getRemoteAddr(); 
	    } 
	    return ip; 
	  } 
}
