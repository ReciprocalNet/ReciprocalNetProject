-- Task #1853
-- recipnet-0.9.0
-- correcting 'referenceSample' column on older annotations

UPDATE sampleAnnotations
    SET referenceSample=NULL
    WHERE referenceSample=0;
