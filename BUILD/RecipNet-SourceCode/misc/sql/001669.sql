-- Task #1669
-- recipnet-0.9.0
-- adding 'description' column to repositoryFiles table

ALTER TABLE repositoryFiles
    ADD description varchar(200);
