package mb.dabm.controller;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import mb.dabm.application.App;

public class FXMLVBoxMainController implements Initializable {
	@FXML
	private MenuItem menuItemSprint;
	@FXML
	private MenuItem menuItemHome;
	@FXML
	private MenuItem menuItemAbout;
	@FXML
	private AnchorPane anchorPane;
	@FXML
	private Label lblVer;
	@FXML
	private Label lblAutor;

	// public static Stage stage = null;

	// uteis
	private static final Logger LOGGER = LogManager.getLogger(FXMLVBoxMainController.class.getName());

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		lerAppProperties();
	}

	// Event Listener on MenuItem[#menuItemLeituraArquivo].onAction
	@FXML
	public void handleMenuItemSprint(ActionEvent event) throws IOException {

		// forma para passar valores entre controllers
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/FXMLAnchorSprint.fxml"));
		Parent aPane = loader.load();

		// exemplo
		// FXMLAnchorPaneLeituraArquivoController c = loader.getController();
		// c.setTotalLinhas("2500");

		anchorPane.getChildren().setAll(aPane);
	}

	// Event Listener on MenuItem[#menuItemGerarArquivo].onAction
	@FXML
	public void handleMenuItemHome(ActionEvent event) throws IOException {
		App.stage.close();
		Platform.runLater(() -> new App().start(new Stage()));
	}

	// Event Listener on MenuItem[#menuItemAbout].onAction
	@FXML
	public void handleMenuItemAbout(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(FXMLAnchorPaneSobreController.class.getResource("/view/FXMLAnchorPaneSobre.fxml"));
		AnchorPane page = (AnchorPane) loader.load();

		// criando um estagio de dialog (Stage Dialog)
		Stage dialogStage = new Stage();
		dialogStage.setTitle("Sobre");
		Scene scene = new Scene(page);
		dialogStage.setScene(scene);
		dialogStage.setResizable(false);
		// dialogStage.initStyle(StageStyle.UNDECORATED);

		// setando o cliente no controller da tela
		// FXMLAnchorPaneCadastrosClientesDialog.fxml
		// FXMLAnchorPaneSobreController controller = loader.getController();
		// controller.setDialogStage(dialogStage);
		// FXMLVBoxMainController.stage = dialogStage;

		// Mostra o dialog e espera ate que o usu o feche
		dialogStage.showAndWait();
	}

	/**
	 * Metodo para ler arquivo properties
	 */
	private void lerAppProperties() {
		Properties props = new Properties();
		InputStream is = getClass().getResourceAsStream("/application.properties");
		try {
			props.load(is);
			is.close();
		} catch (IOException e) {
			LOGGER.error("IOException! " + e.getMessage());
		}

		String versao = props.getProperty("app.versao");
		String data = props.getProperty("app.data");
		String autor = props.getProperty("app.autor");
		String divisao = props.getProperty("app.divisao");

		setVersao(versao + " - " + data);
		lblAutor.setText("Autor: " + autor + " - " + divisao);
	}

	private void setVersao(String ver) {
		lblVer.setText(ver);
	}

}
