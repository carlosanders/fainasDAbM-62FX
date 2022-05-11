package mb.dabm.controller;

import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import mb.dabm.dao.MantisCustomFieldDAO;
import mb.dabm.database.IPool;
import mb.dabm.database.Pool;
import mb.dabm.helper.Helper;
import mb.dabm.model.MantisCustomField;
import mb.dabm.model.MantisCustomFieldString;
import mb.dabm.model.MantisUser;
import mb.dabm.service.MantisCustomFieldService;

public class FXMLAnchorPaneSprintController implements Initializable {

	@FXML
	private AnchorPane root;
	@FXML
	private JFXButton btnExecutar;
	@FXML
	private JFXComboBox<MantisCustomField> cbCampos;
	@FXML
	private Label lblCampoCustom;
	@FXML
	private Label lblTotalLinhas;
	@FXML
	private JFXTextField txtValor;
	@FXML
	private JFXTextArea textAreaChamados;
	@FXML
	private Label lblNroChamdo;
	@FXML
	private AnchorPane anchorPaneResumo;
	@FXML
	private JFXProgressBar progress;
	@FXML
	private Label lblStatus;
	@FXML
	private Label lblTotal;
	@FXML
	private JFXTextField txtTotalLinhas;
	@FXML
	private Label lblTempoProc;
	@FXML
	private JFXTextField txtTempoProc;
	@FXML
	private JFXButton btnTask;
	@FXML
	private JFXComboBox<MantisUser> cbUsers;

	// variaveis uteis
	private ObservableList<MantisCustomField> dataCombobox = FXCollections
			.observableArrayList(new ArrayList<MantisCustomField>());

	private ObservableList<MantisUser> dataComboBoxUser = FXCollections
			.observableArrayList(new ArrayList<MantisUser>());

	// private BooleanProperty editMode = new SimpleBooleanProperty();
	private BooleanProperty modeTask = new SimpleBooleanProperty();
	// private Locale meuLocal = new Locale("pt", "BR");
	// private NumberFormat nfVal = NumberFormat.getIntegerInstance(meuLocal);

	private IPool pool = Pool.getInstacia();
	private final MantisCustomFieldDAO mantisCustomFieldDAO = new MantisCustomFieldDAO(pool);

	// private static final Logger LOGGER =
	// LogManager.getLogger(FXMLAnchorPaneSprintController.class.getName());

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		modeTask.set(true);
		// ao iniciar a var inicia como false e para disable o btn inverte seu valor
		// btnTask.disableProperty().bind(modeTask);
		// so habiliata o btn se o campo textServico for =! de null ou txtLogin =! null
		// btnVrfFile.disableProperty().bind(txtNameFile.textProperty().isEmpty());

		// testConnection();
		dataCombobox.setAll(loadDataCustomField());
		cbCampos.setItems(dataCombobox);

		dataComboBoxUser.setAll(loadDataCustomFieldUser());
		cbUsers.setItems(dataComboBoxUser);

		// long time = System.currentTimeMillis();
		// Timestamp t = new Timestamp(time);
		// Date d = new Date(t.getTime());
		// System.out.println("Data: " + d);

		//// converte data para long
		// Date now = new Date();
		// long ut3 = now.getTime() / 1000L;
		// System.out.println(ut3);
		///

		// 1559923437
		// 1558373486
		// long unixSeconds = 1559923437;
		// convert seconds to milliseconds
		// Date date = new java.util.Date(unixSeconds * 1000L);
		// the format of your date
		// SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss
		// z");
		// give a timezone reference for formatting (see comment at the bottom)
		// sdf.setTimeZone(java.util.TimeZone.getTimeZone("America/Sao_Paulo"));
		// String formattedDate = sdf.format(date);
		// System.out.println(formattedDate);

	}

	// private void testConnection()
	// {
	// TestDAO dao = new TestDAO(pool);
	// try {
	// System.out.println(dao.getDateBD());
	// } catch (SQLException e) {
	// // e.printStackTrace();
	// LOGGER.error("[Exception] " + e.getMessage());
	// }
	// }

	private List<MantisCustomField> loadDataCustomField() {

		List<MantisCustomField> list = new ArrayList<>();
		list = mantisCustomFieldDAO.listar();

		return list;
	}

	private List<MantisUser> loadDataCustomFieldUser() {

		List<MantisUser> list = new ArrayList<>();
		list = mantisCustomFieldDAO.listarUser();

		return list;
	}

	@FXML
	void onProcessarLeitura(ActionEvent event) {
		// System.out.println(textAreaChamados.getText());

		String toCount = textAreaChamados.getText();
		// String[] lineArray = toCount.split("\n");
		// System.out.println("Total de Linhas no array: " + lineArray.length);

		// String lineSeparator = System.getProperty("line.separator");
		// System.out.println("separador: " + lineSeparator);

		// original c/ possiveis espacos vazios ou nulos
		List<String> items = Arrays.asList(toCount.trim().split("(\r\n|\r|\n|,)", -1));
		// faz iteracao para remover os spaces empty/null
		List<String> result = new ArrayList<String>();
		for (String str : items) {
			if (str != null && !str.isEmpty()) {
				result.add(str);
			}
		}

		System.out.println("Total de Linhas na lista: " + result.size());
		System.out.println(result.toString());

		MantisCustomFieldService mantisService = new MantisCustomFieldService();

		//corrigido da lista de "itens" que possui espacos para a lista sem os espacos "result"
		List<MantisCustomFieldString> retorno = mantisService.atulizarMantisCustomString(result, txtValor.getText(),
				cbCampos.getSelectionModel().getSelectedItem(), cbUsers.getSelectionModel().getSelectedItem());

		// retorno.forEach(m -> System.out.println(m.toString()));

		Optional<ButtonType> res = Helper.enviarPergunta(AlertType.CONFIRMATION, "Sucesso!",
				"Atualização realizada com sucesso!", retorno.size() + " registros processado(s).");

		if (res.get() == ButtonType.OK) {
			// dividirFile.set(true);
			textAreaChamados.clear();
			txtValor.clear();
			cbCampos.getSelectionModel().clearSelection();
			cbUsers.getSelectionModel().clearSelection();
		}

	}

	/**
	 * transforma para maiuscula as entradas
	 * 
	 * @param event
	 */
	public void keyListener(KeyEvent event) {
		txtValor.setTextFormatter(new TextFormatter<>((change) -> {
			change.setText(change.getText().toUpperCase());
			return change;
		}));
	}

}
