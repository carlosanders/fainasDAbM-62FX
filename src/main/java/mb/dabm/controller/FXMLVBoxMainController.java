package mb.dabm.controller;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;

public class FXMLVBoxMainController implements Initializable
{
    @FXML
    private MenuItem menuItemLeituraArquivo;
    @FXML
    private MenuItem menuItemTratamentoArquivo;
    @FXML
    private MenuItem menuItemGerarArquivo;
    @FXML
    private MenuItem menuItemAbout;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Label lblVer;
    @FXML
    private Label lblAutor;    
    @FXML
    private Label lblTotal;
    private static final Logger LOGGER = LogManager.getLogger(FXMLVBoxMainController.class.getName());


    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
	lerAppProperties();
    }

    // Event Listener on MenuItem[#menuItemLeituraArquivo].onAction
    @FXML
    public void handleMenuItemLeituraArquivo(ActionEvent event) throws IOException
    {

	//forma para passar valores entre controllers
	FXMLLoader loader = new FXMLLoader();
	loader.setLocation(getClass()
		.getResource("/view/FXMLAnchorPaneLeituraArquivo.fxml"));
	Parent aPane = loader.load();
	
	//exemplo
	//FXMLAnchorPaneLeituraArquivoController c = loader.getController();
	//c.setTotalLinhas("2500");
	

	anchorPane.getChildren().setAll(aPane);
    }

    // Event Listener on MenuItem[#menuItemTratamentoArquivo].onAction
    @FXML
    public void handleMenuItemTratamentoArquivo(ActionEvent event) throws IOException
    {
	AnchorPane a = (AnchorPane) FXMLLoader.load(getClass().getResource("/view/FXMLAnchorPaneTratamentoArquivo.fxml"));
	anchorPane.getChildren().setAll(a);
    }

    // Event Listener on MenuItem[#menuItemGerarArquivo].onAction
    @FXML
    public void handleMenuItemGerarArquivo(ActionEvent event) throws IOException
    {
	AnchorPane a = (AnchorPane) FXMLLoader.load(getClass().getResource("/view/FXMLAnchorPaneLeituraArquivo.fxml"));
	anchorPane.getChildren().setAll(a);
    }

    // Event Listener on MenuItem[#menuItemAbout].onAction
    @FXML
    public void handleMenuItemAbout(ActionEvent event)
    {
	// TODO Anders - Implementar acao About
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
	String divisao = props.getProperty("app.divisao");

	setVersao(versao + " - " + data);
	lblAutor.setText("Autor: " + autor + " - " + divisao);
    }

    private void setVersao(String ver)
    {
	lblVer.setText(ver);
    }

}
