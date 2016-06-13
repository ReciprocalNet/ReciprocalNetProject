-- Task #1661
-- recipnet-0.9.0
-- reclassifying 'provider_id' as a modifiable field

-- rename provider_id to current_provider_id
ALTER TABLE samples CHANGE provider_id current_provider_id int NOT NULL;

-- create a copy of the sampleData table that includes a column of provider_id
-- values equal to the current_provider_id values for the samples table
CREATE TABLE _tmp_sampleData
    SELECT sampleData.*, samples.current_provider_id AS provider_id
    FROM sampleData LEFT JOIN samples ON samples.id=sampleData.sample_id;

-- set characteristics of the new column
ALTER TABLE _tmp_sampleData CHANGE provider_id provider_id int NOT NULL;
ALTER TABLE _tmp_sampleData ADD PRIMARY KEY (first_sampleHistory_id);
ALTER TABLE _tmp_sampleData ADD INDEX (sample_id);
ALTER TABLE _tmp_sampleData ADD INDEX (sample_id,
    first_sampleHistory_id, last_sampleHistory_id);

-- drop the old sampleData table
DROP TABLE sampleData;

-- substitute the new sampleData table
ALTER TABLE _tmp_sampleData RENAME TO sampleData;
