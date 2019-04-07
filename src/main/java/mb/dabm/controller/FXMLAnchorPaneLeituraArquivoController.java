package mb.dabm.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXTextField;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import mb.dabm.helper.Helper;
import mb.dabm.helper.Timer;

public class FXMLAnchorPaneLeituraArquivoController implements Initializable
{

    @FXML // fx:id="root"
    private AnchorPane root; // Value injected by FXMLLoader

    @FXML // fx:id="btnSelectFile"
    private JFXButton btnSelectFile; // Value injected by FXMLLoader

    @FXML // fx:id="btnVrfFile"
    private JFXButton btnVrfFile; // Value injected by FXMLLoader

    @FXML // fx:id="txtNameFile"
    private JFXTextField txtNameFile; // Value injected by FXMLLoader

    @FXML // fx:id="progress"
    private JFXProgressBar progress; // Value injected by FXMLLoader

    @FXML // fx:id="lblStatus"
    private Label lblStatus; // Value injected by FXMLLoader

    @FXML // fx:id="btnTask"
    private JFXButton btnTask; // Value injected by FXMLLoader

    @FXML // fx:id="anchorPaneResumo"
    private AnchorPane anchorPaneResumo; // Value injected by FXMLLoader

    @FXML // fx:id="lblTotalLinhas"
    private Label lblTotalLinhas; // Value injected by FXMLLoader

    @FXML // fx:id="txtTotalLinhas"
    private JFXTextField txtTotalLinhas; // Value injected by FXMLLoader

    @FXML // fx:id="lblTempoProc"
    private Label lblTempoProc; // Value injected by FXMLLoader

    @FXML // fx:id="txtTempoProc"
    private JFXTextField txtTempoProc; // Value injected by FXMLLoader

    // variaveis uteis
    private File arq;
    private Task<Object> copyWorker;
    private BooleanProperty editMode = new SimpleBooleanProperty();
    private BooleanProperty modeTask = new SimpleBooleanProperty();
    // private Long totalLinhas;
    private Long totalLinhasArquivo;
    private Locale meuLocal = new Locale("pt", "BR");
    private NumberFormat nfVal = NumberFormat.getIntegerInstance(meuLocal);

    private static final Logger LOGGER = LogManager.getLogger(FXMLAnchorPaneLeituraArquivoController.class.getName());

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {

    }

    @FXML
    void btnAbrirClickFile(ActionEvent event)
    {
	Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	FileChooser fileChooser = new FileChooser();

	// Set extension filter
	FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TEXT files", "*.txt", "*.csv");
	fileChooser.getExtensionFilters().add(extFilter);

	// para testes pode comentar a linha abaixo (disable para teste - anders)
	configureFileChooser(fileChooser, "Selecione o Arquivo");

	// para testes pode descomentar as linhas abaixo, a fim de facilitar a abertura
	// do arquivo
	// fileChooser.setTitle("Seleicone o Arquivo");
	// File defaultDirectory = new File("./test");
	// fileChooser.setInitialDirectory(defaultDirectory);

	arq = fileChooser.showOpenDialog(stage);

	if (arq != null) {
	    txtNameFile.setText("Arquivo Selecionado: " + arq.getPath());
	    // txtNameFile.setText("Arquivo Selecionado: " + arq.getName());
	}
    }

    @FXML
    void onProcessarLeitura(ActionEvent event)
    {
	copyWorker = createWorkerVerificarErros(this.arq.getPath());

	progress.progressProperty().unbind();
	progress.progressProperty().bind(copyWorker.progressProperty());
	lblStatus.textProperty().bind(copyWorker.messageProperty());

	// atribui uma acao para o button, caso seja press botao parar.
	btnTask.setOnAction(e -> {
	    if (copyWorker.isRunning()) {
		// System.out.println("valor copyWorker - getState: " + copyWorker.getState());
		// //RUNNING ou CANCELLED
		// System.out.println("cancelou 3");
		editMode.set(true);
		modeTask.set(true);
		btnVrfFile.disableProperty().bind(editMode.not());
		btnSelectFile.disableProperty().bind(editMode.not());

		progress.progressProperty().unbind();
		progress.setProgress(0);
		copyWorker.cancel();
	    }

	});

	// caso ocorra alguma excecao na Task
	copyWorker.setOnFailed(evt -> {
	    // System.out.println("Task failed!");
	    if (copyWorker.getException() instanceof IndexOutOfBoundsException) {
		// System.out.println("...with an IndexOutOfBoundsException");
		LOGGER.error("Task failed! ...with an IndexOutOfBoundsException");
	    } else if (copyWorker.getException() instanceof NumberFormatException) {
		// System.out.println("...with a NumberFormatException");
		LOGGER.error("Task failed! ...with a NumberFormatException");
	    } else {
		// System.out.println("...with another, unexpected execption");
		LOGGER.error("Task failed! ...with another, unexpected execption");
	    }

	    Optional<ButtonType> result = Helper.enviarPergunta(AlertType.ERROR, "Ocorreu um Erro",
		    "Houve um erro na verificação do arquivo!",
		    "Mensagem: " + copyWorker.getException().getLocalizedMessage());

	    if (result.get() == ButtonType.OK) {

		editMode.set(true);
		modeTask.set(true);
		btnVrfFile.disableProperty().bind(editMode.not());
		btnSelectFile.disableProperty().bind(editMode.not());
		progress.progressProperty().unbind();
		progress.setProgress(0);
	    }

	    // System.out.println(copyWorker.getException().getLocalizedMessage());
	    // System.out.println(copyWorker.getException().getCause());
	    // System.out.println(copyWorker.getException().getStackTrace());

	});

	// caso ocorra a leitura com sucesso
	copyWorker.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
	    @Override
	    public void handle(WorkerStateEvent t)
	    {
		progress.progressProperty().unbind();
		progress.setProgress(1);
	    }
	});

	new Thread(copyWorker).start();
    }

    /**
     * Processo para fazer a varredura noa arquivo
     * 
     * @param fileName
     * @return
     */
    private Task<Object> createWorkerVerificarErros(String fileName)
    {
	return new Task<Object>() {
	    @Override
	    protected Object call() throws Exception
	    {

		Long nro = 0L;
		Long nroLinha = 0L;
		Timer timer = new Timer();
		String current = null;

		try (BufferedReader br = new BufferedReader(
			new InputStreamReader(new FileInputStream(fileName)))) {

		    // frErros = new FileWriter(fileErros);
		    // bufferErros = new BufferedWriter(frErros);

		    modeTask.set(false);
		    btnVrfFile.disableProperty().bind(editMode.not());
		    btnSelectFile.disableProperty().bind(editMode.not());
		    while (true) {

			current = br.readLine();
			if (current == null) {
			    editMode.set(true);
			    modeTask.set(true);
			    btnVrfFile.disableProperty().bind(editMode.not());
			    btnSelectFile.disableProperty().bind(editMode.not());
			    // System.out.println("terminou 1");
			    break;
			}

			// para cancelar a execucao
			if (isCancelled()) {
			    progress.progressProperty().unbind();
			    progress.setProgress(0);
			    // System.out.println("cancelou 1");
			    break;
			}
			// updateProgress((nro + 1), -1);
			// updateMessage("Task part " + nfVal.format(nro + 1) + " complete");
			// Thread.sleep(10); // 28/11/17

			updateProgress((nro + 1), -1);
			// updateMessage("Task part " + String.valueOf(nro + 1) + " complete");
			updateMessage("Número de Linha(s): " + nfVal.format(nroLinha + 1));
			// Thread.sleep(10); // 28/11/17

			// System.out.println(current);
			// System.out.println("linha:" + nfVal.format(nroLinha));
			nroLinha++;

		    }

		    updateMessage("Processo finalizado: " + nfVal.format(nroLinha) + " Linhas lidas!");

		    // System.out.println("-----Metodo: verificarErros----");

		    // System.out.println("Linhas Lida do Arquivo Principal: " + nroLinha);
		    txtTotalLinhas.setText(nfVal.format(nroLinha));
		    txtTotalLinhas.setStyle("-fx-text-fill: red; -fx-font-size: 13;");

		    totalLinhasArquivo = nroLinha;

		    // System.out.println("Tempo de Processamento: " + timer);
		    txtTempoProc.setText(timer.toString());
		    txtTempoProc.setStyle("-fx-text-fill: red; -fx-font-size: 13;");

		} catch (IOException e) {
		    // e.printStackTrace();
		    LOGGER.error("[IOException] " + e.getMessage());
		    throw e;

		} catch (Exception ex) {
		    // ex.printStackTrace();
		    LOGGER.error("[Exception] " + ex.getMessage());

		    throw ex;
		} finally {

		    updateMessage("Leitura do Aquivo finalizada.");

		}

		return true;
	    }

	};
    }

    /**
     * nro 2 Responsavel para setar um titulo e um diretorio inicial para localizar
     * o arquivo
     * 
     * @param fileChooser
     * @param nameTitle
     */
    private static void configureFileChooser(final FileChooser fileChooser, String nameTitle)
    {
	fileChooser.setTitle(nameTitle);
	fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
    }

}
