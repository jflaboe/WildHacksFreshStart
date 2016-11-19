var io = require('socket.io');

exports.initialize = (function(server){
	io = io.listen(server);

})