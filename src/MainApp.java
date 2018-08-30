import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.stage.Stage;

public class MainApp extends Application {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch();
	}

	@Override
	public void start(Stage myStage) {
		
		TabPane tabPane = new TabPane();
		tabPane.setPrefSize(Assignment5Constants.APP_WIDTH, Assignment5Constants.APP_HEIGHT);
		tabPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
		
		// TODO: Maybe you don't need the params for the tabs.
		TabInsert tabInsert = new TabInsert();
		tabPane.getTabs().add(tabInsert);
		
		TabDisplay tabDisplay = new TabDisplay();		
		tabPane.getTabs().add(tabDisplay);
		TabUpdate tabUpdate = new TabUpdate();		
		tabPane.getTabs().add(tabUpdate);
		
		
		Scene scene = new Scene(tabPane);

		// Set the stage.
		myStage.setTitle("MainApp");
		myStage.setScene(scene);
		myStage.show();
	}

}
