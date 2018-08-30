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
import javafx.geometry.VPos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class TabUpdate extends Tab {
	private FlowPane flowPaneForRadioButtons;
	private RadioButton radioButtonPlayer, radioButtonGame;
	private VBox vBox;
	private GridPane gridPanePlayer, gridPaneGame;
	private TextField textFieldFirstName, textFieldLastName, textFieldAddress, textFieldPostalCode, textFieldProvince,
			textFieldPhoneNumber, textFieldGameTitle;
	private ComboBox<String> comboBoxPlayer, comboBoxGame;
	// private ObservableList<Integer> observableListPlayerIds,
	// observableListGameIds, observableListPlayerAndGameIds;
	private ObservableList<String> observableListPlayerNames, observableListGameNames, observableListPlayerAndGameNames;
	private ObservableList<Player> observableListPlayerObjects;
	private ObservableList<Game> observableListGameObjects;

	// TODO
	// private ObservableList<PlayerAndGame> observableListPlayerAndGameObjecs;
	private Button buttonUpdatePlayer, buttonUpdateGame, buttonRefresh;
	private Game selectedGame;
	private Player selectedPlayer;

	private GridPane gridPaneGameLibrary;
	private ListView<String> listViewGameChoices;
	private ListView<String> listViewGamesPlayed;
	private Button buttonAddGameToLib;
	private Button buttonRemoveGameFromLib;
	private ObservableList<PlayerAndGame> observableArrayListGamesOfPlayerAddOn;
	private ObservableList<Game> observableArrayListGameChoicesAddOn;

	public TabUpdate() {
		super();

		setObservableLists();

		setRadioButtons();
		setFlowPaneRadioButtons();

		setTextFields();
		setComboBoxes();
		setListViews();

		setGridAddOn();
		setGridPanePlayer();

		setGridPaneGame();

		setVBox();
		setTab();

		setHandlers();
	}

	// TODO: Add method for tab events. Call setComboBoxes().

	private void setListViews() {
		listViewGamesPlayed = new ListView<>();
		listViewGamesPlayed.setPrefSize(200, 200);

		// createing a list of games
		listViewGameChoices = new ListView<>();
		// // TODO: Make this adjustment later.
		// listViewGameChoices.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		listViewGameChoices.setPrefSize(200, 200);

	}

	private void setObservableLists() {
		observableListPlayerObjects = FXCollections.observableArrayList();
		observableListGameObjects = FXCollections.observableArrayList();

		observableArrayListGamesOfPlayerAddOn = FXCollections.observableArrayList();
		observableArrayListGameChoicesAddOn = FXCollections.observableArrayList();

	}

	private void setGridAddOn() {
		// gridAddOn = new TabUpdateAddOn();
		setGridPaneGameLibrary();
		setGameLibraryObservableArrayLists();
		setGameLibraryHandlers();

	}

	private void populateTextFieldsPlayer(int indexOfSelectedPlayer) {
		selectedPlayer = observableListPlayerObjects.get(indexOfSelectedPlayer);

		textFieldFirstName.setText(selectedPlayer.getFirstName());
		textFieldLastName.setText(selectedPlayer.getLastName());
		textFieldAddress.setText(selectedPlayer.getAddress());
		textFieldPostalCode.setText(selectedPlayer.getPostalCode());
		textFieldProvince.setText(selectedPlayer.getProvince());
		textFieldPhoneNumber.setText(selectedPlayer.getPhoneNumber());
	}

	private void populateTextFieldsGame(int indexOfSelectedGame) {
		selectedGame = observableListGameObjects.get(indexOfSelectedGame);
		textFieldGameTitle.setText(selectedGame.getGameTitle());
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
		comboBoxGame = new ComboBox<>();

		populateComboBoxes();
	}

	private void populateComboBoxes() {
		// Declare the JDBC objects.
		Connection con = null;

		try {
			// Establish the connection.
			// Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			con = DriverManager.getConnection(Assignment5Constants.DB_CONNECTION_URL, Assignment5Constants.DB_USERNAME,
					Assignment5Constants.DB_PASSWORD);

			// TODO: Remove this.
			System.out.println("Connected bro!");

			// For Player comboBox.
			String querySelect = "SELECT * FROM Player";
			querySelectPlayer(con, querySelect);

			// For Game comboBox.
			querySelect = "SELECT * FROM Game";
			querySelectGame(con, querySelect);

			// TODO
			// For PlayerAndGame.
			// querySelect = "SELECT * FROM Player";
			// querySelectPlayer(con, querySelect);

		} catch (SQLException e) {
			System.err.println("Error bro: " + e);
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
			// object Player.
			observableListGameObjects.add(game);

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
		this.setText("TabUpdate");
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

		buttonUpdateGame = new Button("Update Game");
		gridPaneGame.add(buttonUpdateGame, 1, 2);
		// GridPane.setHalignment(buttonUpdatePlayer, HPos.RIGHT);

	}

	private void setGridPaneGameLibrary() {
		gridPaneGameLibrary = new GridPane();
		gridPaneGameLibrary.setPrefSize(450, 200);

		gridPaneGameLibrary.setAlignment(Pos.CENTER_LEFT);
		gridPaneGameLibrary.setPadding(new Insets(11.5, 12.5, 13.5, 14.5));
		gridPaneGameLibrary.setHgap(5.5);
		gridPaneGameLibrary.setVgap(5.5);

		// Place nodes in the pane
		gridPaneGameLibrary.add(new Label("Games you can play."), 0, 0);
		gridPaneGameLibrary.add(new Label(""), 1, 0);
		gridPaneGameLibrary.add(new Label("Games you currently play."), 2, 0);

		gridPaneGameLibrary.add(new ScrollPane(listViewGameChoices), 0, 1);
		// System.out.println(listViewGameChoices.getSelectionModel().getSelectedIndex());

		buttonAddGameToLib = new Button("->");
		buttonRemoveGameFromLib = new Button("<-");
		FlowPane flowPaneForButtons = new FlowPane();
		flowPaneForButtons.getChildren().add(buttonAddGameToLib);
		flowPaneForButtons.getChildren().add(buttonRemoveGameFromLib);
		flowPaneForButtons.setPrefSize(50, 60);
		gridPaneGameLibrary.add(flowPaneForButtons, 1, 1);
		// GridPane.setHalignment(flowPaneForButtons, HPos.CENTER);
		GridPane.setValignment(flowPaneForButtons, VPos.CENTER);

		// // TODO
		// listViewGamesPlayed.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		gridPaneGameLibrary.add(new ScrollPane(listViewGamesPlayed), 2, 1);

		// GridPane.setHalignment(buttonInsertPlayer, HPos.RIGHT);

	}

	private void setGameLibraryObservableArrayLists() {
		if (selectedPlayer == null) {
			return;
		}

		if (observableArrayListGamesOfPlayerAddOn != null) {
			refreshEditables();
		}

		// observableArrayListGamesOfPlayerAddOn =
		// FXCollections.observableArrayList();
		// observableArrayListGameChoicesAddOn =
		// FXCollections.observableArrayList();

		// observableArrayListGamesOfPlayerAddOn.add(new Pla)
		// Declare the JDBC objects.
		Connection con = null;

		try {
			// Establish the connection.
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			con = DriverManager.getConnection(Assignment5Constants.DB_CONNECTION_URL, Assignment5Constants.DB_USERNAME,
					Assignment5Constants.DB_PASSWORD);

			// For game choices.
			PreparedStatement ps = con.prepareStatement(
					"SELECT * FROM Game WHERE Game.game_Id NOT IN (SELECT PlayerAndGame.game_id FROM PlayerAndGame WHERE PlayerAndGame.player_id = ?)");

			// set the preparedstatement parameters
			ps.setInt(1, selectedPlayer.getPlayerId());

			ResultSet resultSet = ps.executeQuery();

			// Iterate through the data in the result set.
			// Populate observableLists.
			while (resultSet.next()) {
				// Create new Game object.
				Game game = new Game(resultSet.getInt("game_Id"), resultSet.getString("game_title"));

				//
				observableArrayListGameChoicesAddOn.add(game);
				listViewGameChoices.getItems().add(game.getGameTitle());
			}

			// For games that player can add.
			// Query for selecting joined table of Player and Game.
			ps = con.prepareStatement(
					"SELECT PlayerAndGame.player_game_id, Player.player_id, Player.first_name, Game.game_title, PlayerAndGame.playing_date, PlayerAndGame.score, Game.game_Id FROM Player INNER JOIN PlayerAndGame ON Player.player_id = PlayerAndGame.player_id INNER JOIN Game ON PlayerAndGame.game_id = Game.game_Id WHERE Player.player_id = ?");

			// set the preparedstatement parameters
			ps.setInt(1, selectedPlayer.getPlayerId());

			resultSet = ps.executeQuery();

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
				observableArrayListGamesOfPlayerAddOn.add(playerAndGame);

				// Populate comboBox.
				listViewGamesPlayed.getItems().add(playerAndGame.getGameTitle());
			}

			ps.close();
		} catch (SQLException e) {
			System.out.println("Error bro in method populateComboBoxer(): " + e);
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

	private void refreshEditables() {
		if (observableArrayListGameChoicesAddOn != null) {
			int tempSize = observableArrayListGameChoicesAddOn.size();
			for (int i = 0; i < tempSize; i++) {
				observableArrayListGameChoicesAddOn.remove(0);
				listViewGameChoices.getItems().remove(0);
			}
		}

		if (observableArrayListGamesOfPlayerAddOn != null) {
			int tempSize = observableArrayListGamesOfPlayerAddOn.size();
			for (int i = 0; i < tempSize; i++) {
				observableArrayListGamesOfPlayerAddOn.remove(0);
				listViewGamesPlayed.getItems().remove(0);
			}
		}
	}

	private void setGameLibraryHandlers() {
		buttonAddGameToLib.setOnAction(e -> {
			if (selectedPlayer == null) {
				return;
			}
			insertPlayerAndGameToDb(listViewGameChoices.getSelectionModel().getSelectedIndex());
			refreshEditables();
			setGameLibraryObservableArrayLists();
		});

		buttonRemoveGameFromLib.setOnAction(e -> {
			if (selectedPlayer == null) {
				return;
			}
			removePlayerAndGameFromDb(listViewGamesPlayed.getSelectionModel().getSelectedIndex());
			refreshEditables();
			setGameLibraryObservableArrayLists();
		});

	}

	private void insertPlayerAndGameToDb(int indexOfSelectedGame) {
		// Insert to db.
		try {
			PreparedStatement pst;
			Connection con;
			// JDBC driver name and database URL for Oracle
			String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
			// load the driver class
			Class.forName(driver);

			// establish connection to database
			con = DriverManager.getConnection(Assignment5Constants.DB_CONNECTION_URL, Assignment5Constants.DB_USERNAME,
					Assignment5Constants.DB_PASSWORD);

			pst = con.prepareStatement("Insert into [dbo].[PlayerAndGame] (game_id, player_id) VALUES(?,?)");
			// populate the fields
			pst.setInt(1, observableArrayListGameChoicesAddOn.get(indexOfSelectedGame).getGameId());
			pst.setInt(2, selectedPlayer.getPlayerId());// TODO

			// Execute the prepared statement using executeUpdate method:
			pst.executeUpdate(); // returns the row count

			// TODO:
			System.out.println("method insertPlayerAndGameToDb() successful!");

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();

		} finally {
			System.out.println("Done!");
		}

	}

	private void removePlayerAndGameFromDb(int indexOfSelectedGame) {
		try {
			PreparedStatement pst;
			Connection con;
			// JDBC driver name and database URL for Oracle
			String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
			// load the driver class
			Class.forName(driver);

			// establish connection to database
			con = DriverManager.getConnection(Assignment5Constants.DB_CONNECTION_URL, Assignment5Constants.DB_USERNAME,
					Assignment5Constants.DB_PASSWORD);

			pst = con.prepareStatement("DELETE FROM PlayerAndGame WHERE PlayerAndGame.player_game_id = ?");
			// populate the fields
			pst.setInt(1, observableArrayListGamesOfPlayerAddOn.get(indexOfSelectedGame).getPlayerGameId());

			// Execute the prepared statement using executeUpdate method:
			pst.executeUpdate(); // returns the row count

			// TODO:
			System.out.println("method removePlayerAndGameFromDb() successful!");

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();

		} finally {
			System.out.println("Done!");
		}

	}

	private void setTextFields() {
		textFieldFirstName = new TextField();
		textFieldLastName = new TextField();
		textFieldAddress = new TextField();
		textFieldPostalCode = new TextField();
		textFieldProvince = new TextField();
		textFieldPhoneNumber = new TextField();
		textFieldGameTitle = new TextField();
	}

	private void setGridPanePlayer() {
		gridPanePlayer = new GridPane();
		gridPanePlayer.setAlignment(Pos.CENTER_LEFT);
		gridPanePlayer.setPadding(new Insets(11.5, 12.5, 13.5, 14.5));
		gridPanePlayer.setHgap(5.5);
		gridPanePlayer.setVgap(5.5);

		// Place nodes in the pane
		gridPanePlayer.add(new Label("Player to Update"), 0, 0);
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

		buttonUpdatePlayer = new Button("Update Player");
		gridPanePlayer.add(buttonUpdatePlayer, 1, 7);

	}

	private void setVBox() {
		vBox = new VBox(20);
		vBox.setPadding(new Insets(15, 5, 5, 5));
		vBox.getChildren().add(flowPaneForRadioButtons);
		vBox.getChildren().add(gridPanePlayer);
		vBox.getChildren().add(gridPaneGameLibrary);
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
		Label label = new Label("Select which info to update:");

		Button hiddenButton = new Button();
		hiddenButton.setPrefSize(Assignment5Constants.APP_WIDTH - 220, 25);
		hiddenButton.setVisible(false);

		flowPaneForRadioButtons = new FlowPane();
		flowPaneForRadioButtons.getChildren().addAll(label, hiddenButton, buttonRefresh, radioButtonPlayer,
				radioButtonGame);

	}

	private void setHandlers() {
		radioButtonPlayer.setOnAction(e -> {
			if (radioButtonPlayer.isSelected()) {
				vBox.getChildren().remove(gridPaneGame);
				vBox.getChildren().add(gridPanePlayer);
				vBox.getChildren().add(gridPaneGameLibrary);
			}
		});

		radioButtonGame.setOnAction(e -> {
			if (radioButtonGame.isSelected()) {
				vBox.getChildren().remove(gridPanePlayer);
				vBox.getChildren().remove(gridPaneGameLibrary);
				vBox.getChildren().add(gridPaneGame);
			}
		});

		buttonRefresh.setOnAction(e -> {
			if (comboBoxPlayer != null) {
				refreshComboBoxes();
				refreshEditables();
				populateComboBoxes();
				refreshTextFields();
			}

		});

		comboBoxPlayer.setOnAction(e -> {
			populateTextFieldsPlayer(comboBoxPlayer.getSelectionModel().getSelectedIndex());
			setGameLibraryObservableArrayLists();
		});

		comboBoxGame.setOnAction(e -> populateTextFieldsGame(comboBoxGame.getSelectionModel().getSelectedIndex()));
		buttonUpdateGame.setOnAction(e -> updateGame());
		buttonUpdatePlayer.setOnAction(e -> updatePlayer());
	}
	
	private void refreshTextFields() {
		textFieldFirstName.setText("");
		textFieldLastName.setText("");
		textFieldAddress.setText("");
		textFieldPostalCode.setText("");
		textFieldProvince.setText("");
		textFieldPhoneNumber.setText("");
		textFieldGameTitle.setText("");
		
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

	private void updatePlayer() {
		// Check if firstName and phoneNumber is null.
		if (textFieldFirstName.getText().trim().isEmpty() || textFieldPhoneNumber.getText().trim().isEmpty()) {
			new Alert(AlertType.INFORMATION, "Sorry, but First Name and Phone Number can't be empty.").show();
		} else {
			Connection con = null;
			PreparedStatement ps = null;

			// Insert to db.
			try {

				// JDBC driver name and database URL for Oracle
				// load the driver class
				Class.forName(Assignment5Constants.DB_DRIVER);

				// establish connection to database
				con = DriverManager.getConnection(Assignment5Constants.DB_CONNECTION_URL,
						Assignment5Constants.DB_USERNAME, Assignment5Constants.DB_PASSWORD);

				// create our java preparedstatement using a sql update query
				ps = con.prepareStatement(
						"UPDATE Player " + "SET first_name = ?, " + "last_name = ?, " + "address = ?, "
								+ "postal_code = ?, " + "province = ?, " + "phone_number = ? " + "WHERE player_id = ?");

				// set the preparedstatement parameters
				ps.setString(1, textFieldFirstName.getText());
				ps.setString(2, textFieldLastName.getText());
				ps.setString(3, textFieldAddress.getText());
				ps.setString(4, textFieldPostalCode.getText());
				ps.setString(5, textFieldProvince.getText());
				ps.setString(6, textFieldPhoneNumber.getText());
				ps.setInt(7, selectedPlayer.getPlayerId());

				// call executeUpdate to execute our sql update statement
				ps.executeUpdate();
				ps.close();

				new Alert(AlertType.INFORMATION, "Great! Player successfully updated.").show();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
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
	}

	private void updateGame() {
		// Check if firstName and phoneNumber is null.
		if (textFieldGameTitle.getText().trim().isEmpty()) {
			new Alert(AlertType.INFORMATION, "Sorry, but game Title can't be empty.").show();
		} else {
			// TODO
			// Insert to local array.
			// Game game = new Game(gameId, gameTitle)
			// observableListGameObjects.add(e)

			// Insert to db.
			Connection con = null;
			try {

				// JDBC driver name and database URL for Oracle
				String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
				// String DATABASE_URL = "jdbc:sqlserver://localhost:52412;";
				// String connectionUrl =
				// "jdbc:sqlserver://localhost:52412;databaseName=JavaAssignment5";
				// String username = "KateBren";
				// String password = "KateBren";

				// load the driver class
				Class.forName(driver);

				// establish connection to database
				con = DriverManager.getConnection(Assignment5Constants.DB_CONNECTION_URL,
						Assignment5Constants.DB_USERNAME, Assignment5Constants.DB_PASSWORD);

				// create our java preparedstatement using a sql update query
				PreparedStatement ps = con.prepareStatement("UPDATE Game SET game_title = ? WHERE game_Id = ?");

				// set the preparedstatement parameters
				ps.setString(1, textFieldGameTitle.getText());
				ps.setInt(2, selectedGame.getGameId());

				// call executeUpdate to execute our sql update statement
				ps.executeUpdate();
				ps.close();

				new Alert(AlertType.INFORMATION, "Great! Game successfully updated.").show();

			} catch (SQLException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
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
	}

}
