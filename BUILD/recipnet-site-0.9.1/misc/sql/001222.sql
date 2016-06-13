-- Task #1222
-- recipnet-0.9.0
-- adding column type to table searchAtoms, populating with EMPIRICAL_FORMULA

ALTER TABLE searchAtoms ADD type int NOT NULL;

UPDATE searchAtoms SET type=10100 WHERE type=0;
