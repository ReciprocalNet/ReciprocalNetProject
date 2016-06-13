-- Task #435
-- recipnet-0.5.1
-- repairing corrupt level data in sampleAnnotations

UPDATE sampleAnnotations SET level=NULL WHERE level=0;
