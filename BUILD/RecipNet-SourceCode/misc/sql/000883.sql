-- Task #883
-- recipnet-0.6.1
-- creating table repositoryFiles

CREATE TABLE repositoryFiles
(
	id			int		NOT NULL AUTO_INCREMENT,
	sample_id		int		NOT NULL,
	first_sampleHistory_id	int		NOT NULL,
	last_sampleHistory_id	int,
	fileName		varchar(255)	NOT NULL,
	cvsRevision		char(8)		NOT NULL,
	fileBytes		bigint		NOT NULL,
	PRIMARY KEY (id),
	INDEX (sample_id, first_sampleHistory_id, last_sampleHistory_id)
);
