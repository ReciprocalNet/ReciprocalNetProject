-- Task #1449
-- recipnet-0.9.0
-- adding 'preferences' field to table users
-- Add a field to the users table to allow customization of certain properties
--   in the reciprocal web application.

ALTER TABLE users
    ADD preferences bigint(20);
