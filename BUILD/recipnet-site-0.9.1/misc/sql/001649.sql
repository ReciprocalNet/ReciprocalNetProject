-- Task #1649
-- recipnet-0.9.0
-- creating table storedIsms

CREATE TABLE storedIsms
(
        id              bigint          NOT NULL AUTO_INCREMENT,
        sourceSiteId    int             NOT NULL,
        sourceSeqNum    bigint          NOT NULL,
        destSiteId      int,
	PRIMARY KEY (id),
        UNIQUE INDEX (sourceSiteId, sourceSeqNum),
        INDEX (sourceSiteId, destSiteId, sourceSeqNum)
);
