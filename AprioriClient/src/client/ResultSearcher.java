package client;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;


/**
 * La classe ResultSearcher fornisce metodi statici per la ricerca degli item e dei pattern scelti dall'utente
 * che sono contenuti nei risultati dell'operazione di data-mining. 
 */
public class ResultSearcher {
	
	/**
	 * Il metodo statico getRowList suddivide la stringa dei risultati dell'operazione di data-mining in una 
	 * serie di righe (sotto stringhe) le quali vengono inserite in una linked list. 
	 * 	
	 * @param result stringa da suddividere dei risultati dell'operazione di data-mining 
	 * @return linked list contenente le sotto stringhe di result
	 */
	private static List<String> getRowList(String result){
		LinkedList<String> itemlist= new LinkedList<String>();
		int startindx=0;
		int endindx=0;
		while(startindx<result.length()){
			endindx=result.indexOf("\n", startindx);
			String substr =result.substring(startindx,endindx);
			if(!substr.equals("")){
				itemlist.add(substr);
			}	
			startindx=endindx+1;
		}
		return itemlist;
	}
	
	/**
	 * Il metodo statico getItemSet un insieme (set) degli item contenuti nei pattern della lista 
	 * ottenuta richiamando il metodo getRowList sulla stringa result.
	 *
	 * @param result stringa dei risultati dell'operazione di data-mining da cui ricavare l'insieme (set) degli item
	 * @return set di item contenuti nei pattern della lista delle sotto stringhe di result
	 */
	static Set<String> getItemSet(String result){
		Set<String> outputlist= new TreeSet<String>();
		List<String> itemlist= getRowList(result);
		for(int i=0; (i<itemlist.size()) && ( !(itemlist.get(i).contains("AND")) ) ; i++) {
			if(!itemlist.get(i).contains("in [") && !itemlist.get(i).contains("in ]")){
				int spaceindx=itemlist.get(i).indexOf(" ");
				int squareindx=itemlist.get(i).indexOf("[");
				outputlist.add(itemlist.get(i).substring(spaceindx,squareindx));
			}else {
				int spaceindx=itemlist.get(i).indexOf(" ");
				int squareindx=itemlist.get(i).indexOf("[",spaceindx);
				squareindx=itemlist.get(i).indexOf("[",squareindx+1);
				outputlist.add(itemlist.get(i).substring(spaceindx+1,squareindx+1));
			}
			
		}
		
		return outputlist;
	}
	
	/**
	 * Il metodo statico getAntecedentSet restituisce l'insieme (set) degli antecedenti ottenuti dalle
	 * regole di associazione presenti nella lista delle sotto stringhe, ottenuta richiamando il metodo getRowList 
	 * sulla stringa result. 
	 *
	 * @param result stringa dei risultati dell'operazione di data-mining dalla quale ricavare 
	 * l'insieme (set) degli antecedenti  
	 * @return set degli antecedenti contenuti nella stringa result
	 */
	static Set<String> getAntecedentSet(String result){
		Set<String> outputlist= new TreeSet<String>();
		List<String> itemlist= getRowList(result);
		for(int i=0; i<itemlist.size() ; i++){
			if(itemlist.get(i).contains("==>")){
				int spaceindx=itemlist.get(i).indexOf(" ");
				int antecedentindx=itemlist.get(i).indexOf("==>");
				outputlist.add(itemlist.get(i).substring(spaceindx,antecedentindx));
			}
		}
		
		return outputlist;
		
	}
	
	
	/**
	 * Il metodo statico getAntecedentSet restituisce l'insieme (set) dei conseguenti ottenuti dalle
	 * regole di associazione presenti nella lista delle sotto stringhe, ottenuta richiamando il metodo getRowList 
	 * sulla stringa result. 
	 *
	 * @param result stringa dei risultati dell'operazione di data-mining dalla quale ricavare 
	 * l'insieme (set) dei conseguenti  
	 * @return set dei conseguenti contenuti nella stringa result
	 */
	static Set<String> getConsequentSet(String result) {
		TreeSet<String> outputlist= new TreeSet<String>();
		List<String> itemlist= getRowList(result);
		for(int i=0; i<itemlist.size() ; i++){
			if(itemlist.get(i).contains("==>")){
				if(!itemlist.get(i).contains("in [") && !itemlist.get(i).contains("in ]")){
					int startconsindx=itemlist.get(i).indexOf(">");
					int endconsindx=itemlist.get(i).indexOf("[",startconsindx);
					outputlist.add(itemlist.get(i).substring(startconsindx+1,endconsindx));
				}else{
					int startconsindx=itemlist.get(i).indexOf(">");
					int endconsindx=itemlist.get(i).indexOf(")[",startconsindx);
					outputlist.add(itemlist.get(i).substring(startconsindx+1,endconsindx+1));
				}
			}
		}
		
		return outputlist;
		
	}
	
	/**
	 * Il metodo statico findPattern restituisce una tabella ordinata (tree map) contenente tutti i pattern e i
	 * relativi valori di supporto, all'interno della lista delle sotto stringhe ottenuta richiamando 
	 * il metodo getRowList sulla stringa result, in cui compare la stringa item.  
	 *  
	 * @param item stringa da ricercare all'interno dei pattern 
	 * @param result stringa dei risultati dell'operazione di data-mining dalla quale ricavare i pattern
	 * @return tree map contenente i pattern e i relativi valori di supporto in cui 
	 * compare la stringa item  
	 */
	static TreeMap<String,Float> findPattern(String item, String result) {
		TreeMap<String, Float> output = new TreeMap<String,Float>();
		List<String> itemlist= getRowList(result);
		for(int i=0; i<itemlist.size() ; i++){
			if(itemlist.get(i).contains(item) && !(itemlist.get(i).contains("==>"))){
				int startindx=itemlist.get(i).indexOf(" ");
				int endindx;
				if(itemlist.get(i).contains("in [")){
					int squareindx= itemlist.get(i).indexOf("[");
					squareindx=itemlist.get(i).indexOf("[",squareindx+1);
					endindx=itemlist.get(i).indexOf("[",squareindx+1);
				}else{
					endindx=itemlist.get(i).indexOf("[");	
				}
				String substr=itemlist.get(i).substring(startindx,endindx);
				startindx=endindx;
				endindx=itemlist.get(i).indexOf("]",startindx);
				String value= itemlist.get(i).substring(startindx+1,endindx);
				output.put(substr,Float.parseFloat(value));
			}
		}	
		
		return output;
	}
	
	/**
	 * Il metodo statico findAntecedentRule restituisce una tabella ordinata (tree map) contenente 
	 * tutte le regole di associazione e i relativi valori di supporto e di confidenza, che si trovano 
	 * all'interno della lista delle sotto stringhe ottenuta richiamando il metodo getRowList 
	 * sulla stringa result, in cui compare la stringa antecedent nell'antecedente. 
	 *
	 * @param antecedent stringa da ricercare all'inteno degli antecedenti delle regole 
	 * di associazione contenuti nella stringa result
	 * @param result stringa dei risultati dell'operazione di data-mining dalla quale ricavare i pattern
	 * @return tree map contenente i pattern e i relativi valori di supporto e di confidenza in cui 
	 * compare la stringa antecedent
	 */
	static TreeMap<String,LinkedList<Float>> findAntecedentRule(String antecedent, String result) {
		TreeMap<String,LinkedList <Float>> output = new TreeMap<String,LinkedList<Float>>();
		List<String> itemlist= getRowList(result);
		for(int i=0; i<itemlist.size() ; i++){
			if(itemlist.get(i).contains("==>")){
				String antstr =itemlist.get(i).substring(0, itemlist.get(i).indexOf("==>"));
				if(antstr.contains(antecedent)){
					int startindx=itemlist.get(i).indexOf(" ");
					int endindx; 
					if(!itemlist.get(i).contains("in [") && !itemlist.get(i).contains("in ]")){
						endindx=itemlist.get(i).indexOf("[");	
					}else{
						endindx=itemlist.get(i).indexOf(")[");
					}
					String substr = itemlist.get(i).substring(startindx+1,endindx);
					startindx=itemlist.get(i).indexOf(",", endindx);
					String supvalue= itemlist.get(i).substring(endindx+2,startindx);
					endindx=itemlist.get(i).indexOf("]");
					String confvalue = itemlist.get(i).substring(startindx+1,endindx);
					LinkedList<Float> listval = new LinkedList<Float>();
					listval.add(Float.parseFloat(supvalue));
					listval.add(Float.parseFloat(confvalue));
					output.put(substr,listval);
				}
			}
		}	
			
			return output;
	}
	
	/**
	 * 
	 * Il metodo statico findConsequentRule restituisce una tabella ordinata (tree map) contenente 
	 * tutte le regole di associazione e i relativi valori di supporto e di confidenza, che si trovano 
	 * all'interno della lista delle sotto stringhe ottenuta richiamando il metodo getRowList 
	 * sulla stringa result, in cui compare la stringa consequent nel conseguente. 
	 *
	 * @param consequent stringa da ricercare all'inteno dei conseguenti delle regole 
	 * di associazione contenuti nella stringa result
	 * @param result stringa dei risultati dell'operazione di data-mining dalla quale ricavare i pattern
	 * @return tree map contenente i pattern e i relativi valori di supporto e di confidenza in cui 
	 * compare la stringa consequent 
	 */
	static TreeMap<String,LinkedList<Float>> findConsequentRule (String consequent, String result){
		TreeMap<String,LinkedList<Float>> output = new TreeMap<String,LinkedList<Float>>();
		List<String> itemlist= getRowList(result);
		for(int i=0; i<itemlist.size() ; i++){
			if(itemlist.get(i).contains("==>")){
				String antstr =itemlist.get(i).substring( itemlist.get(i).indexOf("==>"),itemlist.get(i).indexOf("]"));
				if(antstr.contains(consequent)){
					int startindx=itemlist.get(i).indexOf(" ");
					int endindx; 
					if(itemlist.get(i).contains("in [")){
						endindx=itemlist.get(i).indexOf(")[");	
					}else{
						endindx=itemlist.get(i).indexOf("[");
					}
					String substr = itemlist.get(i).substring(startindx+1,endindx);
					startindx=itemlist.get(i).indexOf(",", endindx);
					String supvalue= itemlist.get(i).substring(endindx+2,startindx);
					endindx=itemlist.get(i).indexOf("]");
					String confvalue = itemlist.get(i).substring(startindx+1,endindx);
					LinkedList<Float> listval = new LinkedList<Float>();
					listval.add(Float.parseFloat(supvalue));
					listval.add(Float.parseFloat(confvalue));
					output.put(substr,listval);
				}
			}
		}	
			
			return output;
	}
	
	/**
	 * Il metodo statico getPatternAndRules restituisce una tabella ordinata (tree map) al cui interno vengono
	 * inseriti tutti i pattern con il relativo valore di supporto e tutte le regole di associazione con il 
	 * relativo valore di supporto e di confidenza.
	 * 
	 * @param result stringa dalla quale ricavare i pattern e le regole di associazione
	 * @return tree map contenente i pattern e le regole di associazione ricavate dalla stringa result
	 */
	static TreeMap<String,LinkedList<Float>> getPatternAndRules (String result){
		TreeMap<String,LinkedList<Float>> output = new TreeMap<String,LinkedList<Float>>();
		List<String> itemlist= getRowList(result);
		for(int i=0; i<itemlist.size() ; i++){
			LinkedList<Float> listval=new LinkedList<Float>();
			if(itemlist.get(i).contains("AND") || !(itemlist.get(i).contains("==>"))){
				int startindx =itemlist.get(i).indexOf(" ");
				int endindx;
				if(itemlist.get(i).contains("in [")){
					int squareindx= itemlist.get(i).indexOf("[");
					squareindx=itemlist.get(i).indexOf("[",squareindx+1);
					endindx=itemlist.get(i).indexOf("[",squareindx+1);
				}else{
					endindx=itemlist.get(i).indexOf("[");	
				}
				String str =itemlist.get(i).substring(startindx,endindx);
				startindx=endindx+1;
				endindx=itemlist.get(i).indexOf("]",startindx);
				listval.add(Float.parseFloat(itemlist.get(i).substring(startindx,endindx)));
				output.put(str, listval);
			}else{
				int startindx=itemlist.get(i).indexOf(" ");
				int endindx; 
				if(itemlist.get(i).contains("in [")){
					endindx=itemlist.get(i).indexOf(")[");	
				}else{
					endindx=itemlist.get(i).indexOf("[");
				}
				String substr = itemlist.get(i).substring(startindx+1,endindx);
				startindx=itemlist.get(i).indexOf(",", endindx);
				String supvalue= itemlist.get(i).substring(endindx+2,startindx);
				endindx=itemlist.get(i).indexOf("]");
				String confvalue = itemlist.get(i).substring(startindx+1,endindx);
				listval.add(Float.parseFloat(supvalue));
				listval.add(Float.parseFloat(confvalue));
				output.put(substr,listval);
			}	
		}
		return output;
	}
		
}
	

