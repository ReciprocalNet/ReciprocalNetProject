-- Task #1914
-- recipnet-0.9.1
-- dropping redundant index on repository holdings table

ALTER TABLE repositoryHoldings
	DROP INDEX sample_id;

