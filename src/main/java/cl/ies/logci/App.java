package cl.ies.logci;

import cl.ies.logci.utils.LoadLog;
import cl.ies.logci.utils.ProcessCH1;
import cl.ies.logci.utils.ProcessCH2;
import cl.ies.logci.utils.ProcessCI;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	String tiposValidos = "ci ch1 ch2 ch3";
    	String tipo = null;
    	String archivo = null;
    	
    	if(args.length < 1){
    		System.out.println("Debe indicar el nombre del archivo a procesar!");
    		System.exit(0);
    	}else{
    		if(args.length == 2){
    			tipo = args[0];
    			archivo = args[1];
    		}else{
    			archivo = args[0];
    			tipo = getTipo(archivo);
    			if(!tiposValidos.contains(tipo.toLowerCase())){
    				System.out.println("No se pudo obtener el tipo!");
    				System.exit(0);
    			}
    		}
    	}
        
        System.out.println( "Procesando archivo >"+ archivo +"<");
        
        try{
	        LoadLog loadLog = new LoadLog();
	        if("ci".equalsIgnoreCase(tipo)) {
	        	loadLog.process(new ProcessCI(), archivo);
	        }else if("ch1".equalsIgnoreCase(tipo)){
	        	loadLog.process(new ProcessCH1(), archivo);
	        }else if("ch2".equalsIgnoreCase(tipo)){
	        	loadLog.process(new ProcessCH2(), archivo);
	        }else if("ch3".equalsIgnoreCase(tipo)){
	        	loadLog.process(new ProcessCH2(), archivo);
	        }
        }catch (Exception e){
        	System.out.println(e);
        }
    }
    private static String getTipo(String archivo){
    	String[] chuncks = archivo.split("-");
    	if(chuncks.length != 3){
    		return null;
    	}
    	String[] ls = chuncks[2].split("\\.");
    	if(ls.length != 2){
    		return null;
    	}
		return ls[0];
    }
}
