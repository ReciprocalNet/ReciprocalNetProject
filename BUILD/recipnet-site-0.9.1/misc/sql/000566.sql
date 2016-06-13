-- Task #566
-- recipnet-0.5.2
-- adding column approvalCount to table sampleIdBlocks

ALTER TABLE sampleIdBlocks
	ADD COLUMN approvalCount int;
