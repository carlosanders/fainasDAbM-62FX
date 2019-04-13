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
    private static final Logger LOGGER = LogManager.getLogger(App.class.getName());
    
    @Override
    public void start(Stage primaryStage)
    {
	try {

	    Parent root = FXMLLoader.load(getClass().getResource("/view/FXMLVBoxMain.fxml"));
	    Scene scene = new Scene(root, 800, 600);
	    
	    Image icon = new Image(getClass().getResourceAsStream("/img/icon.png"));

	    primaryStage.getIcons().add(icon);
	    primaryStage.setScene(scene);
	    primaryStage.setTitle("Aplicativo de Extração e Tratamento de Dados");
	    primaryStage.setResizable(false);
	    primaryStage.show();
	    
	    
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
