var io = requre('socket.io');

exports.initialize = (function(server){
	io = io.listen(server);

})