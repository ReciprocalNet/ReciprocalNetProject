// JavaScript Document

//To open a new window smaller than the screen size.   
var newWindow;
function openNewWindow(url)
{
	newWindow=window.open(url,'mediumWindow','width=800,height=800,left=100,top=100,resizable=yes,scrollbars=yes,toolbar=yes,status=yes,location=yes');
	if (window.focus) {newWindow.focus()}
}

//To open a new window smaller than the screen size.   
var newSymmetryWindow;
function openNewSymmetryWindow(url)
{
	newSymmetryWindow=window.open(url,'mediumWindow','width=820,height=620,left=100,top=100,resizable=yes,scrollbars=yes,toolbar=yes,status=yes,location=yes');
	if (window.focus) {newSymmetryWindow.focus()}
}

//To open a new window for announcing important event.  the window is smaller than the other one(newWindow)
var newSmallWindow;
function openNewSmallWindow(url)
{
	newSmallWindow=window.open(url,'smallWindow','width=800,height=700,left=100,top=100,resizable=no,scrollbars=yes,toolbar=yes,status=no,location=no');
	if (window.focus) {newSmallWindow.focus()}
}
var newItemWindow;
function openNewItemWindow(url)
{
	newItemWindow=window.open(url,'smallWindow','width=640,height=600,left=60,top=40,resizable=yes,scrollbars=yes,toolbar=yes,status=yes,location=yes');
	if (window.focus) {newItemWindow.focus()}
}
var newQuizWindow;
function openNewQuizWindow(url)
{
	newQuizWindow=window.open(url,'largeWindow','width=1000,height=700,left=100,top=200,resizable=yes,scrollbars=yes,toolbar=yes,status=yes,location=yes');
	if (window.focus) {newQuizWindow.focus()}
}


function closeWindow(){
	window.opener = window;
 window.close();
}

//To restore images. Written by Macromedia Dreamweaver and Fireworks
function MM_swapImgRestore() { //v3.0
  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}
//To preload images. Written by Macromedia Dreamweaver and Fireworks
function MM_preloadImages() { //v3.0
  var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
    if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
}
//To support swap function. Written by Macromedia Dreamweaver and Fireworks
function MM_findObj(n, d) { //v4.01
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
  if(!x && d.getElementById) x=d.getElementById(n); return x;
}
//To swap images. Written by Macromedia Dreamweaver and Fireworks
function MM_swapImage() { //v3.0
  var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
   if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
}

/*JavaSript functions below work for index.html pages under 'common molecules.
When user put the cursor over certain areas, they show the hidden description about them. 
These are written by PVII.
*/

function P7_autoLayers() { //v1.4 by PVII
 	var g,b,k,f,args=P7_autoLayers.arguments;a=parseInt(args[0]);
 	if(isNaN(a))a=0;
 	if(!document.p7setc){
		p7c=new Array();
	 	document.p7setc=true;
	 	for(var u=0;u<10;u++){
			 p7c[u]=new Array();
		}
	}
	for(k=0;k<p7c[a].length;k++){
		if((g=MM_findObj(p7c[a][k]))!=null){
 			b=(document.layers)?g:g.style;b.visibility="hidden";
		}
	}
	for(k=1;k<args.length;k++){
 		if((g=MM_findObj(args[k]))!=null){
			b=(document.layers)?g:g.style;
			b.visibility="visible";
			f=false;
 			for(var j=0;j<p7c[a].length;j++){
				if(args[k]==p7c[a][j]) {
					f=true;
				}
			}
 			if(!f){
				p7c[a][p7c[a].length++]=args[k];
			}
		}
	}
}

function P7_Snap() { //v2.65 by PVII
 	var x,y,ox,bx,oy,p,tx,a,b,k,d,da,e,el,tw,q0,xx,yy,w1,pa='px',args=P7_Snap.arguments;a=parseInt(a);
 	if(document.layers||window.opera){
		pa='';
	}
	for(k=0;k<(args.length);k+=4){
 		if((g=MM_findObj(args[k]))!=null){
			if((el=MM_findObj(args[k+1]))!=null){
 				a=parseInt(args[k+2]);
				b=parseInt(args[k+3]);
				x=0;
				y=0;
				ox=0;
				oy=0;
				p="";
				tx=1;
 				da="document.all['"+args[k]+"']";
				if(document.getElementById){
					d="document.getElementsByName('"+args[k]+"')[0]";
					if(!eval(d)){
						d="document.getElementById('"+args[k]+"')";
						if(!eval(d)){
							d=da;
						}
					}
 				}
				else if(document.all){
					d=da;
				}
				if(document.all||document.getElementById){
					while(tx==1){
						p+=".offsetParent";
						if(eval(d+p)){
							x+=parseInt(eval(d+p+".offsetLeft"));
							y+=parseInt(eval(d+p+".offsetTop"));
 						}
						else{
							tx=0;
						}
					}
					ox=parseInt(g.offsetLeft);
					oy=parseInt(g.offsetTop);
					tw=x+ox+y+oy;
 					if(tw==0||(navigator.appVersion.indexOf("MSIE 4")>-1&&navigator.appVersion.indexOf("Mac")>-1)){
						ox=0;
						oy=0;
						if(g.style.left){
							x=parseInt(g.style.left);
							y=parseInt(g.style.top);
						}
						else{
  						w1=parseInt(el.style.width);
							bx=(a<0)?-5-w1:-10;
							a=(Math.abs(a)<1000)?0:a;
							b=(Math.abs(b)<1000)?0:b;
  						x=document.body.scrollLeft+event.clientX+bx;
							y=document.body.scrollTop+event.clientY;
						}
					}
 				}
				else if(document.layers){
					x=g.x;
					y=g.y;
					q0=document.layers,dd="";
					for(var s=0;s<q0.length;s++){
  					dd='document.'+q0[s].name;
						if(eval(dd+'.document.'+args[k])){
							x+=eval(dd+'.left');
							y+=eval(dd+'.top');
  						break;
						}
					}
				}
				e=(document.layers)?el:el.style;
				xx=parseInt(x+ox+a),yy=parseInt(y+oy+b);
 				if(navigator.appVersion.indexOf("MSIE 5")>-1 && navigator.appVersion.indexOf("Mac")>-1){
					xx+=parseInt(document.body.leftMargin);
					yy+=parseInt(document.body.topMargin);
				}
 				e.left=xx+pa;
				e.top=yy+pa;
			}
		}
	}
}

function P7_hideEl(evt) { //v1.5 by PVII-www.projectseven.com
	var b,r,m=false;
	if(document.layers){
		b=evt.target;
		if(b.p7aHide){
 			b.visibility="hidden";
		}
		else{
			routeEvent(evt);
		}
 	}
	else if(document.all&&!window.opera){
		b=event.srcElement;
		while(b){
 			if(b.p7aHide){
				break;
			}
			b=b.parentElement;
		}
		if(!b.contains(event.toElement)){
 			b.style.visibility="hidden";
		}
	}
	else if(document.getElementById){
 		b=evt.currentTarget;r=evt.relatedTarget;
		while(r){
			if(b==r){
				m=true;
 				break;
			}
			r=r.parentNode;
		}
		if(!m){
			b.style.visibility="hidden";
		}
	}
}

function P7_autoHide() { //v1.5 by PVII-www.projectseven.com
 	var i,g;
	for(i=0;i<arguments.length;i++){
 		if((g=MM_findObj(arguments[i]))!=null){
			g.p7aHide=true;
			if(document.layers){
 				g.captureEvents(Event.MOUSEOUT);
			}
			g.onmouseout=P7_hideEl;
		}
	}
}

//To show hidden images. Written by Macromedia Dreamweaver and Fireworks
function MM_showHideLayers() { //v6.0
  var i,p,v,obj,args=MM_showHideLayers.arguments;
  for (i=0; i<(args.length-2); i+=3) if ((obj=MM_findObj(args[i]))!=null) { v=args[i+2];
    if (obj.style) { obj=obj.style; v=(v=='show')?'visible':(v=='hide')?'hidden':v; }
    obj.visibility=v; }
}