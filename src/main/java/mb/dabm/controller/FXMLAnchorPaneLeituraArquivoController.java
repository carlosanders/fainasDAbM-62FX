package mb.dabm.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXTextField;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import mb.dabm.helper.Helper;
import mb.dabm.helper.Timer;

public class FXMLAnchorPaneLeituraArquivoController implements Initializable
{

    @FXML
    private AnchorPane root;
    @FXML
    private JFXButton btnSelectFile;
    @FXML
    private JFXButton btnVrfFile;
    @FXML
    private JFXTextField txtNameFile;
    @FXML
    private JFXProgressBar progress;
    @FXML
    private Label lblStatus;
    @FXML
    private JFXButton btnTask;
    @FXML
    private AnchorPane anchorPaneResumo;
    @FXML
    private Label lblTotalLinhas;
    @FXML
    private JFXTextField txtTotalLinhas;
    @FXML
    private Label lblTempoProc;
    @FXML
    private JFXTextField txtTempoProc;
    @FXML
    private AnchorPane anchorPaneSplitFile;
    @FXML
    private JFXListView<File> listaViewFilesSplit;
    @FXML
    private Label lblLinhasPorArquivo;
    @FXML
    private JFXButton btnSplitFile;
    @FXML
    private JFXProgressBar progressParte;
    @FXML
    private Label lblStatusParte;
    @FXML
    private TitledPane titledPane;
    @FXML
    private TextField txtLinhasPorArquivo;

    // variaveis uteis
    private File arq;
    private Task<Object> copyWorker;
    private BooleanProperty editMode = new SimpleBooleanProperty();
    private BooleanProperty modeTask = new SimpleBooleanProperty();
    private Long totalLinhas;
    private Task<Object> splitWorker;
    private ObservableList<File> observableListFile;
    private List<File> listaSplitFile;
    private BooleanProperty dividirFile = new SimpleBooleanProperty();
    private Long qtdLinhasPorArquivo;
    private Locale meuLocal = new Locale("pt", "BR");
    private NumberFormat nfVal = NumberFormat.getIntegerInstance(meuLocal);

    private static final Logger LOGGER = LogManager.getLogger(FXMLAnchorPaneLeituraArquivoController.class.getName());

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
	dividirFile.set(false);
	modeTask.set(true);
	// ao iniciar a var inicia como false e para disable o btn inverte seu valor
	btnTask.disableProperty().bind(modeTask);
	// so habiliata o btn se o campo textServico for =! de null ou txtLogin =! null
	btnVrfFile.disableProperty().bind(txtNameFile.textProperty().isEmpty());

	progress.setProgress(0);
	btnSplitFile.setDisable(true);

	progressParte.setProgress(0);
	progressParte.setVisible(false);
	lblStatusParte.setVisible(false);

	carregarLista();
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

		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)))) {

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
			    btnSplitFile.setDisable(false);
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

		    // totalLinhasArquivo = nroLinha;
		    totalLinhas = nroLinha;

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

    @FXML
    public void handleClickFile(MouseEvent event)
    {
	File file = listaViewFilesSplit.getSelectionModel().getSelectedItem();

	if (file != null) {
	    Optional<ButtonType> result = Helper.enviarPergunta(AlertType.CONFIRMATION, "Abrir Arquivo",
		    "Selecionado o arquivo: " + file.getName(), "Deseja abrir?");

	    if (result.get() == ButtonType.OK) {
		Helper.openFile(file);
	    }
	}

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

    /**
     * Para dividir o arquivo com base na quantidade de linhas passada
     * 
     * @param event
     */
    public void handlerBtnSplitFile(ActionEvent event)
    {

	if (txtLinhasPorArquivo.getText() == null || txtLinhasPorArquivo.getText().isEmpty()
		|| txtLinhasPorArquivo.getText().equals("")) {

	    Helper.enviarPergunta(AlertType.WARNING, "Atenção!", "Campo inválido, por favor, corrija...",
		    "Mensagem: O Campo Total de Linhas por arquivo deve ser preenchido.");
	} else {

	    qtdLinhasPorArquivo = Long.parseLong(txtLinhasPorArquivo.getText());

	    dividirFile.set(true);
	    // splitWorker
	    splitWorker = createWorkerSplitFile(this.arq.getPath());

	    progress.setProgress(0);
	    progress.progressProperty().unbind();
	    progress.progressProperty().bind(splitWorker.progressProperty());
	    lblStatus.textProperty().bind(splitWorker.messageProperty());
	    //@todo 10-04-19 anders
	    //lblStatusParte.textProperty().bind(valorFileSplit);

	    // atribui uma acao para o button, caso seja press botao parar.
	    btnTask.setOnAction(e -> {
		if (splitWorker.isRunning()) {
		    // System.out.println("valor copyWorker - getState: " + copyWorker.getState());
		    // //RUNNING ou CANCELLED
		    // System.out.println("cancelou 3");
		    editMode.set(true);
		    modeTask.set(true);
		    btnVrfFile.disableProperty().bind(editMode.not());
		    btnSelectFile.disableProperty().bind(editMode.not());

		    progress.progressProperty().unbind();
		    progress.setProgress(0);
		    splitWorker.cancel();
		}

	    });

	    // caso ocorra alguma excecao na Task
	    splitWorker.setOnFailed(evt -> {
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
	    });

	    // caso ocorra a leitura com sucesso
	    splitWorker.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
		@Override
		public void handle(WorkerStateEvent t)
		{

		    progress.progressProperty().unbind();
		    progress.setProgress(1);
		    
		    //int totalItens = listaViewFilesSplit.getItems().size();
		    //"Processo finalizado: " + nfVal.format(nroLinha) + " Linhas lidas!"
		    lblStatusParte.setText("Subprocesso finalizado.");
		    
		    btnSplitFile.setDisable(false);

		    Helper.enviarAlerta(AlertType.INFORMATION, "Sucesso", "Arquivo analisado com sucesso!",
			    "Total de itens analisados no arquivo: " + nfVal.format(totalLinhas));

		    // System.out.println("processo 2 finalizado");		    
		    observableListFile.clear();
		    listaViewFilesSplit.getItems().clear();

		    // String dir = System.getProperty("user.home") + File.separator + "carga_seg_v"
		    // + File.separator
		    // + "partes";
		    carregarLista();
		    
		    // carregarLista();// carrega a lista com os novo arquivos
		    editMode.set(true);
		    modeTask.set(true);
		    btnVrfFile.disableProperty().bind(editMode.not());
		    btnSelectFile.disableProperty().bind(editMode.not());
		}
	    });

	    new Thread(splitWorker).start();
	}
    }

    /**
     * Executa o Processo para fazer a divisao do arquivo em arquivos menores
     * 
     * @param path
     * @return
     */
    private Task<Object> createWorkerSplitFile(String fileName)
    {
	return new Task<Object>() {
	    @Override
	    protected Object call() throws Exception
	    {

		Long nro = 0L;
		// Long nroLinha = 0L;
		// Long nroLinhatraco = 0L;
		// String nsnAnterior = "";
		Timer timer = new Timer();
		// String temp = "";
		// String current = null;
		// Long nol = 10L; // No. of lines to be split and saved in each output
		// file.
		Long nol = qtdLinhasPorArquivo;

		// String diretorio = "D:\\DAbM\\CDs-RAW-DATA\\partes";
		String dir = System.getProperty("user.home") + File.separator + "carga" + File.separator + "partes";
		// Helper.deleteDirectory(new File(diretorio)); //right way to remove directory
		// in Java

		// File file = new File(diretorio);
		File file = new File(dir);

		FileUtils.forceMkdir(file);
		FileUtils.cleanDirectory(file);

		// System.out.println("passou: " + dir + "\n");
		// System.out.println("passou: " + dividirFile.getValue() + "\n");

		int nof = 1;
		if (dividirFile.getValue()) {

		    double tempLines = (double) totalLinhas / nol;

		    int temp1 = 0;
		    if (totalLinhas < nol) {
			temp1 = 1;
			tempLines = 1;
		    } else {
			temp1 = (int) tempLines;
		    }

		    // System.out.println("passou: " + (temp1 == tempLines) + "\n");
		    // System.out.println("passou: " + temp1 + "\n");
		    // System.out.println("passou: " + tempLines + "\n");
		    nof = 0;
		    if (temp1 == tempLines) {
			nof = temp1;
			// System.out.println("igual" + nof);
		    } else {
			nof = temp1 + 1;
			// System.out.println("nao igual" + nof);
		    }

		} else {
		    nol = totalLinhas;
		}

		// System.out.println("passou: " + totalLinhas + "\n");
		// Displays no of files to be generated.
		// System.out.println("No. of files to be generated :" + nof);
		// System.out.println("No. linhas p/arquivo :" + nol);

		try {

		    modeTask.set(false);
		    btnVrfFile.disableProperty().bind(editMode);
		    btnSelectFile.disableProperty().bind(editMode);
		    
		    progressParte.setVisible(true);
		    lblStatusParte.setVisible(true);
		    btnSplitFile.setDisable(true);

		    FileInputStream fstream = new FileInputStream(fileName);
		    DataInputStream in = new DataInputStream(fstream);

		    BufferedReader br = new BufferedReader(new InputStreamReader(in));
		    String strLine;

		    for (int j = 1; j <= nof; j++) {

			// Destination File Location
			FileWriter fstream1 = new FileWriter(dir + File.separator + "file_" + j + ".csv");
			
			// se for dividir entao progresso
			if (dividirFile.getValue()) {
			    updateProgress(j, nof);
			    updateMessage("Arquivo 'file_" + j + ".csv' concluido.");
			    // Thread.sleep(10); // 28/11/17
			}
			
			progressParte.setProgress(0);

			BufferedWriter out = new BufferedWriter(fstream1);
			for (int i = 1; i <= nol; i++) {

			    double perc = (double) i / nol;
			    //System.out.println("percentual" + perc);
			    progressParte.setProgress(perc);
			    //lblStatusParte.textProperty().setValue("dd");
			    //valorFileSplit.set("123");
			    //lblStatusParte.setText("123");
			    //Thread.sleep(10);

			    strLine = br.readLine();
			    if (strLine != null) {
				nro++;
				out.write(strLine);

				if (i != nol) {
				    out.newLine();
				}
			    }

			} // fim segundo for
			out.close();
		    } // fim primeiro for

		    if (dividirFile.getValue()) {
			updateMessage("Processo Finalizado. " + nof + " arquivos gerados pelo sistema.");
		    } else {
			updateMessage("Processo Finalizado. " + nof + " arquivo gerado pelo sistema. Total: " + nfVal.format(nol));
		    }

		    txtTempoProc.setText(timer.toString());
		    txtTempoProc.setStyle("-fx-text-fill: blue; -fx-font-size: 13;");

		    in.close();

		} catch (Exception ex) {
		    // ex.printStackTrace();
		    LOGGER.error("[Exception] " + ex.getMessage());

		    throw ex;
		}

		return true;
	    }

	};
    }

    /**
     * carrega a lista dos arquivos dividitos
     */
    private void carregarLista()
    {

	// String diretorio = "D:\\DAbM\\CDs-RAW-DATA\\partes";
	String dir = System.getProperty("user.home") + File.separator + "carga" + File.separator + "partes";
	// System.out.println(dir);
	// Path rootPath = Paths.get(System.getProperty("user.home") + File.separator +
	// "carga_seg_v" + File.separator + "partes");

	File file = new File(dir);

	if (!file.isDirectory()) {
	    try {
		FileUtils.forceMkdir(file);
	    } catch (IOException e) {
		// e.printStackTrace();
		LOGGER.error("[IOException] " + e.getMessage());
	    }
	}

	// File file = new File(diretorio);
	File afile[] = file.listFiles();

	listaSplitFile = new ArrayList<>();

	int i = 0;
	for (int j = afile.length; i < j; i++) {
	    File arquivos = afile[i];
	    // System.out.println(arquivos.getAbsolutePath());
	    listaSplitFile.add(arquivos);
	}

	observableListFile = FXCollections.observableArrayList(listaSplitFile);
	listaViewFilesSplit.setItems(observableListFile);

	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	listaViewFilesSplit.setCellFactory(new Callback<ListView<File>, ListCell<File>>() {

	    @Override
	    public ListCell<File> call(ListView<File> param)
	    {
		ListCell<File> cell = new ListCell<File>() {

		    @Override
		    protected void updateItem(File t, boolean bln)
		    {
			super.updateItem(t, bln);
			if (t != null) {
			    setText(t.getAbsoluteFile() + " ( Modificado: " + sdf.format(t.lastModified()) + " "
				    + "Tamanho: " + FileUtils.byteCountToDisplaySize(t.length()) + ")");
			}
		    }

		};

		return cell;
	    }
	});

    }

    public void setTotalLinhas(String total)
    {
	// totalLinhas = Long.parseLong(total);
	txtTotalLinhas.setText(total);
    }

}
