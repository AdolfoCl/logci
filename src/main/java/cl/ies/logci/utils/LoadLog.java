	package cl.ies.logci.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import cl.ies.logci.beans.Base;

public class LoadLog {
	
	public void process(ProcessFile processFile, String archivo) throws IOException{
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		SimpleDateFormat sdfT = new SimpleDateFormat("HH:mm:ss");
		File f = new File(archivo);
		File salida = new File(archivo+".txt");
		
		if(!f.isFile()){
			System.out.println("No se puede leer el archivo de LOG. Verifique su existencia");
			return;
		}
		
		// if file doesnt exists, then create it
		if (!salida.exists()) {
			salida.createNewFile();
		}
		FileWriter fw = new FileWriter(salida.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		
		try(BufferedReader br = new BufferedReader(new FileReader(archivo))) {
			
		    for(String line; (line = br.readLine()) != null; ) {
		        
		    	try{
		    		processFile.loadLine(line);
		    	}catch(Exception e){
		    		System.out.println(e+" "+line );
		    	}
		    	
		    }
		    System.out.println(processFile.getlBases().size() + " registros leidos desde archivo log");
		    
		    long media = 0;
		    long max = Long.MIN_VALUE;
		    long min = Long.MAX_VALUE;
		    Date horaMin = null;
		    Date horaMax = null;
		    
		    Iterator<?> it = processFile.getlBases().entrySet().iterator();
		    while (it.hasNext()) {
		    	@SuppressWarnings("rawtypes")
				Map.Entry pair = (Map.Entry)it.next();
		    	Base b = (Base) pair.getValue();
		    	long secs = 0;
		    	StringBuilder txt = new StringBuilder();
		    	txt.append(pair.getKey()+repetir(" ", processFile.getMaxLen()-pair.getKey().toString().length()+1));
		    	if(b.getTermino() != null && b.getInicio() != null){
		    		if(horaMin == null){
		    			horaMin = b.getInicio();
		    			horaMax = b.getTermino();
		    		}
		    		if(horaMin.getTime() > b.getInicio().getTime()){
		    			horaMin = b.getInicio();
		    		}
		    		if(horaMax.getTime() < b.getTermino().getTime()){
		    			horaMax = b.getTermino();
		    		}
		    		secs = (b.getTermino().getTime() - b.getInicio().getTime()) / 1000;
		    		if(secs < min){
		    			min = secs;
		    		}
		    		if(secs > max){
		    			max = secs;
		    		}
		    		media += secs;
		    		txt.append(sdf.format(b.getInicio())+" -> "+sdfT.format(b.getTermino())+" = "+secs).toString();
		    	}else{
		    		if(b.getInicio() == null){
		    			txt.append("--/--/---- --:--:-- -> ");
		    		}else{
		    			txt.append(sdf.format(b.getInicio())+" -> ");
		    		}
		    		if(b.getTermino() == null){
		    			txt.append("--:--:--");
		    		}else{
		    			txt.append(sdfT.format(b.getTermino()));
		    		}
		    	}
		    	bw.write(txt.toString()+"\r\n");
		    }
		    if(processFile.getlBases().size() > 0){
		    	int[] d = null;
			    bw.write("\r\n");
			    if(horaMin != null && horaMax != null){
			    	long secs = (horaMax.getTime() - horaMin.getTime()) / 1000;
			    	d = splitToComponentTimes(secs);
			    	bw.write("Proceso de "+processFile.getlBases().size()+" bd's, desde: "+sdf.format(horaMin)+" hasta:"+sdf.format(horaMax)+" "+String.format("%02d:%02d:%02d", d[0], d[1], d[2])+"\r\n");
			    }
			    media = media / processFile.getlBases().size();
			    d = splitToComponentTimes(media);
			    bw.write("media: "+media+" "+String.format("%02d:%02d:%02d", d[0], d[1], d[2])+"\r\n");
			    d = splitToComponentTimes(min);
			    bw.write("min: "+min+" "+String.format("%02d:%02d:%02d", d[0], d[1], d[2])+"\r\n");
			    d = splitToComponentTimes(max);
			    bw.write("max: "+max+" "+String.format("%02d:%02d:%02d", d[0], d[1], d[2])+"\r\n");
		    }
		    bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private String repetir(String s, int largo){
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < largo; i++){
			sb.append(s);
		}
		return sb.toString();
	}
	
	private int[] splitToComponentTimes(long totalSecs){
		int hours = (int) totalSecs / 3600;
		int minutes = (int) (totalSecs % 3600) / 60;
		int seconds = (int)totalSecs % 60;
		int[] ints = {hours, minutes, seconds};
		return ints;
	}
	
}
