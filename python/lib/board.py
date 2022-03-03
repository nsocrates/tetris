from math import floor, ceil
from copy import deepcopy
from utils import matrix
from .shape import Shape
from .point import Point
from .grid import Grid

class Board():
    """Representation of a Tetris board.

    Attributes:
        grid: type:Grid
        width: Board width - type:int
        height: Board height - type:int
        widths: List representing the largest width for each row - type:list
        heights: List representing the largest height for each column - type:list
        largest_row: Index of the highest row with a filled block - type:int
        is_committed: Current state of the board - type:bool
        grid_backup: Grid backup - type:Grid
        widths_backup: Widths backup - type:list
        heights_backup: Heights backup - type:list
    """

    PLACE_OK = "PLACE_OK"
    PLACE_OUT_BOUNDS = "PLACE_OUT_BOUNDS"
    PLACE_BAD = "PLACE_BAD"
    PLACE_ROW_FILLED = "PLACE_ROW_FILLED"

    @staticmethod
    def dispatch(message, *payload):
        return (message, payload)

    def __init__(self, width, height):
        self.grid = Grid(width, height, ".")
        self.width = width
        self.height = height
        self.widths = [0] * height
        self.heights = [0] * width
        self.largest_row = 0
        self.is_committed = True
        self.grid_backup = None
        self.widths_backup = None
        self.heights_backup = None

    def __str__(self):
        return str(self.grid)

    def replace(self, matrix):
        """Replaces the current board with a new board."""
        self.grid.replace(matrix)
        self.widths = [0] * self.height
        self.heights = [0] * self.width
        self.count_blocks()

    def insert(self, shape, x, y, color=None):
        """Attempts to place a shape at the specified coordinates."""
        color = color or shape.color.upper()

        if not self.is_committed:
            raise Exception("Previous round has not been committed")

        self.is_committed = False
        self.backup()

        message = self.PLACE_OK
        heights = [0] * self.width

        for coords in shape.body:
            px, py = coords + (x, y)

            if not 0 <= px < self.width or not 0 <= py < self.height:
                message = self.PLACE_OUT_BOUNDS
                break

            if self.grid.get(px, py) != ".":
                message = self.PLACE_BAD
                break

            self.grid.set(px, py, color)
            self.widths[py] += 1

            adjusted_y = 0 if coords.y == 1 else 1
            if heights[px] < adjusted_y + 1:
                heights[px] = adjusted_y + 1

            if self.widths[py] == self.width:
                message = self.PLACE_ROW_FILLED

        for i in range(len(heights)):
            self.heights[i] += heights[i]

        return self.dispatch(message, shape, x, y)

    def count_blocks(self):
        """Sets new values for largest_row, heights, and widths."""
        self.largest_row = 0
        for i in range(self.height):
            row = self.grid.get_row(i)
            should_increment_row = True

            for j in range(self.width):
                if row[j] != ".":
                    self.heights[j] += 1
                else:
                    should_increment_row = False

            if should_increment_row:
                self.widths[i] += 1
                if self.largest_row < i:
                    self.largest_row = i

    def drop_height(self, shape, x):
        """Calculates the y value where the origin of a piece will come to rest if dropped."""
        max_y = 0

        for i in range(len(shape.skirt)):
            offset_x = shape.body[i].x + x
            y = self.heights[offset_x] - shape.height - shape.skirt[i]
            if y > max_y:
                max_y = y

        return self.height - max_y - shape.height

    def clear_rows(self):
        """Clears filled rows and shifts things down."""
        line_count = 0
        for i in (range(self.largest_row + 1)):
            if any([b == "." for b in self.grid.get_row(i)]):
                continue

            for c in self.heights:
                c -= 1

            self.widths[i] = 0
            self.grid.set_row(i, ["."] * self.width)
            line_count += 1

        return line_count

    def backup(self):
        """Creates a backup of the state of the current board."""
        self.grid_backup = self.grid.clone()
        self.widths_backup = deepcopy(self.widths)
        self.heights_backup = deepcopy(self.heights)

    def commit(self):
        """Commits the current state of the board."""
        self.is_committed = True

    # def place_shape(self):
    #     self.grid.save_tmp()

    def undo(self):
        """Replaces the current state of the board with the previous state."""
        self.grid = self.grid_backup
        self.widths = self.widths_backup
        self.heights = self.heights_backup
        self.commit()

    def clear(self):
        """Resets the board."""
        self.grid.clear()
