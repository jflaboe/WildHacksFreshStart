var socket = io.connect('/');
var gamestarted = false;
var name;
var id;
var gamedata;
$(document).keydown(function(e){
	if (gamestarted){
		if (e.which == 1)
			socket.emit('set_up', {up:true, num: id});
		else if (e.which ==2)
			socket.emit('set_right', {right:true, num: id});
		else if (e.which == 3)
			socket.emit('set_down',{down:true, num: id});
		else if (e.which == 4)
			socket.emit('set_down',{down:true, num: id});
}})
$(document).keyup(function(e){
	if (gamestarted){
		if (e.which == 1)
			socket.emit('set_up', {up: false, num: id});
		else if (e.which ==2)
			socket.emit('set_right', {right:false, num: id});
		else if (e.which == 3)
			socket.emit('set_down',{down:false, num: id});
		else if (e.which == 4)
			socket.emit('set_down',{down:false, num: id});
}})
$('#name').click(function(e){
	socket.emit('set_name',{})
})
socket.on('name_set',function(data){
	id = data.id;

})

socket.on('start_game',function(){
	gamestarted=true;
})

socket.on('end_game', function(){
	gamestarted=false;
})

var updateLoop = setInterval(drawGame(),50);


var drawGame = function(data){
	data.forEach(draw());
};
