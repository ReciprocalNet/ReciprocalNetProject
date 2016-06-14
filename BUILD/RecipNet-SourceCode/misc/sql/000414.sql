-- Task #414
-- recipnet-0.5.1
-- creating table searchAtoms

CREATE TABLE searchAtoms
(
      	id		int 		NOT NULL AUTO_INCREMENT,
	sample_id	int		NOT NULL,
	element		char(2)		NOT NULL,
	count		real		NOT NULL,
	PRIMARY KEY (id),
	INDEX (sample_id),
	INDEX (element, sample_id)
);
