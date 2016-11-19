var socket = io.connect('/');

$(document).keydown(function(e){
	if (e.which == 1)
		socket.emit('set_up', {up: true});
	else if (e.which ==2)
		socket.emit('set_right', {right:true});
	else if (e.which == 3)
		socket.emit('set_down',{down:true});
	else if (e.which == 4)
})