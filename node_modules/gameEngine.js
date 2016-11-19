var players = [];
var numPlayers = 0;
var gameObjects = [];

//returns the positions of all the balls
var positions = function(){};

//sets the booleans for whether or not the balls are accelerating
var set_up = function(id, value){};
var set_down = function(id, value){};
var set_left = function(id, value){};
var set_right = function(id, value){};

//checks for collisions between each pair of balls and adjusts speeds accordingly
var checkCollisions = function(){};




//adjusts acceleration, speed, and position of all the balls
var update = function(){};


//adds a ball to the game and returns its index in the list of balls in the game
// different indices of balls should have different colors.
var addBall = function(name){
	newBall = require('ball.js');
	players.push(newBall);
	gameObjects.push(newBall);
	players[numPlayers].id = numPlayers;
	players[numPlayers].name = name;
	//add color change here
	numPlayers = numPlayers + 1;

	return numPlayers-1;
};