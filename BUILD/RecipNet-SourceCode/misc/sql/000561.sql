-- Task #561
-- recipnet-0.5.2
-- adding publicSeqNum and privateSeqNum fields to table sites
-- Add two fields to the sites table to support ISM receipt tracking

ALTER TABLE sites ADD COLUMN 
(
 	publicSeqNum bigint,	
	privateSeqNum bigint
);

