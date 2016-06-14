-- Task #960
-- recipnet-0.6.1
-- creating and populating tables statisticsSite and statisticsLabs

CREATE TABLE statisticsSite
(
        id                      int             NOT NULL,
	lastReset		datetime,
	recipnetdStartups	int		NOT NULL,
	recipnetdShutdowns	int		NOT NULL,
	uptime			int		NOT NULL,
	webappSessionsAuth	int		NOT NULL,
	webappSessionsUnauth	int		NOT NULL,
	oaiPmhQueries		int		NOT NULL,
	oaiPmhQuerySamples	int		NOT NULL,
        PRIMARY KEY(id)
);

CREATE TABLE statisticsLabs
(
	lab_id			int		NOT NULL,
	showSampleViews		int		NOT NULL,
	editSampleViews		int		NOT NULL,
	PRIMARY KEY(lab_id)
);

INSERT INTO statisticsSite VALUES (0, now(), 0, 0, 0, 0, 0, 0, 0);
