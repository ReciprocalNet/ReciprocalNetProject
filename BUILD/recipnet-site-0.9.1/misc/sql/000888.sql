-- Task #888
-- recipnet-0.6.1
-- removing clientIp column from sampleHistory

ALTER TABLE sampleHistory DROP COLUMN clientIp;
