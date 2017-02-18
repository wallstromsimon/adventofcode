#!/usr/bin/python

def walk(pos, step):
    if direction == 0:
        return pos[0], pos[1] + step
    elif direction == 1:
        return pos[0] + step, pos[1]
    elif direction == 2:
        return pos[0], pos[1] - step
    elif direction == 3:
        return pos[0] - step, pos[1]


f = open('input_1_1', 'r')
moves = f.read().split(",")
position = (0,0)
direction = 0
been = {position}

for move in moves:
    move = move.strip()
    turn = move[0]
    steps = int(move[1:])

    if turn == 'R':
        direction = (direction + 1) % 4
    else:
        direction = (direction - 1) % 4

    for s in range(steps):
        position = walk(position,1)
        if position in been:
            print("Steps needed: ", abs(position[0]) + abs(position[1]))
            exit()
        been.add(position)
