-- Task #146
-- recipnet-0.5.0
-- creating initial db structure

CREATE TABLE sites
(
	id		int		NOT NULL,
	name		varchar(64),
	shortName	varchar(20),
	baseUrl		varchar(128),
	repositoryUrl	varchar(128),
	publicKey	blob		NOT NULL,
	ts		bigint		NOT NULL,
	PRIMARY KEY (id)
);

CREATE TABLE labs
(
	id		int		NOT NULL,
	active		bit		NOT NULL,
	name		varchar(64),
	shortName	varchar(20),
	directoryName	varchar(20),
	homeUrl		varchar(128),
	defaultCopyrightNotice	text,
	homeSite_id	int		NOT NULL,	
	ts		bigint		NOT NULL,
	PRIMARY KEY (id),
	INDEX (homeSite_id)
);

CREATE TABLE providers
(
	id		int		NOT NULL,
	lab_id		int		NOT NULL,
	active		bit		NOT NULL,
	name		varchar(64),
	headContact	varchar(32),
	comments	text,
	ts		bigint		NOT NULL,
	PRIMARY KEY (id),
	INDEX (lab_id)
);

CREATE TABLE users
(
	id		int		NOT NULL,
	lab_id		int,
	provider_id	int,
	active		bit		NOT NULL,
	creationDate	datetime	NOT NULL,
	inactiveDate	datetime,
	fullname	varchar(32)	NOT NULL,
	username	char(8)		NOT NULL,
	password	char(32)	NOT NULL,
	globalAccessLevel	int,
	ts		bigint		NOT NULL,
	PRIMARY KEY (id),
	INDEX (lab_id),
	INDEX (provider_id),
	INDEX (username)
);

CREATE TABLE samples
(
	id		int		NOT NULL,
	lab_id		int		NOT NULL,
	provider_id	int		NOT NULL,
	localLabId	varchar(32)     NOT NULL,
	status		int		NOT NULL,
	current_sampleHistory_id int	NOT NULL,
	PRIMARY KEY (id),
	INDEX (lab_id),
	INDEX (provider_id),
	INDEX (localLabId),
	INDEX (lab_id, localLabId)
);

CREATE TABLE sampleAcls
(
	id		int		NOT NULL AUTO_INCREMENT,
	sample_id	int		NOT NULL,
	user_id		int		NOT NULL,
	accessLevel	int		NOT NULL,
	PRIMARY KEY (id),
	INDEX (sample_id),
	INDEX (user_id),
	INDEX (sample_id, user_id)
);

CREATE TABLE repositoryHoldings
(
	id		int		NOT NULL AUTO_INCREMENT,
	sample_id	int		NOT NULL,
	site_id		int		NOT NULL,
	replicaLevel	int		NOT NULL,
	urlPath		varchar(128),
	PRIMARY KEY (id),
	UNIQUE INDEX (sample_id),
	INDEX (site_id),
	INDEX (sample_id, site_id)
);

CREATE TABLE localTracking
(
	sample_id	int		NOT NULL,		
	PRIMARY KEY (sample_id)
);

CREATE TABLE sampleHistory
(
	id		int		NOT NULL AUTO_INCREMENT,
	sample_id	int		NOT NULL,
	action		int		NOT NULL,
	newStatus	int		NOT NULL,
	date		datetime	NOT NULL,
	user_id		int,
	clientIp	char(16),
	comments	text,
	PRIMARY KEY (id),
	INDEX (sample_id),
	INDEX (id, sample_id)
);

CREATE TABLE sampleData
(
	first_sampleHistory_id	int	NOT NULL,
	last_sampleHistory_id	int,
	sample_id		int	NOT NULL,
	a		real,
	b		real,
	c		real,
	alpha		real,
	beta		real,
	gamma		real,
	spgp		varchar(13),
	dcalc		real,
	color		varchar(20),
	z		int,
	t		real,
	v		real,
	rf		real,
	rwf		real,
	rf2		real,
	rwf2		real,
	goof		real,
	summary		varchar(80),
	PRIMARY KEY (first_sampleHistory_id),
	INDEX (sample_id),
	INDEX (sample_id, first_sampleHistory_id, last_sampleHistory_id)
);

CREATE TABLE sampleAtoms
(
	id		int		NOT NULL AUTO_INCREMENT,
	first_sampleHistory_id int	NOT NULL,
	last_sampleHistory_id int,
	sample_id	int		NOT NULL, 
	element		char(2)		NOT NULL,
	count		int		NOT NULL,
	PRIMARY KEY (id),
	INDEX (sample_id),
	INDEX (sample_id, first_sampleHistory_id, last_sampleHistory_id)
);

CREATE TABLE sampleAttributes
(
	id		int		NOT NULL AUTO_INCREMENT,
	first_sampleHistory_id int	NOT NULL,
	last_sampleHistory_id int,
	sample_id	int		NOT NULL,
	type		int		NOT NULL,
	value		varchar(128),
	PRIMARY KEY (id),
	INDEX (sample_id),
	INDEX (sample_id, first_sampleHistory_id, last_sampleHistory_id),
	INDEX (type),
	INDEX (value)
);

CREATE TABLE sampleAnnotations
(
	id		int		NOT NULL AUTO_INCREMENT,
	first_sampleHistory_id int	NOT NULL,
	last_sampleHistory_id int,
	sample_id	int		NOT NULL,
	level		int,
	type		int		NOT NULL,
	value		text,
	referenceSample	int,
	PRIMARY KEY (id),
	INDEX (sample_id),
	INDEX (sample_id, first_sampleHistory_id, last_sampleHistory_id),
	INDEX (type)
);

CREATE TABLE sampleIdBlocks
(
	id		int		NOT NULL,
	site_id		int		NOT NULL,
	status		int		NOT NULL,
	proposalDate	datetime	NOT NULL,
	claimDate	datetime,
	transferDate	datetime,
	transferTargetSiteId int,
	PRIMARY KEY (id),
	INDEX (site_id),
	INDEX (status)
);
	
