USE [JavaAssignment5]
GO

/****** Object:  Table [dbo].[PlayerAndGame]    Script Date: 12/8/2016 6:13:01 AM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[PlayerAndGame](
	[player_game_id] [int] IDENTITY(1,1) NOT NULL,
	[game_id] [int] NOT NULL,
	[player_id] [int] NOT NULL,
	[playing_date] [date] NULL,
	[score] [int] NULL,
 CONSTRAINT [PK_PlayerAndGame] PRIMARY KEY CLUSTERED 
(
	[player_game_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO

ALTER TABLE [dbo].[PlayerAndGame]  WITH CHECK ADD  CONSTRAINT [FK_PlayerAndGame_Game] FOREIGN KEY([game_id])
REFERENCES [dbo].[Game] ([game_Id])
GO

ALTER TABLE [dbo].[PlayerAndGame] CHECK CONSTRAINT [FK_PlayerAndGame_Game]
GO

ALTER TABLE [dbo].[PlayerAndGame]  WITH CHECK ADD  CONSTRAINT [FK_PlayerAndGame_Player] FOREIGN KEY([player_id])
REFERENCES [dbo].[Player] ([player_id])
GO

ALTER TABLE [dbo].[PlayerAndGame] CHECK CONSTRAINT [FK_PlayerAndGame_Player]
GO

