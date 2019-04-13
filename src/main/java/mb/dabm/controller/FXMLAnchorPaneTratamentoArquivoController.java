package mb.dabm.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class FXMLAnchorPaneTratamentoArquivoController implements Initializable
{
    @FXML
    private AnchorPane root;
    @FXML
    private AnchorPane anchorPaneHeader;
    @FXML
    private JFXButton btnVrfFile;
    @FXML
    private JFXProgressBar progress;
    @FXML
    private Label lblStatus;
    @FXML
    private JFXButton btnTask;
    @FXML
    private JFXCheckBox cbIgnoarLinha1;
    @FXML
    private Label lblLinesTerminated;
    @FXML
    private JFXTextField txtLinesTerminated;
    @FXML
    private Label lblDelimitador;
    @FXML
    private JFXTextField txtDelimitador;
    @FXML
    private Label lblEnclosed;
    @FXML
    private JFXTextField txtEnclosed;
    @FXML
    private Label lblEscaped;
    @FXML
    private JFXTextField txtEscaped;
    @FXML
    private AnchorPane anchorPaneResumo;
    @FXML
    private Label lblTotalSuccess;
    @FXML
    private JFXTextField txtTotalSuccess;
    @FXML
    private Label lblTotalErrors;
    @FXML
    private JFXTextField txtTotalErrors;
    @FXML
    private Label lblTempoProc;
    @FXML
    private JFXTextField txtTempoProc;
    @FXML
    private TitledPane titledPane;
    @FXML
    private AnchorPane anchorPaneListView;
    @FXML
    private JFXListView<?> listaViewFiles;
    @FXML
    private JFXButton btnSelectPath;
    @FXML
    private JFXTextField txtNamePath;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
	// TODO Auto-generated method stub

    }
    
    @FXML
    public void btnAbrirDiretorio(ActionEvent event) {

    }

    @FXML
    public void handleClickFile(MouseEvent event) {

    }

    @FXML
    public void onProcessarLeitura(ActionEvent event) {

    }

}
