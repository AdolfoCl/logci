package cl.ies.logci.utils;

import java.text.ParseException;
import java.util.Map;

import cl.ies.logci.beans.Base;

public interface ProcessFile {

	public void loadLine(String line) throws ParseException;
	
	public Map<String, Base> getlBases();

	public void setlBases(Map<String, Base> lBases);

	public int getMaxLen();

	public void setMaxLen(int maxLen);
	
}
