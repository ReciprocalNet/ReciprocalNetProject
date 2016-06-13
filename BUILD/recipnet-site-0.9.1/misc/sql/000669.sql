-- Task #669
-- recipnet-0.6.0
-- inventing deactivation dates for inactive users

UPDATE users SET inactiveDate=NOW()
    WHERE active=0 AND inactiveDate IS NULL;
