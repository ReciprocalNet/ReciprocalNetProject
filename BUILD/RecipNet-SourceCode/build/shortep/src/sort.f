C
C     subroutine sort
C 
C     sorts a list of integer data according to a parallel list of
C     integer sort keys.  This is a key-based bi-directional bubble
C     sort.
C 
      subroutine sort (num,j)
      integer j,num(j)
C 
C     Both the data and the keys are loaded into input array num.
C     The data are stored in the odd-numbered positions, the keys
C     in the even-numbered ones.
C     The keys may be atom types (atomic numbers) or ADCs as used
C     in shortep, but in principle could be any integers.  The data
C     are indexes into that atomid array as used in shortep, but
C     again, in principle they could be any integers.
C 
      integer i,it,k,l,maxx,minx
C 
      i=2
      l=j
      do while (i.lt.l)
         minx=i
         maxx=l
         do k=i+2,l,2
            if (num(k).lt.num(minx)) minx=k
            if (num(k-2).gt.num(maxx)) maxx=k-2
         end do
         if (minx.gt.i) then
            it=num(minx)
            num(minx)=num(i)
            num(i)=it
            it=num(minx-1)
            num(minx-1)=num(i-1)
            num(i-1)=it
            if (maxx.eq.i) then
               maxx=minx
            end if
         end if
         if (maxx.lt.l) then
            it=num(maxx)
            num(maxx)=num(l)
            num(l)=it
            it=num(maxx-1)
            num(maxx-1)=num(l-1)
            num(l-1)=it
         end if
         i=i+2
         l=l-2
      end do
      return
      end
