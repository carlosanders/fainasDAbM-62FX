package mb.dabm.controller;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXTextField;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

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
    private JFXListView<File> listaViewFiles;
    @FXML
    private JFXButton btnSelectPath;
    @FXML
    private JFXTextField txtNamePath;

    private File dir;
    private static final Logger LOGGER = LogManager
	    .getLogger(FXMLAnchorPaneTratamentoArquivoController.class.getName());
    private ObservableList<File> observableListFile;
    private List<File> listaSplitFile;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
	carregarLista("Listando... ");
	titledPane.setExpanded(false); 
	
    }

    @FXML
    /**
     * 
     * @param event
     */
    public void btnAbrirDiretorio(ActionEvent event)
    {
	final DirectoryChooser directoryChooser = new DirectoryChooser();
	configuringDirectoryChooser(directoryChooser);

	Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

	dir = directoryChooser.showDialog(stage);

	if (dir != null) {
	    txtNamePath.setText("Diretório Selecionado: " + dir.getAbsolutePath());

	    carregarLista("Diretório contendo: ");
	}
    }

    @FXML
    public void handleClickFile(MouseEvent event)
    {

    }

    @FXML
    public void onProcessarLeitura(ActionEvent event)
    {

    }

    // metodos privados

    /**
     * carrega a lista dos arquivos dividitos
     */
    private void carregarLista(String nome)
    {

	if (dir != null) {
	    	    
	    titledPane.setExpanded(true);
	    
	    String[] extensions = new String[] { "txt", "csv" };
	    List<File> files = (List<File>) FileUtils.listFiles(dir, extensions, true);
	    
	    titledPane.setText(nome + files.size() + " arquivos do tipo: \".txt\", \".csv\"");

	    listaSplitFile = new ArrayList<>();

	    for (File file : files) {
		listaSplitFile.add(file);
	    }

	    observableListFile = FXCollections.observableArrayList(listaSplitFile);
	    listaViewFiles.setItems(observableListFile);

	    listaViewFiles.setCellFactory(new Callback<ListView<File>, ListCell<File>>() {

		@Override
		public ListCell<File> call(ListView<File> param)
		{
		    ListCell<File> cell = new ListCell<File>() {

			@Override
			protected void updateItem(File t, boolean bln)
			{
			    super.updateItem(t, bln);
			    if (t != null) {
				setText(t.getPath() + " - " + FileUtils.byteCountToDisplaySize(t.length()));
			    }
			}

		    };

		    return cell;
		}
	    });
	}
    }

    private void configuringDirectoryChooser(DirectoryChooser directoryChooser)
    {
	// Set title for DirectoryChooser
	directoryChooser.setTitle("Selecione o Diretório com os arquivos para tratar");

	// Set Initial Directory
	directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
    }

}
