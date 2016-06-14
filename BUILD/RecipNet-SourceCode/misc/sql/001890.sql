-- Task #1890
-- recipnet-0.9.1
-- removing orphaned sample id blocks and repository holdings


-- Delete orphaned rows from the sampleIdBlocks table.  This might be a
-- dangerous thing to do, as it introduces a risk that two sites may propose
-- the same sample id block, except...  In the production Reciprocal Net Site
-- Network, immediately following release 0.9.1 the Coordinator will take
-- action to recover from any database inconsistency our deletion here may
-- introduce.
DELETE sib.* 
    FROM sampleIdBlocks sib
    LEFT JOIN sites s on sib.site_id=s.id
    WHERE s.id IS NULL;

-- Delete orphaned rows from repositoryHoldings.
DELETE rh.* 
    FROM repositoryHoldings rh
    LEFT JOIN sites s on rh.site_id=s.id
    WHERE s.id IS NULL;
