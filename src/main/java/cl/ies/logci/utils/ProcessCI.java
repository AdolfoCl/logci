package cl.ies.logci.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import cl.ies.logci.beans.Base;

public class ProcessCI implements ProcessFile {
	
	private static Map<String, Base> lBases = new HashMap<String, Base>(); 
	
	private static int maxLen = 0;

	public void loadLine(String line) throws ParseException{
		String[] chuncks = line.split("\\s+");
		if(chuncks.length < 8) return;
		
		if("iniciando".equalsIgnoreCase(chuncks[5])){
			if(maxLen < chuncks[7].length()){
				maxLen = chuncks[7].length();
			}
			Base base = new Base();
			base.setName(chuncks[7]);
			base.setInicio(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(chuncks[0]+" "+chuncks[1]));
			lBases.put(base.getName(), base);
		}else if("terminando".equalsIgnoreCase(chuncks[5])){
			Base base = lBases.get(chuncks[7]);
			base.setTermino(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(chuncks[0]+" "+chuncks[1]));
		}
	}
	
	public Map<String, Base> getlBases() {
		return lBases;
	}

	public void setlBases(Map<String, Base> lBases) {
		ProcessCI.lBases = lBases;
	}

	public int getMaxLen() {
		return maxLen;
	}

	public void setMaxLen(int maxLen) {
		ProcessCI.maxLen = maxLen;
	}

	
}
