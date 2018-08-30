import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class TabDisplay extends Tab {
	private FlowPane flowPaneForRadioButtons;
	private RadioButton radioButtonPlayer, radioButtonGame;
	private VBox vBox;
	private GridPane gridPanePlayer, gridPaneGame;
	private TextField textFieldFirstName, textFieldLastName, textFieldAddress, textFieldPostalCode, textFieldProvince,
			textFieldPhoneNumber, textFieldGameTitle, textFieldLastPlayedOn, textFieldHighScore;
	private ComboBox<String> comboBoxPlayer, comboBoxGame, comboBoxGamesOfPlayer;
	// private ObservableList<Integer> observableListPlayerIds,
	// observableListGameIds, observableListPlayerAndGameIds;
	private ObservableList<String> observableListPlayerNames, observableListGameNames, observableListPlayerAndGameNames;
	private ObservableList<Player> observableListPlayerObjects;
	private ObservableList<Game> observableListGameObjects;
	private ObservableList<PlayerAndGame> observableListGamesOfPlayerObjects;

	// TODO
	// private ObservableList<PlayerAndGame> observableListPlayerAndGameObjecs;
	private Button buttonDisplayPlayer, buttonDisplayGame, buttonRefresh; // TODO: Remove
	private Player selectedPlayer;
	private Game selectedGame;
	private PlayerAndGame selectedGameOfPlayer;
	// these.

	// Make the observableList local this class.
	public TabDisplay() {
		super();
//		this.observableListPlayerObjects = observableListPlayerObjects;
//		this.observableListGameObjects = observableListGameObjects;
//		
//		// TODO: Maybe remove this.
//		this.observableListGamesOfPlayerObjects = observableListGamesOfPlayerObjects;
		
		setObservableLists();

		setRadioButtons();
		setFlowPaneRadioButtons();

		setTextFields();
		setComboBoxes();

		// TODO: Remove this.
		// debugPlayer();

		setGridPanePlayer();
		setGridPaneGame();
		setVBox();

		setTab();

		setHandlers();

	}

	// TODO: Make a method that calls setComboBoxes() to refresh them
	// whenever this tab is selected.

	private void populateTextFieldsPlayer(int indexOfSelectedPlayer) {
		selectedPlayer = observableListPlayerObjects.get(indexOfSelectedPlayer);
		textFieldFirstName.setText(selectedPlayer.getFirstName());
		textFieldLastName.setText(selectedPlayer.getLastName());
		textFieldAddress.setText(selectedPlayer.getAddress());
		textFieldPostalCode.setText(selectedPlayer.getPostalCode());
		textFieldProvince.setText(selectedPlayer.getProvince());
		textFieldPhoneNumber.setText(selectedPlayer.getPhoneNumber());

	}
	
	private void setObservableLists() {
		observableListPlayerObjects = FXCollections.observableArrayList();
		observableListGameObjects = FXCollections.observableArrayList();
		observableListGamesOfPlayerObjects = FXCollections.observableArrayList();

	}
	
	private void refreshEditables() {
		
		if (observableListGamesOfPlayerObjects != null) {
			int tempSize = observableListGamesOfPlayerObjects.size();
			for (int i = 0; i < tempSize; i++) {
				observableListGamesOfPlayerObjects.remove(0);
				comboBoxGamesOfPlayer.getItems().remove(0);
			}
		}

	}

	private void updateComboBoxGamesOfPlayer() {
		// Refresh the textfields for info of selected gameOfPlayer.
		textFieldLastPlayedOn.setText("");
		textFieldHighScore.setText("");

		// TODO: Rename to lowercase.
		// String username = "KateBren";
		// String password = "KateBren";
		// String connectionUrl =
		// "jdbc:sqlserver://localhost:52412;databaseName=JavaAssignment5";

		// Declare the JDBC objects.
		Connection con = null;

		try {
			// Establish the connection.
			// Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			con = DriverManager.getConnection(Assignment5Constants.DB_CONNECTION_URL, Assignment5Constants.DB_USERNAME,
					Assignment5Constants.DB_PASSWORD);

			// TODO: Remove this.
			System.out.println("Connected for updating comboBoxGamesOfPlayer!");

			// Query for selecting joined table of Player and Game.
			PreparedStatement ps = con.prepareStatement(
					"SELECT PlayerAndGame.player_game_id, Player.player_id, Player.first_name, Game.game_title, PlayerAndGame.playing_date, PlayerAndGame.score, Game.game_Id FROM Player INNER JOIN PlayerAndGame ON Player.player_id = PlayerAndGame.player_id INNER JOIN Game ON PlayerAndGame.game_id = Game.game_Id WHERE Player.player_id = ?");

			// set the preparedstatement parameters
			ps.setInt(1, selectedPlayer.getPlayerId());

			ResultSet resultSet = ps.executeQuery();

			// Refresh the editables.
			refreshEditables();
//			int tempSize = observableListGamesOfPlayerObjects.size();
//			for (int i = 0; i < tempSize; i++) {
//				observableListGamesOfPlayerObjects.remove(0);
//				comboBoxGamesOfPlayer.getItems().remove(0);
//			}

			// Iterate through the data in the result set.
			// Populate observableLists.
			while (resultSet.next()) {
				// Create new PlayerAndGame object.
				PlayerAndGame playerAndGame = new PlayerAndGame(resultSet.getInt("player_game_id"),
						resultSet.getInt("game_Id"), resultSet.getInt("player_id"), resultSet.getString("game_title"),
						resultSet.getString("playing_date"), resultSet.getInt("score"));

				// Populate observableListGamesOfPlayerObjects with the newly
				// created
				// object playerAndGame.
				observableListGamesOfPlayerObjects.add(playerAndGame);

				// Populate comboBox.
				comboBoxGamesOfPlayer.getItems().add(playerAndGame.getGameTitle());
			}

			ps.close();
			// TODO:
			System.out.println(" comboBoxGamesOfPlayer updated!");
		} catch (SQLException e) {
			System.err.println("Error bro in method updateComboBox(): " + e);
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// // TODO: DEBUG
		// System.out.println();
		// System.out.println("AFTER");
		// System.out.println("observableListGamesOfPlayerObjects.size(): " +
		// observableListGamesOfPlayerObjects.size());
		// System.out.println("comboBoxGamesOfPlayer.size(): " +
		// comboBoxGamesOfPlayer.getItems().size());

	}

	private void populateTextFieldsGame(int indexOfSelectedGame) {
		selectedGame = observableListGameObjects.get(indexOfSelectedGame);
		textFieldGameTitle.setText(selectedGame.getGameTitle());
	}

	private void debugPlayer() {
		// for (Player player : observableListPlayerObjects) {
		// System.out.println(player.getPlayerId());
		// System.out.println(player.getFirstName());
		// System.out.println(player.getLasttName());
		// System.out.println(player.getAddress());
		// System.out.println(player.getPostalCode());
		// System.out.println(player.getProvince());
		// System.out.println(player.getPhoneNumber());
		// System.out.println("\n\n");
		// }

		// for (Game game : observableListGameObjects) {
		// System.out.println(game.getGameId());
		// System.out.println(game.getGameTitle());
		// System.out.println("\n\n");
		// }

	}

	public void setComboBoxes() {
		// For comboBoxes String choices.
		// TODO: Maybe remove these..
		observableListPlayerNames = FXCollections.observableArrayList();
		observableListGameNames = FXCollections.observableArrayList();
		observableListPlayerAndGameNames = FXCollections.observableArrayList();

		// For sort of a local-offline-db for objects to be manipulated.
		// observableListPlayerObjects = FXCollections.observableArrayList();
		// TOOD: Make this a reference from MainApp.
		// observableListGameObjects = FXCollections.observableArrayList();

		comboBoxPlayer = new ComboBox<>();
		comboBoxGamesOfPlayer = new ComboBox<>();
		comboBoxGame = new ComboBox<>();

		populateComboBoxes();
	}

	private void populateComboBoxes() {
		// TODO: Rename to lowercase.
		// String username = "KateBren";
		// String password = "KateBren";
		// String connectionUrl =
		// "jdbc:sqlserver://localhost:52412;databaseName=JavaAssignment5";

		// Declare the JDBC objects.
		Connection con = null;

		try {
			// Establish the connection.
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			con = DriverManager.getConnection(Assignment5Constants.DB_CONNECTION_URL, Assignment5Constants.DB_USERNAME,
					Assignment5Constants.DB_PASSWORD);

			// TODO: Remove this.
			System.out.println("Connected bro!");

			// For Player.
			String querySelect = "SELECT * FROM Player";
			querySelectPlayer(con, querySelect);

			// For Game.
			querySelect = "SELECT * FROM Game";
			querySelectGame(con, querySelect);

			// TODO
			// For PlayerAndGame.
			// querySelect = "SELECT * FROM Player";
			// querySelectPlayer(con, querySelect);

		} catch (SQLException e) {
			System.err.println("Error bro in method populateComboBoxer(): " + e);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	private void querySelectGame(Connection con, String querySelect) throws SQLException {
		Statement statement = con.createStatement();
		ResultSet resultSet = statement.executeQuery(querySelect);

		// Iterate through the data in the result set.
		// Populate observableLists.
		while (resultSet.next()) {
			// Player.
			observableListGameNames.add(resultSet.getString("game_title"));

			// Create new Player object.
			Game game = new Game(resultSet.getInt("game_Id"), resultSet.getString("game_title"));

			// Populate observableListPlayerObjects with the newly created
			// object game.
			observableListGameObjects.add(game);

			// TODO: Don't really need this.
			// Populate comboBox.
			comboBoxGame.getItems().add(game.getGameTitle());
		}

		// // Populate comboBox.
		// comboBoxGame.getItems().addAll(observableListGameNames);

	}

	private void querySelectPlayer(Connection con, String querySelect) throws SQLException {
		Statement statement = con.createStatement();
		ResultSet resultSet = statement.executeQuery(querySelect);

		// Iterate through the data in the result set.
		// Populate observableLists.
		while (resultSet.next()) {
			// Player.
			observableListPlayerNames.add(resultSet.getString("first_name"));

			// Create new Player object.
			Player player = new Player(resultSet.getInt("player_id"), resultSet.getString("first_name"),
					resultSet.getString("last_name"), resultSet.getString("address"),
					resultSet.getString("postal_code"), resultSet.getString("province"),
					resultSet.getString("phone_number"));

			// Populate observableListPlayerObjects with the newly created
			// object Player.
			observableListPlayerObjects.add(player);

			// Populate comboBox.
			comboBoxPlayer.getItems()
					.add(player.getFirstName() + " " + player.getLastName() + ", " + player.getPlayerId());
		}

		// // Populate comboBox.
		// comboBoxPlayer.getItems().addAll(observableListPlayerNames);
	}

	private void setTab() {
		this.setText("TabDisplay");
		this.setContent(vBox);
	}

	private void setGridPaneGame() {
		gridPaneGame = new GridPane();
		gridPaneGame.setAlignment(Pos.CENTER_LEFT);
		gridPaneGame.setPadding(new Insets(11.5, 12.5, 13.5, 14.5));
		gridPaneGame.setHgap(5.5);
		gridPaneGame.setVgap(5.5);

		// Place nodes in the pane
		gridPaneGame.add(new Label("Game to Display"), 0, 0);
		gridPaneGame.add(comboBoxGame, 1, 0);
		gridPaneGame.add(new Label("Game Title:"), 0, 1);
		gridPaneGame.add(textFieldGameTitle, 1, 1);

		buttonDisplayGame = new Button("Display Game");
		gridPaneGame.add(buttonDisplayGame, 1, 2);
		// GridPane.setHalignment(buttonDisplayPlayer, HPos.RIGHT);

	}

	private void setTextFields() {
		textFieldFirstName = new TextField();
		textFieldLastName = new TextField();
		textFieldAddress = new TextField();
		textFieldPostalCode = new TextField();
		textFieldProvince = new TextField();
		textFieldPhoneNumber = new TextField();
		textFieldGameTitle = new TextField();
		textFieldLastPlayedOn = new TextField();
		textFieldHighScore = new TextField();
	}

	private void setGridPanePlayer() {
		gridPanePlayer = new GridPane();
		gridPanePlayer.setAlignment(Pos.CENTER_LEFT);
		gridPanePlayer.setPadding(new Insets(11.5, 12.5, 13.5, 14.5));
		gridPanePlayer.setHgap(5.5);
		gridPanePlayer.setVgap(5.5);

		// Place nodes in the pane
		gridPanePlayer.add(new Label("Player to Display"), 0, 0);
		gridPanePlayer.add(comboBoxPlayer, 1, 0);
		gridPanePlayer.add(new Label("First Name:"), 0, 1);
		gridPanePlayer.add(textFieldFirstName, 1, 1);
		gridPanePlayer.add(new Label("Last Name:"), 0, 2);
		gridPanePlayer.add(textFieldLastName, 1, 2);
		gridPanePlayer.add(new Label("Address:"), 0, 3);
		gridPanePlayer.add(textFieldAddress, 1, 3);
		gridPanePlayer.add(new Label("Postal Code:"), 0, 4);
		gridPanePlayer.add(textFieldPostalCode, 1, 4);
		gridPanePlayer.add(new Label("Province:"), 0, 5);
		gridPanePlayer.add(textFieldProvince, 1, 5);
		gridPanePlayer.add(new Label("Phone number:"), 0, 6);
		gridPanePlayer.add(textFieldPhoneNumber, 1, 6);

		gridPanePlayer.add(new Label(""), 0, 7);
		gridPanePlayer.add(new Label(""), 1, 7);
		gridPanePlayer.add(new Label("Player's Game To Display"), 0, 8);
		gridPanePlayer.add(comboBoxGamesOfPlayer, 1, 8);
		gridPanePlayer.add(new Label("Last Played On"), 0, 9);
		gridPanePlayer.add(textFieldLastPlayedOn, 1, 9);
		gridPanePlayer.add(new Label("High Score"), 0, 10);
		gridPanePlayer.add(textFieldHighScore, 1, 10);

		// buttonDisplayPlayer = new Button("Display Player");
		// gridPanePlayer.add(buttonDisplayPlayer, 1, 7);

	}

	private void setVBox() {
//		vBox = new VBox(20);
//		vBox.setPadding(new Insets(15, 5, 5, 5));
//		vBox.getChildren().add(flowPaneForRadioButtons);
		
		vBox = new VBox(20);
		vBox.setPadding(new Insets(15, 5, 5, 5));
		vBox.getChildren().add(flowPaneForRadioButtons);
		vBox.getChildren().add(gridPanePlayer);
	}

	private void setRadioButtons() {
		radioButtonPlayer = new RadioButton("Player");
		radioButtonPlayer.setSelected(true);
		radioButtonGame = new RadioButton("Game");

		ToggleGroup group = new ToggleGroup();
		radioButtonPlayer.setToggleGroup(group);
		radioButtonGame.setToggleGroup(group);

	}

	private void setFlowPaneRadioButtons() {
		buttonRefresh = new Button("Refresh");
		Label label = new Label("Select which info to display:");
		
		Button hiddenButton = new Button();
		hiddenButton.setPrefSize(Assignment5Constants.APP_WIDTH-220, 25);
		hiddenButton.setVisible(false);
		
		
		flowPaneForRadioButtons = new FlowPane();
		flowPaneForRadioButtons.getChildren().addAll(label, hiddenButton, buttonRefresh, radioButtonPlayer, radioButtonGame);
	}

	private void setHandlers() {
		radioButtonPlayer.setOnAction(e ->

		{
			if (radioButtonPlayer.isSelected()) {
				// TODO: Remove this.
				System.out.println("Radio button player clicked");

				if (gridPaneGame != null) {
					vBox.getChildren().remove(gridPaneGame);
					vBox.getChildren().add(gridPanePlayer);
				} else {
					vBox.getChildren().add(gridPanePlayer);
				}
			}
		});

		radioButtonGame.setOnAction(e -> {
			if (radioButtonGame.isSelected()) {
				System.out.println("Radio button game clicked");
				if (gridPanePlayer != null) {
					vBox.getChildren().remove(gridPanePlayer);
					vBox.getChildren().add(gridPaneGame);

				} else {
					vBox.getChildren().add(gridPaneGame);
				}
			}
		});
		
		buttonRefresh.setOnAction(e -> {
			if (comboBoxPlayer != null) {
				refreshComboBoxes();
				populateComboBoxes();
				refreshEditables();
				refreshTextFields();
			}

		});

		comboBoxPlayer.setOnAction(e -> {
			populateTextFieldsPlayer(comboBoxPlayer.getSelectionModel().getSelectedIndex());
			updateComboBoxGamesOfPlayer();
		});

		comboBoxGamesOfPlayer.setOnAction(
				e -> populateTextFieldsGamesOfPlayer(comboBoxGamesOfPlayer.getSelectionModel().getSelectedIndex()));

		comboBoxGame.setOnAction(e -> populateTextFieldsGame(comboBoxGame.getSelectionModel().getSelectedIndex()));
		// comboBoxPlayer.setOnAction(e ->
		// System.out.println(comboBoxPlayer.getSelectionModel().getSelectedIndex()));
	}
	
	private void refreshTextFields() {
		textFieldFirstName.setText("");
		textFieldLastName.setText("");
		textFieldAddress.setText("");
		textFieldPostalCode.setText("");
		textFieldProvince.setText("");
		textFieldPhoneNumber.setText("");
		textFieldGameTitle.setText("");
		textFieldLastPlayedOn.setText("");
		textFieldHighScore.setText("");
		
	}

	private void refreshComboBoxes() {
		int tempSize = comboBoxPlayer.getItems().size();
		for (int i = 0; i < tempSize; i++) {
			comboBoxPlayer.getItems().remove(0);
			observableListPlayerNames.remove(0);
			observableListPlayerObjects.remove(0);
		}

		tempSize = comboBoxGame.getItems().size();
		for (int i = 0; i < tempSize; i++) {
			comboBoxGame.getItems().remove(0);
			observableListGameNames.remove(0);
			observableListGameObjects.remove(0);
		}

	}

	private void populateTextFieldsGamesOfPlayer(int selectedIndex) {
		selectedGameOfPlayer = observableListGamesOfPlayerObjects.get(selectedIndex);
		textFieldLastPlayedOn.setText(selectedGameOfPlayer.getPlayingDate());
		textFieldHighScore.setText(Integer.toString(selectedGameOfPlayer.getScore()));
	}

}
