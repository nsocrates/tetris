#!/usr/local/bin/python3
import sys
from lib.game import Game

class Cmd():
    def __init__(self):
        self.game = Game()
        self.keys = {
            "q": self.quit_game,
            "P": self.print_board,
            "c": self.clear_playfield,
            "g": self.take_stream,
            "?s": self.print_score,
            "?n": self.print_line_count,
            "s": self.step,
            "t": self.print_active_tetromino,
            ")": self.rotate_right,
            "(": self.rotate_left,
            ";": self.print_newline,
            "p": self.print_frozen_board,
            "<": self.move_left,
            ">": self.move_right,
            "v": self.move_down,
            "V": self.hard_drop,
            ">>>>": self.move_hard_right,
            "<<<<": self.move_hard_left,
        }

    def quit_game(self):
        sys.exit()

    def print_board(self):
        print(self.game.board)

    def print_frozen_board(self):
        print(self.game.frozen_board)

    def print_score(self):
        print(self.game.score)

    def print_line_count(self):
        print(self.game.line_count)

    def print_active_tetromino(self):
        print(self.game.shape)

    def clear_playfield(self):
        self.game.clear_board()

    def take_stream(self):
        self.game.board_from_stream(input)

    def step(self):
        self.game.step()

    def spawn(self, t):
        return lambda: self.game.spawn(t)

    def rotate_right(self):
        self.game.rotate(1)

    def rotate_left(self):
        self.game.rotate(-1)

    def print_newline(self):
        print()

    def move_left(self):
        self.game.move(-1, 0)

    def move_right(self):
        self.game.move(1, 0)

    def move_down(self):
        self.game.move(0, 1)

    def move_hard_right(self):
        self.game.move_hard(1, 0)

    def move_hard_left(self):
        self.game.move_hard(-1, 0)

    def hard_drop(self):
        self.game.hard_drop()

    def switch(self, key):
        return self.keys.get(key, self.spawn(key))()

    def run(self):
        while True:
            keys = input().split(" ")

            for k in keys:
                if k in { "?s", "?n", ">"*4, "<"*4 }:
                    self.switch(k)
                else:
                    for a in k:
                        self.switch(a)

def main():
    program = Cmd()
    program.run()

if __name__ == '__main__':
    main()
