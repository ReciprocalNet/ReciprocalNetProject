-- Task #1596
-- recipnet-0.9.1
-- adding active and finalSeqNum fields to table sites
-- Add two fields to the sites table to support site deactivation tracking

ALTER TABLE sites ADD COLUMN 
(
	active 		bit		NOT NULL,
	finalSeqNum 	bigint
);

UPDATE sites
	SET active = 1;