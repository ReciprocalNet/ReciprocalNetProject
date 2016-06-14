C
C     function arccos
C 
C     calculates ARCCOS(Y) IN DEGREES
C 
      function arccos (y)
      real arccos,x,y
C
      x=y
      if (1.0.lt.abs(x)) x=sign(1.0,x)
      if (x.lt.-1e-8) then
         arccos=sngl(180.0+atan(sqrt(1.0-x*x)/x)*57.29577951d0)
      else if (x.gt.1e-8) then
         arccos=sngl(atan(sqrt(1.0-x*x)/x)*57.29577951d0)
      else
         arccos=90.0
      end if
      return
      end
C
C     subroutine atom
C 
C     RETURN ATOM COORDINATES IN Z ARRAY FOR A GIVEN ADC iA
C 
      subroutine atom (ia,z)
      real z(3)
      integer ia
C 
      integer ng
      common /fault/ ng
  
      real ev(3,4000),p(3,4000),pa(3,3,4000)
      integer natom
      common /inputl/ ev,natom,p,pa
  
      real fs(3,3,192),ts(3,192)
      integer nsym
      common /symm/ fs,ts,nsym

      integer i100,i1000,i10k,i100k,i55501
      common /icon/ i100,i1000,i10k,i100k,i55501
C 
      real x(3)
      integer j,k,k1,k2,k3,ks,ksym,kt
C 
      k=ia/i100k
      if (k.lt.0) then
         ng=5
         return
      else if (k.eq.0) then
         x(1)=0.0
         x(2)=0.0
         x(3)=0.0
      else if (k.gt.natom) then
         ng=5
         return
      else
         do j=1,3
            x(j)=p(j,k)
         end do
      end if
      ksym=mod(iabs(ia),i100k)
      kt=ksym/100
      ks=mod(ksym,100)
      if (ks.gt.nsym.or.ks.lt.0.or.kt.lt.0) then
         ng=4
         return
      end if
      if (ks.eq.0) then
         z(1)=x(1)
         z(2)=x(2)
         z(3)=x(3)
      else
         do k=1,3
            z(k)=ts(k,ks)
            do j=1,3
               z(k)=z(k)+fs(j,k,ks)*x(j)
            end do
         end do
      end if
      if (kt.gt.0.and.kt.ne.555) then
         k1=kt/100
         k=mod(kt,100)
         k2=k/10
         k3=mod(k,10)
         z(1)=z(1)+float(k1-5)
         z(2)=z(2)+float(k2-5)
         z(3)=z(3)+float(k3-5)
      end if
      return
      end
C
C     subroutine axeqb
C 
C     SOLUTION OF MATRIX EQUATION AX=B FOR X
C     USES METHOD OF TRIANGULAR ELIMINATION
C     B, X, and A HAVE DIMENSIONS (3,3) TO INVERT
C     A,MAKE B 3 BY 3 IDENITY MATRIX
C 
      subroutine axeqb (a1,x,b1,nv)
      real a1(3,3),b1(3,3),x(3,3)
      integer nv
C
      real a(3,3),b(3,3),r,s,tem
      integer i,j,k,l,m,n
C 
C     TRANSFER DATA
C 
      do i=1,3
         do j=1,3
            a(i,j)=a1(i,j)
            if (j.le.nv) b(i,j)=b1(i,j)
         end do
      end do
C 
C     TRIANGULARIZE MATRIX A
C 
      l=0
      do i=1,2
         s=0.0
         do j=i,3
            r=abs(a(j,i))
            if (r.ge.s) then
               s=r
               l=j
            end if
         end do
         if (l.ne.i) then
            do j=i,3
               s=a(i,j)
               a(i,j)=a(l,j)
               a(l,j)=s
            end do
            do j=1,nv
               s=b(i,j)
               b(i,j)=b(l,j)
               b(l,j)=s
            end do
         end if
         if (a(i,i).ne.0.) then
            do j=i+1,3
               if (a(j,i).ne.0.) then
                  s=a(j,i)/a(i,i)
                  a(j,i)=0.0
                  do k=i+1,3
                     a(j,k)=a(j,k)-a(i,k)*s
                  end do
                  do k=1,nv
                     b(j,k)=b(j,k)-b(i,k)*s
                  end do
               end if
            end do
         end if
      end do
C 
C     MODIFY SINGULAR MATRIX
C 
      do i=1,3
         if (a(i,i).eq.0.) a(i,i)=amax1(1.e-18,amax1(a(1,1),a(2,2),
     1    a(3,3))*1.e-12)
      end do
      do k=1,nv
         do i=1,3
            n=4-i
            m=n+1
            tem=b(n,k)
            do j=m,3
               tem=tem-a(n,j)*b(j,k)
            end do
            b(n,k)=tem/a(n,n)
            x(n,k)=b(n,k)
         end do
      end do
      return
      end
C
C     subroutine axes
C 
C     STORE THREE ORTHOGONAL VECTORS EACH 1 ANGSTROM LONG
C     ITYPE .GT.0 FOR CARTESIAN,.LE.0 FOR TRICLINIC
C     IABS(ITYPE)=1 W(1)=U,W(2)=(UXV),W(3)=UX(UXV)
C     IABS(ITYPE)=2 W(1)=U,W(2)=(UXV)XU,W(3)=(UXV)
C     ITYPE=0 W(1)=A,W(2)=(AXB)XA,W(3)=(AXB), ABC=CELL VECTORS
C 
      subroutine axes (u,v,x,itype)
      real u(3),v(3),x(3,3)
      integer itype
C
      real w(3,3)
      integer i,ic,it,j
C 
      it=itype
      if (it.eq.0) then
         u(1)=1.
         u(2)=0.
         u(3)=0.
         v(1)=0.
         v(2)=1.
         v(3)=0.
      end if
      do j=1,3
         w(j,1)=u(j)
      end do
      if (iabs(it).eq.1) then
         call norm (u,v,w(1,2),it)
         call norm (u,w(1,2),w(1,3),it)
      else
         call norm (u,v,w(1,3),it)
         call norm (w(1,3),u,w(1,2),it)
      end if
      do i=1,3
         if (it.le.0) then
            ic=-1
         else
            ic=1
         end if
         call vnit (w(1,i),x(1,i),ic)
      end do
      return
      end
C
C subroutine cart
C 
C Generates a cartesian file on unit 12.  The file includes not only
C transformed atomic coordinates and bonding pairs, but also the
C transformed space group origin, lattice vectors, and symmetry
C operations.
C 
      subroutine cart (iopt)
C 
      integer iopt
  
      real atoms(3,4000)
      integer atomid(4000),latm
      common /atoml/ atomid,atoms,latm
  
      real a(9),aa(3,3),bb(3,3)
      common /cell/ a,aa,bb
  
      character*8 chem(4000)
      character*10 sdttit(8)
      character*12 spgtit
      common /chars/ chem,sdttit,spgtit
  
      integer in,lin,lout
      common /lun/ in,lin,lout
  
      real fs(3,3,192),ts(3,192)
      integer nsym
      common /symm/ fs,ts,nsym

      integer i100,i1000,i10k,i100k,i55501
      common /icon/ i100,i1000,i10k,i100k,i55501
C 
      real centroid(3),cntshift(3),radii(93),symcent(3),tatom1(3)
      real tatoms(3,4000),tmat(3,3),tmat2(3,3),torigin(3),tr1,tvec(3)
      real tvec2(3),v1(3),v2(3),work(3,3),work2(3,3),xatoms(3,4000)
      real xmat(3,3),xmatinv(3,3),xorigin(3),xtvec(3)
      integer i,iattype(4000),ibonds(2,8000),it,j,k,lcif,nbonds
      logical here,ish
      character*40 fname
      character*8 atsym(4000)
      character*2 symbols(93),tsym
      character*1 replace
C
      data symbols/
     1 'H ','He','Li','Be','B ','C ','N ','O ','F ','Ne','Na','Mg','Al',
     2 'Si','P ','S ','Cl','Ar','K ','Ca','Sc','Ti','V ','Cr','Mn','Fe',
     3 'Co','Ni','Cu','Zn','Ga','Ge','As','Se','Br','Kr','Rb','Sr','Y ',
     4 'Zr','Nb','Mo','Tc','Ru','Rh','Pd','Ag','Cd','In','Sn','Sb','Te',
     5 'I ','Xe','Cs','Ba','La','Ce','Pr','Nd','Pm','Sm','Eu','Gd','Tb',
     6 'Dy','Ho','Er','Tm','Yb','Lu','Hf','Ta','W ','Re','Os','Ir','Pt',
     7 'Au','Hg','Tl','Pb','Bi','Po','At','Rn','Fr','Ra','Ac','Th','Pa',
     8 'U ','Np'/
      data radii/
     1   .5,  .5, 1.2, 1.5, 1.1, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.2, 1.2,
     2  1.2, 1.2, 1.3, 1.5, 1.5, 1.8, 1.5, 1.5, 1.5, 1.5, 1.5, 1.5, 1.5,
     3  1.5, 1.5, 1.5, 1.5, 1.7, 1.7, 1.7, 1.7, 1.7, 1.7, 1.7, 1.7, 1.7,
     4  1.7, 1.7, 1.7, 1.7, 1.7, 1.7, 1.7, 1.7, 1.7, 1.7, 1.7, 1.7, 1.7,
     5  1.7, 1.7, 1.7, 2.3, 1.7, 1.7, 1.7, 1.7, 1.7, 1.7, 1.7, 1.7, 1.7,
     6  1.7, 1.7, 1.7, 1.7, 1.7, 1.7, 1.7, 1.7, 1.7, 1.7, 1.7, 1.7, 1.7,
     7  1.7, 1.7, 1.7, 1.7, 1.7, 1.7, 1.7, 1.7, 1.7, 1.7, 1.7, 1.7, 1.7,
     8  1.7, 1.7/
C 
      if (latm.le.0) then
         write (lout,'(/,'' ERROR: Cannot generate Cartesian file -- '',
     1          ''no atoms in list.'',/)')
         return
      end if
      if (lin.eq.5) then
         lcif=6
      else
C 
C Open a file
C 
         lcif=12
         inquire (unit=lcif,opened=here)
         if (here) close (lcif)
         do while (.true.)
            write (lout,'('' Input the name for the cartesian file: '',$
     1             )')
            read (in,'(a)') fname
            inquire (file=fname,exist=here)
            if (here) then
               replace='N'
               write (lout,'('' Overwrite existing file? [N] '',$)')
               read (in,'(a1)') replace
            else
               replace='Y'
            end if
            if (replace.eq.'Y'.or.replace.eq.'y') go to 5
         end do
    5    open (lcif,file=fname,status='unknown')
      end if
C 
C Calculate the transformation matrix (to Cartesian)
C 
      v1(1)=a(1)
      v1(2)=0.
      v1(3)=0.
      v2(1)=0.
      v2(2)=0.
      v2(3)=-a(3)
      call axes (v1,v2,work,-1)
      call mm (aa,work,work2)
      do i=1,3
         do j=1,3
            xmat(i,j)=work2(j,i)
         end do
      end do
C 
C Invert the matrix (assumed invertible; if singular or near-singular
C this will cause overflow and/or divide by zero.  If such happens in
C practice it will indicate either a programming (elsewhere) or a data
C input error.)
C 
      xmatinv(1,1)=xmat(2,2)*xmat(3,3)-xmat(3,2)*xmat(2,3)
      xmatinv(2,1)=xmat(3,1)*xmat(2,3)-xmat(2,1)*xmat(3,3)
      xmatinv(3,1)=xmat(2,1)*xmat(3,2)-xmat(3,1)*xmat(2,2)
      tr1=xmat(1,1)*xmatinv(1,1)+xmat(1,2)*xmatinv(2,1)+
     1 xmat(1,3)*xmatinv(3,1)
      xmatinv(1,2)=(xmat(3,2)*xmat(1,3)-xmat(1,2)*xmat(3,3))/tr1
      xmatinv(2,2)=(xmat(1,1)*xmat(3,3)-xmat(3,1)*xmat(1,3))/tr1
      xmatinv(3,2)=(xmat(3,1)*xmat(1,2)-xmat(1,1)*xmat(3,2))/tr1
      xmatinv(1,3)=(xmat(1,2)*xmat(2,3)-xmat(2,2)*xmat(1,3))/tr1
      xmatinv(2,3)=(xmat(2,1)*xmat(1,3)-xmat(1,1)*xmat(2,3))/tr1
      xmatinv(3,3)=(xmat(1,1)*xmat(2,2)-xmat(2,1)*xmat(1,2))/tr1
      do i=1,3
         xmatinv(i,1)=xmatinv(i,1)/tr1
      end do
C 
C Find the centroid of the atoms
C 
      do i=1,3
         centroid(i)=0.
         do j=1,latm
            centroid(i)=centroid(i)+atoms(i,j)
         end do
         centroid(i)=centroid(i)/latm
C 
C   If the centroid is not inside or on the border of the unit cell,
C   then calculate the lattice translation needed to put it there, and
C   calculate the new centroid coordinates for the shifted model.
C 
         cntshift(i)=anint(centroid(i)-0.5)
         centroid(i)=centroid(i)-cntshift(i)
      end do
C 
C The iopt parameter controls whether or not the origin is shifted to
C atom 1.
C 
      if (iopt.eq.0) then
C   iopt.eq.0: original behavior
C 
C   A virtual origin shift is applied to the atoms (an integral number
C   of unit cell lengths in each principal direction) so as to put the
C   centroid of the model inside the 0,0,0 unit cell.  The coordinates
C   of atom 1 in this system are recorded in tatom1, but the atoms are
C   not actually shifted now because in a moment they will all be
C   shifted again to put atom 1 at the origin.  In anticipation of this
C   latter shift, the coordinates of the unit cell origin with respect
C   to an origin at atom 1 are stored in torigin.
C 
         do j=1,3
            tatom1(j)=atoms(j,1)-cntshift(j)
            torigin(j)=-tatom1(j)
         end do
C 
C   Now shift the origin to atom 1.  Usually this origin setting is
C   nonstandard, often drastically so.
C 
         do i=1,latm
            do j=1,3
               tatoms(j,i)=atoms(j,i)-atoms(j,1)
            end do
         end do
      else
C 
C  iopt.ne.0
C 
C  Computationally, this is more straightforward.  We shift the atoms
C  only so as to put the centroid in the unit cell.  Since this is a
C  crystallographic symmetry operation, we don't need to change the
C  origin or take any other compensatory action.
C 
         do i=1,latm
            do j=1,3
               tatoms(j,i)=atoms(j,i)-cntshift(j)
            end do
         end do
         do j=1,3
            tatom1(j)=0.
            torigin(j)=0.
         end do
      end if
C 
C Calculate the transformed (Cartesian) coordinates and determine
C atom types.
C 
      do i=1,latm
         it=atomid(i)/i100k
         call mv (xmat,tatoms(1,i),xatoms(1,i))
         tsym(1:1)=chem(it)(1:1)
         if (chem(it)(2:2).ge.'A'.and.chem(it)(2:2).le.'Z') then
            tsym(2:2)=char(ichar(chem(it)(2:2))+32)
            atsym(i)=tsym//chem(it)(3:8)
         else if (chem(it)(2:2).ge.'a'.and.chem(it)(2:2).le.'z') then
            tsym(2:2)=chem(it)(2:2)
            atsym(i)=tsym//chem(it)(3:8)
         else
            tsym(2:2)=' '
            atsym(i)=tsym(1:1)//chem(it)(2:8)
         end if
         do j=1,93
            if (tsym.eq.symbols(j)) go to 10
         end do
   10    iattype(i)=j
      end do
C 
C Find Bonds
C Atoms are considered bonded if they are at least as close together
C as the sum of their Van der Waals' radii.  Atoms whose element type
C could not be determined are not checked for bonds, and hydrogen is
C never the first atom of a bond.  (Thus no H-H bonds are generated.)
C This algorithm does not depend on H atoms being in any particular
C position in the list with respect to the other atoms.
C 
      nbonds=0
      do i=1,latm-1
         if (iattype(i).gt.93) go to 20
         if (iattype(i).eq.1) then
             ish=.true.
         else
             ish=.false.
         endif
         do j=i+1,latm
            if ((ish.and.iattype(j).eq.1).or.iattype(j).gt.93) go to 15
            tr1=0.
            do k=1,3
               tr1=tr1+(xatoms(k,j)-xatoms(k,i))**2
            end do
            if (tr1.le.(radii(iattype(i))+radii(iattype(j)))**2) then
               nbonds=nbonds+1
               if (ish) then
                   ibonds(1,nbonds)=j
                   ibonds(2,nbonds)=i
               else
                   ibonds(1,nbonds)=i
                   ibonds(2,nbonds)=j
               endif
            end if
   15       continue
         end do
   20    continue
      end do
C 
C Start writing the file.  First the header:
C 
      write (lcif,25) latm,nbonds,sdttit(1)(1:8)
C 
C Now the atoms:
C 
      do i=1,latm
         write (lcif,30) atsym(i),(xatoms(j,i),j=1,3),iattype(i)
      end do
      write (lcif,35)
C 
C Now the bonds:
C 
      do i=1,nbonds
         write (lcif,40) ibonds(1,i),ibonds(2,i)
      end do
      write (lcif,45)
C 
C Write the unit cell.
C We calculate the transformed origin, but we already have the
C transformed lattice vectors in the form of the columns of the
C transformation matrix.
C 
      call mv (xmat,torigin,xorigin)
      write (lcif,50)
      write (lcif,60) xorigin,xmat
C 
C Now the symmetry matrices.  The identity operation is assumed to
C be number 1, and is not included.
C 
      write (lcif,55) nsym-1
      do i=2,nsym
C 
C   Extract the rotation matrix (the transposition transforms it from
C   a post-operand to a pre-operand form).
C 
         do j=1,3
            do k=1,3
               tmat(j,k)=fs(k,j,i)
            end do
         end do
C 
C   Now calculate the origin shift component of the tranformed
C   translation vector.
C 
         call mminusi (tmat,tmat2)
         call mv (tmat2,tatom1(1),tvec2)
C 
C   Now construct the rotation matrix in the transformed coordinates.
C 
         call mm (xmat,tmat,tmat2)
         call mm (tmat2,xmatinv,tmat)
C 
C   Determine a modified translation vector so that the combined
C   operation leaves the centroid in the unit cell.
C 
         do j=1,3
C 
C     Apply the operation to the centroid.
C 
            symcent(j)=ts(j,i)
            do k=1,3
               symcent(j)=symcent(j)+fs(k,j,i)*centroid(k)
            end do
C 
C     Apply a lattice vector, if necessary, to shift the image into
C     the unit cell, and add the origin translation component.
C 
            tvec(j)=ts(j,i)-anint(symcent(j)-0.5)+tvec2(j)
         end do
C 
C   Calculate the transformed translation vector.
C 
         call mv (xmat,tvec,xtvec)
C 
C   Now write it all out.
C 
         write (lcif,60) ((tmat(j,k),k=1,3),j=1,3),xtvec
      end do
      write (lcif,65)
C 
C   All done with the .crt file, but don't just close it -- disassociate
C   it from unit lcif by replacing it with a scratch file.
C 
      close (lcif)
      open (lcif,status='scratch')
      return
C
   25 format ('CARTESIAN ',2i10,6x,a8)
   30 format (a8,2x,3f10.5,i4)
   35 format ('ENDATOMS')
   40 format (2i4)
   45 format ('ENDBONDS')
   50 format ('CELL')
   55 format ('SYMMETRY',i10)
   60 format (3f12.5)
   65 format ('ENDSYMM')
      end
C
C     subroutine eigen
C 
C     Determines the eigenvalues and eigenvectors of a 3x3 matrix
C 
      subroutine eigen (w,valu,vect)
      real valu(3),vect(3,3),w(3,3)
C 
      integer ng
      common /fault/ ng
C 
      real a(3,3),b(3,3),b0,b1,b2,c0,c1,errnd,phif,sigma,smax,t1,tem
      real u(3),v(3),vmv,vnew,vold,x,y,z
      integer i,i1,ii,iii,imax,j,l,lll
C 
C     STATEMENT FUNCTION (This is the characteristic equation)
C 
      phif(z)=((b2-z)*z+b1)*z+b0
C 
C     START OF PROGRAM
C 
      errnd=5.e-7
      sigma=0.
      do j=1,3
         do i=1,3
            a(i,j)=w(i,j)
            sigma=sigma+w(i,j)*w(i,j)
         end do
      end do
C 
C     CHECK FOR NULL MATRIX
C 
      if (sigma.le.0.) go to 5
      sigma=sqrt(sigma)
C 
C     Calculate coefficients of CHARACTERISTIC EQUATION
C 
      b2=a(1,1)+a(2,2)+a(3,3)
      b1=-a(1,1)*a(2,2)-a(1,1)*a(3,3)-a(2,2)*a(3,3)+a(1,3)*a(3,1)+
     1 a(2,3)*a(3,2)+a(1,2)*a(2,1)
      b0=a(1,1)*a(2,2)*a(3,3)+a(1,2)*a(2,3)*a(3,1)+a(1,3)*a(3,2)*a(2,1)-
     1 a(1,3)*a(3,1)*a(2,2)-a(1,1)*a(2,3)*a(3,2)-a(1,2)*a(2,1)*a(3,3)
C 
C     The roots of the characteristic equation are the eigenvalues
C 
C     FIRST ROOT BY BISECTION
C 
      x=0.
      y=sigma
      tem=phif(sigma)
      vnew=0.0
      if (b0.eq.0.) then
         go to 10
      else if (b0.lt.0.) then
         if (tem.le.0.) y=-y
      else
         y=0.
         x=sigma
         if (tem.gt.0.) x=-x
      end if
C 
C     NOW PHIF(X).LT.0.AND.PHIF(Y).GT.0.
C 
      vnew=(x+y)*.5
      do i=1,40
         tem=phif(vnew)
         if (tem.eq.0.) then
            go to 10
         else if (tem.lt.0.) then
            x=vnew
         else
            y=vnew
         end if
         vold=vnew
         vnew=(x+y)*.5
         tem=abs(vold-vnew)
         if (tem.le.errnd) go to 10
         if (vold.ne.0.) then
            if (abs(tem/vold).le.errnd) go to 10
         end if
      end do
C 
C     DID NOT CONVERGE, SET ERROR INDICATOR
C 
    5 ng=6
      go to 50
C 
C     STORE FIRST ROOT
C 
   10 u(3)=vnew
C 
C     DEFLATE
C 
      c1=b2-vnew
      c0=b1+c1*vnew
C 
C     SOLVE QUADRATIC
C 
      tem=c1*c1+4.*c0
      if (tem.lt.0.) then
C 
C     IGNORE IMAGINARY COMPONENT OF COMPLEX ROOT
C 
         tem=0.
      else if (tem.gt.0.) then
         tem=sqrt(tem)
      end if
      u(1)=.5*(c1-tem)
      u(2)=.5*(c1+tem)
C 
C     SORT ROOTS
C 
      do j=1,2
         if (u(j).gt.u(3)) then
            tem=u(j)
            u(j)=u(3)
            u(3)=tem
         end if
      end do
      lll=-2
      do iii=1,2
C 
C     CHECK FOR MULTIPLE ROOTS
C 
         tem=errnd*100.
         ng=0
         l=1
         do 20 i=1,2
            if (u(i+1)-u(i).le.tem) go to 15
            if (u(i).eq.0.) go to 20
            if (abs((u(i+1)-u(i))/u(i)).gt.tem) go to 20
   15       l=l-1
            ng=ng-2*i
   20    continue
         if (lll.ge.l) go to 50
         lll=l
C 
C     EIGENVECTOR ROUTINE
C 
         do ii=1,3
            t1=u(ii)
            if (l.lt.0) go to 25
            if (l.gt.0) go to 30
C 
C     TWO VECTORS Indeterminant FOR DOUBLE ROOT.  These are set null.
C 
            if (ng+5.eq.ii) go to 30
C 
C     ALL VECTORS Indeterminant FOR TRIPLE ROOT.  These are set null.
C 
   25       do j=1,3
               vect(j,ii)=0.0
            end do
            go to 45
   30       do j=1,3
               a(j,j)=w(j,j)-t1
            end do
            smax=0.0
            do 35 i=1,3
               i1=mod(i,3)+1
               b(i,1)=a(i,2)*a(i1,3)-a(i,3)*a(i1,2)
               b(i,2)=a(i,3)*a(i1,1)-a(i,1)*a(i1,3)
               b(i,3)=a(i,1)*a(i1,2)-a(i,2)*a(i1,1)
               tem=b(i,1)**2+b(i,2)**2+b(i,3)**2
               if (tem.gt.smax) then
                  smax=tem
                  imax=i
               end if
   35       continue
            if (smax.gt.0.) go to 40
            ng=7
            go to 45
   40       smax=sqrt(smax)
            do j=1,3
               v(j)=b(imax,j)/smax
            end do
C 
C     REFINE the EIGENVECTOR
C 
            call axeqb (a,v,v,1)
C 
C     Normalize the eigenvector
C 
            tem=amax1(abs(v(1)),abs(v(2)),abs(v(3)))
            do j=1,3
               v(j)=v(j)/tem
            end do
            call vnit (v,vect(1,ii),1)
C 
C     REFINE EIGENVALUE
C 
            t1=vmv(vect(1,ii),w,vect(1,ii))
            u(ii)=t1
   45       valu(ii)=t1
         end do
      end do
   50 return
      end
C
C     subroutine f401
C 
C     handles 401 (add) and 411 (remove) runs of atoms.  It is
C     seperated from subroutine f400 so as to avoid a calling loop
C     involving f400 and searc.
C 
      subroutine f401
  
      integer ng
      common /fault/ ng
  
      double precision ain(30)
      integer nj,nj2
      common /instr/ ain,nj,nj2

      integer i100,i1000,i10k,i100k,i55501
      common /icon/ i100,i1000,i10k,i100k,i55501
C 
      real v1(3)
      integer i,i1,i2,i3,ii,imax,jj,l1,l2,l3,l4,l5,m1,m2,m3,m4,m5,n1,n2
      integer n3,n4,n5
C 
C     EXPAND ATOM NUMBERS TO ADC'S IF NECESSARY.
C     IN 401 AND 411, RUN TERMINATES WITH 0
C 
      do i=1,25
         i1=idnint(ain(i))
         if (i1.eq.0) exit
         if (iabs(i1).le.4000) ain(i)=dble(isign((iabs(i1)*i100k+i55501)
     1    ,i1))
      end do
      imax=i-1
C 
C     FIND RUNS IN AIN ARRAY
C 
      ii=0
      do while (.true.)
C 
C     Find the next positive ADC
C 
         do while (.true.)
            ii=ii+1
            if (ii.gt.imax) return
            i1=idnint(ain(ii))
            if (i1.gt.0) exit
         end do
C 
C     SET INITIAL RUN VALUES
C 
         m1=i1/i100k
         m2=mod(i1,100)
C 
C     Symmetry card 00 is assumed to be 01
C 
         if (m2.eq.0) m2=1
         m5=mod(i1/100,1000)
C 
C     Translations are assumed 555 if not otherwise specified
C 
         if (m5.le.0) m5=555
C 
C     Extract digits
C 
         m3=m5/100
         m4=mod(m5/10,10)
         m5=mod(m5,10)
C 
C     Look for a run (next ADC negative)
C 
         jj=ii+1
         i2=-idnint(ain(jj))
         if (jj.gt.imax.or.i2.le.0) then
C 
C     SET TERMINAL VALUES FOR DEGENERATE RUN
C 
            n1=m1
            n2=m2
            n3=m3
            n4=m4
            n5=m5
         else
            ii=jj
C 
C     SET TERMINAL RUN VALUES
C 
            n1=i2/i100k
            n2=mod(i2,100)
C 
C     Symmetry card 00 is assumed to be 01
C 
            if (n2.eq.0) n2=1
            n5=mod(i2/100,1000)
C 
C     Translations are assumed 555 if not otherwise specified
C 
            if (n5.le.0) n5=555
C 
C     Extract digits
C 
            n3=n5/100
            n4=mod(n5/10,10)
            n5=mod(n5,10)
         end if
C 
C     LOOP THROUGH ALL RUNS
C 
         do l5=m5,n5
            do l4=m4,n4
               do l3=m3,n3
                  do l2=m2,n2
                     do l1=m1,n1
                        i3=l1*i100k+l3*i10k+l4*i1000+l5*i100+l2
                        call atom (i3,v1)
                        if (ng.eq.0) then
C 
C     SUBROUTINE STOR WILL KNOW WHETHER TO ADD OR DELETE THIS ATOM
C 
                           call stor (i3,v1)
                        else
C 
C     ORTEP ERROR MESSAGES ARE NOT USED IN THIS VERSION
C 
                           ng=0
                        end if
                     end do
                  end do
               end do
            end do
         end do
      end do
      end
C
C     subroutine mm
C 
C     MULTIPLY TWO MATRICES
C            Z(3,3)=X(3,3)*Y(3,3)
C 
      subroutine mm (x,y,z)
      real x(3,3),y(3,3),z(3,3)
C
      integer i,j,k
C 
      do i=1,3
         do k=1,3
            z(i,k)=0.0
            do j=1,3
               z(i,k)=z(i,k)+x(i,j)*y(j,k)
            end do
         end do
      end do
      return
      end
C
C     subroutine mminusi
C
C     calculate the matrix difference a-I=b
C     where a and b are 3x3 matrices and I is the 3x3 identity matrix
C 
      subroutine mminusi (a,b)
      real a(3,3),b(3,3)
C
      integer i,j
C 
      do i=1,3
         do j=1,3
            b(i,j)=a(i,j)
         end do
         b(i,i)=b(i,i)-1.
      end do
      return
      end
C
C     subroutine mv
C 
C     MATRIX * VECTOR
C            Z(3)=X(3,3)*Y(3)
C 
      subroutine mv (x,y,z)
      real x(3,3),y(3),z(3)
C
      integer i,j
C 
      do i=1,3
         z(i)=0.0
         do j=1,3
            z(i)=z(i)+x(i,j)*y(j)
         end do
      end do
      return
      end
C
C     subroutine norm
C 
C     VECTOR (CROSS) PRODUCT  Z=X*Y
C 
C     ITYPE .GT.0 FOR CARTESIAN,.LE.0 FOR TRICLINIC
C 
      subroutine norm (x,y,z,itype)
      real x(3),y(3),z(3)
      integer itype
C 
      real a(9),aa(3,3),bb(3,3)
      common /cell/ a,aa,bb
C  
      real t1,z1(3)
      integer i,i1,i2
C 
      do i=1,3
         i1=mod(i,3)+1
         i2=mod(i+1,3)+1
         t1=x(i1)*y(i2)-x(i2)*y(i1)
         if (itype.gt.0) then
            z(i)=t1
         else
            z1(i)=t1
         end if
      end do
      if (itype.le.0) call mv (bb,z1,z)
      return
      end
C
C     subroutine prelimsdt
C 
C     DATA INPUT ROUTINE for standard data tapes
C 
      subroutine prelimsdt (numhyds)
      integer numhyds
C 
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
  
      integer in,lin,lout
      common /lun/ in,lin,lout
  
      real xpop(4000),xtemp(4000)
      integer ibull1(4000),ibull2(4000)
      common /sdtat/ ibull1,ibull2,xpop,xtemp
  
      real fs(3,3,192),ts(3,192)
      integer nsym
      common /symm/ fs,ts,nsym
C 
      double precision tst(3,192)
      real wavl
      integer i,isend,j,k
      character*8 tit(10)
C 
      numhyds=0
C 
C     CELL DIMENSIONS
C     A(I) = A,B,C,COSA,COSB,COSG,ALPHA,BETA,GAMMA
C     WAVL  = WAVELENGTH
C 
      read (lin,20) (a(i),i=1,6),wavl
      write (23,20) (a(i),i=1,6),wavl
C 
C     READ AND WRITE DOWN THRU SYMMETRY TRANSFORMATIONS
C     BX ARRAY HAS ERRORS ON THE CELL + THE VOLUME
C 
      read (lin,25) bx
      write (23,25) bx
C 
C     SPGTIT = SPACE GROUP SYMBOL
C 
      read (lin,30) tit
      write (23,30) tit
      spgtit=tit(1)//tit(2)
C 
C     1ST PART OF THE ISDTIS ARRAY HAS THE
C     NO. OF SCAT.FAC., NO. OF SYMM.CARDS, ETC.
C 
      read (lin,35) (isdtis(i),i=1,16)
      write (23,35) (isdtis(i),i=1,16)
      nsym=isdtis(2)
C 
C     COPY THE SCATTERING FACTORS
C 
      do i=1,2*isdtis(1)
         read (lin,30) tit
         write (23,30) tit
      end do
C 
C     THE 2ND PART (AND 3RD IF NEEDED) OF ISDTIS HAS THE
C     AT.NO. AND NO.OF ATOMS FOR EACH KIND OF ATOM
C 
      do i=17,min(isdtis(1)*2+16,48),16
         read (lin,35) (isdtis(j),j=i,i+15)
         write (23,35) (isdtis(j),j=i,i+15)
      end do
C 
C     COPY LARSON'S SYMMETRY NUMBERS
C 
      read (lin,30) tit
      write (23,30) tit
      read (lin,30) tit
      write (23,30) tit
C 
C     TS AND FS ARRAYS HAVE THE SYMMETRY CARDS (ORTEP FORMAT)
C     Shortep uses a real variable for the translations, but the input
C     format contains too much precision for a default real.  So that
C     the output is not mangled in the least significant figures,
C     a double-precision temporary is used.
C 
      read (lin,40) ((tst(j,i),(fs(k,j,i),k=1,3),j=1,3),i=1,nsym)
      write (23,45) ((tst(j,i),(nint(fs(k,j,i)),k=1,3),j=1,3),i=1,nsym)
      do i=1,nsym
         do j=1,3
            ts(j,i)=sngl(tst(j,i))
         end do
      end do
C 
C     POSITIONAL AND THERMAL PARAMETERS FOR THE INPUT ATOMS
C     P ARRAY HAS THE XYZ'S, PA ARRAY HAS THE BETAS
C 
      do i=1,4000
         read (lin,50) chem(i),isend,ibull1(i),ibull2(i),xpop(i),
     1    (p(j,i),j=1,3)
         if (isend.eq.1) go to 5
         if (chem(i)(1:1).eq.'H'.and.(ichar(chem(i)(2:2)).lt.65 .or.
     1    ichar(chem(i)(2:2)).gt.90)) numhyds=numhyds+1
         read (lin,55) ((pa(j,k,i),j=1,3),k=1,2)
         pa(1,3,i)=0.
         xtemp(i)=pa(1,1,i)
      end do
    5 natom=i-1
      if (lin.ne.5) write (lout,60) natom
C 
C     Convert thermal parameters
C 
      call tpcnvt
C 
      read (lin,'(F10.4)',end=10) scalef
      read (lin,'(E10.4)',end=15) extinf
      return
   10 scalef=1.
   15 extinf=0.
      return
C
   20 format (7f10.6)
   25 format (6f10.5,f10.2)
   30 format (10a8)
   35 format (16i4)
   40 format (3(f15.10,3f3.0))
   45 format (3(f15.10,3i3))
   50 format (bz,a8,i2,i6,i8,6x,4f10.6,2x,i8)
   55 format (6f10.6)
   60 format (i6,' ATOMS WERE ON TAPE 20')
      end
C
C     subroutine proccell
C 
      subroutine proccell (b,q)
      real b(9),q(3,3)
C 
      real a(9),aa(3,3),bb(3,3)
      common /cell/ a,aa,bb
  
      real scalef,extinf,bx(7)
      integer isdtis(48)
      common /ends/ scalef,extinf,isdtis,bx
  
      real aarev(3,3),refv(3,3)
      common /orient/ aarev,refv
C 
      real aid(3,3),arccos,t1
      integer i,j,k
C 
C     STORE IDEMFACTOR (IDENTITY) MATRIX
C 
      do j=1,3
         do k=1,3
            aid(j,k)=0.
         end do
         aid(j,j)=1.
      end do
      do j=1,3
         if (abs(a(j+3)).ge.1.) then
C 
C     CELL ANGLES IN DEGREES
C 
            a(j+6)=a(j+3)
            a(j+3)=cos(sngl(a(j+6)*1.745329252d-2))
            bx(j+3)=sngl(bx(j+3)*sqrt(1.-a(j+3)**2)*1.745329252d-2)
         else
C 
C     COSINES OF CELL ANGLES
C 
            a(j+6)=arccos(a(j+3))
         end if
C 
C     STORE METRIC TENSOR
C 
         aa(j,j)=a(j)**2
      end do
      aa(1,2)=a(1)*a(2)*a(6)
      aa(1,3)=a(1)*a(3)*a(5)
      aa(2,3)=a(2)*a(3)*a(4)
      aa(2,1)=aa(1,2)
      aa(3,1)=aa(1,3)
      aa(3,2)=aa(2,3)
C 
C     INVERT METRIC TENSOR
C 
      call axeqb (aa,bb,aid,3)
C 
C     CALCULATE RECIPROCAL CELL PARAMETERS
C     B(J) = A*,B*,C*,COSA*,COSB*,COSG*,ALPHA*,BETA*,GAMMA*
C 
      do j=1,3
         b(j)=sqrt(bb(j,j))
      end do
      b(6)=bb(1,2)/(b(1)*b(2))
      b(5)=bb(1,3)/(b(1)*b(3))
      b(4)=bb(2,3)/(b(2)*b(3))
      do j=1,3
         b(j+6)=arccos(b(j+3))
      end do
C 
C     WAS INPUT FOR the RECIPROCAL CELL?
C     IF so, SWAP AA AND BB AND SWAP A AND B
C 
      if (a(1).lt.1.) then
         do j=1,3
            do k=1,3
               t1=aa(j,k)
               aa(j,k)=bb(j,k)
               bb(j,k)=t1
               i=(j-1)*3+k
               t1=a(i)
               a(i)=b(i)
               b(i)=t1
            end do
         end do
      end if
C 
C     STORE STANDARD VECTORS
C 
      call axes (aid(1,1),aid(1,2),refv,0)
      call mm (aa,refv,aarev)
      do i=1,3
         do j=1,3
            q(j,i)=refv(i,j)
         end do
      end do
      return
      end
C
C     subroutine pushdown
C 
C     Atom removal by table pushdown
C 
      subroutine pushdown (k)
	integer k
C  
      real atoms(3,4000)
      integer atomid(4000),latm
      common /atoml/ atomid,atoms,latm
C  
      integer i,j
C  
      latm=latm-1
      do i=k,latm
         atomid(i)=atomid(i+1)
         do j=1,3
            atoms(j,i)=atoms(j,i+1)
         end do
      end do
      return
      end
C
C     subroutine stor
C 
C     STORE IN OR REMOVE FROM ATOMS ARRAY
C 
C     ENTER WITH ADC IN TD, COORDINATES IN V1, add/delete flag in
C     ain(27), 408 FLAG IN AIN(28), and delete-by-number flag in ain(24)
C 
      subroutine stor (id,v1)
      real v1(3)
      integer id
C 
      real arad(4000),radmax
      integer ntype(4000),i405,ipoly
      common /adat/ arad,radmax,i405,ipoly,ntype
  
      real atoms(3,4000)
      integer atomid(4000),latm
      common /atoml/ atomid,atoms,latm
  
      double precision ain(30)
      integer nj,nj2
      common /instr/ ain,nj,nj2

      integer i100,i1000,i10k,i100k,i55501
      common /icon/ i100,i1000,i10k,i100k,i55501
C 
      real d
      integer i,i1,i2,j,k,ieq1,ieq2
C 
C     No need to remove anything if the list is empty
C 
      if (latm.le.0) then
         if (ain(27).eq.0d0) then
            go to 10
         else
            return
         end if
C 
C     If we are adding, then check a few things first...
C 
      else if (ain(27).eq.0d0) then
C 
C     Can't add anything if the list is full
C 
         if (latm.ge.4000) then
            return
C 
C     CHECK FOR ATOM DUPLICATION if necessary
C 
         else if (ain(28).ne.0d0) then
            i1=id/i100k
            do i=1,latm
               i2=atomid(i)/i100k
               if (i2.eq.i1) return
            end do
         end if
C 
C     If deleting, are we deleting by atom number?  (414,415,416)
C 
      else if (ain(24).eq.1d0) then
C 
C     Deleting all symmetry equivalents by atom number.
C 
         i1=id/i100k
         i=1
         do while (i.le.latm)
            i2=atomid(i)/i100k
            if (i2.eq.i1) call pushdown (i)
            i=i+1
         end do
         return
      else if (ain(24).eq.-1d0) then
C 
C     Deleting (at most) one atom by atom number.
C 
         do i=1,latm
            if (atomid(i).eq.id) then
               call pushdown (i)
            end if
         end do
         return
      end if
C 
C     CHECK FOR POSITIONAL DUPLICATION
C 
      do k=1,latm
         ieq1=0
         ieq2=0
         do j=1,3
            d=abs(v1(j)-atoms(j,k))
            if (d.gt.0.001) then
               if (i405.eq.0.or.abs(d-anint(d)).gt.0.001) go to 5
               ieq2=ieq2+1
            else
               ieq1=ieq1+1
            end if
         end do
C 
C     If conducting a BUILD, do not add polymericly-related atoms;
C     instead set the polymer flag and return
C 
         if (ieq1.ne.3.and.ieq1+ieq2.eq.3) then
            ipoly=1
            return
         end if
C 
C     Found a match
C 
         if (ain(27).ne.0d0) then
C 
C     delete atom
C 
            call pushdown (k)
         end if
C 
C     Whether or not the atom was removed, we certainly don't want
C     to add it, so return
C 
         return
    5    continue
      end do
   10 if (ain(27).eq.0d0) then
C 
C     STORE ATOM
C 
         latm=latm+1
         atomid(latm)=id
         do j=1,3
            atoms(j,latm)=v1(j)
         end do
      end if
      return
      end
C
C     subroutine tpcnvt
C 
C     CONVERT TEMP FACTOR COEF TO STANDARD TYPE ZERO
C 
      subroutine tpcnvt
C 
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
  
      integer in,lin,lout
      common /lun/ in,lin,lout
  
      real aarev(3,3),refv(3,3)
      common /orient/ aarev,refv
C 
      real am3(3,3),b(9),da(3,3),pat(3,3),q(3,3),rms(3),t1,t2,t3,t6
      real v1(3),vmv,vt(3,3)
      integer i,j,k,l,m,n,ng1
C 
      call proccell (b,q)
C 
      ng1=0
      do 65 i=1,natom
         t1=pa(1,1,i)
         t6=5.066059e-2
         k=1+nint(pa(1,3,i))
         go to (15,5,10,10,15,5,55,60,15,5,15,65),k
C 
C     TYPE 1
C 
    5    do j=1,3
            pa(j,2,i)=pa(j,2,i)*.5
         end do
         go to 15
C 
C     TYPES 2 AND 3 (BASE 2 SYSTEMS)
C 
   10    t6=.3511525
         if (k.eq.4) go to 5
C 
C     TYPES 0 THROUGH 5
C 
   15    if (pa(2,1,i).eq.0..and.pa(3,1,i).eq.0.) go to 55
         do 25 j=1,3
            do 20 l=j,3
               t2=t6
               if (k.ge.5) then
                  if (k.le.6) then
C 
C     TYPES 4 AND 5
C 
                     t2=b(j)*b(l)*t2*.25
                  else
C 
C     TYPES 8 AND 9 (U(I,J) TENSOR SYSTEMS)
C 
                     t2=b(j)*b(l)
                     if (k.ge.11) then
C 
C     TYPE 10, (CARTESIAN TENSOR SYSTEM)
C 
                        t2=1.0
                     end if
                  end if
               end if
               if (j.eq.l) then
                  vt(j,j)=t2*pa(j,1,i)
               else
                  m=j+l
                  vt(j,l)=t2*pa(mod(m,3)+1,2,i)
                  vt(l,j)=vt(j,l)
               end if
   20       continue
   25    continue
C 
C     FIND PRINCIPAL AXES
C 
         if (k.ge.11) then
            call mm (vt,q,am3)
            call mm (refv,am3,vt)
         end if
         call mm (vt,aa,da)
         call eigen (da,rms,pat)
C 
C     ARE EIGENVALUES POSITIVE
C 
         if (rms(1).le.0.) then
            ng=3
            go to 30
         end if
         if (ng.lt.0) go to 40
         if (ng.eq.0) go to 50
C 
C     We always list the atom name (only) for any NPD atoms detected.
C 
   30    if (lin.ne.5) then
            if (ng1.eq.0) then
               write (lout,70)
               ng1=1
            end if
            write (lout,'(1x,a8)') chem(i)
         end if
C 
C     ORTEP ERROR MESSAGES ARE NOT USED IN THIS VERSION
         ng=0
C 
C     3 EQUAL EIGENVALUES, USE REFERENCE VECTORS
C 
   35    t3=sign(sqrt(abs(rms(1)+rms(2)+rms(3))/3.),rms(1))
         do j=1,3
            do k=1,3
               pa(j,k,i)=refv(j,k)
            end do
            ev(j,i)=t3
         end do
         go to 65
   40    if (ng.le.-6) go to 35
C 
C     TWO EQUAL EIGENVALUES
C 
         n=ng+5
         call vnit (pat(1,n),v1,-1)
         do k=1,3
            if (abs(vmv(v1,aa,refv(1,k))).lt..58) go to 45
         end do
   45    call mm (aa,da,vt)
         call axes (v1,refv(1,k),da,-1)
         do k=1,3
            l=mod(n+k-2,3)+1
            do j=1,3
               pa(j,l,i)=da(j,k)
            end do
            ev(l,i)=sign(sqrt(abs(vmv(da(1,k),vt,da(1,k)))),rms(l))
         end do
         go to 65
C 
C     MAKE EIGENVECTORS 1 ANGSTROM LONG
C 
   50    call axes (pat(1,1),pat(1,3),pa(1,1,i),-1)
         ng=0
C 
C     SQRT EIGENVALUE = RMS DISPLACEMENT
C 
         do j=1,3
            ev(j,i)=sign(sqrt(abs(rms(j))),rms(j))
         end do
         go to 65
C 
C     TYPE 6 (ISOTROPIC TEMP FACTOR)
C 
   55    t1=sign(sngl(sqrt(abs(t1)*1.266514796d-2)),t1)
C 
C     TYPE 7 (DUMMY SPHERE)
C 
   60    do j=1,3
            ev(j,i)=t1
         end do
C 
C     REFERENCE VECTORS FOR SPHERE
C 
         do j=1,3
            do k=1,3
               pa(j,k,i)=refv(j,k)
            end do
         end do
   65 continue
      if (in.ne.5) write (lout,'()')
      return
C
   70 format (/,1x,'Warning: The following atoms have non-positive ',
     1 'definate thermal parameters:')
      end
C
C     function vmv
C 
C     TRANSPOSED VECTOR * MATRIX * VECTOR
C 
C     VMV=X1(3)*Q(3,3)*X2(3)    TO  EVALUATE QUADRATIC OR BILINEAR FORM
C 
      function vmv (x1,q,x2)
      real vmv,x1(3),q(3,3),x2(3)
C 
      real t1
      integer j
C 
      t1=0.
      do j=1,3
         t1=t1+x1(j)*(x2(1)*q(j,1)+x2(2)*q(j,2)+x2(3)*q(j,3))
      end do
      vmv=t1
      return 
      end
C
C     subroutine vnit
C 
C     Z = UNIT VECTOR ALONG X
C 
C     ITYPE > 0 IF X IS IN ORTHOGONAL COORDINATE SYSTEM
C 
      subroutine vnit (x,z,itype)
      real x(3),z(3)
      integer itype
C 
      real a(9),aa(3,3),bb(3,3)
      common /cell/ a,aa,bb
C 
      integer ng
      common /fault/ ng
C 
      real t1,y(3)
C 
      y(1)=x(1)
      y(2)=x(2)
      y(3)=x(3)
      if (itype.gt.0) then
         t1=sqrt(y(1)*y(1)+y(2)*y(2)+y(3)*y(3))
      else
         t1=sqrt(y(1)*(y(1)*aa(1,1)+y(2)*(aa(1,2)+aa(2,1))+y(3)*(aa(1,3)
     1    +aa(3,1)))+y(2)*(y(2)*aa(2,2)+y(3)*(aa(2,3)+aa(3,2)))+y(3)*
     2    y(3)*aa(3,3))
      end if
      if (t1.le.0.) then
         ng=5
      else
         z(1)=y(1)/t1
         z(2)=y(2)/t1
         z(3)=y(3)/t1
      end if
      return
      end
C
C     function vv
C 
C     TRANSPOSED VECTOR * VECTOR
C 
C             VV=X(3)*Y(3)
C 
      function vv (x,y)
      real vv,x(3),y(3)
C 
      vv=x(1)*y(1)+x(2)*y(2)+x(3)*y(3)
      return
      end
