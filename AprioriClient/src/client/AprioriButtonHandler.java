package client;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * La classe AprioriButtonHandler modella il gestore del bottone denominato Apriori, il quale
 * permette all'utente di effettuare le operazioni di salvataggio e caricamento di risultati di un'
 * operazione di data-mining considerando i parametri di input inseriti dall'utente sull'interfaccia grafica.
 */
public class AprioriButtonHandler implements EventHandler{

	/**
	 * Richiama il metodo AprioriExecute nel momento in cui l'utente clicca sul bottone runApriori.
	 *
	 *@param event evento associato dal bottone runApriori.
	*/
	public void handle(Event event) {
		try {
			AprioriExecute();
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Il metodo AprioriExecute legge i dati immessi dall'utente, li invia al server e ne visualizza la risposta
	 * nella text area. 
	 * Qualora i dati immessi non rispettino i parametri stabiliti verrà visualizzata
	 * un'opportuna finestra al cui interno è visualizzato l'errore commesso.
	 *
	 * @throws IOException sollevata nel caso di mancata connessione al server
	 * @throws ClassNotFoundException  sollevata se la classe dell'oggetto serializzato da leggere 
	 * 		   dallo stream non viene trovata
	 */
	private void AprioriExecute () throws IOException, ClassNotFoundException {
		Apriori.resultAreaTxt.setText("");
		ObjectInputStream in= null;
		ObjectOutputStream out= null;
		String nome_tab=null;
		Socket socket =null;
		if(Apriori.radioLearning.isSelected()){
			nome_tab=Apriori.textTab.getText();
			if(Apriori.textFile.getText().equals("")){
				
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Attenzione!");
				alert.setHeaderText("Nome del file non inserito");
				alert.setContentText("Inserire il nome del file in cui si desidera salavare");

				alert.showAndWait();
				return ;
			}
			if(nome_tab.equals("")){
			
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Attenzione!");
				alert.setHeaderText("Nome della tabella non inserito");
				alert.setContentText("Inserire il nome di una tabella");

				alert.showAndWait();
				return ;
			}
			float minSup=(float)0.0,minConf=(float)0.0;
			try{
				minSup= Float.parseFloat(Apriori.textSup.getText());
				}catch(NumberFormatException e){
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("Attenzione!");
					alert.setHeaderText("Valore minimo del supporto non valido");
					alert.setContentText("Inserire un valore decimale");

					alert.showAndWait();
					return;
				}
			try{
				minConf=Float.parseFloat(Apriori.textConf.getText());
				}catch(NumberFormatException e){
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("Attenzione!");
					alert.setHeaderText("Valore minimo della confidenza non valido");
					alert.setContentText("Inserire un valore decimale");

					alert.showAndWait();
					return;
				}
		if(minConf<=0 || minConf>=1){
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Attenzione!");
			alert.setHeaderText("Valore minimo della confidenza non valido");
			alert.setContentText("Inserire un valore compreso tra 0 e 1");

			alert.showAndWait();
			return;
			
		}
		if(minSup<=0 || minSup>=1){
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Attenzione!");
			alert.setHeaderText("Valore minimo del supporto non valido");
			alert.setContentText("Inserire un valore compreso tra 0 e 1");

			alert.showAndWait();
			return;
		}
		InetAddress addr = InetAddress.getByName("127.0.0.1"); 
		Apriori.resultAreaTxt.appendText("addr = " + addr+"\n");
		socket = new Socket(addr,8080);
		try{
			out = new ObjectOutputStream(socket.getOutputStream());
			out.flush();
			
			in=new  ObjectInputStream(socket.getInputStream());
			out.writeObject(1);
			out.writeObject(nome_tab);
			out.writeObject(minSup);
			out.writeObject(minConf);
			String output_stampa=(String) in.readObject();
			if(output_stampa.contains("Nome della tabella non esistente")){
				Apriori.resultAreaTxt.appendText(output_stampa+"\n");
				return;
			}
			Apriori.resultAreaTxt.appendText("Risultato del mining:"+"\n");
			Apriori.resultAreaTxt.appendText(output_stampa+"\n");
			
			Apriori.aprioriResult=output_stampa;
			Apriori.buttonChart.setDisable(false);
			out.writeObject(2);
			out.writeObject(Apriori.textFile.getText());
			Apriori.resultAreaTxt.appendText("Pattern e regole salvate CON SUCCESSO"+"\n");
			out.writeObject(4);
		}finally{
			socket.close();
			}
		}else{
			if(Apriori.textFile.getText().equals("")){
				
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Attenzione!");
				alert.setHeaderText("Nome del file non inserito");
				alert.setContentText("Inserire il nome del file in cui si desidera caricare");

				alert.showAndWait();
				return ;
			}
			try{
				InetAddress addr = InetAddress.getByName("127.0.0.1"); 
				Apriori.resultAreaTxt.appendText("addr = " + addr+"\n");
				socket = new Socket(addr,8080);
				out = new ObjectOutputStream(socket.getOutputStream());
				out.flush();
				in=new  ObjectInputStream(socket.getInputStream());
				out.writeObject(3);	
				out.writeObject(Apriori.textFile.getText());
				String output_stampa = null;
				output_stampa = (String)in.readObject();
				Apriori.resultAreaTxt.appendText("Caricando da file ..."+"\n");
				if(!output_stampa.contains("File non trovato")){
					Apriori.buttonChart.setDisable(false);
				} else Apriori.buttonChart.setDisable(true);
				Apriori.resultAreaTxt.appendText(output_stampa);
				Apriori.aprioriResult=output_stampa;
				out.writeObject(4);
			}finally{
				socket.close();
			}
		}		
	}

}
