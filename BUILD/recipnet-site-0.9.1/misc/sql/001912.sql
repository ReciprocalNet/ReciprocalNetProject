-- Task #1912
-- recipnet-0.9.1
-- creating table labTransfers

CREATE TABLE labTransfers
(
        id              	bigint          NOT NULL,
        lab_id			int		NOT NULL,
        previous_homeSite_id   	int             NOT NULL,
	new_homeSite_id		int		NOT NULL,
	complete		bit		NOT NULL,
	PRIMARY KEY (id)
);
