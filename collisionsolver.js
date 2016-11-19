function equalmasscollision(x1,y1,vx1,vy1,x2,y2,vx2,vy2){

var relVX = vx1-vx2;
var relVY = vy1-vy2;
var theta = angleBetween(relVX, relVY, x2-x1, y2-y1);
var magV2 = Math.cos(theta)*magVector(relVX,relVY);

var solution = [solution0,solution1,solution2,solution3];
solution2= normalize(x2-x1, y2-y1)[0]*magV2;
solution3 = normalize(x2-x1, y2-y1)[1]*magV2;
solution0= relVX - solution[2] + vx2;
solution1=relVY - solution[3] + vy2;
solution[2]= normalize(x2-x1, y2-y1)[0]*magV2 +vx2; 
solution[3]= normalize(x2-x1, y2-y1)[0]*magV2 +vy2;


return solution;
}

function magvector(x,y){
return Math.sqrt(Math.pow(x,2) + Math.pow(y,2));
}

function normalize(x, y){
var norm = {x/magVector(x,y),y/magVector(x,y)};
return norm;
}

function angleBetween(x1,y1,x2,y2){
return Math.acos((x1*x2+y1*y2)/(magVector(x1,y1)*magVector(x2,y2)));
}
