-- Task #1891
-- recipnet-0.9.1
-- removing orphaned records from search index tables


-- Delete orphaned rows from searchAtoms.
DELETE sa.* 
    FROM searchAtoms sa
    LEFT JOIN samples s on sa.sample_id=s.id
    WHERE s.id IS NULL;

-- Delete orphaned rows from searchLocalHoldings.
DELETE slh.* 
    FROM searchLocalHoldings slh
    LEFT JOIN samples s on slh.sample_id=s.id
    WHERE s.id IS NULL;

-- Delete orphaned rows from searchSpaceGroups.
DELETE ssg.* 
    FROM searchSpaceGroups ssg
    LEFT JOIN samples s on ssg.sample_id=s.id
    WHERE s.id IS NULL;

-- Delete orphaned rows from searchUnitCells.
DELETE suc.* 
    FROM searchUnitCells suc
    LEFT JOIN samples s on suc.sample_id=s.id
    WHERE s.id IS NULL;
