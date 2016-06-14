-- Task #1880
-- recipnet-0.9.0
-- reassigning action codes for CITATION_ADDED actions


-- Change all MODIFIED_TEXT_FIELDS actions (500) where 
-- CITATION_OF_A_PUBLICATION annotations (100) were deleted to 
-- CITATION_ADDED_CORRECTED actions (610).
UPDATE sampleHistory LEFT JOIN sampleAnnotations
    ON sampleHistory.id=sampleAnnotations.last_sampleHistory_id
    SET sampleHistory.action=610
    WHERE sampleHistory.action=500 
        AND sampleAnnotations.type=100;

-- Change all remaining MODIFIED_TEXT_FIELDS actions (500) where 
-- CITATION_OF_A_PUBLICATION annotations (100) were created to
-- CITATION_ADDED actions (600).
UPDATE sampleHistory LEFT JOIN sampleAnnotations
    ON sampleHistory.id=sampleAnnotations.first_sampleHistory_id
    SET sampleHistory.action=600
    WHERE sampleHistory.action=500
        AND sampleAnnotations.type=100;
