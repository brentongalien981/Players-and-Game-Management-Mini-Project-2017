USE JavaAssignment5
GO

SELECT Player.player_id, Player.first_name, Game.game_title, PlayerAndGame.playing_date, PlayerAndGame.score
	FROM Player INNER JOIN PlayerAndGame ON Player.player_id = PlayerAndGame.player_id
	INNER JOIN Game ON PlayerAndGame.game_id = Game.game_Id
		WHERE Player.player_id = 2
GO

