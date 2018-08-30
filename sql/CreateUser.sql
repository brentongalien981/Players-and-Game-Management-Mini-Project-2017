USE [master]
GO

/* For security reasons the login is created disabled and with a random password. */
/****** Object:  Login [KateBren]    Script Date: 12/8/2016 6:11:34 AM ******/
CREATE LOGIN [KateBren] WITH PASSWORD=N'Z4IL6F0FC/shqML/6RzgLCYTnvYVLOwo9Rrosw9tsuE=', DEFAULT_DATABASE=[JavaAssignment5], DEFAULT_LANGUAGE=[us_english], CHECK_EXPIRATION=OFF, CHECK_POLICY=OFF
GO

ALTER LOGIN [KateBren] DISABLE
GO

