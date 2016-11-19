function equalmasscollision
var relVX = vx1-vx2;
var relVY = vy1-vy2;
var theta = angleBetween(relVX, relVY, x2-x1, y2-y1);
var magV2 = Math.cos(theta)*magVector(relVX,relVY);
