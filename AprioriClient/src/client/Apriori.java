package client;
	
import javafx.scene.text.Font;


import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;

import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.FontWeight;
import javafx.scene.layout.*;

/**
 * La classe Apriori modella l'interfaccia grafica visualizzata a schermo e le sue componenti, 
 * con cui l'utente andrà ad interagire.
 */
public class Apriori extends Application {
	
	/**
	 * Radio button che consente all'utente di selezionare l'operazione di acquisizione dei dati sulla 
	 * tabella del database.
	 */
	static RadioButton radioLearning;
	
	/**
	 * Radio button che permette all'utente di selezionare l'operazione di lettura dei dati sulla tabella del database.
	 */
	static RadioButton radioReading;
	
	/**
	 * Text field che permette all'utente di inserire il nome della tabella da cui prendere i dati.
	 */
	static TextField textTab; 
	
	/**
	 * Text field che permette all'utente di inserire il valore di supporto dei pattern da ricavare.
	 */
	static TextField textSup;
	
	/**
	 * Text field che permette all'utente di inserire il valore di confidenza dei pattern da ricavare.
	 */
	static TextField textConf;
	
	/**
	 * Text area che visualizza i risultati del data-mining ed eventuali messaggi di servizio.
	 */
	static TextArea resultAreaTxt;
	
	/**
	 * Text field che permette all'utente di inserire il nome del file sul quale salvare o caricare 
	 * il risultato di un' operazione di data-mining.
	 */
	static TextField textFile;
	
	/** Stringa contenente i risulati dell'operazione di data-mining richiesta dall'utente. */
	static String aprioriResult;
	
	/** Bottone che permette all'utente di aprire una nuova finestra che mostrerà un istogramma dei risulati
	 * tenendo conto delle scelte dell'utente. */
	static Button buttonChart;
	
	/**
	 * Il metodo start posiziona e imposta le componenti grafiche dell'interfaccia, rendendola
	 * visibile all'utente affinché egli possa interagire.
	 * 
	 * @param primaryStage contenitore top-level di tutte le componenti grafiche dell'interfaccia
	 */
	@Override
	public void start(Stage primaryStage) {
		try {
			VBox root = new VBox();
			FlowPane parameterSetting = new FlowPane();
			GridPane aprioriMining= new GridPane();
			ToggleGroup radio = new ToggleGroup();
			radioLearning= new RadioButton("Learning rules from db");
			radioLearning.setTooltip(new Tooltip("Seleziona l'operazione di learning dei dati sulla tabella del database"));
			radioLearning.setSelected(true);
			radioLearning.setToggleGroup(radio);
			radioLearning.setOnMouseClicked(new AprioriRadioHandler());
			radioReading= new RadioButton("Reading rules from file");
			radioReading.setTooltip(new Tooltip("Seleziona l'operazione di lettura dei dati sulla tabella del database")); 
			radioReading.setToggleGroup(radio);
			radioReading.setOnMouseClicked(new AprioriRadioHandler());
			Label labsource= new Label("Selecting data source");
			labsource.setFont(Font.font(null, FontWeight.BOLD, 12));
			aprioriMining.add(labsource, 0, 0);
			aprioriMining.add(radioLearning, 0, 1);
			aprioriMining.add(radioReading, 0, 2);
			//APRIORI INPUT TOP
			FlowPane aprioriInput = new FlowPane();
			Label labtab= new Label("Nome tabella");
			textTab = new TextField();
			textTab.setPrefColumnCount(8);
			textTab.setTooltip(new Tooltip("Nome della tabella da cui prendere i dati."));
			Label labsup = new Label("min sup");
			textSup = new TextField();
			textSup.setPrefColumnCount(2);
			textSup.setTooltip(new Tooltip("Valore minimo del supporto dei pattern da ricavare"));
			Label labconf = new Label("min conf");
			textConf = new TextField();
			textConf.setPrefColumnCount(2);
			textConf.setTooltip(new Tooltip("Valore minimo della confidenza dei pattern da ricavare"));
			Label labfile = new Label("Nome file");
			textFile= new TextField();
			textFile.setPrefColumnCount(8);
		
			textFile.setTooltip(new Tooltip("Nome del file su cui salvare/da cui caricare i risultati del data-mining"));
			aprioriInput.setVgap(5);
			aprioriInput.getChildren().add(labfile);
			aprioriInput.getChildren().add(textFile);
			aprioriInput.getChildren().add(labtab);
			aprioriInput.getChildren().add(textTab);
			aprioriInput.getChildren().add(labsup);
			aprioriInput.getChildren().add(textSup);
			aprioriInput.getChildren().add(labconf);
			aprioriInput.getChildren().add(textConf);
			GridPane dataInput = new GridPane();
			Label labinput = new Label("Input parameters");
			labinput.setFont(Font.font(null, FontWeight.BOLD, 12));
			dataInput.add(labinput,0,0);
			dataInput.add(aprioriInput,0,1);
			dataInput.setVgap(5);
			//FINE
			FlowPane miningCommand= new FlowPane();
			miningCommand.setAlignment(Pos.CENTER);
			miningCommand.setPadding(new Insets(30,0,20,0));
			miningCommand.setHgap(5);
			Button runApriori = new Button("Esegui Apriori");
			runApriori.setTooltip(new Tooltip("Esegue l'algoritmo Apriori in base ai parametri inseriti e mostra i risultati."));
			runApriori.addEventHandler(MouseEvent.MOUSE_CLICKED, new AprioriButtonHandler());
			buttonChart = new Button("Mostra Grafico");
			buttonChart.setDisable(true);
			buttonChart.setTooltip(new Tooltip("Apre una nuova finestra dove visualizzare i risultati sotto forma di istogramma"));
			buttonChart.addEventHandler(MouseEvent.MOUSE_CLICKED, new BarChartHandler());
			miningCommand.getChildren().add(runApriori);
			miningCommand.getChildren().add(buttonChart);
			BorderPane ruleViewer = new BorderPane();
			Label labViewer = new Label("Patterns and Rules");
			labViewer.setFont(Font.font(null, FontWeight.BOLD, 12));
			resultAreaTxt = new TextArea();
			resultAreaTxt.setEditable(false);
			resultAreaTxt.setPrefSize(695, 250);
			resultAreaTxt.setMaxSize(695, 250);
			ruleViewer.setTop(labViewer);
			BorderPane.setAlignment(labViewer,Pos.TOP_LEFT);
			ruleViewer.setCenter(resultAreaTxt);
			ruleViewer.setPadding(new Insets(5,10,10,10));
			parameterSetting.setPadding(new Insets(20,40,30,40));
			parameterSetting.setHgap(60);
			aprioriMining.setVgap(5);
			aprioriInput.setHgap(10);
			parameterSetting.getChildren().add(aprioriMining);
			parameterSetting.getChildren().add(dataInput);
			root.getChildren().add(parameterSetting);
			root.getChildren().add(miningCommand);
			root.getChildren().add(ruleViewer);
			Scene scene = new Scene(root,700,400);
			//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.setTitle("Apriori");
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Il metodo main lancia l'applicazione che mostrerà l'interfaccia grafica.
	 *
	 * @param args parametro del main
	 */
	public static void main(String[] args) {
		launch(args);
	}
	
	/**
	 * L'inner class AprioriRadioHandler modella il gestore dei radio button radioLearning e radioReading.
	 *
	 */
	class AprioriRadioHandler implements EventHandler {
		
		/**
		 * Disabilita il bottone buttonChart.
		 */
		@Override
		public void handle(Event event) {
			buttonChart.setDisable(true);
		}
	}
	
}
