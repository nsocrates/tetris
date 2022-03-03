from utils import flatten
from .board import Board
from .point import Point
from .shape_proxy import ShapeProxy

class Game:
    BOARD_WIDTH = 10
    BOARD_HEIGHT = 22
    ROTATE_CCW = "ROTATE_CCW"
    ROTATE_CW = "ROTATE_CW"
    MOVE_RIGHT = "MOVE_RIGHT"
    MOVE_LEFT = "MOVE_LEFT"
    MOVE_DOWN = "MOVE_DOWN"
    HARD_DROP = "HARD_DROP"
    SPAWN = "SPAWN"
    TICK = "TICK"

    @staticmethod
    def dispatch(message, *payload):
        return (message, payload)

    def __init__(self):
        self.board = Board(self.BOARD_WIDTH, self.BOARD_HEIGHT)
        self.coords = Point()
        self.shape = ShapeProxy()
        self.score = 0
        self.line_count = 0
        self.player_moved = False
        self.strategies = {
            Board.PLACE_OK: self.place_ok_strategy,
            Board.PLACE_OUT_BOUNDS: self.place_out_bounds_strategy,
            Board.PLACE_BAD: self.place_bad_strategy,
            Board.PLACE_ROW_FILLED: self.place_row_filled_strategy,
            self.ROTATE_CCW: self.rotate,
            self.ROTATE_CW: self.rotate,
            self.MOVE_RIGHT: self.move,
            self.MOVE_LEFT: self.move,
            self.MOVE_DOWN: self.move,
            self.HARD_DROP: self.hard_drop,
            self.SPAWN: self.spawn,
        }

    @property
    def frozen_board(self):
        stringified_board = str(self.board)
        if self.shape.current is None:
            return stringified_board

        return stringified_board.replace(self.shape.color.upper(), ".")

    def receive_message(self, message, payload):
        strategy = self.strategies.get(message)
        strategy(*payload)

    def clear_board(self):
        self.board.clear()

    def board_from_stream(self, func):
        items = [func().split(" ") for _ in range(self.BOARD_HEIGHT)]
        self.board.replace(items)

    def spawn(self, name):
        shape = self.shape.get(name)
        px = int((self.BOARD_WIDTH - shape.width) / 2)
        py = 0
        self.insert(shape, px, py, None, True)

    def rotate(self, direction):
        shape = self.shape.next if direction == 1 else self.shape.prev
        x, y = self.coords
        px = int(x + (self.shape.width - shape.width) / 2)
        py = y
        self.insert(shape, px, py)

    def move(self, x, y):
        px, py = self.coords.add(x, y)
        return self.insert(self.shape.current, px, py)

    def move_hard(self, x, y):
        message = self.move(x, y)
        while message == Board.PLACE_OK:
            message = self.move(x, y)

    def hard_drop(self):
        px = self.coords.x
        py = self.board.drop_height(self.shape.current, px)
        self.board.undo()
        self.insert(self.shape.current, px, py, self.shape.color)

    def insert(self, shape, px, py, color=None, force=False):
        if self.shape.current != None and not force:
            self.board.undo()

        action = self.board.insert(shape, px, py, color)
        self.receive_message(*action)
        return action[0]

    def step(self):
        line_count = self.board.clear_rows()
        self.line_count += line_count
        self.score += line_count * 100

    def tick(self):
        pass

    def place_row_filled_strategy(self, *args):
        self.step()
        self.board.commit()

    def place_ok_strategy(self, shape, x, y, *args):
        self.coords.set(x, y)
        self.shape.set(shape)
        self.board.commit()

    def place_out_bounds_strategy(self, *args):
        self.board.undo()
        px, py = self.coords
        self.insert(self.shape.current, px, py)

    def place_bad_strategy(self, *args):
        self.board.undo()
        px, py = self.coords
        self.insert(self.shape.current, px, py)
