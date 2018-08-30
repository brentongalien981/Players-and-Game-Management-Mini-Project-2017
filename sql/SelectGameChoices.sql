USE JavaAssignment5
GO

--SELECT Game.game_Id, Game.game_title, PlayerAndGame.player_id
--	FROM Game INNER JOIN PlayerAndGame ON Game.game_Id = PlayerAndGame.game_id
--		WHERE PlayerAndGame.player_id <> 3;

--SELECT PlayerAndGame.game_id
--	FROM PlayerAndGame
--		WHERE PlayerAndGame.player_id = 3;
--GO

SELECT *
	FROM Game
		WHERE Game.game_Id NOT IN (SELECT PlayerAndGame.game_id
										FROM PlayerAndGame
											WHERE PlayerAndGame.player_id = 3)
GO