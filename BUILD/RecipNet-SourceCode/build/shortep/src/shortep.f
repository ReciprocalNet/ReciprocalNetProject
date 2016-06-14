CCCCC
C
      program shortep
C 
C     SHORTEP IS THE IUMSC'S DERIVATIVE OF THE ORTEP PROGRAM FOR
C     MANIPULATION OF IUMSC STANDARD DATA TAPES AND CALCULATION OF
C     DISTANCES, ANGLES, ETC.
C
C     Based on ORTEP II by Carroll K. Johnson, 
C                          Oak Ridge National Laboratory
C 
C     March 1989 version for IBM PC by J C Huffman
C 
C     VERSION INCLUDES FULL SYMMETRY AND "BUILD" AND "ANALYZE"
C     COMMANDS.
C 
C     DIMENSIONED FOR 999 ATOMS  INPUT (CHEM, P, PA, ETC BELOW)
C             AND FOR 999 ATOMS OUTPUT (ATOMS ARRAY BELOW)
C 
C     W. E. STREIB - CLEANUP AND DEBUGGING
C                  - MOSTLY TO MAKE ALL ADC USAGE DOUBLE PRECISION
C                  - APRIL,1996
C 
C 
C     J C Bollinger, March-July 1998:
C 
C     Further cleanup, debugging, and new functionality:
C     -  Added 1250/CART command for generating Cartesian coordinate
C        files with tranformed symmetry operations and lattice vectors.
C     -  Modified subroutine prelim so that it displays the labels of
C        any NPD atoms as it reads them in.
C     -  Fixed a bug in prelim that caused it to sometimes misinterpret
C        NPD type 0 thermal parameters as type 6 sphere parameters with
C        specified axis orientation; the bug previously caused prelim
C        to sometimes not notice NPD atoms.  It looks like the bug may
C        have been inherited from ORTEP.
C     -  Modified subroutine STOR so that 411 instructions work when
C        the no duplication flag is on.
C     -  Replaced the sort algorithm with a somewhat faster one.
C     -  Generally removed potential portability problems.
C     -  Broke up the blank common into topical chunks; eliminated
C        unused variables; added explicit declarations for all
C        variables;  sorted all modules and variables in alphabetical
C        order by name; restructured much of the code according to
C        a more modular style; removed all instances of subprograms
C        returning results in common blocks, except for in subroutine
C        prelim which intializes the common blocks.
C     -  Added CIF-reading capability
C     -  Increased the size of the output file name for 1200-series
C        instructions
C     -  Added partial support for 'atom 0' at the origin.
C     -  Modified intruction 500/RESTART so that it now works without
C        overwriting the original input file.  In conjunction with
C        that change, also eliminated the generation of 'XTEL.BAK'.
C     -  Found and corrected a small bug which may have been
C        responsible for the somewhat erratic behavior following a
C        BUILD instruction.
C     -  Removed the UNIQUE/1000 instruction, which was not implemented
C        anyway.  (It just pointed to subroutine analyze).
C     -  Added support for atom resequencing via instruction 540.
C     -  Made 500-series instructions and 1200-series instructions
C        parallel.
C     -  Rewrote symmetry labelling feature for SDT output.
C     -  Modified symmetry card functions (600 series)
C        600 and 609 now by default append the new card to the list
C        if space is available or modify an existing card if a valid
C        card number is supplied.  601 now accepts a minimum and/or
C        maximum for the card numbers to display.  Also fixed a bug
C        in the format statement used for 601 output.
C      - Added option to save the scratch SDT at normal program
C        termination.  This provides an alternative for users used
C        to having the 500-series instruction overwrite the input
C        tape.
C      - Installed 704 instruction to convert selected atoms from
C        anisotropic to isotropic.
C      - Installed 415 instruction to eliminate close contacts from
C        among a specified range of atoms.
C      - fixed tpcnvt by removing another alternative which could cause
C        an NPD atom to not be flagged.
C      - Made the 41x instructions (1<x<6) parallel to the 40x
C        instructions; the 41x instructions remove atoms instead of
C        adding them.  Fixed the 415 instruction so that it can use
C        subroutine SEARC directly.  Caused all atom additions and
C        deletions performed in subroutine SEARC to list the "bonded"
C        atoms and "bond" distance.
C      - Atom addition/deletion in subroutine STOR is now controlled by
C        the ain(27) flag instead of by the instruction number.
C      - Cleaned up SEARC to remove JCH's (?) hacks to make the 407
C        instruction operate by emulating the 101 instruction.
C        Modified CLOSIT so that 407 now works by loading the atoms list
C        and then executing a series of 415 instructions.
C      - Implemented 901 instruction to change element symbols on the
C        input atom labels.
C      - Modified 41n instructions so that self-self distances are
C        ignored.
C      - Changed all ADCs and ADC calculations from double precision to
C        integer.  This will cause problems only on systems whose
C        default integer size is less than four bytes.
C      - Fixed a bug I introduced earlier which caused build to crash
C        upon trying to read in parameters for unrecognized atom types.
C        Rewrote inline so as to avoid internal bounds violations.
C      - Fixed instruction 201 output so that the leading digit of
C        three-digit atom numbers is no longer suppressed in column 1.
C      - added explicit close instructions for the scratch files so
C        that they are properly deleted.
C      - Fixed 407 instruction so that it eliminates symmetry copies
C        of short-bonded atoms from the list in addition to the actual
C        short-bonded atom.
C      - Added 416 instruction which is identical to 415 except that it
C        eliminates all symmetry copies of any atom it eliminates at all
C      - Fixed minor bug with negative isotropic thermal parameters.
C      - Applied a relatively arbitrary adjustment to the atomic radii
C        of Ba and K in subroutine cart.  Radii were increased so as to
C        improve automatic bond generation agreement with local usage
C        for Ba and K compounds.
C      - Ported to Digital Visual Fortran.  Modifications included only
C        changing the library calls in subroutine datime and modifying
C        the open statements for "unnamed" scratch files.
C 
C 
C     J.C. Bollinger, October 1998
C 
C      - Minor debugging
C      - Total rewrite of the help system
C      - Added new trip points to access the help automatically
C      - Added a kludge to subroutine saveit which makes it correctly
C        handle unquoted symmetry cards which begin with a number or
C        minus sign.  (Such fields appear to violate the CIF spec.)
C 
C     J.C. Bollinger, November 1998
C 
C      - Extended the size of the SPGTIT variable from 8 to 12
C        characters to handle the occasional long space group name.
C 
C     J.C. Bollinger, February 1999
C 
C      - Increased array sizes from 999 to 4000.
C 
C     J.C. Bollinger, May 2000
C 
C      - Minor code modifications for compatability with g77
C 
C     J. C. Bollinger, August 2000
C 
C      - Changed format for startup banner to no longer use MS-DOS
C        line-drawing characters
C      - Added support for better error reporting on CIF input,
C        including line number tracking
C 
C     J. C. Bollinger, November 2001
C 
C      - Rearranged some of the code for better internal consistency
C        with no change in functionality
C      - Implemented a new sorting option for 500- and 1200-series
C        instructions. Sort code 2 sorts all hydrogen atoms to the end
C        of the SDT, but preserves the same relative order as option
C        1 within the non-hydrogen block and the hydrogen block
C      - Added an "HSORT" mnemonic command for "501 2"
C      - Rewrote the I/O portion of the CIF-handling routines so as to
C        handle all legal CIF line termination semantics and to reject
C        CIFs containing illegal characters.  This required reading
C        the CIF in direct access mode, and will fail on systems where
C        the unit of the direct access record length is not one byte.
C      - Modified subroutine analyze to sort the reported and found
C        formulae into the Hill form for comparison and display.  Neither
C        the comparison nor the display will now depend on the assumed
C        or actual order of atoms in the input empirical formula
C
C     J. C. Bollinger, December 2002
C
C     - Corrected multiple initialization of the element data common blocks
C     - Added references to the element data common blocks to the main
C       program lest the data be dropped by an uncooperative processor
C     - Fixed a bug in the CRT generator that caused it to miss H-X bonds
C       when the H atom was listed before the X atom on the input file
C 
C     Programming note: AINs 21-24 are cleared before every instruction
C     but can never be set as a result of instruction input.  AIN(21)
C     should not be disturbed, as its zero value may occasionally be
C     necessary as a delimiter.  The others, however, can be used as
C     temporary flags; ain(24) has already been adopted for this
C     purpose with the 400-series instructions.
C 
CCCCC
  
CCCCC 
C     For Digital / Compaq / Intel Visual Fortran uncomment the
C     following two statements and compile with /assume:byterecl 
C
C     use dfport, only: iargc
C     use dflib, only: getarg
C
CCCCC
      real arad(4000),radmax
      integer ntype(4000),i405,ipoly
      common /adat/ arad,radmax,i405,ipoly,ntype
  
      real atoms(3,4000)
      integer atomid(4000),latm
      common /atoml/ atomid,atoms,latm
  
      character*2 radc(110)
      common /ceinfo/ radc
  
      real a(9),aa(3,3),bb(3,3)
      common /cell/ a,aa,bb
  
      character*8 chem(4000)
      character*10 sdttit(8)
      character*12 spgtit
      common /chars/ chem,sdttit,spgtit
  
      integer lineno
      common /cif/ lineno
  
      real scalef,extinf,bx(7)
      integer isdtis(48)
      common /ends/ scalef,extinf,isdtis,bx
  
      integer ng
      common /fault/ ng
  
      integer nelement, iatn(110)
      common /ieinfo/ nelement, iatn

      real ev(3,4000),p(3,4000),pa(3,3,4000)
      integer natom
      common /inputl/ ev,natom,p,pa
  
      double precision ain(30)
      integer nj,nj2
      common /instr/ ain,nj,nj2
  
      integer in,lin,lout
      common /lun/ in,lin,lout
  
      real aarev(3,3),refv(3,3)
      common /orient/ aarev,refv
  
      common /reinfo/ radi
      real radi(110)
C 
      real xpop(4000),xtemp(4000)
      integer ibull1(4000),ibull2(4000)
      common /sdtat/ ibull1,ibull2,xpop,xtemp
  
      real fs(3,3,192),ts(3,192)
      integer nsym
      common /symm/ fs,ts,nsym

      integer i100,i1000,i10k,i100k,i55501
      common /icon/ i100,i1000,i10k,i100k,i55501
C 
      real q(3,3),rms(3),t1
      integer i,iatom1,iatom2,ierrl,isym,j,k,m500,n2,nf,nl,numhyd
      logical here1,lopen
      character*80 ffname
      character*12 itime
      character*10 title(8)
      character*8 idate,labels(20)
      character*1 goon,line(80)
C 
      in=5
      lin=20
      lout=6
      call datime (idate,itime)
      write (lout,150) idate,itime
      write (lout,'(//)')
C 
      if (iargc().gt.0) then
         call getarg (1,ffname)
      else
         write (lout,'(''Name of file to open? ''$)')
         read (in,'(a)') ffname
      end if
      open (lin,file=ffname,status='OLD',err=145)
      open (23,form='FORMATTED',status='SCRATCH')
      open (24,form='FORMATTED',status='SCRATCH')
      m500=0
C 
C     START OR RESTART, E.G., AFTER A 500 INSTRUCTION REWRITES TAPE20
C 
    5 call prime
      m500=m500+1
C 
C     READ JOB TITLE CARD
C     (NO MORE THAN ONE LEADING BLANK CARD ALLOWED)
C 
      rewind lin
      rewind 23
      read (lin,155) title
      if (title(1).eq.'          ') read (lin,155) title
      if (title(1)(1:3).eq.'MSC') then
         write (23,155) title(1),title(2),title(3),title(4),idate,itime
         do i=1,8
            sdttit(i)=title(i)
         end do
         call prelimsdt (numhyd)
      else
         sdttit(5)=idate
         read (itime,'(2a8)') sdttit(6),sdttit(7)
         call prelimcif (numhyd)
         if (ng.eq.20) then
            write (lout,'(''No more data blocks'')')
            stop
         else if (ng.eq.21) then
            write (lout,'(''CIF parse error at line '',i5)') lineno
            stop
         else if (ng.eq.22) then
            write (lout,
     1       '(''CIF syntax error (data outside data block) on line '',
     2        i5)') lineno
            stop
         else if (ng.eq.23) then
            write (lout,
     1       '(''CIF syntax error (unexpected datum value) on line '',
     2       i5)') lineno
            stop
         else if (ng.eq.24) then
            write (lout,
     1       '(''CIF syntax error (illegal character) on line '',i5)')
     2       lineno
            stop
         else if (ng.eq.25) then
            write (lout,'(''Internal I/O error handling the CIF'')')
            stop
         else if (ng.ne.0) then
            write (lout,'(''Internal error at CIF line '',i5)') lineno
            stop
         end if
      end if
C 
C     PREPARE TO READ A NEW COMMAND: ZERO AIN ARRAY (EXCEPT FOR FLAGS IN
C       AIN(25-30)) AND RESET INSTRUCTION CODES
C 
   10 do j=1,24
         ain(j)=0d0
      end do
      nj=0
      nj2=0
C 
C     READ NEW INSTRUCTION CARD (-1 OR <CR> WILL TERMINATE)
C 
      write (lout,160)
      read (in,165) line
      nl=20
      n2=20
      call inline (line,nl,n2,ain,labels,ierrl)
      if (ierrl.eq.1) then
C 
C A blank input line no longer exits the program
C 
         write (lout,'('' Use instruction -1 (or "end") to exit'')')
         go to 10
      else if (ierrl.gt.1) then
C 
C Erroneous input
C 
         write (lout,170)
         go to 10
      end if
C 
C Determine instruction category and number
C 
      nf=1
      if (nl.gt.0) then
C 
C interpret command keywords
C 
         nf=0
         if (labels(1).eq.'ADDATOM ') then
            nf=401
         else if (labels(1).eq.'ANALYZE ') then
            nf=1000
         else if (labels(1).eq.'ANGLE   ') then
            nf=102
         else if (labels(1).eq.'ATOMS   ') then
            nf=200
         else if (labels(1).eq.'BOX     ') then
            nf=404
         else if (labels(1).eq.'BOXIN   ') then
            nf=404
         else if (labels(1).eq.'BUILD   ') then
            nf=1050
         else if (labels(1).eq.'CART    ') then
            nf=1250
         else if (labels(1).eq.'CART2   ') then
            nf=1251
         else if (labels(1).eq.'CLEANUP ') then
            nf=407
         else if (labels(1).eq.'CLEAR   ') then
            nf=410
         else if (labels(1).eq.'CSSR    ') then
            nf=1260
         else if (labels(1).eq.'DISTANCE') then
            nf=101
         else if (labels(1).eq.'DUPOK   ') then
            nf=409
         else if (labels(1).eq.'END     ') then
            nf=-1
         else if (labels(1).eq.'EXPAND  ') then
            nf=405
         else if (labels(1).eq.'HELP    ') then
            nf=900
         else if (labels(1).eq.'HSORT   ') then
            nf=500
            ain(1)=2d0
         else if (labels(1).eq.'LIST    ') then
            nf=200
         else if (labels(1).eq.'LISTRMS ') then
            nf=103
         else if (labels(1).eq.'NEWSDT  ') then
            nf=1201
         else if (labels(1).eq.'NEWSDTX ') then
            nf=1202
         else if (labels(1).eq.'NEWSYM  ') then
            nf=600
         else if (labels(1).eq.'NODUP   ') then
            nf=408
         else if (labels(1).eq.'OFFADC  ') then
            nf=300
         else if (labels(1).eq.'ONADC   ') then
            nf=301
         else if (labels(1).eq.'QUIT    ') then
            nf=-1
         else if (labels(1).eq.'REMOVE  ') then
            nf=411
         else if (labels(1).eq.'RESTART ') then
            nf=500
         else if (labels(1).eq.'VER     ') then
            write (lout,'('' SHORTEP     Version 000831'')')
            go to 10
         end if
      end if
      if (nf.eq.0) nf=900
C 
C     IF NF=1 THEN INPUT MUST HAVE BEGUN WITH A NUMERIC COMMAND; extract
C     the command number from the list and shift the list
C 
      if (nf.eq.1) then
         nf=idnint(ain(1))
         do i=1,n2
            ain(i)=ain(i+1)
         end do
         n2=n2-1
      end if
C 
C     AT THIS POINT THE NUMERIC COMMAND IS IN NF AND ANY ADDITIONAL
C     PARAMETERS, WHICH MUST BE NUMERIC, ARE IN AIN(1) THRU AIN(N2)
C 
C     Perform a normal exit for any negative command number
C 
      if (nf.lt.0) go to 140
C 
C     break the command code into category and command number
C 
      nj=nf/100
      nj2=mod(nf,100)
C 
C     BRANCH TABLE FOR FUNCTION TYPES
C 
      go to (20,25,30,35,75,95,100,15,105,110,15,115),nj
C 
C     (NJ .EQ. 8 or default)
C 
   15 print 185, nf
      call help (0d0)
      go to 10
C 
C     STRUCTURE ANALYSIS FUNCTIONS (NJ .EQ. 1)
C 
   20 if (nj2.le.2) then
         call searc
      else if (nj2.eq.3) then
C 
C     ANISOTROPIC TEMP FACTOR OUTPUT
C 
         write (lout,210)
         iatom1=idnint(ain(1))
         iatom2=idnint(ain(2))
         if (iatom1.eq.0) iatom1=1
         if (iatom2.eq.0) iatom2=natom
         do i=iatom1,iatom2
            call paxes (i*i100k+i55501,rms,q,-3)
            write (lout,215) chem(i),rms(1),rms(2),rms(3)
         end do
      else
         print 185, nf
         call help (1d2)
      end if
      go to 10
C 
C     ATOM LIST (NJ .EQ. 2)
C 
   25 j=idnint(ain(1))
      k=idnint(ain(2))
      if (j.le.0) j=1
      if (k.lt.0) then
         k=-k
      else if (k.eq.0) then
         k=natom
      end if
      write (lout,205) (i,chem(i),i=j,k)
      go to 10
C 
C     ADC printing flag (NJ .EQ. 3)
C 
   30 if (nf.eq.301) then
         ain(30)=1d0
         write (lout,'('' ADC indicator is on...'')')
      else if (nf.eq.300) then
         ain(30)=0d0
         write (lout,'('' ADC indicator is off...'')')
      else
         print 185, nf
         call help (3d2)
      end if
      go to 10
C 
C     ATOM LIST FUNCTIONS (NJ .EQ. 4)
C 
   35 if (nj2.lt.1.or.nj2.gt.16) then
         call help (4d2)
         go to 10
      end if
      go to (55,40,10,55,55,55,65,45,50,60),mod(nj2-1,10)+1
C 
C     402 / 412
C 
   40 write (lout,220) natom,natom-numhyd,latm
      go to 10
C 
C     408 (turn on NODUP flag)
C 
   45 ain(28)=1d0
      write (lout,225)
      go to 10
C 
C     409 (turn off NODUP flag)
C 
   50 ain(28)=0d0
      write (lout,230)
      go to 10
C 
C     401, 404, 405, 406, 411, 414, 415, 416 (handled by f400)
C 
   55 if (nj2.gt.10) then
         ain(27)=1d0
         if (nj2.eq.16) then
            nj2=15
            ain(24)=1d0
         else
            ain(24)=-1d0
         end if
      else
         ain(27)=0d0
      end if
      call f400
      ain(27)=0d0
      go to 70
C 
C     410 (clear atoms list)
C 
   60 latm=0
      go to 10
C 
C     407 (remove close contacts)
C 
   65 call closit
   70 if (latm.gt.0) write (lout,235) (atomid(i),i=1,latm)
      go to 10
C 
C     SHORTEP RESTART (NJ .EQ. 5)
C 
   75 rewind 24
      call ppunch (nf)
C 
c     if no atoms found, return to instructions!
C 
      if (nf.eq.99) go to 10
C 
c     otherwise write a new tape20 and loop back
C 
      close (lin)
      open (lin,form='formatted',status='scratch')
      rewind 23
      rewind 24
   80 read (23,155,end=85) title
      write (lin,155) title
      go to 80
   85 read (24,155,end=90) title
      write (lin,155) title     
C     write (*,155) title     
      go to 85
   90 write (lin,'(''         1000000'')')
      write (lin,'(f10.4)') scalef
      write (lin,'(e10.4)') extinf
      go to 5
C 
C     symmetry card operations (NJ .EQ. 6)
C 
   95 if (nj2.eq.0) then
C 
C     simple symmetry card input
C 
         isym=idnint(ain(1))
         if (isym.eq.0) then
            if (nsym.ge.192) then
               write (lout,
     1          '('' Sorry, no space for new symmetry cards.'')')
               go to 10
            end if
            nsym=nsym+1
            isym=nsym
         else if (isym.gt.nsym.or.isym.lt.0) then
            write (lout,'('' The range of current card numbers is 1 to''
     1             ,i4)') nsym
            go to 10
         end if
         n2=0
         ierrl=0
         do while (n2.ne.6.and.ierrl.eq.0)
            write (lout,190) isym
            read (in,165) line
            nl=20
            n2=20
            call inline (line,nl,n2,ain,labels,ierrl)
            if (ierrl.eq.1) ierrl=0
         end do
         print 195
         ts(1,isym)=sngl(ain(1))
         fs(1,1,isym)=sngl(ain(2))
         ts(2,isym)=sngl(ain(3))
         fs(2,2,isym)=sngl(ain(4))
         ts(3,isym)=sngl(ain(5))
         fs(3,3,isym)=sngl(ain(6))
      else if (nj2.eq.1) then
C 
C     symmetry card output
C 
         write (lout,'(/,'' The requested symmetry cards are:'',/)')
         isym=iabs(idnint(ain(2)))
         if (isym.eq.0) then
            isym=nsym
         else
            isym=min(isym,nsym)
         end if
         do i=max(idnint(ain(1)),1),isym
            write (lout,200) i,(ts(j,i),(int(fs(k,j,i)),k=1,3),j=1,3)
         end do
      else if (nj2.eq.9) then
C 
C     complex symmetry card input
C 
         isym=idnint(ain(1))
         if (isym.eq.0) then
            if (nsym.ge.192) then
               write (lout,
     1          '('' Sorry, no space for new symmetry cards.'')')
               go to 10
            end if
            nsym=nsym+1
            isym=nsym
         else if (isym.gt.nsym.or.isym.lt.0) then
            write (lout,'('' The range of current card numbers is 1 to''
     1             ,i4)') nsym
            go to 10
         end if
         n2=0
         ierrl=0
         do while (n2.ne.12.and.ierrl.eq.0)
            write (lout,'(//,'' Input 12 parameter symmetry card ('',i3,
     1             '')'')') isym
            read (in,165) line
            nl=20
            n2=20
            call inline (line,nl,n2,ain,labels,ierrl)
            if (ierrl.eq.1) ierrl=0
         end do
         print 195
         ts(1,isym)=sngl(ain(1))
         fs(1,1,isym)=sngl(ain(2))
         fs(2,1,isym)=sngl(ain(3))
         fs(3,1,isym)=sngl(ain(4))
         ts(2,isym)=sngl(ain(5))
         fs(1,2,isym)=sngl(ain(6))
         fs(2,2,isym)=sngl(ain(7))
         fs(3,2,isym)=sngl(ain(8))
         ts(3,isym)=sngl(ain(9))
         fs(1,3,isym)=sngl(ain(10))
         fs(2,3,isym)=sngl(ain(11))
         fs(3,3,isym)=sngl(ain(12))
      else
         call help (6d2)
      end if
      go to 10
C 
C     Thermal Parameter Conversion (NJ .EQ. 7)
C 
  100 if (mod(nj2,10).eq.4) then
         write (lout,'(/'' Warning: temperature factor conversion is'','
     1    //' '' irreversible.  Proceed? [Y] '',$)')
         read (in,'(a1)') goon
         if (goon.ne.'n'.and.goon.ne.'N') then
            iatom1=idnint(ain(1))
            iatom2=min(iabs(idnint(ain(2))),natom)
            if (iatom1.le.0) then
               iatom1=1
               iatom2=natom
            else if (iatom2.eq.0) then
               iatom2=iatom1
            end if
            do i=iatom1,iatom2
               if (ibull2(i)/100000.gt.0) then
                  call paxes (i*i100k+i55501,rms,q,-1)
                  t1=(q(1,1)*a(1)**2+q(2,2)*a(2)**2+q(3,3)*a(3)**2+
     1             2*q(1,2)*a(6)*a(1)*a(2)+2*q(1,3)*a(5)*a(1)*a(3)+
     2             2*q(2,3)*a(4)*a(2)*a(3))/3.
                  xtemp(i)=sngl(78.95683521d0*t1)
                  ibull2(i)=mod(ibull2(i),100000)
                  t1=sqrt(t1)
                  do j=1,3
                     do k=1,3
                        pa(j,k,i)=refv(j,k)
                     end do
                     ev(j,i)=t1
                  end do
               end if
            end do
            write (lout,'('' Conversion complete.  Use 1200-series''$)')
            write (lout,'('' instruction to save.'')')
         end if
      else
         print 185, nf
      end if
      go to 10
C 
C     HELP and RELABEL (NJ .EQ. 9)
C 
  105 if (nj2.eq.1) then
         call relabel
      else
         call help (ain(1))
      end if
      go to 10
C 
C     BUILD / ANALYZE (NJ .EQ. 10)
C 
  110 call analyze
      go to 10
C 
C     File output (NJ .EQ. 12)
C 
  115 select case (nj2)
      case (50:59)
         call cart (nj2-50)
         go to 10
      case (60)
         call cssr
         go to 10
      case default
         rewind 24
         call ppunch (nf)
         if (nf.eq.99) go to 10
      end select
      write (lout,175)
      read (in,'(a)') ffname
      inquire (file=ffname,exist=here1,opened=lopen)
      if (lopen) then
         write (lout,'(''Cannot proceed -- file is already open.'')')
         go to 10
      else if (here1) then
         write (lout,180)
         read (in,'(a1)') goon
         if ((goon.ne.'y').and.(goon.ne.'Y')) then
            write (lout,'(''** write aborted **'')')
            go to 10
         end if
      end if
      open (22,file=ffname,status='unknown')
      rewind 23
  120 read (23,155,end=125) title
      write (22,155) title
      go to 120
  125 rewind 24
  130 read (24,155,end=135) title
      write (22,155) title
      go to 130
  135 write (22,'(''         1000000'')')
      write (22,'(F10.4/E10.4)') scalef,extinf
      rewind 22
      rewind 23
      rewind 24
      go to 10
C 
C     Normal exit
C 
  140 if (m500.gt.1) then
         ffname='shortep.t21'
         write (lout,'('' Saving scratch tape as shortep.t21...'')')
         open (29,file=ffname,status='unknown')
         rewind lin
         read (lin,155,iostat=i) title
         do while (i.eq.0)
            write (29,155) title
            read (lin,155,iostat=i) title
         end do
         close (29)
         if (i.gt.0) then
            write (lout,'('' Warning: error '',i3,                      
     1       '' encountered while writing scratch tape.'')') i
         else
            write (lout,'('' Done.''/)')
         end if
      end if
      close (24)
      close (23)
      close (lin)
      stop
C 
C     Error exit:
C 
  145 write (lout,'('' ERROR: Could not open input file'')')
      stop
C
  150 format (' +-------------------------------------------------------
     1---+',/' |       INDIANA UNIVERSITY MOLECULAR STRUCTURE CENTER    
     2  |',/' |   Program SHORTEP started on ',a8,' at ',a12,'    |',/' 
     3|                    Version 021223                        |',/' +
     4----------------------------------------------------------+')
  155 format (8a10)
  160 format (' INPUT ? ')
  165 format (80a1)
  170 format (' ERROR ON INPUT LINE--PLEASE RETYPE')
  175 format (' Input name for new SDT: ',$)
  180 format (/,'   File already exists, type Y to continue: ',$)
  185 format (' Unrecognized instruction (',i6,')')
  190 format (/,' Input new symmetry card number',i4
     1 ,' -- input six numbers for:',/
     2 ,'      Tx, Mx, Ty, My, Tz, Mz; new symmetry card 1 is:',/
     3 ,'   Xnew=(Xold*Mx)+Tx; Ynew=(Yold*My)+Ty; Znew=(Zold*Mz)+Tz'/)
  195 format (' Symmetry card read...verify with 601')
  200 format (i3,3(f7.4,3i3))
  205 format ((2x,5(i3,'.  ',a8)))
  210 format ('  RMS displacements for atoms',/)
  215 format (3x,a8,3f10.6)
  220 format (/,' There were',i4,' atoms on the SDT;',/,'           ',
     1i4,' are nonhydrogens.',/,'           ',i4,' ADC''s are currently 
     2in the ATOMS LIST')
  225 format (/,' ATOMS LIST duplication inhibited',/)
  230 format (/,' ATOMS LIST duplication allowed',/)
  235 format (2x,5i10)
      end
C
C     subroutine adata
C 
C     find Van Der Waals Radius (ARAD) & atomic no. (NTYPE)
C     of the JATth atom
C 
C     WES..APRIL,1996.  THE OPTION TO ENTER A RADIUS AND ATOMIC NUMBER
C                       NOW WORKS, I.E., ONE CAN DEFINE EXTRA ELEMENTS
C                       SUCH AS FOR PK'S OR PX'S AS ATOMIC NUMBERS
C                       105 THRU 110 WITH WHATEVER VDW RADII YOU WANT.
C 
      subroutine adata (chem,jat)
      integer jat
      character*8 chem
C 
      real arad(4000),radmax
      integer ntype(4000),i405,ipoly
      common /adat/ arad,radmax,i405,ipoly,ntype

      character*2 radc(110)
      common /ceinfo/ radc
  
      integer nelement, iatn(110)
      common /ieinfo/ nelement, iatn

      integer in,lin,lout
      common /lun/ in,lin,lout

      common /reinfo/ radi
      real radi(110)
C 
      double precision anewl(2)
      integer i,ian2,ierrl,ii,j,j1,j2,k,m,newan
      character*8 an,labl(4)
      character*2 n
      character*1 an1,an2,ldata(80)
C 
C    extract atomic symbol from atom label
C 
      an=chem
      do i=1,8
         an1=an(i:i)
         if (an1.ne.' ') then
            ii=i+1
            an2=an(ii:ii)
            ian2=ichar(an2)
            if ((ian2.ge.97).and.(ian2.le.122)) then
               an2=char(ian2-32)
            else if ((ian2.lt.65).or.(ian2.gt.90)) then
               an2=an1
               an1=' '
            end if
            n=an1//an2
            go to 5
         end if
      end do
C 
C     ** BLANK ATOM LABEL **
C 
      write (lout,25) jat
      n='PK'
      chem=n
      go to 10
C 
C     locate symbol, radius, & atomic number in stored arrays via
C     a binary search
C 
    5 i=0
      j=nelement+1
      do while (j-i.gt.1)
         m=(i+j)/2
         if (radc(m).gt.n) then
            j=m
         else if (radc(m).lt.n) then
            i=m
         else
            arad(jat)=radi(m)
            ntype(jat)=iatn(m)
            return
         end if
      end do
C 
C     do the following if an unknown element is encountered
C 
   10 write (lout,30) n
      if (nelement.lt.110) go to 15
      write (lout,35)
      stop
   15 write (lout,40)
      read (in,45) ldata
      if (ldata(1).eq.' ') go to 10
      j1=4
      j2=2
      call inline (ldata,j1,j2,anewl,labl,ierrl)
      newan=idnint(anewl(2))
      if (newan.lt.1.or.newan.gt.110) then
         write (lout,50)
         go to 15
      end if
      nelement=nelement+1
      k=nelement-j
      do i=1,k
         radc(nelement-i+1)=radc(nelement-i)
         radi(nelement-i+1)=radi(nelement-i)
      end do
      radc(j)=n
      radi(j)=sngl(anewl(1))
      iatn(j)=newan
      arad(jat)=sngl(anewl(1))
      ntype(jat)=newan
      return
C 
   25 format (' ATOM LABEL NO.',i4,' IS BLANK.  IT WILL BE SET TO PK.')
   30 format (' NO DATA for element ',a2,' in SHORTEP Periodic Table')
   35 format (/' PROGRAM DIMENSIONS EXCEEDED FOR EXTRA SYMBOLS')
   40 format (' Input VDW RADIUS and ATOMIC NUMBER (1-110): ',$)
   45 format (80a1)
   50 format (' BAD ATOMIC NO.')
      end
C
C     Subroutine analyze
C 
      subroutine analyze
C 
      real arad(4000),radmax
      integer ntype(4000),i405,ipoly
      common /adat/ arad,radmax,i405,ipoly,ntype
  
      real atoms(3,4000)
      integer atomid(4000),latm
      common /atoml/ atomid,atoms,latm
  
      real a(9),aa(3,3),bb(3,3)
      common /cell/ a,aa,bb
  
      character*8 chem(4000)
      character*10 sdttit(8)
      character*12 spgtit
      common /chars/ chem,sdttit,spgtit
  
      real scalef,extinf,bx(7)
      integer isdtis(48)
      common /ends/ scalef,extinf,isdtis,bx
  
      real ev(3,4000),p(3,4000),pa(3,3,4000)
      integer natom
      common /inputl/ ev,natom,p,pa
   
      double precision ain(30)
      integer nj,nj2
      common /instr/ ain,nj,nj2
  
      integer in,lin,lout
      common /lun/ in,lin,lout
C  
      real ang1,ang2,ang3,bxx
      integer i,i1,iano(20),isymhash,itmp,ixx(8000),j,latold,nonh
      integer numa(20)
      logical same
      character*50 form1,form2
      character*2 isym,symb(110)
      character*1 form3(50),form4(50)
C 
      data symb/' H','He','Li','Be',' B',' C',' N',' O',' F','Ne','Na',
     1'Mg','Al','Si',' P',' S','Cl','Ar',' K','Ca','Sc','Ti',' V','Cr',
     2'Mn','Fe','Co','Ni','Cu','Zn','Ga','Ge','As','Se','Br','Kr','Rb',
     3'Sr',' Y','Zr','Nb','Mo','Tc','Ru','Rh','Pd','Ag','Cd','In','Sn',
     4'Sb','Te',' I','Xe','Cs','Ba','La','Ce','Pr','Nd','Pm','Sm','Eu',
     5'Gd','Tb','Dy','Ho','Er','Tm','Yb','Lu','Hf','Ta',' W','Re','Os',
     6'Ir','Pt','Au','Hg','Tl','Pb','Bi','Po','At','Rn','Fr','Ra','Ac',
     7'Th','Pa',' U','Np','Pu','Am','Cm','Bk','Cf','Es','Fm','Md','No',
     8'Lr','PK','PK','PK','PK','PK','PK','PK'/
C 
C     Set all ntype(i) to the appropriate atomic number and all arad(i)
C     to the corresponding radius
C 
      do i=1,natom
         call adata (chem(i),i)
         itmp=ntype(i)
         if (itmp.gt.103) then
            isym=chem(i)(1:2)
            if (symb(itmp).ne.isym) symb(itmp)=isym
         end if
      end do
C 
C     Find the maximum atomic radius and set up for a sort on chemical
C     symbol
C 
      j=1
      radmax=0.
      do i=1,natom
         radmax=max(radmax,arad(i))
         ixx(j)=i
         j = j+1
         ixx(j)=isymhash(chem(i)(1:2))
         if (ntype(i).eq.6.or.ntype(i).eq.1) ixx(j)=ixx(j)-65536
         j=j+1
      end do
      j=j-1
C 
C     j is equal to 2*natom at this point.  Sort on chemical symbol into
C     the Hill order
C 
      call sort (ixx,j)
C 
C     Output summary information
C
      write (lout,5) (sdttit(i),i=1,7)
      write (lout,10) spgtit
      ang1=acos(a(4))*57.29578
      ang2=acos(a(5))*57.29578
      ang3=acos(a(6))*57.29578
      write (lout,15) a(1),a(4),ang1,a(2),a(5),ang2,a(3),a(6),ang3
      bxx=bx(1)+bx(2)+bx(3)
      if (bxx.lt.0.001) write (lout,20)
      write (lout,25) scalef,extinf
C 
C     Count the number of atoms of each element.  j is still 2*natom
C     when we get here.
C 
      do i=1,20
         iano(i)=0
         numa(i)=0
      end do
      i1=1
      if (j.gt.0) then
         iano(1)=ntype(ixx(1))
         numa(1)=1
         do i=3,j,2
            if (ntype(ixx(i)).eq.iano(i1)) then
               numa(i1)=numa(i1)+1
            else
               i1=i1+1
               iano(i1)=ntype(ixx(i))
               numa(i1)=1
            end if
         end do
      end if
C 
C   Now check to see whether the formula from the SDT matches the
C   atoms list
C 
      if (i1.ne.isdtis(1)) then
         same=.false.
      else
         j=1
         do i=17,15+2*i1,2
            ixx(j)=i
            j=j+1
            ixx(j)=isymhash(symb(isdtis(i)))
            if (isdtis(i).eq.6.or.isdtis(i).eq.1) ixx(j)=ixx(j)-65536
            j=j+1
         end do
         call sort(ixx, j-1)
         do i=1,2*i1-1,2
            if (isdtis(ixx(i)).ne.iano(i).or.
     1          isdtis(ixx(i)+1).ne.numa(i)) exit
         end do
         same=(i.gt.i1)
      end if
C 
      if (same) then
         write (lout,30)
      else
         write (lout,55)
         write (form1,35) (symb(isdtis(ixx(i))),i=1,2*i1-1,2)
         write (form2,40) (isdtis(ixx(i)+1),i=1,2*i1-1,2)
         read (form1,45) form3
         read (form2,45) form4
         call fcompress (form3,form4)
         write (lout,50) form3
         write (lout,50) form4
         write (lout,60)
      end if
      write (form1,35) (symb(iano(i)),i=1,i1)
      write (form2,40) (numa(i),i=1,i1)
      read (form1,45) form3
      read (form2,45) form4
      call fcompress (form3,form4)
      write (lout,50) form3
      write (lout,50) form4
C 
      if (nj2.eq.0.or.j.eq.0) return
C 
c     now go into build function.......
C 
      if (ain(1).eq.0d0) ain(1)=1d0
      nonh=0
      do i=1,natom
         if (ntype(i).ne.1) nonh=nonh+1
      end do
C 
C     Make sure that the hydrogen atoms all come at the end of the SDT.
C     If not, then return with a diagnostic.
C 
      do i=nonh+1,natom
         if (ntype(i).ne.1) then
            write (lout,65)
            return
         end if
      end do
C 
C     Make sure that the anchor atom is in the list
C 
      nj=4
      nj2=1
      call f400
C 
C     Set up to iterate 405 instructions
C 
      nj2=5
      i405=1
      ipoly=0
C 
C     Reiterate 405 instructions as long as new atoms keep being
C     added
C 
      latold=0
      do while (latold.ne.latm)
         latold=latm
         ain(1)=1d0
         ain(2)=nonh
         ain(3)=1d0
         ain(4)=nonh
         call f400
      end do
      if (nonh.lt.natom) then
         ain(1)=1d0
         ain(2)=nonh
         ain(3)=nonh+1
         ain(4)=natom
         ain(5)=1.5d0
         call f400
      end if
      i405=0
C
C     Diagnose exceptional conditions
C
      if (ipoly.ne.0) write (lout,70)
      if (latm.eq.4000) write (lout,75)
C
C     Summarize build result
C
      if (latm.eq.natom) then
         write (lout,80)
      else if (latm.lt.natom) then
         write (lout,85)
      else if (latm.gt.natom) then
         write (lout,90)
      end if
C 
      return
C
    5 format (' The title of the Standard Data Tape is: ',//,5x,7a10)
   10 format (/' The space group is: ',a12,/)
   15 format (' Cell parameters are:  '/'   a = ',f10.3,'  cos alpha = '
     1,f10.5,'   alpha  =',f10.2,/'   b = ',f10.3,'  cos  beta = '
     2,f10.5,'   beta   =',f10.2,/'   c = ',f10.3,'  cos gamma = '
     3,f10.5,'   gamma  =',f10.2)
   20 format (/' CAUTION--the cell parameters have no esd''s')
   25 format (/' The scale factor is: ',f10.4,' and the extinction is: '
     1,e10.4)
   30 format (/' The Formula is: ')
   35 format (10(a2,3x))
   40 format (10(2x,i3))
   45 format (50a1)
   50 format (20x,50a1)
   55 format (/' There is a discrepancy in the formula, the SDT suggests
     1:')
   60 format (/' while the atoms present correspond to:')
   65 format (' All hydrogen atoms must lie at the end of the SDT')
   70 format (' Warning: "molecule" appears to be polymeric;'/,
     1        '          BUILD limited to one unit-cell dimension.',/)
   75 format (' Warning: atom list filled; BUILD may be incomplete.',/)
   80 format (' BUILD finished... one unique molecule is present')
   85 format (' BUILD finished... more than one fragment is present')
   90 format (' BUILD finished... molecular symmetry was present')
      end
C
C     subroutine closit
C 
C     Handles 407 INSTRUCTIONs
C 
      subroutine closit
C 
      real atoms(3,4000)
      integer atomid(4000),latm
      common /atoml/ atomid,atoms,latm
  
      real ev(3,4000),p(3,4000),pa(3,3,4000)
      integer natom
      common /inputl/ ev,natom,p,pa
  
      double precision ain(30)
      integer nj,nj2
      common /instr/ ain,nj,nj2

      integer i100,i1000,i10k,i100k,i55501
      common /icon/ i100,i1000,i10k,i100k,i55501
C 
      integer ij,k,nmax
C 
      ain(5)=ain(1)
      if (ain(5).eq.0d0) ain(5)=9d-1
      nmax=idnint(ain(2))
      if (nmax.eq.0) nmax=natom
C 
C     Load the atoms list
C 
      nj=4
      nj2=1
      ain(1)=dble(i100k+i55501)
      ain(2)=-dble(nmax*i100k+i55501)
      ain(3)=0d0
      call f400
C 
      ain(24)=1d0
      ain(27)=1d0
      k=1
      nj=4
      nj2=15
      do while (k.lt.latm)
         ij=atomid(k)/i100k
         ain(1)=dble(ij)
         ain(2)=dble(ij)
         ain(3)=dble(ij+1)
         ain(4)=dble(nmax)
         call searc
         k=k+1
      end do
      ain(27)=0d0
      return
      end
C
C     subroutine cssr
C 
C     output a cssr-format file for input to Cerius2
C 
      subroutine cssr
C 
      real a(9),aa(3,3),bb(3,3)
      common /cell/ a,aa,bb
  
      character*8 chem(4000)
      character*10 sdttit(8)
      character*12 spgtit
      common /chars/ chem,sdttit,spgtit
  
      real ev(3,4000),p(3,4000),pa(3,3,4000)
      integer natom
      common /inputl/ ev,natom,p,pa
  
      integer in,lin,lout
      common /lun/ in,lin,lout
C 
      real ang1,ang2,ang3
      integer i,j
C 
      open (43,file='sdt.cssr',status='unknown')
      ang1=acos(a(4))*57.29578
      ang2=acos(a(5))*57.29578
      ang3=acos(a(6))*57.29578
      write (lout,10) a(1),a(4),ang1,a(2),a(5),ang2,a(3),a(6),ang3
      write (43,15) a(1),a(2),a(3)
      write (lout,5) spgtit
      write (43,20) ang1,ang2,ang3
      write (43,25) natom
      write (43,30)
      do i=1,natom
         write (43,35) i,chem(i),(p(j,i),j=1,3)
      end do
      return
C 
    5 format (/' The space group is: ',a12,/)
   10 format (' Cell parameters are:  '/'   a = ',f10.3,'  cos alpha = '
     1,f10.5,'   alpha  =',f10.2,/'   b = ',f10.3,'  cos  beta = '
     2,f10.5,'   beta   =',f10.2,/'   c = ',f10.3,'  cos gamma = '
     3,f10.5,'   gamma  =',f10.2)
   15 format (38x,3f8.3)
   20 format (21x,3f8.3,'    SPGP =000 ',a8,'    OPT = 1')
   25 format (i4,'   0 Created by SHORTEP')
   30 format ('     ')
   35 format (i4,1x,a6,f8.5,2f10.5,8i4,f8.3)
      end
C
C     subroutine datime
C 
C     Returns the current system date and time.  The date_and_time
C     routine used here is standard Fortran 90; compilers that don't
C     support it will normally have date/time routines that can be
C     substituted.
C 
      subroutine datime (date,time)
      character*12 time
      character*8 date
C 
      call date_and_time (date,time)
      date=date(3:4)//'/'//date(5:6)//'/'//date(7:8)
      time=time(1:2)//':'//time(3:4)//':'//time(5:9)
      return
      end
C
C     subroutine difv
C 
C     calculates a vector difference:
C     VECTOR Z = VECTOR X - VECTOR Y
C 
      subroutine difv (x,y,z)
      real x(3),y(3),z(3)
C
      integer i
C 
      do i=1,3
         z(i)=x(i)-y(i)
      end do
      return
      end
C
C     subroutine extract
C
C     extracts a number and an esu from a character argument.  On input
C     ilen contains the string length, on output it is set to the number
C     of characters read.
C 
      subroutine extract (text,ilen,val,esu)
      character*(*) text
      real val,esu
      integer ilen
C
      integer iedec,iesu,inx1,inx2,n
      if (ilen.eq.0) then
         val=0.
         esu=0.
      else
         inx1=1
         iesu=0
         iedec=0
         inx2=2
         do while (inx2.le.ilen)
            if ((text(inx2:inx2).lt.'0'.or.text(inx2:inx2).gt.'9')
     1       .and.(text(inx2:inx2).ne.'.')) exit
            inx2=inx2+1
         end do
         read (text(inx1:inx2-1),'(bn,f11.0)') val
         do n=inx2-1,inx1,-1
            if (text(n:n).eq.'.') then
               iedec=inx2-n-1
               exit
            end if
         end do
         inx1=inx2+1
         if (inx1.le.ilen) then
            if (text(inx1:inx1).ge.'0'.and.text(inx1:inx1).le.'9') then
               inx2=inx1+1
               do while (inx2.le.ilen)
                  if (text(inx2:inx2).lt.'0'.or.text(inx2:inx2).gt.'9')
     1             exit
                  inx2=inx2+1
               end do
               read (text(inx1:inx2-1),'(bn,i10)') iesu
            end if
         end if
         esu=float(iesu)/(10**iedec)
         ilen=inx2-1
      end if
      return
      end
C
C     subroutine f400
C 
C     ATOM LIST FUNCTIONS
C 
C     NOTE: 402,408,409,410 ARE HANDLED BY THE MAIN PROGRAM.
C           407 IS HANDLED BY CLOSIT WHICH TRIGGERS 401'S FOR
C               ACCEPTABLE ADC'S
C           401,404,405,406,411,414,415,416 ARE HANDLED HERE.
C 
      subroutine f400
C  
      real atoms(3,4000)
      integer atomid(4000),latm
      common /atoml/ atomid,atoms,latm
  
      integer ng
      common /fault/ ng
  
      double precision ain(30)
      integer nj,nj2
      common /instr/ ain,nj,nj2
C 
      integer i
C 
      ng=0
C 
C     GET COORDINATES FOR ATOMS IN THE ATOMID ARRAY
C 
      do i=1,latm
         call atom (atomid(i),atoms(1,i))
      end do
C 
      if (mod(nj2,10).gt.1) then
C 
C     SEARC TAKES CARE OF 404,405,406,414,415, and 416
C 
         call searc
      else
C 
C     THIS LEAVES JUST 401 (STORE) OR 411 (REMOVE) RUNS OF ATOMS.
C     RUN HIERARCHY = ATOM NO./SYM/ A/B/C TRANS.
C 
         call f401
      end if
      return
      end
C
C     subroutine fcompress
C 
      subroutine fcompress (form3,form4)
      character*1 form3(50),form4(50)
C
      integer i,j,k
      do i=1,50
         do k=1,2
            if ((form3(i).eq.' ').and.(form4(i).eq.' ')) then
               do j=i,49
                  form3(j)=form3(j+1)
                  form4(j)=form4(j+1)
               end do
               form3(50)=' '
               form4(50)=' '
            end if
         end do
      end do
      return
      end
C
C     subroutine findtoken
C 
C     scans the supplied character buffer for a token; if not found,
C     then the next line is read from the file.  Ignores whitespace
C     and comments.  (Note: tab characters are not whitespace!)
C 
      subroutine findtoken (line,istart,iend,itype,ieof)
      integer iend,ieof,istart,itype
      character*80 line
C  
      integer lineno
      common /cif/ lineno
  
      integer in,lin,lout
      common /lun/ in,lin,lout
C 
      itype=0
      do while (itype.eq.0)
         if (iend.ge.80) then
            call readlinedirect (lin,line,ieof)
            if (ieof.ne.0) return
            lineno=lineno+1
            istart=0
         else
            istart=iend
            iend=80
         end if
         call gettoken (line,istart,iend,itype)
      end do
      return
      end
C
C     subroutine getlooped
C     Reads a loop header and the data items of the associated looped
C     list, storing those items required for SHORTEP
C 
      subroutine getlooped (line,istart,iend,itype,ieof)
      integer iend,ieof,istart,itype
      character*80 line
C 
      integer ng
      common /fault/ ng
  
      integer in,lin,lout
      common /lun/ in,lin,lout
C  
      integer ifield,igetredir,ilct,irec,iredir(25)
C 
C     Read data labels and store the count in ilct.  Build a redirection
C     table for the associated data values.
C 
      ilct=0
      do while (.true.)
         call findtoken (line,istart,iend,itype,ieof)
         if (ieof.ne.0) then
            ng=21
            return
         end if
         if (itype.ne.1) exit
         ilct=ilct+1
         iredir(ilct)=igetredir(line(istart:iend),iend-istart+1)
      end do
C 
C     Read the data values and store according to the redirection table
C 
      if (ilct.eq.0) then
         ng=21
         return
      end if
      irec=1
      ifield=0
      do while (.true.)
         if (itype.eq.6) then
            ng=21
            return
         else if (itype.eq.5) then
C 
C     Block text is read but never stored
C 
            itype=0
            do while (itype.ne.5)
               call findtoken (line,istart,iend,itype,ieof)
               if (ieof.ne.0) go to 10
            end do
         else if (itype.eq.1) then
            go to 10
         else if (itype.eq.2) then
            if (line(1:5).eq.'data_'.or.line(1:5).eq.'loop_') go to 10
         end if
         if (ifield.lt.ilct) then
            ifield=ifield+1
         else
            ifield=1
            irec=irec+1
         end if
         if (iredir(ifield).ne.0) then
            if (itype.ne.5) then
               call saveit (line,istart,iend,itype,iredir(ifield),irec)
            else
               write (lout,'(''Warning: block text not interpreted!'')')
            end if
         end if
         call findtoken (line,istart,iend,itype,ieof)
         if (ieof.ne.0) go to 10
      end do
C 
C     End of loop encountered
C 
   10 if (ifield.ne.ilct) ng=21
      return
      end
C
C     subroutine gettoken
C 
C     parse a string for the first token starting after position istart
C     on input, the length of the string is taken to be iend.
C 
C     values of itype:
C     0:  No token (or only a comment)
C     1:  data name (begins with '_')
C     2:  character token
C     3:  numeric token
C     4:  placeholder
C     5:  block text delimiter (';' symbol at position 1)
C     6:  error -- unmatched delimiter
C 
      subroutine gettoken (text,istart,iend,itype)
      integer iend,istart,itype
      character*(*) text
C
      character*1 match
      integer i,j
C
      itype=0
      do i=istart+1,iend
         if (text(i:i).ne.' ') exit
      end do
      if (i.le.iend) then
         istart=i
         if (text(i:i).eq.'#') then
            return
         else if (text(i:i).eq.';'.and.i.eq.1) then
            itype=5
            return
         else if (text(i:i).eq.'''') then
            match=''''
         else if (text(i:i).eq.'"') then
            match='"'
         else
            match=' '
         end if
         do j=istart+1,iend
            if (text(j:j).eq.match) exit
         end do
         if (match.eq.' ') j=j-1
         if (j.gt.iend) then
            itype=6
            return
         end if
         iend=j
         if (text(istart:iend).eq.'.'.or.text(istart:iend).eq.'?') then
            itype=4
         else if (text(istart:istart).eq.'_') then
            itype=1
         else if (text(istart:istart).eq.'+'.or.
     1            text(istart:istart).eq.'-'.or.
     2            text(istart:istart).eq.'.'.or.
     3            (ichar(text(istart:istart)).gt.47.and.
     4            ichar(text(istart:istart)).lt.58)) then
            itype=3
         else
            itype=2
         end if
      end if
      return
      end
C
C     subroutine getvalue
C 
C     Reads the specified data name.  If it is one that we save,
C     then stores the associated data value; otherwise, reads and
C     discard the data value.  This is intended for non-looped
C     data items only.
C 
      subroutine getvalue (line,istart,iend,ieof)
      integer iend,ieof,istart
      character*80 line
C 
      real arad(4000),radmax
      integer ntype(4000),i405,ipoly
      common /adat/ arad,radmax,i405,ipoly,ntype
  
      real a(9),aa(3,3),bb(3,3)
      common /cell/ a,aa,bb
  
      character*8 chem(4000)
      character*10 sdttit(8)
      character*12 spgtit
      common /chars/ chem,sdttit,spgtit
  
      real scalef,extinf,bx(7)
      integer isdtis(48)
      common /ends/ scalef,extinf,isdtis,bx
  
      integer ng
      common /fault/ ng
  
      integer in,lin,lout
      common /lun/ in,lin,lout
  
      integer nh,nel(104)
      common /nh/ nh,nel
C 
      real val,esu
      integer i,ilen,itype,j,js,je,jes,jt,k,klen
      character*80 keywrd
      character*8 temp
      character*1 spgnam(12)
C 
C     Store the keyword
C 
      keywrd=line(istart:iend)
      klen=iend-istart+1
C 
C     Now look for the data value
C 
      call findtoken (line,istart,iend,itype,ieof)
      if (ieof.ne.0) go to 5
      go to (5,25,30,10,20,15),itype
C 
C     Encountered a keyword instead of a data value
C 
    5 write (lout,35) keywrd(1:klen)
C 
C     Just ignore if the data value is a placeholder
C 
   10 return
C 
C     Parse error
C 
   15 ng=21
      return
C 
C     We handle block text data values only in that we read and
C     ignore them.  They are equivalent to place holders for the
C     purposes of this program.
C 
   20 itype=0
      do while (itype.ne.5.and.ieof.eq.0)
         call findtoken (line,istart,iend,itype,ieof)
      end do
      return
   25 if (keywrd(1:klen).eq.'_symmetry_space_group_name_H-M') then
         j=0
         if (line(istart:istart).eq.''''.or.line(istart:istart).eq.'"')
     1    istart=istart+1
         do i=istart,istart+11
            j=j+1
            if (i.gt.iend) then
               spgnam(j)=' '
            else
               if (line(i:i).ne.''''.and.line(i:i).ne.'"') then
                  k=ichar(line(i:i))
                  if (k.gt.96.and.k.lt.123) then
                     spgnam(j)=char(k-32)
                  else
                     spgnam(j)=line(i:i)
                  end if
               else
                  spgnam(j)=' '
               end if
            end if
         end do
         write (spgtit,'(12a1)') spgnam
      else if (keywrd(1:klen).eq.'_chemical_formula_sum') then
         if (line(istart:istart).eq.''''.or.line(istart:istart).eq.'"')
     1    then
            js=istart
            je=iend-1
         else
            js=istart-1
            je=iend
         end if
         jes=je
         do while (.true.)
            call gettoken (line,js,je,jt)
            if (jt.eq.0) exit
            j=0
            do i=js,js+7
               j=j+1
               if (i.gt.je) then
                  spgnam(j)=' '
               else if (line(i:i).ge.'a'.and.line(i:i).le.'z') then
                  spgnam(j)=char(ichar(line(i:i))-32)
               else
                  spgnam(j)=line(i:i)
               end if
            end do
            write (temp,'(8a1)') (spgnam(i),i=1,8)
            call adata (temp,4000)
            if (ntype(4000).gt.0.and.ntype(4000).lt.104) then
               do js=js,je
                  if (line(js:js).ge.'0'.and.line(js:js).le.'9') exit
               end do
               if (js.gt.je) then
                  nel(ntype(4000))=1
               else
                  jt=1+je-js
                  call extract (line(js:je),jt,val,esu)
                  nel(ntype(4000))=nint(val)
               end if
            end if
            js=je
            je=jes
         end do
      end if
      return
   30 ilen=iend+1-istart
      call extract (line(istart:iend),ilen,val,esu)
      if (keywrd(1:klen).eq.'_cell_length_a') then
         a(1)=val
         bx(1)=esu
      else if (keywrd(1:klen).eq.'_cell_length_b') then
         a(2)=val
         bx(2)=esu
      else if (keywrd(1:klen).eq.'_cell_length_c') then
         a(3)=val
         bx(3)=esu
      else if (keywrd(1:klen).eq.'_cell_angle_alpha') then
         a(4)=val
         bx(4)=esu
      else if (keywrd(1:klen).eq.'_cell_angle_beta') then
         a(5)=val
         bx(5)=esu
      else if (keywrd(1:klen).eq.'_cell_angle_gamma') then
         a(6)=val
         bx(6)=esu
      else if (keywrd(1:klen).eq.'_cell_volume') then
         bx(7)=val
      else if (keywrd(1:klen).eq.'_cell_formula_units_Z') then
         isdtis(5)=nint(val)
      else if (keywrd(1:klen).eq.'_refine_ls_extinction_coef') then
         extinf=val/1000.
      end if
      return
C
   35 format (1x,'CIF error: missing data value for item',/,5x,a)
      end
C
C     subroutine help
C 
C     provides limited on-line instructions regarding the available
C     commands
C 
      subroutine help (aain)
      double precision aain
C  
      integer in,lin,lout
      common /lun/ in,lin,lout
  
      character*79 helptext(500)
      common /helptext/ helptext
  
      integer helpinx(3,50),maxinx
      common /helpinx/ helpinx,maxinx
C  
      integer i,iain,iain00,nearest
C 
C     Identify the appropriate help text through an index search
C 
      iain=idnint(aain)
      iain00=iain/100
      nearest=1
      do i=2,maxinx
         if (helpinx(1,i).eq.iain) then
            nearest=i
            exit
         else if (helpinx(1,i)/100.eq.iain00) then
            if (helpinx(1,i).gt.helpinx(1,nearest).and.helpinx(1,i)
     1       .lt.iain) nearest=i
         end if
      end do
C 
C     Display the text
C 
      write (lout,'()')
      write (lout,'(a79)') (helptext(i),i=helpinx(2,nearest),
     1 helpinx(3,nearest))
      write (lout,'()')
      end
C
C     block data helpdat
C 
C     Initializes the help system data
C 
      block data helpdat
C
      character*79 helptext(500)
      common /helptext/ helptext

      integer helpinx(3,50),maxinx
      common /helpinx/ helpinx,maxinx
C
      integer i
C
C maxinx must be set to the largest used value of the second index of
C helpinx.
C
C helptext contains the actual lines of help text.
C
C helpinx contains an instruction number and the associated start and
C stop line numbers for each block of help text.
C
      data maxinx/23/
C
C Main help menu, lines 1 - 13
C
      data (helpinx(i,1),i=1,3) /0,1,17/
      data (helptext(i),i=1,17) /
     1 'Shortep recogizes instructions from the following centuries:',
     2 '   100s -- Structure analysis (distances, angles, etc.)',
     3 '   200s -- Input Listings',
     4 '   300s -- Shortep options (except for atom list options)',
     5 '   400s -- Atom list manipulation',
     6 '   500s -- Shortep restart',
     7 '   600s -- Symmetry card manipulation',
     8 '   900  -- Help system',
     9 '   901  -- Atom relabelling',
     a '  1000s -- Analyze and Build',
     b '  1200s -- Output',
     c ' ',
     d 'Help is also available for ADCs (1) and the atoms list (2).',
     e ' ',
     f 'Type "help <#>" where <#> is one of the above numbers for additi
     gonal',
     h 'information about the corresponding area, or where <#> is a spec
     iific',
     j 'instruction number for information about that instruction.' /
C
C 100s, lines 21 - 35
C
      data (helpinx(i,4),i=1,3) /100,21,35/
      data (helptext(i),i=21,35) /
     1 '100-series instructions (Structure Analysis)',
     2 ' ',
     3 '   10x  s1 s2 t1 t2 d',
     4 ' ',
     5 '   x=1: Interatomic distances',
     6 '        Lists all distances less than d from atoms between s1',
     7 '        and s2 (inclusive) to atoms between t1 and t2 (inclusive
     8).',
     9 ' ',
     a '   x=2: Interatomic angles',
     b '        Lists angles around all atoms s between s1 and s2 (inclu
     csive) to',
     d '        pairs of atoms both between t1 and t2 and closer to s th
     ean distance d.',
     f ' ',
     g '   x=3: Principal axes of thermal motion',
     h '        Lists the lengths of the principal axes of atoms between
     i s1 and s2.',
     j '        Parameters t1 t2 and d are ignored if present.' /
      data (helptext(i),i=36,41) /
     1 ' ',
     2 '   s1 and t1 default to 1 if absent, s2 and t2 default to NATOMS
     3 (the last',
     4 '   atom), and d defaults 2.0.',
     5 ' ',
     6 '   "distance" is a synonym for 101, "angle" is a synonym for 102
     7, and',
     8 '   "listrms" is a synonym for 103.'/

C
C 200s, lines 51 - 60
C
      data (helpinx(i,5),i=1,3) /200,51,60/
      data (helptext(i),i=51,60) /
     1 '200-series instructions (Input Listing)',
     2 ' ',
     3 '   2xy i1 i2',
     4 ' ',
     5 '   For any x and y, list the atoms i1 through i2 from the input
     6file.  If',
     7 '   i1 is missing or less than 1 then it defaults to 1.  If i2 is
     8 less than',
     9 '   zero then the absolute value is used; if i2 is missing or zer
     ao then the',
     b '   end of the list is used.',
     c ' ',
     d '   "LIST" and "ATOMS" are both synonyms for 200.' /
C
C 300s, lines 71 - 74
C
      data (helpinx(i,6),i=1,3) /300,71,74/
      data (helptext(i),i=71,74) / 
     1 '300-series instructions (Shortep Options)',
     2 ' ',
     3 '   300 (or "offadc") Turn off ADC listing for 100-series instruc
     4tions.',
     5 '   301 (or "onadc")  Turn on ADC listing for 100-series instruct
     6ions.' /
C
C 400s index, lines 91 - 107
C
      data (helpinx(i,7),i=1,3) /400,91,107/
      data (helptext(i),i=91,106) /
     1 '400-series instructions (Atoms List Manipulation)',
     2 ' ',
     3 '   4x1* Add/eliminate atoms to/from the atoms list',
     4 '   402  Display input list statistics',
     6 '   4x4* Triclinic box of enclosure add/eliminate',
     7 '   4x5* Convoluting sphere of enclosure add/eliminate',
     8 '   406* Reiterative convoluting sphere of enclosure add',
     9 '   407* Build "clean" atoms list',
     a '   408  Prevent atom duplication ',
     b '   409  Allow atom duplication',
     c '   410  Clear the atoms list ("clear" is a synonym for 410)',
     d '   416* Special convoluting sphere of enclosure eliminate',
     e ' ',
     f '   x above is either 0 or 1.',
     g '   Additional information is available for those instructions ma
     hrked with an',
     i '   asterisk.  Type "help <instr>", where <instr> is the instruct
     jion number.' /
      data helptext(107) /
     1 '   Type "help 2" for more information on the atoms list.' /
C
C 401/411, lines 121 - 128
C
      data (helpinx(i,8),i=1,3) /401,121,128/
      data (helpinx(i,9),i=1,3) /411,121,128/
      data (helptext(i),i=121,128) /
     1 'Instructions 401 ("addatom") and 411 ("remove"):',
     2 ' ',
     3 '   4x1 n1 n2 ... nn',
     4 '   n1...nn are ADCs or atom numbers to add to or remove from the
     5 atoms list.',
     6 '   Atom numbers are converted to ADCs by appending "55501".  If 
     7a positive',
     8 '   ADC is followed by a negative one then the pair designate a r
     9ange of ADCs',
     a '   to add or remove.  It is invalid to specify two consecutive n
     begative atom',
     c '   numbers or ADCs. Type "help 1" for more information on ADCs.'
     d /
C
C 404/414, lines 131 - 141C
      data (helpinx(i,10),i=1,3) /404,131,142/
      data (helpinx(i,11),i=1,3) /414,131,142/
      data (helptext(i),i=131,142) /
     1 'Instructions 404 ("box" or "boxin") and 414:',
     2 ' ',
     3 '   4x4 s1 s2 t1 t2 d1 d2 d3',
     4 '   For each atom from s1 to s2, generate a "box of enclosure" ce
     5ntered on',
     6 '   that atom -- a parallelepiped with faces parallel to the unit
     7 cell faces',
     8 '   and edges of length 2*d1*a, 2*d2*b, and 2*d3*c along the a, b
     9, and c',
     a '   directions repectively.  Those atoms from t1 to t2 which lie 
     bwithin any of',
     c '   these boxes are added to the atoms list (404) or removed from
     d it (414).',
     e ' ',
     f '   For example, given a 25 atom model and a dummy atom number 26
     g at .5 .5 .5,',
     h '      404 26 26 1 25 .5 .5 .5',
     i '   would generate the complete unit cell.' /
C
C 405/415, lines 151 - 158
C
      data (helpinx(i,12),i=1,3) /405,151,158/
      data (helpinx(i,13),i=1,3) /415,151,158/
      data (helptext(i),i=151,158) /
     1 'Instruction 405 ("expand") and 415:',
     2 ' ',
     3 '   4x5 s1 s2 t1 t2 d',
     4 '   For each atom s from s1 to s2 ALREADY IN THE ATOMS LIST, any 
     5symmetry',
     6 '   copy of any atom from t1 to t2 which is within d Angstroms of
     7 s is',
     8 '   added to (405) or removed from (415) the atoms list.  Caveat:
     9 a source atom',
     a '   is only removed if it is too close to another source atom whi
     bch occurs',
     c '   earlier in the atoms list.' /
C
C 406, lines 161 - 169
C
      data (helpinx(i,14),i=1,3) /406,161,169/
      data (helptext(i),i=161,169) /
     1 'Instruction 406:',
     2 ' ',
     3 '   406 s1 s2 t1 t2 d',
     4 '   This instruction is the same as instruction 405, except that 
     5after it',
     6 '   completes one pass through the original atoms list, it starts
     7 another',
     8 '   through the new atoms list.  It continues until no new atoms 
     9are added.',
     a '   This can be used for building up an entire molecule at once. 
     b Note that',
     c '   the distinction between 405 and 406 is only relevant if the s
     dource and',
     e '   target ranges overlap.' /
C
C 407, lines 181 - 187
C
      data (helpinx(i,15),i=1,3) /407,181,187/
      data (helptext(i),i=181,187) /
     1 'Instruction 407:',
     2 ' ',
     3 '   407 d',
     4 '   Builds up an atoms list such that no two atoms are closer tog
     5ether than d',
     6 '   Angstroms (in any symmetry combination).  Atom 1 is automatic
     7ally added;',
     8 '   thereafter, each atom is considered in sequence and added if 
     9it satisfies',
     a '   the distance criterion with respect to the atoms already in t
     bhe list.' /
C
C 5xy, lines 201 - 222
C
      data (helpinx(i,16),i=1,3) /500,201,224/
      data (helptext(i),i=201,216) /
     1 '500-series instructions:',
     2 ' ', 
     3 '   5xy [n [i [j]]]',
     4 '   Constructs a new SDT and then restarts the program using the 
     5new SDT as',
     6 '   the input.',
     7 ' ',
     8 '   If x is --',
     9 '      0 then no special action',
     a '      1 then reset hydrogen atom thermal parameters to 1.0',
     b '      2 then reset nonhydrogen atom thermal parameters to 0.5',
     c '      3 then reset both hydrogen and nonhydrogen atom thermal pa
     drameters',
     e '      4 then resequence some or all of the atoms (see below)',
     f ' ',
     g '   If x < 4 and y is --',
     h '      0 or 1 then no special action',
     i '      > 1 then append alphabetic symmetry codes to the atom labe
     jls' /
      data (helptext(i),i=217,224) /
     1 ' ',
     2 '   n: if x < 4 then n.ne.0 causes the atoms to be sorted by ADC 
     3prior to',
     4 '      writing.  If n = 2 then the hydrogen atoms are sorted to t
     5he end of the',
     6 '      list as well.',
     8 '      if x >= 4 then n is the initial number to use in renumberi
     9ng',
     a '   i: index (in the atoms list) of the first atom to renumber',
     b '   j: index (in the atoms list) of the last atom to renumber',
     c '   defaults: n=0 if x < 4, n=1 otherwise, i=1, j=latom (last ato
     dm)' /
C
C 600s, lines 231 - 245
C
      data (helpinx(i,17),i=1,3) /600,231,245/
      data (helptext(i),i=231,244) /
     1 '600-series instructions (symmetry card operations)',
     2 ' ',
     3 '   600 [n]',
     4 '   Add (n=0, default) or modify (n.ne.0) a symmetry card.  If no
     5nzero, n is',
     6 '   the number of the card to modify.  The symmetry data entered 
     7with this',
     8 '   instruction are short form -- only the translations and the d
     9iagonal',
     a '   elements of the rotations: tx,rxx,ty,ryy,tz,rzz',
     b ' ',
     c '   601 [n1 [n2]]',
     d '   Display symmetry cards n1 through n2.  n1 defaults to 1, n2 d
     eefaults to',
     f '   the number of symmetry cards.',
     g ' ',
     h '   609 [n]',
     i '   As for 600, add or modify a symmetry card.  This version read
     js the full'/
      data helptext(245) /
     1 '   symmetry card format.' /
C
C 700s, lines 261 - 268
C
      data (helpinx(i,18),i=1,3) /700,261,268/
      data (helptext(i),i=261,268) /
     1 '700-series instructions (thermal parameter manipulation)',
     2 ' ',
     3 '   704 [n1 [n2]]',
     4 '   Converts the specified atoms'' anisotropic thermal parameters
     5 to the',
     6 '   equivalent isotropic thermal parameters.  n2 defaults to n1 i
     7f n1 is',
     8 '   positive, or to the end of the list if n1 is missing or nonpo
     9sitive.',
     a '   IMPORTANT: n1 and n2 refer to the input list, not the atoms l
     bist.  This',
     c '   change cannot be reversed without rereading the input file.'/
C
C 900s, lines 281 - 286
C
      data (helpinx(i,19),i=1,3) /900,281,286/
      data (helptext(i),i=281,286) /
     1 '900-series instructions (help)',
     2 ' ',
     3 '   9xy [n]',
     4 '   Requests help.  If n is missing or nonpositive then an index 
     5is displayed,',
     6 '   otherwise the system attempts to provide help for instruction
     7 or help',
     8 '   option n.  "help" is a synonym for 900.' /
C
C 1000s, lines 301 - 313
C
      data (helpinx(i,20),i=1,3) /1000,301,313/
      data (helptext(i),i=301,312) /
     1 '1000-series instructions (analyze and build)',
     2 ' ',
     3 '   1000 (or analyze)',
     4 '   1050 [n] (or build [n])',
     5 '   Both perform comparisons of the input empirical formula to th
     6e formula',
     7 '   implied by the current model and display the results on the c
     8onsole.',
     9 '   The build instruction then iterates 405 instructions* until n
     ao more',
     b '   nonhydrogen atoms are added, then performs one more 405 to ad
     cd the',
     d '   hydrogen atoms.  If the n parameter is present then the so sp
     eecified atom',
     f '   is added to the atoms list before the building iterations com
     gmence.',
     h ' ',
     i '   *This routine uses a special internal version of 405 which ta
     jkes atomic' /
      data helptext(313) /
     1 '   radii into account.' /
C
C 1200s, lines 321 - 332
C     
      data (helpinx(i,21),i=1,3) /1200,321,332/
      data (helptext(i),i=321,331) /
     1 '1200-series instructions (model output)',
     2 ' ',
     3 '   12xy [n [i [j]]]',
     4 '   The 1200-series instructions contain all the same subcodes an
     5d options',
     6 '   as the 500-series instructions, differing only in that they w
     7rite the',
     8 '   model output to a persistant, user-specified, external file.
     9 In addition,',
     a '   the 1250 and 1251 instructions write the contents of the the 
     bcurrent atoms', 
     c '   list to a user-specified CRT-format file (with or without shi
     dfting the ',
     e 'coordinate origin to atom 1, respectively), and the 1260 instruc
     ftion writes',
     g '   the entire contents of the input SDT to a CSSR-format file "s
     hdt.cssr".',
     i '   "Newsdt" is a synonym for 1201; "newsdtx" is a synonym for 12
     j02; "cart"'/
      data helptext(332) /
     1 '   is a synonym for 1250; and "cssr" is a synonym for 1260.' /
C
C ADCs, lines 451 - 462
C
      data (helpinx(i,2),i=1,3) /1,451,462/
      data (helptext(i),i=451,462) /
     1 'Atom Designator Codes (ADCs):',
     2 ' ',
     3 'nnnxyzss',
     4 ' | ||| |_ symmetry operation',
     5 ' | |||___ unit translations along cell axis c, +5',
     6 ' | ||____ unit translations along cell axis b, +5',
     7 ' | |_____ unit translations along cell axis a, +5',
     8 ' |_______ atom number from the input SDT',
     9 ' ',
     a 'Atom designator codes specify particular symmetry instances of i
     bndividual',
     c 'input atoms.  Atomic positions in the atoms list are identified 
     dby their',
     e 'ADCs.  The translation and symmetry parts may be omitted if they
     f are 55501.' /
C
C Atoms List, lines 471 - 480
C
      data (helpinx(i,3),i=1,3) /2,471,480/
      data (helptext(i),i=471,480) /
     1 'The Atoms List:',
     2 ' ',
     3 'Shortep maintains two lists of atoms, the Input List and the Ato
     4ms List.',
     5 'The Input List contains the atoms read from the input SDT, in th
     6e order read.',
     7 'The Atoms List is an array of ADCs built and manipulated by the 
     8400-series',
     9 'instructions.  Because of the nature of ADCs, the entries in the
     a Atoms List',
     b 'refers to the members of the Input List, and the same member of 
     cthe Input',
     d 'List may be represented more than once in the Atoms List.  The 5
     e00- and',
     f '1200-series instructions (except 1260/CSSR) write the contents o
     gf the Atoms',
     h 'List in various ways and with various effects.' /
C
C Instruction 901, lines 341 - 354
C
      data (helpinx(i,22),i=1,3) /901,341,354/
      data (helptext(i),i=341,352) /
     1 'Instruction 901: Atom relabelling',
     2 ' ',
     3 '   901 [i1 [i2]]',
     4 '   If i1 is present but i2 is missing then i2 defaults to i1.  I
     5f i1 is',
     6 '   missing then it defaults to 1 and i2 defaults to the number o
     7f input atoms.',
     8 '   The user is prompted for a new chemical symbol; a blank respo
     9nse cancels',
     a '   the renumbering.  If a chemical symbol is entered then the us
     ber is prompted',
     c '   for a new primary scattering factor; if the response is blank
     d then atomic',
     e '   scattering factors will not be modified.  Once the user has r
     fesponded, the',
     g '   program changes the chemical symbols and (if selected) the pr
     himary',
     i '   scattering factor numbers of atoms i1 through i2.',
     j ' '/
      data (helptext(i),i=353,354) /
     1 '   IMPORTANT: The changes applied by instruction 901 can only be
     2 reversed by',
     3 '   rereading the input file.'/
C
C Instruction 416, lines 361 - 7
C
      data (helpinx(i,23),i=1,3) /416,361,367/
      data (helptext(i),i=361,367) /
     1 'Instruction 416: Special convoluting sphere of enclosure elimina
     2te',
     3 ' ',
     4 '   416 s1 s2 t1 t2 d',
     5 '   This instruction is the same as instruction 415, except that 
     6if it removes',
     7 '   one symmetry copy of a target atom then it removes all symmet
     8ry copies of',
     9 '   that atom.  See "help 415" for a caveat about removing source
     a atoms.  Note',
     b '   that this instruction is not a close opposite of 406.' /
C
      end
C
C     block data iconbd
C
C     initialize the elements of common block iconbd
C
      block data iconbd
C
      integer i100,i1000,i10k,i100k,i55501
      common /icon/ i100,i1000,i10k,i100k,i55501
C
      data i100/100/,i1000/1000/,i10k/10000/,i100k/100000/,i55501/55501/
C
      end
C
C     function igetredir
C 
C     Performs a table look-up to determine the redirection index for
C     the input data label
C 
      function igetredir (label,llen)
      integer igetredir,llen
      character*(*) label
C 
      integer ng
      common /fault/ ng
C  
      integer nlab
      parameter (nlab=23)
      integer i,ifound,j,min,max,nfound(nlab),nlen(nlab),nlist(nlab)
      character*26 list(nlab)
      save list,nlist,nfound,nlen

      data list/
     1 '_atom_site_B_iso_or_equiv ', '_atom_site_U_iso_or_equiv ',
     2 '_atom_site_aniso_B_11     ', '_atom_site_aniso_B_12     ',
     3 '_atom_site_aniso_B_13     ', '_atom_site_aniso_B_22     ',
     4 '_atom_site_aniso_B_23     ', '_atom_site_aniso_B_33     ',
     5 '_atom_site_aniso_U_11     ', '_atom_site_aniso_U_12     ',
     6 '_atom_site_aniso_U_13     ', '_atom_site_aniso_U_22     ',
     7 '_atom_site_aniso_U_23     ', '_atom_site_aniso_U_33     ',
     8 '_atom_site_aniso_label    ', '_atom_site_fract_x        ',
     9 '_atom_site_fract_y        ', '_atom_site_fract_z        ',
     a '_atom_site_label          ', '_atom_site_occupancy      ',
     b '_atom_site_type_symbol    ', '_atom_type_symbol         ',
     c '_symmetry_equiv_pos_as_xyz' /
      data nlen/25,25,21,21,21,21,21,21,21,21,21,21,21,21,22,18,18,18,
     1 16,20,22,17,26/
      data nlist/-8,8,-10,-13,-14,-11,-15,-12,10,13,14,11,15,12,16,5,6,
     1 7,3,9,4,2,1/
      data nfound/nlab*0/
C 
C     If the label is longer than the longest in the list, then it
C     cannot match
C 
      if (llen.gt.26) then
         igetredir=0
         return
      end if
  
C 
C     Binary search for the current label
C 
      min=1
      max=nlab+1
      ifound=0
      do while (max-min.gt.1)
         i=(min+max)/2
         if (llen.eq.nlen(i).and.label(1:llen).eq.list(i)(1:llen)) then
            ifound=1
            exit
         else if (label(1:llen).le.list(i)(1:llen)) then
            max=i
         else if (label(1:llen).gt.list(i)(1:llen)) then
            min=i
         end if
      end do
C 
C     Retrieve the value from the table or return 0 if not found
C 
      if (ifound.eq.1) then
         j=abs(nlist(i))
         if (nfound(j).ne.0) then
            ng=21
            igetredir=0
         else
            nfound(j)=1
            igetredir=nlist(i)
         end if
      else
         igetredir=0
      end if
      return
      end
C
C     subroutine inline
C 
C  SUBROUTINE TO TAKE 80 COLUMN INPUT (LINE, 80A1) AND CONVERT
C  IT TO A SERIES OF CHARACTER CONSTANTS (LABEL, 20A8) AND double
C  precision NUMBERS (FNUM, 20DX.X).
C 
C  On input, I1 contains the maximum number of labels (the dimension
C  of array label) and I2 contains the maximum number of numbers (the
C  dimension of fnum).  These default to 20 if they are zero.  On
C  output THE NUMBER OF LABELS GENERATED IS I1, AND THE NUMBER OF
C  NUMBERS IS I2.
C 
C  IF IERR IS = 0, NO APPARENT ERRORS WERE DETECTED
C             = 1, NO INPUT WAS DETECTED (CR ONLY, I1=I2=0)
C             = 2, A CHARACTER DETECTED AFTER A NUMBER SEEN
C             = 3, MORE THAN 20 FNUMS OR LABELS
C 
C  LOWER CASE LETTERS IN THE INPUT LINE ARE ALL CONVERTED TO UPPER CASE
C  AND THE RESULTING LABELS ARE ALL LEFT JUSTIFIED.  ALL LABELS MUST
C  OCCUR BEFORE ANY NUMBERS.  THE FIRST NUMBER CAN BE SEPERATED FROM
C  THE LABEL (IF PRESENT) BY A SPACE, OR START IMMEDIATELY.  I.E. LAB 1
C  OR LAB1 BOTH GIVE ONE LABEL AND ONE FLOATING NUMBER.
C 
      subroutine inline (line,i1,i2,fnum,label,ierr)
      double precision fnum(*)
      integer i1,i2,ierr
      character*8 label(*)
      character*1 line(80)
C
      integer i,i10,i20,ilab,j1,j2,nerr
      character*80 line2
      character*8 tlab
      character*1 ct
C 
C Store array dimensions
C 
      i10=i1
      i20=i2
      if (i10.eq.0) i10=20
      if (i20.eq.0) i20=20
C 
C Copy the input line into a string
C 
      write (line2,'(80a1)') line
C 
C Initialize variables
C 
      ierr=0
      ilab=1
      i1=0
      i2=0
      j1=0
      j2=0
C 
C Loop through a series of tokens delimited by sequences of spaces
C and/or commas
C 
      do while (.true.)
C 
C   Find the initial character of the next token; exit if no more
C   tokens are present.
C 
         do j1=j2+1,80
            ct=line(j1)
            if (ct.ne.' '.and.ct.ne.',') exit
         end do
         if (j1.gt.80) exit
C 
C   Check whether it's a number.
C 
         if ((ct.ge.'0'.and.ct.le.'9').or.ct.eq.'.'.or.ct.eq.'-') ilab=0
C 
C   If we're still reading labels then...
C 
         if (ilab.eq.1) then
C 
C     Find the last character of the token.  Watch for a label token
C     concatenated with a number token.
C 
            do j2=j1,79
               if (ct.gt.'Z') line(j2)=char(ichar(ct)-32)
               ct=line(j2+1)
               if (ct.eq.' '.or.ct.eq.',') exit
               if ((ct.ge.'0'.and.ct.le.'9').or.ct.eq.'.'.or.ct.eq.'-')
     1          then
                  ilab=0
                  exit
               end if
            end do
C 
C     Store the label if there is enough room, otherwise exit with an
C     error.
C 
            i1=i1+1
            if (i1.gt.i10) then
               ierr=3
               exit
            end if
            write (tlab,'(8a1)') (line(i),i=j1,j1+7)
            label(i1)=tlab(1:j2-j1+1)//'       '
C 
C   If reading numbers then...
C 
         else
C 
C     Find the end of the token.
C 
            do j2=j1,79
               ct=line(j2+1)
               if (ct.eq.' '.or.ct.eq.',') exit
            end do
C 
C     Store the numeric value of the token, if it is valid and if there
C     is enough space.
C 
            i2=i2+1
            if (i2.gt.i20) then
               ierr=3
               exit
            end if
            read (line2(j1:j2),'(bn,d80.0)',iostat=nerr) fnum(i2)
            if (nerr.ne.0) then
               i2=i2-1
               ierr=2
               exit
            end if
         end if
      end do
C 
C No more tokens.  If input was blank then set the appropriate code.
C 
      if (ierr.eq.0.and.i1.eq.0.and.i2.eq.0) ierr=1
      return
      end
C
C     function isymhash
C
C     calculate a hash value from a two-character 
C
      function isymhash(sym)
      integer isymhash
      character*(*) sym
C
      if (len(sym).lt.2) then
         isymhash = ichar(sym(1:1)) * 256
      else if (sym(1:1).eq.' ') then
         isymhash = ichar(sym(2:2)) * 256
      else
         isymhash = ichar(sym(1:1)) * 256 + ichar(sym(2:2))
      end if
      end
C
C     subroutine paxes
C 
C     determine the principal axes of a thermal ellipsoid
C 
      subroutine paxes (iacode,rms,q,itype)
      real q(3,3),rms(3)
      integer iacode,itype
C 
C     ITYPE .LT.0 FOR COVARIANCE MATRIX IN Q
C     ITYPE .GT.0 FOR ELLIPSOID QUADRATIC FORM IN Q
C 
C     XABSF(ITYPE)=1 BASED ON TRICLINIC COORDINATE SYSTEM
C     =2 OR 3 FOR WORKING OR REFERENCE CARTESIAN SYSTEMS
C 
C     CONTRAVARIANT EIGENVECTORS FOR Q IN COLUMNS OF PAC
C 
C     CHECK ATOM CODE
C 
C     THIS ROUTINE ALSO CALCULATES THE RMS DISPLACEMENTS FOR
C     AN ATOM WITH AN ADC GIVEN BY ACODE
C 
      real a(9),aa(3,3),bb(3,3)
      common /cell/ a,aa,bb
  
      integer ng
      common /fault/ ng
  
      real ev(3,4000),p(3,4000),pa(3,3,4000)
      integer natom
      common /inputl/ ev,natom,p,pa
  
      real aarev(3,3),refv(3,3)
      common /orient/ aarev,refv
  
      real fs(3,3,192),ts(3,192)
      integer nsym
      common /symm/ fs,ts,nsym

      integer i100,i1000,i10k,i100k,i55501
      common /icon/ i100,i1000,i10k,i100k,i55501
C 
      real pac(3,3),pat(3,3),t1,x(3)
      integer i,ii,it,j,k,ks
C 
      it=iabs(itype)-1
      ks=mod(iacode,i100)
      if (nsym.lt.ks) then
         ng=4
         return
      end if
      ii=iacode/i100k
      if (natom.lt.ii.or.ii.le.0) then
         ng=5
         return
      end if
C 
C     CRYSTALLOGRAPHIC SYMMETRY ROTATION
C 
      call tmm (pa(1,1,ii),fs(1,1,ks),pat)
      if (it.gt.1) then
C 
C     TRANSFORM TO CARTESIAN SYSTEMS
C 
         call tmm (pat,aarev,pac)
      else if (it.lt.1) then
         if (itype.eq.0) then
            call tmm (pat,aarev,pac)
         else if (itype.lt.0) then
C 
C     TRANSFORM TO TRICLINIC SYSTEM
C 
            do j=1,3
               do k=1,3
                  pac(j,k)=pat(j,k)
               end do
            end do
         else
            call mm (aa,pat,pac)
         end if
      end if
C 
C     FORM DIAGONAL MATRIX OR ITS INVERSE
C 
      do j=1,3
         t1=ev(j,ii)
         if (itype.gt.0) then
            x(j)=1./(t1*t1)
         else
            x(j)=t1*t1
         end if
         rms(j)=t1
      end do
C 
C     FORM QUADRATIC FORM
C 
      do i=1,3
         do j=i,3
            t1=0.0
            do k=1,3
               t1=t1+pac(i,k)*pac(j,k)*x(k)
            end do
            q(j,i)=t1
            q(i,j)=t1
         end do
      end do
      return
      end
C
C     subroutine ppunch
C 
C     USED BY 500 and 1200 INSTRUCTIONS
C 
C     WRITES A SCRATCH FILE (UNIT 24) WITH POSITION+THERMAL
C     PARAMETERS FOR ATOMS PLACED IN THE ATOMS LIST BY THE 400
C     SERIES INSTRUCTIONS
C 
      subroutine ppunch (inst)
      integer inst
C  
      real arad(4000),radmax
      integer ntype(4000),i405,ipoly
      common /adat/ arad,radmax,i405,ipoly,ntype
  
      real atoms(3,4000)
      integer atomid(4000),latm
      common /atoml/ atomid,atoms,latm
  
      character*8 chem(4000)
      character*10 sdttit(8)
      character*12 spgtit
      common /chars/ chem,sdttit,spgtit
  
      double precision ain(30)
      integer nj,nj2
      common /instr/ ain,nj,nj2
  
      integer in,lin,lout
      common /lun/ in,lin,lout
  
      real xpop(4000),xtemp(4000)
      integer ibull1(4000),ibull2(4000)
      common /sdtat/ ibull1,ibull2,xpop,xtemp
  
      integer ixx(8000)

      integer i100,i1000,i10k,i100k,i55501
      common /icon/ i100,i1000,i10k,i100k,i55501
C 
      real q(3,3),qtherm,rms(3),v1(3)
      integer i,i1,i2,ic,ihtst,isoc,isoh,isymb,isymlist(26),ix,j,k
      integer nend,nstart,nsymb,num,isort
      character*8 cholder,ctemp1,ctemp2
C 
C Make sure the atoms list is non-empty
C 
      if (latm.le.0) then
         write (lout,10)
         inst=99
         return
      end if
C 
      if (nj2.lt.40) then
         isort=idnint(ain(1))
         if (nj2.gt.1.and.nj2.lt.40) then
            if (nj2.ge.20) then
               isoc=1
            else
               isoc=0
            end if
            isoh=mod(nj2/10,2)
            isymb=mod(nj2,10)-1
            if (isymb.gt.0) then
               isymb=1
               do nsymb=26,1,-1
                  isymlist(nsymb)=0
               end do
               open (31,file='shortep.sym',status='unknown')
               write (31,'(''Symmetry code reference'',//,''Label'',5x, 
     1                ''Code'')')
            else
               isymb=0
            end if
         else
            isoc=0
            isoh=0
            isymb=0
         end if
      else
         isort=0
      end if
      nsymb=0
      i=1
      do j=1,latm
         ixx(i)=j
         ixx(i+1)=atomid(j)
         k=atomid(j)/i100k
         call adata (chem(k),k)
         i=i+2
      end do
      if (isort.eq.2) then
         do i=2,2*latm,2
            k=ixx(i)/i100k
            if (ntype(k).eq.1) ixx(i)=ixx(i)+400000000
         end do
      end if
C 
C     Special output functions: Sort for 12xx y or for 5xx y (xx.lt.40, y.ne.0);
C                               resequence for 540 n i j;
C 
      if (nj2.lt.40) then
         nstart=latm+1
         if (isort.ne.0) then
            call sort (ixx,2*latm)
         end if
         nend=-1
      else
         num=max(idnint(ain(1)),1)
         nstart=max(1,idnint(ain(2)))
         nend=max(idnint(ain(3)),latm)
      end if
      do ic=1,latm
         i1=atomid(ixx(2*ic-1))
         call atom (i1,v1)
         ix=i1/i100k
C 
C   For a real atom:
C 
         if (ix.gt.0) then
            ihtst=0
            cholder=chem(ix)
            if (cholder(1:1).eq.'H'.and.isoh.eq.1) then
               j=ichar(cholder(2:2))
               if (j.lt.65.or.j.gt.122.or.(j.gt.90.and.j.lt.97)) ihtst=1
            end if
C 
C     If B is being reset for this atom then reset the anisotropic
C     indicator
C 
            if ((ihtst.ne.0.and.isoh.ne.0).or.(ihtst.eq.0.and.isoc.ne.0)
     1       ) ibull2(ix)=mod(ibull2(ix),100000)
C 
C     Append a symmetry operation flag if required
C 
            i2=mod(i1,i100k)
            if (isymb.ne.0.and.i2.ne.i55501) then
C 
C       Determine which symmetry code to use
C 
               do i=1,nsymb
                  if (isymlist(i).eq.i2) exit
               end do
               if (i.gt.nsymb) then
                  if (i.lt.26) then
                     nsymb=nsymb+1
                     isymlist(nsymb)=i2
                     write (31,'(2x,a1,7x,i5)') char(i+64),i2
                  else
                     write (31,'(2x,a1,7x,i5)') '*',i2
                  end if
               end if
C 
C       Modify the label
C 
               do j=1,8
                  if (cholder(j:j).eq.' ') then
                     if (i.lt.26) then
                        cholder(j:j)=char(i+64)
                     else
                        cholder(j:j)='*'
                     end if
                     exit
                  end if
               end do
C 
            end if
C 
C     Renumber if appropriate
C 
            if (ic.ge.nstart.and.ic.le.nend) then
               i=1
               do while (i.lt.8.and.(cholder(i:i).lt.'0'.or.
     1          cholder(i:i).gt.'9'))
                  i=i+1
               end do
               j=i
               i=i-1
               do while (j.lt.8.and.cholder(j:j).ge.'0'.and.
     1          cholder(j:j).le.'9')
                  j=j+1
               end do
               ctemp1=cholder(j:8)//'        '
               write (ctemp2,'(i8)') num
               k=8-int(alog10(float(num)))
               do while (cholder(i:i).eq.' '.and.i.gt.1)
                  i=i-1
               end do
               cholder=cholder(1:i)//ctemp2(k:8)//ctemp1
               num=num+1
            end if
C 
C     Output the atom
C 
            write (24,15) cholder,ibull1(ix),ibull2(ix),xpop(ix),
     1       (v1(j),j=1,3),i1
            if (ihtst.eq.1) then
               if (isoh.ne.0) then
                  qtherm=0.5
                  write (24,20) qtherm
                  cycle
               end if
            else if (isoc.eq.1) then
               qtherm=1.0
               write (24,20) qtherm
               cycle
            end if
            if (ibull2(ix).ge.100000) then
               call paxes (i1,rms,q,-1)
               do i=1,3
                  do j=1,3
                     q(j,i)=sngl(q(j,i)/5.06605918d-2)
                  end do
               end do
               if (q(1,1).eq.0.) q(1,1)=2.00001
               write (24,20) q(1,1),q(2,2),q(3,3),q(1,2),q(1,3),q(2,3),
     1          i1
            else
               write (24,25) xtemp(ix),i1
            end if
C 
C   For a dummy atom:
C 
         else
            write (24,15) 'ORIGIN  ',0,0,0.,(v1(j),j=1,3),i1
            write (24,25) 0.,i1
         end if
      end do
      if (isymb.gt.0) close (31)
      return
C
   10 format (/,' **************************************',/,'  There are
     1 no atoms in the ATOMS LIST',/,' *********************************
     2*****',/)
   15 format (a8,2i8,6x,4f10.6,2x,i8)
   20 format (6f10.6,12x,i8)
   25 format (f10.6,62x,i8)
      end
C
C     subroutine prelimcif
C 
C     Data input routine for Crystallographic Information Files
C 
      subroutine prelimcif (numhyd)
      integer numhyd
C 
      real atoms(3,4000)
      integer atomid(4000),latm
      common /atoml/ atomid,atoms,latm
  
      real a(9),aa(3,3),bb(3,3)
      common /cell/ a,aa,bb
  
      character*8 chem(4000)
      character*10 sdttit(8)
      character*12 spgtit
      common /chars/ chem,sdttit,spgtit
  
      integer lineno
      common /cif/ lineno
  
      real scalef,extinf,bx(7)
      integer isdtis(48)
      common /ends/ scalef,extinf,isdtis,bx
  
      real xpop(4000),xtemp(4000)
      integer ibull1(4000),ibull2(4000)
      common /sdtat/ ibull1,ibull2,xpop,xtemp
  
      integer ng
      common /fault/ ng
  
      real ev(3,4000),p(3,4000),pa(3,3,4000)
      integer natom
      common /inputl/ ev,natom,p,pa
  
      integer nh,nel(104)
      common /nh/ nh,nel
  
      integer in,lin,lout
      common /lun/ in,lin,lout
  
      real fs(3,3,192),ts(3,192)
      integer nsym
      common /symm/ fs,ts,nsym
C 
      integer i,iend,ieof,istart,itype,j,k
      character*255 fname
      character*80 line,line2
      character*1 reply
C 
C     Initialize
C 
      nh=0
      natom=0
      latm=0
      nsym=0
      do i=1,3
         a(i)=1.
      end do
      do i=4,6
         a(i)=0.
      end do
      do i=7,9
         a(i)=1.
      end do
      spgtit='            '
      do i=1,48
         isdtis(i)=0
      end do
      do i=1,4000
         xpop(i)=1.
         xtemp(i)=99.9999
         ibull1(i)=0
         ibull2(i)=0
         do j=1,3
            p(j,i)=9.99999
            do k=1,2
               pa(j,k,i)=0.0
            end do
            pa(1,3,i)=0.
         end do
      end do
      do i=1,104
         nel(i)=0
      end do
      scalef=1.
      extinf=0.
      ng=0
      rewind 20
      inquire (lin,name=fname)
      close (lin)
      open (lin,file=fname,access='DIRECT',form='UNFORMATTED',recl=1,
     1 status='OLD',iostat=ieof)
      if (ieof.ne.0) then
         ng=25
         return
      end if
C 
C     Find the correct data block
C 
      lineno=0
      iend=80
      reply=' '
      do while (reply.ne.'Y'.and.reply.ne.'y')
C 
C     Extract the next token
C 
         call findtoken (line,istart,iend,itype,ieof)
C 
C     Check for end-of-file or parse error
C 
         if (ieof.ne.0) then
            if (ng.eq.0) ng=20
            return
         else if (itype.eq.6) then
            ng=21
            return
C 
C     If this is a data block header then ask whether to use it
C 
         else if (itype.eq.2.and.iend-istart.ge.4) then
            if (line(istart:istart+4).eq.'data_') then
               istart=istart+4
               call gettoken (line,istart,iend,itype)
               write (lout,'('' Use data block '',a,''? '',$)')
     1          line(istart:iend)
               read (in,'(a1)') reply
               write (lout,'()')
               cycle
            end if
         end if
         if (reply.eq.' ') then
C 
C     Malformed CIF -- non-comment encountered outside any data block
C     or parse error
C 
            ng=22
            return
         end if
      end do
C 
      write (line2,'(80a1)') (' ',i=1,80)
      line2='MSC_CIF_'//line(istart:iend)
      write (23,'(a40,3a8)') line2(1:40),(sdttit(i),i=5,7)
      read (line2(1:iend-istart+4),'(8a10)') sdttit
C 
C     Now read through the CIF and store those data items we have
C     interest in
C 
      iend=80
      do while (.true.)
         call findtoken (line,istart,iend,itype,ieof)
C 
C     check for end of file condition
C 
    5    if (ieof.ne.0) exit
C 
C     check for valid token type
C 
         if (itype.lt.1.or.itype.gt.5) then
            ng=21
            return
         else if (itype.eq.1) then
C 
C     Data Name
C 
            call getvalue (line,istart,iend,ieof)
            cycle
         else if (itype.eq.2) then
C 
C     Character value -- check for "data_" or "loop_"
C 
            if (iend-istart.ge.4) then
               if (line(istart:istart+4).eq.'data_') then
                  exit
               else if (line(istart:iend).eq.'loop_') then
                  call getlooped (line,istart,iend,itype,ieof)
                  go to 5
               end if
            end if
         end if
C 
C     Unexpected data value
C 
         ng=23
         return
      end do
      call tpcnvt
      do k=1,natom
         i=2
         if (chem(k)(2:2).ge.'a'.and.chem(k)(2:2).le.'z') then
            write (chem(k)(2:2),'(a1)') char(ichar(chem(k)(2:2))-32)
         else if (chem(k)(2:2).lt.'A'.or.chem(k)(2:2).gt.'Z') then
            i=1
         end if
         j=6
         do while (chem(k)(j:j).eq.' '.and.j.gt.1)
            j=j-1
         end do
         chem(k)=chem(k)(1:i)//'('//chem(k)(i+1:j)//')'
      end do
      write (lout,'(i6,'' ATOMS WERE ON TAPE 20'')') natom
      isdtis(2)=nsym
      numhyd=nh
      do i=1,isdtis(1)
         isdtis(i*2+16)=nel(isdtis(i*2+15))
      end do
C 
C     Prepare SDT header on unit 23
C 
      write (23,'(6f10.5)') (a(i),i=1,6)
      write (23,'(6f10.5,f10.3)') bx
      write (23,'(a12)') spgtit
      write (23,'(2i4,8x,i4)') isdtis(1),isdtis(2),isdtis(5)
      write (23,'(a1/)') (' ',i=1,isdtis(1))
      write (23,'(16i4)') (isdtis(i+16),i=1,2*isdtis(1))
      write (23,'(/)')
      write (23,'(3(f15.9,3i3))') ((ts(j,i),(nint(fs(k,j,i)),k=1,3),
     1 j=1,3),i=1,nsym)
      return
      end
C
C     subroutine prime
C 
C     GENERAL INITIALIZATION OF PRIME PARAMETERS
C 
      subroutine prime
C 
      real arad(4000),radmax
      integer ntype(4000),i405,ipoly
      common /adat/ arad,radmax,i405,ipoly,ntype
  
      real atoms(3,4000)
      integer atomid(4000),latm
      common /atoml/ atomid,atoms,latm
  
      integer ng
      common /fault/ ng
  
      double precision ain(30)
      integer nj,nj2
      common /instr/ ain,nj,nj2
C 
      integer i
C 
C     CLEAR THE FLAGS IN AIN(25-30)
C 
      do i=25,30
         ain(i)=0d0
      end do
C 
C     SET CONSTANTS
C 
      i405=0
      radmax=1.
      latm=0
      ng=0
C 
      return 
      end
  
C 
C     subroutine readlinedirect
C 
      subroutine readlinedirect (iunit,line,ieof)
      integer iunit,ieof
      character*80 line
C 
C     Reads a line of characters (up to 80 characters) from unit iunit
C     opened for unformatted direct access
C 
      integer ng
      common /fault/ ng
C  
      integer nchar,nfp,ich
      character ch
C 
C     initialize
C 
      line=' '
      inquire (iunit,nextrec=nfp,iostat=ieof)
      if (nfp.eq.0.and.ieof.eq.0) ieof=-1
      if (ieof.ne.0) return
C 
C     read up to 80 characters into line
C 
      do nchar=1,80
C 
C        read a character; exit on error
C 
         read (iunit,rec=nfp,iostat=ieof) ch
         if (ieof.ne.0) exit
C 
C        increment the record pointer and extract the ASCII code for the
C        character
C 
         nfp=nfp+1
         ich=ichar(ch)
C 
C        Exit if we read a line terminator
C 
         if (ich.eq.10.or.ich.eq.12.or.ich.eq.13) then
C 
C           For a carriage return (ASCII 13 decimal) an immediately
C           subsequent line feed (ASCII 10 decimal) is part of the same
C           line terminator
C 
            if (ich.eq.13) then
               read (iunit,rec=nfp,iostat=ieof) ch
               if (ieof.ne.0) exit
               if (ichar(ch).eq.10) nfp=nfp+1
            end if
            exit
C 
C        Check for illegal characters
C 
         else if ((ich.ne.9.and.ich.ne.11.and.ich.lt.32).or.
     1            (ich.gt.126)) then
            ng=24
            return
         end if
C 
C        Insert the character just read into the line
C 
         line(nchar:nchar)=ch
      end do
C 
C     If we have read 80 characters without seeing a line terminator
C     then read through the line terminator
C 
      do while (ich.ne.10.and.ich.ne.12.and.ich.ne.13.and.ieof.eq.0)
         read (iunit,rec=nfp,iostat=ieof) ch
         if (ieof.ne.0) exit
         nfp=nfp+1
         ich=ichar(ch)
         if (ich.eq.13) then
            read (iunit,rec=nfp,iostat=ieof) ch
            if (ieof.ne.0) exit
            if (ichar(ch).eq.10) nfp=nfp+1
         end if
      end do
C 
C     Make sure the file is positioned correctly for the next line.
C     Fixes up for bare carriage returns (i.e. with no line feeds)
C 
      if (ieof.eq.0) read (iunit,rec=nfp-1,iostat=ieof) ch
      end
  
C 
C     subroutine relabel
C 
      subroutine relabel
C
      character*8 chem(4000)
      character*10 sdttit(8)
      character*12 spgtit
      common /chars/ chem,sdttit,spgtit
  
      real ev(3,4000),p(3,4000),pa(3,3,4000)
      integer natom
      common /inputl/ ev,natom,p,pa
  
      double precision ain(30)
      integer nj,nj2
      common /instr/ ain,nj,nj2
  
      integer in,lin,lout
      common /lun/ in,lin,lout
  
      real xpop(4000),xtemp(4000)
      integer ibull1(4000),ibull2(4000)
      common /sdtat/ ibull1,ibull2,xpop,xtemp
C  
      integer i,i1,i2,len1,len2,newpsf
      character*2 newlab
C 
      newlab='  '
      i1=idnint(ain(1))
      i2=min(iabs(idnint(ain(2))),natom)
      if (i2.eq.0) then
         if (i1.ne.0) then
            i2=i1
         else
            i2=natom
         end if
      end if
      i1=max(i1,1)
      write (lout,'('' Type new symbol for input atoms '',i3,'' to '',  
     1 i3,'' [Leave blank to cancel]: '',$)') i1,i2
      read (in,'(a2)') newlab
      if (newlab.ne.'  ') then
         if (newlab(2:2).eq.' ') then
            len1=1
         else
            len1=2
         end if
         write (lout,'('' Type new primary scattering factor [blank to''
     1    ,'' leave unchaged]: '',$)')
         read (in,'(i3)') newpsf
         newpsf=newpsf*1000
         do i=i1,i2
            do len2=1,8
               if ((chem(i)(len2:len2).lt.'A').or.(chem(i)(len2:len2)
     1             .gt.'z').or.((chem(i)(len2:len2).gt.'Z').and.
     2             (chem(i)(len2:len2).lt.'a'))) exit
            end do
            if (len2.lt.9) then
               chem(i)=newlab(1:len1)//chem(i)(len2:8)//'      '
            else
               chem(i)=newlab
            end if
            if (newpsf.ne.0) ibull1(i)=mod(ibull1(i),1000)+newpsf
         end do
      end if
      return
      end
C
C     subroutine saveit
C 
      subroutine saveit (line,istart,iend,itype,ifld,irec)
      integer iend,ifld,irec,istart,itype
      character*80 line
C 
      real arad(4000),radmax
      integer ntype(4000),i405,ipoly
      common /adat/ arad,radmax,i405,ipoly,ntype
  
      character*8 chem(4000)
      character*10 sdttit(8)
      character*12 spgtit
      common /chars/ chem,sdttit,spgtit
  
      real scalef,extinf,bx(7)
      integer isdtis(48)
      common /ends/ scalef,extinf,isdtis,bx
  
      real ev(3,4000),p(3,4000),pa(3,3,4000)
      integer natom
      common /inputl/ ev,natom,p,pa
  
      integer nh,nel(104)
      common /nh/ nh,nel
  
      real xpop(4000),xtemp(4000)
      integer ibull1(4000),ibull2(4000)
      common /sdtat/ ibull1,ibull2,xpop,xtemp
C  
      real val,esu
      integer anisoinx(4000),i,ilen,ilen2
      character*80 string
      save anisoinx
      data anisoinx/4000*0/
C 
      ilen=iend-istart+1
      ilen2=ilen
      if (itype.eq.2) then
         if (line(istart:istart).eq.''''.or.line(istart:istart).eq.'"')
     1    then
            string=line(istart+1:iend-1)
            ilen=ilen-2
         else
            string=line(istart:iend)
         end if
         val=0.
         esu=0.
         if (ilen.lt.8) write (string(ilen+1:ilen+8),'(''        '')')
      else if (itype.eq.3) then
C 
C     The assignment to string is necessary to cover certain cases
C     of CIF non-compliance (unquoted character fields which begin
C     with a digit, +, -, or . .
C 
         string=line(istart:iend)
         call extract (string,ilen,val,esu)
      else
         ilen=1
         string='        '
         val=0.
         esu=0.
      end if
      go to (5,10,15,20,25,30,35,40,45,50,55,60,65,70,75,80),abs(ifld)
      return
C 
C     a symmetry card
C 
    5 if (itype.eq.2.or.itype.eq.3) call storesymm (string,ilen2,irec)
      return
C 
C     an atom type symbol
C 
   10 if (itype.eq.2.and.irec.lt.17) then
         call adata (string(1:8),4000)
         isdtis(1)=irec
         isdtis(irec*2+15)=ntype(4000)
      end if
      return
C 
C     an atom label
C 
   15 if (itype.eq.2.and.irec.lt.1000) then
         natom=irec
         chem(irec)=string(1:8)
         call adata (chem(irec),irec)
         if (ntype(irec).eq.1) nh=nh+1
         do i=1,isdtis(1)
            if (ntype(irec).eq.isdtis(i*2+15)) then
               ibull1(irec)=i*1000
               return
            end if
         end do
         anisoinx(irec)=irec
      end if
      return
C 
C     atom site type symbol  (not currently used)
C 
   20 return
C 
C     x coordinate
C 
   25 if (itype.eq.3.and.irec.lt.1000) then
         natom=irec
         p(1,irec)=val
      end if
      return
C 
C     y coordinate
C 
   30 if (itype.eq.3.and.irec.lt.1000) then
         natom=irec
         p(2,irec)=val
      end if
      return
C 
C     z coordinate
C 
   35 if (itype.eq.3.and.irec.lt.1000) then
         natom=irec
         p(3,irec)=val
      end if
      return
C 
C     Isotropic or equivalent thermal parameter
C 
   40 if (itype.eq.3.and.irec.lt.1000) then
         natom=irec
         if (ifld.lt.0) then
            pa(1,1,irec)=val
         else
            pa(1,1,irec)=sngl(val*78.95683521d0)
         end if
         xtemp(irec)=pa(1,1,irec)
      end if
      return
C 
C     Population factor
C 
   45 if (itype.eq.3.and.irec.lt.1000) then
         natom=irec
         xpop(irec)=1.-val
      end if
      return
C 
C     U_11 / B_11
C 
   50 if (itype.eq.3.and.irec.lt.1000.and.anisoinx(irec).gt.0) then
         pa(1,1,anisoinx(irec))=val
         if (ifld.gt.0) then
            pa(1,3,anisoinx(irec))=8.
         else
            pa(1,3,anisoinx(irec))=4.
         end if
      end if
      return
C 
C     U_22 / B_22
C 
   55 if (itype.eq.3.and.irec.lt.1000.and.anisoinx(irec).gt.0) then
         pa(2,1,anisoinx(irec))=val
         if (ifld.gt.0) then
            pa(1,3,anisoinx(irec))=8.
         else
            pa(1,3,anisoinx(irec))=4.
         end if
      end if
      return
C 
C     U_33 / B_33
C 
   60 if (itype.eq.3.and.irec.lt.1000.and.anisoinx(irec).gt.0) then
         pa(3,1,anisoinx(irec))=val
         if (ifld.gt.0) then
            pa(1,3,anisoinx(irec))=8.
         else
            pa(1,3,anisoinx(irec))=4.
         end if
      end if
      return
C 
C     U_12 / B_12
C 
   65 if (itype.eq.3.and.irec.lt.1000.and.anisoinx(irec).gt.0) then
         pa(1,2,anisoinx(irec))=val
         if (ifld.gt.0) then
            pa(1,3,anisoinx(irec))=8.
         else
            pa(1,3,anisoinx(irec))=4.
         end if
      end if
      return
C 
C     U_13 / B_13
C 
   70 if (itype.eq.3.and.irec.lt.1000.and.anisoinx(irec).gt.0) then
         pa(2,2,anisoinx(irec))=val
         if (ifld.gt.0) then
            pa(1,3,anisoinx(irec))=8.
         else
            pa(1,3,anisoinx(irec))=4.
         end if
      end if
      return
C 
C     U_23 / B_23
C 
   75 if (itype.eq.3.and.irec.lt.1000.and.anisoinx(irec).gt.0) then
         pa(3,2,anisoinx(irec))=val
         if (ifld.gt.0) then
            pa(1,3,anisoinx(irec))=8.
         else
            pa(1,3,anisoinx(irec))=4.
         end if
      end if
      return
C 
C     anisotropic parameters atom label
C 
   80 if (itype.eq.2.and.irec.lt.1000) then
         do i=1,natom
            if (string(1:8).eq.chem(i)) then
               anisoinx(irec)=i
               ibull2(i)=100000
               go to 85
            end if
         end do
      end if
   85 return
      end
C
C     subroutine searc
C
C     Performs searches for 101, 102, 404, 405, 406, 414, 415, and 416
C     instructions
C
      subroutine searc
C 
      real arad(4000),radmax
      integer ntype(4000),i405,ipoly
      common /adat/ arad,radmax,i405,ipoly,ntype
 
      real atoms(3,4000)
      integer atomid(4000),latm
      common /atoml/ atomid,atoms,latm
  
      real a(9),aa(3,3),bb(3,3)
      common /cell/ a,aa,bb
 
      character*8 chem(4000)
      character*10 sdttit(8)
      character*12 spgtit
      common /chars/ chem,sdttit,spgtit

      integer ng
      common /fault/ ng

      real ev(3,4000),p(3,4000),pa(3,3,4000)
      integer natom
      common /inputl/ ev,natom,p,pa
 
      double precision ain(30)
      integer nj,nj2
      common /instr/ ain,nj,nj2

      integer in,lin,lout
      common /lun/ in,lin,lout

      real aarev(3,3),refv(3,3)
      common /orient/ aarev,refv

      real fs(3,3,192),ts(3,192)
      integer nsym
      common /symm/ fs,ts,nsym

      integer i100,i1000,i10k,i100k,i55501
      common /icon/ i100,i1000,i10k,i100k,i55501
C 
      real arccos,dmax,dmx,dsq,dx(3),f,s2(200),t1,t3,t9,tem,tt,u(3),v(3)
      real v1(3),v2(3),vmv,vv,w(2,4),ww(2,3),x(3),y(3),z(3)
      integer i,i1,i2,i3,ii,ij,itar1,itar2,itom,itom1,itom2,izmax,izmin
      integer izsto,j,j1,j2,k,kfud,kfun,kfun2,l,l2,l3,l4,l5,last,latom
      integer list,m,m1,m2,m3,m4,m5,n,n1,n2,n3,n4,n5,nn,num,nw(6)
      integer s1d(200),syito2,syitom
C 
C     OBTAIN PROBLEM PARAMETERS
C
      i1=idnint(ain(1))
      i2=iabs(idnint(ain(2))) 
      if (i1.le.10000) then
         if (i1.le.0) then
            itom1=1
         else
            itom1=i1
         end if
         syitom=55501
      else
         itom1=i1/i100k
         syitom=mod(i1,i100k)
      end if
      if (i2.le.10000) then
         if (i2.eq.0) then
            itom2=natom
         else
            itom2=i2
         end if
         syito2=syitom
      else
         itom2=i2/i100k
         syito2=mod(i2,i100k)
      end if
      itar1=idnint(ain(3))
      if (itar1.le.0) itar1=1
      itar2=iabs(idnint(ain(4)))
      if (itar2.eq.0) itar2=natom
      if (i405.eq.1) then
         dmax=2*radmax
         ain(5)=dble(dmax)
      else
         if (ain(5).le.0d0) ain(5)=2d0
         dmax=sngl(ain(5))
      end if
      dmx=dmax*dmax
      tem=.01
      kfun=nj*100+mod(nj2,10)
      kfud=kfun
      if (ain(30).eq.1d0) kfud=kfud+90
      k=nj*100+nj2
      latom=latm
      do j=1,4
         w(1,j)=99.
         w(2,j)=-99.
      end do
C 
C     Determine the x, y, z, and x-y limits of the target atom
C     coordinates
C 
      do i=itar1,itar2
         call atom (i*i100k,x)
C 
C     ORTEP ERROR MESSAGES ARE NOT USED IN THIS VERSION
C 
         if (ng.ne.0) then
            ng=0
            go to 150
         end if
         do j=1,3
            tem=x(j)
            if (w(2,j).lt.tem) w(2,j)=tem
            if (tem.lt.w(1,j)) w(1,j)=tem
         end do
         tem=x(1)-x(2)
         if (w(2,4).lt.tem) w(2,4)=tem
         if (tem.lt.w(1,4)) w(1,4)=tem
      end do
      kfun2=mod(kfun,10)
      go to (35,35,40,45,35,35),kfun2
C 
C     FIND PARALLELEPIPED WHICH ENCLOSES DMAX SPHERE
C 
   35 t1=1.-a(4)*a(4)-a(5)*a(5)-a(6)*a(6)+2.*a(4)*a(5)*a(6)
      do j=1,3
         dx(j)=sqrt((1.-a(j+3)**2)/t1)*dmax/a(j)
      end do
      go to 50
C 
C     FIND PARALLELEPIPED WHICH ENCLOSES RECTANGULAR BOX
C 
   40 do j=1,3
         dx(j)=0.
         do i=1,3
            t9=sngl(ain(i+4))
            dx(j)=dx(j)+abs(refv(j,i)*t9)
         end do
      end do
      go to 50
C 
C     FIND PARALLELEPIPED WHICH ENCLOSES TRICLINIC BOX
C 
   45 do j=1,3
         dx(j)=sngl(ain(j+4))
      end do
C 
C     START SEARCH AROUND REFERENCE ATOMS
C 
   50 list=0
      last=0
      m1=itom1
      n1=itom2
      if (kfun2.lt.5) go to 65
C 
C     CONVOLUTE AND REITERATIVE CONVOLUTE INSTRUCTIONS
C 
      if (latm.le.0) then
C 
C     FAULT, NO ENTRIES IN ATOMS LIST
C 
c        ng=12
C 
C     ORTEP ERROR MESSAGES ARE NOT USED IN THIS VERSION
C 
         ng=0
         go to 150
      end if
C 
C     CHECK FOR REFERENCE ATOMS IN ATOMS LIST
C 
   55 if (last.ge.latm) go to 150
      list=last
      last=latm
   60 list=list+1
      if (list.gt.min(last,latm)) go to 145
      itom=atomid(list)/i100k
      if (itom.lt.itom1.or.itom.gt.itom2) go to 60
      syitom=mod(atomid(list),i100k)
      syito2=syitom
      m1=itom
      n1=itom
C 
C     SET INITIAL RUN PARAMETERS
C 
   65 m2=mod(syitom,100)
      m5=mod(syitom/100,1000)
      m3=m5/100
      m4=mod(m5/10,10)
      m5=mod(m5,10)
C 
C     SET TERMINAL RUN PARAMETERS
C 
      n2=mod(syito2,100)
      n5=mod(syito2/100,1000)
      n3=n5/100
      n4=mod(n5/10,10)
      n5=mod(n5,10)
C 
C     START SEARCH AROUND REFERENCE ATOMS
C 
      do 144 l5=m5,n5
         do 143 l4=m4,n4
         do 142 l3=m3,n3
         do 141 l2=m2,n2
         do 140 itom=m1,n1
         i3=itom*i100k+l3*i10k+l4*i1000+l5*i100+l2
         call atom (i3,y)
         if (ng.ne.0) then
C 
C     ORTEP ERROR MESSAGES ARE NOT USED IN THIS VERSION
C 
            ng=0
            go to 140
         end if
C 
C     K=SYMMETRY EQUIVALENT POSITION
C 
         num=0
         do 130 k=1,nsym
C 
C     SUBTRACT SYMMETRY TRANSLATION FROM REFERENCE ATOM
C 
            do j=1,3
               u(j)=y(j)-ts(j,k)
            end do
C 
C     DETERMINE LIMITING CELLS TO BE SEARCHED
C     FIRST,MOVE THE BOX THROUGH THE SYMMETRY OPERATION
C 
            do j=1,3
               do l=1,2
                  ww(l,j)=0.0
                     do i=1,3
                     tem=fs(i,j,k)
                     if (tem.ne.0.) then
                        if (tem.lt.0.) then
                           n=mod(l,2)+1
                        else if (tem.gt.0.) then
                           n=l
                        end if
                        ww(l,j)=ww(l,j)+w(n,i)*tem
                     end if
                  end do
               end do
            end do
C 
C     CHECK FOR MIXED INDEX TRANSFORMATION
C 
            do j=1,2
               tem=fs(1,j,k)
               if (tem.eq.-fs(2,j,k)) then
                  if (tem.lt.0.) then
                     ww(1,j)=w(2,4)*tem
                     ww(2,j)=w(1,4)*tem
                  else if (tem.gt.0.) then
                     ww(1,j)=w(1,4)*tem
                     ww(2,j)=w(2,4)*tem
                  end if
               end if
            end do
C 
C     MOVE 4 CELLS AWAY THEN MOVE BACK UNTIL PARALLELEPIPED AROUND
C         REF ATOM AND BOX AROUND TRANSFORMED ASYM UNIT INTERSECT
C 
            n=0
            do j=1,3
               do i=1,2
                  n=n+1
                  tt=(u(j)-ww(i,j))*float(i*2-3)-dx(j)
                  tem=4.0
                  do while (tem+tt.gt.0.)
                     tem=tem-1.0
                  end do
                  nw(n)=nint(tem)*(i*2-3)+5
               end do
C 
C     IF NO POSSIBILITY OF A HIT, GO TO NEXT SYMMETRY OPER
C 
               if (nw(n).lt.nw(n-1)) go to 130
            end do
C 
C     L CELL TRANSLATIONS IN X
C 
            do 128 l=nw(1),nw(2)
               v(1)=u(1)+float(l-5)
C 
C     M CELL TRANSLATIONS IN Y
C 
               do 127 m=nw(3),nw(4)
               v(2)=u(2)+float(m-5)
C 
C     N CELL TRANSLATIONS IN Z
C 
               do 126 nn=nw(5),nw(6)
               v(3)=u(3)+float(nn-5)
C 
C     I = TARGET ATOM
C 
               do 125 i=itar1,itar2
               if (nj2.gt.10.and.i.eq.itom) goto 125
               do j=1,3
                  tem=0.0
                  do ii=1,3
                     tem=tem+fs(ii,j,k)*p(ii,i)
                  end do
C 
C     SEE IF WITHIN PARALLELEPIPED
C 
                  tem=tem-v(j)
                  if (abs(tem).gt.dx(j)) go to 125
C
C     Store the difference vector in x
C
                  x(j)=tem
C
               end do
               go to (85,85,80,90,85,85),kfun2
C 
C     SEE IF WITHIN MODEL BOX
C 
   80          call vm (x,aarev,v1)
               do j=1,3
                  if (ain(j+4).lt.dble(abs(v1(j)))) go to 125
               end do
               go to 90
C 
C     SEE IF WITHIN SPHERE
C 
   85          dsq=vmv(x,aa,x)
               if (i405.eq.1) then
                  dmx=(arad(itom)+arad(i))*(arad(itom)+arad(i))
               end if
               if (dsq.gt.dmx) go to 125
               if (dsq.lt..0001.and.kfun.lt.402) go to 125
               tem=sqrt(dsq)
               if (ain(8).gt.0d0.and.latm.gt.0) then
C 
C     SELECT ONLY FIRST ASYMMETRIC UNIT ENCOUNTERED
C 
                  izmin=i*i100k
                  izmax=izmin+i100k
                  do j=1,latm
                     izsto=atomid(j)
                     if (izsto.ge.izmin.and.izsto.le.izmax) go to 125
                  end do
               end if
C 
C     SELECT VECTORS ACCORDING TO CODES IF ANY
C 
   90          i3=i100k*i+(1110-l*100-m*10-nn)*100+k
               if (kfun.ge.402) go to 120
C 
C     DETERMINE CORRECT POSITION IN SORTED VECTOR TABLE
C 
               do 100 ii=1,num
                  tt=s2(ii)-tem
                  if (abs(tt).gt.0.0001) then
                     if (tt.lt.0.) go to 100
C 
C     MOVE LONGER VECTORS TOWARD END OF TABLE
C 
                     if (num.ge.200) num=199
                     ij=num
                     do j=ii,num
                        s1d(ij+1)=s1d(ij)
                        s2(ij+1)=s2(ij)
                        ij=ij-1
                     end do
                     go to 110
                  end if
C 
C     CHECK FOR DUPLICATE VECTORS IF DISTANCES ARE EQUAL
C 
                  call atom (s1d(ii),z)
                  do j=1,3
                     if (abs(x(j)+y(j)-z(j)).gt.0.0001) go to 100
                  end do
                  go to 125
  100          continue
               if (num.gt.200) go to 115
C 
C     STORE THE RESULT IN VECTOR TABLE
C 
               ii=num+1
  110          num=num+1
               s1d(ii)=i3
               s2(ii)=tem
  115          if (kfun.lt.106) go to 125
C 
C     Adjust ATOMS TABLE
C 
  120          if (ain(27).ge.0d0) then
C
C       Delete atom
C
                  do j=1,3
                     v1(j)=x(j)+y(j)
                  end do
                  call stor (i3,v1)
               else
C
C       Add atom
C
                  do i2=1,latm
                     if (atomid(i2)/i100k.eq.i) then
                        do ij=1,3
                           v1(ij)=atoms(ij,i2) 
                        end do
                        i3=atomid(i2) 
                        call stor(i3,v1)
                     end if 
                  end do
               end if
C
               if ((nj2.eq.7.or.nj2.gt.10).and.dsq.gt.0.0001) 
     1          write (lout,155) chem(itom),chem(i),tem
  125       continue
  126       continue
  127       continue
  128       continue
  130    continue
C 
C     PRINT OUT DISTANCES
C 
         if (num.le.0) go to 140
         do 135 i=1,num
            i1=s1d(i)/i100k
            i2=mod(s1d(i),i100k)
            call atom (s1d(i),z)
            if (kfud.eq.101) write (lout,155) chem(itom),chem(i1),s2(i)
C 
C     KFUD = KFUD +90 IF THE 301 FLAG IS ON FOR ADC'S
C 
            if (kfud.eq.191) write (lout,160) chem(itom),chem(i1),s2(i),
     1       itom,i1,i2
  135    continue
C 
C     CALCULATE ANGLES ABOUT REF ATOM IF CODE IS 102
C 
         if (kfun.eq.102) then
            do i=1,num-1
               t3=s2(i)
               i1=s1d(i)/i100k
               i2=mod(s1d(i),i100k)
               call atom (s1d(i),x)
               call difv (x,y,u)
               call mv (aa,u,v2)
               m=i+1
               do j=m,num
                  j1=s1d(j)/i100k
                  j2=mod(s1d(j),i100k)
                  call atom (s1d(j),z)
                  call difv (z,y,v)
                  f=arccos(vv(v,v2)/(t3*s2(j)))
                  if (kfud.eq.102) write (lout,165) chem(i1),chem(itom),
     1             chem(j1),f
                  if (kfud.eq.192) write (lout,170) chem(i1),chem(itom),
     1             chem(j1),f,i1,i2,itom,j1,j2
               end do
            end do
         end if
  140 continue
  141 continue
  142 continue
  143 continue
  144 continue
      if (list.lt.last) go to 60
  145 if (kfun2.eq.6) go to 55
  150 if (kfun.eq.106) latm=latom
      return
C 
  155 format (' ',2(a8,1x),5x,'D =',f6.3)
  160 format (' ',2(a8,1x),5x,'D =',f6.3,i6,i6,i5)
  165 format (' ',3(a8,1x),5x,'A =',f6.2)
  170 format (' ',3(a8,1x),5x,'A =',f6.2,i6,2i5,i6,i5)
      end
C
C     subroutine storesymm
C 
C     interprets CIF-fromat symmetry operations and stores them in the
C     symmetry arrays.
C 
      subroutine storesymm (string,ilen,irec)
      integer ilen,irec
      character*80 string
C 
      integer ng
      common /fault/ ng
  
      real fs(3,3,192),ts(3,192)
      integer nsym
      common /symm/ fs,ts,nsym
C 
      real denom,anum
      integer i,ifld,len,nflag
C 
C     Initialize
C 
      ifld=1
      i=1
      nflag=0
C 
C     Scan through the string
C 
      do while (i.le.ilen)
         if (string(i:i).eq.',') then
            ifld=ifld+1
            nflag=0
            if (ifld.gt.3) return
         else if (string(i:i).eq.' ') then
            continue
         else if (string(i:i).eq.'-') then
            nflag=1
         else if (string(i:i).eq.'x') then
            if (nflag.eq.0) then
               fs(1,ifld,irec)=1.
            else
               fs(1,ifld,irec)=-1.
               nflag=0
            end if
         else if (string(i:i).eq.'y') then
            if (nflag.eq.0) then
               fs(2,ifld,irec)=1.
            else
               fs(2,ifld,irec)=-1.
               nflag=0
            end if
         else if (string(i:i).eq.'z') then
            if (nflag.eq.0) then
               fs(3,ifld,irec)=1.
            else
               fs(3,ifld,irec)=-1.
               nflag=0
            end if
         else if (string(i:i).eq.'+') then
            continue
         else if ((string(i:i).ge.'0'.and.string(i:i).le.'9')
     1            .or.(string(i:i).eq.'.')) then
C 
C     The extract subroutine was designed to read numbers with
C     associated, parenthesized s.u.'s, but it is loosely enough
C     written that it will also read plain decimal numbers and
C     fractions.  For fractions, the numerator will be returned
C     in val, the denominator in esu.  For non-fractions, esu
C     will be zero.  The routine will fail for fractions with
C     decimal points in the numerator or denominator.
C 
            len=ilen-i+1
            call extract (string(i:ilen),len,anum,denom)
            i=i+len-1
            if (denom.ne.0.) anum=anum/denom
            if (nflag.ne.0) then
               anum=-anum
               nflag=0
            end if
            ts(ifld,irec)=anum
         else
            ng=23
            return
         end if
         i=i+1
      end do
      nsym=irec
      return
      end
C
C     subroutine tmm
C 
C     TRANSPOSE(TRANSPOSE(X)*Y) = Z
C 
      subroutine tmm (x,y,z)
      real x(3,3),y(3,3),z(3,3)
C 
      integer i,k
C 
      do i=1,3
         do k=1,3
            z(k,i)=x(1,i)*y(1,k)+x(2,i)*y(2,k)+x(3,i)*y(3,k)
         end do
      end do
      return
      end
C
C     subroutine vm
C 
C     TRANSPOSED VECTOR TIMES MATRIX
C 
C           Z(3)=X(3)*Y(3,3)
C 
      subroutine vm (x,y,z)
      real x(3),y(3,3),z(3)
C 
      integer i,j
C 
      do j=1,3
         z(j)=0.0
         do i=1,3
            z(j)=z(j)+x(i)*y(i,j)
         end do
      end do
      return
      end
