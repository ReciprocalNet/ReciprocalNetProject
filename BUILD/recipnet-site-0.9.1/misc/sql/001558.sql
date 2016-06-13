-- Task #1558
-- recipnet-0.9.0
-- creating table searchUnitCells

CREATE TABLE searchUnitCells
(
	sample_id	int		NOT NULL,
	aprime		real		NOT NULL,
	bprime		real		NOT NULL,
	cprime		real		NOT NULL,
	vprime		real		NOT NULL,
        astarprime      real            NOT NULL,
        bstarprime      real            NOT NULL,
        cstarprime      real            NOT NULL,
	PRIMARY KEY (sample_id),
	INDEX (vprime, aprime, bprime, cprime, astarprime, bstarprime, cstarprime)
);
