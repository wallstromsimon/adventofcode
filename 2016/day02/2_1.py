#!/usr/bin/python

def up(d):
    if d in (1, 2, 3):
        return d
    else:
        return d - 3


def down(d):
    if d in (7, 8, 9):
        return d
    else:
        return d + 3


def left(d):
    if d in (1, 4, 7):
        return d
    else:
        return d - 1


def right(d):
    if d in (3, 6, 9):
        return d
    else:
        return d + 1


def find_digit(l, start):
    tmp = start
    for c in l:
        if c == 'U':
            tmp = up(tmp)
        if c == 'D':
            tmp = down(tmp)
        if c == 'L':
            tmp = left(tmp)
        if c == 'R':
            tmp = right(tmp)
    return tmp


f = open("input_2_1")
digit = 5
code = ""

for line in f:
    digit = find_digit(line, digit)
    code += str(digit)

print(code)
