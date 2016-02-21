package com.projectning.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Formatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <code>Util</code> contains a variety of constants and static utility methods.
 */
public class Util {
	public final static List<String> ENABLED_LIST = Arrays.asList("true", "on",
			"enabled", "yes");

	public final static List<String> DISABLED_LIST = Arrays.asList(null,
			"false", "off", "disabled", "no");

	public final static String HEX_CHARS_STR = "0123456789abcdef";
	
	public final static String ALPHA_NUMERIC_STR = 
	    "01234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	
  public final static String PRINTABLE_CHAR_STR = ALPHA_NUMERIC_STR
      + "!@#$%^&*()_+-={}|[]:;'<>?,.";

	// public final static String FS = System.getProperty("file.separator");

  public static final Random rng = new Random();
	/**
	 * System-independent newline string for use in code that runs exclusively
	 * in Java.
	 * 
	 * @see #nl
	 */

	/**
	 * Newline string for use in code that runs through the GWT compiler.
	 * 
	 * Unfortunately the GWT compiler does not support System.getProperty() so
	 * any code using <code>NL</code> fails to compile. <code>nl</code> provides
	 * an inelegant workaround.
	 * 
	 * @see #NL
	 */
	// public final static String NL = System.getProperty("line.separator");
	public final static String NL = "\n";

	public final static String INDENT = "  ";

	public static boolean isEnabled(String value)
			throws IllegalArgumentException {
		if (ENABLED_LIST.contains(value)) {
			return true;
		}

		if (DISABLED_LIST.contains(value)) {
			return false;
		}

		throw new IllegalArgumentException("Invalid value of: " + value
				+ ". Expecting one of these: "
				+ ToStringHelper.arrayToString(ENABLED_LIST, ", ") + ", "
				+ ToStringHelper.arrayToString(DISABLED_LIST, ", "));
	}
	
  public static boolean isEnabled(String propertyName, Map<String, String> properties)
  {
    String value = properties.get(propertyName);

    return Util.isEnabled(value);
  }

	/**
	 * Returns whether a String is null or empty after removing leading and
	 * trailing white spaces.
	 * 
	 * @param str
	 *            String to check.
	 * 
	 * @return <code>true</code> if the String is null or empty; otherwise
	 *         <code>false</code>
	 */
	public static boolean isNullOrEmpty(String str) {
		if (str == null) {
			return true;
		}

		if (str.trim().length() == 0) {
			return true;
		}

		return false;
	}

	/**
	 * Returns whether a List is null or empty.
	 * 
	 * @param list
	 *            List to check.
	 * 
	 * @return <code>true</code> if <tt>list</tt> is null or empty; otherwise
	 *         <code>false</code>
	 */
	public static boolean isNullOrEmpty(List<? extends Object> list) {
		if (list == null) {
			return true;
		}

		if (list.size() == 0) {
			return true;
		}

		return false;
	}

	public static boolean isNullOrEmpty(
			Map<? extends Object, ? extends Object> map) {
		if (map == null) {
			return true;
		}

		if (map.size() == 0) {
			return true;
		}

		return false;
	}

	public static void preventNull(String name, Object o) {
		if (o == null) {
			throw new IllegalArgumentException(name + " is null");
		}
	}

  public static void preventNullOrEmpty(String name, String[] values)
    throws IllegalArgumentException 
  {
    if(values != null) 
    {
      for(String v : values)
      {
        if (Util.isNullOrEmpty(v)) 
        {
          throw new IllegalArgumentException(name + " has an empty or null value");
        }
      }
    }
    else
    {
      throw new IllegalArgumentException(name + " is empty or null");
    }
  }

	public static void preventNullOrEmpty(String name, String value)
			throws IllegalArgumentException {
		if (Util.isNullOrEmpty(value)) {
			throw new IllegalArgumentException(name + " is empty or null");
		}
	}

	public static void preventNullOrEmpty(List<? extends Object> list)
			throws IllegalArgumentException {
		preventNullOrEmpty(list, "List is empty or null");
	}

	public static void preventNullOrEmpty(List<? extends Object> list,
			String msg) throws IllegalArgumentException {
		if (Util.isNullOrEmpty(list)) {
			throw new IllegalArgumentException(msg);
		}
	}

	public static void preventOutOfBoundNumber(String name, int value,
			int lowerLimit, int upperLimit) throws IllegalArgumentException {
		if ((value < lowerLimit) || (value > upperLimit)) {
			throw new IllegalArgumentException(ToStringHelper.variableToString(
					name, value)
					+ " is out of bounds: "
					+ ToStringHelper.variableToString("lowerLimit", lowerLimit)
					+ ToStringHelper.variableToString("upperLimit", upperLimit));
		}
	}

	public static void preventNumberTooSmall(String name, int value,
			int lowerLimit) throws IllegalArgumentException {
		if (value < lowerLimit) {
			throw new IllegalArgumentException(ToStringHelper.variableToString(
					name, value)
					+ " must be greater than or equal to: "
					+ ToStringHelper.variableToString("lowerLimit", lowerLimit));
		}
	}

	public static void preventNumberTooBig(String name, int value,
			int upperLimit) throws IllegalArgumentException {
		if (value > upperLimit) {
			throw new IllegalArgumentException(ToStringHelper.variableToString(
					name, value)
					+ " must be less than or equal to: "
					+ ToStringHelper.variableToString("upperLimit", upperLimit));
		}
	}

	public static void preventListTooSmall(String name, List<?> value,
			int lowerLimit) throws IllegalArgumentException {
		if (value == null) {
			throw new IllegalArgumentException(name + " cannot be null");
		}

		if (value.size() < lowerLimit) {
			throw new IllegalArgumentException(ToStringHelper.variableToString(
					name + " size", value.size())
					+ " must be less than or equal to: "
					+ ToStringHelper.variableToString("lowerLimit", lowerLimit));
		}
	}

	public static void preventNotEqual(int expected, int actual) {
		if (actual != expected) {
			throw new IllegalArgumentException("Unexpected value. Expected: "
					+ expected + ". Actual: " + actual + ".");
		}
	}

	/**
	 * Returns a random integer such that:
	 * <p>
	 * <tt>0 &lt;= &lt;returned integer&gt; &lt; max</tt>
	 * <p>
	 * That is to say values of <tt>max</tt> are not returned.
	 * 
	 * @param max
	 *            Specifies to return integers less than this value.
	 * 
	 */
	public static int getRandom(int max) {
		return (int) (Math.random() * (max));
	}

	/**
	 * Returns a random integer such that:
   * <p>
   * <tt>min &lt;= &lt;returned integer&gt; &lt; max</tt>
   * <p>
   * That is to say values of <tt>max</tt> are not returned.
	 * 
	 * @param min
	 * @param max
	 * @return
	 */
  public static int getRandom(int min, int max)
  {
    // nextInt is normally exclusive of the top value,
    // so add 1 to make it inclusive
    int randomNum = rng.nextInt((max - min) + 1) + min;

    return randomNum;
  }

  public static boolean getRandomBoolean()
  {
    int rand = Util.getRandom(2);
    
    if(rand == 0)
    {
      return false;
    }
    else
    {
      return true;
    }
  }
	/**
	 * Returns the contents of a list in a random order.
	 * 
	 * @param <E>
	 *            Any Object
	 * @param sourceList
	 *            List of <tt>Object</tt>s to randomly order.
	 * 
	 * @return A {@link Pair} containing:
	 *         <ol>
	 *         <li>As the first member: a randomly ordered <tt>List</tt>
	 *         containing a shallow copy of the elements in <tt>sourceList</tt>.
	 *         <li>As the second member: A <tt>int[]</tt> where each element
	 *         corresponds, in order, to the elements in the returned
	 *         <tt>List</tt> (the first member of the <tt>Pair</tt>. The value
	 *         of each array element specifies the index of where in find the
	 *         scrambled element in the <tt>sourceList</tt>.
	 *         </ol>
	 *         For example, if <tt>sourceList</tt> contained the following
	 *         elements in this order:
	 *         <ul>
	 *         <li>0: "one"
	 *         <li>1: "two"
	 *         <li>2: "three"
	 *         <li>3: "four"
	 *         </ul>
	 *         and <tt>scrambleList()</tt> returned them in this order:
	 *         <ul>
	 *         <li>0: "three"
	 *         <li>1: "two"
	 *         <li>2: "four"
	 *         <li>3: "one"
	 *         </ul>
	 *         then, the returned <tt>int[]</tt> contains:
	 *         <ul>
	 *         <li>[0]: 2
	 *         <li>[1]: 1
	 *         <li>[2]: 3
	 *         <li>[3]: 0
	 *         </ul>
	 */
	public static <E extends Object> Pair<List<E>, int[]> scrambleList(
			List<E> sourceList) {
		int count = sourceList.size();

		// Each element in this array holds the index to the element in
		// sourceList.
		// That is to say: the value stored in randomToOrinal[0], say 3, means
		// that the
		// 1st element in scrambledList corresponds to the 3rd element in
		// sourceList.
		int[] randomToOriginal = new int[count];

		if (count == 0) { // check whether count is zero before start the loop,
							// throw exception
			return new Pair<List<E>, int[]>(sourceList, randomToOriginal);
		}

		// Shallow copy of the list.
		List<E> tempList = new ArrayList<E>(sourceList);

		// The to-be-returned randomized list.
		List<E> scrambledList = new ArrayList<E>(count);

		int r = 0;

		do {
			// Get a random number from 0 to count.
			int rInt = Util.getRandom(count);

			E curElt = tempList.get(rInt);

			// Put that element into the return list.
			scrambledList.add(tempList.get(rInt));

			int s = 0;

			for (E elt : sourceList) {
				if (curElt == elt) {
					randomToOriginal[r] = s;
					break;
				}

				++s;
			}

			// Remove it from our source list.
			tempList.remove(rInt);

			++r;
			--count;
		} while (count > 0);

		return new Pair<List<E>, int[]>(scrambledList, randomToOriginal);
	}

	public static String[] makeStringArray(String... strings) {
		return strings;
	}

	public static String[] makeStringArray(List<String> stringList) {
		return stringList.toArray(new String[0]);
	}

	/**
	 * Returns a String representation of a <tt>Throwable</tt>'s stack trace.
	 * This is GWT compiler-safe.
	 * 
	 * @param throwable
	 * @return The call stack or an empty string if none.
	 */
	public static String getStackTrace(Throwable throwable,
			boolean htmlFormatting) {
		// Build a String holding the stack trace
		StackTraceElement[] stackTraceElements = throwable.getStackTrace();

		StringBuilder sb = new StringBuilder();

		for (StackTraceElement ste : stackTraceElements) {
			sb.append(ste.toString());

			if (htmlFormatting) {
				sb.append("<br/>");
			} else {
				sb.append("\n");
			}
		}

		return sb.toString();
	}

	/**
	 * Returns the root cause of an exception.
	 * <p>
	 * <tt>getRootCause</tt> recursively traverses an exception's cause member
	 * so long as the cause is not
	 * <tt>null<tt> and is not the current exception itself. 
	 * When either of those conditions are met (there is no further cause or the current cause 
	 * and it's parent exception are the same), <tt>getRootCause</tt> returns
	 * the current exception.
	 * 
	 * @param t
	 *            exception to process
	 * @return root cause of <tt>t</tt>
	 */
	public static Throwable getRootCause(Throwable t) {
		Throwable cause = t.getCause();

		if ((cause != null) && (cause != t)) {
			return getRootCause(cause);
		} else {
			return t;
		}
	}

	/**
	 * Traverses the causes of an exception looking for a target type.
	 * 
	 * @param t
	 *            The exception to process
	 * @param causeWithThisClass
	 *            The Class of the exception of interest.
	 * @return The exception of type <tt>causeWithThisClass</tt>, if found;
	 *         otherwise <tt>null</tt>
	 */
	public static Throwable findCauseOfClassType(Throwable t,
			Class<? extends Exception> causeWithThisClass) {
		boolean isMatch = (t.getClass() == causeWithThisClass);

		if (isMatch) {
			return t;
		} else {
			Throwable curCause = t.getCause();

			if ((curCause != null) && (curCause != t)) {
				return findCauseOfClassType(curCause, causeWithThisClass);
			} else {
				return null;
			}
		}
	}

	/**
	 * Assists a class to enforce the classes intention to be a singleton. The
	 * calling class should declare:
	 * <p>
	 * <code>
	 * public static boolean isInstatiated;
	 * </code>
	 * <p>
	 * In its constructor, the class calls this method as:
	 * <p>
	 * <code>
	 * isInstatiated = verifySingleton(classType, isInstatiated);
	 * </code>
	 * <p>
	 * <p>
	 * <b>Note:</b> You need to be vigilant when calling
	 * <tt>verifySingleton()</tt> in super- and subclasses. For example, assume
	 * (1) B extends A and (2) B is a singleton.
	 * <p>
	 * If you never expect your code to instantiate A directly, then you should
	 * call <tt>verifySingleton()</tt> in A's constructor. This protects against
	 * a piece of code requesting an instance of A independently (of B). You
	 * could additionally call <tt>verifySingleton()</tt> from B, but it is not
	 * necessary.
	 * <p>
	 * However, if you expect your code to use B as a singleton and to use
	 * additional instances of A (other than as a superclass of B), then you
	 * must call <tt>verifySingleton()</tt> in B; but not in A.
	 * 
	 * @param classType
	 *            Class name
	 * @param isInstantiated
	 *            <tt>true</tt> if the class has already been instantiated;
	 *            otherwise <tt>false</tt> that the class has not yet been
	 *            instantiated.
	 * 
	 * @return <tt>true</tt> if the value of <tt>isInstantiated</tt> was
	 *         <tt>false</tt>.
	 * 
	 * @throws SingletonException
	 *             when the current value of <tt>isInstantiated</tt> is
	 *             <tt>true</tt>.
	 */
	public static boolean verifySingleton(String classType,
			boolean isInstantiated) throws SingletonException {
		if (isInstantiated) {
			throw new SingletonException(classType);
		}

		return true;
	}

	public static Map<String, String> parseStringIntoMap(String data) throws IllegalArgumentException {
		HashMap<String,String> map = new HashMap<String,String>();
		Matcher kvpMatcher = Pattern.compile(
			"([a-zA-Z_0-9\\-]+)" // Key
			+ "\\s*=\\s*" // Equals
			+ "\"([a-zA-Z_0-9\\-]+)\"" // Value
			+ "(?=(,\\s*)|(\\z))" // Pair delimiter
		).matcher(data.trim());
		while (kvpMatcher.find()) {
			String key = kvpMatcher.group(1);
			String value = kvpMatcher.group(2);
			map.put(key, value);
		}
		return map;
	}
	
	public static String byteArrayToHexadecimalString(byte[] data) {
		Formatter formatter = new Formatter();
		for (byte b : data) {
			formatter.format("%02x", b);
		}
		String hex = formatter.toString();
		formatter.close();
		return hex;
	}
	
	public static String generateHexString(Random rng, int length)
  {
	  return Util.generateString(rng, Util.HEX_CHARS_STR, length);
  }

	public static String generateAlpaNumericString(Random rng, int length)
	{
	  return Util.generateString(rng, Util.ALPHA_NUMERIC_STR, length);
	}
	
  public static String generatePrintableCharsString(Random rng, int length)
  {
    
    return Util.generateString(rng, Util.PRINTABLE_CHAR_STR, length);
  }
	
	public static String generateString(Random rng, String characters, int length)
	{
	    char[] text = new char[length];
	    for (int i = 0; i < length; i++)
	    {
	        text[i] = characters.charAt(rng.nextInt(characters.length()));
	    }
	    return new String(text);
	}
	
	public static String urlEncode(String url, int times) throws UnsupportedEncodingException
	{
	  String after = url;
	  
	  for(int i=0; i < times; ++i)
    {
	    after = URLEncoder.encode(after, "UTF-8");
    }
	  
    return after;
	}
	
	public static <E extends Enum<E>> void unexpectedEnumValue(E enumValue)
	{
	  throw new IllegalStateException("Uh oh: Someone forgot to handle the " 
	    + enumValue.getDeclaringClass().getName()
	    + " type: " 
	    + enumValue.toString());
	}
	
	
	 public static String getSimpleClassType(Object o)
	  {
	    return getSimpleClassType(o, true);   
	  }
	  
	  public static String getSimpleClassType(Object o, boolean allowNullOrEmpty)
	  {
	    if (o == null)
	    {
	      if(allowNullOrEmpty)
	      {
	        return "";
	      }
	      else
	      {
	        throw new IllegalArgumentException("getSimpleClassType: null not allowed");
	      }
	    }
	    else
	    {
	      return getSimpleClassType(o.getClass().getName(), allowNullOrEmpty);
	    }
	  }
	  
	  public static String getSimpleClassType(String fullName)
	  {
	    return getSimpleClassType(fullName, true);
	  }

	  public static String getSimpleClassType(String fullName, boolean allowNullOrEmpty)
	  {
	    if (Util.isNullOrEmpty(fullName))
	    {
	      if(allowNullOrEmpty)
	      {
	        return "";
	      }
	      else
	      {
	        throw new IllegalArgumentException("getSimpleClassType: null or empty not allowed");
	      }
	    }
	    else
	    {
	      int lastDot = fullName.lastIndexOf('.');

	      if (lastDot == -1)
	      {
	        return fullName;
	      }
	      else
	      {
	        return fullName.substring(lastDot + 1);
	      }
	    }
	  }
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  //Start functions that were copied from the previous Util.java in the tutor
	  
	  
	  public static boolean isDisabled(String value)
	  {
	    String lcValue = value.toLowerCase();
	    
	    if(DISABLED_LIST.contains(lcValue))
	    {
	      return true;
	    }

	    if(ENABLED_LIST.contains(lcValue))
	    {
	      return false;
	    }
	    
	    throw new IllegalArgumentException("Invalid value of: "
	        + value + ". Expecting one of these: " 
	        + ToStringHelper.arrayToString(DISABLED_LIST, ", ")
	        + ", "
	        + ToStringHelper.arrayToString(ENABLED_LIST, ", ")
	        );
	  }

	  public static void failOnNullOrEmpty(String name, String value)
	  {
	    if (isNullOrEmpty(value))
	    {
	      throw new IllegalStateException("Not allowed here: " +  ToStringHelper.variableToString(name, value));
	    }
	  }
	  
	  /**
	   * Null-safe compare of two Strings
	   * @param s1 one string
	   * @param s2 the other string
	   * @return true if both are null or both are contain the same string value; otherwise false 
	   */
	  public static boolean equals(String s1, String s2)
	  {
	    // If s1 is null...
	    // then if s2 is null, they are equal
	    // otherwise, both are non-null and return the result of a case-sensitive string compare.
	    return (s1 == null ? s2 == null : s1.equals(s2));
	  }
	  
	  public static String removeWhitespace(String str)
	  {
	    // compress one or more whitespaces to nothing
	    str = str.replaceAll("\\s+", "");
	    
	    return str;
	  }
	    
	  public static String compressWhitespace(String str)
	  {
	    // Clean up to:
	    // (1) remove leading and trailing whitespaces, and 
	    str = str.trim();

	    // (2) compress multiple whitespaces to a single space.
	    str = str.replaceAll("\\s+", " ");

	    return str;
	  }

	  /**
	   * Returns the characters between the first occurrence of two delimiters
	   * @param str  String to process
	   * @param start Starting delimiter
	   * @param end Ending delimiter
	   * @return The string extracted from between <code>start</code> and <code>end</code> or 
	   * <code>null</code> if:
	   * <ul>
	   * <li>either delimiter was not found, or</li>
	   * <li>only one of the delimiters was not found, or</li>
	   * <li>the <code>end</code> delimiter was found before the <code>start</code> delimiter</li>
	   * </ul>  
	   */
	  public static String getStringBetween(String str, char start, char end)
	  {
	    int from = str.indexOf(start);
	    int to = str.lastIndexOf(end);
	    
	    // Note: If you change this to be a more general method by making
	    // start and end Strings, then need to account for the length of start
	    // and end before doing these tests and before calling substring.
	    
	    // Missing one of the delimiters
	    if ((from == -1) || (to == -1))
	    {
	      return null;
	    }
	    
	    // The delimiters were the same and found only 1 instance of it
	    if(from == to)
	    {
	      return null;
	    }
	    
	    // Found end before start
	    if(to < from)
	    {
	      return null;
	    }
	    
	    return str.substring(from+1, to);
	  }

	  /**
	   * Returns the characters between the first occurrence of two delimiters
	   * @param str  String to process
	   * @param startDelim Starting delimiter
	   * @param endDelim Ending delimiter
	   * @return The string extracted from between <code>start</code> and <code>end</code> or 
	   * <code>null</code> if:
	   * <ul>
	   * <li>either delimiter was not found, or</li>
	   * <li>only one of the delimiters was not found, or</li>
	   * <li>the <code>end</code> delimiter was found before the <code>start</code> delimiter</li>
	   * </ul>  
	   */
	  public static String getStringBetween(String str, String startDelim, String endDelim)
	  {
	    int from = str.indexOf(startDelim);
	    
	    if(from == -1)
	    {
	      return null;
	    }
	    
	    int afterFrom = from + startDelim.length(); // + 1;
	    
	    int to = str.indexOf(endDelim, afterFrom); 
	    
	    // Missing end delim
	    if (to == -1)
	    {
	      return null;
	    }
	        
	    return str.substring(afterFrom, to);
	  }

	  /**AS-794*/
	  //Neil's requesting case, use the stack to verify valid parentheses
	  public static boolean isValidParentheses(String value) 
	  {
	  
	    char[] charArray = value.toCharArray();
	    
	    HashMap<Character, Character> map = new HashMap<Character, Character>();
	    map.put('(', ')');
	   
	    Stack<Character> stack = new Stack<Character>();
	   
	    for (Character c : charArray) {
	      if (map.keySet().contains(c)) {
	        stack.push(c);
	      } else if (map.values().contains(c)) {
	        if (!stack.isEmpty() && map.get(stack.peek()) == c) {
	          stack.pop();
	        } else {
	          return false;
	        }
	      }
	    }
	    return stack.isEmpty();
	    
	  }
	  /***/

	  public static String quoteString(String s)
	  {
	    return "\"" + s + "\"";
	  }

	  public static Map<String, Pair<String,String>> tripletToMap(String value, String listDelim, String tripletDelim)
	  {
	    Map<String, Pair<String,String>> map = new HashMap<String, Pair<String,String>>();
	    
	    if (value == null)
	    {
	      value = "";
	    }
	    
	    String[] items = value.split(listDelim);
	    
	    for(String item : items)
	    {
	      if (item.length() > 0)
	      {
	        String[] triple = item.split(tripletDelim);
	        
	        if (triple.length == 3)
	        {
	          map.put(triple[0], new Pair<String,String>(triple[1],triple[2]));
	        }
	        else
	        {
	          throw new IllegalArgumentException("Invalid Triplet item: " + item + " from string: '" + value + "'");
	        }
	      }
	    }
	    
	    return map;
	  }
	
	  public static String instantToString(Instant instant)
	  {
	    if(instant == null)
	    {
	      return "";
	    }
	    
	    LocalDate ld = LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate();
	    return ld.toString();
	  }
	  
	  public static LocalDate getRandomDate(int startYear, int endYear)
	  {    
	    int minDay = (int) LocalDate.of(startYear, 1, 1).toEpochDay();
	    int maxDay = (int) LocalDate.of(endYear, 1, 1).toEpochDay();
	    long randomDay = minDay + rng.nextInt(maxDay - minDay);

	    LocalDate randomBirthDate = LocalDate.ofEpochDay(randomDay);

	    return randomBirthDate;
	  }

  public static UUID getUUID( )
  {
    return UUID.randomUUID();
  }
	  
  public static String getUUIDStr( )
  {
    return UUID.randomUUID().toString();
  }

  public static <T> Set<T> getSetFromList(List<T> list, Comparator<T> comparator)
  {
    Set<T> result = new TreeSet<T>(comparator);
    
    if(Util.isNullOrEmpty(list))
    {
      return result;
    }
    
    result.addAll(list);
    
    return result;
  }
}
