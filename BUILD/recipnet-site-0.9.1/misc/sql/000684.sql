-- Task #684
-- recipnet-0.6.0
-- adding date fields to table samples, summarizing existing dates
-- Add three fields to the samples table to allow more efficient date
--   support.  Doing this, and populating the new fields, will require the use
--   of four intermediate tables.

-- Create three temporary tables to hold intermediate values - the historyId's
-- of the first action, last action, and release action.
CREATE TABLE _tmp_firstActionIds
    SELECT s.id AS sample_id, MIN(sh.id) AS sampleHistory_id
        FROM samples s 
            LEFT JOIN sampleHistory sh ON s.id=sh.sample_id
        GROUP BY s.id;
CREATE TABLE _tmp_lastActionIds
    SELECT s.id AS sample_id, MAX(sh.id) AS sampleHistory_id
        FROM samples s 
            LEFT JOIN sampleHistory sh ON s.id=sh.sample_id
        GROUP BY s.id;
CREATE TABLE _tmp_releaseActionIds
    SELECT s.id AS sample_id, MAX(sh.id) AS sampleHistory_id
        FROM samples s LEFT JOIN sampleHistory sh ON s.id=sh.sample_id
	    WHERE sh.action=400
        GROUP BY s.id;

-- Build supporting indices on the three temporary tables to speed up the
-- next operation.
ALTER TABLE _tmp_firstActionIds
    ADD PRIMARY KEY (sample_id);
ALTER TABLE _tmp_lastActionIds
    ADD PRIMARY KEY (sample_id);
ALTER TABLE _tmp_releaseActionIds
    ADD PRIMARY KEY (sample_id);

-- Create a fourth temporary table that will replace the samples table in a
-- minute.
CREATE TABLE _tmp_samples
    SELECT s.*, 
     	shfa.date AS firstActionDate, 
	shla.date AS lastActionDate, 
	shra.date AS releaseActionDate
    FROM samples s
	LEFT JOIN _tmp_firstActionIds fa ON s.id=fa.sample_id
	    LEFT JOIN sampleHistory shfa ON fa.sampleHistory_id=shfa.id
	LEFT JOIN _tmp_lastActionIds la ON s.id=la.sample_id
	    LEFT JOIN sampleHistory shla ON la.sampleHistory_id=shla.id
	LEFT JOIN _tmp_releaseActionIds ra ON s.id=ra.sample_id
	    LEFT JOIN sampleHistory shra ON ra.sampleHistory_id=shra.id;

-- Replace the old samples table with the new one we just created.  Recreate
-- the indices just like the old one had them.
DROP TABLE samples;
ALTER TABLE _tmp_samples
    RENAME TO samples,
    ADD PRIMARY KEY (id),
    ADD INDEX (lab_id),
    ADD INDEX (provider_id),
    ADD INDEX (localLabId),
    ADD UNIQUE (lab_id, localLabId);

-- Drop the other three temporary tables.
DROP TABLE _tmp_firstActionIds;
DROP TABLE _tmp_lastActionIds;
DROP TABLE _tmp_releaseActionIds;
