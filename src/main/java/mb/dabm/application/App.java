package mb.dabm.application;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class App extends Application
{
    public static Stage stage = null;
    private static final Logger LOGGER = LogManager.getLogger(App.class.getName());
    
    @Override
    public void start(Stage stage)
    {
	try {

	    // Carrega o layout FXML
	    Parent root = FXMLLoader.load(getClass().getResource("/view/FXMLVBoxMain.fxml"));
	    // Cria a cena
	    Scene scene = new Scene(root, 800, 600);
	    
	    Image icon = new Image(getClass().getResourceAsStream("/img/icon.png"));

	    // Cria a janela principal
	    Stage mainScene = new Stage();
	    
	    mainScene.getIcons().add(icon);
	    mainScene.setScene(scene);
	    mainScene.setTitle("Aplicativo de Extração e Tratamento de Dados");
	    mainScene.setResizable(false);
	    mainScene.show();
	    
	    stage.getIcons().add(icon);
	    // Atrela a cena a janela
	    mainScene.setScene(scene);

	    // passando o movimento para o mouse
	    App.stage = mainScene;
	    // Exibe a janela
	    mainScene.show();
	    
	    
	} catch (Exception e) {
	    //e.printStackTrace();
	    LOGGER.error("Exception - App! " + e.getMessage());
	}
    }

    public static void main(String[] args)
    {
	launch(args);
    }
}
