#!/usr/bin/python

f = open('input_1_1', 'r')
moves = f.read().split(",")
x = 0
y = 0
direction = 0

for move in moves:
    move = move.strip()
    turn = move[0]
    steps = int(move[1:])
    if turn == 'R':
        direction = (direction + 1) % 4
    else:
        direction = (direction - 1) % 4
    if direction == 0:
        y += steps
    elif direction == 1:
        x += steps
    elif direction == 2:
        y -= steps
    elif direction == 3:
        x -= steps

print("Steps needed: ", abs(x)+abs(y))
