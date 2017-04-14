package model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

public class Check {
  private static final String LOCAL_LIST_PATH = "local.xml";  
  private static Document document; 
  private static String last;
	public static boolean check_icp(String icp) {
		int len = icp.length();
		if (len<14) {
			return false;
		}
		if (!abbreviation(icp.substring(0, 1))) {
			return false;
		}
		if (!CheckIcpNum(icp.substring(5, 13))) {
			return false;
		}
		last = icp.substring(14,len);
		if (last.contains("-")) {
			if (!CheckIcpNum(last.substring(last.lastIndexOf("-")+1))) {
				return false;
			}
		}
		return true;
	}
	/**
	 * 验证icp省份简称是否正确
	 * @param abbr
	 * @return
	 */
	protected static boolean abbreviation(String abbr) {
    try {  
        document = new SAXReader().read(LOCAL_LIST_PATH);
        String[] abbrs = document.getStringValue().trim().split("\n");
        for (String string : abbrs) {
        	if (abbr.equals(string.trim())) {
        		return true;
					}
				}
    } catch (DocumentException e) {  
        e.printStackTrace();  
    }
		return false; 
	}
	protected static boolean CheckIcpNum(String info) {
		Pattern r = Pattern.compile("[0-9]\\d*");
		Matcher m = r.matcher(info);
		return m.matches();
	}
}
