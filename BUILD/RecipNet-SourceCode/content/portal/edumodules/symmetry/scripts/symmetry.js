// JavaScript Document
if(!document.getElementById){document.write('<link rel="stylesheet" type="text/css" href="symmetryStyle.css">');document.close()}

function MM_reloadPage(init) {  //reloads the window if Nav4 resized
  if (init==true) with (navigator) {if ((appName=="Netscape")&&(parseInt(appVersion)==4)) {
    document.MM_pgW=innerWidth; document.MM_pgH=innerHeight; onresize=MM_reloadPage; }}
  else if (innerWidth!=document.MM_pgW || innerHeight!=document.MM_pgH) location.reload();
}
MM_reloadPage(true);

function MM_findObj(n, d) { //v4.01
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
  if(!x && d.getElementById) x=d.getElementById(n); return x;
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

function P7_autoLayers() { //v1.4 by PVII
 var g,b,k,f,args=P7_autoLayers.arguments;a=parseInt(args[0]);if(isNaN(a))a=0;
 if(!document.p7setc){p7c=new Array();document.p7setc=true;for(var u=0;u<10;u++){
 p7c[u]=new Array();}}for(k=0;k<p7c[a].length;k++){if((g=MM_findObj(p7c[a][k]))!=null){
 b=(document.layers)?g:g.style;b.visibility="hidden";}}for(k=1;k<args.length;k++){
 if((g=MM_findObj(args[k]))!=null){b=(document.layers)?g:g.style;b.visibility="visible";f=false;
 for(var j=0;j<p7c[a].length;j++){if(args[k]==p7c[a][j]) {f=true;}}
 if(!f){p7c[a][p7c[a].length++]=args[k];}}}
}

function P7_hideDiv(evt) { //v1.3 by PVII
 var b,relT,mT=false; 
 if(document.layers){b=evt.target;if(b.p7aHide){
  b.visibility="hidden";}else{routeEvent(evt);}
 }else if(document.all&&!window.opera){b=event.srcElement;
  while(b!=null){if(b.tagName=="DIV" && b.p7ahD){mT=true;break;}b=b.parentElement;}
  if(!b.contains(event.toElement)){b.style.visibility="hidden";}
 }else if(document.getElementById){b=evt.currentTarget;relT=evt.relatedTarget;
  while(relT!=null){if(b==relT){mT=true;break;}
  relT=relT.parentNode;}if(!mT){b.style.visibility="hidden";}}
}

function P7_autoHide() { //v1.3 by PVII
 var i,g,args=P7_autoHide.arguments;
 for(i=0;i<args.length;i++){if((g=MM_findObj(args[i]))!=null){
  g.p7aHide=true;if(document.layers){
  g.captureEvents(Event.MOUSEOUT);}g.onmouseout=P7_hideDiv;g.p7ahD=true;}}
}

function P7_Snap() { //v2.65 by PVII
 var x,y,ox,bx,oy,p,tx,a,b,k,d,da,e,el,tw,q0,xx,yy,w1,pa='px',args=P7_Snap.arguments;a=parseInt(a);
 if(document.layers||window.opera){pa='';}for(k=0;k<(args.length);k+=4){
 if((g=MM_findObj(args[k]))!=null){if((el=MM_findObj(args[k+1]))!=null){
 a=parseInt(args[k+2]);b=parseInt(args[k+3]);x=0;y=0;ox=0;oy=0;p="";tx=1;
 da="document.all['"+args[k]+"']";if(document.getElementById){
 d="document.getElementsByName('"+args[k]+"')[0]";if(!eval(d)){
 d="document.getElementById('"+args[k]+"')";if(!eval(d)){d=da;}}
 }else if(document.all){d=da;}if(document.all||document.getElementById){while(tx==1){
 p+=".offsetParent";if(eval(d+p)){x+=parseInt(eval(d+p+".offsetLeft"));y+=parseInt(eval(d+p+".offsetTop"));
 }else{tx=0;}}ox=parseInt(g.offsetLeft);oy=parseInt(g.offsetTop);tw=x+ox+y+oy;
 if(tw==0||(navigator.appVersion.indexOf("MSIE 4")>-1&&navigator.appVersion.indexOf("Mac")>-1)){
  ox=0;oy=0;if(g.style.left){x=parseInt(g.style.left);y=parseInt(g.style.top);}else{
  w1=parseInt(el.style.width);bx=(a<0)?-5-w1:-10;a=(Math.abs(a)<1000)?0:a;b=(Math.abs(b)<1000)?0:b;
  x=document.body.scrollLeft+event.clientX+bx;y=document.body.scrollTop+event.clientY;}}
 }else if(document.layers){x=g.x;y=g.y;q0=document.layers,dd="";for(var s=0;s<q0.length;s++){
  dd='document.'+q0[s].name;if(eval(dd+'.document.'+args[k])){x+=eval(dd+'.left');y+=eval(dd+'.top');
  break;}}}e=(document.layers)?el:el.style;xx=parseInt(x+ox+a),yy=parseInt(y+oy+b);
 if(navigator.appVersion.indexOf("MSIE 5")>-1 && navigator.appVersion.indexOf("Mac")>-1){
  xx+=parseInt(document.body.leftMargin);yy+=parseInt(document.body.topMargin);}
 e.left=xx+pa;e.top=yy+pa;}}}
}

function P7_fixSafariBB(){ //v1.1 by PVII
if(navigator.userAgent.indexOf("Safari")>-1){
var jj=onload;if(jj){if(onunload==null){
document.body.setAttribute('onunload',jj);}}}
}

//To open a new window for announcing important event.  the window is smaller than the other one(newWindow)
var newSmallWindow;
function openNewSmallWindow(url)
{
	newSmallWindow=window.open(url,'smallWindow','width=500,height=520,left=100,top=100,resizable=yes,scrollbars=yes,toolbar=yes,status=no,location=no');
	if (window.focus) {newSmallWindow.focus()}
}