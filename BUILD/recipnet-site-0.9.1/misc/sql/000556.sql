-- Task #556
-- recipnet-0.5.2
-- creating table searchLocalHoldings

CREATE TABLE searchLocalHoldings
(
	sample_id			int	NOT NULL,
	repositoryHoldings_replicaLevel	int	NOT NULL,
	PRIMARY KEY (sample_id)
);
