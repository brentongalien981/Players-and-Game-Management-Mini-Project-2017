USE [JavaAssignment5]
GO

/****** Object:  Table [dbo].[Game]    Script Date: 12/7/2016 6:01:54 PM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[Game](
	[game_Id] [int] IDENTITY(1,1) NOT NULL,
	[game_title] [nvarchar](50) NOT NULL,
 CONSTRAINT [PK_Game] PRIMARY KEY CLUSTERED 
(
	[game_Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
CREATE TABLE [dbo].[Player](
	[player_id] [int] IDENTITY(1,1) NOT NULL,
	[first_name] [nvarchar](50) NOT NULL,
	[last_name] [nvarchar](50),
	[address] [nvarchar](50),
	[postal_code] [char](7),
	[province] [char](2),
	[phone_number] [nvarchar](12) NOT NULL,
 CONSTRAINT [PK_Player] PRIMARY KEY CLUSTERED 
(
	[player_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
CREATE TABLE [dbo].[PlayerAndGame](
	[player_game_id] [int] IDENTITY(1,1) NOT NULL,
	[game_id] [int] NOT NULL,
	[player_id] [int] NOT NULL,
	[playing_date] [date],
	[score] [int],
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



