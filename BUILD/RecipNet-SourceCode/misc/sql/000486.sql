-- Task #486
-- recipnet-0.5.2
-- converting publication references to citations

UPDATE sampleAnnotations
	SET type=100
	WHERE type=200;

UPDATE sampleHistory
	SET action=600
	WHERE action=700;
