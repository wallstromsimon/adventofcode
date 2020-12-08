package advent2020

import (
	"fmt"
	assert "github.com/stretchr/testify/assert"
	"testing"
)

func TestDay3Part1Example(t *testing.T) {
	input := [][]string{{".",".","#","#",".",".",".",".",".",".","."},{"#",".",".",".","#",".",".",".","#",".","."},{".","#",".",".",".",".","#",".",".","#","."},{".",".","#",".","#",".",".",".","#",".","#"},{".","#",".",".",".","#","#",".",".","#","."},{".",".","#",".","#","#",".",".",".",".","."},{".","#",".","#",".","#",".",".",".",".","#"},{".","#",".",".",".",".",".",".",".",".","#"},{"#",".","#","#",".",".",".","#",".",".","."},{"#",".",".",".","#","#",".",".",".",".","#"},{".","#",".",".","#",".",".",".","#",".","#"}}
	actual := part1(input)
	expected := 7

	assert.Equal(t, expected, actual, "Fail.")
}


func TestDay3Part1Real(t *testing.T) {

}

func TestDay3Part2Example(t *testing.T) {

}

func TestDay3Part2Real(t *testing.T) {

}


func part1(input [][]string) int {
	//x := 0
	y := 0

	maxY := len(input)
	maxX := len(input[0])

	fmt.Printf("len x:%d,y:%d \n", maxX, maxY)

	for y < maxY {

	}

	return 0
}
