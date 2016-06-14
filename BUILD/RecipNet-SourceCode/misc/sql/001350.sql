-- Task #1350
-- recipnet-0.9.0
-- creating table searchSpaceGroups

CREATE TABLE searchSpaceGroups
(
	sample_id	int             NOT NULL,
        canonicalsymbol char(13)        NOT NULL,
	PRIMARY KEY (sample_id),
	INDEX (canonicalsymbol)
);
