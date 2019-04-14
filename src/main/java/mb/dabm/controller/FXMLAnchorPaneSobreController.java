package mb.dabm.controller;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class FXMLAnchorPaneSobreController implements Initializable
{
    @FXML
    private AnchorPane anchorPaneSobre;
    @FXML
    private Label labelDepto;
    @FXML
    private Label lblAutor;
    @FXML
    private Label lblVer;
    @FXML
    private Label labelSistema;
    @FXML
    private Label lblRamal;
    @FXML
    private Button buttonOk;

    private static final Logger LOGGER = LogManager.getLogger(FXMLAnchorPaneSobreController.class.getName());

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
	lerAppProperties();
    }

    @FXML
    public void btnClose(MouseEvent event)
    {
	//dialogStage.close();
	//FXMLVBoxMainController.stage.close();
    }

    /**
     * Metodo para ler arquivo properties
     */
    private void lerAppProperties()
    {
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
	// String ramal = props.getProperty("app.ramal");
	String depto = props.getProperty("app.depto");
	String sistema = props.getProperty("app.sistema");

	setVersao(versao + " - " + data);
	lblAutor.setText("Autor: " + autor);
	lblRamal.setText("Ramal: " + autor);
	labelSistema.setText(sistema);
	labelDepto.setText(depto);
    }

    private void setVersao(String ver)
    {
	lblVer.setText(ver);
    }

}
