package client;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeMap;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;


/**
 * La classe BarChartHandler modella il gestore del bottone denominato buttonChart il quale andrà a popolare 
 * l'istogramma all'interno della finestra. Tale istogramma mostra il risultato della ricerca di item o pattern 
 * selezionati dall'utente all'interno della stringa 
 */
public class BarChartHandler implements EventHandler {
	
	/** La choice box cbItem permette di scegliere l'item che l'utente intende ricercare all'interno dei pattern
	 *  ottenuti dall'operazione di data-mining. */
	private ChoiceBox<String> cbItem;
 	
	 /** La choice box cbAntecedent permette di scegliere il pattern che l'utente intende ricercare all'interno
	  * dell'antecedente di ogni regola di associazione ottenuta dall'operazione di data-mining. */
	 private ChoiceBox<String> cbAntecedent;
	
	/** La choice box cbConsequent permette di scegliere il pattern che l'utente intende ricercare all'interno
	 * del conseguente di ogni regola di associazione ottenuta dall'operazione di data-mining. */
	private ChoiceBox<String> cbConsequent;
	
	/**
	 * Il radio button radioPatternRules permette di mostrare sull'istogramma tutti i risultati dell'operazione
	 * di data-mining. 
	 */
	private RadioButton radioPatternRules;
	
	/** Il radio button radioPattern permette all'utente di effettuare la selezione per la ricerca
	 * di un item all'interno dei pattern contenuti nei risultati dell'operazione di data-mining. */
	private RadioButton radioPattern;
	
	/**Il radio button radioRules permette all'utente di effettuare la selezione per la ricerca 
	 * di un pattern all'interno dell'antecedente o del conseguente di ogni regola di associazione
	 * ottenuta dall'operazione di data-mining. */
	private RadioButton radioRules;
	
	/** Il bar chart resultChart rappresenta l'istogramma in cui verranno visualizzati i risultati della
	 * ricerca degli item o dei pattern scelti dall'utente che sono contenuti 
	 * nei pattern e regole ottenuti dall'operazione di data-mining. */
	private BarChart<String, Number> resultChart;
	
	/**
	 * Apre una nuova finestra nella quale viene visualizzato l'istogramma insieme alle componenti grafiche che
	 * rappresentano le possibili opzioni di scelta e di selezione dei risultati che l'istogramma mostrerà.
	 */
	@Override
	public void handle(Event event) {
		 
		  VBox root = new VBox(); 
		  GridPane inputSelect = new GridPane();
		  Label labTitle= new Label("Scegli parametri ricerca");
		  labTitle.setFont(Font.font(null, FontWeight.BOLD, 12));
		  ToggleGroup tgradio = new ToggleGroup();
		  radioPatternRules=new RadioButton("Pattern and Rules");
		  radioPatternRules.setOnMouseClicked(new RadioPatternRulesHandler());
		  radioPatternRules.setSelected(true);
		  radioPatternRules.setToggleGroup(tgradio);
		  radioPatternRules.setTooltip(new Tooltip("Scegli di mostrare nel grafico tutti i pattern "
			  		+ "e le regole di associazione trovati"));
		  radioPattern= new RadioButton("Frequent Pattern");
		  radioPattern.setOnMouseClicked(new RadioPatternHandler());
		  radioPattern.setToggleGroup(tgradio);
		  radioPattern.setTooltip(new Tooltip("Scegli di mostrare nel grafico i pattern "
		  		+ "che contengono l'item selezionato"));
		  radioRules= new RadioButton("Association Rules");
		  radioRules.setOnMouseClicked(new RadioRulesHandler());
		  radioRules.setToggleGroup(tgradio);
		  radioRules.setTooltip(new Tooltip("Scegli di mostrare nel grafico le regole "
		  		+ "che contengono l'antecedente o il conseguente selezionato "));
		  HBox inputParameters = new HBox();
		  Label labItem= new Label("Item");
		  cbItem= new ChoiceBox<String>();
		  cbItem.setPrefWidth(100);
		  cbItem.setDisable(true);
		  cbItem.setTooltip(new Tooltip("Seleziona l'item da ricercare nei pattern dei risultati"));
		  Set<String> templist= ResultSearcher.getItemSet(Apriori.aprioriResult);
		  Iterator<String> itSet = templist.iterator();
 		  while (itSet.hasNext()){
			  cbItem.getItems().add(itSet.next());
		  }
		  
		  Label labAntecedent = new Label("Antecedente");
		  cbAntecedent = new ChoiceBox<String>();
		  cbAntecedent.setPrefWidth(100);
		  cbAntecedent.setDisable(true);
		  cbAntecedent.setTooltip(new Tooltip("Seleziona l'antecedente da ricercare nelle regole dei risultati"));
		  
		  Set<String> antecedentSet= ResultSearcher.getAntecedentSet(Apriori.aprioriResult);
		  Iterator<String> itAntSet =antecedentSet.iterator();
 		  while (itAntSet.hasNext()){
			  cbAntecedent.getItems().add(itAntSet.next());
		  }
 		
 		 
		 
 		 
		  Label labConsequent = new Label("Conseguente");
		  cbConsequent = new ChoiceBox<String>();
		  cbConsequent.setDisable(true);
		  cbConsequent.setPrefWidth(100);
		  cbConsequent.setTooltip(new Tooltip("Seleziona il conseguente da ricercare nelle regole dei risultati") );
		 
		  Set<String> consequentSet= ResultSearcher.getConsequentSet(Apriori.aprioriResult);
		  Iterator<String> itConsSet = consequentSet.iterator();
 		  while (itConsSet.hasNext()){
			  cbConsequent.getItems().add(itConsSet.next());
		  }
 		  inputParameters.setSpacing(5);
		  inputParameters.getChildren().add(labItem);
		  inputParameters.getChildren().add(cbItem);
		  inputParameters.getChildren().add(labAntecedent);
		  inputParameters.getChildren().add(cbAntecedent);
		  inputParameters.getChildren().add(labConsequent);
		  inputParameters.getChildren().add(cbConsequent);
	
		  inputParameters.setAlignment(Pos.CENTER);
		  inputSelect.add(labTitle, 0, 0);
		  inputSelect.add(radioPatternRules, 0, 1);
		  inputSelect.add(radioPattern, 0, 2);
		  inputSelect.add(radioRules, 0, 3);
		  Button buttonChart= new Button("Mostra");
		  buttonChart.setTooltip(new Tooltip("Visulizza i risultati nel grafico sulla base"
		  		+ " delle scelte effettuate"));
		  buttonChart.addEventHandler(MouseEvent.MOUSE_CLICKED, new ShowButtonHandler());
		  inputParameters.getChildren().add(buttonChart);
		  HBox.setMargin(buttonChart,new Insets (20));
		  HBox hbInput = new HBox(); 
		  hbInput.setSpacing(20);
		  hbInput.getChildren().add(inputSelect);
		  hbInput.getChildren().add(inputParameters);
		  CategoryAxis xAxis = new CategoryAxis();
		  NumberAxis yAxis =new NumberAxis();
		  resultChart =new BarChart<String,Number>(xAxis,yAxis);
		  resultChart.setPrefSize(1200, 525);
		  resultChart.setBarGap(5);
		  resultChart.setCategoryGap(15);
		  resultChart.setLegendSide(Side.TOP);
		  xAxis.setLabel("Pattern and Rules");
		  yAxis.setLabel("Value"); 
		  resultChart.setAnimated(false);
		  
		  ScrollPane scrollPane = new ScrollPane();
		  scrollPane.setVbarPolicy(ScrollBarPolicy.ALWAYS);
		  scrollPane.setHbarPolicy(ScrollBarPolicy.ALWAYS);
		  scrollPane.setContent(resultChart);
		  VBox.setVgrow(scrollPane, Priority.ALWAYS);
		  scrollPane.setMaxWidth(Double.MAX_VALUE);
		  root.getChildren().add(hbInput);
		  root.getChildren().add(scrollPane);
		  root.setFillWidth(true);
		  Stage stage = new Stage();
	      stage.setTitle("Mostra Grafico");
	      //stage.setResizable(false);
	      stage.setScene(new Scene(root, 1100, 600));
	      stage.show();
	}
	
	/**
	 * L'inner class RadioPatterRulesHandler modella il gestore del radio button radio button radioPatternRules,
	 * invocato nel momento in cui esso viene selezionato con un click del mouse.
	 */
	class RadioPatternRulesHandler implements EventHandler {
		
		/**
		 *  Disabilita le tre choice box, impededendone la selezione.
		 *  
		 *  @param event evento rilevato dal bottone radioPatternRules
		 */
		@Override
		public void handle(Event event) {
			cbItem.getSelectionModel().clearSelection();
			cbItem.setDisable(true);
			cbAntecedent.getSelectionModel().clearSelection();
			cbAntecedent.setDisable(true);
			cbConsequent.getSelectionModel().clearSelection();
			cbConsequent.setDisable(true);
		}
	}
	/**
	 * L'inner class RadioPatternHandler modella il gestore del radio button denominato radioPattern, invocato nel 
	 * momento in cui esso viene selezionato con un click del mouse.
	 */
	class RadioPatternHandler implements EventHandler{

		/**
		 * Rende possibile all'utente l'interazione con la sola choice box cbItem 
		 * per la scelta degli item da ricercare nel risultato dell'operazione di data-mining.
		 * 
		 * @param event evento rilevato dal radio button radioPattern
		 */
		@Override
		public void handle(Event event) {
			cbItem.setDisable(false);
			cbAntecedent.setDisable(true);
			cbAntecedent.getSelectionModel().clearSelection();
			cbConsequent.setDisable(true);
			cbConsequent.getSelectionModel().clearSelection();
			
	 		 
		}
		
	}
	
	/**
	 * L'inner class  RadioRulesHandler modella il gestore del radio button denominato radioRules,invocato nel 
	 * momento in cui esso viene selezionato con un click del mouse.
	 */
	class RadioRulesHandler implements EventHandler {

		/**
		 * Rende possibile all'utente l'interazione con le choice box cbAntecedent o cbConsequent
		 * per la scelta rispettivamente degli antecedenti o dei conseguenti da ricercare nel risultato
		 * dell'operazione di data-mining.
		 * 
		 * @param event evento rilevato dal radio button radioRules
		 */
		@Override
		public void handle(Event event) {
			cbItem.setDisable(true);
			cbItem.getSelectionModel().clearSelection();
			cbAntecedent.setDisable(false);
			cbConsequent.setDisable(false);	
			
		}
		
	}
	
	/**
	 * L'inner class ShowButtonHandler modella il gestore del bottone buttonChart, invocato nel momento in cui
	 * il bottone viene premuto.
	 */
	class ShowButtonHandler implements EventHandler{

		
		/**
		 * Mostra sull'istogramma i risultati della ricerca dei pattern o delle regole di associazione contenenti
		 * l'item, l'antecedente o il conseguente scelti dall'utente.
		 * 
		 * @param event evento rilevato dal bottone showChart
		 */
		@Override
		public void handle(Event event) {
			 	resultChart.setPrefSize(1200, 500);
				resultChart.setBarGap(5);
				resultChart.setCategoryGap(15);
				resultChart.getData().clear();
				TreeMap<String,LinkedList<Float>> tabPatternRules=null;
				TreeMap<String,Float> tabResult=null;
				String strConsequent = null;
				XYChart.Series seriesSup = new XYChart.Series();

				XYChart.Series seriesConf = new XYChart.Series();
				if(radioPattern.isSelected()){
					if( cbItem.getSelectionModel().getSelectedItem()==null){
						Alert alert = new Alert(AlertType.WARNING);
						alert.setTitle("Attenzione!");
						alert.setHeaderText("Impossibile mostare il grafico senza aver selezionato un item");
						alert.setContentText("Inserire l'item da ricercare nei pattern");
						alert.showAndWait();
						return;
					}
					String strToSearch= cbItem.getSelectionModel().getSelectedItem();
					tabResult= ResultSearcher.findPattern(strToSearch, Apriori.aprioriResult);
					resultChart.setTitle(cbItem.getSelectionModel().getSelectedItem());
					seriesSup.setName("support");
					Iterator<String> itPattern= tabResult.keySet().iterator();
					Iterator<Float> itSupport=tabResult.values().iterator();
					while(itPattern.hasNext()){
						seriesSup.getData().add(new XYChart.Data(itPattern.next(),itSupport.next()));
					}
					if(seriesSup.getData().size()<2){
						resultChart.setBarGap(50);
						resultChart.setCategoryGap(300);
						resultChart.setPrefWidth(900);
					}
					resultChart.getData().addAll(seriesSup);
					
					return;
				}else if (radioRules.isSelected()){
					if(cbAntecedent.getSelectionModel().getSelectedItem()==null && cbConsequent.getSelectionModel().getSelectedItem()==null){
						Alert alert = new Alert(AlertType.WARNING);
						alert.setTitle("Attenzione!");
						alert.setHeaderText("Impossibile mostrare il grafico senza aver selezionato un antecedente"
								+ "o un conseguente");
						alert.setContentText("Inserire un antecedente o un conseguente da ricercare.");

						alert.showAndWait();
						
						return;
					}
					tabPatternRules= new TreeMap<String,LinkedList<Float>>();
					String strAntecedent=cbAntecedent.getSelectionModel().getSelectedItem();
					strConsequent=cbConsequent.getSelectionModel().getSelectedItem();
					if(strAntecedent!=null && strConsequent==null){
						tabPatternRules= ResultSearcher.findAntecedentRule(strAntecedent, Apriori.aprioriResult);
						resultChart.setTitle(cbAntecedent.getSelectionModel().getSelectedItem());
						seriesSup.setName("support");
						seriesConf.setName("confidance");
						cbAntecedent.getSelectionModel().clearSelection();
						
					}
					if(strAntecedent==null && strConsequent!=null){
						tabPatternRules= ResultSearcher.findConsequentRule(strConsequent, Apriori.aprioriResult);
						resultChart.setTitle(cbConsequent.getSelectionModel().getSelectedItem());
						seriesSup.setName("support");
						seriesConf.setName("confidance");
						cbConsequent.getSelectionModel().clearSelection();
					}
					if(strConsequent!=null && strAntecedent!=null){
						Alert alert = new Alert(AlertType.WARNING);
						alert.setTitle("Attenzione!");
						alert.setHeaderText("Impossibile mostrare il grafico avendo selezionato sia un antecedente"
								+ "che un conseguente");
						alert.setContentText("Inserire uno solo tra antecedente e conseguente da ricercare.");

						alert.showAndWait();
						
						return;
					}
				}else if(radioPatternRules.isSelected()){
					tabPatternRules= ResultSearcher.getPatternAndRules(Apriori.aprioriResult);
					resultChart.setTitle("All results");
					seriesSup.setName("support");
					seriesConf.setName("confidance");
				}
					
			
				
				
				Iterator<String> itRule=tabPatternRules.keySet().iterator();
				Iterator<LinkedList<Float>> itlist =tabPatternRules.values().iterator();
				while(itRule.hasNext()){
					String str =itRule.next();
					LinkedList<Float> valTabList= itlist.next();
					Iterator<Float> itval= valTabList.iterator();
					while(itval.hasNext()){
						float testSup = itval.next();
						seriesSup.getData().add(new XYChart.Data(str,testSup));
						if(itval.hasNext()){
							float testCon = itval.next();
							seriesConf.getData().add(new XYChart.Data(str,testCon));
						}
						
					}
				}
				if(seriesSup.getData().size()<2 && seriesConf.getData().size()<2){
					resultChart.setBarGap(50);
					resultChart.setCategoryGap(300);
					resultChart.setPrefWidth(900);
				}
				if(seriesSup.getData().size()>20){
					resultChart.setScaleY(0.8);
					resultChart.setBarGap(3);
					resultChart.setCategoryGap(5);
					resultChart.setPrefHeight(700);
					resultChart.setPrefWidth(45*seriesSup.getData().size());
					resultChart.getXAxis().tickLabelFontProperty().set(Font.font(null,FontWeight.SEMI_BOLD,14));
				}
		
				//resultChart.getYAxis().setAutoRanging(true);
				resultChart.getData().addAll(seriesSup,seriesConf);
			}
	}	
}
