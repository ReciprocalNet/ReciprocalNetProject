-- Task #736
-- recipnet-0.6.0
-- cleaning blank data items from sampleData, -Attributes, and -Annotations

UPDATE sampleData SET spgp=NULL WHERE spgp='';
UPDATE sampleData SET color=NULL WHERE color='';
UPDATE sampleData SET summary=NULL WHERE summary='';
DELETE FROM sampleAttributes WHERE value='';
DELETE FROM sampleAnnotations WHERE value='';
