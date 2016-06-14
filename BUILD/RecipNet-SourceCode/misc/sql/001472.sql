-- Task #1472
-- recipnet-0.9.0
-- adding 'original_sampleHistory_id' field to versioned metadata tables

ALTER TABLE sampleAnnotations
    ADD original_sampleHistory_id int NOT NULL;
UPDATE sampleAnnotations SET original_sampleHistory_id=first_sampleHistory_id;

ALTER TABLE sampleAttributes
    ADD original_sampleHistory_id int NOT NULL;
UPDATE sampleAttributes SET original_sampleHistory_id=first_sampleHistory_id;

ALTER TABLE sampleData
    ADD original_sampleHistory_id int NOT NULL;
UPDATE sampleData SET original_sampleHistory_id=first_sampleHistory_id;

ALTER TABLE repositoryFiles
    ADD original_sampleHistory_id int NOT NULL;
UPDATE repositoryFiles SET original_sampleHistory_id=first_sampleHistory_id;
