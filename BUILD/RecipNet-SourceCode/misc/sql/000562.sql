-- Task #562
-- recipnet-0.5.2
-- creating table version

CREATE TABLE version 
(
	buildname 	varchar(32),
	highestTask	int
);

INSERT INTO version 
       	VALUES ('recipnet-0.5.1-41', 456);
