-- Task #1480
-- recipnet-0.9.0
-- reassigning action codes for REVERT actions


-- Change all REVERTED (1200) actions where files were changed to
-- REVERTED_INCLUDING_FILES (1220) actions.
UPDATE sampleHistory LEFT JOIN repositoryFiles 
    ON sampleHistory.id=repositoryFiles.first_sampleHistory_id
    SET action=1220
    WHERE action=1200 
        AND repositoryFiles.id IS NOT NULL;
UPDATE sampleHistory LEFT JOIN repositoryFiles 
    ON sampleHistory.id=repositoryFiles.last_sampleHistory_id
    SET action=1220
    WHERE action=1200
        AND repositoryFiles.id IS NOT NULL; 

-- Change all remaining REVERTED (1200) actions to REVERTED_WITHOUT_FILES
-- (1210) actions.
UPDATE sampleHistory SET action=1210 WHERE action=1200;
