package cl.ies.logci.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import cl.ies.logci.beans.Base;

public class ProcessCH2 implements ProcessFile {

	private static Map<String, Base> lBases = new HashMap<String, Base>(); 
	
	private static int maxLen = 0;
	
	@Override
	public void loadLine(String line) throws ParseException {
		String[] chuncks = line.split("\\s+");
		if(chuncks.length < 8) return;
		
		String name = chuncks[8];
		
		if("iniciando".equalsIgnoreCase(chuncks[5])){
			
			if(maxLen < name.length()){
				maxLen = name.length();
			}
			Base base = new Base();
			base.setName(name);
			base.setInicio(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(chuncks[0]+" "+chuncks[1]));
			lBases.put(base.getName(), base);
		}else if("terminando".equalsIgnoreCase(chuncks[5])){
			Base base = lBases.get(name);
			base.setTermino(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(chuncks[0]+" "+chuncks[1]));
		}
	}

	@Override
	public Map<String, Base> getlBases() {
		return lBases;
	}

	@Override
	public void setlBases(Map<String, Base> lBases) {
		ProcessCH2.lBases = lBases;
	}

	@Override
	public int getMaxLen() {
		return maxLen;
	}

	@Override
	public void setMaxLen(int maxLen) {
		ProcessCH2.maxLen = maxLen;
	}

}
