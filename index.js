var app = require('express')();
var http = require('http').Server(app);
var io = require('socket.io')(http);

var engine = require('gameEngine');

app.get('/', function(req, res){
  res.sendfile(__dirname + '/gamePage.html');
});

http.listen(3000, function(){
  console.log('listening on *:3000');
});

io.on('connection', function(socket){
  console.log('a user connected');
  

  io.on('set_up',function(data){
  	engine.players[data.num].up = data.up;
  })
  io.on('set_right', function(data){
  	engine.players[data.num].right = data.right;
  })
  io.on('set_left', function(data){
  	engine.players[data.num].left = data.left;
  }
  io.on('set_down',function(data){
  	engine.player[data.num].down = data.down;
  })

  io.on('set_name', function(data){

  	var ind = addBall(data.name);

  	io.emit('name_set', {id: ind});
  })



  setInterval(function(){
  	engine.update();
  	io.broadcast.emit('game_update',engine.players)
  },30)


});