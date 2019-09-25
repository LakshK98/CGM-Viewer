package application;
	
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) {
    	
    	Parent root;
    	Path path = FileSystems.getDefault().getPath(".").toAbsolutePath();
    	System.out.println(path.toString());
		try {
			
			root = FXMLLoader.load(getClass().getResource("/application/Main.fxml"));
	        Scene scene = new Scene(root);
	        primaryStage.setScene(scene);
	        primaryStage.setTitle("CGM Viewer");
	        primaryStage.show();  
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
